package com.xyz.mifare.test;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.nio.charset.Charset;


import com.nxp.nfclib.CustomModules;
import com.nxp.nfclib.LibraryManager;
import com.nxp.nfclib.desfire.DESFireEV1;
import com.nxp.nfclib.desfire.DESFireEV1.AuthType;
import com.nxp.nfclib.desfire.DESFireEV1.CardDetails;
import com.nxp.nfclib.desfire.DESFireEV1.CommandSet;
import com.nxp.nfclib.desfire.DESFireEV1.KeyType;
import com.nxp.nfclib.desfire.DESFireFile.BackupDataFileSettings;
import com.nxp.nfclib.desfire.DESFireFile.CyclicRecordFileSettings;
import com.nxp.nfclib.desfire.DESFireFile.FileSettings;
import com.nxp.nfclib.desfire.DESFireFile.LinearRecordFileSettings;
import com.nxp.nfclib.desfire.DESFireFile.StdDataFileSettings;
import com.nxp.nfclib.desfire.DESFireFile.ValueFileSettings;
import com.nxp.nfclib.desfire.IDESFireConstants.MemSize;
import com.nxp.nfclib.desfire.IDESFireEV1;
import com.nxp.nfclib.exceptions.DESFireException;
import com.nxp.nfclib.exceptions.ReaderException;
import com.nxp.nfclib.exceptions.SmartCardException;
import com.nxp.nfclib.keystore.common.IKeyConstants;
import com.nxp.nfclib.keystore.common.IKeyStore;
import com.nxp.nfclib.keystore.software.SoftwareKeyStore;
import com.nxp.nfclib.ndef.FormatException;
import com.nxp.nfclib.ndef.INdefMessage;
import com.nxp.nfclib.ndef.NdefMessageWrapper;
import com.nxp.nfclib.ndef.NdefRecordWrapper;
import com.nxp.nfclib.os.java.Utilities;
import com.nxp.nfclib.sysinterfaces.ILogger;
import com.nxp.nfclib.sysinterfaces.ILogger.LogLevel;
import com.nxp.nfclib.ultralight.ULBase;
import com.nxp.nfclib.ultralight.UltralightEV1;

public class DesfireEV1Test {

	/** Desfire object. */
	private static IDESFireEV1 mDesFireEV1;
	/** Class TAG */
	private static String TAG = "DESFireEV1Test";
	private static String LOGTAG = TAG;

	private LibraryManager libInstance = null;

	private CustomModules modules = new CustomModules();
	/** {@link IKeyStore} instance. */
	public static IKeyStore iKs = null;

	static ILogger report;

	private boolean isDefaultKeyIsOldKey = true;

	private final static int D_PICC_DES_KEY_NO = 0;
	private final static int D_PICC_2KDES_KEY_NO = 1;
	private final static int D_PICC_3K3KDES_KEY_NO = 2;
	private final static int D_PICC_AES_KEY_NO = 3;
	private final static int O_PICC_DES_KEY_NO = 4;
	private final static int O_PICC_2KDES_KEY_NO = 5;
	private final static int O_PICC_3K3KDES_KEY_NO = 6;
	private final static int O_PICC_AES_KEY_NO = 7;

	private final static int D_APP_DES_KEY_NO = 8;
	private final static int D_APP_2KDES_KEY_NO = 9;
	private final static int D_APP_3K3KDES_KEY_NO = 10;
	private final static int D_APP_AES_KEY_NO = 11;
	private final static int O_APP_DES_KEY_NO = 12;
	private final static int O_APP_2KDES_KEY_NO = 13;
	private final static int O_APP_3K3KDES_KEY_NO = 14;
	private final static int O_APP_AES_KEY_NO = 15;
	private final static int RESTORE_KEY_SLOT = 16;

	private final static int DIV_KEY_STORE_SLOT1 = 17;
	private final static int DIV_KEY_STORE_SLOT2 = 18;

	private final static int STD_DATA_FILE_NO = 0;
	private final static int BACKUP_DATA_FILE_NO = 1;
	private final static int VALUE_FILE_NO = 2;
	private final static int LINER_RECORD_FILE_NO = 3;
	private final static int CYCLIC_RECORD_FILE_NO = 4;

	private final static int ISOSTD_DATA_FILE_NO = 5;
	private final static int ISOBACKUP_DATA_FILE_NO = 6;
	private final static int ISOLINER_RECORD_FILE_NO = 7;
	private final static int ISOCYCLIC_RECORD_FILE_NO = 8;

	private final static byte[] ISO_APP_ID = new byte[] { 0x0E, 0x00 };

	private final static byte[] ISO_STD_DATA_FILE_NO = new byte[] { 0x0E, 0x01 };
	private final static byte[] ISO_BACKUP_DATA_FILE_NO = new byte[] { 0x0E,
			0x02 };
	private final static byte[] ISO_VALUE_FILE_NO = new byte[] { 0x0E, 0x03 };
	private final static byte[] ISO_LINER_RECORD_FILE_NO = new byte[] { 0x0E,
			0x04 };
	private final static byte[] ISO_CYCLIC_RECORD_FILE_NO = new byte[] { 0x0E,
			0x05 };

	private final static int DATA_FILE_SIZE = 215;
	private final static int NO_OF_RECORDS = 10;
	private final static int RECORD_SIZE = 16;

	private final static byte[] EIGHT_BYTE_DIV_INPUT = new byte[] { 1, 2, 3, 4,
			5, 6, 7, 8 };
	private final static byte[] SIXTEEN_BYTE_DIV_INPUT = new byte[] { 1, 2, 3,
			4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8 };

	private final static byte[] isoAppName = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 0, 1,
			2, 3, 4, 5, 6 };

	byte[] data = new byte[] { (byte) 0x00, (byte) 0x01, (byte) 0x02,
			(byte) 0x03 };

	byte[] bigData = new byte[] { (byte) 0xff, (byte) 0xff, (byte) 0xff,
			(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
			(byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x11,
			(byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0xff, (byte) 0xff,
			(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
			(byte) 0xff, (byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x11,
			(byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0xff,
			(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
			(byte) 0xff, (byte) 0xff, (byte) 0x11, (byte) 0x11, (byte) 0x11,
			(byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x11,
			(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
			(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x11, (byte) 0x11,
			(byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x11,
			(byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0xff,
			(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
			(byte) 0xff, (byte) 0xff, (byte) 0x11, (byte) 0x11, (byte) 0x11,
			(byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x11,
			(byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0xff, (byte) 0xff,
			(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
			(byte) 0xff, (byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x11,
			(byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x11,
			(byte) 0x11, (byte) 0x11, (byte) 0xff, (byte) 0xff, (byte) 0xff,
			(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
			(byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x11,
			(byte) 0x11, (byte) 0x11, (byte) 0x11 };

	/**
	 * 16 byte default key.
	 */
	byte[] keyDefault16 = new byte[] { (byte) 0x01, (byte) 0x01, (byte) 0x01,
			(byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01,
			(byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01,
			(byte) 0x01, (byte) 0x01, (byte) 0x01 };

	/**
	 * 24 byte default key.
	 */
	byte[] keyDefault24 = new byte[] { (byte) 0x01, (byte) 0x01, (byte) 0x01,
			(byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01,
			(byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01,
			(byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01,
			(byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01,
			(byte) 0x01 };

	// Key From which it is diversified is keyDefault16 { All ones } wiht
	// SIXTEEN_BYTE_DIV_INPUT { 1 -- 8 and 1 -- 8 }
	byte[] aesDivOptDESFireDiversifiedKey = new byte[] { 0x6B, 0x62, 0x09,
			0x62, 0x3E, 0x23, (byte) 0xC0, 0x1B, 0x11, (byte) 0xF1, 0x06, 0x14,
			(byte) 0xD7, 0x14, (byte) 0xC1, (byte) 0xBF };
	// Key From which it is diversified is keyDefault16 { All ones } wiht
	// DIV_INPUT { 1 -- 6 }
	byte[] aesDivOptDESFireDiversifiedKeyWith6DivByte = new byte[] { 0x29,
			(byte) 0x81, (byte) 0x99, (byte) 0x95, 0x2C, (byte) 0xF0,
			(byte) 0xD8, 0x3B, 0x3A, (byte) 0xAE, (byte) 0xF3, (byte) 0xB3,
			0x6B, 0x42, (byte) 0xB8, 0x14 };

	/**
	 * 3k3DES key value.
	 */
	byte[] key3K3DES = new byte[] { (byte) 0x00, (byte) 0x01, (byte) 0x02,
			(byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07,
			(byte) 0x08, (byte) 0x09, (byte) 0x0A, (byte) 0x0B, (byte) 0x0C,
			(byte) 0x0D, (byte) 0x0E, (byte) 0x0F, (byte) 0x10, (byte) 0x11,
			(byte) 0x12, (byte) 0x13, (byte) 0x14, (byte) 0x15, (byte) 0x16,
			(byte) 0x17 };

	byte[] KEY_AES128_DEFAULT = { (byte) 0x00, (byte) 0x00, (byte) 0x00,
			(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
			(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
			(byte) 0x00, (byte) 0x00, (byte) 0x00 };

	byte keySettingsOne;
	byte keySettingsTwo;

	byte[] appIdOne = new byte[] { 0x01, 0x02, 0x03 };
	byte[] appIdTwo = new byte[] { 0x04, 0x05, 0x06 };
	String ndefDataslix2_1300 = "MifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeam";
	String ndefDataslix2_2600 = "MifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeam";
	String ndefDataslix2_26 = "MifareSDKTeamMifareSDKTeam";

	private boolean recoverHelperAuth(AuthType autType) {
		try {
			mDesFireEV1.authenticate(autType, RESTORE_KEY_SLOT, (byte) 0, 0,
					(byte) 0, null);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	boolean tryAESModeToRecover() {
		try {
			mDesFireEV1.selectApplication(0);
			iKs.formatKeyEntry(RESTORE_KEY_SLOT,
					IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_AES128);
			// Try with default key.
			if (recoverHelperAuth(AuthType.AES)) {
				mDesFireEV1.format();
				mDesFireEV1.changeKey(0, RESTORE_KEY_SLOT, (byte) 0,
						D_PICC_DES_KEY_NO, (byte) 0, DESFireEV1.KeyType.DES,
						(byte) 0, (byte) 0, null);

				return true;
			}
			// Try with KEY_AES128_DEFAULT key.
			iKs.setKey(RESTORE_KEY_SLOT, (byte) 0,
					IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_AES128,
					KEY_AES128_DEFAULT);
			if (recoverHelperAuth(AuthType.AES)) {
				mDesFireEV1.format();
				mDesFireEV1.changeKey(0, RESTORE_KEY_SLOT, (byte) 0,
						D_PICC_DES_KEY_NO, (byte) 0, DESFireEV1.KeyType.DES,
						(byte) 0, (byte) 0, null);
				return true;
			}

			// Try with AES diversified key
			// aesDivOptDESFireDiversifiedKey
			iKs.setKey(RESTORE_KEY_SLOT, (byte) 0,
					IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_AES128,
					aesDivOptDESFireDiversifiedKey);
			if (recoverHelperAuth(AuthType.AES)) {
				mDesFireEV1.format();
				mDesFireEV1.changeKey(0, RESTORE_KEY_SLOT, (byte) 0,
						D_PICC_DES_KEY_NO, (byte) 0, DESFireEV1.KeyType.DES,
						(byte) 0, (byte) 0, null);
				return true;
			}

			// Try with AES diversified key
			// aesDivOptDESFireDiversifiedKey
			iKs.setKey(RESTORE_KEY_SLOT, (byte) 0,
					IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_AES128,
					aesDivOptDESFireDiversifiedKeyWith6DivByte);
			if (recoverHelperAuth(AuthType.AES)) {
				mDesFireEV1.format();
				mDesFireEV1.changeKey(0, RESTORE_KEY_SLOT, (byte) 0,
						D_PICC_DES_KEY_NO, (byte) 0, DESFireEV1.KeyType.DES,
						(byte) 0, (byte) 0, null);
				return true;
			}

		} catch (Exception e) {
			return false;
		}
		return false;
	}

	boolean tryNativeAndISOModeToRecover() {
		try {
			mDesFireEV1.selectApplication(0);
			iKs.formatKeyEntry(RESTORE_KEY_SLOT,
					IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_2K3DES);

			// Try with default key.
			if (recoverHelperAuth(AuthType.Native)) {
				mDesFireEV1.format();
				return true;
			}

			// Try with default key.
			if (recoverHelperAuth(AuthType.ISO)) {
				mDesFireEV1.format();
				return true;
			}

			// Try with keyDefault16 key.
			iKs.setKey(RESTORE_KEY_SLOT, (byte) 0,
					IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_2K3DES,
					keyDefault16);
			if (recoverHelperAuth(AuthType.Native)) {
				mDesFireEV1.format();
				return true;
			}
			if (recoverHelperAuth(AuthType.ISO)) {
				mDesFireEV1.format();
				return true;
			}

			// Try with default 3K3DES key.
			iKs.formatKeyEntry(RESTORE_KEY_SLOT,
					IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_3K3DES);
			if (recoverHelperAuth(AuthType.ISO)) {
				mDesFireEV1.format();
				mDesFireEV1.changeKey(0, RESTORE_KEY_SLOT, (byte) 0,
						D_PICC_DES_KEY_NO, (byte) 0, DESFireEV1.KeyType.DES,
						(byte) 0, (byte) 0, null);
				return true;
			}
			// Try with keyDefault24 3K3DES key.
			iKs.setKey(RESTORE_KEY_SLOT, (byte) 0,
					IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_3K3DES,
					keyDefault24);
			if (recoverHelperAuth(AuthType.ISO)) {
				mDesFireEV1.format();
				mDesFireEV1.changeKey(0, RESTORE_KEY_SLOT, (byte) 0,
						D_PICC_DES_KEY_NO, (byte) 0, DESFireEV1.KeyType.DES,
						(byte) 0, (byte) 0, null);
				return true;
			}
			// Try with keyDefault24 3K3DES key.
			iKs.setKey(RESTORE_KEY_SLOT, (byte) 0,
					IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_3K3DES, key3K3DES);
			if (recoverHelperAuth(AuthType.ISO)) {
				mDesFireEV1.format();
				mDesFireEV1.changeKey(0, RESTORE_KEY_SLOT, (byte) 0,
						D_PICC_DES_KEY_NO, (byte) 0, DESFireEV1.KeyType.DES,
						(byte) 0, (byte) 0, null);
				return true;
			}

		} catch (Exception e) {
			return false;
		}
		return false;
	}

	private boolean readNdef() {
		boolean result = false;

		try {
			mDesFireEV1.getReader().close();
			mDesFireEV1.getReader().connect();
			mDesFireEV1.setCommandSet(CommandSet.ISO);
			mDesFireEV1.formatT4T(620, 0, (byte) 0, 0, (byte) 0);

			report.log(LogLevel.INFO, TAG, "Check for isT4T");

			if (mDesFireEV1.isT4T()) {
				report.log(LogLevel.INFO, TAG, "T4T formated");
			} else {
				report.log(LogLevel.INFO, TAG, "Not T4T formated");
			}
			NdefMessageWrapper msg = new NdefMessageWrapper(
					(NdefRecordWrapper) NdefRecordWrapper
							.createUri("http://www.googlefffffffffffffffffffffffffffffffffffffeeeeeeeeee.com"));
			/*
			 * NdefMessageWrapper msg = new NdefMessageWrapper(
			 * NdefRecordWrapper.createUri("This is text! 2154687"));
			 */

			// NdefMessageWrapper msg = new
			// NdefMessageWrapper(createTextRecord(ndefDataslix2_512,Locale.ENGLISH,
			// false));
			mDesFireEV1.writeNDEF(msg);

			msg = (NdefMessageWrapper) mDesFireEV1.readNDEF();
			report.log(LogLevel.INFO, TAG,
					"NDEF Message: " + msg.getByteArrayLength());
			report.log(LogLevel.INFO, TAG, "NDEF Message: " + msg.toString());
			mDesFireEV1.selectApplication(0);
			mDesFireEV1.getApplicationIDs();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}

	private boolean readNdefLargePayload() {
		boolean result = false;

		try {
			mDesFireEV1.getReader().close();
			mDesFireEV1.getReader().connect();
			mDesFireEV1.setCommandSet(CommandSet.ISO);
			mDesFireEV1.formatT4T(1800, 0, (byte) 0, 0, (byte) 0);

			report.log(LogLevel.INFO, TAG, "Check for isT4T");

			if (mDesFireEV1.isT4T()) {
				report.log(LogLevel.INFO, TAG, "T4T formated");
			} else {
				report.log(LogLevel.INFO, TAG, "Not T4T formated");
			}

			NdefMessageWrapper msg = new NdefMessageWrapper(createTextRecord(
					ndefDataslix2_1300, Locale.ENGLISH, false));
			mDesFireEV1.writeNDEF(msg);

			msg = (NdefMessageWrapper) mDesFireEV1.readNDEF();
			report.log(LogLevel.INFO, TAG,
					"NDEF Message: " + msg.getByteArrayLength());
			report.log(LogLevel.INFO, TAG, "NDEF Message: " + msg.toString());
			// report.log(LogLevel.INFO, TAG, "NDEF Message: " + String.);
			mDesFireEV1.selectApplication(0);
			mDesFireEV1.getApplicationIDs();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}

	private boolean readNdefLargePayload_4K() {
		boolean result = false;

		try {
			mDesFireEV1.getReader().close();
			mDesFireEV1.getReader().connect();
			mDesFireEV1.setCommandSet(CommandSet.ISO);
			mDesFireEV1.formatT4T(3600, 0, (byte) 0, 0, (byte) 0);

			report.log(LogLevel.INFO, TAG, "Check for isT4T");

			if (mDesFireEV1.isT4T()) {
				report.log(LogLevel.INFO, TAG, "T4T formated");
			} else {
				report.log(LogLevel.INFO, TAG, "Not T4T formated");
			}

			// Writing 2600 bytes in 4k DESFire Card
			NdefMessageWrapper msg = new NdefMessageWrapper(createTextRecord(
					ndefDataslix2_2600, Locale.ENGLISH, false));
			mDesFireEV1.writeNDEF(msg);

			msg = (NdefMessageWrapper) mDesFireEV1.readNDEF();
			report.log(LogLevel.INFO, TAG,
					"NDEF Message: " + msg.getByteArrayLength());
			report.log(LogLevel.INFO, TAG, "NDEF Message: " + msg.toString());
			// report.log(LogLevel.INFO, TAG, "NDEF Message: " + String.);
			mDesFireEV1.selectApplication(0);
			mDesFireEV1.getApplicationIDs();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}

	private boolean readNdefSmallPayload() {
		boolean result = false;

		try {
			mDesFireEV1.getReader().close();
			mDesFireEV1.getReader().connect();
			mDesFireEV1.setCommandSet(CommandSet.ISO);
			mDesFireEV1.formatT4T(1100, 0, (byte) 0, 0, (byte) 0);

			report.log(LogLevel.INFO, TAG, "Check for isT4T");

			if (mDesFireEV1.isT4T()) {
				report.log(LogLevel.INFO, TAG, "T4T formated");
			} else {
				report.log(LogLevel.INFO, TAG, "Not T4T formated");
			}

			NdefMessageWrapper msg = new NdefMessageWrapper(createTextRecord(
					ndefDataslix2_26, Locale.ENGLISH, false));
			mDesFireEV1.writeNDEF(msg);

			msg = (NdefMessageWrapper) mDesFireEV1.readNDEF();
			report.log(LogLevel.INFO, TAG,
					"NDEF Message: " + msg.getByteArrayLength());
			report.log(LogLevel.INFO, TAG, "NDEF Message: " + msg.toString());
			mDesFireEV1.selectApplication(0);
			mDesFireEV1.getApplicationIDs();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}

	private void testDiversificationHelper(IKeyConstants.KeyType ktype,
			int keyNo, byte mode, byte[] divInput) throws Exception {
		boolean result = true;
		SoftwareKeyStore sKs = (SoftwareKeyStore) iKs;

		byte[] divfKey = sKs.getDiversifiedKey(keyNo, (byte) 0, mode, divInput);
		report.log(LogLevel.INFO, TAG, "Diversified Key : "
				+ CustomModules.getUtility().dumpBytes(divfKey));
		divfKey = sKs.getDiversifiedKey(keyNo, (byte) 0, mode, divInput);
		report.log(LogLevel.INFO, TAG, "Second Time Diversified Key : "
				+ CustomModules.getUtility().dumpBytes(divfKey));

	}

	public void testDiversification2() {
		report.log(LogLevel.INFO, TAG, "Diversification, GroupName");
		report.log(LogLevel.INFO, TAG, "testDiversification, start");
		prepareSoftwareKeyStore();
		boolean result = true;
		try {
			report.log(LogLevel.INFO, TAG,
					"Diversification of DES Key with Eight byte div input");
			testDiversificationHelper(
					IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_DES,
					O_PICC_DES_KEY_NO, IKeyConstants.DIV_OPTION_DESFIRE,
					EIGHT_BYTE_DIV_INPUT);

			report.log(LogLevel.INFO, TAG,
					"Diversification of 2K3DES Key with Eight byte div input");
			testDiversificationHelper(
					IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_2K3DES,
					O_PICC_2KDES_KEY_NO, IKeyConstants.DIV_OPTION_DESFIRE,
					EIGHT_BYTE_DIV_INPUT);

			report.log(
					LogLevel.INFO,
					TAG,
					"Diversification of 2K3DES Key with Eight byte div input and halfkey diversification");
			testDiversificationHelper(
					IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_2K3DES,
					O_PICC_2KDES_KEY_NO,
					(byte) (IKeyConstants.DIV_OPTION_DESFIRE | IKeyConstants.DIV_OPTION_HALF_KEY_DIVERSIFICATION),
					EIGHT_BYTE_DIV_INPUT);

			report.log(LogLevel.INFO, TAG,
					"Diversification of 3K3DES Key with Eight byte div input");
			testDiversificationHelper(
					IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_3K3DES,
					O_PICC_3K3KDES_KEY_NO, IKeyConstants.DIV_OPTION_DESFIRE,
					EIGHT_BYTE_DIV_INPUT);

			report.log(LogLevel.INFO, TAG,
					"Diversification of AES Key with Eight byte div input");
			testDiversificationHelper(
					IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_AES128,
					O_PICC_AES_KEY_NO, IKeyConstants.DIV_OPTION_DESFIRE,
					SIXTEEN_BYTE_DIV_INPUT);
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		report.log(LogLevel.INFO, TAG, "testDiversification result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF11, FRSID");
		report.log(LogLevel.INFO, TAG, "PiccLevelCommands, end");
	}

	public void testDiversification() {
		report.log(LogLevel.INFO, TAG, "Diversification, GroupName");
		report.log(LogLevel.INFO, TAG, "testDiversification, start");
		boolean result = false;
		prepareSoftwareKeyStore();
		SoftwareKeyStore sKs = (SoftwareKeyStore) iKs;
		try {

			sKs.formatKeyEntry(DIV_KEY_STORE_SLOT1,
					IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_2K3DES);
			sKs.formatKeyEntry(DIV_KEY_STORE_SLOT2,
					IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_2K3DES);

			byte[] diversified = sKs.getDiversifiedKey(0, (byte) 0,
					IKeyConstants.DIV_OPTION_DESFIRE,
					Arrays.copyOfRange(keyDefault16, 0, 8));

			byte[] diversified2 = sKs.getDiversifiedKey(O_PICC_DES_KEY_NO,
					(byte) 0, IKeyConstants.DIV_OPTION_DESFIRE,
					Arrays.copyOfRange(keyDefault16, 0, 8));

			sKs.setKey(DIV_KEY_STORE_SLOT1, (byte) 0,
					IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_2K3DES, diversified);
			sKs.setKey(DIV_KEY_STORE_SLOT2, (byte) 0,
					IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_2K3DES,
					diversified2);

			// Check weather we will get same diversified key every time.
			report.log(
					LogLevel.INFO,
					TAG,
					CustomModules.getUtility().dumpBytes(
							sKs.getDiversifiedKey(0, (byte) 0,
									IKeyConstants.DIV_OPTION_DESFIRE,
									Arrays.copyOfRange(keyDefault16, 0, 8))));
			report.log(LogLevel.INFO, TAG, CustomModules.getUtility()
					.dumpBytes(diversified));
			report.log(
					LogLevel.INFO,
					TAG,
					CustomModules.getUtility().dumpBytes(
							sKs.getDiversifiedKey(O_PICC_DES_KEY_NO, (byte) 0,
									IKeyConstants.DIV_OPTION_DESFIRE,
									Arrays.copyOfRange(keyDefault16, 0, 8))));
			report.log(LogLevel.INFO, TAG, CustomModules.getUtility()
					.dumpBytes(diversified2));
			report.log(LogLevel.INFO, TAG,
					"###########################################################");
			report.log(
					LogLevel.INFO,
					TAG,
					CustomModules.getUtility().dumpBytes(
							sKs.getDiversifiedKey(0, (byte) 0,
									IKeyConstants.DIV_OPTION_DESFIRE,
									Arrays.copyOfRange(keyDefault16, 0, 8))));
			report.log(LogLevel.INFO, TAG, CustomModules.getUtility()
					.dumpBytes(diversified));
			report.log(
					LogLevel.INFO,
					TAG,
					CustomModules.getUtility().dumpBytes(
							sKs.getDiversifiedKey(O_PICC_DES_KEY_NO, (byte) 0,
									IKeyConstants.DIV_OPTION_DESFIRE,
									Arrays.copyOfRange(keyDefault16, 0, 8))));
			report.log(LogLevel.INFO, TAG, CustomModules.getUtility()
					.dumpBytes(diversified2));

		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SmartCardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		report.log(LogLevel.INFO, TAG, "testDiversification result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF11, FRSID");
		report.log(LogLevel.INFO, TAG, "PiccLevelCommands, end");
	}

	private void printCardDetails() {
		try {
			CardDetails objCard = mDesFireEV1.getCardDetails();
			report.log(LogLevel.INFO, TAG, "Card Name: " + objCard.cardName);
			report.log(LogLevel.INFO, TAG, "Delivery Type Name: "
					+ objCard.deliveryType);
			report.log(LogLevel.INFO, TAG, "Free Memory: " + objCard.freeMemory);
			report.log(LogLevel.INFO, TAG, "Major Version: "
					+ objCard.majorVersion);
			report.log(LogLevel.INFO, TAG, "Minor Version: "
					+ objCard.minorVersion);
			report.log(LogLevel.INFO, TAG, "Max Transcieve Length: "
					+ objCard.maxTranscieveLength);
			report.log(LogLevel.INFO, TAG, "SAK : " + objCard.sak);
			report.log(LogLevel.INFO, TAG, "Total Memory : "
					+ objCard.totalMemory);
			report.log(LogLevel.INFO, TAG, "Vendor ID : " + objCard.vendorID);
			report.log(LogLevel.INFO, TAG, "ATQA : "
					+ modules.getUtility().dumpBytes(objCard.atqa));
			report.log(LogLevel.INFO, TAG, "ATQA : "
					+ modules.getUtility().dumpBytes(objCard.historicalBytes));
		} catch (SmartCardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testChangeKeyFromDESTOAESINNative() {
		report.log(LogLevel.DEBUG, TAG,
				"changeKeyCommands Operations, GroupName");
		report.log(LogLevel.DEBUG, TAG,
				"testChangeKeyFromDESTOAESINNative, start");
		boolean result;
		try {
			int oldKeyNo = D_PICC_DES_KEY_NO;
			int newKeyNumber = D_PICC_AES_KEY_NO;
			mDesFireEV1.authenticate(AuthType.Native, oldKeyNo, (byte) 0, 0,
					(byte) 0, null);
			mDesFireEV1.changeKey(0, oldKeyNo, (byte) 0, newKeyNumber,
					(byte) 0, KeyType.AES, (byte) 0, (byte) 0, null);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.DEBUG, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.DEBUG, TAG, "Exception: ", e);
		} catch (GeneralSecurityException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.DEBUG, TAG, "Exception: ", e);
		}
		report.log(LogLevel.DEBUG, TAG,
				"NXPALIB_SW_DF05,testChangeKeyFromDESTOAESINNative result is, "
						+ result);
		report.log(LogLevel.DEBUG, TAG, "NXPALIB_SW_DF05, FRSID");
		report.log(LogLevel.DEBUG, TAG,
				"testChangeKeyFromDESTOAESINNative, end");
	}

	public void testAESAuthDiversification() {
		try {

			int D_PICC_AES_KEY_NO = 3;

			int DIV_KEY_STORE_SLOT2 = 18;

			byte[] SIXTEEN_BYTE_DIV_INPUT = new byte[] { 1, 2, 3, 4, 5, 6 };

			// byte[] SIXTEEN_BYTE_DIV_INPUT = new byte[] { 1, 2, 3,
			// 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8 };

			byte[] keyDefault16 = new byte[] { (byte) 0x01, (byte) 0x01,
					(byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01,
					(byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01,
					(byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01,
					(byte) 0x01, (byte) 0x01 };

			// Prepare card for Diversification authentication.
			// Here card PICC master key is of type AES.
			mDesFireEV1.authenticate(AuthType.AES, D_PICC_AES_KEY_NO, (byte) 0,
					0, IKeyConstants.DIV_OPTION_NODIVERSIFICATION, null);
			// 1) Format any slot in key store with AES key.
			iKs.formatKeyEntry(DIV_KEY_STORE_SLOT2,
					IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_AES128);
			// 2) Set a key which has to be diversified. here it is keyDefault16
			// --> all bytes are 1.
			iKs.setKey(DIV_KEY_STORE_SLOT2, (byte) 0,
					IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_AES128,
					keyDefault16);// All keys are one.
			// modules.getLogger().log(LogLevel.INFO,
			// "testAESAuthDiversification",CustomModules.getUtility().dumpBytes(iKs.getDiversifiedKey(DIV_KEY_STORE_SLOT2,
			// (byte) 0, IKeyConstants.DIV_OPTION_DESFIRE,
			// SIXTEEN_BYTE_DIV_INPUT)));
			// Set the diversified key in the card.
			mDesFireEV1.changeKey(0, D_PICC_AES_KEY_NO, (byte) 0,
					DIV_KEY_STORE_SLOT2, (byte) 0, KeyType.AES, (byte) 0,
					IKeyConstants.DIV_OPTION_DESFIRE, SIXTEEN_BYTE_DIV_INPUT);
			// Authenticate with the keyDefault16 with diversification input.
			mDesFireEV1.authenticate(AuthType.AES, DIV_KEY_STORE_SLOT2,
					(byte) 0, 0, IKeyConstants.DIV_OPTION_DESFIRE,
					SIXTEEN_BYTE_DIV_INPUT);

			modules.getLogger().log(LogLevel.INFO,
					"testAESAuthDiversification", "Success");
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SmartCardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void handytest() {
		report.log(LogLevel.INFO, TAG, "Restore Card Opearations, GroupName");
		report.log(LogLevel.INFO, TAG, "handytest, start");
		boolean result = true;
		// prepareSoftwareKeyStore();
		// result = readNdef();
		// testDiversification2();
		testAESAuthDiversification();
		// printCardDetails();
		report.log(LogLevel.INFO, TAG, "handytest result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF11, FRSID");
		report.log(LogLevel.INFO, TAG, "PiccLevelCommands, end");
	}

	public void testLargePayload() {
		report.log(LogLevel.INFO, TAG,
				"Payload Read/Write Ndef Operation, GroupName");
		report.log(LogLevel.INFO, TAG, "testLargePayload, start");
		boolean result = true;
		// prepareSoftwareKeyStore();
		result = readNdefLargePayload();
		// testDiversification2();
		report.log(LogLevel.INFO, TAG, "testLargePayload result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF11, FRSID");
		report.log(LogLevel.INFO, TAG, "PiccLevelCommands, end");
	}

	public void testLargePayload_4K() {
		report.log(LogLevel.INFO, TAG,
				"Payload Read/Write Ndef Operation, GroupName");
		report.log(LogLevel.INFO, TAG, "testLargePayload_4K, start");
		boolean result = true;
		// prepareSoftwareKeyStore();
		result = readNdefLargePayload_4K();
		// testDiversification2();
		report.log(LogLevel.INFO, TAG, "testLargePayload result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF11, FRSID");
		report.log(LogLevel.INFO, TAG, "PiccLevelCommands, end");
	}

	public void testSmallPayload() {
		report.log(LogLevel.INFO, TAG,
				"Payload Read/Write Ndef Operation, GroupName");
		report.log(LogLevel.INFO, TAG, "testSmallPayload, start");
		boolean result = true;
		// prepareSoftwareKeyStore();
		result = readNdefSmallPayload();
		// testDiversification2();
		report.log(LogLevel.INFO, TAG, "testSmallPayload result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF11, FRSID");
		report.log(LogLevel.INFO, TAG, "PiccLevelCommands, end");
	}

	private NdefRecordWrapper createTextRecord(String payload, Locale locale,
			boolean encodeInUtf8) {

		byte[] langBytes = locale.getLanguage().getBytes(
				Charset.forName("US-ASCII"));
		Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset
				.forName("UTF-16");
		byte[] textBytes = payload.getBytes(utfEncoding);
		int utfBit = encodeInUtf8 ? 0 : (1 << 7);
		char status = (char) (utfBit + langBytes.length);
		byte[] data = new byte[1 + langBytes.length + textBytes.length];
		data[0] = (byte) status;
		System.arraycopy(langBytes, 0, data, 1, langBytes.length);
		System.arraycopy(textBytes, 0, data, 1 + langBytes.length,
				textBytes.length);

		NdefRecordWrapper record = new NdefRecordWrapper(
				NdefRecordWrapper.TNF_WELL_KNOWN, NdefRecordWrapper.RTD_TEXT,
				new byte[0], data);
		return record;
	}

	
	public void testRestoreCard() {
		report.log(LogLevel.INFO, TAG, "Restore Card Opearations, GroupName");
		report.log(LogLevel.INFO, TAG, "testRestoreCard, start");
		boolean result = false;
		if (tryAESModeToRecover()) {
			result = true;
		} else {
			if (tryNativeAndISOModeToRecover()) {
				result = true;
			}
		}
		report.log(LogLevel.INFO, TAG, "testRestoreCard result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF11, FRSID");
		report.log(LogLevel.INFO, TAG, "PiccLevelCommands, end");
	}

	public DesfireEV1Test(IDESFireEV1 mDesFire, CustomModules mCustomModules) {
		this.mDesFireEV1 = mDesFire;
		iKs = mCustomModules.getKeyStore();
		this.mDesFireEV1 = mDesFire;
		this.modules = mCustomModules;
		this.report = mCustomModules.getLogger();
	}

	public DesfireEV1Test() {
	}

	public static void setParams(IDESFireEV1 mDesFire, ILogger customLogger,IKeyStore mIks) {
		mDesFireEV1 = mDesFire;
		report = customLogger;
		iKs=mIks;
	}

	/**
	 * Software keystore initialization.
	 */
	void prepareSoftwareKeyStore() {
		if (report.isLoggingEnabled(LogLevel.INFO)) {
			return;
		}
		// libInstance = NxpNfcLib.getInstance();

		//iKs = modules.getKeyStore();
		try {
			// First four zero Keys for PICC.
			iKs.formatKeyEntry(D_PICC_DES_KEY_NO,
					IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_DES);
			iKs.formatKeyEntry(D_PICC_2KDES_KEY_NO,
					IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_2K3DES);
			iKs.formatKeyEntry(D_PICC_3K3KDES_KEY_NO,
					IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_3K3DES);
			iKs.formatKeyEntry(D_PICC_AES_KEY_NO,
					IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_AES128);
			// Second four one Keys for PICC.
			iKs.formatKeyEntry(O_PICC_DES_KEY_NO,
					IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_DES);
			iKs.formatKeyEntry(O_PICC_2KDES_KEY_NO,
					IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_2K3DES);
			iKs.formatKeyEntry(O_PICC_3K3KDES_KEY_NO,
					IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_3K3DES);
			iKs.formatKeyEntry(O_PICC_AES_KEY_NO,
					IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_AES128);

			iKs.setKey(O_PICC_DES_KEY_NO, (byte) 0,
					IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_DES, keyDefault16);
			iKs.setKey(O_PICC_2KDES_KEY_NO, (byte) 0,
					IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_2K3DES,
					keyDefault16);
			iKs.setKey(O_PICC_3K3KDES_KEY_NO, (byte) 0,
					IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_3K3DES,
					keyDefault24);
			iKs.setKey(O_PICC_AES_KEY_NO, (byte) 0,
					IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_AES128,
					keyDefault16);

			// First four zero Keys for APP.
			iKs.formatKeyEntry(D_APP_DES_KEY_NO,
					IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_DES);
			iKs.formatKeyEntry(D_APP_2KDES_KEY_NO,
					IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_2K3DES);
			iKs.formatKeyEntry(D_APP_3K3KDES_KEY_NO,
					IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_3K3DES);
			iKs.formatKeyEntry(D_APP_AES_KEY_NO,
					IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_AES128);
			// Second four one Keys for APP.
			iKs.formatKeyEntry(O_APP_DES_KEY_NO,
					IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_DES);
			iKs.formatKeyEntry(O_APP_2KDES_KEY_NO,
					IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_2K3DES);
			iKs.formatKeyEntry(O_APP_3K3KDES_KEY_NO,
					IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_3K3DES);
			iKs.formatKeyEntry(O_APP_AES_KEY_NO,
					IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_AES128);

			iKs.setKey(O_APP_DES_KEY_NO, (byte) 0,
					IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_DES, keyDefault16);
			iKs.setKey(O_APP_2KDES_KEY_NO, (byte) 0,
					IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_2K3DES,
					keyDefault16);
			iKs.setKey(O_APP_3K3KDES_KEY_NO, (byte) 0,
					IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_3K3DES,
					keyDefault24);
			iKs.setKey(O_APP_AES_KEY_NO, (byte) 0,
					IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_AES128,
					keyDefault16);

		} catch (SmartCardException e) {
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		modules.setKeyStore(iKs);
	}

	/**
	 * Hardware keystore initialization.
	 */
	void prepareHardwareKeyStore() {
		if (!report.isLoggingEnabled(LogLevel.INFO)) {
			return;
		}
		// libInstance = NxpNfcLib.getInstance();

		/*
		 * try { KeyStoreFactory.getInstance().getHardwareKeyStore(
		 * MainActivity.context, new IHardwareKeystoreConnectedCallback() {
		 * 
		 * @Override public void onHardwareKeyStoreConnected( HardwareKeyStore
		 * objHardware) {
		 * 
		 * try { objHardware.authenticateHost( KEY_AES128_DEFAULT,
		 * IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_AES128,
		 * SamAV2.CommunicationType.Plain);
		 * 
		 * HardwareKeyStore iKs = objHardware;
		 * 
		 * // First four zero Keys for PICC.
		 * iKs.formatKeyEntry(D_PICC_DES_KEY_NO,
		 * IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_DES);
		 * iKs.formatKeyEntry(D_PICC_2KDES_KEY_NO,
		 * IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_2K3DES);
		 * iKs.formatKeyEntry(D_PICC_3K3KDES_KEY_NO,
		 * IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_3K3DES);
		 * iKs.formatKeyEntry(D_PICC_AES_KEY_NO,
		 * IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_AES128); // Second four one
		 * Keys for PICC. iKs.formatKeyEntry(O_PICC_DES_KEY_NO,
		 * IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_DES);
		 * iKs.formatKeyEntry(O_PICC_2KDES_KEY_NO,
		 * IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_2K3DES);
		 * iKs.formatKeyEntry(O_PICC_3K3KDES_KEY_NO,
		 * IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_3K3DES);
		 * iKs.formatKeyEntry(O_PICC_AES_KEY_NO,
		 * IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_AES128);
		 * 
		 * iKs.setKey(O_PICC_DES_KEY_NO, (byte) 0,
		 * IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_DES, keyDefault16);
		 * iKs.setKey(O_PICC_2KDES_KEY_NO, (byte) 0,
		 * IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_2K3DES, keyDefault16);
		 * iKs.setKey(O_PICC_3K3KDES_KEY_NO, (byte) 0,
		 * IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_3K3DES, keyDefault24);
		 * iKs.setKey(O_PICC_AES_KEY_NO, (byte) 0,
		 * IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_AES128, keyDefault16);
		 * 
		 * // First four zero Keys for APP. iKs.formatKeyEntry(D_APP_DES_KEY_NO,
		 * IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_DES);
		 * iKs.formatKeyEntry(D_APP_2KDES_KEY_NO,
		 * IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_2K3DES);
		 * iKs.formatKeyEntry(D_APP_3K3KDES_KEY_NO,
		 * IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_3K3DES);
		 * iKs.formatKeyEntry(D_APP_AES_KEY_NO,
		 * IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_AES128); // Second four one
		 * Keys for APP. iKs.formatKeyEntry(O_APP_DES_KEY_NO,
		 * IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_DES);
		 * iKs.formatKeyEntry(O_APP_2KDES_KEY_NO,
		 * IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_2K3DES);
		 * iKs.formatKeyEntry(O_APP_3K3KDES_KEY_NO,
		 * IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_3K3DES);
		 * iKs.formatKeyEntry(O_APP_AES_KEY_NO,
		 * IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_AES128);
		 * 
		 * iKs.setKey(O_APP_DES_KEY_NO, (byte) 0,
		 * IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_DES, keyDefault16);
		 * iKs.setKey(O_APP_2KDES_KEY_NO, (byte) 0,
		 * IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_2K3DES, keyDefault16);
		 * iKs.setKey(O_APP_3K3KDES_KEY_NO, (byte) 0,
		 * IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_3K3DES, keyDefault24);
		 * iKs.setKey(O_APP_AES_KEY_NO, (byte) 0,
		 * IKeyConstants.KeyType.KEYSTORE_KEY_TYPE_AES128, keyDefault16);
		 * 
		 * libInstance.loadKeyStore(objHardware); } catch (SmartCardException e)
		 * { // TODO Auto-generated catch block e.printStackTrace(); } catch
		 * (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } catch (GeneralSecurityException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 * 
		 * } }); } catch (SmartCardException e2) { // TODO Auto-generated catch
		 * block e2.printStackTrace(); }
		 */

	}

	
	public void testDiversifiedSecurityAPIS() {
		report.log(LogLevel.INFO, TAG, "securityCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testDiversifiedSecurityAPIS, start");
		// First format.
		testselectApplicationPICCP1();
		testauthenticatePICCNativeP1();
		testformatP1();

		testauthenticatePICCNativeP1();

		testgetKeySettingsP1();
		testgetKeyVersionP1();
		testauthenticatePICCNativeP1();

		// testchangeKeyDESToDESP1();
		testchangeKeyDESToDESKeyDiversfiedP1();

		selectDefaultKeyasNewKey();

		// testauthenticatePICCNativeP1();
		testauthenticatePICCNativeDiversfiedP1();
		// testchangeKeyDESToDESP1();
		testchangeKeyDESToDESKeyDiversfiedP1();

		// At this stage card key should be normal ones.
		// //////////////////////////////////////////////////////

		selectDefaultKeyasOldKey();
		testauthenticatePICC2K3DESP2();
		// testchangeKeyDESTo2K3DESP5();
		testchangeKeyDESTo2K3DESDiversifiedP5();

		// testauthenticatePICC2K3DESP2();
		testauthenticatePICC2K3DESDiversfiedP2();

		// testchangeKey2K3DESTo2K3DESP2();
		testchangeKey2K3DESTo2K3DESBothKeysDiversifiedP2();

		selectDefaultKeyasNewKey();

		// testauthenticatePICC2K3DESP2();
		testauthenticatePICC2K3DESDiversfiedP2();

		// testchangeKey2K3DESTo2K3DESP2();
		testchangeKey2K3DESTo2K3DESOnlyNewDiversifiedP2();

		// //At this stage card key should be normal ones.
		// ///////////////////////////////////////////////////////////////

		selectDefaultKeyasOldKey();

		testauthenticatePICC2K3DESP2();
		testchangeKeyDESTo3K3DESP6();

		testauthenticatePICC3K3DESP3();
		testchangeKey3K3DESTo3K3DESP3();

		selectDefaultKeyasNewKey();

		testauthenticatePICC3K3DESP3();
		testchangeKey3K3DESTo3K3DESP3();

		testauthenticatePICC3K3DESP3();
		testchangeKey3K3DESToDESP9();

		selectDefaultKeyasOldKey();

		testauthenticatePICCNativeP1();
		testchangeKeyDESToAESP7();

		testauthenticatePICCAESP4();
		testchangeKeyAESToAESP4();

		selectDefaultKeyasNewKey();

		testauthenticatePICCAESP4();
		testchangeKeyAESToAESP4();

		testauthenticatePICCAESP4();
		testchangeKeyAESToDESP8();
		// ---------------------- // -----------------------//

		testauthenticatePICCNativeP1();
		testchangeKeyDESTo2K3DESP5();

		testauthenticatePICCNativeP1();
		testchangeKey2K3DESTo3K3DESP11();

		testauthenticatePICC3K3DESP3();
		testchangeKey3K3DESTo2K3DESP13();

		testauthenticatePICCNativeP1();
		testchangeKey2K3DESToAESP12();

		testauthenticatePICCAESP4();
		testchangeKeyAESTo2K3DESP14();

		// --------------------------// ------------------------//
		testauthenticatePICCNativeP1();
		testchangeKey2K3DESTo3K3DESP11();

		testauthenticatePICC3K3DESP3();
		testchangeKey3K3DESToAESP15();

		testauthenticatePICCAESP4();
		testchangeKeyAESTo3K3DESP16();
		// --------------------------// ------------------------//
		testauthenticatePICC3K3DESP3();
		testchangeKey3K3DESToAESP15();

		testauthenticatePICCAESP4();
		testchangeKeyAESToDESP8();

		// End
		testauthenticatePICCNativeP1();
		testformatP1();

		report.log(LogLevel.INFO, TAG,
				"testDiversifiedSecurityAPIS result is " + true);
		report.log(LogLevel.INFO, TAG, "testDiversifiedSecurityAPIS, end");
	}

	
	public void testSecurityAPIS() {
		report.log(LogLevel.INFO, TAG, "securityCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testSecurityAPIS, start");
		// First format.
		testselectApplicationPICCP1();
		testauthenticatePICCNativeP1();
		testformatP1();

		testauthenticatePICCNativeP1();

		testgetKeySettingsP1();
		testgetKeyVersionP1();
		testauthenticatePICCNativeP1();
		testchangeKeyDESToDESP1();

		selectDefaultKeyasNewKey();

		testauthenticatePICCNativeP1();
		testchangeKeyDESToDESP1();

		testauthenticatePICC2K3DESP2();
		testchangeKeyDESTo2K3DESP5();

		testauthenticatePICC2K3DESP2();
		testchangeKey2K3DESTo2K3DESP2();

		selectDefaultKeyasNewKey();

		testauthenticatePICC2K3DESP2();
		testchangeKey2K3DESTo2K3DESP2();

		testauthenticatePICC2K3DESP2();
		testchangeKeyDESTo3K3DESP6();

		testauthenticatePICC3K3DESP3();
		testchangeKey3K3DESTo3K3DESP3();

		selectDefaultKeyasNewKey();

		testauthenticatePICC3K3DESP3();
		testchangeKey3K3DESTo3K3DESP3();

		testauthenticatePICC3K3DESP3();
		testchangeKey3K3DESToDESP9();

		testauthenticatePICCNativeP1();
		testchangeKeyDESToAESP7();

		testauthenticatePICCAESP4();
		testchangeKeyAESToAESP4();

		selectDefaultKeyasNewKey();

		testauthenticatePICCAESP4();
		testchangeKeyAESToAESP4();

		testauthenticatePICCAESP4();
		testchangeKeyAESToDESP8();
		// ---------------------- // -----------------------//
		testauthenticatePICCNativeP1();
		testchangeKeyDESTo2K3DESP5();

		testauthenticatePICCNativeP1();
		testchangeKey2K3DESTo3K3DESP11();

		testauthenticatePICC3K3DESP3();
		testchangeKey3K3DESTo2K3DESP13();

		testauthenticatePICCNativeP1();
		testchangeKey2K3DESToAESP12();

		testauthenticatePICCAESP4();
		testchangeKeyAESTo2K3DESP14();

		// --------------------------// ------------------------//
		testauthenticatePICCNativeP1();
		testchangeKey2K3DESTo3K3DESP11();

		testauthenticatePICC3K3DESP3();
		testchangeKey3K3DESToAESP15();

		testauthenticatePICCAESP4();
		testchangeKeyAESTo3K3DESP16();
		// --------------------------// ------------------------//
		testauthenticatePICC3K3DESP3();
		testchangeKey3K3DESToAESP15();

		testauthenticatePICCAESP4();
		testchangeKeyAESToDESP8();

		// End
		testauthenticatePICCNativeP1();
		testformatP1();

		report.log(LogLevel.INFO, TAG, "testSecurityAPIS result is " + true);
		report.log(LogLevel.INFO, TAG, "testSecurityAPIS, end");
	}

	
	public void testPiccLevelCommands() {
		report.log(LogLevel.INFO, TAG, "PiccLevelCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testPiccLevelCommands, start");
		// First format.
		testselectApplicationPICCP1();
		testauthenticatePICCNativeP1();
		testformatP1();
		testauthenticatePICCNativeP1();
		// testcreateApplicationP1();
		testcreateApplicationP2();
		testselectApplicationTestAppP2();
		testselectApplicationPICCP1();
		testgetApplicationIDsP1();
		testgetDFNameP1();
		testauthenticatePICCNativeP1();
		testgetCardUIDP1();
		testgetFreeMemP1();

		testselectApplicationPICCP1();
		testauthenticatePICCNativeP1();
		testformatP1();
		testGetCardDetails();

		report.log(LogLevel.INFO, TAG,
				"testPiccLevelCommands result is " + true);
		report.log(LogLevel.INFO, TAG, "testPiccLevelCommands, end");

	}

	
	
	public void testCreateApplicationAndFiles() {
		try {

			int D_PICC_DES_KEY_NO_LOCAL = 0;
			byte[] ISO_APP_ID_LOCAL = new byte[] { 0x0E, 0x00 };
			byte[] isoAppNameLocal = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 0, 1, 2, 3,
					4, 5, 6 };
			int ISOSTD_DATA_FILE_NO_LOCAL = 2;
			byte[] ISO_STD_DATA_FILE_NO_LOCAL = new byte[] { 0x0E, 0x01 };
			int DATA_FILE_SIZE_LOCAL = 20;
			int ISOLINER_RECORD_FILE_NO_LOCAL = 3;
			byte[] ISO_LINER_RECORD_FILE_NO_LOCAL = new byte[] { 0x0E, 0x04 };
			int NO_OF_RECORDS = 10;
			int D_PICC_AES_KEY_NO_LOCAL = 3;

			mDesFireEV1.selectApplication(0);

			mDesFireEV1.authenticate(DESFireEV1.AuthType.Native,
					D_PICC_DES_KEY_NO_LOCAL, (byte) 0, 0, (byte) 0, null);
			mDesFireEV1.format();
			mDesFireEV1
					.createApplication(
							1,
							(byte) (0x10 | DESFireEV1.KSONE_CONFIG_CHANGABLE
									| DESFireEV1.KSONE_FILE_DEL_NO_MKEY
									| DESFireEV1.KSONE_GET_NO_MKEY | DESFireEV1.KSONE_APP_MKEY_CHANGABLE),
							10, DESFireEV1.KeyType.AES, ISO_APP_ID_LOCAL,
							isoAppNameLocal);
			mDesFireEV1.selectApplication(1);

			mDesFireEV1.createFile(ISOSTD_DATA_FILE_NO_LOCAL,
					ISO_STD_DATA_FILE_NO_LOCAL, new StdDataFileSettings(
							DESFireEV1.CommunicationType.Enciphered, 0, 0, 0,
							0, DATA_FILE_SIZE_LOCAL));

			mDesFireEV1.createFile(ISOLINER_RECORD_FILE_NO_LOCAL,
					ISO_LINER_RECORD_FILE_NO_LOCAL,
					new LinearRecordFileSettings(
							DESFireEV1.CommunicationType.MACed, 0, 0, 0, 0, 16,
							NO_OF_RECORDS, 0));

			mDesFireEV1.authenticate(DESFireEV1.AuthType.AES,
					D_PICC_AES_KEY_NO_LOCAL, (byte) 0, 0, (byte) 0, null);

			mDesFireEV1.writeData(ISOSTD_DATA_FILE_NO_LOCAL, 0, new byte[] {
					0x30, 0x30, 0x30, 0x30 });

			byte[] StdFile_value = mDesFireEV1.readData(
					ISOSTD_DATA_FILE_NO_LOCAL, 0, 4);

			report.log(LogLevel.INFO, TAG, "Std Data File Data: "
					+ modules.getUtility().dumpBytes(StdFile_value));

			mDesFireEV1.writeRecord(ISOLINER_RECORD_FILE_NO_LOCAL, 0, data);
			mDesFireEV1.commitTransaction();
			byte[] recordData = mDesFireEV1.readRecords(
					ISOLINER_RECORD_FILE_NO_LOCAL, 0, 1);

			report.log(LogLevel.INFO, TAG, "Record Read: "
					+ modules.getUtility().dumpBytes(recordData));

			mDesFireEV1.selectApplication(0);
			mDesFireEV1.format();
		} catch (Exception e) {

		}
	}

	
	public void testApplicationAndDataManipulationCommands() {
		report.log(LogLevel.INFO, TAG,
				"ApplicationAndDataManipulationCommands, GroupName");
		report.log(LogLevel.INFO, TAG,
				"testApplicationAndDataManipulationCommands, start");
		// First format.
		mDesFireEV1.setCommandSet(CommandSet.ISO);
		testselectApplicationPICCP1();
		testauthenticatePICCNativeP1();
		testformatP1();

		// //----------------------////----------------------///
		testauthenticatePICCNativeP1();

		testcreateApplicationP1();
		// testcreateApplicationP3();// AES key Type
		testselectApplicationTestAppP2();

		testgetFreeMemP1();
		testcreateStandardFileP1();
		testgetFreeMemP1();
		testcreateBackupDataFileP1();
		testcreateValueFileP1();
		testcreateLinearRecordFileP1();
		testcreateCyclicRecordFileP1();

		testauthenticatePICCNativeP1();
		// testauthenticatePICCAESP4(); // AES key Type
		testWriteDataP1();
		testReadDataP1();
		testCreditP1();
		testDebitP1();
		testLinerWriteRecordP1();
		testLinerReadRecordP1();
		testCyclicWriteRecordP1();
		testCyclicReadRecordP1();
		testGetValueP1();
		testClearCyclicRecordP1();
		testClearLinerRecordP1();

		testselectApplicationTestAppP2();
		testGetFileIDsP1();
		testGetSTDFileSettingsP1();
		testGetBACKUPFileSettingsP1();
		testGetValueFileSettingsP1();
		testGetLinerFileSettingsP1();
		testGetCyclicFileSettingsP1();

		testauthenticatePICCNativeP1();
		// testauthenticatePICCAESP4();// AES key Type
		testchangeStdFileSettingsP1();
		testchangeBackupDataFileSettingsP1();
		testauthenticatePICCNativeP1();
		// testauthenticatePICCAESP4();// AES key Type
		testchangeLinearRecordFileSettingsP1();
		testchangeCyclicRecordFileSettingsP1();
		testchangeValueFileSettingsP1();

		testGetSTDFileSettingsP1();
		testGetBACKUPFileSettingsP1();
		testGetValueFileSettingsP1();
		testGetLinerFileSettingsP1();
		testGetCyclicFileSettingsP1();

		testDeleteStandardFileP1();
		testchangeKeySettingsP1();
		testselectApplicationTestAppP2();
		testauthenticatePICCNativeP1();
		// testauthenticatePICCAESP4();// AES key Type
		testDeleteBackupDataFileP1();
		testDeleteCyclicRecordFileP1();
		testDeleteLinearRecordFileP1();
		testDeleteValueFileP1();
		testdeleteApplicationP1();

		// /////////////////////
		// -----------------////////////////---------------//
		testselectApplicationPICCP1();
		testauthenticatePICCNativeP1();
		testformatP1();

		testcreateApplicationP2();
		testselectApplicationTestAppP2();
		testcreateISOStandardFileP1();
		testcreateISOBackupDataFileP1();
		testcreateISOLinearRecordFileP1();
		testcreateISOCyclicRecordFileP1();
		testauthenticatePICCAESP4();
		testWriteISODataP1();
		testReadISODataP1();
		testLinerWriteISORecordP1();
		testLinerReadISORecordP1();
		testCyclicWriteISORecordP1();
		testCyclicReadISORecordP1();
		testselectApplicationTestAppP2();
		testGetFileIDsP1();
		testGetISOFileIDsP1();

		testselectApplicationPICCP1();
		testauthenticatePICCNativeP1();
		testformatP1();

		report.log(LogLevel.INFO, TAG,
				"testApplicationAndDataManipulationCommands result is " + true);
		report.log(LogLevel.INFO, TAG,
				"testApplicationAndDataManipulationCommands, end");
	}

	
	public void testStdandardCommands() {
		report.log(LogLevel.INFO, TAG, "ISOStandardCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testISO7816StdandardCommands, start");
		boolean result = true;
		// First format.
		testselectApplicationPICCP1();
		testauthenticatePICCNativeP1();
		testformatP1();

		testcreateApplicationP4();
		testselectApplicationTestAppP2();
		testcreateISOStandardFileP2();
		testcreateISOLinearRecordFileP2();

		// testselectApplicationPICCP1();
		// testauthenticatePICCNativeP1();
		// testformatP1();

		report.log(LogLevel.INFO, TAG,
				"testISO7816StdandardCommands result is " + result);
		report.log(LogLevel.INFO, TAG, "testISO7816StdandardCommands, end");
	}

	
	public void testFormat() {
		testselectApplicationPICCP1();
		testauthenticatePICCNativeP1();
		testformatP1();
	}

	// Before running the below ISO test cases execute : testStdandardCommands
	// after close and connect
	
	public void testISOCommandsp1() {
		try {
			mDesFireEV1.getReader().close();
			mDesFireEV1.getReader().connect();
		} catch (ReaderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		testSetCommandSet();
		testISOAuthP1();
		testISOSelectMasterFileP2();
		// testLogic.testISOSelectFileP1();
		testISOSelectFileEFunderDF_StandardFileP1();
		testISOAuthP1();
		testISOUpdateBinaryP1();
		testISOReadBinaryP1();
		testISOSelectFileEFunderDF_RecordFileP1();
		testISOAuthP1();
		testISOAppendRecordP1();
		testISOReadRecordP1();
	}

	
	public void testNdefFunctionality() {
		report.log(LogLevel.INFO, TAG, "NDEFFunctionality, GroupName");
		report.log(LogLevel.INFO, TAG, "testNdefFunctionality, start");
		boolean result = true;

		testselectApplicationPICCP1();
		testauthenticatePICCNativeP1();
		testselectApplicationPICCP1();
		testFormatT4T();
		testIsT4T();
		testWriteNdefMessageWrapper();
		testReadNdefMessageWrapper();

		// testselectApplicationPICCP1();
		// testauthenticatePICCNativeP1();
		// testformatP1();

		report.log(LogLevel.INFO, TAG, "testNdefFunctionality result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "testNdefFunctionality, end");
	}

	
	public void testNdefFunctionalityP1() {
		report.log(LogLevel.INFO, TAG, "testNdefFunctionalityP1, GroupName");
		report.log(LogLevel.INFO, TAG, "testNdefFunctionalityP1, start");
		boolean result = true;

		testSetCommandSet();
		// testselectApplicationPICCP1();
		// testauthenticatePICCNativeP1();
		// testFormatT4T();
		testselectApplicationPICCP1();
		testIsT4T();
		testWriteNdefMessageWrapper();
		testReadNdefMessageWrapper();

		// testselectApplicationPICCP1();
		// testauthenticatePICCNativeP1();
		// testformatP1();

		report.log(LogLevel.INFO, TAG, "testNdefFunctionality result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "testNdefFunctionality, end");
	}

	
	public void testDESFireEV1Card() {

		report.log(LogLevel.INFO, TAG, "DESFIRE EV1 Card Test, GroupName");
		report.log(LogLevel.INFO, TAG, "testDESFireEV1Card, start");

		report.log(LogLevel.INFO, TAG,
				".....................TESTING SECURITY RELATED APIS START.....................");
		testSecurityAPIS();
		report.log(
				LogLevel.INFO,
				TAG,
				".....................TESTING SECURITY RELATED APIS END........................");
		report.log(
				LogLevel.INFO,
				TAG,
				"///////////////////////////////////////////////////////////////////////////////");
		report.log(
				LogLevel.INFO,
				TAG,
				".....................TESTING PICC LEVEL APIS START............................");
		testPiccLevelCommands();
		report.log(
				LogLevel.INFO,
				TAG,
				".....................TESTING PICC LEVEL APIS END..............................");
		report.log(
				LogLevel.INFO,
				TAG,
				"///////////////////////////////////////////////////////////////////////////////");
		report.log(
				LogLevel.INFO,
				TAG,
				".....................TESTING APP AND DATA MANIPULATION APIS START.............");
		testApplicationAndDataManipulationCommands();
		report.log(
				LogLevel.INFO,
				TAG,
				".....................TESTING APP AND DATA MANIPULATION APIS END...............");
		report.log(
				LogLevel.INFO,
				TAG,
				"///////////////////////////////////////////////////////////////////////////////");
		report.log(LogLevel.INFO, TAG,
				".....................TESTING STANDARD APIS START.....................");
		testStdandardCommands();
		report.log(LogLevel.INFO, TAG,
				".....................TESTING STANDARD APIS END.....................");
		report.log(
				LogLevel.INFO,
				TAG,
				".....................TESTING ISO 7816 STANDARD APIS START........................");
		try {
			mDesFireEV1.getReader().close();
			mDesFireEV1.getReader().connect();
		} catch (ReaderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		testISOCommandsp1();
		report.log(
				LogLevel.INFO,
				TAG,
				".....................TESTING ISO 7816 STANDARD APIS END........................");
		report.log(
				LogLevel.INFO,
				TAG,
				"///////////////////////////////////////////////////////////////////////////////");
		report.log(
				LogLevel.INFO,
				TAG,
				".....................TESTING NDEF FUNCTIONALITY START..........................");
		try {
			mDesFireEV1.getReader().close();
			mDesFireEV1.getReader().connect();
		} catch (ReaderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		testNdefFunctionality();
		report.log(
				LogLevel.INFO,
				TAG,
				".....................TESTING NDEF FUNCTIONALITY END............................");

		report.log(LogLevel.INFO, TAG, "testDESFireEV1Card result is " + true);
		report.log(LogLevel.INFO, TAG, "testDESFireEV1Card, end");
	}
	
	//TODO
	public void testGenerateDESFireEV1CardReport() {
		System.out.println("########################");
		testauthenticatePICCNativeP1();
	}

	
	public void testGenerateDESFireEV1CardReportA() {

		report.log(LogLevel.DEBUG, TAG,
				"DESFIRE EV1 Card Report Generation, GroupName");
		report.log(LogLevel.DEBUG, TAG,
				"testGenerateDESFireEV1CardReport, start");

		// First format.
		report.log(
				LogLevel.INFO,
				TAG,
				".....................TESTING SECURITY RELATED APIS START........................");
		testselectApplicationPICCP1();
		testauthenticatePICCNativeP1();
		testformatP1();
		testgetLibVersionP1();
		// //Create application on all overriden methods
		// iNxpiTag.disableLogging(LogLevel.INFO);
		// testAllCreateAppliaction();
		// iNxpiTag.enableLogging(LogLevel.INFO);
		//
		report.disableLogging(LogLevel.INFO);
		testauthenticatePICCNativeP1();
		report.enableLogging(LogLevel.INFO);

		testgetKeySettingsP1();
		testgetKeyVersionP1();

		report.disableLogging(LogLevel.INFO);
		testauthenticatePICCNativeP1();
		report.enableLogging(LogLevel.INFO);
		//
		testchangeKeyDESToDESP1();

		selectDefaultKeyasNewKey();
		//
		report.disableLogging(LogLevel.INFO);
		testauthenticatePICCNativeP1();
		report.enableLogging(LogLevel.INFO);
		//
		report.disableLogging(LogLevel.INFO);
		testchangeKeyDESToDESP1();
		report.enableLogging(LogLevel.INFO);

		testauthenticatePICC2K3DESP2();
		testchangeKeyDESTo2K3DESP5();

		report.disableLogging(LogLevel.INFO);
		testauthenticatePICC2K3DESP2();
		report.enableLogging(LogLevel.INFO);
		testchangeKey2K3DESTo2K3DESP2();

		selectDefaultKeyasNewKey();

		report.disableLogging(LogLevel.INFO);
		testauthenticatePICC2K3DESP2();
		report.enableLogging(LogLevel.INFO);
		report.disableLogging(LogLevel.INFO);
		testchangeKey2K3DESTo2K3DESP2();
		report.enableLogging(LogLevel.INFO);

		report.disableLogging(LogLevel.INFO);
		testauthenticatePICC2K3DESP2();
		report.enableLogging(LogLevel.INFO);
		testchangeKeyDESTo3K3DESP6();

		testauthenticatePICC3K3DESP3();
		testchangeKey3K3DESTo3K3DESP3();

		selectDefaultKeyasNewKey();

		report.disableLogging(LogLevel.INFO);
		testauthenticatePICC3K3DESP3();
		report.enableLogging(LogLevel.INFO);
		report.disableLogging(LogLevel.INFO);
		testchangeKey3K3DESTo3K3DESP3();
		report.enableLogging(LogLevel.INFO);

		report.disableLogging(LogLevel.INFO);
		testauthenticatePICC3K3DESP3();
		report.enableLogging(LogLevel.INFO);
		testchangeKey3K3DESToDESP9();

		report.disableLogging(LogLevel.INFO);
		testauthenticatePICCNativeP1();
		report.enableLogging(LogLevel.INFO);
		testchangeKeyDESToAESP7();

		testauthenticatePICCAESP4();
		testchangeKeyAESToAESP4();

		selectDefaultKeyasNewKey();
		//
		// NXPITag.disableLogging();
		testauthenticatePICCAESP4();

		report.enableLogging(LogLevel.INFO);
		report.disableLogging(LogLevel.INFO);
		testchangeKeyAESToAESP4();
		report.enableLogging(LogLevel.INFO);

		report.disableLogging(LogLevel.INFO);
		testauthenticatePICCAESP4();
		report.enableLogging(LogLevel.INFO);
		testchangeKeyAESToDESP8();
		// // ---------------------- // -----------------------//
		report.disableLogging(LogLevel.INFO);
		testauthenticatePICCNativeP1();
		report.enableLogging(LogLevel.INFO);
		report.disableLogging(LogLevel.INFO);
		testchangeKeyDESTo2K3DESP5();
		report.enableLogging(LogLevel.INFO);

		report.disableLogging(LogLevel.INFO);
		testauthenticatePICCNativeP1();
		report.enableLogging(LogLevel.INFO);
		testchangeKey2K3DESTo3K3DESP11();

		report.disableLogging(LogLevel.INFO);
		testauthenticatePICC3K3DESP3();
		report.enableLogging(LogLevel.INFO);
		testchangeKey3K3DESTo2K3DESP13();

		report.disableLogging(LogLevel.INFO);
		testauthenticatePICCNativeP1();
		report.enableLogging(LogLevel.INFO);
		testchangeKey2K3DESToAESP12();

		report.disableLogging(LogLevel.INFO);
		testauthenticatePICCAESP4();
		report.enableLogging(LogLevel.INFO);
		testchangeKeyAESTo2K3DESP14();

		// // --------------------------// ------------------------//
		report.disableLogging(LogLevel.INFO);
		testauthenticatePICCNativeP1();
		report.enableLogging(LogLevel.INFO);
		report.disableLogging(LogLevel.INFO);
		testchangeKey2K3DESTo3K3DESP11();
		report.enableLogging(LogLevel.INFO);

		report.disableLogging(LogLevel.INFO);
		testauthenticatePICC3K3DESP3();
		report.enableLogging(LogLevel.INFO);
		testchangeKey3K3DESToAESP15();

		report.disableLogging(LogLevel.INFO);
		testauthenticatePICCAESP4();
		report.enableLogging(LogLevel.INFO);
		testchangeKeyAESTo3K3DESP16();
		// // --------------------------// ------------------------//
		report.disableLogging(LogLevel.INFO);
		testauthenticatePICC3K3DESP3();
		report.enableLogging(LogLevel.INFO);
		report.disableLogging(LogLevel.INFO);
		testchangeKey3K3DESToAESP15();
		report.enableLogging(LogLevel.INFO);

		report.disableLogging(LogLevel.INFO);
		testauthenticatePICCAESP4();
		report.enableLogging(LogLevel.INFO);
		report.disableLogging(LogLevel.INFO);
		testchangeKeyAESToDESP8();
		report.enableLogging(LogLevel.INFO);

		// End
		report.disableLogging(LogLevel.INFO);
		testauthenticatePICCNativeP1();
		report.enableLogging(LogLevel.INFO);
		report.disableLogging(LogLevel.INFO);
		testformatP1();
		report.enableLogging(LogLevel.INFO);
		report.log(
				LogLevel.INFO,
				TAG,
				".....................TESTING SECURITY RELATED APIS END........................");
		report.log(
				LogLevel.INFO,
				TAG,
				"///////////////////////////////////////////////////////////////////////////////");
		report.log(
				LogLevel.INFO,
				TAG,
				".....................TESTING PICC LEVEL APIS START............................");
		// First format.
		report.disableLogging(LogLevel.INFO);
		testselectApplicationPICCP1();
		report.enableLogging(LogLevel.INFO);
		report.disableLogging(LogLevel.INFO);
		testauthenticatePICCNativeP1();
		report.enableLogging(LogLevel.INFO);
		report.disableLogging(LogLevel.INFO);
		testformatP1();
		report.enableLogging(LogLevel.INFO);

		report.disableLogging(LogLevel.INFO);
		testauthenticatePICCNativeP1();
		report.enableLogging(LogLevel.INFO);
		// testcreateApplicationP1();
		testcreateApplicationP2();
		testselectApplicationTestAppP2();
		report.disableLogging(LogLevel.INFO);
		testselectApplicationPICCP1();
		report.enableLogging(LogLevel.INFO);

		testgetApplicationIDsP1();

		report.disableLogging(LogLevel.INFO);
		testauthenticatePICCNativeP1();
		report.enableLogging(LogLevel.INFO);

		testgetDFNameP1();
		report.disableLogging(LogLevel.INFO);
		testauthenticatePICCNativeP1();
		report.enableLogging(LogLevel.INFO);
		testgetCardUIDP1();
		testgetFreeMemP1();

		report.disableLogging(LogLevel.INFO);
		testselectApplicationPICCP1();
		report.enableLogging(LogLevel.INFO);
		report.disableLogging(LogLevel.INFO);
		testauthenticatePICCNativeP1();
		report.enableLogging(LogLevel.INFO);
		report.disableLogging(LogLevel.INFO);
		testformatP1();
		report.enableLogging(LogLevel.INFO);
		report.log(
				LogLevel.INFO,
				TAG,
				".....................TESTING PICC LEVEL APIS END..............................");
		report.log(
				LogLevel.INFO,
				TAG,
				"///////////////////////////////////////////////////////////////////////////////");
		report.log(
				LogLevel.INFO,
				TAG,
				".....................TESTING APP AND DATA MANIPULATION APIS START.............");
		// First format.
		report.disableLogging(LogLevel.INFO);
		testselectApplicationPICCP1();
		report.enableLogging(LogLevel.INFO);
		report.disableLogging(LogLevel.INFO);
		testauthenticatePICCNativeP1();
		report.enableLogging(LogLevel.INFO);
		report.disableLogging(LogLevel.INFO);
		testformatP1();
		report.enableLogging(LogLevel.INFO);

		// //----------------------////----------------------///
		report.disableLogging(LogLevel.INFO);
		testauthenticatePICCNativeP1();
		report.enableLogging(LogLevel.INFO);

		testcreateApplicationP1();
		// testcreateApplicationP3();// AES key Type
		report.disableLogging(LogLevel.INFO);
		testselectApplicationTestAppP2();
		report.enableLogging(LogLevel.INFO);

		testcreateStandardFileP1();
		testcreateBackupDataFileP1();
		testcreateValueFileP1();
		testcreateLinearRecordFileP1();
		testcreateCyclicRecordFileP1();

		report.disableLogging(LogLevel.INFO);
		testauthenticatePICCNativeP1();
		report.enableLogging(LogLevel.INFO);
		// NXPITag.disableLogging();testauthenticatePICCAESP4();NXPITag.enableLogging();
		// // AES key Type
		testWriteDataP1();
		testReadDataP1();
		testCreditP1();
		testDebitP1();
		testLimitedCreditP1();
		testLinerWriteRecordP1();
		testLinerReadRecordP1();
		testCyclicWriteRecordP1();
		testCyclicReadRecordP1();
		testGetValueP1();
		testClearCyclicRecordP1();
		testClearLinerRecordP1();

		report.disableLogging(LogLevel.INFO);
		testselectApplicationTestAppP2();
		report.enableLogging(LogLevel.INFO);
		testGetFileIDsP1();
		testGetSTDFileSettingsP1();
		testGetBACKUPFileSettingsP1();
		testGetValueFileSettingsP1();
		testGetLinerFileSettingsP1();
		testGetCyclicFileSettingsP1();

		report.disableLogging(LogLevel.INFO);
		testauthenticatePICCNativeP1();
		report.enableLogging(LogLevel.INFO);
		// NXPITag.disableLogging();testauthenticatePICCAESP4();NXPITag.enableLogging();//
		// AES key Type
		testchangeStdFileSettingsP1();
		testchangeBackupDataFileSettingsP1();
		report.disableLogging(LogLevel.INFO);
		testauthenticatePICCNativeP1();
		report.enableLogging(LogLevel.INFO);
		// NXPITag.disableLogging();testauthenticatePICCAESP4();NXPITag.enableLogging();//
		// AES key Type
		testchangeLinearRecordFileSettingsP1();
		testchangeCyclicRecordFileSettingsP1();
		testchangeValueFileSettingsP1();

		report.disableLogging(LogLevel.INFO);
		testGetSTDFileSettingsP1();
		testGetBACKUPFileSettingsP1();
		testGetValueFileSettingsP1();
		testGetLinerFileSettingsP1();
		testGetCyclicFileSettingsP1();
		report.enableLogging(LogLevel.INFO);

		testDeleteStandardFileP1();
		// testchangeKeySettingsP1();
		report.disableLogging(LogLevel.INFO);
		testselectApplicationTestAppP2();
		report.enableLogging(LogLevel.INFO);
		report.disableLogging(LogLevel.INFO);
		testauthenticatePICCNativeP1();
		report.enableLogging(LogLevel.INFO);
		// NXPITag.disableLogging();testauthenticatePICCAESP4();NXPITag.enableLogging();//
		// AES key Type
		testDeleteBackupDataFileP1();
		testDeleteCyclicRecordFileP1();
		testDeleteLinearRecordFileP1();
		testDeleteValueFileP1();
		testdeleteApplicationP1();

		// /////////////////////
		// -----------------////////////////---------------//
		report.disableLogging(LogLevel.INFO);
		testselectApplicationPICCP1();
		report.enableLogging(LogLevel.INFO);
		report.disableLogging(LogLevel.INFO);
		testauthenticatePICCNativeP1();
		report.enableLogging(LogLevel.INFO);
		report.disableLogging(LogLevel.INFO);
		testformatP1();
		report.enableLogging(LogLevel.INFO);

		report.disableLogging(LogLevel.INFO);
		testcreateApplicationP2();
		report.enableLogging(LogLevel.INFO);
		report.disableLogging(LogLevel.INFO);
		testselectApplicationTestAppP2();
		report.enableLogging(LogLevel.INFO);
		report.disableLogging(LogLevel.INFO);
		testcreateISOStandardFileP1();
		testcreateISOBackupDataFileP1();
		testcreateISOLinearRecordFileP1();
		testcreateISOCyclicRecordFileP1();
		report.enableLogging(LogLevel.INFO);
		report.disableLogging(LogLevel.INFO);
		testauthenticatePICCAESP4();
		report.enableLogging(LogLevel.INFO);
		report.disableLogging(LogLevel.INFO);
		testWriteISODataP1();
		testReadISODataP1();
		testWriteISODataP2();
		testauthenticatePICCAESP4();
		testReadISODataP1();

		testLinerWriteISORecordP1();
		testLinerReadISORecordP1();
		testCyclicWriteISORecordP1();
		testCyclicReadISORecordP1();
		report.enableLogging(LogLevel.INFO);
		report.disableLogging(LogLevel.INFO);
		testselectApplicationTestAppP2();
		report.enableLogging(LogLevel.INFO);
		// testGetFileIDsP1();
		testGetISOFileIDsP1();

		report.disableLogging(LogLevel.INFO);
		testselectApplicationPICCP1();
		report.enableLogging(LogLevel.INFO);
		report.disableLogging(LogLevel.INFO);
		testauthenticatePICCNativeP1();
		report.enableLogging(LogLevel.INFO);
		report.disableLogging(LogLevel.INFO);
		testformatP1();
		report.enableLogging(LogLevel.INFO);
		report.log(
				LogLevel.INFO,
				TAG,
				".....................TESTING APP AND DATA MANIPULATION APIS END...............");

		report.log(LogLevel.INFO, TAG,
				".....................TESTING NDEF Functionality START...............");

		// Testing NDEF Functionality
		// below test cases combine of formatT4T+isT4T+writeNDEF
		testNdefFunctionality();
		// testNdefFunctionalityP1();

		testselectApplicationPICCP1();
		testauthenticatePICCNativeP1();
		testformatP1();

		report.log(LogLevel.INFO, TAG,
				".....................TESTING NDEF Functionality END...............");
		report.log(
				LogLevel.INFO,
				TAG,
				"///////////////////////////////////////////////////////////////////////////////");
		report.log(
				LogLevel.INFO,
				TAG,
				".....................TESTING ISO 7816 STANDARD APIS START.....................");

		// First format.
		report.disableLogging(LogLevel.INFO);
		testselectApplicationPICCP1();
		report.enableLogging(LogLevel.INFO);
		report.disableLogging(LogLevel.INFO);
		testauthenticatePICCNativeP1();
		report.enableLogging(LogLevel.INFO);
		report.disableLogging(LogLevel.INFO);
		testformatP1();
		report.enableLogging(LogLevel.INFO);

		testcreateApplicationP4();
		report.disableLogging(LogLevel.INFO);
		testselectApplicationTestAppP2();
		report.enableLogging(LogLevel.INFO);
		testcreateISOStandardFileP2();
		testcreateISOLinearRecordFileP2();

		testGetCardDetails();
		testgetMemorySizeP1();
		// testgetLibVersionP1();
		testisCardDESFireEV1();
		// RATS
		testgetVersion();

		try {
			mDesFireEV1.getReader().close();
			mDesFireEV1.getReader().connect();
		} catch (ReaderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		testSetCommandSet();
		testISOSelectMasterFileP2();
		// testLogic.testISOSelectFileP1();
		testISOSelectFileEFunderDF_StandardFileP1();
		testISOAuthP1();
		testISOUpdateBinaryP1();
		testISOReadBinaryP1();
		testISOSelectFileEFunderDF_RecordFileP1();
		testISOAuthP1();
		testISOAppendRecordP1();
		testISOReadRecordP1();

		try {
			mDesFireEV1.getReader().close();
			mDesFireEV1.getReader().connect();
		} catch (ReaderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// format the card.
		report.disableLogging(LogLevel.INFO);
		testselectApplicationPICCP1();
		report.enableLogging(LogLevel.INFO);
		report.disableLogging(LogLevel.INFO);
		testauthenticatePICCNativeP1();
		report.enableLogging(LogLevel.INFO);
		report.disableLogging(LogLevel.INFO);
		testformatP1();
		report.enableLogging(LogLevel.INFO);
		// testcreateApplicationP5();
		// testselectApplicationTestAppP5();
		// testSetCommandSet();
		report.log(LogLevel.INFO, TAG,
				".....................TESTING ISO 7816 STANDARD APIS END.....................");

		report.log(LogLevel.DEBUG, TAG,
				"testGenerateDESFireEV1CardReport result is " + true);
		report.log(LogLevel.DEBUG, TAG, "testGenerateDESFireEV1CardReport, end");
		report.enableLogging(LogLevel.INFO);
		report.enableLogging(LogLevel.INFO);

	}

	
	private void testAllCreateAppliaction() {
		// TODO Auto-generated method stub
		testFormat();
		testformatApplicationKeySettingOne();
		testformatApplicationKeySettingTwo();
		testcreateApplicationP6();
		testcreateApplicationP7();
		testcreateApplicationP8();
		testcreateApplicationP9();
		testcreateApplicationP10();
		testcreateApplicationP11();
		testcreateApplicationP12();
		testcreateApplicationP13();
		testFormat();
	}

	/**
	 * <li>Pre :</li> NA <li>Desc:</li> <li>Post:</li> NA
	 */
	
	public void testauthenticatePICCNativeP1() {

		report.log(LogLevel.INFO, TAG, "authenticateOperations, GroupName");
		report.log(LogLevel.INFO, TAG, "testauthenticatePICCNativeP1, start");
		prepareSoftwareKeyStore();
		boolean result = false;
		try {

			int KeyNo = isDefaultKeyIsOldKey ? D_PICC_DES_KEY_NO
					: O_PICC_DES_KEY_NO;

			mDesFireEV1.authenticate(DESFireEV1.AuthType.Native, KeyNo,
					(byte) 0, 0, (byte) 0, null);
			result = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}

		report.log(LogLevel.INFO, TAG,
				"testauthenticatePICCNativeP1 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF01, FRSID");
		report.log(LogLevel.INFO, TAG, "testauthenticatePICCNativeP1, end");

	}

	public void applyCommandSetISO() {
		try {
			mDesFireEV1.getReader().close();
			mDesFireEV1.getReader().connect();
			mDesFireEV1.setCommandSet(CommandSet.ISO);
		} catch (ReaderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void applyCommandSetNative() {
		try {
			mDesFireEV1.getReader().close();
			mDesFireEV1.getReader().connect();
			mDesFireEV1.setCommandSet(CommandSet.Native);
		} catch (ReaderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testauthenticatePICCNativeDiversfiedP1() {

		report.log(LogLevel.INFO, TAG, "authenticateOperations, GroupName");
		report.log(LogLevel.INFO, TAG,
				"testauthenticatePICCNativeDiversfiedP1, start");
		prepareSoftwareKeyStore();
		boolean result = false;
		try {

			int KeyNo = isDefaultKeyIsOldKey ? D_PICC_DES_KEY_NO
					: O_PICC_DES_KEY_NO;
			// Take the right key to diversify.
			mDesFireEV1.authenticate(DESFireEV1.AuthType.Native, KeyNo,
					(byte) 0, 0, IKeyConstants.DIV_OPTION_DESFIRE,
					EIGHT_BYTE_DIV_INPUT);
			result = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}

		report.log(LogLevel.INFO, TAG,
				"testauthenticatePICCNativeDiversfiedP1 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF01, FRSID");
		report.log(LogLevel.INFO, TAG,
				"testauthenticatePICCNativeDiversfiedP1, end");

	}

	/**
	 * <li>Pre :</li> NA <li>Desc: before writting ndef the card should be ndef
	 * formatted using formatT4T method</li> <li>Post:</li> NA
	 */
	
	public void testFormatT4T() {

		report.log(LogLevel.INFO, TAG, "NDEFOperations, GroupName");
		report.log(LogLevel.INFO, TAG, "testFormatT4T, start");
		prepareSoftwareKeyStore();
		boolean result = false;
		try {
			mDesFireEV1.formatT4T(256, 0, (byte) 0, 0, (byte) 0);
			result = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}

		report.log(LogLevel.INFO, TAG, "testFormatT4T result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF01, FRSID");
		report.log(LogLevel.INFO, TAG, "testFormatT4T, end");

	}

	/**
	 * <li>Pre :</li> NA <li>Desc: IsT4T need to be test before writting the
	 * ndef messages, If this method evaluates true then it's possible to write
	 * ndef messages.</li> <li>Post:</li> NA
	 */
	
	public void testIsT4T() {

		report.log(LogLevel.INFO, TAG, "NDEFOperations, GroupName");
		report.log(LogLevel.INFO, TAG, "testIsT4T, start");
		prepareSoftwareKeyStore();
		boolean result = false;
		result = mDesFireEV1.isT4T();

		report.log(LogLevel.INFO, TAG, "testIsT4T result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF01, FRSID");
		report.log(LogLevel.INFO, TAG, "testIsT4T, end");

	}

	/**
	 * <li>Pre :</li> NA <li>Desc: writting the ndef messages.</li> <li>Post:</li>
	 * NA
	 */
	
	public void testWriteNdefMessageWrapper() {

		report.log(LogLevel.INFO, TAG, "NDEFOperations, GroupName");
		report.log(LogLevel.INFO, TAG, "testWriteNdefMessageWrapper, start");
		prepareSoftwareKeyStore();
		boolean result = false;
		try {
			NdefMessageWrapper msg = new NdefMessageWrapper(
					(NdefRecordWrapper) NdefRecordWrapper
							.createUri("http://www.google.com"));
			mDesFireEV1.writeNDEF(msg);
			result = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (FormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}

		report.log(LogLevel.INFO, TAG, "testWriteNdefMessageWrapper result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF01, FRSID");
		report.log(LogLevel.INFO, TAG, "testWriteNdefMessageWrapper, end");

	}

	/**
	 * <li>Pre :</li> NA <li>Desc: writting the ndef messages.</li> <li>Post:</li>
	 * NA
	 */
	
	public void testReadNdefMessageWrapper() {

		report.log(LogLevel.INFO, TAG, "NDEFOperations, GroupName");
		report.log(LogLevel.INFO, TAG, "testReadNdefMessageWrapper, start");
		prepareSoftwareKeyStore();
		boolean result = false;
		INdefMessage msg;
		try {
			msg = mDesFireEV1.readNDEF();
			report.log(LogLevel.INFO, TAG, "NDEF Message: " + msg.toString());
			result = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}

		report.log(LogLevel.INFO, TAG, "testReadNdefMessageWrapper result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF01, FRSID");
		report.log(LogLevel.INFO, TAG, "testReadNdefMessageWrapper, end");

	}

	//

	/**
	 * <li>Pre :</li> NA <li>Desc: Card Version.</li> <li>Post:</li> NA
	 */
	
	public void testgetVersion() {

		report.log(LogLevel.INFO, TAG, "CardOperations, GroupName");
		report.log(LogLevel.INFO, TAG, "testgetmVersion, start");
		prepareSoftwareKeyStore();
		boolean result = false;
		byte[] version;
		try {
			version = mDesFireEV1.getVersion();
			report.log(LogLevel.INFO, TAG, "Version" + version.toString());
			result = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (DESFireException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}

		report.log(LogLevel.INFO, TAG, "testgetmVersion result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF01, FRSID");
		report.log(LogLevel.INFO, TAG, "testgetmVersion, end");

	}

	/**
	 * <li>Pre :</li> NA <li>Desc: ISO Operations.</li> <li>Post:</li> NA
	 */
	
	public void testISOCommands() {

		report.log(LogLevel.INFO, TAG, "CardOperations, GroupName");
		report.log(LogLevel.INFO, TAG, "testISO, start");
		prepareSoftwareKeyStore();
		boolean result;

		// ISO
		testselectApplicationPICCP1();
		testauthenticatePICCNativeP1();
		testformatP1();

		testcreateApplicationP2();
		testselectApplicationTestAppP2();
		testcreateISOStandardFileP1();
		testcreateISOLinearRecordFileP1();
		testauthenticateISOPICC();
		testWriteISOData();
		testauthenticateISOPICC();
		testReadISOData();
		testLinerWriteISORecordP1();
		testLinerReadISORecordP1();

		testselectApplicationTestAppP2();
		testGetFileIDsP1();
		testGetISOFileIDsP1();

		// formatting
		testselectApplicationPICCP1();
		testauthenticatePICCNativeP1();
		testformatP1();

		result = true;

		report.log(LogLevel.INFO, TAG, "testISO result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF01, FRSID");
		report.log(LogLevel.INFO, TAG, "testISO, end");

	}

	/**
	 * <li>Pre :</li> NA <li>Desc: CommandSet settings to ISO Mode.</li> <li>
	 * Post:</li> NA
	 */
	
	public void testSetCommandSet() {

		report.log(LogLevel.INFO, TAG, "CommandSetOperations, GroupName");
		report.log(LogLevel.INFO, TAG, "testSetCommandSet, start");
		prepareSoftwareKeyStore();
		boolean result = false;
		mDesFireEV1.setCommandSet(CommandSet.ISO);
		result = true;

		report.log(LogLevel.INFO, TAG, "testSetCommandSet result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF01, FRSID");
		report.log(LogLevel.INFO, TAG, "testSetCommandSet, end");

	}

	/**
	 * <li>Pre :</li> NA <li>Desc:</li> <li>Post:</li> NA
	 */
	
	public void testauthenticatePICC2K3DESP2() {

		report.log(LogLevel.INFO, TAG, "authenticateOperations, GroupName");
		report.log(LogLevel.INFO, TAG, "testauthenticatePICC2K3DESP2, start");
		prepareSoftwareKeyStore();
		boolean result = false;
		try {
			int KeyNo = isDefaultKeyIsOldKey ? D_PICC_2KDES_KEY_NO
					: O_PICC_2KDES_KEY_NO;
			mDesFireEV1.authenticate(DESFireEV1.AuthType.Native, KeyNo,
					(byte) 0, 0, (byte) 0, null);

			result = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			// TODO Auto-generated catch block
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG,
				"testauthenticatePICC2K3DESP2 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF01, FRSID");
		report.log(LogLevel.INFO, TAG, "testauthenticatePICC2K3DESP2, end");

	}

	
	public void testauthenticatePICC2K3DESDiversfiedP2() {

		report.log(LogLevel.INFO, TAG, "authenticateOperations, GroupName");
		report.log(LogLevel.INFO, TAG, "testauthenticatePICC2K3DESP2, start");
		prepareSoftwareKeyStore();
		boolean result = false;
		try {
			int KeyNo = isDefaultKeyIsOldKey ? D_PICC_2KDES_KEY_NO
					: O_PICC_2KDES_KEY_NO;

			mDesFireEV1.authenticate(DESFireEV1.AuthType.Native, KeyNo,
					(byte) 0, 0, IKeyConstants.DIV_OPTION_DESFIRE,
					EIGHT_BYTE_DIV_INPUT);
			result = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			// TODO Auto-generated catch block
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG,
				"testauthenticatePICC2K3DESP2 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF01, FRSID");
		report.log(LogLevel.INFO, TAG, "testauthenticatePICC2K3DESP2, end");

	}

	/**
	 * <li>Pre :</li> NA <li>Desc:</li> <li>Post:</li> NA
	 */
	
	public void testauthenticatePICC3K3DESP3() {

		report.log(LogLevel.INFO, TAG, "authenticateOperations, GroupName");
		report.log(LogLevel.INFO, TAG, "testauthenticatePICC3K3DESP3, start");
		prepareSoftwareKeyStore();
		boolean result = false;
		try {
			mDesFireEV1.authenticate(DESFireEV1.AuthType.ISO,
					D_PICC_3K3KDES_KEY_NO, (byte) 0, 0, (byte) 0, null);
			result = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			// TODO Auto-generated catch block
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG,
				"testauthenticatePICC3K3DESP3 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF01, FRSID");
		report.log(LogLevel.INFO, TAG, "testauthenticatePICC3K3DESP3, end");

	}

	
	public void testauthenticatePICCAESP4() {

		report.log(LogLevel.INFO, TAG, "authenticateOperations, GroupName");
		report.log(LogLevel.INFO, TAG, "testauthenticatePICCAESP4, start");
		prepareSoftwareKeyStore();
		boolean result = false;
		try {
			mDesFireEV1.authenticate(DESFireEV1.AuthType.AES,
					D_PICC_AES_KEY_NO, (byte) 0, 0, (byte) 0, null);
			result = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			// TODO Auto-generated catch block
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testauthenticatePICCAESP4 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF01, FRSID");
		report.log(LogLevel.INFO, TAG, "testauthenticatePICCAESP4, end");

	}

	public void testauthenticatePICCNativeOneKeyP5() {

		report.log(LogLevel.INFO, TAG, "authenticateOperations, GroupName");
		report.log(LogLevel.INFO, TAG,
				"testauthenticatePICCNativeOneKeyP5, start");
		prepareSoftwareKeyStore();
		boolean result = false;
		try {
			mDesFireEV1.authenticate(DESFireEV1.AuthType.Native,
					O_PICC_DES_KEY_NO, (byte) 0, 0, (byte) 0, null);
			result = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			// TODO Auto-generated catch block
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG,
				"testauthenticatePICCNativeOneKeyP5 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF01, FRSID");
		report.log(LogLevel.INFO, TAG,
				"testauthenticatePICCNativeOneKeyP5, end");

	}

	public void testauthenticatePICC2K3DESOneKeyP6() {

		report.log(LogLevel.INFO, TAG, "authenticateOperations, GroupName");
		report.log(LogLevel.INFO, TAG,
				"testauthenticatePICC2K3DESOneKeyP6, start");
		prepareSoftwareKeyStore();
		boolean result = false;
		try {
			mDesFireEV1.authenticate(DESFireEV1.AuthType.Native,
					O_PICC_2KDES_KEY_NO, (byte) 0, 0, (byte) 0, null);
			result = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			// TODO Auto-generated catch block
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG,
				"testauthenticatePICC2K3DESOneKeyP6 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF01, FRSID");
		report.log(LogLevel.INFO, TAG,
				"testauthenticatePICC2K3DESOneKeyP6, end");

	}

	public void testauthenticatePICC3K3DESOneKeyP7() {

		report.log(LogLevel.INFO, TAG, "authenticateOperations, GroupName");
		report.log(LogLevel.INFO, TAG,
				"testauthenticatePICC3K3DESOneKeyP7, start");
		prepareSoftwareKeyStore();
		boolean result = false;
		try {
			mDesFireEV1.authenticate(DESFireEV1.AuthType.ISO,
					O_PICC_3K3KDES_KEY_NO, (byte) 0, 0, (byte) 0, null);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (GeneralSecurityException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG,
				"testauthenticatePICC3K3DESOneKeyP7 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF01, FRSID");
		report.log(LogLevel.INFO, TAG,
				"testauthenticatePICC3K3DESOneKeyP7, end");

	}

	public void testauthenticatePICCAESOneP8() {

		report.log(LogLevel.INFO, TAG, "authenticateOperations, GroupName");
		report.log(LogLevel.INFO, TAG, "testauthenticatePICCAESOneP8, start");
		prepareSoftwareKeyStore();
		boolean result = false;
		try {
			mDesFireEV1.authenticate(DESFireEV1.AuthType.AES,
					O_PICC_AES_KEY_NO, (byte) 0, 0, (byte) 0, null);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (GeneralSecurityException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG,
				"testauthenticatePICCAESOneP8 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF01, FRSID");
		report.log(LogLevel.INFO, TAG, "testauthenticatePICCAESOneP8, end");

	}

	public void testauthenticatePICC2K3DESOneKeyN1() {

		report.log(LogLevel.INFO, TAG,
				"authenticateOperations_negative, GroupName");
		report.log(LogLevel.INFO, TAG,
				"testauthenticatePICC2K3DESOneKeyN1, start");
		prepareSoftwareKeyStore();
		boolean result = false;
		try {
			mDesFireEV1.authenticate(DESFireEV1.AuthType.Native,
					O_PICC_2KDES_KEY_NO, (byte) 100, -1, (byte) 0, null);
			result = false;
		} catch (IOException e) {
			result = true;
			e.printStackTrace();
		} catch (GeneralSecurityException e) {
			result = true;
			e.printStackTrace();
		} catch (SmartCardException e) {
			result = true;
			e.printStackTrace();
		}
		report.log(LogLevel.INFO, TAG,
				"testauthenticatePICC2K3DESOneKeyN1 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF01, FRSID");
		report.log(LogLevel.INFO, TAG,
				"testauthenticatePICC2K3DESOneKeyN1, end");

	}

	public void testauthenticatePICC3K3DESOneKeyN2() {

		report.log(LogLevel.INFO, TAG,
				"authenticateOperations_negative, GroupName");
		report.log(LogLevel.INFO, TAG,
				"testauthenticatePICC3K3DESOneKeyN2, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {
			mDesFireEV1.authenticate(DESFireEV1.AuthType.ISO,
					O_PICC_3K3KDES_KEY_NO, (byte) 0, -100, (byte) 90, null);
			result = false;
		} catch (IOException e) {
			result = true;
			e.printStackTrace();
		} catch (GeneralSecurityException e) {
			result = true;
			e.printStackTrace();
		} catch (SmartCardException e) {
			result = true;
			e.printStackTrace();
		}
		report.log(LogLevel.INFO, TAG,
				"testauthenticatePICC3K3DESOneKeyN2 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF01, FRSID");
		report.log(LogLevel.INFO, TAG,
				"testauthenticatePICC3K3DESOneKeyN2, end");

	}

	public void testauthenticatePICCAESOneN3() {

		report.log(LogLevel.INFO, TAG,
				"authenticateOperations_negative, GroupName");
		report.log(LogLevel.INFO, TAG, "testauthenticatePICCAESOneN3, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {
			mDesFireEV1.authenticate(DESFireEV1.AuthType.AES,
					O_PICC_3K3KDES_KEY_NO, (byte) 0, 0, (byte) 0, null);
			result = false;
		} catch (IOException e) {
			result = true;
			e.printStackTrace();
		} catch (GeneralSecurityException e) {
			result = true;
			e.printStackTrace();
		} catch (SmartCardException e) {
			result = true;
			e.printStackTrace();
		}
		report.log(LogLevel.INFO, TAG,
				"testauthenticatePICCAESOneN3 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF01, FRSID");
		report.log(LogLevel.INFO, TAG, "testauthenticatePICCAESOneN3, end");

	}

	
	public void testselectApplicationPICCP1() {
		report.log(LogLevel.INFO, TAG, "applicationCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testselectApplicationPICCP1, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {
			mDesFireEV1.selectApplication(0);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testselectApplicationPICCP1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF17, FRSID");
		report.log(LogLevel.INFO, TAG, "testselectApplicationPICCP1, end");
	}

	
	public void testselectApplicationTestAppP2() {
		report.log(LogLevel.INFO, TAG, "applicationCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testselectApplicationTestAppP2, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {
			mDesFireEV1.selectApplication(1);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG,
				"testselectApplicationTestAppP2 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF17, FRSID");
		report.log(LogLevel.INFO, TAG, "testselectApplicationTestAppP2, end");
	}

	
	public void testselectApplicationTestAppN1() {
		report.log(LogLevel.INFO, TAG,
				"applicationCommands_negative, GroupName");
		report.log(LogLevel.INFO, TAG, "testselectApplicationTestAppN1, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {
			mDesFireEV1.selectApplication(50);
			result = false;
		} catch (IOException e) {
			result = true;
			e.printStackTrace();
		} catch (SmartCardException e) {
			result = true;
			e.printStackTrace();
		}
		report.log(LogLevel.INFO, TAG,
				"testselectApplicationTestAppN1 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF17, FRSID");
		report.log(LogLevel.INFO, TAG, "testselectApplicationTestAppN1, end");
	}

	
	public void selectDefaultKeyasOldKey() {
		isDefaultKeyIsOldKey = true;
	}

	
	public void selectDefaultKeyasNewKey() {
		isDefaultKeyIsOldKey = false;
	}

	
	public void testchangeKeyDESToDESP1() {
		report.log(LogLevel.INFO, TAG,
				"changeKeyCommands Operations, GroupName");
		report.log(LogLevel.INFO, TAG, "testchangeKeyDESToDESP1, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {
			int oldKeyNo = isDefaultKeyIsOldKey ? D_PICC_DES_KEY_NO
					: O_PICC_DES_KEY_NO;
			int newKeyNumber = isDefaultKeyIsOldKey ? O_PICC_DES_KEY_NO
					: D_PICC_DES_KEY_NO;
			mDesFireEV1.changeKey(0, oldKeyNo, (byte) 0, newKeyNumber,
					(byte) 0, DESFireEV1.KeyType.DES, (byte) 0, (byte) 0, null);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (GeneralSecurityException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testchangeKeyDESToDESP1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF05, FRSID");
		report.log(LogLevel.INFO, TAG, "testchangeKeyDESToDESP1, end");
	}

	public void testchangeKeyDESToDESKeyDiversfiedP1() {
		report.log(LogLevel.INFO, TAG,
				"changeKeyCommands Operations, GroupName");
		report.log(LogLevel.INFO, TAG,
				"testchangeKeyDESToDESNewKeyDiversfiedP1, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {
			int oldKeyNo = isDefaultKeyIsOldKey ? D_PICC_DES_KEY_NO
					: O_PICC_DES_KEY_NO;
			int newKeyNumber = isDefaultKeyIsOldKey ? O_PICC_DES_KEY_NO
					: D_PICC_DES_KEY_NO;

			if (isDefaultKeyIsOldKey) {
				// isDefaultKeyIsOldKey true means old not diversifed key is
				// used for auth.
				// Old key is used for auth, so diversify the new key.
				mDesFireEV1.changeKey(0, oldKeyNo, (byte) 0, newKeyNumber,
						(byte) 0, DESFireEV1.KeyType.DES, (byte) 0,
						IKeyConstants.DIV_OPTION_DESFIRE, EIGHT_BYTE_DIV_INPUT);
			} else {
				// isDefaultKeyIsOldKey false means new diversifed key is used
				// for auth.
				// New diversified key is used for auth, so diversify the old
				// key to get that diversified one..
				mDesFireEV1.changeKey(0, oldKeyNo, (byte) 0, newKeyNumber,
						(byte) 0, DESFireEV1.KeyType.DES,
						IKeyConstants.DIV_OPTION_DESFIRE, (byte) 0,
						EIGHT_BYTE_DIV_INPUT);
			}
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (GeneralSecurityException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG,
				"testchangeKeyDESToDESNewKeyDiversfiedP1 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF05, FRSID");
		report.log(LogLevel.INFO, TAG,
				"testchangeKeyDESToDESNewKeyDiversfiedP1, end");
	}

	
	public void testchangeKey2K3DESTo2K3DESP2() {
		report.log(LogLevel.INFO, TAG,
				"changeKeyCommands Operations, GroupName");
		report.log(LogLevel.INFO, TAG, "testchangeKey2K3DESTo2K3DESP2, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {
			int oldKeyNo = isDefaultKeyIsOldKey ? D_PICC_2KDES_KEY_NO
					: O_PICC_2KDES_KEY_NO;
			int newKeyNumber = isDefaultKeyIsOldKey ? O_PICC_2KDES_KEY_NO
					: D_PICC_2KDES_KEY_NO;
			mDesFireEV1.changeKey(0, oldKeyNo, (byte) 0, newKeyNumber,
					(byte) 0, DESFireEV1.KeyType.TWOK3DES, (byte) 0, (byte) 0,
					null);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (GeneralSecurityException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG,
				"testchangeKey2K3DESTo2K3DESP2 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF05, FRSID");
		report.log(LogLevel.INFO, TAG, "testchangeKey2K3DESTo2K3DESP2, end");
	}

	
	public void testchangeKey2K3DESTo2K3DESOnlyNewDiversifiedP2() {
		report.log(LogLevel.INFO, TAG,
				"changeKeyCommands Operations, GroupName");
		report.log(LogLevel.INFO, TAG, "testchangeKey2K3DESTo2K3DESP2, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {
			int oldKeyNo = isDefaultKeyIsOldKey ? D_PICC_2KDES_KEY_NO
					: O_PICC_2KDES_KEY_NO;
			int newKeyNumber = isDefaultKeyIsOldKey ? O_PICC_2KDES_KEY_NO
					: D_PICC_2KDES_KEY_NO;

			// In order to diversify new key which has been changed eariler.
			// Diversify the old key here.
			mDesFireEV1.changeKey(0, oldKeyNo, (byte) 0, newKeyNumber,
					(byte) 0, DESFireEV1.KeyType.TWOK3DES,
					IKeyConstants.DIV_OPTION_DESFIRE, (byte) 0,
					EIGHT_BYTE_DIV_INPUT);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (GeneralSecurityException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG,
				"testchangeKey2K3DESTo2K3DESP2 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF05, FRSID");
		report.log(LogLevel.INFO, TAG, "testchangeKey2K3DESTo2K3DESP2, end");
	}

	
	public void testchangeKey2K3DESTo2K3DESBothKeysDiversifiedP2() {
		report.log(LogLevel.INFO, TAG,
				"changeKeyCommands Operations, GroupName");
		report.log(LogLevel.INFO, TAG, "testchangeKey2K3DESTo2K3DESP2, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {
			int oldKeyNo = isDefaultKeyIsOldKey ? D_PICC_2KDES_KEY_NO
					: O_PICC_2KDES_KEY_NO;
			int newKeyNumber = isDefaultKeyIsOldKey ? O_PICC_2KDES_KEY_NO
					: D_PICC_2KDES_KEY_NO;

			mDesFireEV1.changeKey(0, oldKeyNo, (byte) 0, newKeyNumber,
					(byte) 0, DESFireEV1.KeyType.TWOK3DES,
					IKeyConstants.DIV_OPTION_DESFIRE,
					IKeyConstants.DIV_OPTION_DESFIRE, EIGHT_BYTE_DIV_INPUT);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (GeneralSecurityException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG,
				"testchangeKey2K3DESTo2K3DESP2 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF05, FRSID");
		report.log(LogLevel.INFO, TAG, "testchangeKey2K3DESTo2K3DESP2, end");
	}

	
	public void testchangeKey3K3DESTo3K3DESP3() {
		report.log(LogLevel.INFO, TAG,
				"changeKeyCommands Operations, GroupName");
		report.log(LogLevel.INFO, TAG, "testchangeKey3K3DESTo3K3DESP3, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {
			int oldKeyNo = isDefaultKeyIsOldKey ? D_PICC_3K3KDES_KEY_NO
					: O_PICC_3K3KDES_KEY_NO;
			int newKeyNumber = isDefaultKeyIsOldKey ? O_PICC_3K3KDES_KEY_NO
					: D_PICC_3K3KDES_KEY_NO;
			mDesFireEV1.changeKey(0, oldKeyNo, (byte) 0, newKeyNumber,
					(byte) 0, DESFireEV1.KeyType.THREEK3DES, (byte) 0,
					(byte) 0, null);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (GeneralSecurityException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG,
				"testchangeKey3K3DESTo3K3DESP3 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF05, FRSID");
		report.log(LogLevel.INFO, TAG, "testchangeKey3K3DESTo3K3DESP3, end");
	}

	
	public void testchangeKeyAESToAESP4() {
		report.log(LogLevel.INFO, TAG,
				"changeKeyCommands Operations, GroupName");
		report.log(LogLevel.INFO, TAG, "testchangeKeyAESToAESP4, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {
			int oldKeyNo = isDefaultKeyIsOldKey ? D_PICC_AES_KEY_NO
					: O_PICC_AES_KEY_NO;
			int newKeyNumber = isDefaultKeyIsOldKey ? O_PICC_AES_KEY_NO
					: D_PICC_AES_KEY_NO;
			mDesFireEV1.changeKey(0, oldKeyNo, (byte) 0, newKeyNumber,
					(byte) 0, DESFireEV1.KeyType.AES, (byte) 0, (byte) 0, null);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (GeneralSecurityException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testchangeKeyAESToAESP4 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF05, FRSID");
		report.log(LogLevel.INFO, TAG, "testchangeKeyAESToAESP4, end");
	}

	
	public void testchangeKeyDESTo2K3DESP5() {
		report.log(LogLevel.INFO, TAG,
				"changeKeyCommands Operations, GroupName");
		report.log(LogLevel.INFO, TAG, "testchangeKeyDESTo2K3DESP5, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {
			int oldKeyNo = D_PICC_DES_KEY_NO;
			int newKeyNumber = D_PICC_2KDES_KEY_NO;
			mDesFireEV1.changeKey(0, oldKeyNo, (byte) 0, newKeyNumber,
					(byte) 0, DESFireEV1.KeyType.TWOK3DES, (byte) 0, (byte) 0,
					null);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (GeneralSecurityException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testchangeKeyDESTo2K3DESP5 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF05, FRSID");
		report.log(LogLevel.INFO, TAG, "testchangeKeyDESTo2K3DESP5, end");
	}

	
	public void testchangeKeyDESTo2K3DESDiversifiedP5() {
		report.log(LogLevel.INFO, TAG,
				"changeKeyCommands Operations, GroupName");
		report.log(LogLevel.INFO, TAG, "testchangeKeyDESTo2K3DESP5, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {
			int oldKeyNo = D_PICC_DES_KEY_NO;
			int newKeyNumber = D_PICC_2KDES_KEY_NO;
			mDesFireEV1.changeKey(0, oldKeyNo, (byte) 0, newKeyNumber,
					(byte) 0, DESFireEV1.KeyType.TWOK3DES, (byte) 0,
					(byte) IKeyConstants.DIV_OPTION_DESFIRE,
					EIGHT_BYTE_DIV_INPUT);

			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (GeneralSecurityException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testchangeKeyDESTo2K3DESP5 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF05, FRSID");
		report.log(LogLevel.INFO, TAG, "testchangeKeyDESTo2K3DESP5, end");
	}

	
	public void testchangeKeyDESTo3K3DESP6() {
		report.log(LogLevel.INFO, TAG,
				"changeKeyCommands Operations, GroupName");
		report.log(LogLevel.INFO, TAG, "testchangeKeyDESTo3K3DESP6, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {

			int oldKeyNo = D_PICC_DES_KEY_NO;
			int newKeyNumber = D_PICC_3K3KDES_KEY_NO;
			mDesFireEV1.changeKey(0, oldKeyNo, (byte) 0, newKeyNumber,
					(byte) 0, DESFireEV1.KeyType.THREEK3DES, (byte) 0,
					(byte) 0, null);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (GeneralSecurityException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testchangeKeyDESTo3K3DESP6 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF05, FRSID");
		report.log(LogLevel.INFO, TAG, "testchangeKeyDESTo3K3DESP6, end");
	}

	
	public void testchangeKeyDESToAESP7() {

		report.log(LogLevel.INFO, TAG,
				"changeKeyCommands Operations, GroupName");
		report.log(LogLevel.INFO, TAG, "testchangeKeyDESToAESP7, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {
			int oldKeyNo = D_PICC_DES_KEY_NO;
			int newKeyNumber = D_PICC_AES_KEY_NO;
			mDesFireEV1.changeKey(0, oldKeyNo, (byte) 0, newKeyNumber,
					(byte) 0, DESFireEV1.KeyType.AES, (byte) 0, (byte) 0, null);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (GeneralSecurityException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testchangeKeyDESToAESP7 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF05, FRSID");
		report.log(LogLevel.INFO, TAG, "testchangeKeyDESToAESP7, end");
	}

	
	public void testchangeKeyAESToDESP8() {
		report.log(LogLevel.INFO, TAG,
				"changeKeyCommands Operations, GroupName");
		report.log(LogLevel.INFO, TAG, "testchangeKeyAESToDESP8, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {
			int oldKeyNo = D_PICC_AES_KEY_NO;
			int newKeyNumber = D_PICC_DES_KEY_NO;
			mDesFireEV1.changeKey(0, oldKeyNo, (byte) 0, newKeyNumber,
					(byte) 0, DESFireEV1.KeyType.DES, (byte) 0, (byte) 0, null);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (GeneralSecurityException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testchangeKeyAESToDESP8 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF05, FRSID");
		report.log(LogLevel.INFO, TAG, "testchangeKeyAESToDESP8, end");
	}

	
	public void testchangeKey3K3DESToDESP9() {
		report.log(LogLevel.INFO, TAG,
				"changeKeyCommands Operations, GroupName");
		report.log(LogLevel.INFO, TAG, "testchangeKey3K3DESToDESP9, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {
			int oldKeyNo = D_PICC_3K3KDES_KEY_NO;
			int newKeyNumber = D_PICC_DES_KEY_NO;
			mDesFireEV1.changeKey(0, oldKeyNo, (byte) 0, newKeyNumber,
					(byte) 0, DESFireEV1.KeyType.DES, (byte) 0, (byte) 0, null);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (GeneralSecurityException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testchangeKey3K3DESToDESP9 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF05, FRSID");
		report.log(LogLevel.INFO, TAG, "testchangeKey3K3DESToDESP9, end");
	}

	public void testchangeKey2K3DESToDESP10() {

		report.log(LogLevel.INFO, TAG,
				"changeKeyCommands Operations, GroupName");
		report.log(LogLevel.INFO, TAG, "testchangeKey2K3DESToDESP10, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {
			int oldKeyNo = D_PICC_2KDES_KEY_NO;
			int newKeyNumber = D_PICC_DES_KEY_NO;
			mDesFireEV1.changeKey(0, oldKeyNo, (byte) 0, newKeyNumber,
					(byte) 0, DESFireEV1.KeyType.DES, (byte) 0, (byte) 0, null);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (GeneralSecurityException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testchangeKey2K3DESToDESP10 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF05, FRSID");
		report.log(LogLevel.INFO, TAG, "testchangeKey2K3DESToDESP10, end");
	}

	
	public void testchangeKey2K3DESTo3K3DESP11() {
		report.log(LogLevel.INFO, TAG,
				"changeKeyCommands Operations, GroupName");
		report.log(LogLevel.INFO, TAG, "testchangeKey2K3DESTo3K3DESP11, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {
			int oldKeyNo = D_PICC_2KDES_KEY_NO;
			int newKeyNumber = D_PICC_3K3KDES_KEY_NO;
			mDesFireEV1.changeKey(0, oldKeyNo, (byte) 0, newKeyNumber,
					(byte) 0, DESFireEV1.KeyType.THREEK3DES, (byte) 0,
					(byte) 0, null);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (GeneralSecurityException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG,
				"testchangeKey2K3DESTo3K3DESP11 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF05, FRSID");
		report.log(LogLevel.INFO, TAG, "testchangeKey2K3DESTo3K3DESP11, end");
	}

	
	public void testchangeKey2K3DESToAESP12() {
		report.log(LogLevel.INFO, TAG,
				"changeKeyCommands Operations, GroupName");
		report.log(LogLevel.INFO, TAG, "testchangeKey2K3DESToAESP12, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {
			int oldKeyNo = D_PICC_2KDES_KEY_NO;
			int newKeyNumber = D_PICC_AES_KEY_NO;
			mDesFireEV1.changeKey(0, oldKeyNo, (byte) 0, newKeyNumber,
					(byte) 0, DESFireEV1.KeyType.AES, (byte) 0, (byte) 0, null);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (GeneralSecurityException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testchangeKey2K3DESToAESP12 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF05, FRSID");
		report.log(LogLevel.INFO, TAG, "testchangeKey2K3DESToAESP12, end");
	}

	
	public void testchangeKey3K3DESTo2K3DESP13() {
		report.log(LogLevel.INFO, TAG,
				"changeKeyCommands Operations, GroupName");
		report.log(LogLevel.INFO, TAG, "testchangeKey3K3DESTo2K3DESP13, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {
			int oldKeyNo = D_PICC_3K3KDES_KEY_NO;
			int newKeyNumber = D_PICC_2KDES_KEY_NO;
			mDesFireEV1.changeKey(0, oldKeyNo, (byte) 0, newKeyNumber,
					(byte) 0, DESFireEV1.KeyType.TWOK3DES, (byte) 0, (byte) 0,
					null);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (GeneralSecurityException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG,
				"testchangeKey3K3DESTo2K3DESP13 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF05, FRSID");
		report.log(LogLevel.INFO, TAG, "testchangeKey3K3DESTo2K3DESP13, end");
	}

	
	public void testchangeKeyAESTo2K3DESP14() {
		report.log(LogLevel.INFO, TAG,
				"changeKeyCommands Operations, GroupName");
		report.log(LogLevel.INFO, TAG, "testchangeKeyAESTo2K3DESP14, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {
			int oldKeyNo = D_PICC_AES_KEY_NO;
			int newKeyNumber = D_PICC_2KDES_KEY_NO;
			mDesFireEV1.changeKey(0, oldKeyNo, (byte) 0, newKeyNumber,
					(byte) 0, DESFireEV1.KeyType.TWOK3DES, (byte) 0, (byte) 0,
					null);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (GeneralSecurityException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testchangeKeyAESTo2K3DESP14 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF05, FRSID");
		report.log(LogLevel.INFO, TAG, "testchangeKeyAESTo2K3DESP14, end");
	}

	
	public void testchangeKey3K3DESToAESP15() {
		report.log(LogLevel.INFO, TAG,
				"changeKeyCommands Operations, GroupName");
		report.log(LogLevel.INFO, TAG, "testchangeKey3K3DESToAESP15, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {
			int oldKeyNo = D_PICC_3K3KDES_KEY_NO;
			int newKeyNumber = D_PICC_DES_KEY_NO;
			mDesFireEV1.changeKey(0, oldKeyNo, (byte) 0, newKeyNumber,
					(byte) 0, DESFireEV1.KeyType.AES, (byte) 0, (byte) 0, null);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (GeneralSecurityException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testchangeKey3K3DESToAESP15 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF05, FRSID");
		report.log(LogLevel.INFO, TAG, "testchangeKey3K3DESToAESP15, end");
	}

	
	public void testchangeKeyAESTo3K3DESP16() {
		report.log(LogLevel.INFO, TAG,
				"changeKeyCommands Operations, GroupName");
		report.log(LogLevel.INFO, TAG, "testchangeKeyAESTo3K3DESP16, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {
			int oldKeyNo = D_PICC_AES_KEY_NO;
			int newKeyNumber = D_PICC_3K3KDES_KEY_NO;
			mDesFireEV1.changeKey(0, oldKeyNo, (byte) 0, newKeyNumber,
					(byte) 0, DESFireEV1.KeyType.THREEK3DES, (byte) 0,
					(byte) 0, null);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (GeneralSecurityException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testchangeKeyAESTo3K3DESP16 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF05, FRSID");
		report.log(LogLevel.INFO, TAG, "testchangeKeyAESTo3K3DESP16, end");
	}

	
	public void testchangeKeySettingsP1() {

		report.log(LogLevel.INFO, TAG,
				"ChangeKeySettings Operations, GroupName");
		report.log(LogLevel.INFO, TAG, "testchangeKeySettingsP1, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {
			mDesFireEV1
					.changeKeySettings((byte) (DESFireEV1.KSONE_APP_KEY_AUTH));
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testchangeKeySettingsP1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF03, FRSID");
		report.log(LogLevel.INFO, TAG, "testchangeKeySettingsP1, end");
	}

	public void testchangeKeySettingsN1() {

		report.log(LogLevel.INFO, TAG,
				"ChangeKeySettings Operations, GroupName");
		report.log(LogLevel.INFO, TAG, "testchangeKeySettingsN1, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {
			mDesFireEV1
					.changeKeySettings((byte) (DESFireEV1.KSONE_APP_KEY_AUTH | DESFireEV1.KSONE_FILE_DEL_NO_MKEY));
			result = false;
		} catch (IOException e) {
			result = true;
			e.printStackTrace();
		} catch (SmartCardException e) {
			result = true;
			e.printStackTrace();
		}
		report.log(LogLevel.INFO, TAG, "testchangeKeySettingsN1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF03, FRSID");
		report.log(LogLevel.INFO, TAG, "testchangeKeySettingsN1, end");
	}

	
	public void testgetKeySettingsP1() {

		report.log(LogLevel.INFO, TAG, "GetKeySettings Operations, GroupName");
		report.log(LogLevel.INFO, TAG, "testgetKeySettingsP1, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {
			report.log(
					LogLevel.INFO,
					TAG,
					"Result: "
							+ modules.getUtility().dumpBytes(
									mDesFireEV1.getKeySettings()));
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testgetKeySettingsP1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF04, FRSID");
		report.log(LogLevel.INFO, TAG, "testgetKeySettingsP1, end");
	}

	public void testgetKeySettingsN1() {

		report.log(LogLevel.INFO, TAG, "GetKeySettings Operations, GroupName");
		report.log(LogLevel.INFO, TAG, "testgetKeySettingsN1, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {
			report.log(
					LogLevel.INFO,
					TAG,
					"Result: "
							+ CustomModules.getUtility().dumpBytes(
									mDesFireEV1.getKeySettings()));
			result = false;
		} catch (IOException e) {
			result = true;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = true;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testgetKeySettingsN1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF04, FRSID");
		report.log(LogLevel.INFO, TAG, "testgetKeySettingsN1, end");
	}

	
	public void testgetKeyVersionP1() {

		report.log(LogLevel.INFO, TAG, "GetKeyVersions Operations, GroupName");
		report.log(LogLevel.INFO, TAG, "testgetKeyVersionP1, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {
			report.log(
					LogLevel.INFO,
					TAG,
					"Result: "
							+ modules.getUtility().dumpBytes(
									mDesFireEV1.getKeyVersion(0)));
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testgetKeyVersionP1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF07, FRSID");
		report.log(LogLevel.INFO, TAG, "testgetKeyVersionP1, end");
	}

	public void testgetKeyVersionN1() {

		report.log(LogLevel.INFO, TAG, "GetKeyVersions Operations, GroupName");
		report.log(LogLevel.INFO, TAG, "testgetKeyVersionN1, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {
			report.log(
					LogLevel.INFO,
					TAG,
					"Result: "
							+ modules.getUtility().dumpBytes(
									mDesFireEV1.getKeyVersion(0)));
			result = false;
		} catch (IOException e) {
			result = true;
			e.printStackTrace();
		} catch (SmartCardException e) {
			result = true;
			e.printStackTrace();
		}
		report.log(LogLevel.INFO, TAG, "testgetKeyVersionN1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF07, FRSID");
		report.log(LogLevel.INFO, TAG, "testgetKeyVersionN1, end");
	}

	
	public void testgetMemorySizeP1() {
		report.log(LogLevel.INFO, TAG, "PiccCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testgetMemorySizeP1, start");
		boolean result = false;
		MemSize mz = null;
		try {
			mz = mDesFireEV1.getMemorySize();
			result = true;
		} catch (DESFireException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
		}
		report.log(LogLevel.INFO, TAG, "Memory Size-->" + mz.name());
		report.log(LogLevel.INFO, TAG, "testgetMemorySizeP1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF73, FRSID");
		report.log(LogLevel.INFO, TAG, "testgetMemorySizeP1, end");
	}

	
	public void testcreateApplicationRise() {
		report.log(LogLevel.INFO, TAG, "PiccCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testcreateApplicationRise, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {
			byte ks2 = mDesFireEV1.formatApplicationKeySettingTwo((byte) 3,
					(byte) 2, true);
			mDesFireEV1
					.createApplication(
							10,
							(byte) (0x10 | DESFireEV1.KSONE_CONFIG_CHANGABLE
									| DESFireEV1.KSONE_FILE_DEL_NO_MKEY
									| DESFireEV1.KSONE_GET_NO_MKEY | DESFireEV1.KSONE_APP_MKEY_CHANGABLE),
							ks2, new byte[] { 0x0E, 0x0E }, new byte[] { 1, 2,
									3, 4, 5 });

			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testcreateApplicationRise result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF11, FRSID");
		report.log(LogLevel.INFO, TAG, "testcreateApplicationRise, end");
	}

	
	public void testcreateApplicationP1() {

		report.log(LogLevel.INFO, TAG, "PiccCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testcreateApplicationP1, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {

			mDesFireEV1
					.createApplication(
							1,
							(byte) (0x10 | DESFireEV1.KSONE_CONFIG_CHANGABLE
									| DESFireEV1.KSONE_FILE_DEL_NO_MKEY
									| DESFireEV1.KSONE_GET_NO_MKEY | DESFireEV1.KSONE_APP_MKEY_CHANGABLE),
							10, DESFireEV1.KeyType.DES);

			// mDesFireEV1
			// .createApplication(
			// 1,
			// (byte) (0x10 | DESFire.KSONE_CONFIG_CHANGABLE
			// | DESFire.KSONE_FILE_DEL_NO_MKEY
			// | DESFire.KSONE_GET_NO_MKEY | DESFire.KSONE_APP_MKEY_CHANGABLE),
			// 10, DESFire.KeyType.DES, ISO_APP_ID, isoAppName);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testcreateApplicationP1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF11, FRSID");
		report.log(LogLevel.INFO, TAG, "testcreateApplicationP1, end");

	}

	public void testcreateApplicationP3() {

		report.log(LogLevel.INFO, TAG, "PiccCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testcreateApplicationP3, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {

			mDesFireEV1
					.createApplication(
							1,
							(byte) (0x10 | DESFireEV1.KSONE_CONFIG_CHANGABLE
									| DESFireEV1.KSONE_FILE_DEL_NO_MKEY
									| DESFireEV1.KSONE_GET_NO_MKEY | DESFireEV1.KSONE_APP_MKEY_CHANGABLE),
							10, DESFireEV1.KeyType.AES);

			// mDesFireEV1
			// .createApplication(
			// 1,
			// (byte) (0x10 | DESFire.KSONE_CONFIG_CHANGABLE
			// | DESFire.KSONE_FILE_DEL_NO_MKEY
			// | DESFire.KSONE_GET_NO_MKEY | DESFire.KSONE_APP_MKEY_CHANGABLE),
			// 10, DESFire.KeyType.DES, ISO_APP_ID, isoAppName);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testcreateApplicationP3 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF11, FRSID");
		report.log(LogLevel.INFO, TAG, "testcreateApplicationP3, end");

	}

	public void testcreateApplicationP5() {

		report.log(LogLevel.INFO, TAG, "PiccCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testcreateApplicationP2, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {

			// mDesFireEV1 .createApplication( 1, (byte) (0x10 |
			// DESFire.KSONE_CONFIG_CHANGABLE | DESFire.KSONE_FILE_DEL_NO_MKEY |
			// DESFire.KSONE_GET_NO_MKEY | DESFire.KSONE_APP_MKEY_CHANGABLE),
			// 10, DESFire.KeyType.AES);

			mDesFireEV1
					.createApplication(
							new byte[] { 0x01, 0x02, 0x03 },
							(byte) (0x10 | DESFireEV1.KSONE_CONFIG_CHANGABLE
									| DESFireEV1.KSONE_FILE_DEL_NO_MKEY
									| DESFireEV1.KSONE_GET_NO_MKEY | DESFireEV1.KSONE_APP_MKEY_CHANGABLE),
							10, DESFireEV1.KeyType.AES, ISO_APP_ID, isoAppName);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testcreateApplicationP2 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF11, FRSID");
		report.log(LogLevel.INFO, TAG, "testcreateApplicationP2, end");

	}

	public void testselectApplicationTestAppP5() {
		report.log(LogLevel.INFO, TAG, "applicationCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testselectApplicationTestAppP2, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {
			mDesFireEV1.selectApplication(new byte[] { 0x01, 0x02, 0x03 });
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG,
				"testselectApplicationTestAppP2 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF17, FRSID");
		report.log(LogLevel.INFO, TAG, "testselectApplicationTestAppP2, end");
	}

	
	public void testcreateApplicationP4() {

		report.log(LogLevel.INFO, TAG, "PiccCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testcreateApplicationP4, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {

			// mDesFireEV1 .createApplication( 1, (byte) (0x10 |
			// DESFire.KSONE_CONFIG_CHANGABLE | DESFire.KSONE_FILE_DEL_NO_MKEY |
			// DESFire.KSONE_GET_NO_MKEY | DESFire.KSONE_APP_MKEY_CHANGABLE),
			// 10, DESFire.KeyType.AES);

			mDesFireEV1
					.createApplication(
							1,
							(byte) (0x10 | DESFireEV1.KSONE_CONFIG_CHANGABLE
									| DESFireEV1.KSONE_FILE_DEL_NO_MKEY
									| DESFireEV1.KSONE_GET_NO_MKEY | DESFireEV1.KSONE_APP_MKEY_CHANGABLE),
							10, DESFireEV1.KeyType.DES, ISO_APP_ID, isoAppName);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testcreateApplicationP4 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF11, FRSID");
		report.log(LogLevel.INFO, TAG, "testcreateApplicationP4, end");

	}

	
	public void testcreateApplicationP2() {

		report.log(LogLevel.INFO, TAG, "PiccCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testcreateApplicationP2, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {

			// mDesFireEV1 .createApplication( 1, (byte) (0x10 |
			// DESFire.KSONE_CONFIG_CHANGABLE | DESFire.KSONE_FILE_DEL_NO_MKEY |
			// DESFire.KSONE_GET_NO_MKEY | DESFire.KSONE_APP_MKEY_CHANGABLE),
			// 10, DESFire.KeyType.AES);

			mDesFireEV1
					.createApplication(
							1,
							(byte) (0x10 | DESFireEV1.KSONE_CONFIG_CHANGABLE
									| DESFireEV1.KSONE_FILE_DEL_NO_MKEY
									| DESFireEV1.KSONE_GET_NO_MKEY | DESFireEV1.KSONE_APP_MKEY_CHANGABLE),
							10, DESFireEV1.KeyType.AES, ISO_APP_ID, isoAppName);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testcreateApplicationP2 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF11, FRSID");
		report.log(LogLevel.INFO, TAG, "testcreateApplicationP2, end");

	}

	public void testformatApplicationKeySettingOne() {

		report.log(LogLevel.INFO, TAG, "PiccCommands, GroupName");
		report.log(LogLevel.INFO, TAG,
				"testformatApplicationKeySettingOne, start");
		prepareSoftwareKeyStore();
		boolean result;
		// keySettingsOne =
		// mDesFireEV1.formatApplicationKeySettingOne(DESFireEV1.KSONE_SAME_KEY_AUTH,
		// DESFireEV1.KSONE_CONFIG_CHANGABLE);

		result = true;
		report.log(LogLevel.INFO, TAG,
				"testformatApplicationKeySettingOne result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF11, FRSID");
		report.log(LogLevel.INFO, TAG,
				"testformatApplicationKeySettingOne, end");

	}

	public void testformatApplicationKeySettingTwo() {

		report.log(LogLevel.INFO, TAG, "PiccCommands, GroupName");
		report.log(LogLevel.INFO, TAG,
				"testformatApplicationKeySettingTwo, start");
		prepareSoftwareKeyStore();
		boolean result;
		keySettingsTwo = mDesFireEV1.formatApplicationKeySettingTwo((byte) 10,
				DESFireEV1.KSTWO_DES_2K3DES, false);

		result = true;
		report.log(LogLevel.INFO, TAG,
				"testformatApplicationKeySettingTwo result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF11, FRSID");
		report.log(LogLevel.INFO, TAG,
				"testformatApplicationKeySettingTwo, end");

	}

	public void testcreateApplicationP6() {

		report.log(LogLevel.INFO, TAG, "PiccCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testcreateApplicationP6, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {

			mDesFireEV1.createApplication(appIdOne, keySettingsOne,
					keySettingsOne);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testcreateApplicationP6 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF11, FRSID");
		report.log(LogLevel.INFO, TAG, "testcreateApplicationP6, end");

	}

	public void testcreateApplicationP7() {

		report.log(LogLevel.INFO, TAG, "PiccCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testcreateApplicationP7, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {

			mDesFireEV1.createApplication(appIdTwo, keySettingsOne,
					keySettingsTwo);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testcreateApplicationP7 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF11, FRSID");
		report.log(LogLevel.INFO, TAG, "testcreateApplicationP7, end");

	}

	/** Appid as integer. */
	public void testcreateApplicationP8() {

		report.log(LogLevel.INFO, TAG, "PiccCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testcreateApplicationP8, start");
		prepareSoftwareKeyStore();
		boolean result;
		int appId = 8;
		try {

			mDesFireEV1
					.createApplication(appId, keySettingsOne, keySettingsTwo);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testcreateApplicationP8 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF11, FRSID");
		report.log(LogLevel.INFO, TAG, "testcreateApplicationP8, end");

	}

	/** Appid as keytype AES. */
	public void testcreateApplicationP9() {

		report.log(LogLevel.INFO, TAG, "PiccCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testcreateApplicationP9, start");
		prepareSoftwareKeyStore();
		boolean result;
		int appId = 9;
		try {

			mDesFireEV1.createApplication(appId, keySettingsOne, 10,
					KeyType.AES);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testcreateApplicationP9 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF11, FRSID");
		report.log(LogLevel.INFO, TAG, "testcreateApplicationP9, end");

	}

	public void testcreateAppP10P11P12() {
		testcreateApplicationP10();
		testcreateApplicationP11();
		testcreateApplicationP12();
	}

	/** Appid as keytype AES. */
	public void testcreateApplicationP10() {

		report.log(LogLevel.INFO, TAG, "PiccCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testcreateApplicationP10, start");
		prepareSoftwareKeyStore();
		boolean result;
		int appId = 10;
		byte[] dfName = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06,
				0x07, 0x08, 0x09, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15 };
		try {

			mDesFireEV1.createApplication(appId, keySettingsOne, 10,
					KeyType.AES, dfName);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testcreateApplicationP10 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF11, FRSID");
		report.log(LogLevel.INFO, TAG, "testcreateApplicationP10, end");

	}

	/** Appid as keytype AES. */
	public void testcreateApplicationP11() {

		report.log(LogLevel.INFO, TAG, "PiccCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testcreateApplicationP11, start");
		prepareSoftwareKeyStore();
		boolean result;
		int appId = 11;
		String dfName = "Nxpsemiconductorsu";
		try {

			mDesFireEV1.createApplication(appId, keySettingsOne, 10,
					KeyType.AES, dfName);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testcreateApplicationP11 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF11, FRSID");
		report.log(LogLevel.INFO, TAG, "testcreateApplicationP11, end");

	}

	public void testcreateApplicationP12() {

		report.log(LogLevel.INFO, TAG, "PiccCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testcreateApplicationP12, start");
		prepareSoftwareKeyStore();
		boolean result;
		byte[] appId = new byte[] { 0x07, 0x08, 0x09 };
		try {

			mDesFireEV1
					.createApplication(
							appId,
							(byte) (0x10 | DESFireEV1.KSONE_CONFIG_CHANGABLE
									| DESFireEV1.KSONE_FILE_DEL_NO_MKEY
									| DESFireEV1.KSONE_GET_NO_MKEY | DESFireEV1.KSONE_APP_MKEY_CHANGABLE),
							10, DESFireEV1.KeyType.AES, ISO_APP_ID, isoAppName);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testcreateApplicationP12 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF11, FRSID");
		report.log(LogLevel.INFO, TAG, "testcreateApplicationP12, end");

	}

	public void testcreateApplicationP13() {

		report.log(LogLevel.INFO, TAG, "PiccCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testcreateApplicationP13, start");
		prepareSoftwareKeyStore();
		boolean result;
		int appId = 13;
		try {

			mDesFireEV1
					.createApplication(
							appId,
							(byte) (0x10 | DESFireEV1.KSONE_CONFIG_CHANGABLE
									| DESFireEV1.KSONE_FILE_DEL_NO_MKEY
									| DESFireEV1.KSONE_GET_NO_MKEY | DESFireEV1.KSONE_APP_MKEY_CHANGABLE),
							10, DESFireEV1.KeyType.AES, ISO_APP_ID, isoAppName);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testcreateApplicationP13 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF11, FRSID");
		report.log(LogLevel.INFO, TAG, "testcreateApplicationP13, end");

	}

	public void testcreateApplicationN1() {

		report.log(LogLevel.INFO, TAG, "PiccCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testcreateApplicationN1, start");
		prepareSoftwareKeyStore();
		boolean result = false;
		try {

			// mDesFireEV1 .createApplication( 1, (byte) (0x10 |
			// DESFire.KSONE_CONFIG_CHANGABLE | DESFire.KSONE_FILE_DEL_NO_MKEY |
			// DESFire.KSONE_GET_NO_MKEY | DESFire.KSONE_APP_MKEY_CHANGABLE),
			// -1, DESFire.KeyType.DES);

			mDesFireEV1
					.createApplication(
							1,
							(byte) (0x10 | DESFireEV1.KSONE_CONFIG_CHANGABLE
									| DESFireEV1.KSONE_FILE_DEL_NO_MKEY
									| DESFireEV1.KSONE_GET_NO_MKEY | DESFireEV1.KSONE_APP_MKEY_CHANGABLE),
							-1, DESFireEV1.KeyType.DES, ISO_APP_ID,
							"ISOAPP".getBytes());
			result = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			result = true;
			e.printStackTrace();
		} catch (SmartCardException e) {
			result = true;
			e.printStackTrace();
		}
		report.log(LogLevel.INFO, TAG, "testcreateApplicationN1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF11, FRSID");
		report.log(LogLevel.INFO, TAG, "testcreateApplicationN1, end");

	}

	public void testcreateApplicationN2() {

		report.log(LogLevel.INFO, TAG, "PiccCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testcreateApplicationN2, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {

			// mDesFireEV1 .createApplication( 1, (byte) (0x10 |
			// DESFire.KSONE_CONFIG_CHANGABLE | DESFire.KSONE_FILE_DEL_NO_MKEY |
			// DESFire.KSONE_GET_NO_MKEY | DESFire.KSONE_APP_MKEY_CHANGABLE),
			// 10, DESFire.KeyType.AES);

			mDesFireEV1
					.createApplication(
							1,
							(byte) (0x10 | DESFireEV1.KSONE_CONFIG_CHANGABLE
									| DESFireEV1.KSONE_FILE_DEL_NO_MKEY
									| DESFireEV1.KSONE_GET_NO_MKEY | DESFireEV1.KSONE_APP_MKEY_CHANGABLE),
							10, DESFireEV1.KeyType.AES, ISO_APP_ID,
							"ISOAPP".getBytes());
			result = false;
		} catch (IOException e) {
			result = true;
			e.printStackTrace();
		} catch (SmartCardException e) {
			result = true;
			e.printStackTrace();
		}
		report.log(LogLevel.INFO, TAG, "testcreateApplicationN2 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF11, FRSID");
		report.log(LogLevel.INFO, TAG, "testcreateApplicationN2, end");

	}

	
	public void testdeleteApplicationP1() {

		report.log(LogLevel.INFO, TAG, "PiccCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testdeleteApplicationP1, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {
			mDesFireEV1.deleteApplication(1);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testdeleteApplicationP1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF13, FRSID");
		report.log(LogLevel.INFO, TAG, "testdeleteApplicationP1, end");
	}

	
	public void testdeleteApplicationN1() {

		report.log(LogLevel.INFO, TAG, "PiccCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testdeleteApplicationN1, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {
			mDesFireEV1.deleteApplication(100);
			result = false;
		} catch (IOException e) {
			result = true;
			e.printStackTrace();
		} catch (SmartCardException e) {
			result = true;
			e.printStackTrace();
		}
		report.log(LogLevel.INFO, TAG, "testdeleteApplicationN1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF13, FRSID");
		report.log(LogLevel.INFO, TAG, "testdeleteApplicationN1, end");
	}

	
	public void testgetApplicationIDsP1() {

		report.log(LogLevel.INFO, TAG, "PiccCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testgetApplicationIDsP1, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {
			int[] aids = mDesFireEV1.getApplicationIDs();
			byte[] data = null;
			for (int idx = 0; idx < aids.length; idx++) {
				data = modules.getUtility().append(data,
						modules.getUtility().intToBytes(aids[idx], 3));
			}
			report.log(LogLevel.INFO, TAG, "Result: "
					+ modules.getUtility().dumpBytes(data));
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testgetApplicationIDsP1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF14, FRSID");
		report.log(LogLevel.INFO, TAG, "testgetApplicationIDsP1, end");
	}

	public void testgetApplicationIDsN1() {

		report.log(LogLevel.INFO, TAG, "PiccCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testgetApplicationIDsN1, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {
			int[] aids = mDesFireEV1.getApplicationIDs();
			byte[] data = null;
			for (int idx = 0; idx < aids.length; idx++) {
				data = modules.getUtility().append(data,
						modules.getUtility().intToBytes(aids[idx], 3));
			}
			report.log(LogLevel.INFO, TAG, "Result: "
					+ modules.getUtility().dumpBytes(data));
			result = false;
		} catch (IOException e) {
			result = true;
			e.printStackTrace();
		} catch (SmartCardException e) {
			result = true;
			e.printStackTrace();
		}
		report.log(LogLevel.INFO, TAG, "testgetApplicationIDsN1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF14, FRSID");
		report.log(LogLevel.INFO, TAG, "testgetApplicationIDsN1, end");
	}

	
	public void testgetCardUIDP1() {

		report.log(LogLevel.INFO, TAG, "PiccCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testgetCardUIDP1, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {
			report.log(LogLevel.INFO, TAG, "Result: " + ""); // mDesFireEV1.getLibraryVersion());
			report.log(LogLevel.INFO, TAG, "Result: "
					+ modules.getUtility().dumpBytes(mDesFireEV1.getCardUID()));
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testgetCardUIDP1 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF24, FRSID");
		report.log(LogLevel.INFO, TAG, "testgetCardUIDP1, end");
	}

	public void testgetCardUIDN1() {

		report.log(LogLevel.INFO, TAG, "PiccCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testgetCardUIDN1, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {
			report.log(LogLevel.INFO, TAG, "Result: "
					+ modules.getUtility().dumpBytes(mDesFireEV1.getCardUID()));
			result = false;
		} catch (IOException e) {
			result = true;
			e.printStackTrace();
		} catch (SmartCardException e) {
			result = true;
			e.printStackTrace();
		}
		report.log(LogLevel.INFO, TAG, "testgetCardUIDN1 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF24, FRSID");
		report.log(LogLevel.INFO, TAG, "testgetCardUIDN1, end");
	}

	
	public void testgetFreeMemP1() {

		report.log(LogLevel.INFO, TAG, "PiccCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testgetFreeMemP1, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {
			report.log(
					LogLevel.INFO,
					TAG,
					"Result: "
							+ modules.getUtility().dumpBytes(
									modules.getUtility().intToBytes(
											mDesFireEV1.getFreeMem(), 4)));
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testgetFreeMemP1 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF21, FRSID");
		report.log(LogLevel.INFO, TAG, "testgetFreeMemP1, end");
	}

	
	public void testgetDFNameP1() {

		report.log(LogLevel.INFO, TAG, "PiccCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testgetDFNameP1, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {
			ArrayList<DESFireEV1.DFName> array = mDesFireEV1.getDFName();
			for (int idx = 0; idx < array.size(); idx++) {
				DESFireEV1.DFName obj = array.get(idx);
				report.log(
						LogLevel.INFO,
						TAG,
						"AID: " + obj.getAID() + "DFName: "
								+ obj.getDFNameAsString() + "FID: "
								+ obj.getFID());

			}
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testgetDFNameP1 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF15, FRSID");
		report.log(LogLevel.INFO, TAG, "testgetDFNameP1, end");
	}

	public void testgetDFNameN1() {

		report.log(LogLevel.INFO, TAG, "PiccCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testgetDFNameN1, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {
			ArrayList<DESFireEV1.DFName> array = mDesFireEV1.getDFName();
			for (int idx = 0; idx < array.size(); idx++) {
				DESFireEV1.DFName obj = array.get(idx);
				report.log(
						LogLevel.INFO,
						TAG,
						"AID: " + obj.getAID() + "DFName: "
								+ obj.getDFNameAsString() + "FID: "
								+ obj.getFID());

			}
			result = false;
		} catch (IOException e) {
			result = true;
			e.printStackTrace();
		} catch (SmartCardException e) {
			result = true;
			e.printStackTrace();
		}
		report.log(LogLevel.INFO, TAG, "testgetDFNameN1 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF15, FRSID");
		report.log(LogLevel.INFO, TAG, "testgetDFNameN1, end");
	}

	
	public void testformatP1() {

		report.log(LogLevel.INFO, TAG, "PiccCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testformatP1, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {
			mDesFireEV1.format();
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testformatP1 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF18, FRSID");
		report.log(LogLevel.INFO, TAG, "testformatP1, end");
	}

	
	public void testformatP2() {

		report.log(LogLevel.INFO, TAG, "PiccCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testformatP1, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {
			mDesFireEV1.format();
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testformatP1 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF18, FRSID");
		report.log(LogLevel.INFO, TAG, "testformatP1, end");
	}

	public void testformatN1() {

		report.log(LogLevel.INFO, TAG, "PiccCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testformatN1, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {
			mDesFireEV1.format();
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
		}
		report.log(LogLevel.INFO, TAG, "testformatN1 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF18, FRSID");
		report.log(LogLevel.INFO, TAG, "testformatN1, end");
	}

	
	public void testcreateStandardFileP1() {

		report.log(LogLevel.INFO, TAG, "CreateFileOperations, GroupName");
		report.log(LogLevel.INFO, TAG, "testcreateStandardFileP1, start");
		prepareSoftwareKeyStore();
		boolean result = false;

		try {

			mDesFireEV1.createFile(STD_DATA_FILE_NO, new StdDataFileSettings(
					DESFireEV1.CommunicationType.MACed, 0, 0, 0, 0,
					DATA_FILE_SIZE));

			result = true;
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			// TODO Auto-generated catch block
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testcreateStandardFileP1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF29, FRSID");
		report.log(LogLevel.INFO, TAG, "testcreateStandardFileP1, end");

	}

	
	public void testcreateBackupDataFileP1() {

		report.log(LogLevel.INFO, TAG, "CreateFileOperations, GroupName");
		report.log(LogLevel.INFO, TAG, "testcreateBackupDataFileP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			mDesFireEV1.createFile(BACKUP_DATA_FILE_NO,
					new BackupDataFileSettings(
							DESFireEV1.CommunicationType.Enciphered, 0, 0, 0,
							0, DATA_FILE_SIZE));
			result = true;
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testcreateBackupDataFileP1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF30, FRSID");
		report.log(LogLevel.INFO, TAG, "testcreateBackupDataFileP1, end");

	}

	
	public void testcreateValueFileP1() {

		report.log(LogLevel.INFO, TAG, "CreateFileOperations, GroupName");
		report.log(LogLevel.INFO, TAG, "testcreateValueFileP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			mDesFireEV1.createFile(VALUE_FILE_NO, new ValueFileSettings(
					DESFireEV1.CommunicationType.Enciphered, 0, 0, 0, 0, 1,
					100, 10, true));
			result = true;
		} catch (DESFireException e) {
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
			result = false;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testcreateValueFileP1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF31, FRSID");
		report.log(LogLevel.INFO, TAG, "testcreateValueFileP1, end");

	}

	
	public void testcreateLinearRecordFileP1() {

		report.log(LogLevel.INFO, TAG, "CreateFileOperations, GroupName");
		report.log(LogLevel.INFO, TAG, "testcreateLinearRecordFileP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			mDesFireEV1.createFile(LINER_RECORD_FILE_NO,
					new LinearRecordFileSettings(
							DESFireEV1.CommunicationType.Enciphered, 0, 0, 0,
							0x0E, RECORD_SIZE, NO_OF_RECORDS, 0));
			result = true;
		} catch (DESFireException e) {
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
			result = false;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG,
				"testcreateLinearRecordFileP1 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF32, FRSID");
		report.log(LogLevel.INFO, TAG, "testcreateLinearRecordFileP1, end");

	}

	
	public void testcreateCyclicRecordFileP1() {

		report.log(LogLevel.INFO, TAG, "CreateFileOperations, GroupName");
		report.log(LogLevel.INFO, TAG, "testcreateCyclicRecordFileP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			mDesFireEV1.createFile(CYCLIC_RECORD_FILE_NO,
					new CyclicRecordFileSettings(
							DESFireEV1.CommunicationType.Enciphered, 0, 0, 0,
							0x0E, 16, NO_OF_RECORDS, 0));
			result = true;
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG,
				"testcreateCyclicRecordFileP1 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF33, FRSID");
		report.log(LogLevel.INFO, TAG, "testcreateCyclicRecordFileP1, end");

	}

	
	public void testGetFileIDsP1() {

		report.log(LogLevel.INFO, TAG, "GetFileIDsOperations, GroupName");
		report.log(LogLevel.INFO, TAG, "testGetFileIDsP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			byte[] ids = mDesFireEV1.getFileIDs();
			report.log(LogLevel.INFO, TAG, "All File Ids" + ids);
			result = true;
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testGetFileIDsP1 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF25, FRSID");
		report.log(LogLevel.INFO, TAG, "testGetFileIDsP1, end");

	}

	
	public void testDeleteStandardFileP1() {

		report.log(LogLevel.INFO, TAG, "DeleteFileOperations, GroupName");
		report.log(LogLevel.INFO, TAG, "testDeleteStandardFileP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			mDesFireEV1.deleteFile(STD_DATA_FILE_NO);
			result = true;
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testDeleteStandardFileP1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF35, FRSID");
		report.log(LogLevel.INFO, TAG, "testDeleteStandardFileP1, end");

	}

	
	public void testDeleteBackupDataFileP1() {

		report.log(LogLevel.INFO, TAG, "DeleteFileOperations, GroupName");
		report.log(LogLevel.INFO, TAG, "testDeleteBackupDataFileP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			mDesFireEV1.deleteFile(BACKUP_DATA_FILE_NO);
			result = true;
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testDeleteBackupDataFileP1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF35, FRSID");
		report.log(LogLevel.INFO, TAG, "testDeleteBackupDataFileP1, end");
	}

	
	public void testDeleteValueFileP1() {

		report.log(LogLevel.INFO, TAG, "DeleteFileOperations, GroupName");
		report.log(LogLevel.INFO, TAG, "testDeleteValueFileP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			mDesFireEV1.deleteFile(VALUE_FILE_NO);
			result = true;
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testDeleteValueFileP1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF35, FRSID");
		report.log(LogLevel.INFO, TAG, "testDeleteValueFileP1, end");

	}

	
	public void testDeleteLinearRecordFileP1() {

		report.log(LogLevel.INFO, TAG, "DeleteFileOperations, GroupName");
		report.log(LogLevel.INFO, TAG, "testDeleteLinearRecordFileP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			mDesFireEV1.deleteFile(LINER_RECORD_FILE_NO);
			result = true;
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG,
				"testDeleteLinearRecordFileP1 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF35, FRSID");
		report.log(LogLevel.INFO, TAG, "testDeleteLinearRecordFileP1, end");

	}

	
	public void testDeleteCyclicRecordFileP1() {

		report.log(LogLevel.INFO, TAG, "CreateFileOperations, GroupName");
		report.log(LogLevel.INFO, TAG, "testDeleteCyclicRecordFileP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			mDesFireEV1.deleteFile(CYCLIC_RECORD_FILE_NO);
			result = true;
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG,
				"testDeleteCyclicRecordFileP1 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF35, FRSID");
		report.log(LogLevel.INFO, TAG, "testDeleteCyclicRecordFileP1, end");

	}

	
	public void testcreateISOStandardFileP1() {

		report.log(LogLevel.INFO, TAG, "CreateISOFileOperations, GroupName");
		report.log(LogLevel.INFO, TAG, "testcreateISOStandardFileP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			mDesFireEV1.createFile(ISOSTD_DATA_FILE_NO, ISO_STD_DATA_FILE_NO,
					new StdDataFileSettings(
							DESFireEV1.CommunicationType.Enciphered, 0, 0, 0,
							0, DATA_FILE_SIZE));
			result = true;
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testcreateISOStandardFileP1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF29, FRSID");
		report.log(LogLevel.INFO, TAG, "testcreateISOStandardFileP1, end");

	}

	
	public void testcreateISOStandardFileP2() {

		report.log(LogLevel.INFO, TAG, "CreateISOFileOperations, GroupName");
		report.log(LogLevel.INFO, TAG, "testcreateISOStandardFileP2, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			mDesFireEV1.createFile(ISOSTD_DATA_FILE_NO, ISO_STD_DATA_FILE_NO,
					new StdDataFileSettings(DESFireEV1.CommunicationType.Plain,
							0, 0, 0, 0, DATA_FILE_SIZE));
			result = true;
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testcreateISOStandardFileP2 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF29, FRSID");
		report.log(LogLevel.INFO, TAG, "testcreateISOStandardFileP2, end");

	}

	
	public void testcreateISOBackupDataFileP1() {

		report.log(LogLevel.INFO, TAG, "CreateISOFileOperations, GroupName");
		report.log(LogLevel.INFO, TAG, "testcreateISOBackupDataFileP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			mDesFireEV1.createFile(ISOBACKUP_DATA_FILE_NO,
					ISO_BACKUP_DATA_FILE_NO, new BackupDataFileSettings(
							DESFireEV1.CommunicationType.Plain, 0, 0, 0, 0,
							DATA_FILE_SIZE));
			result = true;
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG,
				"testcreateISOBackupDataFileP1 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF30, FRSID");
		report.log(LogLevel.INFO, TAG, "testcreateISOBackupDataFileP1, end");

	}

	
	public void testcreateISOLinearRecordFileP1() {

		report.log(LogLevel.INFO, TAG, "CreateISOFileOperations, GroupName");
		report.log(LogLevel.INFO, TAG, "testcreateISOLinearRecordFileP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			mDesFireEV1.createFile(ISOLINER_RECORD_FILE_NO,
					ISO_LINER_RECORD_FILE_NO, new LinearRecordFileSettings(
							DESFireEV1.CommunicationType.MACed, 0, 0, 0, 0, 16,
							NO_OF_RECORDS, 0));
			result = true;
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG,
				"testcreateISOLinearRecordFileP1 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF32, FRSID");
		report.log(LogLevel.INFO, TAG, "testcreateISOLinearRecordFileP1, end");

	}

	
	public void testcreateISOLinearRecordFileP2() {

		report.log(LogLevel.INFO, TAG, "CreateISOFileOperations, GroupName");
		report.log(LogLevel.INFO, TAG, "testcreateISOLinearRecordFileP2, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			mDesFireEV1.createFile(ISOLINER_RECORD_FILE_NO,
					ISO_LINER_RECORD_FILE_NO, new LinearRecordFileSettings(
							DESFireEV1.CommunicationType.Plain, 0, 0, 0, 0, 16,
							NO_OF_RECORDS, 0));
			result = true;
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG,
				"testcreateISOLinearRecordFileP2 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF32, FRSID");
		report.log(LogLevel.INFO, TAG, "testcreateISOLinearRecordFileP2, end");

	}

	
	public void testcreateISOCyclicRecordFileP1() {

		report.log(LogLevel.INFO, TAG, "CreateISOFileOperations, GroupName");
		report.log(LogLevel.INFO, TAG, "testcreateISOCyclicRecordFileP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			mDesFireEV1.createFile(ISOCYCLIC_RECORD_FILE_NO,
					ISO_CYCLIC_RECORD_FILE_NO, new CyclicRecordFileSettings(
							DESFireEV1.CommunicationType.Enciphered, 0, 0, 0,
							0, 16, NO_OF_RECORDS, 0));
			result = true;
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG,
				"testcreateISOCyclicRecordFileP1 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF33, FRSID");
		report.log(LogLevel.INFO, TAG, "testcreateISOCyclicRecordFileP1, end");

	}

	
	public void testGetISOFileIDsP1() {

		report.log(LogLevel.INFO, TAG, "GetISOFileIDs Operations, GroupName");
		report.log(LogLevel.INFO, TAG, "testGetISOFileIDsP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			int[] ids = mDesFireEV1.getISOFileIDs();
			report.log(LogLevel.INFO, TAG, "All File Ids" + ids);
			result = true;
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testGetISOFileIDsP1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF26, FRSID");
		report.log(LogLevel.INFO, TAG, "testGetISOFileIDsP1, end");

	}

	
	public void testGetSTDFileSettingsP1() {

		report.log(LogLevel.INFO, TAG, "GetFileSettings Operations, GroupName");
		report.log(LogLevel.INFO, TAG, "testGetSTDFileSettingsP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			FileSettings ids = mDesFireEV1.getFileSettings(STD_DATA_FILE_NO);
			report.log(LogLevel.INFO, TAG, "All File Ids" + ids);
			result = true;
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testGetSTDFileSettingsP1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF27, FRSID");
		report.log(LogLevel.INFO, TAG, "testGetSTDFileSettingsP1, end");

	}

	
	public void testGetBACKUPFileSettingsP1() {

		report.log(LogLevel.INFO, TAG, "GetFileSettings Operations, GroupName");
		report.log(LogLevel.INFO, TAG, "testGetBACKUPFileSettingsP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			FileSettings ids = mDesFireEV1.getFileSettings(BACKUP_DATA_FILE_NO);
			report.log(LogLevel.INFO, TAG, "All File Ids" + ids);
			result = true;
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testGetBACKUPFileSettingsP1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF27, FRSID");
		report.log(LogLevel.INFO, TAG, "testGetBACKUPFileSettingsP1, end");

	}

	
	public void testGetValueFileSettingsP1() {

		report.log(LogLevel.INFO, TAG, "GetFileSettings Operations, GroupName");
		report.log(LogLevel.INFO, TAG, "testGetValueFileSettingsP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			FileSettings ids = mDesFireEV1.getFileSettings(VALUE_FILE_NO);
			report.log(LogLevel.INFO, TAG, "All File Ids" + ids);
			result = true;
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testGetValueFileSettingsP1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF27, FRSID");
		report.log(LogLevel.INFO, TAG, "testGetValueFileSettingsP1, end");

	}

	
	public void testGetLinerFileSettingsP1() {

		report.log(LogLevel.INFO, TAG, "GetFileSettings Operations, GroupName");
		report.log(LogLevel.INFO, TAG, "testGetLinerFileSettingsP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			FileSettings ids = mDesFireEV1
					.getFileSettings(LINER_RECORD_FILE_NO);
			report.log(LogLevel.INFO, TAG, "All File Ids" + ids);
			result = true;
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testGetLinerFileSettingsP1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF27, FRSID");
		report.log(LogLevel.INFO, TAG, "testGetLinerFileSettingsP1, end");

	}

	
	public void testGetCyclicFileSettingsP1() {

		report.log(LogLevel.INFO, TAG, "GetFileSettings Operations, GroupName");
		report.log(LogLevel.INFO, TAG, "testGetCyclicFileSettingsP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			FileSettings ids = mDesFireEV1
					.getFileSettings(CYCLIC_RECORD_FILE_NO);
			report.log(LogLevel.INFO, TAG, "All File Ids" + ids);
			result = true;
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testGetCyclicFileSettingsP1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF27, FRSID");
		report.log(LogLevel.INFO, TAG, "testGetCyclicFileSettingsP1, end");

	}

	// Data Manipulations Commands
	
	public void testWriteDataP1() {

		report.log(LogLevel.INFO, TAG, "DataManipulationsCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testWriteDataP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			mDesFireEV1.writeData(STD_DATA_FILE_NO, 0, bigData);
			mDesFireEV1.writeData(BACKUP_DATA_FILE_NO, 0, data);
			// mDesFireEV1.commitTransaction();
			result = true;
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testWriteDataP1 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF37, FRSID");
		report.log(LogLevel.INFO, TAG, "testWriteDataP1, end");

	}

	
	public void testReadDataP1() {

		report.log(LogLevel.INFO, TAG, "DataManipulationsCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testReadDataP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			byte[] StdFile_value = mDesFireEV1.readData(STD_DATA_FILE_NO, 0, 4);
			report.log(LogLevel.INFO, TAG, "Std Data File Data: "
					+ StdFile_value);
			byte[] Backup_value = mDesFireEV1.readData(BACKUP_DATA_FILE_NO, 0,
					4);
			report.log(LogLevel.INFO, TAG, "BackUp File Data: " + Backup_value
					+ "Std Data File: " + StdFile_value);
			result = true;
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testReadDataP1 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF36, FRSID");
		report.log(LogLevel.INFO, TAG, "testReadDataP1, end");

	}

	
	public void testWriteISODataP1() {

		report.log(LogLevel.INFO, TAG, "DataManipulationsCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testWriteISODataP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			mDesFireEV1.writeData(ISOSTD_DATA_FILE_NO, 0, data);
			mDesFireEV1.writeData(ISOBACKUP_DATA_FILE_NO, 0, data);
			mDesFireEV1.commitTransaction();
			result = true;
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testWriteISODataP1 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF37, FRSID");
		report.log(LogLevel.INFO, TAG, "testWriteISODataP1, end");

	}

	
	public void testWriteISODataP2() {

		report.log(LogLevel.INFO, TAG, "DataManipulationsCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testWriteISODataP2, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			mDesFireEV1.writeData(ISOSTD_DATA_FILE_NO, 0, data);
			mDesFireEV1.writeData(ISOBACKUP_DATA_FILE_NO, 0, data);
			mDesFireEV1.abortTransaction();
			mDesFireEV1.commitTransaction();
			result = true;
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testWriteISODataP2 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF37, FRSID");
		report.log(LogLevel.INFO, TAG, "testWriteISODataP2, end");

	}

	
	public void testReadISODataP1() {

		report.log(LogLevel.INFO, TAG, "DataManipulationsCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testReadISODataP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			byte[] StdFile_value = mDesFireEV1.readData(ISOSTD_DATA_FILE_NO, 0,
					4);
			report.log(LogLevel.INFO, TAG, "Std Data File Data: "
					+ modules.getUtility().dumpBytes(StdFile_value));
			byte[] Backup_value = mDesFireEV1.readData(ISOBACKUP_DATA_FILE_NO,
					0, 4);
			report.log(LogLevel.INFO, TAG, "BackUp File Data:.............. "
					+ modules.getUtility().dumpBytes(Backup_value));
			result = true;
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testReadISODataP1 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF36, FRSID");
		report.log(LogLevel.INFO, TAG, "testReadISODataP1, end");

	}

	
	public void testGetValueP1() {

		report.log(LogLevel.INFO, TAG, "DataManipulationsCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testGetValueP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			int ValueFile_Data = mDesFireEV1.getValue(VALUE_FILE_NO);
			report.log(LogLevel.INFO, TAG, "Std Data File Data: "
					+ ValueFile_Data);
			result = true;
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testGetValueP1 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF38, FRSID");
		report.log(LogLevel.INFO, TAG, "testGetValueP1, end");

	}

	
	public void testCreditP1() {
		report.log(LogLevel.INFO, TAG, "DataManipulationsCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testCreditP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			mDesFireEV1.credit(VALUE_FILE_NO, 5);
			mDesFireEV1.commitTransaction();
			int ValueFile_Data = mDesFireEV1.getValue(VALUE_FILE_NO);
			report.log(LogLevel.INFO, TAG, "Value After Credit: "
					+ ValueFile_Data);
			result = true;
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testCreditP1 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF39, FRSID");
		report.log(LogLevel.INFO, TAG, "testCreditP1, end");
	}

	
	public void testDebitP1() {

		report.log(LogLevel.INFO, TAG, "DataManipulationsCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testDebitP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			mDesFireEV1.debit(VALUE_FILE_NO, 5);
			mDesFireEV1.commitTransaction();
			int ValueFile_Data = mDesFireEV1.getValue(VALUE_FILE_NO);
			report.log(LogLevel.INFO, TAG, "Value After Debit: "
					+ ValueFile_Data);
			result = true;
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testDebitP1 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF40, FRSID");
		report.log(LogLevel.INFO, TAG, "testDebitP1, end");
	}

	
	public void testLimitedCreditP1() {
		report.log(LogLevel.INFO, TAG, "DataManipulationsCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testLimitedCreditP1, start");
		prepareSoftwareKeyStore();
		boolean result;
		try {
			mDesFireEV1.limitedCredit(VALUE_FILE_NO, 3);
			mDesFireEV1.commitTransaction();
			int ValueFile_Data = mDesFireEV1.getValue(VALUE_FILE_NO);
			report.log(LogLevel.INFO, TAG, "Value After limitedCredit"
					+ ValueFile_Data);
			result = true;
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testLimitedCreditP1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF41, FRSID");
		report.log(LogLevel.INFO, TAG, "testLimitedCreditP1, end");
	}

	
	public void testLinerWriteRecordP1() {

		report.log(LogLevel.INFO, TAG, "DataManipulationsCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testLinerWriteRecordP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			mDesFireEV1.writeRecord(LINER_RECORD_FILE_NO, 0, data);
			mDesFireEV1.commitTransaction();
			result = true;
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testLinerWriteRecordP1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF42, FRSID");
		report.log(LogLevel.INFO, TAG, "testLinerWriteRecordP1, end");
	}

	
	public void testLinerReadRecordP1() {

		report.log(LogLevel.INFO, TAG, "DataManipulationsCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testLinerReadRecordP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			byte[] recordData = mDesFireEV1.readRecords(LINER_RECORD_FILE_NO,
					0, 1);
			report.log(LogLevel.INFO, TAG, "Record Read: "
					+ modules.getUtility().dumpBytes(recordData));
			result = true;
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testLinerReadRecordP1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF43, FRSID");
		report.log(LogLevel.INFO, TAG, "testLinerReadRecordP1, end");
	}

	
	public void testLinerWriteISORecordP1() {

		report.log(LogLevel.INFO, TAG, "DataManipulationsCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testLinerWriteISORecordP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			mDesFireEV1.writeRecord(ISOLINER_RECORD_FILE_NO, 0, data);
			mDesFireEV1.commitTransaction();
			result = true;
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testLinerWriteISORecordP1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF42, FRSID");
		report.log(LogLevel.INFO, TAG, "testLinerWriteISORecordP1, end");
	}

	
	public void testLinerReadISORecordP1() {

		report.log(LogLevel.INFO, TAG, "DataManipulationsCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testLinerReadISORecordP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			byte[] recordData = mDesFireEV1.readRecords(
					ISOLINER_RECORD_FILE_NO, 0, 1);
			report.log(LogLevel.INFO, TAG, "Record Read: "
					+ modules.getUtility().dumpBytes(recordData));
			result = true;
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testLinerReadISORecordP1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF43, FRSID");
		report.log(LogLevel.INFO, TAG, "testLinerReadISORecordP1, end");
	}

	
	public void testCyclicWriteRecordP1() {

		report.log(LogLevel.INFO, TAG, "DataManipulationsCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testCyclicWriteRecordP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			mDesFireEV1.writeRecord(CYCLIC_RECORD_FILE_NO, 0, data);
			mDesFireEV1.commitTransaction();
			result = true;
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testCyclicWriteRecordP1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF42, FRSID");
		report.log(LogLevel.INFO, TAG, "testCyclicWriteRecordP1, end");
	}

	
	public void testCyclicReadRecordP1() {

		report.log(LogLevel.INFO, TAG, "DataManipulationsCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testCyclicReadRecordP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			byte[] dataread = mDesFireEV1.readRecords(CYCLIC_RECORD_FILE_NO, 0,
					1);
			report.log(LogLevel.INFO, TAG, "Record Data: "
					+ modules.getUtility().dumpBytes(dataread));
			result = true;
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testCyclicReadRecordP1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF43, FRSID");
		report.log(LogLevel.INFO, TAG, "testCyclicReadRecordP1, end");
	}

	
	public void testCyclicWriteISORecordP1() {

		report.log(LogLevel.INFO, TAG, "DataManipulationsCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testCyclicWriteISORecordP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			mDesFireEV1.writeRecord(ISOCYCLIC_RECORD_FILE_NO, 0, data);
			mDesFireEV1.commitTransaction();
			result = true;
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testCyclicWriteISORecordP1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF42, FRSID");
		report.log(LogLevel.INFO, TAG, "testCyclicWriteISORecordP1, end");
	}

	
	public void testCyclicReadISORecordP1() {

		report.log(LogLevel.INFO, TAG, "DataManipulationsCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testCyclicReadISORecordP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			byte[] dataread = mDesFireEV1.readRecords(ISOCYCLIC_RECORD_FILE_NO,
					0, 1);
			report.log(LogLevel.INFO, TAG, "Record Data: "
					+ modules.getUtility().dumpBytes(dataread));
			result = true;
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testCyclicReadISORecordP1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF43, FRSID");
		report.log(LogLevel.INFO, TAG, "testCyclicReadISORecordP1, end");
	}

	
	public void testClearCyclicRecordP1() {

		report.log(LogLevel.INFO, TAG, "DataManipulationsCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testClearCyclicRecordP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			mDesFireEV1.clearRecordFile(CYCLIC_RECORD_FILE_NO);
			result = true;
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testClearCyclicRecordP1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF45, FRSID");
		report.log(LogLevel.INFO, TAG, "testClearCyclicRecordP1, end");
	}

	
	public void testClearLinerRecordP1() {

		report.log(LogLevel.INFO, TAG, "DataManipulationsCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testClearLinerRecordP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			mDesFireEV1.clearRecordFile(LINER_RECORD_FILE_NO);
			result = true;
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testClearLinerRecordP1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF45, FRSID");
		report.log(LogLevel.INFO, TAG, "testClearLinerRecordP1, end");
	}

	// ISO Commands!!
	
	public void testISOAuthP1() {

		report.log(LogLevel.INFO, TAG, "ISOCommands Operations, GroupName");
		report.log(LogLevel.INFO, TAG, "testISOAuthP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			mDesFireEV1.isoAuthenticate(0, (byte) 0, 0,
					DESFireEV1.KeyType.TWOK3DES);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testISOAuthP1 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF55, FRSID");
		report.log(LogLevel.INFO, TAG, "testISOAuthP1, end");
	}

	
	public byte[] reverseBytes(final byte[] data) {
		final int len = data.length;
		final byte[] res = new byte[len];
		for (int i = 0; i <= len / 2; i++) {
			res[i] = data[len - 1 - i];
			res[len - 1 - i] = data[i];
		}
		return res;
	}

	
	public void testISOSelectFileP1() {

		report.log(LogLevel.INFO, TAG, "ISOCommands Operations, GroupName");
		report.log(LogLevel.INFO, TAG, "testISOSelectFileP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			mDesFireEV1.isoSelectFile(ISO_STD_DATA_FILE_NO, (byte) 0x00);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testISOSelectFileP1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF49, FRSID");
		report.log(LogLevel.INFO, TAG, "testISOSelectFileP1, end");
	}

	
	public void testISOSelectMasterFileP3() {
		report.log(LogLevel.INFO, TAG, "ISOCommands Operations, GroupName");
		report.log(LogLevel.INFO, TAG, "testISOSelectMasterFileP3, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			mDesFireEV1.isoSelectMasterFile();
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testISOSelectMasterFileP3 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF49, FRSID");
		report.log(LogLevel.INFO, TAG, "testISOSelectMasterFileP3, end");
	}

	
	public void testISOSelectMasterFileP2() {

		report.log(LogLevel.INFO, TAG, "ISOCommands Operations, GroupName");
		report.log(LogLevel.INFO, TAG, "testISOSelectMasterFileP2, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			mDesFireEV1.isoSelectFile(ISO_APP_ID, (byte) 0x00);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testISOSelectMasterFileP2 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF49, FRSID");
		report.log(LogLevel.INFO, TAG, "testISOSelectMasterFileP2, end");
	}

	
	public void testISOAppendRecordP1() {

		report.log(LogLevel.INFO, TAG, "ISOCommands Operations, GroupName");
		report.log(LogLevel.INFO, TAG, "testISOAppendRecordP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			mDesFireEV1.isoAppendRecord((byte) 0, (byte) 0, data);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testISOAppendRecordP1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF53, FRSID");
		report.log(LogLevel.INFO, TAG, "testISOAppendRecordP1, end");
	}

	
	public void testISOReadRecordP1() {

		report.log(LogLevel.INFO, TAG, "ISOCommands Operations, GroupName");
		report.log(LogLevel.INFO, TAG, "testISOReadRecordP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {

			byte[] resp = mDesFireEV1.isoReadRecords((byte) 0, (byte) 4,
					RECORD_SIZE + 8);
			report.log(LogLevel.INFO, TAG, "isoReadRecords Value: "
					+ modules.getUtility().dumpBytes(resp));
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testISOReadRecordP1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF52, FRSID");
		report.log(LogLevel.INFO, TAG, "testISOReadRecordP1, end");
	}

	
	public void testISOUpdateBinaryP1() {

		report.log(LogLevel.INFO, TAG, "ISOCommands Operations, GroupName");
		report.log(LogLevel.INFO, TAG, "testISOUpdateBinaryP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			mDesFireEV1.isoUpdateBinary(ISO_STD_DATA_FILE_NO, 0, data);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testISOUpdateBinaryP1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF51, FRSID");
		report.log(LogLevel.INFO, TAG, "testISOUpdateBinaryP1, end");
	}

	
	public void testISOReadBinaryP1() {

		report.log(LogLevel.INFO, TAG, "ISOCommands Operations, GroupName");
		report.log(LogLevel.INFO, TAG, "testISOReadBinaryP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			byte[] readBin = mDesFireEV1.isoReadBinary(ISO_STD_DATA_FILE_NO, 0,
					data.length + 8);
			report.log(LogLevel.INFO, TAG, "Binary Data: "
					+ modules.getUtility().dumpBytes(readBin));
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testISOReadBinaryP1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF50, FRSID");
		report.log(LogLevel.INFO, TAG, "testISOReadBinaryP1, end");
	}

	
	public void testISOSelectFileEFunderDF_StandardFileP1() {

		report.log(LogLevel.INFO, TAG, "ISOCommands Operations, GroupName");
		report.log(LogLevel.INFO, TAG,
				"testISOSelectFileEFunderDF_StandardFileP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			mDesFireEV1.isoSelectFileEFunderDF(ISO_STD_DATA_FILE_NO);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG,
				"testISOSelectFileEFunderDF_StandardFileP1 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF76, FRSID");
		report.log(LogLevel.INFO, TAG,
				"testISOSelectFileEFunderDF_StandardFileP1, end");
	}

	
	public void testISOSelectFileEFunderDF_RecordFileP1() {

		report.log(LogLevel.INFO, TAG, "ISOCommands Operations, GroupName");
		report.log(LogLevel.INFO, TAG,
				"testISOSelectFileEFunderDF_RecordFileP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			mDesFireEV1.isoSelectFileEFunderDF(ISO_LINER_RECORD_FILE_NO);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG,
				"testISOSelectFileEFunderDF_RecordFileP1 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF76, FRSID");
		report.log(LogLevel.INFO, TAG,
				"testISOSelectFileEFunderDF_RecordFileP1, end");
	}

	
	public void testISOSelectMasterFileP1() {

		report.log(LogLevel.INFO, TAG, "ISOCommands Operations, GroupName");
		report.log(LogLevel.INFO, TAG, "testISOSelectMasterFileP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			// mDesFireEV1.isoSelectFile(ISO_APP_ID, (byte) 0x00);
			mDesFireEV1.isoSelectMasterFile();
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testISOSelectMasterFileP1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF77, FRSID");
		report.log(LogLevel.INFO, TAG, "testISOSelectMasterFileP1, end");
	}

	
	public void testchangeStdFileSettingsP1() {
		report.log(LogLevel.INFO, TAG, "ChangeFileSettings, GroupName");
		report.log(LogLevel.INFO, TAG, "testchangeStdFileSettingsP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			mDesFireEV1.changeFileSettings(STD_DATA_FILE_NO,
					DESFireEV1.CommunicationType.Enciphered, 0, 0, 0, 0);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testchangeStdFileSettingsP1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF28, FRSID");
		report.log(LogLevel.INFO, TAG, "testchangeStdFileSettingsP1, end");
	}

	
	public void testchangeBackupDataFileSettingsP1() {
		report.log(LogLevel.INFO, TAG, "ChangeFileSettings, GroupName");
		report.log(LogLevel.INFO, TAG,
				"testchangeBackupDataFileSettingsP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			mDesFireEV1.changeFileSettings(BACKUP_DATA_FILE_NO,
					DESFireEV1.CommunicationType.Plain, 0, 0, 0, 0);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG,
				"testchangeBackupDataFileSettingsP1 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF28, FRSID");
		report.log(LogLevel.INFO, TAG,
				"testchangeBackupDataFileSettingsP1, end");
	}

	
	public void testchangeValueFileSettingsP1() {
		report.log(LogLevel.INFO, TAG, "ChangeFileSettings, GroupName");
		report.log(LogLevel.INFO, TAG, "testchangeValueFileSettingsP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			mDesFireEV1.changeFileSettings(VALUE_FILE_NO,
					DESFireEV1.CommunicationType.Enciphered, 0, 0, 0, 0);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG,
				"testchangeValueFileSettingsP1 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF28, FRSID");
		report.log(LogLevel.INFO, TAG, "testchangeValueFileSettingsP1, end");
	}

	
	public void testchangeLinearRecordFileSettingsP1() {
		report.log(LogLevel.INFO, TAG, "ChangeFileSettings, GroupName");
		report.log(LogLevel.INFO, TAG,
				"testchangeLinearRecordFileSettingsP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			mDesFireEV1.changeFileSettings(LINER_RECORD_FILE_NO,
					DESFireEV1.CommunicationType.Plain, 0, 0, 0, 0);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG,
				"testchangeLinearRecordFileSettingsP1 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF28, FRSID");
		report.log(LogLevel.INFO, TAG,
				"testchangeLinearRecordFileSettingsP1, end");
	}

	
	public void testchangeCyclicRecordFileSettingsP1() {
		report.log(LogLevel.INFO, TAG, "ChangeFileSettings, GroupName");
		report.log(LogLevel.INFO, TAG,
				"testchangeCyclicRecordFileSettingsP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			mDesFireEV1.changeFileSettings(CYCLIC_RECORD_FILE_NO,
					DESFireEV1.CommunicationType.MACed, 0, 0, 0, 0);
			result = true;
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG,
				"testchangeCyclicRecordFileSettingsP1 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF28, FRSID");
		report.log(LogLevel.INFO, TAG,
				"testchangeCyclicRecordFileSettingsP1, end");
	}

	// public void testcreateISOValueFileP1() {
	//
	//
	// iNxpiTag.log(LogLevel.INFO, TAG, "CreateISOFileOperations, GroupName");
	// iNxpiTag.log(LogLevel.INFO, TAG, "testcreateISOValueFileP1, start");
	// prepareSoftwareKeyStore();
	// boolean result;
	//
	// try {
	// mDesFireEV1.createFile(ISOVALUE_FILE_NO, ISO_VALUE_FILE_NO,
	// new ValueFileSettings(DESFire.CommunicationType.MACed, 0,
	// 0, 0, 0, 1, 100, 10, true));
	// result = true;
	// } catch (DESFireException e) {
	// result = false;
	// e.printStackTrace();
	// iNxpiTag.log(LogLevel.INFO, TAG, "Exception: ", e);
	// } catch (IOException e) {
	// result = false;
	// e.printStackTrace();
	// iNxpiTag.log(LogLevel.INFO, TAG, "Exception: ", e);
	// }
	// iNxpiTag.log(LogLevel.INFO, TAG, "testcreateISOValueFileP1 result is " +
	// result);
	// iNxpiTag.log(LogLevel.INFO, TAG, "testcreateISOValueFileP1, end");
	//
	// }

	/**
	 * <li>Pre :</li> NA <li>Desc:</li> Return Card Name, delivery type,
	 * ManFactData,total memory and vendor id. <li>Post:</li> NA
	 */
	
	public void testGetCardDetails() {

		report.disableLogging(LogLevel.INFO);
		// testauthSectorWithKeyAP1();
		report.enableLogging(LogLevel.INFO);

		report.log(LogLevel.INFO, TAG, "getCardDetailsOperations, GroupName");
		report.log(LogLevel.INFO, TAG, "testGetCardDetails, start");
		boolean result;
		DESFireEV1.CardDetails details;
		String cardName;
		String delivType;
		String manFact;
		int freeMem, majorVer, minorVer, totMem, VenId;
		try {
			details = mDesFireEV1.getCardDetails();

			report.log(LogLevel.INFO, TAG, "sak: " + details.sak);
			report.log(LogLevel.INFO, TAG, "atqa: "
					+ CustomModules.getUtility().dumpBytes(details.atqa));
			report.log(
					LogLevel.INFO,
					TAG,
					"historical bytes: "
							+ CustomModules.getUtility().dumpBytes(
									details.historicalBytes));
			report.log(LogLevel.INFO, TAG, "max transceive length: "
					+ details.maxTranscieveLength);
			report.log(LogLevel.INFO, TAG, "uid: "
					+ CustomModules.getUtility().dumpBytes(details.uid));

			cardName = details.cardName;
			report.log(LogLevel.INFO, TAG, "CardName: " + cardName);

			delivType = details.deliveryType;
			report.log(LogLevel.INFO, TAG, "DeliveryType: " + delivType);

			totMem = details.totalMemory;
			report.log(LogLevel.INFO, TAG, "Total Memory: " + totMem);

			VenId = details.vendorID;
			report.log(LogLevel.INFO, TAG, "Vendor ID: " + VenId);

			freeMem = details.freeMemory;
			report.log(LogLevel.INFO, TAG, "Free Memory: " + freeMem);

			majorVer = details.majorVersion;
			report.log(LogLevel.INFO, TAG, "Major Version: " + majorVer);

			minorVer = details.minorVersion;
			report.log(LogLevel.INFO, TAG, "Minor Version: " + minorVer);

			result = true;
		} catch (Exception e) {
			result = false;
			e.printStackTrace();
		}
		report.log(LogLevel.INFO, TAG, "testGetCardDetails result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_CL05, FRSID");
		report.log(LogLevel.INFO, TAG, "testGetCardDetails, end");
	}

	
	public void testgetLibVersionP1() {
		report.log(LogLevel.INFO, TAG, "getCardDetailsOperations, GroupName");
		report.log(LogLevel.INFO, TAG, "testgetLibVersionP1, start");
		boolean result = false;
		String libVersion;
		libVersion = ""; // mDesFireEV1.getLibraryVersion();

		if (libVersion != null) {
			result = true;
		}
		report.log(LogLevel.INFO, TAG, "testgetLibVersionP1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF73, FRSID");
		report.log(LogLevel.INFO, TAG, "testgetLibVersionP1, end");
	}

	
	public void testisCardDESFireEV1() {
		report.log(LogLevel.INFO, TAG, "getCardDetailsOperations, GroupName");
		report.log(LogLevel.INFO, TAG, "testisCardDESFireEV1, start");
		boolean result = false;
		result = mDesFireEV1.isCardDESFireEV1();

		report.log(LogLevel.INFO, TAG, "testisCardDESFireEV1 result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF73, FRSID");
		report.log(LogLevel.INFO, TAG, "testisCardDESFireEV1, end");
	}

	
	public void testWriteISOData() {

		report.log(LogLevel.INFO, TAG, "DataManipulationsCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testWriteISODataP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			mDesFireEV1.writeData(ISOSTD_DATA_FILE_NO, 0, data);
			mDesFireEV1.commitTransaction();
			result = true;
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testWriteISODataP1 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF37, FRSID");
		report.log(LogLevel.INFO, TAG, "testWriteISODataP1, end");

	}

	
	public void testReadISOData() {

		report.log(LogLevel.INFO, TAG, "DataManipulationsCommands, GroupName");
		report.log(LogLevel.INFO, TAG, "testReadISODataP1, start");
		prepareSoftwareKeyStore();
		boolean result;

		try {
			byte[] StdFile_value = mDesFireEV1.readData(ISOSTD_DATA_FILE_NO, 0,
					4);
			report.log(LogLevel.INFO, TAG, "Std Data File Data: "
					+ modules.getUtility().dumpBytes(StdFile_value));
			result = true;
		} catch (DESFireException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testReadISODataP1 result is " + result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF36, FRSID");
		report.log(LogLevel.INFO, TAG, "testReadISODataP1, end");

	}

	
	public void testauthenticateISOPICC() {

		report.log(LogLevel.INFO, TAG, "authenticateOperations, GroupName");
		report.log(LogLevel.INFO, TAG, "testauthenticateISOPICC, start");
		prepareSoftwareKeyStore();
		boolean result = false;
		try {
			mDesFireEV1.authenticate(DESFireEV1.AuthType.AES,
					D_PICC_AES_KEY_NO, (byte) 0, 0, (byte) 0, null);
			result = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		} catch (SmartCardException e) {
			// TODO Auto-generated catch block
			result = false;
			e.printStackTrace();
			report.log(LogLevel.INFO, TAG, "Exception: ", e);
		}
		report.log(LogLevel.INFO, TAG, "testauthenticateISOPICC result is "
				+ result);
		report.log(LogLevel.INFO, TAG, "NXPALIB_SW_DF01, FRSID");
		report.log(LogLevel.INFO, TAG, "testauthenticateISOPICC, end");

	}

	
	void testActivateAES() {
		try {

			mDesFireEV1.selectApplication(0);
			mDesFireEV1.authenticate(DESFireEV1.AuthType.Native,
					D_PICC_DES_KEY_NO, (byte) 0, 0, (byte) 0, null);
			// mDesFireEV1.format();
			int oldKeyNo = D_PICC_DES_KEY_NO;
			int newKeyNumber = D_PICC_AES_KEY_NO;
			mDesFireEV1.changeKey(0, oldKeyNo, (byte) 0, newKeyNumber,
					(byte) 0, DESFireEV1.KeyType.AES, (byte) 0, (byte) 0, null);
			mDesFireEV1.authenticate(DESFireEV1.AuthType.AES,
					D_PICC_AES_KEY_NO, (byte) 0, 0, (byte) 0, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SmartCardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void testMACError() {
		try {
			mDesFireEV1.selectApplication(1);

			mDesFireEV1.authenticate(DESFireEV1.AuthType.AES,
					D_PICC_AES_KEY_NO, (byte) 0, 0, (byte) 0, null);
			// mDesFireEV1.format();
			mDesFireEV1.createFile(STD_DATA_FILE_NO, new StdDataFileSettings(
					DESFireEV1.CommunicationType.MACed, 0, 0, 0, 0,
					DATA_FILE_SIZE));
			// mDesFireEV1.writeData(STD_DATA_FILE_NO, 0, bigData);
			// byte[] StdFile_value = mDesFireEV1.readData(STD_DATA_FILE_NO, 0,
			// 4);
			// System.out.println(CustomModules.getUtility().dumpBytes(StdFile_value));
			// mDesFireEV1.authenticate(DESFireEV1.AuthType.Native,
			// D_PICC_DES_KEY_NO, (byte) 0, 0, (byte) 0, null);
			// //mDesFireEV1.format();
			// int oldKeyNo = D_PICC_DES_KEY_NO;
			// int newKeyNumber = D_PICC_AES_KEY_NO;
			// mDesFireEV1.changeKey(0, oldKeyNo, (byte) 0, newKeyNumber,
			// (byte) 0, DESFireEV1.KeyType.AES, (byte) 0, (byte) 0, null);

			System.out.println("************************************");
			// mDesFireEV1.authenticate(DESFireEV1.AuthType.AES,
			// D_PICC_AES_KEY_NO, (byte) 0, 0, (byte) 0, null);
			//
			// mDesFireEV1.createApplication(
			// 1,
			// (byte) (0x10 | DESFireEV1.KSONE_CONFIG_CHANGABLE
			// | DESFireEV1.KSONE_FILE_DEL_NO_MKEY
			// | DESFireEV1.KSONE_GET_NO_MKEY |
			// DESFireEV1.KSONE_APP_MKEY_CHANGABLE),
			// 10, DESFireEV1.KeyType.AES);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SmartCardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
