package com.fqy.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    private Integer id;                 //用户id
    private String username;            //用户名
    private String password;            //密码
    private String name;                //真实姓名
    private Integer type;               //用户类型
    private String phone;               //用户电话
    private String registerTime;        //注册时间
    private String lastUpdateTime;      //最后一次修改的时间
    private Integer visible;            //用户存在的状态

}
