
package com.SnakeGame.GameObjects;

import com.SnakeGame.FrameWork.AbstractTile;
import com.SnakeGame.FrameWork.PlayerMovement;
import com.SnakeGame.FrameWork.Settings;
import com.SnakeGame.FrameWork.SnakeGame;
import com.SnakeGame.IDenums.GameLevelObjectID;
import com.SnakeGame.Utilities.GameTileManager;

import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

/**
 * This class represents a cactus which creates a moving
 * or wind caused waving illusion.
 * @author Eudy Contreras
 *
 */
public class DesertBush extends AbstractTile {
	GameTileManager tileManager;
	SnakeGame game;
	float oldX;
	float speed;
	private double count = 60;
	private double threshold = 0;
	private boolean playerCollision = false;
	private boolean contact = true;

	public DesertBush(SnakeGame game,float x, float y, float velR, Image image, GameLevelObjectID id) {
		super(x, y, image, id);
		if (Settings.SAND_STORM)
			this.velR = velR;
		this.oldX = x;
		this.game = game;
		this.view.setTranslateX(x);
		this.view.setTranslateY(y);
		this.view.setRotationAxis(Rotate.Y_AXIS);
		this.draw();
	}
	/**
	 * Method which moves this object
	 */
	public void move() {
		super.move();
		displace();
		checkDisplacement();
		if (Settings.SAND_STORM)
			wave();
	}
	/**
	 * Method which makes this object wave or rotate.
	 */
	public void wave() {
		if (r > 4) {
			velR -= Math.random() * (0.4 - 0.01 + 1) + 0.01;
		}
		if (r <= 0) {
			if (velR < 4)
				velR += 0.5f;
		}
	}
	public void displace(){
		if(playerCollision){
		this.velX += threshold;
			if(x>oldX+3){
				threshold = 0;
				velX-=0.3;
				if(x<=oldX){
					velX = 0;
					contact= true;
					playerCollision = false;
				}
			}
			if(x<oldX-3){
				threshold = 0;
				velX += 0.3;
				if(x>=oldX){
					velX = 0;
					contact = true;
					playerCollision = false;
				}
			}
		}
	}
	public void checkDisplacement(){
		if(playerCollision == true && contact == false){
			count--;
			if(count<=0){
				velX = 0;
				playerCollision = false;
				contact = true;
				count = 60;
			}

		}
		if(contact== true){
			if(x>oldX){
				x-=0.5;
			}
			if(x<oldX){
				x+=0.5;
			}
		}
	}
	/**
	 * Methods which draws a bounding box
	 */
	public void draw() {
		drawBoundingBox();
	}
	/**
	 * Method which creates and draws a bounding box
	 * for debugging purposes
	 */
	public void checkCollision(){
		if(this.getBounds().intersects(game.getGameLoader().getPlayerOne().getBounds())){
			if(contact == true){
				if(game.getGameLoader().getPlayerOne().getCurrentDirection()==PlayerMovement.MOVE_LEFT){
					this.threshold = Math.random()*(-0.2 - 0.8 + 1) + -0.8;
				}
				else if(game.getGameLoader().getPlayerOne().getCurrentDirection()==PlayerMovement.MOVE_RIGHT){
					this.threshold = Math.random()*(0.8 - 0.2 + 1) + 0.2;
				}
			playerCollision = true;
			contact = false;
			}
		}
		if(this.getBounds().intersects(game.getGameLoader().getPlayerTwo().getBounds())){
			if(contact == true){
				if(game.getGameLoader().getPlayerTwo().getCurrentDirection()==PlayerMovement.MOVE_LEFT){
					this.threshold = Math.random()*(-0.2 - 0.8 + 1) + -0.8;
				}
				else if(game.getGameLoader().getPlayerTwo().getCurrentDirection()==PlayerMovement.MOVE_RIGHT){
					this.threshold = Math.random()*(0.8 - 0.2 + 1) + 0.2;
				}
			playerCollision = true;
			contact = false;
			}
		}

	}
	public void drawBoundingBox() {

		if (Settings.DEBUG_MODE) {
			Rectangle bounds = new Rectangle(x+10, y + 50, width - 60, height - 90);
			bounds.setStroke(Color.WHITE);
			bounds.setFill(Color.TRANSPARENT);
			bounds.setStrokeWidth(3);
			game.getSeventhLayer().getChildren().add(bounds);
		}
	}
	/**
	 * This methods will return specified bounds of this object
	 * based on coordinates and dimensions.
	 */
	public Rectangle2D getBounds() {
		return new Rectangle2D(x+10, y + 50, width - 60, height - 90);
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
