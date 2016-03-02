package com.emc.ehc.nick.Socket.Utilities;

import java.io.IOException;
import java.io.InputStream;
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
			System.out.println(address);
			byte[] data = new byte[1024];
			socket.getInputStream().read(data);
			System.out.println(data.toString());
			System.out.println("==============================");
			data = null;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
