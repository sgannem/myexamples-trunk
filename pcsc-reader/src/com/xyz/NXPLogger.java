package com.xyz;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


public class NXPLogger {
	

	public static final int TESTING_START = 0;
	public static final int TESTING_END = 1;
	
	static Logger logger = Logger.getLogger("Report.log");
	
	static FileHandler fh;  
	public static void initialize() {
		try {
			//fh = new FileHandler("/home/ubuntu/Desktop/Report.log");
			fh = new FileHandler("c:\\logs\\Report2.log");
			logger.addHandler(fh);
			logger.setUseParentHandlers(false);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        
	}


	private static boolean isLogEnabled = true;

	public static void disableLogging() {
		isLogEnabled = false;
	}

	public static void enableLogging() {
		isLogEnabled = true;
	}

	public static boolean isLoggingEnabled() {
		return isLogEnabled;
	}

	public static void customLog(final String tag, final String str) {
		if (isLogEnabled) {
			logger.log(Level.INFO, tag , str);
			System.out.println(tag + " " + str);
		}
	}

	public static void customLog(final String tag, final String str,
			Throwable tr) {
		if (isLogEnabled) {
			String sExceptionMsg = tr.getClass().getName();
			sExceptionMsg = sExceptionMsg + "\t" + tr.getMessage() + "\n";
			for (StackTraceElement elem : tr.getStackTrace()) {
				sExceptionMsg = sExceptionMsg + "at " + elem.toString() + "\n";
			}
			logger.log(Level.INFO, tag + " " + str, sExceptionMsg);
			System.out.println(tag + " " + str+ " "+ tr.getMessage());
		}
	}

}
