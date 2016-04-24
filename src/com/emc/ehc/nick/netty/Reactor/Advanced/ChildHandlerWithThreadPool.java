package com.emc.ehc.nick.netty.Reactor.Advanced;

import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年4月24日 下午4:37:51 
* 
*/
public class ChildHandlerWithThreadPool implements ChannelHandler {

	public ChildHandlerWithThreadPool(SocketChannel socketChannel, Selector selector) {
		// TODO Auto-generated constructor stub
	}
	@Override
	public void dealWithChannel() {
		// TODO Auto-generated method stub

	}
	
	private SocketChannel channel;
	private Selector selector;
}
