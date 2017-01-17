package com.EudyContreras.Snake.Utilities;

import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class VariableRateAnimation extends Application {

    private Animation anim ;

    @Override
    public void start(Stage primaryStage) {
        Pane mainPane = new Pane();
        Rectangle rect = new Rectangle(100.0, 10.0);
        mainPane.getChildren().add(rect); //so the rectangle is on screen
        rect.setOnMouseClicked(e -> {

            if (anim != null && anim.getStatus() == Status.RUNNING) {
                System.out.println("Paused (" + anim.getTotalDuration().subtract(anim.getCurrentTime())+ " remaining)");
                anim.pause();
            } else {
                Duration duration = Duration.seconds(30.0 * rect.getWidth() / (100 * Math.random() * 5.0));
                System.out.println("Starting: ("+duration+ " to go)");
                double currentWidth = rect.getWidth() ;
                if (anim != null) {
                    anim.stop();
                }
                anim = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(rect.widthProperty(), currentWidth, Interpolator.LINEAR)),
                        new KeyFrame(duration, new KeyValue(rect.widthProperty(), 0.0, Interpolator.LINEAR)));
                anim.play();
            }
        });

        primaryStage.setScene(new Scene(mainPane, 600, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}