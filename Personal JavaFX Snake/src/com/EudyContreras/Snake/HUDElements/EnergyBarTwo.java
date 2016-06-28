package com.EudyContreras.Snake.HUDElements;

import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.FrameWork.GameSettings;
import com.EudyContreras.Snake.Identifiers.GameStateID;
import com.EudyContreras.Snake.ImageBanks.GameImageBank;
import com.EudyContreras.Snake.PlayerTwo.PlayerTwo;

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
public class EnergyBarTwo {

	private boolean depleated = false;
	private boolean speedThrust = false;
	private double maxEnergyLevel = 100;
	private double initialX;
	private double x = 0;
	private double width = 0;
	private double delay = 0;
	private double moveX = 400;
	private double velX = 0;
	private GameManager game;
	private PlayerTwo player;
	private Rectangle energyBar = new Rectangle();

	/**
	 * Constructor which takes the main class as parameter along with the
	 * position and dimension of this energy bar.
	 * @param game: Main game class which connects this class to all others
	 * @param x: X coordinate for this energy bar
	 * @param y: Y coordinate for this energy bar
	 * @param width: Horizontal dimension for this energy bar
	 * @param height: Vertival dimension for this energy bar
	 */
	public EnergyBarTwo(GameManager game, double x, double y, double width, double height) {
		this.x = x;
		this.initialX = x;
		this.game = game;
		this.width = width;
		this.maxEnergyLevel = width;
		this.player = game.getGameLoader().getPlayerTwo();
		this.energyBar.setWidth(width);
		this.energyBar.setHeight(height);
		this.energyBar.setTranslateX(x);
		this.energyBar.setTranslateY(y);
		this.energyBar.setRotate(0);
		this.energyBar.setFill(new ImagePattern(GameImageBank.energy));
		this.game.getEleventhLayer().getChildren().add(energyBar);
	}
	/**
	 * Method which updates the rate at which this energy bar
	 * depletes and regenarates. This methods calls the deplete and
	 * the regenerate function at the rate of the framerate.
	 */
	public void update(){
		if(game.getStateID() == GameStateID.GAMEPLAY){
		depleteEnergy();
		regenerateEnergy();
		}
		popIn();
	}
	private void popIn(){
		this.moveX+=velX;
		this.energyBar.setTranslateX(x+moveX);
		if(moveX<0){
			moveX = 0;
		}
	}
	public void moveLeft(){
		this.velX = GameManager.ScaleX(-10);
	}
	public void moveRight(){
		this.velX = GameManager.ScaleX(10);
	}
	public void stopMoving(){
		this.velX = 0;
	}
	public void setMoveX(double moveX){
		this.moveX = moveX;
	}
	/**
	 * Method which depletes the energy of the player by a constant
	 * percentage. This method also determines what action to take
	 * when the energy levels have reach their minimum
	 */
	private void depleteEnergy() {
		if (speedThrust == true) {
			x+=GameSettings.ENERGY_COMSUMPTION_SPEED;
			width -= GameSettings.ENERGY_COMSUMPTION_SPEED;
		}

		if (width <= 0) {
			player.setAllowThrust(false);
			player.setThrustState(false);
			width = 0;
			x = initialX + maxEnergyLevel;
		}
		this.energyBar.setWidth(width);
		this.energyBar.setTranslateX(x);
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
					x-=GameSettings.ENERGY_REGENRATION_SPEED;
				}
			}
		}
		if (width >= 25) {
			if(player.isAllowCollision())
			player.setAllowThrust(true);
		}
	}
	/**
	 * Method which when called will refill energy
	 * levels to their maximun levels
	 */
	public void refill() {
		this.x = initialX;
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
		this.x = initialX + maxEnergyLevel;
		this.energyBar.setWidth(width);
		this.player.setAllowThrust(false);
	}
	/**
	 * Method whichs sets the visibility
	 * state of the UI elements used by
	 * this class
	 * @param state
	 */
	public void setVisible(boolean state){
		energyBar.setVisible(state);
	}
	public boolean isDepleated() {
		return depleated;
	}

	public void setDepleated(boolean depleated) {
		this.depleated = depleated;
	}

	public void setSpeedThrust(boolean shotsFired) {
		this.speedThrust = shotsFired;
	}

	public void setSetDelay(boolean setDelay) {
	}

	public double getMaxEnergyLevel() {
		return maxEnergyLevel;
	}

	public void setMaxEnergyLevel(double maxEnergyLevel) {
		this.maxEnergyLevel = maxEnergyLevel;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getDelay() {
		return delay;
	}

	public void setDelay(double delay) {
		this.delay = delay;
	}

	public void setPlayer() {
		this.player = null;
		this.player = game.getGameLoader().getPlayerTwo();
	}
}
