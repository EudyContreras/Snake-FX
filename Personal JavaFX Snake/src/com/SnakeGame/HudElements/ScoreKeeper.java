package com.SnakeGame.HudElements;

import com.SnakeGame.FrameWork.GameLoader;
import com.SnakeGame.FrameWork.Settings;
import com.SnakeGame.FrameWork.SnakeGame;
import com.SnakeGame.ImageBanks.GameImageBank;
import com.SnakeGame.Utilities.ImageUtility;

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
	public SnakeGame game;
	public ImageView apple;
	public Rectangle board;
	public Text text;
	public Text timer;
	public Font timerFont;
	public Font font;
	private float x = 0;
	private float y = 0;
	private float y1 = 0;
	private float y2 = 0;
	private float oldY = 0;
	private float position = 0;
	private double width = 0;
	private int initialAmount;
	private int counter;
	private int seconds = 00;
	private int minutes = 00;
	private int hours = 00;
	private String secondsS = "00";
	private String minutesS = "00";
	private String hoursS = "00";

	public ScoreKeeper(SnakeGame game, int count, double x1, double y1, double x, double y, double width,
			double height) {
		APPLE_COUNT = count;
		this.initialAmount = count;
		this.game = game;
		this.text = new Text();
		this.timer = new Text();
		this.apple = new ImageView(new Image(ImageUtility.loadResource("apple.png")));
		this.setX((float) x);
		this.y = (float) y;
		this.y2 = (float) (y);
		this.oldY = (float) y;
		this.y1 = (float) y1;
		this.setWidth(width);
		this.board = new Rectangle(x, y, width, height);
		this.timerFont = Font.font("Helvetica", FontWeight.BOLD, 25 / GameLoader.ResolutionScaleX);
		this.font = Font.font("Helvetica", FontWeight.BOLD, 25 / GameLoader.ResolutionScaleX);
		this.board.setFill(new ImagePattern(GameImageBank.countKeeper));
		this.apple.setFitWidth(50 / GameLoader.ResolutionScaleX);
		this.apple.setFitHeight(50 / GameLoader.ResolutionScaleY);
		this.apple.setPreserveRatio(true);
		this.apple.setX(Settings.WIDTH / 2);
		this.apple.setY(y2);
		this.text.setTranslateX(apple.getX()+apple.getFitWidth());
		this.text.setTranslateY(y1);
		this.text.setFill(Color.RED);
		this.text.setEffect(null);
		this.text.setFont(font);
		this.text.setText(" X " + APPLE_COUNT);
		this.timer.setTranslateX(Settings.WIDTH/2-120);
		this.timer.setTranslateY(y1);
		this.timer.setFill(Color.rgb(255, 120, 0));
		this.timer.setFont(timerFont);
		this.timer.setText("00:00:00");
		game.getOverlay().getChildren().add(board);
		game.getOverlay().getChildren().add(apple);
		game.getOverlay().getChildren().add(text);
		game.getOverlay().getChildren().add(timer);
		updateCount();
	}

	public void decreaseCount() {
		APPLE_COUNT -= 1;
		updateCount();
	}
	public void updateTimer(){
		counter+=1;
		if(counter>59){
			seconds+=1;
			counter = 0;
		}
		if(seconds>59){
			minutes+=1;
			seconds = 0;
		}
		if(minutes>59){
			hours+=1;
			minutes = 0;
		}
		if(seconds<10){
			secondsS="0"+seconds;
		}
		else if(seconds>=10){
			secondsS = ""+seconds;
		}
		if(minutes<10){
			minutesS="0"+minutes;
		}
		else if(minutes>=10){
			minutesS = ""+minutes;
		}
		if(hours<10){
			hoursS="0"+hours;
		}
		else if(hours>=10){
			hoursS = ""+hours;
		}
		this.timer.setText(hoursS+":"+minutesS+":"+secondsS);


	}
	public void keepCount() {
		updateTimer();
		y = y + getPosition();
		y1 = y1 + getPosition();
		y2 = y2 + getPosition();
		text.setTranslateY(y1);
		timer.setTranslateY(y1);
		board.setY(y);
		apple.setY(y2);
		if (APPLE_COUNT <= 0) {
			if (VictoryScreen.LEVEL_COMPLETE == false) {
				game.getVictoryScreen().removeBoard();
				game.getVictoryScreen().finishLevel();
				game.getGameHud().swipeDown();
				setPosition(1.4f);
				VictoryScreen.LEVEL_COMPLETE = true;
			}
		} else {
			game.getGameHud().swipeUp();
		}
		if (y >= game.getGameHud().height-10) {
			setPosition(0);
		}
		if (game.getGameHud().swipeUp) {
			setPosition(-1.4f);
			if (y <= oldY) {
				setPosition(0);
			}
		}
	}

	public void updateCount() {
		text.setText(" X " + APPLE_COUNT);
	}

	public void resetCount() {
		APPLE_COUNT = initialAmount;
		text.setText(" X " + APPLE_COUNT);
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getPosition() {
		return position;
	}

	public void setPosition(float position) {
		this.position = position;
	}
}
