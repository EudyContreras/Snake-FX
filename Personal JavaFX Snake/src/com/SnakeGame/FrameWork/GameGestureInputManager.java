package com.SnakeGame.FrameWork;

import javafx.event.EventHandler;
import javafx.scene.input.SwipeEvent;

/**
 * Class responsible for processing touch gestures
 * @author Eudy Contreras
 *
 */
public class GameGestureInputManager {
	public GameGestureInputManager(){
	}
	/**
	 * Method that will create an action according to the nature of the
	 * gesture.
	 * @param game
	 */
	public void processGestures(SnakeGame game) {
		game.getGameRoot().setOnSwipeUp(new EventHandler<SwipeEvent>() {

			public void handle(SwipeEvent event) {
				game.getGameLoader().getPlayerOne().setDirection(PlayerMovement.MOVE_UP);
				event.consume();
			}
		});
		game.getGameRoot().setOnSwipeDown(new EventHandler<SwipeEvent>() {

			public void handle(SwipeEvent event) {
				game.getGameLoader().getPlayerOne().setDirection(PlayerMovement.MOVE_DOWN);
				event.consume();
			}
		});
		game.getGameRoot().setOnSwipeLeft(new EventHandler<SwipeEvent>() {
			public void handle(SwipeEvent event) {
				game.getGameLoader().getPlayerOne().setDirection(PlayerMovement.MOVE_LEFT);
				event.consume();
			}
		});
		game.getGameRoot().setOnSwipeRight(new EventHandler<SwipeEvent>() {
			public void handle(SwipeEvent event) {
				game.getGameLoader().getPlayerOne().setDirection(PlayerMovement.MOVE_RIGHT);
				event.consume();
			}
		});
	}
}
