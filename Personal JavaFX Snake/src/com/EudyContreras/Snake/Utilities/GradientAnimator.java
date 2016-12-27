package com.EudyContreras.Snake.Utilities;

import java.util.Random;

import com.EudyContreras.Snake.ThreadManagers.ThreadManager;
import com.EudyContreras.Snake.Utilities.ShapeUtility.Shape;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class GradientAnimator {

	private int changeOne;
	private int changeTwo;
	private int valueOne;
	private int valueTwo;
	private volatile boolean running;
	private Rectangle rect;
	private Circle circle;
	private Random rand;


	public GradientAnimator() {
		this(null,null);
	}

	public GradientAnimator(Node node, Duration duration){
		this.rand = new Random();
		this.setNode(node);
		this.resetAnim();
	}

	private void resetAnim(){
		this.changeOne = 1;
		this.changeTwo = 1;
		this.valueOne =  getRandomRange(100,215);
		this.valueTwo =  getRandomRange(100,215);
	}

	public void setNode(Node node){
		if(node instanceof Circle){
			circle = ((Circle)(node));
			circle.setFill(ShapeUtility.LINEAR_GRADIENT(getColor(valueOne),getColor(valueTwo)));
		}
		if(node instanceof Rectangle){
			rect = ((Rectangle)(node));
			rect.setFill(ShapeUtility.LINEAR_GRADIENT(getColor(valueOne),getColor(valueTwo)));
		}
	}

	private void colorAnimation(){
		clampColors();
		if(rect!=null){
			rect.setFill(ShapeUtility.LINEAR_GRADIENT(getColor(valueOne),getColor(valueTwo)));
		}
		else{
			circle.setFill(ShapeUtility.LINEAR_GRADIENT(getColor(valueOne),getColor(valueTwo)));
		}
	}

	private int getRandomRange(int minValue, int maxValue){
		return rand.nextInt(maxValue + 1 - minValue) + minValue;
	}

	private Color getColor(int color){
		return Color.rgb(color, color, color);
	}

	private void clampColors(){
		valueOne+=changeOne;
		if(valueOne>215){
			valueOne = 215;
			changeOne = -1;
		}if(valueOne<100){
			valueOne = 100;
			changeOne = 1;
		}

		valueTwo+=changeTwo;
		if(valueTwo>215){
			valueTwo = 215;
			changeTwo = -1;
		}if(valueTwo<100){
			valueTwo = 100;
			changeTwo = 1;
		}
	}

	public void play(){
		if (!running) {
			running = true;
			ThreadManager.performeScript(new Runnable() {
				@Override
				public void run() {

					while (running) {
						Platform.runLater(() -> {
							colorAnimation();
						});
						try {
							Thread.sleep(26);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			});
		}
	}

	public void stop(){
		running = false;
		resetAnim();
	}

	public enum AnimatedColor{
		SILVER, GOLD, RED, BLUE, CYAN, ORANGE, YELLOW
	}
}
