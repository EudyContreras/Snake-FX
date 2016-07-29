package com.EudyContreras.Snake.HUDElements;

import com.EudyContreras.Snake.AbstractModels.AbstractHudElement;
import com.EudyContreras.Snake.ClassicSnake.ClassicSnake;
import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.FrameWork.GameSettings;
import com.EudyContreras.Snake.HUDElements.GameTimer.TimerStyle;
import com.EudyContreras.Snake.HUDElements.GameTimer.TimerType;
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


	private DropShadow dropShadowTwo;
	private ImagePattern singlePlayer;
	private ImagePattern multipLayer;
	private GameTimer timer;
	private GameManager game;
	private ImageView apple;
	private Rectangle board;
	private Text countText;
	private boolean swipeUp = false;
	private boolean swipeDown = true;
	private double swipeSpeed = 0;
	private int initialAmount = 0;
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
		this.apple = new ImageView(GameImageBank.apple_alt);
		this.timer = new GameTimer(game, 175,53, 36, TimerStyle.ORANGE_STYLE, Color.TRANSPARENT);
		this.timer.setTimer(0, 0, 0, TimerType.countUp_timer);
		this.board = new Rectangle();
		this.apple.setPreserveRatio(true);
		this.singlePlayer = new ImagePattern(GameImageBank.score_keeper_singlePlayer);
		this.multipLayer = new ImagePattern(GameImageBank.score_keeper_multiPlayer);
		this.game.getThirTeenthLayer().getChildren().add(board);
		this.game.getThirTeenthLayer().getChildren().add(apple);
		this.game.getThirTeenthLayer().getChildren().add(countText);
		this.game.getThirTeenthLayer().getChildren().add(timer.getTimer());
		setupText();
		processCount();
	}

	/**O
	 * Method which initializes various text related elements which
	 * are used by this class and all the sub elements of those texts.
	 */
	private void setupText(){
		this.yOne = 10;
		this.yTwo = 0;
		this.baseY = yTwo+5;
		this.timer.setLocation(GameSettings.WIDTH/2 - timer.getWidth()/2-80, 15);
		this.countText.setX(xTwo + (widthOne*0.52));
		this.countText.setY(yOne+45);
		this.countText.setFont( Font.font(null,FontWeight.EXTRA_BOLD, 38));
        this.dropShadowTwo = new DropShadow();
        this.dropShadowTwo.setColor(Color.RED);
        this.dropShadowTwo.setRadius(20);
        this.dropShadowTwo.setSpread(0.1);
        this.dropShadowTwo.setBlurType(BlurType.TWO_PASS_BOX);
        this.countText.setEffect(dropShadowTwo);
        this.countText.setId("MainScore");
		this.board.setY(yTwo);
		this.apple.setY(yOne + 5);
		this.countText.setY(yOne+48);
        this.singlePlayerInfo();

	}
	public void multiplayerInfo(){
		APPLE_COUNT = count;
		this.xTwo = GameSettings.WIDTH / 2 -  690 / 2;
		this.widthOne = 690;
		this.heightOne = 85;
		this.board.setFill(multipLayer);
		this.board.setX(xTwo);
		this.board.setWidth(widthOne);
		this.board.setHeight(heightOne);
		this.apple.setX(xTwo + 690*0.43);
		this.apple.setFitWidth(55);
		this.apple.setFitHeight(55);
		this.apple.setImage(GameImageBank.apple);
		this.countText.setX(xTwo + (widthOne*0.51));
		this.processCount();
	}

	public void singlePlayerInfo(){
		this.initialAmount = 0;
		this.xTwo = GameSettings.WIDTH / 2 -  690 / 2;
		this.widthOne = 690;
		this.heightOne = 90;
		this.board.setFill(singlePlayer);
		this.board.setX(xTwo);
		this.board.setWidth(widthOne);
		this.board.setHeight(heightOne);
		this.apple.setX(xTwo + 690*0.52);
		this.apple.setFitWidth(55);
		this.apple.setFitHeight(55);
		this.apple.setImage(GameImageBank.apple_alt);
		this.countText.setX(xTwo + (widthOne*0.63));
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
		yTwo = yTwo + swipeSpeed;
		yOne = yOne + swipeSpeed;
		if (swipeDown) {
			swipeSpeed = 2.8f;
			if (yTwo > 110) {
				swipeSpeed = 0;
			}
		}
		if (swipeUp) {
			swipeSpeed = -2.8f;
			if (yTwo <= baseY) {
				swipeSpeed = 0;
			}
		}
		this.board.setY(yTwo);
		this.apple.setY(yOne + 7);
		this.countText.setY(yOne+48);
		this.timer.setLocation(GameSettings.WIDTH/2 - timer.getWidth()/2-80, yOne + 5);
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
		timer.updateUI();

	}
	/**
	 * Method which starts the game timer
	 */
	public void startTimer() {
		timer.startTimer();
	}
	/**
	 * Method which stops the game timer
	 */
	public void stopTimer() {
		timer.stopTimer();
	}
	/**
	 * Method which resets the game timer
	 */
	public void resetTimer() {
		timer.resetTimer();
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
			if(!PlayerOne.DEAD && !PlayerTwo.DEAD && !ClassicSnake.DEAD && game.getStateID()==GameStateID.GAMEPLAY){
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
	 * Returns a reference the timer class used
	 */
	public GameTimer getTimer(){
		return timer;
	}

}
