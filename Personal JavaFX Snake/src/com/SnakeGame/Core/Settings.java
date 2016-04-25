package com.SnakeGame.Core;

import java.awt.Toolkit;

public class Settings {


	public static final int SLITHER_SPEED = 5;
	public static final int SECTION = 5;
	/**
	 * The following settings can be used to increase the speed of the snake
	 * relative to the size of the snake. These are the valid values that can be used
	 * Good settings for speed and size :
	 * 4/24
	 * 4/28
	 * 5/25
	 * 5/30
	 * 6/24
	 * 6/30
	 * 7/28
	 * 8/32
	 * 9/27
	 * 10/30
	 * 11/33
	 * 12/24
	 * 12/36
	 * 13/26
	 * 14/28
	 * 15/30
	 */

	public static double START_X = 1920;
    public static double START_Y = 200;//800
    public static double WIDTH = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    public static double HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    public static double SCALE = 1.0;

    public static double SNAKE_SPEED = 5; //must be a number which the size must be divisible by while the result remains whole
	public static double SECTION_SIZE = 25;	//Could now be any number I think!!

	public static int APPLE_COUNT = 10;
	public static int SECTIONS_TO_ADD = 3;
	public static double SECTION_DISTANCE = 1; //Must be a number divisible by the speed
	public static float FRAME_SCALE = 1.0f;

    public static int TURN_DELAY = 5;
    public static int BITE_DELAY = 10;
    public static int IMMUNITY_TIME = 20;
    public static int COLLISION_DELAY = 0;
    public static double HEALTH_REGENERATION_SPEED = 0.1;
	public static double DAMAGE_AMOUNT = 50;

    public static float PLAYER_SPEED = 4.0f;
    public static float PLAYER_HEALTH = 100.0f;
	public static float GLOBAL_ACCELARATION = 0.01f;
	public static double GlOBAL_ILLUMINATION = 2.0;
	public static double SPECULAR_MAP = 1.6 ;

	public static int BLUR_RANDOMNESS = 200;
	public static int MAX_AMOUNT_OF_DEBRIS = 40;
	public static int MAX_AMOUNT_OF_OBJECTS = 30;
	public static int DEBRIS_LIMIT = 120;
	public static int PARTICLE_LIMIT = 30;
	public static double WIND_FORCE = 1.0;
	public static double WIND_SPEED = 0.2;
    public static double FRAMECAP = 1.0 / 60.0;
    public static double AMBIENTLIGHT_OPACITY = 0.8;

    public static boolean ROCK_COLLISION = true;
    public static boolean AUTOMATIC_EATING = true;
    public static boolean ALLOW_DIRT = false;
    public static boolean SAND_STORM = true;
    public static boolean RENDER_GAME = true;
    public static boolean RENDER_INTERFACE = true;
    public static boolean DEBUG_MODE = false;

    public static boolean ALLOW_MOUSE_INPUT = false;
    public static boolean SHOW_CURSOR = false;

    public static boolean ALLOW_TELEPORT = true;
    public static boolean ALLOW_PHYSICS = true;
    public static boolean ADD_VARIATION = true;
    public static boolean ADD_LIGHTING = true;
    public static boolean ADD_BLUR = true;
    public static boolean ADD_GLOW = false;
	public static boolean FAST_TURNS = false;


}
