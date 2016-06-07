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
 * certain conditions, and that will also self restore
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

	public HealthBarTwo(GameManager game, double x, double y, double width, double height) {
		this.x = x;
		this.oldX = this.x;
		this.width = width;
		this.player = game.getGameLoader().getPlayerTwo();
		this.game = game;
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
		game.getEleventhLayer().getChildren().add(playerHud);
		game.getEleventhLayer().getChildren().addAll(healthBarBorder);
		game.getEleventhLayer().getChildren().add(healthBar);
		game.getEleventhLayer().getChildren().add(playerHead);
		this.maxHealth = width;
	}
	/**
	 * will reduce the health by the amount of damage
	 * @param amountOfDamage
	 */
	public void reduceHealth(double amountOfDamage){
		width -= amountOfDamage;
		setDelay = true;
	}
	/**
	 * This method depletes the health by a specific percentage and under
	 * specific conditions
	 */
	public void depleteHealth() {

		if (player.isCollision() == true) {
			width -= GameSettings.DAMAGE_AMOUNT;
			x += GameSettings.DAMAGE_AMOUNT;
			setDelay = true;
			player.setCollision(false);
		}
		if (width <= 0 && playerIsAlive) {
			killPlayer = true;
			playerIsAlive = false;
		}
		this.healthBar.setTranslateX(x);
		this.healthBar.setWidth(width);

	}

	/**
	 * This method add a predetermined delay to health regeneration
	 */
	public void setDelay() {

		if (setDelay == true) {
			delay = 100;
			setDelay = false;
		}
	}

	/**
	 * This method regenerates the health over a given period of time.
	 */
	public void regerateHealth() {
		// hide();
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
		if (killPlayer == true) {
			player.die();
			killPlayer = false;
		}
		// x = (int) width;
	}

	public void hide() {
		if (PlayerOne.LEVEL_COMPLETED || PlayerTwo.LEVEL_COMPLETED) {
			healthBar.setVisible(false);
			healthBarBorder.setVisible(false);
		}
	}

	public void show() {
		healthBar.setVisible(true);
		healthBarBorder.setVisible(true);
	}

	public void refill() {
		this.setDelay = false;
		this.killPlayer = false;
		this.playerIsAlive = true;
		this.width = maxHealth;
		this.x = oldX;
		this.healthBar.setTranslateX(x);
		this.healthBar.setWidth(maxHealth);
	}

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