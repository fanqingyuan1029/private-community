package com.fqy.entity.paper;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Paper {

    private Integer id;                  //问卷id
    private String name;                 //问卷名字

    private String createTime;           //注册时间

    private List<Question> questionList;

}
