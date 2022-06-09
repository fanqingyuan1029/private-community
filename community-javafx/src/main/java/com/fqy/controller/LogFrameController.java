package com.fqy.controller;

import com.fqy.utils.SimpleTools;
import com.fqy.cookie.Cache;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import com.fqy.dao.LogFrameDao;
import com.fqy.entity.User;

import java.util.Objects;

public class LogFrameController extends Application {

    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Button restButton;
    @FXML
    private Button SignIn;
    @FXML
    private Button Signup;
    Stage primaryStage = new Stage();

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/LogFrameController.fxml")));
        primaryStage.setTitle("颐养社区");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 400, 320));
        primaryStage.getIcons().add(new Image("file:community-javafx/src/main/resources/resource/flour.png"));
//        primaryStage.getIcons().add(new Image("file:src/main/java/com/fqy/resource/flour.png"));
        primaryStage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }

    @FXML
    void SignIn(ActionEvent event) throws Exception {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

        if(SimpleTools.isLogIn(usernameTextField, passwordTextField)){
            User user = LogFrameDao.SignIn(username,password);
            if( user != null) {
                //进入下一个界面
                //如果权限为4，则进入管理员界面
                if(user.getType()==4) {
                    ((Stage) SignIn.getScene().getWindow()).close();
//                    StaffDao.getInstance().setUser(user);
                    Cache.Cookie().put("User",user);
                    (new AdministratorController()).showWindow();

                }
//                //否则进入职工界面
                else{
                    ((Stage) SignIn.getScene().getWindow()).close();
                    Cache.Cookie().put("User",user);
                    (new StaffController()).showWindow();
                }

                //测试数据是否正确获取
//                System.out.println(user.getUsername() + " " +user.getPassword() );
//            }
//            else{
//                //提示用户名或密码不正确
//                SimpleTools.informationDialog(Alert.AlertType.ERROR, "提示", "抱歉", "用户名或密码不正确！");
//
            }
        }
    }


//    对应密码右边的叉，清除密码
    @FXML
    void resetButtonEvent(ActionEvent event) {
        passwordTextField.clear();
    }
    public void  showWindow() throws Exception {
        start(primaryStage);
    }
}
