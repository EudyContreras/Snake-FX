package com.EudyContreras.Snake.Controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.EudyContreras.Snake.AbstractModels.AbstractParticlesEffect;
import com.EudyContreras.Snake.Application.GameManager;

public class GameDebrisController {

	private List<AbstractParticlesEffect> debris;
	private List<AbstractParticlesEffect> particles;
	private AbstractParticlesEffect tempDebris;
	private AbstractParticlesEffect tempParticle;
	public GameManager game;

	public GameDebrisController(GameManager game) {
		this.game = game;
		this.debris = new ArrayList<AbstractParticlesEffect>();
		this.particles = new ArrayList<AbstractParticlesEffect>();
	}

	public void addParticle(AbstractParticlesEffect particle) {
		Collections.addAll(particles, particle);
	}
	public void addParticle(AbstractParticlesEffect... particle) {
		Collections.addAll(particles, particle);
	}
	public void addDebris(AbstractParticlesEffect... debrisEffect) {
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
	public void updateAllStream(){
		debris.stream().parallel().forEach(AbstractParticlesEffect::move);
		particles.stream().parallel().forEach(AbstractParticlesEffect::move);
		debris.forEach(AbstractParticlesEffect::updateUI);
		particles.forEach(AbstractParticlesEffect::updateUI);
		debris.forEach(AbstractParticlesEffect::collide);
		particles.forEach(AbstractParticlesEffect::collide);
		removeDeadDebris();
		removeDeadParticles();
	}
	public synchronized void updateAll() {
		Iterator<AbstractParticlesEffect> debrisList = debris.iterator();
		Iterator<AbstractParticlesEffect> particleList = particles.iterator();
		while(debrisList.hasNext()) {
			AbstractParticlesEffect tempDebris = debrisList.next();
			tempDebris.move();
			tempDebris.updateUI();
			tempDebris.draw();
			tempDebris.collide();
			if (!tempDebris.isAlive()) {
				tempDebris.remove();
				debrisList.remove();
				continue;
			}
		}
		while(particleList.hasNext()) {
			AbstractParticlesEffect tempParticle = particleList.next();
			tempParticle.move();
			tempParticle.updateUI();
			tempParticle.draw();
			tempParticle.collide();
			if (!tempParticle.isAlive()) {
				tempParticle.remove();
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

				particle.remove();
				particleList.remove();

			}

		}

	}
	private void removeDeadDebris() {

		Iterator<AbstractParticlesEffect> debrisList = debris.iterator();
		while(debrisList.hasNext()) {

			AbstractParticlesEffect debris = debrisList.next();

			if (!debris.isAlive()) {

				debris.remove();
				debrisList.remove();

			}
		}
	}
	public synchronized void updateAllFor() {
		for(int i = 0; i<debris.size(); i++){
			AbstractParticlesEffect tempDebris = debris.get(i);
			tempDebris.updateUI();
			tempDebris.draw();
			tempDebris.collide();
			if (!tempDebris.isAlive()) {
				tempDebris.remove();
				debris.remove(tempDebris);
			}
		}
		for(int i = 0; i<particles.size(); i++){
			AbstractParticlesEffect tempParticle = particles.get(i);
			tempParticle.updateUI();
			tempParticle.draw();
			tempParticle.collide();
			if (!tempParticle.isAlive()) {
				tempParticle.remove();
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
	public void updateDebris() {
		for (int i = 0; i < debris.size(); i++) {
			tempDebris = debris.get(i);
			tempDebris.updateUI();
			tempDebris.draw();
			tempDebris.move();
			tempDebris.collide();
			if (!tempDebris.isAlive()) {
				tempDebris.remove();
				debris.remove(i);
			}
		}
	}

	public void updateParticles() {
		for (int i = 0; i < particles.size(); i++) {
			tempParticle = particles.get(i);
			tempParticle.updateUI();
			tempParticle.draw();
			tempParticle.move();
			tempParticle.collide();
			if (!tempParticle.isAlive()) {
				tempParticle.remove();
				particles.remove(i);
			}
		}
	}

	public List<AbstractParticlesEffect> getDebrisList() {
		return debris;
	}

	public List<AbstractParticlesEffect> getParticleList() {
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
