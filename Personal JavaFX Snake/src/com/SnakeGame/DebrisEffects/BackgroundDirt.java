package com.SnakeGame.DebrisEffects;

import com.SnakeGame.AbstractModels.AbstractDebrisEffect;
import com.SnakeGame.AbstractModels.AbstractTile;
import com.SnakeGame.EnumIDs.GameDebrisID;
import com.SnakeGame.FrameWork.GameLoader;
import com.SnakeGame.FrameWork.GameManager;
import com.SnakeGame.FrameWork.GameSettings;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class BackgroundDirt extends AbstractDebrisEffect {

	private GameDebrisID id;
	private double radius = Math.random() * (3.5 - 1 + 1) + 1 / (GameLoader.ResolutionScaleX);
	private double decay;
	private double lifeTime = 1.0f;
	private double energyLoss = 0.9;
	private Pane layer;

	public BackgroundDirt(GameManager game, Pane layer, Image image,double expireTime, double x, double y, Point2D velocity) {
		this.game = game;
		this.imagePattern = new ImagePattern(image);
		this.shape = new Circle(x, y, radius);
		this.shape.setRadius(radius);
		this.shape.setFill(imagePattern);
		this.decay = 0.026/expireTime;
		this.layer = layer;
		this.velX = (double) velocity.getX() / (GameLoader.ResolutionScaleX);
		this.velY = (double) velocity.getY() / (GameLoader.ResolutionScaleX);
		this.x = x;
		this.y = y;
		this.init();
	}
	public void init() {
		layer.getChildren().add(shape);
	}

	public void update() {
		shape.setCenterX(x);
		shape.setCenterY(y);
		lifeTime -= decay;
		velX *= energyLoss;
		velY *= energyLoss;
	}

	public void move() {
		super.move();

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

	public void draw(GraphicsContext gc) {

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
