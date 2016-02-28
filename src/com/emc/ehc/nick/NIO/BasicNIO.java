package com.emc.ehc.nick.NIO;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

public class BasicNIO {
	
	public static void main(String[] args) {
		try {
			ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
			
			SocketAddress socketAddress = new InetSocketAddress(InetAddress.getByName("10.103.201.79"), 8080);
			serverSocketChannel.bind(socketAddress);
			
			serverSocketChannel.configureBlocking(false);
			
			Selector selector = Selector.open();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
