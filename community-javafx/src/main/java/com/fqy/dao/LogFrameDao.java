package com.fqy.dao;


import com.fqy.entity.User;
import com.fqy.entity.entityutils.ResponseMessage;
import com.fqy.utils.SimpleTools;
import javafx.scene.control.Alert;
import lombok.SneakyThrows;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class LogFrameDao {

    //登录，根据用户名和密码返回一个User，若未找到则返回null
    @SneakyThrows
    public static User SignIn(String username, String password) {

        //向后端发送请求，获取数据
        RestTemplate restTemplate = new RestTemplate();
        String url = SimpleTools.SERVER_URL + "/user/login";
        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        HttpEntity<Map<String, Object>> param = new HttpEntity<>(params);
        try {
            ResponseEntity<ResponseMessage<User>> exchange = restTemplate.exchange(url, HttpMethod.POST, param, new ParameterizedTypeReference<ResponseMessage<User>>() {
            });
            ResponseMessage<User> response = exchange.getBody();
            if (response != null) {
                if (response.getCode() == 200) {
                    return response.getData();
                } else
                    SimpleTools.informationDialog(Alert.AlertType.WARNING, "警告", "登录失败", "用户名或密码不正确");
                return null;
            }
        } catch (Exception e){
            SimpleTools.informationDialog(Alert.AlertType.WARNING, "抱歉", "登录失败", "服务器没开机");
        }
        return null;
    }
}
