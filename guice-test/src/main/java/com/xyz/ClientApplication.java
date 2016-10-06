package com.xyz;

import com.google.inject.Guice;
import com.google.inject.Injector;
 
 
public class ClientApplication {
 
    public static void main(String[] args) {
//        Injector injector = Guice.createInjector(new AppInjector());        
    	Injector injector = Guice.createInjector(new AdminMarketGuiceModule()); 
        MyApplication app = injector.getInstance(MyApplication.class);
         
        app.sendMessage("Hi Pankaj", "pankaj@abc.com");
    }
 
}
