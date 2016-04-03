package com.emc.ehc.nick.NIO.Selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class SelectSockets {
	
	public static int PORT_NUMBER = 1234;
	private ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
	
	public static void main(String[] args) throws Exception {
		new SelectSockets().go();
	}
	
	public void go() throws Exception{
		
		int port = PORT_NUMBER;
		
		System.out.println("Listening on port : " + port);
		
		Selector selector = Selector.open();
		
		ServerSocketChannel serverChannel = ServerSocketChannel.open();
		
		serverChannel.socket().bind(new InetSocketAddress(port));
		
		serverChannel.configureBlocking(false);
		
		serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		
		while(true) {
			
			int n = selector.select();
			
			if(n == 0) {
				continue;
			}
			
			Iterator<SelectionKey> it = selector.selectedKeys().iterator();
			
			while(it.hasNext()) {
				
				SelectionKey key = it.next();
				
				if(key.isAcceptable()) {
					
					ServerSocketChannel serverSocket = (ServerSocketChannel) key.channel();
					
					SocketChannel channel = serverSocket.accept();
					
					channel.configureBlocking(false);
					
					channel.register(selector, SelectionKey.OP_READ);
					
					sayHello(channel);
				}
				if(key.isReadable()) {
					readDataFromSocket(key);
				}
				
				it.remove();
			}
		}
	}
	
	protected void readDataFromSocket(SelectionKey key) throws IOException, InterruptedException {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		int count; 
		
		buffer.clear(); // Empty buffer 
		// Loop while data is available; channel is nonblocking 
		while ((count = socketChannel.read(buffer)) > 0) { 
			buffer.flip(); // Make buffer readable 
			// Send the data; don't assume it goes all at once 
			while (buffer.hasRemaining()) { 
				socketChannel.write(buffer); 
			}
			System.out.println(buffer.toString());
			// WARNING: the above loop is evil. Because 
			// it's writing back to the same nonblocking 
			// channel it read the data from, this code can 
			// potentially spin in a busy loop. In real life 
			// you'd do something more useful than this. 
			buffer.clear(); 
			// Empty buffer 
		}
		if (count < 0) { 
			// Close channel on EOF, invalidates the key 
			socketChannel.close(); 
		}
		
	}

	private void sayHello(SocketChannel channel) throws IOException {
		buffer.clear();
		buffer.put("Hi there!\r\n".getBytes()); 
		buffer.flip(); 
		channel.write(buffer);
	}
}
