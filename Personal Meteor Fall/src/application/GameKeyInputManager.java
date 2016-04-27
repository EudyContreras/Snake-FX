package application;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class GameKeyInputManager {
	boolean[] keyDown = new boolean[6];
	Game game;
	public GameKeyInputManager(){
		keyDown[0] = false;
		keyDown[1] = false;
		keyDown[2] = false;
		keyDown[3] = false;
		keyDown[4] = false;
		keyDown[5] = false;
	}
	
    public void processInput(Game game,Player p,Scene scene){
    	this.game = game;
    	scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
    		
            public void handle(KeyEvent e) {
            	
                if (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.W){
                	p.setMovingUp(true);
                	p.setMovingDown(false);
                	keyDown[0] = true;	
                }
                if (e.getCode() == KeyCode.DOWN || e.getCode() == KeyCode.S){
                	p.setMovingDown(true);
                	p.setMovingUp(false);
                	keyDown[1] = true;	
                }
                if (e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.A){
                	keyDown[2] = true;	
                }
                if (e.getCode() == KeyCode.RIGHT || e.getCode() == KeyCode.D){
                	keyDown[3] = true;
                	p.setVelX(4);
                }               
                if (e.getCode() == KeyCode.SPACE) {
                	if(Settings.RENDER_GAME)
                		p.fireLaser(); 
                	keyDown[4] = true;            
                }
                if (e.getCode() == KeyCode.SHIFT) {
                	keyDown[5] = true;
                	Settings.AFTERBURNER = true;   
                }
                if (e.getCode() == KeyCode.P) {               		
                		game.pauseGame();                		
                }
                if (e.getCode() == KeyCode.R) {
            			game.resumeGame();
                }
                if (e.getCode() == KeyCode.F) {
        			p.turnOffLamp();
                }
                if (e.getCode() == KeyCode.ESCAPE) {
        				game.getMainMenu().setMainMenu();
        			//	game.showCursor(true, scene);
                }
                if (e.getCode() == KeyCode.ENTER){
                		p.shakeDuration = 90;
                		p.shake = true;
                }
            }
        });
    	scene.setOnKeyReleased(new EventHandler<KeyEvent>() {

            public void handle(KeyEvent e) {
               
            	if (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.W){
            		keyDown[0] = false;
            	 	p.setMovingUp(false);
                }
                if (e.getCode() == KeyCode.DOWN || e.getCode() == KeyCode.S){
                	keyDown[1] = false;
                  	p.setMovingDown(false);
                }
                if (e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.A){
                	keyDown[2] = false;
                }
                if (e.getCode() == KeyCode.RIGHT || e.getCode() == KeyCode.D){
                	keyDown[3] = false;
                }
                if (e.getCode() == KeyCode.SPACE) {
                	keyDown[4] = false;               	          
                }
                if (e.getCode() == KeyCode.SHIFT) {             	 	
               	 	keyDown[5] = false;              	 	
                }
				if(!keyDown[0] && !keyDown[1]){
				//	p.velY2 = 0;
				}
				if(!keyDown[2] && !keyDown[3]){
				//	p.setVelX(0);
				}
				if(!keyDown[4]){		
					p.applyRecoil = false;  			
				}
				if(!keyDown[5]){		
					Settings.AFTERBURNER = false;   
				}
            }
        });

    }

}
