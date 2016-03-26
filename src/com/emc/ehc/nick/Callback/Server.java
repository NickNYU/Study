package com.emc.ehc.nick.Callback;

public class Server {
	
	public void process(Callback caller, String msg) {
		System.out.println("[Server" + msg + "] Recieves message : " + msg);
		String feedBack = null;
		if(msg == null || msg.trim().isEmpty()) {
			feedBack = "[Server" + msg + "] Null Message got from Server";
		} else {
			feedBack = "[Server" + msg + "] has got message";
		}
		try {
			Thread.sleep(3000);
			System.out.println(feedBack);
		} catch(Exception e) {
			System.err.println(e.getMessage());
		}
		caller.call(feedBack);
	}
}
