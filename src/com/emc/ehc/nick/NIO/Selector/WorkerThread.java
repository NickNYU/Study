package com.emc.ehc.nick.NIO.Selector;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class WorkerThread extends Thread {
	
	private ThreadPool threadPool;
	private SelectionKey key;
	private ByteBuffer buffer = ByteBuffer.allocate(1024);
	
	@Override
	public synchronized void run() {
		while(true) {
			block();
			System.out.println(this.getName() + " has been awakened");
			try {
				drainChannel(key);
			} catch (IOException e) {
				e.printStackTrace();
				// Close channel and nudge selector 
				try { 
					key.channel().close(); 
				} catch (IOException ex) { 
					ex.printStackTrace(); 
				}
				
				key.selector().wakeup();
			}
			key = null;
			this.threadPool.returnWorker(this);
		}
	}
	
	synchronized void serviceChannel(SelectionKey key) { 
		this.key = key; 
		key.interestOps(key.interestOps() & (~SelectionKey.OP_READ)); 
		this.notify(); // Awaken the thread 
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
	
	private void drainChannel(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		int count; 
		
		buffer.clear(); // Empty buffer 
		// Loop while data is available; channel is nonblocking 
		while ((count = socketChannel.read(buffer)) > 0) { 
			buffer.flip(); // Make buffer readable 
			// Send the data; don't assume it goes all at once 
			while (buffer.hasRemaining()) { 
				socketChannel.write(buffer); 
			}
			
			System.out.println(this.getName() + " : " +buffer.toString());
			// WARNING: the above loop is evil. Because 
			// it's writing back to the same nonblocking 
			// channel it read the data from, this code can 
			// potentially spin in a busy loop. In real life 
			// you'd do something more useful than this. 
			buffer.clear(); 
			// Empty buffer 
		}
		if (count < 0) { 
			// Close channel on EOF, invalidates the key 
			socketChannel.close(); 
		}
	}

}
