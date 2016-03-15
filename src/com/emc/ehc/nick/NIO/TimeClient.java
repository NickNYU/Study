package com.emc.ehc.nick.NIO;

public class TimeClient {
	public void openClient(String host, int port) {
		new Thread(new TimeClientHandler(host, port));
	}
}
