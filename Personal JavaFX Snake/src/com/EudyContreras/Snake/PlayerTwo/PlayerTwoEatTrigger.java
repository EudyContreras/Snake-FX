package com.EudyContreras.Snake.PlayerTwo;

import com.EudyContreras.Snake.AbstractModels.AbstractObject;
import com.EudyContreras.Snake.AbstractModels.AbstractTile;
import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Application.GameSettings;
import com.EudyContreras.Snake.FrameWork.PlayerMovement;
import com.EudyContreras.Snake.Identifiers.GameLevelObjectID;
import com.EudyContreras.Snake.Identifiers.GameObjectID;

import javafx.geometry.Rectangle2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class PlayerTwoEatTrigger extends AbstractObject {
	private GameManager game;
	private PlayerTwo snake;
	private Rectangle boundBox;
	private PlayerTwoHead head;

	public PlayerTwoEatTrigger(PlayerTwoHead head, PlayerTwo snake, GameManager game, Pane layer, Circle node, double x, double y,
			GameObjectID id, PlayerMovement Direction) {
		super(game, layer, node, x, y, id);
		this.snake = snake;
		this.head = head;
		this.game = game;
		if (Direction == PlayerMovement.MOVE_UP) {
			this.y = (float) (y - this.circle.getRadius() * 3);
			this.x = x;
			this.velX = snake.getVelX();
			this.velY = snake.getVelY();
		} else if (Direction == PlayerMovement.MOVE_DOWN) {
			this.y = (float) (y + this.circle.getRadius() * 3);
			this.x = x;
			this.velX = snake.getVelX();
			this.velY = snake.getVelY();
		} else if (Direction == PlayerMovement.MOVE_LEFT) {
			this.x = (float) (x - this.circle.getRadius() * 3);
			this.y = y;
			this.velX = snake.getVelX();
			this.velY = snake.getVelY();
		} else if (Direction == PlayerMovement.MOVE_RIGHT) {
			this.x = (float) (x + this.circle.getRadius() * 3);
			this.y = y;
			this.velX = snake.getVelX();
			this.velY = snake.getVelY();
		} else if (Direction == PlayerMovement.STANDING_STILL) {
			this.y = (float) (y + this.circle.getRadius() * 3);
			this.x = x;
			this.velX = snake.getVelX();
			this.velY = snake.getVelY();
		}
		if (GameSettings.DEBUG_MODE) {
			this.circle.setStroke(Color.WHITE);
			this.circle.setStrokeWidth(3);
			this.boundBox = new Rectangle(x,y,head.getRadius()*2,head.getRadius()*2);
			this.boundBox.setStroke(Color.WHITE);
			this.boundBox.setStrokeWidth(3);
			this.boundBox.setFill(Color.TRANSPARENT);
			this.layer.getChildren().add(boundBox);
		}

	}

	public void move() {
		super.move();
		this.circle.setRadius(GameSettings.PLAYER_TWO_SIZE * 0.8);
		if (snake.getCurrentDirection() == PlayerMovement.MOVE_UP) {
			this.y = (float) (snake.getY() - this.circle.getRadius() * 3);
			this.x = snake.getX();
		} else if (snake.getCurrentDirection() == PlayerMovement.MOVE_DOWN) {
			this.y = (float) (snake.getY() + this.circle.getRadius() * 3);
			this.x = snake.getX();
		} else if (snake.getCurrentDirection() == PlayerMovement.MOVE_LEFT) {
			this.x = (float) (snake.getX() - this.circle.getRadius() * 3);
			this.y = snake.getY();
		} else if (snake.getCurrentDirection() == PlayerMovement.MOVE_RIGHT) {
			this.x = (float) (snake.getX() + this.circle.getRadius() * 3);
			this.y = snake.getY();
		}
		if(GameSettings.DEBUG_MODE){
		this.boundBox.setX(x - head.getRadius());
		this.boundBox.setY(y - head.getRadius());
		this.boundBox.setWidth(head.getRadius() * 2);
		this.boundBox.setHeight(head.getRadius() * 2);
		}
	}

	public void checkCollision() {
		for (int i = 0; i < game.getGameObjectController().getFruitList().size(); i++) {
			AbstractObject tempObject = game.getGameObjectController().getFruitList().get(i);
			if (tempObject.getId() == GameObjectID.Fruit) {
				if (getRadialBounds().intersects(tempObject.getRadialBounds())) {
					if (PlayerTwo.MOUTH_CLOSE && GameSettings.ALLOW_AUTOMATIC_EATING) {
						snake.openMouth();
						break;
					}
				}
			}
		}
		if (!PlayerTwo.DEAD && !PlayerTwo.LEVEL_COMPLETED) {
			for (int i = 0; i < game.getGameLoader().getTileManager().getTile().size(); i++) {
				AbstractTile tempTile = game.getGameLoader().getTileManager().getTile().get(i);
				if (tempTile.getId() == GameLevelObjectID.cactus) {
					if(getBounds().intersects(tempTile.getBounds())){
						game.getPathFinder().avoidObstacle(tempTile);
					}
				}
			}
			for (int i = 0; i < game.getGameLoader().getTileManager().getBlock().size(); i++) {
				AbstractTile tempTile = game.getGameLoader().getTileManager().getBlock().get(i);
				if (tempTile.getId() == GameLevelObjectID.rock) {
					if (getBounds().intersects(tempTile.getBounds())) {
						if (GameSettings.ALLOW_ROCK_COLLISION) {
							if(getBounds().intersects(tempTile.getBounds())){
								game.getPathFinder().avoidObstacle(tempTile);
							}
						}
					}
				}
			}
			for (int i = 0; i < game.getGameLoader().getTileManager().getTrap().size(); i++) {
				AbstractTile tempTile = game.getGameLoader().getTileManager().getTrap().get(i);
				if(getBounds().intersects(tempTile.getBounds())){
					game.getPathFinder().avoidObstacle(tempTile);
				}
			}
		}
	}
	public Rectangle2D getBounds() {
		return new Rectangle2D(x - head.getRadius(), y - head.getRadius(), head.getRadius()*2, head.getRadius()*2);
	}
}
