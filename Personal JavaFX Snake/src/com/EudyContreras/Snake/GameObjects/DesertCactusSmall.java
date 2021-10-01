
package com.EudyContreras.Snake.GameObjects;

import com.EudyContreras.Snake.AbstractModels.AbstractTile;
import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Application.GameSettings;
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
public class DesertCactusSmall extends AbstractTile {

	private GameManager game;
	private Rectangle bounds;
	private Rectangle2D bounds2D;
	private float oldX;

	public DesertCactusSmall(GameManager game, float x, float y, float velX, float velR, Image image, GameLevelObjectID id) {
		super(x, y, image, id);
		this.oldX = x;
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
		 bounds2D = new Rectangle2D(x+15, y+height*0.4, width*0.6, height*0.5);
	}
	/**
	 * Method which moves this object
	 */
	public void move() {
		super.move();
		if (GameSettings.SAND_STORM)
			wave();
	}
	/**
	 * Method which makes this object
	 * move or rotate
	 */
	public void wave() {
		if (x > oldX + GameSettings.WIND_FORCE) {
			velX -= Math.random() * (0.35 - 0.05 + 1) + 0.05;
		}
		if (x <= oldX) {
			if (velX < GameSettings.WIND_FORCE)
				velX += 0.4f;
		}
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
			bounds = new Rectangle(x+15, y+55, width-70, height-70);
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

	public Bounds getCollisionBounds() {
		return this.view.getBoundsInParent();
	}

}
