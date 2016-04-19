package com.emc.ehc.nick.netty.heartbeat.share;

import com.emc.ehc.nick.netty.heartbeat.Server.NettyChannelMap;

import io.netty.channel.ChannelHandlerContext;

/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年4月17日 下午10:52:55 
* 
*/
public class PingMsg extends AbstractMsg {
	public PingMsg() {
		super();
		setType(MsgType.PING);
	}

	@Override
	public boolean dealWithServerMessage(ChannelHandlerContext ctx) {
		PingMsg replyPing = new PingMsg();
        NettyChannelMap.get(this.getClientId()).writeAndFlush(replyPing);
		return true;
	}
}
