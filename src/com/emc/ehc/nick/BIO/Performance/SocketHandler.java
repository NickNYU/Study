package com.emc.ehc.nick.BIO.Performance;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.log4j.Logger;

/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年4月4日 下午10:55:58 
* 
*/
public class SocketHandler implements Runnable {
	
	private final static Logger log = Logger.getLogger(SocketHandler.class);
	
	private Socket socket;
	
	public SocketHandler(Socket s) {
		this.socket = s;
	}
	
	@Override
	public void run() {
		//下面我们收取信息
        InputStream in = null;
        OutputStream out = null; 
		try {
			in = socket.getInputStream();
			out = socket.getOutputStream();
	        Integer sourcePort = socket.getPort();
	        
	        int maxLen = 2048;
	        byte[] contextBytes = new byte[maxLen];
	        //这里也会被阻塞，直到有数据准备好
	        int realLen = in.read(contextBytes, 0, maxLen);
	        //读取信息
	        String message = new String(contextBytes , 0 , realLen);

	        //下面打印信息
	        SocketHandler.log.info("服务器收到来自于端口：" + sourcePort + "的信息：" + message);

	        //下面开始发送信息
	        out.write("Server 回发响应信息！".getBytes());
		} catch (IOException e) {
			SocketHandler.log.error(e.getMessage(), e);
		} finally {
			try {
		        //关闭
		        out.close();
		        in.close();
		        socket.close();
			} catch(Exception e) {
				SocketHandler.log.error(e.getMessage(), e);
			}
		}
	}

}
