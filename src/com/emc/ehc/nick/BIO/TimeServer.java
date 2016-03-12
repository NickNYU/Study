package com.emc.ehc.nick.BIO;

import java.io.IOException;
import java.net.*;

public class TimeServer {
	
	public void startServer(int port) throws IOException {
		ServerSocket server = new ServerSocket(port);
		Socket socket = null;
		TimeServerExcutorPool excutor = new TimeServerExcutorPool(20, 100);
		while(true) {
			socket = server.accept();
			//new Thread(new TimeServerHandler(socket)).start();
			excutor.execute(new TimeServerHandler(socket));
		}
	}
	
	public static void main(String[] args) {
		int port = 8080;
		try {
			new TimeServer().startServer(port);
		} catch(IOException e) {
			System.err.println(e.getMessage());
		}
	}
}
