package com.EudyContreras.Snake.FrameWork;

import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Snake extends Application {

	public enum Direction {
		up, down, left, right
	}

	public static final int Section_Size = 20;
	public static final int Width = 1920;
	public static final int Height = 1080;

	private Direction direction = Direction.right;
	private boolean moved = false;
	private boolean running = false;

	private Circle[] tails = new Circle[30];
	private Timeline timeLine = new Timeline();
	private Circle head = new Circle(Section_Size, Color.LAWNGREEN);
	private double x = 0;
	private double y = 0;
	private double velX = 0;
	private double velY = 0;
	private List<Node> snake;

	public void start(Stage primaryStage) throws Exception {
		Scene scene = new Scene(createContent());
		scene.setFill(Color.BLACK);
		primaryStage.setScene(scene);
		primaryStage.setFullScreen(true);
		primaryStage.show();
		scene.setOnKeyPressed(event -> {
			if (!moved)
				return;
			switch (event.getCode()) {
			case W:
				if (direction != Direction.down)
					direction = Direction.up;
				break;
			case S:
				if (direction != Direction.up)
					direction = Direction.down;
				break;
			case A:
				if (direction != Direction.right)
					direction = Direction.left;
				break;
			case D:
				if (direction != Direction.left)
					direction = Direction.right;
				break;
			default:
				break;
			}
		});
		startGame();
	}

	private Parent createContent() {
		Pane root = new Pane();
		// Pane appleRoot = new Pane();
		root.setPrefSize(Width, Height);

		Group snakeBody = new Group();
		snake = snakeBody.getChildren();

		Circle food = new Circle(Section_Size, Color.RED);
		food.setFill(Color.RED);
		food.setTranslateX(
				(int) (Math.random() * (Width - food.getRadius() - food.getRadius() + 1) + food.getRadius()));
		food.setTranslateY(
				(int) (Math.random() * (Height - food.getRadius() - food.getRadius() + 1) + food.getRadius()));

		KeyFrame frame = new KeyFrame(Duration.seconds(0.008), event -> {
			if (!running)
				return;
			boolean toRemove = snake.size() > 1;
			Node tail;
			 x = x+velX;
			 y = y+velY;

			if (toRemove == true) {
				tail = snake.remove(snake.size() - 1);
			} else {
				tail = snake.get(0);
			}
			double tailX = tail.getTranslateX();
			double tailY = tail.getTranslateY();

			head.setTranslateY(y);
			head.setTranslateX(x);
			teleport();
			switch (direction) {

			case up:
				tail.setTranslateX(snake.get(0).getTranslateX());
				tail.setTranslateY(snake.get(0).getTranslateY() - 5);
				velY = -5;
				velX = 0;
				break;

			case down:
				tail.setTranslateX(snake.get(0).getTranslateX());
				tail.setTranslateY(snake.get(0).getTranslateY() + 5);
				velY = 5;
				velX = 0;
				break;

			case left:
				tail.setTranslateX(snake.get(0).getTranslateX() - 5);
				tail.setTranslateY(snake.get(0).getTranslateY());
				velY = 0;
				velX = -5;
				break;

			case right:
				tail.setTranslateX(snake.get(0).getTranslateX() + 5);
				tail.setTranslateY(snake.get(0).getTranslateY());
				velY = 0;
				velX = 5;
				break;

			}
			moved = true;

			if (toRemove)
				snake.add(0, tail);

			if (tail.getBoundsInParent().intersects(food.getBoundsInParent())) {
				food.setTranslateX(
						(int) (Math.random() * (Width - food.getRadius() - food.getRadius() + 1) + food.getRadius()));
				food.setTranslateY(
						(int) (Math.random() * (Height - food.getRadius() - food.getRadius() + 1) + food.getRadius()));

				for (int i = 0; i < tails.length; i++) {
					tails[i] = new Circle(Section_Size, Color.GREEN);
					tails[i].setTranslateX(tailX);
					tails[i].setTranslateY(tailY);
					snake.add(tails[i]);
				}

			}

		});
		timeLine.getKeyFrames().add(frame);
		timeLine.setCycleCount(Timeline.INDEFINITE);

		root.getChildren().addAll(snakeBody, head, food);

		return root;
	}

	public void restartGame() {
		stopGame();
		startGame();
	}

	private void stopGame() {
		running = false;
		timeLine.stop();
		snake.clear();

	}

	private void teleport() {
		if (x < 0 - Section_Size) {
			x = Width + Section_Size;
		} else if (x > Width + Section_Size) {
			x = 0 - Section_Size;
		} else if (y < 0 - Section_Size) {
			y = Height + Section_Size;
		} else if (y > Height + Section_Size) {
			y = 0 - Section_Size;
		}
		if (snake.get(0).getTranslateX() < 0 - Section_Size) {
			snake.get(0).setTranslateX(Width + Section_Size);
		} else if (snake.get(0).getTranslateX() > Width + Section_Size) {
			snake.get(0).setTranslateX(0 - Section_Size);
		} else if (snake.get(0).getTranslateY() < 0 - Section_Size) {
			snake.get(0).setTranslateY(Height + Section_Size);
		} else if (snake.get(0).getTranslateY() > Height + Section_Size) {
			snake.get(0).setTranslateY(0 - Section_Size);
		}
	}

	private void startGame() {
		x = 0;
		y = 0;
		velX = 0;
		velY = 0;
		direction = Direction.right;
		Circle head = new Circle(Section_Size, Color.GREEN);
		snake.add(head);
		timeLine.play();
		running = true;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
