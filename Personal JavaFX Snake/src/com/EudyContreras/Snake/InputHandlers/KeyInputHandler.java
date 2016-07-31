package com.EudyContreras.Snake.InputHandlers;

import com.EudyContreras.Snake.ClassicSnake.ClassicSnake;
import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.FrameWork.PlayerMovement;
import com.EudyContreras.Snake.Identifiers.GameStateID;
import com.EudyContreras.Snake.PlayerOne.PlayerOne;
import com.EudyContreras.Snake.PlayerTwo.PlayerTwo;

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

	private PlayerOne playerOne;
	private PlayerTwo playerTwo;
	private ClassicSnake classicSnake;

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
	 * This method will process all the key events within a node and wil	l assign
	 * specific actions to the them.
	 *
	 * @param game
	 * @param playerOne
	 * @param playerTwo
	 * @param slither
	 * @param scene
	 */
	public void processInput(GameManager game, Scene scene) {
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

			public void handle(KeyEvent e) {

				if (e.getCode() == KeyCode.W) {
					keyDown[0] = true;
					if (playerOne != null)
						playerOne.setDirection(PlayerMovement.MOVE_UP);

					if(classicSnake != null)
						classicSnake.setDirection(PlayerMovement.MOVE_UP);
				}
				if (e.getCode() == KeyCode.S) {
					keyDown[1] = true;
					if (playerOne != null)
						playerOne.setDirection(PlayerMovement.MOVE_DOWN);
					if(classicSnake != null)
						classicSnake.setDirection(PlayerMovement.MOVE_DOWN);
				}
				if (e.getCode() == KeyCode.A) {
					keyDown[2] = true;
					if (playerOne != null)
						playerOne.setDirection(PlayerMovement.MOVE_LEFT);
					if(classicSnake != null)
						classicSnake.setDirection(PlayerMovement.MOVE_LEFT);
				}
				if (e.getCode() == KeyCode.D) {
					keyDown[3] = true;
					if (playerOne != null)
						playerOne.setDirection(PlayerMovement.MOVE_RIGHT);
					if(classicSnake != null)
						classicSnake.setDirection(PlayerMovement.MOVE_RIGHT);
				}
				if (e.getCode() == KeyCode.H) {
					game.getGameHud().showHide();
				}
				if (e.getCode() == KeyCode.SPACE) {
					if (playerOne != null)
						if (playerOne.isAllowOpen()) {
							playerOne.openMouth();
						}
					keyDown[4] = true;
				}
				if (e.getCode() == KeyCode.SHIFT) {
					if (playerOne != null)
						if (playerOne.isAllowThrust())
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
					if (game.getStateID() == GameStateID.GAMEPLAY || game.getStateID() == GameStateID.GAME_MENU) {
						if (game.getStateID() != GameStateID.GAME_MENU) {
							game.getPauseMenu().pauseGame();
						} else {
							game.getPauseMenu().resumeGame();
						}
					}
					else if (game.getStateID() != GameStateID.GAME_OVER) {
						if(PlayerOne.DEAD){
							playerOne.setManualGameOver(true);
							PlayerOne.ALLOW_FADE = true;
						}
						else if(PlayerTwo.DEAD){
							playerTwo.setManualGameOver(true);
							PlayerTwo.ALLOW_FADE = true;
						}
					}
				}
				if (e.getCode() == KeyCode.ENTER) {
					if (playerTwo != null){
						if (playerTwo.isAllowThrust())
							playerTwo.setSpeedThrust(true);
					}

					if(e.isControlDown()){
						if(!game.getMainWindow().isFullScreen()){
							game.setNewRatio(true);
							game.getMainWindow().setFullScreen(true);
							game.getGameBorder().showBorders(false);
						}
						else{
							game.setNewRatio(false);
							game.getMainWindow().setFullScreen(false);
							game.getGameBorder().showBorders(true);
						}
					}
				}
				if (e.getCode() == KeyCode.CONTROL) {
					if (playerTwo != null)
						if (playerTwo.isAllowThrust())
							playerTwo.setSpeedThrust(true);
				}
				if (e.getCode() == KeyCode.UP) {
					if (playerTwo != null)
						playerTwo.setDirection(PlayerMovement.MOVE_UP);
					if(classicSnake != null)
						classicSnake.setDirection(PlayerMovement.MOVE_UP);
				}
				if (e.getCode() == KeyCode.DOWN) {
					if (playerTwo != null)
						playerTwo.setDirection(PlayerMovement.MOVE_DOWN);
					if(classicSnake != null)
						classicSnake.setDirection(PlayerMovement.MOVE_DOWN);
				}
				if (e.getCode() == KeyCode.LEFT) {
					if (playerTwo != null)
						playerTwo.setDirection(PlayerMovement.MOVE_LEFT);
					if(classicSnake != null)
						classicSnake.setDirection(PlayerMovement.MOVE_LEFT);
				}
				if (e.getCode() == KeyCode.RIGHT) {
					if (playerTwo != null)
						playerTwo.setDirection(PlayerMovement.MOVE_RIGHT);
					if(classicSnake != null)
						classicSnake.setDirection(PlayerMovement.MOVE_RIGHT);
				}
				if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.DELETE || e.getCode() == KeyCode.CONTROL) {
					if (playerTwo != null)
						if (playerTwo.isAllowOpen()) {
							playerTwo.openMouth();
						}
				}
				if (e.getCode() == KeyCode.NUMPAD8) {

				}
				if (e.getCode() == KeyCode.NUMPAD5) {

				}
				if (e.getCode() == KeyCode.NUMPAD4) {

				}
				if (e.getCode() == KeyCode.NUMPAD6) {

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
					if (playerOne != null)
						playerOne.setSpeedThrust(false);
					keyDown[5] = false;
				}
				if (e.getCode() == KeyCode.ENTER) {
					if (playerTwo != null)
						playerTwo.setSpeedThrust(false);
				}
				if (e.getCode() == KeyCode.CONTROL) {
					if (playerTwo != null)
						playerTwo.setSpeedThrust(false);
				}
				if (e.getCode() == KeyCode.NUMPAD8) {

				}
				if (e.getCode() == KeyCode.NUMPAD5) {

				}
				if (e.getCode() == KeyCode.NUMPAD4) {

				}
				if (e.getCode() == KeyCode.NUMPAD6) {

				}
				if (!keyDown[0] && !keyDown[1]) {
				}
				if (!keyDown[2] && !keyDown[3]) {
				}
				if (!keyDown[4]) {
				}
				if (!keyDown[5]) {

				}
			}
		});
	}
	public void setPlayerOne(PlayerOne player){
		this.playerOne = null;
		this.playerOne = player;
	}
	public void setPlayerTwo(PlayerTwo player){
		this.playerTwo = null;
		this.playerTwo = player;
	}
	public void setClassicSnake(ClassicSnake player){
		this.classicSnake = null;
		this.classicSnake = player;
	}
}
