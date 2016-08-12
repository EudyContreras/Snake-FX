package com.EudyContreras.Snake.Controllers;

import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;

import com.EudyContreras.Snake.AbstractModels.AbstractGameStates;
import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Identifiers.GameStateID;

/**
 * Class used for updating, switching, and removing states This class may be
 * useful when a game have different states which must be individually rendered
 * or updated
 *
 * @author Eudy Contreras
 *
 */
public class GameStateController{

	private Map<GameStateID, AbstractGameStates> map;
	private AbstractGameStates currentState;
	public GameManager game;

	public GameStateController(GameManager game) {
		this.game = game;
		map = new HashMap<GameStateID, AbstractGameStates>();
	}

	public void addState(AbstractGameStates state) {
		map.put(state.getStateID(), state);
		state.initializeState();
		if (currentState == null) {
			state.enterState();
			currentState = state;
		}
	}

	public void setState(GameStateID id) {
		AbstractGameStates state = map.get(id);
		if (state == null) {
			System.err.println("State <" + id + "> does not exist");
			return;
		}
		currentState.exitState();
		state.enterState();
		currentState = state;
	}

	public AbstractGameStates getState() {
		return currentState;
	}

	public void updateInterface() {
		currentState.updateInterface(this);
	}

	public void updateGame() {
		currentState.updateGame(this);
	}

	public void updateMenu() {
		currentState.updateMenu(this);
	}

	public void renderInterface(Graphics g) {
		currentState.renderInterface();
	}

	public void renderGame() {
		currentState.renderGame();
	}

	public void renderMenu() {
		currentState.renderMenu();
	}

	public void removeState(GameStateID id) {
		map.remove(id);
	}
}
