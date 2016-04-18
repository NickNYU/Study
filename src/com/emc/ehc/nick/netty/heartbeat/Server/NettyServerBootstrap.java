package com.emc.ehc.nick.netty.heartbeat.Server;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.emc.ehc.nick.netty.heartbeat.share.AskMsg;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年4月18日 下午10:16:39 
* 
*/
public class NettyServerBootstrap {
	private static int PORT;

	public static int getPort() {
		return PORT;
	}
	
	public NettyServerBootstrap setPort(int port) {
		PORT = port;
		return this;
	}
	
	public void startServer() throws InterruptedException {
		EventLoopGroup boss = null;
		EventLoopGroup worker = null;
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		try {
			boss = new NioEventLoopGroup(1);
			worker = new NioEventLoopGroup();
			serverBootstrap.group(boss, worker)
						.channel(NioServerSocketChannel.class)
						.option(ChannelOption.SO_BACKLOG, 128)
						.childOption(ChannelOption.SO_KEEPALIVE, true)
						.option(ChannelOption.TCP_NODELAY, true);
			
			serverBootstrap.childHandler(new ChannelInitializer<SocketChannel> () {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline pipeline = ch.pipeline();
					pipeline.addLast(new ObjectEncoder());
					pipeline.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
					pipeline.addLast(new ServerHandler());
				}
				
			});
			
			ChannelFuture f = serverBootstrap.bind(PORT).sync();
			if(f.isSuccess()){
	            System.out.println("server start---------------");
	        }
		} finally {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
		
	}
	
	public static void main(String []args) throws InterruptedException {
        NettyServerBootstrap server = new NettyServerBootstrap();
        server.setPort(8090).startServer();
        while (true){
            Channel channel = NettyChannelMap.getRandomChannel();
            if(channel!=null){
                AskMsg askMsg = new AskMsg();
                channel.writeAndFlush(askMsg);
            }
            TimeUnit.SECONDS.sleep(5);
        }
    }
}
