package application;

import java.awt.Graphics;

public abstract class GameStates {
	protected GameStateID id;
	protected GameStateManager stateMangager;
	
	public abstract void enterState();
	public abstract void initializeState();
	public abstract void updateInterface(GameStateManager stateManager);
	public abstract void updateStateAt1(GameStateManager stateManager);
	public abstract void updateStateAt10(GameStateManager stateManager);
	public abstract void updateStateAt30(GameStateManager stateManager);
	public abstract void updateStateAt60(GameStateManager stateManager);
	public abstract void updateStateAt120(GameStateManager stateManager);
	public abstract void updateStateAt240(GameStateManager stateManager);
	public abstract void updateAnimations(long timePassed, GameStateManager stateManager);
	public abstract void renderInterface(Graphics g);
	public abstract void renderStateFirst(Graphics g);
	public abstract void renderStateSecond(Graphics g);
	public abstract void renderStateThird(Graphics g);
	public abstract void renderStateFourth(Graphics g);
	public abstract void renderStateFith(Graphics g);
	public abstract void renderToScreen(Graphics g);
	public abstract void renderBacgroundPlaceHolder(Graphics g);
	public abstract void exitState();
	public abstract GameStateID getStateID();
	public abstract void setStateID(GameStateID id);
	
	
}
