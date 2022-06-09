package com.fqy.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fqy.entity.entityutils.ResponseMessage;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.function.Function;

public class SimpleTools {

    /**
     * 操作结果：JavaFX判断是否为空
     *
     * @param str 文本
     * @return boolean 如果为空返回true，否则返回false
     */
    public static boolean isEmpty(String str) {
        return str == null || "".equals(str.trim());
    }

    /**
     * 操作：循环判断数组是否有空值（返回值与上一方法完全相反）
     *
     * @param properties 字符串数组
     * @return boolean 如果为空返回true，否则返回false
     */
    public static boolean isEmpty(String[] properties) {
        for (String s : properties) {
            if (s == null || "".equals(s.trim())) return true;
        }
        return false;
    }

    /**
     * 操作结果：自定义一个JavaFX的对话框
     *
     * @param alterType 对话框类型0
     * @param title     对话框标题
     * @param header    对话框头信息
     * @param message   对话框显示内容
     * @return boolean 如果点击了”确定“那么就返回true，否则返回false
     */
    public static boolean informationDialog(Alert.AlertType alterType, String title, String header, String message) {
        // 按钮部分可以使用预设的也可以像这样自己 new 一个
        Alert alert;
        if (alterType.equals(Alert.AlertType.CONFIRMATION)) {
            alert = new Alert(alterType, message, new ButtonType("取消", ButtonBar.ButtonData.CANCEL_CLOSE), new ButtonType("确定", ButtonBar.ButtonData.YES));
        } else {
            alert = new Alert(alterType, message, new ButtonType("确定", ButtonBar.ButtonData.YES));
        }

        // 设置对话框的标题
        alert.setTitle(title);
        alert.setHeaderText(header);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("file:community-javafx/src/main/resources/resource/flour.png"));
        // showAndWait() 将在对话框消失以前不会执行之后的代码
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.isPresent()) {
            ButtonType type = buttonType.get();
            // 根据点击结果返回
            return type.getButtonData().equals(ButtonBar.ButtonData.YES);// 如果点击了“确定”就返回true
        }
        return false;
    }

    /**
     * 操作结果：JavaFX判断登录界面用户名密码是否填写
     *
     * @param userNameTextField 用户名文本框
     * @param passwordTextField 密码文本框
     * @return boolean 如果登录成功返回true，否则返回false
     */
    public static boolean isLogIn(TextInputControl userNameTextField, TextInputControl passwordTextField) {
        if (SimpleTools.isEmpty(userNameTextField.getText())) {
            SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "警告", "用户名不能为空！");
            return false;
        }
        if (SimpleTools.isEmpty(passwordTextField.getText())) {
            SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "警告", "密码不能为空！");
            return false;
        }
        return true;
    }

    public static boolean register(String url, Object obj) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, obj, String.class);
        String body = response.getBody();
        if (!"".equals(body) && null != body) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                ResponseMessage<?> responseMessage = objectMapper.readValue(body, ResponseMessage.class);
                if (responseMessage.getCode() == 200) {
                    System.out.println(responseMessage);
                    return true;
                } else {
                    System.out.println(responseMessage.getData());
                    SimpleTools.informationDialog(Alert.AlertType.ERROR, "错误", responseMessage.getMessage(), responseMessage.getData() != null ? responseMessage.getData().toString() : "");
//                    return false;
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        }
        return false;
    }

    public static final String SERVER_URL = "http://localhost:8123";
//    public static final String SERVER_URL = "http://localhost:8123";

    public static final RestTemplate restTemplate = new RestTemplate();

    /**
     * 有时候数据库里面会存int类型,比如1男2女,显示则需要转化为字符串
     *
     * @param value    需要转化的int值
     * @param function 转化规则
     * @return 转化后的String
     */
    public static String IntegerValueToString(Integer value, Function<Integer, String> function) {
        return function.apply(value);
    }

}