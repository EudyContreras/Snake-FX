package com.EudyContreras.Snake.UserInterface;

import com.EudyContreras.Snake.FrameWork.GameManager;

public class MenuButtonStyles {
	 public static final String STANDARD_BUTTON_STYLE =
			 "-fx-fill-radius: "+GameManager.ScaleX_Y(30)+";"
			+"-fx-fill:"
	 		+"linear-gradient(green, limegreen);"
	 		+"-fx-effect: dropshadow(three-pass-box, limeGreen, "+GameManager.ScaleX_Y(5)+", 0, 0, 0);";
	 public static final String HOVERED_BUTTON_STYLE  =
			 "-fx-fill-radius: "+GameManager.ScaleX_Y(30)+";"
			+"-fx-fill: radial-gradient(center 50% -40%, radius 200%, lime 45%, green 50%);"
			+"-fx-effect: dropshadow(three-pass-box, limeGreen,"+GameManager.ScaleX_Y(30)+", 0.5, 0, 0);";
	 public static final String PRESSED_BUTTON_STYLE  =
			 "-fx-fill-radius: "+GameManager.ScaleX_Y(30)+";"
			+"-fx-fill: linear-gradient(palegreen, greenyellow, limegreen);"
			+"-fx-effect: dropshadow(gaussian, limegreen, "+GameManager.ScaleX_Y(80)+", 0.6, 0, 0);";
	 public static final String BUTTON_TEXT =
			 "-fx-fill: limegreen;"
			+"-fx-stroke: white;"
	        +"-fx-stroke-width: 3;";
}