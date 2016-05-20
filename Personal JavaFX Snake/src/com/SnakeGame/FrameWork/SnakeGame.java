package com.SnakeGame.FrameWork;

import com.SnakeGame.HudElements.EnergyMeter;
import com.SnakeGame.HudElements.GameHud;
import com.SnakeGame.HudElements.GameOverScreen;
import com.SnakeGame.HudElements.HealthBarOne;
import com.SnakeGame.HudElements.HealthBarTwo;
import com.SnakeGame.HudElements.ScoreBoard;
import com.SnakeGame.HudElements.ScoreKeeper;
import com.SnakeGame.HudElements.VictoryScreen;
import com.SnakeGame.ImageBanks.GameImageBank;
import com.SnakeGame.ImageBanks.GameLevelImage;
import com.SnakeGame.Interface.MenuMain;
import com.SnakeGame.Particles.GameDebrisManager;
import com.SnakeGame.Particles.SandEmitter;
import com.SnakeGame.PlayerOne.PlayerOne;
import com.SnakeGame.PlayerOne.PlayerOneSectionManager;
import com.SnakeGame.PlayerTwo.PlayerTwo;
import com.SnakeGame.PlayerTwo.PlayerTwoSectionManager;
import com.SnakeGame.SlitherSnake.SlitherManager;
import com.SnakeGame.SlitherSnake.SlitherSectionManager;
import com.SnakeGame.Utilities.ImageUtility;
import com.SnakeGame.Utilities.ScreenOverlay;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
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
import javafx.scene.shape.Rectangle;
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
public class SnakeGame extends Application implements Runnable {

	private GameLoader loader;
	private GameKeyInputManager keyInput;
	private GameMouseInputManager mouseInput;
	private GameGestureInputManager gestures;
	private GraphicsContext gc;
	private GameObjectManager objectManager;
	private SlitherManager slitherManager;
	private GameDebrisManager debrisManager;
	private PlayerOneSectionManager sectManagerOne;
	private PlayerTwoSectionManager sectManagerTwo;;
	private SlitherSectionManager sectManagerThree;
	private FadeTransition fadeSplash;
	private Stage mainWindow;
	private MenuMain mainMenu;
	private Scene scene;
	private Scene splashScene;
	private Group mainRoot;
	private Pane root;
	private Canvas canvas;
	private Pane splashLayout;
	private Pane playfieldLayer;
	private Pane particleLayer;
	private Pane debrisLayer;
	private Pane bottomLayer;
	private Pane levelLayer;
	private	Pane fadeScreen;
	private Pane snakeHead;
	private Pane snakeBody;
	private Pane overlay;
	private Text TextFPS;
	public GameImageBank imageBank;
	public GameLevelImage levelImageBank;
	private HealthBarOne healthBarOne;
	private HealthBarTwo healthBarTwo;
	private ScoreBoard scoreBoard;
	private ScoreBoard scoreBoard2;
	private EnergyMeter energyMeter;
	private SandEmitter sandEmitter;
	private VictoryScreen victoryScreen;
	private AnimationTimer gameLoop;
	private AnimationTimer animationLoop;
	private AnimationTimer particleLoop;
	private GameOverScreen gameOverScreen;
	private ScreenOverlay postEffects;
	private ScoreKeeper scoreKeeper;
	private GameHud gameHud;
	private ImageView backgroundImage;
	private ImageView splash;
	private Rectangle2D bounds;
	private String title = "SNAKE";
	private Rectangle fadeRect = new Rectangle(0, 0, Settings.WIDTH, Settings.HEIGHT);
	public boolean isRunning = true;
	public boolean gameRunning = false;
	public boolean imageReady = true;
	public boolean checkCollisions = false;
	public boolean fadeOut = false;
	public int splashWidth;
	public int splashHeight;
	public int levelLenght;
	public double fade = 0.0;
	public double splashFadeDuration;
	public double splashFadeDelay;

	public void start(Stage primaryStage) {
		GameLoader.scaleResolution();
		GameLoader.scaleSpeedAndSize();
		mainWindow = primaryStage;
		initialize();
		showSplashScreen();
	}

	public void initSplash() {
		splashFadeDelay = 0;
		splashFadeDuration = 1;
		splash = new ImageView(new Image(ImageUtility.loadResource("SplashScreen5.png")));
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
		getGameRoot().getChildren().add(debrisLayer);
		getGameRoot().getChildren().add(bottomLayer);
		getGameRoot().getChildren().add(playfieldLayer);
		getGameRoot().getChildren().add(snakeBody);
		getGameRoot().getChildren().add(snakeHead);
		getGameRoot().getChildren().add(particleLayer);
		getGameRoot().getChildren().add(overlay);
		mainRoot.getChildren().add(getGameRoot());
		scene.setFill(Color.BLACK);
		loader.loadPixelMap();
		loader.loadPlayerTwo();
		loader.loadPlayerOne();
	//	loader.createSlither();
		sandEmitter = new SandEmitter(this, -200, 0, 1, 1);
		gameHud = new GameHud(this, -5, -10, Settings.WIDTH + 10, 82 / GameLoader.ResolutionScaleY);
		setHealthBarOne(new HealthBarOne(this, 55 / GameLoader.ResolutionScaleX, 15,
				(int) (350 / GameLoader.ResolutionScaleX), (int) (40 / GameLoader.ResolutionScaleY)));
		setHealthBarTwo(new HealthBarTwo(this,(int) (Settings.WIDTH - (int) (395 / GameLoader.ResolutionScaleX)) - 20 / GameLoader.ResolutionScaleX,
				15, (int) (350 / GameLoader.ResolutionScaleX), (int) (40 / GameLoader.ResolutionScaleY)));
		scoreKeeper = new ScoreKeeper(this, Settings.APPLE_COUNT, Settings.WIDTH / 2 - 10 / GameLoader.ResolutionScaleX,
				50 / GameLoader.ResolutionScaleY,ScaleX((int) (Settings.WIDTH / 2 - (500 / 2))) , 10,
				ScaleX(500),65 / GameLoader.ResolutionScaleY);
		scoreBoard = new ScoreBoard("", this, ScaleX((int) (healthBarOne.getX() + healthBarOne.getWidth() + 100)),
				ScaleY(50));
		scoreBoard2 = new ScoreBoard("", this, ScaleX((int) (healthBarTwo.getX() - healthBarTwo.getWidth()/2+20)),
				ScaleY(50));
		victoryScreen = new VictoryScreen(this, GameImageBank.levelCompleteSplash, 800, 450);
		gameOverScreen = new GameOverScreen(this, GameImageBank.gameOverScreen, 800, 450);
		processGameInput();
		processGestures();
		mainMenu.setupMainMenu();
		mainWindow.setScene(scene);
		mainWindow.setWidth(Settings.WIDTH);
		mainWindow.setHeight(Settings.HEIGHT);
		mainWindow.setResizable(false);
		mainWindow.setFullScreen(true);
		mainWindow.setFullScreenExitHint("");
		mainWindow.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		mainWindow.setTitle(title);
		mainWindow.show();
		Platform.setImplicitExit(false);
		translateObjects(mainRoot.getChildren());
		pauseGame();
		frameBaseGameLoop();
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
		initSplash();
		imageBank = new GameImageBank();
		levelImageBank = new GameLevelImage();
		mainRoot = new Group();
		root = new Pane();
		mainMenu = new MenuMain(this);
		backgroundImage = new ImageView(GameLevelImage.desertBackground);
		canvas = new Canvas(Settings.WIDTH, Settings.HEIGHT);
		scene = new Scene(mainMenu.getMenuRoot(), Settings.WIDTH, Settings.HEIGHT);
		gc = canvas.getGraphicsContext2D();
		playfieldLayer = new Pane();
		particleLayer = new Pane();
		bottomLayer = new Pane();
		debrisLayer = new Pane();
		levelLayer = new Pane();
		fadeScreen = new Pane();
		snakeHead = new Pane();
		snakeBody = new Pane();
		overlay = new Pane();
		loader = new GameLoader(this);
		objectManager = new GameObjectManager(this);
		slitherManager = new SlitherManager(this);
		sectManagerOne = new PlayerOneSectionManager(this);
		sectManagerTwo = new PlayerTwoSectionManager(this);
		sectManagerThree = new SlitherSectionManager(this);
		keyInput = new GameKeyInputManager();
		gestures = new GameGestureInputManager();
		mouseInput = new GameMouseInputManager();
		debrisManager = new GameDebrisManager(this);
		postEffects = new ScreenOverlay(this, getGameRoot());
	}

	public static Double ScaleX(int value) {
		Double newSize = value/GameLoader.ResolutionScaleX;
		return newSize;
	}

	public static Double ScaleY(int value) {
		Double newSize = value/GameLoader.ResolutionScaleY;
		return newSize;
	}

	public void resumeGame() {
		if (Settings.RENDER_GAME == true)
			return;
		Settings.RENDER_GAME = true;
	}

	public void pauseGame() {
		if (Settings.RENDER_GAME == false)
			return;
		Settings.RENDER_GAME = false;
	}

	public void pauseAndResume() {
		if (Settings.RENDER_GAME == false)
			Settings.RENDER_GAME = true;
		else if (Settings.RENDER_GAME == true)
			Settings.RENDER_GAME = false;
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
		gestures.processGestures(this);
	}
	public void closeGame() {
		pauseGame();
		this.getObjectManager().clearAll();
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
	public void gameLoop() {

		gameLoop = new AnimationTimer() {

			long lastTime = System.nanoTime();
			long nanoSecond = 1000000000;
			long currentTime = 0;
			double delta = 0;
			double FPS = 0;

			public void handle(long now) {
				FPS++;
				currentTime = now;
				delta += currentTime - lastTime;
				if (!Settings.RENDER_GAME) {
					mainMenu.transition();

				}
				if (Settings.RENDER_GAME) {
					drawOverlay(gc);
					debrisManager.update(gc);
					objectManager.updateAll(gc, now);
					sectManagerTwo.updateAll(gc, now);
					objectManager.checkCollisions();
					if (loader.getPlayerOne() != null && getHealthBarOne() != null) {
						getHealthBarOne().depleteHealth();
						getHealthBarOne().regerateHealth();
					}
					if (loader.getPlayerTwo() != null && getHealthBarTwo() != null) {
						getHealthBarTwo().depleteHealth();
						getHealthBarTwo().regerateHealth();
					}
					if (Settings.ALLOW_PHYSICS) {
						if (!debrisLayer.getChildren().isEmpty()) {
							if (debrisLayer.getChildren().size() > Settings.DEBRIS_LIMIT) {
								debrisLayer.getChildren().remove(50, 100);
							}
						}
					}
					if (!Settings.ALLOW_PHYSICS) {
						if (!debrisLayer.getChildren().isEmpty()) {
							if (debrisLayer.getChildren().size() > Settings.DEBRIS_LIMIT + 50) {
								debrisLayer.getChildren().remove(50, 100);
							}
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
	public void frameBaseGameLoop() {
		Timeline gameLoop = new Timeline();
		gameLoop.setCycleCount(Timeline.INDEFINITE);

		KeyFrame keyFrame = new KeyFrame(Duration.seconds(Settings.FRAMECAP), // 60
																				// FPS
				new EventHandler<ActionEvent>() {
					long startTime = System.currentTimeMillis();
					long cummulativeTime = startTime;
					long lastTime = System.nanoTime();
					long nanoSecond = 1000000000;
					long currentTime = 0;
					double delta = 0;
					double FPS = 0;

					public void handle(ActionEvent e) {
						long now = System.nanoTime();
						FPS++;
						currentTime = now;
						delta += currentTime - lastTime;
						long timePassed = System.currentTimeMillis() - cummulativeTime;
						cummulativeTime += timePassed;
						if (!Settings.RENDER_GAME) {
							mainMenu.transition();

						}
						if (Settings.RENDER_GAME) {
							fade();
							drawOverlay(gc);
							gameHud.update();
							victoryScreen.swipeRight();
							gameOverScreen.swipeDown();
							scoreKeeper.keepCount();
							slitherManager.updateAll(gc, timePassed);
							slitherManager.checkCollisions();
							objectManager.updateAll(gc, timePassed);
							objectManager.checkCollisions();
							sectManagerTwo.updateAll(gc, timePassed);
							sectManagerThree.updateAll(gc, timePassed);
							sectManagerOne.updateAll(gc, timePassed);
							debrisManager.updateDebris(gc);
							debrisManager.updateParticles(gc);
							loader.updateLevelObjects();
							sandEmitter.move();
							sandEmitter.emit();
							if (loader.getPlayerOne() != null && getHealthBarOne() != null) {
								getHealthBarOne().depleteHealth();
								getHealthBarOne().regerateHealth();
							}
							if (loader.getPlayerTwo() != null && getHealthBarTwo() != null) {
								getHealthBarTwo().depleteHealth();
								getHealthBarTwo().regerateHealth();
							}
							if (scoreBoard != null) {
								scoreBoard.hide();
							}
							if (scoreBoard2 != null) {
								scoreBoard2.hide();
							}
							if (Settings.ALLOW_PHYSICS) {
								if (!particleLayer.getChildren().isEmpty()) {
									if (particleLayer.getChildren().size() > Settings.DEBRIS_LIMIT) {
										particleLayer.getChildren().remove(0);
									}
								}
							}
							if (!Settings.ALLOW_PHYSICS) {
								if (!particleLayer.getChildren().isEmpty()) {
									if (particleLayer.getChildren().size() > Settings.DEBRIS_LIMIT) {
										particleLayer.getChildren().remove(0);
									}
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

	// public class ThreadLoop extends Thread{
	public void run() {
		long lastTime = System.nanoTime();
		long startTime = System.currentTimeMillis();
		// long lastUpdate;
		long cummulativeTime = startTime;
		double amountOfUpadates = 60.0;
		double ns = 1000000000 / amountOfUpadates;
		double delta = 0;
		double delta1 = 0;
		double delta2 = 0;
		double delta3 = 0;
		double delta4 = 0;
		double delta5 = 0;
		double delta6 = 0;
		long timer = System.currentTimeMillis();
		while (isRunning) {

			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			if (!Settings.RENDER_GAME) {
				while (delta >= 1) {
					updateInterface();
					delta--;
				}
			}
			if (Settings.RENDER_GAME) {
				long timePassed = System.currentTimeMillis() - cummulativeTime;
				cummulativeTime += timePassed;
				delta1 += (now - lastTime) / ns / 100;
				delta2 += (now - lastTime) / ns / 6;
				delta3 += (now - lastTime) / ns / 2;
				delta4 += (now - lastTime) / ns;
				delta5 += (now - lastTime) / ns * 2;
				delta6 += (now - lastTime) / ns * 4;

				while (delta1 >= 1) {
					updateAt1();
					delta1--;
				}

				while (delta2 >= 1) {
					updateAt10();
					delta2--;
				}

				while (delta3 >= 1) {
					updateAt30();
					delta3--;
				}

				while (delta4 >= 1) {
					updateAt60(now);
					delta4--;
				}

				while (delta5 >= 1) {
					updateAt120();
					delta5--;
				}

				while (delta6 >= 1) {
					updateAt240();
					delta6--;
				}
			}
			lastTime = now;
			if (System.currentTimeMillis() - timer > 250) {
				timer += 1000;
				lastTime = System.nanoTime();
			}
		}
	}

	// }
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

				if (Settings.RENDER_GAME) {
					objectManager.updateAnimation(timePassed);
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
				if (Settings.RENDER_GAME) {
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
		if (Settings.DEBUG_MODE) {
			gc.setFill(Color.WHITE);
			gc.fillRect(0, 0, Settings.WIDTH, Settings.HEIGHT);
		}
	}

	/**
	 * The level thread function is used for two things this thread is frame
	 * controlled and can provide updates on the current amount of children
	 * nodes being used by the layers used in game and the amount of objects in
	 * each list This thread can also be used to procedurally create a level
	 * which will then put objects on the level based on the set speed
	 */
	public void levelThread() {
		Timeline levelUpdateLoop = new Timeline();
		levelUpdateLoop.setCycleCount(Timeline.INDEFINITE);

		KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.256), new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {

				if (imageReady == true) {
					checkCollisions = true;
					imageReady = false;
				}
				// loader.procedurallyCreateLevel();
				System.out.println("Amount of objects in debris layer: " + debrisLayer.getChildren().size());
				System.out.println("Amount of objects in game layer: " + playfieldLayer.getChildren().size());
				System.out.println("Amount of objects in bottom layer: " + bottomLayer.getChildren().size());
				System.out.println("Amount of objects in radar layer: " + overlay.getChildren().size());
				System.out.println("Amount of objects in level layer: " + getGameRoot().getChildren().size());
				System.out.println();
				System.out.println();
				System.out.println("Amount of objects in object manager: " + objectManager.getObjectList().size());
				System.out.println("Amount of objects in debris manager: " + debrisManager.getDebrisList().size());
				System.out.println();
				System.out.println();
				System.out.println("---------------------------------------------------------------------------");

			}
		});
		levelUpdateLoop.getKeyFrames().add(keyFrame);
		levelUpdateLoop.play();
	}

	protected void updateInterface() {
		mainMenu.transition();
	}

	protected void updateAt1() {
	}

	protected void updateAt10() {
	}

	protected void updateAt30() {
	}

	protected void updateAt60(long now) {
		drawOverlay(gc);
		debrisManager.update(gc);
		objectManager.updateAll(gc, now);
		objectManager.checkCollisions();
		sectManagerTwo.updateAll(gc, now);
		if (loader.getPlayerOne() != null) {
			// energyMeter.deplete();
			// energyMeter.regerate();
			getHealthBarOne().depleteHealth();
			getHealthBarOne().regerateHealth();
		}
	}

	protected void updateAt120() {
	}

	protected void updateAt240() {
	}

	protected void updateAnimation(long timePassed) {
	}

	/**
	 * Method used to translate objects on the screen nodes translated will stay
	 * on the visual range of the screen useful for things like heads up
	 * displays and and other nodes that need to be constantly visible by the
	 * player
	 */
	public void translateObjects(ObservableList<Node> rootPane) {
		TextFPS = new Text("FPS : ");
		// TextFPS.setX(getHealthBarOne().width+45/GameLoader.ResolutionScaleX);
		TextFPS.setX(ScaleX(20));
		TextFPS.setY(ScaleY(70));
		// overlay.setTranslateX(0);
		// overlay.setTranslateY(0);
		TextFPS.setFill(Color.WHITE);
		TextFPS.setFont(Font.font("AERIAL", FontWeight.BOLD, ScaleX(20)));
		rootPane.add(overlay);
		rootPane.add(TextFPS);
		mainRoot.getChildren().add(fadeScreen);

	}

	public void restart() {
		clearAll();
		fade = 0.0;
		fadeRect.setOpacity(fade);
		PlayerOne.NUMERIC_ID = 0;
		PlayerOne.DEAD = false;
		PlayerOne.MOUTH_CLOSE = true;
		PlayerOne.KEEP_MOVING = true;
		PlayerTwo.NUMERIC_ID = 0;
		PlayerTwo.DEAD = false;
		PlayerTwo.MOUTH_CLOSE = true;
		getGameRoot().setEffect(null);
		scoreBoard.resetScore();
		scoreBoard2.resetScore();
		scoreKeeper.resetCount();
		scoreKeeper.resetTimer();
		gameHud.show();
		gameHud.swipeUp();
		scoreBoard.show();
		scoreBoard2.show();
		fadeOut = false;
		victoryScreen.removeBlur();
		victoryScreen.removeBoard();
		gameOverScreen.removeBlur();
		gameOverScreen.removeBoard();
		loader.killPlayerOne();
		loader.killPlayerTwo();
		loader.loadPlayerOne();
		loader.loadPlayerTwo();
		getHealthBarOne().show();
		getHealthBarTwo().show();
		getHealthBarOne().refill();
		getHealthBarTwo().refill();
		getHealthBarOne().setPlayer();
		getHealthBarTwo().setPlayer();
		loader.loadPixelMap();
		processGameInput();
	}

	public void reset() {
		clearAll();
		fade = 0.0;
		fadeRect.setOpacity(fade);
		PlayerOne.NUMERIC_ID = 0;
		PlayerOne.DEAD = false;
		PlayerOne.MOUTH_CLOSE = true;
		PlayerOne.KEEP_MOVING = true;
		PlayerTwo.NUMERIC_ID = 0;
		PlayerTwo.DEAD = false;
		PlayerTwo.MOUTH_CLOSE = true;
		getGameRoot().setEffect(null);
		scoreBoard.resetScore();
		scoreBoard2.resetScore();
		scoreKeeper.resetCount();
		scoreKeeper.resetTimer();
		gameHud.show();
		gameHud.swipeUp();
		scoreBoard.show();
		scoreBoard2.show();
		fadeOut = false;
		victoryScreen.removeBlur();
		victoryScreen.removeBoard();
		gameOverScreen.removeBlur();
		gameOverScreen.removeBoard();
		loader.killPlayerOne();
		loader.killPlayerTwo();
		loader.loadPlayerOne();
		loader.loadPlayerTwo();
		getHealthBarOne().show();
		getHealthBarTwo().show();
		getHealthBarOne().refill();
		getHealthBarTwo().refill();
		getHealthBarOne().setPlayer();
		getHealthBarTwo().setPlayer();
		loader.loadPixelMap();
		processGameInput();
		getMainMenu().setMainMenu();
	}

	public void clearAll() {
		playfieldLayer.getChildren().clear();
		debrisLayer.getChildren().clear();
		bottomLayer.getChildren().clear();
		snakeHead.getChildren().clear();
		snakeBody.getChildren().clear();
		levelLayer.getChildren().clear();
		particleLayer.getChildren().clear();
		debrisManager.clearAll();
		objectManager.clearAll();
		sectManagerOne.clearAll();
		sectManagerTwo.clearAll();
		sectManagerThree.clearAll();
		loader.clearTiles();
		fadeScreen.getChildren().clear();

	}

	public void gameOver() {
		if (GameOverScreen.FAILED_LEVEL == false) {

			playfieldLayer.getChildren().clear();
			debrisLayer.getChildren().clear();
			bottomLayer.getChildren().clear();
			snakeHead.getChildren().clear();
			snakeBody.getChildren().clear();
			levelLayer.getChildren().clear();
			particleLayer.getChildren().clear();
			debrisManager.clearAll();
			objectManager.clearAll();
			sectManagerOne.clearAll();
			slitherManager.clearAll();
			sectManagerTwo.clearAll();
			sectManagerThree.clearAll();
			loader.clearTiles();
			fade = 0.0;
			fadeRect.setOpacity(fade);
			PlayerOne.NUMERIC_ID = 0;
			PlayerOne.DEAD = false;
			PlayerOne.MOUTH_CLOSE = true;
			PlayerOne.KEEP_MOVING = true;
			PlayerTwo.NUMERIC_ID = 0;
			PlayerTwo.DEAD = false;
			PlayerTwo.MOUTH_CLOSE = true;
			getGameRoot().setEffect(null);
			scoreBoard.resetScore();
			scoreBoard2.resetScore();
			scoreKeeper.resetCount();
			scoreKeeper.resetTimer();
			fadeOut = false;
			victoryScreen.removeBlur();
			victoryScreen.removeBoard();
			gameOverScreen.removeBlur();
			gameOverScreen.removeBoard();
			processGameInput();
			gameOverScreen.removeBoard();
			gameOverScreen.finishLevel();
			scoreKeeper.setPosition(1.5f);
			getGameHud().swipeDown();
			GameOverScreen.FAILED_LEVEL = true;
		}
	}

	public void addFadeScreen() {
		if (!fadeOut) {
			getMainRoot().getChildren().add(fadeRect);
			fadeOut = true;
		}
	}

	public void fade() {
		if (fadeOut) {
			fade += 0.01;
			fadeRect.setOpacity(fade);
			if (fade >= 1.0f) {
				fadeRect.setOpacity(1);

			}
			if (fade >= 1.1f) {
				getMainRoot().getChildren().remove(fadeRect);
				reset();
			}
		}
	}

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

	public GameLoader getloader() {
		return loader;
	}

	public void setLevelManager(GameLoader loader) {
		this.loader = loader;
	}

	public Pane getGameRoot() {
		return root;
	}

	public Pane getLevelLayer() {
		return levelLayer;
	}

	public ScreenOverlay getPostEffects() {
		return postEffects;
	}

	public void setRoot(Parent root) {
		scene.setRoot(root);
	}

	public GameObjectManager getObjectManager() {
		return objectManager;
	}

	public SlitherManager getSlitherManager() {
		return slitherManager;
	}

	public void setObjectManager(GameObjectManager objectManager) {
		this.objectManager = objectManager;
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
	public void setDebrisManager(GameDebrisManager debrisManager) {
		this.debrisManager = debrisManager;
	}

	public Pane getPlayfieldLayer() {
		return playfieldLayer;
	}

	public void setPlayfieldLayer(Pane playfieldLayer) {
		this.playfieldLayer = playfieldLayer;
	}

	public Pane getOverlay() {
		return overlay;
	}

	public void getOverlay(Pane radarLayer) {
		this.overlay = radarLayer;
	}

	public ScoreBoard getScoreBoardOne() {
		return scoreBoard;
	}

	public ScoreBoard getScoreBoardTwo() {
		return scoreBoard2;
	}

	public GameHud getGameHud() {
		return gameHud;
	}

	public Group getMainRoot() {
		return mainRoot;
	}

	public void setScoreBoard(ScoreBoard scoreBoard) {
		this.scoreBoard = scoreBoard;
	}

	public void setScoreBoard2(ScoreBoard scoreBoard) {
		this.scoreBoard2 = scoreBoard;
	}

	public Pane getBottomLayer() {
		return bottomLayer;
	}

	public void setBottomLayer(Pane bottomLayer) {
		this.bottomLayer = bottomLayer;
	}

	public GameKeyInputManager getKeyInput() {
		return keyInput;
	}

	public Pane getSnakeHeadLayer() {
		return snakeHead;
	}

	public Pane getSnakeBodyLayer() {
		return snakeBody;
	}

	public void setKeyInput(GameKeyInputManager keyInput) {
		this.keyInput = keyInput;
	}

	public Pane getDebrisLayer() {
		return debrisLayer;
	}

	public Pane getParticleLayer() {
		return particleLayer;
	}


	public void setDebrisLayer(Pane debrisLayer) {
		this.debrisLayer = debrisLayer;
	}

	public Scene getScene() {
		return scene;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public Pane getFadeScreen() {
		return fadeScreen;
	}

	public ScoreKeeper getScoreKeeper() {
		return scoreKeeper;
	}

	public void showCursor(boolean choice, Scene scene) {
		if (!choice)
			scene.setCursor(Cursor.NONE);

		else if (choice)
			scene.setCursor(Cursor.DEFAULT);
	}


	public SlitherSectionManager getSectionManager3() {
		return sectManagerThree;
	}

	public void allowMouseInput(boolean choice) {
		if (choice)
			mouseInput.processInput(this, getloader().getPlayerOne(), getloader().getPlayerTwo(), scene);
	}

	public static void main(String[] args) {
		launch(args);
	}

}
