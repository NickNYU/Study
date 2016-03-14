package com.emc.ehc.nick.NIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class MultipleTimeServer implements Runnable {
	
	private Selector selector = null;
	
	private ServerSocketChannel serverSocketChannel = null;
	
	private volatile boolean isStop = false;
	
	public MultipleTimeServer(int port) throws IOException {
		
		this.serverSocketChannel = ServerSocketChannel.open();
		this.serverSocketChannel.socket().bind(new InetSocketAddress(port));
		this.serverSocketChannel.configureBlocking(false);
		
		this.selector = Selector.open();
		
		this.serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
		
	}
	
	@Override
	public void run() {
		try {
			while(!this.isStop) {
				int num = selector.select();
				Set<SelectionKey> selectedKeys = this.selector.selectedKeys();
				Iterator<SelectionKey> it = selectedKeys.iterator();
				while(it.hasNext()) {
					SelectionKey key = it.next();
					it.remove();
					handleKey(key);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			
		}
	}
	
	private void handleKey(SelectionKey key) throws IOException {
		if(key.isValid()) {
			if(key.isAcceptable()) {
				// 如果是acceptable，说明是 “ServerSocketChannel” （能使用method accept（））
				ServerSocketChannel acceptSocketChannel = (ServerSocketChannel) key.channel();
				SocketChannel socketChannel = acceptSocketChannel.accept();
				socketChannel.configureBlocking(false);
				
				socketChannel.register(this.selector, SelectionKey.OP_READ);
			} else if(key.isReadable()) {
				// 如果是 readable，说明是“SocketChannel”
				SocketChannel readChannel = (SocketChannel) key.channel();
				
				ByteBuffer buff = ByteBuffer.allocate(1024);
				int bytes = readChannel.read(buff);
				
				if(bytes > 0) {
					buff.flip();
					byte[] buffer = new byte[buff.remaining()];
					
					buff.get(buffer);
					String context = new String(buffer, "UTF-8");
					
					
				}
			}
		}
	}
}
