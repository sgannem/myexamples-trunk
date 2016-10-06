package com.xyz;

import javax.inject.Singleton;

/**
 * http://www.journaldev.com/2403/google-guice-dependency-injection-example-tutorial
 * @author nxa30710
 *
 */

	@Singleton
	public class EmailService implements MessageService {
	 
	    public boolean sendMessage(String msg, String receipient) {
	        //some fancy code to send email
	        System.out.println("Email Message sent to "+receipient+" with message="+msg);
	        return true;
	    }
	 
	}

