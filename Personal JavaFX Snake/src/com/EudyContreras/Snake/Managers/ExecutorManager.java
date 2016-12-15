package com.EudyContreras.Snake.Managers;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

import com.EudyContreras.Snake.Exceptions.DuplicateTaskIDException;
import com.EudyContreras.Snake.Exceptions.NoSuchTaskException;
import com.EudyContreras.Snake.ThreadExecutors.DefaultExecutorSupplier;
import com.EudyContreras.Snake.ThreadExecutors.EventQueue;
import com.EudyContreras.Snake.ThreadExecutors.PriorityRunnable;
import com.EudyContreras.Snake.ThreadExecutors.TaskPriority;
import com.EudyContreras.Snake.ThreadExecutors.TaskWrapper;


/**
 * Created by Eudy Contreras on 10/14/2016.
 */

public class ExecutorManager {

    private int taskStartDelay = 0;
    private int updateFrequency = 1000;

    private boolean running = false;
    private boolean useParallelStream = true;

    private Map<String,TaskWrapper> taskCollection;
    private Future<?> futureTask;
    private WorkerThread thread;
    private EventQueue eventQueue;

    private TaskPriority threadPriority = TaskPriority.HIGH;
    private TaskType taskType = TaskType.FUTURE_TASK;


    public ExecutorManager(){
    	this(1000,0,TaskType.NORMAL_THREAD);
    }

    public ExecutorManager(int updateFrequency){
    	this(updateFrequency,0,TaskType.NORMAL_THREAD);
    }

    public ExecutorManager(TaskType taskType){
    	this(1000,0, taskType);
    }

    public ExecutorManager(int updateFrequency, TaskType taskType){
    	this(updateFrequency,0, taskType);
    }

    public ExecutorManager(int updateFrequency, int startTime, TaskType taskType){
        this.taskType = taskType;
        this.taskStartDelay = startTime;
        this.updateFrequency = updateFrequency;
        this.taskCollection = Collections.synchronizedMap(new ConcurrentHashMap<String,TaskWrapper>());
    }

    public void start(){
        this.running = true;
    	switch(taskType){
    	case NORMAL_THREAD:
    		cancelTask();
    		startThread();
    		break;
		case FUTURE_TASK:
			cancelTask();
			doInBackground();
			break;
		case LIGHT_BACKGROUND_TASK:
			doInBackground();
			break;
		case NORMAL_BACKGROUND_TASK:
			doInBackground();
			break;
		case PRIORITIZED_TASK:
			doInBackground();
			break;
		default:
			break;
    	}
    }

    public void resume(){
    	setRunning(true);
    }

    public void pause(){
    	setRunning(false);
    }

    public void stopEventQueue(){
    	if(eventQueue!=null){
    		if(eventQueue.isRunning()){
    			eventQueue.stop();
    		}
    	}
    }

    private void startThread(){
    	if(thread==null){
    		thread = new WorkerThread();
    		thread.start();
    	}
    }

    public synchronized int getTaskStartDelay() {
		return taskStartDelay;
	}

	public synchronized void setTaskStartDelay(int taskStartDelay) {
		this.taskStartDelay = taskStartDelay;
	}

	public synchronized int getUpdateFrequency() {
		return updateFrequency;
	}

	public void setUpdateFrequency(int updateFrequency) {
		this.updateFrequency = updateFrequency;
	}

	public synchronized boolean isRunning() {
		return running;
	}

	public synchronized void setRunning(boolean running) {
		this.running = running;
	}

	public synchronized TaskPriority getThreadPriority() {
		return threadPriority;
	}

	public void setThreadPriority(TaskPriority threadPriority) {
		this.threadPriority = threadPriority;
	}

	private void threadSleep(){
		try {
			Thread.sleep(updateFrequency);
		} catch (InterruptedException e) {}
	}

	private void threadSleep(int milliseconds){
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {}
	}

    public synchronized void clearTasks(){
    	taskCollection.clear();
    }

    public synchronized int tasksCount(){
    	return taskCollection.size();
    }

    public EventQueue addToEventQueue(Runnable runnable){
    	if(eventQueue == null){
    		eventQueue = new EventQueue();
    		eventQueue.start();
    	}
    	eventQueue.execute(runnable);
    	return eventQueue;
    }

	public synchronized ExecutorManager addTask(String name, TaskWrapper task){
		if (!taskCollection.containsKey(name)) {
			taskCollection.put(name, task);
		} else {
			try {
				throw new DuplicateTaskIDException("The task manager already contains a task with this ID!");
			} catch (DuplicateTaskIDException e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
		return this;
	}

	public synchronized ExecutorManager removeTask(String name) {
		if (taskCollection.containsKey(name)) {
			taskCollection.remove(name);
		} else {
			try {
				throw new NoSuchTaskException("The task manager does not contain a task with this ID!");
			} catch (NoSuchTaskException e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
		return this;
	}

	public synchronized boolean containsTask(String name){
		return taskCollection.containsKey(name);
	}

    public synchronized boolean cancelTask() {
		if (taskType == TaskType.FUTURE_TASK) {
			if (futureTask != null) {
				running = false;

				if (!futureTask.isDone()) {
					futureTask.cancel(true);
					try {
						return futureTask.isDone();
					} finally {
						futureTask = null;
					}
				}
			}
		} else if(taskType == TaskType.NORMAL_THREAD) {
			if(thread!=null) {
				running = false;
			    thread.interrupt();
			    thread=null;
			    return true;
			}
		}
        return false;
    }


    private void doInBackground() {
    	switch(taskType){
		case FUTURE_TASK:
			futureTask = DefaultExecutorSupplier.getInstance().backgroundTasks().submit(()->{
				threadSleep(taskStartDelay);

				while (running) {
					backgroundWork();
					threadSleep();
				}
			});
			break;
		case LIGHT_BACKGROUND_TASK:
			DefaultExecutorSupplier.getInstance().lightWeightBackgroundTasks().execute(()->{
				threadSleep(taskStartDelay);

				while (running) {
					backgroundWork();
					threadSleep();
				}
			});
			break;
		case NORMAL_BACKGROUND_TASK:
			DefaultExecutorSupplier.getInstance().backgroundTasks().execute(()->{
				threadSleep(taskStartDelay);

				while (running) {
					backgroundWork();
					threadSleep();
				}
			});
			break;
		case PRIORITIZED_TASK:
			DefaultExecutorSupplier.getInstance().prioritizeBackgroundTask()
					.submit(new PriorityRunnable(threadPriority) {
						@Override
						public void run() {

							threadSleep(taskStartDelay);

							while (running) {
								backgroundWork();
								threadSleep();
							}
						}
					});
			break;
		default:
			break;

		}
    }

    private class WorkerThread extends Thread {
		public void run() {

			threadSleep(taskStartDelay);

			while(running && thread!=null) {
				backgroundWork();
				threadSleep();
			}
		}
	}

	private void backgroundWork() {
		if(useParallelStream){

			synchronized (taskCollection) {

				taskCollection.entrySet().parallelStream().forEach(entry -> {

					TaskWrapper value = entry.getValue();

					if (value != null) {
						value.doBackgroundWork();
					}
				});
			}

		} else {
			synchronized (taskCollection) {

				for (Map.Entry<String, TaskWrapper> entry : taskCollection.entrySet()) {

					TaskWrapper value = entry.getValue();

					if (value != null) {
						value.doBackgroundWork();
					}
				}
			}
		}
	}

    public enum TaskType{
    	NORMAL_BACKGROUND_TASK,
    	LIGHT_BACKGROUND_TASK,
    	PRIORITIZED_TASK,
    	NORMAL_THREAD,
    	FUTURE_TASK,

    }

}
