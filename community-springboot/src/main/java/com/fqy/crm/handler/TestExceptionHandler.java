package com.fqy.crm.handler;

import com.fqy.crm.entity.entityutils.ResponseMessage;
import com.fqy.crm.exception.ValidaException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class TestExceptionHandler {

//    @Resource
//    private ResponseMessage responseMessage;

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public String validFail(MethodArgumentNotValidException exception) {
//        log.warn(exception.toString());
//        FieldError fieldError = exception.getBindingResult().getFieldError();
//        if (fieldError != null)
//            return fieldError.getDefaultMessage();
//        return "GG";
//    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseMessage validFail(MethodArgumentNotValidException exception) {
//        log.warn(exception.toString());
//        FieldError fieldError = exception.getBindingResult().getFieldError();
//        ResponseMessage responseMessage = ResponseMessage.getResponseMessage();
//        if (fieldError != null){
//            return responseMessage.error().message(fieldError.getDefaultMessage());
//        }
//        return responseMessage.error().message("unknown error").data(null);
//    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseMessage validFail(MethodArgumentNotValidException exception) {
//        log.warn(exception.toString());
//        FieldError fieldError = exception.getBindingResult().getFieldError();
//        if (fieldError != null){
//            responseMessage.setCode(500);
//            responseMessage.setMessage(fieldError.getDefaultMessage());
//            return responseMessage;
//        }
//        responseMessage.setCode(500);
//        responseMessage.setMessage("unknown error");
//        return responseMessage;
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseMessage<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseMessage<>(400, "请求参数不合法", errors);
    }

    @ExceptionHandler(IOException.class)
    public ResponseMessage<?> exceptionHandler(Exception exception) {
        log.warn(exception.toString());
        return new ResponseMessage<>(500, "服务器异常", exception.getMessage());
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseMessage<?> sqlExceptionHandler(SQLIntegrityConstraintViolationException exception) {
        log.warn(exception.toString());
        return new ResponseMessage<>(400, "唯一约束触发", "这个名称已经有了");
    }

    @ExceptionHandler(ValidaException.class)
    public ResponseMessage<Map<String,String>> validaExceptionHandler(ValidaException e) {
        return new ResponseMessage<>(400, "不合法", e.getExceptionMessages());
    }
}
