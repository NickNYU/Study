package com.emc.ehc.nick.NIO.Selector;

import java.util.LinkedList;
import java.util.Queue;

public class ThreadPool {
	private Queue<WorkerThread> idle;
	private final int MAX_THREAD;
	
	public ThreadPool(int num) {
		this.MAX_THREAD = num;
		idle = new LinkedList<WorkerThread> ();
	}
	
	public void returnWorker(WorkerThread thread) {
		this.idle.add(thread);
		this.notifyAll();
	}
	
	public void getWorker() throws InterruptedException {
		while(this.idle.isEmpty()) {
			this.wait();
		}
	}
}
