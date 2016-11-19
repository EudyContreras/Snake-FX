package com.EudyContreras.Snake.HUDElements;

import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Application.GameSettings;
import com.EudyContreras.Snake.ClassicSnake.ClassicSnake;
import com.EudyContreras.Snake.FrameWork.ResizeHelper;
import com.EudyContreras.Snake.Identifiers.GameStateID;
import com.EudyContreras.Snake.ImageBanks.GameImageBank;
import com.EudyContreras.Snake.PlayerOne.PlayerOne;
import com.EudyContreras.Snake.PlayerTwo.PlayerTwo;
import com.EudyContreras.Snake.Utilities.ScreenEffectUtility;

import javafx.animation.TranslateTransition;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
/**
 * Class which represents a board showing which player lost and the score of each
 * player.If the current mode is local multiplayer this board will then show once
 * either one of the player dies. If the current mode is online multiplayer this board
 * will show only for the player that died.
 * @author Eudy Contreras
 *
 */
public class GameOverScreen {

	public static boolean LEVEL_COMPLETE = false;
	public static boolean LEVEL_FAILED = false;
	private TranslateTransition transitionOne;
	private TranslateTransition transitionTwo;
	private ScreenEffectUtility overlay;
	private LocalScoreScreen scoreScreen;
	private GameManager game;
	private DropShadow borderGlow;
	private ImageView sceneSnapshot;
	private ImageView baseGameBoard;
	private ImageView mainGameBoard;
	private ImageView continue_btt;
	private ImageView quitGame_btt;
	private ImageView restart_btt;
	private ImageView optionsBoard;
	private Image boardImage;
	private Pane scoreLayer;
	private int counter = 0;
	private int waitTime = 0;
	private int currentChoice = 1;
	private double width = 0;
	private double height = 0;
	private double confirmX = 0;
	private double confirmXTwo = 0;
	private double confirmXPosition = 0;
	private double transitionOpacity = 1;
	private double opacityValue = -0.016;
	private double acceleration = 0.3f;
	private boolean transitioning = true;
	private boolean showTransition = false;
	private boolean showWinner = false;
	private boolean showScores = false;
	private boolean swipeRight = false;
	private boolean swipeLeft = false;
	private boolean center = true;
	private boolean allowSlowMo = false;
	/**
	 * Main constructur which takes an instance of the main game class along with
	 * with the base image of this board and the elements dimensions.
	 * @param game: main game class which connects this class to all other classes
	 * @param boardImage: Image which will be used as base for this board
	 * @param width: Horizontal dimension of this board
	 * @param height: Vertival dimension of this board
	 */
	public GameOverScreen(GameManager game, Image boardImage, double width, double height) {
		this.game = game;
		this.overlay = game.getOverlayEffect();
		this.scoreLayer = new Pane();
		this.boardImage = boardImage;
		this.width = width;
		this.height = height;
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
		baseGameBoard = new ImageView(GameImageBank.game_over_trans_board);
		mainGameBoard = new ImageView(GameImageBank.game_over_trans_board);
		continue_btt = new ImageView(GameImageBank.continue_button_alt);
		quitGame_btt = new ImageView(GameImageBank.quit_button);
		restart_btt = new ImageView(GameImageBank.restart_button);
		optionsBoard = new ImageView(GameImageBank.options_board);
		scoreScreen = new LocalScoreScreen(game,0,0,0,0, scoreLayer);
		baseGameBoard.setFitWidth(width);
		baseGameBoard.setFitHeight(height);
		baseGameBoard.setImage(boardImage);
		baseGameBoard.setY(GameSettings.HEIGHT / 2 - mainGameBoard.getFitHeight() / 2);
		scoreLayer.setPrefSize(GameSettings.WIDTH, GameSettings.HEIGHT);
		optionsBoard.setFitWidth(830);
		optionsBoard.setFitHeight((460)/4);
		continue_btt.setFitWidth(250);
		continue_btt.setFitHeight(80);
		quitGame_btt.setFitWidth(continue_btt.getFitWidth());
		quitGame_btt.setFitHeight(continue_btt.getFitHeight());
		restart_btt.setFitWidth((continue_btt.getFitWidth()));
		restart_btt.setFitHeight(quitGame_btt.getFitHeight());
		confirmX = (float) (0 - baseGameBoard.getFitWidth() - 50);
		confirmXTwo = (float) (0 - optionsBoard.getFitWidth() - 100);
		baseGameBoard.setX(confirmX);
		optionsBoard.setX(confirmXTwo);
		continue_btt.setX(optionsBoard.getX()+20);
		continue_btt.setY(optionsBoard.getY()+20);
		quitGame_btt.setX(optionsBoard.getX() + optionsBoard.getFitWidth() - quitGame_btt.getFitWidth()-20);
		quitGame_btt.setY(optionsBoard.getY()+20);
		restart_btt.setX(continue_btt.getX() + continue_btt.getFitWidth()+23);
		restart_btt.setY(continue_btt.getY());
		scoreLayer.getChildren().addAll(baseGameBoard,mainGameBoard,optionsBoard, continue_btt, quitGame_btt, restart_btt);
		baseGameBoard.setVisible(false);
		mainGameBoard.setVisible(false);
		optionsBoard.setVisible(false);
		continue_btt.setVisible(false);
		quitGame_btt.setVisible(false);
		restart_btt.setVisible(false);
		removeBoard();
		processMouseHandling();
	}

	@SuppressWarnings("unused")
	private void initTransitions(){
		transitionOne = new TranslateTransition(Duration.millis(1000), baseGameBoard);
		transitionTwo = new TranslateTransition(Duration.millis(1000), optionsBoard);
	}
	/**
	 * Method which is to be called once any of the players have
	 * died. This method marks the end of the game being played
	 */
	private void endGame() {
		game.getScene().setOnKeyPressed(e -> {});
		game.showCursor(true, game.getScene());
		game.getScoreKeeper().stopTimer();
		if(PlayerOne.DEAD){
			this.boardImage = GameImageBank.player_one_loses;
		}
		else if(PlayerTwo.DEAD){
			this.boardImage = GameImageBank.player_two_loses;
		}
		else if(ClassicSnake.DEAD){
			this.boardImage = GameImageBank.player_one_loses;
		}
		resetBoard();

		//TODO: Calculate rank
		/**
		 * int rank = Scoreboard.INSTANCE.calculateRank(score);

		stopGame();

		if( state == State.GAME_OVER && rank == -1) {
			Scoreboard.INSTANCE.update(null, null, score);
			screensController.setScreen(Main.SCREEN_GAME, Main.SCREEN_GAME_OVER);
		} else if(state == State.GAME_OVER && rank > -1) {
			Scoreboard.INSTANCE.updateLatestRank(rank);
			Scoreboard.INSTANCE.updateLatestScore(score);
			screensController.setScreen(Main.SCREEN_GAME, Main.SCREEN_ADD_SCORE);
		}
		 */
	}

	/**
	 * Method which processes events within the buttons used by the options board
	 * of this game board. This method processes all mouse input within these buttons
	 * and ensures to show visual ques when the buttons are selected
	 */
	private void processMouseHandling() {
		quitGame_btt.setOnMouseEntered(e -> {
			selectionReset();
			currentChoice = 1;
			borderGlow.setColor(Color.rgb(240,0,0));
			quitGame_btt.setEffect(borderGlow);
		});
		quitGame_btt.setOnMouseExited(e -> {
			quitGame_btt.setEffect(null);
		});
		quitGame_btt.setOnMouseClicked(e -> {
			game.setStateID(GameStateID.MAIN_MENU);
			if(!transitioning){
				allowSlowMo = false;
				game.getFadeScreenHandler().menu_fade_screen();
			}
		});
		restart_btt.setOnMouseEntered(e -> {
			selectionReset();
			currentChoice = 2;
			borderGlow.setColor(Color.rgb(240, 150,0));
			restart_btt.setEffect(borderGlow);
		});
		restart_btt.setOnMouseExited(e -> {
			restart_btt.setEffect(null);
		});
		restart_btt.setOnMouseClicked(e -> {
			game.setStateID(GameStateID.LEVEL_RESTART);
			if(!transitioning)
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
				if (currentChoice >= 2) {
					currentChoice = 2;
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
				if (currentChoice >= 2) {
					currentChoice = 2;
				}
				break;
			case ENTER:
				if (!e.isControlDown()) {
					if (currentChoice == 1) {
						game.setStateID(GameStateID.LEVEL_RESTART);
						if (!transitioning)
							restartLevel();
					}
					if (currentChoice == 2) {
						game.setStateID(GameStateID.MAIN_MENU);
						if (!transitioning)
							game.getFadeScreenHandler().menu_fade_screen();
					}
				}
				if(e.isControlDown()){
					if(!game.getMainWindow().isFullScreen()&& ResizeHelper.isSupportedRatio()){
						game.setNewRatio(true);
						game.getMainWindow().setFullScreen(true);
						game.getGameBorder().showBorders(false);
					}
					else{
						game.setNewRatio(false);
						game.getMainWindow().setFullScreen(false);
						game.getGameBorder().showBorders(true);
					}
				}
				break;
			case SPACE:
				if (currentChoice == 1) {
					game.setStateID(GameStateID.LEVEL_RESTART);
					if(!transitioning)
					restartLevel();
				}
				if (currentChoice == 2) {
					game.setStateID(GameStateID.MAIN_MENU);
					if(!transitioning)
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
			borderGlow.setColor(Color.rgb(240,150,0));
			restart_btt.setEffect(borderGlow);
			quitGame_btt.setEffect(null);
		}
		if(currentChoice==2){
			borderGlow.setColor(Color.rgb(240,0,0));
			quitGame_btt.setEffect(borderGlow);
			restart_btt.setEffect(null);
		}
	}
	/**
	 * Method used to reset the visual que
	 * of of all buttons by removing said
	 * visual que
	 */
	private void selectionReset(){
		restart_btt.setEffect(null);
		quitGame_btt.setEffect(null);
	}
	/**
	 * Method which checks the status of players.
	 * It checks if any of the players has died
	 * and if so it will then end the game and
	 * show the game over board.
	 */
	private void checkStatus(){
		if (PlayerOne.ALLOW_FADE || PlayerTwo.ALLOW_FADE || ClassicSnake.ALLOW_FADE) {
			if (LEVEL_FAILED == false) {
//				removeBoard();
				endGame();
				game.getScoreKeeper().swipeDown();
				game.getGameHud().showHUDCover();
				LEVEL_FAILED = true;
				PlayerOne.ALLOW_FADE = false;
				PlayerTwo.ALLOW_FADE = false;
				ClassicSnake.ALLOW_FADE = false;
			}
		}
	}
	/**
	 * Method called by the dead player at the
	 * end of the death animation. This method will
	 * show the game over screen adn will allow the player
	 * to restart or continue.
	 */
	public void gameOver(){
		if (LEVEL_FAILED == false) {
//			removeBoard();
			game.getScoreKeeper().swipeDown();
			game.getGameHud().showHUDCover();
			endGame();
			LEVEL_FAILED = true;
		}
	}
	/**
	 * Method which when called updates
	 * various elements of this game board
	 * by further calling methods which will translate
	 * or transform said elements
	 */
	public void updateUI(){
		if (game.getStateID() != GameStateID.GAMEPLAY) {
			positionScoreScreen();
			positionScreen();
			showScores();
			swipeRight();
			swipeLeft();
			setSlowMotion();
		}
		checkStatus();
	}
	/**
	 * Method which when called will show the board and will animate
	 * the board with a range of transitions .
	 */
	@SuppressWarnings("unused")
	private void showTheBoard(){

		transitionOne.setFromX(0 - baseGameBoard.getFitWidth());
		transitionOne.setToX(GameSettings.WIDTH / 2 - baseGameBoard.getFitWidth() / 2);
		transitionOne.setCycleCount(1);
		transitionOne.setAutoReverse(true);
		transitionOne.setOnFinished(event -> {
			transitionOpacity = 0;
			mainGameBoard.setOpacity(transitionOpacity);
			opacityValue = 0.016;
			waitTime = 10;
			mainGameBoard.setVisible(true);
			scoreScreen.showScores();
			showWinner = true;
			blurOut();
			fadeOut();
			transitionOne.stop();
		});

		transitionTwo.setFromX(0 - optionsBoard.getFitWidth());
		transitionTwo.setToX(GameSettings.WIDTH / 2 - optionsBoard.getFitWidth() / 2);
		transitionTwo.setCycleCount(1);
		transitionTwo.setAutoReverse(true);
		transitionTwo.setOnFinished(event -> {
			processPlayerScores();
			processKeyHandling();
			transitionTwo.stop();
		});

		transitionOne.play();
		transitionTwo.play();
	}
	/**
	 * Method which when called will hide the board and will animate
	 * the board with a range of transitions .
	 */
	@SuppressWarnings("unused")
	private void hideTheBoard(){

		transitionOne.setToX(0 - baseGameBoard.getFitWidth());
		transitionOne.setFromX(GameSettings.WIDTH / 2 - baseGameBoard.getFitWidth() / 2);
		transitionOne.setCycleCount(1);
		transitionOne.setAutoReverse(true);
		transitionOne.setOnFinished(event -> {
			PlayerOne.LEVEL_COMPLETED = false;
			PlayerTwo.LEVEL_COMPLETED = false;
			ClassicSnake.LEVEL_COMPLETED = false;
			game.getFadeScreenHandler().restart_fade_screen();
			transitionOne.stop();
		});

		transitionTwo.setToX(0 - optionsBoard.getFitWidth());
		transitionTwo.setFromX(GameSettings.WIDTH / 2 - optionsBoard.getFitWidth() / 2);
		transitionTwo.setCycleCount(1);
		transitionTwo.setAutoReverse(true);
		transitionTwo.setOnFinished(event -> {
			transitionTwo.stop();
		});

		transitionOne.play();
		transitionTwo.play();
	}

	public void swipeRight() {
		if (swipeRight == true) {
			baseGameBoard.setX(confirmX);
			optionsBoard.setX(confirmXTwo);
			confirmX += confirmXPosition;
			confirmXTwo += confirmXPosition;
			confirmXPosition += acceleration*1.05;
			if (center) {
				acceleration -= 1.00;
				if (acceleration <= 0) {

					acceleration = 0;
					confirmXPosition -= 0.5;
					if (confirmXPosition <= 0.25) {
						confirmXPosition = 0.25f;
					}

				}
				if (confirmX >= GameSettings.WIDTH / 2 - baseGameBoard.getFitWidth() / 2) {
					confirmX = (float) (GameSettings.WIDTH / 2 - baseGameBoard.getFitWidth() / 2);
					acceleration = 0;
					currentChoice = 1;
					center = false;
					swipeRight = false;
					showWinner = true;
					transitioning = false;
					transitionOpacity = 0;
					opacityValue = 0.016;
					waitTime = 10;
					baseGameBoard.setX(confirmX);
					mainGameBoard.setOpacity(transitionOpacity);
					scoreScreen.showScores();
					processPlayerScores();
					blurOut();
					fadeOut();
					processKeyHandling();
				}
				if (confirmXTwo >= GameSettings.WIDTH / 2 - optionsBoard.getFitWidth() / 2) {
					confirmXTwo = (float) (GameSettings.WIDTH / 2 - optionsBoard.getFitWidth() / 2);
					optionsBoard.setX(confirmXTwo);
				}
			}
			continue_btt.setX(optionsBoard.getX()+20);
			continue_btt.setY(optionsBoard.getY()+20);
			quitGame_btt.setX(optionsBoard.getX() + optionsBoard.getFitWidth() - quitGame_btt.getFitWidth()-20);
			quitGame_btt.setY(optionsBoard.getY()+20);
			restart_btt.setX(continue_btt.getX() + continue_btt.getFitWidth()+23);
			restart_btt.setY(continue_btt.getY());
		}
	}

	public void swipeLeft() {
		if (swipeLeft == true) {
			positionScoreScreen();
			baseGameBoard.setX(confirmX);
			mainGameBoard.setX(confirmX);
			optionsBoard.setX(confirmXTwo);
			confirmX -= confirmXPosition;
			confirmXTwo -= confirmXPosition;
			confirmXPosition += acceleration;
			if (center) {
				acceleration -= 0.50;
				if (acceleration <= 0) {
					confirmXPosition -= 0.1;
					acceleration = 0;
					if (confirmXPosition <= 0.001) {
						confirmXPosition = 0.001f;
					}

				}
				if (confirmX <= 0 - baseGameBoard.getFitWidth() - 50) {
					confirmX = (float) (0 - baseGameBoard.getFitWidth() + 50);
					confirmXPosition = 0;
					swipeLeft = false;
					allowSlowMo = false;
					PlayerOne.LEVEL_COMPLETED = false;
					PlayerTwo.LEVEL_COMPLETED = false;
					ClassicSnake.LEVEL_COMPLETED = false;
					removeBoard();
					center = false;
				}
			}
			continue_btt.setX(optionsBoard.getX()+20);
			continue_btt.setY(optionsBoard.getY()+20);
			quitGame_btt.setX(optionsBoard.getX() + optionsBoard.getFitWidth() - quitGame_btt.getFitWidth()-20);
			quitGame_btt.setY(optionsBoard.getY()+20);
			restart_btt.setX(continue_btt.getX() + continue_btt.getFitWidth()+23);
			restart_btt.setY(continue_btt.getY());
		}
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
				mainGameBoard.setImage(GameImageBank.game_over_score_board);
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
				mainGameBoard.setImage(boardImage);
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

	public void setSlowMotion(){
		if (allowSlowMo) {
			GameSettings.FRAME_SCALE -= 0.0015;
			if (GameSettings.FRAME_SCALE <= 0) {
				GameSettings.FRAME_SCALE = 0;
				allowSlowMo = false;
			}
		}
	}

	public void showSceneSnap(){
		game.getNinthLayer().getChildren().remove(sceneSnapshot);
		sceneSnapshot = null;
		show(false);
		if(game.getMainWindow().isFullScreen()){
			sceneSnapshot = new ImageView(game.getScene().snapshot(null));
			sceneSnapshot.setFitWidth(GameSettings.WIDTH);
			sceneSnapshot.setFitHeight(GameSettings.HEIGHT);
		}
		else{
			game.getGameBorder().removeBorders();
			sceneSnapshot = new ImageView(game.getScene().snapshot(null));
			sceneSnapshot.setFitWidth(GameSettings.SCREEN_WIDTH);
			sceneSnapshot.setFitHeight(GameSettings.SCREEN_HEIGHT);
			sceneSnapshot.setTranslateX(-20);
			sceneSnapshot.setTranslateY(-55);
			game.getGameBorder().reAddBorders();
		}

		show(true);
		game.getNinthLayer().getChildren().add(sceneSnapshot);
		overlay.removeBlur();
	}
	/**
	 * Method which collects the scores to be shown by the local
	 * score screen and positions the scores at a desired position
	 * relative to the game board of this class
	 */
	private void processPlayerScores(){
		scoreScreen.setScores();
		scoreScreen.relocateScoreOne(mainGameBoard.getX()+135, mainGameBoard.getY()+mainGameBoard.getFitHeight()/1.3);
		scoreScreen.relocateScoreTwo(mainGameBoard.getX()+mainGameBoard.getFitWidth()/2+25, mainGameBoard.getY()+mainGameBoard.getFitHeight()/1.3);
	}
	/**
	 * Method which updates the position of the local score
	 * so that it remains at a desired location relative to the board
	 * element of this class
	 */
	private void positionScoreScreen(){
		scoreScreen.relocateScoreOne(mainGameBoard.getX()+135, mainGameBoard.getY()+mainGameBoard.getFitHeight()/1.3);
		scoreScreen.relocateScoreTwo(mainGameBoard.getX()+mainGameBoard.getFitWidth()/2+25, mainGameBoard.getY()+mainGameBoard.getFitHeight()/1.3);
	}
	/**
	 * Method which updates teh position of the various UI elements
	 * used by this class in order to keeps these elements
	 * relative to the main board of this class
	 */
	private void positionScreen(){
		mainGameBoard.setRotate(baseGameBoard.getRotate());
		mainGameBoard.setFitWidth(baseGameBoard.getFitWidth());
		mainGameBoard.setFitHeight(baseGameBoard.getFitHeight());
		mainGameBoard.setX(baseGameBoard.getX());
		mainGameBoard.setY(baseGameBoard.getY());
	}
	/**
	 * Method which is called if the player
	 * decides to restart the level
	 */
	private void restartLevel() {
		transitioning = true;
		showSceneSnap();
		game.getFadeScreenHandler().restart_fade_screen();
		game.getScoreKeeper().resetTimer();
		center = true;
		swipeLeft = true;
		acceleration = 6.0f;
		confirmXPosition = 0.001f;
	}
	/**
	 * Method which removes this board from
	 * the main root of the game and makes
	 * all the UI elements of this board not visible
	 */
	public void removeBoard() {
		allowSlowMo = false;
		baseGameBoard.setVisible(false);
		mainGameBoard.setVisible(false);
		optionsBoard.setVisible(false);
		continue_btt.setVisible(false);
		quitGame_btt.setVisible(false);
		restart_btt.setVisible(false);
		scoreScreen.hideScores();
		game.getMainRoot().getChildren().remove(scoreLayer);
		LEVEL_FAILED = false;
		confirmX = (float) (0 - baseGameBoard.getFitWidth() - 50);
		confirmXTwo = (float) (0 - optionsBoard.getFitWidth() - 100);
		baseGameBoard.setX(confirmX);
		optionsBoard.setX(confirmXTwo);
		mainGameBoard.setX(0 - mainGameBoard.getFitWidth() - 50);
		confirmX = 0;
		acceleration = 6.0f;
		center = false;
	}
	/**
	 * Method which resets the board along with
	 * most of the other UI elements of this board
	 * allowing this board to be reused next time
	 * the game ends
	 */
	private void resetBoard() {
		game.getGameRoot().setEffect(null);
		confirmX = 0 - baseGameBoard.getFitWidth() - 50;
		confirmXTwo = 0 - optionsBoard.getFitWidth() - 100;
		optionsBoard.setX(confirmXTwo);
		baseGameBoard.setX(confirmX);
		mainGameBoard.setOpacity(0);
		baseGameBoard.setImage(GameImageBank.game_over_trans_board);
		baseGameBoard.setY(GameSettings.HEIGHT / 2 - mainGameBoard.getFitHeight() / 2);
		game.getMainRoot().getChildren().add(scoreLayer);
		center = true;
		swipeRight = true;
		showScores = false;
		showWinner = false;
		allowSlowMo = true;
		counter = 0;
		opacityValue = 0.016;
		transitionOpacity = 0;
		acceleration = 8.0f;
		confirmXPosition = 0.002f;
		mainGameBoard.setVisible(true);
		baseGameBoard.setVisible(true);
		optionsBoard.setVisible(true);
		continue_btt.setVisible(true);
		quitGame_btt.setVisible(true);
		restart_btt.setVisible(true);
	}

	public void show(boolean state){
		mainGameBoard.setVisible(state);
		baseGameBoard.setVisible(state);
		optionsBoard.setVisible(state);
		continue_btt.setVisible(state);
		quitGame_btt.setVisible(state);
		restart_btt.setVisible(state);
		scoreScreen.show(state);
		game.getGameHud().showShadow(state);
	}
	/**
	 * Method which when called will
	 * blur all the elements behind all
	 * the UI elements used by this class
	 */
	private void blurOut() {
		overlay.addDeathBlur();
	}
	/**
	 * Method which when called will add
	 * a fade screen transition
	 */
	public void fadeOut(){
		//game.getFadeScreenHandler().renderFadeScreen();
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
	}
}
