package com.fqy.entity.building;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Room {

    private Integer id;             //房间号id
    private Integer buildingId;     //楼栋id
    private String name;            //房间名
    private String createTime;    //注册时间
    private List<Bed> bedList;
}
