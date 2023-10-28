package com.mkj.gist.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {

	public static LocalDate getDateFromString(String date)
	{
		LocalDate localDate = null;
		try {
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
			localDate = LocalDate.parse(date, formatter);
		
			return localDate;// format will be YYYY-MM-DD
		
		} catch (Exception e) {
			
			localDate = getDateFromString(getFormattedDate(date));
		}
		return localDate;
	}
	
	public static String getFormattedDate(String date)
	{
		String arr[] = date.split("\\-");
		String month = (arr[0].length() == 1)? 0+""+arr[0]:arr[0];
		String day = (arr[1].length() == 1)? 0+""+arr[1]:arr[1];
		date = month+"/"+day+"/"+arr[2];
		
		return date;
		
		
	}
	
	public static void main(String[] args) {
		System.out.println(getDateFromString("1/1/2021"));
	}
	
	
}
