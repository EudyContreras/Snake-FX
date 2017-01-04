package com.EudyContreras.Snake.Utilities;

import java.util.ArrayList;
import java.util.List;

import com.EudyContreras.Snake.ThreadManagers.ThreadManager;

import javafx.application.Platform;
import javafx.scene.shape.Rectangle;

public class ScaleAnimator implements Animator{

	public static final int RESIZE_WIDTH = 0;
	public static final int RESIZE_HEIGHT = 1;
	public static final int RESIZE_WIDTH_HEIGHT = 2;
	private List<AnimatorListener> listeners;
	private Interpolator interpolator;
	private Runnable endScript;
	private Period playTime;
	private Rectangle node;
    private long startTime = -1;
	private boolean keepCenter = false;
	private boolean running = true;
	private boolean revert = false;
	private boolean repeat = false;
	private double fromX;
	private double fromY;
	private double toX;
	private double toY;
  	private double scaleX;
    private double scaleY;
	private int scaleAxis;
	private int delay = 0;


	public ScaleAnimator(int axis){
		scaleAxis = axis;
		listeners = new ArrayList<>();
	}

    private void applyTransformation(float delta) {
    	double progress;
        switch (scaleAxis){
            case RESIZE_WIDTH:
                progress = interpolator.interpolate(delta);
                scaleX = fromX + ((int) Math.round((toX - fromX) * progress));
                Platform.runLater(()->{
                    node.setScaleX(scaleX);
				});
                break;
            case RESIZE_HEIGHT:
            	progress = interpolator.interpolate(delta);
                scaleY = fromY + ((int) Math.round((toY - fromY) * progress));
                Platform.runLater(()->{
                    node.setScaleY(scaleY);
				});
                break;
            case RESIZE_WIDTH_HEIGHT:
                progress = interpolator.interpolate(delta);
                scaleX = fromX + ((int) Math.round((toX - fromX) * progress));
                scaleY = fromY + ((int) Math.round((toY - fromY) * progress));
                Platform.runLater(()->{
                    node.setScaleX(scaleX);
                    node.setScaleY(scaleY);
				});
                break;
        }
    }

	public void setNode(Rectangle node){
		this.node = node;
	}

	public void setResizeAxis(int axis) {
		this.scaleAxis = axis;
	}

	public double getStartWidth() {
		return fromX;
	}

	public void setFromX(double startWidth) {
		this.fromX = startWidth;
		this.scaleX = startWidth;
	}

	public double getStartHeight() {
		return fromY;
	}

	public void setFromY(double startHeight) {
		this.fromY = startHeight;
		this.scaleY = startHeight;
	}

	public double getEndWidth() {
		return toX;
	}

	public void setToX(double endWidth) {
		this.toX = endWidth;
	}

	public double getEndHeight() {
		return toY;
	}

	public void setToY(double endHeight) {
		this.toY = endHeight;
	}

	public void setDuration(Period duration){
		this.playTime = duration;
	}

	@Override
	public long getDuration() {
		return playTime!=null ? playTime.getDuration() : 0;
	}

	public void setOnFinished(Runnable script){
		this.endScript = script;
	}

	public void setInterpolator(Interpolator interpolator){
		this.interpolator = interpolator;
	}

	public ScaleAnimator setRepeat(boolean state){
		this.repeat = state;
		return this;
	}

	public ScaleAnimator setRevert(boolean state){
		this.revert = state;
		return this;
	}

	public ScaleAnimator keepCenter(boolean state){
		this.keepCenter = state;
		return this;
	}

	public void setDelay(int delay){
		this.delay = delay;
	}

	public void sleep(int time){
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void addListener(AnimatorListener listener){
		listeners.add(listener);
	}

	public void removeListener(AnimatorListener listener){
		listeners.remove(listener);
	}

	private void notifyStart(){
		if(!listeners.isEmpty()){
			listeners.stream().filter(listener-> listener!=null).forEach(listener-> listener.OnStart(this));
		}
	}

	private void notifyEnd(){
		if(!listeners.isEmpty()){
			listeners.stream().filter(listener-> listener!=null).forEach(listener-> listener.OnEnd(this));
		}
	}

	private void notifyRepeat(){
		if(!listeners.isEmpty()){
			listeners.stream().filter(listener-> listener!=null).forEach(listener-> listener.OnRepeat(this));
		}
	}
	@Override
	public void play(){
		ThreadManager.performeScript(() ->{

			sleep(delay);
			notifyStart();

			while(running){
				  if (startTime < 0) {
	                  startTime = System.currentTimeMillis();
	              }
	              long now = System.currentTimeMillis();
	              long duration = now - startTime;
	              float deltaTime = (float) duration / (float) playTime.getDuration();

	              applyTransformation(deltaTime);

	              if (duration >= playTime.getDuration()) {
	                  deltaTime = 1;
	                  running = false;
		              notifyEnd();
	              }

	      		sleep(1);
			}
			System.out.println("Width :" +scaleX);
			if(endScript!=null){
				Platform.runLater(endScript);

			}
		});
	}

	@Override
	public void stop(){
		running = false;
	}


}
