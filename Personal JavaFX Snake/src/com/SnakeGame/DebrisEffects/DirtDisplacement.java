package com.SnakeGame.DebrisEffects;

import com.SnakeGame.AbstractModels.AbstractDebrisEffect;
import com.SnakeGame.FrameWork.GameLoader;
import com.SnakeGame.FrameWork.GameManager;
import com.SnakeGame.FrameWork.GameSettings;
import com.SnakeGame.IDEnums.GameDebrisID;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class DirtDisplacement extends AbstractDebrisEffect {

	private GameDebrisID id;
	private double radius = Math.random() * (3.5 - 1 + 1) + 1 / (GameLoader.ResolutionScaleX);
	private double decay;
	private double lifeTime = 1.0f;
	private double energyLoss = 0.9;
	Point2D velocity = new Point2D((Math.random() * (10 - -10 + 1) + -10), Math.random() * (10 - -10 + 1) + -10);

	public DirtDisplacement(GameManager game, Image image,double expireTime, double x, double y, Point2D velocity) {
		this.game = game;
		this.imagePattern = new ImagePattern(image);
		this.shape = new Circle(x, y, radius);
		this.shape.setRadius(radius/ (GameLoader.ResolutionScaleX + GameLoader.ResolutionScaleY / 2));
		this.shape.setFill(imagePattern);
		this.velocity = velocity;
		this.decay = 0.026/expireTime;
		this.velX = (double) velocity.getX() / (GameLoader.ResolutionScaleX)*0.8;
		this.velY = (double) velocity.getY() / (GameLoader.ResolutionScaleX)*0.8;
		this.x = x;
		this.y = y;
		init();
	}

	public void init() {
		game.getDebrisLayer().getChildren().add(shape);
	}

	public void update() {
		shape.setCenterX(x);
		shape.setCenterY(y);
		shape.setOpacity(lifeTime);
		lifeTime -= decay;
		velX *= energyLoss;
		velY *= energyLoss;
	}

	public void move() {
		super.move();
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