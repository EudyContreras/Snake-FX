package com.SnakeGame.Particles;

import com.SnakeGame.FrameWork.GameLoader;
import com.SnakeGame.FrameWork.Settings;
import com.SnakeGame.FrameWork.SnakeGame;
import com.SnakeGame.ObjectIDs.GameDebrisID;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class FruitSplashOne extends AbstractDebrisEffect {

	private GameDebrisID id;
	private Paint color;
	private double radius;
	private double decay;
	private double lifeTime = 1.0f;
	private double energyLoss = 0.9;

	public FruitSplashOne(SnakeGame game, Paint fill, double expireTime, double radius, double x, double y) {
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

	public FruitSplashOne(SnakeGame game, Image image, double expireTime, double radius, double x, double y) {
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