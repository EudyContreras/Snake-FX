
package com.EudyContreras.Snake.GameObjects;

import com.EudyContreras.Snake.AbstractModels.AbstractTile;
import com.EudyContreras.Snake.Application.GameManager;

import javafx.scene.image.Image;

/**
 * Class used by the level culler. The culler uses this class by placing this
 * class at specific checkpoints and and loading another area of the game once
 * this checkpoint is reached
 *
 * @author Eudy Contreras
 *
 */
public class Checkpoint extends AbstractTile {
	boolean loadFirstSect = false;
	boolean loadSecondSect = false;
	boolean loadThirdSect = false;
	boolean loadFourthSect = false;
	boolean loadFithSect = false;
	boolean loadFirst = false;
	boolean loadSecond = false;
	boolean loadThird = false;
	boolean loadFourth = false;
	boolean loadFith = false;
	/**
	 * Constructor which determines the position or velocity of this checkpoint.
	 * the checkpoint may also be a visible object
	 * @param game
	 * @param x
	 * @param y
	 * @param velX
	 * @param velY
	 * @param image
	 */
	public Checkpoint(GameManager game, float x, float y, float velX, float velY, Image image) {
		super(x, y, image);
		this.velX = velX;
		this.velY = velY;
		this.view.setTranslateX(x);
		this.view.setTranslateY(y);
	}
	public void Action(){

	}

}
