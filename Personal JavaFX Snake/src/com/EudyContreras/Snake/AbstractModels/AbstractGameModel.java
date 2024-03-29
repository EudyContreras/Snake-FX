package com.EudyContreras.Snake.AbstractModels;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledExecutorService;

import com.EudyContreras.Snake.ClassicSnake.ClassicSnakeManager;
import com.EudyContreras.Snake.ClassicSnake.ClassicSnakeSectionManager;
import com.EudyContreras.Snake.Controllers.FadeScreenController;
import com.EudyContreras.Snake.Controllers.GameDebrisController;
import com.EudyContreras.Snake.Controllers.GameObjectController;
import com.EudyContreras.Snake.EffectEmitter.RainEmitter;
import com.EudyContreras.Snake.EffectEmitter.SandEmitter;
import com.EudyContreras.Snake.FrameWork.CursorUtility;
import com.EudyContreras.Snake.FrameWork.CursorUtility.CursorID;
import com.EudyContreras.Snake.FrameWork.GameLoader;
import com.EudyContreras.Snake.FrameWork.LogicThread;
import com.EudyContreras.Snake.FrameWork.ResizeListener;
import com.EudyContreras.Snake.HudElements.CountDownScreen;
import com.EudyContreras.Snake.HudElements.EnergyBarOne;
import com.EudyContreras.Snake.HudElements.EnergyBarTwo;
import com.EudyContreras.Snake.HudElements.GameBorder;
import com.EudyContreras.Snake.HudElements.GameHud;
import com.EudyContreras.Snake.HudElements.GameOverScreen;
import com.EudyContreras.Snake.HudElements.HealthBarOne;
import com.EudyContreras.Snake.HudElements.HealthBarTwo;
import com.EudyContreras.Snake.HudElements.PauseMenu;
import com.EudyContreras.Snake.HudElements.ReadyNotification;
import com.EudyContreras.Snake.HudElements.ScoreBoard;
import com.EudyContreras.Snake.HudElements.ScoreKeeper;
import com.EudyContreras.Snake.HudElements.VictoryScreen;
import com.EudyContreras.Snake.Identifiers.GameModeID;
import com.EudyContreras.Snake.Identifiers.GameStateID;
import com.EudyContreras.Snake.ImageBanks.GameImageBank;
import com.EudyContreras.Snake.ImageBanks.GameLevelImage;
import com.EudyContreras.Snake.InputHandlers.KeyInputHandler;
import com.EudyContreras.Snake.InputHandlers.MouseInputHandler;
import com.EudyContreras.Snake.InputHandlers.TouchInputHandler;
import com.EudyContreras.Snake.PathFindingAI.AIController;
import com.EudyContreras.Snake.PlayerOne.PlayerOneManager;
import com.EudyContreras.Snake.PlayerOne.PlayerOneSectionManager;
import com.EudyContreras.Snake.PlayerTwo.PlayerTwoManager;
import com.EudyContreras.Snake.PlayerTwo.PlayerTwoSectionManager;
import com.EudyContreras.Snake.SlitherSnake.SlitherSectionManager;
import com.EudyContreras.Snake.UserInterface.MenuManager;
import com.EudyContreras.Snake.Utilities.ScreenEffectUtility;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public abstract class AbstractGameModel extends Application {

	protected GameStateID stateID;
	protected GameModeID modeID = GameModeID.LocalMultiplayer;
	protected BorderPane sceneRoot;
	protected BorderPane layoutContainer;
	protected GameLoader loader;
	protected Timeline frameGameLoop;
	protected ResizeListener sizeListener;
	protected Service<Void> backgroundThread;
	protected Task<Void> task;
	protected ScheduledExecutorService scheduledExecutor;
	protected AnimationTimer playerMovementLoop;
	protected KeyInputHandler keyInput;
	protected MouseInputHandler mouseInput;
	protected TouchInputHandler gestures;
	protected ScreenEffectUtility overlayEffect;
	protected GameObjectController objectManager;
	protected PlayerOneManager playerOneManager;
	protected PlayerTwoManager playerTwoManager;
	protected ClassicSnakeManager classicSnakeManager;
	protected GameDebrisController debrisManager;
	protected PlayerOneSectionManager sectManagerOne;
	protected PlayerTwoSectionManager sectManagerTwo;
	protected ClassicSnakeSectionManager sectManagerThree;
	protected CountDownScreen countDownScreen;
	protected AIController aiController;
	protected Group rootContainer;
	protected GameBorder gameBorder;
	protected FadeTransition fadeSplash;
	protected LogicThread thread;
	protected Thread mainThread;
	protected Stage splashWindow;
	protected MenuManager menuManager;
	protected Scene scene;
	protected Scene splashScene;
	protected Pane mainRoot;
	protected Pane root;
	protected Pane gridLayer;
	protected Pane splashLayout;
	protected Pane levelLayer;
	protected Pane snakeOneLayer;
	protected Pane snakeTwoLayer;
	protected Pane fruitLayer;
	protected Pane dirtLayer;
	protected Pane innerParticleLayer;
	protected Pane outerParticleLayer;
	protected Pane debrisLayer;
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
	protected Text FPS_TEXT;
	protected PauseMenu pauseMenu;
	protected GameImageBank imageBank;
	protected GameLevelImage levelImageBank;
	protected HealthBarOne healthBarOne;
	protected HealthBarTwo healthBarTwo;
	protected ScoreBoard scoreBoardOne;
	protected ScoreBoard scoreBoardTwo;
	protected EnergyBarOne energyBarOne;
	protected EnergyBarTwo energyBarTwo;
	protected SandEmitter sandEmitter;
	protected RainEmitter rainEmitter;
	protected VictoryScreen victoryScreen;
	protected AnimationTimer gameLoop;
	protected AnimationTimer animationLoop;
	protected AnimationTimer particleLoop;
	protected GameOverScreen gameOverScreen;
	protected ReadyNotification readyNotification;
	protected FadeScreenController fadeHandler;
	protected ScoreKeeper scoreKeeper;
	protected ImageView backgroundImage;
	protected ImageView splash;
	protected Rectangle2D bounds;
	protected GameHud gameHud;
	protected String title = "SNAKE";
	protected boolean allowStageScale = false;
	protected boolean isRunning = true;
	protected boolean gameRunning = false;
	protected boolean goToNext;
	protected boolean goToMain;
	protected int splashWidth;
	protected int splashHeight;
	protected int levelLenght;
	protected double animationWidth = 0;
	protected double animationHeight = 0;
	protected double splashFadeDuration;
	protected double splashFadeDelay;
	protected double yOffset = 0;
	protected double xOffset = 0;
	protected static Stage mainWindow;

	public AIController getAIController() {
		return aiController;
	}

	public BorderPane getRootLayer() {
		return sceneRoot;
	}

	public GameBorder getGameBorder() {
		return gameBorder;
	}

	public HealthBarOne getHealthBarOne() {
		return healthBarOne;
	}

	public HealthBarTwo getHealthBarTwo() {
		return healthBarTwo;
	}

	public PlayerOneManager getPlayerOneManager() {
		return playerOneManager;
	}

	public void setPlayerOneManager(PlayerOneManager playerManager) {
		this.playerOneManager = playerManager;
	}

	public PlayerTwoManager getPlayerTwoManager() {
		return playerTwoManager;
	}

	public void setPlayerTwoManager(PlayerTwoManager playerManager) {
		this.playerTwoManager = playerManager;
	}

	public ClassicSnakeManager getClassicSnakeManager() {
		return classicSnakeManager;
	}

	public void setClassicSnakeManager(ClassicSnakeManager classicSnakeManager) {
		this.classicSnakeManager = classicSnakeManager;
	}

	public ClassicSnakeSectionManager getSectManagerThree() {
		return sectManagerThree;
	}

	public void setSectManagerThree(ClassicSnakeSectionManager sectManagerThree) {
		this.sectManagerThree = sectManagerThree;
	}

	public SlitherSectionManager getSlitherSectManager() {
		return null;
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

	public EnergyBarOne getEnergyBarOne() {
		return energyBarOne;
	}

	public void setEnergyBarOne(EnergyBarOne energyBarOne) {
		this.energyBarOne = energyBarOne;
	}

	public EnergyBarTwo getEnergyBarTwo() {
		return energyBarTwo;
	}

	public void setEnergyBarTwo(EnergyBarTwo energyBarTwo) {
		this.energyBarTwo = energyBarTwo;
	}

	public PauseMenu getPauseMenu() {
		return pauseMenu;
	}

	public CountDownScreen getCountDownScreen() {
		return countDownScreen;
	}

	public ReadyNotification getReadyNotification() {
		return readyNotification;
	}

	public MenuManager getMainMenu() {
		return menuManager;
	}

	public void setMainMenu(MenuManager menuManager) {
		this.menuManager = menuManager;
	}

	public Stage getMainWindow() {
		return mainWindow;
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

	public Pane getBaseLayer() {
		return gridLayer;
	}

	public Pane getSnakeOneLayer() {
		return snakeOneLayer;
	}

	public void setSnakeOneLayer(Pane snakeOneLayer) {
		this.snakeOneLayer = snakeOneLayer;
	}

	public Pane getSnakeTwoLayer() {
		return snakeTwoLayer;
	}

	public void setSnakeTwoLayer(Pane snakeTwoLayer) {
		this.snakeTwoLayer = snakeTwoLayer;
	}

	public Pane getLevelLayer() {
		return levelLayer;
	}

	public GameObjectController getGameObjectController() {
		return objectManager;
	}

	public void setGameObjectController(GameObjectController objectManager) {
		this.objectManager = objectManager;
	}

	public GameDebrisController getDebrisManager() {
		return debrisManager;
	}

	public PlayerOneSectionManager getSectManagerOne() {
		return sectManagerOne;
	}

	public PlayerTwoSectionManager getSectManagerTwo() {
		return sectManagerTwo;
	}

	public void setDebrisManager(GameDebrisController debrisManager) {
		this.debrisManager = debrisManager;
	}

	public FadeScreenController getFadeScreenHandler() {
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

	public Pane getMainRoot() {
		return mainRoot;
	}

	public void setScoreBoardOne(ScoreBoard scoreBoard) {
		this.scoreBoardOne = scoreBoard;
	}

	public void setScoreBoardTwo(ScoreBoard scoreBoard) {
		this.scoreBoardTwo = scoreBoard;
	}

	public KeyInputHandler getKeyInput() {
		return keyInput;
	}

	public void setKeyInput(KeyInputHandler keyInput) {
		this.keyInput = keyInput;
	}

	public Pane getFruitLayer() {
		return fruitLayer;
	}

	public Pane getDirtLayer() {
		return dirtLayer;
	}

	public void setDirtLayer(Pane dirtLayer) {
		this.dirtLayer = dirtLayer;
	}

	public Pane getInnerParticleLayer() {
		return innerParticleLayer;
	}

	public void setInnerParticleLayer(Pane particleLayer) {
		this.innerParticleLayer = particleLayer;
	}

	public Pane getOuterParticleLayer() {
		return outerParticleLayer;
	}

	public void setOuterParticleLayer(Pane outerParticleLayer) {
		this.outerParticleLayer = outerParticleLayer;
	}

	public Pane getDebrisLayer() {
		return debrisLayer;
	}

	public void setDebrisLayer(Pane debrisLayer) {
		this.debrisLayer = debrisLayer;
	}

	public void setBaseLayer(Pane baseLayer) {
		this.fruitLayer = baseLayer;
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

	public GameModeID getModeID() {
		return modeID;
	}

	public void setModeID(GameModeID modeID) {
		this.modeID = modeID;
	}

	public ScreenEffectUtility getOverlayEffect() {
		return overlayEffect;
	}

	public void showCursor(boolean choice, Scene scene) {
		if (!choice)
			CursorUtility.setCursor(CursorID.NONE, scene);

		else if (choice)
			CursorUtility.setCursor(CursorID.NORMAL, scene);
	}

	public int getLevelLenght() {
		return levelLenght;
	}

	public Timeline getGameLoop() {
		return frameGameLoop;
	}

	public void setLevelLenght(int levelLenght) {
		this.levelLenght = levelLenght;
	}

	public void backgroundWorker() {
		backgroundThread = new Service<Void>() {
			@Override
			protected Task<Void> createTask() {
				return new Task<Void>() {
					@Override
					protected Void call() throws Exception {
						// Background work
						final CountDownLatch latch = new CountDownLatch(1);

						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								try {
									// FX Stuff done here
								} finally {
									latch.countDown();
								}
							}
						});
						latch.await();
						// Keep with the background work
						return null;
					}
				};
			}
		};
		backgroundThread.start();
	}

}
