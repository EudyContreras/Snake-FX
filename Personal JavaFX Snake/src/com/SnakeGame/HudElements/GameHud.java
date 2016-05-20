package com.SnakeGame.HudElements;

import com.SnakeGame.FrameWork.SnakeGame;
import com.SnakeGame.ImageBanks.GameImageBank;
import com.SnakeGame.PlayerOne.PlayerOne;
import com.SnakeGame.PlayerTwo.PlayerTwo;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

/**
 * This class is used to simulate a simple health bar which will decrease under
 * certain conditions, and that will also self restore
 *
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
	PlayerOne player;
	GraphicsContext gc;
	Rectangle hudBarCover = new Rectangle();
	Rectangle hudBar = new Rectangle();

	public GameHud(SnakeGame game, double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.limit = width;
		this.width = width;
		this.height = height+5;
		this.player = game.getloader().getPlayerOne();
		this.game = game;
		this.hudBarCover.setWidth(width);
		this.hudBarCover.setHeight(height+20);
		this.hudBarCover.setTranslateX(x);
		this.hudBarCover.setTranslateY(y);
		this.hudBarCover.setArcHeight(20);
		this.hudBarCover.setArcWidth(20);
		this.hudBar.setWidth(width);
		this.hudBar.setHeight(height + 5);
		this.hudBar.setTranslateX(x);
		this.hudBar.setTranslateY(y);
		this.hudBar.setArcWidth(20);
		this.hudBar.setArcHeight(20);
		this.hudBar.setFill(new ImagePattern(GameImageBank.hudBar));
		this.hudBarCover.setFill(new ImagePattern(GameImageBank.hudBar));
		game.getOverlay().getChildren().add(hudBar);
		game.getFadeScreen().getChildren().add(hudBarCover);
		this.maxHealth = width;
	}

	public void update() {
		y = y + swipeSpeed;
		if (swipeDown) {
			swipeSpeed = 1.5;
			if (y >= hudBar.getTranslateY()) {
				swipeSpeed = 0;
			}
		}
		if (swipeUp) {
			swipeSpeed = -1.5;
			if (y < hudBar.getTranslateY() - height+10) {
				swipeSpeed = 0;
			}
		}
		hudBarCover.setTranslateY(y);
	}

	public void showHide() {
		if (swipeDown) {
			swipeDown = false;
			swipeUp = true;
		} else if (swipeUp) {
			swipeUp = false;
			swipeDown = true;
		}
	}

	public void swipeDown() {
		swipeUp = false;
		swipeDown = true;
	}

	public void swipeUp() {
		swipeDown = false;
		swipeUp = true;
	}

	public void hide() {
		if (PlayerOne.LEVEL_COMPLETED || PlayerTwo.LEVEL_COMPLETED) {
			hudBarCover.setVisible(false);
			hudBar.setVisible(false);
		}
	}

	public void show() {
		game.getFadeScreen().getChildren().add(hudBarCover);
		hudBarCover.setVisible(true);
		hudBar.setVisible(true);
	}

	public void setPlayer() {
		this.player = null;
		this.player = game.getloader().getPlayerOne();
	}
	public double getHudbarY(){
		return hudBarCover.getY();
	}
}
