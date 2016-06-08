package com.EudyContreras.Snake.HUDElements;

import com.EudyContreras.Snake.AbstractModels.AbstractHudElement;
import com.EudyContreras.Snake.EnumIDs.GameStateID;
import com.EudyContreras.Snake.FrameWork.GameLoader;
import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.FrameWork.GameSettings;
import com.EudyContreras.Snake.ImageBanks.GameImageBank;
import com.EudyContreras.Snake.PlayerOne.PlayerOne;
import com.EudyContreras.Snake.PlayerTwo.PlayerTwo;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Class which represents a score keeper. This class keeps track of the game score
 * and perform events according to the kept records.
 * @author Eudy Contreras
 *
 */
public class ScoreKeeper extends AbstractHudElement{


	private DropShadow dropShadowOne;
	private DropShadow dropShadowTwo;
	private GameManager game;
	private ImageView apple;
	private Rectangle board;
	private String secondsS = "00";
	private String minutesS = "00";
	private String hoursS = "00";
	private Text countText;
	private Text timerText;
	private boolean startTimer = false;
	private boolean swipeUp = true;
	private boolean swipeDown = false;
	private double swipeSpeed = 0;
	private int initialAmount = 0;
	private int counter = 0;
	private int seconds = 00;
	private int minutes = 00;
	private int hours = 00;
	public static int APPLE_COUNT = 50;
	
	/**
	 * Constructor which takes the main class as a parameter along with a initial count and the x and y
	 * coordinates fo all the UI elements controlled by this class
	 * @param game: main game class which connect this class to all the other classes
	 * @param count: Initial count which is to be assigned to the amount of apples to collect during the game
	 * @param xOne: Horizontal coordinate for UI element one
	 * @param yOne: Vertical coordinate for UI element one
	 * @param xTwo: Horizontal coordinate for UI element two
	 * @param yTwo: Vertical coordinate for UI element two
	 * @param width: Horizontal dimension
	 * @param height: Vertical dimension
	 */
	public ScoreKeeper(GameManager game, int count, double xOne, double yOne, double xTwo, double yTwo, double width,double height) {
		APPLE_COUNT = count;
		this.yOne = yOne;
		this.xTwo = xTwo;
		this.yTwo = yTwo;
		this.baseY = yTwo;
		this.game = game;
		this.widthOne = width;
		this.initialAmount = count;
		this.countText = new Text();
		this.timerText = new Text();
		this.apple = new ImageView(GameImageBank.apple);
		this.board = new Rectangle(xTwo, yTwo, width, height);
		this.board.setFill(new ImagePattern(GameImageBank.score_keeper));
		this.apple.setFitWidth(45 / GameLoader.ResolutionScaleX);
		this.apple.setFitHeight(45 / GameLoader.ResolutionScaleY);
		this.apple.setPreserveRatio(true);
		this.apple.setX(GameSettings.WIDTH/2+15);
		this.apple.setY(yOne-40);
		this.game.getThirTeenthLayer().getChildren().add(board);
		this.game.getThirTeenthLayer().getChildren().add(apple);
		this.game.getThirTeenthLayer().getChildren().add(countText);
		this.game.getThirTeenthLayer().getChildren().add(timerText);
		setupText();
		updateCount();
	}
	/**
	 * Method which initializes various text related elements which 
	 * are used by this class and all the sub elements of those texts.
	 */
	private void setupText(){
		this.countText.setX(apple.getX() + apple.getFitWidth()-5);
		this.countText.setY(yOne-GameManager.ScaleY(25));
		this.timerText.setX(xTwo+widthOne*0.33);
		this.timerText.setY(yOne-GameManager.ScaleY(25));
		this.timerText.setFont(Font.font(null,FontWeight.BOLD, GameManager.ScaleX(27)));
		this.countText.setFont( Font.font(null,FontWeight.BOLD, GameManager.ScaleX(27)));
		this.countText.setText("x " + APPLE_COUNT);
		this.timerText.setText("00:00:00");
        this.dropShadowOne = new DropShadow();
        this.dropShadowTwo = new DropShadow();
        this.dropShadowOne.setColor(Color.DODGERBLUE);
        this.dropShadowOne.setRadius(25);
        this.dropShadowOne.setSpread(0.15);
        this.dropShadowOne.setBlurType(BlurType.TWO_PASS_BOX);
        this.dropShadowTwo.setColor(Color.RED);
        this.dropShadowTwo.setRadius(25);
        this.dropShadowTwo.setSpread(0.15);
        this.dropShadowTwo.setBlurType(BlurType.TWO_PASS_BOX);
        this.countText.setEffect(dropShadowTwo);
        this.timerText.setEffect(dropShadowOne);
        this.timerText.setId("MainTimer");
        this.countText.setId("MainScore");

	}
	/**
	 * Method which updates all the UI elements 
	 * of this score keeping class along with the 
	 * logic behind the timer and the count keeper
	 * of this class
	 */

	public void updateUI(){
		updatePositions();
		updateTimer();
		keepCount();
	}
	/**
	 * Method which updates the position of the score
	 * keeping board and all of its components relative 
	 * to the movement of the main HUD. 
	 */
	private void updatePositions() {
		yTwo = yTwo + swipeSpeed/GameManager.ScaleY;
		yOne = yOne + swipeSpeed/GameManager.ScaleY;
		if (swipeDown) {
			swipeSpeed = 2.5f;
			if (yTwo >= GameManager.ScaleX(105)) {
				swipeSpeed = 0;
			}
		}
		if (swipeUp) {
			swipeSpeed = -2.5f;
			if (yTwo < baseY) {
				swipeSpeed = 0;
			}
		}
		board.setY(yTwo);
		apple.setY(yOne-GameManager.ScaleY(15));
		countText.setTranslateY(yOne);
		timerText.setTranslateY(yOne);
	}
	/**
	 * Method which shows or hide the board
	 * depeding on its current state. If the 
	 * board is currently showing it will hide
	 * it and viceversa.
	 */
	public void showHide() {
		if (swipeDown) {
			swipeDown = false;
			swipeUp = true;
		} else if (swipeUp) {
			swipeUp = false;
			swipeDown = true;
		}
	}
	/**
	 * Method which swipes down the 
	 * board 
	 */
	public void swipeDown() {
		swipeUp = false;
		swipeDown = true;
	}
	/**
	 * Method which swipes up the 
	 * board
	 */
	public void swipeUp() {
		swipeDown = false;
		swipeUp = true;
	}
	/**
	 * Method which updates the timer of this 
	 * score keeping class 
	 */
	private void updateTimer() {
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
			this.timerText.setText(hoursS + ":" + minutesS + ":" + secondsS);
		}
		else{
			stopTimer();
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
	public void resetTimer() {
		startTimer = false;
		secondsS = "00";
		minutesS = "00";
		hoursS = "00";
		seconds = 0;
		minutes = 0;
		hours = 0;
		timerText.setText(hoursS + ":" + minutesS + ":" + secondsS);
	}
	/**
	 * Method which triggers events based on the 
	 * current count of the score keeper. This method
	 * will end the game if the count reaches zero.
	 */
	private void keepCount() {
		if (APPLE_COUNT <= 0) {
			if (VictoryScreen.LEVEL_COMPLETE == false) {
				game.getVictoryScreen().removeBoard();
				game.getVictoryScreen().finishLevel();
				game.getGameHud().showHUDCover();
				game.setStateID(GameStateID.LEVEL_COMPLETED);
				swipeDown();
				VictoryScreen.LEVEL_COMPLETE = true;
			}
		}
		else {
			if(!PlayerOne.DEAD && !PlayerTwo.DEAD && game.getStateID()!=GameStateID.GAME_MENU){
			swipeUp();
			game.getGameHud().hideHUDCover();
			}
		}
	}
	/**
	 * Method which when called decreases the count
	 * of this score keeper
	 */
	public void decreaseCount() {
		APPLE_COUNT -= 1;
		updateCount();
	}
	/**
	 * Method which when called increases the count
	 * of this score keeper
	 */
	public void increaseCount(){
		APPLE_COUNT += 1;
		updateCount();
	}
	/**
	 * Method which when called visually updates the count
	 * of this score keeper
	 */
	public void updateCount() {
		countText.setText(" x " + APPLE_COUNT);
	}
	/**
	 * Method which when called resets the count
	 * of this score keeper
	 */
	public void resetCount() {
		APPLE_COUNT = initialAmount;
		countText.setText(" x " + APPLE_COUNT);
	}

}
