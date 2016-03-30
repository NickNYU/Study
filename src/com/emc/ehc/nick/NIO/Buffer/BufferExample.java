package com.emc.ehc.nick.NIO.Buffer;

import java.nio.Buffer;
import java.nio.CharBuffer;

public class BufferExample {
	public static void main(String[] args) {
		CharBuffer buffer = CharBuffer.allocate(100);
		while(fillBuffer(buffer)) {
			buffer.flip();
			readBuffer(buffer);
			buffer.clear();
		}
	}
	
	private static boolean fillBuffer(CharBuffer buffer) {
		if(index >= strings.length)
			return false;
		
		String msg = strings[index++];
		
		for(int i = 0; i < msg.length(); i++) {
			buffer.put(msg.charAt(i));
		}
		
		return true;
	}
	
	private static void readBuffer(CharBuffer buffer) {
		while(buffer.hasRemaining()) {
			System.out.print(buffer.get());
		}
		System.out.println("");
	}
	
	private static int index = 0; 
	
	private static String [] strings = { "A random string value", 
				"The product of an infinite number of monkeys", 
				"Hey hey we're the Monkees", 
				"Opening act for the Monkees: Jimi Hendrix", 
				"'Scuse me while I kiss this fly", 
				"Help Me! Help Me!" };
}
