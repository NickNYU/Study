package com.emc.ehc.nick.netty.REST;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年4月16日 下午10:11:06 
* 
*/
public class RESTServer {
	public void startServer() {
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup worker = new NioEventLoopGroup();
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(boss, worker)
				.channel(NioServerSocketChannel.class)
				.childOption(ChannelOption.SO_BACKLOG, 128)
				.option(ChannelOption.SO_KEEPALIVE, true);
	}
}
