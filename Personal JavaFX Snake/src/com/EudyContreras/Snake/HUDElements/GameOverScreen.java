package com.EudyContreras.Snake.HUDElements;

import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.FrameWork.GameSettings;
import com.EudyContreras.Snake.Identifiers.GameStateID;
import com.EudyContreras.Snake.ImageBanks.GameImageBank;
import com.EudyContreras.Snake.PlayerOne.PlayerOne;
import com.EudyContreras.Snake.PlayerTwo.PlayerTwo;
import com.EudyContreras.Snake.Utilities.ScreenEffectUtility;

import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class GameOverScreen {

	public static boolean LEVEL_COMPLETE = false;
	public static boolean LEVEL_FAILED = false;
	private ScreenEffectUtility overlay;
	private LocalScoreScreen scoreScreen;
	private DropShadow borderGlow;
	private DropShadow boardPulse;
	private GameManager game;
	private ImageView confirmScreenBack;
	private ImageView confirmScreen;
	private ImageView continue_btt;
	private ImageView quitGame_btt;
	private ImageView restart_btt;
	private ImageView optionsBoard;
	private Pane scoreLayer;
	private Image boardImage;
	private int counter = 0;
	private int waitTime = 0;
	private int currentChoice = 1;
	private double width = 0;
	private double height = 0;
	private double pulse = 0;
	private double confirmX = 0;
	private double confirmXTwo = 0;
	private double confirmXPosition = 0;
	private double transitionOpacity = 1;
	private double opacityValue = -0.016;
	private double acceleration = 0.3f;
	private boolean showTransition = false;
	private boolean showWinner = false;
	private boolean showScores = false;
	private boolean allowGlow = false;
	private boolean pulseUp = true;
	private boolean pulseDown = false;
	private boolean swipeRight = false;
	private boolean swipeLeft = false;
	private boolean center = true;


	public GameOverScreen(GameManager game, Image boardImage, double width, double height) {
		this.game = game;
		this.overlay = game.getOverlayEffect();
		this.scoreLayer = new Pane();
		this.boardImage = boardImage;
		this.width = width;
		this.height = height;
		this.borderGlow = new DropShadow();
		this.boardPulse = new DropShadow();
		this.borderGlow.setOffsetY(0f);
		this.borderGlow.setOffsetX(0f);
		this.borderGlow.setSpread(0.3);
		this.borderGlow.setWidth(35);
		this.borderGlow.setHeight(35);
		this.boardPulse.setOffsetY(0f);
		this.boardPulse.setOffsetX(0f);
		this.boardPulse.setSpread(0.5);
		this.boardPulse.setWidth(0);
		this.boardPulse.setHeight(0);
		this.borderGlow.setColor(Color.WHITE);
		this.boardPulse.setColor(Color.GREEN);
		this.borderGlow.setBlurType(BlurType.THREE_PASS_BOX);
		this.boardPulse.setBlurType(BlurType.ONE_PASS_BOX);
		confirmScreenSetup();
	}
	private void confirmScreenSetup() {
		confirmScreen = new ImageView();
		confirmScreen.setFitWidth(width);
		confirmScreen.setFitHeight(height);
		confirmScreen.setImage(boardImage);
		if (allowGlow)
			confirmScreen.setEffect(boardPulse);
		scoreLayer.setPrefSize(GameSettings.WIDTH, GameSettings.HEIGHT);
//		confirmX = 0 - confirmScreen.getFitWidth() - 50;
//		confirmScreen.setY(GameSettings.HEIGHT / 2 - confirmScreen.getFitHeight() / 2);
		confirmScreenBack = new ImageView(GameImageBank.game_over_trans_board);
		continue_btt = new ImageView(GameImageBank.continue_button_alt);
		quitGame_btt = new ImageView(GameImageBank.quit_button);
		restart_btt = new ImageView(GameImageBank.restart_button);
		optionsBoard = new ImageView(GameImageBank.options_board);
		optionsBoard.setFitWidth(GameManager.ScaleX(800));
		optionsBoard.setFitHeight((GameManager.ScaleY(450)/4));
		continue_btt.setFitWidth(GameManager.ScaleX(240));
		continue_btt.setFitHeight(GameManager.ScaleY(70));
		quitGame_btt.setFitWidth(GameManager.ScaleX(240));
		quitGame_btt.setFitHeight(GameManager.ScaleY(70));
		restart_btt.setFitWidth((continue_btt.getFitWidth()));
		restart_btt.setFitHeight(quitGame_btt.getFitHeight());
		scoreLayer.getChildren().addAll(confirmScreenBack,confirmScreen,optionsBoard, continue_btt, quitGame_btt, restart_btt);
		scoreScreen = new LocalScoreScreen(game,0,0,0,0, scoreLayer);
		processMouseHandling();

	}
	public void finishLevel() {
		game.showCursor(true, game.getScene());
		game.getScoreKeeper().stopTimer();
		if(PlayerOne.DEAD){
			this.boardImage = GameImageBank.player_one_loses;
		}
		else if(PlayerTwo.DEAD){
			this.boardImage = GameImageBank.player_two_loses;
		}
		GameSettings.ALLOW_DAMAGE_IMMUNITY = true;
		askConfirm();
		showTheBox();
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
			GameSettings.ALLOW_DAMAGE_IMMUNITY = false;
			game.getFadeScreenHandler().menu_fade_screen();
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
			GameSettings.ALLOW_DAMAGE_IMMUNITY = false;
			restartLevel();
		});

	}
	/**
	 * Sets the key input handling for the labels
	 * Code below determines what will happen if the user presses enter or
	 * space on the different choices
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
				if (currentChoice == 1) {
					game.setStateID(GameStateID.LEVEL_RESTART);
					GameSettings.ALLOW_DAMAGE_IMMUNITY = false;
					restartLevel();
				}
				if (currentChoice == 2) {
					game.setStateID(GameStateID.MAIN_MENU);
					GameSettings.ALLOW_DAMAGE_IMMUNITY = false;
					game.getFadeScreenHandler().menu_fade_screen();
				}
				break;
			case SPACE:
				if (currentChoice == 1) {
					game.setStateID(GameStateID.LEVEL_RESTART);
					GameSettings.ALLOW_DAMAGE_IMMUNITY = false;
					restartLevel();
				}
				if (currentChoice == 2) {
					game.setStateID(GameStateID.MAIN_MENU);
					GameSettings.ALLOW_DAMAGE_IMMUNITY = false;
					game.getFadeScreenHandler().menu_fade_screen();
				}
				break;
			default:
				break;
			}
			updateSelections();
		});

		}
	public void updateSelections(){
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
	public void selectionReset(){
		restart_btt.setEffect(null);
		quitGame_btt.setEffect(null);
	}
	public void checkStatus(){
		if (PlayerOne.DEAD || PlayerTwo.DEAD) {
			if (LEVEL_FAILED == false) {
				removeBoard();
				finishLevel();
				game.getScoreKeeper().swipeDown();
				game.getGameHud().showHUDCover();
				LEVEL_FAILED = true;
			}
		}
	}
	public void boardPulse() {
		if (allowGlow) {
			boardPulse.setWidth(pulse);
			boardPulse.setHeight(pulse);
			if (pulseUp) {
				pulse += 1.5;
				if (pulse >= 120) {
					pulseUp = false;
					pulseDown = true;
				}
			}
			if (pulseDown) {
				pulse -= 1.5;
				if (pulse <= 20) {
					pulseDown = false;
					pulseUp = true;
				}
			}
		}
	}
	public void updateUI(){
		checkStatus();
		positionScreen();
		showScores();
		boardPulse();
		//swipeRight();
		//hide();

	}
	public void showTheBox(){

		TranslateTransition translateTransition = new TranslateTransition(Duration.millis(1000), confirmScreenBack);
		translateTransition.setFromX(0 - confirmScreenBack.getFitWidth());
		translateTransition.setToX(GameSettings.WIDTH / 2 - confirmScreenBack.getFitWidth() / 2);
		translateTransition.setCycleCount(1);
		translateTransition.setAutoReverse(true);
		translateTransition.setOnFinished(event -> {

			transitionOpacity = 0;
			confirmScreen.setOpacity(transitionOpacity);
			opacityValue = 0.016;
			waitTime = 10;
			processPlayerScores();
			// processKeyHandling();
			blurOut();
			fadeOut();
			confirmScreen.setVisible(true);
			showWinner = true;
		});
		TranslateTransition translateTransitionTwo = new TranslateTransition(Duration.millis(1000), optionsBoard);
		translateTransitionTwo.setFromX(0 - optionsBoard.getFitWidth());
		translateTransitionTwo.setToX(GameSettings.WIDTH / 2 - optionsBoard.getFitWidth() / 2);
		translateTransitionTwo.setCycleCount(1);
		translateTransitionTwo.setAutoReverse(true);
		translateTransitionTwo.setOnFinished(event -> {
			processKeyHandling();
		});

		// RotateTransition rotateTransition =
		// new RotateTransition(Duration.millis(1000), confirmScreen);
		// rotateTransition.setByAngle(360f);
		// rotateTransition.setCycleCount(1);
		// rotateTransition.setAutoReverse(true);

		// ScaleTransition scaleTransition =
		// new ScaleTransition(Duration.millis(2000), confirmScreen);
		// scaleTransition.setFromX(1);
		// scaleTransition.setFromY(1);
		// scaleTransition.setToX(2);
		// scaleTransition.setToY(2);
		// scaleTransition.setCycleCount(1);
		// scaleTransition.setAutoReverse(true);
		//
		// create a sequential transition to do four transitions one after
		// another
		// SequentialTransition sequentialTransition = new
		// SequentialTransition();
		// sequentialTransition.getChildren().addAll(
		// // fadeTransition,
		// // translateTransition,
		// translateTransition);
		// // scaleTransition);
		// sequentialTransition.setCycleCount(Timeline.);
		// sequentialTransition.setAutoReverse(true);
		// sequentialTransition.play();
		translateTransition.play();
		translateTransitionTwo.play();
	}
	public void swipeRight() {
		if (swipeRight == true) {
			//confirmScreen.setX(confirmX);
			optionsBoard.setX(confirmXTwo);
			confirmX += confirmXPosition/GameManager.ScaleX;
			confirmXTwo += confirmXPosition/GameManager.ScaleX;
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
				if (confirmX >= GameSettings.WIDTH / 2 - confirmScreen.getFitWidth() / 2) {
					confirmX = (float) (GameSettings.WIDTH / 2 - confirmScreen.getFitWidth() / 2);
					confirmScreen.setX(confirmX);
					acceleration = 0;
					currentChoice = 1;
					center = false;
					swipeRight = false;
//					showWinner = true;
//					transitionOpacity = 0;
//					opacityValue = 0.016;
//					waitTime = 10;
//					processPlayerScores();
//					processKeyHandling();
//					blurOut();
//					fadeOut();
				}
				if (confirmXTwo >= GameSettings.WIDTH / 2 - optionsBoard.getFitWidth() / 2) {
					confirmXTwo = (float) (GameSettings.WIDTH / 2 - optionsBoard.getFitWidth() / 2);
					optionsBoard.setX(confirmXTwo);
				}
			}

		}
	}

	public void hide() {
		if (swipeLeft == true) {
			positionScoreScreen();
			confirmScreen.setX(confirmX);
			optionsBoard.setX(confirmXTwo);
			confirmX -= confirmXPosition/GameManager.ScaleX;
			confirmXTwo -= confirmXPosition/GameManager.ScaleX;
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
				if (confirmX <= 0 - confirmScreen.getFitWidth() - 50) {
					game.getFadeScreenHandler().restart_fade_screen();
					confirmX = (float) (0 - confirmScreen.getFitWidth() + 50);
					confirmXPosition = 0;
					swipeLeft = false;
					game.processGameInput();
					PlayerOne.LEVEL_COMPLETED = false;
					PlayerTwo.LEVEL_COMPLETED = false;
					center = false;
				}
			}
			continue_btt.setX(optionsBoard.getX()+20);
			continue_btt.setY(optionsBoard.getY()+20);
			quitGame_btt.setX(optionsBoard.getX() + optionsBoard.getFitWidth() - quitGame_btt.getFitWidth()-20);
			quitGame_btt.setY(optionsBoard.getY()+20);
			restart_btt.setX(continue_btt.getX() + continue_btt.getFitWidth()+20);
			restart_btt.setY(continue_btt.getY());
		}
	}
	public void showScores(){
		if(showScores == true){
			confirmScreen.setOpacity(transitionOpacity);
			counter++;
			if(counter>=waitTime){
				counter = waitTime;
				showTransition = true;
			}
			if(showTransition == true){
				confirmScreen.setImage(GameImageBank.game_over_score_board);
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
			confirmScreen.setOpacity(transitionOpacity);
			counter++;
			if(counter>=waitTime){
				counter = waitTime;
				showTransition = true;
			}
			if(showTransition == true){
				confirmScreen.setImage(boardImage);
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
	public void processPlayerScores(){
		scoreScreen.setScores();
		scoreScreen.relocateScoreOne(confirmScreen.getX()+GameManager.ScaleX(240), confirmScreen.getY()+confirmScreen.getFitHeight()/1.3);
		scoreScreen.relocateScoreTwo(confirmScreen.getX()+confirmScreen.getFitWidth()/2+GameManager.ScaleX(130), confirmScreen.getY()+confirmScreen.getFitHeight()/1.3);
	}
	public void positionScoreScreen(){
		scoreScreen.relocateScoreOne(confirmScreen.getX()+GameManager.ScaleX(240), confirmScreen.getY()+confirmScreen.getFitHeight()/1.3);
		scoreScreen.relocateScoreTwo(confirmScreen.getX()+confirmScreen.getFitWidth()/2+GameManager.ScaleX(130), confirmScreen.getY()+confirmScreen.getFitHeight()/1.3);
	}
	public void positionScreen(){
		confirmScreenBack.setRotate(confirmScreen.getRotate());
		confirmScreenBack.setFitWidth(confirmScreen.getFitWidth());
		confirmScreenBack.setFitHeight(confirmScreen.getFitHeight());
		continue_btt.setX(optionsBoard.getTranslateX()+20/GameManager.ScaleX);
		continue_btt.setY(optionsBoard.getTranslateY()+20/GameManager.ScaleY);
		quitGame_btt.setX(optionsBoard.getTranslateX() + optionsBoard.getFitWidth() - quitGame_btt.getFitWidth()-20/GameManager.ScaleX);
		quitGame_btt.setY(optionsBoard.getTranslateY()+20/GameManager.ScaleY);
		restart_btt.setX(continue_btt.getX() + continue_btt.getFitWidth()+23/GameManager.ScaleX);
		restart_btt.setY(continue_btt.getY());
	}
	public void restartLevel() {
		overlay.removeBlur();
		game.getScoreKeeper().resetTimer();
		center = true;
		swipeLeft = true;
		acceleration = 6.0f;
		confirmXPosition = 0.001f;
	}

	public void removeBoard() {
		confirmScreen.setVisible(false);
		optionsBoard.setVisible(false);
		continue_btt.setVisible(false);
		quitGame_btt.setVisible(false);
		restart_btt.setVisible(false);
		game.getMainRoot().getChildren().remove(scoreLayer);
		LEVEL_FAILED = false;
		confirmScreen.setX(0 - confirmScreen.getFitWidth() - 50);
		confirmX = 0;
		acceleration = 6.0f;
		center = false;
	}

	private void askConfirm() {
		game.getGameRoot().setEffect(null);
		confirmScreen.setImage(GameImageBank.game_over_trans_board);
		confirmX = (float) (0 - confirmScreen.getFitWidth() - 50);
		confirmXTwo = (float) (0 - optionsBoard.getFitWidth() - 100);
		confirmScreen.setX(GameSettings.WIDTH / 2 - confirmScreen.getFitWidth() / 2);
		confirmScreen.setY(GameSettings.HEIGHT / 2 - confirmScreen.getFitHeight() / 2);
		confirmScreenBack.setY(GameSettings.HEIGHT / 2 - confirmScreen.getFitHeight() / 2);
		confirmScreenBack.setVisible(true);	
		optionsBoard.setVisible(true);
		continue_btt.setVisible(true);
		quitGame_btt.setVisible(true);
		restart_btt.setVisible(true);
		game.getMainRoot().getChildren().add(scoreLayer);
		showScores = false;
		showWinner = false;
		counter = 0;
		opacityValue = 0.016;
		transitionOpacity = 0;
		center = true;
		swipeRight = true;
		acceleration = 8.0f;
		confirmXPosition = 0.002f;
	}

	public void blurOut() {
		overlay.addDeathBlur();
	}

	public void fadeOut(){
		game.getFadeScreenHandler().renderFadeScreen();
	}

	public void removeBlur() {
		overlay.removeBlur();
		PlayerTwo.LEVEL_COMPLETED = false;
		PlayerOne.LEVEL_COMPLETED = false;
		removeBoard();
	}
}
