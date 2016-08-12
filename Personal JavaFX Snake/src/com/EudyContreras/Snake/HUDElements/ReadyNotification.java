package com.EudyContreras.Snake.HUDElements;

import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Application.GameSettings;
import com.EudyContreras.Snake.FrameWork.ResizeHelper;
import com.EudyContreras.Snake.ImageBanks.GameImageBank;

import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * This class aims to represent a game ready notification which determines
 * whether or not the player is ready to start the game. If the space bar or
 * enter is pressed the count down will begin and the player will be able to start
 *
 * @author Eudy Contreras
 *
 */
public class ReadyNotification {
	private int wait;
	private double x;
	private double y;
	private double width;
	private double height;
	private double fadeValue = 0;;
	private double fadeSpeed = 0.035;
	private double showCounter = 0;
	private Pane layer;
	private Boolean hideNotice = false;
	private Boolean waiting = false;
	private Boolean fadeState = false;
	private ImageView readyView;
	private DropShadow glow;
	private GameManager game;

	/**
	 * Constructor which takes the main game class along with the width and the
	 * height of each of each element to be show. The parameter also takes a
	 * layer to which the elements will be added and displayed upon.
	 *
	 * @param game:Main game class which communicates with almost all the other classes
	 * @param width:Horizontal dimension of the UI element
	 * @param height:Vertical dimension of the UI element
	 * @param layer:Layer to which the UI element will be added
	 */
	public ReadyNotification(GameManager game, double width, double height, Pane layer) {
		this.game = game;
		this.width = width;
		this.height = height;
		this.layer = layer;
		this.initialize();
		this.position();
		this.display();
	}
	private void initialize(){
		this.glow = new DropShadow();
		this.glow.setColor(Color.LIMEGREEN);
		this.glow.setRadius(45);
		this.glow.setSpread(.6);
		this.readyView = new ImageView(GameImageBank.ready_notification);
		this.readyView.setPreserveRatio(true);
		this.readyView.setFitWidth(width*.5);
		this.readyView.setFitHeight(height*.5);
		this.readyView.setOpacity(fadeValue);
	}
	private void position(){
		this.x = GameSettings.WIDTH/2 - readyView.getFitWidth()/2;
		this.y = 20;
		this.readyView.setTranslateX(x);
		this.readyView.setTranslateY(y);
	}
	private void display(){
		this.layer.getChildren().remove(readyView);
		this.layer.getChildren().add(readyView);
	}
	public void showNotification(int wait) {
		this.wait = wait;
		showCounter = 0;
		display();
		waiting = true;
		hideNotice = false;
		processEvent();
	}
	public void hideNotification(){
		readyView.setEffect(glow);
		fadeValue = 1;
		waiting = false;
		hideNotice = true;
	}
	public void fadeOutNotice(){
		if(hideNotice){
			fadeValue -= 0.05;
			if (fadeValue <= 0) {
				fadeValue = 0;
				hideNotice = false;
				readyView.setEffect(null);
				startCounter();
			}
		}
		this.readyView.setOpacity(fadeValue);
	}
	private void startCounter() {
		layer.getChildren().remove(readyView);
		game. processGameInput();
		game.getCountDownScreen().startCountdown(0);
	}
	private void fadeAnimation(){
		showCounter++;
		if (showCounter >= wait) {
			showCounter = wait;
			if (!fadeState) {
				fadeValue += fadeSpeed;
				if (fadeValue >= 1.5) {
					fadeValue = 1.5;
					fadeState = true;
				}
			}
			if (fadeState) {
				fadeValue -= fadeSpeed;
				if (fadeValue <= 0) {
					fadeValue = 0;
					fadeState = false;
				}
			}
		}
		if(fadeValue<=1.0)
		this.readyView.setOpacity(fadeValue);
	}

	public void updateUI(){
		if(waiting)
			fadeAnimation();
		if(!waiting)
			fadeOutNotice();
	}
	public void setVisibility(boolean state){
		this.readyView.setVisible(state);
		this.waiting = state;
	}
	private void processEvent(){
		game.getScene().setOnKeyPressed(e -> {
			switch(e.getCode()){
			case ENTER:
				if(!e.isControlDown()){
					hideNotification();

				}
				else{
					if(!game.getMainWindow().isFullScreen() && ResizeHelper.isSupportedRatio()){
						game.setNewRatio(true);
						game.getMainWindow().setFullScreen(true);
						game.getGameBorder().showBorders(false);
					}
					else{
						game.setNewRatio(false);
						game.getMainWindow().setFullScreen(false);
						game.getGameBorder().showBorders(true);
					}
				}

				break;
			case SPACE:
				hideNotification();
				break;
			default:
				break;

			}
		});

	}



}
