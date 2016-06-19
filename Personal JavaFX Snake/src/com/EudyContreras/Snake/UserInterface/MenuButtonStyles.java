package com.EudyContreras.Snake.UserInterface;

import com.EudyContreras.Snake.FrameWork.GameManager;

public class MenuButtonStyles {
	 public static final String STANDARD_BUTTON_STYLE =
			 "-fx-fill-radius: "+GameManager.ScaleX_Y(30)+";"
			+"-fx-fill:"
			+" linear-gradient(#686868 0%, #232723 25%, #373837 75%, #757575 100%),"
	 		+" linear-gradient(#020b02, #3a3a3a),"
	 		+" linear-gradient(green, limegreen);"
	 		+"-fx-effect: dropshadow(three-pass-box, black, "+GameManager.ScaleX_Y(5)+", 0, 0, 0);";
	 public static final String HOVERED_BUTTON_STYLE  =
			 "-fx-fill-radius: "+GameManager.ScaleX_Y(30)+";"
			+"-fx-fill: radial-gradient(center 50% -40%, radius 200%, lime 45%, green 50%);"
			+"-fx-effect: dropshadow(three-pass-box, limeGreen,"+GameManager.ScaleX_Y(30)+", 0.5, 0, 0);";
	 public static final String PRESSED_BUTTON_STYLE  =
			 "-fx-fill-radius: "+GameManager.ScaleX_Y(30)+";"
		    +"-fx-fill: radial-gradient(center 50% -40%, radius 200%, palegreen 45%, limeGreen 50%);"
			+"-fx-effect: dropshadow(two-pass-box, limegreen, "+GameManager.ScaleX_Y(50)+", 0.7, 0, 0);";
	 public static final String BUTTON_TEXT =
			 "-fx-fill: white;"
			+"-fx-effect: dropshadow(two-pass-box, white, "+GameManager.ScaleX_Y(4)+", 0, 0, 0);";
	 public static final String MENU_BOX_STYLE  =
			 "-fx-fill-radius: "+GameManager.ScaleX_Y(30)+";"
			+"-fx-fill: radial-gradient(center 50% -40%, radius 200%, rgba(100,100,100,1) 45%, gray 50%);"
			+"-fx-effect: dropshadow(three-pass-box, lime,"+GameManager.ScaleX_Y(30)+", 0.5, 0, 0);";
}