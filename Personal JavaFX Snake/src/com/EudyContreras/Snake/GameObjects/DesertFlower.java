
package com.EudyContreras.Snake.GameObjects;

import com.EudyContreras.Snake.AbstractModels.AbstractTile;
import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.FrameWork.GameSettings;
import com.EudyContreras.Snake.Identifiers.GameLevelObjectID;
import com.EudyContreras.Snake.ImageBanks.GameLevelImage;
import com.EudyContreras.Snake.Utilities.RandomGenUtility;

import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
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
public class DesertFlower extends AbstractTile {

	private Rectangle2D collisionBounds;
	private GameManager game;


	public DesertFlower(GameManager game, float x, float y, float speed, float velY, Image image, GameLevelObjectID id) {
		super(x, y, image, id);
		this.game = game;
		this.velX = 0;
		this.velY = velY;
		this.view.setTranslateX(x);
		this.view.setTranslateY(y);
		int rand = RandomGenUtility.getRandomInteger(1, 3);
		if( rand == 3){
			this.view.setImage(GameLevelImage.desert_flower_alt);
		}
		else if(rand == 2){
			this.view.setImage(GameLevelImage.desert_flower_alt2);
		}
		this.adjustBounds();
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.draw();
	}

	public DesertFlower(GameManager game, float x, float y, float velX, float velY, Image image) {
		super(x, y, image);
		this.game = game;
		this.velX = velX;
		this.velY = velY;
		this.view.setTranslateX(x);
		this.view.setTranslateY(y);
		this.adjustBounds();
		this.draw();
	}

	/**
	 * Moves this object
	 */
	public void move() {
		x = x + velX;
	}
	/**
	 * Method which initializes bounds for a specific object
	 */
	public void adjustBounds() {
		collisionBounds = new Rectangle2D(x+10, y + 45, width - 65, height - 45);

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
			Rectangle bounds = new Rectangle(x+10, y + 45, width - 65, height - 45);
			bounds.setStroke(Color.WHITE);
			bounds.setFill(Color.TRANSPARENT);
			bounds.setStrokeWidth(3);
			game.getSeventhLayer().getChildren().add(bounds);

		}
	}
	/**
	 * This methods will return specified bounds of this object
	 * based on coordinates and dimensions.
	 */
	public Rectangle2D getBounds() {
		return collisionBounds;
	}

	public Bounds getCollisionBounds() {
		return this.view.getBoundsInParent();
	}

}
