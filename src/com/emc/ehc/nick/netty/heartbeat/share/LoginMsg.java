package com.emc.ehc.nick.netty.heartbeat.share;

import com.emc.ehc.nick.netty.heartbeat.Server.NettyChannelMap;

import io.netty.channel.ChannelHandlerContext;

/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年4月17日 下午10:45:26 
* 
*/
public class LoginMsg extends AbstractMsg {
	
	public LoginMsg() {
		super();
		setType(MsgType.LOGIN);
	}
	
	private String username;
	private String password;
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public boolean dealWithMessage(ChannelHandlerContext ctx) {
		try {
			NettyChannelMap.add(this.getClientId(), ctx.channel());
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	
}
