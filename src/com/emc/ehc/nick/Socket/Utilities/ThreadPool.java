package com.emc.ehc.nick.Socket.Utilities;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Test;

public class ThreadPool<T extends Runnable> extends Thread {
	
	private int capacity = 5;
	
	private BlockingQueue<T> taskQueue = null;
	
	private volatile boolean isStopped = false;
	
	public ThreadPool(int capacity) {
		this.capacity = capacity;
		this.taskQueue = new LinkedBlockingQueue<T> (capacity);
	}
	
	
	@Override
	public void run() {
		while(!isStopped()) {
			try {
				Runnable task = this.taskQueue.take();
				task.run();
			} catch(Exception e) {
				System.err.println("Error occurs when taking task");
			}
		}
	}
	
	public void put(T e) {
		try {
			this.taskQueue.put(e);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	public synchronized void close() {
		this.toStop();
		this.taskQueue = null;
	}
	
	
	public synchronized boolean isStopped() {
		return this.isStopped;
	}
	
	public synchronized void toStop() {
		this.isStopped = true;
		this.interrupt();
	}
	
	public synchronized boolean isFull() {
		return this.capacity == taskQueue.size();
	}
	
	@Test
	public static void testThreadPool() {
		ThreadPool<SocketWrapper> pool = new ThreadPool<SocketWrapper>(10);
	}
}
