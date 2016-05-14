package com.SnakeGame.HudElements;

import com.SnakeGame.FrameWork.GameLoader;
import com.SnakeGame.FrameWork.Settings;
import com.SnakeGame.FrameWork.SnakeGame;
import com.SnakeGame.ImageBanks.GameImageBank;
import com.SnakeGame.PlayerOne.OrgPlayer;
import com.SnakeGame.PlayerTwo.Player2;
import com.SnakeGame.Utilities.ScreenOverlay;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GameOverScreen {

	ScreenOverlay overlay;
	SnakeGame gamePane;
	Rectangle gameOverScreen;
	ImageView yes;
	ImageView no;
	ImageView restart;
	Pane scoreLayer;
	Text text;
	Font font;
	Image boardImage;
	float position = 0;
	double width = 0;
	double height = 0;
	float delay = 0;
	float boardX = 0;
	float boardXPosition = 0;
	float accelaration = 0.3f;
	boolean swipeRight = false;
	boolean swipeLeft = false;
	boolean center = true;
	public static boolean FAILED_LEVEL = false;

	public GameOverScreen(SnakeGame game, Image boardImage, double width, double height) {
		this.gamePane = game;
		this.text = new Text();
		this.overlay = new ScreenOverlay(game, game.getGameRoot());
		this.scoreLayer = new Pane();
		this.boardImage = boardImage;
		this.width = width;
		this.height = height;
		confirmScreenSetup();
	}

	public void finishLevel() {
		gamePane.showCursor(true, gamePane.getScene());
		OrgPlayer.LEVEL_COMPLETED = true;
		Player2.levelComplete = true;
		gameOver();
	}

	private void confirmScreenSetup() {
		scoreLayer.setPrefSize(Settings.WIDTH, Settings.HEIGHT);
		gameOverScreen = new Rectangle(0, 0, width, height);
		gameOverScreen.setWidth(width / GameLoader.ResolutionScaleX);
		gameOverScreen.setHeight(height / GameLoader.ResolutionScaleY);
		gameOverScreen.setFill(new ImagePattern(boardImage));
		boardX = (float) (0 - gameOverScreen.getWidth() - 50);
		gameOverScreen.setX(boardX);
		gameOverScreen.setY(Settings.HEIGHT / 2 - gameOverScreen.getHeight() / 2 - 100);
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
		yes.setX(gameOverScreen.getX());
		yes.setY(gameOverScreen.getY() + gameOverScreen.getHeight());
		no.setX(gameOverScreen.getX() + gameOverScreen.getWidth() - no.getFitWidth());
		no.setY(gameOverScreen.getY() + gameOverScreen.getHeight());
		restart.setX(yes.getX() + yes.getFitWidth());
		restart.setY(yes.getY());
		restart.setFitWidth((no.getX() - yes.getX() - yes.getFitWidth()));
		restart.setFitHeight(no.getFitHeight());
		scoreLayer.getChildren().addAll(gameOverScreen, yes, no, restart);
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

	public void swipeFromLeft() {
		if (swipeRight == true) {
			gameOverScreen.setY(boardX);
			boardX += boardXPosition;
			boardXPosition += accelaration;
			if (center) {
				accelaration -= 0.60;
				if (accelaration <= 0) {

					accelaration = 0;
					// if(confirmXPosition>=0.005){
					// //confirmXPosition = 0.001f;
					// }
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
			yes.setX(gameOverScreen.getX());
			yes.setY(gameOverScreen.getY() + gameOverScreen.getHeight());
			no.setX(gameOverScreen.getX() + gameOverScreen.getWidth() - no.getFitWidth());
			no.setY(gameOverScreen.getY() + gameOverScreen.getHeight());
			restart.setX(yes.getX() + yes.getFitWidth());
			restart.setY(yes.getY());
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
				if (boardX <= 0 - gameOverScreen.getHeight() + 50) {
					boardX = (float) (0 - gameOverScreen.getHeight() + 50);
					boardXPosition = 0;
					swipeLeft = false;
					gamePane.restart();
					OrgPlayer.LEVEL_COMPLETED = false;
					Player2.levelComplete = false;
					center = false;
				}
			}
			yes.setX(gameOverScreen.getX());
			yes.setY(gameOverScreen.getY() + gameOverScreen.getHeight());
			no.setX(gameOverScreen.getX() + gameOverScreen.getWidth() - no.getFitWidth());
			no.setY(gameOverScreen.getY() + gameOverScreen.getHeight());
			restart.setX(yes.getX() + yes.getFitWidth());
			restart.setY(yes.getY());
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
		yes.setVisible(false);
		no.setVisible(false);
		restart.setVisible(false);
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
		yes.setX(gameOverScreen.getX());
		yes.setY(gameOverScreen.getY() + gameOverScreen.getHeight());
		no.setX(gameOverScreen.getX() + gameOverScreen.getWidth() - no.getFitWidth());
		no.setY(gameOverScreen.getY() + gameOverScreen.getHeight());
		restart.setX(yes.getX() + yes.getFitWidth());
		restart.setY(yes.getY());
		gameOverScreen.setVisible(true);
		yes.setVisible(true);
		no.setVisible(true);
		restart.setVisible(true);
		gamePane.getFadeScreen().getChildren().add(scoreLayer);
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
		Player2.levelComplete = false;
		OrgPlayer.LEVEL_COMPLETED = false;
		removeBoard();
	}
}
