package com.EudyContreras.Snake.ClassicSnake;

import com.EudyContreras.Snake.AbstractModels.AbstractSection;
import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.FrameWork.GameSettings;
import com.EudyContreras.Snake.FrameWork.PlayerMovement;
import com.EudyContreras.Snake.Identifiers.GameObjectID;
import com.EudyContreras.Snake.Identifiers.GameStateID;
import com.EudyContreras.Snake.ImageBanks.GameImageBank;
import com.EudyContreras.Snake.ParticleEffects.SectionDisintegration;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;

public class ClassicSnakeSection extends AbstractSection {
	private double particleLife;
	private double particleSize;
	private double fadeValue = 1.0;
	private boolean fade = false;
	private boolean blowUp = true;
	private ClassicSnake classicSnake;
	private GameManager game;
	private Paint tailFill;
	private ImagePattern normalPattern;
	private ImagePattern blurredPattern;
	private AbstractSection previousSection;
	private ClassicSnakeSectionManager sectManager;

	public ClassicSnakeSection(ClassicSnake snake, GameManager game, Pane layer, Node node, double x, double y, GameObjectID id,
			PlayerMovement Direction, int numericID) {
		super(game, layer, node, id);
		this.game = game;
		this.classicSnake = snake;
		this.numericID = numericID;
		this.sectManager = game.getSectManagerThree();
		if (this.numericID <= 0) {
			if (Direction == PlayerMovement.MOVE_UP) {
				this.setLastDirection(Direction);
				this.y = y + this.circle.getRadius() * GameSettings.SECTION_DISTANCE;
				this.x = x;
				this.r = snake.getR();
				this.velX = snake.getVelX();
				this.velY = snake.getVelY();
				snake.setNeighbor(this);
			} else if (Direction == PlayerMovement.MOVE_DOWN) {
				this.setLastDirection(Direction);
				this.y = y - this.circle.getRadius() * GameSettings.SECTION_DISTANCE;
				this.x = x;
				this.r = snake.getR();
				this.velX = snake.getVelX();
				this.velY = snake.getVelY();
				snake.setNeighbor(this);
			} else if (Direction == PlayerMovement.MOVE_LEFT) {
				this.setLastDirection(Direction);
				this.x = x + this.circle.getRadius() * GameSettings.SECTION_DISTANCE;
				this.y = y;
				this.r = snake.getR();
				this.velX = snake.getVelX();
				this.velY = snake.getVelY();
				snake.setNeighbor(this);
			} else if (Direction == PlayerMovement.MOVE_RIGHT) {
				this.setLastDirection(Direction);
				this.x = x - this.circle.getRadius() * GameSettings.SECTION_DISTANCE;
				this.y = y;
				this.r = snake.getR();
				this.velX = snake.getVelX();
				this.velY = snake.getVelY();
				snake.setNeighbor(this);
			} else if (Direction == PlayerMovement.STANDING_STILL) {
				this.setLastDirection(Direction);
				this.x = x - this.circle.getRadius() * GameSettings.SECTION_DISTANCE;
				this.y = y;
				this.r = snake.getR();
				this.velX = snake.getVelX();
				this.velY = snake.getVelY();
				snake.setNeighbor(this);
			}
		} else if (this.numericID > 0) {
			for (int i = sectManager.getSectionList().size() - 1; i >= 0; i--) {
				AbstractSection previousSect = sectManager.getSectionList().get(i);
				if (previousSect.getNumericID() == this.numericID - 1) {
					previousSection = previousSect;
					switch (previousSect.getLastDirection()) {
					case MOVE_UP:
						setLastDirection(PlayerMovement.MOVE_UP);
						this.y = previousSect.getY() + this.circle.getRadius() * GameSettings.SECTION_DISTANCE;
						this.x = previousSect.getX();
						this.r = previousSect.getR();
						this.velX = previousSect.getVelX();
						this.velY = previousSect.getVelY();
						break;
					case MOVE_DOWN:
						setLastDirection(PlayerMovement.MOVE_DOWN);
						this.y = previousSect.getY() - this.circle.getRadius() * GameSettings.SECTION_DISTANCE;
						this.x = previousSect.getX();
						this.r = previousSect.getR();
						this.velX = previousSect.getVelX();
						this.velY = previousSect.getVelY();
						break;
					case MOVE_LEFT:
						setLastDirection(PlayerMovement.MOVE_LEFT);
						this.x = previousSect.getX() + this.circle.getRadius() * GameSettings.SECTION_DISTANCE;
						this.y = previousSect.getY();
						this.r = previousSect.getR();
						this.velX = previousSect.getVelX();
						this.velY = previousSect.getVelY();
						break;
					case MOVE_RIGHT:
						setLastDirection(PlayerMovement.MOVE_RIGHT);
						this.x = previousSect.getX() - this.circle.getRadius() * GameSettings.SECTION_DISTANCE;
						this.y = previousSect.getY();
						this.r = previousSect.getR();
						this.velX = previousSect.getVelX();
						this.velY = previousSect.getVelY();
						break;
					case STANDING_STILL:
						setLastDirection(PlayerMovement.STANDING_STILL);
						this.x = previousSect.getX() - this.circle.getRadius() * GameSettings.SECTION_DISTANCE;
						this.y = previousSect.getY();
						this.r = previousSect.getR();
						this.velX = previousSect.getVelX();
						this.velY = previousSect.getVelY();
						break;
					}
				}
			}
		}
		loadPatterns();
	}
	private void loadPatterns() {
		this.normalPattern = new ImagePattern(GameImageBank.classicSnakeBody);
		this.blurredPattern = new ImagePattern(GameImageBank.classicSnakeBodyBlurred);
		this.tailFill = new ImagePattern(GameImageBank.transparentFill);

	}
	public void move() {
		this.circle.setRadius(GameSettings.PLAYER_ONE_SIZE);
		checkBounds();
		disguiseLast();
		sectionAdjustment();
		if (ClassicSnake.DEAD == false && ClassicSnake.LEVEL_COMPLETED == false && ClassicSnake.KEEP_MOVING && game.getStateID()!=GameStateID.GAME_MENU)
			super.move();
		if (lastPosition.size() > 0) {
			if (x == lastPosition.get(0).getX() && y == lastPosition.get(0).getY()) {
				removeLatestLocation();
				if (lastDirection.get(0) == PlayerMovement.MOVE_UP) {
					setLastDirection(PlayerMovement.MOVE_UP);
					removeLatestDirection();
					velX = 0;
					velY = -GameSettings.SNAKE_ONE_SPEED;
					r = 180;
					sectManager.addNewCoordinates(new Point2D(x, y), PlayerMovement.MOVE_UP, this.numericID + 1);
				} else if (lastDirection.get(0) == PlayerMovement.MOVE_DOWN) {
					setLastDirection(PlayerMovement.MOVE_DOWN);
					removeLatestDirection();
					velX = 0;
					velY = GameSettings.SNAKE_ONE_SPEED;
					r = 0;
					sectManager.addNewCoordinates(new Point2D(x, y), PlayerMovement.MOVE_DOWN, this.numericID + 1);
				} else if (lastDirection.get(0) == PlayerMovement.MOVE_LEFT) {
					setLastDirection(PlayerMovement.MOVE_LEFT);
					removeLatestDirection();
					velY = 0;
					velX = -GameSettings.SNAKE_ONE_SPEED;
					r = 90;
					sectManager.addNewCoordinates(new Point2D(x, y), PlayerMovement.MOVE_LEFT, this.numericID + 1);
				} else if (lastDirection.get(0) == PlayerMovement.MOVE_RIGHT) {
					setLastDirection(PlayerMovement.MOVE_RIGHT);
					removeLatestDirection();
					velY = 0;
					velX = GameSettings.SNAKE_ONE_SPEED;
					r = -90;
					sectManager.addNewCoordinates(new Point2D(x, y), PlayerMovement.MOVE_RIGHT, this.numericID + 1);
				}
			}
		}
	}
	public void logicUpdate(){
		fadeDeath();
	}
	public void setMotionBlur(){
		if(classicSnake.getSpeedThrust()){
			this.circle.setFill(blurredPattern);
		}
		else{
			this.circle.setFill(normalPattern);
		}
	}

	public void hideLast() {
		if (this.numericID == ClassicSnake.NUMERIC_ID - 1) {
			this.circle.setVisible(false);
		} else if (this.numericID != ClassicSnake.NUMERIC_ID - 1) {
			this.circle.setVisible(true);
		}
	}

	public void disguiseLast() {
		if (!ClassicSnake.DEAD) {
			if (this.numericID == ClassicSnake.NUMERIC_ID - 1) {
				this.circle.setFill(tailFill);
			}
			else if (this.numericID != ClassicSnake.NUMERIC_ID - 1) {
				setMotionBlur();
			}
		}
	}
	public void fadeDeath() {
		if (fade == true) {
			fadeValue -= 0.03/GameManager.ScaleX_ScaleY;
			this.circle.setOpacity(fadeValue);
			if (fadeValue <= 0) {
				fadeValue = 0;
			}
		}
	}
	public void checkBounds() {
		if (x < 0 - radius) {
			x = (float) (GameSettings.WIDTH + radius);
		} else if (x > GameSettings.WIDTH + radius) {
			x = (float) (0 - radius);
		} else if (y < GameSettings.START_Y - radius) {
			y = (float) (GameSettings.HEIGHT + radius);
		} else if (y > GameSettings.HEIGHT + radius) {
			y = (float) (GameSettings.START_Y - radius);
		}
	}

	public void sectionAdjustment() {
		if (previousSection != null) {
			if (x > 0 + radius && x < GameSettings.WIDTH - radius && y > GameSettings.START_Y + radius
					&& y < GameSettings.HEIGHT - radius) {
				if (this.direction == PlayerMovement.MOVE_DOWN) {
					if (previousSection.getY() - y >= this.radius) {
						y = previousSection.getY() - this.radius;
						x = previousSection.getX();
					}
				}
				if (this.direction == PlayerMovement.MOVE_UP) {
					if (previousSection.getY() - y >= this.radius) {
						y = previousSection.getY() - this.radius;
						x = previousSection.getX();
					}
				}
				if (this.direction == PlayerMovement.MOVE_LEFT) {
					if (x - previousSection.getX() >= this.radius) {
						x = previousSection.getX() + this.radius;
						y = previousSection.getY();
					}
				}
				if (this.direction == PlayerMovement.MOVE_RIGHT) {
					if (previousSection.getX() - x >= this.radius) {
						x = previousSection.getX() - this.radius;
						y = previousSection.getY();
					}
				}
			}
		}
	}

	public void blowUp() {
		if (blowUp == true) {
			for (int i = 0; i < GameSettings.MAX_DEBRIS_AMOUNT; i++) {
				if (GameSettings.ADD_VARIATION) {
					particleSize = (Math.random() * (15 - 7 + 1) + 7)/GameManager.ScaleX_ScaleY;
					particleLife = (Math.random() * (1.5 - 0.5 + 1) + 0.5)/GameManager.ScaleX_ScaleY;
				}
				game.getDebrisManager().addParticle(new SectionDisintegration(game, GameImageBank.classicSnakeBodyDebris,
						particleLife, particleSize, (double) (x + this.radius / 2), (double) (y + this.radius / 2)));
			}
			blowUp = false;
		}
	}

	public void die() {
		fade = true;
		blowUp();
	}
	public Rectangle2D getBounds() {

		return new Rectangle2D(x - radius / 2, y - radius / 2, radius, radius);
	}
}
