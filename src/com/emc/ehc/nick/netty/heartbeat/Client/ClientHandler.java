package com.emc.ehc.nick.netty.heartbeat.Client;

import com.emc.ehc.nick.netty.heartbeat.share.AbstractMsg;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年4月19日 下午8:15:16 
* 
*/
public class ClientHandler extends SimpleChannelInboundHandler<AbstractMsg> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, AbstractMsg msg) throws Exception {
		
	}

}
