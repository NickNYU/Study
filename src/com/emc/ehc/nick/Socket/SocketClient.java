package com.emc.ehc.nick.Socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketClient {
	
	public void send(String message, OutputStream output) throws IOException {
		byte[] messageStream = message.getBytes();
		output.write(messageStream);
	}
	
	public void read(InputStream input) throws IOException {
		byte[] data = new byte[1024];
		int readLength = 0;
		while(readLength != -1) {
			readLength = input.read(data);
			System.out.println(data.toString());
			
		}
		data = null;
	}
	
	public void openSocket(String host, int port, String message) {
		
		Socket socket = null;
		
		try {
			socket = new Socket(host, port);
			
			InputStream input = socket.getInputStream();
			OutputStream output = socket.getOutputStream();
			
			send(message, output);
			
			read(input);
			
		} catch(Exception e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				socket.close();
			} catch(Exception exc) {
				System.err.println("Error occurs when trying to close socket : " + exc.getMessage());
			}
		}
	}
	
	public static void main(String[] args) {
		SocketClient client = new SocketClient();
		for(int i = 0; i < 10; i++) {
			client.openSocket("localhost", 8080, "Hello World! : " + i);
		}
	}
}
