package com.emc.ehc.nick.multithread;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/** 
* @author Nick Zhu
* @email  cz739@nyu.edu 
* @version 创建时间：2016年5月16日 下午10:48:06 
* 
*/

public class FutureExample {
	
	// Call back function
	class MyCallable implements Callable<String> {
		
		private String task;
		
		public MyCallable(String task) {
			this.task = task;
		}
		
		@Override
		public String call() throws Exception {
			System.out.println(">>>" + task + "任务启动");  
		    Date dateTmp1 = new Date();  
		    Thread.sleep(1000);  
		    Date dateTmp2 = new Date();  
		    long time = dateTmp2.getTime() - dateTmp1.getTime();  
		    System.out.println(">>>" + task + "任务终止");  
		    return task + "任务返回运行结果,当前任务时间【" + time + "毫秒】";
		}
		
	}

	
	//@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		System.out.println("----程序开始运行----");  
		Date date1 = new Date();  
		  
		int taskSize = 5;  
		// 创建一个线程池  
		ExecutorService pool = Executors.newFixedThreadPool(taskSize);
		List<Future<String>> futures = new ArrayList<Future<String>> ();
		for(int i = 0; i < taskSize; i++) {
			String taskNum = i + "";
			//Callable<Object> c = new MyCallable(taskNum);  
		    // 执行任务并获取Future对象  
			Callable<String> c = new FutureExample().new MyCallable(taskNum);
		    Future<String> f = pool.submit(c);   
		    futures.add(f);
		}
		pool.shutdown();
		
		// 获取所有并发任务的运行结果  
	   for (Future<String> f : futures) {  
	    // 从Future对象上获取任务的返回值，并输出到控制台  
	    System.out.println(">>>" + f.get());  
	   }  
		  
	   Date date2 = new Date();  
	   System.out.println("----程序结束运行----，程序运行时间【"  
	     + (date2.getTime() - date1.getTime()) + "毫秒】");
	}
}
