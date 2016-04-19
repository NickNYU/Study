package com.emc.ehc.nick.netty.heartbeat.share;

import io.netty.channel.ChannelHandlerContext;

/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年4月17日 下午10:57:23 
* 
*/
public class ReplyMsg extends AbstractMsg {
	public ReplyMsg() {
		super();
		setType(MsgType.REPLY);
	}
	
	private ReplyBody body;

    public ReplyBody getBody() {
        return body;
    }

    public void setBody(ReplyBody body) {
        this.body = body;
    }

	@Override
	public boolean dealWithServerMessage(ChannelHandlerContext ctx) {
		ClientReplyBody clientBody = (ClientReplyBody) this.getBody();
        System.out.println("receive client msg: " + clientBody.getClientInfo());
		return true;
	}
}
