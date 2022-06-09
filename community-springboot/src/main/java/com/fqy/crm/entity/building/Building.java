package com.fqy.crm.entity.building;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Building {

    private Integer id;             //楼栋id
    private String name;            //楼栋名字
    private String createTime;     //注册时间
    private List<Room> roomList;

}
