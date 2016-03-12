package com.emc.ehc.nick.BIO;

import java.io.*;
import java.net.Socket;

public class TimeServerHandler implements Runnable {
	
	private Socket socket = null;
	
	public TimeServerHandler(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		BufferedReader input = null;
		PrintWriter output = null;
		try {
			input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			output = new PrintWriter(this.socket.getOutputStream(), true);
			
			String currentTime = null;
			String body = null;
			
			while((body = input.readLine()) != null) {
				System.out.println("The time server receive order : " + body);
		        currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new java.util.Date(
		            System.currentTimeMillis()).toString() : "BAD ORDER";
		        output.println(currentTime);
				
			}
		} catch(IOException e) {
			if (input != null) {
			    try {
			    	input.close();
			    } catch (IOException e1) {
			        e.printStackTrace();
			    }
			}
			if (output != null) {
				output.close();
				output = null;
			}
	        if (this.socket != null) {
		        try {
		            this.socket.close();
		        } catch (IOException e1) {
		            e.printStackTrace();
		        }
		        this.socket = null;
	        }
		}
	}

}
