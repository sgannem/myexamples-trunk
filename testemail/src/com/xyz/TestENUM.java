package com.xyz;

public class TestENUM {
	
	private enum TEST {
		PASS, FAIL, UNKNOWN;
	}
	
	public static void main(String[] args) {
//		System.out.println(TEST.FAIL);
//		for(TEST temp:TEST.values()) {
//			System.out.println(temp);
//		}
		TEST t = TEST.FAIL;
		switch(t) {
		case FAIL:
			System.out.println(t);
			break;
		case PASS:
			System.out.println(t);
			break;
			
		default:
			System.out.println(TEST.UNKNOWN);
		}
		
	}

}
