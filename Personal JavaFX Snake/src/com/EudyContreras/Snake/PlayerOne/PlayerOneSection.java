package com.EudyContreras.Snake.PlayerOne;

import com.EudyContreras.Snake.AbstractModels.AbstractSection;
import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Application.GameSettings;
import com.EudyContreras.Snake.FrameWork.PlayerMovement;
import com.EudyContreras.Snake.Identifiers.GameObjectID;
import com.EudyContreras.Snake.Identifiers.GameStateID;
import com.EudyContreras.Snake.ImageBanks.GameImageBank;
import com.EudyContreras.Snake.ParticleEffects.DirtDisplacement;
import com.EudyContreras.Snake.ParticleEffects.SectionDisintegration;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class PlayerOneSection extends AbstractSection {
	private double opacity = 0;
	private double particleLife;
	private double particleSize;
	private double fadeValue = 1.0;
	private boolean newBorn = true;
	private boolean fade = false;
	private boolean blowUp = true;
	private int dirtDelay = 10;
	private PlayerOne playerOne;
	private GameManager game;
	private Circle bones;
	private AbstractSection previousSect;
	private PlayerOneSectionManager sectManager;

	public PlayerOneSection(PlayerOne snake, GameManager game, Pane layer, Node node, double x, double y, GameObjectID id,
			PlayerMovement Direction, int numericID) {
		super(game, layer, node, id);
		this.game = game;
		this.playerOne = snake;
		this.numericID = numericID;
		this.circle.setOpacity(0);
		this.sectManager = game.getSectManagerOne();
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
			previousSect = sectManager.getSectionList().get(sectManager.getSectionList().size() - 1);
			if (previousSect.getNumericID() == this.numericID - 1) {
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

	public void move() {
		this.circle.setRadius(GameSettings.PLAYER_ONE_SIZE);
		this.radius = circle.getRadius();
		checkBounds();
		sectionAdjustment();
		if (PlayerOne.DEAD == false && PlayerOne.LEVEL_COMPLETED == false && PlayerOne.KEEP_MOVING && game.getStateID()!=GameStateID.GAME_MENU)
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
		if(playerOne.getSpeedThrust() && GameSettings.ALLOW_DIRT){
			updateSpeedDirt();
		}
		disguiseLast();
		fadeToBones();
		setVisible();
	}

	public void setMotionBlur(){
		if(playerOne.getSpeedThrust()){
			this.circle.setFill(GameImageBank.speedPatternOne);
		}
		else{
			this.circle.setFill(GameImageBank.normalPatternOne);
		}
	}

	public void updateDirt() {
		if ((this.numericID & 1) == 0) {
			dirtDelay--;
			if (dirtDelay <= 0) {
				if (PlayerOne.KEEP_MOVING) {
					displaceDirt(x + width / 2, y + height / 2, 18, 18);
					dirtDelay = 10;
				}
			}
		}
	}

	public void updateSpeedDirt() {
		if ((this.numericID & 1) == 0) {
			dirtDelay--;
			if (dirtDelay <= 0) {
				if (PlayerOne.KEEP_MOVING) {
					displaceSpeedDirt(x + width / 2, y + height / 2, 18, 18);
					dirtDelay = 10;
				}
			}
		}
	}

	public void displaceDirt(double x, double y, double low, double high) {
		if (direction != PlayerMovement.STANDING_STILL && !PlayerOne.DEAD && !PlayerOne.LEVEL_COMPLETED) {
			for (int i = 0; i <GameSettings.DIRT_AMOUNT; i++) {
				game.getDebrisManager().addDebris(new DirtDisplacement(game, GameImageBank.dirt_grain,1.5, x, y,
						new Point2D((Math.random() * (5 - -5 + 1) + -5), Math.random() * (6 - -6+ 1) + -6)));
			}
		}
	}

	public void displaceSpeedDirt(double x, double y, double low, double high) {
		if (direction != PlayerMovement.STANDING_STILL && !PlayerOne.DEAD && !PlayerOne.LEVEL_COMPLETED) {
			for (int i = 0; i <GameSettings.DIRT_AMOUNT; i++) {
				game.getDebrisManager().addDebris(new DirtDisplacement(game, GameImageBank.dirt_grain,1.5, x, y,
						new Point2D((Math.random() * (5 - -5 + 1) + -5), Math.random() * (6 - -6+ 1) + -6)));
			}
		}
	}

	public void disguiseLast() {
		if (!PlayerOne.DEAD) {
			if (this.numericID == PlayerOne.NUMERIC_ID - 1) {
				this.circle.setFill(GameImageBank.tailImage);
			}
			else if (this.numericID != PlayerOne.NUMERIC_ID - 1) {
				setMotionBlur();
			}
		}
	}

	public void fadeToBones() {
		if (fade == true) {
			if (this.numericID != PlayerOne.NUMERIC_ID - 1) {
				fadeValue -= 0.03;
				this.circle.setOpacity(fadeValue);
				if (fadeValue <= 0) {
					fadeValue = 0;
				}
			}
		}
	}

	private void setVisible(){
		if(newBorn){
			opacity +=0.05;
			circle.setOpacity(opacity);
			if(opacity>=1){
				circle.setOpacity(1);
				newBorn = false;
			}
		}
	}

	public void checkBounds() {
		if (x < 0 - radius) {
			x = (float) (GameSettings.WIDTH + radius);
		} else if (x > GameSettings.WIDTH + radius) {
			x = (float) (0 - radius);
		} else if (y < GameSettings.MIN_Y - radius) {
			y = (float) (GameSettings.HEIGHT + radius);
		} else if (y > GameSettings.HEIGHT + radius) {
			y = (float) (GameSettings.MIN_Y - radius);
		}
	}

	public void sectionAdjustment() {
		if (previousSect != null) {
			if (x > 0 + radius && x < GameSettings.WIDTH - radius && y > GameSettings.MIN_Y + radius && y < GameSettings.HEIGHT - radius) {
				if (this.direction == previousSect.getLastDirection()) {
					if (previousSect.getY()>y && x==previousSect.getX()) {
						if(previousSect.getY() - y < radius*.75){
							y = previousSect.getY() - circle.getRadius();
							System.out.println("TRUE");
						}
					}
					if (previousSect.getY()<y && x==previousSect.getX()) {
						if(y - previousSect.getY() < radius*.75){
							y = previousSect.getY() + circle.getRadius();
							System.out.println("TRUE");
						}
					}
					if (previousSect.getX()>x && y==previousSect.getY()) {
						if(previousSect.getX() - x < radius*.75){
							x = previousSect.getX() - circle.getRadius();
							System.out.println("TRUE");
						}
					}
					if (previousSect.getX()<x && y==previousSect.getY()) {
						if(x - previousSect.getX() < radius*.75){
							x = previousSect.getX() + circle.getRadius();
							System.out.println("TRUE");
						}
					}
				}
			}
		}
	}

	public void loadBones() {
		if (this.numericID == PlayerOne.NUMERIC_ID - 1) {
			bones = new Circle(x, y, this.radius * 0.4, new ImagePattern(GameImageBank.snakeBones));
			game.getDebrisLayer().getChildren().add(bones);
			bones.setRotate(r-90);
		}
		else if (this.numericID != PlayerOne.NUMERIC_ID - 1) {
			if (this.numericID > 0) {
				bones = new Circle(x, y, this.radius * 0.8, new ImagePattern(GameImageBank.snakeBones));
				game.getDebrisLayer().getChildren().add(bones);
				bones.setRotate(r - 90);
			}
		}
	}

	public void blowUp() {
		if (blowUp == true) {
			SectionDisintegration[] sectParticle = new SectionDisintegration[GameSettings.MAX_DEBRIS_AMOUNT];
			for (int i = 0; i < GameSettings.MAX_DEBRIS_AMOUNT; i++) {
				if (GameSettings.ALLOW_VARIATIONS) {
					particleSize = (Math.random() * (10 - 5 + 1) + 5);
					particleLife = (Math.random() * (1.5 - 0.5 + 1) + 0.5);
				}
				sectParticle[i] = new SectionDisintegration(game, GameImageBank.snakeOneDebris,
						particleLife, particleSize, (double) (x + this.radius / 2), (double) (y + this.radius / 2));
			}
			game.getDebrisManager().addParticle(sectParticle);
			blowUp = false;
		}
	}

	public void die() {
		loadBones();
		fade = true;
		blowUp();
	}

	public Rectangle2D getBounds() {
		return new Rectangle2D(x - radius / 2, y - radius / 2, radius, radius);
	}
}
