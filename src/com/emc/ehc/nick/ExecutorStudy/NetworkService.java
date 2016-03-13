package com.emc.ehc.nick.ExecutorStudy;

import java.io.IOException;
import java.net.ServerSocket;
/**
 *从 Interface Executor 实现的线程池类server */
import java.util.concurrent.*;

public class NetworkService {
	private Executor executor = null;
	private ServerSocket server = null;
	
	public NetworkService(int poolSize, int port) throws IOException {
		server = new ServerSocket(port);
		executor = Executors.newFixedThreadPool(poolSize);
	}
	
	public void startServer() {
		try {
			for(;;) {
				executor.execute(new Handler(server.accept()));
			}
		} catch(IOException e) {
			
		}
	}
}
