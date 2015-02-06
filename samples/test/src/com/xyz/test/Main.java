package com.xyz.test;

public class Main {
	public static void main(String[] args) {
		MyConstants consts = DynamicProvider.create(MyConstants.class);
		System.out.println(consts.getKey1());
	}
}
