package com.SnakeGame.VisualInterface;

import com.SnakeGame.Core.GameLoader;
import com.SnakeGame.Core.Settings;
import com.SnakeGame.Core.SnakeGame;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MenuOptions {
	private int currentChoice = 1;
	private ImageView backgroundImage;
	private Label gameTitle = new Label("Snake");
	private Label soundLabel = new Label("Sound");
	private Label modeLabel = new Label("Mode");
	private Label controllsLabel = new Label("Controlls");
	private Label settingsLabel = new Label("Settings");
	private Label backLabel = new Label("Back");
	private Rectangle r1, r2, r3, r4, r5;
	private VBox optionsBox = new VBox();
	private VBox titleBox = new VBox();
	private VBox boundBox = new VBox();
	private DropShadow borderGlow = new DropShadow();
	Light.Point light = new Light.Point();
	Lighting lighting = new Lighting();
	private Pane optionsRoot = new Pane();
	private SnakeGame game;
	private float x, y, velX, velY, x2, y2, velX2, velY2;
	private MenuMain mainMenu;
	private MenuSound soundMenu;
	private MenuSettings settingsMenu;
	private MenuMode modeMenu;
	private MenuControlls controllsMenu;

	public MenuOptions(MenuMain mainMenu, SnakeGame game) {
		this.mainMenu = mainMenu;
		this.game = game;
		backgroundImage = new ImageView(MenuImageBank.optionsMenuBackground);
		soundMenu = new MenuSound(this, game);
		modeMenu = new MenuMode(this, game);
		controllsMenu = new MenuControlls(this, game);
		settingsMenu = new MenuSettings(this, game);
		gameTitle.setLayoutX(0);
		y2 = 400;
		x = (float) Settings.WIDTH / 3;
		x2 = (float) (Settings.WIDTH);
		velX = -1;
		velX2 = -1;
	}

	/**
	 * Sets up the optionsmenu
	 */
	public void setupOptionsMenu() {
		setTitleStyle(gameTitle);
		setStyle(soundLabel);
		setStyle(modeLabel);
		setStyle(controllsLabel);
		setStyle(settingsLabel);
		setStyle(backLabel);

		titleBox.getChildren().addAll(gameTitle);
		titleBox.setBackground(Background.EMPTY);
		titleBox.setPrefWidth(900);
		titleBox.maxWidth(900);
		titleBox.setMinWidth(900);
		titleBox.setAlignment(Pos.CENTER);
		gameTitle.setAlignment(Pos.CENTER);
		gameTitle.setPrefWidth(900);

		optionsBox.setPadding(new Insets(20 / GameLoader.ResolutionScaleY, 20, 20, 20 / GameLoader.ResolutionScaleY));
		optionsBox.getChildren().addAll(soundLabel, modeLabel, controllsLabel, settingsLabel, backLabel);
		optionsBox.setBackground(Background.EMPTY);
		optionsBox.setPrefWidth(500);
		optionsBox.setMinWidth(500);
		optionsBox.setPrefHeight(600);
		optionsBox.setMinHeight(600);
		optionsBox.setAlignment(Pos.CENTER);
		optionsBox.setTranslateX(Settings.WIDTH / 2 - optionsBox.getPrefWidth() / 2);
		optionsBox.setTranslateY(Settings.HEIGHT / 2 - optionsBox.getPrefHeight() / 2);
		VBox.setMargin(soundLabel,
				new Insets(20 / GameLoader.ResolutionScaleY, 20, 20, 20 / GameLoader.ResolutionScaleY));
		VBox.setMargin(modeLabel,
				new Insets(20 / GameLoader.ResolutionScaleY, 20, 20, 20 / GameLoader.ResolutionScaleY));
		VBox.setMargin(controllsLabel,
				new Insets(20 / GameLoader.ResolutionScaleY, 20, 20, 20 / GameLoader.ResolutionScaleY));
		VBox.setMargin(settingsLabel,
				new Insets(20 / GameLoader.ResolutionScaleY, 20, 20, 20 / GameLoader.ResolutionScaleY));
		VBox.setMargin(backLabel,
				new Insets(20 / GameLoader.ResolutionScaleY, 20, 20, 20 / GameLoader.ResolutionScaleY));

		soundLabel.setStyle("-fx-text-fill: #A5DF00; -fx-font-family: Impact; -fx-font-size: "
				+ 36 / GameLoader.ResolutionScaleY + "px");
		modeLabel.setAlignment(Pos.CENTER);
		controllsLabel.setAlignment(Pos.CENTER);
		settingsLabel.setAlignment(Pos.CENTER);
		backLabel.setAlignment(Pos.CENTER);

		r1 = new Rectangle(Settings.WIDTH / 2 - 250 / 2, optionsBox.getTranslateY() / GameLoader.ResolutionScaleY, 250,
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
		// r5.setFill(Color.TRANSPARENT);

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
		VBox.setMargin(r1, new Insets(14 / GameLoader.ResolutionScaleY, 10 / GameLoader.ResolutionScaleY, 20,
				20 / GameLoader.ResolutionScaleY));
		VBox.setMargin(r2, new Insets(15 / GameLoader.ResolutionScaleY, 10 / GameLoader.ResolutionScaleY, 20,
				20 / GameLoader.ResolutionScaleY));
		VBox.setMargin(r3, new Insets(15 / GameLoader.ResolutionScaleY, 10 / GameLoader.ResolutionScaleY, 20,
				20 / GameLoader.ResolutionScaleY));
		VBox.setMargin(r4, new Insets(15 / GameLoader.ResolutionScaleY, 10 / GameLoader.ResolutionScaleY, 20,
				20 / GameLoader.ResolutionScaleY));
		VBox.setMargin(r5, new Insets(15 / GameLoader.ResolutionScaleY, 10 / GameLoader.ResolutionScaleY, 20,
				20 / GameLoader.ResolutionScaleY));

		optionsRoot.getChildren().addAll(backgroundImage, titleBox, optionsBox, boundBox);
		soundLabel.setStyle("-fx-text-fill: #DBA901; -fx-font-family: Impact; -fx-font-size: "
				+ 36 / GameLoader.ResolutionScaleY + "px");
		soundMenu.setupSoundsMenu();
		modeMenu.setupModeMenu();
		controllsMenu.setupControllsMenu();
		settingsMenu.setupSettingsMenu();
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
		ds.setColor(Color.CHOCOLATE);
		ds.setOffsetX(3);
		ds.setOffsetY(3);
		ds.setRadius(5);
		ds.setSpread(0.2);

		blend.setBottomInput(ds);

		DropShadow ds1 = new DropShadow();
		ds1.setColor(Color.CHOCOLATE);
		ds1.setRadius(100);
		ds1.setSpread(0.6);

		Blend blend2 = new Blend();
		blend2.setMode(BlendMode.MULTIPLY);

		InnerShadow is = new InnerShadow();
		is.setColor(Color.BURLYWOOD);
		is.setRadius(9);
		is.setChoke(0.8);
		blend2.setBottomInput(is);

		InnerShadow is1 = new InnerShadow();
		is1.setColor(Color.BURLYWOOD);
		is1.setRadius(5);
		is1.setChoke(0.4);
		blend2.setTopInput(is1);

		Blend blend1 = new Blend();
		blend1.setMode(BlendMode.MULTIPLY);
		blend1.setBottomInput(ds1);
		blend1.setTopInput(blend2);

		blend.setTopInput(blend1);

		label.setEffect(blend);
		label.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: 110px;");
		label.setPadding(new Insets(30, 30, 30, 30));
	}

	/**
	 * Sets the keyinputhandling for the optionsmenu
	 */
	public void setKeyInputHandler() {
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
					soundMenu();
				}
				if (currentChoice == 2) {
					modeMenu();
				}
				if (currentChoice == 3) {
					controllsMenu();
				}
				if (currentChoice == 4) {
					settingsMenu();
				}
				if (currentChoice == 5) {
					mainMenu.getMenuRoot().getChildren().remove(optionsRoot);
					mainMenu.setMainMenu();
				}
				break;
			case ESCAPE:
				mainMenu.getMenuRoot().getChildren().remove(optionsRoot);
				mainMenu.setMainMenu();
				break;
			case SPACE:
				if (currentChoice == 1) {
					soundMenu();
				}
				if (currentChoice == 2) {
					modeMenu();
				}
				if (currentChoice == 3) {
					controllsMenu();
				}
				if (currentChoice == 4) {
					settingsMenu();
				}
				if (currentChoice == 5) {
					mainMenu.getMenuRoot().getChildren().remove(optionsRoot);
					mainMenu.setMainMenu();
					mainMenu.setCurrentChoice(1);
				}
			default:
				break;
			}
			/*
			 * Code below determines the styling of the different labels if the
			 * user has toggled to that choice
			 */
			if (currentChoice == 1) {
				soundLabel.setStyle("-fx-text-fill: #DBA901; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				modeLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				controllsLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				settingsLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				backLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");

			} else if (currentChoice == 2) {
				modeLabel.setStyle("-fx-text-fill: #DBA901; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				soundLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				controllsLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				settingsLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				backLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");

			} else if (currentChoice == 3) {
				controllsLabel.setStyle("-fx-text-fill: #DBA901; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				modeLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				soundLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				settingsLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				backLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
			} else if (currentChoice == 4) {
				settingsLabel.setStyle("-fx-text-fill: #DBA901; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				modeLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				soundLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				controllsLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				backLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
			} else if (currentChoice == 5) {
				backLabel.setStyle("-fx-text-fill: #DBA901; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				settingsLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				modeLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				controllsLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				soundLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
			}
		});
	}

	/**
	 * Sets the mouseinputhandling for the optionslabels
	 */
	public void setMouseInputHandler() {
		/**
		 * Code below determines what will happen if the mouse presses one of
		 * the different rectangles
		 */
		r1.setOnMousePressed(e -> {
			soundMenu();
		});
		r2.setOnMousePressed(e -> {
			modeMenu();
		});
		r3.setOnMousePressed(e -> {
			controllsMenu();
		});
		r4.setOnMousePressed(e -> {
			settingsMenu();
		});
		r5.setOnMousePressed(e -> {
			mainMenu.getMenuRoot().getChildren().remove(optionsRoot);
			mainMenu.setMainMenu();
			mainMenu.setCurrentChoice(1);
		});

		/**
		 * Code below determines the style of the labels if the mouse enters one
		 * of the rectangles
		 */
		r1.setOnMouseEntered(e -> {
			soundLabel.setStyle("-fx-text-fill: #DBA901; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			modeLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			controllsLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			settingsLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			backLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
		});
		r2.setOnMouseEntered(e -> {
			modeLabel.setStyle("-fx-text-fill: #DBA901; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			soundLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			controllsLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			settingsLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			backLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
		});
		r3.setOnMouseEntered(e -> {
			controllsLabel.setStyle("-fx-text-fill: #DBA901; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			soundLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			modeLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			settingsLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			backLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
		});
		r4.setOnMouseEntered(e -> {
			settingsLabel.setStyle("-fx-text-fill: #DBA901; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			soundLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			modeLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			backLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			controllsLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
		});
		r5.setOnMouseEntered(e -> {
			backLabel.setStyle("-fx-text-fill: #DBA901; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			soundLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			modeLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			settingsLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			controllsLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
		});
	}

	public Pane getOptionsRoot() {
		return optionsRoot;
	}

	private void soundMenu() {
		currentChoice = 1;
		optionsRoot.getChildren().add(soundMenu.getSoundsRoot());
		soundMenu.setKeyInputHandler();
		soundMenu.setMouseInputHandler();
	}

	private void modeMenu() {
		currentChoice = 1;
		optionsRoot.getChildren().add(modeMenu.getModeRoot());
		modeMenu.setKeyInputHandler();
		modeMenu.setMouseInputHandler();
	}

	private void controllsMenu() {
		currentChoice = 1;
		optionsRoot.getChildren().add(controllsMenu.getControllsRoot());
		controllsMenu.setKeyInputHandler();
		controllsMenu.setMouseInputHandler();
	}

	private void settingsMenu() {
		currentChoice = 1;
		optionsRoot.getChildren().add(settingsMenu.getSettingsRoot());
		settingsMenu.setKeyInputHandler();
		settingsMenu.setMouseInputHandler();
	}
}