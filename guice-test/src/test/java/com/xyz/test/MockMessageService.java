package com.xyz.test;

import com.xyz.MessageService;

public class MockMessageService implements MessageService {
	 
    public boolean sendMessage(String msg, String receipient) {
    	System.out.printf("#sendMessage{%s} {%s}", msg, receipient);
        return true;
    }
 
}
