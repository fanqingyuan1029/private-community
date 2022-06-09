package com.fqy.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OldMan {

    private Integer id;         //老人id
    private String name;        //老人姓名
    private Integer sex;        //老人性别
    private Integer age;        //老人年龄
    private String phone;       //老人电话
    private String contactName;    //紧急联系人
    private String contactPhone;   //紧急联系人电话

    private Integer bedId;     //床位id

//    private Integer managerId;      //负责人的id
//    private User manager;           //负责人的信息
    private String loadingName;     //住宿名字

    private String createTime;    //注册时间
    private String lastUpdateTime;  //最后一次修改的时间

    public String oldManDetail() {
        StringBuilder stringBuilder = new StringBuilder();
        if (this.id != null) stringBuilder.append("老人id\t\t\t").append(this.id).append("\n");
        if (this.name != null) stringBuilder.append("老人姓名\t\t").append(this.name).append("\n");
        if (this.sex == 1) stringBuilder.append("老人性别\t\t").append("男").append("\n");
        else stringBuilder.append("老人性别\t\t").append("女").append("\n");
        if (this.age != null) stringBuilder.append("老人年龄\t\t").append(this.age).append("\n");
        if (this.phone != null) stringBuilder.append("老人电话\t\t").append(this.phone).append("\n");
        if (this.contactName != null) stringBuilder.append("老人紧急联系人\t\t").append(this.contactName).append("\n");
        if (this.contactPhone != null) stringBuilder.append("老人紧急联系电话\t").append(this.contactPhone).append(" ");
        return stringBuilder.toString();
    }

}
