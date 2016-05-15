
package com.SnakeGame.FrameWork;

import com.SnakeGame.GameObjects.Tile;

import javafx.scene.image.Image;

/**
 * Class used by the level culler. The culler uses this class by placing this
 * class at specific checkpoints and and loading another area of the game once
 * this checkpoint is reached
 *
 * @author Eudy Contreras
 *
 */
public class GameLevelCheckpoint extends Tile {
	SnakeGame game;
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

	public GameLevelCheckpoint(SnakeGame game, float x, float y, float velX, float velY, Image image) {
		super(x, y, image);
		this.game = game;
		this.velX = velX;
		this.velY = velY;
		this.view.setTranslateX(x);
		this.view.setTranslateY(y);
	}
	public void Action(){

	}

}
