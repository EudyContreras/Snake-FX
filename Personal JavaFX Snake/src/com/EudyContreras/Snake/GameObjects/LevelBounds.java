package com.EudyContreras.Snake.GameObjects;

import com.EudyContreras.Snake.AbstractModels.AbstractTile;
import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.FrameWork.GameSettings;
import com.EudyContreras.Snake.ImageBanks.GameImageBank;

import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Bounds class which can be used for adding specific events to objects
 * that surround the level
 *
 * @author Eudy Contreras
 *
 */
public class LevelBounds extends AbstractTile {
	private Rectangle2D collisionBounds;;
	private GameManager game;
	private ImageView leftBound;
	private ImageView rightBound;
	private ImageView topBound;
	private ImageView bottomBound;
	private Pane layer;

	public LevelBounds(GameManager game, Pane layer) {
		this.game = game;
		this.velX = 0;
		this.view.setTranslateX(x);
		this.view.setTranslateY(y);
		this.layer = layer;
		setUpBounds();
		draw();
	}

	public LevelBounds(GameManager game, float x, float y, float velX, float velY, Image image) {
		super(x, y, image);
		this.game = game;
		this.velX = velX;
		this.velY = velY;
		this.view.setTranslateX(x);
		this.view.setTranslateY(y);
		draw();
	}

	public void setUpBounds(){
		this.leftBound = new ImageView(GameImageBank.clipping_bar_v);
		this.rightBound = new ImageView(GameImageBank.clipping_bar_v);
		this.topBound = new ImageView(GameImageBank.hud_bar_orange);
		this.bottomBound = new ImageView(GameImageBank.clipping_bar_h);
		this.setDimensions_h(bottomBound);
		this.setDimensions_v(leftBound);
		this.setDimensions_v(rightBound);
		this.setDimensions_h(topBound);
		this.leftBound.setX(-GameManager.ScaleX(85));
		this.rightBound.setX(GameSettings.WIDTH - rightBound.getFitWidth()+GameManager.ScaleX(85));
		this.topBound.setX(-GameManager.ScaleX(65));
		this.topBound.setY(topBound.getFitHeight()-GameManager.ScaleY(100));
		this.bottomBound.setX(-GameManager.ScaleX(65));
		this.bottomBound.setY(GameSettings.HEIGHT - bottomBound.getFitHeight()+GameManager.ScaleY(85));
		this.displayBounds();
	}
	public void setDimensions_h(ImageView view){
		view.setFitWidth(GameManager.ScaleX(view.getImage().getWidth())+GameManager.ScaleY(30));
		view.setFitHeight(GameManager.ScaleY(view.getImage().getHeight()));
	}
	public void setDimensions_v(ImageView view){
		view.setFitWidth(GameManager.ScaleX(view.getImage().getWidth()));
		view.setFitHeight(GameManager.ScaleY(view.getImage().getHeight()));
	}
	public void displayBounds(){
		this.layer.getChildren().add(leftBound);
		this.layer.getChildren().add(rightBound);
		this.layer.getChildren().add(topBound);
		this.layer.getChildren().add(bottomBound);
	}
	public void showBounds(boolean state){
		this.leftBound.setVisible(state);
		this.rightBound.setVisible(state);
		this.topBound.setVisible(state);
		this.bottomBound.setVisible(state);
	}
	public void move() {


	}

	public void draw() {
		drawBoundingBox();
	}

	public void drawBoundingBox() {

		if (GameSettings.DEBUG_MODE) {
			Rectangle bounds = new Rectangle(x, y + 30, width - 30, height - 30);
			bounds.setStroke(Color.BLACK);
			bounds.setFill(Color.TRANSPARENT);
			game.getSeventhLayer().getChildren().add(bounds);

		}

	}

	public Rectangle2D getBounds() {
		return collisionBounds;
	}

	public Rectangle2D getBoundsTop() {
		return new Rectangle2D(topBound.getX(), topBound.getY(), topBound.getFitWidth(), topBound.getFitHeight());
	}

	public Rectangle2D getBoundsRight() {
		return new Rectangle2D(x + width - 20, y + 10, 20, height - 10);
	}

	public Rectangle2D getBoundsLeft() {
		return new Rectangle2D(x, y + 10, 20, height - 10);
	}

	public Bounds getCollisionBounds() {
		return this.view.getBoundsInParent();
	}

}
