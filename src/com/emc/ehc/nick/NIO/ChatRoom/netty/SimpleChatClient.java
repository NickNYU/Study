package com.emc.ehc.nick.NIO.ChatRoom.netty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年4月13日 下午11:09:25 
* 
*/
public class SimpleChatClient {
	
	public static void main(String[] args) throws InterruptedException, IOException {
		new SimpleChatClient("localhost", 8090).run();
	}
	
	private final String host;
    private final int port;

    public SimpleChatClient(String host, int port){
        this.host = host;
        this.port = port;
    }
    
	public void run() throws InterruptedException, IOException {
		Bootstrap bootstrap = new Bootstrap();
		EventLoopGroup group = null;
		
		try {
			group = new NioEventLoopGroup(1);
			bootstrap.group(group);
			bootstrap.channel(NioSocketChannel.class)
					.handler(new SimpleChatClientInitializer());
			Channel channel = bootstrap.connect(this.host, this.port).sync().channel();
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            while(true){
                channel.writeAndFlush(in.readLine() + "\r\n");
            }
		} finally {
			group.shutdownGracefully();
		}
	}
}
