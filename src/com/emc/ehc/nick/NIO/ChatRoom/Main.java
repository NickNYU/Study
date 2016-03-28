package com.emc.ehc.nick.NIO.ChatRoom;

public class Main {
	public static void main(String[] args) {
		ChatServer server = new ChatServer();
		server.startServer(19999);
		new Thread(server).run();
	}
}
