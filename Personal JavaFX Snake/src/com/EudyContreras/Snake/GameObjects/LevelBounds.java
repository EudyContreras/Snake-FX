package com.EudyContreras.Snake.GameObjects;

import com.EudyContreras.Snake.AbstractModels.AbstractTile;
import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Application.GameSettings;
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
	public static int MAX_Y = 100;
	public static int MIN_Y = 100;
	public static int MIN_X = 0;
	public static int MAX_X = 0;
	private Rectangle2D collisionBounds;;
	private GameManager game;
	private ImageView leftBound;
	private ImageView rightBound;
	private ImageView topBoundV;
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
		this.topBoundV = new ImageView(GameImageBank.hud_bar_black);

		this.setDimensions_h(bottomBound);
		this.setDimensions_v(leftBound);
		this.setDimensions_v(rightBound);

		this.leftBound.setX(0);
		this.rightBound.setX(GameSettings.WIDTH - 25);
		this.topBound.setX(-3);
		this.topBound.setY(topBound.getFitHeight()+25);
		this.topBoundV.setX(-3);
		this.topBoundV.setY(0);
		this.bottomBound.setX(0);
		this.bottomBound.setY(GameSettings.HEIGHT-25);


		this.displayBounds();
		this.setBoundaries();
	}

	public void setBoundaries(){
		MAX_Y = (int) bottomBound.getY();
		MIN_Y = (int) (topBound.getY()+topBound.getImage().getHeight());
		MAX_X = (int) rightBound.getX();
		MIN_X = (int) (leftBound.getX()+leftBound.getFitWidth());
	}

	public void setDimensions_h(ImageView view){
		view.setFitWidth(GameSettings.WIDTH);
		view.setFitHeight(25);
	}

	public void setDimensions_v(ImageView view){
		view.setFitWidth(25);
		view.setFitHeight(GameSettings.HEIGHT);
	}

	public void displayBounds(){
		this.layer.getChildren().add(leftBound);
		this.layer.getChildren().add(rightBound);
		this.layer.getChildren().add(topBound);
		this.layer.getChildren().add(bottomBound);
		this.game.getEleventhLayer().getChildren().add(topBoundV);
	}

	public void showBounds(boolean state){
		this.leftBound.setVisible(state);
		this.rightBound.setVisible(state);
		this.topBound.setVisible(state);
		this.bottomBound.setVisible(state);
		this.topBoundV.setVisible(state);
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
