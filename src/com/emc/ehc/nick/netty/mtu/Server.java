package com.emc.ehc.nick.netty.mtu;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;

/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年4月25日 下午10:18:58 
* 
*/
public class Server {
	
	public static void main(String[] args) throws InterruptedException {
		new Server(8090).run();
	}
	
	private int port;
	public Server(int port) {
		this.port = port;
	}
	
	public void run() throws InterruptedException {
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup worker = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(boss, worker);
			b.channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_KEEPALIVE, true)
			.handler(new LoggingHandler())
			.childHandler(new ChannelInitializer<SocketChannel> () {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new ServerReplyDecoder());
					ch.pipeline().addLast(new ServerHandler());
				}
				
			});
			
			ChannelFuture f = b.bind(this.port).sync();
			f.channel().closeFuture().sync();
			
		} finally {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}
}
