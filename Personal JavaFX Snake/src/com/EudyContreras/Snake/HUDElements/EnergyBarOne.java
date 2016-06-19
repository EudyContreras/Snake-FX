package com.EudyContreras.Snake.HUDElements;

import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.FrameWork.GameSettings;
import com.EudyContreras.Snake.ImageBanks.GameImageBank;
import com.EudyContreras.Snake.PlayerOne.PlayerOne;

import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

/**
 * This class is used to keep track of energy used by the player. If there's is
 * any energy it will decrease and regenerate according to a set delay and
 * and regeneration speed passed and given. A set of spsecific actions by the player
 * will reduce energy levels
 *
 * @author Eudy Contreras
 *
 */
public class EnergyBarOne {

	private boolean depleated = false;
	private boolean speedThrust = false;
	private double maxEnergyLevel = 100;
	private double width = 0;
	private double delay = 0;
	private GameManager game;
	private PlayerOne player;
	private Rectangle energyBar = new Rectangle();
	private Rectangle energyBarBorder = new Rectangle();

	/**
	 * Constructor which takes the main class as parameter along with the
	 * position and dimension of this energy bar.
	 * @param game: Main game class which connects this class to all others
	 * @param x: X coordinate for this energy bar
	 * @param y: Y coordinate for this energy bar
	 * @param width: Horizontal dimension for this energy bar
	 * @param height: Vertival dimension for this energy bar
	 */
	public EnergyBarOne(GameManager game, double x, double y, double width, double height) {
		this.game = game;
		this.width = width;
		this.maxEnergyLevel = width;
		this.player = game.getGameLoader().getPlayerOne();
		this.energyBar.setWidth(width);
		this.energyBar.setHeight(height);
		this.energyBar.setTranslateX(x);
		this.energyBar.setTranslateY(y);
		this.energyBar.setRotate(1);
		this.energyBar.setFill(new ImagePattern(GameImageBank.energy_bar_one));
		this.energyBarBorder.setWidth(width);
		this.energyBarBorder.setHeight(height);
		this.energyBarBorder.setTranslateX(x);
		this.energyBarBorder.setTranslateY(y);
		this.energyBarBorder.setRotate(1);
		this.energyBarBorder.setFill(new ImagePattern(GameImageBank.energy_bar_one_border));
		this.game.getEleventhLayer().getChildren().add(energyBarBorder);
		this.game.getEleventhLayer().getChildren().add(energyBar);
	}
	/**
	 * Method which updates the rate at which this energy bar
	 * depletes and regenarates. This methods calls the deplete and
	 * the regenerate function at the rate of the framerate.
	 */
	public void update(){
		depleteEnergy();
		regenerateEnergy();
	}
	/**
	 * Method which depletes the energy of the player by a constant
	 * percentage. This method also determines what action to take
	 * when the energy levels have reach their minimum
	 */
	private void depleteEnergy() {
		if (speedThrust == true) {
			width -= GameSettings.ENERGY_COMSUMPTION_SPEED;
		}

		if (width <= 0) {
			player.setAllowThrust(false);
			player.setThrustState(false);
			width = 0;
		}
		this.energyBar.setWidth(width);
	}

	/**
	 * Method which adds a precalculated delay
	 * to the the time it takes before energy
	 * can start regenerating
	 */
	public void setDelay() {
		delay = 60;
	}

	/**
	 * Method which regenerates the energy levels of the player
	 * at a constant precalculated rate. This method also controls
	 * the action to perform once energy levels have surpassed
	 * a precalculated thereshold.
	 */
	private void regenerateEnergy() {
		if(speedThrust==false){
			delay--;
			if (width < maxEnergyLevel) {
				if (delay <= 0){
					delay = 0;
					width += GameSettings.ENERGY_REGENRATION_SPEED;
				}
			}
		}
		if (width >= 25) {
			if(PlayerOne.KEEP_MOVING){
				player.setAllowThrust(true);
			}
		}
	}
	/**
	 * Method which when called will refill energy
	 * levels to their maximun levels
	 */
	public void refill() {
		this.width = maxEnergyLevel;
		this.energyBar.setWidth(maxEnergyLevel);

		this.player.setAllowThrust(true);
	}
	/**
	 * Method which when called will deplete all
	 * energy to their minimum leveles
	 */
	public void drainAll(){
		this.width = 0;
		this.energyBar.setWidth(width);
		this.player.setAllowThrust(false);
	}
	public boolean isSpeedThrust() {
		return speedThrust;
	}

	public void setSpeedThrust(boolean speedBoost) {
		this.speedThrust = speedBoost;
	}

	public void setSetDelay(boolean setDelay) {
	}

	public double getMaxEnergyLevel() {
		return maxEnergyLevel;
	}

	public void setMaxEnergyLevel(double maxEnergyLevel) {
		this.maxEnergyLevel = maxEnergyLevel;
	}

	public double getDelay() {
		return delay;
	}

	public void setDelay(double delay) {
		this.delay = delay;
	}

	public void setPlayer() {
		this.player = null;
		this.player = game.getGameLoader().getPlayerOne();
	}
	public boolean isDepleated() {
		return depleated;
	}
	public void setDepleated(boolean depleated) {
		this.depleated = depleated;
	}

}
