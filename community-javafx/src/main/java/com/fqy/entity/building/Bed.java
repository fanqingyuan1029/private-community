package com.fqy.entity.building;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fqy.entity.OldMan;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Bed {

    private Integer id;             //床位id
    private Integer roomId;         //房间号id
    private String name;            //床的名字
    private OldMan oldMan;          //床绑定病人的信息
    private String createTime;      //注册时间

}
