package com.EudyContreras.Snake.PlayRoomHub;

import com.EudyContreras.Snake.PlayRoomHub.ConnectLabel.Style;
import com.EudyContreras.Snake.Utilities.Interpolators;
import com.EudyContreras.Snake.Utilities.ResizeAnimator;
import com.EudyContreras.Snake.Utilities.ScaleAnimator;
import com.EudyContreras.Snake.Utilities.TimePeriod;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


public class TestWindow extends Application {

    public void start(Stage stage) throws Exception {


    	VBox box = new VBox(50);
    	box.setAlignment(Pos.CENTER);

    	Rectangle rect = new Rectangle(100,100);
    	rect.setFill(Color.RED);

    	Rectangle rect2 = new Rectangle(100,100);
    	rect2.setFill(Color.RED);

    	ScaleAnimator anim = new ScaleAnimator(ResizeAnimator.RESIZE_WIDTH_HEIGHT);
    	anim.setNode(rect);
    	anim.setDuration(TimePeriod.seconds(1));
    	anim.setInterpolator(Interpolators.getEasingInstance(0,0.1f));
    	anim.setFromX(0);
    	anim.setFromY(0);
    	anim.setToX(2.0);
    	anim.setToY(2.0);

    	ResizeAnimator anim2 = new ResizeAnimator(ResizeAnimator.RESIZE_WIDTH);
    	anim2.setNode(rect2);
    	anim2.setDuration(TimePeriod.seconds(1));
    	anim2.setInterpolator(Interpolators.getEasingInstance(.5f,.5f));
    	anim2.setStartWidth(0);
    	anim2.setStartHeight(0);
    	anim2.setEndWidth(600);
    	anim2.setEndHeight(100);

    	box.getChildren().addAll(rect);

        StackPane root = new StackPane();

        ConnectUsers users = new ConnectUsers(null);

        StatusIndicator circle = new StatusIndicator(140);

        ConnectLeaderboard leaderBoard = new ConnectLeaderboard(null);

        ConnectLabel label = new ConnectLabel(null,"Hellow",40, 1000, 800, Style.BLUE_STYLE);
        label.setFrameGradient(Color.GRAY,Color.SILVER.brighter(),Color.GRAY);
        label.setTextSize(50);

        root.getChildren().addAll(box);

        stage.setTitle("JavaFX Test Window");
        stage.setScene(new Scene(root,1900,1000, Color.BLACK));
        stage.show();
        anim.play();
        anim2.play();
    }

    public static void main(String[] args) {
        launch(args);
    }

}