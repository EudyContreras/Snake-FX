package com.SnakeGame.FrameWork;

import com.SnakeGame.AbstractModels.AbstractGameModel;
import com.SnakeGame.DebrisEffects.RainEmitter;
import com.SnakeGame.DebrisEffects.SandEmitter;
import com.SnakeGame.HudElements.EnergyBarOne;
import com.SnakeGame.HudElements.EnergyBarTwo;
import com.SnakeGame.HudElements.GameHud;
import com.SnakeGame.HudElements.GameOverScreen;
import com.SnakeGame.HudElements.HealthBarOne;
import com.SnakeGame.HudElements.HealthBarTwo;
import com.SnakeGame.HudElements.PauseMenu;
import com.SnakeGame.HudElements.ScoreBoard;
import com.SnakeGame.HudElements.ScoreKeeper;
import com.SnakeGame.HudElements.VictoryScreen;
import com.SnakeGame.IDEnums.GameStateID;
import com.SnakeGame.ImageBanks.GameImageBank;
import com.SnakeGame.ImageBanks.GameLevelImage;
import com.SnakeGame.PlayerOne.PlayerOne;
import com.SnakeGame.PlayerOne.PlayerOneSectionManager;
import com.SnakeGame.PlayerTwo.PlayerTwo;
import com.SnakeGame.PlayerTwo.PlayerTwoSectionManager;
import com.SnakeGame.UserInterface.MenuMain;
import com.SnakeGame.Utilities.ScreenEffectUtility;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * This class is the core of the game. Here is where every object connects with
 * every object. all classes used by the game will communicate with this class
 * and therefore with every other class. This class takes care of the main loop
 * of the game which is in charge of rendering and moving mobs from a to b as
 * you can see most of the rendering is being handled on a single thread, This
 * class also monitors the amount of objects used in the game,
 *
 * @author Eudy Contreras
 *
 */
public class GameManager extends AbstractGameModel{


	public void start(Stage primaryStage) {
		GameLoader.scaleResolution();
		GameLoader.scalePlayerSize();
		ScaleX = GameLoader.ResolutionScaleX;
		ScaleY = GameLoader.ResolutionScaleY;
		mainWindow = primaryStage;
		initialize();
		showSplashScreen();
	}

	public void initSplash() {
		splashFadeDelay = 0;
		splashFadeDuration = 1;
		splash = new ImageView(GameImageBank.splash_screen);
		splashWidth = (int) splash.getImage().getWidth();
		splashHeight = (int) splash.getImage().getHeight();
		splashLayout = new StackPane();
		splashLayout.getChildren().add(splash);
		splashLayout.setBackground(Background.EMPTY);
		splashLayout.setEffect(new DropShadow());
	}

	public void showSplashScreen() {
		splashScene = new Scene(splashLayout);
		splashScene.setFill(Color.TRANSPARENT);
		bounds = Screen.getPrimary().getBounds();
		mainWindow.initStyle(StageStyle.TRANSPARENT);
		mainWindow.setScene(splashScene);
		mainWindow.setX(bounds.getMinX() + bounds.getWidth() / 2 - splashWidth / 2);
		mainWindow.setY(bounds.getMinY() + bounds.getHeight() / 2 - splashHeight / 2);
		mainWindow.show();
		fadeSplash = new FadeTransition(Duration.seconds(splashFadeDuration), splashLayout);
		fadeSplash.setDelay(Duration.seconds(splashFadeDelay));
		fadeSplash.setFromValue(1.0);
		fadeSplash.setToValue(0.0);
		fadeSplash.setOnFinished(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent actionEvent) {
				mainWindow.hide();
				mainWindow = null;
				mainWindow = new Stage();
				if (!gameRunning) {
					initGame();
					gameRunning = true;
				}
			}
		});
		fadeSplash.play();
	}

	public void initGame() {
		fadeSplash = null;
		splashLayout = null;
		splash = null;
		splashScene = null;
		getGameRoot().getChildren().add(backgroundImage);
		getGameRoot().getChildren().add(baseLayer);
		getGameRoot().getChildren().add(dirtLayer);
		getGameRoot().getChildren().add(debrisLayer);
		getGameRoot().getChildren().add(innerParticleLayer);
		getGameRoot().getChildren().add(snakeOneLayer);
		getGameRoot().getChildren().add(snakeTwoLayer);
		getGameRoot().getChildren().add(firstLayer);
		getGameRoot().getChildren().add(secondLayer);
		getGameRoot().getChildren().add(thirdLayer);
		getGameRoot().getChildren().add(fourthLayer);
		getGameRoot().getChildren().add(fithLayer);
		getGameRoot().getChildren().add(sixthLayer);
		getGameRoot().getChildren().add(seventhLayer);
		getGameRoot().getChildren().add(eighthLayer);
		getGameRoot().getChildren().add(ninthLayer);
		getGameRoot().getChildren().add(outerParticleLayer);
		getGameRoot().getChildren().add(tenthLayer);
		getGameRoot().getChildren().add(eleventhLayer);
		getGameRoot().getChildren().add(twelfthLayer);
		mainRoot.getChildren().add(getGameRoot());
		scene.setFill(Color.BLACK);
		loader.loadPixelMap();
		loader.loadPlayerTwo();
		loader.loadPlayerOne();
		rainEmitter = new RainEmitter(this, 0, -200, 75, 1, 1);
		sandEmitter = new SandEmitter(this, -200, 0, 1, 1);
		setEnergyBarOne(new EnergyBarOne(this, 25 /ScaleX, 10/ScaleY, 350/ScaleX, 15/ScaleY));
		setHealthBarOne(new HealthBarOne(this, 25 / ScaleX, 35/ScaleY,
				350 / ScaleX, 30 / ScaleY));
		setEnergyBarTwo(new EnergyBarTwo(this,GameSettings.WIDTH - 375 / ScaleX, 10/ScaleY, 350 / ScaleX, 15/ScaleY));
		setHealthBarTwo(new HealthBarTwo(this,GameSettings.WIDTH - 375 / ScaleX,
				35 / ScaleY, 350 / ScaleX,30 / ScaleY));
		pauseMenu = new PauseMenu(this,0,0,GameSettings.WIDTH,300);
		gameHud = new GameHud(this, -5, -10, GameSettings.WIDTH + 10, 100 / ScaleY);
		scoreKeeper = new ScoreKeeper(this, GameSettings.APPLE_COUNT, (GameSettings.WIDTH / 2) - 30/ ScaleX,
				45 / ScaleY, GameSettings.WIDTH / 2 - 680/ScaleX / 2 , 15/ScaleY,
				680/ScaleX,85 / ScaleY);
		scoreBoardOne = new ScoreBoard("", this, healthBarOne.getX() + healthBarOne.getWidth() + 100/ScaleX,
				50/ScaleY, Color.RED);
		scoreBoardTwo = new ScoreBoard("", this, healthBarTwo.getX() - healthBarTwo.getWidth()/2 +25/ScaleX,
				50/ScaleY, Color.RED);
		victoryScreen = new VictoryScreen(this, GameImageBank.level_complete_board, 800/ScaleX, 450/ScaleY);
		gameOverScreen = new GameOverScreen(this, GameImageBank.game_over_board, 800/ScaleX, 450/ScaleY);
		processGameInput();
		processGestures();
		mainMenu.setupMainMenu();
		mainWindow.setScene(scene);
		mainWindow.setWidth(GameSettings.WIDTH);
		mainWindow.setHeight(GameSettings.HEIGHT);
		mainWindow.setResizable(false);
		mainWindow.setFullScreen(true);
		mainWindow.setFullScreenExitHint("");
		mainWindow.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		mainWindow.setTitle(title);
		mainWindow.show();
		Platform.setImplicitExit(false);
		translateObjects(mainRoot.getChildren());
		pauseGame();
		objectChecker();
		frameBaseGameLoop();
		//playerMovementLoop();

	}

	public Image copyBackground(Group content) {
		Image image = content.snapshot(new SnapshotParameters(), null);
		return image;
	}

	public Image copyBackground(Pane content) {
		Image image = content.snapshot(new SnapshotParameters(), null);
		return image;
	}

	private void initialize() {
		frameGameLoop = new Timeline();
		imageBank = new GameImageBank();
		initSplash();
		levelImageBank = new GameLevelImage();
		mainRoot = new Group();
		root = new Pane();
		thread = new LogicThread(this);
		mainMenu = new MenuMain(this);
		backgroundImage = new ImageView(GameLevelImage.desertBackground);
		canvas = new Canvas(GameSettings.WIDTH, GameSettings.HEIGHT);
		scene = new Scene(mainMenu.getMenuRoot(), GameSettings.WIDTH, GameSettings.HEIGHT);
		gc = canvas.getGraphicsContext2D();
		baseLayer = new Pane();
		dirtLayer = new Pane();
		debrisLayer = new Pane();
		snakeOneLayer = new Pane();
		snakeTwoLayer = new Pane();
		firstLayer = new Pane();
		secondLayer = new Pane();
		thirdLayer = new Pane();
		fadeScreenLayer = new Pane();
		fourthLayer = new Pane();
		fithLayer = new Pane();
		sixthLayer = new Pane();
		seventhLayer = new Pane();
		eighthLayer = new Pane();
		ninthLayer = new Pane();
		tenthLayer = new Pane();
		eleventhLayer = new Pane();
		twelfthLayer = new Pane();
		thirTeenthLayer = new Pane();
		fourTeenthLayer = new Pane();
		innerParticleLayer = new Pane();
		outerParticleLayer = new Pane();
		levelLayer = new Pane();
		loader = new GameLoader(this);
		overlayEffect = new ScreenEffectUtility(this,getGameRoot());
		fadeHandler = new FadeScreenHandler(this);
		objectManager = new ObjectManager(this);
		playerOneManager = new PlayerOneManager(this);
		playerTwoManager = new PlayerTwoManager(this);
		sectManagerOne = new PlayerOneSectionManager(this);
		sectManagerTwo = new PlayerTwoSectionManager(this);
		keyInput = new KeyInputManager();
		gestures = new GestureInputManager();
		mouseInput = new MouseInputManager();
		debrisManager = new GameDebrisManager(this);

	}

	public static double ScaleX(double value) {
		double newSize = value/ScaleX;
		return newSize;
	}

	public static double ScaleY(double value) {
		double newSize = value/ScaleY;
		return newSize;
	}

	public void resumeGame() {
		if (GameSettings.RENDER_GAME == true)
			return;
		setStateID(GameStateID.GAMEPLAY);
		GameSettings.RENDER_GAME = true;
	}

	public void pauseGame() {
		if (GameSettings.RENDER_GAME == false)
			return;
		GameSettings.RENDER_GAME = false;
	}

	public void pauseAndResume() {
		if (GameSettings.RENDER_GAME == false)
			GameSettings.RENDER_GAME = true;
		else if (GameSettings.RENDER_GAME == true)
			GameSettings.RENDER_GAME = false;
	}

	public void startThreads() {
		gameLoop.start();
	}

	public void stopThreads() {
		gameLoop.stop();
	}

	public void processGameInput() {
		keyInput.processInput(this, loader.getPlayerOne(), loader.getPlayerTwo(), loader.getSlither(), scene);
	}
	public void processGestures(){
		pauseMenu.processTouch();
		gestures.processGestures(this);
	}
	public void setPerformance(Pane pane){
		pane.setCache(true);
		pane.setCacheShape(true);
		pane.setCacheHint(CacheHint.SPEED);
	}
	public void closeGame() {
		pauseGame();
		Platform.exit();
	}

	public void setBackgroundImage(Image image) {
		backgroundImage.setImage(image);
	}

	/**
	 * This is the game loop. virtually every object in the game is processed,
	 * rendered, and moved here by calling the update methods of every manager.
	 * The animation timer is frame dynamic meaning it will capped the frame
	 * rate according to the performance of the machine is running on. This
	 * method also keeps track of the frame rate
	 */
	public synchronized void gameLoop() {

		gameLoop = new AnimationTimer() {

			long lastTime = System.nanoTime();
			long nanoSecond = 1000000000;
			long currentTime = 0;
			double delta = 0;
			double FPS = 0;

			public synchronized void handle(long now) {
				FPS++;
				currentTime = now;
				delta += currentTime - lastTime;
				if (!GameSettings.RENDER_GAME) {
					mainMenu.transition();

				}
				if (GameSettings.RENDER_GAME) {
					drawOverlay(gc);
					fadeHandler.innerFade_update();
					fadeHandler.outer_fade_update();
					pauseMenu.updateTouchPanel();
					gameHud.updateHudBars();
					victoryScreen.swipeRight();
					gameOverScreen.swipeDown();
					gameOverScreen.checkStatus();
					scoreKeeper.keepCount();
					for (int speed = 0; speed < GameSettings.PLAYER_ONE_SPEED; speed += 1) {
						playerOneManager.updateAllMovement();
						sectManagerOne.updateAllMovement(gc, now);

					}
					for (int speed = 0; speed < GameSettings.PLAYER_TWO_SPEED; speed += 1) {
						playerTwoManager.updateAllMovement();
						sectManagerTwo.updateAllMovement(gc, now);
					}
					objectManager.updateAll(gc, now);
					playerOneManager.updateAllLogic(gc, now);
					playerTwoManager.updateAllLogic(gc, now);
					sectManagerTwo.updateAllLogic(gc, now);
					sectManagerOne.updateAllLogic(gc, now);
					debrisManager.updateDebris(gc);
					debrisManager.updateParticles(gc);
					loader.updateLevelObjects();
					sandEmitter.move();
					rainEmitter.move();
					if(GameSettings.SAND_STORM){
						sandEmitter.emit();
					}
					if(GameSettings.RAIN_STORM){
						rainEmitter.emit();
					}
					if (loader.getPlayerOne() != null && getHealthBarOne() != null) {
						getHealthBarOne().depleteHealth();
						getHealthBarOne().regerateHealth();
					}
					if (loader.getPlayerTwo() != null && getHealthBarTwo() != null) {
						getHealthBarTwo().depleteHealth();
						getHealthBarTwo().regerateHealth();
					}
					if (scoreBoardOne != null) {
						scoreBoardOne.hide();
					}
					if (scoreBoardTwo != null) {
						scoreBoardTwo.hide();
					}
					if (!debrisLayer.getChildren().isEmpty()) {
						if (debrisLayer.getChildren().size() >= GameSettings.DEBRIS_LIMIT) {
							debrisLayer.getChildren().remove(0,10);
						}
					}
					if (!innerParticleLayer.getChildren().isEmpty()) {
						if (innerParticleLayer.getChildren().size() >= GameSettings.DEBRIS_LIMIT*0.7) {
							innerParticleLayer.getChildren().remove(0);
						}
					}
				}

				if (delta > nanoSecond) {
					TextFPS.setText("FPS :" + FPS);
					delta -= nanoSecond;
					FPS = 0;
				}
				lastTime = currentTime;
			}

		};
		gameLoop.start();
	}

	/**
	 * As the name says, this is a frame based game loop. virtually every object
	 * in the game is processed, rendered, and moved here by calling the update
	 * methods of every manager. The time line is controlled by keyFrames which
	 * specify the speed, delays and more. this loop gives a bit more control to
	 * the user.
	 */
	public synchronized void frameBaseGameLoop() {
		Timeline gameLoop = new Timeline();
		gameLoop.setCycleCount(Timeline.INDEFINITE);

		KeyFrame keyFrame = new KeyFrame(Duration.seconds(GameSettings.FRAMECAP), // 60FPS

				new EventHandler<ActionEvent>() {
					long startTime = System.currentTimeMillis();
					long cummulativeTime = startTime;
					long lastTime = System.nanoTime();
					long nanoSecond = 1000000000;
					long currentTime = 0;
					long timePassed = 0;
					long now = 0;
					double delta = 0;
					double FPS = 0;

					public synchronized void handle(ActionEvent e) {
						FPS++;
						now = System.nanoTime();
						currentTime = now;
						delta += currentTime - lastTime;
						timePassed = System.currentTimeMillis() - cummulativeTime;
						cummulativeTime += timePassed;

						if (!GameSettings.RENDER_GAME) {
							mainMenu.transition();

						}
						if (GameSettings.RENDER_GAME) {
							drawOverlay(gc);
							overlayEffect.updateEffect();
							fadeHandler.innerFade_update();
							fadeHandler.outer_fade_update();
							pauseMenu.updateTouchPanel();
							gameHud.updateHudBars();
							victoryScreen.swipeRight();
							gameOverScreen.swipeDown();
							gameOverScreen.checkStatus();
							scoreKeeper.keepCount();
							objectManager.updateAll(gc, timePassed);
							for (int speed = 0; speed < GameSettings.PLAYER_ONE_SPEED; speed += 1) {
								playerOneManager.updateAllMovement();
								sectManagerOne.updateAllMovement(gc, timePassed);
							}
							for (int speed = 0; speed < GameSettings.PLAYER_TWO_SPEED; speed += 1) {
								playerTwoManager.updateAllMovement();
								sectManagerTwo.updateAllMovement(gc, timePassed);
							}
							playerOneManager.updateAllLogic(gc, timePassed);
							playerTwoManager.updateAllLogic(gc, timePassed);
							sectManagerOne.updateAllLogic(gc, timePassed);
							sectManagerTwo.updateAllLogic(gc, timePassed);
							debrisManager.updateDebris(gc);
							debrisManager.updateParticles(gc);
							loader.updateLevelObjects();
							sandEmitter.move();
							rainEmitter.move();
							if(GameSettings.SAND_STORM){
								sandEmitter.emit();
							}
							if(GameSettings.RAIN_STORM){
								rainEmitter.emit();
							}
							if (loader.getPlayerOne() != null && getHealthBarOne() != null) {
								getHealthBarOne().depleteHealth();
								getHealthBarOne().regerateHealth();
							}
							if (loader.getPlayerTwo() != null && getHealthBarTwo() != null) {
								getHealthBarTwo().depleteHealth();
								getHealthBarTwo().regerateHealth();
							}
							if (loader.getPlayerOne() != null && getEnergyBarOne() != null) {
								getEnergyBarOne().deplete();
								getEnergyBarOne().regerate();
							}
							if (loader.getPlayerTwo() != null && getEnergyBarTwo() != null) {
								getEnergyBarTwo().deplete();
								getEnergyBarTwo().regerate();
							}
							if (scoreBoardOne != null) {
								scoreBoardOne.hide();
							}
							if (scoreBoardTwo != null) {
								scoreBoardTwo.hide();
							}
							if (!debrisLayer.getChildren().isEmpty()) {
								if (debrisLayer.getChildren().size() >= GameSettings.DEBRIS_LIMIT) {
									debrisLayer.getChildren().remove(0,10);
								}
							}
							if (!innerParticleLayer.getChildren().isEmpty()) {
								if (innerParticleLayer.getChildren().size() >= GameSettings.DEBRIS_LIMIT*0.7) {
									innerParticleLayer.getChildren().remove(0);
								}
							}
						}
						if (delta > nanoSecond) {
							TextFPS.setText("FPS :" + FPS);
							delta -= nanoSecond;
							FPS = 0;
						}
						lastTime = currentTime;
					}
				});
		gameLoop.getKeyFrames().add(keyFrame);
		gameLoop.play();
	}
	public void playerMovementLoop(){
		new SpeedController().playerMovementLoop();
	}

	public class SpeedController {
		public void playerMovementLoop() {

			playerMovementLoop = new AnimationTimer() {

				private long startTime = System.currentTimeMillis();
				private long cummulativeTime = startTime;

				public void handle(long now) {
					long timePassed = System.currentTimeMillis() - cummulativeTime;
					cummulativeTime += timePassed;
					if (GameSettings.RENDER_GAME) {
						for (int speed = 0; speed < GameSettings.PLAYER_ONE_SPEED; speed += 1) {
							playerOneManager.updateAllMovement();
							sectManagerOne.updateAllMovement(gc, timePassed);
							sectManagerTwo.updateAllMovement(gc, timePassed);

						}
					}
				}
			};
			playerMovementLoop.start();
		}
	}
	/**
	 * This method is used to perform a frame base animation on a sequence of
	 * images.
	 */
	public void animationLoop() {

		animationLoop = new AnimationTimer() {

			long startTime = System.currentTimeMillis();
			long cummulativeTime = startTime;

			public void handle(long now) {
				long timePassed = System.currentTimeMillis() - cummulativeTime;
				cummulativeTime += timePassed;

				if (GameSettings.RENDER_GAME) {
					playerOneManager.updateAnimation(timePassed);
				}
			}
		};
		animationLoop.start();
	}

	/**
	 * This is a physics loop which can be used to render physics on its own
	 * separate thread
	 */
	public void physicsLoop() {
		particleLoop = new AnimationTimer() {
			public void handle(long now) {
				if (GameSettings.RENDER_GAME) {
					objectManager.addPhysics();
					objectManager.checkCollisions();

				}
			}
		};
		particleLoop.start();
	}

	/**
	 * Method used to draw any given overlay over the game. this method is
	 * currently used for debugging
	 *
	 * @param GraphicsContext
	 *            is used to render the overlay on a canvas layer
	 */
	public void drawOverlay(GraphicsContext gc) {
		if (GameSettings.DEBUG_MODE) {
			gc.setFill(Color.WHITE);
			gc.fillRect(0, 0, GameSettings.WIDTH, GameSettings.HEIGHT);
		}
	}

	/**
	 * The level thread function is used for two things this thread is frame
	 * controlled and can provide updates on the current amount of children
	 * nodes being used by the layers used in game and the amount of objects in
	 * each list This thread can also be used to procedurally create a level
	 * which will then put objects on the level based on the set speed
	 */
	public void objectChecker() {
		if(GameSettings.OBJECT_TRACKER){
		Timeline levelUpdateLoop = new Timeline();
		levelUpdateLoop.setCycleCount(Timeline.INDEFINITE);

		KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.512), new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {

				// loader.procedurallyCreateLevel();
				System.out.println("Amount of objects in dirt layer: " + dirtLayer.getChildren().size());
				System.out.println("Amount of objects in debris layer: " 	+ debrisLayer.getChildren().size());
				System.out.println("Amount of objects in lower particle layer: " + innerParticleLayer.getChildren().size());
				System.out.println("Amount of objects in higher particle layer: " + outerParticleLayer.getChildren().size());
				System.out.println("Amount of objects in all layers: " + getGameRoot().getChildren().size());
				System.out.println();
				System.out.println();
				System.out.println("Amount of objects in player manager: " + playerOneManager.getObjectList().size());
				System.out.println("Amount of objects in object manager: " + objectManager.getObjectList().size());
				System.out.println("Amount of objects in debris manager: " + debrisManager.getDebrisList().size());
				System.out.println("Amount of objects in particle manager: " + debrisManager.getParticleList().size());
				System.out.println("Amount of objects in tile manager: " + loader.getTileManager().getTile().size());
				System.out.println("Amount of objects in block manager: " + loader.getTileManager().getBlock().size());
				System.out.println("Amount of objects in trap manager: " +loader.getTileManager().getTrap().size());
				System.out.println();
				System.out.println("---------------------------------------------------------------------------");

			}
		});
		levelUpdateLoop.getKeyFrames().add(keyFrame);
		levelUpdateLoop.play();
		}
	}


	/**
	 * Method used to translate objects on the screen nodes translated will stay
	 * on the visual range of the screen useful for things like heads up
	 * displays and and other nodes that need to be constantly visible by the
	 * player
	 */
	public void translateObjects(ObservableList<Node> rootPane) {
		TextFPS = new Text("FPS : ");
		TextFPS.setX(ScaleX(20));
		TextFPS.setY(ScaleY(80));
		TextFPS.setOpacity(0.5);
		TextFPS.setFill(Color.WHITE);
		TextFPS.setFont(Font.font("AERIAL", FontWeight.BOLD, ScaleX(20)));
		rootPane.add(fadeScreenLayer);
		rootPane.add(thirTeenthLayer);
		rootPane.add(fourTeenthLayer);
		mainRoot.getChildren().add(TextFPS);
	}

	public void restart() {
		clearAll();
		PlayerOne.NUMERIC_ID = 0;
		PlayerOne.DEAD = false;
		PlayerOne.MOUTH_CLOSE = true;
		PlayerOne.KEEP_MOVING = true;
		PlayerTwo.NUMERIC_ID = 0;
		PlayerTwo.DEAD = false;
		PlayerTwo.MOUTH_CLOSE = true;
		victoryScreen.removeBlur();
		victoryScreen.removeBoard();
		gameOverScreen.removeBlur();
		gameOverScreen.removeBoard();
		scoreBoardOne.resetScore();
		scoreBoardTwo.resetScore();
		scoreKeeper.resetCount();
		scoreKeeper.resetTimer();
		scoreBoardOne.show();
		scoreBoardTwo.show();
		gameHud.show();
		gameHud.swipeUp();
		loader.killPlayerOne();
		loader.killPlayerTwo();
		loader.loadPlayerOne();
		loader.loadPlayerTwo();
		getGameRoot().setEffect(null);
		getHealthBarOne().show();
		getHealthBarTwo().show();
		getHealthBarOne().refill();
		getHealthBarTwo().refill();
		getHealthBarOne().setPlayer();
		getHealthBarTwo().setPlayer();
		PlayerOne.LEVEL_COMPLETED = false;
		PlayerTwo.LEVEL_COMPLETED = false;
		loader.loadPixelMap();
		showCursor(false, getScene());
		processGameInput();
		processGestures();
		setStateID(GameStateID.GAMEPLAY);
	}
	public void goToNext() {
		clearAll();
		PlayerOne.NUMERIC_ID = 0;
		PlayerOne.DEAD = false;
		PlayerOne.MOUTH_CLOSE = true;
		PlayerOne.KEEP_MOVING = true;
		PlayerTwo.NUMERIC_ID = 0;
		PlayerTwo.DEAD = false;
		PlayerTwo.MOUTH_CLOSE = true;
		victoryScreen.removeBlur();
		victoryScreen.removeBoard();
		gameOverScreen.removeBlur();
		gameOverScreen.removeBoard();
		scoreBoardOne.resetScore();
		scoreBoardTwo.resetScore();
		scoreKeeper.resetCount();
		scoreKeeper.resetTimer();
		scoreBoardOne.show();
		scoreBoardTwo.show();
		gameHud.show();
		gameHud.swipeUp();
		loader.killPlayerOne();
		loader.killPlayerTwo();
		loader.loadPlayerOne();
		loader.loadPlayerTwo();
		getGameRoot().setEffect(null);
		getHealthBarOne().show();
		getHealthBarTwo().show();
		getHealthBarOne().refill();
		getHealthBarTwo().refill();
		getHealthBarOne().setPlayer();
		getHealthBarTwo().setPlayer();
		PlayerOne.LEVEL_COMPLETED = false;
		PlayerTwo.LEVEL_COMPLETED = false;
		loader.switcLevel();
		showCursor(false, getScene());
		processGameInput();
		processGestures();
	}
	public void reset() {
		clearAll();
		PlayerOne.NUMERIC_ID = 0;
		PlayerOne.DEAD = false;
		PlayerOne.MOUTH_CLOSE = true;
		PlayerOne.KEEP_MOVING = true;
		PlayerTwo.NUMERIC_ID = 0;
		PlayerTwo.DEAD = false;
		PlayerTwo.MOUTH_CLOSE = true;
		victoryScreen.removeBlur();
		victoryScreen.removeBoard();
		gameOverScreen.removeBlur();
		gameOverScreen.removeBoard();
		scoreBoardOne.resetScore();
		scoreBoardTwo.resetScore();
		scoreKeeper.resetCount();
		scoreKeeper.resetTimer();
		scoreBoardOne.show();
		scoreBoardTwo.show();
		gameHud.show();
		gameHud.swipeUp();
		loader.killPlayerOne();
		loader.killPlayerTwo();
		loader.loadPlayerOne();
		loader.loadPlayerTwo();
		getGameRoot().setEffect(null);
		getHealthBarOne().show();
		getHealthBarTwo().show();
		getHealthBarOne().refill();
		getHealthBarTwo().refill();
		getHealthBarOne().setPlayer();
		getHealthBarTwo().setPlayer();
		PlayerOne.LEVEL_COMPLETED = false;
		PlayerTwo.LEVEL_COMPLETED = false;
		loader.loadPixelMap();
		processGameInput();
		processGestures();
		getMainMenu().setMainMenu();
	}

	public void clearAll() {
		GameSettings.PLAYER_ONE_SIZE = 30;
		GameSettings.PLAYER_TWO_SIZE = 30;
		GameSettings.PLAYER_ONE_SPEED = 6.0;
		GameSettings.PLAYER_TWO_SPEED = 6.0;
		GameLoader.scaleResolution();
		GameLoader.scalePlayerSize();
		baseLayer.getChildren().clear();
		dirtLayer.getChildren().clear();
		debrisLayer.getChildren().clear();
		innerParticleLayer.getChildren().clear();
		outerParticleLayer.getChildren().clear();
		snakeOneLayer.getChildren().clear();
		snakeTwoLayer.getChildren().clear();
		firstLayer.getChildren().clear();
		secondLayer.getChildren().clear();
		thirdLayer.getChildren().clear();
		fourthLayer.getChildren().clear();
		fithLayer.getChildren().clear();
		sixthLayer.getChildren().clear();
		seventhLayer.getChildren().clear();
		eighthLayer.getChildren().clear();
		levelLayer.getChildren().clear();
		playerOneManager.clearAll();
		playerTwoManager.clearAll();
		debrisManager.clearAll();
		objectManager.clearAll();
		sectManagerOne.clearAll();
		sectManagerTwo.clearAll();
		loader.clearTiles();

	}
	public void removePlayers() {

		fithLayer.getChildren().clear();
		fourthLayer.getChildren().clear();
		playerOneManager.clearAll();
		playerTwoManager.clearAll();
		objectManager.clearAll();
		sectManagerOne.clearAll();
		sectManagerTwo.clearAll();



	}

	public void allowMouseInput(boolean choice) {
		if (choice)
			mouseInput.processInput(this, getGameLoader().getPlayerOne(), getGameLoader().getPlayerTwo(), scene);
	}
	public static void main(String[] args) {
		launch(args);
	}



}
