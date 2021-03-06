package com.fqy.entity.entityutils;


public interface RegexUtils {
    //匹配用户的用户名，范围是大小写字母、数字，长度为6到18位
    String username = "^[a-zA-Z0-9]{6,18}$";
    //匹配用户名的密码，长度是6到30位，里面必须包含一个大写字母、一个小写字母、一个数字、一个特殊字符
    String password = "^.*(?=.{6,30})(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*? ]).*$";
    //匹配用户的真实姓名，必须为为汉字
    String trueName = "[\\u4e00-\\u9fa5]{2,10}";
    //匹配用户的电话号码，按国内主流运行商号段进行匹配
    String phone = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$";
    //匹配用户的真实年龄，年龄为数字
    String age = "^(?:[1-9][0-9]?|1[01][0-9]|120)$";
}
