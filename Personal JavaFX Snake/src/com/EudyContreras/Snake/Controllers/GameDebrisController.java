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

	public GameDebrisController(GameManager game) {
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
		debris.stream().forEach(debri->{
			if (debri != null) {
				debri.move();
				debri.updateUI();
				debri.draw();
				debri.collide();
				if (!debri.isAlive()) {
					removeDebris(debri);
				}
			}
		});

		particles.stream().forEach(particle->{
			if (particle != null) {
				particle.move();
				particle.updateUI();
				particle.draw();
				particle.collide();
				if (!particle.isAlive()) {
					removeParticle(particle);
				}
			}
		});
	}

	public void updateAll() {
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

	private void removeDebris(AbstractParticlesEffect debri){
		debri.remove();
		debris.remove(debri);
	}

	private void removeParticle(AbstractParticlesEffect particle){
		particle.remove();
		particles.remove(particle);
	}

	public void clearDebris() {
		this.debris.clear();
	}

	public void clearParticles() {
		this.particles.clear();
	}

	public void clearAll() {
		this.debris.clear();
		this.particles.clear();
	}

}
