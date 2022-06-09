package com.fqy.entity.paper;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PublishPaper {
    private Integer id;                  //问卷id
    private String name;                 //问卷名字
    private String registerTime;         //注册时间
    private String endTime;              //发布的截止时间
    private String detail;               //问卷详情
    private Integer version;             //轮次号
    private Integer status;              //发布的状态
}
