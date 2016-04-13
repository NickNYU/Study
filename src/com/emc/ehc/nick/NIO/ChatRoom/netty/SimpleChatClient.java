package com.emc.ehc.nick.NIO.ChatRoom.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年4月13日 下午11:09:25 
* 
*/
public class SimpleChatClient {
	
	public void run() {
		Bootstrap bootstrap = new Bootstrap();
		EventLoopGroup group = new NioEventLoopGroup(1);
		bootstrap.group(group);
		bootstrap.channel(NioSocketChannel.class)
				.handler(new SimpleChatClientInitializer());
	}
}
