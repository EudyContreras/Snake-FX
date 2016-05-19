package com.SnakeGame.Particles;

import java.util.Random;

import com.SnakeGame.FrameWork.Settings;
import com.SnakeGame.FrameWork.SnakeGame;
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
public class SandEmitter {

	private boolean goUp = false;
	private boolean goDown = true;
	private double x = 0;
	private double y = 0;
	private double velY;
	private int interval = 0;
	private SnakeGame game;
	private Random rand;
	private ScreenOverlay overlay;

	public SandEmitter(SnakeGame game, double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.velY = 5;
		this.game = game;
		this.rand = new Random();
		this.overlay = new ScreenOverlay(game, game.getGameRoot());
	}

	public void move() {
		this.overlay.updateEffect();
		y += velY;
		if (y >= Settings.HEIGHT) {
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
		if (Settings.SAND_STORM && !PlayerOne.DEAD && !PlayerTwo.DEAD) {
			addRandomBlur(true);
			interval++;
			if (interval == Settings.SAND_SPAWN_DELAY) {
				for (int i = 0; i < Settings.SAND_AMOUNT; i++) {
					game.getDebrisManager().addParticle(new SandStorms(game, GameLevelImage.desert_sand, 5, x, y));
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
