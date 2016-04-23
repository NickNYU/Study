package com.emc.ehc.nick.netty.Reactor;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年4月23日 下午10:59:17 
* 
*/
public class Handler implements Runnable {
	
	private SocketChannel channel;
	private Selector selector;
	
	private final int READ = 0, WRITE = 1;
	
	//初始状态  
    int state = READ;  
    String clientName = "";  

	public Handler(SocketChannel socketChannel, Selector selector) throws IOException {
		this.channel = socketChannel;
		this.selector = selector;
		
		this.channel.configureBlocking(false);
		SelectionKey selectedKey1 = this.channel.register(this.selector, SelectionKey.OP_READ);
		
		/* 
        handler作为SellectionKey的attachment。这样，handler就与SelectionKey也就是interestOps对应起来了 
        反过来说，当interestOps发生、SelectionKey被选中时，就能从SelectionKey中取得handler 
        */  
		selectedKey1.attach(this);
		selectedKey1.interestOps(SelectionKey.OP_READ);
		
		/**
		 * {@link Reactor} 中会通过调用 run 来真正触发操作*/
        this.selector.wakeup();
	}

	@Override
	public void run() {
		
	}

}
