package com.emc.ehc.nick.NIO;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NioServer {
	
	private class Server implements Runnable {
		private String ip;
		private int port;
		
		public Server(String ip, int port) {
			this.ip = ip;
			this.port = port;
		}
		
		public Server(int port) {
			this.ip = "localhost";
			this.port = port;
		}
		
		@Override
		public void run() {
			try {
				init(this.ip, this.port);
			} catch(UnknownHostException e) {
				System.err.println(e.getMessage());
			} catch(IOException ioException) {
				System.err.println(ioException.getMessage());
			}
		}
	}
	
	private void init(String ip, int port) throws IOException, UnknownHostException{
		System.out.println("Open connection port : " + port);
		
		Selector selector = Selector.open();
		
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		
		SocketAddress address = new InetSocketAddress(ip, port);
		
		serverSocketChannel.bind(address);
		
		serverSocketChannel.configureBlocking(false);
		
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		
		while(true) {
			int keys = selector.select();
			
			if(keys > 0) {
				// 依次处理selector上的每个已选择的SelectionKey
				try {
					for(SelectionKey key : selector.selectedKeys()) {
						// 从selector上的已选择Key集中删除正在处理的SelectionKey  
                        selector.selectedKeys().remove(key);
                        // 如果key对应的通道包含客户端的连接请求
                        if(key.isAcceptable()) {
                        	// 调用accept方法接受连接，产生服务器端对应的SocketChannel  
                        	SocketChannel channel = serverSocketChannel.accept();
                        	channel.configureBlocking(false);
                        	// 将该SocketChannel也注册到selector  
                        	channel.register(selector, SelectionKey.OP_READ);
                        	// 将key对应的Channel设置成准备接受其他请求
                        	key.interestOps(SelectionKey.OP_ACCEPT);
                        }
                        // 如果key对应的通道有数据需要读取
                        if(key.isReadable()) {
                        	// 获取该SelectionKey对应的Channel，该Channel中有可读的数据
                        	SocketChannel channel = (SocketChannel) key.channel();
                        	// 定义准备执行读取数据的ByteBuffer  
                            ByteBuffer buff = ByteBuffer.allocate(1024);  
                            // 开始读取数据
                            int len = 0;
                            try {
                            	if((len = channel.read(buff)) > 0) {
                            		buff.flip();// 缓存 2指针复位 准备下次读取数据
                            		System.out.println("读取数据:" + buff.array());  
                                    key.interestOps(SelectionKey.OP_READ);
                            	} else {  
                                    System.out.println("没有数据读取,关闭通道");  
                                    key.cancel();  
                                    if (key.channel() != null) {  
                                    	key.channel().close();  
                                    }  
                                }  
                            } catch(Exception e) {
                            	// 如果捕捉到该key对应的Channel出现了异常，即表明该Channel  
                                // 对应的Client出现了问题，所以从Selector中取消sk的注册 
                            	System.err.println(e.getMessage());
								key.cancel();  
								if (key.channel() != null) {  
									key.channel().close();  
								}  
								System.out.println("关闭一个客户端"); 
                            }
                            
                         // 如果content的长度大于0，即聊天信息不为空  
                            if (len > 0) {  
                                // 遍历该selector里注册的所有SelectKey  
                                SelectionKey writeKey = null;  
                                for (SelectionKey selectKey : selector.keys()) {  
                                    // 获取该key对应的Channel  
  
                                    Channel targetChannel = selectKey.channel();  
                                    // 如果该channel是SocketChannel对象  
                                    if (targetChannel instanceof SocketChannel) {  
                                        // && key != sk) {  
                                        // 将读到的内容写入该Channel中 ,返回到客户端  
                                        if (targetChannel instanceof SocketChannel  
                                                && selectKey != key) {  
                                            SocketChannel dest = null;  
                                            try {  
                                                // 将读到的内容写入该Channel中 ,返回到客户端  
                                                dest = (SocketChannel) targetChannel;  
                                                System.out.println("写数据:"  
                                                        + buff.array());  
                                                dest.write(buff);  
                                            } catch (Exception e) {  
                                                // 写异常,关闭通道  
                                                e.printStackTrace();  
                                                if (dest != null) {  
                                                    dest.close();  
                                                }  
                                                targetChannel.close();  
                                                writeKey = selectKey;  
                                            }  
                                        }  
                                    }  
                                }  
                                if (writeKey != null) {  
                                    writeKey.cancel();  
                                    if (writeKey.channel() != null) {  
                                        writeKey.channel().close();  
                                    }  
                                }  
                            }  
                        }
					}
				} catch(Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}
	}
	
	
	public static void main(String[] args) throws IOException {  
        // System.out.println(InetAddress.getLocalHost().getHostAddress());  
        String host = InetAddress.getLocalHost().getHostAddress();  
        int port = 9090;  
        new NioServer().new Server(host, port).run();
        // new NServer().init(InetAddress.getLocalHost().getHostAddress(),  
        // 30000);  
        System.out.println("Nio服务端启动了,host:" + host);  
    } 
}
