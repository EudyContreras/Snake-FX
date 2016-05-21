package com.SnakeGame.FrameWork;

import com.SnakeGame.PlayerOne.PlayerOne;
import com.SnakeGame.PlayerTwo.PlayerTwo;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

/**
 * Class allows a specified object to be moved using the mouse this class also
 * allows mapping a specific action to every key of the mouse
 *
 * @author Eudy Contreras
 *
 */
public class GameMouseInputManager {

	public static int mouseX, mouseY;
	SnakeGame game;
	/**
	 * Method used to process all mouse input within a node. this method will assign
	 * specific actions to the perform mouse actions.
	 * @param game
	 * @param playerOne
	 * @param playerTwo
	 * @param scene
	 */
	public void processInput(SnakeGame game, PlayerOne playerOne, PlayerTwo playerTwo, Scene scene) {
		this.game = game;
		scene.setOnMouseMoved(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent e) {

				mouseX = (int) (e.getX() / Settings.SIZE_SCALE);
				mouseY = (int) (e.getY() / Settings.SIZE_SCALE);
				if (Settings.ALLOW_MOUSE_INPUT) {

				}
			}
		});
		scene.setOnMouseDragged(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent e) {

				mouseX = (int) (e.getX() / Settings.SIZE_SCALE);
				mouseY = (int) (e.getY() / Settings.SIZE_SCALE);
				if (Settings.ALLOW_MOUSE_INPUT) {

				}
			}
		});
		scene.setOnMousePressed(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent e) {
				if (e.isPrimaryButtonDown()) {

				}

				if (e.isSecondaryButtonDown()) {

				}

				if (e.isMiddleButtonDown()) {

				}
			}
		});
	}

	public static int getMouseX() {
		return mouseX;
	}

	public static int getMouseY() {
		return mouseY;
	}
}