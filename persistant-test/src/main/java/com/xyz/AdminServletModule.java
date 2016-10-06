package com.xyz;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.JerseyServletModule; // NOSONAR package com.sun.* is ok here
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer; // NOSONAR package com.sun.* is ok here

/**
 * A {@link ServletModule} that sets up Guice and Jersey.
 *
 * @author Alexander Zapletal (alexander.zapletal@rise-world.com)
 */
public class AdminServletModule extends JerseyServletModule {

  @Override
  protected void configureServlets() {
    super.configureServlets();
    System.out.println("###configureServlets() method is called...");
    install(new AdminMarketGuiceModule());

    install(new JpaPersistModule("appstore"));
    filter("/*").through(SilentPersistFilter.class);

    install(new JsonGuiceModule());

    final Map<String, String> map = new HashMap<String, String>();
    map.put("com.sun.jersey.spi.container.ResourceFilters", "com.sun.jersey.api.container.filter.RolesAllowedResourceFilterFactory");
    map.put("com.sun.jersey.api.json.POJOMappingFeature", "true");
    serve("/*").with(GuiceContainer.class, map);
    filter("/rest" + "/*").through(AdminIdProviderFilter.class);
  }
}
