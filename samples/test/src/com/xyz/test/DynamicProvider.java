package com.xyz.test;

import java.lang.reflect.Proxy;

public class DynamicProvider {

	public static <T extends Constants> T create(Class<T> clazz) {
		@SuppressWarnings("unchecked")
		T object = (T) Proxy.newProxyInstance(clazz.getClassLoader(),
				new Class[] { clazz }, new Handler(clazz));
		return object;
	}

}
