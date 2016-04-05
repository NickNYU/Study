package com.emc.ehc.nick.BIO.Performance;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年4月4日 下午10:37:20 
* 
*/
public class SocketServerMultiThread {
	
	private static final Logger log = Logger.getLogger(SocketServerMultiThread.class);

	public static void main(String[] args) {
		new SocketServerMultiThread().startServer();
	}
	public void startServer() {
		ServerSocket server = null;
//		ExecutorService executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), 
//				50, 
//				120L, 
//				TimeUnit.SECONDS,
//				new ArrayBlockingQueue(10));
		try {
			int port = 8090;
			server = new ServerSocket(port);
			System.out.println("Server started at port" + port);
			while(true) {
			
				Socket socket = server.accept();
				System.out.println("[Server] recieve socket");
				new Thread(new SocketHandler(socket)).start();
				//executor.execute(new SocketHandler(socket));
			
			}
		} catch(Exception e) {
			SocketServerMultiThread.log.error(e.getMessage(), e);
		} finally {
			if(server != null) {
				try {
					server.close();
				} catch (IOException e) {
					SocketServerMultiThread.log.error(e.getMessage(), e);
				}
			}
		}
	}
}	
