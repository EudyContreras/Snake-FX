package com.EudyContreras.Snake.HUDElements;

import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.FrameWork.GameSettings;
import com.EudyContreras.Snake.ImageBanks.GameImageBank;
import com.EudyContreras.Snake.PlayerOne.PlayerOne;
import com.EudyContreras.Snake.PlayerTwo.PlayerTwo;

import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
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
	private double swipeSpeedTop = 0;
	private double y2 = GameSettings.HEIGHT;
	private double swipeSpeedBottom = 0;
	private DropShadow shadow = new DropShadow();
	private Rectangle topHudBar = new Rectangle();
	private Rectangle bottomHudBar = new Rectangle();
	private Rectangle hudBar = new Rectangle();


	public GameHud(GameManager game, double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height+10;
		this.y2 = GameSettings.HEIGHT-height;
		this.shadow.setColor(Color.rgb(0, 0, 0, 0.5));
		this.shadow.setRadius(5);
		this.shadow.setOffsetX(0);
		this.shadow.setOffsetY(15);
		this.topHudBar.setWidth(width);
		this.topHudBar.setHeight(height+40);
		this.topHudBar.setTranslateX(x);
		this.topHudBar.setTranslateY(y-20);
		this.topHudBar.setArcHeight(20);
		this.topHudBar.setArcWidth(20);
		this.bottomHudBar.setWidth(width);
		this.bottomHudBar.setHeight(height+40);
		this.bottomHudBar.setTranslateX(x);
		this.bottomHudBar.setTranslateY(y2+20);
		this.bottomHudBar.setRotate(180);
		this.bottomHudBar.setArcHeight(20);
		this.bottomHudBar.setArcWidth(20);
		this.hudBar.setWidth(width);
		this.hudBar.setHeight(height + 5);
		this.hudBar.setTranslateX(x);
		this.hudBar.setTranslateY(y);
		this.hudBar.setArcWidth(20);
		this.hudBar.setArcHeight(20);
		this.hudBar.setEffect(shadow);
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
		y = y + swipeSpeedTop/GameManager.ScaleY;
		y2 = y2 + swipeSpeedBottom/GameManager.ScaleY;
		if (swipeDown) {
			swipeSpeedTop = 2.5;
			if (y >= hudBar.getTranslateY() - GameManager.ScaleY(21)) {
				swipeSpeedTop = 0;
			}
			swipeSpeedBottom = -2.5;
			if (y2 < GameSettings.HEIGHT-bottomHudBar.getHeight()+GameManager.ScaleY(40)) {
				swipeSpeedBottom = 0;
			}
		}
		if (swipeUp) {
			swipeSpeedTop = -2.55;
			if (y < hudBar.getTranslateY() - height) {
				swipeSpeedTop = 0;
			}
			swipeSpeedBottom = 2.55;
			if (y2 > GameSettings.HEIGHT) {
				swipeSpeedBottom = 0;
			}
		}
		topHudBar.setTranslateY(y);
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
