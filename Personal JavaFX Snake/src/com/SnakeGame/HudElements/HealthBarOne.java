package com.SnakeGame.HudElements;

import com.SnakeGame.Core.GameImageBank;
import com.SnakeGame.Core.Settings;
import com.SnakeGame.Core.SnakeGame;
import com.SnakeGame.PlayerTwo.Player2;
import com.SnakeGame.SnakeOne.SnakeOne;

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
public class HealthBarOne {

	boolean depleated = false;
	boolean setDelay = false;
	boolean killPlayer = false;
	boolean playerIsAlive = true;
	double maxHealth = 100;
	double x = 0;
	double y = 0;
	double width = 0;
	double height = 0;
	int delay = 0;
	SnakeGame game;
	SnakeOne player;
	GraphicsContext gc;
	Rectangle healthBar = new Rectangle();
	Rectangle healthBarBorder = new Rectangle();

	public HealthBarOne(SnakeGame game, double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.player = game.getloader().getPlayer();
		this.game = game;
		// this.healthBar.setWidth(width);
		// this.healthBar.setHeight(height);
		// this.healthBar.setTranslateX(x);
		// this.healthBar.setTranslateY(y);
		// this.healthBar.setArcHeight(20);
		// this.healthBar.setArcWidth(20);

		this.healthBar.setWidth(width);
		this.healthBar.setHeight(height);
		this.healthBar.setTranslateX(x);
		this.healthBar.setTranslateY(y);
		this.healthBarBorder.setWidth(width);
		this.healthBarBorder.setHeight(height);
		this.healthBarBorder.setTranslateX(x);
		this.healthBarBorder.setTranslateY(y);
		this.healthBar.setFill(new ImagePattern(GameImageBank.healthBarGreen1));
		// this.healthBarBorder.setWidth(width);
		// this.healthBarBorder.setHeight(height);
		// this.healthBarBorder.setArcWidth(20);
		// this.healthBarBorder.setArcHeight(20);
		// this.healthBarBorder.setTranslateX(x);
		// this.healthBarBorder.setTranslateY(y);
		this.healthBarBorder.setFill(new ImagePattern(GameImageBank.healthBarRed1));
		game.getOverlay().getChildren().add(healthBarBorder);
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
			setDelay = true;
			player.setCollision(false);
		}
		if (width <= 0 && playerIsAlive) {
			killPlayer = true;
			playerIsAlive = false;
		}
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
					if (delay <= 0)
						width += Settings.HEALTH_REGENERATION_SPEED;
				}
			}
		}
		if (killPlayer == true) {
			player.die();
			killPlayer = false;
		}
	}

	public void hide() {
		if (SnakeOne.levelComplete || Player2.levelComplete) {
			healthBar.setVisible(false);
			healthBarBorder.setVisible(false);
		}
	}

	public void show() {
		healthBar.setVisible(true);
		healthBarBorder.setVisible(true);
	}

	public void refill() {
		this.depleated = false;
		this.setDelay = false;
		this.killPlayer = false;
		this.playerIsAlive = true;
		this.width = maxHealth;
		this.healthBar.setWidth(maxHealth);
	}

	public void drainAll() {
		this.width = 0;
		this.healthBar.setWidth(width);
	}

	public void setPlayer() {
		this.player = null;
		this.player = game.getloader().getPlayer();
	}

}
