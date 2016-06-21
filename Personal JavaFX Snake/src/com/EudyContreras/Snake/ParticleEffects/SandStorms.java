package com.EudyContreras.Snake.ParticleEffects;

import com.EudyContreras.Snake.AbstractModels.AbstractParticlesEffect;
import com.EudyContreras.Snake.FrameWork.GameLoader;
import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.FrameWork.GameSettings;
import com.EudyContreras.Snake.Identifiers.GameDebrisID;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class SandStorms extends AbstractParticlesEffect {

	private GameDebrisID id;
	private double dropVel;
	private double radius;
	private double decay;
	private double lifeTime = 6.0f;
	private double width = GameSettings.SAND_SIZE/GameLoader.ResolutionScaleX;
	private double height = GameSettings.SAND_SIZE/GameLoader.ResolutionScaleY;
	private double expireTime = Math.random() * (1 - 0.01 + 1) + 0.01;

	public SandStorms(GameManager game, Image image, double expireTime, double radius, double x, double y) {
		this.game = game;
		this.radius = GameManager.ScaleX_Y((radius / 2));
		this.shape = new Circle(radius, x, y);
		this.imagePattern = new ImagePattern(image);
		this.shape.setRadius(this.radius);
		this.decay = 0.016 / expireTime;
		this.x = x;
		this.y = y;
		this.velX = GameManager.ScaleX(Math.random() * (8 - 2 + 1) + 2 );
		this.velY = GameManager.ScaleY(Math.random() * (8 - -5 + 1) + -5);
		this.dropVel =GameManager.ScaleX_Y( Math.random() * (0.003 - 0.001) + 0.001 );
		init();
	}

	public SandStorms(GameManager game, Image image, double expireTime, double x, double y) {
		this.game = game;
		this.setView(new ImageView(image));
		this.view.setFitWidth(width);
		this.view.setFitHeight(height);
		this.decay = 0.016 / this.expireTime;
		this.x = x;
		this.y = y;
		this.velX = GameManager.ScaleX(Math.random() * (8 - 2 + 1) + 2 );
		this.velY = GameManager.ScaleY(Math.random() * (8 - -5 + 1) + -5);
		this.dropVel =GameManager.ScaleX_Y( Math.random() * (0.003 - 0.001) + 0.001 );
		init();
	}

	public void init() {
		if (shape != null) {
			layer = game.getOuterParticleLayer();
			addToLayer(shape);
		}
		if (view != null) {
			layer = game.getOuterParticleLayer();
			addToLayer(view);
		}
	}

	public void updateUI() {
		if (shape != null) {
			shape.setCenterX(x);
			shape.setCenterY(y);
			shape.setOpacity(lifeTime);
		}
		if (view != null) {
			view.setTranslateX(x);
			view.setTranslateY(y);
		}

	}
	public void move() {
		super.move();
		lifeTime -= decay;
		velY -= dropVel;
	}
	public void collide() {
	}

	public boolean isAlive() {

		return x < GameSettings.WIDTH && y < GameSettings.HEIGHT && y > 0 && lifeTime > 0;
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
