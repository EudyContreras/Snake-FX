package com.EudyContreras.Snake.ThreadManagers;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import com.EudyContreras.Snake.ThreadTools.DefaultExecutorSupplier;
import com.EudyContreras.Snake.ThreadTools.PriorityRunnable;
import com.EudyContreras.Snake.ThreadTools.TaskPriority;
import com.EudyContreras.Snake.ThreadTools.TaskWrapper;


/**
 * Created by Eudy Contreras on 10/14/2016.
 */

public class ExecutorManager {


    private Future<?> futureTask;
    private ThreadPoolExecutor executor;

    private ExecutorType taskType = ExecutorType.FUTURE_TASK;


    public ExecutorManager(ExecutorType taskType){
    	this(1000,0, taskType);
    }

    public ExecutorManager(int updateFrequency, ExecutorType taskType){
    	this(updateFrequency,0, taskType);
    }

    public ExecutorManager(int updateFrequency, int startTime, ExecutorType taskType){
        this.taskType = taskType;
        this.initialize();
    }

    private void initialize(){
    	switch(taskType){
		case FUTURE_TASK:
			cancelExecutor();
			break;
		case LIGHT_BACKGROUND_TASK:
			executor = DefaultExecutorSupplier.getInstance().lightWeightBackgroundTasks();
			break;
		case NORMAL_BACKGROUND_TASK:
			executor = DefaultExecutorSupplier.getInstance().backgroundTasks();
			break;
		case PRIORITIZED_TASK:
			executor = DefaultExecutorSupplier.getInstance().prioritizeBackgroundTask();
			break;
    	}
    }

    public synchronized final ThreadPoolExecutor getExecutor(){
    	return executor;
    }

    public synchronized final Future<?> getFutureTask(){
    	return futureTask;
    }

    public void submitTask(TaskWrapper... tasks) {
    	submitTask(true, TaskPriority.MEDIUM, tasks);
    }

    public void submitTask(TaskPriority priority, TaskWrapper... tasks) {
    	submitTask(true, priority, tasks);
    }

    public synchronized void submitTask(boolean parallel, TaskWrapper... tasks) {
    	submitTask(parallel, tasks);
    }

    public synchronized void submitTask(boolean parallel, TaskPriority priority, TaskWrapper... tasks) {
    	switch(taskType){
		case FUTURE_TASK:
			futureTask = DefaultExecutorSupplier.getInstance().backgroundTasks().submit(()->{
				if(parallel){
					Arrays.asList(tasks).parallelStream().forEach(task -> task.doBackgroundWork());
				}else{
					Arrays.asList(tasks).stream().forEach(task -> task.doBackgroundWork());
				}
			});
			break;
		case LIGHT_BACKGROUND_TASK:
			executor.execute(()->{
				if(parallel){
					Arrays.asList(tasks).parallelStream().forEach(task -> task.doBackgroundWork());
				}else{
					Arrays.asList(tasks).stream().forEach(task -> task.doBackgroundWork());
				}
			});
			break;
		case NORMAL_BACKGROUND_TASK:
			executor.execute(()->{
				if(parallel){
					Arrays.asList(tasks).parallelStream().forEach(task -> task.doBackgroundWork());
				}else{
					Arrays.asList(tasks).stream().forEach(task -> task.doBackgroundWork());
				}
			});
			break;
		case PRIORITIZED_TASK:
			executor.submit(new PriorityRunnable(priority) {
				@Override
				public void run() {
					if(parallel){
						Arrays.asList(tasks).parallelStream().forEach(task -> task.doBackgroundWork());
					}else{
						Arrays.asList(tasks).stream().forEach(task -> task.doBackgroundWork());
					}
				}
			});
			break;
		default:
			break;
		}
    }

    public static void executeTask(Runnable task){
    	DefaultExecutorSupplier.getInstance().backgroundTasks().execute(task);
    }

    public static void executeTask(TaskWrapper task){
    	DefaultExecutorSupplier.getInstance().backgroundTasks().execute(()-> task.doBackgroundWork());
    }

    public static final <VALUE> VALUE computeValue(Callable<VALUE> call){

    	Future<VALUE> future = DefaultExecutorSupplier.getInstance().backgroundTasks().submit(call);
    	try {
			return future.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
    	return null;
    }

    public synchronized void shutDown(boolean immidiate){
    	if(executor!=null){
    		if(!immidiate){
    			executor.shutdown();
    		}else{
    			executor.shutdownNow();
    		}
    	}
    }

    public synchronized boolean cancelExecutor() {
		if (taskType == ExecutorType.FUTURE_TASK) {
			if (futureTask != null) {
				if (!futureTask.isDone()) {
					futureTask.cancel(true);
					try {
						return futureTask.isDone();
					} finally {
						futureTask = null;
					}
				}
			}
		}
        return false;
    }

    public enum ExecutorType{
    	NORMAL_BACKGROUND_TASK,
    	LIGHT_BACKGROUND_TASK,
    	PRIORITIZED_TASK,
    	FUTURE_TASK,
    }
}
