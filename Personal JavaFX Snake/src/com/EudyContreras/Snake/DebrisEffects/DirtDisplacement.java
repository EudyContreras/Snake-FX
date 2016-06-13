package com.EudyContreras.Snake.DebrisEffects;

import com.EudyContreras.Snake.AbstractModels.AbstractDebrisEffect;
import com.EudyContreras.Snake.EnumIDs.GameDebrisID;
import com.EudyContreras.Snake.FrameWork.GameLoader;
import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.FrameWork.GameSettings;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DirtDisplacement extends AbstractDebrisEffect {

	private GameDebrisID id;
	private double radius = Math.random() * (3.5 - 1 + 1) + 1 / (GameLoader.ResolutionScaleX);
	private double decay;
	private double lifeTime = 1.0f;
	private double energyLoss = 0.9;

//	public DirtDisplacement(GameManager game, Image image,double expireTime, double x, double y, Point2D velocity) {
//		this.game = game;
//		this.imagePattern = new ImagePattern(image);
//		this.shape = new Circle(x,y,radius);
//		this.decay = 0.026/expireTime;
//		this.velX = (double) velocity.getX() / (GameLoader.ResolutionScaleX)*0.8;
//		this.velY = (double) velocity.getY() / (GameLoader.ResolutionScaleX)*0.8;
//		this.x = x;
//		this.y = y;
//		initShape();
//	}
	public DirtDisplacement(GameManager game, Image image,double expireTime, double x, double y, Point2D velocity) {
		this.game = game;
		this.view = new ImageView(image);
		this.view.setFitWidth(radius*2);
		this.view.setFitHeight(radius*2);
		this.decay = 0.026/expireTime;
		this.velX = (double) velocity.getX() / (GameLoader.ResolutionScaleX)*0.8;
		this.velY = (double) velocity.getY() / (GameLoader.ResolutionScaleX)*0.8;
		this.x = x;
		this.y = y;
		initView();
	}
	public void initView() {
		game.getDebrisLayer().getChildren().add(view);
	}
	public void initShape() {
		shape.setFill(imagePattern);
		game.getDebrisLayer().getChildren().add(shape);
	}

	public void update() {
		if (view != null) {
			view.setTranslateX(x);
			view.setTranslateY(y);
			view.setOpacity(lifeTime);
		}
		if (shape != null) {
			shape.setCenterX(x);
			shape.setCenterY(y);
			shape.setOpacity(lifeTime);
		}
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
