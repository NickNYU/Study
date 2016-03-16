package com.emc.ehc.nick.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.nio.channels.SocketChannel;

public class TimeServer {
	
	public void bind(int port) throws InterruptedException {
		EventLoopGroup workGroup = new NioEventLoopGroup();
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workGroup)
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 1024)
				.childHandler(new ChildrenChannelHandler());
			
			ChannelFuture future = b.bind(port).sync();
			
			future.channel().closeFuture().sync();
		} finally {
			workGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}
	
	private class ChildrenChannelHandler extends ChannelInitializer {

		@Override
		protected void initChannel(Channel channel) throws Exception {
			channel.pipeline().addLast(new TimeServerHandler());
		}
		
	}
	
	public static void main(String[] args) {
		int port = 8080;
		try {
			new TimeServer().bind(port);
		} catch(Exception e) {
			System.err.println(e.getMessage());
		}
	}
}
