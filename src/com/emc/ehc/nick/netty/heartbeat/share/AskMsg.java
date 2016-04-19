package com.emc.ehc.nick.netty.heartbeat.share;

import com.emc.ehc.nick.netty.heartbeat.Server.NettyChannelMap;

import io.netty.channel.ChannelHandlerContext;

/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年4月17日 下午10:54:18 
* 
*/
public class AskMsg extends AbstractMsg {
	public AskMsg() {
		super();
		setType(MsgType.ASK);
	}
	
	private AskParams params;

    public AskParams getParams() {
        return params;
    }

    public void setParams(AskParams params) {
        this.params = params;
    }

	@Override
	public boolean dealWithServerMessage(ChannelHandlerContext ctx) {
		//收到客户端的请求
		try {
	        if("authToken".equals(this.getParams().getAuth())){
	            ServerReplyBody replyBody=new ServerReplyBody("[server info] " + ctx.name() + "!!!");
	            ReplyMsg replyMsg=new ReplyMsg();
	            replyMsg.setBody(replyBody);
	            NettyChannelMap.get(this.getClientId()).writeAndFlush(replyMsg);
	        }
	        return true;
		} catch(Exception e) {
			return false;
		}
	}
}
