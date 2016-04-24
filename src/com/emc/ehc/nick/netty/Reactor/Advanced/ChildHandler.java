package com.emc.ehc.nick.netty.Reactor.Advanced;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年4月24日 下午4:36:31 
* 
*/
public class ChildHandler implements ChannelHandler {

	public ChildHandler(SocketChannel socketChannel, Selector selector) throws Exception {
		this.channel = socketChannel;
		this.key = channel.register(selector, SelectionKey.OP_READ);
		// important : 将Handler放入初始化后的selectionkey，这样如果轮询到key时，就可以直接调用dealwithchannel()了
		key.attach(this);
		
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
		channel.read(message);
		
		this.key.interestOps(SelectionKey.OP_WRITE);
		state = WRITE;
	}
	
	private void write() {
		
	}
	
	private SocketChannel channel;
	private SelectionKey key;
	private int state = 0;
	private ByteBuffer message = ByteBuffer.allocate(1024);
	private final static int READ = 0, WRITE = 1;
}
