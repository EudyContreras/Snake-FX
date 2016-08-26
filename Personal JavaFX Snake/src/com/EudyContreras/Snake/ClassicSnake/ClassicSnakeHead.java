package com.EudyContreras.Snake.ClassicSnake;

import com.EudyContreras.Snake.AbstractModels.AbstractObject;
import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Application.GameSettings;
import com.EudyContreras.Snake.FrameWork.PlayerMovement;
import com.EudyContreras.Snake.Identifiers.GameObjectID;
import com.EudyContreras.Snake.Identifiers.GameStateID;

import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class ClassicSnakeHead extends AbstractObject {
	private double targetRotation = 0.0;
	private double offsetX = 0;
	private double offsetY = 0;
	private int equivalence;
	private Text text;
	private Font font;
	private GameManager game;
	private Circle radialBounds;
	private ClassicSnake snake;
	private Rectangle bounds;
	private PlayerMovement newDirection;

	public ClassicSnakeHead(ClassicSnake snake, GameManager game, Pane layer, Circle node, float x, float y, GameObjectID id,
			PlayerMovement Direction) {
		super(game, layer, node, x, y, id);
		this.r = snake.getR();
		this.snake = snake;
		this.game = game;
		this.game.getClassicSnakeManager();
		this.text = new Text();
		this.font = Font.font("Plain", FontWeight.BOLD, 18);
		this.text.setFill(Color.rgb(210, 0, 0));
		this.text.setFont(font);
		this.text.setText(GameSettings.PLAYER_ONE_NAME);
		if (GameSettings.DEBUG_MODE) {
			this.drawBoundingBox();
		}
		this.layer.getChildren().add(text);
	}

	public void move() {
		if (ClassicSnake.DEAD == false && ClassicSnake.LEVEL_COMPLETED == false && ClassicSnake.KEEP_MOVING && game.getStateID()!= GameStateID.GAME_MENU) {
			this.circle.setRadius(GameSettings.PLAYER_ONE_SIZE*1.4);
			this.radius = circle.getRadius();
			this.y = snake.getY();
			this.x = snake.getX();
			this.r = snake.getR();
			this.text.setX(x - 50);
			this.text.setY(y - 40);
		}

	}
	public void updateUI(){
		if(!ClassicSnake.DEAD)
		super.updateUI();
	}
	public void logicUpdate(){
		updateBounds();
	}
	public void updateBounds() {
		if (GameSettings.DEBUG_MODE) {
			bounds.setX(x - radius*1.1 / 2 + offsetX);
			bounds.setY(y - radius*1.1 / 2 + offsetY);
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
	public void checkCollision() {


	}

	public void setRotate(boolean rotate, PlayerMovement newDirection, int targetRotation) {
		this.newDirection = newDirection;
		this.targetRotation = targetRotation;
	}

	public void setAnim(ImagePattern scene) {
		this.circle.setFill(scene);
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

	public Bounds getRadialBounds(){
		return radialBounds.getBoundsInParent();
	}
	public Rectangle2D getBounds() {
		return new Rectangle2D(x - radius*1.1 / 2, y - radius*1.1 / 2, radius*1.1, radius*1.1);
	}


}
