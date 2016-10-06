package com.xyz;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path(value="service")
public class PingResource {
	
	@GET
	@Path("/ping")
	public Response ping() {
		System.out.println("ping is called....");
	 return Response.ok("Pong").build();
	}

}
