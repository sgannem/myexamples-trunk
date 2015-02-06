package com.xyz.test;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class Handler implements InvocationHandler {
	
	public Object object;
	
	public Handler(Object object) {
		this.object = object;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		String key="", value = "";
		Annotation[] annotation = method.getAnnotations();
		for(Annotation temp:annotation) {
			if(temp instanceof DefaultStringValue) {
				value=((DefaultStringValue) temp).value();
			} else if(temp instanceof Key) {
				key = ((Key) temp).value();
			}
		}
		String keyValue=PropertiesLoader.getValue(key);
		
		return keyValue==null?value:keyValue;
	}

}
