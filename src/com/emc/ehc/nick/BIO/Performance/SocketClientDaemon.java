package com.emc.ehc.nick.BIO.Performance;

import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;

/**
 * @author NickNYU
 * @email  cz739@nyu.edu
 */
public class SocketClientDaemon {
	
	private static final Logger log = Logger.getLogger(SocketClientDaemon.class);
	
	public static void main(String[] args) throws Exception {
        Integer clientNumber = 20;
        CountDownLatch countDownLatch = new CountDownLatch(clientNumber);

        //分别开始启动这20个客户端
        for(int index = 0 ; index < clientNumber ; index++ , countDownLatch.countDown()) {
            SocketClientRequestThread client = new SocketClientRequestThread(countDownLatch, index);
            new Thread(client).start();
        }

        //这个wait不涉及到具体的实验逻辑，只是为了保证守护线程在启动所有线程后，进入等待状态
        synchronized (SocketClientDaemon.class) {
            SocketClientDaemon.class.wait();
        }
    }
}
