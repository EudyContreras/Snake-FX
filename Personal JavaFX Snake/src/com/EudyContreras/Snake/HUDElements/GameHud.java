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

	private double yOne = 0;
	private double swipeSpeedTop = 0;
	private double yTwo = GameSettings.HEIGHT;
	private double swipeSpeedBottom = 0;
	private boolean hideHUDCover = false;
	private boolean showHUDCover = false;
	private DropShadow shadow = new DropShadow();
	private Rectangle topHudBar = new Rectangle();
	private Rectangle bottomHudBar = new Rectangle();
	private Rectangle mainBar = new Rectangle();
	/**
	 * Constructor which takes the main class as a parameter along with the coordintate
	 * and dimensions of Hud element.
	 * @param game: Main game class which connects this class to all others
	 * @param x: X coordinate of this element
	 * @param y: Y coordinate of this element
	 * @param width: Horizontal dimension of this element
	 * @param height: Vertical dimension of this element
	 */

	public GameHud(GameManager game, double x, double y, double width, double height) {
		this.yOne = y;
		this.shadow.setColor(Color.rgb(0, 0, 0, 0.5));
		this.shadow.setRadius(5);
		this.shadow.setOffsetX(0);
		this.shadow.setOffsetY(15);
		this.topHudBar.setWidth(width);
		this.topHudBar.setHeight(height+GameManager.ScaleY(50));
		this.topHudBar.setTranslateX(x);
		this.topHudBar.setTranslateY(y-GameManager.ScaleY(20));
//		this.topHudBar.setArcHeight(20);
//		this.topHudBar.setArcWidth(20);
		this.bottomHudBar.setWidth(width);
		this.bottomHudBar.setHeight(height+GameManager.ScaleY(80));
		this.yTwo = GameSettings.HEIGHT-bottomHudBar.getHeight()+GameManager.ScaleY(40);
		this.bottomHudBar.setTranslateX(x);
		this.bottomHudBar.setTranslateY(yTwo);
		this.bottomHudBar.setRotate(180);
//		this.bottomHudBar.setArcHeight(20);
//		this.bottomHudBar.setArcWidth(20);
		this.mainBar.setWidth(width);
		this.mainBar.setHeight(height + +GameManager.ScaleY(20));
		this.mainBar.setTranslateX(x);
		this.mainBar.setTranslateY(y);
//		this.mainBar.setArcWidth(20);
//		this.mainBar.setArcHeight(20);
		this.mainBar.setEffect(shadow);
		this.mainBar.setFill(new ImagePattern(GameImageBank.hud_bar));
		this.topHudBar.setFill(new ImagePattern(GameImageBank.hud_bar_cover));
		this.bottomHudBar.setFill(new ImagePattern(GameImageBank.hud_bar_cover));
		game.getTenthLayer().getChildren().add(mainBar);
		game.getFourTeenthLayer().getChildren().add(topHudBar);
		game.getFourTeenthLayer().getChildren().add(bottomHudBar);
	}
	/**
	 * Method which updates the movement of
	 * both the top and the bottom HUD bar
	 *
	 */
	public void updateHudBars() {
		yOne = yOne + swipeSpeedTop/GameManager.ScaleY;
		yTwo = yTwo + swipeSpeedBottom/GameManager.ScaleY;
		if (showHUDCover) {
			swipeSpeedTop = 2.5;
			if (yOne > mainBar.getTranslateY() - GameManager.ScaleY(15)) {
				swipeSpeedTop = 0;
			}
			swipeSpeedBottom = -3.5;
			if (yTwo < GameSettings.HEIGHT-bottomHudBar.getHeight()+GameManager.ScaleY(40)) {
				swipeSpeedBottom = 0;
			}
		}
		if (hideHUDCover) {
			swipeSpeedTop = -2.55;
			if (yOne < 0 - topHudBar.getHeight()+GameManager.ScaleY(20)) {
				swipeSpeedTop = 0;
			}
			swipeSpeedBottom = 3.5;
			if (yTwo > GameSettings.HEIGHT) {
				swipeSpeedBottom = 0;
			}
		}
		topHudBar.setTranslateY(yOne);
		bottomHudBar.setTranslateY(yTwo);
	}
	/**
	 * Method which shows or hides the HUD bar
	 * depending on the current status of the HUD
	 */
	public void showHide() {
		if (showHUDCover) {
			showHUDCover = false;
			hideHUDCover = true;
		} else if (hideHUDCover) {
			hideHUDCover = false;
			showHUDCover = true;
		}
	}
	/**
	 * Method which shows the HUD bar
	 * cover
	 */
	public void showHUDCover() {
		hideHUDCover = false;
		showHUDCover = true;
	}
	/**
	 * Method which hides the HUD bar
	 * cove
	 */
	public void hideHUDCover() {
		showHUDCover = false;
		hideHUDCover = true;
	}
	/**
	 * Method which sets the visibility
	 * of all the hud elements in this class
	 * to false
	 */
	public void hide() {
		if (PlayerOne.LEVEL_COMPLETED || PlayerTwo.LEVEL_COMPLETED) {
			topHudBar.setVisible(false);
			bottomHudBar.setVisible(false);
			mainBar.setVisible(false);
		}
	}
	/**
	 * Method which set the visibility
	 * of all the hud elements in this class
	 * to true
	 */
	public void show() {
		topHudBar.setVisible(true);
		bottomHudBar.setVisible(true);
		mainBar.setVisible(true);
	}
	public double getTopCoverY(){
		return topHudBar.getY();
	}
	public double getBottomCoverY(){
		return bottomHudBar.getY();
	}

}
