package com.SnakeGame.Core;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
/**
 * This class is used to simulate a simple health bar
 * which will decrease under certain conditions, and that will
 * also self restore
 * @author Eudy Contreras
 *
 */
public class GameHud {

	boolean swipeUp = true;
	boolean swipeDown = false;
	boolean killPlayer = false;
	boolean playerIsAlive = true;
	double maxHealth = 100;
	double x = 0;
	double y = 0;
	double width = 0;
	double height = 0;
	double swipeSpeed = 0;
	double limit;
	SnakeGame game;
	Player player;
	GraphicsContext gc;
	Rectangle hudBar = new Rectangle();
	Rectangle hudBarBorder = new Rectangle();


	public GameHud(SnakeGame game,double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.limit = width;
		this.width = width;
		this.height = height;
		this.player = game.getloader().getPlayer();
		this.game = game;
		this.hudBar.setWidth(width);
		this.hudBar.setHeight(height);
		this.hudBar.setTranslateX(x);
		this.hudBar.setTranslateY(y);
		this.hudBar.setArcHeight(20);
		this.hudBar.setArcWidth(20);
		this.hudBarBorder.setWidth(width);
		this.hudBarBorder.setHeight(height+5);
		this.hudBarBorder.setTranslateX(x);
		this.hudBarBorder.setTranslateY(y);
		this.hudBar.setFill(new ImagePattern(GameImageBank.hudBar));
		this.hudBarBorder.setArcWidth(20);
		this.hudBarBorder.setArcHeight(20);
		this.hudBarBorder.setFill(new ImagePattern(GameImageBank.hudBar));
		game.getOverlay().getChildren().add(hudBarBorder);
		game.getFadeScreen().getChildren().add(hudBar);
		this.maxHealth = width;
	}
	public void update(){
		y = y+swipeSpeed;
		if(swipeDown){
			swipeSpeed = 1.5;
			if(y>=0){
				swipeSpeed = 0;
				//swipeDown = false;
			}
		}
		if(swipeUp){
			swipeSpeed = -1.5;
			if(y<0-height){
				swipeSpeed = 0;
				//swipeUp = false;
			}
		}
		hudBar.setTranslateY(y);
	}
	public void showHide(){
		if(swipeDown){
			swipeDown = false;
			swipeUp = true;
		}
		else if(swipeUp){
			swipeUp = false;
			swipeDown = true;
		}
	}
	public void swipeDown(){
		swipeUp = false;
		swipeDown = true;
	}
	public void swipeUp(){
		swipeDown = false;
		swipeUp = true;
	}
	public void hide(){
		if(Player.levelComplete || Player2.levelComplete){
			hudBar.setVisible(false);
			hudBarBorder.setVisible(false);
		}
	}
	public void show(){
		game.getFadeScreen().getChildren().add(hudBar);
		hudBar.setVisible(true);
		hudBarBorder.setVisible(true);
	}
	public void setPlayer(){
		this.player = null;
		this.player = game.getloader().getPlayer();
	}

}
