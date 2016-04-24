package com.SnakeGame.Core;

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

public class MenuSettings {
	private int currentChoice = 1;
	private ImageView backgroundImage;
	private Label gameTitle = new Label("Snake");
	private Label illuminationLabel = new Label("Global illumination");
	private Label specularLabel = new Label("Specular map");
	private Label debrisLabel = new Label("Debris limit");
	private Label particleLabel = new Label("Particle limit");
	private Label framecapLabel = new Label("Framecap");
	private Label autoEatLabel = new Label("Automatic eating: ON");
	private Label dirtLabel = new Label("Allow Dirt: OFF");
	private Label backLabel = new Label("Back");
	private Rectangle r1, r2, r3, r4, r5, r6, r7, r8;
	private VBox settingsBox = new VBox();
	private VBox titleBox = new VBox();
	private VBox boundBox = new VBox();
	private DropShadow borderGlow = new DropShadow();
	Light.Point light = new Light.Point();
	Lighting lighting = new Lighting();
	private Pane settingsRoot = new Pane();
	private SnakeGame game;
	private float x, y, velX, velY, x2, y2, velX2, velY2;
	private MenuOptions optionsMenu;
	private Settings settings;

	public MenuSettings(MenuOptions optionsMenu, SnakeGame game) {
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
	public void setupSettingsMenu() {
		setTitleStyle(gameTitle);
		setStyle(illuminationLabel);
		setStyle(specularLabel);
		setStyle(debrisLabel);
		setStyle(particleLabel);
		setStyle(framecapLabel);
		setStyle(autoEatLabel);
		setStyle(dirtLabel);
		setStyle(backLabel);

		titleBox.getChildren().addAll(gameTitle);
		titleBox.setBackground(Background.EMPTY);
		titleBox.setPrefWidth(900);
		titleBox.maxWidth(900);
		titleBox.setMinWidth(900);
		titleBox.setAlignment(Pos.CENTER);
		gameTitle.setAlignment(Pos.CENTER);
		gameTitle.setPrefWidth(900);

		settingsBox.setPadding(new Insets(20 / GameLoader.ResolutionScaleY, 20, 20, 20 / GameLoader.ResolutionScaleY));
		settingsBox.getChildren().addAll(illuminationLabel, specularLabel, debrisLabel, framecapLabel, particleLabel,
				autoEatLabel, dirtLabel, backLabel);
		settingsBox.setBackground(Background.EMPTY);
		settingsBox.setPrefWidth(500);
		settingsBox.setMinWidth(500);
		settingsBox.setPrefHeight(600);
		settingsBox.setMinHeight(600);
		settingsBox.setAlignment(Pos.CENTER);
		settingsBox.setTranslateX(Settings.WIDTH / 2 - settingsBox.getPrefWidth() / 2);
		settingsBox.setTranslateY(Settings.HEIGHT / 2 - settingsBox.getPrefHeight() / 2);
		VBox.setMargin(illuminationLabel,
				new Insets(20 / GameLoader.ResolutionScaleY, 20, 20, 20 / GameLoader.ResolutionScaleY));
		VBox.setMargin(specularLabel,
				new Insets(20 / GameLoader.ResolutionScaleY, 20, 20, 20 / GameLoader.ResolutionScaleY));
		VBox.setMargin(debrisLabel,
				new Insets(20 / GameLoader.ResolutionScaleY, 20, 20, 20 / GameLoader.ResolutionScaleY));
		VBox.setMargin(framecapLabel,
				new Insets(20 / GameLoader.ResolutionScaleY, 20, 20, 20 / GameLoader.ResolutionScaleY));
		VBox.setMargin(autoEatLabel,
				new Insets(20 / GameLoader.ResolutionScaleY, 20, 20, 20 / GameLoader.ResolutionScaleY));
		VBox.setMargin(dirtLabel,
				new Insets(20 / GameLoader.ResolutionScaleY, 20, 20, 20 / GameLoader.ResolutionScaleY));
		VBox.setMargin(backLabel,
				new Insets(20 / GameLoader.ResolutionScaleY, 20, 20, 20 / GameLoader.ResolutionScaleY));

		illuminationLabel.setStyle("-fx-text-fill: #DBA901; -fx-font-family: Impact; -fx-font-size: "
				+ 36 / GameLoader.ResolutionScaleY + "px");
		specularLabel.setAlignment(Pos.CENTER);
		debrisLabel.setAlignment(Pos.CENTER);
		framecapLabel.setAlignment(Pos.CENTER);
		autoEatLabel.setAlignment(Pos.CENTER);
		dirtLabel.setAlignment(Pos.CENTER);
		backLabel.setAlignment(Pos.CENTER);

		r1 = new Rectangle(Settings.WIDTH / 2 - 250 / 2, settingsBox.getTranslateY() / GameLoader.ResolutionScaleY, 350,
				50 / GameLoader.ResolutionScaleY);
//		 r1.setFill(Color.rgb(255, 0, 0,0.5));
		r1.setFill(Color.TRANSPARENT);
		r2 = new Rectangle(Settings.WIDTH / 2 - 250 / 2,
				r1.getY() + r1.getHeight() + (20 / GameLoader.ResolutionScaleY), 350, 50 / GameLoader.ResolutionScaleY);
//		 r2.setFill(Color.rgb(255, 0, 0,0.5));
		r2.setFill(Color.TRANSPARENT);
		r3 = new Rectangle(Settings.WIDTH / 2 - 250 / 2,
				r2.getY() + r2.getHeight() + (20 / GameLoader.ResolutionScaleY), 350, 50 / GameLoader.ResolutionScaleY);
//		 r3.setFill(Color.rgb(255, 0, 0,0.5));
		r3.setFill(Color.TRANSPARENT);
		r4 = new Rectangle(Settings.WIDTH / 2 - 250 / 2,
				r3.getY() + r3.getHeight() + (20 / GameLoader.ResolutionScaleY), 350, 50 / GameLoader.ResolutionScaleY);
//		 r4.setFill(Color.rgb(255, 0, 0,0.5));
		r4.setFill(Color.TRANSPARENT);
		r5 = new Rectangle(Settings.WIDTH / 2 - 250 / 2,
				r4.getY() + r4.getHeight() + (20 / GameLoader.ResolutionScaleY), 350, 50 / GameLoader.ResolutionScaleY);
//		 r5.setFill(Color.rgb(255, 0, 0,0.5));
		r5.setFill(Color.TRANSPARENT);
		r6 = new Rectangle(Settings.WIDTH / 2 - 250 / 2,
				r5.getY() + r5.getHeight() + (20 / GameLoader.ResolutionScaleY), 350, 50 / GameLoader.ResolutionScaleY);
//		 r6.setFill(Color.rgb(255, 0, 0,0.5));
		r6.setFill(Color.TRANSPARENT);
		r7 = new Rectangle(Settings.WIDTH / 2 - 250 / 2,
				r6.getY() + r6.getHeight() + (20 / GameLoader.ResolutionScaleY), 350, 50 / GameLoader.ResolutionScaleY);
//		 r7.setFill(Color.rgb(255, 0, 0,0.5));
		r7.setFill(Color.TRANSPARENT);
		r8 = new Rectangle(Settings.WIDTH / 2 - 250 / 2,
				r7.getY() + r7.getHeight() + (20 / GameLoader.ResolutionScaleY), 350, 50 / GameLoader.ResolutionScaleY);
//		 r8.setFill(Color.rgb(255, 0, 0,0.5));
		r8.setFill(Color.TRANSPARENT);

		titleBox.setTranslateX(Settings.WIDTH / 2 - titleBox.getPrefWidth() / 2);
		titleBox.setTranslateY(0);

		boundBox.setPadding(new Insets(20 / GameLoader.ResolutionScaleY, 20, 20, 20 / GameLoader.ResolutionScaleY));

		boundBox.getChildren().addAll(r1, r2, r3, r4, r5, r6, r7, r8);
		boundBox.setBackground(Background.EMPTY);
		boundBox.setPrefWidth(600);
		boundBox.setMinWidth(600);
		boundBox.setPrefHeight(700);
		boundBox.setMinHeight(700);
		boundBox.setAlignment(Pos.CENTER);
		boundBox.setTranslateX(Settings.WIDTH / 2 - boundBox.getPrefWidth() / 2);
		boundBox.setTranslateY(Settings.HEIGHT / 2 - boundBox.getPrefHeight() / 2);
		VBox.setMargin(r1, new Insets(18 / GameLoader.ResolutionScaleY, 20, 12, 20 / GameLoader.ResolutionScaleY));
		VBox.setMargin(r2, new Insets(18 / GameLoader.ResolutionScaleY, 20, 12, 20 / GameLoader.ResolutionScaleY));
		VBox.setMargin(r3, new Insets(18 / GameLoader.ResolutionScaleY, 20, 12, 20 / GameLoader.ResolutionScaleY));
		VBox.setMargin(r4, new Insets(18 / GameLoader.ResolutionScaleY, 20, 12, 20 / GameLoader.ResolutionScaleY));
		VBox.setMargin(r5, new Insets(18 / GameLoader.ResolutionScaleY, 20, 12, 20 / GameLoader.ResolutionScaleY));
		VBox.setMargin(r6, new Insets(18 / GameLoader.ResolutionScaleY, 20, 12, 20 / GameLoader.ResolutionScaleY));
		VBox.setMargin(r7, new Insets(18 / GameLoader.ResolutionScaleY, 20, 12, 20 / GameLoader.ResolutionScaleY));
		VBox.setMargin(r8, new Insets(18 / GameLoader.ResolutionScaleY, 20, 12, 20 / GameLoader.ResolutionScaleY));

		settingsRoot.getChildren().addAll(backgroundImage, titleBox, settingsBox, boundBox);
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
	 * Sets the keyinputhandling for the settings
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
					currentChoice = 8;
				}
				break;
			case DOWN:
				currentChoice += 1;
				if (currentChoice > 8) {
					currentChoice = 1;
				}
				break;
			case W:
				currentChoice -= 1;
				if (currentChoice < 1) {
					currentChoice = 8;
				}
				break;
			case S:
				currentChoice += 1;
				if (currentChoice > 8) {
					currentChoice = 1;
				}
				break;
			case ENTER:
				if (currentChoice == 1) {
				}
				if (currentChoice == 2) {
				}
				if (currentChoice == 3) {
				}
				if (currentChoice == 4) {

				}
				if (currentChoice == 5) {

				}
				if (currentChoice == 6) {
					if (autoEatLabel.getText() == "Automatic eating: ON"){
						Settings.AUTOMATIC_EATING = false;
						autoEatLabel.setText("Automatic eating: OFF");
					} else if (autoEatLabel.getText() == "Automatic eating: OFF"){
						Settings.AUTOMATIC_EATING = true;
						autoEatLabel.setText("Automatic eating: ON");
					}
				} 
				if (currentChoice == 7) {
					if (dirtLabel.getText() == "Allow Dirt: ON"){
						Settings.ALLOW_DIRT = false;
						dirtLabel.setText("Allow Dirt: OFF");
					} else if (dirtLabel.getText() == "Allow Dirt: OFF"){
						Settings.ALLOW_DIRT = true;
						dirtLabel.setText("Allow Dirt: ON");
					}
				}
				if (currentChoice == 8) {
					optionsMenu.getOptionsRoot().getChildren().remove(settingsRoot);
					optionsMenu.setKeyInputHandler();
					optionsMenu.setMouseInputHandler();
				}
				break;
			case ESCAPE:
				optionsMenu.getOptionsRoot().getChildren().remove(settingsRoot);
				optionsMenu.setKeyInputHandler();
				optionsMenu.setMouseInputHandler();
				break;
			case SPACE:
				if (currentChoice == 1) {
				}
				if (currentChoice == 2) {
				}
				if (currentChoice == 3) {
				}
				if (currentChoice == 4) {
				}
				if (currentChoice == 5) {
				}
				if (currentChoice == 6) {
					if (autoEatLabel.getText() == "Automatic eating: ON"){
						Settings.AUTOMATIC_EATING = false;
						autoEatLabel.setText("Automatic eating: OFF");
					} else if (autoEatLabel.getText() == "Automatic eating: OFF"){
						Settings.AUTOMATIC_EATING = true;
						autoEatLabel.setText("Automatic eating: ON");
					}
				} 
				if (currentChoice == 7) {
					if (dirtLabel.getText() == "Allow Dirt: ON"){
						Settings.ALLOW_DIRT = false;
						dirtLabel.setText("Allow Dirt: OFF");
					} else if (dirtLabel.getText() == "Allow Dirt: OFF"){
						Settings.ALLOW_DIRT = true;
						dirtLabel.setText("Allow Dirt: ON");
					}
				}
				if (currentChoice == 8) {
					optionsMenu.getOptionsRoot().getChildren().remove(settingsRoot);
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
				illuminationLabel.setStyle("-fx-text-fill: #DBA901; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				specularLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				debrisLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				framecapLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				particleLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				autoEatLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				dirtLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				backLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
			} else if (currentChoice == 2) {
				specularLabel.setStyle("-fx-text-fill: #DBA901; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				illuminationLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				debrisLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				framecapLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				particleLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				autoEatLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				dirtLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				backLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
			} else if (currentChoice == 3) {
				debrisLabel.setStyle("-fx-text-fill: #DBA901; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				specularLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				illuminationLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				framecapLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				particleLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				autoEatLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				dirtLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				backLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
			} else if (currentChoice == 4) {
				framecapLabel.setStyle("-fx-text-fill: #DBA901; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				specularLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				debrisLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				illuminationLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				particleLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				autoEatLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				dirtLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				backLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
			} else if (currentChoice == 5) {
				particleLabel.setStyle("-fx-text-fill: #DBA901; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				specularLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				debrisLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				framecapLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				illuminationLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				autoEatLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				dirtLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				backLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
			} else if (currentChoice == 6) {
				autoEatLabel.setStyle("-fx-text-fill: #DBA901; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				specularLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				debrisLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				framecapLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				particleLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				illuminationLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				dirtLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				backLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
			} else if (currentChoice == 7) {
				dirtLabel.setStyle("-fx-text-fill: #DBA901; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				specularLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				debrisLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				framecapLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				particleLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				autoEatLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				illuminationLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				backLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
			} else if (currentChoice == 8) {
				backLabel.setStyle("-fx-text-fill: #DBA901; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				specularLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				debrisLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				framecapLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				particleLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				autoEatLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				dirtLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
						+ 36 / GameLoader.ResolutionScaleY + "px");
				illuminationLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
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
		});
		r2.setOnMousePressed(e -> {
		});
		r3.setOnMousePressed(e -> {
		});
		r4.setOnMousePressed(e -> {
		});
		r5.setOnMousePressed(e -> {
		});
		r6.setOnMousePressed(e -> {
			if (autoEatLabel.getText() == "Automatic eating: ON") {
				Settings.AUTOMATIC_EATING = false;
				autoEatLabel.setText("Automatic eating: OFF");
			} else if (autoEatLabel.getText() == "Automatic eating: OFF") {
				Settings.AUTOMATIC_EATING = true;
				autoEatLabel.setText("Automatic eating: ON");
			}
		});
		r7.setOnMousePressed(e -> {
			if (dirtLabel.getText() == "Allow Dirt: ON") {
				Settings.ALLOW_DIRT = false;
				dirtLabel.setText("Allow Dirt: OFF");
			} else if (dirtLabel.getText() == "Allow Dirt: OFF") {
				Settings.ALLOW_DIRT = true;
				dirtLabel.setText("Allow Dirt: ON");
			}

		});
		r8.setOnMousePressed(e -> {
			optionsMenu.getOptionsRoot().getChildren().remove(settingsRoot);
			optionsMenu.setKeyInputHandler();
			optionsMenu.setMouseInputHandler();
		});

		/**
		 * Code below determines the style of the labels if the mouse enters one
		 * of the rectangles
		 */
		r1.setOnMouseEntered(e -> {
			illuminationLabel.setStyle("-fx-text-fill: #DBA901; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			specularLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			debrisLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			framecapLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			particleLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			autoEatLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			dirtLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			backLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
		});
		r2.setOnMouseEntered(e -> {
			specularLabel.setStyle("-fx-text-fill: #DBA901; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			illuminationLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			debrisLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			framecapLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			particleLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			autoEatLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			dirtLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			backLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
		});
		r3.setOnMouseEntered(e -> {
			debrisLabel.setStyle("-fx-text-fill: #DBA901; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			specularLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			illuminationLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			framecapLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			particleLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			autoEatLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			dirtLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			backLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
		});
		r4.setOnMouseEntered(e -> {
			framecapLabel.setStyle("-fx-text-fill: #DBA901; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			specularLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			debrisLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			illuminationLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			particleLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			autoEatLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			dirtLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			backLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
		});
		r5.setOnMouseEntered(e -> {
			particleLabel.setStyle("-fx-text-fill: #DBA901; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			specularLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			debrisLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			framecapLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			illuminationLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			autoEatLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			dirtLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			backLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
		});
		r6.setOnMouseEntered(e -> {
			autoEatLabel.setStyle("-fx-text-fill: #DBA901; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			specularLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			debrisLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			framecapLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			particleLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			illuminationLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			dirtLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			backLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
		});
		r7.setOnMouseEntered(e -> {
			dirtLabel.setStyle("-fx-text-fill: #DBA901; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			specularLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			debrisLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			framecapLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			particleLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			autoEatLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			illuminationLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			backLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
		});
		r8.setOnMouseEntered(e -> {
			backLabel.setStyle("-fx-text-fill: #DBA901; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			specularLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			debrisLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			framecapLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			particleLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			autoEatLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			dirtLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
			illuminationLabel.setStyle("-fx-text-fill: #F5F6CE; -fx-font-family: Impact; -fx-font-size: "
					+ 36 / GameLoader.ResolutionScaleY + "px");
		});
	}
	
	public Pane getSettingsRoot(){
		return settingsRoot;
	}

}
