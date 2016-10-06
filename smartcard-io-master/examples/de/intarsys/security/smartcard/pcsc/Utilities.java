/*
 * =============================================================================
 *
 *                       Copyright (c), NXP Semiconductors
 *
 *                        (C)NXP Electronics N.V.2013
 *         All rights are reserved. Reproduction in whole or in part is
 *        prohibited without the written consent of the copyright owner.
 *    NXP reserves the right to make changes without notice at any time.
 *   NXP makes no warranty, expressed, implied or statutory, including but
 *   not limited to any implied warranty of merchantability or fitness for any
 *  particular purpose, or that the use will not infringe any third party patent,
 *   copyright or trademark. NXP must not be liable for any loss or damage
 *                            arising from its use.
 *
 * =============================================================================
 */

package de.intarsys.security.smartcard.pcsc;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.ECFieldFp;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.EllipticCurve;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.rmi.runtime.Log;

/**
 * Utils classes used to provide suppport for different types of calculation,
 * byte format conversion, base 64 encoder & decoder, string and number
 * conversion methods, and other utilities.
 * 
 * @since 1.0
 * @author nxp69452
 */
public final class Utilities {

	/** Utilities Tag. */
	private static final String TAG = "UTILITIES";

	/**
	 * private constructor.
	 */
	private Utilities() {
	}

	/**
	 * String Decryption Key.
	 */
	static final byte[] SDECRYPT_KEY = new byte[] { 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00 };

	/**
	 * @return String Decryption IV.
	 */
	static byte[] getMyIv() {
		return reverseBits(rotateTwoByteLeft(SDECRYPT_KEY));
	}

	/**
	 * Takes Input as Encrypted String and returns Decrypted one.
	 * 
	 * @param sEncrypted
	 *            input encrypted string.
	 * @return Decrypted String.
	 */
	public static String toString(final String sEncrypted) {
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("AES/CBC/NoPadding");
			final IvParameterSpec iv = new IvParameterSpec(getMyIv());
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(SDECRYPT_KEY,
					"AES"), iv);
			String dec = new String(cipher.doFinal(stringToBytes(sEncrypted)));
			String[] arr = dec.split("#");
			Integer.parseInt(arr[0]);
			return arr[1].substring(0, Integer.parseInt(arr[0]));

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sEncrypted;

	}

	/**
	 * /* converting short array to byte array.
	 * 
	 * @param a
	 *            byte array
	 * @return byte[] if data is properly bitwise ORed
	 */
	public static byte[] bytes(final short[] a) {
		if (a == null) {
			return null;
		}
		final byte[] b = new byte[a.length];
		for (int i = 0; i < a.length; i++) {
			b[i] = (byte) (a[i] & 0xFF);
		}
		return b;
	}

	/**
	 * converting byte array to short array.
	 * 
	 * @param data
	 *            short array
	 * @return short[] if data is properly bitwise ORed
	 */
	public static short[] shorts(final byte[] data) {
		if (data == null) {
			return null;
		}
		final short[] b = new short[data.length];
		for (int i = 0; i < data.length; i++) {
			b[i] = (short) (data[i] & 0xFF);
		}
		return b;
	}

	/**
	 * convert byte to integer.
	 * 
	 * @param high
	 *            upper 4-bit
	 * @param low
	 *            lower 4-bit
	 * @return integer calculated converted value
	 */
	public static int toInt(final byte high, final byte low) {
		return (high & 0xFF) * 0x100 + (low & 0xFF);
	}

	/**
	 * convert byte to integer.
	 * 
	 * @param high
	 *            upper 4-bit
	 * @param mid
	 *            mid value
	 * @param low
	 *            lower value
	 * @return integer calculated & converted value
	 */
	public static int toInt(final byte high, final byte mid, final byte low) {
		return (high & 0xFF) * 0x10000 + (mid & 0xFF) * 0x100 + (low & 0xFF);
	}

	/**
	 * convert byte to integer.
	 * 
	 * @param high
	 *            upper 4-bit
	 * @param mid1
	 *            mid upper
	 * @param mid2
	 *            mid lower
	 * @param low
	 *            lower value
	 * @return integer calculated & converted value
	 */
	public static int toInt(final byte high, final byte mid1, final byte mid2,
			final byte low) {
		return (high & 0xFF) * 0x1000000 + (mid1 & 0xFF) * 0x10000
				+ (mid2 & 0xFF) * 0x100 + (low & 0xFF);
	}

	/**
	 * check print.
	 * 
	 * @param ch
	 *            byte to be printed
	 * @return true if byte satisfy condition
	 */
	public static boolean isPrint(final byte ch) {
		return (ch & 0xFF) >= 0x020 // ' '
				&& (ch & 0xFF) <= 0x7E; // '~'
	}

	/**
	 * check print option.
	 * 
	 * @param data
	 *            byte to input
	 * @param start
	 *            start bit
	 * @param len
	 *            lenght to be printed
	 * @return true if satisfy the value
	 */
	public static boolean isPrint(final byte[] data, final int start,
			final int len) {
		boolean rv = true;
		final int l = data.length;
		int i = 0;
		for (i = start; i < l && i < start + len && rv; i++) {
			rv = rv && isPrint(data[i]);
		}
		return rv && i == start + len;
	}

	/**
	 * function to check print option.
	 * 
	 * @param data
	 *            byte array to input
	 * @param start
	 *            start bit
	 * @return true if satisfy the value.
	 */
	public static boolean isPrint(final byte[] data, final int start) {
		return isPrint(data, start, data.length);
	}

	/**
	 * function to check print.
	 * 
	 * @param data
	 *            byte value to input
	 * @return true if satisfy the value
	 */
	public static boolean isPrint(final byte[] data) {
		return isPrint(data, 0, data.length);
	}

	/**
	 * Helper function that dumps one byte in hex form.
	 * 
	 * @param buffer
	 *            The byte to dump
	 * @param prefix
	 *            The String printed before the hex representation
	 * @return A string representation of the array of bytes
	 */
	public static String dumpHex(final byte buffer, final String prefix) {
		final StringBuffer sb = new StringBuffer();
		sb.append(prefix);
		sb.append(String.format("%02X", buffer));
		return sb.toString();
	}

	/**
	 * Helper function that dumps an array of bytes in hex form.
	 * 
	 * @param buffer
	 *            The bytes array to dump
	 * @param prefix
	 *            The String printed before the hex representation
	 * @param separator
	 *            The String printed between the individual bytes in hex
	 * @return A string representation of the array of bytes
	 */
	public static String dumpHex(final byte[] buffer, final String prefix,
			final String separator) {
		if (buffer == null || buffer.length == 0) {
			return (prefix + "[none]");
		}

		final StringBuffer sb = new StringBuffer();
		sb.append(prefix);
		for (int i = 0; i < buffer.length - 1; i++) {
			sb.append(String.format("%02X%s", buffer[i], separator));
		}
		sb.append(String.format("%02X", buffer[buffer.length - 1]));

		return sb.toString();
	}

	/**
	 * Helper function dump hex length over logcat view.
	 * 
	 * @param buffer
	 *            The bytes array to dump
	 * @param prefix
	 *            The String printed before the hex representation
	 * @param separator
	 *            The String printed between the individual bytes in hex
	 * @return A string representation of the array of bytes
	 */
	public static String dumpHexLen(final byte[] buffer, final String prefix,
			final String separator) {
		return dumpHex(buffer, prefix, separator)
				+ String.format(" Length: %d", buffer.length);
	}

	/**
	 * Helper function dump an array of bytes in hex form.
	 * 
	 * @param data
	 *            The bytes array to dump
	 * @return A string representation of the array of bytes
	 */
	public static String hexDump(final byte[] data) {
		final StringBuffer rv = new StringBuffer("");
		for (int j = 0; j < data.length; j += 8) {
			if (j > 0) {
				rv.append("\n");
			}
			rv.append(String.format("[%02X] ", j));
			rv.append(Utilities.dumpHexAscii(data, j, 8));
		}
		return rv.toString();
	}

	/**
	 * Helper function that dumps an array of bytes in reversed hex form.
	 * 
	 * @param buffer
	 *            The bytes array to dump
	 * @return A string representation of the array of bytes
	 */
	public static byte[] reverseBits(final byte[] buffer) {
		final byte[] revBuffer = new byte[buffer.length];
		for (int i = 0; i < buffer.length; i++) {
			int v = buffer[i] & 0xFF; // input bits to be reversed
			int r = v; // r will be reversed bits of v; first get LSB of v
			int s = 7; // extra shift needed at end

			for (v >>>= 1; v != 0; v >>>= 1) {
				r <<= 1;
				r |= v & 1;
				s--;
			}
			r <<= s; // shift when v's highest bits are zero
			revBuffer[i] = (byte) (r & 0xFF);
		}
		return revBuffer;
	}

	/**
	 * Helper function that dumps an array of bytes in reversed hex form with
	 * custom order.
	 * 
	 * @param buf
	 *            The bytes array to dump
	 * @param prefix
	 *            The String printed before the hex representation
	 * @param separator
	 *            The String printed for separation format
	 * @param start
	 *            The index where to start the dump
	 * @param len
	 *            length to be dumped
	 * @return A string representation of the array of bytes along with filler
	 *         seperator
	 */
	public static String dumpHex(final byte[] buf, final String prefix,
			final String separator, final int start, final int len) {
		if (buf == null) {
			return null;
		}
		if (buf.length >= start + len) {
			final byte[] buffer = new byte[len];
			System.arraycopy(buf, start, buffer, 0, len);
			return dumpHex(buffer, prefix, separator);
		}
		final byte[] buffer = new byte[buf.length - start];
		System.arraycopy(buf, start, buffer, 0, buf.length - start);
		final StringBuffer filler = new StringBuffer("");
		for (int i = 0; i < len - buf.length + start; i++) {
			filler.append(separator + "  ");
		}
		return dumpHex(buffer, prefix, separator) + filler.toString();
	}

	/**
	 * Helper function that dumps an array of bytes in reversed hex form with
	 * custom options.
	 * 
	 * @param buf
	 *            The bytes array to dump
	 * @param prefix
	 *            The String printed before the hex representation
	 * @param separator
	 *            The String printed for separation format
	 * @param newline
	 *            The String printed option for new line
	 * @param start
	 *            The index where to start the dump
	 * @param blocklen
	 *            length of the block
	 * @param len
	 *            length to be dumped
	 * @return A string representation of the array of bytes along with filler
	 *         separator
	 */
	private static String dumpHex(final byte[] buf, final String prefix,
			final String separator, final String newline, final int start,
			final int blocklen, final int len) {
		final StringBuffer sb = new StringBuffer();
		sb.append(prefix);
		int offset = start;
		for (offset = start; offset + blocklen < len; offset += blocklen) {
			sb.append(dumpHex(buf, offset == start ? "" : newline, separator,
					offset, blocklen));
		}
		if (offset < len) {
			if (offset == start) {
				sb.append(prefix);
			} else {
				sb.append(separator);
			}
			sb.append(dumpBytes(buf, offset, len - offset));
		}
		return sb.toString();
	}

	/**
	 * Helper function that dumps an array of bytes in ASCII representation.
	 * 
	 * @param buffer
	 *            The bytes array to dump
	 * @param prefix
	 *            The String printed before the ASCII representation
	 * @param postfix
	 *            The String printed after the ASCII representation
	 * @return A string representation of the array of bytes
	 */
	private static String dumpAscii(final byte[] buffer, final String prefix,
			final String postfix) {
		if (buffer == null || buffer.length == 0) {
			return "";
		}

		final StringBuffer sb = new StringBuffer();
		sb.append(prefix);
		for (final byte ch : buffer) {
			if (isPrint(ch)) {
				sb.append((char) ch);
			} else {
				sb.append(".");
			}
			sb.append(postfix);
		}

		return sb.toString();
	}

	/**
	 * Helper function that dumps an array of bytes as a large hex value.
	 * 
	 * @param buffer
	 *            The bytes array to dump
	 * @return A string representation of the array of bytes
	 */
	public static String dumpBytes(final byte[] buffer) {
		return dumpHex(buffer, "0x", "");
	}

	/**
	 * Helper function that dumps an array of bytes as a string value.
	 * 
	 * @param buffer
	 *            The bytes array to dump
	 * @return A string representation of the array of bytes
	 */
	public static String bytesToString(final byte[] buffer) {
		return dumpHex(buffer, "0x", "");
	}

	/**
	 * Helper function that dumps an array of bytes as a large hex value.
	 * 
	 * @param buffer
	 *            array of bytes
	 * @return A string representation of the array of bytes
	 */
	public static String dumpBytesLen(final byte[] buffer) {
		return dumpHexLen(buffer, "0x", "");
	}

	/**
	 * Helper function that dumps a sub-array of an array of bytes as a large
	 * hex value.
	 * 
	 * @param buf
	 *            The bytes array to dump
	 * @param start
	 *            The index where to start the dump
	 * @param len
	 *            The number of bytes to dump
	 * @return A string representation of the array of bytes
	 */
	public static String dumpBytes(final byte[] buf, final int start,
			final int len) {
		return dumpHex(buf, "0x", "", start, len);
	}

	/**
	 * Helper function that dumps a sub-array of an array of bytes as a large
	 * hex value.
	 * 
	 * @param buf
	 *            The bytes array to dump
	 * @param separator
	 *            The String printed for separation format
	 * @param start
	 *            The index where to start the dump
	 * @param blocklen
	 *            length of the block
	 * @param len
	 *            Total length to dump
	 * @return A string representation of the array of bytes
	 */
	public static String dumpBytes(final byte[] buf, final String separator,
			final int start, final int blocklen, final int len) {
		return dumpHex(buf, "0x", "", separator, start, blocklen, len);
	}

	/**
	 * Helper function that dumps an array of bytes as a hex value.
	 * 
	 * @param buffer
	 *            The bytes array to dump
	 * @return A string representation of the array of bytes in ASCII
	 *         representation.
	 */
	public static String dumpBytesAscii(final byte[] buffer) {
		return dumpBytes(buffer) + dumpAscii(buffer, " |", "|");
	}

	/**
	 * Helper function that convert an array of bytes as a buffer to ASCII
	 * Strings.
	 * 
	 * @param buf
	 *            The bytes array to dump
	 * @param start
	 *            start index
	 * @param len
	 *            total length
	 * @return A string as ASCII representation.
	 */
	public static String dumpBytesAscii(final byte[] buf, final int start,
			final int len) {
		if (buf != null) {
			if (buf.length >= start + len) {
				final byte[] buffer = new byte[len];
				System.arraycopy(buf, start, buffer, 0, len);
				return dumpBytes(buffer) + dumpAscii(buffer, " |", "|");
			}
			final byte[] buffer = new byte[buf.length - start];
			System.arraycopy(buf, start, buffer, 0, buf.length - start);
			final StringBuffer filler1 = new StringBuffer("");
			final StringBuffer filler2 = new StringBuffer("");
			for (int i = 0; i < len - buf.length + start; i++) {
				filler1.append("   ");
				filler2.append(" ");
			}
			return dumpBytes(buffer) + filler1.toString()
					+ dumpAscii(buffer, " |", filler2 + "|");
		}
		return null;
	}

	/**
	 * Helper function that convert an array of hex values as a buffer to ASCII
	 * Strings.
	 * 
	 * @param buffer
	 *            array of hex values
	 * @return A string as ASCII representation.
	 */
	public static String dumpHexAscii(final byte[] buffer) {
		return dumpHex(buffer, "", " ") + dumpAscii(buffer, " |", "|");
	}

	/**
	 * Helper function that convert an array of hex values as a buffer to ASCII
	 * Strings.
	 * 
	 * @param buffer
	 *            The bytes array to dump
	 * @param blocklen
	 *            length of the block
	 * @return A string as ASCII representation.
	 */
	public static String dumpHexAscii(final byte[] buffer, final int blocklen) {
		final StringBuffer rv = new StringBuffer("");
		for (int j = 0; j < buffer.length; j += blocklen) {
			if (j != 0) {
				rv.append("\n");
			}
			rv.append(String.format("[%02X] ", j));
			rv.append(Utilities.dumpHexAscii(buffer, j, blocklen));
		}
		return rv.toString();
	}

	/**
	 * Helper function that convert an array of hex values as a buffer to ASCII
	 * Strings.
	 * 
	 * @param buf
	 *            The bytes array to dump
	 * @param start
	 *            start index
	 * @param len
	 *            data length
	 * @return A string as ASCII representation.
	 */
	public static String dumpHexAscii(final byte[] buf, final int start,
			final int len) {
		if (buf != null) {
			if (buf.length >= start + len) {
				final byte[] buffer = new byte[len];
				System.arraycopy(buf, start, buffer, 0, len);
				return dumpHex(buffer, "", " ") + dumpAscii(buffer, " |", "|");
			}
			final byte[] buffer = new byte[buf.length - start];
			System.arraycopy(buf, start, buffer, 0, buf.length - start);
			final StringBuffer filler1 = new StringBuffer("");
			final StringBuffer filler2 = new StringBuffer("");
			for (int i = 0; i < len - buf.length + start; i++) {
				filler1.append("   ");
				filler2.append(" ");
			}
			return dumpHex(buffer, "", " ") + filler1.toString()
					+ dumpAscii(buffer, " |", filler2 + "|");
		}
		return null;
	}

	/**
	 * @param src
	 *            source data
	 * @param des
	 *            data to be appended
	 * @return concatenated value to the end of this String.
	 */
	public static byte[] concat(final byte[] src, final byte[] des) {
		final byte[] c = Arrays.copyOf(src, src.length + des.length);
		System.arraycopy(des, 0, c, src.length, des.length);
		return c;
	}

	/**
	 * @param src
	 *            source data
	 * @param des
	 *            data to be appended
	 * @param l
	 *            length of the des data
	 * @return concatenated value to the end of this String.
	 */
	public static byte[] concat(final byte[] src, final byte[] des, final int l) {
		return concat(src, des, 0, l);
	}

	/**
	 * @param src
	 *            source data
	 * @param des
	 *            data to be appended
	 * @param start
	 *            start index
	 * @param len
	 *            length of data
	 * @return concatenated value to the end of this String.
	 */
	public static byte[] concat(final byte[] src, final byte[] des,
			final int start, final int len) {
		int l = len;
		if (des.length < start) {
			return src;
		}
		if (des.length < start + len) {
			l = des.length - start;
		}
		if (l < 0) {
			l = 0;
		}
		final byte[] c = Arrays.copyOf(src, src.length + l);
		System.arraycopy(des, start, c, src.length, l);
		return c;
	}

	/**
	 * concatenates the string representation of any other type of data to the
	 * end of the invoking object.
	 * 
	 * @param str
	 *            source data
	 * @param txt
	 *            text object data
	 * @param size
	 *            size of the text
	 */
//	public static void appendMono(final SpannableStringBuilder str,
//			final SpannableStringBuilder txt, final int size) {
//		str.append(txt);
//		final int strLen = str.length();
//		final int l = strLen - txt.length();
//		str.setSpan(CharacterStyle.wrap(new TypefaceSpan("monospace")), l,
//				strLen, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//		str.setSpan(new AbsoluteSizeSpan(size, true), l, strLen,
//				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//	}

	/**
	 * concatenates the string representation of any other type of data to the
	 * end of the invoking object.
	 * 
	 * @param str
	 *            source data
	 * @param txt
	 *            text data
	 * @param size
	 *            size of the text
	 */
//	public static void appendMono(final SpannableStringBuilder str,
//			final String txt, final int size) {
//		str.append(txt);
//		final int strLen = str.length();
//		final int l = strLen - txt.length();
//		str.setSpan(CharacterStyle.wrap(new TypefaceSpan("monospace")), l,
//				strLen, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//		str.setSpan(new AbsoluteSizeSpan(size, true), l, strLen,
//				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//	}

	/**
	 * concatenates the string representation of any other type of data to the
	 * end of the invoking object.
	 * 
	 * @param str
	 *            source data
	 * @param txt
	 *            text data
	 */
//	public static void appendMono(final SpannableStringBuilder str,
//			final String txt) {
//		appendMono(str, txt, 11);
//	}

	/**
	 * concatenates the string representation of any other type of data to the
	 * end of the invoking object.
	 * 
	 * @param str
	 *            source data
	 * @param txt
	 *            text object data
	 */
//	public static void appendMono(final SpannableStringBuilder str,
//			final SpannableStringBuilder txt) {
//		appendMono(str, txt, 11);
//	}

	/**
	 * @param data
	 *            source data
	 * @param arg2
	 *            byte to be appended
	 * @return A string representation of appended data
	 */
	public static byte[] append(final byte[] data, final byte arg2) {
		if (data == null) {
			return new byte[] { arg2 };
		}
		if (data.length == 0) {
			return new byte[] { arg2 };
		}
		final byte[] ret = new byte[data.length + 1];
		System.arraycopy(data, 0, ret, 0, data.length);
		ret[data.length] = arg2;
		return ret;
	}

	/**
	 * @param data
	 *            source data
	 * @param arg
	 *            byte array to be appended
	 * @return A string representation of appended data
	 */
	public static byte[] append(final byte data, final byte[] arg) {
		if (arg == null) {
			return new byte[] { data };
		}
		if (arg.length == 0) {
			return new byte[] { data };
		}
		final byte[] ret = new byte[arg.length + 1];
		System.arraycopy(arg, 0, ret, 1, arg.length);
		ret[0] = data;
		return ret;
	}

	/**
	 * @param arg1
	 *            source data
	 * @param arg2
	 *            data to be appended
	 * @return A byte array of appended data
	 */
	public static byte[] append(final byte arg1, final byte arg2) {
		return new byte[] { arg1, arg2 };
	}

	/**
	 * @param data
	 *            source data
	 * @param append
	 *            data to be appended
	 * @return A byte array of appended data
	 */
	public static byte[] append(final byte[] data, final byte[] append) {
		if (data == null) {
			if (append == null) {
				return null;
			} else {
				return Arrays.copyOfRange(append, 0, append.length);
			}
		} else if (append == null) {
			return Arrays.copyOfRange(data, 0, data.length);
		}

		final byte[] ret = new byte[data.length + append.length];
		System.arraycopy(data, 0, ret, 0, data.length);
		System.arraycopy(append, 0, ret, data.length, append.length);
		return ret;
	}

	/**
	 * @param data
	 *            source object data
	 * @return A byte array of appended data.
	 */
	public static byte[] append(final byte[]... data) {
		byte[] ret = new byte[0];
		for (final byte[] d : data) {
			ret = append(ret, d);
		}
		return ret;
	}

	/**
	 * @param cmd
	 *            source data
	 * @param data
	 *            source object data
	 * @return A byte array of appended data
	 */
	public static byte[] append(final byte cmd, final byte[]... data) {
		byte[] ret = new byte[] { cmd };
		for (final byte[] d : data) {
			ret = append(ret, d);
		}
		return ret;
	}

	/**
	 * @param cmd
	 *            source data
	 * @param opt
	 *            intermittent source byte
	 * @param data
	 *            byte object to be appended
	 * @return A byte array of appended data.
	 */
	public static byte[] append(final byte cmd, final byte opt,
			final byte[]... data) {
		byte[] ret = new byte[] { cmd, opt };
		for (final byte[] d : data) {
			ret = append(ret, d);
		}
		return ret;
	}

	/**
	 * @param min
	 *            minimum value
	 * @param max
	 *            maximum value
	 * @param value
	 *            value to be checked
	 * @return boolean value
	 */
	public static boolean checkRange(final int min, final int max,
			final int value) {
		return ((value >= min) && (value <= max));
	}

	/**
	 * @param address
	 *            address of the block
	 * @param lockinfo
	 *            string value
	 * @param data
	 *            data byte
	 * @param comment
	 *            comment to be added
	 * @return A string representation of data
	 */
	public static String printDataBlock(final int address,
			final String lockinfo, final int[] data, final String comment) {
		String s = String.format("[%04X] %s", address, lockinfo);
		s = s.concat(String.format(" %02X %02X %02X %02X ", data[0], data[1],
				data[2], data[3]));
		s = s.concat(comment);
		return s;
	}

	/**
	 * @param address
	 *            address of the block
	 * @param lockinfo
	 *            string value
	 * @param secured
	 *            true if secured
	 * @param comment
	 *            comment to be added
	 * @return A string representation of data
	 */
	public static String printDataBlock(final int address,
			final String lockinfo, final boolean secured, final String comment) {
		String s = String.format("[%04X] %s XX XX XX XX ", address, lockinfo);
		s = s.concat(comment);
		return s;
	}

	/**
	 * @param original
	 *            input data
	 * @param mode
	 *            encrypt/decrypt mode
	 * @param keySpec
	 *            secret key
	 * @param ivSpec
	 *            init vector
	 * @return byte array encrypted/decrypted data representation
	 * @throws GeneralSecurityException
	 *             general security exception and the superclass for all
	 *             security specific exceptions
	 */
	public static byte[] tripleDes(final byte[] original, final int mode,
			final SecretKeySpec keySpec, final byte[] ivSpec)
			throws GeneralSecurityException {
		final Cipher cipher = Cipher.getInstance("DESede/CBC/NoPadding");
		final IvParameterSpec iv = new IvParameterSpec(ivSpec);
		cipher.init(mode, keySpec, iv);
		return cipher.doFinal(original);
	}

	/**
	 * @param high
	 *            high input data.
	 * @param mid
	 *            mid input data.
	 * @param low
	 *            low input data.
	 * @return integer decoded value.
	 */
	public static int toIntBcd(final byte high, final byte mid, final byte low) {
		final String str = String.format("%02X%02X%02X", high, mid, low);
		return Integer.decode(str);
	}

	/**
	 * Reverse data bytes.
	 * 
	 * @param data
	 *            input data
	 * @return reversed byte array
	 */
	public static byte[] reverseBytes(final byte[] data) {
		final int len = data.length;
		final byte[] res = new byte[len];
		for (int i = 0; i <= len / 2; i++) {
			res[i] = data[len - 1 - i];
			res[len - 1 - i] = data[i];
		}
		return res;
	}

	/**
	 * Rotate data input by 1 byte left shift.
	 * 
	 * @param data
	 *            input data byte
	 * @return Array of byte with left shift
	 */
	public static byte[] rotateOneByteLeft(final byte[] data) {
		final byte[] ret = new byte[data.length];
		System.arraycopy(data, 1, ret, 0, data.length - 1);
		ret[ret.length - 1] = data[0];
		return ret;
	}

	/** CRC constant. */
	public static final int CRC16BYTECOUNT = 2;
	/** CRC constant. */
	public static final int CRC32BYTECOUNT = 4;

	/**
	 * @param message
	 *            input data byte
	 * @return integer data formed for CRC16.
	 */
	public static int crc16(final byte[] message) {
		int c = 0x6363; // initial value
		for (int i = 0; i < message.length; i++) {
			final int d = message[i] & 0xFF;
			final int e = (c ^ d) & 0xFF;
			final int f = (e ^ e << 4) & 0xFF;
			c = c >> 8 ^ f << 8 ^ f << 3 ^ f >> 4;
		}
		return c & 0xFFFF;
	}

	/**
	 * @param message
	 *            input data byte
	 * @return integer data formed for CRC32.
	 */
	public static int crc32(final byte[] message) {
		int crc = 0xFFFFFFFF; // initial value
		final int poly = 0xEDB88320; // reverse polynomial

		for (int b = 0; b < message.length; b++) {
			int temp = (crc ^ message[b]) & 0xff;

			// read 8 bits one at a time
			for (int i = 0; i < 8; i++) {
				if ((temp & 1) == 1) {
					temp = (temp >>> 1) ^ poly;
				} else {
					temp = (temp >>> 1);
				}
			}
			crc = (crc >>> 8) ^ temp;
		}
		return crc;
	}

	/**
	 * @param dataAndCRC
	 *            input data with CRC
	 * @return true if satisfy
	 */
	public static boolean checkCRC16(final byte[] dataAndCRC) {
		if (dataAndCRC == null | dataAndCRC.length < CRC16BYTECOUNT + 1) {
			return false;
		}
		final int crc = crc16(Arrays.copyOfRange(dataAndCRC, 0,
				dataAndCRC.length - CRC16BYTECOUNT));
		return ((byte) (crc & 0xff) == dataAndCRC[dataAndCRC.length
				- CRC16BYTECOUNT])
				& ((byte) (crc >> 8 & 0xff) == dataAndCRC[dataAndCRC.length
						- CRC16BYTECOUNT + 1]);
	}

	/**
	 * @param dataAndCRC
	 *            input data with CRC
	 * @return true if satisfy
	 */
	public static boolean checkCRC32(final byte[] dataAndCRC) {
		if (dataAndCRC == null | dataAndCRC.length < CRC32BYTECOUNT + 1) {
			return false;
		}
		final int crc = crc32(Arrays.copyOfRange(dataAndCRC, 0,
				dataAndCRC.length - CRC32BYTECOUNT));
		return ((byte) (crc & 0xff) == dataAndCRC[dataAndCRC.length
				- CRC32BYTECOUNT])
				& ((byte) (crc >> 8 & 0xff) == dataAndCRC[dataAndCRC.length
						- CRC32BYTECOUNT + 1])
				& ((byte) (crc >> 16 & 0xff) == dataAndCRC[dataAndCRC.length
						- CRC32BYTECOUNT + 2])
				& ((byte) (crc >> 24 & 0xff) == dataAndCRC[dataAndCRC.length
						- CRC32BYTECOUNT + 3]);
	}

	/**
	 * @param s
	 *            input string data
	 * @return byte array
	 */
	public static byte[] stringToBytes(final String s) {
		if (s.length() == 0 | s == null) {
			return new byte[] {};
		}
		final String temp = s.replaceAll("\\s", "");
		if (temp.length() % 2 != 0) {
			return new byte[] {};
		}
		final char[] upper = temp.toUpperCase(Locale.ENGLISH).toCharArray();
		final byte[] result = new byte[upper.length / 2];
		for (int i = 0; i < result.length; i++) {
			result[i] += getHexValue(upper[i * 2]) * 16
					+ getHexValue(upper[i * 2 + 1]);
		}
		return result;
	}

	/**
	 * @param c
	 *            char input data
	 * @return hex value
	 */
	public static byte getHexValue(final char c) {
		return (byte) ((c <= '9' && c >= '0') ? (c) - ('0')
				: (c <= 'F' && c >= 'A') ? (c) - ('A') + 10 : 0);
	}

	/**
	 * XOR links ar1 with arg2.
	 * 
	 * @param arg1
	 *            input data
	 * @param arg2
	 *            input data
	 * @return arg1 ^ arg2 XOR output data byte
	 */
	public static byte[] xor(final byte[] arg1, final byte[] arg2) {
		final byte[] ret = new byte[(arg1.length < arg2.length) ? arg2.length
				: arg1.length];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = (byte) (arg1[i] ^ arg2[i]);
		}
		return ret;
	}

	/**
	 * Shift data by one bit left.
	 * 
	 * @param data
	 *            data to shift
	 * @return shifted data
	 */
	public static byte[] shiftOneBitLeft(final byte[] data) {
		byte overflow = 0x00;
		final byte[] returnBytes = new byte[data.length];
		for (int i = data.length - 1; i >= 0; i--) {
			returnBytes[i] = (byte) ((data[i] << 1) | overflow);
			overflow = (byte) (((data[i] & 0x80) == 0x80) ? 0x01 : 0x00);
		}
		return returnBytes;
	}

	/**
	 * Append zero by length of data.
	 * 
	 * @param data
	 *            The bytes array to append
	 * @param addLen
	 *            The Length of the zero padding
	 * @return the representation of the array of bytes
	 */
	public static byte[] addZeroPadding(final byte[] data, final int addLen) {
		if (data == null) {
			return data;
		}
		if (data.length == 0) {
			return data;
		}
		final byte[] padded = new byte[data.length + addLen];
		System.arraycopy(data, 0, padded, 0, data.length);
		return padded;
	}

	/**
	 * Get odd bytes from the given data.
	 * 
	 * @param data
	 *            byte array
	 * @return the representation of the array of bytes
	 */
	public static byte[] getOddBytes(final byte[] data) {

		int bLength = data.length;
		int size = bLength / 2;
		int i = 0, count = 0;
		byte[] oddBytes = new byte[size];
		for (i = 1; i < bLength; i = i + 2) {
			oddBytes[count] = data[i];
			count++;
		}
		return oddBytes;
	}

	/**
	 * Truncate byte.
	 * 
	 * @param data
	 *            byte
	 * @param start
	 *            The position of the data byte
	 * @param len
	 *            of the byte
	 * @return the representation of the array of bytes
	 */
	public static byte[] truncate(final byte[] data, final int start,
			final int len) {
		final byte[] truncated = Arrays.copyOfRange(data, start, start + len);
		return truncated;
	}

	/**
	 * Generate the random number.
	 * 
	 * @param size
	 *            input value
	 * @return the representation of the array of bytes
	 */
	public static byte[] generateRandom(final int size) {

		final byte[] rndA1 = new byte[size];
		try {
			// SHA1PRNG
			final SecureRandom secureRandom = SecureRandom
					.getInstance("SHA1PRNG");
			// Utilities
			// .toString("1621025080C1E80F59EEF3566BD831B7"));
			secureRandom.nextBytes(rndA1);
		} catch (final NoSuchAlgorithmException noSuchAlgo) {
			noSuchAlgo.getStackTrace();
		}
		return rndA1;
	}

	/**
	 * Rotate two byte from the left position.
	 * 
	 * @param data
	 *            byte
	 * @return the representation of the array of bytes
	 */
	public static byte[] rotateTwoByteLeft(final byte[] data) {
		final byte[] ret = new byte[data.length];
		System.arraycopy(data, 2, ret, 0, data.length - 2);
		ret[ret.length - 2] = data[0];
		ret[ret.length - 1] = data[1];
		return ret;
	}

	/**
	 * Check ECDA Signature for verifying key.
	 * 
	 * @param nxpPubKey
	 *            Nxp public key
	 * @param signature
	 *            32 bytes ECC Signature to verify.
	 * @param uid
	 *            Signed Data is the 7 bytes Unique Identifier (or Serial
	 *            Number).
	 * @return boolean return verified ot Not.
	 * @throws NoSuchAlgorithmException
	 *             if the requested algorithm could not be found.
	 */
	public static boolean checkEcdaSignature(final String nxpPubKey,
			final byte[] signature, final byte[] uid)
			throws NoSuchAlgorithmException {
		final ECPublicKeySpec ecPubKeySpec = getEcPubKey(nxpPubKey,
				getECSecp128r1());
		return checkEcdsaSignature(ecPubKeySpec, signature, uid);
	}

	/**
	 * Generating and verifing ECPublicKeySpec with nxp 32 bytes public key.
	 * 
	 * @param ecPubKey
	 *            EC public key encryption of nxpPubKey and ECPublicKeySpec.
	 * @param signature
	 *            32 bytes ECC Signature to verify.
	 * @param data
	 *            Signed Data is the 7 bytes Unique Identifier (or Serial
	 *            Number).
	 * @return boolean return verified ot Not.
	 * @throws NoSuchAlgorithmException
	 *             a requested algorithm could not be found
	 */
	public static boolean checkEcdsaSignature(final ECPublicKeySpec ecPubKey,
			final byte[] signature, final byte[] data)
			throws NoSuchAlgorithmException {
		KeyFactory keyFac = null;

		try {
			keyFac = KeyFactory.getInstance("EC");
		} catch (final NoSuchAlgorithmException e1) {
			keyFac = KeyFactory.getInstance("ECDSA");
		}

		if (keyFac != null) {
			try {
				final PublicKey publicKey = keyFac.generatePublic(ecPubKey);
				final Signature dsa = Signature.getInstance("NONEwithECDSA");
				dsa.initVerify(publicKey);
				dsa.update(data);
				return dsa.verify(derEncodeSignature(signature));
			} catch (final SignatureException e) {
				e.printStackTrace();
			} catch (final InvalidKeySpecException e) {
				e.printStackTrace();
			} catch (final InvalidKeyException e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	/**
	 * getEcPublickey generation and encrypted .
	 * 
	 * @param key
	 *            Nxp public key
	 * @param curve
	 *            EllipticCurve value.
	 * @return ECPublicKeySpec ECPublicKey with curve.
	 * 
	 */
	public static ECPublicKeySpec getEcPubKey(final String key,
			final ECParameterSpec curve) {
		if (key == null || key.length() != 2 * 33 || !key.startsWith("04")) {
			return null;
		}
		final String keyX = key.substring(2 * 1, 2 * 17);
		final String keyY = key.substring(2 * 17, 2 * 33);

		final BigInteger affineX = new BigInteger(keyX, 16);
		final BigInteger affineY = new BigInteger(keyY, 16);
		final ECPoint w = new ECPoint(affineX, affineY);
		return new ECPublicKeySpec(w, curve);
	}

	/**
	 * getECSecp128r1 .. SSL
	 * 
	 * @return new ECParameterSpec with the specified elliptic curve, the base
	 *         point, the order of the generator (or base point) and the
	 *         co-factor
	 */
	private static ECParameterSpec getECSecp128r1() {
		// TODO Auto-generated method stub
		final BigInteger p = new BigInteger("fffffffdffffffffffffffffffffffff",
				16);
		final ECFieldFp field = new ECFieldFp(p);

		final BigInteger a = new BigInteger("fffffffdfffffffffffffffffffffffc",
				16);
		final BigInteger b = new BigInteger("e87579c11079f43dd824993c2cee5ed3",
				16);
		final EllipticCurve curve = new EllipticCurve(field, a, b);

		final BigInteger genX = new BigInteger(
				"161ff7528b899b2d0c28607ca52c5b86", 16);
		final BigInteger genY = new BigInteger(
				"cf5ac8395bafeb13c02da292dded7a83", 16);
		final ECPoint generator = new ECPoint(genX, genY);

		final BigInteger order = new BigInteger(
				"fffffffe0000000075a30d1b9038a115", 16);
		final int coFactor = 1;
		return new ECParameterSpec(curve, generator, order, coFactor);
	}

	/**
	 * Decoding the 32 bytes Tag signature .
	 * 
	 * @param signature
	 *            32 bytes ECC Signature TO encode.
	 * @return byte array with encoded signature
	 */
	public static byte[] derEncodeSignature(final byte[] signature) {
		// split into r and s
		final byte[] r = Arrays.copyOfRange(signature, 0, 16);
		final byte[] s = Arrays.copyOfRange(signature, 16, 32);

		// When encoded in DER, this becomes the following sequence of bytes:
		// 0x30 b1 0x02 b2 (vr) 0x02 b3 (vs)
		//
		// where:
		// b1 is a single byte value, equal to the length, in bytes, of the
		// remaining list of bytes
		// (from the first 0x02 to the end of the encoding);
		// b2 is a single byte value, equal to the length, in bytes, of (vr);
		// b3 is a single byte value, equal to the length, in bytes, of (vs);
		// (vr) is the signed big-endian encoding of the value "r", of minimal
		// length;
		// (vs) is the signed big-endian encoding of the value "s", of minimal
		// length.
		//
		// Signed big-endian encoding of minimal length" means that the
		// numerical value must be encoded as a sequence of bytes,
		// such that the least significant byte comes last (that's what
		// "big endian" means), the total length is the shortest
		// possible to represent the value (that's "minimal length"), and the
		// first bit of the first byte specifies the sign of
		// the value (that's "signed"). For ECDSA, the r and s values are
		// positive integers, so the first bit of the first byte
		// must be a 0; i.e. the first byte of (vr) (respectively (vs)) must
		// have a value between 0x00 and 0x7F.
		//
		// Source: http://crypto.stackexchange.com/a/1797/4110

		int rLen = r.length;
		int sLen = s.length;

		if ((r[0] & 0x80) != 0) {
			rLen++;
		}

		if ((s[0] & 0x80) != 0) {
			sLen++;
		}

		final byte[] encodedSig = new byte[rLen + sLen + 6]; // 6 T and L bytes
		encodedSig[0] = 0x30; // SEQUENCE
		encodedSig[1] = (byte) (4 + rLen + sLen);
		encodedSig[2] = 0x02; // INTEGER
		encodedSig[3] = (byte) rLen;
		encodedSig[4 + rLen] = 0x02; // INTEGER
		encodedSig[4 + rLen + 1] = (byte) sLen;

		// copy in r and s
		encodedSig[4] = 0;
		encodedSig[4 + rLen + 2] = 0;
		System.arraycopy(r, 0, encodedSig, 4 + rLen - r.length, r.length);
		System.arraycopy(s, 0, encodedSig, 4 + rLen + 2 + sLen - s.length,
				s.length);

		// final StringBuilder str = new StringBuilder("0x");
		// for (final byte b : encodedSig) {
		// str.append(String.format("%02X", b));
		// }
		// Log.v(TAG, "Encoded sig: " + str.toString());

		return encodedSig;
	}

	/**
	 * 
	 * @param value
	 *            int value to be converted to a byte array
	 * @param noOfBytes
	 *            number of bytes of resulting array
	 * @return byte array
	 */
	public static byte[] intToBytes(final int value, final int noOfBytes) {
		final byte[] bytes = new byte[noOfBytes];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) (value >> (i * 8));
		}
		return bytes;
	}

	/**
	 * 
	 * @param bytes
	 *            raw bytes array
	 * @return integer value of byte array
	 */
	public static int bytesToInt(final byte[] bytes) {
		int value = 0;
		for (int i = 0; i < bytes.length; i++) {
			value += (bytes[i] & 0xffL) << (8 * i);
		}
		return value;
	}

	/**
	 * Returns the first 16 bytes out of given parameter, if sKey length is less
	 * than 16 bytes then it is appended with zeros. ir sKey length is more than
	 * 16 bytes then it is truncated to 16 bytes.
	 * 
	 * @param sKey
	 *            key String.
	 * @return 16 bytes key.
	 */
	public static byte[] get16Bytes(final String sKey) {
		return Arrays.copyOfRange(sKey.getBytes(), 0, 16);
	}

	/**
	 * @param data
	 *            Input data on which padding is applied, zero is padded upto
	 *            current ciper block boundary.
	 * @param blockSize
	 *            Size of the blocks to which the padding will be made
	 * 
	 * @return Padded data.
	 */
	public static byte[] addPadding(final byte[] data, final int blockSize) {
		if (data == null) {
			return data;
		}
		if (data.length == 0) {
			return data;
		}
		int paddingBytes = 0;

		paddingBytes = (blockSize - (data.length) % blockSize) % blockSize;

		final byte[] padded = new byte[data.length + paddingBytes];
		System.arraycopy(data, 0, padded, 0, data.length);
		return padded;
	}

	/**
	 * @param original
	 *            Input data to Encrypt/Decrypt ( AES ).
	 * @param mode
	 *            Decides weather to Encrypt or Decrypt. it can be either
	 *            Cipher.ENCRYPT_MODE or Cipher.DECRYPT_MODE.
	 * @param iv
	 *            Initial Vector to Decrypt
	 * @param key
	 *            Key to run the cryptogram
	 * @return Encrypted/Decrypted data.
	 * @throws GeneralSecurityException
	 *             The GeneralSecurityException class is a generic security
	 *             exception class that provides mType safety for all the
	 *             security-related exception classes that extend from it.
	 */
	public static byte[] aes(final byte[] original, final int mode,
			final byte[] iv, final byte[] key) throws GeneralSecurityException {
		final Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
		// Utilities
		// .toString("F3C031894E5FEF13693B9740E7C614EAD943B33B18F38560C0CA66294CFD6A01"));
		final SecretKeySpec keySpec = new SecretKeySpec(key, "AES");

		cipher.init(mode, keySpec, new IvParameterSpec(iv));
		final byte[] returnData = cipher.doFinal(original);
		if (mode == Cipher.ENCRYPT_MODE) {
			Arrays.copyOfRange(returnData,
					returnData.length - cipher.getBlockSize(),
					returnData.length);
		} else {
			Arrays.copyOfRange(original,
					original.length - cipher.getBlockSize(), original.length);
		}
		return returnData;
	}

	/**
	 * @param sEncrypted
	 *            Input data to Decrypt ( AES ).
	 * @param pIV
	 *            Initialization Vector.
	 * @param pKey
	 *            16 byte AES key used to decrypt the data.
	 * @return Decrypted data.
	 * @throws GeneralSecurityException
	 *             The GeneralSecurityException class is a generic security
	 *             exception class that provides mType safety for all the
	 *             security-related exception classes that extend from it.
	 */
	public static String getDecryptedAESString(final String sEncrypted,
			final byte[] pIV, final byte[] pKey)
			throws GeneralSecurityException {
		byte[] encryptedBytes = Utilities.stringToBytes(sEncrypted);
		byte[] decryptedBytes = aes(encryptedBytes, Cipher.DECRYPT_MODE, pIV,
				pKey);

		return byteToHexString(decryptedBytes);

	}

	/**
	 * Converts the byte array to Hex String.
	 * 
	 * @param buffer
	 *            Byte array buffer.
	 * @return Hex String.
	 */
	public static String byteToHexString(final byte[] buffer) {
		final StringBuffer sb = new StringBuffer();

		for (int i = 0; i < buffer.length; i++) {
			sb.append(String.format("%02X", buffer[i]));
		}

		return sb.toString();
	}
}
