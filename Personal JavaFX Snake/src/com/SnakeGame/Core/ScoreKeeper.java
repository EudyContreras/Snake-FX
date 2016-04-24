package com.SnakeGame.Core;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class ScoreKeeper {

	public static int APPLE_COUNT = 50;
	public  SnakeGame game;
	public  ImageView apple;
	public  Rectangle board;
	public  Text text;
	public  Font font;
	float x = 0;
	float y = 0;
	float x1 = 0;
	float y1 = 0;
	float y2 = 0;
	float oldY = 0;
	float oldY1 = 0;
	float oldY2 = 0;
	float position = 0;
	double width = 0;
	double height = 0;
	float delay = 0;
	float confirmX = 0;
	float confirmXPosition = 0;
	float accelaration = 0.3f;
	boolean swipeRight = false;
	boolean goLeft = false;
	boolean goRight = true;
	boolean center = true;
	int initialAmount;
	public ScoreKeeper(SnakeGame game, int count, double x1, double y1,double x, double y, double width, double height){
		APPLE_COUNT = count;
		this.initialAmount = count;
		this.game = game;
		this.text = new Text();
    	this.apple = new ImageView(new Image(ImageUtility.loadResource("apple.png")));
		this.x = (float) x;
		this.y = (float) y;
		this.oldY = (float) y;
		this.x1 = (float) x1;
		this.y1 = (float) y1;
		this.oldY1 = (float) y1;
		this.width = width;
		this.height = height;
		this.board = new Rectangle(x,y,width,height);
		this.font = Font.font( "Helvetica", FontWeight.BOLD, 20/GameLoader.ResolutionScaleX );
		this.board.setFill(new ImagePattern(GameImageBank.countKeeper));
		this.apple.setFitWidth(40/GameLoader.ResolutionScaleX);
		this.apple.setFitHeight(40/GameLoader.ResolutionScaleY);
		this.apple.setPreserveRatio(true);
		this.apple.setX(Settings.WIDTH/2-apple.getFitWidth()/2-20);
		this.apple.setY(y2);
		this.text.setTranslateX(x1);
		this.text.setTranslateY(y1);
		this.text.setFill(Color.RED);
		this.text.setEffect(null);
		this.text.setFont(font);
		this.text.setText(" X " +APPLE_COUNT);
		game.getOverlay().getChildren().add(board);
		game.getOverlay().getChildren().add(apple);
		game.getOverlay().getChildren().add(text);
		updateCount();
	}
	public  void decreaseCount(){
		APPLE_COUNT-=1;
		updateCount();
	}
	public  void keepCount(){
		y = y + position;
		y1 = y1 + position;
		y2 = y2 + position;
		text.setTranslateY(y1);
		board.setY(y);
		apple.setY(y2);
		if(APPLE_COUNT<=0){
			if(VictoryScreen.LEVEL_COMPLETE == false){
				game.getVictoryScreen().removeBoard();
				game.getVictoryScreen().finishLevel();
				position = 1.5f;
				game.getGameHud().swipeDown();
				VictoryScreen.LEVEL_COMPLETE = true;
			}
		}
		else{
			game.getGameHud().swipeUp();
		}
		if(y>=game.getGameHud().height){
			position = 0;
		}
		if(game.getGameHud().swipeUp){
			position = -1.5f;
			if(y<=oldY){
				position = 0;
//				y = oldY;
//				y1 = oldY1;
//				y2 = 0;
			}
		}
	}
	public void updateCount(){
		text.setText(" X " + APPLE_COUNT);
	}
	public void resetCount() {
		APPLE_COUNT = initialAmount;
		text.setText(" X " + APPLE_COUNT);
//		y = oldY;
//		y1 = oldY1;
//		y2 = 0;
	}
}
