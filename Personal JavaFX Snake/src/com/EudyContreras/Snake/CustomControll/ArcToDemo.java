package com.EudyContreras.Snake.CustomControll;

import com.EudyContreras.Snake.Utilities.FillUtility;
import com.EudyContreras.Snake.Utilities.TimePeriod;
import com.EudyContreras.Snake.Utilities.ValueAnimator;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class ArcToDemo extends Application {

	   public static void main(String[] args) {
	        launch(args);
	    }

	@Override
	public void start(Stage primaryStage) {
		Color color = Color.ORANGE;
//		Arc base = new Arc();
//	    base.setFill(null);
//
//	    base.setRadiusX(50);
//	    base.setRadiusY(50);
//
//	    base.setCenterX((130)/2);
//	    base.setCenterY((130)/2);
//
//	    base.setStrokeWidth(20);
//	    base.setStroke(Color.ORANGE.deriveColor(0, 0.7, 1, 0.7));
//	    base.setStartAngle(270);
//	    base.setLength(0);
		
		Rectangle base = new Rectangle();
		base.setWidth(20);
		base.setHeight(20);
		base.setFill(color.deriveColor(0, 0.6, 1, 1));
		base.setArcHeight(10);
		base.setArcWidth(10);
		base.setTranslateX(130/2 - 20/2);
		base.setTranslateY(130-20);
	    
	    Arc arc2 = new Arc();
	    arc2.setFill(null);

	    arc2.setRadiusX(50);
	    arc2.setRadiusY(50);

	    arc2.setCenterX((130)/2);
	    arc2.setCenterY((130)/2);

	    arc2.setStrokeWidth(10);
	    arc2.setStroke(color.saturate());
	    arc2.setStartAngle(270);
	    arc2.setLength(0);

	    
	    Arc arc = new Arc();
	    arc.setFill(null);

	    arc.setRadiusX(60);
	    arc.setRadiusY(60);

	    arc.setCenterX((130)/2);
	    arc.setCenterY((130)/2);

	    arc.setStrokeWidth(10);
	    arc.setStroke(color.deriveColor(0, 1, 1, 0.7));
	    arc.setStartAngle(270);
	    arc.setLength(0);
	    
	    Arc arc1 = new Arc();
	    arc1.setFill(null);

	    arc1.setRadiusX(65);
	    arc1.setRadiusY(65);

	    arc1.setCenterX((130)/2);
	    arc1.setCenterY((130)/2);

	    arc1.setStrokeWidth(5);
	    arc1.setStroke(color);
	    arc1.setStartAngle(270);
	    arc1.setLength(0);


	    ValueAnimator animator = new ValueAnimator();
	    animator.setDelay(500);
	    animator.setDuration(TimePeriod.seconds(3));
	    animator.setFrom(0);
	    animator.setTo(-360);
	    animator.onUpdate(e -> {
	    	arc.setLength(e);
	    	arc1.setLength(e);
	    	arc2.setLength(e);
	    });
	    animator.setOnFinished(()->{
	    	arc2.setLength(-300);
	    });
	    animator.play();
//	    arc.lengthProperty().bind(value.subtract(minValue).divide(maxValue.subtract(minValue)).multiply(-maxRange));

	    Pane pane = new Pane();
	    pane.setBackground(FillUtility.PAINT_FILL(Color.WHITE));


	    pane.setMinSize(130,130);
	    pane.setMaxSize(130,130);
	    pane.getChildren().addAll(arc,arc2,arc1);
//	    pane.setStyle("-fx-background-color: black");


	    Scene scene = new Scene(new StackPane(pane));


	    primaryStage.setScene(scene);
	    primaryStage.show();
	}
}