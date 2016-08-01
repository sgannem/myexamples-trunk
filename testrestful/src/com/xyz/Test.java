package com.xyz;

public class Test {

	public static void main(String[] args) {
		Utils.now();
	}

	public static void main2(String[] args) {
		byte[] s = new byte[] { (byte) 16, (byte) 255 };
		String str = Utils.getHexString(s);
		System.out.println(str);
	}

	public static void main1(String[] args) {

		byte[] s = Utils.getByteArray("ffff");
		for (byte t : s) {
			System.out.println(t);
		}

	}

}