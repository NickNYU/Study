package com.emc.ehc.nick.NIO.ChatRoom.netty;

import java.nio.channels.SelectionKey;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Iterator;

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
public class ChatServerHandler extends SimpleChannelInboundHandler<String> {
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	
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
				System.out.println("[" + incoming.remoteAddress() + "]" + msg + "\n");
				readMsg(ctx, msg);
			} else {
				channel.writeAndFlush("[you]" + msg + "\n");
			}
		}
	}

	private void readMsg(ChannelHandlerContext ctx, String str) {
		if (str.indexOf("open_") != -1) {//客户端连接服务器
			String name = str.substring(5);
			System.out.println(name + " online");
		} else if (str.indexOf("exit_") != -1) {// 客户端发送退出命令
			String userName = str.substring(5);
			System.out.println(userName + " offline");
		} else {// 读取客户端消息
			String uname = str.substring(0, str.indexOf("^"));
			String message = str.substring(str.indexOf("^") + 1);
			System.out.println("("+uname+")说：" + message);
			String dateTime = sdf.format(new Date(System.currentTimeMillis()));
			String smsg = uname + " " + dateTime + "\n  " + message + "\n";
			Channel incoming = ctx.channel();
			for(Channel channel : channels) {
				if(incoming != channel) {
					channel.writeAndFlush(smsg);
				} else {
					channel.writeAndFlush("[you]" + smsg);
				}
			}
		}
	}

}
