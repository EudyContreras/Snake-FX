package com.EudyContreras.Snake.ThreadTools;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import com.EudyContreras.Snake.ThreadManagers.ThreadManager;



/**
 * Class which represents a worker. This class
 * creates a thread and assigns a valueTask to the thread.
 * The functions of this class are controlled by a Thread Manager.
 * Created by Eudy Contreras on 10/14/2016.
 */
public class WorkerThread{

	private String name;

	private ThreadManager manager;
	private WorkerThreadHelper workerHelper;

	private LinkedHashMap<String,TaskWrapper> computeTask;

	private volatile boolean onHold = false;
	private volatile boolean parallel = false;
	private volatile boolean running = false;
	private volatile boolean active = false;

	private TimeUnit timeUnit = TimeUnit.MILLISECONDS;
	private TaskType taskType  = TaskType.CONTINUOUS;

	private volatile int taskCounter = 0;
	private volatile int updateFrequency = 0;
	private volatile int startDelay = 0;

	public WorkerThread(ThreadManager manager, String name, int updateFrequency,TaskWrapper... update) {
		this(manager, name, TaskType.CONTINUOUS, TimeUnit.MILLISECONDS,updateFrequency,0,update);
	}

	public WorkerThread(ThreadManager manager, String name, TaskType taskType, int updateFrequency,TaskWrapper... update) {
		this(manager, name, taskType, TimeUnit.MILLISECONDS,updateFrequency,0,update);
	}

	public WorkerThread(ThreadManager manager, String name, TaskType taskType, int updateFrequency, int startDelay, TaskWrapper... update) {
		this(manager, name, taskType, TimeUnit.MILLISECONDS,updateFrequency,startDelay,update);
	}

	/**
	 * Constructor which creates a worker thread and sets all the initial values.
	 * @param manager The manager of this worker
	 * @param taskType The type of valueTask: Continuous or Single instance
	 * @param timeUnit The time unit which the valueTask uses
	 * @param updateFrequency The frequency in which the updates will be made
	 * @param startDelay The delay before the first updates is performed
	 * @param updates The valueTask to be performed by this thread
	 */
	public WorkerThread(ThreadManager manager, String name, TaskType taskType, TimeUnit timeUnit, int updateFrequency, int startDelay, TaskWrapper... update) {
		this.computeTask = addTasks(update);
		this.name = name;
		this.timeUnit = timeUnit;
		this.taskType = taskType;
		this.manager = manager;
		this.updateFrequency = getSleepTime(timeUnit,updateFrequency);
		this.startDelay = getSleepTime(timeUnit,startDelay);;
	}

	private LinkedHashMap<String,TaskWrapper> addTasks(TaskWrapper...taskWrappers){
		computeTask = new LinkedHashMap<>();
		for(TaskWrapper wrapper: taskWrappers){
			taskCounter++;
			if(wrapper instanceof TaskUpdate){
				TaskUpdate update = (TaskUpdate)wrapper;
				if(update.getTaskID()!=null){
					computeTask.put(update.getTaskID(), update);
				}else{
					computeTask.put(String.valueOf(taskCounter), update);
				}
			}else{
				computeTask.put(String.valueOf(taskCounter), wrapper);
			}
		}
		return computeTask;
	}
	/**
	 * Method which creates and start the thread
	 * used by this class
	 */
	public void start() {
		if (workerHelper == null) {
			workerHelper = null;
			workerHelper = new WorkerThreadHelper();
			workerHelper.setDaemon(true);
			workerHelper.setName(name);
			workerHelper.start();
			running = true;
			active = true;
		}
	}
	/**
	 * Method which stops the thread of this class
	 */
	public void stop() {
		if (workerHelper != null) {
			active = false;
			running = false;
			workerHelper.interrupt();
			workerHelper = null;
		}
	}

	/**
	 * Method which joins this this thread
	 */
	public void join() {
		if (workerHelper != null) {
			try {
				workerHelper.join();
			} catch (InterruptedException e) {

			}
		}
	}

	/**
	 * Method which joins this this thread
	 */
	public void setDaemon(boolean state) {
		if(workerHelper == null)
			return;

		workerHelper.setDaemon(state);
	}
	/**
	 * Method which resumes the thread
	 */
	public void resume() {
		running = true;
	}

	/**
	 * Method which pauses the thread
	 */
	public void pause() {
		running = false;
	}

	/**
	 * Method which returns whether the thread is running or not
	 * @return
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * Method which returns whether the thread is alive or not
	 * @return
	 */
	public boolean isAlive(){
		return active;
	}

	/**
	 * Method which makes the thread wait if not
	 * already waiting.
	 */
	public void hold(){
		if(workerHelper!=null){
			if(!isOnHold()){
				try {
					workerHelper.wait();
					onHold = true;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Method which allows this thread to
	 * proceed if it is waiting
	 */
	public void proceed(){
		if(workerHelper!=null){
			if(isOnHold()){
				workerHelper.notify();
				onHold = false;
			}
		}
	}

	/**
	 * Returns whether this thread is on hold or not
	 * @return
	 */
	public boolean isOnHold(){
		return onHold;
	}
	/**
	 * Method which returns the updates frequency of this thread
	 * @return
	 */
	public int getUpdateFrequency() {
		return getReversedSleepTime(timeUnit,updateFrequency);
	}

	/**
	 * Method which sets the updates frequency of this thread
	 * @param updateFrequency
	 */
	public void setUpdateFrequency(int updateFrequency) {
		this.updateFrequency = getSleepTime(timeUnit,updateFrequency);;
	}

	/**
	 * Method which returns the time unit used by this thread
	 * @return
	 */
	public TimeUnit getTimeUnit() {
		return timeUnit;
	}

	/**
	 * Method which sets a new time unit for the thread
	 * @param timeUnit
	 */
	public void setTimeUnit(TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
	}

	/**
	 * Method which returns the ID of the valueTask being performed by the thread
	 * @return
	 */
	public String getThreadName(){
		return workerHelper.getName();

	}

	/**
	 * Method which returns the valueTask being performed by this thread
	 * @return
	 */
	public TaskWrapper getTask(String name) {
		return computeTask.get(name);
	}

	/**
	 * Method which adds a new valueTask to be performed by the thread.
	 * @param updates
	 */
	public void addTask(TaskWrapper update) {
		if(update instanceof TaskUpdate){
			TaskUpdate task = (TaskUpdate)update;
			if(task.getTaskID()!=null){
				computeTask.put(task.getTaskID(), update);
			}else{
				computeTask.put(String.valueOf(taskCounter), update);
			}
		}else{
			computeTask.put(String.valueOf(taskCounter), update);
		}
	}

	/**
	 * Method which removes valueTask from the valueTask collection
	 * @param name
	 * @return
	 */
	public boolean removeTask(String name){
		if(computeTask.containsKey(name)){
			computeTask.remove(name);
			return true;
		}else{
			return false;
		}
	}

	/**
	 * Method which a Thread sleep time.
	 * @param milliseconds
	 */
	private void threadSleep(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
		}
	}
	/**
	 * Method which performs an updates to the valueTask
	 */
	private void performUpdate() {
		if (parallel) {
			computeTask.entrySet().parallelStream().forEach(entry -> {
				TaskWrapper value = entry.getValue();

				if (value != null) {
					value.doBackgroundWork();
				}
			});

		} else {
			for(Entry<String,TaskWrapper> entry : computeTask.entrySet()){
				entry.getValue().doBackgroundWork();
			}
		}
	}

	/**
	 * Thread class used by the worker to perform a valueTask.
	 * @author Eudy Contreas
	 *
	 */
	public class WorkerThreadHelper extends Thread {

		public void run() {

			switch(taskType){
			case CONTINUOUS:

				threadSleep(startDelay);

				while (running) {

					performUpdate();
					threadSleep(updateFrequency);
				}
				break;
			case SINGLE_INSTANCE:

				performUpdate();
				remove();

				break;
			default:
				break;

			}
		}
	}

	/**
	 * Method which removes this worker from the manager.
	 */
	private void remove(){
		if(manager!=null)
		manager.removeTask(this);
	}

	/**
	 * Method which returns a time conversion based on time unit
	 * @param unit
	 * @param time
	 * @return
	 */
	private int getSleepTime(TimeUnit unit, int time){
		switch(unit){
		case HOURS:
			return ((time*1000)*60)*60;
		case MINUTES:
			return ((time*1000)*60);
		case SECONDS:
			return (time*1000);
		case MILLISECONDS:
			return time;
		default:
			break;
		}
		return time;
	}

	/**
	 * Method which returns a reversed time conversion base on time unit
	 * @param unit
	 * @param time
	 * @return
	 */
	private int getReversedSleepTime(TimeUnit unit, int time){
		switch(unit){
		case HOURS:
			return ((time/60)/60)*1000;
		case MINUTES:
			return ((time/60)/1000);
		case SECONDS:
			return (time/1000);
		case MILLISECONDS:
			return time;
		default:
			break;
		}
		return time;
	}

	/**
	 * Enumeration containing valueTask types
	 * @author Eudy Contreras
	 *
	 */
	public enum TaskType{
		CONTINUOUS, SINGLE_INSTANCE
	}
}
