package com.SnakeGame.HudElements;

import com.SnakeGame.FrameWork.GameManager;
import com.SnakeGame.PlayerOne.PlayerOne;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * This class is used to keep track of energy used by the player if there's is
 * any it will decrease and regenerate according to time passed and given
 * actions by the player.
 *
 * @author Eudy Contreras
 *
 */
public class EnergyMeter {



	private boolean depleated = false;
	private boolean shotsFired = false;
	private boolean setDelay = false;
	private int maxEnergyLevel = 100;
	private int x = 0;
	private int y = 0;
	private int width = 0;
	private int height = 0;
	private int delay = 0;
	private PlayerOne player;
	private Rectangle energyBar = new Rectangle();
	private Rectangle energyBarBorder = new Rectangle();

	public EnergyMeter(GameManager game, int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.player = game.getGameLoader().getPlayerOne();
		this.energyBar.setWidth(width);
		this.energyBar.setHeight(height);
		this.energyBar.setTranslateX(x);
		this.energyBar.setTranslateY(y);
		this.energyBar.setFill(Color.ORANGE);
		this.energyBarBorder.setWidth(width);
		this.energyBarBorder.setHeight(height);
		this.energyBarBorder.setTranslateX(x);
		this.energyBarBorder.setTranslateY(y);
		this.energyBarBorder.setStroke(Color.ORANGE);
		this.energyBarBorder.setFill(Color.TRANSPARENT);
		game.getSeventhLayer().getChildren().add(energyBar);
		game.getSeventhLayer().getChildren().add(energyBarBorder);
		this.maxEnergyLevel = width;
	}

	/**
	 * depletes the energy by a constant percentage
	 */
	public void deplete() {
		if (shotsFired == true) {
			width -= 70;
			shotsFired = false;
		}

		if (width >= 15) {
			player.setSpeedBump(false);
		} else if (width <= 0) {
			player.setSpeedBump(true);
			setDelay = true;
		}
		this.energyBar.setWidth(width);
	}

	/**
	 * sets a regeneration delay by a constant percentage
	 */
	public void setDelay() {
		if (setDelay == true) {
			delay = 90;
			width = 1;
			setDelay = false;
		}
	}

	public void shotFired(boolean state) {
		this.shotsFired = state;
	}

	/**
	 * Regenerates the energy until it reaches its max value
	 */
	public void regerate() {
		setDelay();
		if (delay >= 0) {
			delay--;
		}
		if (player.isNotMovingFast() == true) {
			if (width < maxEnergyLevel) {
				if (delay <= 0)
					width += 2;
			}
		}
	}

	public void refill() {
		this.width = maxEnergyLevel;
		this.energyBar.setWidth(maxEnergyLevel);
	}
	public boolean isDepleated() {
		return depleated;
	}

	public void setDepleated(boolean depleated) {
		this.depleated = depleated;
	}

	public boolean isShotsFired() {
		return shotsFired;
	}

	public void setShotsFired(boolean shotsFired) {
		this.shotsFired = shotsFired;
	}

	public boolean isSetDelay() {
		return setDelay;
	}

	public void setSetDelay(boolean setDelay) {
		this.setDelay = setDelay;
	}

	public int getMaxEnergyLevel() {
		return maxEnergyLevel;
	}

	public void setMaxEnergyLevel(int maxEnergyLevel) {
		this.maxEnergyLevel = maxEnergyLevel;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public PlayerOne getPlayer() {
		return player;
	}

	public void setPlayer(PlayerOne player) {
		this.player = player;
	}

	public Rectangle getEnergyBar() {
		return energyBar;
	}

	public void setEnergyBar(Rectangle energyBar) {
		this.energyBar = energyBar;
	}

	public Rectangle getEnergyBarBorder() {
		return energyBarBorder;
	}

	public void setEnergyBarBorder(Rectangle energyBarBorder) {
		this.energyBarBorder = energyBarBorder;
	}
}
