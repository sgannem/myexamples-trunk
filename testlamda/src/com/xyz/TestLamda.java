package com.xyz;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * http://viralpatel.net/blogs/lambda-expressions-java-tutorial/
 * @author sgann1
 *
 */

public class TestLamda {
	public static void main(String[] args) {
		new Thread(() -> (System.out.println("hello world"))).start();
		
		/**
		 * 
		 */
		
		new Thread(new Runnable() {
			public void run() {
				System.out.println("hello world");
			}
		}).start();
	
		/**
		 * 
		 */
		

	  List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
	  
//      System.out.println("Print all numbers:");
//      evaluate(list, (n)->true);

//      System.out.println("Print no numbers:");
//      evaluate(list, (n)->false);
//
//      System.out.println("Print even numbers:");
//      evaluate(list, (n)-> n%2 == 0 );
//
//      System.out.println("Print odd numbers:");
//      evaluate(list, (n)-> n%2 == 1 );
//
//      System.out.println("Print numbers greater than 5:");
//      evaluate(list, (n)-> n > 5 );
      
      
    //New way:
//      List<Integer> list2 = Arrays.asList(1,2,3,4,5,6,7);
//      list2.stream().map((x) -> x*x).forEach(System.out::println);
      
    //New way:
      List<Integer> list3 = Arrays.asList(1,2,3,4,5,6,7);
      int sum = list3.stream().map(x -> x*x).reduce((x,y) -> x + y).get();
      System.out.println(sum);
      
      
      

  }
	
	public static void evaluate(List<Integer> list, Predicate<Integer> predicate) {
	      for(Integer n: list)  {
	          if(predicate.test(n)) {
	              System.out.println(n + " ");
	          }
	      }
	  }

  
	
}
