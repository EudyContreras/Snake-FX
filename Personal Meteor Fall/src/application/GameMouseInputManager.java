package application;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

public class GameMouseInputManager {
	
	public static int mouseX , mouseY;
	Game game;
	public void processInput(Game game, Player player,Scene scene){
		this.game = game;
	scene.setOnMouseMoved(new EventHandler<MouseEvent>() {

        public void handle(MouseEvent e) {
           
    		mouseX = (int) (e.getX()/ Settings.SCALE);
    		mouseY = (int) (e.getY()/ Settings.SCALE);
    		if(Settings.ALLOW_MOUSE_INPUT){
//    			if(game.getLoader().spotLight!=null){
//    				game.getLoader().spotLight.setY((float)e.getY()-100);
//    				game.getLoader().spotLight.setX((float)e.getX()-100);
//    					e.consume();
//    				}
    		}
        }
    });
	scene.setOnMouseDragged(new EventHandler<MouseEvent>() {

        public void handle(MouseEvent e) {
            
    		mouseX = (int) (e.getX()/ Settings.SCALE);
    		mouseY = (int) (e.getY()/ Settings.SCALE);
    		if(Settings.ALLOW_MOUSE_INPUT){
    			if(game!=null){
    			//if (e.getX() > 0 && e.getX() < GameSettings.WIDTH-player.getWidth()) {
    				if(player!=null)
    					player.setY((float)e.getY()-100);
    					e.consume();
    				}
    			//}
    		}
        }
    });
	scene.setOnMousePressed(new EventHandler<MouseEvent>() {
		
        public void handle(MouseEvent e) {
        	if(e.isPrimaryButtonDown()){
        		player.fireLaser();
        	}
            
        	if(e.isSecondaryButtonDown()){
        		player.fireLaser();
        	}
        	
        	if(e.isMiddleButtonDown()){
        		player.fireLaser();
        	}
        }
    });
	}
	public static int getMouseX() {
		return mouseX;
	}
	public static int getMouseY() {
		return mouseY;
	}
}