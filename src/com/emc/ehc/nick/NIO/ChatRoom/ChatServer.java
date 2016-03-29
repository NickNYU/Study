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
	private boolean isRunning;
	private Vector<String> userNames;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public ChatServer(int port) {
		isRunning = true;
		userNames = new Vector<String>();
		init(port);
	}

	/**
	 * 初始化选择器和服务器套接字
	 * 
	 * @param port
	 */
	private void init(int port) {
		try {
			selector = Selector.open();
			ServerSocketChannel serverChannel = ServerSocketChannel.open();
			serverChannel.socket().bind(new InetSocketAddress(port));
			serverChannel.configureBlocking(false);
			serverKey = serverChannel.register(selector, SelectionKey.OP_ACCEPT);
			printInfo("server starting...");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		try {
			while (isRunning) {
				int n = selector.select();
				if (n > 0) {
					Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
					while (iter.hasNext()) {
						SelectionKey key = iter.next();
						if (key.isAcceptable()) {
							iter.remove();
							ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
							SocketChannel channel = serverChannel.accept();
							if (channel == null) {
								continue;
							}
							channel.configureBlocking(false);
							channel.register(selector, SelectionKey.OP_READ);
						}
						if (key.isReadable()) {
							readMsg(key);
						}
						if (key.isWritable()) {
							writeMsg(key);
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void readMsg(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel) key.channel();
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		StringBuffer sb = new StringBuffer();
		int count = channel.read(buffer);
		if (count > 0) {
			buffer.flip();
			sb.append(new String(buffer.array(), 0, count));
		}
		String str = sb.toString();
		if (str.indexOf("open_") != -1) {//客户端连接服务器
			String name = str.substring(5);
			printInfo(name + " online");
			userNames.add(name);
			Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
			while (iter.hasNext()) {
				SelectionKey selKey = iter.next();
				if (selKey != serverKey) {
					selKey.attach(userNames);
					selKey.interestOps(selKey.interestOps() | SelectionKey.OP_WRITE);
				}
			}
		} else if (str.indexOf("exit_") != -1) {// 客户端发送退出命令
			String userName = str.substring(5);
			userNames.remove(userName);
			key.attach("close");
			key.interestOps(SelectionKey.OP_WRITE);
			Iterator<SelectionKey> iter = key.selector().selectedKeys().iterator();
			while (iter.hasNext()) {
				SelectionKey selKey = iter.next();
				if (selKey != serverKey && selKey != key) {
					selKey.attach(userNames);
					selKey.interestOps(selKey.interestOps() | SelectionKey.OP_WRITE);
				}
			}
			printInfo(userName + " offline");
		} else {// 读取客户端消息
			String uname = str.substring(0, str.indexOf("^"));
			String msg = str.substring(str.indexOf("^") + 1);
			printInfo("("+uname+")说：" + msg);
			String dateTime = sdf.format(new Date());
			String smsg = uname + " " + dateTime + "\n  " + msg + "\n";
			Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
			while (iter.hasNext()) {
				SelectionKey selKey = iter.next();
				if (selKey != serverKey) {
					selKey.attach(smsg);
					selKey.interestOps(selKey.interestOps() | SelectionKey.OP_WRITE);
				}
			}
		}
	}

	private void writeMsg(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel) key.channel();
		Object obj = key.attachment();
		key.attach("");
		if (obj.toString().equals("close")) {
			key.cancel();
			channel.socket().close();
			channel.close();
			return;
		}else {
			channel.write(ByteBuffer.wrap(obj.toString().getBytes()));
		}
		key.interestOps(SelectionKey.OP_READ);
	}

	private void printInfo(String str) {
		System.out.println("[" + sdf.format(new Date()) + "] -> " + str);
	}
}
