package application;

import javafx.animation.AnimationTimer;
import javafx.animation.Timeline;

public class GameLoops extends AnimationTimer {
    
    public Timeline animation;
    public Player player;
    
    GameLoops(Player player) {
    
    	this.player = player;
    }
    
    public void handle(long now) {
        
        animation = new Timeline(now);
        player.move();
        player.updateUI();
    }
    
    public void start() {
        super.start();
    }
    
    public void stop() {
        super.stop();
    }
}