package com.xyz.test;

import com.xyz.model.beans.Person;

public class TestPerson {
	
	public static void main(String[] args) {
		Person p = Person.builder().setName("Srini").setSSN("xxxx").build();
		System.out.println(p);
	}

}
