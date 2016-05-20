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
	private ImageView yes;
	private ImageView no;
	private ImageView restart;
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
		confirmScreen.setY(Settings.HEIGHT / 2 - confirmScreen.getHeight() / 2 - 100);
		yes = new ImageView();
		no = new ImageView();
		restart = new ImageView();
		optionBoard = new ImageView(GameImageBank.options_board);
		optionBoard.setX(confirmX);
		optionBoard.setY(confirmScreen.getY()+confirmScreen.getHeight());
		optionBoard.setFitWidth(width/ GameLoader.ResolutionScaleX);
		optionBoard.setFitHeight((height/4)/GameLoader.ResolutionScaleY);
		yes.setImage(GameImageBank.continue_button);
		no.setImage(GameImageBank.quit_button);
		restart.setImage(GameImageBank.restart_button);
		yes.setFitWidth(240 / GameLoader.ResolutionScaleX);
		yes.setFitHeight(70 / GameLoader.ResolutionScaleY);
		no.setFitWidth(240 / GameLoader.ResolutionScaleX);
		no.setFitHeight(70 / GameLoader.ResolutionScaleY);
		yes.setX(optionBoard.getX()+30);
		yes.setY(optionBoard.getY()+30);
		no.setX(optionBoard.getX() + optionBoard.getFitWidth() - no.getFitWidth());
		no.setY(optionBoard.getY()+10);
		restart.setX(yes.getX() + yes.getFitWidth());
		restart.setY(yes.getY());
		restart.setFitWidth((yes.getFitWidth())-5 );
		restart.setFitHeight(no.getFitHeight());
		scoreLayer.getChildren().addAll(confirmScreen,optionBoard, yes, no, restart);
		processInput();
	}

	private void processInput() {
		yes.setOnMouseEntered(e -> {
			borderGlow.setColor(Color.rgb(0,240,0));
			yes.setEffect(borderGlow);
		});
		yes.setOnMouseExited(e -> {
			yes.setEffect(null);
		});
		no.setOnMouseEntered(e -> {
			borderGlow.setColor(Color.rgb(240,0,0));
			no.setEffect(borderGlow);
		});
		no.setOnMouseExited(e -> {
			no.setEffect(null);
		});
		no.setOnMouseClicked(e -> {
			gamePane.addFadeScreen();
		});
		restart.setOnMouseEntered(e -> {
			borderGlow.setColor(Color.rgb(240, 150,0));
			restart.setEffect(borderGlow);
		});
		restart.setOnMouseExited(e -> {
			restart.setEffect(null);
		});
		restart.setOnMouseClicked(e -> {
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
			yes.setX(optionBoard.getX()+20);
			yes.setY(optionBoard.getY()+20);
			no.setX(optionBoard.getX() + optionBoard.getFitWidth() - no.getFitWidth()-20);
			no.setY(optionBoard.getY()+20);
			restart.setX(yes.getX() + yes.getFitWidth()+20);
			restart.setY(yes.getY());
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
		confirmScreen.setFill(new ImagePattern(boardImage));
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
