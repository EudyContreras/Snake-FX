package com.SnakeGame.HUDElements;

import com.SnakeGame.FrameWork.GameManager;
import com.SnakeGame.FrameWork.GameSettings;
import com.SnakeGame.ImageBanks.GameImageBank;
import com.SnakeGame.PlayerTwo.PlayerTwo;

import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

/**
 * This class is used to keep track of energy used by the player if there's is
 * any it will decrease and regenerate according to time passed and given
 * actions by the player.
 *
 * @author Eudy Contreras
 *
 */
public class EnergyBarTwo {



	private boolean depleated = false;
	private boolean speedThrust = false;
	private boolean setDelay = false;
	private double maxEnergyLevel = 100;
	private double initialX;
	private double x = 0;
	private double y = 0;
	private double width = 0;
	private double height = 0;
	private double delay = 0;
	private PlayerTwo player;
	private Rectangle energyBar = new Rectangle();
	private Rectangle energyBarBorder = new Rectangle();

	public EnergyBarTwo(GameManager game, double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.initialX = x;
		this.width = width;
		this.height = height;
		this.player = game.getGameLoader().getPlayerTwo();
		this.energyBar.setWidth(width);
		this.energyBar.setHeight(height);
		this.energyBar.setTranslateX(x);
		this.energyBar.setTranslateY(y);
		this.energyBar.setRotate(-1);
		this.energyBar.setFill(new ImagePattern(GameImageBank.energy_bar_two));
		this.energyBarBorder.setWidth(width);
		this.energyBarBorder.setHeight(height);
		this.energyBarBorder.setTranslateX(x);
		this.energyBarBorder.setTranslateY(y);
		this.energyBarBorder.setRotate(-1);
		this.energyBarBorder.setFill(new ImagePattern(GameImageBank.energy_bar_two_border));
		game.getEleventhLayer().getChildren().add(energyBarBorder);
		game.getEleventhLayer().getChildren().add(energyBar);
		this.maxEnergyLevel = width;
	}

	/**
	 * depletes the energy by a constant percentage
	 */
	public void deplete() {
		if (speedThrust == true) {
			x+=GameSettings.ENERGY_COMSUMPTION_SPEED;
			width -= GameSettings.ENERGY_COMSUMPTION_SPEED;
		}

		if (width <= 0) {
			player.setAllowThrust(false);
			player.setThrustState(false);
			width = 0;
			x = initialX + maxEnergyLevel;
			setDelay = true;
		}
		this.energyBar.setWidth(width);
		this.energyBar.setTranslateX(x);
	}

	/**
	 * sets a regeneration delay by a constant percentage
	 */
	public void setDelay() {
		delay = 90;
		setDelay = false;
	}

	/**
	 * Regenerates the energy until it reaches its max value
	 */
	public void regerate() {
		if(speedThrust==false){
			delay--;
			if (width < maxEnergyLevel) {
				if (delay <= 0){
					delay = 0;
					width += GameSettings.ENERGY_REGENRATION_SPEED;
					x-=GameSettings.ENERGY_REGENRATION_SPEED;
				}
			}
		}
		if (width >= 25) {
			if(player.isAllowCollision())
			player.setAllowThrust(true);
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

	public boolean isSpeedThrust() {
		return speedThrust;
	}

	public void setSpeedThrust(boolean shotsFired) {
		this.speedThrust = shotsFired;
	}

	public boolean isSetDelay() {
		return setDelay;
	}

	public void setSetDelay(boolean setDelay) {
		this.setDelay = setDelay;
	}

	public double getMaxEnergyLevel() {
		return maxEnergyLevel;
	}

	public void setMaxEnergyLevel(double maxEnergyLevel) {
		this.maxEnergyLevel = maxEnergyLevel;
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

	public double getDelay() {
		return delay;
	}

	public void setDelay(double delay) {
		this.delay = delay;
	}

	public PlayerTwo getPlayer() {
		return player;
	}

	public void setPlayer(PlayerTwo player) {
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
