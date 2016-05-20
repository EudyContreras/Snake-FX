package com.SnakeGame.HudElements;

import com.SnakeGame.FrameWork.GameLoader;
import com.SnakeGame.FrameWork.Settings;
import com.SnakeGame.FrameWork.SnakeGame;
import com.SnakeGame.ImageBanks.GameImageBank;

import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class ScoreKeeper {

	public static int APPLE_COUNT = 50;
	private SnakeGame game;
	private ImageView apple;
	private Rectangle board;
	private Boolean startTimer = false;
	private String secondsS = "00";
	private String minutesS = "00";
	private String hoursS = "00";
	private Text text;
	private Text timer;
	private Font font;
	private boolean swipeUp = true;
	private boolean swipeDown = false;
	private float swipeSpeed = 0;
	private double width = 0;
	private float x = 0;
	private float y = 0;
	private float y1 = 0;
	private float oldY = 0;
	private float position = 0;
	private int initialAmount;
	private int counter;
	private int seconds = 00;
	private int minutes = 00;
	private int hours = 00;

	public ScoreKeeper(SnakeGame game, int count, double x1, double y1, double x, double y, double width,
			double height) {
		APPLE_COUNT = count;
		this.initialAmount = count;
		this.game = game;
		this.text = new Text();
		this.timer = new Text();
		this.apple = new ImageView(GameImageBank.apple);
		this.setX((float) x);
		this.setY((float) y);
		this.oldY = (float) y;
		this.y1 = (float) y1;
		this.setWidth(width);
		this.board = new Rectangle(x, y, width, height);
		this.font = Font.font("Helvetica", FontWeight.BOLD, 18 / GameLoader.ResolutionScaleX);
		this.board.setFill(new ImagePattern(GameImageBank.countKeeper));
		this.apple.setFitWidth(35 / GameLoader.ResolutionScaleX);
		this.apple.setFitHeight(35 / GameLoader.ResolutionScaleY);
		this.apple.setPreserveRatio(true);
		this.apple.setX(Settings.WIDTH/2+apple.getFitWidth());
		this.apple.setY(y1-15);
		this.text.setX(apple.getX() + apple.getFitWidth()/2+10);
		this.text.setY(y1-15);
		this.text.setFill(Color.RED);
		this.text.setEffect(null);
		this.text.setFont(font);
		this.text.setText(" x " + APPLE_COUNT);
		this.timer.setX(SnakeGame.ScaleX((int) (x+width/2.6	)));
		this.timer.setY(y1-15);
		this.timer.setFill(Color.RED);
		this.timer.setFont(font);
		this.timer.setText("00:00:00");
		game.getOverlay().getChildren().add(board);
		game.getOverlay().getChildren().add(apple);
		game.getOverlay().getChildren().add(text);
		game.getOverlay().getChildren().add(timer);
		startTimer();
		updateCount();
	}

	public void decreaseCount() {
		APPLE_COUNT -= 1;
		updateCount();
	}
	public void update() {
		y = y + swipeSpeed;
		y1 = y1 + swipeSpeed;
		if (swipeDown) {
			swipeSpeed = 1.5f;
			if (y >= 85) {
				swipeSpeed = 0;
			}
		}
		if (swipeUp) {
			swipeSpeed = -1.5f;
			if (y < oldY) {
				swipeSpeed = 0;
			}
		}
		board.setY(y);
		apple.setY(y1-5);
		text.setTranslateY(y1);
		timer.setTranslateY(y1);
	}

	public void showHide() {
		if (swipeDown) {
			swipeDown = false;
			swipeUp = true;
		} else if (swipeUp) {
			swipeUp = false;
			swipeDown = true;
		}
	}

	public void swipeDown() {
		swipeUp = false;
		swipeDown = true;
	}

	public void swipeUp() {
		swipeDown = false;
		swipeUp = true;
	}
	public void updateTimer() {
		if (!VictoryScreen.LEVEL_COMPLETE && startTimer) {
			counter += 1;
			if (counter > 59) {
				seconds += 1;
				counter = 0;
			}
			if (seconds > 59) {
				minutes += 1;
				seconds = 0;
			}
			if (minutes > 59) {
				hours += 1;
				minutes = 0;
			}
			if (seconds < 10) {
				secondsS = "0" + seconds;
			} else if (seconds >= 10) {
				secondsS = "" + seconds;
			}
			if (minutes < 10) {
				minutesS = "0" + minutes;
			} else if (minutes >= 10) {
				minutesS = "" + minutes;
			}
			if (hours < 10) {
				hoursS = "0" + hours;
			} else if (hours >= 10) {
				hoursS = "" + hours;
			}
			this.timer.setText(hoursS + ":" + minutesS + ":" + secondsS);
		}
		else{
			stopTimer();
		}

	}
	public void startTimer() {
		startTimer = true;
	}
	public void stopTimer() {
		startTimer = false;
	}
	public void resetTimer() {
		startTimer = true;
		seconds = 0;
		minutes = 0;
		hours = 0;
	}

	public void keepCount() {
		updateTimer();
		update();
		if (APPLE_COUNT <= 0) {
			if (VictoryScreen.LEVEL_COMPLETE == false) {
				game.getVictoryScreen().removeBoard();
				game.getVictoryScreen().finishLevel();
				setPosition(1.5f);
				swipeDown();
				game.getGameHud().swipeDown();
				VictoryScreen.LEVEL_COMPLETE = true;
			}
		}
		else {
			swipeUp();
			game.getGameHud().swipeUp();
		}
	}

	public void updateCount() {
		text.setText(" x " + APPLE_COUNT);
	}

	public void resetCount() {
		APPLE_COUNT = initialAmount;
		text.setText(" x " + APPLE_COUNT);
	}
	public void setPosition(float position) {
		this.position = position;
	}
	public float getPosition() {
		return position;
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
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}

}
