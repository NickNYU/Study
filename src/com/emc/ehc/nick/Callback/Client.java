package com.emc.ehc.nick.Callback;

public class Client implements Callback {
	@Override
	public void call(String msg) {
		System.out.println("[Client" + msg + "] Recieve message : " + msg);
	}
	
	private Server server;
	
	public void connectServer(String msg) {
		System.out.println("[Client" + msg + "] Start Connecting the Server");
		new Thread(new Runnable(){
			@Override
			public void run() {
				server = new Server();
				// 坑，一定是Client.this,否则会默认为这个Runnable的this
				server.process(Client.this, msg);
			}
		}).start();
		System.out.println("[Client" + msg + "] Close Connect");
	}
	
	public static void main(String[] args) {
		Client client = new Client();
		for(int i = 0; i < 10; i++) {
			client.connectServer(i+" ");
		}
	}
}
