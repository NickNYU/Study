package com.emc.ehc.nick.Socket;

public class Main {
	public static void main(String[] args) {
		SocketServer server = SocketServer.getServer();
		server.run();
	}
}
