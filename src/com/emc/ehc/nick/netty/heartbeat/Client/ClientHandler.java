package com.emc.ehc.nick.netty.heartbeat.Client;

import com.emc.ehc.nick.netty.heartbeat.share.AbstractMsg;
import com.emc.ehc.nick.netty.heartbeat.share.PingMsg;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年4月19日 下午8:15:16 
* 
*/
public class ClientHandler extends SimpleChannelInboundHandler<AbstractMsg> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, AbstractMsg msg) throws Exception {
		msg.dealWithClientMessage(ctx);
	}
	
	@Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case WRITER_IDLE:
                    PingMsg pingMsg = new PingMsg();
                    ctx.writeAndFlush(pingMsg);
                    System.out.println("send ping to server----------");
                    break;
                default:
                    break;
            }
        }
    }
}
