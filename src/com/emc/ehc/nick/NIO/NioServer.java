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
				// ���δ���selector�ϵ�ÿ����ѡ���SelectionKey
				try {
					for(SelectionKey key : selector.selectedKeys()) {
						// ��selector�ϵ���ѡ��Key����ɾ�����ڴ����SelectionKey  
                        selector.selectedKeys().remove(key);
                        // ���key��Ӧ��ͨ�������ͻ��˵���������
                        if(key.isAcceptable()) {
                        	// ����accept�����������ӣ������������˶�Ӧ��SocketChannel  
                        	SocketChannel channel = serverSocketChannel.accept();
                        	channel.configureBlocking(false);
                        	// ����SocketChannelҲע�ᵽselector  
                        	channel.register(selector, SelectionKey.OP_READ);
                        	// ��key��Ӧ��Channel���ó�׼��������������
                        	key.interestOps(SelectionKey.OP_ACCEPT);
                        }
                        // ���key��Ӧ��ͨ����������Ҫ��ȡ
                        if(key.isReadable()) {
                        	// ��ȡ��SelectionKey��Ӧ��Channel����Channel���пɶ�������
                        	SocketChannel channel = (SocketChannel) key.channel();
                        	// ����׼��ִ�ж�ȡ���ݵ�ByteBuffer  
                            ByteBuffer buff = ByteBuffer.allocate(1024);  
                            // ��ʼ��ȡ����
                            int len = 0;
                            try {
                            	if((len = channel.read(buff)) > 0) {
                            		buff.flip();// ���� 2ָ�븴λ ׼���´ζ�ȡ����
                            		System.out.println("��ȡ����:" + buff.array());  
                                    key.interestOps(SelectionKey.OP_READ);
                            	} else {  
                                    System.out.println("û�����ݶ�ȡ,�ر�ͨ��");  
                                    key.cancel();  
                                    if (key.channel() != null) {  
                                    	key.channel().close();  
                                    }  
                                }  
                            } catch(Exception e) {
                            	// �����׽����key��Ӧ��Channel�������쳣����������Channel  
                                // ��Ӧ��Client���������⣬���Դ�Selector��ȡ��sk��ע�� 
                            	System.err.println(e.getMessage());
								key.cancel();  
								if (key.channel() != null) {  
									key.channel().close();  
								}  
								System.out.println("�ر�һ���ͻ���"); 
                            }
                            
                         // ���content�ĳ��ȴ���0����������Ϣ��Ϊ��  
                            if (len > 0) {  
                                // ������selector��ע�������SelectKey  
                                SelectionKey writeKey = null;  
                                for (SelectionKey selectKey : selector.keys()) {  
                                    // ��ȡ��key��Ӧ��Channel  
  
                                    Channel targetChannel = selectKey.channel();  
                                    // �����channel��SocketChannel����  
                                    if (targetChannel instanceof SocketChannel) {  
                                        // && key != sk) {  
                                        // ������������д���Channel�� ,���ص��ͻ���  
                                        if (targetChannel instanceof SocketChannel  
                                                && selectKey != key) {  
                                            SocketChannel dest = null;  
                                            try {  
                                                // ������������д���Channel�� ,���ص��ͻ���  
                                                dest = (SocketChannel) targetChannel;  
                                                System.out.println("д����:"  
                                                        + buff.array());  
                                                dest.write(buff);  
                                            } catch (Exception e) {  
                                                // д�쳣,�ر�ͨ��  
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
        System.out.println("Nio�����������,host:" + host);  
    } 
}
