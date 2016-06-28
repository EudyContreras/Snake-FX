package com.EudyContreras.Snake.HUDElements;

import com.EudyContreras.Snake.FrameWork.GameManager;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Class which represents a score keeper. This class keeps track of the game
 * score and perform events according to the kept records.
 *
 * @author Eudy Contreras
 *
 */
public class GameTimer {

	private TimerType timerType;
	private Runnable script;
	private DropShadow dropShadowOne;
	private StackPane timerBox;
	private TimerWatch timerWatch;
	private String secondsS = "00";
	private String minutesS = "00";
	private String hoursS = "00";
	private Text timerText;
	private boolean startTimer = false;
	private double size = 00;
	private int counter = 0;
	private int seconds = 00;
	private int minutes = 00;
	private int hours = 00;


	public GameTimer(GameManager game, double width, double height, double size, TimerStyle style, Color background) {
		this.size = GameManager.ScaleX_Y(size);
		this.timerText = new Text();
		this.timerBox = new StackPane();
		this.timerBox.setPrefSize(GameManager.ScaleX(width), GameManager.ScaleY(height));
		this.timerBox.setBackground(new Background(new BackgroundFill(background, null, null)));
		this.timerBox.getChildren().add(timerText);
		setupText(style);
	}

	/**
	 * O Method which initializes various text related elements which are used
	 * by this class and all the sub elements of those texts.
	 */
	private void setupText(TimerStyle style) {
		this.timerText.setFont(Font.font(null, FontWeight.EXTRA_BOLD, size));
		this.timerText.setText("00:00:00");
		this.dropShadowOne = new DropShadow();
		this.dropShadowOne.setRadius(25);
		this.dropShadowOne.setSpread(0.3);
		this.dropShadowOne.setBlurType(BlurType.TWO_PASS_BOX);
		this.setStyle(style);
	}

	public void setStyle(TimerStyle style){
		if (style == TimerStyle.BLUE_STYLE) {
			this.timerText.setStyle(Styles.BLUE_STYLE);
			this.dropShadowOne.setColor(Color.DODGERBLUE);
		}
		if (style == TimerStyle.GREEN_STYLE){
			this.timerText.setStyle(Styles.GREEN_STYLE);
			this.dropShadowOne.setColor(Color.GREEN);
		}
		if (style == TimerStyle.ORANGE_STYLE){
			this.timerText.setStyle(Styles.ORANGE_STYLE);
			this.dropShadowOne.setColor(Color.DARKORANGE);
		}
		if (style == TimerStyle.RED_STYLE){
			this.timerText.setStyle(Styles.RED_STYLE);
			this.dropShadowOne.setColor(Color.RED);
		}
		this.timerText.setEffect(dropShadowOne);
	}
	public void setTimer(int seconds, int minutes, int hours, TimerType timer) {
		timerWatch = null;
		timerWatch = new TimerWatch(seconds, minutes, hours);
		resetTimer();
		timerType = timer;
		startTimer = true;
	}

	public void resetTimer() {
		startTimer = false;
		secondsS = "00";
		minutesS = "00";
		hoursS = "00";
		if(timerWatch!=null){
		seconds = timerWatch.getSeconds();
		minutes = timerWatch.getMinutes();
		hours = timerWatch.getHours();
		}
		timerText.setText(hoursS + ":" + minutesS + ":" + secondsS);
	}

	/**
	 * Method which updates all the UI elements of this Timer class along with
	 * the logic behind the timer count douwn and count up methods
	 */

	public void updateUI() {
		updateCountUp();
		updateCountDown();
	}

	/**
	 * Method which when called makes a count up sequence on the timer.
	 */
	private void updateCountUp() {
		if (!VictoryScreen.LEVEL_COMPLETE && startTimer && timerType == TimerType.countUp_timer) {
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
			this.timerText.setText(hoursS + ":" + minutesS + ":" + secondsS);
		}
	}

	/**
	 * Method which when called makes a count down sequence on the timer.
	 */
	private void updateCountDown() {
		if (!VictoryScreen.LEVEL_COMPLETE && startTimer && timerType == TimerType.countDown_timer) {
			counter -= 1;
			if (counter < 0) {
				seconds -= 1;
				counter = 59;
			}
			if (seconds < 0 && minutes > 0) {
				minutes -= 1;
				seconds = 59;
			} else if(seconds<=0 && minutes<=0){
			    startTimer = false;
				performEvent();
			}
			if (minutes < 0) {
				hours -= 1;
				minutes = 59;
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
			this.timerText.setText(hoursS + ":" + minutesS + ":" + secondsS);
		}
	}

	/**
	 * Method which starts the game timer
	 */
	public void startTimer() {
		startTimer = true;
	}

	/**
	 * Method which stops the game timer
	 */
	public void stopTimer() {
		startTimer = false;
	}

	/**
	 * Method which resets the game timer
	 */
	public void setCountDown() {
		timerType = TimerType.countDown_timer;
	}

	public void setCountUp() {
		timerType = TimerType.countUp_timer;
	}

	public void setEndTimeEvent(Runnable script) {
		this.script = script;
	}

	private void performEvent() {
		if (script != null) {
			script.run();
		}
	}
	public void showTimer(boolean state){
		this.timerBox.setVisible(state);
	}
	public void setLocation(double x, double y) {
		timerBox.setTranslateX(x);
		timerBox.setTranslateY(y);
	}

	public void setWidth(double width) {
		timerBox.setPrefWidth(width);
	}

	public void setHeight(double height) {
		timerBox.setPrefHeight(height);
	}

	public double getWidth() {
		return timerBox.getPrefWidth();
	}

	public double getHeight() {
		return timerBox.getPrefHeight();
	}

	public void setSize(double size){
		this.size = size;
	}
	/**
	 * Method which triggers events based on the current count of the score
	 * keeper. This method will end the game if the count reaches zero.
	 */
	public enum TimerType {
		countDown_timer, countUp_timer;
	}

	private class TimerWatch {

		private int seconds;
		private int minutes;
		private int hours;

		public TimerWatch(int seconds, int minutes, int hours) {
			this.seconds = seconds;
			this.minutes = minutes;
			this.hours = hours;
		}

		public int getSeconds() {
			return seconds;
		}

		public int getMinutes() {
			return minutes;
		}

		public int getHours() {
			return hours;
		}
	}
	public enum TimerStyle{
		BLUE_STYLE,
		RED_STYLE,
		GREEN_STYLE,
		ORANGE_STYLE,
	}
	private class Styles {
		public static final String BLUE_STYLE = "-fx-fill:  linear-gradient(cyan , dodgerblue);";
		public static final String RED_STYLE = "-fx-fill:  linear-gradient(red , orangered );";
		public static final String GREEN_STYLE = "-fx-fill:  linear-gradient(limegreen , lime);";
		public static final String ORANGE_STYLE = "-fx-fill:  linear-gradient(yellow , darkorange);";
	}

	public StackPane getTimer() {
		return timerBox;
	}

}
