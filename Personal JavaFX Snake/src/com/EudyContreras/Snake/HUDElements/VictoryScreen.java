package com.EudyContreras.Snake.HUDElements;

import com.EudyContreras.Snake.AbstractModels.AbstractHudElement;
import com.EudyContreras.Snake.EnumIDs.GameStateID;
import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.FrameWork.GameSettings;
import com.EudyContreras.Snake.ImageBanks.GameImageBank;
import com.EudyContreras.Snake.PlayerOne.PlayerOne;
import com.EudyContreras.Snake.PlayerTwo.PlayerTwo;
import com.EudyContreras.Snake.Utilities.ScreenEffectUtility;

import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class VictoryScreen extends AbstractHudElement{

	public static boolean LEVEL_COMPLETE = false;
	private ScreenEffectUtility overlay;
	private DropShadow borderGlow;
	private DropShadow boardPulse;
	private GameManager game;
	private Rectangle confirmScreen;
	private ImageView continue_btt;
	private ImageView quitGame_btt;
	private ImageView restart_btt;
	private ImageView optionsBoard;
	private Pane scoreLayer;
	private Image boardImage;
	private int currentChoice = 1;
	private double widthPan = 12;
	private double heightPan = 4;
	private double width = 0;
	private double height = 0;
	private double pulse = 0;
	private double confirmX = 0;
	private double confirmXPosition = 0;
	private double acceleration = 0.3f;
	private boolean allowGlow = false;
	private boolean pulseUp = true;
	private boolean pulseDown = false;
	private boolean goToNext = false;
	private boolean restartLevel = false;
	private boolean swipeRight = false;
	private boolean swipeLeft = false;
	private boolean panIn = false;
	private boolean center = true;



	public VictoryScreen(GameManager game, Image boardImage, double width, double height) {
		this.game = game;
		this.overlay = game.getOverlayEffect();
		this.scoreLayer = new Pane();
		this.boardImage = boardImage;
		this.width = width;
		this.height = height;
		this.borderGlow = new DropShadow();
		this.boardPulse = new DropShadow();
		this.borderGlow.setOffsetY(0f);
		this.borderGlow.setOffsetX(0f);
		this.borderGlow.setSpread(0.3);
		this.borderGlow.setWidth(35);
		this.borderGlow.setHeight(35);
		this.boardPulse.setOffsetY(0f);
		this.boardPulse.setOffsetX(0f);
		this.boardPulse.setSpread(0.5);
		this.boardPulse.setWidth(0);
		this.boardPulse.setHeight(0);
		this.borderGlow.setColor(Color.WHITE);
		this.boardPulse.setColor(Color.GREEN);
		this.borderGlow.setBlurType(BlurType.THREE_PASS_BOX);
		this.boardPulse.setBlurType(BlurType.ONE_PASS_BOX);
		confirmScreenSetup();
	}
	private void confirmScreenSetup() {
		confirmScreen = new Rectangle(0, 0, width, height);
		confirmScreen.setWidth(width);
		confirmScreen.setHeight(height);
		confirmScreen.setFill(new ImagePattern(boardImage));
		confirmScreen.setEffect(boardPulse);
		scoreLayer.setPrefSize(GameSettings.WIDTH, GameSettings.HEIGHT);
		confirmX = 0 - width- 50;
		confirmScreen.setY(GameSettings.HEIGHT / 2 - confirmScreen.getHeight() / 2 - GameManager.ScaleY(30));
		continue_btt = new ImageView(GameImageBank.continue_button);
		quitGame_btt = new ImageView(GameImageBank.quit_button);
		restart_btt = new ImageView(GameImageBank.restart_button);
		optionsBoard = new ImageView(GameImageBank.options_board);
		optionsBoard.setFitWidth(width);
		optionsBoard.setFitHeight((height/4));
		continue_btt.setFitWidth(GameManager.ScaleX(240));
		continue_btt.setFitHeight(GameManager.ScaleY(70));
		quitGame_btt.setFitWidth(GameManager.ScaleX(240));
		quitGame_btt.setFitHeight(GameManager.ScaleY(70));
		restart_btt.setFitWidth((continue_btt.getFitWidth()));
		restart_btt.setFitHeight(quitGame_btt.getFitHeight());
		scoreLayer.getChildren().addAll(confirmScreen,optionsBoard, continue_btt, quitGame_btt, restart_btt);
		widthOne = 1;
		heightOne = 1;
		widthPan = 12/GameManager.ScaleX;
		heightPan = 7/GameManager.ScaleY;
		velROne = 15;
		processMouseHandling();
	}
	public void finishLevel() {
		game.showCursor(true, game.getScene());
		PlayerTwo.LEVEL_COMPLETED = true;
		PlayerOne.LEVEL_COMPLETED = true;
		if(game.getGameLoader().getPlayerOne().getAppleCount()>game.getGameLoader().getPlayerTwo().getAppleCount()){
			this.boardImage = GameImageBank.player_one_wins;
		}
		else if(game.getGameLoader().getPlayerOne().getAppleCount()<game.getGameLoader().getPlayerTwo().getAppleCount()){
			this.boardImage = GameImageBank.player_two_wins;
		}
		else if(game.getGameLoader().getPlayerOne().getAppleCount()==game.getGameLoader().getPlayerTwo().getAppleCount()){
			this.boardImage = GameImageBank.draw_game;
		}
		askConfirm();
		
	}

	private void processMouseHandling() {
		continue_btt.setOnMouseEntered(e -> {
			selectionReset();
			currentChoice = 1;
			borderGlow.setColor(Color.rgb(0,240,0));
			continue_btt.setEffect(borderGlow);
		});
		continue_btt.setOnMouseExited(e -> {
			continue_btt.setEffect(null);
		});
		continue_btt.setOnMouseClicked(e -> {
			game.setStateID(GameStateID.LEVEL_TRANSITIONING);
			goToNext();
		});
		quitGame_btt.setOnMouseEntered(e -> {
			selectionReset();
			currentChoice = 2;
			borderGlow.setColor(Color.rgb(240,0,0));
			quitGame_btt.setEffect(borderGlow);
		});
		quitGame_btt.setOnMouseExited(e -> {
			quitGame_btt.setEffect(null);
		});
		quitGame_btt.setOnMouseClicked(e -> {
			game.setStateID(GameStateID.MAIN_MENU);
			game.getFadeScreenHandler().menu_fade_screen();
		});
		restart_btt.setOnMouseEntered(e -> {
			currentChoice = 3;
			selectionReset();
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
	/**
	 * Sets the key input handling for the labels
	 * Code below determines what will happen if the user presses enter or
	 * space on the different choices
	 */
	private void processKeyHandling() {
		updateSelections();
		game.getScene().setOnKeyPressed(e -> {
			switch (e.getCode()) {
			case LEFT:
				currentChoice -= 1;
				if (currentChoice <= 1) {
					currentChoice = 1;
				}
				break;
			case RIGHT:
				currentChoice += 1;
				if (currentChoice >= 3) {
					currentChoice = 3;
				}
				break;
			case A:
				currentChoice -= 1;
				if (currentChoice <= 1) {
					currentChoice = 1;
				}
				break;
			case D:
				currentChoice += 1;
				if (currentChoice >= 3) {
					currentChoice = 3;
				}
				break;
			case ENTER:
				if (currentChoice == 1) {
					game.setStateID(GameStateID.LEVEL_TRANSITIONING);
					goToNext();
				}
				if (currentChoice == 2) {
					game.setStateID(GameStateID.LEVEL_RESTART);
					restartLevel();
				}
				if (currentChoice == 3) {
					game.setStateID(GameStateID.MAIN_MENU);
					game.getFadeScreenHandler().menu_fade_screen();
				}
				break;
			case SPACE:
				if (currentChoice == 1) {
					game.setStateID(GameStateID.LEVEL_TRANSITIONING);
					goToNext();
				}
				if (currentChoice == 2) {
					game.setStateID(GameStateID.LEVEL_RESTART);
					restartLevel();
				}
				if (currentChoice == 3) {
					game.setStateID(GameStateID.MAIN_MENU);
					game.getFadeScreenHandler().menu_fade_screen();
				}
				break;
			default:
				break;
			}
			updateSelections();
		});

		}
	public void updateSelections(){
		if(currentChoice==1){
			borderGlow.setColor(Color.rgb(0,240,0));
			continue_btt.setEffect(borderGlow);
			restart_btt.setEffect(null);
			quitGame_btt.setEffect(null);
		}
		if(currentChoice==2){
			borderGlow.setColor(Color.rgb(240,150,0));
			restart_btt.setEffect(borderGlow);
			quitGame_btt.setEffect(null);
			continue_btt.setEffect(null);
		}
		if(currentChoice==3){
			borderGlow.setColor(Color.rgb(240,0,0));
			quitGame_btt.setEffect(borderGlow);
			restart_btt.setEffect(null);
			continue_btt.setEffect(null);
		}
	}
	public void selectionReset(){
		continue_btt.setEffect(null);
		restart_btt.setEffect(null);
		quitGame_btt.setEffect(null);
	}
	public void boardPulse(){
		if(allowGlow){
		boardPulse.setWidth(pulse);
		boardPulse.setHeight(pulse);
		if(pulseUp){
			pulse+=1.5;
			if(pulse>=120){
				pulseUp = false;
				pulseDown = true;
			}
		}
		if(pulseDown){
			pulse-=1.5;
			if(pulse<=20){
				pulseDown = false;
				pulseUp = true;
			}
		}
		}
	}
	public void updateUI(){
		boardPulse();
		swipeRight();
		panIn();
		hide();

	}
	public void swipeRight() {
		if (swipeRight == true) {
			//confirmScreen.setX(confirmX);
			optionsBoard.setX(confirmX);
			confirmX += confirmXPosition/GameManager.ScaleX;
			confirmXPosition += acceleration;
			if (center) {
				acceleration -= 0.70;
				if (acceleration <= 0) {

					acceleration = 0;
					confirmXPosition -= 1.15;
					if (confirmXPosition <= 0.25) {
						confirmXPosition = 0.25f;
					}

				}
				if (confirmX >= GameSettings.WIDTH / 2 - width / 2) {
					confirmX = GameSettings.WIDTH / 2 - width / 2;
					confirmXPosition = 0;
					acceleration = 0;
					processKeyHandling();
					currentChoice = 1;
					swipeRight = false;
					center = false;
	
				}
			}
			continue_btt.setX(optionsBoard.getX()+20/GameManager.ScaleX);
			continue_btt.setY(optionsBoard.getY()+20/GameManager.ScaleY);
			quitGame_btt.setX(optionsBoard.getX() + optionsBoard.getFitWidth() - quitGame_btt.getFitWidth()-20/GameManager.ScaleX);
			quitGame_btt.setY(optionsBoard.getY()+20/GameManager.ScaleY);
			restart_btt.setX(continue_btt.getX() + continue_btt.getFitWidth()+23/GameManager.ScaleX);
			restart_btt.setY(continue_btt.getY());
		}
	}
	public void panIn() {
		if(panIn){
		this.rOne = rOne+velROne;
		this.widthOne += widthPan;
		this.heightOne += heightPan;
		this.confirmScreen.setWidth(widthOne);
		this.confirmScreen.setHeight(heightOne);
		this.confirmScreen.setRotate(rOne);
		this.confirmScreen.setX(GameSettings.WIDTH/2-confirmScreen.getWidth()/2);
		this.confirmScreen.setY(GameSettings.HEIGHT/2-confirmScreen.getHeight()/2);
		if(widthOne>=width){
			heightPan = 0;
			widthPan = 0;
		}
		if(heightOne>=height){;
			
		}
		if(widthOne>=width && heightOne>=height){
			if(rOne>=360){
				velROne = 0;
				panIn = false;
				allowGlow = true;
				blurOut();
			}
		}
		else{
			if(rOne>=360){
				rOne = 0;
			}
		}
		}
	}
	public void hide() {
		if (swipeLeft == true) {
			confirmScreen.setX(confirmX);
			optionsBoard.setX(confirmX);
			confirmX -= confirmXPosition/GameManager.ScaleX;
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
					game.processGameInput();
					if(restartLevel){
						game.getFadeScreenHandler().quick_restart_fade_screen();
						PlayerOne.LEVEL_COMPLETED = false;
						PlayerTwo.LEVEL_COMPLETED = false;
					}
					if(goToNext){
						game.getFadeScreenHandler().continue_fade_screen();
					}
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
		game.removePlayers();
		overlay.removeBlur();
		restartLevel = true;
		goToNext = false;
		center = true;
		swipeLeft = true;
		acceleration = 6.0f;
		confirmXPosition = 0.002f;
	}
	public void goToNext(){
		game.removePlayers();
		overlay.removeBlur();
		goToNext = true;
		restartLevel = false;
		center = true;
		swipeLeft = true;
		acceleration = 6.0f;
		confirmXPosition = 0.002f;
	}

	public void removeBoard() {
		confirmScreen.setVisible(false);
		optionsBoard.setVisible(false);
		continue_btt.setVisible(false);
		quitGame_btt.setVisible(false);
		restart_btt.setVisible(false);
		game.getMainRoot().getChildren().remove(scoreLayer);
		LEVEL_COMPLETE = false;
		confirmScreen.setX(0 - confirmScreen.getWidth() - 50);
		confirmX = 0;
		acceleration = 6.0f;
		center = false;
	}

	private void askConfirm() {
		game.setStateID(GameStateID.LEVEL_COMPLETED);
		confirmScreen.setFill(new ImagePattern(boardImage));
		confirmX = 0 - width - 50;
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
		panIn = true;
		widthOne = 1;
		heightOne = 1;
		widthPan = 14/GameManager.ScaleX;
		heightPan = 8/GameManager.ScaleY;
		velROne = 12;
		acceleration = 8.0f;
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
