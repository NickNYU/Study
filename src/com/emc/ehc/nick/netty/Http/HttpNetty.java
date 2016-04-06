package com.emc.ehc.nick.netty.Http;

import java.net.InetSocketAddress;
import java.nio.channels.spi.SelectorProvider;
import java.util.concurrent.ThreadFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.util.concurrent.DefaultThreadFactory;

/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年4月6日 下午11:11:37 
* 
*/
public class HttpNetty {
	public static void main(String[] args) {
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		//=======================下面我们设置线程池
        EventLoopGroup bossLoopGroup = new NioEventLoopGroup(1);
        ThreadFactory threadFactory = new DefaultThreadFactory("work thread pool");
        int processorsNumber = Runtime.getRuntime().availableProcessors();
        EventLoopGroup workLoogGroup = new NioEventLoopGroup(processorsNumber * 2, threadFactory, SelectorProvider.provider());
        serverBootstrap.group(bossLoopGroup , workLoogGroup);
        // 服务通道类型
        serverBootstrap.channel(NioServerSocketChannel.class);
      //========================设置处理器
        HttpServerHandler httpServerHandler = new HttpServerHandler();
        serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel> (){

			@Override
			protected void initChannel(NioSocketChannel ch) throws Exception {
				//我们在socket channel pipeline中加入http的编码和解码器
				ch.pipeline().addLast(new HttpResponseEncoder());
				ch.pipeline().addLast(new HttpRequestDecoder());
				ch.pipeline().addLast(httpServerHandler);
			}
        	
        });
        
        serverBootstrap.option(ChannelOption.SO_BACKLOG, 128);
        serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        serverBootstrap.bind(new InetSocketAddress("0.0.0.0", 8090));
	}
}
