
package com.EudyContreras.Snake.GameObjects;

import com.EudyContreras.Snake.AbstractModels.AbstractTile;
import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.FrameWork.GameSettings;
import com.EudyContreras.Snake.Identifiers.GameLevelObjectID;

import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * This class represents a cactus which creates a moving
 * or wind caused waving illusion.
 * @author Eudy Contreras
 *
 */
public class DesertTrap extends AbstractTile {

	private GameManager game;
	private Rectangle bounds;
	private Rectangle2D bounds2D;

	public DesertTrap(GameManager game, float x, float y, float velX, float velR, Image image, GameLevelObjectID id) {
		super(x, y, image, id);
		if (GameSettings.SAND_STORM)
			this.velX = velX;
		this.velR = velR;
		this.game = game;
		this.view.setTranslateX(x);
		this.view.setTranslateY(y);
		this.draw();
		this.setBounds();
	}
	public void setBounds(){
		 bounds2D = new Rectangle2D(x+width*0.15, y+height*0.45, width*0.5, height*0.45);
	}
	/**
	 * Method which moves this object
	 */
	public void move() {
		super.move();

	}
	/**
	 * Methods which draws a bounding box
	 */
	public void draw() {
		drawBoundingBox();
	}
	/**
	 * Method which creates and draws a bounding box
	 * for debugging purposes
	 */
	public void drawBoundingBox() {

		if (GameSettings.DEBUG_MODE) {
			bounds = new Rectangle(x+width*0.15, y+height*0.45, width*0.5, height*0.45);
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
		return bounds2D;
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
