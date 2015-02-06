package com.xyz.test;

import java.util.Properties;

public class PropertiesLoader {

	public static Properties properties;

	public static String getValue(String key) {
		if (properties == null) {
			init();
		}
		return properties.getProperty(key);
	}

	public static void init() {
		try {
			properties = new Properties();
			properties.load(PropertiesLoader.class
					.getResourceAsStream("/com/xyz/test/my.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
