package com.SnakeGame.FrameWork;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

import com.SnakeGame.AbstractModels.AbstractDebrisEffect;

import javafx.scene.canvas.GraphicsContext;

public class GameDebrisManager {

	private LinkedList<AbstractDebrisEffect> debris;
	private LinkedList<AbstractDebrisEffect> particles;
	private AbstractDebrisEffect tempDebris;
	private AbstractDebrisEffect tempParticle;
	public GameManager game;

	public GameDebrisManager(GameManager game) {
		this.game = game;
		this.debris = new LinkedList<AbstractDebrisEffect>();
		this.particles = new LinkedList<AbstractDebrisEffect>();
	}

	public void addParticle(AbstractDebrisEffect particle) {
		particles.add(particle);
	}

	public void addDebris(AbstractDebrisEffect debrisEffect) {
		debris.add(debrisEffect);
	}

//	public void addObject(AbstractDebrisEffect debris) {
//		this.debris.add(debris);
//	}

	public void addObjectA(AbstractDebrisEffect... db) {
		if (db.length > 1) {
			debris.addAll(Arrays.asList(db));
		} else {
			debris.add(db[0]);
		}
	}

	public void updateAll(GraphicsContext gc) {
		for (Iterator<AbstractDebrisEffect> debrisList = debris.iterator(); debrisList.hasNext();) {
			AbstractDebrisEffect tempDebris = debrisList.next();
			tempDebris.update();
			tempDebris.draw(gc);
			tempDebris.move();
			tempDebris.collide();
			if (!tempDebris.isAlive()) {
				game.getDebrisLayer().getChildren().remove(tempDebris.getView());
				game.getDebrisLayer().getChildren().remove(tempDebris.getShape());
				game.getInnerParticleLayer().getChildren().remove(tempDebris.getView());
				game.getInnerParticleLayer().getChildren().remove(tempDebris.getShape());
				debrisList.remove();
				continue;
			}
		}
		for (Iterator<AbstractDebrisEffect> particleList = particles.iterator(); particleList.hasNext();) {
			AbstractDebrisEffect tempParticle = particleList.next();
			tempParticle.update();
			tempParticle.draw(gc);
			tempParticle.move();
			tempParticle.collide();
			if (!tempParticle.isAlive()) {
				game.getDebrisLayer().getChildren().remove(tempParticle.getView());
				game.getDebrisLayer().getChildren().remove(tempParticle.getShape());
				game.getInnerParticleLayer().getChildren().remove(tempParticle.getView());
				game.getInnerParticleLayer().getChildren().remove(tempParticle.getShape());
				particleList.remove();
				continue;
			}
		}
	}

	public void updateDebris(GraphicsContext gc) {
		for (int i = 0; i < debris.size(); i++) {
			tempDebris = debris.get(i);
			tempDebris.update();
			tempDebris.draw(gc);
			tempDebris.move();
			tempDebris.collide();
			if (!tempDebris.isAlive()) {
				game.getDebrisLayer().getChildren().remove(tempDebris.getView());
				game.getDebrisLayer().getChildren().remove(tempDebris.getShape());
				game.getInnerParticleLayer().getChildren().remove(tempDebris.getView());
				game.getInnerParticleLayer().getChildren().remove(tempDebris.getShape());
				debris.remove(i);
			}
		}
	}

	public void updateParticles(GraphicsContext gc) {
		for (int i = 0; i < particles.size(); i++) {
			tempParticle = particles.get(i);
			tempParticle.update();
			tempParticle.draw(gc);
			tempParticle.move();
			tempParticle.collide();
			if (!tempParticle.isAlive()) {
				game.getInnerParticleLayer().getChildren().remove(tempParticle.getView());
				game.getInnerParticleLayer().getChildren().remove(tempParticle.getShape());
				game.getOuterParticleLayer().getChildren().remove(tempParticle.getShape());
				game.getOuterParticleLayer().getChildren().remove(tempParticle.getView());
				particles.remove(i);
			}
		}
	}

	public void checkCollision() {
		for (int i = 0; i < debris.size(); i++) {
			tempDebris = debris.get(i);
			tempDebris.collide();
		}
	}

	public void checkIfAlive() {
		for (int i = 0; i < debris.size(); i++) {
			tempDebris = debris.get(i);
			if (!tempDebris.isAlive()) {
				game.getFirstLayer().getChildren().remove(tempDebris.getShape());
				debris.remove(i);
			}
			if (tempDebris.getShape() == null) {
				debris.remove(i);

			}
		}
	}

	public LinkedList<AbstractDebrisEffect> getDebrisList() {
		return debris;
	}

	public LinkedList<AbstractDebrisEffect> getParticleList() {
		return particles;
	}
	public void clearDebris(){
		this.debris.clear();
	}
	public void clearParticles(){
		this.particles.clear();
	}
	public void clearAll() {
		this.debris.clear();
		this.particles.clear();
	}

}
