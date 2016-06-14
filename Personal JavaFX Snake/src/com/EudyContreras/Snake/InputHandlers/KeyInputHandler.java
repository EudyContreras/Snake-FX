package com.EudyContreras.Snake.InputHandlers;

import com.EudyContreras.Snake.EnumIDs.GameStateID;
import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.FrameWork.PlayerMovement;
import com.EudyContreras.Snake.PlayerOne.PlayerOne;
import com.EudyContreras.Snake.PlayerTwo.PlayerTwo;
import com.EudyContreras.Snake.SlitherSnake.SlitherSnake;

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
public class KeyInputHandler {
	private boolean[] keyDown = new boolean[6];

	public KeyInputHandler() {
		keyDown[0] = false;
		keyDown[1] = false;
		keyDown[2] = false;
		keyDown[3] = false;
		keyDown[4] = false;
		keyDown[5] = false;
	}
	/**
	 * This method will process all the key events within a node and will assign specific actions
	 * to the them.
	 * @param game
	 * @param playerOne
	 * @param playerTwo
	 * @param slither
	 * @param scene
	 */
	public void processInput(GameManager game, PlayerOne playerOne, PlayerTwo playerTwo, SlitherSnake slither, Scene scene) {
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

			public void handle(KeyEvent e) {

				if (e.getCode() == KeyCode.W && e.getCode()!= KeyCode.A && e.getCode()!=KeyCode.D) {
					keyDown[0] = true;
					// slither.moveUp();
					game.getGameLoader().getPlayerOne().setDirection(PlayerMovement.MOVE_UP);
				}
				if (e.getCode() == KeyCode.S && e.getCode()!=KeyCode.A && e.getCode()!=KeyCode.D) {
					keyDown[1] = true;
					// slither.moveDown();
					game.getGameLoader().getPlayerOne().setDirection(PlayerMovement.MOVE_DOWN);
				}
				if (e.getCode() == KeyCode.A && e.getCode()!=KeyCode.W && e.getCode()!=KeyCode.S) {
					keyDown[2] = true;
					// slither.rotateLeft = true;
					game.getGameLoader().getPlayerOne().setDirection(PlayerMovement.MOVE_LEFT);
				}
				if (e.getCode() == KeyCode.D && e.getCode()!=KeyCode.W && e.getCode()!=KeyCode.S) {
					keyDown[3] = true;
					// slither.rotateRight = true;
					game.getGameLoader().getPlayerOne().setDirection(PlayerMovement.MOVE_RIGHT);
				}
				if (e.getCode() == KeyCode.H) {
					game.getGameHud().showHide();
				}
				if (e.getCode() == KeyCode.SPACE) {
					if (playerOne.isAllowOpen()) {
						playerOne.openMouth();
					}
					keyDown[4] = true;
				}
				if (e.getCode() == KeyCode.SHIFT) {
					if(playerOne.isAllowThrust())
					playerOne.setSpeedThrust(true);
 					keyDown[5] = true;
				}
				if (e.getCode() == KeyCode.P) {
					game.setStateID(GameStateID.GAME_MENU);
					game.pauseGame();
				}
				if (e.getCode() == KeyCode.R) {
					game.resumeGame();
				}
				if (e.getCode() == KeyCode.F) {

				}
				if (e.getCode() == KeyCode.ESCAPE) {
					if (game.getStateID() == GameStateID.GAMEPLAY || game.getStateID()==GameStateID.GAME_MENU) {
						if (game.getStateID() != GameStateID.GAME_MENU) {
							game.getPauseMenu().pauseGame();
						} else {
							game.getPauseMenu().resumeGame();
						}
					}
				}
				if (e.getCode() == KeyCode.ENTER) {
					if(playerTwo.isAllowThrust())
					playerTwo.setSpeedThrust(true);
				}
				if (e.getCode() == KeyCode.CONTROL) {
					if(playerTwo.isAllowThrust())
					playerTwo.setSpeedThrust(true);
				}
				if (e.getCode() == KeyCode.UP) {
					playerTwo.setDirection(PlayerMovement.MOVE_UP);
				}
				if (e.getCode() == KeyCode.DOWN) {
					playerTwo.setDirection(PlayerMovement.MOVE_DOWN);
				}
				if (e.getCode() == KeyCode.LEFT) {
					playerTwo.setDirection(PlayerMovement.MOVE_LEFT);
				}
				if (e.getCode() == KeyCode.RIGHT) {
					playerTwo.setDirection(PlayerMovement.MOVE_RIGHT);
				}
				if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.DELETE || e.getCode() == KeyCode.CONTROL) {
					if (playerTwo.isAllowOpen()) {
						playerTwo.openMouth();
					}
				}
				if (e.getCode() == KeyCode.NUMPAD8) {

				}
				if (e.getCode() == KeyCode.NUMPAD5) {

				}
				if (e.getCode() == KeyCode.NUMPAD4) {
					if(slither!=null)
					slither.setRotateLeft(true);
				}
				if (e.getCode() == KeyCode.NUMPAD6) {
					if(slither!=null)
					slither.setRotateRight(true);
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
					playerOne.setSpeedThrust(false);
					keyDown[5] = false;
				}
				if (e.getCode() == KeyCode.ENTER) {
					playerTwo.setSpeedThrust(false);
				}
				if (e.getCode() == KeyCode.CONTROL) {
					playerTwo.setSpeedThrust(false);
				}
				if (e.getCode() == KeyCode.NUMPAD8) {

				}
				if (e.getCode() == KeyCode.NUMPAD5) {

				}
				if (e.getCode() == KeyCode.NUMPAD4) {
					//slither.rotateLeft = false;
				}
				if (e.getCode() == KeyCode.NUMPAD6) {
					//slither.rotateRight = false;
				}
				if (!keyDown[0] && !keyDown[1]) {
				}
				if (!keyDown[2] && !keyDown[3]) {
					if(slither!=null){
					 slither.setRotateLeft(false);
					 slither.setRotateRight(false);
					}
				}
				if (!keyDown[4]) {
				}
				if (!keyDown[5]) {
					// slither.thrust = false;
				}
			}
		});
	}
}