package com.xyz;

import java.io.File;
import java.io.IOException;

public class TWR {

	public static void main(String[] args) {
		
		try {
			File file = null;
			file.createNewFile();
		}catch(IOException | NullPointerException e)
		{
			e.printStackTrace();
		}
		
	}
	
}
