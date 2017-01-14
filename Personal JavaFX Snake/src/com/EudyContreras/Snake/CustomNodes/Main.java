package com.EudyContreras.Snake.CustomNodes;

import java.util.stream.IntStream;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
//  w w  w  .j  a  v a 2s . com
public class Main extends Application {
    @Override
    public void start(Stage stage) {
    	 IntStream.range(0, 5).forEach(i ->{
	        	System.out.println(i);
	        });
    }
    public static void main(String[] args) {
        launch(args);
    }
}
