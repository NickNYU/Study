package com.emc.ehc.nick.NIO.Selector;

import java.util.LinkedList;
import java.util.Queue;

public class ThreadPool {
	private Queue<WorkerThread> idle;
	private final int MAX_THREAD;
	
	public ThreadPool(int num) {
		this.MAX_THREAD = num;
		idle = new LinkedList<WorkerThread> ();
		for(int i = 0; i < MAX_THREAD; i++) {
			WorkerThread thread = new WorkerThread(this);
			thread.setName("Worker" + (i + 1));
			thread.start();
			
			idle.add(thread);
		}
	}
	
	public void returnWorker(WorkerThread thread) {
		synchronized(idle) {
			this.idle.add(thread);
		}
	}
	
	public WorkerThread getWorker() throws InterruptedException {
		WorkerThread worker = null;
		synchronized(idle) {
			if(idle.size() > 0) {
				worker = idle.remove();
			}
		}
		return worker;
	}
}
