package com.emc.ehc.nick.netty.Reactor.Advanced;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年4月24日 下午4:36:31 
* 
*/
public class ChildHandler implements ChannelHandler {

	public ChildHandler(SocketChannel socketChannel, Selector selector) throws Exception {
		//System.out.println();
		this.channel = socketChannel;
		channel.configureBlocking(false);
		this.key = channel.register(selector, SelectionKey.OP_READ);
		// important : 将Handler放入初始化后的selectionkey，这样如果轮询到key时，就可以直接调用dealwithchannel()了
		key.attach(this);
		
		selector.wakeup();
	}
	
	
	@Override
	public void dealWithChannel() {
		try {
			if(state == READ) {
				read();
			} else {
				write();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void read() throws IOException {
		int readCount = channel.read(message);
		if(readCount > 0) {
			proccessRead(readCount);
		}
		this.key.interestOps(SelectionKey.OP_WRITE);
		state = WRITE;
	}
	
	private synchronized void proccessRead(int readCount) {
        message.flip();   //from writing mode to reading mode
        byte[] bytes = message.array();
        clientID = new String(bytes, Charset.forName("UTF-8"));
        message.clear();  
	}


	private void write() throws IOException {
		System.out.println("Saying hello to " + clientID);  
        ByteBuffer output = ByteBuffer.wrap(("Hello " + clientID + "\n").getBytes());
        
        channel.write(output);
        output.clear();
        
        key.interestOps(SelectionKey.OP_READ);
        state = READ;
	}
	
	private SocketChannel channel;
	private SelectionKey key;
	private String clientID;
	private int state = 0;
	private ByteBuffer message = ByteBuffer.allocate(1024);
	private final static int READ = 0, WRITE = 1;
}
