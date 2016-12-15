package com.EudyContreras.Snake.Managers;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.EudyContreras.Snake.ThreadExecutors.TaskWrapper;
import com.EudyContreras.Snake.ThreadExecutors.WorkerThread;
import com.EudyContreras.Snake.ThreadExecutors.WorkerThread.TaskType;
import com.EudyContreras.Snake.ThreadExecutors.WorkerThread.TimeUnit;

/**
 * Thread managing class which allows the managing of threads
 * create for different task. Each time a task is submitted to this
 * class a thread is created for the handling of said task.
 * Created by Eudy Contreras on 10/14/2016.
 */

public class ThreadManager {

    private ConcurrentHashMap<String, WorkerThread> threads;

    /**
     * Constructor which initializes this manager
     * @param updateFrequency
     * @param startTime
     */
    public ThreadManager(){
        this.threads = new ConcurrentHashMap<String, WorkerThread>();
    }

    public synchronized ThreadManager sumbitThread(String name, TaskWrapper... tasks){
    	return submitThread(name, TaskType.CONTINUOUS,tasks);
	}

    public synchronized ThreadManager submitThread(String name, TaskType taskType, TaskWrapper... tasks){
    	return submitThread(name,taskType,0,0,tasks);
    }

    public synchronized ThreadManager submitThread(String name, TaskType taskType, int updateFrequency, int startDelay, TaskWrapper... tasks){
    	return submitThread(name,taskType,TimeUnit.MILLISECONDS, updateFrequency,0,tasks);
    }

    /**
     * Method which submits a task to the task collection.
     * The method creates a thread to handle said task.
     * @param taskType
     * @param tasks
     * @return
     */
    public synchronized ThreadManager submitThread(String name, TaskType taskType, TimeUnit timeUnit, int updateFrequency, int startDelay, TaskWrapper... tasks){
    	threads.put(name, new WorkerThread(this, name, taskType, timeUnit, updateFrequency, startDelay, tasks));
    	return this;
	}

    /**
     * Method which removes specified thread from the collection
     * @param thread
     */
    public synchronized void removeThread(String name){
    	threads.get(name).stop();
    	threads.remove(name);
    }
    /**
     * Method which starts of the threads created
     * by this thread manager.
     */
    public synchronized void startAll(){
    	for(Entry<String, WorkerThread> entry: threads.entrySet()){
			entry.getValue().start();
		}
    }
    /**
     * Method which starts a thread at a specified index
     * @param index
     */
    public synchronized void start(String name){
    	if(threads.size()<= 0)
    		return;

    	threads.get(name).start();
    }

    /**
     * Method which starts and joins all threads
     * @param index
     */
    public synchronized void startAll(boolean join){
    	for(Entry<String, WorkerThread> entry: threads.entrySet()){
			entry.getValue().start();
		}
		if (join) {
			for(Entry<String, WorkerThread> entry: threads.entrySet()){
				entry.getValue().join();
			}
		}
    }

    /**
     * Method which starts and joins a thread at a specified index
     * @param index
     */
    public synchronized void start(String name, boolean join){
     	if(threads.size()<= 0)
    		return;

     	threads.get(name).start();
    	if(join)
    		threads.get(name).join();
    }

    /**
     * Method which stops a thread at a specified index
     * @param index
     */
    public synchronized void stop(String name){
    	if(threads.size()<= 0)
    		return;

    	threads.get(name).stop();
    }

    /**
     * Method which stops all threads
     * @param index
     */
    public synchronized void stopAll(){
    	for(Entry<String, WorkerThread> entry: threads.entrySet()){
			entry.getValue().stop();
		}
    }

    /**
     * Method which resumes a thread at a specified index
     * @param index
     */
    public synchronized void resume(String name){
    	if(threads.size()<= 0)
    		return;

    	threads.get(name).resume();
    }

    /**
     * Method which resumes all threads
     */
    public synchronized void resumeAll(){
    	for(Entry<String, WorkerThread> entry: threads.entrySet()){
			entry.getValue().resume();
		}
    }

    /**
     * Method which pauses a thread at a specified index
     * @param index
     */
    public synchronized void pause(String name){
    	if(threads.size()<= 0)
    		return;

    	threads.get(name).pause();
    }

    /**
     * Method which pauses all threads
     * @param index
     */
    public synchronized void pauseAll(){
     	for(Entry<String, WorkerThread> entry: threads.entrySet()){
			entry.getValue().pause();
		}
    }

    /**
     * Method which makes a thread wait until notified
     * @param name
     */
    public synchronized void hold(String name){
    	if(threads.size()<= 0)
    		return;

    	threads.get(name).pause();
    }

    /**
     * Method which makes all threads wait until notified
     * @param index
     */
    public synchronized void holdAll(){
     	for(Entry<String, WorkerThread> entry: threads.entrySet()){
			entry.getValue().hold();
		}
    }

    /**
     * Method which pauses a thread at a specified index
     * @param name
     */
    public synchronized void proceed(String name){
    	if(threads.size()<= 0)
    		return;

    	threads.get(name).proceed();
    }

    /**
     * Method which pauses all threads
     * @param index
     */
    public synchronized void proceedAll(){
     	for(Entry<String, WorkerThread> entry: threads.entrySet()){
			entry.getValue().proceed();
		}
    }

    /**
     * Method which adds task to the specified thread
     * @param index
     */
	public synchronized ThreadManager addTask(String thread, TaskWrapper task) {
		threads.get(thread).addTask(task);
		return this;
	}


    /**
     * Method which uses a task id to remove a thread from the collection
     * @param taskID
     */
    public synchronized void removeTask(String threadName, String taskID){
    	if(threads.size()<= 0)
    		return;

		threads.get(threadName).removeTask(taskID);
    }

	public synchronized void removeTask(WorkerThread workerThread) {
		threads.remove(workerThread);
	}

    /**
     * Method which checks if a thread at a specified index is running
     * @param index
     */
	public synchronized boolean isRunning(String name) {
		return threads.get(name).isRunning();
	}

	  /**
     * Method which gets the update frequency of a thread at a specified index is running
     * @param name
     */
	public synchronized int getUpdateFrequency(String name) {
		if(threads.size()<= 0)
    		return 0;

		return threads.get(name).getUpdateFrequency();
	}

	  /**
     * Method which sets the update frequency of a thread at a specified index is running
     * @param index
     */
	public synchronized void setUpdateFrequency(int updateFrequency, int String) {
		if(threads.size()<= 0)
    		return;

		threads.get(String).setUpdateFrequency(updateFrequency);
	}

	  /**
     * Method which sets the update frequency of all threads
     * @param index
     */
	public synchronized void setUpdateFrequencyForAll(int updateFrequency) {
		if(threads.size()<= 0)
    		return;

	  	for(Entry<String, WorkerThread> entry: threads.entrySet()){
			entry.getValue().setUpdateFrequency(updateFrequency);
		}
	}

	/**
	 * Method which removes all the threads
	 */
    public synchronized void clearThreads(){
    	if(threads.size()<= 0)
    		return;

    	stopAll();
    	threads.clear();
    }

    /**
     * Method which returns the current thread count
     * @return
     */
    public synchronized int threadCount(){
    	return threads.size();
    }


}
