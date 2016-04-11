package com.emc.ehc.nick.NIO.ChatRoom.netty;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.*;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年4月9日 下午11:14:46 
* 
*/
public class ChatServer {
	public void startServer() {
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		// 初始化 eventloopgroup
		int processorsNumber = Runtime.getRuntime().availableProcessors();
		EventLoopGroup boss = new NioEventLoopGroup(1);
		EventLoopGroup worker = new NioEventLoopGroup(processorsNumber - 1);
		serverBootstrap.group(boss, worker);
		// 绑定接口
		serverBootstrap.channel(NioServerSocketChannel.class)
						.childHandler(new ChannelInitializer<NioSocketChannel> (){

							@Override
							protected void initChannel(NioSocketChannel ch) throws Exception {
							}
							
						});
		serverBootstrap.option(ChannelOption.SO_BACKLOG, 128);
        serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        serverBootstrap.bind(new InetSocketAddress("0.0.0.0", 9090));
		
	}
	
	public static void main(String[] args) {
		new ChatServer().startServer();
	}
}
