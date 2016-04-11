package com.emc.ehc.nick.NIO.ChatRoom.netty;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.*;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年4月9日 下午11:14:46 
* 
*/
public class ChatServer {
	
	private int port;

    public ChatServer(int port) {
        this.port = port;
    }
	
	public void startServer() throws Exception {
		EventLoopGroup boss = null;
		EventLoopGroup worker = null;
		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			// 初始化 eventloopgroup
			int processorsNumber = Runtime.getRuntime().availableProcessors();
			boss = new NioEventLoopGroup(1);
			worker = new NioEventLoopGroup(processorsNumber - 1);
			serverBootstrap.group(boss, worker);
			// 绑定接口
			serverBootstrap.channel(NioServerSocketChannel.class)
							.childHandler(new SimpleChatServerInitializer());
			
			serverBootstrap.option(ChannelOption.SO_BACKLOG, 128);
		    serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
		    
		    System.out.println("SimpleChatServer 启动了");
		    
		    // 绑定端口，开始接收进来的连接
		    ChannelFuture f = serverBootstrap.bind(port).sync();
		 // 等待服务器  socket 关闭 。
		    // 在这个例子中，这不会发生，但你可以优雅地关闭你的服务器。
		    f.channel().closeFuture().sync();
		} finally {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
			System.out.println("SimpleChatServer 关闭了");
		}
	}
	
	public static void main(String[] args) throws Exception {
		new ChatServer(8090).startServer();
	}
}
