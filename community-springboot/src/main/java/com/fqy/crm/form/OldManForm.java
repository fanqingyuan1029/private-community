package com.fqy.crm.form;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class OldManForm {

    @Pattern(regexp = "[\\u4e00-\\u9fa5]+", message = "姓名只能输入中文")
    @NotBlank(message = "用户真实姓名不能为空")
    private String name;        //病人姓名

    @NotNull(message = "病人性别不能为空")
    @Range(min = 1,max = 2,message = "用户类型参数不合法")
    private Integer sex;        //病人性别

    @NotNull(message = "病人年龄不能为空")
    @Range(min = 0,max = 150,message = "用户类型参数不合法")
    private Integer age;        //病人年龄

    @Pattern(regexp = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$",message = "电话号码只能长为11位的数字")
    @NotBlank(message = "电话号码不能空")
    private String phone;       //病人电话

    @Pattern(regexp = "[\\u4e00-\\u9fa5]+", message = "姓名只能输入中文")
    @NotBlank(message = "紧急联系人真实姓名不能为空")
    private String contactName;    //紧急联系人

    @Pattern(regexp = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$",message = "电话号码只能长为11位的数字")
    @NotBlank(message = "电话号码不能空")
    private String contactPhone;   //紧急联系人电话
}
