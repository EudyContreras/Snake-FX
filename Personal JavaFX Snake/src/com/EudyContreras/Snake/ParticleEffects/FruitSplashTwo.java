package com.EudyContreras.Snake.ParticleEffects;

import java.util.Random;

import com.EudyContreras.Snake.AbstractModels.AbstractParticlesEffect;
import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.FrameWork.GameSettings;
import com.EudyContreras.Snake.Identifiers.GameDebrisID;

import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class FruitSplashTwo extends AbstractParticlesEffect {

	private GameDebrisID id;
	private Random rand = new Random();
	private Paint color;
	private double radius;
	private double decay;
	private float lifeTime = 1.0f;

	public FruitSplashTwo(GameManager game, Paint fill, double expireTime, double radius, float x, float y) {
		this.game = game;
		this.radius = radius / 2;
		this.shape = new Circle();
		this.shape.setRadius(this.radius);
		this.shape.setFill(color);
		this.decay = 0.016 / expireTime;
		this.color = fill;
		this.velX = rand.nextInt(15 - -15 + 1) + -15;
		this.velY = rand.nextInt(15 - -15 + 1) + -15;
		this.x = x;
		this.y = y;
		init();
	}

	public void init() {
		layer = game.getInnerParticleLayer();
		addToLayer(shape);
	}
	public void updateUI() {
		shape.setCenterX(x);
		shape.setCenterY(y);
		shape.setOpacity(lifeTime);

	}
	public void move() {
		x = x + velX;
		y = y + velY;
		lifeTime -= decay;
	}

	public void collide() {
	}

	public boolean isAlive() {

		return x < GameSettings.WIDTH && x > 0 && y < GameSettings.HEIGHT && y > 0 && lifeTime > 0;
	}

	public void draw() {


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
