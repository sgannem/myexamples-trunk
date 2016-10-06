package com.xyz;

import java.util.List;

import javax.smartcardio.*;

public class TestPCSCReader {
	
	public static void main(String[] args) {
		
		 try {
		        final List<CardTerminal> t = TerminalFactory.getDefault().terminals().list();
		       System.out.println(t);
		    }
		    catch (final Exception ex) {
		        ex.printStackTrace();
		    }
		}

	}

