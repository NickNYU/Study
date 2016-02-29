package com.emc.ehc.nick.Socket;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class URLConnectionStudy {
	public static void main(String[] args) {
		URLConnection conn = null;
		try {
			URL url = new URL("http://www.baidu.com");
			conn = url.openConnection();
			InputStream input = conn.getInputStream();
			
			int data = input.read();
			while(data != -1){
			    System.out.print((char) data);
			    data = input.read();
			}
			input.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
}
