package com.emc.ehc.nick.netty;

import org.junit.*;

public class TestStudy {

	@Test
	public void testSystemProperty() {
		//回车距离 系统自带property
		int length = System.getProperty("line.separator").length();
		System.out.println(length);
	}
}
