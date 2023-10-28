package com.mkj.gist.service;

import java.io.FileNotFoundException;
import java.io.FileReader;

import com.opencsv.CSVReader;

public class StockCSVReaderService {
	public static int x = -1;
	public static final int DATE = ++x;
	public static final int SYMBOL = ++x;
	public static final int PREVIOUS_CLOSE = ++x;
	public static final int OPEN_Price = ++x; // 3
	public static final int HIGH_Price = ++x; //4
	public static final int LOW_Price = ++x; //5
	public static final int LAST = ++x; //6
	
	public static final int CLOSE_Price = ++x; //7
	
	
	public static final int MACD_Blue_Line = ++x; //8
	public static final int MACD_Signal_RED_Line = ++x; //9
	public static final int UP_HISTOGRAM = ++x; // 10
	public static final int DOWN_HISTOGRAM = ++x; //11
	
	
	public static final int RSI = ++x; //12
	public static final int EMA10 = ++x; // 13
	public static final int EMA20 =++x; //14
	public static final int EMA50 = ++x; //15
	public static final int EMA200 = ++x;// 16
	public static final int EMA250 = ++x; //17
	public static final int EMA300 = ++x; //18
	
	public static final int DMIP = ++x;//19
	public static final int DMIN = ++x;//20
	public static final int ADX = ++x;//21
	
	
	
	public static CSVReader loadFile(String fileName) {
		
		try {
			//C:\gistpro\inputcsv
			//String folder = "E:\\Kirti Bansal\\Gist-ProV1\\July2023";
			String folder = "C:\\gistpro\\inputcsv";
			if(!fileName.endsWith(".csv"))
			{
				throw new Exception("Only CSV File Excepted");
			}
			String path = folder+"\\"+fileName+"";
			return new CSVReader(new FileReader(path));
		} 
		catch (FileNotFoundException e) {
			System.err.println(" !!! Stock CSV Input Util , static code file upload issue \n"+e);
		} 
		catch (Exception e) {
			System.err.println(" !!! Stock CSV Input Util , static code file upload issue \n"+e);
		}
		return null;
	}//end loadfile
	
}//end class
