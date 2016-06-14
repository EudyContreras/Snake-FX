package com.EudyContreras.Snake.Controllers;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import com.EudyContreras.Snake.AbstractModels.AbstractParticlesEffect;
import com.EudyContreras.Snake.FrameWork.GameManager;

import javafx.scene.canvas.GraphicsContext;

public class GameDebrisController {

	private LinkedList<AbstractParticlesEffect> debris;
	private LinkedList<AbstractParticlesEffect> particles;
	private AbstractParticlesEffect tempDebris;
	private AbstractParticlesEffect tempParticle;
	public GameManager game;

	public GameDebrisController(GameManager game) {
		this.game = game;
		this.debris = new LinkedList<AbstractParticlesEffect>();
		this.particles = new LinkedList<AbstractParticlesEffect>();
	}

	public void addParticle(AbstractParticlesEffect particle) {
		Collections.addAll(particles, particle);
	}

	public void addDebris(AbstractParticlesEffect debrisEffect) {
		Collections.addAll(debris, debrisEffect);
	}

	public void addDebrisA(AbstractParticlesEffect... db) {
		if (db.length > 1) {
			debris.addAll(Arrays.asList(db));
		} else {
			debris.add(db[0]);
		}
	}
	public void addParticleA(AbstractParticlesEffect... db) {
		if (db.length > 1) {
			particles.addAll(Arrays.asList(db));
		} else {
			particles.add(db[0]);
		}
	}
	public void updateAllStream(GraphicsContext gc){
		debris.stream().parallel().forEach(AbstractParticlesEffect::move);
		particles.stream().parallel().forEach(AbstractParticlesEffect::move);
		debris.forEach(AbstractParticlesEffect::updateUI);
		particles.forEach(AbstractParticlesEffect::updateUI);
		debris.forEach(AbstractParticlesEffect::collide);
		particles.forEach(AbstractParticlesEffect::collide);
		removeDeadDebris();
		removeDeadParticles();
	}
	public synchronized void updateAll(GraphicsContext gc) {
		Iterator<AbstractParticlesEffect> debrisList = debris.iterator();
		Iterator<AbstractParticlesEffect> particleList = particles.iterator();
		while(debrisList.hasNext()) {
			AbstractParticlesEffect tempDebris = debrisList.next();
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
			AbstractParticlesEffect tempParticle = particleList.next();
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

		Iterator<AbstractParticlesEffect> particleList = particles.iterator();
		while(particleList.hasNext()) {

			AbstractParticlesEffect particle = particleList.next();

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

		Iterator<AbstractParticlesEffect> debrisList = debris.iterator();
		while(debrisList.hasNext()) {

			AbstractParticlesEffect debris = debrisList.next();

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
			AbstractParticlesEffect tempDebris = debris.get(i);
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
			AbstractParticlesEffect tempParticle = particles.get(i);
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
			AbstractParticlesEffect tempDebris = debris.get(i);
			tempDebris.move();
		}
		for(int i = 0; i<particles.size(); i++){
			AbstractParticlesEffect tempParticle = particles.get(i);
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

	public LinkedList<AbstractParticlesEffect> getDebrisList() {
		return debris;
	}

	public LinkedList<AbstractParticlesEffect> getParticleList() {
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
