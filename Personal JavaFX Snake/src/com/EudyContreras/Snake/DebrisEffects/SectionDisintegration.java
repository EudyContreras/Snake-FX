package com.EudyContreras.Snake.DebrisEffects;

import com.EudyContreras.Snake.AbstractModels.AbstractDebrisEffect;
import com.EudyContreras.Snake.EnumIDs.GameDebrisID;
import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.FrameWork.GameSettings;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SectionDisintegration extends AbstractDebrisEffect {

	private GameDebrisID id;
	private double decay;
	private double lifeTime = 1.0f;

//	public SectionDisintegration(GameManager game, Image image, double expireTime, double radius, double x, double y) {
//		this.game = game;
//		this.shape = new Circle(radius, x, y);
//		this.imagePattern = new ImagePattern(image);
//		this.shape.setRadius(radius/2);
//		this.decay = 0.016 / expireTime;
//		this.x = x;
//		this.y = y;
//		this.velX = (Math.random() * (2 - -2 + 1) + -2)/GameManager.ScaleX;
//		this.velY = (Math.random() * (2 - -2 + 1) + -2)/GameManager.ScaleY;
//		init();
//	}
	public SectionDisintegration(GameManager game, Image image, double expireTime, double radius, double x, double y) {
		this.game = game;
		this.view = new ImageView(image);
		this.view.setFitWidth(radius);
		this.view.setFitHeight(radius);
		this.view.setTranslateX(x);
		this.view.setTranslateY(y);
		this.decay = 0.016 / expireTime;
		this.x = x;
		this.y = y;
		this.velX = (Math.random() * (2 - -2 + 1) + -2)/GameManager.ScaleX;
		this.velY = (Math.random() * (2 - -2 + 1) + -2)/GameManager.ScaleY;
		initView();
	}
	public void initShape() {
		shape.setFill(imagePattern);
		game.getOuterParticleLayer().getChildren().add(shape);

	}
	public void initView() {
		game.getOuterParticleLayer().getChildren().add(view);

	}
	public void move() {
		super.move();
		lifeTime -= decay;
		velX += 0.05/GameManager.ScaleX;
		velY -= 0.002/GameManager.ScaleY;
	}

	public void updateUI() {
		if(shape!=null){
			shape.setCenterX(x);
			shape.setCenterY(y);
			shape.setOpacity(lifeTime);
		}
		if(view!=null){
			view.setTranslateX(x);
			view.setTranslateY(y);
			view.setOpacity(lifeTime);
		}
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
