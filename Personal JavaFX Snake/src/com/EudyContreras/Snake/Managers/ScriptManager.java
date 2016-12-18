package com.EudyContreras.Snake.Managers;

import java.util.LinkedList;

import com.EudyContreras.Snake.ThreadExecutors.ValueTask;

/**
 * Thread managing class which allows the managing of threads
 * create for different task. Each time a task is submitted to this
 * class a thread is created for the handling of said task.
 * Created by Eudy Contreras on 10/14/2016.
 */

public class ScriptManager<VALUE> {

	private LinkedList<ValueTask<VALUE>> valueTask;

    /**
     * Constructor which initializes this manager
     * @param updateFrequency
     * @param startTime
     */
    public ScriptManager(){
        this.valueTask = new LinkedList<ValueTask<VALUE>>();
    }

    public synchronized VALUE computeValue(String name, ValueTask<VALUE> task){
    	valueTask.add(task);

    	Worker worker = new Worker();

    	Thread thread = new Thread(worker);

    	thread.setName(name);
    	thread.setDaemon(true);
    	thread.start();

    	try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

    	return worker.getValue();
    }

    private class Worker implements Runnable {

	     private volatile VALUE value;

	     @Override
	     public void run() {
	        value = computeValue();
	     }

	     public VALUE getValue() {
	    	 return value;
	     }
	 }

	private VALUE computeValue(){
		return valueTask.poll().computeValue();
	}
}
