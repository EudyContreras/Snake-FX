package com.SnakeGame.Particles;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

import com.SnakeGame.FrameWork.SnakeGame;

import javafx.scene.canvas.GraphicsContext;

public class GameDebrisManager {

	private LinkedList<AbstractDebrisEffect> debris;
	private LinkedList<AbstractDebrisEffect> particles;
	private AbstractDebrisEffect tempDebris;
	public SnakeGame game;

	public GameDebrisManager(SnakeGame game) {
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

	public void addObject(AbstractDebrisEffect debris) {
		this.debris.add(debris);
	}

	public void addObjectA(AbstractDebrisEffect... db) {
		if (db.length > 1) {
			debris.addAll(Arrays.asList(db));
		} else {
			debris.add(db[0]);
		}
	}

	public void update(GraphicsContext gc) {
		for (Iterator<AbstractDebrisEffect> debrisList = debris.iterator(); debrisList.hasNext();) {
			AbstractDebrisEffect tempDebris = debrisList.next();
			tempDebris.update();
			tempDebris.draw(gc);
			tempDebris.move();
			tempDebris.collide();
			if (!tempDebris.isAlive()) {
				game.getFirstLayer().getChildren().remove(tempDebris.view);
				game.getSecondLayer().getChildren().remove(tempDebris.view);
				game.getFirstLayer().getChildren().remove(tempDebris.shape);
				game.getSecondLayer().getChildren().remove(tempDebris.shape);
				debrisList.remove();
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
				game.getFirstLayer().getChildren().remove(tempDebris.view);
				game.getSecondLayer().getChildren().remove(tempDebris.view);
				game.getFirstLayer().getChildren().remove(tempDebris.shape);
				game.getSecondLayer().getChildren().remove(tempDebris.shape);
				debris.remove(i);
			}
		}
	}

	public void updateParticles(GraphicsContext gc) {
		for (int i = 0; i < particles.size(); i++) {
			tempDebris = particles.get(i);
			tempDebris.update();
			tempDebris.draw(gc);
			tempDebris.move();
			tempDebris.collide();
			if (!tempDebris.isAlive()) {
				game.getSixthLayer().getChildren().remove(tempDebris.shape);
				game.getSixthLayer().getChildren().remove(tempDebris.view);
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
				game.getFirstLayer().getChildren().remove(tempDebris.shape);
				debris.remove(i);
			}
			if (tempDebris.shape == null) {
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

	public void clearAll() {
		this.debris.clear();
		this.particles.clear();
	}

}
