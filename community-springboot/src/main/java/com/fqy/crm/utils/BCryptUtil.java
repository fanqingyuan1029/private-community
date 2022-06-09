package com.fqy.crm.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptUtil {
    /**
     * 对密码进行加密
     * @param password 密码
     * @return 加密后的密码
     */
    public static String encode(String password) {
        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        String hashPass = bcryptPasswordEncoder.encode(password);
        return hashPass;
    }
    /**
     * 对原密码和已加密的密码进行匹配，判断是否相等
     * @param password 密码
     * @param encodedPassword 加密后的密码
     * @return 匹配结果
     */
    public static boolean match(String password,String encodedPassword) {
        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        boolean result = bcryptPasswordEncoder.matches(password, encodedPassword);
        return result;
    }

    /**
     * 测试密码
     * @param args ...
     */
    public static void main(String[] args) {
        String hashPass = encode("123456");
        System.out.println(hashPass);
        //true
        System.out.println(match("123456",hashPass));
        //true
        System.out.println(match("123456","$2a$10$7wOQPHU2MfHt3X4wCFx5H.EZu.rlHMtY5HTFsqXiPd6BA5vNHJNf2"));
        //true
        System.out.println(match("123456","$2a$10$nYQWXcY.eVUwI8kYGtMCVOD0hWE4AKjzFg0oo91qc/ECQg/DD/CpS"));
        //true
        System.out.println(match("123456","$2a$10$9etIPtquQ3f..ACQkDHAVuBfjBoDXXWHHCOBl/RaJADxuXdSQB6I2"));
    }

}
