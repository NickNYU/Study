package com.emc.ehc.nick.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.nio.channels.SocketChannel;

public class TimeServer {
	
	public void bind(int port) {
		EventLoopGroup workGroup = new NioEventLoopGroup();
		EventLoopGroup boosGroup = new NioEventLoopGroup();
		
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(boosGroup, workGroup)
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 1024)
				.childHandler(new ChildrenChannelHandler());
		} finally {
			
		}
	}
	
	private class ChildrenChannelHandler extends ChannelInitializer {

		@Override
		protected void initChannel(Channel channel) throws Exception {
			channel.pipeline().addLast(new TimeServerHandler());
		}
		
	}
}
