package com.fqy.controller;


import com.fqy.dao.AdministratorDao;
import com.fqy.entity.entityutils.RegexUtils;
import com.fqy.utils.SimpleTools;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AddStaffController extends Application implements Initializable {
    Stage primaryStage = new Stage();

    /*
    待修改为数据库中的字段
     */
    @FXML
    AnchorPane addPane;
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private TextField name;
    @FXML
    private TextField type;
    @FXML
    private TextField phone;


    @FXML
    private Button titleButton;
    @FXML
    private Label titleLabel;

    public AddStaffController() {
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        titleButton.setText("添加");
        titleLabel.setText("添加职工");
        addPane.setVisible(true);
    }

    @FXML
    public void titleButton() {
        //添加职工
        String[] properties = {username.getText(), password.getText(), name.getText(), type.getText(), phone.getText()};
        if (SimpleTools.isEmpty(properties))
            SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "警告", "请输入全部信息");

        else {
            //查询数据库，看看这个人有没有注册过
            if (AdministratorDao.isContain(properties[0])) {
                SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "抱歉", "该用户名已注册");
            } else {

                //发现没有注册，向数据库添加

                if (!Pattern.matches(RegexUtils.username, properties[0]))
                    SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "抱歉", "用户名只能大小写字母和数字，" +
                            "且长度为6-18之间");
                else if (!Pattern.matches(RegexUtils.password, properties[1]))
                    SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "抱歉", "密码应该最少6位，最多30位，" +
                            "包括至少1个大写字母,1个小写字母，1个数字，1个特殊字符");
                else if (!Pattern.matches(RegexUtils.trueName, properties[2]))
                    SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "抱歉", "姓名只能输入中文");

                else if (!Pattern.matches(RegexUtils.phone, properties[4]))
                    SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "抱歉", "电话号码只能长为11位的数字");

                else if (SimpleTools.informationDialog(Alert.AlertType.CONFIRMATION, "提示", "确认", "是否添加该用户？")) {
                    properties[3] = stringTypeToIntType(properties[3]);
                    boolean flag = AdministratorDao.register(properties);
                    //弹出提示,添加成功
                    if (flag) {
                        //添加完毕后，清空这些框框
                        username.clear();
                        password.clear();
                        name.clear();
                        type.clear();
                        phone.clear();
                        SimpleTools.informationDialog(Alert.AlertType.INFORMATION, "提示", "", "添加成功！");
                        ((Stage) titleButton.getScene().getWindow()).close();
                    }

                }
            }
        }
    }


    private static String stringTypeToIntType(String type) {
        switch (type) {
            case "护工": {
                return "1";
            }
            case "护士": {
                return "2";
            }
            case "医生": {
                return "3";
            }
            default: {
                SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "错误", "这类型我不认识,就当他是护工了");
                return "1";
            }
        }
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/AddStaffController.fxml")));
        primaryStage.setTitle("颐养社区");
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.getIcons().add(new Image("file:community-javafx/src/main/resources/resource/flour.png"));
        primaryStage.showAndWait();
    }

    public void showWindow() throws Exception {
        start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
