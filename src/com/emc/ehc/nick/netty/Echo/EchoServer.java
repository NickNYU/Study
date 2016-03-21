package com.emc.ehc.nick.netty.Echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class EchoServer {
	private static final int PORT = Integer.parseInt(System.getProperty("port", "8080"));
	
	public EchoServer() {
	}
	
	public void startServer() throws InterruptedException {
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup worker = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(boss, worker);
			b.channel(NioServerSocketChannel.class);
			b.option(ChannelOption.SO_BACKLOG, 1024);
			//b.localAddress(PORT);
			b.childHandler(new EchoServerHandler());
			b.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new ChannelInitializer() {
				@Override
				public void initChannel(Channel channel) throws Exception {
					channel.pipeline().addLast(new EchoServerHandler());
				}
			});
			ChannelFuture f = b.bind(PORT).sync();
			System.out.println(EchoServer.class.getName() + " started and listen on " + f.channel().localAddress());
			f.channel().close().sync();
		} finally {
			boss.shutdownGracefully().sync();
			worker.shutdownGracefully().sync();
		}
	}
	
	public static void main(String[] args) {
		try {
			new EchoServer().startServer();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
