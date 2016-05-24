package com.SnakeGame.Particles;

import java.util.Random;

import com.SnakeGame.FrameWork.Settings;
import com.SnakeGame.FrameWork.SnakeGame;
import com.SnakeGame.IDenums.GameStateID;
import com.SnakeGame.ImageBanks.GameLevelImage;
import com.SnakeGame.PlayerOne.PlayerOne;
import com.SnakeGame.PlayerTwo.PlayerTwo;
import com.SnakeGame.Utilities.ScreenOverlay;

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
	private SnakeGame game;
	private Random rand;
	private ScreenOverlay overlay;

	public RainEmitter(SnakeGame game, double x, double y,  double speed, double width, double height) {
		this.x = x;
		this.y = y;
		this.velX = 5;
		this.speed = speed;
		this.game = game;
		this.rand = new Random();
		this.overlay = new ScreenOverlay(game, game.getGameRoot());
	}

	public void move() {
		this.overlay.updateEffect();
		x += velX;
		if (x >= Settings.WIDTH) {
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
		if (Settings.RAIN_STORM && !PlayerOne.DEAD && !PlayerTwo.DEAD && !PlayerOne.LEVEL_COMPLETED && !PlayerTwo.LEVEL_COMPLETED && game.getStateID()!=GameStateID.GAME_MENU) {
			addRandomBlur(true);
			interval++;
			if (interval == Settings.SAND_SPAWN_DELAY) {
				for (int i = 0; i < Settings.SAND_AMOUNT; i++) {
					game.getDebrisManager().addParticle(new RainStorm(game, GameLevelImage.jungle_rain, 5, x, y));
				}
				interval = 0;
			}
		}
	}

	public void addRandomBlur(boolean random) {
		if (random && rand.nextInt(Settings.BLUR_RANDOMNESS) != 0) {
			return;
		}
		if (!PlayerOne.LEVEL_COMPLETED && !PlayerTwo.LEVEL_COMPLETED)
			this.overlay.addStormBlur();

	}

}
