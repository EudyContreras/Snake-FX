package com.SnakeGame.Core;

import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;

import com.SnakeGame.ObjectIDs.GameStateID;

/**
 * Class used for updating, switching, and removing states 
 * This class may be useful when a game have different states which 
 * must be individually rendered or updated
 * @author Eudy Contreras
 *
 */
public class GameStateManager {

	
	private Map<GameStateID, GameStates> map;
	private GameStates currentState;
	public SnakeGame game;
	
	public GameStateManager(SnakeGame game){
		this.game = game;
		map = new HashMap<GameStateID, GameStates>();
	}
	public void addState(GameStates state){
		map.put(state.getStateID(), state);
		state.initializeState();
		if(currentState == null){
			state.enterState();
			currentState = state;
		}
	}
	public void setState(GameStateID id){
		GameStates state = map.get(id);
		if(state == null){
			System.err.println("State <" + id + "> does not exist" );
			return;
		}
		currentState.exitState();
		state.enterState();
		currentState = state;
	}
	public GameStates getState(){
		return currentState;
	}
	public void updateInterface(){
		currentState.updateInterface(this);
	}
	public void updateGame(){
		currentState.updateGame(this);
	}
	public void updateMenu(){
		currentState.updateMenu(this);
	}
	public void renderInterface(Graphics g){
		currentState.renderInterface();
	}
	public void renderGame(){
		currentState.renderGame();
	}
	public void renderMenu(){
		currentState.renderMenu();
	}
	public void removeState(GameStateID id){
		map.remove(id);
	}
}
