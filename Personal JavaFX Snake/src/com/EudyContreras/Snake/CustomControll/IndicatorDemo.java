package com.EudyContreras.Snake.CustomControll;

import com.EudyContreras.Snake.Utilities.TimePeriod;
import com.EudyContreras.Snake.Utilities.ValueAnimator;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class IndicatorDemo extends Application {

	   public static void main(String[] args) {
	        launch(args);
	    }

	@Override
	public void start(Stage primaryStage) {
		CustomProgressIndicator indicator = new CustomProgressIndicator(40,Color.DODGERBLUE);
	    ValueAnimator animator = new ValueAnimator();
	    
		indicator.setIndeterminate(true);
		indicator.setVisible(true);
		
	    animator.setDuration(TimePeriod.seconds(3));
	    animator.setDelay(500);
	    animator.setFrom(0);
	    animator.setTo(1);
	    animator.onUpdate(e -> {
	    	indicator.progressProperty().set(e);
	    });
	    
	    animator.setOnFinished(()->{

	    });
	    animator.play();


	    Scene scene = new Scene(new StackPane(indicator.get()));


	    primaryStage.setScene(scene);
	    primaryStage.show();
	}
}