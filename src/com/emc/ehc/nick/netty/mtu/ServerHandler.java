package com.emc.ehc.nick.netty.mtu;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年4月25日 下午10:19:05 
* 
*/
public class ServerHandler extends SimpleChannelInboundHandler {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println(msg.toString());
		String message = ((ByteBuf) msg).toString(CharsetUtil.UTF_8);
        System.out.println("[server]ServerHandler:" + message.length());
        //输出到文件，方便观察。打印到console时，可能看到的是一片空白，但实际上是有输出的
        //FileUtils.writeStringToFile(new File("C:/Users/lijinnan/Desktop/tmp.txt"), msg, CharsetUtil.UTF_8);
        System.out.println("[server]ServerHandler:finished");
	}

}
