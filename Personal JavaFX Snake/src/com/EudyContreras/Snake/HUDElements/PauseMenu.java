package com.EudyContreras.Snake.HUDElements;

import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.FrameWork.GameSettings;
import com.EudyContreras.Snake.Identifiers.GameModeID;
import com.EudyContreras.Snake.Identifiers.GameStateID;
import com.EudyContreras.Snake.ImageBanks.GameImageBank;
import com.EudyContreras.Snake.PlayerOne.PlayerOne;
import com.EudyContreras.Snake.PlayerTwo.PlayerTwo;
import com.EudyContreras.Snake.Utilities.ScreenEffectUtility;

import javafx.event.EventHandler;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.SwipeEvent;
import javafx.scene.paint.Color;


public class PauseMenu {
    private GameManager game;
	private ImageView mainBoard;
	private ImageView leftTouchPanel;
	private ImageView rightTouchPanel;
	private ImageView centerTouchPanel;
	private ImageView continueBtt;
	private ImageView restartBtt;
	private ImageView mainMenuBtt;
	private ImageView quitGameBtt;
	private DropShadow borderGlow;
	private ScreenEffectUtility overlay;
	private int currentChoice = 1;
	private boolean allowTouch = false;
	private boolean hide = true;
	private boolean show = false;
	private boolean goToMain = false;
	private double fadeLevel = 0.0;
	private double hideSpeedX = 0.0;
	private double hideSpeedY = -20.0;
	private double showSpeedX = 0.0;
	private double showSpeedY = 20.0;
	private double acceleration = 0.0;
	private double width = 0.0;
	private double height = 0.0;
	private double x;
	private double y;


	public PauseMenu(GameManager game, double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.game = game;
		this.width = width;
		this.height = height;
		this.initilialize();
	}
	public void initilialize(){
		this.overlay = game.getOverlayEffect();
		this.borderGlow = new DropShadow();
		this.borderGlow.setOffsetY(0f);
		this.borderGlow.setOffsetX(0f);
		this.borderGlow.setSpread(0.3);
		this.borderGlow.setColor(Color.WHITE);
		this.borderGlow.setWidth(35);
		this.borderGlow.setHeight(35);
		this.borderGlow.setBlurType(BlurType.THREE_PASS_BOX);
		this.mainBoard = new ImageView(GameImageBank.pause_menu);
		this.continueBtt = new ImageView(GameImageBank.pause_continue);
		this.continueBtt.setFitWidth(this.continueBtt.getImage().getWidth());
		this.continueBtt.setFitHeight(this.continueBtt.getImage().getHeight());
		this.restartBtt = new ImageView(GameImageBank.pause_restart);
		this.restartBtt.setFitWidth(this.restartBtt.getImage().getWidth());
		this.restartBtt.setFitHeight(this.restartBtt.getImage().getHeight());
		this.mainMenuBtt = new ImageView(GameImageBank.pause_main);
		this.mainMenuBtt.setFitWidth(this.mainMenuBtt.getImage().getWidth());
		this.mainMenuBtt.setFitHeight(this.mainMenuBtt.getImage().getHeight());
		this.quitGameBtt = new ImageView(GameImageBank.pause_quit);
		this.quitGameBtt.setFitWidth(this.quitGameBtt.getImage().getWidth());
		this.quitGameBtt.setFitHeight(this.quitGameBtt.getImage().getHeight());
		this.mainBoard.setFitWidth((this.continueBtt.getImage().getWidth()+100));
		this.mainBoard.setFitHeight(((this.continueBtt.getFitHeight()*4)+90));
		this.x = GameSettings.WIDTH/2 - mainBoard.getFitWidth()/2;
		this.y = 0 - mainBoard.getImage().getHeight();
		this.continueBtt.setX(GameSettings.WIDTH/2-continueBtt.getImage().getWidth()/2);
		this.continueBtt.setY(mainBoard.getY()+40);
		this.restartBtt.setX(continueBtt.getX());
		this.restartBtt.setY(continueBtt.getY()+continueBtt.getFitHeight());
		this.mainMenuBtt.setX(continueBtt.getX()-mainMenuBtt.getFitWidth());
		this.mainMenuBtt.setY(continueBtt.getY()+mainMenuBtt.getFitHeight()/2+5);
		this.quitGameBtt.setX(continueBtt.getX()+continueBtt.getFitWidth());
		this.quitGameBtt.setY(continueBtt.getY()+mainMenuBtt.getFitHeight()/2+5);
		this.game.getFourTeenthLayer().getChildren().add(mainBoard);
		this.game.getFourTeenthLayer().getChildren().add(continueBtt);
		this.game.getFourTeenthLayer().getChildren().add(restartBtt);
		this.game.getFourTeenthLayer().getChildren().add(mainMenuBtt);
		this.game.getFourTeenthLayer().getChildren().add(quitGameBtt);
		if(!allowTouch){
			dimObjects();
		}
		processButtonGesture();
		processKeyHandling();
	}
	public void pauseGame(){
		selectionReset();
		showTouchPanel();

	}
	public void resumeGame(){
		selectionReset();
		hideTouchPanel();

	}
	public void goToMainMenu(){
		selectionReset();
		hideAndGoToMain();
	}
	public void dimObjects(){
		this.mainBoard.setOpacity(0);
		this.continueBtt.setOpacity(0);
		this.restartBtt.setOpacity(0);
		this.mainMenuBtt.setOpacity(0);
		this.quitGameBtt.setOpacity(0);
	}
	public void showObjects(double opacity){
		this.mainBoard.setOpacity(opacity);
		this.continueBtt.setOpacity(opacity);
		this.restartBtt.setOpacity(opacity);
		this.mainMenuBtt.setOpacity(opacity);
		this.quitGameBtt.setOpacity(opacity);
	}
	public void showTouchPanel(){
		if(!show){
			if(game.getModeID() == GameModeID.LocalMultiplayer){
				mainBoard.setImage(GameImageBank.pause_menu);
			}
			if(game.getModeID() == GameModeID.ClassicMode){
				mainBoard.setImage(GameImageBank.pause_menu_classic);
			}
			show = true;
			hide = false;
			game.setStateID(GameStateID.GAME_MENU);
			game.getScoreKeeper().stopTimer();
			game.getScoreKeeper().swipeDown();
			game.getGameHud().showHUDCover();
			game.showCursor(true, game.getScene());
			processKeyHandling();
			selectionReset();
			borderGlow.setColor(Color.rgb(0,240,0));
			continueBtt.setEffect(borderGlow);
			currentChoice = 1;
		}
	}
	public void hideTouchPanel(){
		if(!hide)
			blurOff();
			game.getScoreKeeper().swipeUp();
			game.getGameHud().hideHUDCover();
			game.showCursor(false, game.getScene());
			hide = true;
			show = false;
	}
	public void hideAndReset(){
		if(!hide)
			blurOff();
			game.showCursor(false, game.getScene());
			hide = true;
			show = false;
	}
	public void hideAndGoToMain(){
		if(!hide)
			blurOff();
//			game.getScoreKeeper().swipeUp();
//			game.getGameHud().hideHUDCover();
			game.showCursor(false, game.getScene());
			hide = false;
			goToMain = true;
			show = false;
	}
	public void updateTouchPanel(){
		if(allowTouch){
			if(show){
				showSpeedY+=acceleration;
				y = y + showSpeedY;
				x = x + showSpeedX;
				mainBoard.setX(x);
				mainBoard.setY(y);
				if (mainBoard.getY()>=GameSettings.HEIGHT/2-mainBoard.getFitHeight()/2){
					blurOut();
					show=false;
				}
			}
			if(hide){
				hideSpeedY-=acceleration;
				y = y + hideSpeedY;
				x = x + hideSpeedX;
				mainBoard.setX(x);
				mainBoard.setY(y);
				if(mainBoard.getY()<0-mainBoard.getImage().getHeight()){
					//game.setStateID(GameStateID.GAMEPLAY);
					game.getScoreKeeper().startTimer();
					hide = false;
				}
			}
		}
		else{
			if(show){
				fadeLevel+=0.03;
				showObjects(fadeLevel);
				if(fadeLevel>=1.0){
					blurOut();
					fadeLevel = 1.0;
					show=false;
				}
			}
			if(hide){
				fadeLevel-=0.03;
				showObjects(fadeLevel);
				if(fadeLevel<=0){
					if(CountDownScreen.COUNTDOWN_OVER){
						game.getScoreKeeper().startTimer();
						game.setStateID(GameStateID.GAMEPLAY);
					}
					game.processGameInput();
					fadeLevel = 0;
					hide = false;

				}
			}
			if(goToMain){
				fadeLevel-=0.03;
				showObjects(fadeLevel);
				if(fadeLevel<=0){
					fadeLevel = 0;
					goToMain = false;

				}
			}
			this.mainBoard.setY(GameSettings.HEIGHT/2-mainBoard.getFitHeight()/2);
			this.mainBoard.setX(x);

		}
		this.continueBtt.setX(GameSettings.WIDTH/2-this.continueBtt.getFitWidth()/2);
		this.continueBtt.setY(mainBoard.getY()+40);
		this.restartBtt.setX(continueBtt.getX());
		this.restartBtt.setY(continueBtt.getY()+continueBtt.getFitHeight());
		this.mainMenuBtt.setX(continueBtt.getX());
		this.mainMenuBtt.setY(continueBtt.getY()+mainMenuBtt.getFitHeight()*2);
		this.quitGameBtt.setX(continueBtt.getX());
		this.quitGameBtt.setY(continueBtt.getY()+mainMenuBtt.getFitHeight()*3);
	}
	public void processTouch(){
		game.getFourTeenthLayer().setOnSwipeUp(new EventHandler<SwipeEvent>() {

			public void handle(SwipeEvent event) {
				if(game.getStateID()==GameStateID.GAME_MENU){
				hideTouchPanel();
				event.consume();
				}
			}
		});
		game.getFourTeenthLayer().setOnSwipeDown(new EventHandler<SwipeEvent>() {

			public void handle(SwipeEvent event) {
				if(game.getStateID()==GameStateID.GAMEPLAY){
				showTouchPanel();
				event.consume();
				}
			}
		});
		processButtonGesture();
	}
	public void processButtonGesture(){
		continueBtt.setOnMouseEntered(e -> {
			borderGlow.setColor(Color.rgb(0,240,0));
			continueBtt.setEffect(borderGlow);
			restartBtt.setEffect(null);
			quitGameBtt.setEffect(null);
			mainMenuBtt.setEffect(null);
			currentChoice = 1;
		});
		continueBtt.setOnMousePressed(e -> {
			if(fadeLevel>=1)
			resumeGame();
		});
		restartBtt.setOnMouseEntered(e -> {
			borderGlow.setColor(Color.rgb(255,150,0));
			restartBtt.setEffect(borderGlow);
			continueBtt.setEffect(null);
			quitGameBtt.setEffect(null);
			mainMenuBtt.setEffect(null);
			currentChoice = 2;
		});
		restartBtt.setOnMousePressed(e -> {
			if(fadeLevel>=1){
			game.setStateID(GameStateID.LEVEL_RESTART);
			restartLevel();
			}
		});
		mainMenuBtt.setOnMouseEntered(e -> {
			borderGlow.setColor(Color.rgb(255,80,0));
			mainMenuBtt.setEffect(borderGlow);
			continueBtt.setEffect(null);
			quitGameBtt.setEffect(null);
			restartBtt.setEffect(null);
			currentChoice = 3;
		});
		mainMenuBtt.setOnMousePressed(e -> {
			if(fadeLevel>=1){
			game.setStateID(GameStateID.MAIN_MENU);
			game.getFadeScreenHandler().menu_fade_screen();
			goToMainMenu();
			}
		});
		quitGameBtt.setOnMouseEntered(e -> {
			borderGlow.setColor(Color.rgb(255,0,0));
			quitGameBtt.setEffect(borderGlow);
			continueBtt.setEffect(null);
			restartBtt.setEffect(null);
			mainMenuBtt.setEffect(null);
			currentChoice = 4;
		});
		quitGameBtt.setOnMousePressed(e -> {
			if(fadeLevel>=1){
			game.closeGame();
			}
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
			case UP:
				currentChoice -= 1;
				if (currentChoice < 1) {
					currentChoice = 4;
				}
				break;
			case DOWN:
				currentChoice += 1;
				if (currentChoice > 4) {
					currentChoice = 1;
				}
				break;
			case W:
				currentChoice -= 1;
				if (currentChoice < 1) {
					currentChoice = 4;
				}
				break;
			case S:
				currentChoice += 1;
				if (currentChoice > 4) {
					currentChoice = 1;
				}
				break;
			case ENTER:
				if (currentChoice == 1) {
					if(fadeLevel>=1)
					resumeGame();
				}
				if (currentChoice == 2) {
					if(fadeLevel>=1){
					game.setStateID(GameStateID.LEVEL_RESTART);
					restartLevel();
					}
				}
				if (currentChoice == 3) {
					if(fadeLevel>=1){
					game.setStateID(GameStateID.MAIN_MENU);
					game.getFadeScreenHandler().menu_fade_screen();
					goToMainMenu();
					}
				}
				if (currentChoice == 4) {
					if(fadeLevel>=1)
					game.closeGame();
				}
				break;
			case SPACE:
				if (currentChoice == 1) {
					if(fadeLevel>=1)
					resumeGame();
				}
				if (currentChoice == 2) {
					if(fadeLevel>=1){
					game.setStateID(GameStateID.LEVEL_RESTART);
					restartLevel();
					}
				}
				if (currentChoice == 3) {
					if(fadeLevel>=1){
					game.setStateID(GameStateID.MAIN_MENU);
					game.getFadeScreenHandler().menu_fade_screen();
					goToMainMenu();
					}
				}
				if (currentChoice == 4) {
					if(fadeLevel>=1)
					game.closeGame();
				}
				break;
			case ESCAPE:
				hideTouchPanel();
				game.processGameInput();
				continueBtt.setEffect(null);
				restartBtt.setEffect(null);
				quitGameBtt.setEffect(null);
				mainMenuBtt.setEffect(null);
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
			continueBtt.setEffect(borderGlow);
			quitGameBtt.setEffect(null);
			restartBtt.setEffect(null);
			mainMenuBtt.setEffect(null);
		}
		if(currentChoice==2){
			borderGlow.setColor(Color.rgb(255,150,0));
			restartBtt.setEffect(borderGlow);
			continueBtt.setEffect(null);
			quitGameBtt.setEffect(null);
			mainMenuBtt.setEffect(null);
		}
		if(currentChoice==3){
			borderGlow.setColor(Color.rgb(255,80,0));
			mainMenuBtt.setEffect(borderGlow);
			continueBtt.setEffect(null);
			restartBtt.setEffect(null);
			quitGameBtt.setEffect(null);
		}
		if(currentChoice==4){
			borderGlow.setColor(Color.rgb(255,0,0));
			quitGameBtt.setEffect(borderGlow);
			continueBtt.setEffect(null);
			restartBtt.setEffect(null);
			mainMenuBtt.setEffect(null);
		}
	}
	public void selectionReset(){
		quitGameBtt.setEffect(null);
		restartBtt.setEffect(null);
		mainMenuBtt.setEffect(null);
	}
	public void restartLevel() {
		game.removePlayers();
		game.restart();
		PlayerOne.LEVEL_COMPLETED = false;
		PlayerTwo.LEVEL_COMPLETED = false;
		selectionReset();
		hideAndReset();
		game.getScoreKeeper().resetTimer();
		game.getCountDownScreen().startCountdown();
	}
	public synchronized void blurOut(){
		game.getOuterParticleLayer().getChildren().clear();
		game.getDebrisManager().clearParticles();
		overlay.levelCompleteBlur();
	}
	public synchronized void blurOff(){
		overlay.levelCompleteBlurOff();
	}
	public boolean isAllowTouch() {
		return allowTouch;
	}

	public void setAllowTouch(boolean allowTouch) {
		this.allowTouch = allowTouch;
	}

	public boolean isHide() {
		return hide;
	}

	public void setHide(boolean hide) {
		this.hide = hide;
	}

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public double getHideSpeedY() {
		return hideSpeedY;
	}

	public void setHideSpeedY(double hideSpeedY) {
		this.hideSpeedY = hideSpeedY;
	}

	public double getShowSpeedY() {
		return showSpeedY;
	}

	public void setShowSpeedY(double showSpeedY) {
		this.showSpeedY = showSpeedY;
	}

	public double getAccelaration() {
		return acceleration;
	}

	public void setAccelaration(double accelaration) {
		this.acceleration = accelaration;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	public ImageView getMainBoard() {
		return mainBoard;
	}
	public void setMainBoard(ImageView mainBoard) {
		this.mainBoard = mainBoard;
	}
	public ImageView getLeftTouchPanel() {
		return leftTouchPanel;
	}
	public void setLeftTouchPanel(ImageView leftTouchPanel) {
		this.leftTouchPanel = leftTouchPanel;
	}
	public ImageView getRightTouchPanel() {
		return rightTouchPanel;
	}
	public void setRightTouchPanel(ImageView rightTouchPanel) {
		this.rightTouchPanel = rightTouchPanel;
	}
	public ImageView getCenterTouchPanel() {
		return centerTouchPanel;
	}
	public void setCenterTouchPanel(ImageView centerTouchPanel) {
		this.centerTouchPanel = centerTouchPanel;
	}
	public ImageView getGoUp() {
		return continueBtt;
	}
	public void setGoUp(ImageView goUp) {
		this.continueBtt = goUp;
	}
	public ImageView getGoDown() {
		return restartBtt;
	}
	public void setGoDown(ImageView goDown) {
		this.restartBtt = goDown;
	}
	public ImageView getGoLeft() {
		return mainMenuBtt;
	}
	public void setGoLeft(ImageView goLeft) {
		this.mainMenuBtt = goLeft;
	}
	public ImageView getGoRight() {
		return quitGameBtt;
	}
	public void setGoRight(ImageView goRight) {
		this.quitGameBtt = goRight;
	}

}
