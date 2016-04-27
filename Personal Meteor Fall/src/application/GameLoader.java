package application;


import java.awt.image.BufferedImage;
import java.util.Random;
import javafx.scene.image.Image;


public class GameLoader {
	private BufferedImage level;
	private BufferedImage level1;
	private BufferedImage level2;
	private BufferedImage level3;
	private BufferedImage level4;
	private BufferedImage level5;
	private BufferedImage level6;
	private int levelWidth;
	private int levelHeight;
	private int pixel;
	private int red;
	private int green;
	private int blue;
	private double Front_Distance_LOD = 1;
	private double Rear_Distance_LOD = 0;
	private float randomRotation;	
	private double randomOpacity;
    private double randomSize;
    private float speed;
	private Game game;
	TileMap texture;
	GameObjectManager objectManger;
	GameTileManager tileManager;
    Player player;
    Random rand ;
    Stars star ;
    LightBeam spotLight;
	

    
	public GameLoader(Game game) {
		this.game = game;
		this.rand = new Random();
		this.objectManger = game.getObjectManager();
		this.tileManager = new GameTileManager(game);
		loadLevelManager();
	}
	public void loadLevelManager(){
		this.level1 = GameImageLoader.loadImage("/JavaFXLevel.png");
		this.level2 = GameImageLoader.loadImage("/JavaFXLevel2.png");
		this.level3 = GameImageLoader.loadImage("/JavaFXLevel3.png");
		this.level4 = GameImageLoader.loadImage("/JavaFXLevel4.png");
		this.level5 = GameImageLoader.loadImage("/JavaFXLevel5.png");
		this.level6 = GameImageLoader.loadImage("/JavaFXLevel6.png");
		setLevel(level2);
		this.levelWidth = getLevel().getWidth();
		this.levelHeight = getLevel().getHeight();	
		this.game.levelLenght = 512*64;
		System.out.print("loaded");
	}
	public void loadPixelMap(){
		for(int i=0;i<Settings.MAX_AMOUNT_OF_STARS;i++){
			spawnBackgroundStars(true);
		}
		loadWholeLevel();
		game.levelLenght = 512*64;
	}
	public void cullTheLevel(){
		Front_Distance_LOD += 0.14;
		Rear_Distance_LOD += 0.14;
		if(Front_Distance_LOD>=512){
			Front_Distance_LOD = 512;
			Rear_Distance_LOD = 511;
		}
	}
	public void loadWholeLevel(){	
		speed = rand.nextFloat() * 8.0f + Settings.ASTEROID_MAX_SPEED*4;	
	    for(double row = 0; row<levelWidth; row++){
	        for(double col = 0; col<levelHeight; col++){
	            pixel = level.getRGB((int) row, (int) col);
	            red = (pixel >> 16) & 0xff;
	            green = (pixel >> 8) & 0xff;
	            blue = (pixel) & 0xff;
	            if(red == 0 && green == 255 && blue == 255){	    
	            	TileMap texture = new TileMap((float) (row*256), (float) (col*52),-4,0, GameLevelImage.tile4, LevelObjectID.BackgroundTile);
	            	tileManager.addTile(texture);
	            	game.bottomLayer.getChildren().add(texture.getView()); 
	            }
	            else if(red == 255 && green == 255 && blue == 255){
	            	texture = new TileMap((float) (row*256), (float) (col*58),-15,0, GameLevelImage.tile2, LevelObjectID.LevelTile);
	            	tileManager.addTile(texture);
	            	game.levelLayer.getChildren().add(texture.getView());         
	            }            
	            else if(red == 0 && green == 0 && blue == 255){	    
	            	TileMap texture = new TileMap((float) (row*200), (float) (col*48.5),-15,0, GameLevelImage.tile3, LevelObjectID.CollideBox);
	            	tileManager.addBox(texture);
	            	game.levelLayer.getChildren().add(texture.getView()); 
	            }
	            else if(red == 255 && green == 255 && blue == 0){	    
	            	TileMap texture = new TileMap((float) (row*200), (float) (col*53),-15,0, GameLevelImage.tile5, LevelObjectID.ExplosiveBarrel);
	            	tileManager.addBarrel(texture);
	            	game.levelLayer.getChildren().add(texture.getView()); 
	            }
	        }
	    }getLevel().flush();
	}
//	public void loadBySection(){	
//		speed = rand.nextFloat() * 1.0f + Settings.ASTEROID_MAX_SPEED;	
//	    for(double row = 0; row<Front_Distance_LOD; row++){
//	        for(int col = 0; col<levelHeight; col++){
//	            pixel = level.getRGB((int) row, col);
//	            red = (pixel >> 16) & 0xff;
//	            green = (pixel >> 8) & 0xff;
//	            blue = (pixel) & 0xff;
//	            
//	            if(red == 255 && green == 255 && blue == 0){
//	            	GameSectionChecker checker = new GameSectionChecker(game,(float) (row*64), col*64,-4,0, GameLevelImage.tile1);
//	            	tileManager.addObject(checker);
//	            	game.root.getChildren().add(checker.getView());         
//	            }
//	            else if(red == 255 && green == 0 && blue == 0){	    
//	            	game.getObjectManager().addObject(new Asteroid(game, game.getPlayfieldLayer(), GameImageBank.asteroid, (float) (row*64),col*64, 0, -speed, 0, GameObjectID.asteroid));
//	            
//	            }
//	        }
//	    }getLevel().flush();
//	}
//	public void loadFirstSection(){	
//		GameLevelImage.firstSectionLoaded = true;
//		speed = rand.nextFloat() * 1.0f + Settings.ASTEROID_MAX_SPEED;	
//	    for(double row = 0; row<=100; row++){
//	        for(int col = 0; col<levelHeight; col++){
//	            pixel = level.getRGB((int) row, col);
//	            red = (pixel >> 16) & 0xff;
//	            green = (pixel >> 8) & 0xff;
//	            blue = (pixel) & 0xff;
//	            
//	            if(red == 255 && green == 255 && blue == 255){
//	            	TileMap texture = new TileMap((float) (row*64), col*64,-4,0, GameLevelImage.tile1);
//	            	tileManager.addObject(texture);
//	            	game.root.getChildren().add(texture.getView());         
//	            }
//	            else if(red == 255 && green == 0 && blue == 0){	    
//	            	game.getObjectManager().addObject(new Asteroid(game, game.getPlayfieldLayer(), GameImageBank.asteroid, (float) (row*64),col*64, 0, -speed, 0, GameObjectID.asteroid));
//	            
//	            }           
//	        }
//	    }getLevel().flush();
//	}
//	public void loadSecondSection(){	
//		GameLevelImage.secondSectionLoaded = true;
//		speed = rand.nextFloat() * 1.0f + Settings.ASTEROID_MAX_SPEED;	
//	    for(double row = 51; row<=150; row++){
//	        for(int col = 0; col<levelHeight; col++){
//	            pixel = level.getRGB((int) row, col);
//	            red = (pixel >> 16) & 0xff;
//	            green = (pixel >> 8) & 0xff;
//	            blue = (pixel) & 0xff;
//	            
//	            if(red == 255 && green == 255 && blue == 255){
//	            	TileMap texture = new TileMap((float) (row*64), col*64,-4,0, GameLevelImage.tile1);
//	            	tileManager.addObject(texture);
//	            	game.root.getChildren().add(texture.getView());         
//	            }
//	            else if(red == 255 && green == 0 && blue == 0){	    
//	            	game.getObjectManager().addObject(new Asteroid(game, game.getPlayfieldLayer(), GameImageBank.asteroid, (float) (row*64),col*64, 0, -speed, 0, GameObjectID.asteroid));
//	            
//	            }
//	        }
//	    }getLevel().flush();
//	}
//	public void loadThirdSection(){	
//		GameLevelImage.thirdSectionLoaded = true;
//		speed = rand.nextFloat() * 1.0f + Settings.ASTEROID_MAX_SPEED;	
//	    for(double row = 51; row<=150; row++){
//	        for(int col = 0; col<levelHeight; col++){
//	            pixel = level.getRGB((int) row, col);
//	            red = (pixel >> 16) & 0xff;
//	            green = (pixel >> 8) & 0xff;
//	            blue = (pixel) & 0xff;
//	            
//	            if(red == 255 && green == 255 && blue == 255){
//	            	TileMap texture = new TileMap((float) (row*64), col*64,-4,0, GameLevelImage.tile1);
//	            	tileManager.addObject(texture);
//	            	game.root.getChildren().add(texture.getView());         
//	            }
//	            else if(red == 255 && green == 0 && blue == 0){	    
//	            	game.getObjectManager().addObject(new Asteroid(game, game.getPlayfieldLayer(), GameImageBank.asteroid, (float) (row*64),col*64, 0, -speed, 0, GameObjectID.asteroid));
//	            
//	            }
//	        }
//	    }getLevel().flush();
//	}
//	public void loadFourthSection(){
//		GameLevelImage.fourthSectionLoaded = true;
//		speed = rand.nextFloat() * 1.0f + Settings.ASTEROID_MAX_SPEED;	
//	    for(double row = 301; row<=400; row++){
//	        for(int col = 0; col<levelHeight; col++){
//	            pixel = level.getRGB((int) row, col);
//	            red = (pixel >> 16) & 0xff;
//	            green = (pixel >> 8) & 0xff;
//	            blue = (pixel) & 0xff;
//	            
//	            if(red == 255 && green == 255 && blue == 255){
//	            	TileMap texture = new TileMap((float) (row*64), col*64,-4,0, GameLevelImage.tile1);
//	            	tileManager.addObject(texture);
//	            	game.root.getChildren().add(texture.getView());         
//	            }
//	            else if(red == 255 && green == 0 && blue == 0){	    
//	            	game.getObjectManager().addObject(new Asteroid(game, game.getPlayfieldLayer(), GameImageBank.asteroid, (float) (row*64),col*64, 0, -speed, 0, GameObjectID.asteroid));
//	            
//	            }
//	        }
//	    }getLevel().flush();
//	}
//	public void loadFithSection(){	
//		GameLevelImage.fithSectionLoaded = true;
//		speed = rand.nextFloat() * 1.0f + Settings.ASTEROID_MAX_SPEED;	
//	    for(double row = 401; row<512; row++){
//	        for(int col = 0; col<levelHeight; col++){
//	            pixel = level.getRGB((int) row, col);
//	            red = (pixel >> 16) & 0xff;
//	            green = (pixel >> 8) & 0xff;
//	            blue = (pixel) & 0xff;
//	            
//	            if(red == 255 && green == 255 && blue == 255){
//	            	TileMap texture = new TileMap((float) (row*64), col*64,-4,0, GameLevelImage.tile1);
//	            	tileManager.addObject(texture);
//	            	game.root.getChildren().add(texture.getView());         
//	            }
//	            else if(red == 255 && green == 0 && blue == 0){	    
//	            	game.getObjectManager().addObject(new Asteroid(game, game.getPlayfieldLayer(), GameImageBank.asteroid, (float) (row*64),col*64, 0, -speed, 0, GameObjectID.asteroid));
//	            
//	            }
//	        }
//	    }getLevel().flush();
//	}
	public void loadPlayer(){
		for(int row = 0; row<levelWidth; row++){
			for(int col = 0; col<levelHeight; col++){
				pixel = level1.getRGB(row, col);
				red = (pixel >> 16) & 0xff;
				green = (pixel >> 8) & 0xff;
				blue = (pixel) & 0xff;								
				if(red == 0 && green == 0 && blue == 255){
					this.player = new Player(game, game.getPlayfieldLayer(), GameImageBank.player, row*20,col*20, 0, 0, 0, 0, Settings.PLAYER_HEALTH, 0,Settings.PLAYER_SPEED, GameObjectID.player, game.getObjectManager());
					game.getObjectManager().addObject(player);
				}
			}
		}getLevel().flush();
	}
	public void switcLevel(){
		game.getObjectManager().clearAll();
		game.getDebrisManager().clearAll();
		game.getParticleManager().clearAll();
		game.getDebrisLayer().getChildren().clear();
		game.getPlayfieldLayer().getChildren().clear();
		game.getBottomLayer().getChildren().clear();
		Front_Distance_LOD = 512;
		Rear_Distance_LOD = 511;
		setLevel(level2);
		loadPlayer();
		switch(GameLevelImage.LEVEL)
		{
		case 1:
			setLevel(level2);
			break;
		case 2:
			setLevel(level3);
			break;
		case 3:
			setLevel(level4);
			break;
		case 4:
			setLevel(level4);
			break;
		case 5:
			setLevel(level4);
			break;
		case 6:
			setLevel(level5);
			break;
		case 7:
			setLevel(level6);
			break;

		}
		
		GameLevelImage.LEVEL++;
	}
	public BufferedImage getLevel(){
		return level;
	}
	public void setLevel(BufferedImage level){
		this.level = level;
	}
	public void procedurallyCreateLevel() {		
		if(Front_Distance_LOD<=512 && Rear_Distance_LOD<=511){
			if(player!=null){
				loadWholeLevel();
				cullTheLevel();
			}
		}
	}
	public void updateStuff(){
		tileManager.updateTiles();
		tileManager.updateBarrels();
		tileManager.updateBoxes();
		tileManager.checkIfRemovable();
		
	}
	public void clearTiles(){
		tileManager.clearAll();
	}
	public void createGameObject(float x, float y, Image image) {
		TileMap texture = new TileMap(x, y,-2,0, image);
		if (image.isBackgroundLoading())
			game.levelLayer.getChildren().add(texture.getView());
	}
	public void createPlayers() {

		float x = (float) (GameImageBank.player.getWidth() / 2.0);
		float y = (float) (Settings.HEIGHT * 0.5);
		player = new Player(game, game.getPlayfieldLayer(), GameImageBank.player, x, y, 0, 0, 0, 0, Settings.PLAYER_HEALTH, 0,Settings.PLAYER_SPEED, GameObjectID.player, game.getObjectManager());
		game.getObjectManager().addObject(player);
		spawnRadar();
	}
	public void spawnAsteroids(boolean random) {
		if(texture.getVelX()<=-10){
		randomRotation = rand.nextFloat()*(1.2f - -1.2f + 1)+ -1.2f;
		
		if (random && rand.nextInt(Settings.ASTEROID_SPAWN_RANDOMNESS) != 0) {
			return;
		}
		randomSize= rand.nextFloat()*(GameImageBank.asteroid.getWidth()+150 - GameImageBank.asteroid.getWidth()-50  + 1)+ GameImageBank.asteroid.getWidth()-50 ;
		Image image = GameImageBank.asteroid;
	    speed = rand.nextFloat() * 3.0f + Settings.ASTEROID_MAX_SPEED*4*(Settings.FRAMECAP); 
		float y = (float) (rand.nextDouble() * ((Settings.HEIGHT - image.getHeight()) - Settings.START_Y +1) + Settings.START_Y);
		float x = (float) (rand.nextDouble() * (Settings.WIDTH+1000  - Settings.WIDTH+200 +1) +Settings.WIDTH+200 );
		checkTheAsteroidsPosition(x,y);
		Asteroid asteroid = new Asteroid(game, game.getPlayfieldLayer(), image,randomSize, x, y, 0, -speed, 0, GameObjectID.asteroid);
		game.getObjectManager().addObject(asteroid);
		}
	}
	public void spawnRadar(){
		int x = (int) (Settings.WIDTH-GameImageBank.radar.getWidth());
		int y = (int) (Settings.HEIGHT-GameImageBank.radar.getHeight());
		game.getObjectManager().addObject( new Radar(game, GameImageBank.radar, game.getRadarLayer(),0.5, x,y,0,null));
		game.getObjectManager().addObject( new Radar(game, GameImageBank.radarGlass, game.getRadarLayer(),1, x,y,1,null));
	}
	public void checkTheAsteroidsPosition(float x, float y){		

		int xPos = (int) (Settings.WIDTH-GameImageBank.radar.getWidth());
		int yPos = (int) (Settings.HEIGHT-GameImageBank.radar.getHeight());
		
		if(x<Settings.WIDTH+600 && y<=100){
			game.getDebrisManager().addObject(new RadarMarker(game,3,xPos+65,yPos+35));
		}
		else if(x<Settings.WIDTH+600 && y>100 && y<Settings.HEIGHT/2){
			game.getDebrisManager().addObject(new RadarMarker(game,3,xPos+40,yPos+70));
		}
		else if(x<Settings.WIDTH+600 && y>=Settings.HEIGHT-350 ){
			game.getDebrisManager().addObject(new RadarMarker(game,3,xPos+65,yPos+160));
		}
		else if(x<Settings.WIDTH+600 && y<Settings.HEIGHT-350 && y>Settings.HEIGHT/2){
			game.getDebrisManager().addObject(new RadarMarker(game,3,xPos+40,yPos+120));
		}		
		//right side of radar
		else if(x>Settings.WIDTH+600 && y<=100){
			game.getDebrisManager().addObject(new RadarMarker(game,3,xPos+120,yPos+35));
		}
		else if(x>Settings.WIDTH+600 && y>100 && y<Settings.HEIGHT/2){
			game.getDebrisManager().addObject(new RadarMarker(game,3,xPos+160,yPos+70));
		}
		else if(x>Settings.WIDTH+600 && y>=Settings.HEIGHT-350 ){
			game.getDebrisManager().addObject(new RadarMarker(game,3,xPos+150,yPos+160));
		}
		else if(x>Settings.WIDTH+600 && y<Settings.HEIGHT-350 && y>Settings.HEIGHT/2){
			game.getDebrisManager().addObject(new RadarMarker(game,3,xPos+120,yPos+130));
		}
		
	}
	public void spawnBackgroundStars(boolean random) {
		randomOpacity = rand.nextFloat()*(0.5- 0.2 + 1)+ 0.2;
		float randomSize = rand.nextFloat()*(1f- 0.8f + 1)+ 0.8f;
		float y = (float) (rand.nextDouble() * ((Settings.HEIGHT - 10) - Settings.START_Y +1) + Settings.START_Y);
		float x = (float) (rand.nextDouble()* (Settings.WIDTH - 20));
		star = new Stars(game, x, y,0,0,  randomSize,randomOpacity);
	}
	public void spawnBackgroundDebris(boolean random) {
		float randomSize = rand.nextFloat()*(45- 15 + 1)+ 15;
		randomRotation = rand.nextFloat()*(1.2f - -1.2f + 1)+ -1.2f*(Settings.FRAMECAP); 
		if (random && rand.nextInt(20) != 0) {
			return;
		}
		float speed = rand.nextFloat()*( 15 - 3 + 1) + 3*(Settings.FRAMECAP); 
		float y = (float) (rand.nextDouble() * ((Settings.HEIGHT - 10) - Settings.START_Y +1) + Settings.START_Y);
		float x = 2000 ;
		Meteors meteor = new Meteors(game, x, y,-speed,randomRotation,  randomSize,2);
		game.getDebrisManager().addObject(meteor);
	}
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}

}
