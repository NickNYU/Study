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
	}
	
	public void handleKey(SelectionKey key) {
		SocketChannel channel = (SocketChannel) key.channel();
		
	}

	@Override
	public void run() {
		//boolean isConnect = this.clientChannel.isConnected();
		boolean isConnect = false;
		try {
			isConnect = this.clientChannel.connect(new InetSocketAddress(host, port));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(!isConnect) {
			
		} else {
			
		}
	}
}
