
package com.EudyContreras.Snake.GameObjects;

import com.EudyContreras.Snake.AbstractModels.AbstractTile;
import com.EudyContreras.Snake.EnumIDs.GameLevelObjectID;
import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.FrameWork.GameSettings;
import com.EudyContreras.Snake.Utilities.GameTileManager;

import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * this class is an object which will kill the player
 * if the collision bounds of the player intersect the collision
 * bounds of this class
 *
 * @author Eudy Contreras
 *
 */
public class SpikeFence extends AbstractTile {
	private Rectangle2D collisionBounds;
	private GameManager game;
	private int orientation;

	public SpikeFence(GameManager game, float x, float y, float speed, float velY, int orientation, Image image,GameLevelObjectID id) {
		super(x, y, image, id);
		this.game = game;
		this.velX = 0;
		this.velY = velY;
		this.orientation = orientation;
		this.view.setTranslateX(x);
		this.view.setTranslateY(y);
		adjustBounds();
		draw();
	}

	public SpikeFence(GameManager game, float x, float y, float velX, float velY, Image image) {
		super(x, y, image);
		this.game = game;
		this.velX = velX;
		this.velY = velY;
		this.view.setTranslateX(x);
		this.view.setTranslateY(y);
		adjustBounds();
		draw();
	}
	/**
	 * Method which initializes bounds for a specific object
	 */
	public void adjustBounds() {
		if(orientation==1)
			collisionBounds = new Rectangle2D(x, y+GameManager.ScaleY(20), width, height-GameManager.ScaleY(40));
		if(orientation==2)
			collisionBounds = new Rectangle2D(x+GameManager.ScaleX(15), y, width-GameManager.ScaleX(65), height);
	}
	/**
	 * Moves this object
	 */
	public void move() {
		x = x + velX;
	}
	/**
	 * Draws a bounding box
	 */
	public void draw() {
		drawBoundingBox();
	}
	/**
	 * Draws the bounding box of this object for debugging purposes
	 */
	public void drawBoundingBox() {

		if (GameSettings.DEBUG_MODE) {
			if(orientation == 1){
				Rectangle bounds = new Rectangle(x, y+GameManager.ScaleY(20), width, height-GameManager.ScaleY(40));
				bounds.setStroke(Color.WHITE);
				bounds.setFill(Color.TRANSPARENT);
				bounds.setStrokeWidth(3);
				game.getSeventhLayer().getChildren().add(bounds);
			}
			else if (orientation == 2){
				Rectangle bounds = new Rectangle(x+GameManager.ScaleX(15), y, width-GameManager.ScaleX(65), height);
				bounds.setStroke(Color.WHITE);
				bounds.setFill(Color.TRANSPARENT);
				bounds.setStrokeWidth(3);
				game.getSeventhLayer().getChildren().add(bounds);
			}
		}
	}

	/**
	 * This methods will return specified bounds of this object
	 * based on coordinates and dimensions.
	 */
	public Rectangle2D getBounds() {
		return collisionBounds;
	}

	public Rectangle2D getBoundsTop() {
		return new Rectangle2D(x + 20, y, width - 40, height);
	}

	public Rectangle2D getBoundsRight() {
		return new Rectangle2D(x + width - 20, y + 10, 20, height - 10);
	}

	public Rectangle2D getBoundsLeft() {
		return new Rectangle2D(x, y + 10, 20, height - 10);
	}

	public Bounds getCollisionBounds() {
		return this.view.getBoundsInParent();
	}

}
