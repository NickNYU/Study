package com.emc.ehc.nick.Socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import com.emc.ehc.nick.Socket.Utilities.*;

public class SocketServer implements Runnable {
	
	private SocketServer(int port) {
		System.out.println("Create one instance for Server");
		this.port = port;
	}
	
	private SocketServer() {
		System.out.println("Create one instance for Server");
	}
	
	public static class ServerFactory {
		private static SocketServer socketServer = new SocketServer();
	}
	
	public SocketServer getServer() {
		return ServerFactory.socketServer;
	}
	
	@Override
	public void run() {
		openServerSocket();
		ThreadPool<SocketWrapper> threadPool = new ThreadPool<SocketWrapper>(3);
		threadPool.run();
		while(!this.isStopped) {
			Socket socket = null;
			try {
				socket = this.serverSocket.accept();
				SocketWrapper wrap = new SocketWrapper(socket);
				while(!threadPool.isStopped() && threadPool.isFull()) {
					this.wait();
				}
				threadPool.put(wrap);
			} catch(IOException | InterruptedException e) {
				if(isStopped) {
                    System.out.println("Server Stopped.") ;
                    return;
                }
                throw new RuntimeException(
                    "Error accepting client connection", e);
			} finally {
				threadPool.close();
			}
			
		}
	}
	
	private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.port);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port 80", e);
        }
    }
	
	public synchronized void stop(){
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }
	
	private int port = 80;
	
	private ServerSocket serverSocket = null;
	
	private boolean isStopped = false;
}
