package com.xyz;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import javax.smartcardio.CardTerminal;

import com.nxp.nfclib.LibraryManager;
import com.nxp.nfclib.ProtocolDetails;
import com.nxp.nfclib.desfire.DESFireEV1.AuthType;
import com.nxp.nfclib.desfire.DESFireEV1.CommandSet;
import com.nxp.nfclib.desfire.DESFireFactory;
import com.nxp.nfclib.desfire.IDESFireEV1;
import com.nxp.nfclib.exceptions.DESFireException;
import com.nxp.nfclib.exceptions.SmartCardException;
import com.nxp.nfclib.keystore.common.IKeyConstants;
import com.nxp.nfclib.keystore.common.IKeyStore;
import com.nxp.nfclib.plus.PlusFactory;
import com.nxp.nfclib.plus.PlusS;

public class PCSCReader {
	
	
	
		public static final byte[] KEY_AES128 = { (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 }; 
		
		/** 0F1E2D3C4B5A69788796A5B4C3D2E1F0 **/
		public static final byte[] RISE_PICC_MASTER_KEY_AES128 = { (byte) 0x0F, (byte) 0x1E, (byte) 0x2D, (byte) 0x0E,
				(byte) 0x3C, (byte) 0x4B, (byte) 0x5A, (byte) 0x69, (byte) 0x78, (byte) 0x87, (byte) 0x96, (byte) 0xA5,
				(byte) 0xB4, (byte) 0xC3, (byte) 0xD2, (byte) 0xE1, (byte) 0xF0 };
		
		
		private static byte[] keyDefault16 = { (byte) 0x00 };
		
		private static IKeyStore ks; 
		static LibraryManager MIFARESdk = new LibraryManager();
		
		
		/**
		 * @param args
		 * @throws IOException
		 * @throws DESFireException
		 */
		public static void main222(String[] args) throws DESFireException, IOException {
			try {
				 MIFARESdk.getSupportModules().setLogger(new NXPITLogger());
				ProtocolDetails protocolDetails = new ProtocolDetails();
				PlusS plusSCard =  (PlusS) PlusFactory.getInstance().getPlusS(MIFARESdk.getSupportModules(), null);

				PCSCTranscieve transceiver = new PCSCTranscieve();

				// transceiver = new PCSCTranscieve();
				MIFARESdk.setApduHandler(transceiver);

//				plusSCard.setCommandSet(CommandSet.ISO);

//				List<CardTerminal> terminals;
//				terminals = transceiver.getAvaialbleTerminals();
//				int index = 0;
//				for (CardTerminal cardTerminal : terminals) {
//					System.out.println("Terminal " + index + ":" + cardTerminal);
//					index++;
//				}
//				System.out.println("Enter the terminal number you want to use from the above list ....");
//
//				Scanner in = new Scanner(System.in);
//				int choice = in.nextInt();
//
//			transceiver.start(choice);
				
				transceiver.initialize();
				
				transceiver.waitForCard();
				
				plusSCard.resetAuth();
				
//				boolean cardType = plusSCard.isCardPlusS();
				
//				System.out.println("#Card Type:"+cardType);
				

//				byte[] version = plusSCard.getVersion();

//				System.out.println(MIFARESdk.getUtility().dumpBytes(version));

				// show the list of available terminals
				// TerminalFactory factory = TerminalFactory.getDefault();
				// List<CardTerminal> terminals = factory.terminals().list();
				// System.out.println("Terminals: " + terminals);
				// for (CardTerminal t : terminals) {
				// System.out.println("card terminal:" + t);
				// if (t.isCardPresent()) {
				// System.out.println("#Card is present on terminal:" + t);
				// try {
				// System.out.println("#############################");
				// Card card = t.connect("*");
				// System.out.println("#Got Card:" + card);
				// CardChannel cc = card.getBasicChannel();
				// String protocol = card.getProtocol();
				// System.out.println("#card channel:" + cc + ", protocol:" +
				// protocol);
				// CommandAPDU capdu = new CommandAPDU("REQA".getBytes());
				// ResponseAPDU rapdu = cc.transmit(capdu);
				// System.out.println("#rapdu:" + rapdu);
				// System.out.println("#############################");
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
				// } else {
				// System.out.println("#Card is not present @terminal:" + t);
				// }
				//
				// }

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		
		
		
		public static void main(String[] args) {
			
			try {

				MIFARESdk.getSupportModules().setLogger(new NXPITLogger());
				ProtocolDetails protocolDetails = new ProtocolDetails();
				IDESFireEV1 desfireCard = DESFireFactory.getInstance().getDESFire(MIFARESdk.getSupportModules(), null);
				
				SmartCardIOAdapter transceiver = new SmartCardIOAdapter();
				PCSCTranscieve t = new PCSCTranscieve();
				t.initialize();
				// transceiver = new PCSCTranscieve();
				MIFARESdk.setApduHandler(t);

//				desfireCard.setCommandSet(CommandSet.ISO);

				List<CardTerminal> terminals;
				terminals = transceiver.getAvaialbleTerminals();
				int index = 0;
				for (CardTerminal cardTerminal : terminals) {
					System.out.println("Terminal " + index + ":" + cardTerminal);
					index++;
				}
				System.out.println("Enter the terminal number you want to use from the above list ....");

				Scanner in = new Scanner(System.in);
				int choice = in.nextInt();

				transceiver.start(choice);
				int iAppId = 0x00;
//				desfireCard.selectApplication(iAppId);

				// authentication needed after selecting the application.
//				desfireCard.authenticate(AuthType.Native, 3, (byte) 0, 0, (byte) 0,
//						null); 
				initializeKeyStore();
				desfireCard.authenticate(AuthType.AES, 7, (byte) 0, 0, (byte) 0,
						null); 

//				System.out.println(MIFARESdk.getUtility().dumpBytes(version));
				System.out.println("auth success!");

				// show the list of available terminals
				// TerminalFactory factory = TerminalFactory.getDefault();
				// List<CardTerminal> terminals = factory.terminals().list();
				// System.out.println("Terminals: " + terminals);
				// for (CardTerminal t : terminals) {
				// System.out.println("card terminal:" + t);
				// if (t.isCardPresent()) {
				// System.out.println("#Card is present on terminal:" + t);
				// try {
				// System.out.println("#############################");
				// Card card = t.connect("*");
				// System.out.println("#Got Card:" + card);
				// CardChannel cc = card.getBasicChannel();
				// String protocol = card.getProtocol();
				// System.out.println("#card channel:" + cc + ", protocol:" +
				// protocol);
				// CommandAPDU capdu = new CommandAPDU("REQA".getBytes());
				// ResponseAPDU rapdu = cc.transmit(capdu);
				// System.out.println("#rapdu:" + rapdu);
				// System.out.println("#############################");
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
				// } else {
				// System.out.println("#Card is not present @terminal:" + t);
				// }
				//
				// }

			} catch (Exception e) {
				e.printStackTrace();
			}

			
		}


	/**
	 * @param args
	 * @throws IOException
	 * @throws DESFireException
	 */
	public static void main333(String[] args) throws DESFireException, IOException {
		try {
			 MIFARESdk.getSupportModules().setLogger(new NXPITLogger());
			ProtocolDetails protocolDetails = new ProtocolDetails();
			IDESFireEV1 desfireCard = DESFireFactory.getInstance().getDESFire(MIFARESdk.getSupportModules(), null);

			SmartCardIOAdapter transceiver = new SmartCardIOAdapter();

			// transceiver = new PCSCTranscieve();
			PCSCTranscieve t = new PCSCTranscieve();
			t.initialize();
//			MIFARESdk.setApduHandler(transceiver);
			MIFARESdk.setApduHandler(t);

			desfireCard.setCommandSet(CommandSet.ISO);

			List<CardTerminal> terminals;
			terminals = transceiver.getAvaialbleTerminals();
			int index = 0;
			for (CardTerminal cardTerminal : terminals) {
				System.out.println("Terminal " + index + ":" + cardTerminal);
				index++;
			}
			System.out.println("Enter the terminal number you want to use from the above list ....");

			Scanner in = new Scanner(System.in);
			int choice = in.nextInt();

			transceiver.start(choice);

			byte[] version = desfireCard.getVersion();

			System.out.println(MIFARESdk.getUtility().dumpBytes(version));

			// show the list of available terminals
			// TerminalFactory factory = TerminalFactory.getDefault();
			// List<CardTerminal> terminals = factory.terminals().list();
			// System.out.println("Terminals: " + terminals);
			// for (CardTerminal t : terminals) {
			// System.out.println("card terminal:" + t);
			// if (t.isCardPresent()) {
			// System.out.println("#Card is present on terminal:" + t);
			// try {
			// System.out.println("#############################");
			// Card card = t.connect("*");
			// System.out.println("#Got Card:" + card);
			// CardChannel cc = card.getBasicChannel();
			// String protocol = card.getProtocol();
			// System.out.println("#card channel:" + cc + ", protocol:" +
			// protocol);
			// CommandAPDU capdu = new CommandAPDU("REQA".getBytes());
			// ResponseAPDU rapdu = cc.transmit(capdu);
			// System.out.println("#rapdu:" + rapdu);
			// System.out.println("#############################");
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			// } else {
			// System.out.println("#Card is not present @terminal:" + t);
			// }
			//
			// }

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
		static void initializeKeyStore() {

				/* Initialize the keystore and load the key */
				ks = MIFARESdk.getSupportModules().getKeyStore();

				/* MIFARE classic card set the key. */
				try {
					ks.formatKeyEntry(0, IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_DES);
					ks.formatKeyEntry(4, IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_DES);
					/* MIFARE DESFire card set the key. */
					ks.formatKeyEntry(2, IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_2K3DES);
//					ks.setKey(2, (byte) 0,
//							IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_2K3DES, KEY_2KTDES);

					ks.formatKeyEntry(3, IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_AES128);
					ks.setKey(3, (byte) 0,
							IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_AES128, KEY_AES128);

					ks.formatKeyEntry(7, IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_AES128);
					ks.setKey(7, (byte) 0,
							IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_AES128,
							RISE_PICC_MASTER_KEY_AES128);

				} catch (SmartCardException e) {
					e.printStackTrace();
				}
				MIFARESdk.getSupportModules().setKeyStore(ks);
			} 

	
}
