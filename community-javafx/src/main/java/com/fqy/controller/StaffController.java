package com.fqy.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fqy.dao.*;
import com.fqy.entity.OldMan;
import com.fqy.entity.paper.PublishPaper;
import com.fqy.utils.SimpleTools;
import com.fqy.entity.User;
import com.fqy.entity.building.Bed;
import com.fqy.entity.building.Building;
import com.fqy.entity.building.Room;
import com.fqy.entity.entityutils.RegexUtils;
import com.fqy.entity.paper.Answer;
import com.fqy.entity.paper.Paper;
import com.fqy.entity.paper.Question;
import com.fqy.cookie.Cache;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
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

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;

public class StaffController extends Application implements Initializable {
    public Label preOldManName;
    public Label preOldManSex;
    public Label preOldManAge;
    public Label preOldManPhone;
    public Label preOldManContactName;
    public Label preOldManContactPhone;
    Stage primaryStage = new Stage();

    @FXML
    private Label nameLabel;
    @FXML
    private AnchorPane settlePane;
    @FXML
    private TextField adminPassword;
    @FXML
    private TextField adminUserName;
    @FXML
    private TextField adminName;
    @FXML
    private TextField adminPhone;

    /**
     * ————————————————————————————修改个人信息————————————————————————
     */
    @FXML
    void settle() {
        settlePane.setVisible(true);
        User user = (User) Cache.Cookie().get("User");
        adminUserName.setPromptText(user.getUsername());
        adminPassword.setPromptText("请输入新密码");
        adminName.setPromptText(user.getName());
        adminPhone.setPromptText(user.getPhone());
    }

    @FXML
    void Finish() throws Exception {

        User user = new User();
        if (!SimpleTools.isEmpty(adminPassword.getText()))
            user.setPassword(adminPassword.getText());
        if (!SimpleTools.isEmpty(adminName.getText()))
            user.setName(adminName.getText());
        if (!SimpleTools.isEmpty(adminPhone.getText()))
            user.setPhone(adminPhone.getText());

        if (SimpleTools.informationDialog(Alert.AlertType.CONFIRMATION, "提示", "确认", "您确定要修改个人信息吗？")) {
            User user1 = (User) Cache.Cookie().get("User");
            user.setId(user1.getId());
            //选择确定,开始更新
            if (AdministratorDao.replace(user.getId(), user)) {
                settlePane.setVisible(false);
                adminPassword.clear();
                adminName.clear();
                adminPhone.clear();
                //更新成功,重新读取当前用户
                User newCurrentUser = AdministratorDao.getAdminById(user.getId());
                if (newCurrentUser != null) {
                    Cache.Cookie().put("User", newCurrentUser);
                    nameLabel.setText(newCurrentUser.getName());
                } else {
                    //当前用户已经不在系统里面了,返回登录界面
                    SimpleTools.informationDialog(Alert.AlertType.ERROR, "提示", "抱歉", "获取用户信息失败，请重新尝试登录");
                    ((Stage) goBack.getScene().getWindow()).close();
                    (new LogFrameController()).showWindow();
                }
            }
        } else {//取消更新
            settlePane.setVisible(false);
        }
    }

    @FXML
    void back() {
        settlePane.setVisible(false);
    }

    /**
     * 注销
     */
    @FXML
    public void goBack() throws Exception {
        ((Stage) goBack.getScene().getWindow()).close();
        (new LogFrameController()).showWindow();
    }


    public StaffController() {
    }

    /*------------------------床位管理--------------------- */
    @FXML
    private ListView<String> buildingList;
    @FXML
    private ListView<String> roomList;
    @FXML
    private ListView<String> bedList;
    @FXML
    private Label oldManInformation;
    @FXML
    private AnchorPane oldManInBedPane;
    @FXML
    private Label oldManInBedName;
    @FXML
    private AnchorPane oldManNotInBedPane;
    @FXML
    private TableView<OldMan> notInBedOldManTable;
    @FXML
    private TableColumn<OldMan, String> notInBedOldManNameColumn;

    /**
     * 跳转到床位管理
     */
    @FXML
    void bedManagePage() {

        oldManPane.setVisible(false);
        settlePane.setVisible(false);
        paperPane.setVisible(false);
        questionPane.setVisible(false);
        publishPaperPane.setVisible(false);
        paperAnswerPane.setVisible(false);

        bedPane.setVisible(true);
        oldManNotInBedPane.setVisible(false);
        oldManInBedPane.setVisible(false);
        oldManInformation.setVisible(false);
        //数据库里面查楼栋
        List<Building> buildings = BuildingDao.getBuildings();
//        List<Integer> idList = BuildingDao.getIds(buildings);
        ObservableList<String> strList = FXCollections.observableArrayList(BuildingDao.getNames(buildings));

        //楼栋数据显示出来
        buildingList.setItems(null);
        buildingList.setItems(strList);
        roomList.setItems(null);
        bedList.setItems(null);

    }

    @FXML
    void chooseBuilding() {
        //在列表中选择了楼栋
        //session里面拿出楼栋的集合,按照点击的名字找到他
        String buildingName = buildingList.getSelectionModel().getSelectedItem();
        List<?> rooms = BuildingDao.getRoomListFromBuilding(buildingName);
        //如果房间名为空，房间列表为空，
        if (buildingName == null) {
            //房间列表为空
            roomList.setItems(null);
            //床位列表为空
            bedList.setItems(null);
            return;
        }
        if (rooms.size() > 0) {
            buildRoomsResultList(rooms);
        } else
            roomList.setItems(null);
        oldManInBedPane.setVisible(false);
        oldManNotInBedPane.setVisible(false);
        oldManInformation.setVisible(false);
        bedList.setItems(null);
    }

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

    @FXML
    void chooseRoom() {
        //在列表中选择了房间
        String buildingName = buildingList.getSelectionModel().getSelectedItem();
        String roomName = roomList.getSelectionModel().getSelectedItem();
        List<?> beds = BuildingDao.getBedListFromRoom(buildingName, roomName);
//        System.out.println("选择了" + buildingName + "下的" + roomName);
        if (SimpleTools.isEmpty(roomName)) {
            if (SimpleTools.isEmpty(buildingName)) {
                bedList.setItems(null);
                SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "警告", "你还没有选择楼栋");
                return;
            } else if (roomList.getItems() == null || roomList.getItems().isEmpty()) {
                SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "警告", "该楼栋还没有房间");
                return;
            }
        }
        List<String> bedsName = new ArrayList<>();
        if (beds.size() > 0) {
            for (Object bedObj : beds) {
                Bed bed = (Bed) bedObj;
                String str = bed.getName();
                bedsName.add(str);
            }
        }
        oldManInBedPane.setVisible(false);
        oldManNotInBedPane.setVisible(false);
        oldManInformation.setVisible(false);
        ObservableList<String> strList = FXCollections.observableArrayList(bedsName);
        System.out.println("这个房间里面有：" + bedsName);
        bedList.setItems(strList);

    }

    @FXML
    void chooseBed() {
        //在列表中选了床位
        String buildingName = buildingList.getSelectionModel().getSelectedItem();
        String roomName = roomList.getSelectionModel().getSelectedItem();
        String bedName = bedList.getSelectionModel().getSelectedItem();
//        deleteBedButton.setVisible(bedName != null);
        if (SimpleTools.isEmpty(roomName)) {
            SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "警告", "你还没有选择房间");
            return;
        } else if (bedList.getItems() == null || bedList.getItems().isEmpty()) {
            SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "警告", "该房间还没有床位,请先添加");
            return;
        } else if (SimpleTools.isEmpty(bedName)) {
            SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "警告", "还没有选择床位");
            return;
        }
        OldMan oldManFromBed = BuildingDao.getOldManFromBed(buildingName, roomName, bedName);
        if (oldManFromBed != null) {
            oldManInformation.setVisible(true);
            oldManInformation.setText(oldManFromBed.oldManDetail());
            oldManInBedPane.setVisible(true);
            oldManInBedName.setText(oldManFromBed.getName());
            oldManNotInBedPane.setVisible(false);


        } else {
            oldManInBedPane.setVisible(false);
            oldManNotInBedPane.setVisible(true);
            //去数据库查没进床的病人
            ObservableList<OldMan> list = FXCollections.observableArrayList(OldManDao.getNoBedOldMan());
            notInBedOldManTable.setItems(null);
            notInBedOldManTable.setItems(list);

            oldManIntoBedButton.setVisible(false);
            oldManInformation.setVisible(false);
        }
    }

    @FXML
    private Button oldManIntoBedButton;

    @FXML
    void chooseOldMan() {
        //在列表中选了病人
        OldMan selected = notInBedOldManTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            oldManIntoBedButton.setVisible(false);
            return;
        }
//        Cache.Cookie().put("selectedToBedOldMan",selected);
        //*****这里要给下面的框设置内容
        oldManInformation.setText(selected.oldManDetail());
        oldManInformation.setVisible(true);
        oldManIntoBedButton.setVisible(true);
    }

    @FXML
    void oldManIntoBed() {
        //入住按钮,将选中的病人入住
        OldMan selectedToBedOldMan = notInBedOldManTable.getSelectionModel().getSelectedItem();
        if (selectedToBedOldMan == null) {
            return;
        }
        String buildingName = buildingList.getSelectionModel().getSelectedItem();
        String roomName = roomList.getSelectionModel().getSelectedItem();
        String bedName = bedList.getSelectionModel().getSelectedItem();
        Bed bed = BuildingDao.getBedByBuildingRoomBedName(buildingName, roomName, bedName);
        if (bed == null) {
            return;
        }
        if (SimpleTools.informationDialog(Alert.AlertType.CONFIRMATION, "提示", "请确认", "您是否将床位分配给该老人")) {
            OldMan param = new OldMan();
            param.setId(selectedToBedOldMan.getId());
            param.setBedId(bed.getId());
            boolean result = OldManDao.updateOldMan(param);
            if (result) {
                oldManInformation.setVisible(true);
                oldManNotInBedPane.setVisible(false);
                oldManInBedPane.setVisible(true);
                oldManInBedName.setText(selectedToBedOldMan.getName());
                oldManInformation.setText(selectedToBedOldMan.oldManDetail());
                BuildingDao.getBuildings();
            } else {
                SimpleTools.informationDialog(Alert.AlertType.ERROR, "提示", "抱歉", "数据异常,请您刷新");
            }
        }


    }

    @FXML
    void oldManOutBed() {
        //选中的病人从床位退出
        String buildingName = buildingList.getSelectionModel().getSelectedItem();
        String roomName = roomList.getSelectionModel().getSelectedItem();
        String bedName = bedList.getSelectionModel().getSelectedItem();
        OldMan oldMan = BuildingDao.getOldManFromBed(buildingName, roomName, bedName);
        boolean result = false;
        if (oldMan != null) {
            if (SimpleTools.informationDialog(Alert.AlertType.CONFIRMATION, "提示", "请确认", "您是否将该老人退床")) {
                result = OldManDao.removeOldManBed(oldMan.getId());
                if (result) {
                    oldManNotInBedPane.setVisible(true);
                    oldManInBedPane.setVisible(false);
                    oldManInformation.setVisible(false);
                    //去数据库查没进床的病人
                    ObservableList<OldMan> list = FXCollections.observableArrayList(OldManDao.getNoBedOldMan());
                    notInBedOldManTable.setItems(null);
                    notInBedOldManTable.setItems(list);
                    BuildingDao.getBuildings();
                } else {
                    SimpleTools.informationDialog(Alert.AlertType.ERROR, "提示", "抱歉", "数据异常,请您刷新");
                }
            }
        }

    }
    /*----------------------------------------------*/

    /*-----------老人管理---------------*/

    @FXML
    private AnchorPane oldManPane;
    @FXML
    private AnchorPane bedPane;
    @FXML
    private Button goBack;
    @FXML
    private Button toAddOldManPageButton;
    @FXML
    private Button toChangeOldManPageButton;
    @FXML
    private Button searchOldManButton;
    @FXML
    private Button deleteOldManButton;
    @FXML
    private Button toPaperAskPageButton;
    @FXML
    private Button backButton;
    @FXML
    private AnchorPane oldManAddPane;
    @FXML
    private TextField oldManName;
    @FXML
    private RadioButton sexButton1;
    @FXML
    private RadioButton sexButton2;
    @FXML
    private AnchorPane oldManHeader;
    @FXML
    private TextField oldManAge;
    @FXML
    private TextField oldManPhone;
    @FXML
    private TextField oldManContactName;
    @FXML
    private TextField oldManContactPhone;
    @FXML
    private TableView<OldMan> oldManTable;
    @FXML
    private TableColumn<OldMan, String> oldManNameColumn;
    @FXML
    private TableColumn<OldMan, String> oldManSexColumn;
    @FXML
    private TableColumn<OldMan, Integer> oldManAgeColumn;
    @FXML
    private TableColumn<OldMan, String> oldManPhoneColumn;
    @FXML
    private TableColumn<OldMan, String> oldManContactNameColumn;
    @FXML
    private TableColumn<OldMan, String> oldManContactPhoneColumn;
    @FXML
    private TableColumn<OldMan, String> oldManRoomColumn;
    @FXML
    private TextField oldManSearch;
    private ObservableList<OldMan> oldMen;
    @FXML
    private Button addOrUpdateOldManButton;

    @FXML
    void oldManManagePage() {

        publishPaperPane.setVisible(false);
        settlePane.setVisible(false);
        paperPane.setVisible(false);
        questionPane.setVisible(false);
        bedPane.setVisible(false);
        paperAnswerPane.setVisible(false);

        oldManPane.setVisible(true);
//        oldManTable.setVisible(true);
        //重新加载一遍数据
        oldMen.clear();
        oldMen.addAll(OldManDao.getOldMen());
        oldManTable.setItems(null);
        oldManTable.setItems(oldMen);

    }

    /**
     * 退出新增病人页面
     */
    @FXML
    void backButton() {

        oldManAddPane.setVisible(false);
        oldManTable.setVisible(true);
        backButton.setVisible(false);
        oldManHeader.setVisible(true);
        //...
    }

    @FXML
    void searchOldMan() {
        //根据搜索框进行搜索
//        System.out.println(oldManSearch.getText());
        oldMen.clear();
        oldMen.addAll(OldManDao.searchOldMan(oldManSearch.getText()));
        oldManTable.setItems(null);
        oldManTable.setItems(oldMen);
    }

    @FXML
    void preAddOldMan() {
        //要增加病人，打开对应的页面
        addOrUpdateOldManButton.setText("添加");

        resetAddForm();

        oldManAddPane.setVisible(true);
        oldManHeader.setVisible(false);
        oldManTable.setVisible(false);
        backButton.setVisible(true);

        //...
    }

    private void resetAddForm() {
        oldManName.clear();
        sexButton1.setSelected(true);
        sexButton2.setSelected(false);
        oldManAge.clear();
        oldManPhone.clear();
        oldManContactName.clear();
        oldManContactPhone.clear();
        oldManName.setPromptText("");
        oldManAge.setPromptText("");
        oldManPhone.setPromptText("");
        oldManContactName.setPromptText("");
        oldManContactPhone.setPromptText("");
    }

    @FXML
    void preUpdateOldMan() {

        OldMan selected = oldManTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            SimpleTools.informationDialog(Alert.AlertType.ERROR, "提示", "抱歉", "你还没有选择病人");
        } else {
            //要更新病人，打开对应的页面
            addOrUpdateOldManButton.setText("更新");
            oldManAddPane.setVisible(true);
            oldManHeader.setVisible(false);
            oldManTable.setVisible(false);
            backButton.setVisible(true);

            Cache.Cookie().put("operateOldManId", selected.getId());
            oldManName.setPromptText(selected.getName());
            if (selected.getSex() == 1)
                sexButton1.setSelected(true);
            else
                sexButton2.setSelected(true);
//        oldManSex.setText(String.valueOf(selected.getSex()));
            oldManAge.setPromptText(String.valueOf(selected.getAge()));
            oldManPhone.setPromptText(selected.getPhone());
            oldManContactName.setPromptText(selected.getContactName());
            oldManContactPhone.setPromptText(selected.getContactPhone());
            //...
        }
    }

    @FXML
    void oldManAdd() {
        //...
        if (Objects.equals(addOrUpdateOldManButton.getText(), "添加")) {
            OldMan oldMan = new OldMan();
            int selected;
            if (sexButton1.isSelected())
                selected = 1;
            else if (sexButton2.isSelected())
                selected = 2;
            else
                selected = 1;
            if (SimpleTools.isEmpty(new String[]{oldManName.getText(), oldManAge.getText(), oldManPhone.getText(),
                    oldManContactName.getText(), oldManContactPhone.getText()}))
                SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "警告", "请输入全部信息");
            else {
                if (!Pattern.matches(RegexUtils.trueName, oldManName.getText()) || !Pattern.matches(RegexUtils.trueName, oldManContactName.getText()))
                    SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "抱歉", "姓名只能输入中文");
                else if (!Pattern.matches(RegexUtils.age, oldManAge.getText()))
                    SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "抱歉", "年龄只能是1-120岁");
                else if (!Pattern.matches(RegexUtils.phone, oldManPhone.getText()) || !Pattern.matches(RegexUtils.phone, oldManContactPhone.getText()))
                    SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "抱歉", "电话号码只能长为11位的数字");
                else {
                    oldMan.setName(oldManName.getText());
                    oldMan.setSex(selected);
                    oldMan.setAge(Integer.valueOf(oldManAge.getText()));
                    oldMan.setPhone(oldManPhone.getText());
                    oldMan.setContactName(oldManContactName.getText());
                    oldMan.setContactPhone(oldManContactPhone.getText());
                    boolean flag = OldManDao.addOldMan(oldMan);

                    //弹出提示,添加成功
                    if (flag) {
                        SimpleTools.informationDialog(Alert.AlertType.INFORMATION, "提示", "", "添加成功！");
                        //添加完毕后，清空这些框框
                        resetAddForm();

                        oldManAddPane.setVisible(false);
                        oldManTable.setVisible(true);
                        backButton.setVisible(false);
                        oldManHeader.setVisible(true);
                        oldMenReload();

                    }
                }
            }

        } else if (Objects.equals(addOrUpdateOldManButton.getText(), "更新")) {
            OldMan selectedOldMan = oldManTable.getSelectionModel().getSelectedItem();
            OldMan oldMan = new OldMan();
//            System.out.println(oldManName.getText());
            Integer id = (Integer) Cache.Cookie().get("operateOldManId");
            oldMan.setId(id);
            int selected = 0;
            if (sexButton1.isSelected())
                selected = 1;
            if (sexButton2.isSelected())
                selected = 2;
            if (!oldManName.getText().equals("") && !Pattern.matches(RegexUtils.trueName, oldManName.getText()) && !Pattern.matches(RegexUtils.trueName, oldManContactName.getText()))
                SimpleTools.informationDialog(Alert.AlertType.WARNING, "抱歉", "字段不合法", "姓名只能输入中文");
            else if (!oldManAge.getText().equals("") && !Pattern.matches(RegexUtils.age, oldManAge.getText()))
                SimpleTools.informationDialog(Alert.AlertType.WARNING, "抱歉", "字段不合法", "年龄只能输入数字且在1-120之间");
            else if (!oldManPhone.getText().equals("") && !Pattern.matches(RegexUtils.phone, oldManPhone.getText()) && !Pattern.matches(RegexUtils.phone, oldManContactPhone.getText()))
                SimpleTools.informationDialog(Alert.AlertType.WARNING, "抱歉", "字段不合法", "电话号码只能是长为11位的数字");
            else {
                oldMan.setName(oldManName.getText());
                if (!selectedOldMan.getSex().equals(selected))
                    oldMan.setSex(selected);
                if (!oldManAge.getText().equals("")) oldMan.setAge(Integer.valueOf(oldManAge.getText()));
                oldMan.setPhone(oldManPhone.getText());
                oldMan.setContactName(oldManContactName.getText());
                oldMan.setContactPhone(oldManContactPhone.getText());
                boolean flag = OldManDao.updateOldMan(oldMan);
                if (flag) {
                    //更新完毕后，清空这些框框
                    resetAddForm();
                    oldManAddPane.setVisible(false);
                    oldManTable.setVisible(true);
                    backButton.setVisible(false);
                    oldManHeader.setVisible(true);
                    //重新加载数据
                    oldMenReload();
                    for (OldMan pa : oldMen) {
                        if (pa.getId().equals(id)) {
                            oldManTable.getSelectionModel().select(pa);
                            break;
                        }
                    }
                }
            }
        }

    }

    private void oldMenReload() {
        oldMen.clear();
        oldMen.addAll(OldManDao.getOldMen());
        oldManTable.setItems(null);
        oldManTable.setItems(oldMen);
    }

    @FXML
    void deleteOldMan() {
        //根据选中的病人删除oldMan
        OldMan selected = oldManTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "警告", "你还没有选择病人");
        } else if (SimpleTools.informationDialog(Alert.AlertType.CONFIRMATION, "提示", "请确认！", "是否删除该老人？")) {
            if (OldManDao.deleteOldMan(selected.getId()))
                oldMenReload();
        }
    }

    @FXML
    void toPaperAskPage() throws Exception {
        //这里跳转去答题页面
        //带选中的oldMan
        OldMan selectedItem = oldManTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "警告", "你还没有选择病人");
            return;
        }
        Cache.Cookie().put("selectedOldMan", selectedItem);
        (new AssessController()).showWindow();
    }

    /**
     * —————————————————————————————————问题管理——————————————————————
     */
    @FXML
    private AnchorPane questionPane;
    @FXML
    private TextField questionText;
    @FXML
    private TextField optionOne;
    @FXML
    private TextField optionTwo;
    @FXML
    private TextField optionThree;
    @FXML
    private TextField optionFour;

    @FXML
    private TableView<Question> questionTable;
    @FXML
    private TableColumn<Question, Label> questionIdColumn;
    @FXML
    private TableColumn<Question, String> questionNameColumn;
    @FXML
    private ListView<String> optionList;
    @FXML
    private Button preUpdateQuestionButton;
    @FXML
    private Button deleteQuestionButton;
    @FXML
    private Button preUpdateOptionButton;
    @FXML
    private AnchorPane questionUpdatePane;
    @FXML
    private AnchorPane questionOperatePane;
    @FXML
    private Label questionUpdateLabel;
    @FXML
    private TextField questionUpdateField;
    @FXML
    private TextField questionSearch;

    private ObservableList<Question> questions = FXCollections.observableArrayList();

    private ObservableList<String> questionOptions;
    /*
        页面切换按钮
     */

    /**
     * 点击后切换到问题管理页面
     */
    @FXML
    void questionManagePage() {

        questions.clear();
        questions.addAll(QuestionDao.getQuestionsFromServer());

        questionPane.setVisible(true);

        settlePane.setVisible(false);
        oldManPane.setVisible(false);
        bedPane.setVisible(false);
        paperPane.setVisible(false);
        publishPaperPane.setVisible(false);
        paperAnswerPane.setVisible(false);

        questionTable.setItems(null);
        questionTable.setItems(questions);

        optionList.setItems(null);
        questionUpdatePane.setVisible(false);
        questionOperatePane.setVisible(false);

    }


    @FXML
    void questionSearch() {
        questions.clear();
        questions.addAll(QuestionDao.searchQuestions(questionSearch.getText()));

        questionTable.setItems(null);
        questionTable.setItems(questions);
    }

    @FXML
    void addQuestion() throws IOException {
//        if(questionText.getText() != null && optionOne.getText(), optionTwo.getText(), optionThree.getText())
        String[] questionOptions = {questionText.getText(), optionOne.getText(), optionTwo.getText(), optionThree.getText(), optionFour.getText()};
        if (SimpleTools.isEmpty(questionOptions)) {
            SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "警告", "请输入全部信息");
            return;
        }
        Set<String> optionsSet = new HashSet<>();
        optionsSet.add(optionOne.getText());
        optionsSet.add(optionTwo.getText());
        optionsSet.add(optionThree.getText());
        optionsSet.add(optionFour.getText());
        if (optionsSet.size() < 4) {
            SimpleTools.informationDialog(Alert.AlertType.ERROR, "提示", "错误", "您设置的选项中有重复选项");
            return;
        }
        Question question = new Question();
        question.setName(questionText.getText());
        Map<String, String> options = new LinkedHashMap<>();
        options.put("A", optionOne.getText());
        options.put("B", optionTwo.getText());
        options.put("C", optionThree.getText());
        options.put("D", optionFour.getText());
        ObjectMapper objectMapper = new ObjectMapper();
        String optionsJsonStr = objectMapper.writeValueAsString(options);
        question.setText(optionsJsonStr);
        boolean flag = QuestionDao.addNewQuestion(question);
        if (flag) {
            //成功
            questionText.setText(null);
            optionOne.setText(null);
            optionTwo.setText(null);
            optionThree.setText(null);
            optionFour.setText(null);
            //更新数据
            questions.clear();
            questions.addAll(QuestionDao.getQuestionsFromServer());
            questionTable.setItems(null);
            questionTable.setItems(questions);

        } else {
            SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "警告", "服务器正忙,您请稍后");
        }
    }

    @FXML
    void preUpdateQuestion() {
        Question selectedItem = questionTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "错误", "您还没有选择问题");
            questionUpdatePane.setVisible(false);
            questionUpdateField.clear();
            questionOperatePane.setVisible(true);
            return;
        }
        questionUpdatePane.setVisible(true);
        questionUpdateLabel.setText("修改问题");
        questionUpdateField.setPromptText(selectedItem.getName());
        questionOperatePane.setVisible(false);

    }

    @FXML
    void preUpdateOption() {
        //...
        String option = optionList.getSelectionModel().getSelectedItem();
        if (SimpleTools.isEmpty(option)) {
            SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "警告", "您还没有选择选项");
            questionUpdatePane.setVisible(false);
            questionUpdateField.clear();
            questionOperatePane.setVisible(true);
            return;
        }
        questionOperatePane.setVisible(false);
        questionUpdatePane.setVisible(true);
        questionUpdateLabel.setText("修改选项");
        questionUpdateField.setPromptText(option);
    }

    @FXML
    void commitQuestionUpdate() throws JsonProcessingException {
        Question selectedItem = questionTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "警告", "您还没有选择问题");
            questionUpdatePane.setVisible(false);
            questionUpdateField.clear();
            questionOperatePane.setVisible(true);
            return;
        }
        if (SimpleTools.isEmpty(questionUpdateField.getText())) {
            SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "警告", "您还没有输入内容");
            return;
        }
        if (selectedItem.getName().equals(questionUpdateField.getText())) {
            SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "警告", "您还没有做出任何修改");
            return;
        }
        Question question = new Question();
        question.setId(selectedItem.getId());

        if (questionUpdateLabel.getText().equals("修改问题")) {
            question.setName(questionUpdateField.getText());

        } else if (questionUpdateLabel.getText().equals("修改选项")) {
            String option = optionList.getSelectionModel().getSelectedItem();
            if (SimpleTools.isEmpty(option)) {
                SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "警告", "您还没有选择选项");
                questionUpdatePane.setVisible(false);
                questionUpdateField.clear();
                questionOperatePane.setVisible(true);
                return;
            }
            if (SimpleTools.isEmpty(questionUpdateField.getText())) {
                SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "警告", "您还没有输入内容");
                return;
            }
            if (option.equals(questionUpdateField.getText())) {
                SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "警告", "您还没有做出任何修改");
                return;
            }
            ObjectMapper objectMapper = new ObjectMapper();

            LinkedHashMap<String, String> options = objectMapper.readValue(selectedItem.getText(), new TypeReference<LinkedHashMap<String, String>>() {
            });
            String key = null;
            for (Map.Entry<String, String> entry : options.entrySet()) {
                if (entry.getValue().equals(option)) {
                    key = entry.getKey();
                    break;
                }
            }
            if (key == null) {
                SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "错误", "出现异常,选项已改变");
                return;
            }
            options.put(key, questionUpdateField.getText());
            //text的json字符串
            question.setText(objectMapper.writeValueAsString(options));

        }

        if (QuestionDao.updateQuestionName(question)) {

            questionUpdatePane.setVisible(false);

            questionOperatePane.setVisible(true);
        } else {
            SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "错误", "服务器异常");
            return;
        }

        if (questionUpdateLabel.getText().equals("修改问题")) {
//            selectedItem.setName(question.getName());
            questions.clear();
            List<Question> questionsFromServer = QuestionDao.getQuestionsFromServer();
            questions.addAll(questionsFromServer);
            for (Question qs : questionsFromServer) {
                if (qs.getId().equals(selectedItem.getId())) {
                    questionTable.getSelectionModel().select(qs);
                    break;
                }
            }


        } else if (questionUpdateLabel.getText().equals("修改选项")) {

            questionOptions.clear();
            List<Question> questionsFromServer = QuestionDao.getQuestionsFromServer();
            for (Question qs : questionsFromServer) {
                if (qs.getId().equals(selectedItem.getId())) {
                    String optionsJsonStr = qs.getText();
                    ObjectMapper objectMapper = new ObjectMapper();
                    LinkedHashMap<String, String> options = objectMapper.readValue(optionsJsonStr, new TypeReference<LinkedHashMap<String, String>>() {
                    });
                    questionOptions.addAll(options.values());
                    optionList.getSelectionModel().select(questionUpdateField.getText());
                    return;
                }
            }
        }
        questionUpdateField.clear();
    }

    @FXML
    void cancelUpdateQuestion() {
        questionUpdatePane.setVisible(false);
        questionUpdateField.clear();
        questionOperatePane.setVisible(true);
    }

    @FXML
    void chooseQuestion() throws IOException {
        Question selectedItem = questionTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            return;
        }
        String optionsJsonStr = selectedItem.getText();
        ObjectMapper objectMapper = new ObjectMapper();
        LinkedHashMap<String, String> options = objectMapper.readValue(optionsJsonStr, new TypeReference<LinkedHashMap<String, String>>() {
        });

        questionOptions = FXCollections.observableArrayList(options.values());

        optionList.setItems(questionOptions);
        questionOperatePane.setVisible(true);
    }


    @FXML
    void deleteQuestion() {
        Question question = questionTable.getSelectionModel().getSelectedItem();
        if (question != null) {
            if (SimpleTools.informationDialog(Alert.AlertType.CONFIRMATION, "提示", null, "您确定要删除该问题吗？")) {
                if (QuestionDao.deleteQuestion(question)) {
                    questions.clear();
                    questions.addAll(QuestionDao.getQuestionsFromServer());
                    optionList.setItems(null);
                    questionTable.setItems(null);
                    questionTable.setItems(questions);
                }
            }
        } else SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "抱歉", "请至少选择一个问题！");
    }

    /*---------------------模板管理---------------------*/
    @FXML
    private AnchorPane paperPane;
    @FXML
    private TableView<Paper> paperTable;
    @FXML
    private TableColumn<Paper, Label> paperIdColumn;
    @FXML
    private TableColumn<Paper, String> paperNameColumn;
    @FXML
    private TableColumn<Paper, String> paperCreateTimeColumn;
    @FXML
    private TextField paperName;

    //这个地方是问卷问题页
    @FXML
    private AnchorPane paperQuestionPane;
    @FXML
    private TableView<Question> addQuestionTable;
    @FXML
    private TableColumn<Question, String> addQuestionColumn;
    @FXML
    private TableView<Question> removeQuestionTable;
    @FXML
    private TableColumn<Question, String> removeQuestionColumn;
    @FXML
    private Label labelTop;
    @FXML
    private Label label1;
    @FXML
    private Label label2;
    @FXML
    private Label label3;
    @FXML
    private Label label4;


    private ObservableList<Paper> papers;
    private ObservableList<PublishPaper> publishPapers;
    private ObservableList<Question> questionsInPaper;
    private ObservableList<Question> questionsNotInPaper;


    /*
        主页面中
     */
    @FXML
    void paperManagePage() {
        papers.clear();
        papers.addAll(PaperDao.getPapersFromServer());

        paperPane.setVisible(true);

        publishPaperPane.setVisible(false);
        settlePane.setVisible(false);
        questionPane.setVisible(false);
        bedPane.setVisible(false);
        oldManPane.setVisible(false);
        paperAnswerPane.setVisible(false);

        paperTable.setItems(null);
        paperTable.setItems(papers);

        addQuestionTable.setItems(null);
        removeQuestionTable.setItems(null);

        paperQuestionPane.setVisible(true);
        labelTop.setText("操作指南");
        label1.setText("1.在左表中选择操作的问卷");
        label2.setText("2.从备用题库选择问题新增");
        label3.setText("3.从已有题库选择问题移除");
        label4.setText("4.点右下的按钮以查看回答");

    }

    /**
     * 选择了某一个Paper,这时候就要准备数据
     */
    @FXML
    void choosePaper() {
        //...
        Paper selectedItem = paperTable.getSelectionModel().getSelectedItem();
        //***
        if (selectedItem == null) {
            return;
        }
        //准备三个数据,根据选中的id
        questionsInPaper = FXCollections.observableArrayList(PaperDao.getQuestionInPaper(selectedItem.getId()));
        questionsNotInPaper = FXCollections.observableArrayList(PaperDao.getQuestionNotInPaper(selectedItem.getId()));
        paperAnswers = FXCollections.observableArrayList();

        //将数据放入table中
        addQuestionTable.setItems(null);
        removeQuestionTable.setItems(null);
        removeQuestionTable.setItems(questionsInPaper);
        addQuestionTable.setItems(questionsNotInPaper);
        labelTop.setText("操作指南");
        label1.setText("1.在左表中选择操作的问卷");
        label2.setText("2.从备用题库选择问题新增");
        label3.setText("3.从已有题库选择问题移除");
        label4.setText("4.点右下的按钮以查看回答");
    }

    @FXML
    void addPaperButton() {
        if (SimpleTools.isEmpty(paperName.getText())) {
            SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "警告", "请输入问卷名称");
            return;
        }
        if (PaperDao.searchPaperName(paperName.getText())) {
            SimpleTools.informationDialog(Alert.AlertType.WARNING, "抱歉", "错误", "该名字已被注册,请重新输入");
            return;
        }
        boolean flag = PaperDao.addNewPaper(paperName.getText());
        if (flag) {
            //清空输入框
            paperName.clear();
            //更新数据
            papers.clear();
            papers.addAll(PaperDao.getPapersFromServer());
            paperTable.setItems(null);
            paperTable.setItems(papers);
        } else {
            SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "警告", "服务器正忙,您请稍后");
        }
    }

    @FXML
    void deletePaperButton() {
        Paper paper = paperTable.getSelectionModel().getSelectedItem();
        if (paper != null) {
            if (SimpleTools.informationDialog(Alert.AlertType.CONFIRMATION, "提示", null, "您确定要删除该问卷吗？")) {
                if (PaperDao.deletePaper(paper.getId())) {
                    papers.clear();
                    papers.addAll(PaperDao.getPapersFromServer());
                    paperTable.setItems(null);
                    paperTable.setItems(papers);
                }
//                else {
////                    SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "错误", "服务器异常");
//                }
            }
        } else SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "抱歉", "请至少选择一个问卷！");
    }

    @FXML
    void changePaperButton() {
        Paper paper = paperTable.getSelectionModel().getSelectedItem();
        if (paper == null) {
            SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "警告", "请选择您想要修改的问卷");
            return;
        }
        if (SimpleTools.isEmpty(paperName.getText()) || paperName.getText().equals(paper.getName())) {
            SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "警告", "您还没有输入要修改的名字");
            return;
        }
        if (PaperDao.searchPaperName(paperName.getText())) {
            SimpleTools.informationDialog(Alert.AlertType.WARNING, "抱歉", "错误", "该名字已被注册,请重新输入");
        } else if (SimpleTools.informationDialog(Alert.AlertType.CONFIRMATION, "提示", null, "您确定要修改该问卷吗？")) {
            Paper param = new Paper();
            param.setId(paper.getId());
            param.setName(paperName.getText());
            if (PaperDao.updatePaperName(param)) {
                //修改完成
                SimpleTools.informationDialog(Alert.AlertType.INFORMATION, "提示", "成功", "修改成功");
                paperName.clear();
                papers.clear();
                List<Paper> papersFromServer = PaperDao.getPapersFromServer();
                papers.addAll(PaperDao.getPapersFromServer());
                paperTable.setItems(null);
                paperTable.setItems(papers);
                for (Paper pa : papersFromServer) {
                    if (pa.getId().equals(paper.getId())) {
                        paperTable.getSelectionModel().select(pa);
                        break;
                    }
                }
            } else {
                SimpleTools.informationDialog(Alert.AlertType.ERROR, "提示", "错误", "数据异常");
            }
        }
    }

    /*
        子页面A:回答页
     */
    @FXML
    void showAnswerDetailButton() {
        //...
        //这里应该弹出一个新页面
    }

    @FXML
    void clickNotInPaperQuestion() {
        Paper selectedPaper = paperTable.getSelectionModel().getSelectedItem();
        //选择问题
        Question selectedQuestion = addQuestionTable.getSelectionModel().getSelectedItem();
        //判断是否选择了问卷
        if (selectedPaper != null) {
            //判断是否选择了问题
            if (selectedQuestion != null) {
                //显示问题的详情
                preSeeQuestion(selectedQuestion);

//          如果没有选择问题，则显示警告
            } else {
                SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "警告", "请先选择问题");
            }

            //如果没有选择问卷，则显示警告
        } else {
            SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "警告", "请先选择问卷");
        }
    }

    @FXML
    void clickInPaperQuestion() {
        //选择问卷
        Paper selectedPaper = paperTable.getSelectionModel().getSelectedItem();
        //选择问题
        Question selectedQuestion = removeQuestionTable.getSelectionModel().getSelectedItem();
        //判断是否选择了问卷
        if (selectedPaper != null) {
            //判断是否选择了问题
            if (selectedQuestion != null) {
                //显示问题的详情
                preSeeQuestion(selectedQuestion);

//          如果没有选择问题，则显示警告
            } else {
                SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "警告", "请先选择问题");
            }

            //如果没有选择问卷，则显示警告
        } else {
            SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "警告", "请先选择问卷");
        }
    }

    @FXML
    void removeQuestionFromPaperButton() {

        //选择问卷
        Paper selectedPaper = paperTable.getSelectionModel().getSelectedItem();
        //选择问题
        Question selectedQuestion = removeQuestionTable.getSelectionModel().getSelectedItem();
        //判断是否选择了问卷
        if (selectedPaper != null) {
            //判断是否选择了问题
            if (selectedQuestion != null) {
                //从问卷中移除问题
                if (SimpleTools.informationDialog(Alert.AlertType.CONFIRMATION, "提示", null, "您确定要从问卷中移除该问题吗？")) {
                    boolean flag = PaperDao.removeQuestionFromPaper(selectedPaper.getId(), selectedQuestion.getId());
                    if (flag) {

                        SimpleTools.informationDialog(Alert.AlertType.INFORMATION, "提示", "成功", "移除成功");

                        //重新加载问卷中的问题
                        questionsInPaper.clear();
                        questionsInPaper.addAll(PaperDao.getQuestionInPaper(selectedPaper.getId()));
                        removeQuestionTable.setItems(null);
                        removeQuestionTable.setItems(questionsInPaper);

                        //重新加载不在问卷中的问题
                        questionsNotInPaper.clear();

                        questionsNotInPaper.addAll(PaperDao.getQuestionNotInPaper(selectedPaper.getId()));
                        addQuestionTable.setItems(null);
                        addQuestionTable.setItems(questionsNotInPaper);
                    }
                }
                //如果没有选择问题，则显示警告
            } else {
                SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "警告", "请先从现有题库中选择问题");
            }

            //如果没有选择问卷，则显示警告
        } else {
            SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "警告", "请先选择问卷");
        }
    }

    private void preSeeQuestion(Question selectedQuestion) {
        labelTop.setText("问题详情");
        String text = selectedQuestion.getText();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            HashMap<String, String> optionsMap = objectMapper.readValue(text, new TypeReference<HashMap<String, String>>() {
            });
            label1.setText("A：" + optionsMap.get("A"));
            label2.setText("B：" + optionsMap.get("B"));
            label3.setText("C：" + optionsMap.get("C"));
            label4.setText("D：" + optionsMap.get("D"));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addQuestionToPaperButton() {
        //选择问卷
        Paper selectedPaper = paperTable.getSelectionModel().getSelectedItem();
        //选择问题
        Question selectedQuestion = addQuestionTable.getSelectionModel().getSelectedItem();
        //判断是否选择了问卷
        if (selectedPaper != null) {
            //判断是否选择了问题
            if (selectedQuestion != null) {
                //从问卷中移除问题
                if (SimpleTools.informationDialog(Alert.AlertType.CONFIRMATION, "提示", null, "您确定要向问卷模板中添加该问题吗？")) {
                    boolean flag = PaperDao.addQuestionToPaper(selectedPaper.getId(), selectedQuestion.getId());
                    if (flag) {

                        SimpleTools.informationDialog(Alert.AlertType.INFORMATION, "提示", "成功", "添加成功");

                        //重新加载问卷中的问题
                        questionsInPaper.clear();
                        questionsInPaper.addAll(PaperDao.getQuestionInPaper(selectedPaper.getId()));
                        removeQuestionTable.setItems(null);
                        removeQuestionTable.setItems(questionsInPaper);

                        //重新加载不在问卷中的问题
                        questionsNotInPaper.clear();
                        questionsNotInPaper.addAll(PaperDao.getQuestionNotInPaper(selectedPaper.getId()));
                        addQuestionTable.setItems(null);
                        addQuestionTable.setItems(questionsNotInPaper);
                    }
                }
                //如果没有选择问题，则显示警告
            } else {
                SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "警告", "请先从备用题库中选择问题");
            }

            //如果没有选择问卷，则显示警告
        } else {
            SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "警告", "请先选择问卷模板");
        }
    }

    @FXML
    void makePaperButton() {
        //选择问卷模板
        Paper selectedPaper = paperTable.getSelectionModel().getSelectedItem();
        String publishPaperName = paperName.getText().trim();
//        selectedPaper.getQuestionList();
        //判断是否选择了模板
        if (selectedPaper != null) {
            if (publishPaperName.length() < 5) {
                SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "警告", "请在问卷名称框中输入大于5个字符的发布问卷名");
                return;
            }
            if (SimpleTools.informationDialog(Alert.AlertType.CONFIRMATION, "提示", null, "您确定要根据该模板制作问卷吗？")) {
                selectedPaper.setName(publishPaperName);
                if (PaperDao.makePaper(selectedPaper)) {
                    SimpleTools.informationDialog(Alert.AlertType.INFORMATION, "提示", "成功", "请前往问卷管理页面查看！");
                }
            }
        } else {
            SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "警告", "请先选择问卷模板");
        }

    }


    /**
     * 这个地方是问卷回答页
     */

    @FXML
    private AnchorPane paperAnswerPane;
    @FXML
    private TableView<Answer> answerQuestionTable;
    @FXML
    private TableColumn<Answer, String> answerDateColumn;
    @FXML
    private TableColumn<Answer, String> answerOldManColumn;
    @FXML
    private TableColumn<Answer, String> answerPaperColumn;
    @FXML
    private TableColumn<Answer, Integer> answerPaperVersionColumn;
    @FXML
    private TableColumn<Answer, Button> answerOperatorColumn;

    private ObservableList<Answer> paperAnswers;

    @FXML
    void answerManagePage() {


        paperAnswerPane.setVisible(true);

        publishPaperPane.setVisible(false);
        paperPane.setVisible(false);
        settlePane.setVisible(false);
        questionPane.setVisible(false);
        bedPane.setVisible(false);
        oldManPane.setVisible(false);

        paperAnswers.clear();
        paperAnswers.addAll(PaperDao.getAllAnswers());

        answerQuestionTable.setItems(null);
        answerQuestionTable.setItems(paperAnswers);
    }
    /*
        子页面B:问卷问题页
     */


    @FXML
    void changeToAnswerPaneButton() {
        //B隐藏
        paperQuestionPane.setVisible(false);
        //A出现
        paperAnswerPane.setVisible(true);
    }


    /*---------------------问卷管理---------------------*/
    @FXML
    private AnchorPane publishPaperPane;
    @FXML
    private TableView<PublishPaper> publishPaperTable;
    @FXML
    private TableColumn<PublishPaper, Label> publishPaperIdColumn;
    @FXML
    private TableColumn<PublishPaper, String> publishPaperNameColumn;
    @FXML
    private TableColumn<PublishPaper, String> publishPaperTimeColumn;
    @FXML
    private TableColumn<PublishPaper, String> publishPaperEndTimeColumn;
    @FXML
    private TableColumn<PublishPaper, Integer> publishPaperVersionColumn;
    @FXML
    private TableColumn<PublishPaper, String> publishPaperTypeColumn;
    @FXML
    private TableColumn<PublishPaper, Button> publishPaperUpdateColumn;
    @FXML
    private TableColumn<PublishPaper, Button> publishPaperShowColumn;


    @FXML
    void publishPaperManagePage() {

        publishPaperPane.setVisible(true);

        paperPane.setVisible(false);
        settlePane.setVisible(false);
        questionPane.setVisible(false);
        bedPane.setVisible(false);
        oldManPane.setVisible(false);
        paperAnswerPane.setVisible(false);

        publishPapers.clear();
        publishPapers.addAll(PaperDao.getAllPublishPaper());
        publishPaperTable.setItems(null);
        publishPaperTable.setItems(publishPapers);
    }


    /**
     * 函数接口  改版源自从整数类型改为字符类型
     */
    Function<Integer, String> sexFunction = sex -> {
        switch (sex) {
            case 1:
                return "男";
            case 2:
                return "女";
            default:
                return "未知";
        }
    };


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        User user = (User) Cache.Cookie().get("User");
        nameLabel.setText(user.getName());

        //设置单选
        ToggleGroup tg = new ToggleGroup();
        sexButton1.setToggleGroup(tg);
        sexButton2.setToggleGroup(tg);

        //获取数据
        oldMen = FXCollections.observableArrayList(OldManDao.getOldMen());
        questions = FXCollections.observableArrayList(QuestionDao.getQuestionsFromServer());
        papers = FXCollections.observableArrayList(PaperDao.getPapersFromServer());

        paperAnswers = FXCollections.observableArrayList();

        oldManTable.setItems(oldMen);
        questionTable.setItems(questions);
        paperTable.setItems(papers);

        //加载老人的表格
        oldManNameColumn.setCellValueFactory(oldManList -> new SimpleStringProperty(oldManList.getValue().getName()));
        oldManSexColumn.setCellValueFactory(oldManList -> new SimpleStringProperty(SimpleTools.IntegerValueToString(oldManList.getValue().getSex(), sexFunction)));
        oldManAgeColumn.setCellValueFactory(oldManList -> new SimpleObjectProperty<>(oldManList.getValue().getAge()));
        oldManPhoneColumn.setCellValueFactory(oldManList -> new SimpleStringProperty(oldManList.getValue().getPhone()));
        oldManContactNameColumn.setCellValueFactory(oldManList -> new SimpleStringProperty(oldManList.getValue().getContactName()));
        oldManContactPhoneColumn.setCellValueFactory(oldManList -> new SimpleStringProperty(oldManList.getValue().getContactPhone()));
        oldManRoomColumn.setCellValueFactory(oldManList -> new SimpleStringProperty(oldManList.getValue().getLoadingName()));

        //加载问题的表格
//        questionIdColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        questionNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        questionIdColumn.setCellFactory(cellData -> new TableCell<Question, Label>() {
            @Override
            protected void updateItem(Label item, boolean empty) {
                super.updateItem(item, empty);
                Label label = new Label();
                label.setText(String.valueOf(getIndex() + 1));
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(label);
                }
            }
        });

        //加载问卷模板的表格
        paperNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
//        paperIdColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        paperIdColumn.setCellFactory(cellData -> new TableCell<Paper, Label>() {
            @Override
            protected void updateItem(Label item, boolean empty) {
                super.updateItem(item, empty);
                Label label = new Label();
                label.setText(String.valueOf(getIndex() + 1));
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(label);
                }
            }
        });
        paperCreateTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCreateTime()));
        //初始化
        questionsInPaper = FXCollections.observableArrayList();
        questionsNotInPaper = FXCollections.observableArrayList();

        addQuestionTable.setItems(questionsInPaper);
        removeQuestionTable.setItems(questionsNotInPaper);

        addQuestionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        removeQuestionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

        //初始化回答表格
        answerDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCreateTime()));
        answerPaperColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPublishPaperName()));
        answerOldManColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOldManName()));
        answerPaperVersionColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPublishPaperVersion()));
        answerOperatorColumn.setCellFactory(cellData -> new TableCell<Answer, Button>() {
            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                Button detailButton = new Button("答案详情");
                detailButton.setStyle("-fx-background-color: #00bcff;-fx-text-fill: #ffffff");
                detailButton.setOnAction(event -> {
                    Answer selectAnswer = paperAnswers.get(getIndex());
                    if (PaperDao.getPublishPaperById(selectAnswer.getPublishPaperId())) {
                        Cache.Cookie().put("showPublishPaperStatus", "查看回答");
                        Cache.Cookie().put("currentAnswerDetail", selectAnswer.getAnswer());
                        try {
                            (new PaperDetailController()).showWindow();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(detailButton);
                }
            }
        });

        //病人入床的表格
        notInBedOldManNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

        //发布问卷模块初始化
        publishPapers = FXCollections.observableArrayList();
        publishPaperTable.setItems(publishPapers);
//        publishPaperIdColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        publishPaperIdColumn.setCellFactory(cellData -> new TableCell<PublishPaper, Label>() {
            @Override
            protected void updateItem(Label item, boolean empty) {
                super.updateItem(item, empty);
                Label label = new Label();
                label.setText(String.valueOf(getIndex() + 1));
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(label);
                }
            }
        });

        publishPaperNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        publishPaperTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus() == 2 ? cellData.getValue().getRegisterTime() : "暂未发布,时间待定"));
        publishPaperTypeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus() == 2 ? "已发布" : "准备中"));
        publishPaperEndTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus() == 2 ? cellData.getValue().getEndTime() : "暂未发布,时间待定"));
        publishPaperVersionColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getVersion()));
        publishPaperUpdateColumn.setCellFactory(cellData -> new TableCell<PublishPaper, Button>() {
            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                Button updateButton = new Button("更新状态");
                updateButton.setStyle("-fx-background-color: #00bcff;-fx-text-fill: #ffffff");
                updateButton.setOnAction(event -> {
                    PublishPaper selectedPaper = publishPapers.get(getIndex());
                    if (SimpleTools.informationDialog(Alert.AlertType.CONFIRMATION, "提示", "请确认", "确定要改变问卷的状态吗？")) {
                        PublishPaper paper = new PublishPaper();
                        paper.setId(selectedPaper.getId());
                        paper.setStatus(selectedPaper.getStatus() == 1 ? 2 : 1);
                        if (!PaperDao.updatePublishPaperStatus(paper)) {
                            SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "错误", "更新失败，服务器异常,请稍后再试");
                            return;
                        }
                        SimpleTools.informationDialog(Alert.AlertType.INFORMATION, "提示", "成功", "更新成功");
                        publishPapers.clear();
                        publishPapers.addAll(PaperDao.getAllPublishPaper());
                        publishPaperTable.setItems(null);
                        publishPaperTable.setItems(publishPapers);
                    }

                });
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(updateButton);
                }
            }
        });

        publishPaperShowColumn.setCellFactory(cellData -> new TableCell<PublishPaper, Button>() {
            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                Button showButton = new Button("查看详情");
                showButton.setStyle("-fx-background-color: #00bcff;-fx-text-fill: #ffffff");
                showButton.setOnAction(event -> {
                    PublishPaper selectedPaper = publishPapers.get(getIndex());

                    if (!PaperDao.getPublishPaperById(selectedPaper.getId())) {
                        SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "错误", "服务器异常,请稍后再试");
                        return;
                    }
                    try {
                        Cache.Cookie().put("showPublishPaperStatus", "查看问卷");
                        (new PaperDetailController()).showWindow();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(showButton);
                }
            }

        });
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/StaffController.fxml")));
        primaryStage.setTitle("颐养社区");
        primaryStage.setScene(new Scene(root, 1200, 720));
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
