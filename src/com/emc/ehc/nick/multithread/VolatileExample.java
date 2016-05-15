package com.emc.ehc.nick.multithread;
/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年5月15日 下午11:35:01 
* 
*/
public class VolatileExample implements Runnable {
	
	private static boolean flag = false;
	
	@Override
	public void run() {
		System.out.println("VolatileExample Thread started");
		while(!flag) {
			
		}
		System.out.println("Out of loop");
	}
	
	public static void main(String[] args) {
		System.out.println("Main start");
		
		new Thread(new VolatileExample()).start();
		
		VolatileExample.flag = true;
		
		System.out.println("Main stops");
	}
}
