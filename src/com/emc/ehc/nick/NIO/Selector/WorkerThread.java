package com.emc.ehc.nick.NIO.Selector;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

public class WorkerThread extends Thread {
	
	private ThreadPool threadPool;
	private SelectionKey key;
	private ByteBuffer buffer = ByteBuffer.allocate(1024);
	
	@Override
	public synchronized void run() {
		while(true) {
			block();
			System.out.println(this.getName() + " has been awakened");
			drainChannel(key);
			key = null;
		}
	}
	
	public WorkerThread(ThreadPool pool) {
		this.threadPool = pool;
	}
	
	private void block() {
		try {
			this.wait();
		} catch(Exception e) {
			System.err.println(e.getMessage());
			this.interrupt();
		}
	}
	
	private void drainChannel(SelectionKey key) {
		
	}
}
