package com.SnakeGame.HudElements;

import com.SnakeGame.FrameWork.GameSettings;
import com.SnakeGame.FrameWork.GameManager;
import com.SnakeGame.IDenums.GameStateID;
import com.SnakeGame.ImageBanks.GameImageBank;
import com.SnakeGame.PlayerOne.PlayerOne;
import com.SnakeGame.PlayerTwo.PlayerTwo;
import com.SnakeGame.Utilities.ScreenOverlay;

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
	private ScreenOverlay overlay;
	private boolean allowTouch = false;
	private boolean hide = true;
	private boolean show = false;
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
		this.overlay = new ScreenOverlay(game, game.getGameRoot());
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
		this.restartBtt = new ImageView(GameImageBank.pause_restart);
		this.mainMenuBtt = new ImageView(GameImageBank.pause_main);
		this.quitGameBtt = new ImageView(GameImageBank.pause_quit);
		this.mainBoard.setFitWidth(continueBtt.getImage().getWidth()+GameManager.ScaleX(100));
		this.mainBoard.setFitHeight(mainBoard.getImage().getHeight()-140);
		this.x = GameSettings.WIDTH/2 - mainBoard.getFitWidth()/2;
		this.y = 0 - mainBoard.getImage().getHeight();
		this.continueBtt.setX(GameSettings.WIDTH/2-continueBtt.getImage().getWidth()/2);
		this.continueBtt.setY(mainBoard.getY()+GameManager.ScaleX(40));
		this.restartBtt.setX(continueBtt.getX());
		this.restartBtt.setY(continueBtt.getY()+continueBtt.getImage().getHeight());
		this.mainMenuBtt.setX(continueBtt.getX()-mainMenuBtt.getImage().getWidth());
		this.mainMenuBtt.setY(continueBtt.getY()+mainMenuBtt.getImage().getHeight()/2+5);
		this.quitGameBtt.setX(continueBtt.getX()+continueBtt.getImage().getWidth());
		this.quitGameBtt.setY(continueBtt.getY()+mainMenuBtt.getImage().getHeight()/2+5);
		this.game.getFourTeenthLayer().getChildren().add(mainBoard);
		this.game.getFourTeenthLayer().getChildren().add(continueBtt);
		this.game.getFourTeenthLayer().getChildren().add(restartBtt);
		this.game.getFourTeenthLayer().getChildren().add(mainMenuBtt);
		this.game.getFourTeenthLayer().getChildren().add(quitGameBtt);
		if(!allowTouch){
			dimObjects();
		}
		processButtonGesture();
	}
	public void pauseGame(){
		showTouchPanel();

	}
	public void resumeGame(){
		hideTouchPanel();

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
			show = true;
			hide = false;
			game.setStateID(GameStateID.GAME_MENU);
			game.getScoreKeeper().stopTimer();
			game.getScoreKeeper().swipeDown();
			game.getGameHud().swipeDown();
			game.showCursor(true, game.getScene());
//			if(!allowTouch){
//				blurOut();
//			}
		}
	}
	public void hideTouchPanel(){
		if(!hide)
			blurOff();
			game.getScoreKeeper().swipeUp();
			game.getGameHud().swipeUp();
			game.showCursor(false, game.getScene());
			hide = true;
			show = false;
	}
	public void updateTouchPanel(){
		overlay.updateEffect();
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
					game.setStateID(GameStateID.GAMEPLAY);
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
					game.getScoreKeeper().startTimer();
					game.setStateID(GameStateID.GAMEPLAY);
					fadeLevel = 0;
					hide = false;

				}
			}
			this.mainBoard.setY(GameSettings.HEIGHT/2-mainBoard.getFitHeight()/2);
			this.mainBoard.setX(x);

		}
		this.continueBtt.setY(mainBoard.getY()+GameManager.ScaleX(40));
		this.restartBtt.setX(continueBtt.getX());
		this.restartBtt.setY(continueBtt.getY()+continueBtt.getImage().getHeight());
		this.mainMenuBtt.setX(continueBtt.getX());
		this.mainMenuBtt.setY(continueBtt.getY()+mainMenuBtt.getImage().getHeight()*2);
		this.quitGameBtt.setX(continueBtt.getX());
		this.quitGameBtt.setY(continueBtt.getY()+mainMenuBtt.getImage().getHeight()*3);
	}
	public void processTouch(){
		game.getScene().setOnSwipeUp(new EventHandler<SwipeEvent>() {

			public void handle(SwipeEvent event) {
				hideTouchPanel();
				event.consume();
			}
		});
		game.getScene().setOnSwipeDown(new EventHandler<SwipeEvent>() {

			public void handle(SwipeEvent event) {
				showTouchPanel();
				event.consume();
			}
		});
		processButtonGesture();
	}
	public void processButtonGesture(){
		continueBtt.setOnMouseEntered(e -> {
			borderGlow.setColor(Color.rgb(0,240,0));
			continueBtt.setEffect(borderGlow);
		});
		continueBtt.setOnMouseExited(e -> {
			continueBtt.setEffect(null);
		});
		continueBtt.setOnMouseClicked(e -> {
			resumeGame();
		});
		restartBtt.setOnMouseEntered(e -> {
			borderGlow.setColor(Color.rgb(255,150,0));
			restartBtt.setEffect(borderGlow);
		});
		restartBtt.setOnMouseExited(e -> {
			restartBtt.setEffect(null);
		});
		restartBtt.setOnMouseClicked(e -> {
			game.setStateID(GameStateID.LEVEL_RESTART);
			restartLevel();
		});
		mainMenuBtt.setOnMouseEntered(e -> {
			borderGlow.setColor(Color.rgb(255,80,0));
			mainMenuBtt.setEffect(borderGlow);
		});
		mainMenuBtt.setOnMouseExited(e -> {
			mainMenuBtt.setEffect(null);
		});
		mainMenuBtt.setOnMouseClicked(e -> {
			game.setStateID(GameStateID.MAIN_MENU);
			game.getFadeScreenHandler().menu_fade_screen();
			resumeGame();
		});
		quitGameBtt.setOnMouseEntered(e -> {
			borderGlow.setColor(Color.rgb(255,0,0));
			quitGameBtt.setEffect(borderGlow);
		});
		quitGameBtt.setOnMouseExited(e -> {
			quitGameBtt.setEffect(null);
		});
		quitGameBtt.setOnMouseClicked(e -> {
			game.closeGame();
		});
	}
	public void restartLevel() {
		game.removePlayers();
		game.restart();
		PlayerOne.LEVEL_COMPLETED = false;
		PlayerTwo.LEVEL_COMPLETED = false;
		resumeGame();
	}
	public synchronized void blurOut(){
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
