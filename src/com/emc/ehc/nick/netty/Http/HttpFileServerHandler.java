package com.emc.ehc.nick.netty.Http;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

public class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
	
	private final String url;
	public HttpFileServerHandler(String url) {
		this.url = url;
	}
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
