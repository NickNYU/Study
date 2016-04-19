package com.emc.ehc.nick.netty.heartbeat.Client;

import java.util.concurrent.TimeUnit;

import com.emc.ehc.nick.netty.heartbeat.share.AbstractMsg;
import com.emc.ehc.nick.netty.heartbeat.share.AskMsg;
import com.emc.ehc.nick.netty.heartbeat.share.AskParams;
import com.emc.ehc.nick.netty.heartbeat.share.Constants;
import com.emc.ehc.nick.netty.heartbeat.share.LoginMsg;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
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
	
	public ClientBootstrap(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public ChannelFuture startClient() throws InterruptedException {
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
			ChannelFuture future = client.connect(this.host, this.port).sync();
	        if (future.isSuccess()) {
	            System.out.println("connect server  成功---------");
	            return future;
	        }
		} finally {
			group.shutdownGracefully();
		}
		return null;
	}
	
	public static void main(String[]args) throws InterruptedException {
        Constants.setClientID("001");
        ClientBootstrap client = new ClientBootstrap("localhost", 8090);
        ChannelFuture future = client.startClient();
        
        if(future == null) {
        	throw new NullPointerException("ChannelFuture could not be null");
        }
        AbstractMsg message = new LoginMsg();
        ((LoginMsg) message).setUsername("username");
        ((LoginMsg) message).setPassword("password");
        future.channel().writeAndFlush(message);
        while (true){
            TimeUnit.SECONDS.sleep(3);
            AskMsg askMsg = new AskMsg();
            AskParams askParams = new AskParams();
            askParams.setAuth("authToken");
            askMsg.setParams(askParams);
            future.channel().writeAndFlush(askMsg);
        }
    }
}
