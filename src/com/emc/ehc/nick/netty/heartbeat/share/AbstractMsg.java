package com.emc.ehc.nick.netty.heartbeat.share;

import java.io.Serializable;

import io.netty.channel.ChannelHandlerContext;

/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年4月17日 下午10:37:36 
* 
*/
public abstract class AbstractMsg implements Serializable {
	private final static long serialVersionUID = 1L;
	private MsgType msgType;
	// unique id
	private String clientID;
	
	public AbstractMsg() {
		this.clientID = Constants.getClientID();
	}
	
	public String getClientId() {
        return clientID;
    }

    public void setClientId(String clientId) {
        this.clientID = clientId;
    }

    public MsgType getType() {
        return msgType;
    }

    public void setType(MsgType type) {
        this.msgType = type;
    }
    
    public abstract boolean dealWithServerMessage(ChannelHandlerContext ctx);
    public abstract boolean dealWithClientMessage(ChannelHandlerContext ctx);
}
