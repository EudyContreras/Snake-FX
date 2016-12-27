package com.EudyContreras.Snake.UserInterface;

public class MenuButtonStyles {
	public static final String STANDARD_BUTTON_STYLE =
			" -fx-fill-radius: "+30+";"
//			+"-fx-fill: radial-gradient(center 50% -40%, radius 200%, rgba(145,0,0,1) 45%, rgba(200,0,0,1) 50%);"
			+"-fx-fill:"
			+ "linear-gradient(#686868 0%, #232723 25%, #373837 75%, #757575 100%),"
			+ "linear-gradient(#020b02, #3a3a3a),"
			+ "linear-gradient(green, limegreen);"
			+"-fx-effect: dropshadow(three-pass-box, black, "+5+", 0, 0, 0);";
	public static final String HOVERED_BUTTON_STYLE  =
			" -fx-fill-radius: "+30+";"
			+"-fx-fill: radial-gradient(center 50% -40%, radius 200%, lime 45%, green 50%);"
		    +"-fx-effect: dropshadow(three-pass-box, limeGreen,"+30+", 0.5, 0, 0);";
	public static final String PRESSED_BUTTON_STYLE  =
			" -fx-fill-radius: "+30+";"
			+"-fx-fill: radial-gradient(center 50% -40%, radius 200%, palegreen 45%, limeGreen 50%);"
			+"-fx-effect: dropshadow(two-pass-box, limegreen, "+50+", 0.7, 0, 0);";
	public static final String BUTTON_TEXT =
			" -fx-fill: white;"
			+"-fx-effect: dropshadow(two-pass-box, white, "+4+", 0, 0, 0);";
	public static final String MENU_BOX_STYLE  =
			" -fx-fill-radius: "+30+";"
//			+"-fx-fill: radial-gradient(center 50% -40%, radius 200%, black 45%, rgba(40,40,40,1) 50%);"
			+"-fx-fill: radial-gradient(center 50% -40%, radius 200%, rgba(255,209,124,1) 45%, rgba(241,174,23) 50%);"
			+"-fx-effect: dropshadow(three-pass-box, orange,"+55+", 0.7, 0, 0);";
	public static final String FX_CONNECT_BOX_STYLE  =
			" -fx-fill-radius: "+30+";"
//			+"-fx-fill: radial-gradient(center 50% -40%, radius 200%, black 45%, rgba(40,40,40,1) 50%);"
			+"-fx-fill: radial-gradient(center 50% -40%, radius 200%, rgba(192,192,192,1) 45%, rgba(140,140,140) 50%);"
			+"-fx-effect: dropshadow(two-pass-box, rgba(200,200,200),"+10+", 0.2, 0, 0);";
}