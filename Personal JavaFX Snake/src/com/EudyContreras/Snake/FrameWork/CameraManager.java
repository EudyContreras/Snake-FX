package com.EudyContreras.Snake.FrameWork;

import com.EudyContreras.Snake.AbstractModels.AbstractObject;
import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Application.GameSettings;

import javafx.scene.image.ImageView;

/**
 * Class used to translate a given object across the game world allowing the
 * object to move towards different directions and keeping the object on the
 * center of the screen at all times.
 *
 * @author Eudy Contreras
 *
 */

public class CameraManager {
	private float x;
	private float y;
	private GameManager game;

	/**
	 * Main constructor which takes an instance of the game class along with the
	 * coordinates to be used
	 * 
	 * @param game:Main game class
	 * @param x: x coordinate
	 * @param y: y coordinate
	 */
	public CameraManager(GameManager game, float x, float y) {
		this.x = x;
		this.y = y;
		this.game = game;
	}

	/**
	 * Method used to translate the player x and y coordinates
	 * 
	 * @param player
	 */
	public void followPlayer(AbstractObject player) {
		y = (float) (-player.getY() + GameSettings.HEIGHT - 300);
		x = (float) (-player.getX() + GameSettings.WIDTH - 600);
	}

	/**
	 * Method used to scroll the map according to player movement.
	 * 
	 * @param player
	 */
	public void setScroller(ImageView player) {
		player.translateXProperty().addListener((v, oldValue, newValue) -> {
			int offSet = newValue.intValue();
			if (offSet > 100 && offSet < game.getLevelLenght() - 1900) {
				game.getMainRoot().setTranslateX(-(offSet - 100));
			}
		});
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

}
