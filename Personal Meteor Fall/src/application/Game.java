package application;


import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Game extends Application {

    GameLoader loader;
    GameKeyInputManager keyInput ;
    GameMouseInputManager mouseInput;
	GraphicsContext gc;
    GameObjectManager objectManager;
    GameParticleManager particleManager;
    GameDebrisManager debrisManager;
    GameImageBank imageBank;
	GameCamera camera;
	MapLoader mapLoader;
	Stage mainWindow;
	MainMenu mainMenu;
	Scene scene;
	Group mainRoot;
    Pane root;
    Canvas canvas;
    Pane playfieldLayer;
    Pane debrisLayer;
    Pane bottomLayer;
    Pane radarLayer;
    Pane levelLayer;
    Pane fadeScreen;
    Stage primaryStage;
    Text TextFPS;
    Timeline physicsLoop;
    HealthBar healthBar;
    ScoreBoard scoreBoard;
	EnergyMeter energyMeter;
	AnimationTimer gameLoop;
    AnimationTimer animationLoop;
    AnimationTimer particleLoop;
    Boolean stopDrawing = false;
    String title = "Space Legion or Meteor Fall";
    boolean imageReady = true;
	boolean checkCollisions = false;
    int levelLenght;
    int x =250;
    int y =250;
    int h =0;


    public void start(Stage primaryStage) {
    	initialize();
    	mainWindow = primaryStage;
        root.getChildren().add( canvas );
        root.getChildren().add( bottomLayer);
        root.getChildren().add( playfieldLayer);
        root.getChildren().add( debrisLayer);
        root.getChildren().add( levelLayer);
        mainRoot.getChildren().add(root);
        scene.setFill(Color.BLACK);
        loader.loadPixelMap();
     	loader.createPlayers();
     	energyMeter = new EnergyMeter(this,(int) (Settings.WIDTH-300), 50, 300, 25);
     	scoreBoard = new ScoreBoard(this,(int)(Settings.WIDTH-100), 100,0,0);
     	healthBar = new HealthBar(this, 0, 50, 300, 25);
        processGameInput();
        mainMenu.enterMainMenu();
        mainWindow.setScene(scene);
        mainWindow.setResizable(false);
        mainWindow.setFullScreen(true);
        mainWindow.setFullScreenExitHint("");
        mainWindow.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        mainWindow.setTitle(title);
        mainWindow.show();
        translateObjects(mainRoot.getChildren());
        pauseGame();
        gameLoop();
   //   frameBaseGameLoop();
   //   levelThread();
    }

	private void initialize() {
		mainRoot = new Group();
    	root = new Pane();
    	mainMenu = new MainMenu(this);
    	canvas = new Canvas( Settings.WIDTH, Settings.HEIGHT );
    	scene = new Scene( mainMenu.getMenuRoot(), Settings.WIDTH, Settings.HEIGHT);
    	gc = canvas.getGraphicsContext2D();
        playfieldLayer = new Pane();
        levelLayer = new Pane();
        bottomLayer = new Pane();
        debrisLayer = new Pane();
        radarLayer = new Pane();
        fadeScreen = new Pane();
        imageBank = new GameImageBank();
        loader = new GameLoader(this);
        objectManager = new GameObjectManager(this);
        keyInput = new GameKeyInputManager();
        mouseInput = new GameMouseInputManager();
        debrisManager = new GameDebrisManager(this);
    }

	public void resumeGame() {
		if(Settings.RENDER_GAME == true)
			return;
		Settings.RENDER_GAME = true;
	}
	public void pauseGame() {
		if(Settings.RENDER_GAME == false)
			return;
		Settings.RENDER_GAME = false;
	}
	public void pauseAndResume(){
		if(	Settings.RENDER_GAME == false)
		Settings.RENDER_GAME = true;
	else if(Settings.RENDER_GAME == true)
		Settings.RENDER_GAME = false;
	}
	public void startThreads(){
		gameLoop.start();
	}
	public void stopThreads(){
		gameLoop.stop();
	}
	public void processGameInput(){
		keyInput.processInput(this,loader.getPlayer(),scene);
	}
	public void closeGame() {
		pauseGame();
		this.getObjectManager().clearAll();
		System.exit(0);
	}
    public void loadStringMap() {
       mapLoader = new MapLoader(this);
       mapLoader.loadMap();
       levelLenght = mapLoader.levelWidth;
    }
    public void gameLoop(){

        gameLoop = new AnimationTimer() {

        	long lastTime = System.nanoTime();
    		long nanoSecond = 1000000000;
    		long currentTime = 0;
    		double delta = 0;
    		double FPS = 0;

            public void handle(long now) {
            	FPS++;
                currentTime = now;
                delta += currentTime-lastTime;
            	if(!Settings.RENDER_GAME){
            		mainMenu.setGlowPulse();

            	}
                if(Settings.RENDER_GAME){
	                	drawOverlay(gc);
	                	loader.spawnBackgroundDebris(true);
	                	loader.spawnAsteroids(true);
	                	loader.updateStuff();
	                	debrisManager.update(gc);
		            	objectManager.updateAll(gc, now);
						objectManager.checkCollisions();
						if(loader.getPlayer()!=null){
						energyMeter.deplete();
						energyMeter.regerate();
						healthBar.depleteHealth();
						healthBar.regerateHealth();
						}
						if(Settings.ALLOW_PHYSICS){
							if(!debrisLayer.getChildren().isEmpty()){
								if(debrisLayer.getChildren().size()>Settings.DEBRIS_LIMIT){
									debrisLayer.getChildren().remove(50, 100);
								}
							}
						}
						if(!Settings.ALLOW_PHYSICS){
							if(!debrisLayer.getChildren().isEmpty()){
								if(debrisLayer.getChildren().size()>Settings.DEBRIS_LIMIT+50){
									debrisLayer.getChildren().remove(50, 100);
								}
							}
						}
					}

                if(delta > nanoSecond){
                    TextFPS.setText("FPS :"+FPS);
                    delta -= nanoSecond;
                    FPS = 0;
                 }
                 lastTime = currentTime;
            }

        };
        gameLoop.start();
    }
    public void frameBaseGameLoop() {
 		Timeline gameLoop = new Timeline();
 		gameLoop.setCycleCount(Timeline.INDEFINITE);

 		KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.0166), // 60 FPS
 			new EventHandler<ActionEvent>() {
         	long lastTime = System.nanoTime();
     		long nanoSecond = 1000000000;
     		long currentTime = 0;
     		double delta = 0;
     		double FPS = 0;

             public void handle(ActionEvent e) {
             	long now = System.nanoTime();
             	FPS++;
                 currentTime = now;
                 delta += currentTime-lastTime;

                 if(Settings.RENDER_GAME){
	                	drawOverlay(gc);
	                	loader.spawnBackgroundDebris(true);
	                	loader.spawnAsteroids(true);
	                	loader.updateStuff();
	                	debrisManager.update(gc);
		            	objectManager.updateAll(gc, now);
						objectManager.checkCollisions();
						if(Settings.ALLOW_PHYSICS){
							if(!debrisLayer.getChildren().isEmpty()){
								if(debrisLayer.getChildren().size()>Settings.DEBRIS_LIMIT){
									debrisLayer.getChildren().remove(50, 100);
								}
							}
						}
						if(!Settings.ALLOW_PHYSICS){
							if(!debrisLayer.getChildren().isEmpty()){
								if(debrisLayer.getChildren().size()>Settings.DEBRIS_LIMIT+50){
									debrisLayer.getChildren().remove(50, 100);
								}
							}
						}
					}

                 if(delta > nanoSecond){
                     TextFPS.setText("FPS :"+FPS);
                     delta -= nanoSecond;
                     FPS = 0;
                  }
                  lastTime = currentTime;
             }
 				});
 		gameLoop.getKeyFrames().add(keyFrame);
 		gameLoop.play();
 	}
     public void particleLoop(){
     	particleLoop = new AnimationTimer() {
             public void handle(long now) {
                 if(Settings.RENDER_GAME){
                 	if(!Settings.DEBUG_MODE){
                 	particleManager.updateParticles(gc);
                 	particleManager.updateNow(now);
                 	}
                 }
             }
         };
         particleLoop.start();
     }
     public void animationLoop(){

         animationLoop = new AnimationTimer() {

         	long startTime = System.currentTimeMillis();
     		long cummulativeTime = startTime;

             public void handle(long now) {
             	long timePassed = System.currentTimeMillis() - cummulativeTime;
     			cummulativeTime += timePassed;

                 if(Settings.RENDER_GAME){
                 }
             }
         };
         animationLoop.start();
     }
     public void physicsLoop(){
     	particleLoop = new AnimationTimer() {
             public void handle(long now) {
             	if(Settings.RENDER_GAME){
 					objectManager.addPhysics();
 					objectManager.checkCollisions();
 					if(Settings.ALLOW_PHYSICS){
 						if(!debrisLayer.getChildren().isEmpty()){
 							if(debrisLayer.getChildren().size()>Settings.DEBRIS_LIMIT-50){
 								debrisLayer.getChildren().remove(50, 100);
 							}
 						}
 					}
 					if(!Settings.ALLOW_PHYSICS){
 						if(!debrisLayer.getChildren().isEmpty()){
 							if(debrisLayer.getChildren().size()>Settings.DEBRIS_LIMIT){
 								debrisLayer.getChildren().remove(50, 100);
 							}
 						}
 					}
 				}
             }
         };
         particleLoop.start();
     }

     public void drawOverlay(GraphicsContext gc){
     	if(Settings.DEBUG_MODE){
 		gc.setFill(Color.WHITE);
 		gc.fillRect(0,0,Settings.WIDTH, Settings.HEIGHT);
     	}
     }

     public void levelThread() {
 		Timeline levelUpdateLoop = new Timeline();
 		levelUpdateLoop.setCycleCount(Timeline.INDEFINITE);

 		KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.256), // 60 FPS
 				new EventHandler<ActionEvent>() {
 					public void handle(ActionEvent ae) {

 						if(imageReady == true){
 							checkCollisions  = true;
 							imageReady = false;
 						}
 						//loader.procedurallyCreateLevel();
 						System.out.println("Amount of objects in debris layer: " + debrisLayer.getChildren().size());
 						System.out.println("Amount of objects in game layer: " + playfieldLayer.getChildren().size());
 						System.out.println("Amount of objects in bottom layer: " + bottomLayer.getChildren().size());
 						System.out.println("Amount of objects in radar layer: " + radarLayer.getChildren().size());
 						System.out.println("Amount of objects in level layer: " + levelLayer.getChildren().size());
 						System.out.println();
 						System.out.println();
 						System.out.println("Amount of objects in object manager: " + objectManager.object.size());
 						System.out.println("Amount of objects in debris manager: " + debrisManager.debris.size());
 						System.out.println("Amount of objects in level manager: " + loader.tileManager.tile.size());
 						System.out.println();
 						System.out.println();
 						System.out.println("---------------------------------------------------------------------------");

 					}
 				});
 		levelUpdateLoop.getKeyFrames().add(keyFrame);
 		levelUpdateLoop.play();
 	}
	public void translateObjects(ObservableList<Node> rootPane) {
		TextFPS = new Text("FPS : ");
		TextFPS.setTranslateX(15);
		TextFPS.setTranslateY(60);
		radarLayer.setTranslateX(0);
		radarLayer.setTranslateY(-40);
		TextFPS.setFill(Color.WHITE);
		TextFPS.setFont(Font.font("AERIAL", FontWeight.BOLD, 20));
		rootPane.add(TextFPS);
		rootPane.add(radarLayer);
        mainRoot.getChildren().add(fadeScreen);
	}
	public void reset(){
		clearAll();
		energyMeter = null;
     	scoreBoard = null;
     	healthBar = null;
        loader.loadPixelMap();
     	loader.createPlayers();
     	energyMeter = new EnergyMeter(this,(int) (Settings.WIDTH-300), 50, 300, 25);
     	scoreBoard = new ScoreBoard(this,(int)(Settings.WIDTH-100), 100,0,0);
     	healthBar = new HealthBar(this, 0, 50, 300, 25);
        processGameInput();
		getMainMenu().setMainMenu();
	}
	public void clearAll(){
		playfieldLayer.getChildren().clear();
		debrisLayer.getChildren().clear();
		bottomLayer.getChildren().clear();
		radarLayer.getChildren().clear();
		levelLayer.getChildren().clear();
		debrisManager.clearAll();
		objectManager.clearAll();
		loader.clearTiles();
        fadeScreen.getChildren().clear();

	}
    public EnergyMeter getEnergyMeter() {
		return energyMeter;
	}
	public void setEnergyMeter(EnergyMeter energyMeter) {
		this.energyMeter = energyMeter;
	}
	public MainMenu getMainMenu() {
		return mainMenu;
	}
	public void setMainMenu(MainMenu mainMenu) {
		this.mainMenu = mainMenu;
	}
	public Stage getMainWindow() {
		return mainWindow;
	}
	public void setMainWindow(Stage mainWindow) {
		this.mainWindow = mainWindow;
	}
 	public GameCamera getCamera() {
 		return camera;
 	}
 	public void setCamera(GameCamera camera) {
 		this.camera = camera;
 	}
     public GameLoader getloader() {
 		return loader;
 	}
 	public void setLevelManager(GameLoader loader) {
 		this.loader = loader;
 	}
 	public Pane getFadeScreen(){
 		return fadeScreen;
 	}
 	public Parent getRoot() {
 		return scene.getRoot();
 	}
 	public void setRoot(Parent root) {
 		scene.setRoot(root);
 	}
 	public Pane getGameRoot(){
 		return root;
 	}
     public GameObjectManager getObjectManager() {
 		return objectManager;
 	}
 	public void setObjectManager(GameObjectManager objectManager) {
 		this.objectManager = objectManager;
 	}
 	public GameDebrisManager getDebrisManager() {
 		return debrisManager;
 	}
 	public void setDebrisManager(GameDebrisManager debrisManager) {
 		this.debrisManager = debrisManager;
 	}
     public GameParticleManager getParticleManager() {
 		return particleManager;
 	}
 	public void setParticleManager(GameParticleManager particleManager) {
 		this.particleManager = particleManager;
 	}
     public Pane getPlayfieldLayer() {
 		return playfieldLayer;
 	}
 	public void setPlayfieldLayer(Pane playfieldLayer) {
 		this.playfieldLayer = playfieldLayer;
 	}
 	public Pane getRadarLayer() {
 		return radarLayer;
 	}
 	public void setRadarLayer(Pane radarLayer) {
 		this.radarLayer = radarLayer;
 	}
 	public Pane getLevelLayer(){
 		return levelLayer;
 	}
    public ScoreBoard getScoreBoard() {
		return scoreBoard;
	}
	public void setScoreBoard(ScoreBoard scoreBoard) {
		this.scoreBoard = scoreBoard;
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
 	public void setKeyInput(GameKeyInputManager keyInput) {
 		this.keyInput = keyInput;
 	}
 	public Pane getDebrisLayer() {
 		return debrisLayer;
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
 	public void showCursor(boolean choice, Scene scene) {
 		if (!choice)
 			scene.setCursor(Cursor.NONE);

 		else if (choice)
 			scene.setCursor(Cursor.DEFAULT);
 	}
 	public void allowMouseInput(boolean choice){
 		if(choice)
 		mouseInput.processInput(this,getloader().getPlayer(), scene);
 	}
     public static void main(String[] args) {
         launch(args);
     }
 }
