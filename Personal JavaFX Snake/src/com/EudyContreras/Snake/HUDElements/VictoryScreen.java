package com.EudyContreras.Snake.HUDElements;

import com.EudyContreras.Snake.AbstractModels.AbstractHudElement;
import com.EudyContreras.Snake.ClassicSnake.ClassicSnake;
import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.FrameWork.GameSettings;
import com.EudyContreras.Snake.FrameWork.ResizeHelper;
import com.EudyContreras.Snake.Identifiers.GameStateID;
import com.EudyContreras.Snake.ImageBanks.GameImageBank;
import com.EudyContreras.Snake.PlayerOne.PlayerOne;
import com.EudyContreras.Snake.PlayerTwo.PlayerTwo;
import com.EudyContreras.Snake.Utilities.ScreenEffectUtility;

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

public class VictoryScreen extends AbstractHudElement {

	public static boolean LEVEL_COMPLETE = false;
	private ScreenEffectUtility overlay;
	private LocalScoreScreen scoreScreen;
	private TranslateTransition translateTransition;
	private TranslateTransition translateTransitionTwo;
	private RotateTransition rotateTransition;
	private ScaleTransition scaleTransition;
	private ScaleTransition scaleTransitionTwo;
	private GameManager game;
	private DropShadow borderGlow;
	private ImageView sceneSnapshot;
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
	private double confirmX = 0;
	private double confirmXTwo = 0;
	private double opacityLevel = 0;
	private double confirmXPosition = 0;
	private double transitionOpacity = 1;
	private double opacityValue = -0.016;
	private double acceleration = 0.3f;
	private boolean transitioning = true;
	private boolean showTransition = false;
	private boolean showWinner = false;
	private boolean showScores = false;
	private boolean goToNext = false;
	private boolean restartLevel = false;
	private boolean swipeRight = false;
	private boolean swipeLeft = false;
	private boolean allowSlowMo = false;
	private boolean panIn = false;
	private boolean center = true;

	/**
	 * Main constructur which takes an instance of the main game class along
	 * with with the base image of this board and the elements dimensions.
	 *
	 * @param game:
	 *            main game class which connects this class to all other classes
	 * @param boardImage:
	 *            Image which will be used as base for this board
	 * @param width:
	 *            Horizontal dimension of this board
	 * @param height:
	 *            Vertival dimension of this board
	 */
	public VictoryScreen(GameManager game, Image boardImage, double width, double height) {
		this.game = game;
		this.overlay = game.getOverlayEffect();
		this.scoreLayer = new Pane();
		this.baseBoardImage = boardImage;
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
	 * Method which initializes most of the UI elements used by this board Most
	 * elements are also asigned fills and dimensions within this method
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
		mainGameBoard.setY(GameSettings.HEIGHT / 2 - mainGameBoard.getFitHeight() / 2 - 30);
		scoreScreen = new LocalScoreScreen(game, 0, 0, 0, 0, scoreLayer);
		continue_btt = new ImageView(GameImageBank.continue_button);
		quitGame_btt = new ImageView(GameImageBank.quit_button);
		restart_btt = new ImageView(GameImageBank.restart_button);
		optionsBoard = new ImageView(GameImageBank.options_board);
		optionsBoard.setFitWidth(800);
		optionsBoard.setFitHeight(450 / 4);
		continue_btt.setFitWidth(240);
		continue_btt.setFitHeight(70);
		quitGame_btt.setFitWidth(240);
		quitGame_btt.setFitHeight(70);
		restart_btt.setFitWidth((continue_btt.getFitWidth()));
		restart_btt.setFitHeight(quitGame_btt.getFitHeight());
		scoreLayer.getChildren().addAll(baseGameBoard, mainGameBoard, optionsBoard, continue_btt, quitGame_btt, restart_btt);
		processMouseHandling();
	}

	@SuppressWarnings("unused")
	private void initTransitions(){
		 translateTransition = new TranslateTransition(Duration.millis(1000),optionsBoard);
		 translateTransitionTwo = new TranslateTransition(Duration.millis(1000), baseGameBoard);
		 rotateTransition = new RotateTransition(Duration.millis(1000), baseGameBoard);
		 scaleTransition = new ScaleTransition(Duration.millis(1000), baseGameBoard);
		 scaleTransitionTwo = new ScaleTransition(Duration.millis(1000), mainGameBoard);
	}
	/**
	 * Method which is to be called once any of the players have compleated the
	 * game or if all the apples have been collected. This method marks the end
	 * of the game being played
	 */

	public void endGame() {
		game.showCursor(true, game.getScene());
		game.setStateID(GameStateID.GAME_OVER);
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
			} else if (game.getGameLoader().getPlayerOne().getAppleCount() == game.getGameLoader().getPlayerTwo()
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
//		game.getGameHud().showHUDCover();
//		game.getScoreKeeper().swipeDown();
	}

	/**
	 * Method which processes events within the buttons used by the options
	 * board of this game board. This method processes all mouse input within
	 * these buttons and ensures to show visual ques when the buttons are
	 * selected
	 */
	private void processMouseHandling() {
		continue_btt.setOnMouseEntered(e -> {
			selectionReset();
			currentChoice = 1;
			borderGlow.setColor(Color.rgb(0, 240, 0));
			continue_btt.setEffect(borderGlow);
		});
		continue_btt.setOnMouseExited(e -> {
			continue_btt.setEffect(null);
		});
		continue_btt.setOnMouseClicked(e -> {
			game.setStateID(GameStateID.LEVEL_TRANSITIONING);
			if(!transitioning)
			goToNext();
		});
		quitGame_btt.setOnMouseEntered(e -> {
			selectionReset();
			currentChoice = 2;
			borderGlow.setColor(Color.rgb(240, 0, 0));
			quitGame_btt.setEffect(borderGlow);
		});
		quitGame_btt.setOnMouseExited(e -> {
			quitGame_btt.setEffect(null);
		});
		quitGame_btt.setOnMouseClicked(e -> {
			game.setStateID(GameStateID.MAIN_MENU);
			if(!transitioning)
			game.getFadeScreenHandler().menu_fade_screen();
		});
		restart_btt.setOnMouseEntered(e -> {
			currentChoice = 3;
			selectionReset();
			borderGlow.setColor(Color.rgb(240, 150, 0));
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
	 * Sets the key input handling for the buttons used by this game board The
	 * code below determines what will happen if the user presses enter or space
	 * on the different choices. The method also takes care of showing visual
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
				if (!e.isControlDown()) {
					if (currentChoice == 1) {
						game.setStateID(GameStateID.LEVEL_TRANSITIONING);
						if (!transitioning)
							goToNext();
					}
					if (currentChoice == 2) {
						game.setStateID(GameStateID.LEVEL_RESTART);
						if (!transitioning)
							restartLevel();
					}
					if (currentChoice == 3) {
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
					game.setStateID(GameStateID.LEVEL_TRANSITIONING);
					if(!transitioning)
					goToNext();
				}
				if (currentChoice == 2) {
					game.setStateID(GameStateID.LEVEL_RESTART);
					if(!transitioning)
					restartLevel();
				}
				if (currentChoice == 3) {
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
	 * Method which updates the visual ques used in order to show which button
	 * is selected
	 */
	private void updateSelections() {
		if (currentChoice == 1) {
			borderGlow.setColor(Color.rgb(0, 240, 0));
			continue_btt.setEffect(borderGlow);
			restart_btt.setEffect(null);
			quitGame_btt.setEffect(null);
		}
		if (currentChoice == 2) {
			borderGlow.setColor(Color.rgb(240, 150, 0));
			restart_btt.setEffect(borderGlow);
			quitGame_btt.setEffect(null);
			continue_btt.setEffect(null);
		}
		if (currentChoice == 3) {
			borderGlow.setColor(Color.rgb(240, 0, 0));
			quitGame_btt.setEffect(borderGlow);
			restart_btt.setEffect(null);
			continue_btt.setEffect(null);
		}
	}

	/**
	 * Method used to reset the visual que of of all buttons by removing said
	 * visual que
	 */
	private void selectionReset() {
		continue_btt.setEffect(null);
		restart_btt.setEffect(null);
		quitGame_btt.setEffect(null);
	}

	/**
	 * Method which when called updates various elements of this game board by
	 * further calling methods which will translate or transform said elements
	 */
	public void updateUI() {
		if (game.getStateID() != GameStateID.GAMEPLAY) {
			positionScoreScreen();
			positionScreen();
			showScores();
			swipeRight();
			panIn();
			hide();
			setSlowMotion();
		}
	}

	/**
	 * Method which when called will show the board and will animate the board
	 * with a range of transitions .
	 */
	@SuppressWarnings("unused")
	private void showTheBoard() {
		baseGameBoard.setTranslateX(0);

		rotateTransition.setFromAngle(0);
		rotateTransition.setToAngle(360f * 3);
		rotateTransition.setCycleCount(1);
		rotateTransition.setOnFinished(event -> {
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
			scaleTransition.stop();
			rotateTransition.stop();
		});

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

		scaleTransition.play();
		rotateTransition.play();
		translateTransition.play();
		baseGameBoard.setVisible(true);
		optionsBoard.setVisible(true);
		continue_btt.setVisible(true);
		quitGame_btt.setVisible(true);
		restart_btt.setVisible(true);
	}

	/**
	 * Method which when called will show the board and will animate the board
	 * with a range of transitions .
	 */
	@SuppressWarnings("unused")
	private void hideTheBoard() {
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

		translateTransitionTwo.setToX(0 - baseGameBoard.getFitWidth() * 2);
		translateTransitionTwo.setFromX(baseGameBoard.getTranslateX());
		translateTransitionTwo.setCycleCount(1);
		translateTransitionTwo.setOnFinished(event -> {
			game.processGameInput();
			if (restartLevel) {
				game.getFadeScreenHandler().quick_restart_fade_screen();
				PlayerOne.LEVEL_COMPLETED = false;
				PlayerTwo.LEVEL_COMPLETED = false;
				ClassicSnake.LEVEL_COMPLETED = false;
			} else if (goToNext) {
				game.getFadeScreenHandler().continue_fade_screen();
			}
			translateTransition.stop();
			translateTransitionTwo.stop();
		});

		translateTransition.play();
		translateTransitionTwo.play();
		mainGameBoard.setVisible(true);
		baseGameBoard.setVisible(true);
		optionsBoard.setVisible(true);
		continue_btt.setVisible(true);
		quitGame_btt.setVisible(true);
		restart_btt.setVisible(true);

	}
	public void swipeRight() {
		if (swipeRight == true) {
			optionsBoard.setX(confirmX);
			confirmX += confirmXPosition ;
			confirmXPosition += acceleration;
			if (center) {
				acceleration -= 0.80;
				if (acceleration <= 0) {

					acceleration = 0;
					confirmXPosition -= 0.5;
					if (confirmXPosition <= 0.30) {
						confirmXPosition = 0.30;
					}

				}
				if (confirmX >= GameSettings.WIDTH / 2 - 800 / 2) {
					confirmX = GameSettings.WIDTH / 2 - 800 / 2;
					optionsBoard.setX(confirmX);
					confirmXPosition = 0;
					acceleration = 0;
					currentChoice = 1;
					center = false;
					swipeRight = false;
					processKeyHandling();
				}
			}
			continue_btt.setX(optionsBoard.getX() + 20 );
			continue_btt.setY(optionsBoard.getY() + 20);
			quitGame_btt.setX(optionsBoard.getX() + optionsBoard.getFitWidth() - quitGame_btt.getFitWidth()
					- 20 );
			quitGame_btt.setY(optionsBoard.getY() + 20);
			restart_btt.setX(continue_btt.getX() + continue_btt.getFitWidth() + 23 );
			restart_btt.setY(continue_btt.getY());
		}
	}

	public void hide() {
		if (swipeLeft == true) {
			positionScoreScreen();
			baseGameBoard.setX(confirmXTwo);
			optionsBoard.setX(confirmX);
			confirmX -= confirmXPosition ;
			confirmXTwo -= confirmXPosition ;
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
					game.processGameInput();
					scoreScreen.hideScores();
					if (restartLevel) {
						game.getFadeScreenHandler().quick_restart_fade_screen();
						PlayerOne.LEVEL_COMPLETED = false;
						PlayerTwo.LEVEL_COMPLETED = false;
						ClassicSnake.LEVEL_COMPLETED = false;
					} else if (goToNext) {
						game.getFadeScreenHandler().continue_fade_screen();
					}
					center = false;
				}
			}
			continue_btt.setX(optionsBoard.getX() + 20);
			continue_btt.setY(optionsBoard.getY() + 20);
			quitGame_btt.setX(optionsBoard.getX() + optionsBoard.getFitWidth() - quitGame_btt.getFitWidth() - 20);
			quitGame_btt.setY(optionsBoard.getY() + 20);
			restart_btt.setX(continue_btt.getX() + continue_btt.getFitWidth() + 20);
			restart_btt.setY(continue_btt.getY());
		}
	}

	public void panIn() {
		if (panIn) {
			this.rOne = rOne + velROne;
			this.velROne = velROne*.995;
			this.widthOne += width * 0.015;
			this.heightOne += height * 0.015;
			this.mainGameBoard.setOpacity(1);
			this.baseGameBoard.setFitWidth(widthOne);
			this.baseGameBoard.setFitHeight(heightOne);
			this.baseGameBoard.setRotate(rOne);
			this.baseGameBoard.setX(GameSettings.WIDTH / 2 - baseGameBoard.getFitWidth() / 2);
			this.baseGameBoard.setY(GameSettings.HEIGHT / 2 - baseGameBoard.getFitHeight() / 2);
			if (widthOne >= width) {
				widthOne = width;
			}
			if (heightOne >= height) {
				;
				heightOne = height;
			}
			if (opacityLevel >= 1) {
				opacityLevel = 1.0;
			}
			if (velROne > 2) {
				velROne -= 0.10;
			}
			if (widthOne >= width - 20 && heightOne >= height - 20) {
				if (rOne >= 360 || rOne <= 0) {
					velROne = 0;
					rOne = 0;
					baseGameBoard.setRotate(rOne);
					processPlayerScores();
					processKeyHandling();
					overlay.addScreenShake(game.getGameRoot(),1.2,25, true, true);
					overlay.addNodeShake(baseGameBoard, 0.6);
					blurOut();
					counter = 0;
					transitionOpacity = 0;
					opacityValue = 0.016;
					waitTime = 10;
					mainGameBoard.setOpacity(0);
					mainGameBoard.setVisible(true);
					scoreScreen.showScores();
					panIn = false;
					transitioning = false;
					showWinner = true;
					showScores = false;
				}
			} else {
				if (rOne >= 360) {
					rOne = 0;
				}
			}
		}
	}
	@SuppressWarnings("unused")
	private void zoomAnimation(){
		this.baseGameBoard.setFitWidth(widthOne);
		this.baseGameBoard.setFitHeight(heightOne);
		this.mainGameBoard.setFitWidth(widthOne);
		this.mainGameBoard.setFitHeight(heightOne);
	}
	/**
	 * method used to both update the opacity of the main score board shown
	 * after the game ends. This method also produces a transition which will
	 * change the image shown each time the board reaches its minimum opacity
	 * level
	 */
	private void showScores() {
		if (showScores == true) {
			mainGameBoard.setOpacity(transitionOpacity);
			counter++;
			if (counter >= waitTime) {
				counter = waitTime;
				showTransition = true;
			}
			if (showTransition == true) {
				mainGameBoard.setImage(scoreBoardImage);
				scoreScreen.setScoreOpacity(transitionOpacity);
				transitionOpacity += opacityValue;
				if (transitionOpacity >= 1 && opacityValue > 0) {
					scoreScreen.setScoreOpacity(transitionOpacity);
					transitionOpacity = 1;
					opacityValue = -0.010;
				}
				if (transitionOpacity <= 0 && opacityValue < 0) {
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
		if (showWinner == true) {
			mainGameBoard.setOpacity(transitionOpacity);
			counter++;
			if (counter >= waitTime) {
				counter = waitTime;
				showTransition = true;
			}
			if (showTransition == true) {
				mainGameBoard.setImage(winnerBoardImage);
				transitionOpacity += opacityValue;
				if (transitionOpacity >= 1 && opacityValue > 0) {
					transitionOpacity = 1;
					opacityValue = -0.010;
				}
				if (transitionOpacity <= 0 && opacityValue < 0) {
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
	 * Method which collects the scores to be shown by the local score screen
	 * and positions the scores at a desired position relative to the game board
	 * of this class
	 */
	private void processPlayerScores() {
		scoreScreen.setScores();
		scoreScreen.relocateScoreOne(mainGameBoard.getX() + 135,
				mainGameBoard.getY() + mainGameBoard.getFitHeight() / 1.3);
		scoreScreen.relocateScoreTwo(mainGameBoard.getX() + mainGameBoard.getFitWidth() / 2 + 25,
				mainGameBoard.getY() + mainGameBoard.getFitHeight() / 1.3);
	}

	/**
	 * Method which updates the position of the local score so that it remains
	 * at a desired location relative to the board element of this class
	 */
	private void positionScoreScreen() {
		scoreScreen.relocateScoreOne(mainGameBoard.getX() + 135,
				mainGameBoard.getY() + mainGameBoard.getFitHeight() / 1.3);
		scoreScreen.relocateScoreTwo(mainGameBoard.getX() + mainGameBoard.getFitWidth() / 2 + 25,
				mainGameBoard.getY() + mainGameBoard.getFitHeight() / 1.3);
	}

	/**
	 * Method which updates teh position of the various UI elements used by this
	 * class in order to keeps these elements relative to the main board of this
	 * class
	 */
	private void positionScreen() {
		mainGameBoard.setX(baseGameBoard.getX());
		mainGameBoard.setY(baseGameBoard.getY());
		mainGameBoard.setFitWidth(baseGameBoard.getFitWidth());
		mainGameBoard.setFitHeight(baseGameBoard.getFitHeight());
	}

	/**
	 * Method which is called if the player decides to restart the level
	 */
	private void restartLevel() {
		transitioning = true;
		showSceneSnap();
		game.removePlayers();
		confirmXTwo = baseGameBoard.getX();
		restartLevel = true;
		goToNext = false;
		center = true;
		swipeLeft = true;
		acceleration = 6.0f;
		confirmXPosition = 0.002f;
	}

	/**
	 * Method which is called if the player decides to continue to the next
	 * level of the game.
	 */
	private void goToNext() {
		transitioning = true;
		showSceneSnap();
		game.removePlayers();
		confirmXTwo = baseGameBoard.getX();
		goToNext = true;
		restartLevel = false;
		center = true;
		swipeLeft = true;
		acceleration = 6.0f;
		confirmXPosition = 0.002f;
	}

	/**
	 * Method which removes this board from the main root of the game and makes
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
		confirmX = 0;
		acceleration = 6.0f;
		center = false;
	}
	public void show(boolean state){
		mainGameBoard.setVisible(state);
		baseGameBoard.setVisible(state);
		optionsBoard.setVisible(state);
		continue_btt.setVisible(state);
		quitGame_btt.setVisible(state);
		restart_btt.setVisible(state);
		scoreScreen.show(state);
	}
	/**
	 * Method which resets the board along with most of the other UI elements of
	 * this board allowing this board to be reused next time the game ends
	 */
	private void resetBoard() {
		mainGameBoard.setOpacity(0);
		game.setStateID(GameStateID.LEVEL_COMPLETED);
		baseGameBoard.setImage(baseBoardImage);
		mainGameBoard.setImage(baseBoardImage);
		confirmX = 0 - width - 50;
		baseGameBoard.setX(-1000);
		baseGameBoard.setY(-1000);
		baseGameBoard.setFitWidth(0);
		baseGameBoard.setFitHeight(0);
		game.getMainRoot().getChildren().add(scoreLayer);
		scoreScreen.hideScores();
		showTransition = false;
		allowSlowMo = true;
		swipeRight = true;
		showScores = false;
		showWinner = false;
		center = true;
		panIn = true;
		counter = 0;
		opacityValue = 0.016;
		transitionOpacity = 0;
		widthOne = 1;
		heightOne = 1;
		opacityLevel = 0;
		velROne = 36;
		acceleration = 8.0f;
		confirmXPosition = 0.002f;
		baseGameBoard.setVisible(true);
		optionsBoard.setVisible(true);
		continue_btt.setVisible(true);
		quitGame_btt.setVisible(true);
		restart_btt.setVisible(true);
	}

	/**
	 * Method which when called will blur all the elements behind all the UI
	 * elements used by this class
	 */
	public void blurOut() {
		overlay.levelCompleteBlur();
	}

	/**
	 * Method which when called will removed all blur from screen.
	 */
	public void removeBlur() {
		overlay.removeBlur();
		PlayerTwo.LEVEL_COMPLETED = false;
		PlayerOne.LEVEL_COMPLETED = false;
		ClassicSnake.LEVEL_COMPLETED = false;
		removeBoard();
	}
}
