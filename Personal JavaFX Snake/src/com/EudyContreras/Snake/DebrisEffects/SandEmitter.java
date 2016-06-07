package com.EudyContreras.Snake.DebrisEffects;

import java.util.Random;

import com.EudyContreras.Snake.EnumIDs.GameStateID;
import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.FrameWork.GameSettings;
import com.EudyContreras.Snake.ImageBanks.GameImageBank;
import com.EudyContreras.Snake.PlayerOne.PlayerOne;
import com.EudyContreras.Snake.PlayerTwo.PlayerTwo;
import com.SnakeGame.Utilities.ScreenEffectUtility;

/**
 * This class is used to keep track of energy used by the player if there's is
 * any it will decrease and regenerate according to time passed and given
 * actions by the player.
 *
 * @author Eudy Contreras
 *
 */
public class SandEmitter {

	private boolean goUp = false;
	private boolean goDown = true;
	private double x = 0;
	private double y = 0;
	private double velY;
	private int interval = 0;
	private GameManager game;
	private Random rand;
	private ScreenEffectUtility overlay;

	public SandEmitter(GameManager game, double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.velY = 5;
		this.game = game;
		this.rand = new Random();
		this.overlay = game.getOverlayEffect();
	}

	public void move() {
		y += velY;
		if (y >= GameSettings.HEIGHT) {
			goUp = true;
			goDown = false;
		}
		if (y <= 0) {
			goDown = true;
			goUp = false;
		}
		if (goDown) {
			velY = 25;
		}
		if (goUp) {
			velY = -25;
		}
	}

	public void emit() {
		if (GameSettings.SAND_STORM && !PlayerOne.DEAD && !PlayerTwo.DEAD && !PlayerOne.LEVEL_COMPLETED && !PlayerTwo.LEVEL_COMPLETED && game.getStateID()!=GameStateID.GAME_MENU) {
			addRandomBlur(true);
			interval++;
			if (interval == GameSettings.SAND_SPAWN_DELAY && GameSettings.SAND_AMOUNT>1) {
				for (int i = 0; i < GameSettings.SAND_AMOUNT; i++) {
					game.getDebrisManager().addParticle(new SandStorms(game, GameImageBank.sand_grain, 5, x, y));
				}
				interval = 0;
			}
			else if (interval == GameSettings.SAND_SPAWN_DELAY) {
				game.getDebrisManager().addParticle(new SandStorms(game, GameImageBank.sand_grain, 5, x, y));
				interval = 0;
			}
		}
	}

	public void addRandomBlur(boolean random) {
		if (random && rand.nextInt(GameSettings.BLUR_RANDOMNESS) != 0) {
			return;
		}
		if (!PlayerOne.LEVEL_COMPLETED && !PlayerTwo.LEVEL_COMPLETED)
			this.overlay.addStormBlur();

	}

}
