package com.fqy.entity.paper;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Answer {

    private Integer id;                   //回答问卷的id
    private Integer publishPaperId;       //问卷id
    private Integer oldManId;             //回答问卷用户的id
    private String answer;                //答案的序列，比如{1,2,3,4}代表4个问题分别选了第1，2，3，4个选项
    private String createTime;            //注册的时间

    private Integer publishPaperVersion;  //发布问卷轮次号

    private String oldManName;            //病人的姓名
    private String publishPaperName;      //发布问卷名字
}
