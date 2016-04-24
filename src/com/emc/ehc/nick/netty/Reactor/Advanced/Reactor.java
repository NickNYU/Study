package com.emc.ehc.nick.netty.Reactor.Advanced;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年4月24日 上午9:28:34 
* 
*/
public class Reactor implements Runnable {
	
	@Override
	public void run() {
		//System.out.println("Reactor : " + name + " has been started");
		while(true) {
			for(Selector selector : selectors) {
				//System.out.println("Debug : Reactor[" + name + "] loop the selectors");
				try {
					int selectedNum = selector.selectNow();
					//System.out.println("Reactor[" + name + "] select " + selectedNum + " keys");
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
					try {
						selector.close();
						System.err.println(e.getMessage());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
				}
			}
		}
	}
	
	public void dispatch(SelectionKey key) {
		ChannelHandler handler = (ChannelHandler) key.attachment();
		if(handler != null) {
			System.out.println("Reactor[" + name +"] dispatcher : " + handler.getClass().getName());
			handler.dealWithChannel();
		}
	}
	
	//首先，要有一个可以轮询的机制
	private Selector[] selectors;
	//默认是1
	private int selectorNum = 1;
	
	private String name;
	
	public Reactor(String name) throws IOException {
		this(name, 1);
	}
	
	public Reactor(String name, int num) throws IOException {
		this.name = name;
		this.selectorNum = num;
		selectors = new Selector[selectorNum];
		for(int i = 0; i < selectorNum; i++) {
			selectors[i] = Selector.open();
		}
		System.out.println("Reactor : " + name + "has been initilized");
	}
	
	public Selector[] getSelectors() {
		return this.selectors;
	}
}
