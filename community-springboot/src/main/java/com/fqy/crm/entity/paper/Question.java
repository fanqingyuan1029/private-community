package com.fqy.crm.entity.paper;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Question {

    private Integer id;                 //题目id
    private String name;                //题目名字
    private String text;                //题目内容，json格式
    private String registerTime;        //注册时间
    private String lastUpdateTime;      //最后一次修改的时间
}
