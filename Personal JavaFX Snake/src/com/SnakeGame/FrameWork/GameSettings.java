package com.SnakeGame.FrameWork;

import javafx.stage.Screen;

/**
 * This class contains all global settings within the game which can be easily
 * modified. The following settings can be used to increase the speed of the
 * snake relative to the size of the snake. These are the valid values that can
 * be used Good settings for speed and size : 4/24 4/28 5/25 5/30 6/24 6/30 7/28
 * 8/32 9/27 10/30 11/33 12/24 12/36 13/26 14/28 15/30
 *
 * @author Eudy Contreras
 *
 */
public class GameSettings {

	public static int START_X = 1920;
	public static int START_Y = 80; //This number must be a whole number
	public static int WIDTH = (int) (Screen.getPrimary().getBounds().getWidth()/GameLoader.ResolutionScaleX);
	public static int HEIGHT = (int) (Screen.getPrimary().getBounds().getHeight()/GameLoader.ResolutionScaleY);
	public static double SIZE_SCALE = 1.0;

	public static int SNAKE_SPEED = 1; // must be a number which the size of the result remains whole
	public static int SECTION_SIZE = 30;
	public static int SNAKE_ONE_SPEED = 1;
	public static int SNAKE_TWO_SPEED = 1;
	public static int SLITHER_SPEED = 8;
	public static int SLITHER_SIZE = 30;
	public static double PLAYER_ONE_SIZE = 28; // Must be even or divisible by two 24 25 26 27 28 29 30 May still bugout while teleporting
	public static double PLAYER_TWO_SIZE = 28; // Must be even or divisible by two

	public static int APPLE_COUNT = 20;
	public static int SECTIONS_TO_ADD = 2;
	public static double SECTION_DISTANCE = 1; // Must be a number divisible by the speed
	public static float FRAME_SCALE = 1.0f;

	public static int TURN_DELAY = 6;
	public static int BITE_DELAY = 10;
	public static int IMMUNITY_TIME = 20;
	public static int COLLISION_DELAY = 0;
	public static double HEALTH_REGENERATION_SPEED = 0.2/GameLoader.ResolutionScaleX;
	public static double ENERGY_COMSUMPTION_SPEED = 7/GameLoader.ResolutionScaleX;
	public static double ENERGY_REGENRATION_SPEED = 0.5/GameLoader.ResolutionScaleX;
	public static double DAMAGE_AMOUNT = 50;

	public static double PLAYER_ONE_SPEED = 6.0/GameLoader.ResolutionScaleX;
	public static double PLAYER_TWO_SPEED = 6.0/GameLoader.ResolutionScaleX;
	public static float PLAYER_HEALTH = 100.0f;
	public static float GLOBAL_ACCELARATION = 0.01f;
	public static double GlOBAL_ILLUMINATION = 2.0;
	public static double SPECULAR_MAP = 1.5;

	public static int BLUR_RANDOMNESS = 200;
	public static int MAX_AMOUNT_OF_DEBRIS = 40;
	public static int MAX_AMOUNT_OF_OBJECTS = 200;
	public static int DEBRIS_LIMIT = 120;
	public static int PARTICLE_LIMIT = 20;
	public static int SAND_AMOUNT = 5;
	public static int SAND_SPAWN_DELAY = 1;
	public static int RAIN_AMOUNT = 2;
	public static int RAIN_SPAWN_DELAY = 1;

	public static int DIRT_AMOUNT = 12;
	public static double SAND_SIZE = 5;
	public static double WIND_FORCE = 0.8;
	public static double WIND_SPEED = 0.2;
	public static double FRAMECAP = 1.0 / 60.0;
	public static double MANUAL_SIZE_SCALE = 1.1;

	public static boolean MANUAL_SIZE_SCALE_STATE = true;
	public static boolean OBJECT_TRACKER = false;
	public static boolean ALLOW_SELF_COLLISION = true;
	public static boolean ALLOW_TOUCH_CONTROL = false;
	public static boolean DAMAGE_IMMUNITY = false;
	public static boolean ROCK_COLLISION = true;
	public static boolean AUTOMATIC_EATING = true;
	public static boolean ALLOW_DIRT = false;
	public static boolean SAND_STORM = true;
	public static boolean RAIN_STORM = false;
	public static boolean RENDER_GAME = true;
	public static boolean RENDER_INTERFACE = true;
	public static boolean LOAD_SPIKE_FENCE = false;
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
	public static String PLAYER_ONE_NAME = "";
	public static String PLAYER_TWO_NAME = "";





}
