package com.emc.ehc.nick.netty.Reactor;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;
/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年4月23日 下午10:35:57 
* 
*/

public class Reactor implements Runnable {

	@Override
	public void run() {
		System.out.println("Server listening to port: " + serverSocketChannel.socket().getLocalPort());
		while(!Thread.interrupted()) {
			int selectedNum;
			try {
				selectedNum = this.selector.select();
				if(selectedNum == 0) {
					continue;
				}
				
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				Iterator<SelectionKey> it = selectedKeys.iterator();
				for(int i = 0; i < selectedNum; i++) {
					dispatch((SelectionKey) it.next());
				}
				
				selectedKeys.clear();
				
			} catch (IOException e) {
				e.printStackTrace();
			}		
		}
	}
	
	private void dispatch(SelectionKey key) {
		
	}
	
	private ServerSocketChannel serverSocketChannel;
	private Selector selector;
	private boolean isWithThreadPool;
	private int port;
	
	public Reactor(int port, boolean isWithThreadPool) throws IOException {
		this.port = port;
		this.isWithThreadPool = isWithThreadPool;
		this.selector = Selector.open();
		this.serverSocketChannel = ServerSocketChannel.open();
		this.serverSocketChannel.configureBlocking(false);
		this.serverSocketChannel.bind(new InetSocketAddress(this.port));
		SelectionKey selectKey0 = this.serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		selectKey0.attach(new Acceptor());
	}
	
	class Acceptor implements Runnable {
		
		@Override
		public void run() {
			
		}
	}

}
