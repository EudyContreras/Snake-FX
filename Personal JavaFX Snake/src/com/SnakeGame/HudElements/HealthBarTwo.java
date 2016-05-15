package com.SnakeGame.HudElements;

import com.SnakeGame.FrameWork.Settings;
import com.SnakeGame.FrameWork.SnakeGame;
import com.SnakeGame.ImageBanks.GameImageBank;
import com.SnakeGame.PlayerOne.PlayerOne;
import com.SnakeGame.PlayerTwo.PlayerTwo;

import javafx.scene.paint.ImagePattern;
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
	private double oldX = 0;
	private double width = 0;
	private int delay = 0;
	private SnakeGame game;
	private PlayerTwo player;
	private Rectangle healthBar = new Rectangle();
	private Rectangle healthBarBorder = new Rectangle();

	public HealthBarTwo(SnakeGame game, double x, double y, double width, double height) {
		this.x = x;
		this.oldX = this.x;
		this.width = width;
		this.player = game.getloader().getPlayerTwo();
		this.game = game;
		this.healthBar.setWidth(width);
		this.healthBar.setHeight(height);
		this.healthBar.setTranslateX(x);
		this.healthBar.setTranslateY(y);
		this.healthBarBorder.setWidth(width);
		this.healthBarBorder.setHeight(height);
		this.healthBarBorder.setTranslateX(x);
		this.healthBarBorder.setTranslateY(y);
		this.healthBar.setFill(new ImagePattern(GameImageBank.healthBarGreen2));
		this.healthBarBorder.setFill(new ImagePattern(GameImageBank.healthBarRed2));
		game.getOverlay().getChildren().addAll(healthBarBorder);
		game.getOverlay().getChildren().add(healthBar);
		this.maxHealth = width;
	}

	/**
	 * This method depletes the health by a specific percentage and under
	 * specific conditions
	 */
	public void depleteHealth() {

		if (player.isCollision() == true) {
			width -= Settings.DAMAGE_AMOUNT;
			x += Settings.DAMAGE_AMOUNT;
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
						width += Settings.HEALTH_REGENERATION_SPEED;
						x -= Settings.HEALTH_REGENERATION_SPEED;
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
		this.player = game.getloader().getPlayerTwo();
	}

}
