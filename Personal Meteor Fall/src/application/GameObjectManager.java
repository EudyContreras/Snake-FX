package application;

import java.util.Iterator;
import java.util.LinkedList;

import javafx.scene.canvas.GraphicsContext;


public class GameObjectManager {
	
	public LinkedList<GameObject> object = new LinkedList<GameObject> ();
	private GameObject tempObject;
	public Game game;
	
	public GameObjectManager(Game gameJavaFX){
		this.game = gameJavaFX;
	}
	public void initialize() {

	}	
    public void update(GraphicsContext gc,long timePassed) {
        Iterator<? extends GameObject> spriteIter = object.iterator();
        while( spriteIter.hasNext()) {
            GameObject sprite = spriteIter.next();
            sprite.updateUI();
            sprite.addPhysics();
            sprite.updateAnimation(timePassed);
            sprite.draw(gc);
            sprite.move();
            sprite.checkRemovability();
            if( sprite.isRemovable() || !sprite.isAlive()) {
                sprite.removeFromLayer();
                spriteIter.remove();    
                continue;
            }
        }
    }
	public void buryYourDead() {
        Iterator<? extends GameObject> spriteIter = object.iterator();
        while( spriteIter.hasNext()) {
            GameObject sprite = spriteIter.next();
            sprite.checkRemovability();
            if( sprite.isRemovable() || !sprite.isAlive()) {
                sprite.removeFromLayer();
                spriteIter.remove();    
                continue;
            }
        }
		
	}
	public void updateAll(GraphicsContext gc,long timePassed){
		
		for(int i = 0; i<object.size(); i++){
			tempObject = object.get(i);
			tempObject.updateUI();
			tempObject.addPhysics();
			tempObject.updateAnimation(timePassed);
			tempObject.draw(gc);
			tempObject.move();
			tempObject.checkRemovability();
            if( tempObject.isRemovable() || !tempObject.isAlive()) {
            	tempObject.removeFromLayer();
                object.remove(i);            
            }
		}
	}
	public void updateUI(){
		
		for(int i = 0; i<object.size(); i++){
			tempObject = object.get(i);
			tempObject.updateUI();
		}
	}
	public void updateAnimation(long timePassed){
		
		for(int i = 0; i<object.size(); i++){
			tempObject = object.get(i);
			tempObject.updateAnimation(timePassed);
		}
	}
	public void move(){
		
		for(int i = 0; i<object.size(); i++){
			tempObject = object.get(i);
			tempObject.move();
		}		
	}
	public void draw(GraphicsContext gc){
		
		for(int i = 0; i<object.size(); i++){
			tempObject = object.get(i);
			tempObject.draw(gc);
		}		
	}
	public void addPhysics(){
		
		for(int i = 0; i<object.size(); i++){
			tempObject = object.get(i);
			tempObject.addPhysics();
		}		
	}
	public void checkIfRemoveable(){
		for(int i = 0; i<object.size(); i++){
			tempObject = object.get(i);
			tempObject.checkRemovability();
		}
	}
    public void checkCollisions() {
		for(int i = 0; i<object.size(); i++){
			tempObject = object.get(i);
			tempObject.checkCollision();
		}
    }
    public void checkStatus(){    	
    	if(game.getPlayfieldLayer().getChildren().size()>50){
    		game.getPlayfieldLayer().getChildren().remove(2, 35);
    	}
    }
    public void procedurallyCreateLevel(){
    	   Iterator<? extends GameObject> spriteIter = object.iterator();
           while( spriteIter.hasNext()) {
               GameObject sprite = spriteIter.next();
               sprite.createLevel();
           }
    }
	public void addObject(GameObject object){
		this.object.add(object);
	}
	public void removeObject(GameObject object){
		this.object.remove(object);
	}
	public void clearAll(){
		this.object.clear();
	}
	public GameObject findObject(GameObjectID id){
		for(GameObject go: object){
			if(go.getId() == id){
				return go;
			}
		}
		return null;
	}

}
