package com.EudyContreras.Snake.CustomNodes ;

import com.EudyContreras.Snake.CustomControll.CustomProgressIndicator;
import com.EudyContreras.Snake.Utilities.TimePeriod;
import com.EudyContreras.Snake.Utilities.ValueAnimator;

import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SemiDemo extends Application {

    @Override
    public void start(Stage primaryStage) {

    	SimpleDoubleProperty progress = new SimpleDoubleProperty();

    	CustomProgressIndicator indicator = new CustomProgressIndicator(100, Color.ORANGE);

//    	indicator.setVisible(true);
//    	indicator.progressProperty().bind(progress);
//
//    	ValueAnimator animator = new ValueAnimator();
//	    animator.setDelay(500);
//	    animator.setDuration(TimePeriod.seconds(3));
//	    animator.setFrom(0);
//	    animator.setTo(1);
//	    animator.onUpdate(e -> {
//	    	progress.set(e);
//	    });
//
//	    animator.play();
    	indicator.setIndeterminate(true);

    	StackPane root = new StackPane(indicator.get());

        Scene scene = new Scene(root, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}