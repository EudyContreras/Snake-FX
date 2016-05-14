package com.SnakeGame.FrameWork;

import javafx.event.EventHandler;
import javafx.scene.input.SwipeEvent;

public class GameGestureInputManager {
	public GameGestureInputManager(){
	}
	public void processGestures(SnakeGame game) {
		game.getGameRoot().setOnSwipeUp(new EventHandler<SwipeEvent>() {

			public void handle(SwipeEvent event) {
				game.getloader().getPlayerOne().setDirection(PlayerMovement.MOVE_UP);
				event.consume();
			}
		});
		game.getGameRoot().setOnSwipeDown(new EventHandler<SwipeEvent>() {

			public void handle(SwipeEvent event) {
				game.getloader().getPlayerOne().setDirection(PlayerMovement.MOVE_DOWN);
				event.consume();
			}
		});
		game.getGameRoot().setOnSwipeLeft(new EventHandler<SwipeEvent>() {
			public void handle(SwipeEvent event) {
				game.getloader().getPlayerOne().setDirection(PlayerMovement.MOVE_LEFT);
				event.consume();
			}
		});
		game.getGameRoot().setOnSwipeRight(new EventHandler<SwipeEvent>() {
			public void handle(SwipeEvent event) {
				game.getloader().getPlayerOne().setDirection(PlayerMovement.MOVE_RIGHT);
				event.consume();
			}
		});
	}
}
