package com.xyz;

/**
 * @author nxa30710
 *
 */
public interface MessageService {
	
	/**
	 * @param msg
	 * @param receipient
	 * @return
	 */
	boolean sendMessage(String msg, String receipient);

}
