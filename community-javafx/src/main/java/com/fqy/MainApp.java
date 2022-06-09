package com.fqy;

import com.fqy.controller.LogFrameController;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }


    /**
     *  首先 JavaFx 它的兼容性比其他模板引擎要好。使用传统 HTML 前端技术，需要考虑到不同浏览器内核的兼容性，
     *
     *  根据不同的浏览器编写不同的代码，但 JavaFx 不同，它基于 Java 技术，使用 JVM 虚拟机运行，兼容性问题已经由不同平台版本的 JVM 解决。
     *
     *  而且对于我来说，它和后端使用同样的Java语言，开发效率更高。
     *
     *  而且相较于基于浏览器内核的客户端技术，如 Electron，它虽然可以做到使用Web端同样的页面技术，即可封装为客户端，
     *
     *  它存在因集成了浏览器内核而导致的运行内存消耗高，存储空间占用大的问题，相较于JavaFx没有明显的优势。
     *
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        LogFrameController logFrameController = new LogFrameController();
        Stage stage = new Stage();
        logFrameController.start(stage);
    }
}
