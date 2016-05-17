package com.emc.ehc.nick.netty.analysis;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年5月17日 下午10:47:02 
* 
*/
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

public class NettyExcutorHandler extends ChannelInitializer<SocketChannel> {

	private static final EventExecutorGroup group = new DefaultEventExecutorGroup(10);
	
	@Override
	protected void initChannel(SocketChannel channel) throws Exception {
		ChannelPipeline pipeline = channel.pipeline();
		pipeline.addLast(new LoggingHandler());
		pipeline.addLast(group, "excutorHandler", new StringDecoder());
	}
	
}
