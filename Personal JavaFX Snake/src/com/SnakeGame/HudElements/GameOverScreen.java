package com.SnakeGame.HudElements;

import com.SnakeGame.FrameWork.Settings;
import com.SnakeGame.FrameWork.SnakeGame;
import com.SnakeGame.IDenums.GameStateID;
import com.SnakeGame.ImageBanks.GameImageBank;
import com.SnakeGame.PlayerOne.PlayerOne;
import com.SnakeGame.PlayerTwo.PlayerTwo;
import com.SnakeGame.Utilities.ScreenOverlay;

import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class GameOverScreen {

	public static boolean LEVEL_COMPLETE = false;
	public static boolean FAILED_LEVEL = false;
	private ScreenOverlay overlay;
	private DropShadow borderGlow;
	private SnakeGame game;
	private Rectangle confirmScreen;
	private ImageView continue_btt;
	private ImageView quitGame_btt;
	private ImageView restart_btt;
	private ImageView optionsBoard;
	private Pane scoreLayer;
	private Image boardImage;
	private double width = 0;
	private double height = 0;
	private double confirmX = 0;
	private double confirmXPosition = 0;
	private double acceleration = 0.3f;
	private boolean swipeRight = false;
	private boolean swipeLeft = false;
	private boolean center = true;

	public GameOverScreen(SnakeGame game, Image boardImage, double width, double height) {
		this.game = game;
		this.overlay = new ScreenOverlay(game, game.getGameRoot());
		this.scoreLayer = new Pane();
		this.boardImage = boardImage;
		this.width = width;
		this.height = height;
		this.borderGlow = new DropShadow();
		this.borderGlow.setOffsetY(0f);
		this.borderGlow.setOffsetX(0f);
		this.borderGlow.setSpread(0.3);
		this.borderGlow.setColor(Color.WHITE);
		this.borderGlow.setWidth(35);
		this.borderGlow.setHeight(35);
		this.borderGlow.setBlurType(BlurType.THREE_PASS_BOX);
		confirmScreenSetup();
	}
	private void confirmScreenSetup() {
		confirmScreen = new Rectangle(0, 0, width, height);
		confirmScreen.setWidth(width);
		confirmScreen.setHeight(height);
		confirmScreen.setFill(new ImagePattern(boardImage));
		scoreLayer.setPrefSize(Settings.WIDTH, Settings.HEIGHT);
		confirmX = 0 - confirmScreen.getWidth() - 50;
		confirmScreen.setY(Settings.HEIGHT / 2 - confirmScreen.getHeight() / 2 - SnakeGame.ScaleY(30));
		continue_btt = new ImageView(GameImageBank.continue_button_alt);
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
		scoreLayer.getChildren().addAll(confirmScreen,optionsBoard, continue_btt, quitGame_btt, restart_btt);
		processInput();
	}
	public void finishLevel() {
		game.showCursor(true, game.getScene());
		askConfirm();
	}

	private void processInput() {
		quitGame_btt.setOnMouseEntered(e -> {
			borderGlow.setColor(Color.rgb(240,0,0));
			quitGame_btt.setEffect(borderGlow);
		});
		quitGame_btt.setOnMouseExited(e -> {
			quitGame_btt.setEffect(null);
		});
		quitGame_btt.setOnMouseClicked(e -> {
			game.setStateID(GameStateID.MAIN_MENU);
			game.addFadeScreen();
		});
		restart_btt.setOnMouseEntered(e -> {
			borderGlow.setColor(Color.rgb(240, 150,0));
			restart_btt.setEffect(borderGlow);
		});
		restart_btt.setOnMouseExited(e -> {
			restart_btt.setEffect(null);
		});
		restart_btt.setOnMouseClicked(e -> {
			game.setStateID(GameStateID.LEVEL_RESTART);
			restartLevel();
		});

	}
	public void checkStatus(){
		if (PlayerOne.DEAD || PlayerTwo.DEAD) {
			if (FAILED_LEVEL == false) {
				removeBoard();
				finishLevel();
				game.getScoreKeeper().swipeDown();
				game.getGameHud().swipeDown();
				FAILED_LEVEL = true;
			}
		}
	}
	public void swipeDown() {
		if (swipeRight == true) {
			confirmScreen.setX(confirmX);
			optionsBoard.setX(confirmX);
			confirmX += confirmXPosition;
			confirmXPosition += acceleration;
			if (center) {
				acceleration -= 0.70;
				if (acceleration <= 0) {

					acceleration = 0;
					confirmXPosition -= 1.17;
					if (confirmXPosition <= 0.25) {
						confirmXPosition = 0.25f;
					}

				}
				if (confirmX >= Settings.WIDTH / 2 - confirmScreen.getWidth() / 2) {
					confirmX = (float) (Settings.WIDTH / 2 - confirmScreen.getWidth() / 2);
					confirmXPosition = 0;
					blurOut();
					fadeOut();
					swipeRight = false;
					center = false;
				}
			}
			continue_btt.setX(optionsBoard.getX()+20/SnakeGame.ScaleX);
			continue_btt.setY(optionsBoard.getY()+20/SnakeGame.ScaleY);
			quitGame_btt.setX(optionsBoard.getX() + optionsBoard.getFitWidth() - quitGame_btt.getFitWidth()-20/SnakeGame.ScaleX);
			quitGame_btt.setY(optionsBoard.getY()+20/SnakeGame.ScaleY);
			restart_btt.setX(continue_btt.getX() + continue_btt.getFitWidth()+23/SnakeGame.ScaleX);
			restart_btt.setY(continue_btt.getY());
		}
		overlay.updateEffect();
		hide();
	}

	public void hide() {
		if (swipeLeft == true) {
			confirmScreen.setX(confirmX);
			optionsBoard.setX(confirmX);
			confirmX -= confirmXPosition;
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
				if (confirmX <= 0 - confirmScreen.getWidth() - 50) {
					confirmX = (float) (0 - confirmScreen.getWidth() + 50);
					confirmXPosition = 0;
					swipeLeft = false;
					game.restartLevel();
					PlayerOne.LEVEL_COMPLETED = false;
					PlayerTwo.LEVEL_COMPLETED = false;
					center = false;
				}
			}
			continue_btt.setX(optionsBoard.getX()+20);
			continue_btt.setY(optionsBoard.getY()+20);
			quitGame_btt.setX(optionsBoard.getX() + optionsBoard.getFitWidth() - quitGame_btt.getFitWidth()-20);
			quitGame_btt.setY(optionsBoard.getY()+20);
			restart_btt.setX(continue_btt.getX() + continue_btt.getFitWidth()+20);
			restart_btt.setY(continue_btt.getY());
		}
	}

	public void restartLevel() {
		overlay.removeBlur();
		center = true;
		swipeLeft = true;
		acceleration = 6.0f;
		confirmXPosition = 0.001f;
	}

	public void removeBoard() {
		confirmScreen.setVisible(false);
		optionsBoard.setVisible(false);
		continue_btt.setVisible(false);
		quitGame_btt.setVisible(false);
		restart_btt.setVisible(false);
		game.getMainRoot().getChildren().remove(scoreLayer);
		FAILED_LEVEL = false;
		confirmScreen.setX(0 - confirmScreen.getWidth() - 50);
		confirmX = 0;
		acceleration = 6.0f;
		center = false;
	}

	private void askConfirm() {
		confirmScreen.setFill(new ImagePattern(boardImage));
		confirmX = (float) (0 - confirmScreen.getWidth() - 50);
		confirmScreen.setX(confirmX);
		continue_btt.setX(confirmScreen.getX());
		continue_btt.setY(confirmScreen.getY() + confirmScreen.getHeight());
		quitGame_btt.setX(confirmScreen.getX() + confirmScreen.getWidth() - quitGame_btt.getFitWidth());
		quitGame_btt.setY(confirmScreen.getY() + confirmScreen.getHeight());
		restart_btt.setX(continue_btt.getX() + continue_btt.getFitWidth());
		restart_btt.setY(continue_btt.getY());
		confirmScreen.setVisible(true);
		optionsBoard.setVisible(true);
		continue_btt.setVisible(true);
		quitGame_btt.setVisible(true);
		restart_btt.setVisible(true);
		game.getMainRoot().getChildren().add(scoreLayer);
		center = true;
		swipeRight = true;
		acceleration = 8.0f;
		confirmXPosition = 0.002f;
	}

	public void blurOut() {
		overlay.addDeathBlur();
	}

	public void fadeOut(){
		game.renderFadeScreen();
	}

	public void removeBlur() {
		overlay.removeBlur();
		PlayerTwo.LEVEL_COMPLETED = false;
		PlayerOne.LEVEL_COMPLETED = false;
		removeBoard();
	}
}
