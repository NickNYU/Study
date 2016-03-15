package com.emc.ehc.nick.BIO;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TimeServerExcutorPool {
	
	private ExecutorService excutor = null;
	
	public TimeServerExcutorPool(int maxPoolSize, int blockQueueSize) {
		this.excutor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
													maxPoolSize,
													120L,
													TimeUnit.SECONDS,
													new ArrayBlockingQueue(blockQueueSize));
		
	}
	
	public void execute(Runnable task) {
		excutor.execute(task);
	}
}
