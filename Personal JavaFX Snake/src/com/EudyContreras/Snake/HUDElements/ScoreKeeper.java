package com.EudyContreras.Snake.HUDElements;

import com.EudyContreras.Snake.AbstractModels.AbstractHudElement;
import com.EudyContreras.Snake.FrameWork.GameLoader;
import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.FrameWork.GameSettings;
import com.EudyContreras.Snake.Identifiers.GameModeID;
import com.EudyContreras.Snake.Identifiers.GameStateID;
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
	private ImagePattern singlePlayer;
	private ImagePattern multipLayer;
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
	private int count = 0;
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
	public ScoreKeeper(GameManager game, int count) {
		APPLE_COUNT = count;
		this.game = game;
		this.count = count;
		this.initialAmount = count;
		this.countText = new Text();
		this.timerText = new Text();
		this.apple = new ImageView(GameImageBank.apple_alt);
		this.board = new Rectangle();
		this.apple.setPreserveRatio(true);
		this.singlePlayer = new ImagePattern(GameImageBank.score_keeper_singlePlayer);
		this.multipLayer = new ImagePattern(GameImageBank.score_keeper_multiPlayer);
		this.game.getThirTeenthLayer().getChildren().add(board);
		this.game.getThirTeenthLayer().getChildren().add(apple);
		this.game.getThirTeenthLayer().getChildren().add(countText);
		this.game.getThirTeenthLayer().getChildren().add(timerText);
		setupText();
		processCount();
	}

	/**O
	 * Method which initializes various text related elements which
	 * are used by this class and all the sub elements of those texts.
	 */
	private void setupText(){
		this.countText.setX(xTwo + (widthOne*0.52));
		this.countText.setY(yOne+GameManager.ScaleY(45));
		this.timerText.setX(xTwo+GameManager.ScaleX(40));
		this.timerText.setY(yOne-GameManager.ScaleY(30));
		this.timerText.setFont(Font.font(null,FontWeight.EXTRA_BOLD, GameManager.ScaleX(34)));
		this.countText.setFont( Font.font(null,FontWeight.EXTRA_BOLD, GameManager.ScaleX(38)));
		this.timerText.setText("00:00:00");
        this.dropShadowOne = new DropShadow();
        this.dropShadowTwo = new DropShadow();
        this.dropShadowOne.setColor(Color.DODGERBLUE);
        this.dropShadowOne.setRadius(20);
        this.dropShadowOne.setSpread(0.1);
        this.dropShadowOne.setBlurType(BlurType.TWO_PASS_BOX);
        this.dropShadowTwo.setColor(Color.RED);
        this.dropShadowTwo.setRadius(20);
        this.dropShadowTwo.setSpread(0.1);
        this.dropShadowTwo.setBlurType(BlurType.TWO_PASS_BOX);
        this.countText.setEffect(dropShadowTwo);
        this.timerText.setEffect(dropShadowOne);
        this.timerText.setId("MainTimer");
        this.countText.setId("MainScore");
        this.singlePlayerInfo();

	}
	public void multiplayerInfo(){
		APPLE_COUNT = count;
		this.initialAmount = count;
		this.yOne = GameManager.ScaleY( 10);
		this.xTwo = GameSettings.WIDTH / 2 - GameManager.ScaleX( 180 / 2);
		this.yTwo = GameManager.ScaleY(5);
		this.baseY = yTwo;
		this.widthOne = GameManager.ScaleX(180);
		this.heightOne = GameManager.ScaleY(75);
		this.board.setFill(multipLayer);
		this.board.setX(xTwo);
		this.board.setY(yTwo);
		this.board.setWidth(widthOne);
		this.board.setHeight(heightOne);
		this.apple.setX(xTwo + GameManager.ScaleX(18));
		this.apple.setY(yOne + GameManager.ScaleY(3));
		this.apple.setFitWidth(55 / GameLoader.ResolutionScaleX);
		this.apple.setFitHeight(55 / GameLoader.ResolutionScaleY);
		this.apple.setImage(GameImageBank.apple);
		this.countText.setX(xTwo + (widthOne*0.52));
		this.countText.setY(yOne+GameManager.ScaleY(45));
		this.timerText.setX(xTwo+GameManager.ScaleX(40));
		this.timerText.setY(yOne-GameManager.ScaleY(30));
		this.timerText.setVisible(false);
		this.processCount();
	}
	public void singlePlayerInfo(){
		this.initialAmount = 0;
		this.yOne = GameManager.ScaleY( 10);
		this.xTwo = GameSettings.WIDTH / 2 - GameManager.ScaleX( 800 / 2);
		this.yTwo = GameManager.ScaleY(0);
		this.baseY = yTwo;
		this.widthOne = GameManager.ScaleX(800);
		this.heightOne = GameManager.ScaleY(90);
		this.board.setFill(singlePlayer);
		this.board.setX(xTwo);
		this.board.setY(yTwo);
		this.board.setWidth(widthOne);
		this.board.setHeight(heightOne);
		this.apple.setX(xTwo + GameManager.ScaleX(700)*0.58);
		this.apple.setY(yOne + GameManager.ScaleY(7));
		this.apple.setFitWidth(55 / GameLoader.ResolutionScaleX);
		this.apple.setFitHeight(55 / GameLoader.ResolutionScaleY);
		this.apple.setImage(GameImageBank.apple_alt);
		this.countText.setX(xTwo + (widthOne*0.60));
		this.countText.setY(yOne+GameManager.ScaleY(45));
		this.timerText.setX(xTwo+GameManager.ScaleX(255));
		this.timerText.setY(yOne+GameManager.ScaleY(45));
		this.timerText.setVisible(true);
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

	public void setboardMode(GameModeID id){
		if(id == GameModeID.ClassicMode){
			singlePlayerInfo();
		}
		else if(id == GameModeID.LocalMultiplayer || id == GameModeID.RemoteMultiplayer){
			multiplayerInfo();
		}
		else {

		}
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
			if (yTwo > GameManager.ScaleX(125)) {
				swipeSpeed = 0;
			}
		}
		if (swipeUp) {
			swipeSpeed = -2.5f;
			if (yTwo < baseY) {
				swipeSpeed = 0;
			}
		}
//		board.setY(yTwo);
//		apple.setY(yOne-GameManager.ScaleY(20));
//		countText.setTranslateY(yOne);
//		timerText.setTranslateY(yOne);
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
		if (APPLE_COUNT <= 0 && game.getModeID()!=GameModeID.ClassicMode) {
			if (VictoryScreen.LEVEL_COMPLETE == false) {
				game.getVictoryScreen().removeBoard();
				game.getVictoryScreen().endGame();
				game.getGameHud().showHUDCover();
				game.setStateID(GameStateID.LEVEL_COMPLETED);
				swipeDown();
				VictoryScreen.LEVEL_COMPLETE = true;
			}
		}
		else {
			if(!PlayerOne.DEAD && !PlayerTwo.DEAD && game.getStateID()!=GameStateID.GAME_MENU && game.getStateID()!=GameStateID.COUNT_DOWN){
//			swipeUp();
//			game.getGameHud().hideHUDCover();
			}
		}
	}
	/**
	 * Method which when called decreases the count
	 * of this score keeper
	 */
	public void decreaseCount() {
		APPLE_COUNT -= 1;
		processCount();
	}
	/**
	 * Method which when called increases the count
	 * of this score keeper
	 */
	public void increaseCount(){
		APPLE_COUNT += 1;
		processCount();
	}

	/**
	 * Determines the format in which the apple count
	 * is shown on the board by checking the current amount
	 * of apples.
	 */
	public void processCount(){
		if(APPLE_COUNT>9 && APPLE_COUNT<100){
			countText.setText("0" + APPLE_COUNT);
		}
		else if(APPLE_COUNT>99){
			countText.setText(APPLE_COUNT+"");
		}
		else {
			countText.setText("00"+APPLE_COUNT);
		}
	}
	/**
	 * Method which when called resets the count
	 * of this score keeper
	 */
	public void resetCount() {
		APPLE_COUNT = initialAmount;
		processCount();
	}
	/**
	 * Return the text used to display a timer
	 * @return: The text timer of this UI element
	 */
	public Text getTimer(){
		return timerText;
	}

}
