package com.emc.ehc.nick.netty;

import java.net.SocketAddress;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

public class TimeClientHandler implements ChannelHandler {
	
	private static final Logger logger = Logger.getLogger(TimeClientHandler.class.getName());
	private ByteBuf firstMessage = null;
	
	public TimeClientHandler() {
		byte[] req = "QUERY TIME ORDER".getBytes();
		this.firstMessage = Unpooled.buffer(req.length);
		this.firstMessage.writeBytes(req);
	}
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub

	}

	

}
