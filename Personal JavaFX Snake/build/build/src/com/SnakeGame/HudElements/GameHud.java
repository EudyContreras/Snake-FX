package com.SnakeGame.HudElements;

import com.SnakeGame.FrameWork.Settings;
import com.SnakeGame.FrameWork.SnakeGame;
import com.SnakeGame.ImageBanks.GameImageBank;
import com.SnakeGame.PlayerOne.PlayerOne;
import com.SnakeGame.PlayerTwo.PlayerTwo;

import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

/**
 * This class simulates a simple game heads up display which is shown at the to the level
 * this display shows useful information concerning game statistics
 * @author Eudy Contreras
 *
 */
public class GameHud {

	private boolean swipeUp = true;
	private boolean swipeDown = false;
	private double x = 0;
	private double y = 0;
	private double width = 0;
	private double height = 0;
	private double swipeSpeed = 0;
	private double y2 = Settings.HEIGHT;
	private double swipeSpeed2;
	private Rectangle topHudBar = new Rectangle();
	private Rectangle bottomHudBar = new Rectangle();
	private Rectangle hudBar = new Rectangle();


	public GameHud(SnakeGame game, double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height+5;
		this.y2 = Settings.HEIGHT-height;
		this.topHudBar.setWidth(width);
		this.topHudBar.setHeight(height+40);
		this.topHudBar.setTranslateX(x);
		this.topHudBar.setTranslateY(y-20);
		this.topHudBar.setArcHeight(20);
		this.topHudBar.setArcWidth(20);
		this.bottomHudBar.setWidth(width);
		this.bottomHudBar.setHeight(height+40);
		this.bottomHudBar.setTranslateX(x);
		this.bottomHudBar.setTranslateY(y2);
		this.bottomHudBar.setRotate(180);
		this.bottomHudBar.setArcHeight(20);
		this.bottomHudBar.setArcWidth(20);
		this.hudBar.setWidth(width);
		this.hudBar.setHeight(height + 5);
		this.hudBar.setTranslateX(x);
		this.hudBar.setTranslateY(y);
		this.hudBar.setArcWidth(20);
		this.hudBar.setArcHeight(20);
		this.hudBar.setFill(new ImagePattern(GameImageBank.hud_bar));
		this.topHudBar.setFill(new ImagePattern(GameImageBank.hud_bar_cover));
		this.bottomHudBar.setFill(new ImagePattern(GameImageBank.hud_bar_cover));
		game.getTenthLayer().getChildren().add(hudBar);
		game.getFourTeenthLayer().getChildren().add(topHudBar);
		game.getFourTeenthLayer().getChildren().add(bottomHudBar);
	}
	/**
	 * Method which updates the movement of the hudbar
	 */
	public void updateHudBars() {
		y = y + swipeSpeed;
		if (swipeDown) {
			swipeSpeed = 1.5;
			if (y >= hudBar.getTranslateY()) {
				swipeSpeed = 0;
			}
		}
		if (swipeUp) {
			swipeSpeed = -1.5;
			if (y < hudBar.getTranslateY() - height-10) {
				swipeSpeed = 0;
			}
		}
		topHudBar.setTranslateY(y);

		y2 = y2 + swipeSpeed2;
		if (swipeDown) {
			swipeSpeed2 = -1.5;
			if (y2 < Settings.HEIGHT-bottomHudBar.getHeight()+SnakeGame.ScaleY(15)) {
				swipeSpeed2 = 0;
			}
		}
		if (swipeUp) {
			swipeSpeed2 = 1.5;
			if (y2 > Settings.HEIGHT) {
				swipeSpeed2 = 0;
			}
		}
		bottomHudBar.setTranslateY(y2);
	}
	/**
	 * Method which shows or hides the hud bar
	 * depending on the current status of the hud
	 */
	public void showHide() {
		if (swipeDown) {
			swipeDown = false;
			swipeUp = true;
		} else if (swipeUp) {
			swipeUp = false;
			swipeDown = true;
		}
	}
	/**
	 * Method which swipes down the hud bar
	 */
	public void swipeDown() {
		swipeUp = false;
		swipeDown = true;
	}
	/**
	 * Method which swipes up the hud bar
	 */
	public void swipeUp() {
		swipeDown = false;
		swipeUp = true;
	}
	/**
	 * Method which hides the hud bar
	 */
	public void hide() {
		if (PlayerOne.LEVEL_COMPLETED || PlayerTwo.LEVEL_COMPLETED) {
			topHudBar.setVisible(false);
			bottomHudBar.setVisible(false);
			hudBar.setVisible(false);
		}
	}
	/**
	 * Method which shows a hud bar
	 */
	public void show() {
//		game.getNinthLayer().getChildren().add(topHudBar);
//		game.getNinthLayer().getChildren().add(bottomHudBar);
		topHudBar.setVisible(true);
		bottomHudBar.setVisible(true);
		hudBar.setVisible(true);
	}
	public double getHudbarY(){
		return topHudBar.getY();
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
}
