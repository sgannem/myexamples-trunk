package com.xyz;

import com.nxp.nfclib.ProtocolDetails;
import com.nxp.nfclib.exceptions.ReaderException;
import com.nxp.nfclib.sysinterfaces.IReader;

public class SCReader implements IReader {

	@Override
	public void close() throws ReaderException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void connect() throws ReaderException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ProtocolDetails getProtocolDetails() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getTimeout() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isConnected() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setTimeout(long arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public byte[] transceive(byte[] arg0) throws ReaderException {
		// TODO Auto-generated method stub
		return null;
	}

}
