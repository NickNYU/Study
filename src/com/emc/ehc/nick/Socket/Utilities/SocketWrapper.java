package com.emc.ehc.nick.Socket.Utilities;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class SocketWrapper implements Runnable {
	private Socket socket = null;
	
	public SocketWrapper(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		SocketAddress address = socket.getRemoteSocketAddress();
		try {
			OutputStream output = this.socket.getOutputStream();
			output.write(address.toString().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
