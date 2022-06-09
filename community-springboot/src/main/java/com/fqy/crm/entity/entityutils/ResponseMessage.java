package com.fqy.crm.entity.entityutils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMessage<T>{
    private  Integer code;     //代码
    private  String message;   //日志
    private  T data;           //数据

    public ResponseMessage(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
///**
// * 单例模式
// *
// */
//public class ResponseMessage {
//
//    private volatile static ResponseMessage responseMessage;
//
//    private  Integer code;     //代码
//    private  String message;   //日志
//    private  Object data;           //数据
//
//    public Integer getCode() {
//        return code;
//    }
//
//    private void setCode(Integer code) {
//        this.code = code;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    private void setMessage(String message) {
//        this.message = message;
//    }
//
//    public Object getData() {
//        return data;
//    }
//
//    private void setData(Object data) {
//        this.data = data;
//    }
//
//    private ResponseMessage() {
//    }
//
//    public static ResponseMessage getResponseMessage(){
//        if(responseMessage==null){
//            synchronized (ResponseMessage.class){
//                if(responseMessage==null){
//                    responseMessage=new ResponseMessage();
//                }
//            }
//        }
//        return responseMessage;
//    }
//
//    public ResponseMessage ok(){
//        getResponseMessage().setCode(200);
//        return responseMessage;
//    }
//
//    public ResponseMessage error(){
//        responseMessage.setCode(500);
//        return responseMessage;
//    }
//
//    public ResponseMessage message(String message){
//        responseMessage.setMessage(message);
//        return responseMessage;
//    }
//
//    public ResponseMessage data(Object data){
//        responseMessage.setData(data);
//        return responseMessage;
//    }
//}
