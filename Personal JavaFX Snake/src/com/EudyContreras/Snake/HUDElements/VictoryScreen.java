package com.EudyContreras.Snake.HUDElements;

import com.EudyContreras.Snake.AbstractModels.AbstractHudElement;
import com.EudyContreras.Snake.ClassicSnake.ClassicSnake;
import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.FrameWork.GameSettings;
import com.EudyContreras.Snake.Identifiers.GameStateID;
import com.EudyContreras.Snake.ImageBanks.GameImageBank;
import com.EudyContreras.Snake.PlayerOne.PlayerOne;
import com.EudyContreras.Snake.PlayerTwo.PlayerTwo;
import com.EudyContreras.Snake.Utilities.ScreenEffectUtility;

import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class VictoryScreen extends AbstractHudElement{

	public static boolean LEVEL_COMPLETE = false;
	private ScreenEffectUtility overlay;
	private LocalScoreScreen scoreScreen;
	private ParallelTransition parallelTransition;
	private TranslateTransition translateTransition;
	private RotateTransition rotateTransition;
	private ScaleTransition scaleTransition;
	private ScaleTransition scaleTransitionTwo;
	private GameManager game;
	private DropShadow borderGlow;
	private ImageView baseGameBoard;
	private ImageView mainGameBoard;
	private ImageView continue_btt;
	private ImageView quitGame_btt;
	private ImageView restart_btt;
	private ImageView optionsBoard;
	private Image winnerBoardImage;
	private Image scoreBoardImage;
	private Image baseBoardImage;
	private Pane scoreLayer;
	private int counter = 0;
	private int waitTime = 0;
	private int currentChoice = 1;
	private double width = 0;
	private double height = 0;
	private double transitionOpacity = 1;
	private double opacityValue = -0.016;
	private boolean showTransition = false;
	private boolean showWinner = false;
	private boolean showScores = false;
	private boolean goToNext = false;
	private boolean restartLevel = false;

	/**
	 * Main constructur which takes an instance of the main game class along with
	 * with the base image of this board and the elements dimensions.
	 * @param game: main game class which connects this class to all other classes
	 * @param boardImage: Image which will be used as base for this board
	 * @param width: Horizontal dimension of this board
	 * @param height: Vertival dimension of this board
	 */
	public VictoryScreen(GameManager game, Image boardImage, double width, double height) {
		this.game = game;
		this.overlay = game.getOverlayEffect();
		this.scoreLayer = new Pane();
		this.baseBoardImage = boardImage;
		this.width = GameManager.ScaleX(width);
		this.height = GameManager.ScaleY(height);
		this.borderGlow = new DropShadow();
		this.borderGlow.setOffsetY(0f);
		this.borderGlow.setOffsetX(0f);
		this.borderGlow.setSpread(0.3);
		this.borderGlow.setWidth(35);
		this.borderGlow.setHeight(35);
		this.borderGlow.setColor(Color.WHITE);
		this.borderGlow.setBlurType(BlurType.THREE_PASS_BOX);
		confirmScreenSetup();
	}
	/*
	 * Method which initializes most of the UI elements used by this board
	 * Most elements are also asigned fills and dimensions within this method
	 */

	private void confirmScreenSetup() {
		mainGameBoard = new ImageView();
		baseGameBoard = new ImageView();
		baseGameBoard.setFitWidth(width);
		baseGameBoard.setFitHeight(height);
		baseGameBoard.setImage(baseBoardImage);
		mainGameBoard.setFitWidth(width);
		mainGameBoard.setFitHeight(height);
		mainGameBoard.setImage(baseBoardImage);
		scoreLayer.setPrefSize(GameSettings.WIDTH, GameSettings.HEIGHT);
		mainGameBoard.setY(GameSettings.HEIGHT / 2 - mainGameBoard.getFitHeight() / 2 - GameManager.ScaleY(30));
		scoreScreen = new LocalScoreScreen(game,0,0,0,0, scoreLayer);
		continue_btt = new ImageView(GameImageBank.continue_button);
		quitGame_btt = new ImageView(GameImageBank.quit_button);
		restart_btt = new ImageView(GameImageBank.restart_button);
		optionsBoard = new ImageView(GameImageBank.options_board);
		optionsBoard.setFitWidth(GameManager.ScaleX(800));
		optionsBoard.setFitHeight(GameManager.ScaleY(450)/4);
		continue_btt.setFitWidth(GameManager.ScaleX(240));
		continue_btt.setFitHeight(GameManager.ScaleY(70));
		quitGame_btt.setFitWidth(GameManager.ScaleX(240));
		quitGame_btt.setFitHeight(GameManager.ScaleY(70));
		restart_btt.setFitWidth((continue_btt.getFitWidth()));
		restart_btt.setFitHeight(quitGame_btt.getFitHeight());
		scoreLayer.getChildren().addAll(baseGameBoard,mainGameBoard,optionsBoard, continue_btt, quitGame_btt, restart_btt);
		translateTransition = new TranslateTransition(Duration.millis(1000), optionsBoard);
		rotateTransition = new RotateTransition(Duration.millis(1000), baseGameBoard);
		scaleTransition = new ScaleTransition(Duration.millis(1000), baseGameBoard);
		scaleTransitionTwo = new ScaleTransition(Duration.millis(1000), mainGameBoard);
		processMouseHandling();
		//askConfirm();
	}
	/**
	 * Method which is to be called once any of the players have
	 * compleated the game or if all the apples have been collected.
	 * This method marks the end of the game being played
	 */

	public void endGame() {
		game.showCursor(true, game.getScene());
		PlayerTwo.LEVEL_COMPLETED = true;
		PlayerOne.LEVEL_COMPLETED = true;
		ClassicSnake.LEVEL_COMPLETED = true;
		if (game.getGameLoader().getPlayerOne() != null) {
			if (game.getGameLoader().getPlayerOne().getAppleCount() > game.getGameLoader().getPlayerTwo()
					.getAppleCount()) {
				this.winnerBoardImage = GameImageBank.player_one_wins;
				this.baseBoardImage = GameImageBank.player_score_trans_board;
				this.scoreBoardImage = GameImageBank.player_score_board;
			} else if (game.getGameLoader().getPlayerOne().getAppleCount() < game.getGameLoader().getPlayerTwo()
					.getAppleCount()) {
				this.winnerBoardImage = GameImageBank.player_two_wins;
				this.baseBoardImage = GameImageBank.player_score_trans_board;
				this.scoreBoardImage = GameImageBank.player_score_board;
			}else if (game.getGameLoader().getPlayerOne().getAppleCount() == game.getGameLoader().getPlayerTwo()
					.getAppleCount()) {
				this.winnerBoardImage = GameImageBank.draw_game;
				this.baseBoardImage = GameImageBank.game_draw_trans_board;
				this.scoreBoardImage = GameImageBank.game_draw_score_board;
			}
		} else if (game.getGameLoader().getClassicSnake() != null) {
				this.winnerBoardImage = GameImageBank.player_one_wins;
				this.baseBoardImage = GameImageBank.player_score_trans_board;
				this.scoreBoardImage = GameImageBank.player_score_board;
			
		}
		resetBoard();
		showTheBoard();
	}
	/**
	 * Method which processes events within the buttons used by the options board
	 * of this game board. This method processes all mouse input within these buttons
	 * and ensures to show visual ques when the buttons are selected
	 */
	private void processMouseHandling() {
		continue_btt.setOnMouseEntered(e -> {
			selectionReset();
			currentChoice = 1;
			borderGlow.setColor(Color.rgb(0,240,0));
			continue_btt.setEffect(borderGlow);
		});
		continue_btt.setOnMouseExited(e -> {
			continue_btt.setEffect(null);
		});
		continue_btt.setOnMouseClicked(e -> {
			game.setStateID(GameStateID.LEVEL_TRANSITIONING);
			goToNext();
		});
		quitGame_btt.setOnMouseEntered(e -> {
			selectionReset();
			currentChoice = 2;
			borderGlow.setColor(Color.rgb(240,0,0));
			quitGame_btt.setEffect(borderGlow);
		});
		quitGame_btt.setOnMouseExited(e -> {
			quitGame_btt.setEffect(null);
		});
		quitGame_btt.setOnMouseClicked(e -> {
			game.setStateID(GameStateID.MAIN_MENU);
			game.getFadeScreenHandler().menu_fade_screen();
		});
		restart_btt.setOnMouseEntered(e -> {
			currentChoice = 3;
			selectionReset();
			borderGlow.setColor(Color.rgb(240, 150,0));
			restart_btt.setEffect(borderGlow);
		});
		restart_btt.setOnMouseExited(e -> {
			restart_btt.setEffect(null);
		});
		restart_btt.setOnMouseClicked(e -> {
			game.setStateID(GameStateID.LEVEL_RESTART);
			restartLevel();
		});
	}
	/**
	 * Sets the key input handling for the buttons used by this game board
	 * The code below determines what will happen if the user presses enter or
	 * space on the different choices. The method also takes care of showing visual
	 * ques once the buttons are selected
	 */
	private void processKeyHandling() {
		updateSelections();
		game.getScene().setOnKeyPressed(e -> {
			switch (e.getCode()) {
			case LEFT:
				currentChoice -= 1;
				if (currentChoice <= 1) {
					currentChoice = 1;
				}
				break;
			case RIGHT:
				currentChoice += 1;
				if (currentChoice >= 3) {
					currentChoice = 3;
				}
				break;
			case A:
				currentChoice -= 1;
				if (currentChoice <= 1) {
					currentChoice = 1;
				}
				break;
			case D:
				currentChoice += 1;
				if (currentChoice >= 3) {
					currentChoice = 3;
				}
				break;
			case ENTER:
				if (currentChoice == 1) {
					game.setStateID(GameStateID.LEVEL_TRANSITIONING);
					goToNext();
				}
				if (currentChoice == 2) {
					game.setStateID(GameStateID.LEVEL_RESTART);
					restartLevel();
				}
				if (currentChoice == 3) {
					game.setStateID(GameStateID.MAIN_MENU);
					game.getFadeScreenHandler().menu_fade_screen();
				}
				break;
			case SPACE:
				if (currentChoice == 1) {
					game.setStateID(GameStateID.LEVEL_TRANSITIONING);
					goToNext();
				}
				if (currentChoice == 2) {
					game.setStateID(GameStateID.LEVEL_RESTART);
					restartLevel();
				}
				if (currentChoice == 3) {
					game.setStateID(GameStateID.MAIN_MENU);
					game.getFadeScreenHandler().menu_fade_screen();
				}
				break;
			default:
				break;
			}
			updateSelections();
		});

		}
	/**
	 * Method which updates the visual ques used
	 * in order to show which button is selected
	 */
	private void updateSelections(){
		if(currentChoice==1){
			borderGlow.setColor(Color.rgb(0,240,0));
			continue_btt.setEffect(borderGlow);
			restart_btt.setEffect(null);
			quitGame_btt.setEffect(null);
		}
		if(currentChoice==2){
			borderGlow.setColor(Color.rgb(240,150,0));
			restart_btt.setEffect(borderGlow);
			quitGame_btt.setEffect(null);
			continue_btt.setEffect(null);
		}
		if(currentChoice==3){
			borderGlow.setColor(Color.rgb(240,0,0));
			quitGame_btt.setEffect(borderGlow);
			restart_btt.setEffect(null);
			continue_btt.setEffect(null);
		}
	}
	/**
	 * Method used to reset the visual que
	 * of of all buttons by removing said
	 * visual que
	 */
	private void selectionReset(){
		continue_btt.setEffect(null);
		restart_btt.setEffect(null);
		quitGame_btt.setEffect(null);
	}
	/**
	 * Method which when called updates
	 * various elements of this game board
	 * by further calling methods which will translate
	 * or transform said elements
	 */
	public void updateUI(){
		positionScoreScreen();
		positionScreen();
		showScores();
	}
	/**
	 * Method which when called will show the board and will animate
	 * the board with a range of transitions .
	 */
	private void showTheBoard() {
		rotateTransition.setToAngle(360f*3);
		rotateTransition.setCycleCount(1);

		scaleTransition.setFromX(0);
		scaleTransition.setFromY(0);
		scaleTransition.setToX(1);
		scaleTransition.setToY(1);
		scaleTransition.setCycleCount(1);

		scaleTransitionTwo.setFromX(0);
		scaleTransitionTwo.setFromY(0);
		scaleTransitionTwo.setToX(1);
		scaleTransitionTwo.setToY(1);
		scaleTransitionTwo.setCycleCount(1);

		translateTransition.setFromX(0 - optionsBoard.getFitWidth());
		translateTransition.setToX(GameSettings.WIDTH / 2 - optionsBoard.getFitWidth() / 2);
		translateTransition.setCycleCount(1);

		parallelTransition = new ParallelTransition(baseGameBoard, scaleTransition, rotateTransition);
		parallelTransition.play();
		parallelTransition.setOnFinished(event -> {
			processPlayerScores();
			processKeyHandling();
			blurOut();
			counter = 0;
			opacityValue = 0.016;
			waitTime = 10;
			mainGameBoard.setVisible(true);
			scoreScreen.showScores();
			showWinner = true;
			showScores = false;
			parallelTransition.stop();
		});
		translateTransition.play();
		scaleTransitionTwo.play();
		baseGameBoard.setVisible(true);
		optionsBoard.setVisible(true);
		continue_btt.setVisible(true);
		quitGame_btt.setVisible(true);
		restart_btt.setVisible(true);
	}
	/**
	 * Method which when called will show the board and will animate
	 * the board with a range of transitions .
	 */
	private void hideTheBoard(){
		scoreScreen.hideScores();
		rotateTransition.setToAngle(0);
		rotateTransition.setCycleCount(1);

		scaleTransition.setFromX(1);
		scaleTransition.setFromY(1);
		scaleTransition.setToX(0);
		scaleTransition.setToY(0);
		scaleTransition.setCycleCount(1);

		scaleTransitionTwo.setFromX(1);
		scaleTransitionTwo.setFromY(1);
		scaleTransitionTwo.setToX(0);
		scaleTransitionTwo.setToY(0);
		scaleTransitionTwo.setCycleCount(1);

		translateTransition.setToX(0 - optionsBoard.getFitWidth());
		translateTransition.setFromX(GameSettings.WIDTH / 2 - optionsBoard.getFitWidth() / 2);
		translateTransition.setCycleCount(1);

		parallelTransition = new ParallelTransition(baseGameBoard, scaleTransition, rotateTransition);
		parallelTransition.play();
		parallelTransition.setOnFinished(event -> {
			game.processGameInput();
			if(restartLevel){
				game.getFadeScreenHandler().quick_restart_fade_screen();
				PlayerOne.LEVEL_COMPLETED = false;
				PlayerTwo.LEVEL_COMPLETED = false;
				ClassicSnake.LEVEL_COMPLETED = false;
			}
			else if(goToNext){
				game.getFadeScreenHandler().continue_fade_screen();
			}
			parallelTransition.stop();
		});
		translateTransition.play();
		scaleTransitionTwo.play();
		mainGameBoard.setVisible(true);
		baseGameBoard.setVisible(true);
		optionsBoard.setVisible(true);
		continue_btt.setVisible(true);
		quitGame_btt.setVisible(true);
		restart_btt.setVisible(true);

	}
	/**
	 * method used to both update the opacity of the main
	 * score board shown after the game ends. This method
	 * also produces a transition which will change the image
	 * shown each time the board reaches its minimum opacity
	 * level
	 */
	private void showScores(){
		if(showScores == true){
			mainGameBoard.setOpacity(transitionOpacity);
			counter++;
			if(counter>=waitTime){
				counter = waitTime;
				showTransition = true;
			}
			if(showTransition == true){
				mainGameBoard.setImage(scoreBoardImage);
				scoreScreen.setScoreOpacity(transitionOpacity);
				transitionOpacity+= opacityValue;
				if(transitionOpacity>=1 && opacityValue>0){
					scoreScreen.setScoreOpacity(transitionOpacity);
					transitionOpacity = 1;
					opacityValue = -0.010;
				}
				if(transitionOpacity<=0 && opacityValue<0){
					scoreScreen.setScoreOpacity(transitionOpacity);
					showScores = false;
					showWinner = true;
					counter = 0;
					waitTime = 60;
					opacityValue = 0.016;
					showTransition = false;
				}
			}
		}
		if(showWinner == true){
			mainGameBoard.setOpacity(transitionOpacity);
			counter++;
			if(counter>=waitTime){
				counter = waitTime;
				showTransition = true;
			}
			if(showTransition == true){
				mainGameBoard.setImage(winnerBoardImage);
				transitionOpacity+= opacityValue;
				if(transitionOpacity>=1 && opacityValue>0){
					transitionOpacity = 1;
					opacityValue = -0.010;
				}
				if(transitionOpacity<=0 && opacityValue<0){
					showScores = true;
					showWinner = false;
					counter = 0;
					waitTime = 60;
					opacityValue = 0.016;
					showTransition = false;
				}
			}
		}
	}
	/**
	 * Method which collects the scores to be shown by the local
	 * score screen and positions the scores at a desired position
	 * relative to the game board of this class
	 */
	private void processPlayerScores(){
		scoreScreen.setScores();
		scoreScreen.relocateScoreOne(mainGameBoard.getX()+GameManager.ScaleX(135), mainGameBoard.getY()+mainGameBoard.getFitHeight()/1.3);
		scoreScreen.relocateScoreTwo(mainGameBoard.getX()+mainGameBoard.getFitWidth()/2+GameManager.ScaleX(25), mainGameBoard.getY()+mainGameBoard.getFitHeight()/1.3);
	}
	/**
	 * Method which updates the position of the local score
	 * so that it remains at a desired location relative to the board
	 * element of this class
	 */
	private void positionScoreScreen(){
		scoreScreen.relocateScoreOne(mainGameBoard.getX()+GameManager.ScaleX(135), mainGameBoard.getY()+mainGameBoard.getFitHeight()/1.3);
		scoreScreen.relocateScoreTwo(mainGameBoard.getX()+mainGameBoard.getFitWidth()/2+GameManager.ScaleX(25), mainGameBoard.getY()+mainGameBoard.getFitHeight()/1.3);
	}
	/**
	 * Method which updates teh position of the various UI elements
	 * used by this class in order to keeps these elements
	 * relative to the main board of this class
	 */
	private void positionScreen(){
		mainGameBoard.setX(baseGameBoard.getX());
		mainGameBoard.setY(baseGameBoard.getY());
		mainGameBoard.setRotate(baseGameBoard.getRotate());
		continue_btt.setX(optionsBoard.getTranslateX()+20/GameManager.ScaleX);
		continue_btt.setY(optionsBoard.getTranslateY()+20/GameManager.ScaleY);
		quitGame_btt.setX(optionsBoard.getTranslateX() + optionsBoard.getFitWidth() - quitGame_btt.getFitWidth()-20/GameManager.ScaleX);
		quitGame_btt.setY(optionsBoard.getTranslateY()+20/GameManager.ScaleY);
		restart_btt.setX(continue_btt.getX() + continue_btt.getFitWidth()+23/GameManager.ScaleX);
		restart_btt.setY(continue_btt.getY());
	}
	/**
	 * Method which is called if the player
	 * decides to restart the level
	 */
	private void restartLevel() {
		game.removePlayers();
		overlay.removeBlur();
		restartLevel = true;
		goToNext = false;
		hideTheBoard();
	}
	/**
	 * Method which is called if the player
	 * decides to continue to the next level
	 * of the game.
	 */
	private void goToNext(){
		game.removePlayers();
		overlay.removeBlur();
		goToNext = true;
		restartLevel = false;
		hideTheBoard();
	}
	/**
	 * Method which removes this board from
	 * the main root of the game and makes
	 * all the UI elements of this board not visible
	 */
	public void removeBoard() {
		baseGameBoard.setVisible(false);
		mainGameBoard.setVisible(false);
		optionsBoard.setVisible(false);
		continue_btt.setVisible(false);
		quitGame_btt.setVisible(false);
		restart_btt.setVisible(false);
		game.getMainRoot().getChildren().remove(scoreLayer);
		LEVEL_COMPLETE = false;
	}
	/**
	 * Method which resets the board along with
	 * most of the other UI elements of this board
	 * allowing this board to be reused next time
	 * the game ends
	 */
	private void resetBoard() {
		scoreScreen.hideScores();
		baseGameBoard.setImage(baseBoardImage);
		baseGameBoard.setVisible(false);
		mainGameBoard.setVisible(false);
		optionsBoard.setVisible(false);
		continue_btt.setVisible(false);
		quitGame_btt.setVisible(false);
		restart_btt.setVisible(false);
		baseGameBoard.setX(GameSettings.WIDTH/2-baseGameBoard.getFitWidth()/2);
		baseGameBoard.setY(GameSettings.HEIGHT/2-baseGameBoard.getFitHeight()/2);
		game.getMainRoot().getChildren().add(scoreLayer);
		game.setStateID(GameStateID.LEVEL_COMPLETED);
		showScores = false;
		showWinner = false;
		showTransition = false;
		counter = 0;
		waitTime = 10;
		opacityValue = 0.016;
		transitionOpacity = 0;
	}
	/**
	 * Method which when called will
	 * blur all the elements behind all
	 * the UI elements used by this class
	 */
	public void blurOut() {
		overlay.levelCompleteBlur();
	}
	/**
	 * Method which when called will removed
	 * all blur from screen.
	 */
	public void removeBlur() {
		overlay.removeBlur();
		PlayerTwo.LEVEL_COMPLETED = false;
		PlayerOne.LEVEL_COMPLETED = false;
		ClassicSnake.LEVEL_COMPLETED = false;
		removeBoard();
	}
}
