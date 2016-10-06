package com.xyz;
import java.util.Arrays;
import java.util.List;

import com.nxp.nfclib.CustomModules;
import com.nxp.nfclib.LibraryManager;
import com.nxp.nfclib.sysinterfaces.IApduHandler;
import com.nxp.nfclib.sysinterfaces.IReader;

import de.intarsys.security.smartcard.pcsc.IPCSCCardReader;
import de.intarsys.security.smartcard.pcsc.IPCSCConnection;
import de.intarsys.security.smartcard.pcsc.IPCSCContext;
import de.intarsys.security.smartcard.pcsc.PCSCCardReader;
import de.intarsys.security.smartcard.pcsc.PCSCCardReaderState;
import de.intarsys.security.smartcard.pcsc.PCSCContextFactory;
import de.intarsys.security.smartcard.pcsc.PCSCException;
import de.intarsys.security.smartcard.pcsc.PCSCStatusMonitor;
import de.intarsys.security.smartcard.pcsc.PCSCStatusMonitor.IStatusListener;
import de.intarsys.security.smartcard.pcsc.nativec._IPCSC;

public class PCSCTranscieve implements IApduHandler  {

	static String PICC_READER_NAME = "BROADCOM NFC Smartcard Reader 1";
	static String SAM_READER_NAME = "BROADCOM NFC Smartcard Reader 1";
	static String ICC_READER_NAME = "BROADCOM NFC Smartcard Reader 1";
	// PCSCCardReader myReader = null;
	// PCSCStatusMonitor monitor = null;
	// PCSCCardReader myReaderICC = null;
	PCSCCardReader myReaderPICC = null;
	// PCSCCardReader myReaderSAM = null;
	IPCSCContext context = null;
	IPCSCConnection connection = null;
	
	
	
	// PCSCStatusMonitor monitorICC = null;
	PCSCStatusMonitor monitorPICC = null;
	// PCSCStatusMonitor monitorSAM = null;
	final int MAX_RECIVE_BUFFER = 255;
	volatile boolean IsConnected = false;
	

	private synchronized void monitorCard() {

		monitorPICC = new PCSCStatusMonitor(myReaderPICC);
		
		monitorPICC.addStatusListener(new IStatusListener() {
			@Override
			public void onException(IPCSCCardReader reader, PCSCException e) {
				e.printStackTrace();
			}

			@Override
			public void onStatusChange(IPCSCCardReader reader,
					PCSCCardReaderState cardReaderState) {
				System.out.println("reader " + cardReaderState.getReader()
						+ " state " + cardReaderState);
				if (cardReaderState.isPresent()) {
					try {
						System.out.println("Card Came...");
						context = myReaderPICC.getContext().establishContext();
						System.out.println("After EstablishContext Came...");
						connection = context.connect(myReaderPICC.getName(),
								_IPCSC.SCARD_SHARE_SHARED,
								_IPCSC.SCARD_PROTOCOL_Tx);
						System.out.println("After Connect...");
						IsConnected = true;
						monitorPICC.stop();
					} catch (PCSCException e) {
						e.printStackTrace();
					}
				}
			}
		});

	}

	public void waitForCard() {
			
		try {
			while (!IsConnected) {
				System.out.println("Waiting...");
				Thread.sleep(500);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean initialize() {
		try {
			context = PCSCContextFactory.get().establishContext();
			List<IPCSCCardReader> readers = context.listReaders();
			if (readers.isEmpty()) {
				System.out.println("no reader found");
				return false;
			} else {
				for (IPCSCCardReader reader : readers) {
					if (reader.getName().equals(PICC_READER_NAME)) {
						myReaderPICC = (PCSCCardReader) reader;
						monitorCard();
					}
				}
			}
		} catch (PCSCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void reconnect() {

		// TODO Auto-generated method stub
		try {
			
//			connection.disconnect(_IPCSC.SCARD_LEAVE_CARD);
//			if (context != null) {
//				System.out.print("Inside Dispose");
//				context.dispose();
//				context = null;
//			}
//		connection.endTransaction(_IPCSC.SCARD_LEAVE_CARD);
//		run();
//		initialize();
			
			
			connection.reconnect(_IPCSC.SCARD_SHARE_SHARED,
					_IPCSC.SCARD_PROTOCOL_Tx, _IPCSC.SCARD_UNPOWER_CARD);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// @Override
	// public byte[] layer3Transceive(byte[] apduData) {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// public byte[] layer4Transceive(byte[] apduData) {
	// // TODO Auto-generated method stub
	// try {
	// return connection.transmit(apduData, 0, apduData.length,
	// MAX_RECIVE_BUFFER, false);
	// } catch (PCSCException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return null;
	// }
	//
	// @Override
	// public void setTimeout(int milliSeconds) {
	// // TODO Auto-generated method stub
	//
	// }
	
//	protected void connect(CardTerminal cardTerminal) throws Exception {
//		System.out.println("" + cardTerminal + " connect");
//		Card card = cardTerminal.connect("*");
//		try {
//			System.out.println("" + card + " begin transaction");
//			card.beginExclusive();
//			//System.out.println("" + card + " end transaction");
//			//card.endExclusive();
//		} finally {
//			System.out.println("" + card + " disconnect");
//			//card.disconnect(false);
//		}
//	}
//	

//	public void run() throws Exception {
//		Security.insertProviderAt(new SmartcardioProvider(), 1);
//		//
//		TerminalFactory factory = TerminalFactory.getDefault();
//		CardTerminals terminals = factory.terminals();
//		//while (System.in.available() == 0) {
//			List<CardTerminal> list = terminals.list(State.CARD_INSERTION);
//			if (list.isEmpty()) {
//				System.out.println("no terminals");
//			}
//			for (CardTerminal cardTerminal : list) {
//				try {
//					connect(cardTerminal);
//				} catch (Exception e) {
//					System.out.println("error connecting " + e);
//				}
//			}
//			System.out.println("wait for change");
//			if (terminals.waitForChange(5000)) {
//				System.out.println("change detected...");
//			} else {
//				System.out.println("timeout");
//			}
//		//}
//	}
	
//	protected void connect(IPCSCCardReader reader) throws PCSCException {
//		// recommended use: create a new context for the connection
//		System.out.println("" + reader + " establish context");
//		IPCSCContext connectionContext = reader.getContext().establishContext();
//		System.out.println("" + reader + " connect");
//		IPCSCConnection connection = connectionContext.connect(
//				reader.getName(), _IPCSC.SCARD_SHARE_SHARED,
//				_IPCSC.SCARD_PROTOCOL_Tx);
//		System.out.println("" + reader + " begin transaction");
//		connection.beginTransaction();
//		System.out.println("" + reader + " end transaction");
//		connection.endTransaction(_IPCSC.SCARD_LEAVE_CARD);
//		System.out.println("" + reader + " disconnect");
//		connection.disconnect(_IPCSC.SCARD_LEAVE_CARD);
//		System.out.println("" + reader + " dispose context");
//		connectionContext.dispose();
//	}

	@Override
	public byte[] apduExchange(byte[] apduData) {
		// TODO Auto-generated method stub
		byte[] resp1 = new byte[] { 0, (byte) 0x90, 0 };
		byte[] resp2 = new byte[] { (byte) 0xAF, (byte) 0x90, 0 };
		byte[] wrappedAPDU = new byte[apduData.length + 5];
		byte[] directTransmitHeader = new byte[] { (byte) 0xFF,(byte) 0x00, 0x00,
				0x00 };
//		byte[] directTransmitHeader2 = new byte[] { (byte) 0x01,(byte) 0xA0, 0x00,
//				0x05 };
		wrappedAPDU = CustomModules.getUtility().append(
				(byte) apduData.length, apduData);
		wrappedAPDU = CustomModules.getUtility().append(directTransmitHeader,
				wrappedAPDU);

		try {
			byte[] resp = null;
			System.out.println(CustomModules.getUtility().dumpBytes(
					apduData));
			resp = connection.transmit(apduData, 0, apduData.length,
					MAX_RECIVE_BUFFER, false);
			System.out.println(CustomModules.getUtility().dumpBytes(resp));
			if (resp == null) {
				throw new RuntimeException();
			}
			if (Arrays.equals(resp, resp1) || Arrays.equals(resp, resp2)) {
				resp = new byte[] { resp[0] };
			}
//			if (resp[resp.length - 2] == -112) {
//				resp = Arrays.copyOf(resp, resp.length - 2);
//			} else {
//				return null;
//			}

			return resp;
		} catch (PCSCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	
	public void setTimeOut(int tiemoutValue) {
		try {
			byte[] status = connection.transmit(new byte[] { (byte) 0x1f, 0x03,
					0x0b, 0x08, 0x64 }, 0, 2, MAX_RECIVE_BUFFER, false);
			System.out.println(CustomModules.getUtility().dumpBytes(status));
		} catch (PCSCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void openGenericSession() {
		try {
			byte[] status = connection.transmit(new byte[] { (byte) 0xff, (byte)0xA0,
					0x00, 0x07, 0x03,0x01,0x00,0x01 }, 0, 2, MAX_RECIVE_BUFFER, false);
			System.out.println(CustomModules.getUtility().dumpBytes(status));
		} catch (PCSCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getFWI() {
		byte[] out = new byte[2];
		
			out = apduExchange(new byte[] {(byte) 0x1f,0x00});
		
	}
	
	public void acsRfReset() {
		try {
			byte[] status = connection.transmit(new byte[] { (byte) 0xff ,0x00,0x00,0x00,0x04,(byte)0xD4,0x32,0x01,0x00}, 0, 2, MAX_RECIVE_BUFFER, false);
			status = connection.transmit(new byte[] { (byte) 0xff ,0x00,0x00,0x00,0x04,(byte)0xD4,0x32,0x01,0x01}, 0, 2, MAX_RECIVE_BUFFER, false);
			System.out.println(CustomModules.getUtility().dumpBytes(status));
			
		} catch (PCSCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public IReader getReader() {
		// TODO Auto-generated method stub
		return null;
	}
}
