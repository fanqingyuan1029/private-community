package com.fqy.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fqy.cookie.Cache;
import com.fqy.dao.PaperDao;
import com.fqy.entity.OldMan;
import com.fqy.entity.paper.*;
import com.fqy.utils.SimpleTools;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

public class AssessController extends Application implements Initializable {
    public Label totalLabel1;
    Stage stage = new Stage();
    private int index = 1;

    /**
     * ————————————————————————回答问卷————————————————————
     */
    @FXML
    private AnchorPane answerPaperPane;
    @FXML
    private Label title;
    @FXML
    private RadioButton radioButton1;
    @FXML
    private RadioButton radioButton2;
    @FXML
    private RadioButton radioButton3;
    @FXML
    private RadioButton radioButton4;
    @FXML
    private ImageView goButton;
    @FXML
    private ImageView backButton;
    @FXML
    private Label indexLabel;
    @FXML
    private Label totalLabel;
    @FXML
    private AnchorPane choosePaperPane;
    @FXML
    private Button finishButton;

    @FXML
    void back() {
        if (index == 1) {
            goButton.setVisible(true);
            backButton.setVisible(false);
            return;
        }

        if (index == 2) {
            goButton.setVisible(true);
            backButton.setVisible(false);
            index--;
            loadQuestion();
            return;
        }
        goButton.setVisible(true);
        backButton.setVisible(true);
        index--;
        //
        loadQuestion();
    }

    @FXML
    void go() {
        if (index == publishQuestions.size()) {
            goButton.setVisible(false);
            backButton.setVisible(true);
            return;
        }

        if (index == publishQuestions.size() - 1) {
            goButton.setVisible(false);
            backButton.setVisible(true);
            index++;
            loadQuestion();
            return;
        }
        goButton.setVisible(true);
        backButton.setVisible(true);
        index++;
        //
        loadQuestion();
    }

    @FXML
    void finishPaper() throws JsonProcessingException {
        if (SimpleTools.informationDialog(Alert.AlertType.CONFIRMATION, "提示", null, "确定要结束吗")) {
            ObjectMapper objectMapper = new ObjectMapper();
            String answerJsonStr = objectMapper.writeValueAsString(answerIndexList);
            Answer answer = new Answer();
            OldMan selectedOldMan = (OldMan) Cache.Cookie().get("selectedOldMan");

            answer.setPublishPaperId(paperTable.getSelectionModel().getSelectedItem().getId());
            answer.setOldManId(selectedOldMan.getId());
            answer.setPublishPaperVersion(paperTable.getSelectionModel().getSelectedItem().getVersion());
            answer.setAnswer(answerJsonStr);
            boolean flag = PaperDao.answerPaper(answer);
            if (flag) {
                SimpleTools.informationDialog(Alert.AlertType.INFORMATION, "提示", null, "提交成功,请在问卷管理页面查看作答记录！");
                ((Stage) finishButton.getScene().getWindow()).close();
            } else
                SimpleTools.informationDialog(Alert.AlertType.ERROR, "提示", "错误", "提交未成功,请在稍后重试！");
        }
    }

    /**
     * ————————————————————————显示问卷表格————————————————————
     */

    @FXML
    private TableView<PublishPaper> paperTable;
    @FXML
    private TableColumn<PublishPaper, Integer> paperIdColumn;
    @FXML
    private TableColumn<PublishPaper, String> paperNameColumn;
    @FXML
    private TableColumn<PublishPaper, String> paperCreateTimeColumn;
    @FXML
    private TableColumn<PublishPaper, String> paperEndTimeColumn;
    @FXML
    private TableColumn<PublishPaper, String> paperStatusColumn;
    @FXML
    private TableColumn<PublishPaper, Integer> paperVersionColumn;
    @FXML
    private Label selectedOldManName;


    private List<PublishQuestion> publishQuestions;
    private int[] answerIndexList;

    @FXML
    void startPaper() throws JsonProcessingException {
        PublishPaper paper = paperTable.getSelectionModel().getSelectedItem();
        if (paper == null) {
            SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "警告", "请先选择问卷");
            return;
        }
        if (paper.getStatus() == 1) {
            SimpleTools.informationDialog(Alert.AlertType.WARNING, "提示", "警告", "这份问卷暂时还没准备好！请换一份已就绪的");
            return;
        }
        if (SimpleTools.informationDialog(Alert.AlertType.CONFIRMATION, "提示", null, "确定要开始吗")) {
//            publishQuestions = ;
            ObjectMapper objectMapper = new ObjectMapper();
            String detailJson = paper.getDetail();
            publishQuestions = objectMapper.readValue(detailJson, new TypeReference<List<PublishQuestion>>() {
            });

            int size = publishQuestions.size();
            answerIndexList = new int[size];
            //将数组中每一项都置为 给定值(0)
            Arrays.fill(answerIndexList, 0);
            loadQuestion();

            totalLabel.setText(String.valueOf(size));
            indexLabel.setText(String.valueOf(index));
            choosePaperPane.setVisible(false);
            answerPaperPane.setVisible(true);
            backButton.setVisible(false);
        }
    }

    private void loadQuestion() {
        if (index < 1) {
            return;
        }

        PublishQuestion currentQuestion = publishQuestions.get(index - 1);
        List<String> options = currentQuestion.getOptions();

        title.setText(currentQuestion.getName());
        radioButton1.setText(options.size() > 0 ? options.get(0) : "");
        radioButton2.setText(options.size() > 1 ? options.get(1) : "");
        radioButton3.setText(options.size() > 2 ? options.get(2) : "");
        radioButton4.setText(options.size() > 3 ? options.get(3) : "");

        radioButton1.setSelected(false);
        radioButton2.setSelected(false);
        radioButton3.setSelected(false);
        radioButton4.setSelected(false);

        switch (answerIndexList[index - 1]) {
            case 1:
                radioButton1.setSelected(true);
                break;
            case 2:
                radioButton2.setSelected(true);
                break;
            case 3:
                radioButton3.setSelected(true);
                break;
            case 4:
                radioButton4.setSelected(true);
                break;
        }

        indexLabel.setText(String.valueOf(index));
    }


    @FXML
    void optionClick() {
        if (radioButton1.isSelected())
            answerIndexList[index - 1] = 1;
        else if (radioButton2.isSelected())
            answerIndexList[index - 1] = 2;
        else if (radioButton3.isSelected())
            answerIndexList[index - 1] = 3;
        else if (radioButton4.isSelected())
            answerIndexList[index - 1] = 4;

        for (int selectIndex : answerIndexList) {
            if (selectIndex == 0) {
                return;
            }
        }
        finishButton.setVisible(true);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ObservableList<PublishPaper> publishPapers = FXCollections.observableArrayList();

        ToggleGroup tg = new ToggleGroup();
        radioButton1.setToggleGroup(tg);
        radioButton2.setToggleGroup(tg);
        radioButton3.setToggleGroup(tg);
        radioButton4.setToggleGroup(tg);
        publishPapers.clear();
        publishPapers.addAll(PaperDao.getAllPublishPaper());
        paperIdColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        paperNameColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getName()));
        paperStatusColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getStatus() == 2 ? "已发布" : "准备中"));
        paperCreateTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRegisterTime()));
        paperEndTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEndTime()));
        paperVersionColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getVersion()));
        paperTable.setItems(publishPapers);
        OldMan selectedOldMan = (OldMan) Cache.Cookie().get("selectedOldMan");
        selectedOldManName.setText(selectedOldMan.getName());
        choosePaperPane.setVisible(true);
        answerPaperPane.setVisible(false);
        finishButton.setVisible(false);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/AssessController.fxml")));
        primaryStage.setTitle("评估");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 800, 700));
        primaryStage.getIcons().add(new Image("file:community-javafx/src/main/resources/resource/flour.png"));
        primaryStage.showAndWait();
    }

    public void showWindow() throws Exception {
        start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
