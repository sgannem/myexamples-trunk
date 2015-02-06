package com.xyz.test;

public interface MyConstants extends Constants {
	
	@DefaultStringValue(value="defaultValue1")
	@Key(value="key")
	public String getKey1();
}
