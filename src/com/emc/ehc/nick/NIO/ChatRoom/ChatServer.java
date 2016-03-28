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
			
			serverKey = server.register(selector, SelectionKey.OP_ACCEPT);
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
				//it.remove();  不在此remove，因为要根据人名做循环拿出read的buffer
				// 如果是server的accept
				if(key.isAcceptable()) {
					it.remove(); // 只在加入socket的时候remove
					registerSocketChannel(key);
				}
				if(key.isReadable()) {
					try {
						readMessage(key);
					} catch (IOException e) {
						e.printStackTrace();
					}
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
	
	private void readMessage(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel) key.channel();
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		StringBuffer sb = new StringBuffer();
		
		//将通道的数据读到缓存区  
        int count = channel.read(buffer);
        if (count > 0) {  
            //翻转缓存区(将缓存区由写进数据模式变成读出数据模式)  
            buffer.flip();  
            //将缓存区的数据转成String  
            sb.append(new String(buffer.array(), 0, count));  
        }
        String str = sb.toString();  
        //若消息中有"open_"，表示客户端准备进入聊天界面  
        //客户端传过来的数据格式是"open_zing"，表示名称为zing的用户请求打开聊天窗体  
        //用户名称列表有更新，则应将用户名称数据写给每一个已连接的客户端  
        if(str.indexOf("open_") != -1) {
        	String name = str.substring("open_".length());
        	printInfo(name + " login");
        	this.users.add(name);
        	// 因为之前没有被remove掉，得以在此循环结果
        	Iterator<SelectionKey> it = selector.selectedKeys().iterator();
        	while(it.hasNext()) {
        		SelectionKey selKey = it.next();  
                //若不是服务器套接字通道的key，则将数据设置到此key中  
                //并更新此key感兴趣的动作  
                if (selKey != serverKey) {  
                    selKey.attach(users);  
                    selKey.interestOps(selKey.interestOps() | SelectionKey.OP_WRITE);  
                } 
        	}
        } else if(str.indexOf("exit_") != -1) {
        	String name = str.substring("exit_".length());
        	this.users.remove(name);
        	
        	key.attach("close");
        	
        	key.interestOps(SelectionKey.OP_WRITE);
        	Iterator<SelectionKey> iter = key.selector().selectedKeys().iterator();  
            while (iter.hasNext()) {  
                SelectionKey selKey = iter.next();  
                if (selKey != serverKey && selKey != key) {  
                    selKey.attach(users);  
                    selKey.interestOps(selKey.interestOps() | SelectionKey.OP_WRITE);  
                }  
            }  
            printInfo(name + " offline"); 
        } else {
        	if(str.indexOf("^") != -1) {
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
	}
	
	private void writeMessage(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel) key.channel();
		Object msg = key.attachment();
		////这里必要要将key的附加数据设置为空，否则会有问题
		key.attach("");
		
		if("close".toString().equalsIgnoreCase((String) msg)) {
			key.cancel();
			channel.socket().close();
			channel.close();
		} else {
			channel.write(ByteBuffer.wrap(msg.toString().getBytes()));
		}
		//重设此key兴趣  
        key.interestOps(SelectionKey.OP_READ); 
	}
	
}
