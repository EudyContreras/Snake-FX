package com.SnakeGame.AbstractModels;

import com.SnakeGame.IDenums.GameStateID;
import com.SnakeGame.Utilities.GameStateManager;

/**
 * This class is the parent of every state. Each state must extend this class.
 * Events within this class will affect the childres of this class
 * 
 * @author Eudy Contreras
 *
 */

public abstract class AbstractGameStates {
	protected GameStateID id;
	protected GameStateManager stateMangager;

	public abstract void enterState();

	public abstract void initializeState();

	public abstract void updateInterface(GameStateManager stateManager);

	public abstract void updateGame(GameStateManager stateManager);

	public abstract void updateMenu(GameStateManager stateManager);

	public abstract void renderInterface();

	public abstract void renderGame();

	public abstract void renderMenu();

	public abstract void renderBacgroundPlaceHolder();

	public abstract void exitState();

	public abstract GameStateID getStateID();

	public abstract void setStateID(GameStateID id);

}
