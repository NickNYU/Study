package com.emc.ehc.nick.netty.Echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EchoServer {
	private final int port;
	
	public EchoServer(int port) {
		this.port = port;
	}
	
	public void startServer() throws InterruptedException {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(group);
			b.channel(NioServerSocketChannel.class);
			b.localAddress(port);
			b.childHandler(new EchoServerHandler());
			b.handler(new ChannelInitializer() {
				@Override
				public void initChannel(Channel channel) throws Exception {
					channel.pipeline().addLast(new EchoServerHandler());
				}
			});
			ChannelFuture f = b.bind().sync();
			System.out.println(EchoServer.class.getName() + " started and listen on " + f.channel().localAddress());
			f.channel().close().sync();
		} finally {
			group.shutdownGracefully().sync();
		}
	}
	
	public static void main(String[] args) {
		try {
			new EchoServer(8080).startServer();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
