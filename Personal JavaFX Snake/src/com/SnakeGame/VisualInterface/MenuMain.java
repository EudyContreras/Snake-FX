package com.SnakeGame.VisualInterface;

import com.SnakeGame.Core.GameLoader;
import com.SnakeGame.Core.Settings;
import com.SnakeGame.Core.SnakeGame;
import com.SnakeGame.Utilities.GameAudio;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MenuMain {

	private int currentChoice = 1;
	private ImageView backgroundImage;
	private ImageView parallaxObject1;
	private ImageView parallaxObject2;
	private GaussianBlur blur = new GaussianBlur();
	private Label gameTitle = new Label("Snake");
	private Label startLabel = new Label("Start Game");
	private Label optionsLabel = new Label("Options");
	private Label multiplayerLabel = new Label("Multiplayer");
	private Label highScoreLabel = new Label("High Score");
	private Label exitLabel = new Label("Exit");
	private Rectangle r1, r2, r3, r4, r5;
	private VBox menuBox = new VBox();
	private VBox boundBox = new VBox();
	private VBox titleBox = new VBox();
	private Rectangle clearUp;
	private Pane fadeScreen;
	private Double radius = 63.0;
	private boolean showMenu = false;
	private double opacity = 1.0;
	private DropShadow borderGlow = new DropShadow();
	private Pane menuRoot = new Pane();
	private SnakeGame game;
	private float x, y, velX, velY, x2, y2, velX2, velY2;
	private MenuOptions optionsMenu;
	private MenuMultiplayer multiplayerMenu;
	private MediaPlayer music;

	public MenuMain(SnakeGame game) {
		this.game = game;
		optionsMenu = new MenuOptions(this, game);
		multiplayerMenu = new MenuMultiplayer(this, game);
		backgroundImage = new ImageView(MenuImageBank.mainMenuBackground);
		y2 = 400;
		x = (float) Settings.WIDTH / 3;
		x2 = (float) (Settings.WIDTH);
		velX = -1;
		velX2 = -1;
		fadeScreen = new Pane();
		clearUp = new Rectangle(0, 0, Settings.WIDTH, Settings.HEIGHT);
		clearUp.setFill(Color.BLACK);
		// addMusic();
	}

	public void addMusic() {
		music = GameAudio.getAudio("AudioResources/Jungle Loop.wav");
		music.play();
		music.setCycleCount(MediaPlayer.INDEFINITE);
	}

	public void playMusic() {
		// if(music.getStatus() == Status.STOPPED){
		music = GameAudio.getAudio("AudioResources/Jungle Loop.wav");
		music.play();
		music.setCycleCount(MediaPlayer.INDEFINITE);
		// }
	}

	public void stopMusic() {
		// if(music.getStatus() == Status.PLAYING){
		music.stop();
		// }
	}

	/**
	 * Sets up the main menu
	 */
	public void setupMainMenu() {
		setTitleStyle(gameTitle);
		setStyle(startLabel);
		setStyle(optionsLabel);
		setStyle(multiplayerLabel);
		setStyle(highScoreLabel);
		setStyle(exitLabel);

		titleBox.getChildren().add(gameTitle);
		// titleBox.getChildren().add(new Rectangle(900,200,new
		// ImagePattern(MenuImageBank.startLogo)));
		titleBox.setBackground(Background.EMPTY);
		titleBox.setPrefWidth(900);
		titleBox.maxWidth(900);
		titleBox.setMinWidth(900);
		titleBox.setAlignment(Pos.CENTER);
		gameTitle.setAlignment(Pos.CENTER);
		gameTitle.setPrefWidth(900);

		menuBox.setPadding(new Insets(20 / GameLoader.ResolutionScaleY, 20, 20, 20 / GameLoader.ResolutionScaleY));
		menuBox.getChildren().addAll(startLabel, optionsLabel, multiplayerLabel, highScoreLabel, exitLabel);
		menuBox.setBackground(Background.EMPTY);
		menuBox.setPrefWidth(500);
		menuBox.setMinWidth(500);
		menuBox.setPrefHeight(600);
		menuBox.setMinHeight(600);
		menuBox.setAlignment(Pos.CENTER);
		menuBox.setTranslateX(Settings.WIDTH / 2 - menuBox.getPrefWidth() / 2);
		menuBox.setTranslateY(Settings.HEIGHT / 2 - menuBox.getPrefHeight() / 2);
		VBox.setMargin(startLabel,
				new Insets(20 / GameLoader.ResolutionScaleY, 20, 20, 20 / GameLoader.ResolutionScaleY));
		VBox.setMargin(multiplayerLabel,
				new Insets(20 / GameLoader.ResolutionScaleY, 20, 20, 20 / GameLoader.ResolutionScaleY));
		VBox.setMargin(optionsLabel,
				new Insets(20 / GameLoader.ResolutionScaleY, 20, 20, 20 / GameLoader.ResolutionScaleY));
		VBox.setMargin(highScoreLabel,
				new Insets(20 / GameLoader.ResolutionScaleY, 20, 20, 20 / GameLoader.ResolutionScaleY));
		VBox.setMargin(exitLabel,
				new Insets(20 / GameLoader.ResolutionScaleY, 20, 20, 20 / GameLoader.ResolutionScaleY));

		startLabel.setStyle("-fx-text-fill: #A5DF00; -fx-font-family: Impact; -fx-font-size: "
				+ 36 / GameLoader.ResolutionScaleY + "px");
		startLabel.setAlignment(Pos.CENTER);
		optionsLabel.setAlignment(Pos.CENTER);
		highScoreLabel.setAlignment(Pos.CENTER);
		exitLabel.setAlignment(Pos.CENTER);

		r1 = new Rectangle(Settings.WIDTH / 2 - 250 / 2, menuBox.getTranslateY() / GameLoader.ResolutionScaleY, 250,
				50 / GameLoader.ResolutionScaleY);
		r1.setFill(Color.rgb(255, 0, 0, 0.5));
		// r1.setFill(Color.TRANSPARENT);
		r2 = new Rectangle(Settings.WIDTH / 2 - 250 / 2,
				r1.getY() + r1.getHeight() + (20 / GameLoader.ResolutionScaleY), 250, 50 / GameLoader.ResolutionScaleY);
		r2.setFill(Color.rgb(255, 0, 0, 0.5));
		// r2.setFill(Color.TRANSPARENT);
		r3 = new Rectangle(Settings.WIDTH / 2 - 250 / 2,
				r2.getY() + r2.getHeight() + (20 / GameLoader.ResolutionScaleY), 250, 50 / GameLoader.ResolutionScaleY);
		r3.setFill(Color.rgb(255, 0, 0, 0.5));
		// r3.setFill(Color.TRANSPARENT);
		r4 = new Rectangle(Settings.WIDTH / 2 - 250 / 2,
				r3.getY() + r3.getHeight() + (20 / GameLoader.ResolutionScaleY), 250, 50 / GameLoader.ResolutionScaleY);
		r4.setFill(Color.rgb(255, 0, 0, 0.5));
		// r4.setFill(Color.TRANSPARENT);
		r5 = new Rectangle(Settings.WIDTH / 2 - 250 / 2,
				r4.getY() + r4.getHeight() + (20 / GameLoader.ResolutionScaleY), 250, 50 / GameLoader.ResolutionScaleY);
		r5.setFill(Color.rgb(255, 0, 0, 0.5));
		// r4.setFill(Color.TRANSPARENT);
		showMenu = true;
		titleBox.setTranslateX(Settings.WIDTH / 2 - titleBox.getPrefWidth() / 2);
		titleBox.setTranslateY(0);

		boundBox.setPadding(new Insets(20 / GameLoader.ResolutionScaleY, 20, 20, 20 / GameLoader.ResolutionScaleY));

		boundBox.getChildren().addAll(r1, r2, r3, r4, r5);
		boundBox.setBackground(Background.EMPTY);
		boundBox.setPrefWidth(500);
		boundBox.setMinWidth(500);
		boundBox.setPrefHeight(600);
		boundBox.setMinHeight(600);
		boundBox.setAlignment(Pos.CENTER);
		boundBox.setTranslateX(Settings.WIDTH / 2 - boundBox.getPrefWidth() / 2);
		boundBox.setTranslateY(Settings.HEIGHT / 2 - boundBox.getPrefHeight() / 2);
		VBox.setMargin(r1, new Insets(20 / GameLoader.ResolutionScaleY, 20 / GameLoader.ResolutionScaleY, 20,
				20 / GameLoader.ResolutionScaleY));
		VBox.setMargin(r2, new Insets(15 / GameLoader.ResolutionScaleY, 20 / GameLoader.ResolutionScaleY, 20,
				20 / GameLoader.ResolutionScaleY));
		VBox.setMargin(r3, new Insets(15 / GameLoader.ResolutionScaleY, 20 / GameLoader.ResolutionScaleY, 20,
				20 / GameLoader.ResolutionScaleY));
		VBox.setMargin(r4, new Insets(15 / GameLoader.ResolutionScaleY, 20 / GameLoader.ResolutionScaleY, 20,
				20 / GameLoader.ResolutionScaleY));
		VBox.setMargin(r5, new Insets(15 / GameLoader.ResolutionScaleY, 20 / GameLoader.ResolutionScaleY, 20,
				20 / GameLoader.ResolutionScaleY));

		fadeScreen.getChildren().add(clearUp);
		menuRoot.getChildren().addAll(backgroundImage, titleBox, menuBox, boundBox, fadeScreen);

		game.setRoot(menuRoot);

		setKeyInputHandler();
		setMouseInputHandler();
		optionsMenu.setupOptionsMenu();
		multiplayerMenu.setupMultiplayerMenu();
	}

	public void transition() {
		if (showMenu) {
			opacity -= 0.01;
			clearUp.setOpacity(opacity);
			if (opacity <= 0) {
				opacity = 0;
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
		menuRoot.getChildren().addAll(backgroundImage, parallaxObject1, parallaxObject2, menuBox, r1, r2, r3, r4);
	}

	public void setBackgroundObjects() {
		x = x + velX;
		y = y + velY;
		x2 = x2 + velX2;
		y2 = y2 + velY2;
		parallaxObject1.setX(x);
		parallaxObject1.setY(y);
		if (x < 0 - parallaxObject1.getImage().getWidth()) {
			x = (float) Settings.WIDTH;
		}
		parallaxObject2.setX(x2);
		parallaxObject2.setY(y2);
		if (x2 < 0 - parallaxObject2.getImage().getWidth()) {
			x2 = (float) Settings.WIDTH;
		}
	}

	/**
	 * Sets the standard style of the labels
	 * 
	 * @param label
	 */
	private void setStyle(Label label) {
		label.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
				+ 36 / GameLoader.ResolutionScaleY + "px;");
		label.setEffect(borderGlow);
	}

	/**
	 * Sets the style for the title
	 * 
	 * @param label
	 */
	public void setTitleStyle(Label label) {
		Blend blend = new Blend();
		blend.setMode(BlendMode.MULTIPLY);

		DropShadow ds = new DropShadow();
		ds.setColor(Color.GREEN);
		ds.setOffsetX(3);
		ds.setOffsetY(3);
		ds.setRadius(5);
		ds.setSpread(0.2);

		blend.setBottomInput(ds);

		DropShadow ds1 = new DropShadow();
		ds1.setColor(Color.LIGHTGREEN);
		ds1.setRadius(100);
		ds1.setSpread(0.6);

		Blend blend2 = new Blend();
		blend2.setMode(BlendMode.MULTIPLY);

		InnerShadow is = new InnerShadow();
		is.setColor(Color.LIGHTGREEN);
		is.setRadius(9);
		is.setChoke(0.8);
		blend2.setBottomInput(is);

		InnerShadow is1 = new InnerShadow();
		is1.setColor(Color.LIGHTGREEN);
		is1.setRadius(5);
		is1.setChoke(0.4);
		blend2.setTopInput(is1);

		Blend blend1 = new Blend();
		blend1.setMode(BlendMode.MULTIPLY);
		blend1.setBottomInput(ds1);
		blend1.setTopInput(blend2);

		blend.setTopInput(blend1);

		label.setEffect(blend);
		label.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
				+ 170 / GameLoader.ResolutionScaleY + "px;");
		label.setPadding(new Insets(0, 30, 30, 30));
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
					currentChoice = 5;
				}
				break;
			case DOWN:
				currentChoice += 1;
				if (currentChoice > 5) {
					currentChoice = 1;
				}
				break;
			case W:
				currentChoice -= 1;
				if (currentChoice < 1) {
					currentChoice = 5;
				}
				break;
			case S:
				currentChoice += 1;
				if (currentChoice > 5) {
					currentChoice = 1;
				}
				break;
			case ENTER:
				if (currentChoice == 1) {
					startSelected();
				}
				if (currentChoice == 2) {
					OptionsMenu();
				}
				if (currentChoice == 3) {
					multiplayerMenu();
				}
				if (currentChoice == 4) { // highScore();
				}
				if (currentChoice == 5) {
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
				if (currentChoice == 3) { // highScore();
				}
				if (currentChoice == 4) {
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
				startLabel.setStyle("-fx-text-fill: #A5DF00; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				optionsLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				multiplayerLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				highScoreLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				exitLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
			} else if (currentChoice == 2) {
				optionsLabel.setStyle("-fx-text-fill: #A5DF00; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				multiplayerLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				startLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				highScoreLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				exitLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
			} else if (currentChoice == 3) {
				multiplayerLabel.setStyle("-fx-text-fill: #A5DF00; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				highScoreLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				exitLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				startLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				optionsLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
			} else if (currentChoice == 4) {
				highScoreLabel.setStyle("-fx-text-fill: #A5DF00; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				multiplayerLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				exitLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				startLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				optionsLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
			} else if (currentChoice == 5) {
				exitLabel.setStyle("-fx-text-fill: #A5DF00; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				multiplayerLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				highScoreLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				startLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				optionsLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
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
		r1.setOnMousePressed(e -> {
			startSelected();
		});
		r2.setOnMousePressed(e -> {
			OptionsMenu();
		});
		r3.setOnMousePressed(e -> {
			multiplayerMenu();
		});
		r4.setOnMousePressed(e -> {
			multiplayerMenu();
		});
		r5.setOnMousePressed(e -> {
			closeGame();
		});

		/**
		 * Code below determines the style of the labels if the mouse enters one
		 * of the rectangles
		 */
		r1.setOnMouseEntered(e -> {
			startLabel.setStyle("-fx-text-fill: #A5DF00; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			optionsLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			multiplayerLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			highScoreLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			exitLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
		});
		r2.setOnMouseEntered(e -> {
			optionsLabel.setStyle("-fx-text-fill: #A5DF00; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			multiplayerLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			startLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			highScoreLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			exitLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
		});
		r3.setOnMouseEntered(e -> {
			multiplayerLabel.setStyle("-fx-text-fill: #A5DF00; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			highScoreLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			exitLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			startLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			optionsLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
		});
		r4.setOnMouseEntered(e -> {
			highScoreLabel.setStyle("-fx-text-fill: #A5DF00; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			multiplayerLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			exitLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			startLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			optionsLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
		});
		r5.setOnMouseEntered(e -> {
			exitLabel.setStyle("-fx-text-fill: #A5DF00; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			multiplayerLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			startLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			optionsLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			highScoreLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
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
		opacity = 1;
		clearUp.setOpacity(opacity);
		setKeyInputHandler();
		setMouseInputHandler();
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
		menuRoot.getChildren().add(optionsMenu.getOptionsRoot());
		optionsMenu.setKeyInputHandler();
		optionsMenu.setMouseInputHandler();
	}

	private void multiplayerMenu() {
		menuRoot.getChildren().add(multiplayerMenu.getMultiplayerRoot());
		// multiplayerMenu.setKeyInputHandler();
		// multiplayerMenu.setMouseInputHandler();
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
