package com.emc.ehc.nick.NIO.ChatRoom;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

public class ChatServer implements Runnable {
	
	private Selector selector;
	
	private SelectionKey serverKey;
	
	private boolean isRun;
	
	private Vector<String> users;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	
	public ChatServer() {
		this.isRun = true;
		this.users = new Vector<String> ();
	}
	
	public void startServer(int port) {
		try {
			selector = Selector.open();
			ServerSocketChannel server = ServerSocketChannel.open();
			server.socket().bind(new InetSocketAddress(port));
			server.configureBlocking(false);
			
			serverKey = server.register(selector, serverKey.OP_ACCEPT);
			printInfo("Server starting ......");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void printInfo(String str) {
		System.out.println("[" + sdf.format(new Date()) + "] -> " + str); 
	}
	
	@Override
	public void run() {
		int n = 0;
		while(isRun) {
			try {
				n = selector.select();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 跳过selector，如果没有东西的话
			if(n < 1)	continue;
			
			Set<SelectionKey> selectedKeys = selector.selectedKeys();
			Iterator<SelectionKey> it = selectedKeys.iterator();
			
			while(it.hasNext()) {
				SelectionKey key = it.next();
				it.remove();
				// 如果是server的accept
				if(key.isAcceptable()) {
					registerSocketChannel(key);
				}
				if(key.isReadable()) {
					readMessage(key);
				}
				if(key.isWritable()) {
					try {
						writeMessage(key);
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	private void registerSocketChannel(SelectionKey key) {
		ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
		try {
			SocketChannel channel = serverChannel.accept();
			if(channel != null)	{
				channel.configureBlocking(false); 
				channel.register(this.selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void readMessage(SelectionKey key) {
		SocketChannel channel = (SocketChannel) key.channel();
		ByteBuffer buff = ByteBuffer.allocate(1024);
		StringBuffer sb = new StringBuffer();
		
	}
	
	private void writeMessage(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel) key.channel();
		Object msg = key.attachment();
		////这里必要要将key的附加数据设置为空，否则会有问题
		key.attach("");
		
		if(msg.toString().equalsIgnoreCase("close")) {
			key.cancel();
			channel.socket().close();
			channel.close();
		} else {
			channel.write(ByteBuffer.wrap(msg.toString().getBytes()));
		}
	}
	
}
