package com.emc.ehc.nick.netty.Reactor.Advanced;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;


/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年4月24日 下午12:18:43 
* 
*/
public class Acceptor implements ChannelHandler {

	@Override
	public void dealWithChannel() {
		try {
			// 等到轮询到的时候就可以accept啦
			SocketChannel connection = this.channel.accept();
			Selector selector = getSelector();
			System.out.println("[Acceptor] recieve the connection " + connection.getRemoteAddress().toString());
			if(isWithThreadPool) {
				new ChildHandlerWithThreadPool(connection, selector);
			} else {
				new ChildHandler(connection, selector);
			}
			System.out.println("Connection Accepted by Acceptor Handler");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	// 注 ： 这里的selector 是给 accept 出来的socketchannel用的，不是serversocketchannel本身的selector
	private Selector[] selectors;
	private ServerSocketChannel channel;
	private boolean isWithThreadPool = false;
	private int next = 0;
	
	// 初始化 Handler， 之后会attach 到key上， 然后 select()轮到的时候调用上面的  ‘dealWithChannel’ 方法
	// 第一个selector 是这个serversocketchannel本身的selector，第二个是用于socketchannel的selector数组
	public Acceptor(SelectionKey key, Selector[] selectors, boolean isWithThreadPool) throws Exception {
		this.selectors = selectors;
		this.channel = (ServerSocketChannel) key.channel();
		this.isWithThreadPool = isWithThreadPool;
		
		// 把Acceptor本身 bind到key上
		//key.attach(this);
		//System.out.println("Acceptor has been initialized : " + key.attachment().getClass().getName());
	}
	
	private Selector getSelector() {
		Selector selector = this.selectors[next++];
		next = (next == selectors.length) ? 0 : next;
		return selector;
	}
}
