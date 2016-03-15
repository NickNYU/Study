package com.emc.ehc.nick.NIO;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.*;

public class TimeClientHandler implements Runnable {
	
	private Selector selector = null;
	private SocketChannel clientChannel = null;
	private String host;
	private int port;
	
	public TimeClientHandler(String host, int port) throws IOException {
		this.clientChannel = SocketChannel.open();
		this.clientChannel.configureBlocking(false);
		this.selector = Selector.open();
		
		this.host = host == null ? "127.0.0.1" : host;
		this.port = port;
	}
	
	public void handleKey(SelectionKey key) {
		SocketChannel channel = (SocketChannel) key.channel();
		
	}

	@Override
	public void run() {
		
		try {
			doConnection();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void doConnection() throws IOException {
		//boolean isConnect = this.clientChannel.isConnected();
		boolean isConnect = this.clientChannel.connect(new InetSocketAddress(host, port));
	}
}
