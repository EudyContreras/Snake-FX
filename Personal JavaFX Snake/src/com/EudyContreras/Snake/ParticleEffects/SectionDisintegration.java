package com.EudyContreras.Snake.ParticleEffects;

import com.EudyContreras.Snake.AbstractModels.AbstractParticlesEffect;
import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.FrameWork.GameSettings;
import com.EudyContreras.Snake.Identifiers.GameDebrisID;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class SectionDisintegration extends AbstractParticlesEffect {

	private GameDebrisID id;
	private double decay;
	private double lifeTime = 1.0f;

	public SectionDisintegration(GameManager game, Image image, double expireTime, double radius, double x, double y) {
		this.game = game;
		this.shape = new Circle(radius, x, y);
		this.imagePattern = new ImagePattern(image);
		this.shape.setRadius(radius/2);
		this.decay = 0.016 / expireTime;
		this.x = x;
		this.y = y;
		this.velX = (Math.random() * (2 - -2 + 1) + -2)/GameManager.ScaleX;
		this.velY = (Math.random() * (2 - -2 + 1) + -2)/GameManager.ScaleY;
		init();
	}

	public void init() {
		shape.setFill(imagePattern);
		game.getOuterParticleLayer().getChildren().add(shape);
	}

	public void updateUI() {
		shape.setCenterX(x);
		shape.setCenterY(y);
		shape.setOpacity(lifeTime);
	}

	public void move() {
		super.move();
		lifeTime -= decay;
		velX += 0.05/GameManager.ScaleX;
		velY -= 0.002/GameManager.ScaleY;
	}

	public void collide() {
	}

	public boolean isAlive() {

		return x < GameSettings.WIDTH && x > 0 && y < GameSettings.HEIGHT && y > 0 && lifeTime > 0;
	}

	public void draw(GraphicsContext gc) {

	}

	public Rectangle2D getBounds() {

		return null;
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
