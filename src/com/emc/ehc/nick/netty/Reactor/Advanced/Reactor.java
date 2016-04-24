package com.emc.ehc.nick.netty.Reactor.Advanced;

import java.nio.channels.Selector;

/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年4月24日 上午9:28:34 
* 
*/
public abstract class Reactor implements Runnable {
	
	@Override
	public void run() {
		
	}
	
	//首先，要有一个可以轮询的机制
	private Selector[] selectors;
	//默认是1
	private int selectorNum = 1;
	
}
