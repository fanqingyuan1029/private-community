package com.fqy.controller;


import com.fqy.utils.SimpleTools;
import com.fqy.dao.AdministratorDao;
import com.fqy.dao.BuildingDao;
import com.fqy.entity.OldMan;
import com.fqy.entity.User;
import com.fqy.entity.building.Bed;
import com.fqy.entity.building.Building;
import com.fqy.entity.building.Room;
import com.fqy.cookie.Cache;
import javafx.application.Application;
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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class AdministratorController extends Application implements Initializable {


    Stage primaryStage = new Stage();
    @FXML
    private AnchorPane settlePane;
    @FXML
    private Label adminNameLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label typeLabel;
    @FXML
    private Label UserPhoneLabel;
    @FXML
    private Button goBack;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField typeTextField;
    @FXML
    private TextField phoneTextField;
    @FXML
    private TextField adminPassword;
    @FXML
    private TextField adminUserName;
    @FXML
    private TextField adminName;
    @FXML
    private TextField phone;
    @FXML
    private AnchorPane buildingPane;
    @FXML
    private AnchorPane StaffPane;


    //    获取医生、护士、护工的数据
    List<Integer> idList1;
    List<Integer> idList2;
    List<Integer> idList3;
    ObservableList<String> strList1;
    ObservableList<String> strList2;
    ObservableList<String> strList3;


    //    数据封装到ListView()中
    @FXML
    private ListView<String> assistantAdmin = new ListView<>(strList1);
    @FXML
    private ListView<String> nurseAdmin = new ListView<>(strList2);
    @FXML
    private ListView<String> doctorAdmin = new ListView<>(strList3);

    /**
     * 菜单事件
     * 进入了员工管理菜单，包括但不限于医生/护士/护工
     */
    @FXML
    void StaffClick() {
        buildingPane.setVisible(false);
        StaffPane.setVisible(true);
        settlePane.setVisible(false);
        nameLabel.setText("");
        typeLabel.setText("");
        UserPhoneLabel.setText("");
        Cache.Cookie().remove("OperateUserId");
        assistantAdmin.getSelectionModel().clearSelection();
        nurseAdmin.getSelectionModel().clearSelection();
        doctorAdmin.getSelectionModel().clearSelection();
    }

    //界面初始化
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AdministratorDao.getUsers();
        idList1 = AdministratorDao.getIds(AdministratorDao.getUsersByType(1));
        idList2 = AdministratorDao.getIds(AdministratorDao.getUsersByType(2));
        idList3 = AdministratorDao.getIds(AdministratorDao.getUsersByType(3));
        strList1 = FXCollections.observableArrayList(AdministratorDao.getNames(AdministratorDao.getUsersByType(1)));
        strList2 = FXCollections.observableArrayList(AdministratorDao.getNames(AdministratorDao.getUsersByType(2)));
        strList3 = FXCollections.observableArrayList(AdministratorDao.getNames(AdministratorDao.getUsersByType(3)));

        assistantAdmin.setItems(strList1);
        nurseAdmin.setItems(strList2);
        doctorAdmin.setItems(strList3);
        User user = (User) Cache.Cookie().get("User");
        String label = "尊敬的管理员" + user.getName() + "，您好";
        adminNameLabel.setText(label);


    }

    public AdministratorController() {
    }

    /**
     * 按钮事件
     * 修改个人信息按钮，用于进入修改个人信息页面，并为页面内容设定PromptText
     */
    @FXML
    void settle() {
        StaffPane.setVisible(true);
        settlePane.setVisible(true);
        buildingPane.setVisible(false);
        User user = (User) Cache.Cookie().get("User");
        adminUserName.setPromptText(user.getUsername());
        adminPassword.setPromptText("请输入新密码");
        adminName.setPromptText(user.getName());
        phone.setPromptText(user.getPhone());
    }

    /**
     * 按钮事件
     * 修改个人信息提交按钮，点击后确认修改
     *
     * @throws Exception FX页面跳转可能会产生异常
     */
    @FXML
    void Finish() throws Exception {

        User user = new User();
        if (!SimpleTools.isEmpty(adminPassword.getText()))
            user.setPassword(adminPassword.getText());
        if (!SimpleTools.isEmpty(adminName.getText()))
            user.setName(adminName.getText());
        if (!SimpleTools.isEmpty(phone.getText()))
            user.setPhone(phone.getText());

        if (SimpleTools.informationDialog(Alert.AlertType.CONFIRMATION, "提示", "确认", "您确定要修改个人信息吗？")) {
            User user1 = (User) Cache.Cookie().get("User");
            user.setId(user1.getId());
            //选择确定,开始更新
            if (AdministratorDao.replace(user.getId(), user)) {
                settlePane.setVisible(false);
                adminPassword.clear();
                adminName.clear();
                phone.clear();
                //更新成功,重新读取当前用户
                User newCurrentUser = AdministratorDao.getAdminById(user.getId());
                if (newCurrentUser != null) {
                    Cache.Cookie().put("User", newCurrentUser);
                    adminNameLabel.setText("尊敬的管理员" + newCurrentUser.getName() + "，您好");
                } else {
                    //当前用户已经不在系统里面了,返回登录界面
                    SimpleTools.informationDialog(Alert.AlertType.ERROR, "提示", "抱歉", "获取用户信息失败，请重新尝试登录");
                    ((Stage) goBack.getScene().getWindow()).close();
                    (new LogFrameController()).showWindow();
                }
            }
        } else {//取消更新
            StaffPane.setVisible(false);
        }
    }

    /**
     * 按钮事件
     * 修改个人信息页面的返回按钮，取消修改
     */
    @FXML
    void back() {
        settlePane.setVisible(false);
    }

    /**
     * 按钮事件
     * 添加职工按钮，显示出添加职工页面
     *
     * @throws Exception FX页面跳转可能会抛出异常
     */
    @FXML
    void addButton() throws Exception {
        //前往添加的页面
        AddStaffController addStaffController = new AddStaffController();
        addStaffController.showWindow();
        //添加完毕,重载数据
        reload();
    }

    /**
     * 内部方法
     * 用于刷新医生/护士/护工页面的数据
     */
    private void reload() {
        idList1.clear();
        idList2.clear();
        idList3.clear();

        AdministratorDao.getUsers();

        idList1 = AdministratorDao.getIds(AdministratorDao.getUsersByType(1));
        idList2 = AdministratorDao.getIds(AdministratorDao.getUsersByType(2));
        idList3 = AdministratorDao.getIds(AdministratorDao.getUsersByType(3));

        assistantAdmin.setItems(null);
        nurseAdmin.setItems(null);
        doctorAdmin.setItems(null);

        strList1.clear();
        strList2.clear();
        strList3.clear();

        strList1.addAll(AdministratorDao.getNames(AdministratorDao.getUsersByType(1)));
        strList2.addAll(AdministratorDao.getNames(AdministratorDao.getUsersByType(2)));
        strList3.addAll(AdministratorDao.getNames(AdministratorDao.getUsersByType(3)));

        assistantAdmin.setItems(strList1);
        nurseAdmin.setItems(strList2);
        doctorAdmin.setItems(strList3);
    }

    /**
     * 按钮事件
     * 搜索按钮，弹出搜索页面
     *
     * @throws Exception FX页面跳转可能会抛出异常
     */
    @FXML
    void searchButton() throws Exception {
        //前往查找的界面
        SearchStaffController searchStaffController = new SearchStaffController();
        searchStaffController.showWindow();
    }

    /**
     * 内部方法
     * 将string类型的 用户类型type 转换成int类型
     *
     * @param type 用户类型的string表示
     * @return 用户类型的int表示
     */
    private static Integer StringTypeToIntType(String type) {
        switch (type) {
            case "护工": {
                return 1;
            }
            case "护士": {
                return 2;
            }
            case "医生": {
                return 3;
            }
            default: {
                SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "警告", "这类型我不认识,就当他是护工了");
                return 1;
            }
        }
    }

    /**
     * 内部方法
     * 数字用户类型转为字符串
     *
     * @param type 用户类型数字
     * @return 用户类型字符串
     */
    private static String IntTypeToStringType(Integer type) {
        switch (type) {
            case 1: {
                return "护工";
            }
            case 2: {
                return "护士";
            }
            case 3: {
                return "医生";
            }
            default: {
                return "未知";
            }
        }
    }

    /**
     * 按钮点击事件
     * 修改职工信息，将所选职工信息修改
     *
     * @throws Exception 方法内涉及到的JSON操作可能会抛出异常
     */
    @FXML
    void changeButton() throws Exception {
        User user;
        user = new User();
        /*
          获取当前正操作的用户Id
         */
        Integer operateUserId = (Integer) Cache.Cookie().get("OperateUserId");
        if (operateUserId == null) {
            SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "注意！", "请先选择一个用户");
            return;//清空显示栏中的数据
        }
//        if (operateUserId == null) return;//清空显示栏中的数据
        user.setId(operateUserId);
        //判空 的过程
        if (!SimpleTools.isEmpty(nameTextField.getText())) user.setName(nameTextField.getText());
        if (!SimpleTools.isEmpty(typeTextField.getText()))
            user.setType(StringTypeToIntType(typeTextField.getText()));
        if (!SimpleTools.isEmpty(phoneTextField.getText())) user.setPhone(phoneTextField.getText());
        //判空通过,调数据库,修改信息
        if (AdministratorDao.replace(user.getId(), user)) {//成功修改
            //请求提交完毕,更新页面
            user = AdministratorDao.getUserById(operateUserId);
            if (user == null) {
                Cache.Cookie().remove("OperateUserId");
                //给一个友好提示，告诉用户操作的目标已经不存在了
                SimpleTools.informationDialog(Alert.AlertType.INFORMATION, "提示", "抱歉", "该用户已被删除");

            }
            //清空输入框
            nameLabel.setText("");
            typeLabel.setText("");
            UserPhoneLabel.setText("");
            Cache.Cookie().remove("OperateUserId");
            assistantAdmin.getSelectionModel().clearSelection();
            nurseAdmin.getSelectionModel().clearSelection();
            doctorAdmin.getSelectionModel().clearSelection();
            nameTextField.clear();
            typeTextField.clear();
            phoneTextField.clear();
            //重新载入数据
            reload();
        }
    }

    /**
     * 按钮事件
     * 移除职工按钮，将选中职工删除
     */
    @FXML
    void removeButton() {
        User user = new User();
        Integer operateUserId = (Integer) Cache.Cookie().get("OperateUserId");
        if (operateUserId == null) {
            SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "注意！", "请先选择一个用户");
            return;//清空显示栏中的数据
        }

        user.setId(operateUserId);
        if (SimpleTools.informationDialog(Alert.AlertType.CONFIRMATION, "提示", "请确认！", "您确定要移除该职工吗？")) {
//           改成自己的dao
            AdministratorDao.remove(user.getId());
            //输入框框里面的值清空
            nameLabel.setText(null);
            typeLabel.setText(null);
            UserPhoneLabel.setText(null);
            Cache.Cookie().remove("OperateUserId");
            //删除完毕之后，重新载入所有数据
            reload();
        }
    }

    /**
     * 按钮点击事件
     * 注销按钮，注销用户
     *
     * @throws Exception 关闭FX页面可能会抛出异常
     */
    @FXML
    void goBack() throws Exception {
        ((Stage) goBack.getScene().getWindow()).close();
        (new LogFrameController()).showWindow();
    }

    /**
     * ListView点击事件
     * 点击了ListView中的护工名字
     */
    @FXML
    void assistantManage() {
        getIndexFromList(assistantAdmin, idList1);
    }

    /**
     * 内部方法,获取当前选择的医生/护士/护工的id
     *
     * @param listView 对应的ListView
     * @param idList   对应的id集合
     */
    private void getIndexFromList(ListView<String> listView, List<Integer> idList) {
        int index = listView.getSelectionModel().getSelectedIndex();
        if (index == -1) return;
        Integer selectUserId = idList.get(index);
        Cache.Cookie().put("OperateUserId", selectUserId);
        User user = AdministratorDao.byUserId(selectUserId);
        if (user != null) {
            nameLabel.setText(user.getName());
            typeLabel.setText(IntTypeToStringType(user.getType()));
            UserPhoneLabel.setText(user.getPhone());
        }
    }

    /**
     * ListView点击事件
     * 点击了ListView中的护士名字
     */
    @FXML
    void nurseManage() {
        getIndexFromList(nurseAdmin, idList2);
    }

    /**
     * ListView点击事件
     * 点击了医生ListView中的医生名字
     */
    @FXML
    void doctorManage() {
        getIndexFromList(doctorAdmin, idList3);
    }

    /**
     * ——————————————————————————————————————————楼宇管理——————————————————————————————
     */
    @FXML
    private ListView<String> buildingList;
    @FXML
    private ListView<String> roomList;
    @FXML
    private ListView<String> bedList;
    @FXML
    private Button addBuildingButton;
    @FXML
    private Button deleteBuildingButton;
    @FXML
    private Button addRoomButton;
    @FXML
    private Button deleteRoomButton;
    @FXML
    private Button addBedButton;
    @FXML
    private Button deleteBedButton;
    @FXML
    private TextField getRoomField;
    @FXML
    private TextField getBedField;
    @FXML
    private TextField getBuildingField;

    /**
     * 按钮事件
     * 社区空间管理按钮绑定的事件：显示楼宇管理界面
     */
    @FXML
    void buildingButton() {
        buildingPane.setVisible(true);
        StaffPane.setVisible(false);

        //数据库里面查楼栋
        List<Building> buildings = BuildingDao.getBuildings();
//        List<Integer> idList = BuildingDao.getIds(buildings);
        ObservableList<String> strList = FXCollections.observableArrayList(BuildingDao.getNames(buildings));


        //删除楼栋按钮不显示
        deleteBuildingButton.setVisible(false);

        //楼栋添加按钮与文本框显示
        addBuildingButton.setVisible(true);
        getBuildingField.setVisible(true);

        //房间相关的按钮与文本框不显示
        addRoomButton.setVisible(false);
        deleteRoomButton.setVisible(false);
        getRoomField.setVisible(false);

        //床位相关的按钮与文本框不显示
        deleteBedButton.setVisible(false);
        addBedButton.setVisible(false);
        getBedField.setVisible(false);

        //楼栋数据显示出来
        buildingList.setItems(null);
        buildingList.setItems(strList);
        roomList.setItems(null);
        bedList.setItems(null);

    }

    /**
     * 楼栋ListView点击事件：
     * 选了一栋楼,获取一栋楼里的房间列表
     */
    @FXML
    void chooseBuilding() {

        //session里面拿出楼栋的集合,按照点击的名字找到他
        String buildingName = buildingList.getSelectionModel().getSelectedItem();
        List<?> rooms = BuildingDao.getRoomListFromBuilding(buildingName);
        //如果房间名为空，房间列表为空，
        if (buildingName == null) {
            //房间列表为空
            roomList.setItems(null);
            //床位列表为空
            bedList.setItems(null);
            //删除楼栋按钮、房间相关的按钮，文本框、床位相关的按钮，文本框均不显示
            deleteBuildingButton.setVisible(false);
            deleteRoomButton.setVisible(false);
            addRoomButton.setVisible(false);
            getRoomField.setVisible(false);
            deleteBedButton.setVisible(false);
            addBedButton.setVisible(false);
            getBedField.setVisible(false);
            return;
        } else {
            deleteBuildingButton.setVisible(true);
            deleteRoomButton.setVisible(false);
            deleteBedButton.setVisible(false);
        }
        if (rooms.size() > 0) {
            buildRoomsResultList(rooms);
        } else {

            roomList.setItems(null);
            deleteRoomButton.setVisible(false);
        }

        addRoomButton.setVisible(true);
        getRoomField.setVisible(true);

        deleteBedButton.setVisible(false);
        addBedButton.setVisible(false);
        getBedField.setVisible(false);
        bedList.setItems(null);

    }

    /**
     * 内部方法
     * 这是从房间集合中获取所有房间的名字，放入显示ListView
     *
     * @param rooms 房间集合
     */
    private void buildRoomsResultList(List<?> rooms) {
        List<String> roomsName = new ArrayList<>();
        for (Object roomObj : rooms) {
            Room room = (Room) roomObj;
            String str = room.getName();
            roomsName.add(str);
        }

        ObservableList<String> strList = FXCollections.observableArrayList(roomsName);

        roomList.setItems(null);
        roomList.setItems(strList);
    }


    /**
     * 房间ListView点击事件：
     * 选了一间房,获取房间里的床位列表
     */
    @FXML
    void chooseRoom() {
        String buildingName = buildingList.getSelectionModel().getSelectedItem();
        String roomName = roomList.getSelectionModel().getSelectedItem();
        List<?> beds = BuildingDao.getBedListFromRoom(buildingName, roomName);
//        System.out.println("选择了" + buildingName + "下的" + roomName);
        if (roomName == null) {
            bedList.setItems(null);
            deleteRoomButton.setVisible(false);
            deleteBedButton.setVisible(false);
            addBedButton.setVisible(false);
            getBedField.setVisible(false);
            return;
        } else {
            deleteRoomButton.setVisible(true);
        }
        List<String> bedsName = new ArrayList<>();
        if (beds.size() > 0) {
            for (Object bedObj : beds) {
                Bed bed = (Bed) bedObj;
                String str = bed.getName();
                bedsName.add(str);
            }
        } else {
            deleteBedButton.setVisible(false);
        }

        ObservableList<String> strList = FXCollections.observableArrayList(bedsName);
        System.out.println("这个房间里面有：" + bedsName);
        bedList.setItems(strList);
        addBedButton.setVisible(true);
        getBedField.setVisible(true);
    }

    /**
     * 床位ListView点击事件：
     * 选择床位
     */
    @FXML
    void chooseBed() {
        String buildingName = buildingList.getSelectionModel().getSelectedItem();
        String roomName = roomList.getSelectionModel().getSelectedItem();
        String bedName = bedList.getSelectionModel().getSelectedItem();
        deleteBedButton.setVisible(bedName != null);
        BuildingDao.getOldManFromBed(buildingName, roomName, bedName);
        addBedButton.setManaged(true);
        getBedField.setManaged(true);
    }

    /**
     * 按钮事件
     * 移除床位按钮点击
     */
    @FXML
    void reduceBedButton() {
        String buildingName = buildingList.getSelectionModel().getSelectedItem();
        String roomName = roomList.getSelectionModel().getSelectedItem();
        String bedName = bedList.getSelectionModel().getSelectedItem();
        //缺少判断床位上是否有人
        OldMan oldMan = BuildingDao.getOldManFromBed(buildingName, roomName, bedName);
        if (oldMan != null) {
            SimpleTools.informationDialog(Alert.AlertType.ERROR, "提示", "错误", "床上有人，您还不能删除该床位");
        } else if (SimpleTools.informationDialog(Alert.AlertType.CONFIRMATION, "提示", "请确认", "是否移除该床位？")) {
            if (BuildingDao.delete(buildingList.getSelectionModel().getSelectedItem(), roomList.getSelectionModel().getSelectedItem(),
                    bedList.getSelectionModel().getSelectedItem()))
                refreshBeds();
        }
    }

    /**
     * 内部方法
     * 刷新床位列表
     */
    private void refreshBeds() {
        BuildingDao.getBuildings();
        List<?> beds = BuildingDao.getBedListFromRoom(buildingList.getSelectionModel().getSelectedItem(), roomList.getSelectionModel().getSelectedItem());
        if (beds.size() > 0) {
            List<String> bedsName = new ArrayList<>();
            for (Object bedObj : beds) {
                Bed bed = (Bed) bedObj;
                String str = bed.getName();
                bedsName.add(str);
                ObservableList<String> strList = FXCollections.observableArrayList(bedsName);
                bedList.setItems(strList);
            }

        } else {
            bedList.setItems(null);

        }
        deleteBedButton.setVisible(false);
    }

    /**
     * 按钮事件
     * 移除房间按钮事件，移除房间
     */
    @FXML
    void reduceRoomButton() {
        List<?> beds = BuildingDao.getBedListFromRoom(buildingList.getSelectionModel().getSelectedItem(), roomList.getSelectionModel().getSelectedItem());
        if (beds.size() > 0)
            SimpleTools.informationDialog(Alert.AlertType.ERROR, "提示", "错误", "请先删除下一级单位");
        else if (SimpleTools.informationDialog(Alert.AlertType.CONFIRMATION, "提示", "请确认", "是否移除该房间？")) {
            if (BuildingDao.delete(buildingList.getSelectionModel().getSelectedItem(), roomList.getSelectionModel().getSelectedItem()))
                refreshRooms();
        }
    }

    /**
     * 内部方法
     * 刷新房间列表
     */
    private void refreshRooms() {
        BuildingDao.getBuildings();
        List<?> rooms = BuildingDao.getRoomListFromBuilding(buildingList.getSelectionModel().getSelectedItem());
        buildRoomsResultList(rooms);
        bedList.setItems(null);
        deleteBedButton.setVisible(false);
        deleteRoomButton.setVisible(false);
        addBedButton.setVisible(false);
        getBedField.setVisible(false);
    }

    /**
     * 按钮事件
     * 删除楼号按钮绑定事件：删除楼号
     */
    @FXML
    void reduceBuildingButton() {

        List<?> rooms = BuildingDao.getRoomListFromBuilding(buildingList.getSelectionModel().getSelectedItem());
        if (rooms.size() > 0)
            SimpleTools.informationDialog(Alert.AlertType.ERROR, "提示", "错误", "请先删除下一级单位");
        else if (SimpleTools.informationDialog(Alert.AlertType.CONFIRMATION, "提示", "请确认", "是否移除该楼栋？")) {
            if (BuildingDao.delete(buildingList.getSelectionModel().getSelectedItem()))
                refreshBuildings();
        }
    }

    /**
     * 内部方法
     * 刷新楼栋的所有信息
     */
    private void refreshBuildings() {
//        BuildingDao.delete(buildingList.getSelectionModel().getSelectedItem());
        ObservableList<String> strList = FXCollections.observableArrayList(BuildingDao.getNames(BuildingDao.getBuildings()));

        buildingList.setItems(null);
        buildingList.setItems(strList);
        roomList.setItems(null);
        bedList.setItems(null);
        deleteBedButton.setVisible(false);
        deleteRoomButton.setVisible(false);
        addBedButton.setVisible(false);
        getBedField.setVisible(false);
        getRoomField.setVisible(false);
        deleteBuildingButton.setVisible(false);
        addRoomButton.setVisible(false);
    }

    /**
     * 按钮事件
     * 新增床位按钮，新增床位
     */
    @FXML
    void setBedButton() {
        String buildingName = buildingList.getSelectionModel().getSelectedItem();
        String roomName = roomList.getSelectionModel().getSelectedItem();
        String bedName = getBedField.getText();
        if (SimpleTools.isEmpty(bedName)) {
            SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "注意", "你怎么名字都不写！");
            return;
        }
        Bed bed = new Bed();
        bed.setName(bedName);
        boolean isOk = BuildingDao.registerBed(buildingName, roomName, bed);
        if (isOk) {
            refreshBeds();
        }
        getBedField.clear();

    }

    /**
     * 按钮事件
     * 新增房间按钮，新增房间
     */
    @FXML
    void setRoomButton() {

        String buildingName = buildingList.getSelectionModel().getSelectedItem();
        String roomName = getRoomField.getText();
        if (SimpleTools.isEmpty(roomName)) {
            SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "注意", "你怎么名字都不写！");
            return;
        }
        Room room = new Room();
        room.setName(roomName);
        boolean isOk = BuildingDao.registerRoom(buildingName, room);
        if (isOk) {
            refreshRooms();
        }
        getRoomField.clear();
    }

    /**
     * 按钮事件
     * 新增楼栋按钮，新增楼栋
     */
    @FXML
    void setBuildingButton() {
        String buildingName = getBuildingField.getText();
        Building building = new Building();
        building.setName(buildingName);
        if (SimpleTools.isEmpty(buildingName)) {
            SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "注意", "你怎么名字都不写！");
            return;
        }
        boolean isOk = BuildingDao.registerBuilding(building);
        if (isOk)
            refreshBuildings();

        getBuildingField.clear();
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/AdministratorController.fxml")));
        primaryStage.setTitle("颐养社区");
        primaryStage.setScene(new Scene(root, 1035, 720));
        primaryStage.getIcons().add(new Image("file:community-javafx/src/main/resources/resource/flour.png"));
        primaryStage.show();
    }

    public void showWindow() throws Exception {
        start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
