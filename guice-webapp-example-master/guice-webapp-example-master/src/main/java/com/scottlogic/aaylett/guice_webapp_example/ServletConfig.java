package com.scottlogic.aaylett.guice_webapp_example;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

public class ServletConfig extends GuiceServletContextListener {
    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new ServletModule() {
            @Override
            protected void configureServlets() {
                super.configureServlets();

                serve("/").with(TestServlet.class);
                
                bind(String.class).toInstance("Hello, World!");
            }
        });
    }
}
