package com.EudyContreras.Snake.ThreadExecutors;

import java.util.LinkedList;

public class ThreadBuffer<T> {
	
	private LinkedList<T> buffer = new LinkedList<>();

	public synchronized void put(T element) {
		buffer.addLast(element);
		notifyAll();
	}

	public synchronized T get() throws InterruptedException {
		while(buffer.isEmpty()) {
			wait();
		}
		return buffer.removeFirst();
	}

	public synchronized boolean hasNext(){
		return !buffer.isEmpty();
	}
}
