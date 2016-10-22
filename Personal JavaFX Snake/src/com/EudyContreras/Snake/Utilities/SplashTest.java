package com.EudyContreras.Snake.Utilities;

import java.util.Random;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SplashTest extends Application{

	private int itemsToLoad = 310000;
	private TilePane root;
	private Stage mainWindow;
	private Scene scene;
	private Image splash_image = new Image("http://fxexperience.com/wp-content/uploads/2010/06/logo.png");

	public void start(Stage stage) throws Exception {
		new SplashScreen(stage,splash_image,()-> initializeGame(),()->showGame());
	}

	private void initializeGame() {
		Circle[] circles = new Circle[itemsToLoad];
		root = new TilePane();
		for (int i = 0; i < circles.length; i++) {
			circles[i] = new Circle(5, randomColor());
		}
		root.getChildren().addAll(circles);
	}

	private void showGame() {
		scene = new Scene(root, 1280, 720, Color.BLACK);
		mainWindow = new Stage();
		mainWindow.setFullScreen(false);
		mainWindow.setScene(scene);
		mainWindow.initStyle(StageStyle.DECORATED);
		mainWindow.show();
	}

	private Color randomColor() {
		return Color.rgb(0, getRandomColor(0, 125), getRandomColor(180, 255));
	}

	public int getRandomColor(int minValue, int maxValue) {
		Random rand = new Random();
		int color = rand.nextInt(maxValue + 1 - minValue) + minValue;
		return color;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
