package com.emc.ehc.nick.netty.Reactor.Advanced;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年4月24日 下午12:18:43 
* 
*/
public class Acceptor implements ChannelHandler {

	@Override
	public void dealWithChannel(SelectionKey key) {
		
	}
	
	private Selector selector;
	
	public Acceptor(Selector selector) {
		this.selector = selector;
	}
}
