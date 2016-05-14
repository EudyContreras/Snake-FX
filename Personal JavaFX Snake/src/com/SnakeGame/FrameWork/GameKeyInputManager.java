package com.SnakeGame.FrameWork;

import com.SnakeGame.PlayerOne.PlayerOne;
import com.SnakeGame.PlayerTwo.PlayerTwo;
import com.SnakeGame.SlitherSnake.SlitherSnake;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Class that controls the game's key mappings. this class allows us to assign
 * specific actions to every key
 *
 * @author Eudy Contreras
 *
 */
public class GameKeyInputManager {
	boolean[] keyDown = new boolean[6];

	public GameKeyInputManager() {
		keyDown[0] = false;
		keyDown[1] = false;
		keyDown[2] = false;
		keyDown[3] = false;
		keyDown[4] = false;
		keyDown[5] = false;
	}

	public void processInput(SnakeGame game, PlayerOne p1, PlayerTwo p2, SlitherSnake p3, Scene scene) {
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

			public void handle(KeyEvent e) {

				if (e.getCode() == KeyCode.W) {
					keyDown[0] = true;
					// p3.moveUp();
					game.getloader().getOrgPlayer2().setDirection(PlayerMovement.MOVE_UP);
				}
				if (e.getCode() == KeyCode.S) {
					keyDown[1] = true;
					// p3.moveDown();
					game.getloader().getOrgPlayer2().setDirection(PlayerMovement.MOVE_DOWN);
				}
				if (e.getCode() == KeyCode.A) {
					keyDown[2] = true;
					// p3.rotateLeft = true;
					game.getloader().getOrgPlayer2().setDirection(PlayerMovement.MOVE_LEFT);
				}
				if (e.getCode() == KeyCode.D) {
					keyDown[3] = true;
					// p3.rotateRight = true;
					game.getloader().getOrgPlayer2().setDirection(PlayerMovement.MOVE_RIGHT);
				}
				if (e.getCode() == KeyCode.H) {
					game.getGameHud().showHide();
				}
				if (e.getCode() == KeyCode.SPACE) {
					if (p1.allowOpen) {
						p1.openMouth();
					}
					keyDown[4] = true;
				}
				if (e.getCode() == KeyCode.SHIFT) {
					keyDown[5] = true;
					p3.thrust = true;
				}
				if (e.getCode() == KeyCode.P) {
					game.pauseGame();
				}
				if (e.getCode() == KeyCode.R) {
					game.resumeGame();
				}
				if (e.getCode() == KeyCode.F) {

				}
				if (e.getCode() == KeyCode.ESCAPE) {
					game.getMainMenu().setMainMenu();
				}
				if (e.getCode() == KeyCode.ENTER) {

				}
				if (e.getCode() == KeyCode.UP) {
					p2.setDirection(PlayerMovement.MOVE_UP);
				}
				if (e.getCode() == KeyCode.DOWN) {
					p2.setDirection(PlayerMovement.MOVE_DOWN);
				}
				if (e.getCode() == KeyCode.LEFT) {
					p2.setDirection(PlayerMovement.MOVE_LEFT);
				}
				if (e.getCode() == KeyCode.RIGHT) {
					p2.setDirection(PlayerMovement.MOVE_RIGHT);
				}
				if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.DELETE || e.getCode() == KeyCode.CONTROL) {
					if (p2.allowOpen) {
						p2.openMouth();
					}
				}
				if (e.getCode() == KeyCode.NUMPAD8) {

				}
				if (e.getCode() == KeyCode.NUMPAD5) {

				}
				if (e.getCode() == KeyCode.NUMPAD4) {
					p3.rotateLeft = true;
				}
				if (e.getCode() == KeyCode.NUMPAD6) {
					p3.rotateRight = true;
				}
			}
		});
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {

			public void handle(KeyEvent e) {

				if (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.W) {
					keyDown[0] = false;
				}
				if (e.getCode() == KeyCode.DOWN || e.getCode() == KeyCode.S) {
					keyDown[1] = false;
				}
				if (e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.A) {
					keyDown[2] = false;
				}
				if (e.getCode() == KeyCode.RIGHT || e.getCode() == KeyCode.D) {
					keyDown[3] = false;
				}
				if (e.getCode() == KeyCode.SPACE) {
					keyDown[4] = false;
				}
				if (e.getCode() == KeyCode.SHIFT) {
					keyDown[5] = false;
				}
				if (e.getCode() == KeyCode.NUMPAD8) {

				}
				if (e.getCode() == KeyCode.NUMPAD5) {

				}
				if (e.getCode() == KeyCode.NUMPAD4) {
					p3.rotateLeft = false;
				}
				if (e.getCode() == KeyCode.NUMPAD6) {
					p3.rotateRight = false;
				}
				if (!keyDown[0] && !keyDown[1]) {
				}
				if (!keyDown[2] && !keyDown[3]) {
					// p3.rotateLeft = false;
					// p3.rotateRight = false;
				}
				if (!keyDown[4]) {
				}
				if (!keyDown[5]) {
					// p3.thrust = false;
				}
			}
		});
	}
}
