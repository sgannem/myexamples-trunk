package com.xyz;

interface One {
	default void one() {
		System.out.println("One::one");
	}
}

interface Two {
	default void one() {
		System.out.println("Two::one");
	}
}

public class TestInterfaces implements One, Two {
	
	public void one() {
		Two.super.one();
	}
	
	public static void main(String[] args) {
		TestInterfaces ti = new TestInterfaces();
		ti.one();
	}
}
