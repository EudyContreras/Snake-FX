package com.EudyContreras.Snake.ThreadTools;

import java.util.LinkedList;

public class ThreadBuffer<T> {
	
	private LinkedList<T> buffer = new LinkedList<>();

	public synchronized void put(T element) {
		buffer.add(element);
		notifyAll();
	}

	public synchronized T get() throws InterruptedException {
		while(buffer.isEmpty()) {
			wait();
		}
		return buffer.poll();
	}

	public synchronized boolean hasNext(){
		return !buffer.isEmpty();
	}
}
