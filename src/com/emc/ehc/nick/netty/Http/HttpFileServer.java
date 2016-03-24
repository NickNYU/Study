package com.emc.ehc.nick.netty.Http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

public class HttpFileServer {
	private static final String DEFAULT_URL = "/src/com/emc/ehc/nick/netty/Http/";
	
	public void startServer(final String url, final int port) throws Exception {
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup worker = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(boss, worker)
			.channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 1024)
			.childHandler(new ChannelInitializer<SocketChannel>() {
	
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast("Http-Decoder", new HttpRequestDecoder());
					ch.pipeline().addLast("Http-Aggregator", new HttpObjectAggregator(65536));
					ch.pipeline().addLast("Http-Encoder", new HttpResponseEncoder());
					ch.pipeline().addLast("Http-Chunked", new ChunkedWriteHandler());
					ch.pipeline().addLast("Http-FileServerHandler", new HttpFileServerHandler(url));
				}
				
			});
			
			ChannelFuture future = b.bind("192.168.1.102", port).sync();
			System.out.println("Server starts at 192.168.1.102:" + port + url);
			future.channel().closeFuture().sync();
		} finally {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) {
		int port = 8080;
		String url = DEFAULT_URL;
		try {
			new HttpFileServer().startServer(url, port);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
