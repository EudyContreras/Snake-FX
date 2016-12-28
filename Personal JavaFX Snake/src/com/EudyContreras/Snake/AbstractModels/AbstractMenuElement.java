
package com.EudyContreras.Snake.AbstractModels;

import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Identifiers.GameModeID;
import com.EudyContreras.Snake.UserInterface.MenuManager;

import javafx.scene.effect.DropShadow;
import javafx.scene.shape.Rectangle;

/**

 *
 * @author Eudy Contreras
 *
 */
public abstract class AbstractMenuElement {

	protected int currentChoice = 0;
	protected double radius = 63.0;
	protected double opacity = 0.0;
	protected double buttonSizeW = 400;
	protected double buttonSizeH = 80;
	protected double fontSize = 50;
	protected double fadeSpeed = 0.01;
	protected boolean showMenu = false;
	protected boolean hideMenu = false;
	protected boolean startingGame = false;
	protected GameModeID modeID;
	protected DropShadow glowLED;
	protected Rectangle menuLogo;
	protected GameManager game;
	protected MenuManager menu;


	public void move() {

	}

	public void scale() {

	}

	public void updateUI() {

	}



}
