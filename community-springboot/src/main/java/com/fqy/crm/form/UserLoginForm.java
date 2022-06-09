package com.fqy.crm.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


@Data
public class UserLoginForm {

    @Pattern(regexp = "^[a-zA-Z0-9]{6,18}$", message = "用户名只能大小写字母和数字，且长度为6-18之间")
    @NotBlank(message = "用户名不能为空")
    private String username;            //用户名

    @Pattern(regexp = "^.*(?=.{6,30})(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*? ]).*$",
            message = "密码应该最少6位，最多30位，包括至少1个大写字母，1个小写字母，1个数字，1个特殊字符")
    @NotBlank(message = "密码不能为空")
    private String password;            //密码
}
