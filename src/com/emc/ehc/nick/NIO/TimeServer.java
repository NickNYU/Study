package com.emc.ehc.nick.NIO;

import java.io.IOException;

public class TimeServer {
	public void startServer(int port) {
		MultipleTimeServer server;
		try {
			server = new MultipleTimeServer(port);
			new Thread(server, "NIO-MultipleTimeServer-001").start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
