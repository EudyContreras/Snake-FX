package com.EudyContreras.Snake.CustomNodes ;

import com.EudyContreras.Snake.Utilities.FillUtility;
import com.EudyContreras.Snake.Utilities.Interpolators;
import com.EudyContreras.Snake.Utilities.ResizeAnimation;
import com.EudyContreras.Snake.Utilities.TimePeriod;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TestBench extends Application {

    private static final Duration ANIMATION_DURATION = Duration.seconds(0.4);

	public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        stage.setTitle("Table View Sample");
        stage.setWidth(900);
        stage.setHeight(500);

        ResizeAnimation resize = new ResizeAnimation(ResizeAnimation.RESIZE_HEIGHT);
//        Rectangle rect = new Rectangle(300,300);
        Pane rect = new Pane();
        rect.setBackground(FillUtility.PAINT_FILL(Color.RED));
        rect.setPrefHeight(300);
        rect.setPrefWidth(300);


        resize.setRegion(rect);
        resize.setStartHeight(rect.getPrefHeight());
        resize.setEndHeight(0);
        resize.setOnFinished(()->{
        	System.out.println("Done");
        });
        resize.setInterpolator(Interpolators.getEasingInstance());
        resize.setDuration(TimePeriod.millis(3000));

        StackPane pane = new StackPane(rect);


        final Scene scene = new Scene(pane);


        stage.setScene(scene);
        stage.show();

        resize.play();
    }


}