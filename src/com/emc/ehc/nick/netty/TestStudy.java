package com.emc.ehc.nick.netty;

import org.junit.*;

public class TestStudy {

	@Test
	public void testSystemProperty() {
		//回车距离 系统自带property
		int length = System.getProperty("line.separator").length();
		System.out.println(length);
	}
	
	@Test
	public void testSystemPropertyPort() {
		String port = System.getProperty("port", "80809080898");
		
		System.out.println(port);
	}
}
