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
public abstract class Reactor implements Runnable {
	
	@Override
	public void run() {
		while(!Thread.interrupted()) {
			for(Selector selector : selectors) {
				try {
					int selectedNum = selector.select();
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
						selector = Selector.open();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
				}
			}
		}
	}
	
	public void dispatch(SelectionKey key) {
		
	}
	
	//首先，要有一个可以轮询的机制
	private Selector[] selectors;
	//默认是1
	private int selectorNum = 1;
	
	public Reactor() throws IOException {
		this(1);
	}
	
	public Reactor(int num) throws IOException {
		this.selectorNum = num;
		selectors = new Selector[selectorNum];
		for(int i = 0; i < selectorNum; i++) {
			selectors[i] = Selector.open();
		}
	}
}
