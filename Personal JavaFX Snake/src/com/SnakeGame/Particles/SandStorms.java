package com.SnakeGame.Particles;

import com.SnakeGame.FrameWork.GameLoader;
import com.SnakeGame.FrameWork.Settings;
import com.SnakeGame.FrameWork.SnakeGame;
import com.SnakeGame.ObjectIDs.GameDebrisID;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class SandStorms extends DebrisEffect {

	GameDebrisID id;
	Paint color;
	double radius;
	double decay;
	double lifeTime = 4.0f;
	double width = Math.random() * (6 - 2.5 + 1) + 2.5;
	double height = Math.random() * (6 - 2.5 + 1) + 2.5;
	boolean isAlive = false;
	boolean removable = false;
	int depth = 400;
	int amount = 200;
	double expireTime = Math.random() * (1 - 0.01 + 1) + 0.01;

	public SandStorms(SnakeGame game, Image image, double expireTime, double radius, double x, double y) {
		this.game = game;
		this.radius = radius / 2;
		this.shape = new Circle(radius, x, y);
		this.imagePattern = new ImagePattern(image);
		this.shape.setRadius(this.radius);
		this.decay = 0.016 / expireTime;
		this.x = x;
		this.y = y;
		this.velX = Math.random() * (8 - 2 + 1) + 2 / (GameLoader.ResolutionScaleX + GameLoader.ResolutionScaleY / 2);
		this.velY = Math.random() * (8 - -5 + 1) + -5 / (GameLoader.ResolutionScaleX + GameLoader.ResolutionScaleY / 2);
		init();
	}

	public SandStorms(SnakeGame game, Image image, double expireTime, double x, double y) {
		this.game = game;
		this.view = new ImageView(image);
		this.view.setFitWidth(width);
		this.view.setFitHeight(height);
		this.decay = 0.016 / this.expireTime;
		this.x = x;
		this.y = y;
		this.velX = Math.random() * (8 - 2 + 1) + 2 / (GameLoader.ResolutionScaleX + GameLoader.ResolutionScaleY / 2);
		this.velY = Math.random() * (8 - -5 + 1) + -5 / (GameLoader.ResolutionScaleX + GameLoader.ResolutionScaleY / 2);
		init();
	}

	public void init() {
		if (shape != null) {
			shape.setFill(imagePattern);
			game.getParticleLayer().getChildren().add(shape);
		}
		if (view != null) {
			game.getParticleLayer().getChildren().add(view);
		}
	}

	public void update() {
		super.move();
		lifeTime -= decay;
		velX += Settings.WIND_SPEED / (GameLoader.ResolutionScaleX + GameLoader.ResolutionScaleY / 2);
		velY -= 0.002;
	}

	public void move() {
		if (shape != null) {
			shape.setCenterX(x);
			shape.setCenterY(y);
			shape.setOpacity(lifeTime);
		}
		if (view != null) {
			view.setTranslateX(x);
			view.setTranslateY(y);
			// view.setOpacity(lifeTime);
		}

	}

	public void collide() {
	}

	public boolean isAlive() {

		return x < Settings.WIDTH && y < Settings.HEIGHT && y > 0 && lifeTime > 0;
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
