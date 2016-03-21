package com.emc.ehc.nick.netty.Echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class EchoClient {
	
	private final int port;
	private final String host;
	static final int SIZE = 10;
	
	public EchoClient(String host, int port) {
		this.port = port;
		this.host = host;
	}
	
	public void startClient() throws InterruptedException {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group);
			b.channel(NioSocketChannel.class);
			b.option(ChannelOption.TCP_NODELAY, true);
			//b.remoteAddress(this.host, this.port);
			b.handler(new ChannelInitializer(){
				
				@Override
				protected void initChannel(Channel ch) throws Exception {
					ch.pipeline().addLast(new EchoClientHandler());
				}
			});
			ChannelFuture f = b.connect(this.host, this.port).sync();
			f.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully().sync();
		}
	}
	
	public static void main(String[] args) {
		try {
			new EchoClient("127.0.0.1", 9090).startClient();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
