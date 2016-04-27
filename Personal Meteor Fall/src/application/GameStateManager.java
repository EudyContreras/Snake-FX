package application;

import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;


public class GameStateManager {

	
	private Map<GameStateID, GameStates> map;
	private GameStates currentState;
	public Game game;
	
	public GameStateManager(Game game){
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
	public void updateAt1(){
		currentState.updateStateAt1(this);
	}
	public void updateAt10(){
		currentState.updateStateAt10(this);
	}
	public void updateAt30(){
		currentState.updateStateAt30(this);
	}
	public void updateAt60(){
		currentState.updateStateAt60(this);
	}
	public void updateAt120(){
		currentState.updateStateAt120(this);
	}
	public void updateAt240(){
		currentState.updateStateAt240(this);
	}
	public void updateAnimations(long TimePassed){
		currentState.updateAnimations(TimePassed,this);
	}
	public void renderInterface(Graphics g){
		currentState.renderInterface(g);
	}
	public void renderFirst(Graphics g){
		currentState.renderStateFirst(g);
	}
	public void renderSecond(Graphics g){
		currentState.renderStateSecond(g);
	}
	public void renderThird(Graphics g){
		currentState.renderStateThird(g);
	}
	public void renderFourth(Graphics g){
		currentState.renderStateFourth(g);
	}
	public void renderFith(Graphics g){
		currentState.renderStateFith(g);
	}
	public void renderToScreen(Graphics g){
		currentState.renderToScreen(g);
	}
	public void renderBacgroundPlaceHolder(Graphics g){
		currentState.renderBacgroundPlaceHolder(g);
	}
	public void removeState(GameStateID id){
		map.remove(id);
	}
}
