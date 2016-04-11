package com.emc.ehc.nick.NIO.ChatRoom.netty;

import java.sql.Date;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年4月11日 下午10:28:24 
* 
*/
public class SimpleChatServerHandler extends SimpleChannelInboundHandler<String> {
	
	public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	
	/* (non-Javadoc)
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelActive(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		System.out.println("SimpleChatClient : " + channel.remoteAddress() + "上线");
	}

	/* (non-Javadoc)
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelInactive(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		System.out.println("SimpleChatClient : " + channel.remoteAddress() + "掉线");
	}

	/* (non-Javadoc)
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#exceptionCaught(io.netty.channel.ChannelHandlerContext, java.lang.Throwable)
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		Channel channel = ctx.channel();
		System.err.println("SimpleChatClient : " + channel.remoteAddress() + cause.getMessage());
	}

	/* (non-Javadoc)
	 * @see io.netty.channel.ChannelHandlerAdapter#handlerAdded(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();
		for(Channel channel : channels) {
			channel.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 加入\n");
		}
		channels.add(incoming);
	}

	/* (non-Javadoc)
	 * @see io.netty.channel.ChannelHandlerAdapter#handlerRemoved(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();
		for(Channel channel : channels) {
			channel.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 离开\n");
		}
		channels.remove(incoming);
	}
	
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		Channel incoming = ctx.channel();
		for(Channel channel : channels) {
			if(incoming != channel) {
				channel.writeAndFlush("[" + incoming.remoteAddress() + "]" + msg + "\n");
			} else {
				channel.writeAndFlush("[you]" + msg + "\n");
			}
		}
	}

}
