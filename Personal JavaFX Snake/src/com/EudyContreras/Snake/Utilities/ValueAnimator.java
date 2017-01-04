package com.EudyContreras.Snake.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.EudyContreras.Snake.ThreadManagers.ThreadManager;

import javafx.application.Platform;

public class ValueAnimator implements Animator{

	private Period playTime;
	private Runnable endScript;
	private ValueWrapper update;
	private Interpolator interpolator;
	private List<AnimatorListener> listeners;
	private boolean running = true;
	private boolean revert = false;
	private boolean repeat = false;
	private double valueInterpolator;
	private double fromValue;
	private double toValue;
	private double value;
	private long startTime = -1;
	private int delay = 0;


	public ValueAnimator(){
		this.interpolator = Interpolators.getLinearInstance();
		this.listeners = new ArrayList<>();
	}

	public ValueAnimator(Period duration, double from, double to, ValueWrapper update){
		this.update = update;
		this.playTime = duration;
		this.fromValue = from;
		this.toValue = to;
		this.value = from;
		this.listeners = new ArrayList<>();
	}

	public ValueAnimator(Period duration, double from, double to){
		this.playTime = duration;
		this.fromValue = from;
		this.toValue = to;
		this.value = from;
		this.listeners = new ArrayList<>();
	}

	public ValueAnimator onUpdate(ValueWrapper update){
		this.update = update;
		return this;
	}

	public ValueAnimator addUpdateAlt(Consumer<ValueWrapper> consumer){
		consumer.accept(update);
		return this;
	}

	private void updateValue(float deltaTime){
		value = fromValue + ((toValue - fromValue) * interpolator.interpolate(deltaTime));

		Platform.runLater(() -> {
			update.onUpdate(value);
		});
	}

	private void updateValue(){
		Platform.runLater(()->{
			update.onUpdate(value);
			value += valueInterpolator;
		});
	}

	private void calculateValue(){
		double margin = (toValue-fromValue);
		valueInterpolator = margin/playTime.getDuration();
		value = fromValue;
	}

	private void calculateValue(double from, double to){
		double margin = (to-from);
		valueInterpolator = margin/playTime.getDuration();
		value = fromValue;
	}

	public void setFrom(double from){
		this.fromValue = from;
		this.value = from;
	}

	public void setTo(double to){
		this.toValue = to;
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

	public ValueAnimator setRepeat(boolean state){
		this.repeat = state;
		return this;
	}

	public ValueAnimator setRevert(boolean state){
		this.revert = state;
		return this;
	}

	public void setInterpolator(Interpolator interpolator){
		this.interpolator = interpolator;
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
	public void play() {
		calculateValue();
		ThreadManager.performeScript(() -> {

			sleep(delay);
			notifyStart();

			long lastTime = System.nanoTime();
			double updateCount = 1000.0;
			double nanoSeconds = 1000000000 / updateCount;
			double deltaTime = 0;
			long timer = System.currentTimeMillis();

			while (running) {
				long now = System.nanoTime();
				deltaTime += (now - lastTime) / nanoSeconds;
				lastTime = now;
				if (deltaTime >= 1) {
					updateValue();
					deltaTime--;
				}
				if (System.currentTimeMillis() - timer >= playTime.getDuration()) {
					notifyEnd();
					break;
				}
			}

			if (revert ^ repeat) {
				if (repeat) {
					calculateValue(fromValue, toValue);
					play();
				} else {
					calculateValue(value, fromValue);
					revert = false;
					play();
				}
			} else {
				if (endScript != null) {
					Platform.runLater(endScript);
				}
			}

		});
	}

	public void playL(){
		startTime = -1;
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

	              if (duration >= playTime.getDuration() || value>=toValue) {
	                  deltaTime = 1;
	                  notifyEnd();
	                  break;
	              }

	              updateValue(deltaTime);

	      		sleep(1);
			}
			System.out.println("Value :" +value);
			if(endScript!=null){
				Platform.runLater(endScript);
			}
		});
	}

	public void stop(){
		running = false;
	}



}
