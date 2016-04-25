package com.SnakeGame.Core;

import com.SnakeGame.OriginalSnake.OrgGameObjectManager;
import com.SnakeGame.OriginalSnake.OrgGameSectionManager;
import com.SnakeGame.SlitherSnake.GameSlitherManager;
import com.SnakeGame.SlitherSnake.GameSlitherSectionManager;
import com.SnakeGame.SnakeOne.SnakeOne;
import com.SnakeGame.SnakeOne.SnakeOneSectionManager;

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
import javafx.scene.input.SwipeEvent;
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

	GameLoader loader;
	GameKeyInputManager keyInput;
	GameMouseInputManager mouseInput;
	GraphicsContext gc;
	GameObjectManager objectManager;
	OrgGameObjectManager orgObjectManager;
	GameSlitherManager slitherManager;
	GameDebrisManager debrisManager;
	SnakeOneSectionManager sectManager;
	OrgGameSectionManager orgSectManager;
	SnakeTwoSectionManager sectManager2;
	GameSlitherSectionManager sectManager3;
	GameImageBank imageBank;
	FadeTransition fadeSplash;
	Stage mainWindow;
	MenuMain mainMenu;
	Scene scene;
	Scene splashScene;
	Group mainRoot;
	Pane root;
	Canvas canvas;
	Pane splashLayout;
	Pane playfieldLayer;
	Pane particleLayer;
	Pane debrisLayer;
	Pane bottomLayer;
	Pane levelLayer;
	Pane fadeScreen;
	Pane endGame;
	Pane snakeHead;
	Pane snakeBody;
	Pane overlay;
	Stage primaryStage;
	Text TextFPS;
	Timeline physicsLoop;
	private HealthBarOne healthBarOne;
	HealthBarTwo healthBarTwo;
	ScoreBoard scoreBoard;
	ScoreBoard scoreBoard2;
	EnergyMeter energyMeter;
	SandEmitter sandEmitter;
	VictoryScreen victoryScreen;
	AnimationTimer gameLoop;
	AnimationTimer animationLoop;
	AnimationTimer particleLoop;
	GameOverScreen gameOver;
	ScreenOverlay postEffects;
	ScoreKeeper scoreKeeper;
	GameHud gameHud;
	ImageView backgroundImage;
	ImageView apple;
	ImageView splash;
	Rectangle2D bounds;
	Boolean stopDrawing = false;
	String title = "SNAKE";
	Rectangle fadeRect = new Rectangle(0, 0, Settings.WIDTH, Settings.HEIGHT);
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
		// reset();
		GameLoader.scaleResolution();
		GameLoader.scaleSpeedAndSize();
		initialize();
		mainWindow = primaryStage;
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
		root.getChildren().add(backgroundImage);
		root.getChildren().add(debrisLayer);
		root.getChildren().add(bottomLayer);
		root.getChildren().add(playfieldLayer);
		root.getChildren().add(snakeBody);
		root.getChildren().add(snakeHead);
		root.getChildren().add(particleLayer);
		root.getChildren().add(overlay);
		mainRoot.getChildren().add(root);
		scene.setFill(Color.BLACK);
		loader.loadPixelMap();
		loader.loadPlayer1();
		loader.loadPlayer2();
		loader.loadOrgPlayer();
//		loader.createSlither();
		sandEmitter = new SandEmitter(this, -200, 0, 1, 1);
		gameHud = new GameHud(this, -5, 0, Settings.WIDTH+10, 55 / GameLoader.ResolutionScaleY);
		setHealthBarOne(new HealthBarOne(this, 20 / GameLoader.ResolutionScaleX, 0, (int) (350 / GameLoader.ResolutionScaleX),
				(int) (40 / GameLoader.ResolutionScaleY)));
		healthBarTwo = new HealthBarTwo(this,
				(int) (Settings.WIDTH - (int) (350 / GameLoader.ResolutionScaleX)) - 20 / GameLoader.ResolutionScaleX,
				0, (int) (350 / GameLoader.ResolutionScaleX), (int) (40 / GameLoader.ResolutionScaleY));
		scoreKeeper = new ScoreKeeper(this, Settings.APPLE_COUNT, Settings.WIDTH / 2 - 10 / GameLoader.ResolutionScaleX,
				30 / GameLoader.ResolutionScaleY,
				Settings.WIDTH / 2 - GameImageBank.countKeeper.getWidth() / 2 / GameLoader.ResolutionScaleX, 0,
				GameImageBank.countKeeper.getWidth() / GameLoader.ResolutionScaleX,
				GameImageBank.countKeeper.getHeight() / 2 / GameLoader.ResolutionScaleY);
		scoreBoard = new ScoreBoard("Player 1", this, scoreKeeper.x + 70/GameLoader.ResolutionScaleX, 30 / GameLoader.ResolutionScaleY, 0, 0,
				100 / GameLoader.ResolutionScaleX, 30 / GameLoader.ResolutionScaleY);
		scoreBoard2 = new ScoreBoard("Player 2", this, scoreKeeper.x + scoreKeeper.width - 190/GameLoader.ResolutionScaleX,
				30 / GameLoader.ResolutionScaleY, Settings.WIDTH - 100 / GameLoader.ResolutionScaleX, 0,
				100 / GameLoader.ResolutionScaleX, 30 / GameLoader.ResolutionScaleY);
		victoryScreen = new VictoryScreen(this,GameImageBank.levelCompleteSplash,800,450);
		gameOver = new GameOverScreen(this,GameImageBank.gameOverScreen,800,450);
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
		mainRoot = new Group();
		root = new Pane();
		mainMenu = new MenuMain(this);
		backgroundImage = new ImageView(GameLevelImage.desertBackground);
		canvas = new Canvas(Settings.WIDTH, Settings.HEIGHT);
		scene = new Scene(mainMenu.getMenuRoot(), Settings.WIDTH, Settings.HEIGHT);
		gc = canvas.getGraphicsContext2D();
		endGame = new Pane();
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
		slitherManager = new GameSlitherManager(this);
		sectManager = new SnakeOneSectionManager(this);
		orgObjectManager = new OrgGameObjectManager(this);
		orgSectManager = new OrgGameSectionManager(this);
		sectManager2 = new SnakeTwoSectionManager(this);
		sectManager3 = new GameSlitherSectionManager(this);
		keyInput = new GameKeyInputManager();
		mouseInput = new GameMouseInputManager();
		debrisManager = new GameDebrisManager(this);
		postEffects = new ScreenOverlay(this, root);
	}

	public void processGestures() {
		root.setOnSwipeUp(new EventHandler<SwipeEvent>() {

			public void handle(SwipeEvent event) {
				loader.getPlayer().setDirection(PlayerMovement.MOVE_UP);
				event.consume();
			}
		});
		root.setOnSwipeDown(new EventHandler<SwipeEvent>() {

			public void handle(SwipeEvent event) {
				loader.getPlayer().setDirection(PlayerMovement.MOVE_DOWN);
				event.consume();
			}
		});
		root.setOnSwipeLeft(new EventHandler<SwipeEvent>() {
			public void handle(SwipeEvent event) {
				loader.getPlayer().setDirection(PlayerMovement.MOVE_LEFT);
				event.consume();
			}
		});
		root.setOnSwipeRight(new EventHandler<SwipeEvent>() {
			public void handle(SwipeEvent event) {
				loader.getPlayer().setDirection(PlayerMovement.MOVE_RIGHT);
				event.consume();
			}
		});
	}

	public Double ScaleX(int value) {
		return value / GameLoader.ResolutionScaleX;
	}

	public Double ScaleY(int value) {
		return value / GameLoader.ResolutionScaleY;
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
		keyInput.processInput(this, loader.getPlayer(), loader.getPlayer2(), loader.getSlither(),scene);
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
					sectManager.updateAll(gc, now);
					sectManager2.updateAll(gc, now);
					objectManager.checkCollisions();
					if (loader.getPlayer() != null && getHealthBarOne() != null) {
						getHealthBarOne().depleteHealth();
						getHealthBarOne().regerateHealth();
					}
					if (loader.getPlayer2() != null && healthBarTwo != null) {
						healthBarTwo.depleteHealth();
						healthBarTwo.regerateHealth();
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
							victoryScreen.swipeFromLeft();
							gameOver.swipeFromLeft();
							scoreKeeper.keepCount();
							// loader.spawnObjectRandomly(true);
							// loader.updateLevelObjects();

							slitherManager.updateAll(gc, timePassed);
							slitherManager.checkCollisions();
							objectManager.updateAll(gc, timePassed);
							objectManager.checkCollisions();
							sectManager.updateAll(gc, timePassed);
							sectManager2.updateAll(gc, timePassed);
							sectManager3.updateAll(gc, timePassed);
							orgObjectManager.updateAll(gc, timePassed);
							orgObjectManager.checkCollisions();
							orgSectManager.updateAll(gc, timePassed);
							debrisManager.updateDebris(gc);
							debrisManager.updateParticles(gc);
							loader.updateLevelObjects();
							sandEmitter.move();
							sandEmitter.emit(1, 9);
							if (loader.getPlayer() != null && getHealthBarOne() != null) {
								getHealthBarOne().depleteHealth();
								getHealthBarOne().regerateHealth();
							}
							if (loader.getPlayer2() != null && healthBarTwo != null) {
								healthBarTwo.depleteHealth();
								healthBarTwo.regerateHealth();
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
				System.out.println("Amount of objects in level layer: " + root.getChildren().size());
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
		sectManager.updateAll(gc, now);
		sectManager2.updateAll(gc, now);
		if (loader.getPlayer() != null) {
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
		TextFPS.setX(getHealthBarOne().width+45/GameLoader.ResolutionScaleX);
		TextFPS.setY(ScaleY(30));
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
		SnakeOne.NUMERIC_ID = 0;
		SnakeOne.killTheSnake = false;
		SnakeOne.MOUTH_CLOSE = true;
		SnakeOne.keepMoving = true;
		Player2.NUMERIC_ID = 0;
		Player2.killTheSnake = false;
		Player2.MOUTH_CLOSE = true;
		root.setEffect(null);
		scoreBoard.resetScore();
		scoreBoard2.resetScore();
		scoreKeeper.resetCount();
		gameHud.show();
		gameHud.swipeUp();
		scoreBoard.show();
		scoreBoard2.show();
		fadeOut = false;
		victoryScreen.removeBlur();
		victoryScreen.removeBoard();
		gameOver.removeBlur();
		gameOver.removeBoard();
		loader.killPlayer();
		loader.killPlayer2();
		loader.loadPlayer1();
		loader.loadPlayer2();
		getHealthBarOne().show();
		healthBarTwo.show();
		getHealthBarOne().refill();
		healthBarTwo.refill();
		getHealthBarOne().setPlayer();
		healthBarTwo.setPlayer();
		loader.loadPixelMap();
		processGameInput();
	}

	public void reset() {
		clearAll();
		fade = 0.0;
		fadeRect.setOpacity(fade);
		SnakeOne.NUMERIC_ID = 0;
		SnakeOne.killTheSnake = false;
		SnakeOne.MOUTH_CLOSE = true;
		SnakeOne.keepMoving = true;
		Player2.NUMERIC_ID = 0;
		Player2.killTheSnake = false;
		Player2.MOUTH_CLOSE = true;
		root.setEffect(null);
		scoreBoard.resetScore();
		scoreBoard2.resetScore();
		scoreKeeper.resetCount();
		gameHud.show();
		gameHud.swipeUp();
		scoreBoard.show();
		scoreBoard2.show();
		fadeOut = false;
		victoryScreen.removeBlur();
		victoryScreen.removeBoard();
		gameOver.removeBlur();
		gameOver.removeBoard();
		loader.killPlayer();
		loader.killPlayer2();
		loader.loadPlayer1();
		loader.loadPlayer2();
		getHealthBarOne().show();
		healthBarTwo.show();
		getHealthBarOne().refill();
		healthBarTwo.refill();
		getHealthBarOne().setPlayer();
		healthBarTwo.setPlayer();
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
		objectManager.clearAll();
		sectManager.clearAll();
		sectManager2.clearAll();
		sectManager3.clearAll();
		loader.clearTiles();
		fadeScreen.getChildren().clear();

	}
	public void gameOver() {
		if(GameOverScreen.FAILED_LEVEL == false) {

			playfieldLayer.getChildren().clear();
			debrisLayer.getChildren().clear();
			bottomLayer.getChildren().clear();
			snakeHead.getChildren().clear();
			snakeBody.getChildren().clear();
			levelLayer.getChildren().clear();
			particleLayer.getChildren().clear();
			debrisManager.clearAll();
			objectManager.clearAll();
			slitherManager.clearAll();
			sectManager.clearAll();
			sectManager2.clearAll();
			sectManager3.clearAll();
			loader.clearTiles();
			fade = 0.0;
			fadeRect.setOpacity(fade);
			SnakeOne.NUMERIC_ID = 0;
			SnakeOne.killTheSnake = false;
			SnakeOne.MOUTH_CLOSE = true;
			SnakeOne.keepMoving = true;
			Player2.NUMERIC_ID = 0;
			Player2.killTheSnake = false;
			Player2.MOUTH_CLOSE = true;
			root.setEffect(null);
			scoreBoard.resetScore();
			scoreBoard2.resetScore();
			scoreKeeper.resetCount();
			fadeOut = false;
			victoryScreen.removeBlur();
			victoryScreen.removeBoard();
			gameOver.removeBlur();
			gameOver.removeBoard();
			processGameInput();

		gameOver.removeBoard();
		gameOver.finishLevel();
		scoreKeeper.position = 1.5f;
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
	public HealthBarOne getHealthBar() {
		return getHealthBarOne();
	}

	public void setHealthBar(HealthBarOne healthBarOne) {
		this.setHealthBarOne(healthBarOne);
	}

	public HealthBarTwo getHealthBar2() {
		return healthBarTwo;
	}

	public void setHealthBar2(HealthBarTwo healthBarTwo) {
		this.healthBarTwo = healthBarTwo;
	}

	public static void writeToLog(String text) {
		System.out.println(text + "\n");
	}
	public VictoryScreen getVictoryScreen(){
		return victoryScreen;
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

	public Parent getRoot() {
		return scene.getRoot();
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

	public GameSlitherManager getSlitherManager() {
		return slitherManager;
	}

	public void setObjectManager(GameObjectManager objectManager) {
		this.objectManager = objectManager;
	}

	public GameDebrisManager getDebrisManager() {
		return debrisManager;
	}

	public OrgGameObjectManager getOrgObjectManager() {
		return orgObjectManager;
	}

	public OrgGameSectionManager getOrgSectManager() {
		return orgSectManager;
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

	public ScoreBoard getScoreBoard() {
		return scoreBoard;
	}

	public ScoreBoard getScoreBoard2() {
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

	public HealthBarOne getHealthBarOne() {
		return healthBarOne;
	}

	public void setHealthBarOne(HealthBarOne healthBarOne) {
		this.healthBarOne = healthBarOne;
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

	public SnakeOneSectionManager getSectionManager() {
		return sectManager;
	}
	public SnakeTwoSectionManager getSectionManager2() {
		return sectManager2;
	}
	public GameSlitherSectionManager getSectionManager3() {
		return sectManager3;
	}
	public void allowMouseInput(boolean choice) {
		if (choice)
			mouseInput.processInput(this, getloader().getPlayer(), scene);
	}

	public static void main(String[] args) {
		launch(args);
	}





}
