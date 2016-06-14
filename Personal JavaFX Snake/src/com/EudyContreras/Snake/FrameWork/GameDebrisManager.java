package com.EudyContreras.Snake.FrameWork;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import com.EudyContreras.Snake.AbstractModels.AbstractDebrisEffect;
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
		Collections.addAll(particles, particle);
	}

	public void addDebris(AbstractDebrisEffect debrisEffect) {
		Collections.addAll(debris, debrisEffect);
	}

	public void addDebrisA(AbstractDebrisEffect... db) {
		if (db.length > 1) {
			debris.addAll(Arrays.asList(db));
		} else {
			debris.add(db[0]);
		}
	}
	public void addParticleA(AbstractDebrisEffect... db) {
		if (db.length > 1) {
			particles.addAll(Arrays.asList(db));
		} else {
			particles.add(db[0]);
		}
	}
	public void updateAllStream(GraphicsContext gc){
		debris.stream().parallel().forEach(AbstractDebrisEffect::move);
		particles.stream().parallel().forEach(AbstractDebrisEffect::move);
		debris.forEach(AbstractDebrisEffect::updateUI);
		particles.forEach(AbstractDebrisEffect::updateUI);
		debris.forEach(AbstractDebrisEffect::collide);
		particles.forEach(AbstractDebrisEffect::collide);
		removeDeadDebris();
		removeDeadParticles();
	}
	public synchronized void updateAll(GraphicsContext gc) {
		Iterator<AbstractDebrisEffect> debrisList = debris.iterator();
		Iterator<AbstractDebrisEffect> particleList = particles.iterator();
		while(debrisList.hasNext()) {
			AbstractDebrisEffect tempDebris = debrisList.next();
			tempDebris.move();
			tempDebris.updateUI();
			tempDebris.draw(gc);
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
		while(particleList.hasNext()) {
			AbstractDebrisEffect tempParticle = particleList.next();
			tempParticle.move();
			tempParticle.updateUI();
			tempParticle.draw(gc);
			tempParticle.collide();
			if (!tempParticle.isAlive()) {
				game.getInnerParticleLayer().getChildren().remove(tempParticle.getView());
				game.getInnerParticleLayer().getChildren().remove(tempParticle.getShape());
				game.getOuterParticleLayer().getChildren().remove(tempParticle.getShape());
				game.getOuterParticleLayer().getChildren().remove(tempParticle.getView());
				particleList.remove();
				continue;
			}
		}
	}
	private void removeDeadParticles() {

		Iterator<AbstractDebrisEffect> particleList = particles.iterator();
		while(particleList.hasNext()) {

			AbstractDebrisEffect particle = particleList.next();

			if (!particle.isAlive()) {

				game.getInnerParticleLayer().getChildren().remove(particle.getView());
				game.getInnerParticleLayer().getChildren().remove(particle.getShape());
				game.getOuterParticleLayer().getChildren().remove(particle.getShape());
				game.getOuterParticleLayer().getChildren().remove(particle.getView());

				particleList.remove();

			}

		}

	}
	private void removeDeadDebris() {

		Iterator<AbstractDebrisEffect> debrisList = debris.iterator();
		while(debrisList.hasNext()) {

			AbstractDebrisEffect debris = debrisList.next();

			if (!debris.isAlive()) {

				game.getDebrisLayer().getChildren().remove(debris.getView());
				game.getDebrisLayer().getChildren().remove(debris.getShape());
				game.getInnerParticleLayer().getChildren().remove(debris.getView());
				game.getInnerParticleLayer().getChildren().remove(debris.getShape());

				debrisList.remove();

			}
		}
	}
	public synchronized void updateAllFor(GraphicsContext gc) {
		for(int i = 0; i<debris.size(); i++){
			AbstractDebrisEffect tempDebris = debris.get(i);
			tempDebris.updateUI();
			tempDebris.draw(gc);
			tempDebris.collide();
			if (!tempDebris.isAlive()) {
				game.getDebrisLayer().getChildren().remove(tempDebris.getView());
				game.getDebrisLayer().getChildren().remove(tempDebris.getShape());
				game.getInnerParticleLayer().getChildren().remove(tempDebris.getView());
				game.getInnerParticleLayer().getChildren().remove(tempDebris.getShape());
				debris.remove(tempDebris);
			}
		}
		for(int i = 0; i<particles.size(); i++){
			AbstractDebrisEffect tempParticle = particles.get(i);
			tempParticle.updateUI();
			tempParticle.draw(gc);
			tempParticle.collide();
			if (!tempParticle.isAlive()) {
				game.getInnerParticleLayer().getChildren().remove(tempParticle.getView());
				game.getInnerParticleLayer().getChildren().remove(tempParticle.getShape());
				game.getOuterParticleLayer().getChildren().remove(tempParticle.getShape());
				game.getOuterParticleLayer().getChildren().remove(tempParticle.getView());
				particles.remove(tempParticle);
			}
		}
	}
	public synchronized void moveAllFor() {
		for(int i = 0; i<debris.size(); i++){
			AbstractDebrisEffect tempDebris = debris.get(i);
			tempDebris.move();
		}
		for(int i = 0; i<particles.size(); i++){
			AbstractDebrisEffect tempParticle = particles.get(i);
			tempParticle.move();
		}
	}
	public void updateDebris(GraphicsContext gc) {
		for (int i = 0; i < debris.size(); i++) {
			tempDebris = debris.get(i);
			tempDebris.updateUI();
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
			tempParticle.updateUI();
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
