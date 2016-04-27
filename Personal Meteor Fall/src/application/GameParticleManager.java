package application;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;


public class GameParticleManager {
	
	
	public List<ParticleEffect> particle ;
	public Game game;
	private ParticleEmitter emitter;
	private ParticleEffect tempParticle;
	
	public GameParticleManager(Game gameJavaFX){
		this.game = gameJavaFX;
		initialize();
	}
	public void initialize() {
		particle = new LinkedList<ParticleEffect> ();
		emitter = new FireEmitter(game);
	}	
	public void addObject(ParticleEffect particle){
		this.particle.add(particle);
	}  
    public void addObject(ParticleEffect... pe) {
        if (pe.length > 1) {
        	particle.addAll(Arrays.asList(pe));
        } else {
        	particle.add(pe[0]);
        }
    } 
	public void update(GraphicsContext gc){
		if(!Settings.DEBUG_MODE){
		gc.setGlobalAlpha(1.0);
		gc.setGlobalBlendMode(BlendMode.SRC_OVER);
		gc.setFill(Color.BLACK);
		gc.fillRect(0,0,Settings.WIDTH, Settings.HEIGHT);	
		}
		particle.addAll(emitter.emit(300, 300));
		for(Iterator<ParticleEffect> p = particle.iterator(); p.hasNext();){
			ParticleEffect pt = p.next();
			pt.updateUI();
			pt.draw(gc);
			if(!pt.isAlive()){
				p.remove();
				continue;
			}
		}
	}
	public void updateParticles(GraphicsContext gc){	
		if(!Settings.DEBUG_MODE){
		gc.setGlobalAlpha(1.0);
		gc.setGlobalBlendMode(BlendMode.SRC_OVER);
		gc.setFill(Color.BLACK);
		gc.fillRect(0,0,Settings.WIDTH, Settings.HEIGHT);	
		}
		particle.addAll(emitter.emit(300, 300));
		for(int i = 0; i<particle.size(); i++){
			tempParticle = particle.get(i);
			tempParticle.updateUI();
			tempParticle.draw(gc);
			if(!tempParticle.isAlive()){
				particle.remove(i);
			}
		}
	}
	public void updateDebris(GraphicsContext gc){	
		for(int i = 0; i<particle.size(); i++){
			tempParticle = particle.get(i);
			tempParticle.updateUI();
			tempParticle.draw(gc);
			if(!tempParticle.isAlive()){
				particle.remove(i);
			}
		}
	}
	public void updateNow(long now){	
		for(int i = 0; i<particle.size(); i++){
			tempParticle = particle.get(i);
			tempParticle.updateNow(now);
		}
	}
	public void clearAll(){
		this.particle.clear();
	}
}

