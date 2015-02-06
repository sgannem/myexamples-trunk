package com.xyz.dynamic;


interface LoginConstants extends Constants {
	
       @DefaultStringValue("Welcome to my super app")
       @Key("appDescription")
       String appDescription();
 
       @DefaultStringValue("Ok")
       @Key("okButtonLabel")
       String okButtonLabel();
}
 
public class Main {
    public static void main(String[] args) {
        LoginConstants constants = DynamicProperty.create(LoginConstants.class);
//        try{
//        System.out.println(constants);
//        }catch(Exception e) {
//        	e.printStackTrace();
//        }
        System.out.println(constants.appDescription());
        System.out.println(constants.okButtonLabel());
    }
}