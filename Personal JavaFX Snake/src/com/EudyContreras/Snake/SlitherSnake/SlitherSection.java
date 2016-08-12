package com.EudyContreras.Snake.SlitherSnake;

import com.EudyContreras.Snake.AbstractModels.AbstractSlitherSection;
import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Application.GameSettings;
import com.EudyContreras.Snake.FrameWork.PlayerMovement;
import com.EudyContreras.Snake.Identifiers.GameObjectID;
import com.EudyContreras.Snake.ImageBanks.GameImageBank;
import com.EudyContreras.Snake.ParticleEffects.DirtDisplacement;
import com.EudyContreras.Snake.ParticleEffects.SectionDisintegration;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class SlitherSection extends AbstractSlitherSection {
	private double particleLife;
	private double particleSize;
	private double fadeValue = 1.0;
	private boolean fade = false;
	private boolean blowUp = true;
	private int dirtDelay = 10;
	private GameManager game;
	private Circle bones;
	private AbstractSlitherSection previousSect;
	private SlitherSectionManager sectManager;

	public SlitherSection(SlitherSnake snake, GameManager game, Pane layer, Node node, double x, double y, GameObjectID id,
			int numericID) {
		super(game, layer, node, id);
		this.game = game;
		this.numericID = numericID;
		this.sectManager = game.getSlitherSectManager();
		this.x = x;
		this.y = y;
		this.velX = GameSettings.SLITHER_SPEED;
		this.velY = GameSettings.SLITHER_SPEED;
		if (this.numericID <= 1) {
			this.circle.setVisible(false);
		}
		if (numericID > 0) {
			for (int i = sectManager.getSectionList().size() - 1; i >= 0; i--) {
				AbstractSlitherSection previousSect = sectManager.getSectionList().get(i);
				if (previousSect.getNumericID() == this.numericID - 1) {
					this.previousSect = previousSect;
					this.r = previousSect.getR();
				}
			}
		}
	}

	public void move() {
		if (this.numericID > 0) {
			this.setRotation(previousSect.getRotation());
		}
		if (this.numericID > 0) {
			if (previousSect.getR() >= r) {
				r += 5.0f / SlitherSnake.ROTATION_OFFSET;
				if (r >= previousSect.getR()) {
					r = previousSect.getR();
				}
			}
			if (previousSect.getR() < r) {
				r -= 5.0f / SlitherSnake.ROTATION_OFFSET;
				if (r <= previousSect.getR()) {
					r = previousSect.getR();
				}
			}
		}
	}
	public void updateUI() {
		super.updateUI();
		 if(x>0+radius && x<GameSettings.WIDTH-radius && y>GameSettings.MIN_Y && y<GameSettings.MIN_Y-radius){
			 teleporting = false;
		 }
		if (GameSettings.ALLOW_DIRT) {
			dirtDelay--;
			if (dirtDelay <= 0) {
				displaceDirt(x + width / 2, y + height / 2, 18, 18);
				dirtDelay = 18;
			}
		}
		if (fade == true) {
			fadeValue -= 0.01;
			this.circle.setOpacity(fadeValue);
			if (fadeValue <= 0) {
				fadeValue = 0;
			}
		}
	}

	public void loadBones() {
		bones = new Circle(x, y, this.radius * 0.8, new ImagePattern(GameImageBank.snakeBones));
		game.getFirstLayer().getChildren().add(bones);
		bones.setRotate(r);
	}

	public void die() {
		loadBones();
		fade = true;
		blowUp();
	}

	public void displaceDirt(double x, double y, double low, double high) {
		if (!SlitherSnake.DEAD) {
			for (int i = 0; i < 8; i++) {
				game.getDebrisManager().addDebris(new DirtDisplacement(game, GameImageBank.dirt,1, (double) x, (double) y,
						new Point2D((Math.random() * (8 - -8 + 1) + -8), Math.random() * (8 - -8 + 1) + -8)));
			}
		}
	}

	public void blowUp() {
		if (blowUp == true) {
			for (int i = 0; i < GameSettings.MAX_DEBRIS_AMOUNT; i++) {
				if (GameSettings.ADD_VARIATION) {
					particleSize = Math.random() * (12 - 5 + 1) + 5;
					particleLife = Math.random() * (2.0 - 1.0 + 1) + 1.5;
				}
				game.getDebrisManager().addParticle(new SectionDisintegration(game, GameImageBank.snakeOneDebris,
						particleLife, particleSize, (double) (x + this.radius / 2), (double) (y + this.radius / 2)));
			}
			blowUp = false;
		}
	}


	public void checkRemovability() {

	}

	public void checkCollision() {

	}

	public PlayerMovement getDirection() {
		return direction;
	}

}
