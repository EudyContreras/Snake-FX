package com.EudyContreras.Snake.ParticleEffects;

import java.util.Random;

import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.FrameWork.GameSettings;
import com.EudyContreras.Snake.Identifiers.GameStateID;
import com.EudyContreras.Snake.ImageBanks.GameLevelImage;
import com.EudyContreras.Snake.PlayerOne.PlayerOne;
import com.EudyContreras.Snake.PlayerTwo.PlayerTwo;
import com.EudyContreras.Snake.Utilities.ScreenEffectUtility;

/**
 * This class is used to keep track of energy used by the player if there's is
 * any it will decrease and regenerate according to time passed and given
 * actions by the player.
 *
 * @author Eudy Contreras
 *
 */
public class RainEmitter {

	private boolean goLeft = false;
	private boolean goRight = true;
	private double x = 0;
	private double y = 0;
	private double velX;
	private double speed;
	private int interval = 0;
	private GameManager game;
	private Random rand;
	private ScreenEffectUtility overlay;

	public RainEmitter(GameManager game, double x, double y,  double speed, double width, double height) {
		this.x = x;
		this.y = y;
		this.velX = 5;
		this.speed = speed;
		this.game = game;
		this.rand = new Random();
		this.overlay = game.getOverlayEffect();
	}

	public void move() {
		x += velX;
		if (x >= GameSettings.WIDTH) {
			goLeft = true;
			goRight = false;
		}
		if (x <= 0) {
			goRight = true;
			goLeft = false;
		}
		if (goRight) {
			velX = speed;
		}
		if (goLeft) {
			velX = -speed;
		}
	}

	public void emit() {
		if (GameSettings.RAIN_STORM && !PlayerOne.DEAD && !PlayerTwo.DEAD && !PlayerOne.LEVEL_COMPLETED && !PlayerTwo.LEVEL_COMPLETED &&
		    game.getStateID()!=GameStateID.GAME_OVER && game.getStateID()!= GameStateID.LEVEL_COMPLETED && game.getStateID()!=GameStateID.GAME_MENU) {
			addRandomBlur(true);
			interval++;
			if (interval == GameSettings.SAND_SPAWN_DELAY && GameSettings.SAND_AMOUNT>1) {
				for (int i = 0; i < GameSettings.SAND_AMOUNT; i++) {
					game.getDebrisManager().addParticle(new RainStorm(game, GameLevelImage.jungle_rain, 5, x, y));
				}
				interval = 0;
			}
			else if (interval == GameSettings.SAND_SPAWN_DELAY){
				game.getDebrisManager().addParticle(new RainStorm(game, GameLevelImage.jungle_rain, 5, x, y));
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
