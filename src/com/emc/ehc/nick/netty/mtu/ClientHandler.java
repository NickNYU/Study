package com.emc.ehc.nick.netty.mtu;

import java.net.SocketAddress;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年4月25日 下午10:28:14 
* 
*/
public class ClientHandler extends ChannelOutboundHandlerAdapter {

	private static final int LENGTH = 30;

	
	@Override
	public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress,
			ChannelPromise promise) throws Exception {
		ByteBuf buffer = ctx.alloc().buffer(4 + LENGTH*2);
		buffer.writeInt(0);
//		StringBuffer sb = new StringBuffer(LENGTH);
//		for(int i = 0; i < LENGTH; i++)
//			sb.append("A");
//		
		String temp = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
				+ "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
				+ "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
				+ "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
		buffer.writeBytes(temp.getBytes());
		buffer.setInt(0, buffer.writerIndex() - 4);
		
		System.out.println(buffer.toString());
		ChannelFuture f = ctx.writeAndFlush(buffer);
		f.addListener(new ChannelFutureListener() {

			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				System.out.println("in channelConnected: write ok");
			}
			
		});
		if(f.isSuccess()) {
			System.out.println("send out successfully");
		}
	}
	
	
	
}
