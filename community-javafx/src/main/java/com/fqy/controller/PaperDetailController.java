package com.fqy.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fqy.cookie.Cache;
import com.fqy.entity.paper.PublishPaper;
import com.fqy.entity.paper.PublishQuestion;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class PaperDetailController extends Application implements Initializable {
    Stage primaryStage = new Stage();

    @FXML
    private ScrollPane paperDetail;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/PaperDetailController.fxml")));
        primaryStage.setTitle("颐养社区");
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.getIcons().add(new Image("file:community-javafx/src/main/resources/resource/flour.png"));
        primaryStage.show();


    }

    private void loadPaper() {
        try {
            PublishPaper publishPaper = (PublishPaper) Cache.Cookie().get("currentPublishPaper");
            if (publishPaper == null) {
                primaryStage.close();
                return;
            }
            ObjectMapper objectMapper = new ObjectMapper();
            List<PublishQuestion> questions = objectMapper.readValue(publishPaper.getDetail(), new TypeReference<List<PublishQuestion>>() {
            });


            ImageView imageView = new ImageView();
            imageView.setImage(new Image("file:src/main/java/com/fqy/resource/图片6.png"));
            imageView.setFitWidth(paperDetail.getPrefViewportWidth());
            imageView.setFitHeight(paperDetail.getPrefViewportHeight());
            VBox vBox = new VBox();
            int index = 0;
            for (PublishQuestion question : questions) {
                AnchorPane anchorPane = new AnchorPane();
                anchorPane.setPrefHeight(520);
                anchorPane.setPrefWidth(580);
                Label titleLabel = new Label();
                Label optionALabel = new Label();
                Label optionBLabel = new Label();
                Label optionCLabel = new Label();
                Label optionDLabel = new Label();
                index++;
                titleLabel.setText(index + "." + question.getName());
                titleLabel.setWrapText(true);
                titleLabel.setFont(new Font("KaiTi", 20));
                titleLabel.setPrefWidth(570);
                titleLabel.setLayoutX(10);
                titleLabel.setLayoutY(10);

                optionALabel.setText(question.getOptions().size() > 0 ? "A: " + question.getOptions().get(0) : "没有这个选项");
                optionALabel.setWrapText(true);
                optionALabel.setFont(new Font("KaiTi", 20));
                optionALabel.setPrefWidth(560);
                optionALabel.setLayoutX(20);
                optionALabel.setLayoutY(110);

                optionBLabel.setText(question.getOptions().size() > 1 ? "B: " + question.getOptions().get(1) : "没有这个选项");
                optionBLabel.setWrapText(true);
                optionBLabel.setFont(new Font("KaiTi", 20));
                optionBLabel.setPrefWidth(560);
                optionBLabel.setLayoutX(20);
                optionBLabel.setLayoutY(210);

                optionCLabel.setText(question.getOptions().size() > 2 ? "C: " + question.getOptions().get(2) : "没有这个选项");
                optionCLabel.setWrapText(true);
                optionCLabel.setFont(new Font("KaiTi", 20));
                optionCLabel.setPrefWidth(560);
                optionCLabel.setLayoutX(20);
                optionCLabel.setLayoutY(310);

                optionDLabel.setText(question.getOptions().size() > 3 ? "D: " + question.getOptions().get(3) : "没有这个选项");
                optionDLabel.setWrapText(true);
                optionDLabel.setFont(new Font("KaiTi", 20));
                optionDLabel.setPrefWidth(560);
                optionDLabel.setLayoutX(20);
                optionDLabel.setLayoutY(410);

                anchorPane.getChildren().add(titleLabel);
                anchorPane.getChildren().add(optionALabel);
                anchorPane.getChildren().add(optionBLabel);
                anchorPane.getChildren().add(optionCLabel);
                anchorPane.getChildren().add(optionDLabel);

                BackgroundImage backgroundImage = new BackgroundImage(
                        new Image("file:src/main/java/com/fqy/resource/图片6.png"),
                        null,
                        null,
                        null,
                        null
                );
                Background background = new Background(backgroundImage);
                anchorPane.setBackground(background);

                vBox.getChildren().add(anchorPane);
            }
//        paperDetail.setContent(imageView);
            paperDetail.setContent(vBox);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void loadPaperAndAnswer() {
        try {
            PublishPaper publishPaper = (PublishPaper) Cache.Cookie().get("currentPublishPaper");
            if (publishPaper == null) {
                primaryStage.close();
                return;
            }
            ObjectMapper objectMapper = new ObjectMapper();
            List<PublishQuestion> questions = objectMapper.readValue(publishPaper.getDetail(), new TypeReference<List<PublishQuestion>>() {
            });


            ImageView imageView = new ImageView();
            imageView.setImage(new Image("file:src/main/java/com/fqy/resource/图片6.png"));
            imageView.setFitWidth(paperDetail.getPrefViewportWidth());
            imageView.setFitHeight(paperDetail.getPrefViewportHeight());
            VBox vBox = new VBox();
            int index = 0;
            String answersStr = String.valueOf(Cache.Cookie().get("currentAnswerDetail"));
            List<Integer> answers = objectMapper.readValue(answersStr, new TypeReference<List<Integer>>() {
            });
            for (PublishQuestion question : questions) {
                AnchorPane anchorPane = new AnchorPane();
                anchorPane.setPrefHeight(520);
                anchorPane.setPrefWidth(580);
                Label titleLabel = new Label();
                Label optionALabel = new Label();
                Label optionBLabel = new Label();
                Label optionCLabel = new Label();
                Label optionDLabel = new Label();
                index++;
                titleLabel.setText(index + "." + question.getName());
                titleLabel.setWrapText(true);
                titleLabel.setFont(new Font("KaiTi", 20));
                titleLabel.setPrefWidth(570);
                titleLabel.setLayoutX(10);
                titleLabel.setLayoutY(10);

                optionALabel.setText(question.getOptions().size() > 0 ? "A: " + question.getOptions().get(0) : "没有这个选项");
                optionALabel.setWrapText(true);
                optionALabel.setFont(new Font("KaiTi", 20));
                optionALabel.setPrefWidth(560);
                optionALabel.setLayoutX(20);
                optionALabel.setLayoutY(110);
                if (answers.size() > (index - 1) && answers.get(index - 1).equals(1))
                    optionALabel.setStyle("-fx-background-color: red");

                optionBLabel.setText(question.getOptions().size() > 1 ? "B: " + question.getOptions().get(1) : "没有这个选项");
                optionBLabel.setWrapText(true);
                optionBLabel.setFont(new Font("KaiTi", 20));
                optionBLabel.setPrefWidth(560);
                optionBLabel.setLayoutX(20);
                optionBLabel.setLayoutY(210);
                if (answers.size() > (index - 1) && answers.get(index - 1).equals(2))
                    optionBLabel.setStyle("-fx-background-color: red");

                optionCLabel.setText(question.getOptions().size() > 2 ? "C: " + question.getOptions().get(2) : "没有这个选项");
                optionCLabel.setWrapText(true);
                optionCLabel.setFont(new Font("KaiTi", 20));
                optionCLabel.setPrefWidth(560);
                optionCLabel.setLayoutX(20);
                optionCLabel.setLayoutY(310);
                if (answers.size() > (index - 1) && answers.get(index - 1).equals(3))
                    optionCLabel.setStyle("-fx-background-color: red");

                optionDLabel.setText(question.getOptions().size() > 3 ? "D: " + question.getOptions().get(3) : "没有这个选项");
                optionDLabel.setWrapText(true);
                optionDLabel.setFont(new Font("KaiTi", 20));
                optionDLabel.setPrefWidth(560);
                optionDLabel.setLayoutX(20);
                optionDLabel.setLayoutY(410);
                if (answers.size() > (index - 1) && answers.get(index - 1).equals(4))
                    optionDLabel.setStyle("-fx-background-color: red");

                anchorPane.getChildren().add(titleLabel);
                anchorPane.getChildren().add(optionALabel);
                anchorPane.getChildren().add(optionBLabel);
                anchorPane.getChildren().add(optionCLabel);
                anchorPane.getChildren().add(optionDLabel);

                BackgroundImage backgroundImage = new BackgroundImage(
                        new Image("file:src/main/java/com/fqy/resource/图片6.png"),
                        null,
                        null,
                        null,
                        null
                );
                Background background = new Background(backgroundImage);
                anchorPane.setBackground(background);

                vBox.getChildren().add(anchorPane);
            }
//        paperDetail.setContent(imageView);
            paperDetail.setContent(vBox);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void showWindow() throws Exception {
        start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Object status = Cache.Cookie().get("showPublishPaperStatus");
        if (status == null) {
            primaryStage.close();
            return;
        }
        if (status.equals("查看问卷")) {
            loadPaper();
        } else if (status.equals("查看回答")) {
            loadPaperAndAnswer();
        } else {
            primaryStage.close();
        }

    }
}
