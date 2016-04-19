package com.emc.ehc.nick.netty.heartbeat.Server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;

import com.emc.ehc.nick.netty.heartbeat.share.AbstractMsg;
import com.emc.ehc.nick.netty.heartbeat.share.LoginMsg;
import com.emc.ehc.nick.netty.heartbeat.share.MsgType;
/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年4月18日 下午9:45:23 
* 
*/
public class ServerHandler extends SimpleChannelInboundHandler<AbstractMsg> {
	Log logger = LogFactory.getLog(ServerHandler.class);
	
	private NettyChannelMap map = new NettyChannelMap();
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		map.remove(ctx.channel());
		logger.info("Channel : " + ctx.channel().localAddress() + " has been removed");
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, AbstractMsg msg) throws Exception {
		//如果客户端还没有登录，发送登录需求
		if(msg.getClientId() == null) {
			AbstractMsg loginMsg = new LoginMsg();
			ctx.channel().writeAndFlush(loginMsg);
		} else {
			// 回调机制，由message本身来完成操作
			msg.dealWithServerMessage(ctx);
		}
	}
	
}
