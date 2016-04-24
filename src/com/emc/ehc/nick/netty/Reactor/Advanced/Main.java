package com.emc.ehc.nick.netty.Reactor.Advanced;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年4月24日 下午5:24:12 
* 
*/
public class Main {
	public static void main(String[] args) throws Exception {
		int processNum = Runtime.getRuntime().availableProcessors();
		Reactor boss = new Reactor("boss");
		Reactor worker = new Reactor("worker", 2 * processNum);
		
		ServerSocketChannel channel = ServerSocketChannel.open();
		Selector selector = boss.getSelectors()[0];
		channel.configureBlocking(false);
		channel.bind(new InetSocketAddress(8090));
		SelectionKey key = channel.register(selector, SelectionKey.OP_ACCEPT);
		Acceptor acceptor = new Acceptor(key, worker.getSelectors(), false);
		
		key.attach(acceptor);
		
		new Thread(boss).start();
		new Thread(worker).start();
	}
}
