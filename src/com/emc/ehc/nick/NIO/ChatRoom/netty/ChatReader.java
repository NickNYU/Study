package com.emc.ehc.nick.NIO.ChatRoom.netty;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.util.AttributeKey;
/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年4月9日 下午11:27:05 
* 
*/
@Sharable
public class ChatReader extends ChannelInboundHandlerAdapter {
	
	private static AttributeKey<List<String>> users = AttributeKey.valueOf("users");
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/* (non-Javadoc)
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelRead(io.netty.channel.ChannelHandlerContext, java.lang.Object)
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String str = (String) msg;
		if(str.indexOf("open_") != -1) {
			String name = str.substring("open_".length());
			printInfo(name + " online");
			List<String> userList = ctx.attr(ChatReader.users).get();
			userList.add(name);
		} else if(str.indexOf("exit_") != -1) {
			String name = str.substring("open_".length());
			printInfo(name + " online");
			List<String> userList = ctx.attr(ChatReader.users).get();
			userList.remove(name);
			printInfo(name + " offline");
		} else {
			String uname = str.substring(0, str.indexOf("^"));
			String message = str.substring(str.indexOf("^") + 1);
			printInfo("("+uname+")说：" + msg);
			String dateTime = sdf.format(new Date());
			String smsg = uname + " " + dateTime + "\n  " + msg + "\n";
		}
	}

	/* (non-Javadoc)
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelReadComplete(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelReadComplete(ctx);
	}
	
	private void printInfo(String str) {
		System.out.println(str);
	}
}
