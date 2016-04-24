package com.SnakeGame.Core;

/**
 * Keeps a database of the IDs used by 
 * different sorts of objects that are used
 * in the game in order to uniquely identify 
 * them by name
 * @author Eudy Contreras
 *
 */
public enum GameLevelObjectID {
	player(),
	fruit(),
	mouse(),
	wall(),
	object();
}
