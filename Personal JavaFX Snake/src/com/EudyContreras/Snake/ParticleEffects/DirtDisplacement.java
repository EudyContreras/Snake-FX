package com.EudyContreras.Snake.ParticleEffects;

import com.EudyContreras.Snake.AbstractModels.AbstractParticlesEffect;
import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Application.GameSettings;
import com.EudyContreras.Snake.Identifiers.GameDebrisID;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;

public class DirtDisplacement extends AbstractParticlesEffect {

	private GameDebrisID id;
	private double radius = Math.random() * (3.5 - 1 + 1) + 1;
	private double decay;
	private double lifeTime = 1.0f;
	private double energyLoss = 0.9;

	public DirtDisplacement(GameManager game, Image image,double expireTime, double x, double y, Point2D velocity) {
		this.game = game;
		this.imagePattern = new ImagePattern(image);
		this.view = new ImageView(image);
		this.view.setFitWidth(radius*2);
		this.view.setFitHeight(radius*2);
		this.decay = 0.026/expireTime;
		this.velX = (double) velocity.getX()*0.8;
		this.velY = (double) velocity.getY()*0.8;
		this.layer = game.getDebrisLayer();
		this.x = x;
		this.y = y;
		init();
	}
	public DirtDisplacement(GameManager game,Pane layer, Image image,double expireTime, double x, double y, Point2D velocity) {
		this.game = game;
		this.imagePattern = new ImagePattern(image);
		this.view = new ImageView(image);
		this.view.setFitWidth(radius*2);
		this.view.setFitHeight(radius*2);
		this.decay = 0.026/expireTime;
		this.velX = (double) velocity.getX()*0.8;
		this.velY = (double) velocity.getY()*0.8;
		this.layer = layer;
		this.x = x;
		this.y = y;
		init();
	}
	public void init() {
		addToLayer(view);
	}

	public void updateUI() {
		view.setTranslateX(x);
		view.setTranslateY(y);
		view.setOpacity(lifeTime);
	}

	public void move() {
		super.move();
		lifeTime -= decay;
		velX *= energyLoss;
		velY *= energyLoss;
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
