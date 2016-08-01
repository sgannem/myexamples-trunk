package com.xyz;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Util implements IUtil {

	public String readFile(String fileName) {
		// BufferedReader br = new BufferedReader(new
		// InputStreamReader(getClass().getResourceAsStream("appstore.apk")));
		// BufferedReader br = new BufferedReader(new
		// InputStreamReader(getClass().getResourceAsStream("test.txt")));
		InputStreamReader isr = new InputStreamReader(getClass().getResourceAsStream(fileName));
		BufferedReader br = new BufferedReader(isr);
		// System.out.println(br);
		String s = "";
		StringBuffer sb = new StringBuffer();
		try {
			while ((s = br.readLine()) != null) {
				sb.append(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (isr != null)
					isr.close();
				if (br != null) {
					br.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println(sb);
		return sb.toString();
	}

	// public static void main(String[] args) throws IOException {
	// new Util().readFile();
	// }

}
