package com.SnakeGame.GameObjects;

import com.SnakeGame.Core.Settings;
import com.SnakeGame.Core.SnakeGame;
import com.SnakeGame.ObjectIDs.LevelObjectID;
import com.SnakeGame.Utilities.GameTileManager;

import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Every static object or esthetic object in the game such as walls, boxes etc
 * is considered a tile. This class is the main tile class and can be used for
 * creating any level object.
 * 
 * @author Eudy Contreras
 *
 */
public class DesertBounds extends Tile {
	GameTileManager tileManager;
	Rectangle2D collisionBounds;;
	SnakeGame game;
	float speed;

	public DesertBounds(SnakeGame game, float x, float y, float speed, float velY, LevelObjectID id) {
		super(x, y);
		this.game = game;
		this.velX = 0;
		this.speed = speed;
		this.velY = velY;
		this.view.setTranslateX(x);
		this.view.setTranslateY(y);
		draw();
		adjustBounds();
	}

	public DesertBounds(SnakeGame game, float x, float y, float velX, float velY, Image image) {
		super(x, y, image);
		this.game = game;
		this.velX = velX;
		this.velY = velY;
		this.view.setTranslateX(x);
		this.view.setTranslateY(y);
		draw();
		adjustBounds();
	}

	public void adjustBounds() {
		if (this.id == LevelObjectID.rock) {
			collisionBounds = new Rectangle2D(x, y + 30, width - 30, height - 30);
		}
	}

	public void move() {
		x = x + velX;

	}

	public void draw() {
		drawBoundingBox();
	}

	public void drawBoundingBox() {

		if (Settings.DEBUG_MODE) {
			Rectangle bounds = new Rectangle(x, y + 30, width - 30, height - 30);
			bounds.setStroke(Color.BLACK);
			bounds.setFill(Color.TRANSPARENT);
			game.getOverlay().getChildren().add(bounds);

		}

	}

	public Rectangle2D getBounds() {
		return collisionBounds;
	}

	public Rectangle2D getBoundsTop() {
		return new Rectangle2D(x + 20, y, width - 40, height);
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
