package com.fqy.crm.form;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class UserRegisterForm {

    @Pattern(regexp = "^[a-zA-Z0-9]{6,18}$", message = "用户名只能大小写字母和数字，且长度为6-18之间")
    @NotBlank(message = "用户名不能为空")
    private String username;            //用户名

    @Pattern(regexp = "^.*(?=.{6,30})(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*? ]).*$",
            message = "密码应该最少6位，最多30位，包括至少1个大写字母，1个小写字母，1个数字，1个特殊字符")
    @NotBlank(message = "密码不能为空")
    private String password;            //密码

    @Pattern(regexp = "[\\u4e00-\\u9fa5]+", message = "姓名只能输入中文")
    @NotBlank(message = "用户真实姓名不能为空")
    private String name;                //真实姓名

    @NotNull(message = "用户类型不能为空")
    @Range(min = 1,max = 4,message = "用户类型参数不合法")
    private Integer type;               //用户类型

    @Pattern(regexp = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$",message = "电话号码只能长为11位的数字")
    @NotBlank(message = "电话号码不能空")
    private String phone;               //用户电话

}
