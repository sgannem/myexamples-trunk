package com.xyz;

import java.io.IOException;

import com.nxp.nfclib.sysinterfaces.IApduHandler;
import com.nxp.nfclib.sysinterfaces.IReader;

public class CAPDUHandler implements IApduHandler {

	@Override
	public byte[] apduExchange(byte[] arg0) throws IOException {
		return "REQA".getBytes();
	}

	@Override
	public IReader getReader() {
		// TODO Auto-generated method stub
		return null;
	}

} 
