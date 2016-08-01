package com.xyz;

import java.io.File;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class TestJNDI {
	
	public static void main(String[] args) {
		try {
			String somename="Temp";
			Hashtable env = new Hashtable();
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.fscontext.RefFSContextFactory");
			env.put(Context.PROVIDER_URL, "file:///");
		    Context ctx = new InitialContext(env);
		    File newFile = new File("C:/nxp/workspaces/appstore/test-jndi/jndi.properties");
		    ctx.bind("newFile", newFile);
		    Object obj = ctx.lookup(somename);
		    System.out.println(obj);
		} catch (NamingException e) {
		    // Handle the error
		    System.err.println(e);
		}
	}

	public static void main1(String[] args) throws NamingException {
		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.fscontext.RefFSContextFactory");
		env.put(Context.PROVIDER_URL, "file:///");
		Context ctx = new InitialContext(env);
		System.out.println("#Got context:"+ctx);
		// Look up the connection factory object in the JNDI object store.

//		 String CF_LOOKUP_NAME = "java:comp/env/ejb/myEJB";
//		 ConnectionFactory myFactory = (ConnectionFactory) ctx.lookup(CF_LOOKUP_NAME);

	}

}
