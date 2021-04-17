package com.risetek;

public class Bootstrap {

	public static void main(String[] args) {
		System.out.println("Hello");
		Runtime.getRuntime().addShutdownHook(new Thread() {
		    public void run() {
		    	try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		    	
		    	System.out.println("JVM Shutdown");
		    }
		 });

		try {
			Thread.currentThread().join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}
