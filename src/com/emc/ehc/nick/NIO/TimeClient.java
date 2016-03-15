package com.emc.ehc.nick.NIO;

import java.io.IOException;

public class TimeClient {
	public void openClient(String host, int port) {
		try {
			new Thread(new TimeClientHandler(host, port)).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
