package com.EudyContreras.Snake.InputHandlers;

import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Application.GameSettings;
import com.EudyContreras.Snake.FrameWork.PlayerMovement;
import com.EudyContreras.Snake.Identifiers.GameStateID;

import javafx.event.EventHandler;
import javafx.scene.input.SwipeEvent;
import javafx.scene.input.TouchEvent;

/**
 * Class responsible for processing touch gestures
 *
 * @author Eudy Contreras
 *
 */
public class TouchInputHandler {
	public TouchInputHandler() {
	}

	/**
	 * Method that will create an action according to the nature of the gesture.
	 *
	 * @param game
	 */
	public void processGestures(GameManager game) {
		if (GameSettings.ALLOW_TOUCH_CONTROL) {
			game.getScene().setOnSwipeUp(new EventHandler<SwipeEvent>() {

				public void handle(SwipeEvent event) {
					if (game.getStateID() == GameStateID.GAMEPLAY)
					game.getGameLoader().getPlayerOne().setGestureDirection(PlayerMovement.MOVE_UP);
					event.consume();
				}
			});
			game.getScene().setOnSwipeDown(new EventHandler<SwipeEvent>() {

				public void handle(SwipeEvent event) {
					if (game.getStateID() == GameStateID.GAMEPLAY)
					game.getGameLoader().getPlayerOne().setGestureDirection(PlayerMovement.MOVE_DOWN);
					event.consume();
				}
			});
			game.getScene().setOnSwipeLeft(new EventHandler<SwipeEvent>() {
				public void handle(SwipeEvent event) {
					if (game.getStateID() == GameStateID.GAMEPLAY)
					game.getGameLoader().getPlayerOne().setGestureDirection(PlayerMovement.MOVE_LEFT);
					event.consume();
				}
			});
			game.getScene().setOnSwipeRight(new EventHandler<SwipeEvent>() {
				public void handle(SwipeEvent event) {
					if (game.getStateID() == GameStateID.GAMEPLAY)
					game.getGameLoader().getPlayerOne().setGestureDirection(PlayerMovement.MOVE_RIGHT);
					event.consume();
				}
			});
			game.getScene().setOnTouchStationary(new EventHandler<TouchEvent>() {
				public void handle(TouchEvent event) {
					if (event.isShiftDown()) {
						if (game.getStateID() == GameStateID.GAMEPLAY)
						if (game.getGameLoader().getPlayerOne().isAllowThrust())
							game.getGameLoader().getPlayerOne().setSpeedThrust(true);
						;
						event.consume();
					}
				}
			});
			game.getScene().setOnTouchReleased(new EventHandler<TouchEvent>() {
				public void handle(TouchEvent event) {
					game.getGameLoader().getPlayerOne().setSpeedThrust(false);
					event.consume();
				}
			});
		}
	}
}
