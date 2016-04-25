package com.SnakeGame.Core;


import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

import javafx.scene.canvas.GraphicsContext;


public class GameDebrisManager {


	private LinkedList<DebrisEffect> debris ;
	private LinkedList<DebrisEffect> particles ;
	private DebrisEffect tempDebris;
	public SnakeGame game;

	public GameDebrisManager(SnakeGame game){
		this.game = game;
		this.debris = new LinkedList<DebrisEffect> ();
		this.particles = new LinkedList<DebrisEffect>();
	}
	public void addParticle(DebrisEffect particle){
		particles.add(particle);
	}
	public void addDebris(DebrisEffect debrisEffect){
		debris.add(debrisEffect);
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
			if(!tempDebris.isAlive()){
				game.getDebrisLayer().getChildren().remove(tempDebris.view);
				game.getBottomLayer().getChildren().remove(tempDebris.view);
				game.getDebrisLayer().getChildren().remove(tempDebris.shape);
				game.getBottomLayer().getChildren().remove(tempDebris.shape);
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
			if(!tempDebris.isAlive()){
				game.getDebrisLayer().getChildren().remove(tempDebris.view);
				game.getBottomLayer().getChildren().remove(tempDebris.view);
				game.getDebrisLayer().getChildren().remove(tempDebris.shape);
				game.getBottomLayer().getChildren().remove(tempDebris.shape);
				debris.remove(i);
			}
		}
	}
	public void updateParticles(GraphicsContext gc){
		for(int i = 0; i<particles.size(); i++){
			tempDebris = particles.get(i);
			tempDebris.update();
			tempDebris.draw(gc);
			tempDebris.move();
			tempDebris.collide();
			if(!tempDebris.isAlive()){
				game.getParticleLayer().getChildren().remove(tempDebris.shape);
				game.getParticleLayer().getChildren().remove(tempDebris.view);
				particles.remove(i);
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
	public LinkedList<DebrisEffect> getDebrisList(){
		return debris;
	}
	public LinkedList<DebrisEffect> getParticleList(){
		return particles;
	}
	public void clearAll(){
		this.debris.clear();
		this.particles.clear();
	}

}
