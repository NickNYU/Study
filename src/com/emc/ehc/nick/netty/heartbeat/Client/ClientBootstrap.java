package com.emc.ehc.nick.netty.heartbeat.Client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;

/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年4月19日 下午8:15:25 
* 
*/
public class ClientBootstrap {
	
	private int port;
	private String host;
	
	public void startClient() {
		EventLoopGroup group = null;
		try {
			Bootstrap client = new Bootstrap();
			client.group(group)
				.channel(NioSocketChannel.class)
				.option(ChannelOption.SO_KEEPALIVE,true)
				.remoteAddress(host,port)
				.handler(new ChannelInitializer<SocketChannel> () {
					
					 @Override
			         public void initChannel(SocketChannel ch) {
						 ChannelPipeline pipeline = ch.pipeline();
						 pipeline.addLast(new IdleStateHandler(20,10,0));
						 pipeline.addLast(new ObjectEncoder());
						 pipeline.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
						 pipeline.addLast(new ClientHandler());
					 }
				});
		} finally {
			group.shutdownGracefully();
		}
	}
}
