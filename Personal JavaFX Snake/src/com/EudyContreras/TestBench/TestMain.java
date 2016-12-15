package com.EudyContreras.TestBench;

import java.util.concurrent.atomic.AtomicInteger;

import com.EudyContreras.Snake.Managers.ExecutorManager;
import com.EudyContreras.Snake.Managers.ExecutorManager.TaskType;
import com.EudyContreras.Snake.ThreadExecutors.TaskPriority;


public class TestMain {

	private static final String TASK_ONE = " taskOne";
	private static final String TASK_TWO = " taskTwo";

	public static void main(String[] args){

		AtomicInteger counter = new AtomicInteger(0);
		ExecutorManager task1 = new ExecutorManager(1000, TaskType.FUTURE_TASK);

		task1.setThreadPriority(TaskPriority.HIGH);

		task1.addTask(TASK_ONE, ()->{

			counter.set(counter.get()+1);
			System.out.print(TASK_ONE+": "+counter);

			if(counter.get()>=20){
				task1.cancelTask();
			}
		});
		task1.start();

		task1.addTask(TASK_TWO, ()->{
			counter.set(counter.get()+1);
			System.out.print(TASK_TWO+": "+counter);

			if(counter.get()>=10){
				task1.removeTask(TASK_TWO);
			}
		});

	}
}
