package com.SnakeGame.Core;

import java.util.Random;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class SectionDisintegration extends DebrisEffect {

	GameDebrisID id;
	Random rand = new Random();
	Paint color;
	double radius;
	double decay;
	double lifeTime = 1.0f;
	double width;
	double height;
	boolean isAlive = false;
	boolean removable = false;
	int depth = 400;
	int amount = 200;
	double greenRange = Math.random() * (200 - 65 + 1) + 65;

	public SectionDisintegration(SnakeGame game, Image image, double expireTime, double radius, double x, double y) {
		this.game = game;
		this.radius = radius / 2;
		this.shape = new Circle(radius,x,y);
		this.imagePattern = new ImagePattern(image);
		this.shape.setRadius(this.radius);
		this.decay = 0.016 / expireTime;
		this.x = x;
		this.y = y;
		this.velX = Math.random() * (2 - -2 + 1) + -2 / (GameLoader.ResolutionScaleX + GameLoader.ResolutionScaleY / 2);
		this.velY = Math.random() * (2 - -2 + 1) + -2 / (GameLoader.ResolutionScaleX + GameLoader.ResolutionScaleY / 2);
		init();
	}

	public void init() {
		shape.setFill(imagePattern);
		game.getParticleLayer().getChildren().add(shape);
	}

	public void update() {
		super.move();
		lifeTime -= decay;
		velX += 0.05 / (GameLoader.ResolutionScaleX + GameLoader.ResolutionScaleY / 2);
		velY -= 0.002;
	}

	public void move() {
		shape.setCenterX(x);
		shape.setCenterY(y);
	}

	public void collide() {
	}

	public boolean isAlive() {

		return x < Settings.WIDTH && x > 0 && y < Settings.HEIGHT && y > 0 && lifeTime > 0;
	}

	public void draw(GraphicsContext gc) {

		shape.setOpacity(lifeTime);
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