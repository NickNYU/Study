package com.emc.ehc.nick.netty.Reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年4月23日 下午10:59:17 
* 
*/
public class Handler implements Runnable {
	
	private SocketChannel channel;
	private SelectionKey selectionKey;
	private ByteBuffer input = ByteBuffer.allocate(1024);
	
	private final int READ = 0, WRITE = 1;
	
	//初始状态  
    int state = READ;  
    String clientName = "";  

	public Handler(SocketChannel socketChannel, Selector selector) throws IOException {
		this.channel = socketChannel;
		selector = selector;
		
		this.channel.configureBlocking(false);
		selectionKey = this.channel.register(selector, SelectionKey.OP_READ);
		
		/* 
        handler作为SellectionKey的attachment。这样，handler就与SelectionKey也就是interestOps对应起来了 
        反过来说，当interestOps发生、SelectionKey被选中时，就能从SelectionKey中取得handler 
        */  
		selectionKey.attach(this);
		selectionKey.interestOps(SelectionKey.OP_READ);
		
		/**
		 * {@link Reactor} 中会通过调用 run 来真正触发操作*/
        selector.wakeup();
	}

	@Override
	/**
	 * 因为 Reactor 通过 dispatch来调用 runnable task， 而这里 通过 attach(this) 所以可以直接用这里的channel
	 * */
	public void run() {
		try {
			if (state == READ) {  
			    read();  
			} else if (state == WRITE) {  
			    write();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}
	
	public void read() throws IOException {
		int readNumber = channel.read(input);
		if(readNumber > 0) {
			proccessRead(readNumber);
		}
		state = WRITE;
		selectionKey.interestOps(SelectionKey.OP_WRITE);
	}
	
	public void write() throws IOException {
		System.out.println("Saying hello to " + clientName);  
        ByteBuffer output = ByteBuffer.wrap(("Hello " + clientName + "\n").getBytes());  
        channel.write(output);  
        selectionKey.interestOps(SelectionKey.OP_READ);  
        state = READ;
	}
	
	public synchronized void proccessRead(int num) {
		StringBuilder sb = new StringBuilder();  
        input.flip();   //from writing mode to reading mode  
        byte[] subStringBytes = new byte[num];  
        byte[] array = input.array();  
        System.arraycopy(array, 0, subStringBytes, 0, num);  
        // Assuming ASCII (bad assumption but simplifies the example)  
        sb.append(new String(subStringBytes));  
        input.clear();  
        
        clientName = sb.toString().trim(); 
	}

}
