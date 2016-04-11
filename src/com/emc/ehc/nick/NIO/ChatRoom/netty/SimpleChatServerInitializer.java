package com.emc.ehc.nick.NIO.ChatRoom.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年4月11日 下午10:47:58 
* 
*/
public class SimpleChatServerInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast("farmer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
		ch.pipeline().addLast("Encoder", new StringEncoder());
		ch.pipeline().addLast("Decoder", new StringDecoder());
		ch.pipeline().addLast("Handler", new SimpleChatServerHandler());
		System.out.println("SimpleChatClient : "+ch.remoteAddress() +"连接上");
	}

}
