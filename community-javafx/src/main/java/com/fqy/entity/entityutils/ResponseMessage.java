package com.fqy.entity.entityutils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMessage<T>{
    private  Integer code;     //代码
    private  String message;   //日志
    private  T data;           //数据

}
