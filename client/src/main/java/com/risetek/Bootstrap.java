package com.risetek;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.risetek.nats.NatsListener;
import com.risetek.storage.TDEngine;

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

		Injector injector = Guice.createInjector(
			new AbstractModule() {
				@Override
				protected void configure() {
					bind(TDEngine.class);
					bind(NatsListener.class);
				}
			}
        );
		injector.getInstance(NatsListener.class).start();

		try {
			Thread.currentThread().join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}
