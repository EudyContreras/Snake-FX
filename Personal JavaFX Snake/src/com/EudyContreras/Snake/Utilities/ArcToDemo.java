package com.EudyContreras.Snake.Utilities;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class ArcToDemo extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {

        StackPane root = new StackPane();
        primaryStage.setResizable(true);
        primaryStage.setScene(new Scene(root,500,500));

        ObservableList<Circle> list = FXCollections.observableArrayList();

        for(int i = 0; i<10; i++){
        	  Circle circle = new Circle(10);
              circle.setCenterX(i+12);
              circle.setTranslateY(i*12);
              circle.setFill(Color.GREEN);
              root.getChildren().add(circle);
        }

        final long startNanoTime = System.nanoTime();

		primaryStage.show();

		int i = 0;
		new AnimationTimer() {

			public void handle(long currentNanoTime) {
//				i++;

				double t = (currentNanoTime - startNanoTime) / 1000000000.0;

				t = t * 3;
				double radius = 14;

				for(Node node: root.getChildren()){
					Circle circle = (Circle)node;
					double x = (circle.getRadius()+i + circle.getCenterX()*i) * Math.cos(t) * (radius);
					double y = (circle.getRadius()+i + circle.getCenterY()*i) * Math.sin(t) * (radius);

					circle.setTranslateX(x);
					circle.setTranslateY(y);
				}

			}
		}.start();

    }

    public static void main(String[] args) {
        launch(args);
    }
}