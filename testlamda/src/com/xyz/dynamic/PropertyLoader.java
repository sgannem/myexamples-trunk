package com.xyz.dynamic;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
 
public class PropertyLoader {
    private static Properties prop = null;
 
    public static String get(String key) {
        if (null == prop) {
            init();
        }
        return (String) prop.get(key);
    }
 
    private static void init() {
        prop = new Properties();
        InputStream in = Properties.class
                .getResourceAsStream("/com/xyz/dynamic/config.properties");
        try {
            prop.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
 
    }
}