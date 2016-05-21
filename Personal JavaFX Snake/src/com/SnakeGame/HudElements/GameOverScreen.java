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

public class GameOverScreen {

	private ScreenOverlay overlay;
	private SnakeGame gamePane;
	private Rectangle gameOverScreen;
	private ImageView continue_btt;
	private ImageView quitGame_btt;
	private ImageView restart_btt;
	private ImageView optionsBoard;
	private Pane scoreLayer;
	private Image boardImage;
	private double width = 0;
	private double height = 0;
	private float boardX = 0;
	private float boardXPosition = 0;
	private float accelaration = 0.3f;
	private boolean swipeRight = false;
	private boolean swipeLeft = false;
	private boolean center = true;
	public static boolean FAILED_LEVEL = false;

	public GameOverScreen(SnakeGame game, Image boardImage, double width, double height) {
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
		PlayerOne.LEVEL_COMPLETED = true;
		PlayerTwo.LEVEL_COMPLETED = true;
		gameOver();
	}

	private void confirmScreenSetup() {
		scoreLayer.setPrefSize(Settings.WIDTH, Settings.HEIGHT);
		gameOverScreen = new Rectangle(0, 0, width, height);
		gameOverScreen.setWidth(width);
		gameOverScreen.setHeight(height);
		gameOverScreen.setFill(new ImagePattern(boardImage));
		boardX = (float) (0 - gameOverScreen.getWidth() - SnakeGame.ScaleX(50));
		gameOverScreen.setX(boardX);
		gameOverScreen.setY(Settings.HEIGHT / 2 - gameOverScreen.getHeight() / 2 - SnakeGame.ScaleY(100));
		continue_btt = new ImageView(GameImageBank.continue_button);
		quitGame_btt = new ImageView(GameImageBank.quit_button);
		restart_btt = new ImageView(GameImageBank.restart_button);
		optionsBoard = new ImageView(GameImageBank.options_board);
		optionsBoard.setFitWidth(width);
		optionsBoard.setFitHeight((height/4));
		continue_btt.setFitWidth(SnakeGame.ScaleX(240));
		continue_btt.setFitHeight(SnakeGame.ScaleY(70));
		quitGame_btt.setFitWidth(SnakeGame.ScaleX(240));
		quitGame_btt.setFitHeight(SnakeGame.ScaleY(70));
		restart_btt.setFitWidth((continue_btt.getFitWidth()));
		restart_btt.setFitHeight(quitGame_btt.getFitHeight());
		scoreLayer.getChildren().addAll(gameOverScreen,optionsBoard, continue_btt, quitGame_btt, restart_btt);
		processInput();
	}

	private void processInput() {
		continue_btt.setOnMouseEntered(e -> {
			continue_btt.setImage(GameImageBank.continueOpt2);
		});
		continue_btt.setOnMouseExited(e -> {
			continue_btt.setImage(GameImageBank.continueOpt);
		});
		quitGame_btt.setOnMouseEntered(e -> {
			quitGame_btt.setImage(GameImageBank.quitOpt2);
		});
		quitGame_btt.setOnMouseExited(e -> {
			quitGame_btt.setImage(GameImageBank.quitOpt);
		});
		quitGame_btt.setOnMouseClicked(e -> {
			gamePane.addFadeScreen();
		});
		restart_btt.setOnMouseEntered(e -> {
			restart_btt.setImage(GameImageBank.restartOpt2);
		});
		restart_btt.setOnMouseExited(e -> {
			restart_btt.setImage(GameImageBank.restartOpt);
		});
		restart_btt.setOnMouseClicked(e -> {
			restartLevel();
		});

	}

	public void swipeDown() {
		if (swipeRight == true) {
			gameOverScreen.setY(boardX);
			boardX += boardXPosition;
			boardXPosition += accelaration;
			if (center) {
				accelaration -= 0.60;
				if (accelaration <= 0) {

					accelaration = 0;
					boardXPosition -= 0.60;
					if (boardXPosition <= 0.25) {
						boardXPosition = 0.25f;
					}

				}
				if (boardX >= Settings.HEIGHT / 2 - gameOverScreen.getHeight() / 1.5) {
					boardX = (float) (Settings.HEIGHT / 2 - gameOverScreen.getHeight() / 1.5);
					boardXPosition = 0;
					blurOut();
					swipeRight = false;
					center = false;
				}
			}
			continue_btt.setX(gameOverScreen.getX());
			continue_btt.setY(gameOverScreen.getY() + gameOverScreen.getHeight());
			quitGame_btt.setX(gameOverScreen.getX() + gameOverScreen.getWidth() - quitGame_btt.getFitWidth());
			quitGame_btt.setY(gameOverScreen.getY() + gameOverScreen.getHeight());
			restart_btt.setX(continue_btt.getX() + continue_btt.getFitWidth());
			restart_btt.setY(continue_btt.getY());
		}
		overlay.updateEffect();
		hide();
	}

	public void hide() {
		if (swipeLeft == true) {
			gameOverScreen.setY(boardX);
			boardX -= boardXPosition;
			boardXPosition += accelaration;
			if (center) {
				accelaration -= 0.50;
				if (accelaration <= 0) {
					boardXPosition -= 0.1;
					accelaration = 0;
					if (boardXPosition <= 0.001) {
						boardXPosition = 0.001f;
					}

				}
				if (boardX <= 0 - (gameOverScreen.getHeight() + 150)) {
					boardX = (float) (0 - gameOverScreen.getHeight() + 50);
					boardXPosition = 0;
					swipeLeft = false;
					gamePane.restart();
					PlayerOne.LEVEL_COMPLETED = false;
					PlayerTwo.LEVEL_COMPLETED = false;
					center = false;
				}
			}
			continue_btt.setX(gameOverScreen.getX());
			continue_btt.setY(gameOverScreen.getY() + gameOverScreen.getHeight());
			quitGame_btt.setX(gameOverScreen.getX() + gameOverScreen.getWidth() - quitGame_btt.getFitWidth());
			quitGame_btt.setY(gameOverScreen.getY() + gameOverScreen.getHeight());
			restart_btt.setX(continue_btt.getX() + continue_btt.getFitWidth());
			restart_btt.setY(continue_btt.getY());
		}
	}

	public void restartLevel() {
		overlay.removeBlur();
		center = true;
		swipeLeft = true;
		accelaration = 6.0f;
		boardXPosition = 0.001f;
	}

	public void removeBoard() {
		gameOverScreen.setVisible(false);
		continue_btt.setVisible(false);
		quitGame_btt.setVisible(false);
		restart_btt.setVisible(false);
		gamePane.getMainRoot().getChildren().remove(scoreLayer);
		FAILED_LEVEL = false;
		gameOverScreen.setX(0 - gameOverScreen.getWidth() - 50);
		boardX = 0;
		accelaration = 5.0f;
		center = false;
	}

	private void gameOver() {
		boardX = (float) (0 - gameOverScreen.getHeight() - 50);
		gameOverScreen.setY(boardX);
		gameOverScreen.setX((Settings.WIDTH / 2 - gameOverScreen.getWidth() / 2));
		continue_btt.setX(gameOverScreen.getX());
		continue_btt.setY(gameOverScreen.getY() + gameOverScreen.getHeight());
		quitGame_btt.setX(gameOverScreen.getX() + gameOverScreen.getWidth() - quitGame_btt.getFitWidth());
		quitGame_btt.setY(gameOverScreen.getY() + gameOverScreen.getHeight());
		restart_btt.setX(continue_btt.getX() + continue_btt.getFitWidth());
		restart_btt.setY(continue_btt.getY());
		gameOverScreen.setVisible(true);
		continue_btt.setVisible(true);
		quitGame_btt.setVisible(true);
		restart_btt.setVisible(true);
		gamePane.getFadeScreenLayer().getChildren().add(scoreLayer);
		center = true;
		swipeRight = true;
		accelaration = 5.5f;
		boardXPosition = 0.002f;
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
