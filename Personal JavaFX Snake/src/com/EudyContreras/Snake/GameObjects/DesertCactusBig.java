
package com.EudyContreras.Snake.GameObjects;

import com.EudyContreras.Snake.AbstractModels.AbstractTile;
import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Application.GameSettings;
import com.EudyContreras.Snake.Identifiers.GameLevelObjectID;
import com.EudyContreras.Snake.ImageBanks.GameLevelImage;
import com.EudyContreras.Snake.Utilities.RandomGenUtility;

import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Shear;

/**
 * This class represents a cactus which creates a moving
 * or wind caused waving illusion.
 * @author Eudy Contreras
 *
 */
public class DesertCactusBig extends AbstractTile {
	private Shear shear = new Shear();
	private double waveX = 0.0;
	private double waveXVel = 0.0;
	private double maxWaveLeft = 0;
	private double maxWaveRight = 0;
	private GameManager game;

	public DesertCactusBig(float x, float y, float velR, Image image, GameLevelObjectID id) {
		super(x, y, image, id);
		if (GameSettings.SAND_STORM)
			this.velR = velR;
		this.waveXVel = 0.01;
		this.maxWaveLeft = -0.07;
		this.maxWaveRight = 0.07;
		this.view.setFitWidth(view.getImage().getWidth());
		this.view.setFitHeight(view.getImage().getHeight());
		this.view.setTranslateX(x);
		this.view.setTranslateY(y);
		this.view.setRotationAxis(Rotate.Y_AXIS);
		this.view.getTransforms().add(shear);
		if(RandomGenUtility.getRandom(1, 3) == 3){
			this.view.setImage(GameLevelImage.desert_cactus_big_alt);
			this.width = image.getWidth()+20;
			this.height = image.getHeight()+30;
			this.view.setFitWidth(width);
			this.view.setFitHeight(height);
			this.y = y-30;
		}

		this.shear.setPivotY(view.getFitHeight());
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
	 * Method which makes this object wave or rotate.
	 */
	public void wave() {
		waveX+=waveXVel;

		if(waveX>=maxWaveRight){
			maxWaveRight = RandomGenUtility.getRandom(0.05, 0.07);
			waveXVel = -RandomGenUtility.getRandom(0.008, 0.014);
		}
		if(waveX<=maxWaveLeft){
			maxWaveLeft = -RandomGenUtility.getRandom(0.05, 0.07);
			waveXVel = RandomGenUtility.getRandom(0.008, 0.014);
		}
		if (r > 4) {
			velR -= Math.random() * (0.4 - 0.01 + 1) + 0.01;
		}
		if (r <= 0) {
			if (velR < 4)
				velR += 0.5f;
		}

		shear.setX(waveX);
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
			Rectangle bounds = new Rectangle(x, y + 30, width - 30, height - 30);
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
		return new Rectangle2D(x, y + 30, width - 30, height - 30);
	}

	public Bounds getCollisionBounds() {
		return this.view.getBoundsInParent();
	}

}
