package com.EudyContreras.Snake.PlayerTwo;

import com.EudyContreras.Snake.AbstractModels.AbstractObject;
import com.EudyContreras.Snake.AbstractModels.AbstractTile;
import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Application.GameSettings;
import com.EudyContreras.Snake.FrameWork.PlayerMovement;
import com.EudyContreras.Snake.Identifiers.GameLevelObjectID;
import com.EudyContreras.Snake.Identifiers.GameObjectID;
import com.EudyContreras.Snake.Identifiers.GameStateID;
import com.EudyContreras.Snake.ImageBanks.GameImageBank;
import com.EudyContreras.Snake.ParticleEffects.DirtDisplacement;
import com.EudyContreras.Snake.ParticleEffects.FruitSplashTwo;

import javafx.geometry.Bounds;
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

public class PlayerTwoHead extends AbstractObject {
	private double targetRotation = 0.0;
	private double fadeValue = 1.0;
	private double offsetX = 0;
	private double offsetY = 0;
	private boolean showTheSkull = false;
	private int equivalence;
	private Text text;
	private Font font;
	private GameManager game;
	private Circle skull;
	private Circle radialBounds;
	private PlayerTwo snake;
	private Rectangle bounds;
	private Rectangle headBoundsLeft;
	private Rectangle headBoundsRight;
	private Rectangle headBoundsTop;
	private Rectangle headBoundsBottom;
	private Rectangle clearFromCollision;
	private PlayerTwoManager playerManager;
	private PlayerMovement newDirection;

	public PlayerTwoHead(PlayerTwo snake, GameManager game, Pane layer, Circle node, double x, double y, GameObjectID id,
			PlayerMovement Direction) {
		super(game, layer, node, x, y, id);
		this.r = snake.getR();
		this.snake = snake;
		this.game = game;
		this.playerManager = game.getPlayerTwoManager();
		this.text = new Text();
		this.font = Font.font("Plain", FontWeight.BOLD, 18);
		this.text.setFill(Color.rgb(210, 0, 0));
		this.text.setFont(font);
		this.text.setText(GameSettings.PLAYER_TWO_NAME);
		this.headBoundsLeft = new Rectangle(x, y, node.getRadius() * .5, node.getRadius() * .5);
		this.headBoundsRight = new Rectangle(x, y, node.getRadius() * .5, node.getRadius() * .5);
		this.headBoundsTop = new Rectangle(x, y, node.getRadius() * .5, node.getRadius() * .5);
		this.headBoundsBottom = new Rectangle(x, y, node.getRadius() * .5, node.getRadius() * .5);
		this.clearFromCollision = new Rectangle(x-radius, y-radius, node.getRadius() * 2, node.getRadius() * 2);
		this.radialBounds = new Circle(radius,x,y, Color.TRANSPARENT);
		if (GameSettings.DEBUG_MODE) {
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
			this.drawBoundingBox();
			this.adjustBounds();
		}
		this.layer.getChildren().add(text);
		this.loadMouth();
	}
	private void loadMouth(){
		this.playerManager.addObject(new PlayerTwoEatTrigger(this, snake, game, layer, new Circle(GameSettings.PLAYER_TWO_SIZE * 0.8, Color.TRANSPARENT), this.x,
				this.y, GameObjectID.SnakeMouth, PlayerMovement.MOVE_LEFT));
		this.playerManager.addObject(new PlayerTwoFangs(this, snake, game, layer, new Circle(GameSettings.PLAYER_TWO_SIZE * 0.30, Color.TRANSPARENT), this.x,
				this.y, GameObjectID.SnakeMouth, PlayerMovement.MOVE_LEFT));
	}
	public void move() {
		if (PlayerTwo.DEAD == false && PlayerTwo.LEVEL_COMPLETED == false && PlayerTwo.KEEP_MOVING && game.getStateID()!= GameStateID.GAME_MENU) {
			if (GameSettings.DEBUG_MODE) {
				adjustBounds();
			}
			this.circle.setRadius(GameSettings.PLAYER_TWO_SIZE*1.5);
			this.radius = circle.getRadius();
			this.y = snake.getY();
			this.x = snake.getX();
			this.text.setX(x - 50);
			this.text.setY(y - 40);
		}

	}
	public void updateUI(){
		if(!PlayerTwo.DEAD)
		super.updateUI();
	}
	public void logicUpdate(){
		showTheSkull();
		updateBounds();
	}
	public void updateBounds() {
		if (GameSettings.DEBUG_MODE) {
			bounds.setX(x - radius*1.1 / 2 + offsetX);
			bounds.setY(y - radius*1.1 / 2 + offsetY);
			bounds.setWidth(radius*1.1);
			bounds.setHeight(radius*1.1);
		}
	}
	public void showTheSkull() {
		if (showTheSkull == true) {
			fadeValue -= 0.01;
			this.circle.setOpacity(fadeValue);
			if (fadeValue <= 0) {
				fadeValue = 0;
			}
		}
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
		if (!PlayerTwo.DEAD && PlayerTwo.KEEP_MOVING) {
			for (int i = 0; i < 2; i++) {
				game.getDebrisManager().addDebris(new DirtDisplacement(game,game.getSixthLayer(), GameImageBank.dirt,0.5, (double) x, (double) y,
						new Point2D((Math.random() * (8 - -8 + 1) + -8), Math.random() * (8 - -8 + 1) + -8)));
			}
		}
	}

	public void checkCollision() {
		if (GameSettings.DEBUG_MODE) {
			for (int i = 0; i < game.getGameLoader().getTileManager().getBlock().size(); i++) {
				AbstractTile tempTile = game.getGameLoader().getTileManager().getBlock().get(i);
				if (tempTile.getId() == GameLevelObjectID.rock) {
					if (getBoundsLeft().intersects(tempTile.getBounds())) {
						if (GameSettings.ALLOW_ROCK_COLLISION) {
							showVisualQue(Color.RED);
						}
					} else if (getBoundsRight().intersects(tempTile.getBounds())) {
						if (GameSettings.ALLOW_ROCK_COLLISION) {
							showVisualQue(Color.BLUE);
						}
					} else if (getBoundsTop().intersects(tempTile.getBounds())) {
						if (GameSettings.ALLOW_ROCK_COLLISION) {
							showVisualQue(Color.GREEN);
						}
					} else if (getBoundsBottom().intersects(tempTile.getBounds())) {
						if (GameSettings.ALLOW_ROCK_COLLISION) {
							showVisualQue(Color.YELLOW);
						}
					}
				}
			}
		}
		if (!GameSettings.DEBUG_MODE) {
			for (int i = 0; i < game.getGameLoader().getTileManager().getBlock().size(); i++) {
				AbstractTile tempTile = game.getGameLoader().getTileManager().getBlock().get(i);
				if (tempTile.getId() == GameLevelObjectID.rock) {
					if (getBoundsLeft().intersects(tempTile.getBounds())) {
						if (GameSettings.ALLOW_ROCK_COLLISION) {
							displaceDirt(getBoundsLeft().getMinX(),getBoundsLeft().getMinY(),0,0);
						}
					} else if (getBoundsRight().intersects(tempTile.getBounds())) {
						if (GameSettings.ALLOW_ROCK_COLLISION) {
							displaceDirt(getBoundsRight().getMinX(),getBoundsRight().getMinY(),0,0);
						}
					} else if (getBoundsTop().intersects(tempTile.getBounds())) {
						if (GameSettings.ALLOW_ROCK_COLLISION) {
							displaceDirt(getBoundsTop().getMinX(),getBoundsTop().getMinY(),0,0);
						}
					} else if (getBoundsBottom().intersects(tempTile.getBounds())) {
						if (GameSettings.ALLOW_ROCK_COLLISION) {
							displaceDirt(getBoundsBottom().getMinX(),getBoundsBottom().getMinY(),0,0);
						}
					}
				}
			}
		}
	}

	public boolean allowLeftTurn() {
		for (int i = 0; i < game.getGameLoader().getTileManager().getBlock().size(); i++) {
			AbstractTile tempTile = game.getGameLoader().getTileManager().getBlock().get(i);
			if (getBoundsLeft().intersects(tempTile.getBounds())) {
				return false;

			}
		}
		return true;
	}

	public boolean allowRightTurn() {
		for (int i = 0; i < game.getGameLoader().getTileManager().getBlock().size(); i++) {
			AbstractTile tempTile = game.getGameLoader().getTileManager().getBlock().get(i);
			if (getBoundsRight().intersects(tempTile.getBounds())) {
				return false;

			}
		}
		return true;
	}

	public boolean allowUpTurn() {
		for (int i = 0; i < game.getGameLoader().getTileManager().getBlock().size(); i++) {
			AbstractTile tempTile = game.getGameLoader().getTileManager().getBlock().get(i);
			if (getBoundsTop().intersects(tempTile.getBounds())) {
				return false;
			}
		}
		return true;
	}

	public boolean allowDownTurn() {
		for (int i = 0; i < game.getGameLoader().getTileManager().getBlock().size(); i++) {
			AbstractTile tempTile = game.getGameLoader().getTileManager().getBlock().get(i);
			if (getBoundsBottom().intersects(tempTile.getBounds())) {
				return false;

			}
		}
		return true;
	}

	public void checkRadiusCollision() {
		for (int i = 0; i < game.getGameLoader().getTileManager().getBlock().size(); i++) {
			AbstractTile tempTile = game.getGameLoader().getTileManager().getBlock().get(i);
			if (tempTile.getId() == GameLevelObjectID.rock) {
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
		game.getDebrisManager().addParticle(
				new FruitSplashTwo(game, color, 1, 10, (float) (x + this.radius / 2), (float) (y + this.radius / 2)));
	}

	public void setRotate(boolean rotate, PlayerMovement newDirection, int targetRotation) {
		this.newDirection = newDirection;
		this.targetRotation = targetRotation;
	}

	public void setAnim(ImagePattern scene) {
		this.circle.setFill(scene);
	}
	public void addBones() {
		skull = new Circle(this.radius * .8, new ImagePattern(GameImageBank.snakeSkull));
		skull.setTranslateX(x);
		skull.setTranslateY(y);
		skull.setRotate(circle.getRotate());
		game.getBaseLayer().getChildren().add(skull);
	}
	public double getRadius(){
		return this.circle.getRadius();
	}

	public boolean isShowTheSkull() {
		return showTheSkull;
	}

	public void setShowTheSkull(boolean showTheSkull) {
		this.showTheSkull = showTheSkull;
	}
	public void drawBoundingBox() {

		if (GameSettings.DEBUG_MODE) {
			bounds = new Rectangle(x - radius*1.1 / 2, y - radius*1.1 / 2, radius*1.1, radius*1.1);
			bounds.setStroke(Color.WHITE);
			bounds.setStrokeWidth(3);
			bounds.setFill(Color.TRANSPARENT);
			game.getSeventhLayer().getChildren().add(bounds);
		}
	}

	public void adjustBounds() {
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

		this.headBoundsLeft.setWidth(circle.getRadius() * .5);
		this.headBoundsLeft.setHeight(circle.getRadius() * .5);
		this.headBoundsRight.setWidth(circle.getRadius() * .5);
		this.headBoundsRight.setHeight(circle.getRadius() * .5);
		this.headBoundsTop.setWidth(circle.getRadius() * .5);
		this.headBoundsTop.setHeight(circle.getRadius() * .5);
		this.headBoundsBottom.setWidth(circle.getRadius() * .5);
		this.headBoundsBottom.setHeight(circle.getRadius() * .5);
		this.clearFromCollision.setWidth(circle.getRadius() * 2);
		this.clearFromCollision.setHeight(circle.getRadius() * 2);
	}
	public Bounds getRadialBounds(){
		return radialBounds.getBoundsInParent();
	}
	public Rectangle2D getBounds() {
		return new Rectangle2D(x - radius*1.1 / 2, y - radius*1.1 / 2, radius*1.1, radius*1.1);
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
