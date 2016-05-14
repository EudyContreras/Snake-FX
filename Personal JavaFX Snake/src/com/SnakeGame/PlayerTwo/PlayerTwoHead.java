package com.SnakeGame.PlayerTwo;

import com.SnakeGame.FrameWork.GameLoader;
import com.SnakeGame.FrameWork.GameObject;
import com.SnakeGame.FrameWork.GameObjectManager;
import com.SnakeGame.FrameWork.PlayerMovement;
import com.SnakeGame.FrameWork.Settings;
import com.SnakeGame.FrameWork.SnakeGame;
import com.SnakeGame.GameObjects.Tile;
import com.SnakeGame.ObjectIDs.GameObjectID;
import com.SnakeGame.ObjectIDs.LevelObjectID;
import com.SnakeGame.Particles.FruitSplash2;

import javafx.geometry.Rectangle2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class PlayerTwoHead extends GameObject {
	double rotation = 0;
	double rotationSpeed = 0;
	double targetRotation;
	int equivalence;
	boolean rotate;
	SnakeGame game;
	PlayerTwo snake;
	Rectangle headBoundsLeft;
	Rectangle headBoundsRight;
	Rectangle headBoundsTop;
	Rectangle headBoundsBottom;
	Rectangle clearFromCollision;
	PlayerTwoFangs fangs;
	PlayerTwoSectionManager sectManager;
	GameObjectManager gom;
	PlayerMovement direction = PlayerMovement.MOVE_DOWN;
	PlayerMovement newDirection;

	public PlayerTwoHead(PlayerTwo snake, SnakeGame game, Pane layer, Circle node, float x, float y, GameObjectID id,
			PlayerMovement Direction) {
		super(game, layer, node, x, y, id);
		this.r = snake.getR();
		this.snake = snake;
		this.game = game;
		this.gom = game.getOrgObjectManager();
		this.sectManager = game.getSectManagerTwo();
		this.gom.addObject(new PlayerTwoEatTrigger(this, snake, game, layer, new Circle(Settings.SECTION_SIZE * 0.8 / GameLoader.ResolutionScaleX, Color.TRANSPARENT), this.x,
				this.y, GameObjectID.SnakeMouth, PlayerMovement.MOVE_LEFT));
		this.gom.addObject(new PlayerTwoFangs(this, snake, game, layer, new Circle(Settings.SECTION_SIZE * 0.2 / GameLoader.ResolutionScaleX, Color.BLACK), this.x,
				this.y, GameObjectID.SnakeMouth, PlayerMovement.MOVE_LEFT));
		this.headBoundsLeft = new Rectangle(x, y, node.getRadius() * .5, node.getRadius() * .5);
		this.headBoundsRight = new Rectangle(x, y, node.getRadius() * .5, node.getRadius() * .5);
		this.headBoundsTop = new Rectangle(x, y, node.getRadius() * .5, node.getRadius() * .5);
		this.headBoundsBottom = new Rectangle(x, y, node.getRadius() * .5, node.getRadius() * .5);
		this.clearFromCollision = new Rectangle(x, y, node.getRadius() * 2, node.getRadius() * 2);
		if (Settings.DEBUG_MODE) {
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
		//this.positionFangs();
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
