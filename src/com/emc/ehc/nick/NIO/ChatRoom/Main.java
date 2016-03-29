package com.emc.ehc.nick.NIO.ChatRoom;

public class Main {
	public static void main(String[] args) {
		ChatServer server = new ChatServer(19999);
		new Thread(server).run();
	}
}
