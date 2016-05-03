package com.emc.ehc.nick.netty.mtu;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;;

/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年4月25日 下午10:58:10 
 * @param <VoidEnum>
* 
*/
public class ServerReplyDecoder extends ReplayingDecoder {
	
	private boolean readLength = false;
    private int length;
    
    private static final AtomicInteger COUNT = new AtomicInteger(0);
    private static final AtomicInteger BYTES_COUNT = new AtomicInteger(0);
    
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
		if (!readLength) {
            length = buf.readInt();
            readLength = true;
            checkpoint();
        }

        if (readLength) {
            System.out.println("[server]messageReceived Count=" + COUNT);
            System.out.println("[server]messageReceived BYTES_COUNT=" + BYTES_COUNT);
            readLength = false;
            checkpoint();
        }

	}

	// fake push
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		int readableBytes = ((ByteBuf) msg).readableBytes();
        
        int time = COUNT.addAndGet(1);
        BYTES_COUNT.addAndGet(readableBytes);
        System.out.println("[server]messageReceived-" + time + ":" + readableBytes);
        
        super.channelRead(ctx, msg);
	}

}
