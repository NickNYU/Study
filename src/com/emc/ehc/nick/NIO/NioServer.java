package com.emc.ehc.nick.NIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

public class NioServer {
	
	private class Server implements Runnable {
		private String ip;
		private int port;
		
		public Server(String ip, int port) {
			this.ip = ip;
			this.port = port;
		}
		
		public Server(int port) {
			this.ip = "localhost";
			this.port = port;
		}
		
		@Override
		public void run() {
			try {
				init(this.ip, this.port);
			} catch(UnknownHostException e) {
				System.err.println(e.getMessage());
			} catch(IOException ioException) {
				System.err.println(ioException.getMessage());
			}
		}
	}
	
	private void init(String ip, int port) throws IOException, UnknownHostException{
		System.out.println("Open connection port : " + port);
		
		Selector selector = Selector.open();
		
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		
		SocketAddress address = new InetSocketAddress(ip, port);
		
		serverSocketChannel.bind(address);
		
		serverSocketChannel.configureBlocking(false);
		
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		
		while(true) {
			int keys = selector.select();
			
			if(keys > 0) {
				// 依次处理selector上的每个已选择的SelectionKey
				try {
					for(SelectionKey key : selector.selectedKeys()) {
						
					}
				} catch(Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}
	}
}
