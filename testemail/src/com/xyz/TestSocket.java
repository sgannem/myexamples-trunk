package com.xyz;

import java.net.InetAddress;
import java.net.Socket;

public class TestSocket {

	public static void main(String[] args) {
		for (int i = 20; i < 25; i++) {
			try {
				System.out.println("#Connecting socket on :"+i);
				Socket s = new Socket(InetAddress.getByName("smtp.sendgrid.net"), i);
				s.setSoTimeout(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
