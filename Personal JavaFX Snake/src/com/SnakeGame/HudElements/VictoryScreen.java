package com.SnakeGame.HudElements;

import com.SnakeGame.FrameWork.GameLoader;
import com.SnakeGame.FrameWork.Settings;
import com.SnakeGame.FrameWork.SnakeGame;
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

public class VictoryScreen {

	public static boolean LEVEL_COMPLETE = false;
	private ScreenOverlay overlay;
	private SnakeGame gamePane;
	private Rectangle confirmScreen;
	private ImageView continue_btt;
	private ImageView quit_btt;
	private ImageView restart_btt;
	private ImageView optionBoard;
	private Pane scoreLayer;
	private Image boardImage;
	private DropShadow borderGlow;
	private double width = 0;
	private double height = 0;
	private float confirmX = 0;
	private float confirmXPosition = 0;
	private float accelaration = 0.3f;
	private boolean swipeRight = false;
	private boolean swipeLeft = false;
	private boolean center = true;
	private boolean proceed = false;
	private boolean restart = false;

	public VictoryScreen(SnakeGame game, Image boardImage, double width, double height) {
		this.gamePane = game;
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

	public void finishLevel() {
		gamePane.showCursor(true, gamePane.getScene());
		PlayerTwo.LEVEL_COMPLETED = true;
		PlayerOne.LEVEL_COMPLETED = true;
		if(gamePane.getloader().getPlayerOne().getAppleCount()>gamePane.getloader().getPlayerTwo().getAppleCount()){
			this.boardImage = GameImageBank.player_one_wins;
		}
		else{
			this.boardImage = GameImageBank.player_two_wins;
		}
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
		confirmScreen.setY(Settings.HEIGHT / 2 - confirmScreen.getHeight() / 2 - SnakeGame.ScaleX(80));
		continue_btt = new ImageView();
		quit_btt = new ImageView();
		restart_btt = new ImageView();
		optionBoard = new ImageView(GameImageBank.options_board);
		optionBoard.setX(confirmX);
		optionBoard.setY(confirmScreen.getY()+confirmScreen.getHeight());
		optionBoard.setFitWidth(width/ GameLoader.ResolutionScaleX);
		optionBoard.setFitHeight((height/4)/GameLoader.ResolutionScaleY);
		continue_btt.setImage(GameImageBank.continue_button);
		quit_btt.setImage(GameImageBank.quit_button);
		restart_btt.setImage(GameImageBank.restart_button);
		continue_btt.setFitWidth(240 / GameLoader.ResolutionScaleX);
		continue_btt.setFitHeight(70 / GameLoader.ResolutionScaleY);
		quit_btt.setFitWidth(240 / GameLoader.ResolutionScaleX);
		quit_btt.setFitHeight(70 / GameLoader.ResolutionScaleY);
		continue_btt.setX(optionBoard.getX()+30);
		continue_btt.setY(optionBoard.getY()+30);
		quit_btt.setX(optionBoard.getX() + optionBoard.getFitWidth() - quit_btt.getFitWidth());
		quit_btt.setY(optionBoard.getY()+10);
		restart_btt.setX(continue_btt.getX() + continue_btt.getFitWidth());
		restart_btt.setY(continue_btt.getY());
		restart_btt.setFitWidth((continue_btt.getFitWidth())-5 );
		restart_btt.setFitHeight(quit_btt.getFitHeight());
		scoreLayer.getChildren().addAll(confirmScreen,optionBoard, continue_btt, quit_btt, restart_btt);
		processInput();
	}

	private void processInput() {
		continue_btt.setOnMouseEntered(e -> {
			borderGlow.setColor(Color.rgb(0,240,0));
			continue_btt.setEffect(borderGlow);
		});
		continue_btt.setOnMouseExited(e -> {
			continue_btt.setEffect(null);
		});
		continue_btt.setOnMouseClicked(e -> {
			restart = false;
			proceed = true;
			restartLevel();
		});
		quit_btt.setOnMouseEntered(e -> {
			borderGlow.setColor(Color.rgb(240,0,0));
			quit_btt.setEffect(borderGlow);
		});
		quit_btt.setOnMouseExited(e -> {
			quit_btt.setEffect(null);
		});
		quit_btt.setOnMouseClicked(e -> {
			gamePane.addFadeScreen();
		});
		restart_btt.setOnMouseEntered(e -> {
			borderGlow.setColor(Color.rgb(240, 150,0));
			restart_btt.setEffect(borderGlow);
		});
		restart_btt.setOnMouseExited(e -> {
			restart_btt.setEffect(null);
		});
		restart_btt.setOnMouseClicked(e -> {
			proceed = false;
			restart = true;
			restartLevel();
		});

	}

	public void swipeRight() {
		if (swipeRight == true) {
			confirmScreen.setX(confirmX);
			optionBoard.setX(confirmX);
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
			continue_btt.setX(optionBoard.getX()+20);
			continue_btt.setY(optionBoard.getY()+20);
			quit_btt.setX(optionBoard.getX() + optionBoard.getFitWidth() - quit_btt.getFitWidth()-20);
			quit_btt.setY(optionBoard.getY()+20);
			restart_btt.setX(continue_btt.getX() + continue_btt.getFitWidth()+20);
			restart_btt.setY(continue_btt.getY());
		}
		overlay.updateEffect();
		hide();
	}

	public void hide() {
		if (swipeLeft == true) {
			confirmScreen.setX(confirmX);
			optionBoard.setX(confirmX);
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
				if (confirmX <= 0 - confirmScreen.getWidth() - 50) {
					confirmX = (float) (0 - confirmScreen.getWidth() + 50);
					confirmXPosition = 0;
					swipeLeft = false;
					if(restart){
						gamePane.restart();
						PlayerOne.LEVEL_COMPLETED = false;
						PlayerTwo.LEVEL_COMPLETED = false;
					}
					if(proceed){
						gamePane.startNextLevel();
					}
					center = false;
				}
			}
			continue_btt.setX(optionBoard.getX()+20);
			continue_btt.setY(optionBoard.getY()+20);
			quit_btt.setX(optionBoard.getX() + optionBoard.getFitWidth() - quit_btt.getFitWidth()-20);
			quit_btt.setY(optionBoard.getY()+20);
			restart_btt.setX(continue_btt.getX() + continue_btt.getFitWidth()+20);
			restart_btt.setY(continue_btt.getY());
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
		continue_btt.setVisible(false);
		quit_btt.setVisible(false);
		restart_btt.setVisible(false);
		gamePane.getMainRoot().getChildren().remove(scoreLayer);
		LEVEL_COMPLETE = false;
		confirmScreen.setX(0 - confirmScreen.getWidth() - 50);
		confirmX = 0;
		accelaration = 6.0f;
		center = false;
	}

	private void askConfirm() {
		confirmScreen.setFill(new ImagePattern(boardImage));
		confirmX = (float) (0 - confirmScreen.getWidth() - 50);
		confirmScreen.setX(confirmX);
		continue_btt.setX(confirmScreen.getX());
		continue_btt.setY(confirmScreen.getY() + confirmScreen.getHeight());
		quit_btt.setX(confirmScreen.getX() + confirmScreen.getWidth() - quit_btt.getFitWidth());
		quit_btt.setY(confirmScreen.getY() + confirmScreen.getHeight());
		restart_btt.setX(continue_btt.getX() + continue_btt.getFitWidth());
		restart_btt.setY(continue_btt.getY());
		confirmScreen.setVisible(true);
		continue_btt.setVisible(true);
		quit_btt.setVisible(true);
		restart_btt.setVisible(true);
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
