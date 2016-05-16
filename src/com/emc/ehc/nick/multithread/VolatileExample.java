package com.emc.ehc.nick.multithread;
/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年5月15日 下午11:35:01 
* 
*/
public class VolatileExample extends Thread {
	
	private static boolean flag = false;
	
	@Override
	public void run() {
		System.out.println("VolatileExample Thread started");
		while(!flag) {
			
		}
		System.out.println("Out of loop");
	}
	
	public static void main(String[] args) throws InterruptedException {
		//System.out.println("Main start");
		
		new VolatileExample().start();
		//sleep的目的是等待线程启动完毕,也就是说进入run的无限循环体了
        Thread.sleep(100);
        
		VolatileExample.flag = true;
		
		System.out.println("Main stops");
	}
}
