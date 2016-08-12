package com.EudyContreras.Snake.HUDElements;

import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Application.GameSettings;
import com.EudyContreras.Snake.ImageBanks.GameImageBank;
import com.EudyContreras.Snake.PlayerOne.PlayerOne;
import com.EudyContreras.Snake.PlayerTwo.PlayerTwo;

import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

/**
 * This class is used to simulate a simple health bar which will decrease under
 * certain conditions, and that will also self restore.
 *
 * @author Eudy Contreras
 *
 */
public class HealthBarTwo {

	private boolean setDelay = false;
	private boolean hidePlayer = true;
	private boolean killPlayer = false;
	private boolean playerIsAlive = true;
	private double playerOpacity = 0;
	private double maxHealth = 100;
	private double damageX;
	private double x = 0;
	private double y = 0;
	private double width = 0;
	private double height = 0;
	private double moveX = 400;
	private double velX = 0;
	private int delay = 0;
	private GameManager game;
	private PlayerTwo player;
	private Rectangle healthBarRed = new Rectangle();
	private Rectangle healthBarGreen = new Rectangle();
	private Rectangle healthBarBorder = new Rectangle();
	private Circle playerHead = new Circle();


	/**
	 * Constructor which takes the main class as parameter along with the
	 * position and dimension of this energy bar.
	 * @param game: Main game class which connects this class to all others
	 * @param x: X coordinate for this energy bar
	 * @param y: Y coordinate for this energy bar
	 * @param width: Horizontal dimension for this energy bar
	 * @param height: Vertival dimension for this energy bar
	 */
	public HealthBarTwo(GameManager game) {
		this.width = 515;
		this.height = 110;
		this.maxHealth = width*0.64;
		this.x = GameSettings.WIDTH - width;
		this.y = 1;
		this.game = game;
		this.player = game.getGameLoader().getPlayerTwo();
		this.healthBarGreen.setWidth(width*0.81);
		this.healthBarGreen.setHeight(height*0.3);
		this.healthBarGreen.setTranslateX(x+10);
		this.healthBarGreen.setTranslateY(y+9);
		this.healthBarGreen.setRotationAxis(Rotate.Y_AXIS);
		this.healthBarRed.setWidth(width*0.81);
		this.healthBarRed.setHeight(height*0.3);
		this.healthBarRed.setTranslateX(x+10);
		this.healthBarRed.setTranslateY(y+9);
		this.healthBarRed.setRotationAxis(Rotate.Y_AXIS);
		this.healthBarBorder.setWidth(width);
		this.healthBarBorder.setHeight(height);
		this.healthBarBorder.setTranslateX(x);
		this.healthBarBorder.setTranslateY(y);
		this.healthBarBorder.setRotationAxis(Rotate.Y_AXIS);
		this.playerHead.setRadius(40);
		this.playerHead.setCenterX(x+width - playerHead.getRadius()-(15));
		this.playerHead.setTranslateY(y+60);
		this.playerHead.setFill(new ImagePattern(GameImageBank.snakeTwoEating));
		this.healthBarRed.setFill(new ImagePattern(GameImageBank.red_health));
		this.healthBarGreen.setFill(new ImagePattern(GameImageBank.green_health));
		this.healthBarBorder.setFill(new ImagePattern(GameImageBank.health_bar_two));
		this.game.getEleventhLayer().getChildren().add(healthBarRed);
		this.game.getEleventhLayer().getChildren().add(healthBarGreen);
		this.game.getEleventhLayer().getChildren().add(healthBarBorder);
		this.game.getEleventhLayer().getChildren().add(playerHead);
	}
	/**
	 * Calls to this method which update
	 * the draining and regeneration funcitonality
	 * of this healthbar
	 */
	public void update(){
		depleteHealth();
		regenerateHealth();
		hidePlayer();
		popIn();
	}
	private void popIn(){
		this.moveX+=velX;
		this.healthBarGreen.setTranslateX(x+10+moveX+damageX);
		this.healthBarRed.setTranslateX(x+10+moveX);
		this.playerHead.setCenterX(GameSettings.WIDTH - 55+moveX);
		this.healthBarBorder.setTranslateX(x+moveX);
		if(healthBarBorder.getTranslateX()>GameSettings.WIDTH){
			this.stopMoving();
		}
		if(moveX<0){
			moveX = 0;
		}
		game.getEnergyBarTwo().setMoveX(moveX);
		game.getScoreBoardTwo().setMoveX(moveX);
	}
	public void moveLeft(){
		velX = -15;
	}
	public void moveRight(){
		velX = 15;
	}
	public void stopMoving(){
		this.velX = 0;
	}
	/**
	 * Method which when called will reduce the health
	 * by the amount of damage specified in the parameter
	 * @param amountOfDamage: damage which this method inflicts to the player
	 */
	public void reduceHealth(double amountOfDamage){
		width -= amountOfDamage;
		setDelay = true;
	}
	/**
	 * This method depletes the health by a specific percentage when
	 * under specific conditions. This method also triggers the specified
	 * event that will happen if the the health reaches zero
	 */
	public void depleteHealth() {

		if (player.isCollision() == true) {
			width -= GameSettings.DAMAGE_AMOUNT;
			damageX += GameSettings.DAMAGE_AMOUNT;
			setDelay = true;
			player.setCollision(false);
		}
		if (width <= 0 && playerIsAlive) {
			killPlayer();
		}
		this.healthBarGreen.setWidth(width);

	}

	/**
	 * This method add a precalculated delay which plays
	 * a role in the time it takes for health to start self
	 * regeneration
	 */
	public void setDelay() {

		if (setDelay == true) {
			delay = 100;
			setDelay = false;
		}
	}

	/**
	 * This method regenerates the health over a given period of time.
	 * This method will regenerate the health as long as the health
	 * is not being depleated, the delay has reach zero, and the
	 * player is still alived
	 */
	public void regenerateHealth() {
		if (player.isDead() == false) {
			setDelay();
			if (delay >= 0) {
				delay--;
			}

			if (player.isCollision() == false) {
				if (width < maxHealth) {
					if (delay <= 0) {
						width += GameSettings.HEALTH_REGENERATION_SPEED;
						damageX -= GameSettings.HEALTH_REGENERATION_SPEED;
					}
					if (damageX <= 0) {
						damageX = 0;
					}
				}
			}
		}
	}
	/**
	 * Method which kills the player once the
	 * health has reach its minimun amount which
	 * is zero
	 */
	public void killPlayer(){
		if(!killPlayer){
			player.die();
			playerIsAlive = false;
			killPlayer = true;
		}
	}
	/**
	 * Method which sets thev visibility of this health bar
	 * to false.
	 */
	public void hide() {
		if (PlayerOne.LEVEL_COMPLETED || PlayerTwo.LEVEL_COMPLETED) {
			healthBarGreen.setVisible(false);
			healthBarBorder.setVisible(false);
			healthBarRed.setVisible(false);
		}
	}
	/**
	 * Method which sets the visibility of this health bar
	 * to true
	 */
	public void show() {
		healthBarGreen.setVisible(true);
		healthBarBorder.setVisible(true);
		healthBarRed.setVisible(true);
	}
	/**
	 * Method which refills this health bar to its maximum value
	 *
	 */
	public void refill() {
		this.setDelay = false;
		this.killPlayer = false;
		this.playerIsAlive = true;
		this.width = maxHealth;
		this.damageX = 0;
		this.healthBarGreen.setTranslateX(x+moveX);
		this.healthBarGreen.setWidth(maxHealth);
	}
	/**
	 * Method which drains this health bar to its minimum value
	 */
	public void drainAll() {
		this.width = 0;
		this.healthBarGreen.setWidth(width);
	}
	/**
	 * Method whichs sets the visibility
	 * state of the UI elements used by
	 * this class
	 * @param state
	 */
	public void setVisible(boolean state){
		playerHead.setVisible(state);
		healthBarBorder.setVisible(state);
		healthBarGreen.setVisible(state);
		healthBarRed.setVisible(state);
	}
	public void hidePlayer(){
		playerHead.setOpacity(playerOpacity);
		if(hidePlayer){
			playerOpacity-=0.05;
			if(playerOpacity<=0){
				playerOpacity = 0;
			}

		}
		if(!hidePlayer){
			playerOpacity+=0.05;
			if(playerOpacity>=1){
				playerOpacity = 1;
			}
		}
	}
	public void setPlayer() {
		this.player = null;
		this.player = game.getGameLoader().getPlayerTwo();
	}
	public void hidePlayerHead(boolean state){
		hidePlayer = state;
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public double getWidth() {
		return width;
	}
	public double getHeight() {
		return height;
	}

}
