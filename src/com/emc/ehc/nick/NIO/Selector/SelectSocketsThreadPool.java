package com.emc.ehc.nick.NIO.Selector;

import java.io.IOException;
import java.nio.channels.SelectionKey;

public class SelectSocketsThreadPool extends SelectSockets {
	private static final int MAX_THREADS = Runtime.getRuntime().availableProcessors() - 1;
	private ThreadPool pool = new ThreadPool(MAX_THREADS);
	
	public static void main(String[] argv) throws Exception { 
		new SelectSocketsThreadPool().go(); 
	}
	
	@Override
	protected void readDataFromSocket(SelectionKey key) throws InterruptedException {
		WorkerThread worker = pool.getWorker(); 
		if (worker == null) { 
			// No threads available. Do nothing. The selection 
			// loop will keep calling this method until a 
			// thread becomes available. This design could // be improved. 
			return; 
		}
			// Invoking this wakes up the worker thread, then returns 
		worker.serviceChannel(key); 
	}
}
