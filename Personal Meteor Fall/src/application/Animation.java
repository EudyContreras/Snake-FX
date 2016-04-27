package application;


import java.util.ArrayList;

import javafx.scene.image.Image;

public class Animation {
	
	private ArrayList<Object> scenes = new ArrayList<Object>(); 
	private int sceneIndex;
	private long movieTime;
	private long totalTime;
	
	public Animation(){
		totalTime = 0;
		start();
	}
	public synchronized void addScene(Image image, long time){
		totalTime+=time;
		scenes.add(new OneScene(image, totalTime));
	}
	public synchronized void start(){
		movieTime = 0;
		sceneIndex = 0;
		
	}
	public synchronized void update(long timePassed){
		if(scenes.size()> 1){
			movieTime += timePassed;
			if(movieTime >= totalTime){
				movieTime = 0;
				sceneIndex = 0;
			}
			while(movieTime > getScene(sceneIndex).endTime){
				sceneIndex++;
			}
		}
	}
	public synchronized Image getImage(){
		if(scenes.size() == 0){
			return null;
		}
		else{
			return getScene(sceneIndex).pic;
		}
	}
	private OneScene getScene(int x){
		return (OneScene)scenes.get(x);
	}
	
	private class OneScene{
		Image pic;
		long endTime;
		
		public OneScene(Image pic, long endTime){
			this.pic = pic;
			this.endTime = endTime;
		}
	}
}
