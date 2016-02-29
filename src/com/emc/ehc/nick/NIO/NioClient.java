/**
 * 
 */
package com.emc.ehc.nick.NIO;

import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * @author NickNYU
 *
 */
public class NioClient {
	
	private class SendDataThread extends Thread {
		SocketChannel socketChannel;
		Selector selector;
		
		public SendDataThread(SocketChannel sc,Selector selector){  
            this.socketChannel = sc;  
            this.selector = selector;  
        }
	}
}
