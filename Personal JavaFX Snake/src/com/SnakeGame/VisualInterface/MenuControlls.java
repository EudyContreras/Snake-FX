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

public class MenuControlls {

	private int currentChoice = 1;
	private ImageView backgroundImage;
	private Label gameTitle = new Label("Snake");
	private Label upLabel = new Label("To go up use: UP KEY/W");
	private Label downLabel = new Label("To go down use: DOWN KEY/S");
	private Label leftLabel = new Label("To go left use: LEFT KEY/A");
	private Label rightLabel = new Label("To go right use: RIGHT KEY/D");
	private Label backLabel = new Label("Back");
	private Rectangle r1, r2, r3, r4, r5;
	private VBox controllsBox = new VBox();
	private VBox titleBox = new VBox();
	private VBox boundBox = new VBox();
	private DropShadow borderGlow = new DropShadow();
	Light.Point light = new Light.Point();
	Lighting lighting = new Lighting();
	private Pane controllsRoot = new Pane();
	private SnakeGame game;
	private float x, y, velX, velY, x2, y2, velX2, velY2;
	private MenuOptions optionsMenu;

	public MenuControlls(MenuOptions optionsMenu, SnakeGame game) {
		this.optionsMenu = optionsMenu;
		this.game = game;
		backgroundImage = new ImageView(MenuImageBank.controllsMenuBackground);
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
	public void setupControllsMenu() {
		setTitleStyle(gameTitle);
		setStyle(upLabel);
		setStyle(downLabel);
		setStyle(leftLabel);
		setStyle(rightLabel);
		setStyle(backLabel);

		titleBox.getChildren().addAll(gameTitle);
		titleBox.setBackground(Background.EMPTY);
		titleBox.setPrefWidth(900);
		titleBox.maxWidth(900);
		titleBox.setMinWidth(900);
		titleBox.setAlignment(Pos.CENTER);
		gameTitle.setAlignment(Pos.CENTER);
		gameTitle.setPrefWidth(900);

		controllsBox.setPadding(new Insets(20 / GameLoader.ResolutionScaleY, 20, 20, 20 / GameLoader.ResolutionScaleY));
		controllsBox.getChildren().addAll(upLabel, downLabel, leftLabel, rightLabel, backLabel);
		controllsBox.setBackground(Background.EMPTY);
		controllsBox.setPrefWidth(500);
		controllsBox.setMinWidth(500);
		controllsBox.setPrefHeight(600);
		controllsBox.setMinHeight(600);
		controllsBox.setAlignment(Pos.CENTER);
		controllsBox.setTranslateX(Settings.WIDTH / 2 - controllsBox.getPrefWidth() / 2);
		controllsBox.setTranslateY(Settings.HEIGHT / 2 - controllsBox.getPrefHeight() / 2);
		VBox.setMargin(upLabel, new Insets(20 / GameLoader.ResolutionScaleY, 20, 20, 20 / GameLoader.ResolutionScaleY));
		VBox.setMargin(downLabel,
				new Insets(20 / GameLoader.ResolutionScaleY, 20, 20, 20 / GameLoader.ResolutionScaleY));
		VBox.setMargin(leftLabel,
				new Insets(20 / GameLoader.ResolutionScaleY, 20, 20, 20 / GameLoader.ResolutionScaleY));
		VBox.setMargin(rightLabel,
				new Insets(20 / GameLoader.ResolutionScaleY, 20, 20, 20 / GameLoader.ResolutionScaleY));
		VBox.setMargin(backLabel,
				new Insets(20 / GameLoader.ResolutionScaleY, 20, 20, 20 / GameLoader.ResolutionScaleY));

		upLabel.setStyle("-fx-text-fill: #A5DF00; -fx-font-family: Impact; -fx-font-size: "
				+ 36 / GameLoader.ResolutionScaleY + "px");
		downLabel.setAlignment(Pos.CENTER);
		leftLabel.setAlignment(Pos.CENTER);
		rightLabel.setAlignment(Pos.CENTER);
		backLabel.setAlignment(Pos.CENTER);

		r1 = new Rectangle(Settings.WIDTH / 2 - 250 / 2, controllsBox.getTranslateY() / GameLoader.ResolutionScaleY,
				350, 50 / GameLoader.ResolutionScaleY);
		// r1.setFill(Color.rgb(255, 0, 0,0.5));
		r1.setFill(Color.TRANSPARENT);
		r2 = new Rectangle(Settings.WIDTH / 2 - 250 / 2,
				r1.getY() + r1.getHeight() + (20 / GameLoader.ResolutionScaleY), 350, 50 / GameLoader.ResolutionScaleY);
		// r2.setFill(Color.rgb(255, 0, 0,0.5));
		r2.setFill(Color.TRANSPARENT);
		r3 = new Rectangle(Settings.WIDTH / 2 - 250 / 2,
				r2.getY() + r2.getHeight() + (20 / GameLoader.ResolutionScaleY), 350, 50 / GameLoader.ResolutionScaleY);
		// r3.setFill(Color.rgb(255, 0, 0,0.5));
		r3.setFill(Color.TRANSPARENT);
		r4 = new Rectangle(Settings.WIDTH / 2 - 250 / 2,
				r3.getY() + r3.getHeight() + (20 / GameLoader.ResolutionScaleY), 350, 50 / GameLoader.ResolutionScaleY);
		// r4.setFill(Color.rgb(255, 0, 0,0.5));
		r4.setFill(Color.TRANSPARENT);
		r5 = new Rectangle(Settings.WIDTH / 2 - 250 / 2,
				r4.getY() + r4.getHeight() + (20 / GameLoader.ResolutionScaleY), 350, 50 / GameLoader.ResolutionScaleY);
		// r5.setFill(Color.rgb(255, 0, 0,0.5));
		r5.setFill(Color.TRANSPARENT);

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
		VBox.setMargin(r1, new Insets(20 / GameLoader.ResolutionScaleY, 20, 20, 20 / GameLoader.ResolutionScaleY));
		VBox.setMargin(r2, new Insets(20 / GameLoader.ResolutionScaleY, 20, 20, 20 / GameLoader.ResolutionScaleY));
		VBox.setMargin(r3, new Insets(20 / GameLoader.ResolutionScaleY, 20, 20, 20 / GameLoader.ResolutionScaleY));
		VBox.setMargin(r4, new Insets(20 / GameLoader.ResolutionScaleY, 20, 20, 20 / GameLoader.ResolutionScaleY));
		VBox.setMargin(r5, new Insets(20 / GameLoader.ResolutionScaleY, 20, 20, 20 / GameLoader.ResolutionScaleY));

		controllsRoot.getChildren().addAll(backgroundImage, titleBox, controllsBox, boundBox);
		upLabel.setStyle("-fx-text-fill: #A5DF00; -fx-font-family: Impact; -fx-font-size: "
				+ 36 / GameLoader.ResolutionScaleY + "px");
	}

	/**
	 * Sets the standard style of the labels
	 * 
	 * @param label
	 */
	private void setStyle(Label label) {
		label.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
				+ 36 / GameLoader.ResolutionScaleY + "px;");
		// label.setPadding(new Insets(20, 20, 20, 20));
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
		label.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: 110px;");
		label.setPadding(new Insets(30, 30, 30, 30));
	}

	/**
	 * Sets the keyinputhandling for the controlls menu
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
				if (currentChoice == 5) {
					optionsMenu.getOptionsRoot().getChildren().remove(controllsRoot);
					optionsMenu.setKeyInputHandler();
					optionsMenu.setMouseInputHandler();
				}
				break;
			case ESCAPE:
				optionsMenu.getOptionsRoot().getChildren().remove(controllsRoot);
				optionsMenu.setKeyInputHandler();
				optionsMenu.setMouseInputHandler();
				break;
			case SPACE:
				if (currentChoice == 5) {
					optionsMenu.getOptionsRoot().getChildren().remove(controllsRoot);
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
				upLabel.setStyle("-fx-text-fill: #A5DF00; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				downLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				leftLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				rightLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				backLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");

			} else if (currentChoice == 2) {
				downLabel.setStyle("-fx-text-fill: #A5DF00; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				upLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				leftLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				rightLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				backLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");

			} else if (currentChoice == 3) {
				leftLabel.setStyle("-fx-text-fill: #A5DF00; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				upLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				downLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				rightLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				backLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");

			} else if (currentChoice == 4) {
				rightLabel.setStyle("-fx-text-fill: #A5DF00; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				upLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				leftLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				downLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				backLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");

			} else if (currentChoice == 5) {
				backLabel.setStyle("-fx-text-fill: #A5DF00; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				upLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				leftLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				rightLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				downLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
			}
		});
	}

	/**
	 * Sets the mouseinputhandling for the controlls menu
	 */
	public void setMouseInputHandler() {
		/**
		 * Code below determines what will happen if the mouse presses one of
		 * the different rectangles
		 */
		r5.setOnMousePressed(e -> {
			optionsMenu.getOptionsRoot().getChildren().remove(controllsRoot);
			optionsMenu.setKeyInputHandler();
			optionsMenu.setMouseInputHandler();
		});

		/**
		 * Code below determines the style of the labels if the mouse enters one
		 * of the rectangles
		 */
		r1.setOnMouseEntered(e -> {
			upLabel.setStyle("-fx-text-fill: #A5DF00; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			downLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			leftLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			rightLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			backLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
		});
		r2.setOnMouseEntered(e -> {
			downLabel.setStyle("-fx-text-fill: #A5DF00; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			upLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			leftLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			rightLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			backLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
		});
		r3.setOnMouseEntered(e -> {
			leftLabel.setStyle("-fx-text-fill: #A5DF00; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			upLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			downLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			rightLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			backLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
		});
		r4.setOnMouseEntered(e -> {
			rightLabel.setStyle("-fx-text-fill: #A5DF00; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			upLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			leftLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			downLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			backLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
		});
		r5.setOnMouseEntered(e -> {
			backLabel.setStyle("-fx-text-fill: #A5DF00; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			upLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			leftLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			rightLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			downLabel.setStyle("-fx-text-fill: #E1F5A9; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
		});
	}

	public Pane getControllsRoot() {
		return controllsRoot;
	}
}