package com.xyz;

import java.util.List;
import java.util.Scanner;

import javax.smartcardio.CardTerminal;

import com.nxp.nfclib.LibraryManager;
import com.nxp.nfclib.ProtocolDetails;
import com.nxp.nfclib.desfire.DESFireEV1.AuthType;
import com.nxp.nfclib.desfire.DESFireEV1.CommandSet;
import com.nxp.nfclib.desfire.DESFireFactory;
import com.nxp.nfclib.desfire.IDESFireEV1;
import com.nxp.nfclib.exceptions.SmartCardException;
import com.nxp.nfclib.keystore.common.IKeyConstants;
import com.nxp.nfclib.keystore.common.IKeyStore;

public class PCSCReaderForAuth {

	public static final byte[] KEY_AES128 = { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
			(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
			(byte) 0x00, (byte) 0x00, (byte) 0x00 };
	
	/** 0F1E2D3C4B5A69788796A5B4C3D2E1F0 **/
	public static final byte[] RISE_PICC_MASTER_KEY_AES128 = { (byte) 0x0F, (byte) 0x1E, (byte) 0x2D, (byte) 0x0E, (byte) 0x3C,
			(byte) 0x4B, (byte) 0x5A, (byte) 0x69, (byte) 0x78, (byte) 0x87, (byte) 0x96, (byte) 0xA5, (byte) 0xB4,
			(byte) 0xC3, (byte) 0xD2, (byte) 0xE1, (byte)0xF0 };

	private static byte[] keyDefault16 = { (byte) 0x00 };

	private static IKeyStore ks;
	static LibraryManager MIFARESdk = new LibraryManager();

	public static void main(String[] args) {

		try {
			MIFARESdk.getSupportModules().setLogger(new NXPITLogger());
			ProtocolDetails protocolDetails = new ProtocolDetails();
			IDESFireEV1 desfireCard = DESFireFactory.getInstance().getDESFire(MIFARESdk.getSupportModules(), null);
			PCSCTranscieve transceiver = new PCSCTranscieve();
			MIFARESdk.setApduHandler(transceiver);
//			desfireCard.setCommandSet(CommandSet.ISO);
			transceiver.initialize();
			// authentication needed after selecting the application.
//			desfireCard.authenticate(AuthType.AES, 0, (byte) 0, 0, (byte) 0, RISE_PICC_MASTER_KEY_AES128);
			desfireCard.authenticate(AuthType.Native, 0, (byte) 0, 0, (byte) 0, null);
			System.out.println("auth success!");
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
			// ks.setKey(2, (byte) 0,
			// IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_2K3DES, KEY_2KTDES);

			ks.formatKeyEntry(3, IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_AES128);
			ks.setKey(3, (byte) 0, IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_AES128, KEY_AES128);

			ks.formatKeyEntry(7, IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_AES128);
//			ks.setKey(7, (byte) 0, IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_AES128, keyDefault16);
			ks.setKey(7, (byte) 0, IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_AES128, RISE_PICC_MASTER_KEY_AES128);
			

		} catch (SmartCardException e) {
			e.printStackTrace();
		}
		MIFARESdk.getSupportModules().setKeyStore(ks);
	}

}
