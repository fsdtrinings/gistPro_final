package com.mkj.gist.dao;

import java.util.Map;
import java.util.TreeMap;

import com.mkj.gist.entity.Stock;

public class StockMapDAOImpl {

	public static Map<Integer, Stock> dayWisedataMap = new TreeMap<>();
	
	public static void insertData(int key,Stock value)
	{
		dayWisedataMap.put(key, value);
	}
	
	
}
