package com.xyz.mifare.test;

import com.nxp.nfclib.LibraryManager;
import com.nxp.nfclib.ProtocolDetails;
import com.nxp.nfclib.desfire.DESFireFactory;
import com.nxp.nfclib.desfire.IDESFireEV1;
import com.nxp.nfclib.keystore.common.IKeyStore;
import com.xyz.NXPITLogger;
import com.xyz.SmartCardIOAdapter;

public class EV1Main {

	public static final byte[] KEY_AES128 = { (byte) 0x00, (byte) 0x00,
			(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
			(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
			(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 }; 
	
	private static byte[] keyDefault16 = { (byte) 0x00 };
	
	private static IKeyStore ks; 
	static LibraryManager MIFARESdk = new LibraryManager();

	public static void main(String[] args) {
		try {
			MIFARESdk.getSupportModules().setLogger(new NXPITLogger());
			ProtocolDetails protocolDetails = new ProtocolDetails();
			IDESFireEV1 desfireCard = DESFireFactory.getInstance().getDESFire(MIFARESdk.getSupportModules(), null);

			SmartCardIOAdapter transceiver = new SmartCardIOAdapter();
			MIFARESdk.setApduHandler(transceiver);
			
			DesfireEV1Test t = new DesfireEV1Test(desfireCard, MIFARESdk.getSupportModules());
			t.testGenerateDESFireEV1CardReport();
			
//			transceiver.initialize();
//			transceiver.waitForCard();
//			plusSCard.resetAuth();
			
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
