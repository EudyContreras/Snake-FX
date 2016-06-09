package com.EudyContreras.Snake.HUDElements;

import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.FrameWork.GameSettings;
import com.EudyContreras.Snake.ImageBanks.GameImageBank;
import com.EudyContreras.Snake.PlayerOne.PlayerOne;
import com.EudyContreras.Snake.PlayerTwo.PlayerTwo;

import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * This class is used to simulate a simple health bar which will decrease under
 * certain conditions, and that will also self restore.
 *
 * @author Eudy Contreras
 *
 */
public class HealthBarTwo {

	private boolean setDelay = false;
	private boolean killPlayer = false;
	private boolean playerIsAlive = true;
	private double maxHealth = 100;
	private double x = 0;
	private double y = 0;
	private double oldX = 0;
	private double width = 0;
	private double height = 0;
	private int delay = 0;
	private GameManager game;
	private PlayerTwo player;
	private Rectangle healthBar = new Rectangle();
	private Rectangle healthBarBorder = new Rectangle();
	private Rectangle playerHud = new Rectangle();
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
	public HealthBarTwo(GameManager game, double x, double y, double width, double height) {
		this.x = x;
		this.oldX = this.x;
		this.width = width;
		this.game = game;
		this.maxHealth = width;
		this.player = game.getGameLoader().getPlayerTwo();
		this.healthBar.setWidth(width);
		this.healthBar.setHeight(height+4);
		this.healthBar.setTranslateX(x);
		this.healthBar.setTranslateY(y-2);
		this.healthBar.setRotate(-1);
		this.healthBarBorder.setWidth(width+3);
		this.healthBarBorder.setHeight(height+2);
		this.healthBarBorder.setTranslateX(x-2);
		this.healthBarBorder.setTranslateY(y-1);
		this.healthBarBorder.setRotate(-1);
		this.playerHud.setWidth(width/2);
		this.playerHud.setHeight(height*2);
		this.playerHud.setTranslateX(x-width+width/2);
		this.playerHud.setTranslateY(10);
		this.playerHead.setRadius(GameManager.ScaleX(30));
		this.playerHead.setCenterX(x-width+width-playerHead.getRadius()*1.40);
		this.playerHead.setCenterY(y+3);
		this.playerHead.setFill(new ImagePattern(GameImageBank.snakeTwoEating));
		this.playerHud.setFill(new ImagePattern(GameImageBank.player_two_hud));
		this.healthBar.setFill(new ImagePattern(GameImageBank.health_bar_green_two));
		this.healthBarBorder.setFill(new ImagePattern(GameImageBank.health_bar_red_two));
		this.game.getEleventhLayer().getChildren().add(playerHud);
		this.game.getEleventhLayer().getChildren().addAll(healthBarBorder);
		this.game.getEleventhLayer().getChildren().add(healthBar);
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
			x += GameSettings.DAMAGE_AMOUNT;
			setDelay = true;
			player.setCollision(false);
		}
		if (width <= 0 && playerIsAlive) {
			killPlayer();
		}
		this.healthBar.setTranslateX(x);
		this.healthBar.setWidth(width);

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
						x -= GameSettings.HEALTH_REGENERATION_SPEED;
					}
					if (x <= oldX) {
						x = oldX;
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
		if(killPlayer){
			player.die();
			playerIsAlive = false;
			killPlayer = false;
		}
	}
	/**
	 * Method which sets thev visibility of this health bar
	 * to false.
	 */
	public void hide() {
		if (PlayerOne.LEVEL_COMPLETED || PlayerTwo.LEVEL_COMPLETED) {
			healthBar.setVisible(false);
			healthBarBorder.setVisible(false);
		}
	}
	/**
	 * Method which sets the visibility of this health bar 
	 * to true
	 */
	public void show() {
		healthBar.setVisible(true);
		healthBarBorder.setVisible(true);
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
		this.x = oldX;
		this.healthBar.setTranslateX(x);
		this.healthBar.setWidth(maxHealth);
	}
	/**
	 * Method which drains this health bar to its minimum value
	 */
	public void drainAll() {
		this.width = 0;
		this.healthBar.setWidth(width);
	}

	public void setPlayer() {
		this.player = null;
		this.player = game.getGameLoader().getPlayerTwo();
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
