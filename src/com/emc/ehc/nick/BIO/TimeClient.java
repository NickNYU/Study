package com.emc.ehc.nick.BIO;

import java.io.*;
import java.net.*;

public class TimeClient {
	
	private Socket socket = null;
	BufferedReader in = null;
	PrintWriter out = null;
	
	public void startClient(int port) throws IOException {
		this.socket = new Socket("localhost", port);
		this.in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
		this.out = new PrintWriter(socket.getOutputStream(), true);
        this.out.println("QUERY TIME ORDER");
        System.out.println("Send order 2 server succeed.");
        String resp = this.in.readLine();
        System.out.println("Now is : " + resp);
	}
	
	public static void main(String[] args) {
		TimeClient client = new TimeClient();
		try {
			client.startClient(8080);
		} catch(IOException e) {
			System.err.println(e.getMessage());
		} finally {
			if(client.in != null) {
				try {
					client.in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				client.in = null;
			}
			if(client.out != null) {
				client.out.close();
				client.out = null;
			}
			if(client.socket != null) {
				try {
					client.socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				client.socket = null;
			}
		}
	}
}
