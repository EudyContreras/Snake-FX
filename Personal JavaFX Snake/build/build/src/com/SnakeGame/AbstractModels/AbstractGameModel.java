package com.SnakeGame.AbstractModels;

import com.SnakeGame.FrameWork.FadeScreenHandler;
import com.SnakeGame.FrameWork.GameGestureInputManager;
import com.SnakeGame.FrameWork.GameKeyInputManager;
import com.SnakeGame.FrameWork.GameLoader;
import com.SnakeGame.FrameWork.GameMouseInputManager;
import com.SnakeGame.FrameWork.GameObjectManager;
import com.SnakeGame.HudElements.EnergyMeter;
import com.SnakeGame.HudElements.GameHud;
import com.SnakeGame.HudElements.GameOverScreen;
import com.SnakeGame.HudElements.HealthBarOne;
import com.SnakeGame.HudElements.HealthBarTwo;
import com.SnakeGame.HudElements.PauseMenu;
import com.SnakeGame.HudElements.ScoreBoard;
import com.SnakeGame.HudElements.ScoreKeeper;
import com.SnakeGame.HudElements.VictoryScreen;
import com.SnakeGame.IDenums.GameStateID;
import com.SnakeGame.ImageBanks.GameImageBank;
import com.SnakeGame.ImageBanks.GameLevelImage;
import com.SnakeGame.Interface.MenuMain;
import com.SnakeGame.Particles.GameDebrisManager;
import com.SnakeGame.Particles.RainEmitter;
import com.SnakeGame.Particles.SandEmitter;
import com.SnakeGame.PlayerOne.PlayerOneSectionManager;
import com.SnakeGame.PlayerTwo.PlayerTwoSectionManager;
import com.SnakeGame.SlitherSnake.SlitherManager;
import com.SnakeGame.SlitherSnake.SlitherSectionManager;
import com.SnakeGame.Utilities.ScreenOverlay;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public abstract class AbstractGameModel extends Application {

	protected GameStateID stateID;
	protected GameLoader loader;
	protected GameKeyInputManager keyInput;
	protected GameMouseInputManager mouseInput;
	protected GameGestureInputManager gestures;
	protected GraphicsContext gc;
	protected GameObjectManager objectManager;
	protected SlitherManager slitherManager;
	protected GameDebrisManager debrisManager;
	protected PlayerOneSectionManager sectManagerOne;
	protected PlayerTwoSectionManager sectManagerTwo;;
	protected SlitherSectionManager sectManagerThree;
	protected FadeTransition fadeSplash;
	protected MenuMain mainMenu;
	protected Scene scene;
	protected Scene splashScene;
	protected Group mainRoot;
	protected Stage mainWindow;
	protected Canvas canvas;
	protected Pane root;
	protected Pane splashLayout;
	protected Pane levelLayer;
	protected Pane baseLayer;
	protected Pane firstLayer;
	protected Pane secondLayer;
	protected Pane thirdLayer;
	protected Pane fourthLayer;
	protected Pane fithLayer;
	protected Pane sixthLayer;
	protected Pane seventhLayer;
	protected Pane eighthLayer;
	protected Pane ninthLayer;
	protected Pane tenthLayer;
	protected Pane eleventhLayer;
	protected Pane twelfthLayer;
	protected Pane thirTeenthLayer;
	protected Pane fourTeenthLayer;
	protected Pane fadeScreenLayer;
	protected Text TextFPS;
	protected PauseMenu pauseMenu;
	protected GameImageBank imageBank;
	protected GameLevelImage levelImageBank;
	protected HealthBarOne healthBarOne;
	protected HealthBarTwo healthBarTwo;
	protected ScoreBoard scoreBoardOne;
	protected ScoreBoard scoreBoardTwo;
	protected EnergyMeter energyMeter;
	protected SandEmitter sandEmitter;
	protected RainEmitter rainEmitter;
	protected VictoryScreen victoryScreen;
	protected AnimationTimer gameLoop;
	protected AnimationTimer animationLoop;
	protected AnimationTimer particleLoop;
	protected GameOverScreen gameOverScreen;
	protected FadeScreenHandler fadeHandler;
	protected ScreenOverlay postEffects;
	protected ScoreKeeper scoreKeeper;
	protected ImageView backgroundImage;
	protected ImageView splash;
	protected Rectangle2D bounds;
	protected GameHud gameHud;
	protected String title = "SNAKE";
	protected boolean isRunning = true;
	protected boolean gameRunning = false;
	protected boolean goToNext;
	protected boolean goToMain;
	protected int splashWidth;
	protected int splashHeight;
	protected int levelLenght;
	protected double splashFadeDuration;
	protected double splashFadeDelay;
	public static double ScaleX = GameLoader.ResolutionScaleX;
	public static double ScaleY = GameLoader.ResolutionScaleY;

	public HealthBarOne getHealthBarOne() {
		return healthBarOne;
	}

	public HealthBarTwo getHealthBarTwo() {
		return healthBarTwo;
	}

	public void setHealthBarOne(HealthBarOne healthBarOne) {
		this.healthBarOne = healthBarOne;
	}

	public void setHealthBarTwo(HealthBarTwo healthBarTwo) {
		this.healthBarTwo = healthBarTwo;
	}

	public static void writeToLog(String text) {
		System.out.println(text + "\n");
	}

	public VictoryScreen getVictoryScreen() {
		return victoryScreen;
	}

	public void setVictoryScreen(VictoryScreen victoryScreen) {
		this.victoryScreen = victoryScreen;
	}

	public GameOverScreen getGameOverScreen() {
		return gameOverScreen;
	}

	public void setGameOverScreen(GameOverScreen gameOverScreen) {
		this.gameOverScreen = gameOverScreen;
	}

	public EnergyMeter getEnergyMeter() {
		return energyMeter;
	}

	public void setEnergyMeter(EnergyMeter energyMeter) {
		this.energyMeter = energyMeter;
	}
	public PauseMenu getPauseMenu(){
		return pauseMenu;
	}
	public MenuMain getMainMenu() {
		return mainMenu;
	}

	public void setMainMenu(MenuMain mainMenu) {
		this.mainMenu = mainMenu;
	}

	public Stage getMainWindow() {
		return mainWindow;
	}

	public void setMainWindow(Stage mainWindow) {
		this.mainWindow = mainWindow;
	}

	public GameLoader getGameLoader() {
		return loader;
	}

	public void setGameLoader(GameLoader loader) {
		this.loader = loader;
	}

	public Pane getGameRoot() {
		return root;
	}

	public void setRoot(Parent root) {
		scene.setRoot(root);
	}

	public Pane getLevelLayer() {
		return levelLayer;
	}

	public ScreenOverlay getPostEffects() {
		return postEffects;
	}

	public GameObjectManager getObjectManager() {
		return objectManager;
	}

	public void setObjectManager(GameObjectManager objectManager) {
		this.objectManager = objectManager;
	}

	public SlitherManager getSlitherManager() {
		return slitherManager;
	}

	public GameDebrisManager getDebrisManager() {
		return debrisManager;
	}

	public PlayerOneSectionManager getSectManagerOne() {
		return sectManagerOne;
	}

	public PlayerTwoSectionManager getSectManagerTwo() {
		return sectManagerTwo;
	}

	public SlitherSectionManager getSectManagerThree() {
		return sectManagerThree;
	}

	public void setDebrisManager(GameDebrisManager debrisManager) {
		this.debrisManager = debrisManager;
	}

	public FadeScreenHandler getFadeScreenHandler(){
		return fadeHandler;
	}

	public ScoreBoard getScoreBoardOne() {
		return scoreBoardOne;
	}

	public ScoreBoard getScoreBoardTwo() {
		return scoreBoardTwo;
	}

	public GameHud getGameHud() {
		return gameHud;
	}

	public Group getMainRoot() {
		return mainRoot;
	}

	public void setScoreBoardOne(ScoreBoard scoreBoard) {
		this.scoreBoardOne = scoreBoard;
	}

	public void setScoreBoardTwo(ScoreBoard scoreBoard) {
		this.scoreBoardTwo = scoreBoard;
	}

	public GameKeyInputManager getKeyInput() {
		return keyInput;
	}

	public void setKeyInput(GameKeyInputManager keyInput) {
		this.keyInput = keyInput;
	}

	public Pane getBaseLayer() {
		return baseLayer;
	}

	public void setBaseLayer(Pane baseLayer) {
		this.baseLayer = baseLayer;
	}

	public Pane getFirstLayer() {
		return firstLayer;
	}

	public void setFirstLayer(Pane debrisLayer) {
		this.firstLayer = debrisLayer;
	}

	public Pane getSecondLayer() {
		return secondLayer;
	}

	public void setSecondLayer(Pane bottomLayer) {
		this.secondLayer = bottomLayer;
	}

	public Pane getThirdLayer() {
		return thirdLayer;
	}

	public void setThirdLayer(Pane playfieldLayer) {
		this.thirdLayer = playfieldLayer;
	}

	public Pane getFourthLayer() {
		return fourthLayer;
	}

	public Pane getFithLayer() {
		return fithLayer;
	}

	public Pane getSixthLayer() {
		return sixthLayer;
	}

	public Pane getSeventhLayer() {
		return seventhLayer;
	}

	public void setSeventhLayer(Pane radarLayer) {
		this.seventhLayer = radarLayer;
	}

	public Pane getEighthLayer() {
		return eighthLayer;
	}

	public void setEighthLayer(Pane eighthLayer) {
		this.eighthLayer = eighthLayer;
	}

	public Pane getNinthLayer() {
		return ninthLayer;
	}

	public void setNinthLayer(Pane ninthLayer) {
		this.ninthLayer = ninthLayer;
	}

	public Pane getTenthLayer() {
		return tenthLayer;
	}

	public void setTenthLayer(Pane tenthLayer) {
		this.tenthLayer = tenthLayer;
	}

	public Pane getEleventhLayer() {
		return eleventhLayer;
	}

	public void setEleventhLayer(Pane eleventhLayer) {
		this.eleventhLayer = eleventhLayer;
	}

	public Pane getTwelfthLayer() {
		return twelfthLayer;
	}

	public void setTwelfthLayer(Pane eighthLayer) {
		this.twelfthLayer = eighthLayer;
	}

	public Pane getThirTeenthLayer() {
		return thirTeenthLayer;
	}

	public void setThirTeenthLayer(Pane ninthLayer) {
		this.thirTeenthLayer = ninthLayer;
	}

	public Pane getFourTeenthLayer() {
		return fourTeenthLayer;
	}

	public void setFourTeenthLayer(Pane tenthLayer) {
		this.fourTeenthLayer = tenthLayer;
	}

	public Pane getFadeScreenLayer() {
		return fadeScreenLayer;
	}

	public Scene getScene() {
		return scene;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public ScoreKeeper getScoreKeeper() {
		return scoreKeeper;
	}

	public GameStateID getStateID() {
		return stateID;
	}

	public void setStateID(GameStateID stateID) {
		this.stateID = stateID;
	}

	public void showCursor(boolean choice, Scene scene) {
		if (!choice)
			scene.setCursor(Cursor.NONE);

		else if (choice)
			scene.setCursor(Cursor.DEFAULT);
	}

	public int getLevelLenght() {
		return levelLenght;
	}

	public void setLevelLenght(int levelLenght) {
		this.levelLenght = levelLenght;
	}

}
