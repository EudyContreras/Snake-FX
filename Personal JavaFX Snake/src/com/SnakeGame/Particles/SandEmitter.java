package com.SnakeGame.Particles;

import java.util.Random;

import com.SnakeGame.FrameWork.Settings;
import com.SnakeGame.FrameWork.SnakeGame;
import com.SnakeGame.ImageBanks.GameLevelImage;
import com.SnakeGame.PlayerOne.PlayerOne;
import com.SnakeGame.PlayerTwo.PlayerTwo;
import com.SnakeGame.Utilities.ScreenOverlay;

import javafx.scene.shape.Circle;

/**
 * This class is used to keep track of energy used by the player if there's is
 * any it will decrease and regenerate according to time passed and given
 * actions by the player.
 *
 * @author Eudy Contreras
 *
 */
public class SandEmitter {

	boolean goUp = false;
	boolean goDown = true;
	boolean blur = false;
	double maxEnergyLevel = 100;
	double x = 0;
	double y = 0;
	double velY;
	double width = 0;
	double height = 0;
	int interval = 0;
	int spawnDelay = 0;
	PlayerOne player;
	Circle emitter;
	SnakeGame game;
	Random rand;
	ScreenOverlay overlay;

	public SandEmitter(SnakeGame game, double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.velY = 5;
		this.width = width;
		this.height = height;
		this.game = game;
		this.rand = new Random();
		this.overlay = new ScreenOverlay(game, game.getGameRoot());
//	    this.overlay.addToneOverlay(Color.rgb(255, 150, 0,1), 20, 1);
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
					game.getDebrisManager().addParticle(new SandStorms(game, GameLevelImage.sandGrain, 5, x, y));
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