package com.SnakeGame.VisualInterface;

import com.SnakeGame.FrameWork.GameLoader;
import com.SnakeGame.FrameWork.Settings;
import com.SnakeGame.FrameWork.SnakeGame;

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

public class MenuMode {

	private int currentChoice = 1;
	private ImageView backgroundImage;
	private Label gameTitle = new Label("Snake");
	private Label modeLabel = new Label("Current mode is: FREESTYLE MODE");
	private Label backLabel = new Label("Back");
	private Rectangle r1, r2;
	private VBox modeBox = new VBox();
	private VBox titleBox = new VBox();
	private VBox boundBox = new VBox();
	private DropShadow borderGlow = new DropShadow();
	Light.Point light = new Light.Point();
	Lighting lighting = new Lighting();
	private Pane modeRoot = new Pane();
	private SnakeGame game;
	private float x, y, velX, velY, x2, y2, velX2, velY2;
	private MenuOptions optionsMenu;

	public MenuMode(MenuOptions optionsMenu, SnakeGame game) {
		this.optionsMenu = optionsMenu;
		this.game = game;
		backgroundImage = new ImageView(MenuImageBank.modeMenuBackground);
		gameTitle.setLayoutX(0);
		y2 = 400;
		x = (float) Settings.WIDTH / 3;
		x2 = (float) (Settings.WIDTH);
		velX = -1;
		velX2 = -1;
	}

	/**
	 * Sets up the sounds menu
	 */
	public void setupModeMenu() {
		setTitleStyle(gameTitle);
		setStyle(modeLabel);
		setStyle(backLabel);

		titleBox.getChildren().addAll(gameTitle);
		titleBox.setBackground(Background.EMPTY);
		titleBox.setPrefWidth(900);
		titleBox.maxWidth(900);
		titleBox.setMinWidth(900);
		titleBox.setAlignment(Pos.CENTER);
		gameTitle.setAlignment(Pos.CENTER);
		gameTitle.setPrefWidth(900);

		modeBox.setPadding(new Insets(20 / GameLoader.ResolutionScaleY, 20, 20, 20 / GameLoader.ResolutionScaleY));
		modeBox.getChildren().addAll(modeLabel, backLabel);
		modeBox.setBackground(Background.EMPTY);
		modeBox.setPrefWidth(500);
		modeBox.setMinWidth(500);
		modeBox.setPrefHeight(600);
		modeBox.setMinHeight(600);
		modeBox.setAlignment(Pos.CENTER);
		modeBox.setTranslateX(Settings.WIDTH / 2 - modeBox.getPrefWidth() / 2);
		modeBox.setTranslateY(Settings.HEIGHT / 2 - modeBox.getPrefHeight() / 2);
		VBox.setMargin(modeLabel,
				new Insets(20 / GameLoader.ResolutionScaleY, 20, 20, 20 / GameLoader.ResolutionScaleY));
		VBox.setMargin(backLabel,
				new Insets(20 / GameLoader.ResolutionScaleY, 20, 20, 20 / GameLoader.ResolutionScaleY));

		modeLabel.setStyle("-fx-text-fill: #0040FF; -fx-font-family: Impact; -fx-font-size: "
				+ 36 / GameLoader.ResolutionScaleY + "px");
		modeLabel.setAlignment(Pos.CENTER);
		backLabel.setAlignment(Pos.CENTER);

		r1 = new Rectangle(Settings.WIDTH / 2 - 250 / 2, modeBox.getTranslateY() / GameLoader.ResolutionScaleY, 350,
				50 / GameLoader.ResolutionScaleY);
		r1.setFill(Color.rgb(255, 0, 0, 0.5));
		// r1.setFill(Color.TRANSPARENT);
		r2 = new Rectangle(Settings.WIDTH / 2 - 250 / 2,
				r1.getY() + r1.getHeight() + (20 / GameLoader.ResolutionScaleY), 250, 50 / GameLoader.ResolutionScaleY);
		r2.setFill(Color.rgb(255, 0, 0, 0.5));
		// r2.setFill(Color.TRANSPARENT);

		titleBox.setTranslateX(Settings.WIDTH / 2 - titleBox.getPrefWidth() / 2);
		titleBox.setTranslateY(0);

		boundBox.setPadding(new Insets(20 / GameLoader.ResolutionScaleY, 20, 20, 20 / GameLoader.ResolutionScaleY));

		boundBox.getChildren().addAll(r1, r2);
		boundBox.setBackground(Background.EMPTY);
		boundBox.setPrefWidth(500);
		boundBox.setMinWidth(500);
		boundBox.setPrefHeight(600);
		boundBox.setMinHeight(600);
		boundBox.setAlignment(Pos.CENTER);
		boundBox.setTranslateX(Settings.WIDTH / 2 - boundBox.getPrefWidth() / 2);
		boundBox.setTranslateY(Settings.HEIGHT / 2 - boundBox.getPrefHeight() / 2);
		VBox.setMargin(r1, new Insets(20 / GameLoader.ResolutionScaleY, 20, 20, 20 / GameLoader.ResolutionScaleY));
		VBox.setMargin(r2, new Insets(20 / GameLoader.ResolutionScaleY, 20, 20, 20 / GameLoader.ResolutionScaleY));

		modeRoot.getChildren().addAll(backgroundImage, titleBox, modeBox, boundBox);
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
		ds.setColor(Color.BLUE);
		ds.setOffsetX(3);
		ds.setOffsetY(3);
		ds.setRadius(5);
		ds.setSpread(0.2);

		blend.setBottomInput(ds);

		DropShadow ds1 = new DropShadow();
		ds1.setColor(Color.BLUE);
		ds1.setRadius(100);
		ds1.setSpread(0.6);

		Blend blend2 = new Blend();
		blend2.setMode(BlendMode.MULTIPLY);

		InnerShadow is = new InnerShadow();
		is.setColor(Color.CADETBLUE);
		is.setRadius(9);
		is.setChoke(0.8);
		blend2.setBottomInput(is);

		InnerShadow is1 = new InnerShadow();
		is1.setColor(Color.CADETBLUE);
		is1.setRadius(5);
		is1.setChoke(0.4);
		blend2.setTopInput(is1);

		Blend blend1 = new Blend();
		blend1.setMode(BlendMode.MULTIPLY);
		blend1.setBottomInput(ds1);
		blend1.setTopInput(blend2);

		blend.setTopInput(blend1);

		label.setEffect(blend);
		label.setStyle("-fx-text-fill: #0040FF; -fx-font-family: Impact; -fx-font-size: 110px;");
		label.setPadding(new Insets(30, 30, 30, 30));
	}

	/**
	 * Sets up the keyinputhandling for the mode menu
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
					currentChoice = 2;
				}
				break;
			case DOWN:
				currentChoice += 1;
				if (currentChoice > 2) {
					currentChoice = 1;
				}
				break;
			case W:
				currentChoice -= 1;
				if (currentChoice < 1) {
					currentChoice = 2;
				}
				break;
			case S:
				currentChoice += 1;
				if (currentChoice > 2) {
					currentChoice = 1;
				}
				break;
			case ENTER:
				if (currentChoice == 1) {
					mode();
				}
				if (currentChoice == 2) {
					optionsMenu.getOptionsRoot().getChildren().remove(modeRoot);
					optionsMenu.setKeyInputHandler();
					optionsMenu.setMouseInputHandler();
				}
				break;
			case ESCAPE:
				optionsMenu.getOptionsRoot().getChildren().remove(modeRoot);
				optionsMenu.setKeyInputHandler();
				optionsMenu.setMouseInputHandler();
				break;
			case SPACE:
				if (currentChoice == 1) {
					mode();
				}
				if (currentChoice == 2) {
					optionsMenu.getOptionsRoot().getChildren().remove(modeRoot);
					optionsMenu.setKeyInputHandler();
					optionsMenu.setMouseInputHandler();
				}
			default:
				break;
			}
			/*
			 * Code below determines the styling of the different labels if the
			 * user has toggled to that choice
			 */
			if (currentChoice == 1) {
				modeLabel.setStyle("-fx-text-fill: #0040FF; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				backLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");

			} else if (currentChoice == 2) {
				backLabel.setStyle("-fx-text-fill: #0040FF; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				modeLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
			}
		});
	}

	/**
	 * Sets the mouseinputhandling for the mode menu
	 */
	public void setMouseInputHandler() {
		/**
		 * Code below determines what will happen if the mouse presses one of
		 * the different rectangles
		 */
		r1.setOnMousePressed(e -> {
			mode();
		});
		r2.setOnMousePressed(e -> {
			optionsMenu.getOptionsRoot().getChildren().remove(modeRoot);
			optionsMenu.setKeyInputHandler();
			optionsMenu.setMouseInputHandler();
		});

		/**
		 * Code below determines the style of the labels if the mouse enters one
		 * of the rectangles
		 */
		r1.setOnMouseEntered(e -> {
			modeLabel.setStyle("-fx-text-fill: #0040FF; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			backLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
		});
		r2.setOnMouseEntered(e -> {
			backLabel.setStyle("-fx-text-fill: #0040FF; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			modeLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
		});
	}

	public Pane getModeRoot() {
		return modeRoot;
	}

	private void mode() {
		if (modeLabel.getText() == "Current mode is: FREESTYLE MODE") {
			modeLabel.setText("Current mode is: MODE 2");
			// change to Mode 2
		} else if (modeLabel.getText() == "Current mode is: MODE 2") {
			modeLabel.setText("Current mode is: MODE 3");
			// change to Mode 3
		} else if (modeLabel.getText() == "Current mode is: MODE 3") {
			modeLabel.setText("Current mode is: FREESTYLE MODE");
		}
	}

}