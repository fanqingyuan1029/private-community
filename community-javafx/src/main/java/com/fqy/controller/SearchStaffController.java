package com.fqy.controller;

import com.fqy.dao.AdministratorDao;
import com.fqy.entity.User;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class SearchStaffController extends Application implements Initializable {
    Stage primaryStage = new Stage();

    /*
    待修改为数据库中的字段
     */

    @FXML
    AnchorPane searchPane;
    @FXML
    private TextField nameSearch;
    @FXML
    private TableView<User> table;
    @FXML
    private TableColumn<User, String> usernameTable;
    @FXML
    private TableColumn<User, String> nameTable;
    @FXML
    private TableColumn<User, String> typeTable;
    @FXML
    private TableColumn<User, String> phoneTable;

    @FXML
    private Button titleButton;
    @FXML
    private Label titleLabel;

    public SearchStaffController() {
    }



    List<User> startList;
    ObservableList<User> cellData;

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        titleButton.setText("查询");
        titleLabel.setText("查询职工");

        System.out.println("开始请求数据了-------------------");
        startList  = AdministratorDao.getUsers();
        System.out.println("请求完了-----------------------");
        cellData = FXCollections.observableArrayList(startList);
        System.out.println("塞进cellData---------------------");
        usernameTable.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        nameTable.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        typeTable.setCellValueFactory(cellData -> new SimpleStringProperty(showType(cellData.getValue().getType())));
        phoneTable.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhone()));

        searchPane.setVisible(true);
    }

    private String showType(Integer num) {
        switch (num) {
            case 1:
                return "护工";
            case 2:
                return "护士";
            case 3:
                return "医生";
            case 0:
                return "未知生物";
            default:
                return "管理员";
        }
    }

    @FXML
    public void titleButton() {
        //查询职工
        cellData.clear();
        cellData.addAll(AdministratorDao.byName(nameSearch.getText()));
        table.setItems(null);
        table.setItems(cellData);
//        }
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/SearchStaffController.fxml")));
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
