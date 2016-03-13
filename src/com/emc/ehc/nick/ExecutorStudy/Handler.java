package com.emc.ehc.nick.ExecutorStudy;

import java.net.Socket;

public class Handler implements Runnable {
	
	private Socket socket = null;
	
	public Handler(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		// do things to socket
	}
}
