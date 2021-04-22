package com.risetek;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.risetek.nats.NatsListener;
import com.risetek.storage.DebugEngine;
import com.risetek.storage.IEngine;
import com.risetek.storage.TDEngine;

public class Bootstrap {

	public static void main(String[] args) {
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

		boolean isDebug = false;
		for(String arg:args) {
			if("debug".equals(arg))
				isDebug = true;
		}

		Injector injector = Guice.createInjector(new myModule(isDebug));
		injector.getInstance(NatsListener.class).start();

		try {
			Thread.currentThread().join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	private static class myModule extends AbstractModule {
		private final boolean isdebug;
		public myModule(boolean isdebug) {
			this.isdebug = isdebug;
		}

		@Override
		protected void configure() {
			if(isdebug)
				bind(IEngine.class).to(DebugEngine.class).asEagerSingleton();
			else
				bind(IEngine.class).to(TDEngine.class).asEagerSingleton();
			bind(NatsListener.class);
		}
	}
}
