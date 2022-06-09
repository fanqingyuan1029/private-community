package com.fqy.crm.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OldMan {

    private Integer id;         //病人id
    private String name;        //病人姓名
    private Integer sex;        //病人性别
    private Integer age;        //病人年龄
    private String phone;       //病人电话
    private String contactName;    //紧急联系人
    private String contactPhone;   //紧急联系人电话

    private Integer bedId;     //床位id

//    private Integer managerId;      //负责人的id
//    private User manager;           //负责人的信息
    private String loadingName;     //住宿名字

    private String createTime;    //注册时间
    private String lastUpdateTime;  //最后一次修改的时间

    /*
     private Integer buildingId;    //楼栋id
     private Integer roomId;    //房间id
     private Bed bed;
     */

}
