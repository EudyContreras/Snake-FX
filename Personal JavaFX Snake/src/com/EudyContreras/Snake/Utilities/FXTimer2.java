package com.EudyContreras.Snake.Utilities;


import java.util.function.Consumer;
import java.util.function.Predicate;

import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * Provides factory methods for timers that are manipulated from and execute
 * their action on the JavaFX application thread.
 */
public class FXTimer2<T> implements Timer {

    /**
     * Prepares a (stopped) timer that lasts for {@code delay} and whose action runs when timer <em>ends</em>.
     */
    private static Timer create(Duration delay, TimerAction action) {
        return new FXTimer2<>(delay, delay, action, 1);
    }

    /**
     * Equivalent to {@code create(delay, action).restart()}.
     */
    public static Timer runLater(Duration delay, TimerAction action) {
        Timer timer = create(delay, action);
        timer.restart();
        return timer;
    }

    /**
     * Prepares a (stopped) timer that lasts for {@code interval} and that executes the given action periodically
     * when the timer <em>ends</em>.
     */
    private static Timer createPeriodic(Duration interval, TimerAction action) {
        return new FXTimer2<>(interval, interval, action, Animation.INDEFINITE);
    }

    /**
     * Prepares a (stopped) timer that lasts for {@code interval} and that executes the given action periodically
     * when the timer <em>ends</em>.
     */
    private static Timer createPeriodic(Duration interval,Repeater repeats, TimerAction action) {
        return new FXTimer2<>(interval, interval,repeats, action, Animation.INDEFINITE);
    }

    /**
     * Prepares a (stopped) timer that lasts for {@code interval} and that executes the given action periodically
     * when the timer <em>ends</em>.
     */
    private static<T> Timer createPeriodic(Duration interval, TimerAction action, T type, Predicate<T> predicate, Consumer<T> consumer) {
        return new FXTimer2<>(interval, interval, null, action, Animation.INDEFINITE, type, predicate, consumer);
    }

    /**
     * Prepares a (stopped) timer that lasts for {@code interval} and that executes the given action periodically
     * when the timer <em>ends</em>.
     */
    private static<T> Timer createPeriodic(Duration interval, Repeater repeats, TimerAction action, T type, Predicate<T> predicate, Consumer<T> consumer) {
        return new FXTimer2<>(interval, interval, repeats, action, Animation.INDEFINITE, type, predicate, consumer);
    }


    /**
     * Equivalent to {@code createPeriodic(interval, action).restart()}.
     */
    public static Timer runPeriodically(Duration interval, TimerAction action) {
        Timer timer = createPeriodic(interval, action);
        timer.restart();
        return timer;
    }


    /**
     * Equivalent to {@code createPeriodic(interval, action).restart()}.
     */
    public static Timer runPeriodically(Duration interval, Repeater repeats, TimerAction action) {
        Timer timer = createPeriodic(interval,repeats,action);
        timer.restart();
        return timer;
    }

    /**
     * Equivalent to {@code createPeriodic(interval, action).restart()}.
     */
    public static<T> Timer runAsLong(Duration interval, Repeater repeats, TimerAction action, T type, Predicate<T> condition, Consumer<T> consumer) {
        Timer timer = createPeriodic(interval, repeats, action, type, condition, consumer);
        timer.restart();
        return timer;
    }


    /**
     * Equivalent to {@code createPeriodic(interval, action).restart()}.
     */
    public static<T> Timer runAsLong(Duration interval, TimerAction action, T type, Predicate<T> condition, Consumer<T> consumer) {
        Timer timer = createPeriodic(interval, action, type, condition, consumer);
        timer.restart();
        return timer;
    }

    /**
     * Prepares a (stopped) timer that lasts for {@code interval} and that executes the given action periodically
     * when the timer <em>starts</em>.
     */
    private static Timer createPeriodic_zero(Duration interval, TimerAction action) {
        return new FXTimer2<>(Duration.ZERO, interval, action, Animation.INDEFINITE);
    }

    /**
     * Equivalent to {@code createPeriodic0(interval, action).restart()}.
     */
    public static Timer runPeriodically_zero(Duration interval, TimerAction action) {
        Timer timer = createPeriodic_zero(interval, action);
        timer.restart();
        return timer;
    }


    private final Consumer<T> consumer;
    private final Predicate<T> predicate;
    private final Duration actionTime;
    private final Timeline timeline;
    private final Capsule capsule;
    private final TimerAction action;
    private final Repeater repeater;
    private int iterations;
    private final T type;
    private long seq = 0;

    private FXTimer2(Duration actionTime, Duration period, TimerAction action, int cycles) {
        this(actionTime,period,null,action,cycles,null,null,null);
    }

    private FXTimer2(Duration actionTime, Duration period, Repeater repeats, TimerAction action, int cycles) {
        this(actionTime,period,repeats,action,cycles,null,null,null);
    }


    private FXTimer2(Duration actionTime, Duration period, Repeater repeats, TimerAction action, int cycles, T type, Predicate<T> predicate, Consumer<T> consumer) {
    	 this.actionTime = actionTime;
    	 this.predicate = predicate;
    	 this.repeater = repeats;
    	 this.consumer = consumer;
         this.action = action;
    	 this.type = type;

    	 capsule = new Capsule();
    	 timeline = new Timeline();

         timeline.getKeyFrames().add(new KeyFrame(this.actionTime));

         if (period != actionTime) {
             timeline.getKeyFrames().add(new KeyFrame(Duration.millis(period.toMillis())));
         }

         timeline.setCycleCount(cycles);

	}

	@Override
    public void restart() {
        stop();
        long expected = seq;
        timeline.getKeyFrames().set(0, new KeyFrame(actionTime, ae -> {
			if (repeater != null) {

				FXRepeater.repeat(repeater.getRepeats(), (repetitions)->{
					if (isRunning() && !capsule.stopped()) {
						iterations++;
						if (seq == expected) {
							action.run(repetitions,capsule);
						}
						if (predicate != null) {
							if (predicate.test(type)) {
							} else {
								stop();
								if (consumer != null) {
									consumer.accept(type);
								}
							}
						}
					}else{
						stop();
					}
				});
			} else{
				if (isRunning() && !capsule.stopped()) {
					iterations++;
					if (seq == expected) {
						action.run(0,capsule);
					}
					if (predicate != null) {
						if (predicate.test(type)) {
						} else {
							stop();
							if (consumer != null) {
								consumer.accept(type);
							}
						}
					}
				}else{
					stop();
				}
			}
        }));

        timeline.play();
    }

    @Override
    public void stop() {
		timeline.stop();
		++seq;
	}

    @Override
    public boolean isRunning(){
    	return timeline.getStatus() == Status.RUNNING;
    }

    public interface TimerAction{
    	void run(int actions, Capsule capsule);
    }

    public static class Capsule{

    	private boolean stop;

    	public void stop(){
    		
    		this.stop = true;
    	}
    	public boolean stopped(){
    		return stop;
    	}
    }
}
