package com.SnakeGame.Core;

import java.util.Random;

import com.SnakeGame.ObjectIDs.GameDebrisID;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class FruitSplash extends DebrisEffect {

	GameDebrisID id;
	Random rand = new Random();
	Paint color;
	double radius;
	double decay;
	double lifeTime = 1.0f;
	double width;
	double height;
	double energyLoss = 0.9;
	boolean isAlive = false;
	boolean removable = false;
	int depth = 400;
	int amount = 200;
	double greenRange = Math.random() * (200 - 65 + 1) + 65;

	public FruitSplash(SnakeGame game, Paint fill, double expireTime, double radius, double x, double y) {
		this.game = game;
		this.radius = radius / 2;
		this.shape = new Circle();
		this.shape.setRadius(this.radius);
		this.decay = 0.016 / expireTime;
		this.color = fill;
		this.velX = (Math.random() * (10 - -10 + 1) + -10)
				/ (GameLoader.ResolutionScaleX + GameLoader.ResolutionScaleY / 2);
		this.velY = (Math.random() * (10 - -10 + 1) + -10)
				/ (GameLoader.ResolutionScaleX + GameLoader.ResolutionScaleY / 2);
		this.x = x;
		this.y = y;
		this.shape.setFill(color);
		init();
	}

	public FruitSplash(SnakeGame game, Image image, double expireTime, double radius, double x, double y) {
		this.game = game;
		this.radius = radius / 2;
		this.imagePattern = new ImagePattern(image);
		this.shape.setRadius(this.radius);
		this.decay = 0.016 / expireTime;
		this.x = x;
		this.y = y;
		this.shape.setFill(imagePattern);
		init();
	}

	public void init() {

		game.getDebrisLayer().getChildren().add(shape);
	}

	public void update() {
		super.move();
		lifeTime -= decay;
		// radius-=1;
		// this.shape.setRadius(this.radius);
		// if(radius<=0){
		// lifeTime =0;
		// }
	}

	public void move() {
		velX *= energyLoss;
		velY *= energyLoss;
		shape.setCenterX(x);
		shape.setCenterY(y);

	}

	public void collide() {
	}

	public boolean isAlive() {

		return x < Settings.WIDTH && x > 0 && y < Settings.HEIGHT && y > 0 && lifeTime > 0;
	}

	public void draw(GraphicsContext gc) {

		// shape.setOpacity(lifeTime);
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
