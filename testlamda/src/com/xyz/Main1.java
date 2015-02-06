package com.xyz;


interface Person {
    //adds a java 8 default method
    default void sayHello() {
        System.out.println("Hello there!");
    }
}
 
class Sam implements Person {
 
}
 
public class Main1 {
     
    public static void main(String [] args) {
         
        Sam sam = new Sam();
         
        //calling sayHello method calls the method
        //defined in interface
        sam.sayHello();
    }
}