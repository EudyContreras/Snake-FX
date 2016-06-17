package com.EudyContreras.Snake.UserInterface;

public class MenuButtonStyles {
	 public static final String STANDARD_BUTTON_STYLE =
			 "-fx-background-radius: 30;"
			+"-fx-text-fill: black;"
			+"-fx-background-color:"
	 		+ "linear-gradient(#686868 0%, #232723 25%, #373837 75%, #757575 100%),"
	 		+ "linear-gradient(#020b02, #3a3a3a),"
	 		+ "linear-gradient(green, limegreen);"
	 		+ "-fx-effect: dropshadow(three-pass-box, limeGreen, 5, 0, 0, 0);";
	 public static final String HOVERED_BUTTON_STYLE  =
			 "-fx-background-radius: 30;"
			+"-fx-background-color: radial-gradient(center 50% -40%, radius 200%, lime 45%, green 50%);"
			+"-fx-effect: dropshadow(three-pass-box, limeGreen, 30, 0.5, 0, 0);";
	 public static final String PRESSED_BUTTON_STYLE  =
			 "-fx-background-radius: 30;"
			+"-fx-background-color: linear-gradient(white, greenyellow, limegreen);"
			+"-fx-effect: dropshadow(three-pass-box, limeGreen, 30, 0.5, 0, 0);";
}