package com.SnakeGame.Core;
/**
 * Keeps a database of the IDs used by 
 * different sorts of states which the game can
 * be in
 * @author Eudy Contreras
 *
 */
public enum GameStateID {

	MenuState(), 
	GamePlayState(),  
	OptionsMenuState(), 
	InfoScreenState(), 
	AudioOptionsMenuState(), 
	DifficultyMenuState(), 
	ChooseLevelMenuState(), 
	CustomizationMenuState();
}
