
package com.EudyContreras.Snake.AbstractModels;

import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.UserInterface.GameMenuInterface;

import javafx.scene.effect.DropShadow;
import javafx.scene.shape.Rectangle;

/**

 *
 * @author Eudy Contreras
 *
 */
public abstract class AbstractMenuElement {

	protected int currentChoice = 0;
	protected DropShadow glowLED;
	protected Rectangle clearUp;
	protected Rectangle menuLogo;
	protected double radius = 63.0;
	protected double opacity = 1.0;
	protected double buttonSizeW = 400;
	protected double buttonSizeH = 80;
	protected double fontSize = 50;
	protected boolean showMenu = false;
	protected GameManager game;
	protected GameMenuInterface menu;


	public void move() {

	}

	public void scale() {

	}

	public void updateUI() {

	}



}
