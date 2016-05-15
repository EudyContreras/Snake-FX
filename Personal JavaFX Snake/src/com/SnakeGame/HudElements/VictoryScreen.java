package com.SnakeGame.HudElements;

import com.SnakeGame.FrameWork.GameLoader;
import com.SnakeGame.FrameWork.Settings;
import com.SnakeGame.FrameWork.SnakeGame;
import com.SnakeGame.ImageBanks.GameImageBank;
import com.SnakeGame.PlayerOne.PlayerOne;
import com.SnakeGame.PlayerTwo.PlayerTwo;
import com.SnakeGame.Utilities.ScreenOverlay;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class VictoryScreen {

	public static boolean LEVEL_COMPLETE = false;
	private ScreenOverlay overlay;
	private SnakeGame gamePane;
	private Rectangle confirmScreen;
	private ImageView yes;
	private ImageView no;
	private ImageView restart;
	private Pane scoreLayer;
	private Image boardImage;
	private double width = 0;
	private double height = 0;
	private float confirmX = 0;
	private float confirmXPosition = 0;
	private float accelaration = 0.3f;
	private boolean swipeRight = false;
	private boolean swipeLeft = false;
	private boolean center = true;

	public VictoryScreen(SnakeGame game, Image boardImage, double width, double height) {
		this.gamePane = game;
		this.overlay = new ScreenOverlay(game, game.getGameRoot());
		this.scoreLayer = new Pane();
		this.boardImage = boardImage;
		this.width = width;
		this.height = height;
		confirmScreenSetup();
	}

	public void finishLevel() {
		gamePane.showCursor(true, gamePane.getScene());
		PlayerTwo.LEVEL_COMPLETED = true;
		PlayerOne.LEVEL_COMPLETED = true;
		askConfirm();
	}

	private void confirmScreenSetup() {
		scoreLayer.setPrefSize(Settings.WIDTH, Settings.HEIGHT);
		confirmScreen = new Rectangle(0, 0, width, height);
		confirmScreen.setWidth(width / GameLoader.ResolutionScaleX);
		confirmScreen.setHeight(height / GameLoader.ResolutionScaleY);
		confirmScreen.setFill(new ImagePattern(boardImage));
		confirmX = (float) (0 - confirmScreen.getWidth() - 50);
		confirmScreen.setX(confirmX);
		confirmScreen.setY(Settings.HEIGHT / 2 - confirmScreen.getHeight() / 2 - 100);
		yes = new ImageView();
		no = new ImageView();
		restart = new ImageView();
		yes.setImage(GameImageBank.continueOpt);
		no.setImage(GameImageBank.quitOpt);
		restart.setImage(GameImageBank.restartOpt);
		yes.setFitWidth(200 / GameLoader.ResolutionScaleX);
		yes.setFitHeight(50 / GameLoader.ResolutionScaleY);
		no.setFitWidth(200 / GameLoader.ResolutionScaleX);
		no.setFitHeight(50 / GameLoader.ResolutionScaleY);
		yes.setX(confirmScreen.getX());
		yes.setY(confirmScreen.getY() + confirmScreen.getHeight());
		no.setX(confirmScreen.getX() + confirmScreen.getWidth() - no.getFitWidth());
		no.setY(confirmScreen.getY() + confirmScreen.getHeight());
		restart.setX(yes.getX() + yes.getFitWidth());
		restart.setY(yes.getY());
		restart.setFitWidth((no.getX() - yes.getX() - yes.getFitWidth()));
		restart.setFitHeight(no.getFitHeight());
		scoreLayer.getChildren().addAll(confirmScreen, yes, no, restart);
		processInput();
	}

	private void processInput() {
		yes.setOnMouseEntered(e -> {
			yes.setImage(GameImageBank.continueOpt2);
		});
		yes.setOnMouseExited(e -> {
			yes.setImage(GameImageBank.continueOpt);
		});
		no.setOnMouseEntered(e -> {
			no.setImage(GameImageBank.quitOpt2);
		});
		no.setOnMouseExited(e -> {
			no.setImage(GameImageBank.quitOpt);
		});
		no.setOnMouseClicked(e -> {
			gamePane.addFadeScreen();
		});
		restart.setOnMouseEntered(e -> {
			restart.setImage(GameImageBank.restartOpt2);
		});
		restart.setOnMouseExited(e -> {
			restart.setImage(GameImageBank.restartOpt);
		});
		restart.setOnMouseClicked(e -> {
			restartLevel();
		});

	}

	public void swipeRight() {
		if (swipeRight == true) {
			confirmScreen.setX(confirmX);
			confirmX += confirmXPosition;
			confirmXPosition += accelaration;
			if (center) {
				accelaration -= 0.70;
				if (accelaration <= 0) {

					accelaration = 0;
					confirmXPosition -= 1.17;
					if (confirmXPosition <= 0.25) {
						confirmXPosition = 0.25f;
					}

				}
				if (confirmX >= Settings.WIDTH / 2 - confirmScreen.getWidth() / 2) {
					confirmX = (float) (Settings.WIDTH / 2 - confirmScreen.getWidth() / 2);
					confirmXPosition = 0;
					blurOut();
					swipeRight = false;
					center = false;
				}
			}
			yes.setX(confirmScreen.getX());
			yes.setY(confirmScreen.getY() + confirmScreen.getHeight());
			no.setX(confirmScreen.getX() + confirmScreen.getWidth() - no.getFitWidth());
			no.setY(confirmScreen.getY() + confirmScreen.getHeight());
			restart.setX(yes.getX() + yes.getFitWidth());
			restart.setY(yes.getY());
		}
		overlay.updateEffect();
		hide();
	}

	public void hide() {
		if (swipeLeft == true) {
			confirmScreen.setX(confirmX);
			confirmX -= confirmXPosition;
			confirmXPosition += accelaration;
			if (center) {
				accelaration -= 0.50;
				if (accelaration <= 0) {
					confirmXPosition -= 0.1;
					accelaration = 0;
					if (confirmXPosition <= 0.001) {
						confirmXPosition = 0.001f;
					}

				}
				if (confirmX <= 0 - confirmScreen.getWidth() + 50) {
					confirmX = (float) (0 - confirmScreen.getWidth() + 50);
					confirmXPosition = 0;
					swipeLeft = false;
					gamePane.restart();
					PlayerOne.LEVEL_COMPLETED = false;
					PlayerTwo.LEVEL_COMPLETED = false;
					center = false;
				}
			}
			yes.setX(confirmScreen.getX());
			yes.setY(confirmScreen.getY() + confirmScreen.getHeight());
			no.setX(confirmScreen.getX() + confirmScreen.getWidth() - no.getFitWidth());
			no.setY(confirmScreen.getY() + confirmScreen.getHeight());
			restart.setX(yes.getX() + yes.getFitWidth());
			restart.setY(yes.getY());
		}
	}

	public void restartLevel() {
		overlay.removeBlur();
		center = true;
		swipeLeft = true;
		accelaration = 6.0f;
		confirmXPosition = 0.001f;
	}

	public void removeBoard() {
		confirmScreen.setVisible(false);
		yes.setVisible(false);
		no.setVisible(false);
		restart.setVisible(false);
		gamePane.getMainRoot().getChildren().remove(scoreLayer);
		LEVEL_COMPLETE = false;
		confirmScreen.setX(0 - confirmScreen.getWidth() - 50);
		confirmX = 0;
		accelaration = 6.0f;
		center = false;
	}

	private void askConfirm() {
		confirmX = (float) (0 - confirmScreen.getWidth() - 50);
		confirmScreen.setX(confirmX);
		yes.setX(confirmScreen.getX());
		yes.setY(confirmScreen.getY() + confirmScreen.getHeight());
		no.setX(confirmScreen.getX() + confirmScreen.getWidth() - no.getFitWidth());
		no.setY(confirmScreen.getY() + confirmScreen.getHeight());
		restart.setX(yes.getX() + yes.getFitWidth());
		restart.setY(yes.getY());
		confirmScreen.setVisible(true);
		yes.setVisible(true);
		no.setVisible(true);
		restart.setVisible(true);
		gamePane.getMainRoot().getChildren().add(scoreLayer);
		center = true;
		swipeRight = true;
		accelaration = 8.0f;
		confirmXPosition = 0.002f;
	}

	public void blurOut() {
		overlay.levelCompleteBlur();
	}

	public void removeBlur() {
		overlay.removeBlur();
		PlayerTwo.LEVEL_COMPLETED = false;
		PlayerOne.LEVEL_COMPLETED = false;
		removeBoard();
	}
}
