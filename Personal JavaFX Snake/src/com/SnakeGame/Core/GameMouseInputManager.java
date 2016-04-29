package com.SnakeGame.Core;

import com.SnakeGame.SnakeOne.SnakeOne;

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

	public void processInput(SnakeGame game, SnakeOne player, Scene scene) {
		this.game = game;
		scene.setOnMouseMoved(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent e) {

				mouseX = (int) (e.getX() / Settings.SCALE);
				mouseY = (int) (e.getY() / Settings.SCALE);
				if (Settings.ALLOW_MOUSE_INPUT) {

				}
			}
		});
		scene.setOnMouseDragged(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent e) {

				mouseX = (int) (e.getX() / Settings.SCALE);
				mouseY = (int) (e.getY() / Settings.SCALE);
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