package com.fqy.crm.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fqy.crm.entity.Log;
import com.fqy.crm.service.AsyncService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Objects;

@Component
@Aspect
@Slf4j
public class ControllerLogAspect {

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private AsyncService asyncService;

    @Pointcut("execution(public * com.fqy.crm.controller.*.*(..))")
    public void controllerLog() {

    }

    private Log getLog(){
        // 获取Request对象
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(requestAttributes).getRequest();

        String startTimeStr = LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Log log = new Log();
        log.setUrl(request.getRequestURI());
        log.setMethod(request.getMethod());
        log.setStartTime(startTimeStr);
        return log;
    }


    private void logPrePrint(Log log){
        ControllerLogAspect.log.info("\n{} {}\n请求时间:{}",
                log.getUrl(),
                log.getMethod(),
                log.getStartTime());
    }

    private void logAfterPrint(Log log){
        ControllerLogAspect.log.info("\n{} {}\n请求时间:{}\n执行完毕,耗时:{}毫秒\n返回值:{}",
                log.getUrl(),
                log.getMethod(),
                log.getStartTime(),
                log.getExecutionTime(),
                log.getResponseBody());
    }


    @Around("controllerLog()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // 记录方法开始执行的时间
        long startTime = System.currentTimeMillis(); // 开始时间
        // 读取必要参数,封装到Log对象中
        Log log = getLog();
        // 控制台输出开始执行日志
        logPrePrint(log);
        // 环绕方法真正执行,得到返回值
        Object result = proceedingJoinPoint.proceed();
        // 返回值序列化
        String resultJson = objectMapper.writeValueAsString(result);
        // 计算整个方法的执行时间
        Long executionTime = System.currentTimeMillis() - startTime;
        // 将新的值传入Log对象
        log.setExecutionTime(executionTime);
        log.setResponseBody(resultJson);
        // 控制台打印方法结果日志
        logAfterPrint(log);
        // 异步保存日志到MySQL数据库
//        asyncService.saveControllerLog(log);
        return result;
    }
}
