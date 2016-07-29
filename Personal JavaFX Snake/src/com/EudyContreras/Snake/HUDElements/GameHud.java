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
	private double hideY = 0;
	private double hideVelY = 0;
	private double yTwo = GameSettings.HEIGHT;
	private double swipeSpeedBottom = 0;
	private boolean showHUD = true;
	private boolean hideHUD = false;
	private boolean hideHUDCover = false;
	private boolean showHUDCover = false;
	private GameManager game;
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
		this.game = game;
		this.shadow.setColor(Color.rgb(0, 0, 0, 0.5));
		this.shadow.setRadius(5);
		this.shadow.setOffsetX(0);
		this.shadow.setOffsetY(15);
		this.topHudBar.setWidth(width);
		this.topHudBar.setHeight(height+50);
		this.topHudBar.setTranslateX(x);
		this.topHudBar.setTranslateY(y-20);
		this.bottomHudBar.setWidth(width);
		this.bottomHudBar.setHeight(height+50);
		this.yTwo = GameSettings.HEIGHT-bottomHudBar.getHeight()+20;
		this.bottomHudBar.setTranslateX(x);
		this.bottomHudBar.setTranslateY(yTwo);
		this.bottomHudBar.setRotate(180);
		this.mainBar.setWidth(GameSettings.WIDTH+5);
		this.mainBar.setHeight(height-20);
		this.mainBar.setTranslateX(-2);
		this.mainBar.setTranslateY(-1);
		this.mainBar.setEffect(shadow);
		this.mainBar.setFill(new ImagePattern(GameImageBank.hud_bar));
		this.topHudBar.setFill(new ImagePattern(GameImageBank.hud_bar_cover));
		this.bottomHudBar.setFill(new ImagePattern(GameImageBank.hud_bar_cover));
		game.getEleventhLayer().getChildren().add(0,mainBar);
		game.getThirTeenthLayer().getChildren().add(topHudBar);
		game.getThirTeenthLayer().getChildren().add(bottomHudBar);
	}
	/**
	 * Method which updates the movement of
	 * both the top and the bottom HUD bar
	 *
	 */
	public void updateHudBars() {
		yOne = yOne + swipeSpeedTop;
		yTwo = yTwo + swipeSpeedBottom;
		if (showHUDCover) {
			swipeSpeedTop = 2.8;
			if (yOne > -41) {
				swipeSpeedTop = 0;
			}
			swipeSpeedBottom = -2.8;
			if (yTwo < GameSettings.HEIGHT-bottomHudBar.getHeight()+35) {
				swipeSpeedBottom = 0;
			}
		}
		if (hideHUDCover) {
			swipeSpeedTop = -2.8;
			if (yOne < 0 - (topHudBar.getHeight()-17)) {
				swipeSpeedTop = 0;
			}
			swipeSpeedBottom = 2.8;
			if (yTwo > GameSettings.HEIGHT) {
				swipeSpeedBottom = 0;
			}
		}
		topHudBar.setTranslateY(yOne);
		bottomHudBar.setTranslateY(yTwo);
		updateMovement();
	}
	public void updateMovement(){
		hideY+=hideVelY;
		if(hideY<-80){
			this.stopMoving();
		}
		if(hideY >0){
			hideY = 0;
		}
		this.mainBar.setTranslateY(-1+hideY);
	}
	public void moveUp(){
		hideVelY = -4;
	}
	public void moveDown(){
		hideVelY = 4;
	}
	public void stopMoving(){
		hideVelY = 0;
	}
	/**
	 * Method which shows or hides the HUD bar
	 * depending on the current status of the HUD
	 */
	public void showHide() {
		if (showHUD) {
			showHUD = false;
			hideHUD = true;
			moveDown();
		} else if (hideHUD) {
			hideHUD = false;
			showHUD = true;
			moveUp();
		}
	}
	/**
	 * Method which shows the HUD bar
	 * cover
	 */
	public void showHUDCover() {
		game.getHealthBarOne().hidePlayerHead(true);
		game.getHealthBarTwo().hidePlayerHead(true);
		game.getHealthBarOne().moveLeft();
		game.getHealthBarTwo().moveRight();
		hideHUDCover = false;
		showHUDCover = true;
	}
	/**
	 * Method which hides the HUD bar
	 * cove
	 */
	public void hideHUDCover() {
		game.getHealthBarOne().hidePlayerHead(false);
		game.getHealthBarTwo().hidePlayerHead(false);
		game.getHealthBarOne().moveRight();
		game.getHealthBarTwo().moveLeft();
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
	public final double getHudBottomY(){
		return mainBar.getTranslateX()+mainBar.getHeight();
	}

}
