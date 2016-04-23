package com.emc.ehc.nick.netty.heartbeat.Client;

import java.util.concurrent.TimeUnit;

import com.emc.ehc.nick.netty.TimeClientHandler;
import com.emc.ehc.nick.netty.heartbeat.share.AbstractMsg;
import com.emc.ehc.nick.netty.heartbeat.share.AskMsg;
import com.emc.ehc.nick.netty.heartbeat.share.AskParams;
import com.emc.ehc.nick.netty.heartbeat.share.Constants;
import com.emc.ehc.nick.netty.heartbeat.share.LoginMsg;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.socket.oio.OioSocketChannel;
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
		EventLoopGroup group = new OioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(OioSocketChannel.class)
				.option(ChannelOption.TCP_NODELAY, true)
				.handler(new ChannelInitializer<SocketChannel> (){

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(new IdleStateHandler(20,10,0));
						ch.pipeline().addLast(new ObjectEncoder());
						ch.pipeline().addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
						ch.pipeline().addLast(new ClientHandler());
					}
					
				});
			ChannelFuture future = b.connect(host, port).sync();
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
        ClientBootstrap client = new ClientBootstrap("127.0.0.1", 9090);
        ChannelFuture future = client.startClient();
        future.channel().closeFuture().sync();
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
