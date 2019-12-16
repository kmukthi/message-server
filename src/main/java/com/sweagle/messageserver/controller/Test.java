package com.sweagle.messageserver.controller;

import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@FunctionalInterface 
interface MyInterface{  
    void display();  
}

public class Test {
	    
    public static void main(String[] args) {  
    	ZonedDateTime rightNow = ZonedDateTime.now();
		int diff = rightNow.getDayOfWeek().compareTo(DayOfWeek.SUNDAY);
		int target = -4;
		int result = target - diff;
		ZonedDateTime wednesday = rightNow.plusDays(result);
    }  
	
	
}
