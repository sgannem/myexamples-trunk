package com.xyz;

import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.servlet.ServletModule;

public class GuiceConfigurationModule extends ServletModule {
	
	 @Override
	 protected void configureServlets() {
		 installFactories();
         super.configureServlets();
         serve("/").with(TestServlet.class);
         serve("/download").with(FileDownloadServlet.class);
         serve("/student").with(StudentServlet.class);
//         bind(String.class).toInstance("Hello, World!");
         bind(String.class).toInstance("Appstore distribution application is up and running...");
         bind(IUtil.class).to(Util.class);
     }
	 
	 private void installFactories() {
		    install(new FactoryModuleBuilder().build(StudentFactory.class));
//		    install(new FactoryModuleBuilder().implement(MessageHandler.class, MessageHandler.class).build(MessageHandlerFactory.class));
//		    install(new FactoryModuleBuilder().build(RequestHandlerFactory.class));
//		    install(new FactoryModuleBuilder().implement(RequestHandlerRunner.class, AsyncRequestHandlerRunner.class).build
//		        (RequestHandlerRunnerFactory.class));
		  }
}
