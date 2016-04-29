package com.SnakeGame.PlayerOne;

import com.SnakeGame.Core.FruitSplash2;
import com.SnakeGame.Core.PlayerMovement;
import com.SnakeGame.Core.Settings;
import com.SnakeGame.Core.SnakeGame;
import com.SnakeGame.GameObjects.Tile;
import com.SnakeGame.ObjectIDs.GameObjectID;
import com.SnakeGame.ObjectIDs.LevelObjectID;

import javafx.geometry.Rectangle2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class OrgSnakeHead extends OrgGameObject {
	double rotation = 0;
	double rotationSpeed = 0;
	double targetRotation;
	int equivalence;
	boolean rotate;
	SnakeGame game;
	OrgPlayer snake;
	Rectangle headBounds;
	Rectangle headBoundsLeft;
	Rectangle headBoundsRight;
	Rectangle headBoundsTop;
	Rectangle headBoundsBottom;
	Rectangle clearFromCollision;
	OrgGameSectionManager sectManager;
	OrgGameObjectManager gom;
	PlayerMovement direction = PlayerMovement.MOVE_DOWN;
	PlayerMovement newDirection;

	public OrgSnakeHead(OrgPlayer snake, SnakeGame game, Pane layer, Circle node, float x, float y, GameObjectID id,
			PlayerMovement Direction) {
		super(game, layer, node, x, y, id);
		this.r = snake.getR();
		this.snake = snake;
		this.game = game;
		this.gom = game.getOrgObjectManager();
		this.sectManager = game.getOrgSectManager();
		this.headBounds = new Rectangle(x, y, node.getRadius(), node.getRadius());
		this.headBoundsLeft = new Rectangle(x, y, node.getRadius() * .5, node.getRadius() * .5);
		this.headBoundsRight = new Rectangle(x, y, node.getRadius() * .5, node.getRadius() * .5);
		this.headBoundsTop = new Rectangle(x, y, node.getRadius() * .5, node.getRadius() * .5);
		this.headBoundsBottom = new Rectangle(x, y, node.getRadius() * .5, node.getRadius() * .5);
		if (Settings.DEBUG_MODE) {
			this.headBounds.setFill(Color.TRANSPARENT);
			this.headBounds.setStroke(Color.WHITE);
			this.layer.getChildren().add(headBounds);
			this.headBoundsRight.setFill(Color.BLUE);
			this.headBoundsRight.setStroke(Color.WHITE);
			this.layer.getChildren().add(headBoundsRight);
			this.headBoundsLeft.setFill(Color.RED);
			this.headBoundsLeft.setStroke(Color.WHITE);
			this.layer.getChildren().add(headBoundsLeft);
			this.headBoundsTop.setFill(Color.GREEN);
			this.headBoundsTop.setStroke(Color.WHITE);
			this.layer.getChildren().add(headBoundsTop);
			this.headBoundsBottom.setFill(Color.YELLOW);
			this.headBoundsBottom.setStroke(Color.WHITE);
			this.layer.getChildren().add(headBoundsBottom);
			this.clearFromCollision.setFill(Color.TRANSPARENT);
			this.clearFromCollision.setStroke(Color.WHITE);
			this.clearFromCollision.setStrokeWidth(4);
			this.layer.getChildren().add(clearFromCollision);
		}
	}

	public void move() {
		if (Settings.DEBUG_MODE) {
			adjustBounds();
		}
		this.y = snake.getY();
		this.x = snake.getX();
	}

	public void rotate() {
		if (r == 0 && newDirection == PlayerMovement.MOVE_LEFT) {
			velR = 8;
			targetRotation = 89;
			equivalence = 1;
		} else if (r == 0 && newDirection == PlayerMovement.MOVE_RIGHT) {
			velR = -8;
			targetRotation = -89;
			equivalence = 0;
		} else if (r == 89 && newDirection == PlayerMovement.MOVE_UP) {
			velR = 8;
			targetRotation = 89;
			equivalence = 1;
		} else if (r == 89 && newDirection == PlayerMovement.MOVE_DOWN) {
			velR = -8;
			targetRotation = 0;
			equivalence = 0;
		} else if (r == -89 && newDirection == PlayerMovement.MOVE_UP) {
			velR = 8;
			targetRotation = 180;
			equivalence = 1;
		} else if (r == -89 && newDirection == PlayerMovement.MOVE_DOWN) {
			velR = 8;
			targetRotation = 0;
			equivalence = 1;
		} else if (r == 180 && newDirection == PlayerMovement.MOVE_LEFT) {
			velR = -8;
			targetRotation = 89;
			equivalence = 0;
		} else if (r == 180 && newDirection == PlayerMovement.MOVE_RIGHT) {
			velR = 8;
			targetRotation = 270;
			equivalence = 0;
		}
	}

	public boolean isApproximate(float tail_X, double sect_X, float tail_Y, double sect_Y) {
		double distance = Math.sqrt((tail_X - sect_X) * (tail_X - sect_X) + (tail_Y - sect_Y) * (tail_Y - sect_Y));
		if (distance > 10) {
			return true;
		}
		return false;
	}

	public void setLimit() {
		if (equivalence == 0) {
			if (r <= targetRotation) {
				velR = 0;
			}
		}
		if (equivalence == 1) {
			if (r >= targetRotation) {
				velR = 0;
			}
		}
	}

	public void checkRemovability() {

	}

	public void checkCollision() {
		if (Settings.DEBUG_MODE) {
			for (int i = 0; i < game.getloader().tileManager.block.size(); i++) {
				Tile tempTile = game.getloader().tileManager.block.get(i);
				if (tempTile.getId() == LevelObjectID.rock) {
					if (getBoundsLeft().intersects(tempTile.getBounds())) {
						if (Settings.ROCK_COLLISION) {
							showVisualQue(Color.RED);
						}
					} else if (getBoundsRight().intersects(tempTile.getBounds())) {
						if (Settings.ROCK_COLLISION) {
							showVisualQue(Color.BLUE);
						}
					} else if (getBoundsTop().intersects(tempTile.getBounds())) {
						if (Settings.ROCK_COLLISION) {
							showVisualQue(Color.GREEN);
						}
					} else if (getBoundsBottom().intersects(tempTile.getBounds())) {
						if (Settings.ROCK_COLLISION) {
							showVisualQue(Color.YELLOW);
						}
					}
				}
			}
		}
	}

	public boolean allowLeftTurn() {
		for (int i = 0; i < game.getloader().tileManager.block.size(); i++) {
			Tile tempTile = game.getloader().tileManager.block.get(i);
			if (getBoundsLeft().intersects(tempTile.getBounds())) {
				return false;

			}
		}
		return true;
	}

	public boolean allowRightTurn() {
		for (int i = 0; i < game.getloader().tileManager.block.size(); i++) {
			Tile tempTile = game.getloader().tileManager.block.get(i);
			if (getBoundsRight().intersects(tempTile.getBounds())) {
				return false;

			}
		}
		return true;
	}

	public boolean allowUpTurn() {
		for (int i = 0; i < game.getloader().tileManager.block.size(); i++) {
			Tile tempTile = game.getloader().tileManager.block.get(i);
			if (getBoundsTop().intersects(tempTile.getBounds())) {
				return false;
			}
		}
		return true;
	}

	public boolean allowDownTurn() {
		for (int i = 0; i < game.getloader().tileManager.block.size(); i++) {
			Tile tempTile = game.getloader().tileManager.block.get(i);
			if (getBoundsBottom().intersects(tempTile.getBounds())) {
				return false;

			}
		}
		return true;
	}

	public void checkRadiusCollision() {
		for (int i = 0; i < game.getloader().tileManager.block.size(); i++) {
			Tile tempTile = game.getloader().tileManager.block.get(i);
			if (tempTile.getId() == LevelObjectID.rock) {
				if (getCollisionRadiusBounds().intersects(tempTile.getBounds()) == false) {
					showVisualQue(Color.WHITE);
				}
			}
		}
	}

	public boolean overlaps(Tile r) {
		return x - radius * 1.5 < r.x + r.width && x - radius * 1.5 + radius * 3 > r.x
				&& y - radius * 1.5 < r.y + r.height && +radius * 3 > r.y;
	}

	public void showVisualQue(Color color) {
		game.getDebrisManager().addObject(
				new FruitSplash2(game, color, 1, 10, (float) (x + this.radius / 2), (float) (y + this.radius / 2)));
	}

	public void setRotate(boolean rotate, PlayerMovement newDirection, int targetRotation) {
		this.newDirection = newDirection;
		this.rotate = rotate;
		this.targetRotation = targetRotation;
	}

	public void setAnim(ImagePattern scene) {
		this.circle.setFill(scene);
	}

	public void adjustBounds() {
		this.headBounds.setX(x - radius / 2);
		this.headBounds.setY(y - radius / 2);
		this.headBoundsLeft.setX(x - radius - headBoundsLeft.getWidth() / 2);
		this.headBoundsLeft.setY(y + radius / 2 - headBoundsLeft.getHeight() * 1.5);
		this.headBoundsRight.setX(x + radius - headBoundsRight.getWidth() / 2);
		this.headBoundsRight.setY(y + radius / 2 - headBoundsRight.getHeight() * 1.5);
		this.headBoundsTop.setX(x - radius / 2 + headBoundsTop.getWidth() / 2);
		this.headBoundsTop.setY(y - radius - headBoundsTop.getHeight() / 2);
		this.headBoundsBottom.setX(x - radius / 2 + headBoundsBottom.getWidth() / 2);
		this.headBoundsBottom.setY(y + radius - headBoundsBottom.getHeight() / 2);
		this.clearFromCollision.setX(x - radius);
		this.clearFromCollision.setY(y - radius);
	}

	public Rectangle2D getBounds() {
		return new Rectangle2D(x - radius / 2, y - radius / 2, radius, radius);
	}

	public Rectangle2D getBoundsTop() {
		return new Rectangle2D(x - radius / 2 + headBoundsTop.getWidth() / 2,
				y - radius - headBoundsTop.getHeight() / 2, circle.getRadius() * .5, circle.getRadius() * .5);
	}

	public Rectangle2D getBoundsBottom() {
		return new Rectangle2D(x - radius / 2 + headBoundsBottom.getWidth() / 2,
				y + radius - headBoundsBottom.getHeight() / 2, circle.getRadius() * .5, circle.getRadius() * .5);
	}

	public Rectangle2D getBoundsRight() {
		return new Rectangle2D(x + radius - headBoundsRight.getWidth() / 2,
				y + radius / 2 - headBoundsRight.getHeight() * 1.5, circle.getRadius() * .5, circle.getRadius() * .5);
	}

	public Rectangle2D getBoundsLeft() {
		return new Rectangle2D(x - radius - headBoundsRight.getWidth() / 2,
				y + radius / 2 - headBoundsLeft.getHeight() * 1.5, circle.getRadius() * .5, circle.getRadius() * .5);
	}

	private Rectangle2D getCollisionRadiusBounds() {
		return new Rectangle2D(x - radius * 1.25, y - radius * 1.25, radius * 2.5, radius * 2.5);
	}

}
