package com.EudyContreras.Snake.UserInterface;

import com.EudyContreras.Snake.FrameWork.GameLoader;
import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.FrameWork.GameSettings;
import com.EudyContreras.Snake.Utilities.GameAudio;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class MainMenu {

	private int currentChoice = 1;
	private ImageView backgroundImage;
	private ImageView parallaxObject1;
	private ImageView parallaxObject2;
	private DropShadow dropShadowOne = new DropShadow();
	private DropShadow dropShadowTwo = new DropShadow();
	private GaussianBlur blur = new GaussianBlur();
	private Pane menuRoot = new Pane();
	private Pane mainMenu = new Pane();
	private VBox titleBox = new VBox();
	private VBox menuBox = new VBox();
	private Label gameTitle = new Label("Snake");
	private Label startLabel = new Label("Start Game");
	private Label gameModeLabel = new Label("Mode");
	private Label optionsLabel = new Label("Options");
	private Label multiplayerLabel = new Label("Multiplayer");
	private Label highScoreLabel = new Label("High Score");
	private Label exitLabel = new Label("Exit");
	private Rectangle clearUp, logo;
	private Pane fadeScreen;
	private Double radius = 63.0;
	private Double opacity = 1.0;
	private Boolean showMenu = false;
	private GameManager game;
	private float x, y, velX, velY, x2, y2, velX2, velY2;

	private MediaPlayer music;

	public MainMenu(GameManager game) {
		this.game = game;
		backgroundImage = new ImageView(MenuImageBank.mainMenuBackground);
//		y2 = 400;
//		x = (float) GameSettings.WIDTH / 3;
//		x2 = (float) (GameSettings.WIDTH);
//		velX = -1;
//		velX2 = -1;
        dropShadowTwo.setColor(Color.LIME);
        dropShadowTwo.setRadius(15);
        dropShadowTwo.setSpread(0.15);
        dropShadowTwo.setBlurType(BlurType.TWO_PASS_BOX);
		fadeScreen = new Pane();
		clearUp = new Rectangle(0, 0, GameSettings.WIDTH, GameSettings.HEIGHT);
		clearUp.setFill(Color.BLACK);
		setupLogo();
	}
	public void setupLogo(){
		logo = new Rectangle();
		logo.setEffect(dropShadowTwo);
		logo.setFill(new ImagePattern(MenuImageBank.gameLogo));
		logo.setWidth(MenuImageBank.gameLogo.getWidth()/GameLoader.ResolutionScaleX);
		logo.setHeight(MenuImageBank.gameLogo.getHeight()/GameLoader.ResolutionScaleY);
		logo.setX(GameSettings.WIDTH/2-logo.getWidth()/2);
		logo.setY(30);
	}
	public void addMusic() {
		music = GameAudio.getAudio("AudioResources/Jungle Loop.wav");
		music.play();
		music.setCycleCount(MediaPlayer.INDEFINITE);
	}

	public void playMusic() {
		if(music.getStatus() == Status.STOPPED){
		music = GameAudio.getAudio("AudioResources/Jungle Loop.wav");
		music.play();
		music.setCycleCount(MediaPlayer.INDEFINITE);
		}
	}

	public void stopMusic() {
		if(music.getStatus() == Status.PLAYING){
		music.stop();
		}
	}

	/**
	 * Sets up the main menu
	 */
	public void setupMainMenu() {
		setStyleOn(startLabel);
		setStyle(optionsLabel);
		setStyle(gameModeLabel);
		setStyle(multiplayerLabel);
		setStyle(highScoreLabel);
		setStyle(exitLabel);

		titleBox.getChildren().add(gameTitle);
		titleBox.setBackground(Background.EMPTY);
		titleBox.setPrefWidth(900);
		titleBox.maxWidth(900);
		titleBox.setMinWidth(900);
		titleBox.setAlignment(Pos.CENTER);
		gameTitle.setAlignment(Pos.CENTER);
		gameTitle.setPrefWidth(900);

		menuBox.setPadding(new Insets(20 / GameLoader.ResolutionScaleY, 20, 20, 20 / GameLoader.ResolutionScaleY));
		menuBox.getChildren().addAll(startLabel, optionsLabel, gameModeLabel, multiplayerLabel, highScoreLabel, exitLabel);
		menuBox.setBackground(Background.EMPTY);
		menuBox.setPrefWidth(500);
		menuBox.setMinWidth(500);
		menuBox.setPrefHeight(600);
		menuBox.setMinHeight(600);
		menuBox.setAlignment(Pos.CENTER);
		menuBox.setTranslateX(GameSettings.WIDTH / 2 - menuBox.getPrefWidth() / 2);
		menuBox.setTranslateY(GameSettings.HEIGHT / 2 - menuBox.getPrefHeight() / 2+50/GameManager.ScaleY);

		VBox.setMargin(startLabel,
				new Insets(40 / GameLoader.ResolutionScaleY, 20, 20, 15 / GameLoader.ResolutionScaleY));
		VBox.setMargin(gameModeLabel,
				new Insets(15 / GameLoader.ResolutionScaleY, 20, 20, 15 / GameLoader.ResolutionScaleY));
		VBox.setMargin(multiplayerLabel,
				new Insets(15 / GameLoader.ResolutionScaleY, 20, 20, 15 / GameLoader.ResolutionScaleY));
		VBox.setMargin(optionsLabel,
				new Insets(15 / GameLoader.ResolutionScaleY, 20, 20, 15 / GameLoader.ResolutionScaleY));
		VBox.setMargin(highScoreLabel,
				new Insets(15 / GameLoader.ResolutionScaleY, 20, 20, 15 / GameLoader.ResolutionScaleY));
		VBox.setMargin(exitLabel,
				new Insets(15 / GameLoader.ResolutionScaleY, 20, 20, 15 / GameLoader.ResolutionScaleY));


		startLabel.setAlignment(Pos.CENTER);
		optionsLabel.setAlignment(Pos.CENTER);
		gameModeLabel.setAlignment(Pos.CENTER);
		multiplayerLabel.setAlignment(Pos.CENTER);
		highScoreLabel.setAlignment(Pos.CENTER);
		exitLabel.setAlignment(Pos.CENTER);

		showMenu = true;
		titleBox.setTranslateX(GameSettings.WIDTH / 2 - titleBox.getPrefWidth() / 2);
		titleBox.setTranslateY(0);


		fadeScreen.getChildren().add(clearUp);
		mainMenu.getChildren().addAll(menuBox);
		menuRoot.getChildren().addAll(backgroundImage, logo, mainMenu, fadeScreen);
		game.setRoot(menuRoot);

		setKeyInputHandler();
		setMouseInputHandler();
	}

	public void transition() {
		if (showMenu) {
			opacity -= 0.01;
			clearUp.setOpacity(opacity);
			if (opacity <= 0) {
				opacity = 0.0;
				menuRoot.getChildren().remove(fadeScreen);
				showMenu = false;
			}
		}

		if (radius >= 0) {
			radius -= 1;
			menuRoot.setEffect(blur);
			blur.setRadius(radius);
		}
	}

	public void addBackgroundImages() {
		menuRoot.getChildren().addAll(backgroundImage, parallaxObject1, parallaxObject2);
	}

	public void setBackgroundObjects() {
		x = x + velX;
		y = y + velY;
		x2 = x2 + velX2;
		y2 = y2 + velY2;
		parallaxObject1.setX(x);
		parallaxObject1.setY(y);
		if (x < 0 - parallaxObject1.getImage().getWidth()) {
			x = (float) GameSettings.WIDTH;
		}
		parallaxObject2.setX(x2);
		parallaxObject2.setY(y2);
		if (x2 < 0 - parallaxObject2.getImage().getWidth()) {
			x2 = (float) GameSettings.WIDTH;
		}
	}

	/**
	 * Sets the standard style of the labels
	 *
	 * @param label
	 */
	private void setStyle(Label label) {
		label.setStyle("-fx-text-fill: limegreen; -fx-font-family: Impact; -fx-font-size: "
				+ 36 / GameLoader.ResolutionScaleY + "px;");
		label.setEffect(dropShadowOne);
	}
	private void setStyleOn(Label label){
		label.setStyle("-fx-text-fill: linear-gradient(lightgreen, seagreen); -fx-font-family: Impact; -fx-font-size: "
				+ 48 / GameLoader.ResolutionScaleY + "px");
	}
	private void setStyleOff(Label label){
		label.setStyle("-fx-text-fill: limegreen; -fx-font-family: Impact; -fx-font-size: "
				+ 36 / GameLoader.ResolutionScaleY + "px;");

	}
	/**
	 * Sets the key input handling for the labels
	 */
	private void setKeyInputHandler() {

		/*
		 * Code below determines what will happen if the user presses enter or
		 * space on the different choices
		 */
		game.getScene().setOnKeyPressed(e -> {
			switch (e.getCode()) {
			case UP:
				currentChoice -= 1;
				if (currentChoice < 1) {
					currentChoice = 1;
				}
				break;
			case DOWN:
				currentChoice += 1;
				if (currentChoice > 6) {
					currentChoice = 6;
				}
				break;
			case W:
				currentChoice -= 1;
				if (currentChoice < 1) {
					currentChoice = 1;
				}
				break;
			case S:
				currentChoice += 1;
				if (currentChoice > 6) {
					currentChoice = 6;
				}
				break;
			case ENTER:
				if (currentChoice == 1) {
					startSelected();
				}
				if (currentChoice == 2) {

				}
				if (currentChoice == 3) {

				}
				if (currentChoice == 4) {
					// highScore();
				}
				if (currentChoice == 5) {

				}
				if (currentChoice == 6) {
					closeGame();
				}
				break;
			case SPACE:
				if (currentChoice == 1) {
					startSelected();
				}
				if (currentChoice == 2) {
					OptionsMenu();
				}
				if (currentChoice == 3) {
					multiplayerMenu();
				}
				if (currentChoice == 4) {
					// highScore();
				}
				if (currentChoice == 5) {

				}
				if (currentChoice == 6) {
					closeGame();
				}
			default:
				break;
			}

			/*
			 * Code below determines the styling of the different labels if the
			 * user has toggled to that choice
			 */
			if (currentChoice == 1) {
				setStyleOn(startLabel);
				setStyleOff(optionsLabel);
				setStyleOff(multiplayerLabel);
				setStyleOff(highScoreLabel);
				setStyleOff(exitLabel);
				setStyleOff(gameModeLabel);
			} else if (currentChoice == 2) {
				setStyleOff(startLabel);
				setStyleOn(optionsLabel);
				setStyleOff(multiplayerLabel);
				setStyleOff(highScoreLabel);
				setStyleOff(exitLabel);
				setStyleOff(gameModeLabel);
			} else if (currentChoice == 3) {
				setStyleOn(gameModeLabel);
				setStyleOff(startLabel);
				setStyleOff(optionsLabel);
				setStyleOff(multiplayerLabel);
				setStyleOff(highScoreLabel);
				setStyleOff(exitLabel);
			} else if (currentChoice == 4) {
				setStyleOff(startLabel);
				setStyleOff(optionsLabel);
				setStyleOn(multiplayerLabel);
				setStyleOff(highScoreLabel);
				setStyleOff(exitLabel);
				setStyleOff(gameModeLabel);
			} else if (currentChoice == 5) {
				setStyleOff(startLabel);
				setStyleOff(optionsLabel);
				setStyleOff(multiplayerLabel);
				setStyleOn(highScoreLabel);
				setStyleOff(exitLabel);
				setStyleOff(gameModeLabel);
			} else if (currentChoice == 6) {
				setStyleOff(startLabel);
				setStyleOff(optionsLabel);
				setStyleOff(multiplayerLabel);
				setStyleOff(highScoreLabel);
				setStyleOff(gameModeLabel);
				setStyleOn(exitLabel);
			}
		});
	}

	/**
	 * Sets the mouse input handling for the labels
	 */
	public void setMouseInputHandler() {
		/**
		 * Code below determines what will happen if the mouse presses one of
		 * the different rectangles
		 */
		startLabel.setOnMousePressed(e -> {
			startSelected();
		});
		optionsLabel.setOnMousePressed(e -> {
			OptionsMenu();
		});
		gameModeLabel.setOnMousePressed(e -> {

		});
		multiplayerLabel.setOnMousePressed(e -> {
			multiplayerMenu();
		});
		highScoreLabel.setOnMousePressed(e -> {

		});
		exitLabel.setOnMousePressed(e -> {
			closeGame();
		});

		/**
		 * Code below determines the style of the labels if the mouse enters one
		 * of the rectangles
		 */
		startLabel.setOnMouseEntered(e -> {
			setStyleOn(startLabel);
			setStyleOff(optionsLabel);
			setStyleOff(multiplayerLabel);
			setStyleOff(highScoreLabel);
			setStyleOff(exitLabel);
			setStyleOff(gameModeLabel);
		});
		optionsLabel.setOnMouseEntered(e -> {
			setStyleOff(startLabel);
			setStyleOn(optionsLabel);
			setStyleOff(multiplayerLabel);
			setStyleOff(highScoreLabel);
			setStyleOff(exitLabel);
			setStyleOff(gameModeLabel);
		});
		gameModeLabel.setOnMouseEntered(e -> {
			setStyleOff(startLabel);
			setStyleOff(optionsLabel);
			setStyleOff(multiplayerLabel);
			setStyleOff(highScoreLabel);
			setStyleOff(exitLabel);
			setStyleOn(gameModeLabel);
		});
		multiplayerLabel.setOnMouseEntered(e -> {
			setStyleOff(startLabel);
			setStyleOff(optionsLabel);
			setStyleOn(multiplayerLabel);
			setStyleOff(highScoreLabel);
			setStyleOff(exitLabel);
			setStyleOff(gameModeLabel);
		});
		highScoreLabel.setOnMouseEntered(e -> {
			setStyleOff(startLabel);
			setStyleOff(optionsLabel);
			setStyleOff(multiplayerLabel);
			setStyleOn(highScoreLabel);
			setStyleOff(exitLabel);
			setStyleOff(gameModeLabel);
		});
		exitLabel.setOnMouseEntered(e -> {
			setStyleOff(startLabel);
			setStyleOff(optionsLabel);
			setStyleOff(multiplayerLabel);
			setStyleOff(highScoreLabel);
			setStyleOff(gameModeLabel);
			setStyleOn(exitLabel);
		});
	}

	/**
	 * Sets up key input handling and mouse input handling for the main menu.
	 * Shows the cursor, sets the root of the game to menu root and pauses the
	 * game.
	 */
	public void setMainMenu() {
		menuRoot.getChildren().add(fadeScreen);
		showMenu = true;
		opacity = 1.0;
		clearUp.setOpacity(opacity);
		setMouseInputHandler();
		setKeyInputHandler();
		game.showCursor(true, game.getScene());
		game.setRoot(menuRoot);
		game.pauseGame();
	}

	/**
	 * Starts the game if the startbutton is pressed
	 */
	private void startSelected() {
		menuRoot.getChildren().remove(fadeScreen);
		game.showCursor(false, game.getScene());
		game.setRoot(game.getMainRoot());
		game.resumeGame();
		game.processGameInput();
	}

	/**
	 * Sets up the optionsmenu
	 */
	private void OptionsMenu() {

	}

	private void multiplayerMenu() {

	}

	public void setCurrentChoice(int choice) {
		currentChoice = choice;
	}

	private void closeGame() {
		game.closeGame();
	}

	public Pane getMenuRoot() {
		return menuRoot;
	}

	public void setMenuRoot(Pane menuRoot) {
		this.menuRoot = menuRoot;
	}
}
