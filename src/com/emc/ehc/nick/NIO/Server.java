package com.emc.ehc.nick.NIO;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class Server {
	private static SelectorLoop eventLoop = null;
	private static SelectorLoop businessLoop = null;
	private volatile boolean isBusinessRunning = false;
	
	private class SelectorLoop implements Runnable {
		
		private Selector selector = null;
		private ByteBuffer buffer = null;
		
		public SelectorLoop() throws IOException {
			this.selector = Selector.open();
			buffer = ByteBuffer.allocate(1024);
		}
		
		public Selector getSelector() {
			return this.selector;
		}
		
		@Override
		public void run() {
			while(true) {
				try {
					this.selector.select();
					Set<SelectionKey> selectedKeys = selector.selectedKeys();
					Iterator<SelectionKey> it = selectedKeys.iterator();
					while(it.hasNext()) {
						SelectionKey key = it.next();
						it.remove();
						
						dealKey(key);
					}
				} catch (IOException e) {
					System.err.println(e.getMessage());
				}
			}
			
		}
		
		public void dealKey(SelectionKey key) throws IOException {
			// An accept() Event : used for event loop
			if(key.isAcceptable()) {
				ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
				SocketChannel socketChannel = serverSocketChannel.accept();
				socketChannel.register(businessLoop.getSelector(), SelectionKey.OP_READ);
				socketChannel.configureBlocking(false);
				synchronized(Server.this) {
					if(!isBusinessRunning) {
						isBusinessRunning = true;
						new Thread(businessLoop).start();
					}
				}
			} else if(key.isReadable()) {
			// An read() Event : used for business loop
				SocketChannel socketChannel = (SocketChannel) key.channel();
				int length = socketChannel.read(buffer);
				// disconnection
				if(length < 0) {
					key.cancel();
					socketChannel.close();
					return;
				} 
				
				buffer.flip();
				String meesge = Charset.forName("UTF-8").decode(buffer).toString();
				System.out.println("Server received ["+meesge+"] from client address:" + socketChannel.getRemoteAddress());
				
				// echo back.
				socketChannel.write(ByteBuffer.wrap(meesge.getBytes(Charset.forName("UTF-8"))));
                 
                // 清空buffer
                buffer.clear();
			}
		}
	}
	
	public void startServer(int port) throws IOException {
		// Start a Event
		eventLoop = new SelectorLoop();
		// And a business Event
		businessLoop = new SelectorLoop();
		
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		InetSocketAddress address = new InetSocketAddress("localhost", port);
		ServerSocket socket = serverSocketChannel.socket();
		socket.bind(address);
		serverSocketChannel.configureBlocking(false);
		
		// Set event loop to listen on accept(), and start it
		serverSocketChannel.register(eventLoop.getSelector(), SelectionKey.OP_ACCEPT);
		
		new Thread(eventLoop).start();
	}
}
