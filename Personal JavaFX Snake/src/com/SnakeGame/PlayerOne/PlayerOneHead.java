package com.SnakeGame.PlayerOne;

import com.SnakeGame.FrameWork.AbstractObject;
import com.SnakeGame.FrameWork.AbstractTile;
import com.SnakeGame.FrameWork.GameLoader;
import com.SnakeGame.FrameWork.GameObjectManager;
import com.SnakeGame.FrameWork.PlayerMovement;
import com.SnakeGame.FrameWork.Settings;
import com.SnakeGame.FrameWork.SnakeGame;
import com.SnakeGame.IDenums.GameObjectID;
import com.SnakeGame.IDenums.LevelObjectID;
import com.SnakeGame.ImageBanks.GameImageBank;
import com.SnakeGame.Particles.DirtDisplacement;
import com.SnakeGame.Particles.FruitSplashTwo;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class PlayerOneHead extends AbstractObject {
	private double targetRotation;
	private int equivalence;
	private Text text;
	private Font font;
	private SnakeGame game;
	private PlayerOne snake;
	private Rectangle headBoundsLeft;
	private Rectangle headBoundsRight;
	private Rectangle headBoundsTop;
	private Rectangle headBoundsBottom;
	private Rectangle clearFromCollision;
	private GameObjectManager gom;
	private PlayerMovement newDirection;

	public PlayerOneHead(PlayerOne snake, SnakeGame game, Pane layer, Circle node, double x, double y, GameObjectID id,
			PlayerMovement Direction) {
		super(game, layer, node, x, y, id);
		this.r = snake.getR();
		this.snake = snake;
		this.game = game;
		this.gom = game.getObjectManager();
		this.text = new Text();
		this.font = Font.font("Plain", FontWeight.BOLD, 18 / GameLoader.ResolutionScaleX);
		this.text.setFill(Color.rgb(210, 0, 0));
		this.text.setFont(font);
		this.text.setText(Settings.PLAYER_ONE_NAME);
		this.gom.addObject(new PlayerOneEatTrigger(this, snake, game, layer, new Circle(Settings.SECTION_SIZE * 0.8 / GameLoader.ResolutionScaleX, Color.TRANSPARENT), this.x,
				this.y, GameObjectID.SnakeMouth, PlayerMovement.MOVE_LEFT));
		this.gom.addObject(new PlayerOneFangs(this, snake, game, layer, new Circle(Settings.SECTION_SIZE * 0.2 / GameLoader.ResolutionScaleX, Color.TRANSPARENT), this.x,
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
		this.layer.getChildren().add(text);
	}

	public void move() {
		if (Settings.DEBUG_MODE) {
			adjustBounds();
		}
		this.y = snake.getY();
		this.x = snake.getX();
		this.text.setX(x-50);
		this.text.setY(y-40);
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
	public void displaceDirt(double x, double y, double low, double high) {
		if (!PlayerOne.DEAD) {
			for (int i = 0; i < 2; i++) {
				game.getDebrisManager().addDebris(new DirtDisplacement(game, GameImageBank.dirt,0.5, (double) x, (double) y,
						new Point2D((Math.random() * (8 - -8 + 1) + -8), Math.random() * (8 - -8 + 1) + -8)));
			}
		}
	}

	public void checkCollision() {
		if (Settings.DEBUG_MODE) {
			for (int i = 0; i < game.getGameLoader().tileManager.block.size(); i++) {
				AbstractTile tempTile = game.getGameLoader().tileManager.block.get(i);
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
		if (!Settings.DEBUG_MODE) {
			for (int i = 0; i < game.getGameLoader().tileManager.block.size(); i++) {
				AbstractTile tempTile = game.getGameLoader().tileManager.block.get(i);
				if (tempTile.getId() == LevelObjectID.rock) {
					if (getBoundsLeft().intersects(tempTile.getBounds())) {
						if (Settings.ROCK_COLLISION) {
							displaceDirt(getBoundsLeft().getMinX(),getBoundsLeft().getMinY(),0,0);
						}
					} else if (getBoundsRight().intersects(tempTile.getBounds())) {
						if (Settings.ROCK_COLLISION) {
							displaceDirt(getBoundsRight().getMinX(),getBoundsRight().getMinY(),0,0);
						}
					} else if (getBoundsTop().intersects(tempTile.getBounds())) {
						if (Settings.ROCK_COLLISION) {
							displaceDirt(getBoundsTop().getMinX(),getBoundsTop().getMinY(),0,0);
						}
					} else if (getBoundsBottom().intersects(tempTile.getBounds())) {
						if (Settings.ROCK_COLLISION) {
							displaceDirt(getBoundsBottom().getMinX(),getBoundsBottom().getMinY(),0,0);
						}
					}
				}
			}
		}
	}

	public boolean allowLeftTurn() {
		for (int i = 0; i < game.getGameLoader().tileManager.block.size(); i++) {
			AbstractTile tempTile = game.getGameLoader().tileManager.block.get(i);
			if (getBoundsLeft().intersects(tempTile.getBounds())) {
				return false;

			}
		}
		return true;
	}

	public boolean allowRightTurn() {
		for (int i = 0; i < game.getGameLoader().tileManager.block.size(); i++) {
			AbstractTile tempTile = game.getGameLoader().tileManager.block.get(i);
			if (getBoundsRight().intersects(tempTile.getBounds())) {
				return false;

			}
		}
		return true;
	}

	public boolean allowUpTurn() {
		for (int i = 0; i < game.getGameLoader().tileManager.block.size(); i++) {
			AbstractTile tempTile = game.getGameLoader().tileManager.block.get(i);
			if (getBoundsTop().intersects(tempTile.getBounds())) {
				return false;
			}
		}
		return true;
	}

	public boolean allowDownTurn() {
		for (int i = 0; i < game.getGameLoader().tileManager.block.size(); i++) {
			AbstractTile tempTile = game.getGameLoader().tileManager.block.get(i);
			if (getBoundsBottom().intersects(tempTile.getBounds())) {
				return false;

			}
		}
		return true;
	}

	public void checkRadiusCollision() {
		for (int i = 0; i < game.getGameLoader().tileManager.block.size(); i++) {
			AbstractTile tempTile = game.getGameLoader().tileManager.block.get(i);
			if (tempTile.getId() == LevelObjectID.rock) {
				if (getCollisionRadiusBounds().intersects(tempTile.getBounds()) == false) {
					showVisualQue(Color.WHITE);
				}
			}
		}
	}

	public boolean overlaps(AbstractTile r) {
		return x - radius * 1.5 < r.getX() + r.getWidth() && x - radius * 1.5 + radius * 3 > r.getX()
				&& y - radius * 1.5 < r.getY() + r.getHeight() && +radius * 3 > r.getY();
	}

	public void showVisualQue(Color color) {
		game.getDebrisManager().addObject(
				new FruitSplashTwo(game, color, 1, 10, (float) (x + this.radius / 2), (float) (y + this.radius / 2)));
	}

	public void setRotate(boolean rotate, PlayerMovement newDirection, int targetRotation) {
		this.newDirection = newDirection;
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
