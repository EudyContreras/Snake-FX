package application;

public class Settings {
    public static float FRAMECAP = 1.5f;
    public static double START_X = 1920;
    public static double START_Y = 200;//800
    public static double WIDTH = 1920;
    public static double HEIGHT = 950;//800
    public static double SCALE = 1.0;
    
    public static float SHOOT_DELAY = 10;
    public static float PLAYER_SPEED = 4.0f;
    public static float PLAYER_HEALTH = 100.0f;
	public static float GLOBAL_ACCELARATION = 0.01f;

    public static float PLAYER_MISSILE_SPEED = 4.0f;

    public static int ASTEROID_SPAWN_RANDOMNESS = (int) (120*(FRAMECAP));
	public static int MAX_AMOUNT_OF_DEBRIS = 50;
	public static int MAX_AMOUNT_OF_STARS = 30;
	public static int DEBRIS_LIMIT = 160;
	public static int PARTICLE_LIMIT = 40;
    public static float ASTEROID_MAX_SPEED = 3.0f;
    public static double AMBIENTLIGHT_OPACITY = 0.8;
    
    public static float  EXPLOSION_POSITION_X = 0;
    public static float  EXPLOSION_POSITION_Y = 0;
        
    public static boolean RENDER_GAME = true;
    public static boolean RENDER_INTERFACE = true;
    public static boolean AFTERBURNER = false;
    public static boolean DEBUG_MODE = false;
    
    public static boolean ALLOW_MOUSE_INPUT = false;
    public static boolean SHOW_CURSOR = false;
    
    public static boolean ALLOW_PHYSICS = true;
	public static boolean DIRECT_HIT = false; 
    public static boolean ADD_VARIATION = true;
    public static boolean ADD_LIGHTING = true;
    public static boolean ADD_BLUR = true;
    public static boolean ADD_GLOW = true;


}
