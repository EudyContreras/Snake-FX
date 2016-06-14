package com.EudyContreras.Snake.AbstractModels;

import com.EudyContreras.Snake.EnumIDs.GameStateID;
import com.EudyContreras.Snake.Utilities.GameStateController;

/**
 * This class is the parent of every state. Each state must extend this class.
 * Events within this class will affect the childres of this class
 *
 * @author Eudy Contreras
 *
 */

public abstract class AbstractGameStates {
	protected GameStateID id;
	protected GameStateController stateMangager;

	public abstract void enterState();

	public abstract void initializeState();

	public abstract void updateInterface(GameStateController stateManager);

	public abstract void updateGame(GameStateController stateManager);

	public abstract void updateMenu(GameStateController stateManager);

	public abstract void renderInterface();

	public abstract void renderGame();

	public abstract void renderMenu();

	public abstract void renderBacgroundPlaceHolder();

	public abstract void exitState();

	public abstract GameStateID getStateID();

	public abstract void setStateID(GameStateID id);

}
