package com.emc.ehc.nick.netty.mtu;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;

/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年4月25日 下午10:19:13 
* 
*/
public class Client {
	
	public static void main(String[] args) throws Exception {
		new Client("localhost", 8090).OioRun();
	}
	
	private String host;
	private int port;
	
	public Client(String host, int port) {
		this.host = System.getProperty("host", host);
		this.port = port;
	}
	
	public void run() throws InterruptedException {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group);
			b.channel(NioSocketChannel.class)
			.option(ChannelOption.TCP_NODELAY, true);
			b.handler(new ChannelInitializer<SocketChannel> () {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new ClientHandler());
				}
				
			});
			
			ChannelFuture f = b.connect(this.host, this.port).sync();
			//f.channel().closeFuture().sync();
			
		} finally {
			group.shutdownGracefully();
		}
	}
	
	public void OioRun() throws IOException {
		Socket socket = new Socket();
		socket.connect(new InetSocketAddress(this.host, this.port));
		OutputStream output = socket.getOutputStream();
		String temp = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
				+ "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
				+ "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
				+ "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
		
		
		output.write(temp.getBytes());
		socket.shutdownOutput();
	}
}
