package com.EudyContreras.Snake.PlayRoomHub;

import com.EudyContreras.Snake.PlayRoomHub.ConnectLabel.Style;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class TestWindow extends Application {

    public void start(Stage stage) throws Exception {


        StackPane root = new StackPane();

        ConnectUsers users = new ConnectUsers(null);

        StatusIndicator circle = new StatusIndicator(140);
        
        ConnectLeaderboard leaderBoard = new ConnectLeaderboard(null);

        ConnectLabel label = new ConnectLabel(null,"Hellow",40, 200, 100, Style.BLUE_STYLE);
        label.setFrameGradient(Color.GRAY,Color.SILVER.brighter(),Color.GRAY);
        label.setTextSize(50);

        root.getChildren().add(leaderBoard.get());

        stage.setTitle("JavaFX Test Window");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}