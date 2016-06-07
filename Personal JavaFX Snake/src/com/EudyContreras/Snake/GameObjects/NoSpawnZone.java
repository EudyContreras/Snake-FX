
package com.EudyContreras.Snake.GameObjects;

import com.EudyContreras.Snake.AbstractModels.AbstractTile;
import com.EudyContreras.Snake.EnumIDs.GameLevelObjectID;
import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.FrameWork.GameSettings;
import com.EudyContreras.Snake.HUDElements.ScoreKeeper;

import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Every static object or esthetic object in the game such as walls, boxes etc
 * is considered a tile. This class is the main tile class and can be used for
 * creating any level object.
 *
 * @author Eudy Contreras
 *
 */
public class NoSpawnZone extends AbstractTile {
	private Rectangle2D collisionBounds;
	private Rectangle bounds;
	private GameManager game;

	public NoSpawnZone(GameManager game, float x, float y, double width, double height, GameLevelObjectID id) {
		super(x, y, id);
		this.game = game;
		this.width = width;
		this.height = height;
		adjustBounds();
		drawBounds();

	}
	/**
	 * Method which initializes bounds for a specific object
	 */
	public void adjustBounds() {
		collisionBounds = new Rectangle2D(x, y, width, height);
	}
	/**
	 * Draws a bounding box
	 */
	public void drawBounds() {
		drawBoundingBox();
	}
	/**
	 * Draws the bounding box of this object for debugging purposes
	 */
	public void drawBoundingBox() {

		bounds = new Rectangle(x, y, width, height);
		if(GameSettings.DEBUG_MODE)
		bounds.setStroke(Color.RED);
		bounds.setFill(Color.TRANSPARENT);
		bounds.setStrokeWidth(3);
		game.getSeventhLayer().getChildren().add(bounds);

	}
	public void updateUI(){
		if(ScoreKeeper.APPLE_COUNT<GameSettings.APPLE_COUNT	){
			this.setAlive(false);
			game.getDirtLayer().getChildren().remove(this.bounds);
			game.getGameLoader().getTileManager().getTile().remove(this);
		}
	}
	/**O
	 * This methods will return specified bounds of this object
	 * based on coordinates and dimensions.
	 */
	public Rectangle2D getBounds() {
		return collisionBounds;
	}
	public Rectangle getDebugBounds(){
		return bounds;
	}
	public Bounds getCollisionBounds() {
		return this.bounds.getBoundsInParent();
	}

}
