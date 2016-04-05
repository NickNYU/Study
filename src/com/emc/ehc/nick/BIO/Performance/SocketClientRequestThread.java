package com.emc.ehc.nick.BIO.Performance;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;

/** 
* @author Nick Zhu
* @email cz739@nyu.edu 
* @version 创建时间：2016年4月4日 下午10:15:56 
* 
*/
public class SocketClientRequestThread implements Runnable {
	
	private static final Logger log = Logger.getLogger(SocketClientRequestThread.class);
	
	private CountDownLatch countDownLatch;
	
	/*
	 * 这个线程的 countDownLatch Index*/
	private Integer clientIndex;
	
	/**
     * countDownLatch是java提供的同步计数器。
     * 当计数器数值减为0时，所有受其影响而等待的线程将会被激活。这样保证模拟并发请求的真实性
     * @param countDownLatch
     */
	public SocketClientRequestThread(CountDownLatch countDownLatch , Integer clientIndex) {
		this.countDownLatch = countDownLatch;
		this.clientIndex = clientIndex;
	}
	
	@Override
	public void run() {
		Socket socket = null;
        OutputStream clientRequest = null;
        InputStream clientResponse = null;
        try {
        	int port = 8090;
        	socket = new Socket("localhost", port);
        	//System.out.println("Socket has been created at port : " + port);
        	clientResponse = socket.getInputStream();
        	clientRequest = socket.getOutputStream();
        	
        	//等待，直到SocketClientDaemon完成所有线程的启动，然后所有线程一起发送请求
            this.countDownLatch.await();
            
            System.out.println("[Socket" + this.clientIndex + "] Socket has been created at port : " + port);
            //发送请求信息
            clientRequest.write(("这是第" + this.clientIndex + " 个客户端的请求。").getBytes());
            clientRequest.flush();
            
          //在这里等待，直到服务器返回信息
            SocketClientRequestThread.log.info("第" + this.clientIndex + "个客户端的请求发送完成，等待服务器返回信息");
            System.out.println("第" + this.clientIndex + "个客户端的请求发送完成，等待服务器返回信息");
            int maxLen = 1024;
            byte[] contextBytes = new byte[maxLen];
            int realLen;
            StringBuffer sb = new StringBuffer();
            //程序执行到这里，会一直等待服务器返回信息（注意，前提是in和out都不能close，如果close了就收不到服务器的反馈了）
            while((realLen = clientResponse.read(contextBytes, 0, maxLen)) != -1) {
                sb.append(new String(contextBytes , 0 , realLen));
            }
            String message = sb.toString();
            SocketClientRequestThread.log.info("接收到来自服务器的信息:" + message);
            System.out.println("接收到来自服务器的信息:" + message);
        } catch(Exception e) {
        	SocketClientRequestThread.log.error(e.getMessage(), e);
        } finally {
            try {
                if(clientRequest != null) {
                    clientRequest.close();
                }
                if(clientResponse != null) {
                    clientResponse.close();
                }
            } catch (Exception e) {
            	SocketClientRequestThread.log.error(e.getMessage(), e);
            }
        }
	}
}
