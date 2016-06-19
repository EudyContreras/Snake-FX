package com.EudyContreras.Snake.ParticleEffects;

import com.EudyContreras.Snake.AbstractModels.AbstractParticlesEffect;
import com.EudyContreras.Snake.AbstractModels.AbstractTile;
import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.FrameWork.GameSettings;
import com.EudyContreras.Snake.Identifiers.GameDebrisID;
import com.EudyContreras.Snake.Utilities.RandomGenUtility;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class BackgroundDirt extends AbstractParticlesEffect {

	private GameDebrisID id;
	private double radius = GameManager.ScaleX_Y(RandomGenUtility.getRNG(1, 3.5));
	private double lifeTime = 1.0f;

	public BackgroundDirt(GameManager game, Pane layer, Image image,double x, double y) {
		this.game = game;
		this.imagePattern = new ImagePattern(image);
		this.shape = new Circle(x, y, radius);
		this.shape.setRadius(radius);
		this.shape.setFill(imagePattern);
		this.layer = layer;
		this.x = x;
		this.y = y;
		this.init();
	}
	public void init() {
		addToLayer(shape);
	}

	public void updateUI() {

	}

	public void move() {

	}

	public void collide() {
		for(AbstractTile block: game.getGameLoader().getTileManager().getBlock()){
			if(getBounds().intersects(block.getBounds())){
				this.layer.getChildren().remove(this.shape);
			}
		}
	}

	public boolean isAlive() {
		return x < GameSettings.WIDTH && x > 0 && y < GameSettings.HEIGHT && y > 0 && lifeTime > 0;
	}

	public void draw() {

	}

	public Rectangle2D getBounds() {

		return new Rectangle2D(x,y,radius,radius);
	}

	public Rectangle2D getBoundsTop() {

		return null;
	}

	public Rectangle2D getBoundsRight() {

		return null;
	}

	public Rectangle2D getBoundsLeft() {

		return null;
	}

	public GameDebrisID getID() {
		return id;
	}

	public void setID(GameDebrisID id) {
		this.id = id;
	}
}
