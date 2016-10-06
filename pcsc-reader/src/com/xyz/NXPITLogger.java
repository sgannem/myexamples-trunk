package com.xyz;

import com.nxp.nfclib.sysinterfaces.ILogger;

public class NXPITLogger implements ILogger {
	

	public NXPITLogger() {
		NXPLogger.initialize();
	}

	@Override
	public void enableLogging(LogLevel arg0) {
		NXPLogger.enableLogging();
		
	}

	@Override
	public void log(LogLevel arg0, String arg1, String arg2) {
		NXPLogger.customLog(arg1, arg2);
		
	}

	@Override
	public void logAsciiValue(LogLevel arg0, String arg1, byte[] arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void logBinaryValue(LogLevel arg0, String arg1, byte[] arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void logHexValue(LogLevel arg0, String arg1, byte[] arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isLoggingEnabled(LogLevel loglevel) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void log(LogLevel arg0, String arg1, String arg2, Throwable arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disableLogging(LogLevel arg0) {
		// TODO Auto-generated method stub
		
	}
	
	

}
