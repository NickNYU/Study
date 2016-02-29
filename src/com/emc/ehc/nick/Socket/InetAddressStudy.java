package com.emc.ehc.nick.Socket;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressStudy {
	public static void main(String[] args) {
		String hostName = "www.google.com";
		try {
			InetAddress[] addresses = InetAddress.getAllByName(hostName);
			System.out.println(InetAddress.getLocalHost().getHostAddress());
			System.out.println(InetAddress.getLoopbackAddress().getHostName());
			System.out.println(InetAddress.getLocalHost().getCanonicalHostName());
			for(InetAddress address : addresses)
				System.out.println(address.getHostName());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}
