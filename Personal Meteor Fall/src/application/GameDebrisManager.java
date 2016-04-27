package application;


import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;


public class GameDebrisManager {
	
	
	public List<DebrisEffect> debris ;
	public Game game;
	private DebrisEffect tempDebris;
	
	public GameDebrisManager(Game game){
		this.game = game;
		this.debris = new LinkedList<DebrisEffect> ();
		//this.debris.ensureCapacity(20);
	}
	public void addObject(DebrisEffect debris){
		this.debris.add(debris);
	}
    public void addObjectA(DebrisEffect... db) {
        if (db.length > 1) {
        	debris.addAll(Arrays.asList(db));
        } else {
        	debris.add(db[0]);
        }
    } 
	public void update(GraphicsContext gc){
		for(Iterator<DebrisEffect> debrisList = debris.iterator(); debrisList.hasNext();){
			DebrisEffect tempDebris = debrisList.next();
			tempDebris.update();
			tempDebris.draw(gc);
			tempDebris.move();
			tempDebris.collide();			
			if(!tempDebris.isAlive() || tempDebris.shape==null){
				game.getDebrisLayer().getChildren().remove(tempDebris.shape);
				debrisList.remove();
				continue;
			}
		}
	}
	
	public void updateDebris(GraphicsContext gc){	
		for(int i = 0; i<debris.size(); i++){
			tempDebris = debris.get(i);
			tempDebris.update();
			tempDebris.draw(gc);
			tempDebris.move();
			tempDebris.collide();
			if(!tempDebris.isAlive()|| tempDebris.shape==null){
				game.getDebrisLayer().getChildren().remove(tempDebris.shape);
				debris.remove(i);
			}
		}
	}
	public void checkCollision(){	
		for(int i = 0; i<debris.size(); i++){
			tempDebris = debris.get(i);
			tempDebris.collide();
		}
	}
	public void checkIfAlive(){
		for(int i = 0; i<debris.size(); i++){
			tempDebris = debris.get(i);
			if(!tempDebris.isAlive()){
				game.getDebrisLayer().getChildren().remove(tempDebris.shape);
				debris.remove(i);
			}
			if(tempDebris.shape == null){
				debris.remove(i);

			}
		}
	}
	public void clearAll(){
		this.debris.clear();
	}

}

