package com.SnakeGame.Core;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
/**
 * This class is used to keep track of energy used by the player if there's is any
 * it will decrease and regenerate according to time passed and given actions by the
 * player.
 * @author Eudy Contreras
 *
 */
public class EnergyMeter {

	boolean depleated = false;
	boolean shotsFired = false;
	boolean setDelay = false;
	int maxEnergyLevel = 100;
	int x = 0;
	int y = 0;
	int width = 0;
	int height = 0;
	int delay = 0;
	Player player;
	Rectangle energyBar = new Rectangle();
	Rectangle energyBarBorder = new Rectangle();

	public EnergyMeter(SnakeGame game, int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.player = game.getloader().getPlayer();
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
		game.getOverlay().getChildren().add(energyBar);
		game.getOverlay().getChildren().add(energyBarBorder);
		this.maxEnergyLevel = width;
	}
	/**
	 * depletes the energy
	 * by a constant percentage
	 */
	public void deplete() {
		if (shotsFired == true) {
			width -= 70;
			shotsFired = false;
		}

		if (width >= 15) {
			player.setSpeedBump(false);
		}
		else if (width <= 0) {
			player.setSpeedBump(true);
			setDelay = true;
		}
		this.energyBar.setWidth(width);
	}
	/**
	 * sets a regeneration delay
	 * by a constant percentage
	 */
	public void setDelay(){
		if(setDelay == true){
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
		if(delay>=0){
			delay--;
		}
		if (player.isNotMovingFast() == true) {
			if (width < maxEnergyLevel) {
				if(delay<=0)
				width+=2;
			}
		}
	}
	public void refill(){
		this.width = maxEnergyLevel;
		this.energyBar.setWidth(maxEnergyLevel);
	}

}
