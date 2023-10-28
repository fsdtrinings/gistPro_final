package com.mkj.gist.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import com.mkj.gist.dao.StockMapDAOImpl;
import com.mkj.gist.entity.Stock;
import com.mkj.gist.util.DMI;
import com.mkj.gist.util.MACD;
import com.mkj.gist.util.Signal;
import com.opencsv.CSVReader;
import static java.util.stream.Collectors.*;
import static java.util.Map.Entry.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class RSIIndicator {

	Map<Integer, Stock> dataMap = new TreeMap<>();
	Map<String, Integer> rsiRangeWeightMap = new LinkedHashMap();
	int rsiR1A = 0;
	int rsiR1B = 0;
	int rsiR1Weightage = 0;
	int rsiR2A = 0;
	int rsiR2B = 0;
	int rsiR2Weightage = 0;
	int rsiR3A = 0;
	int rsiR3B = 0;
	int rsiR3Weightage = 0;
	int rsiR4A = 0;
	int rsiR4B = 0;
	int rsiR4Weightage = 0;
	int rsiR5A = 0;
	int rsiR5B = 0;
	int rsiR5Weightage = 0;
	int rsiR6A = 0;
	int rsiR6B = 0;
	int rsiR6Weightage = 0;
	
	String rsiRankMax1 = ""; // hold value like 75-80
	int rsiRank1A = 0;
	int rsiRank1B = 0;
	boolean rank1Filled = false;
	
	String rsiRankMax2 = ""; // hold value like 75-80
	int rsiRank2A = 0;
	int rsiRank2B = 0;
	boolean rank2Filled = false;
	
	

	MACDIndicator macd = new MACDIndicator();
	DMIIndecator dmi = new DMIIndecator();

	public RSIIndicator(Map<Integer, Stock> dataMap) {
		this.dataMap = dataMap;

		rsiRangeWeightMap.put("60-65", 0);
		rsiRangeWeightMap.put("65-70", 0);
		rsiRangeWeightMap.put("70-75", 0);
		rsiRangeWeightMap.put("75-80", 0);
		rsiRangeWeightMap.put("80-85", 0);
		rsiRangeWeightMap.put("85-100", 0);

		this.setRSIRangeWeightMap();

		System.out.println("\n\n [[[[[[[[[");
		
		System.out.println(rsiRangeWeightMap);
		
		System.out.println("\n ]]]]]]]]]\n\n");
		
		
		int count = 0;
		for (Map.Entry<String, Integer> entry : rsiRangeWeightMap.entrySet()) {
			count++;

			String key = entry.getKey();
			int value = entry.getValue();

			String arr[] = key.split("\\-");
			if (count == 1) // these count is just to control the loop iteration
			{
				this.rsiR6A = Integer.parseInt(arr[0]);
				this.rsiR6B = Integer.parseInt(arr[1]);
				this.rsiR6Weightage = value;
			}
			if (count == 2) {
				this.rsiR5A = Integer.parseInt(arr[0]);
				this.rsiR5B = Integer.parseInt(arr[1]);
				this.rsiR5Weightage = value;
			}
			if (count == 3) {
				this.rsiR4A = Integer.parseInt(arr[0]);
				this.rsiR4B = Integer.parseInt(arr[1]);
				this.rsiR4Weightage = value;
			}
			if (count == 4) {
				this.rsiR3A = Integer.parseInt(arr[0]);
				this.rsiR3B = Integer.parseInt(arr[1]);
				this.rsiR3Weightage = value;
			}
			if (count == 5) {
				this.rsiR2A = Integer.parseInt(arr[0]);
				this.rsiR2B = Integer.parseInt(arr[1]);
				this.rsiR2Weightage = value;
			}
			if (count == 6) {
				this.rsiR1A = Integer.parseInt(arr[0]);
				this.rsiR1B = Integer.parseInt(arr[1]);
				this.rsiR1Weightage = value;
			}

		}

		
		if(rsiR1Weightage>=2)
		{
			rsiRankMax1 = "85-100";
			rsiRank1A = 85; rsiRank1B = 100;
			rank1Filled = true;
		}
		else if(rsiR2Weightage>=2)
		{
			rsiRankMax1 = "80-85";
			rsiRank1A = 80; rsiRank1B = 85;
			rank1Filled = true;
		}
		else if(rsiR3Weightage>=2)
		{
			rsiRankMax1 = "75-80";
			rsiRank1A = 75; rsiRank1B = 80;
			rank1Filled = true;
		}
		else if(rsiR4Weightage>=2)
		{
			rsiRankMax1 = "70-75";
			rsiRank1A = 70; rsiRank1B = 75;
			rank1Filled = true;
		}
		else if(rsiR5Weightage>=2)
		{
			rsiRankMax1 = "65-70";
			rsiRank1A = 65; rsiRank1B = 70;
			rank1Filled = true;
		}
		else if(rsiR6Weightage>=2)
		{
			rsiRankMax1 = "60-65";
			rsiRank1A = 60; rsiRank1B = 65;
			rank1Filled = true;
		}
		
		//---------------------------------------
		
		if(rsiR2Weightage>=4 && rank1Filled == true & !rsiRankMax1.equals("80-85"))
		{
			rsiRankMax2 = "80-85";
			rsiRank2A = 80; rsiRank2B = 85;
			rank2Filled = true;
		}
		else if(rsiR3Weightage>=4 && rank1Filled == true & !rsiRankMax1.equals("75-80"))
		{
			rsiRankMax2 = "75-80";
			rsiRank2A = 75; rsiRank2B = 80;
			rank2Filled = true;
		}
		else if(rsiR4Weightage>=4 && rank1Filled == true & !rsiRankMax1.equals("70-75"))
		{
			rsiRankMax2 = "70-75";
			rsiRank2A = 70; rsiRank2B = 75;
			rank2Filled = true;
		}
		else if(rsiR5Weightage>=4 && rank1Filled == true & !rsiRankMax1.equals("65-70"))
		{
			rsiRankMax2 = "65-70";
			rsiRank2A = 65; rsiRank2B = 70;
			rank2Filled = true;
		}
		else {
			rsiRankMax2 = "Not Set";
			rank2Filled = true;
			rsiRank2A = 0; rsiRank2B = 0;
		}
		
		this.justPrint();
	}

	public void setRSIRangeWeightMap() {
		int size = dataMap.size();

		for (int i = 0; i < 150; i++) {

			Stock stock = dataMap.get(size--);
			float rsi = (Float.parseFloat(stock.getRsi()));

			if (rsi >= 60 && rsi <= 65) {
				int count = rsiRangeWeightMap.get("60-65");
				rsiRangeWeightMap.put("60-65", ++count);
			} else if (rsi >= 65.01 && rsi <= 70) {
				int count = rsiRangeWeightMap.get("65-70");
				rsiRangeWeightMap.put("65-70", ++count);
			} else if (rsi >= 70.01 && rsi <= 75) {
				int count = rsiRangeWeightMap.get("70-75");
				rsiRangeWeightMap.put("70-75", ++count);
			} else if (rsi >= 75.01 && rsi <= 80) {
				int count = rsiRangeWeightMap.get("75-80");
				rsiRangeWeightMap.put("75-80", ++count);
			} else if (rsi >= 80.01 && rsi <= 85) {
				int count = rsiRangeWeightMap.get("80-85");
				rsiRangeWeightMap.put("80-85", ++count);
			} else if (rsi >= 85.01) {
				int count = rsiRangeWeightMap.get("85-100");
				rsiRangeWeightMap.put("85-100", ++count);
			}

		} // end for
		
		
		
	
	
		
	}// end of setRSI weight

	public void justPrint() {
		
		System.out.println("RSI R1 " + this.rsiR1A + " - " + this.rsiR1B + " = " + rsiR1Weightage);
		System.out.println("RSI R2 " + this.rsiR2A + " - " + this.rsiR2B + " = " + rsiR2Weightage);
		System.out.println("RSI R3 " + this.rsiR3A + " - " + this.rsiR3B + " = " + rsiR3Weightage);
		System.out.println("RSI R4 " + this.rsiR4A + " - " + this.rsiR4B + " = " + rsiR4Weightage);
		System.out.println("RSI R5 " + this.rsiR5A + " - " + this.rsiR5B + " = " + rsiR5Weightage);
		System.out.println("RSI R6 " + this.rsiR6A + " - " + this.rsiR6B + " = " + rsiR6Weightage);
		
		System.out.println("Rank 1 "+rsiRankMax1);
		System.out.println("Rank 2 "+rsiRankMax2);
		
	}

	public Map<Integer, Stock> getRSIPrediction() {

		Map<Integer, Stock> tempDataMap = dataMap;
		Iterator<Integer> itr = dataMap.keySet().iterator();

		while (itr.hasNext()) {
			int key = itr.next();
			if(key<100) // just skip first 100 days
			{
				continue;
			}
			Stock dayValues = dataMap.get(key);
			Signal daySignal = Signal.NO_SIGNAL;

			Stock stockLast6Days[] = new Stock[6];

			stockLast6Days[0] = dataMap.get(key); // Current Date
			stockLast6Days[1] = dataMap.get(key - 1);
			stockLast6Days[2] = dataMap.get(key - 2);
			stockLast6Days[3] = dataMap.get(key - 3);
			stockLast6Days[4] = dataMap.get(key - 4);
			stockLast6Days[5] = dataMap.get(key - 5);
			
			System.out.println("\n\n %%%%%%%%%%%%    Day "+key+"  %%%%%%%%%%%%%%%%%%");
		
			
//_________________________________________________________________________________________________________________________________________________________________________			
			/* Objective no 1 : get prediction based on RSI */
			/* RSI Buy , Objective 1A */
			int rsi = (int) (Float.parseFloat(dayValues.getRsi()));
			
			System.out.println(" \n RSI := "+rsi);
			System.out.println("RSI Rank 1 "+rsiRankMax1);
			System.out.println("RSI Rank 2 "+rsiRankMax2);
			
			
			
			daySignal = getBuySignal(rsi);
			System.out.println("1 . Signal Through RSI Simple : - "+daySignal);
			
			/* MACD Buy , Objective 1B */
			if (rsi > 40 && rsi < 70) {
				daySignal = macd.getMACDBuySignal(stockLast6Days);
				System.out.println("2. Signal Through MACD : - "+daySignal);
				
			}

			

//___________________________________________________________________________________________________________________________			
			/* RSI Sell Signal */
			/* Objective 2A */
			boolean isSellConsiderable = isSellConsiderableDay(rsi);

			if (isSellConsiderable) {
				
				Signal tempSignal = getSellSignal(rsi, dayValues);
				
				System.out.println("\n3. TempSignal Recieved : - "+tempSignal );
				
				
				if(tempSignal == Signal.BUY || tempSignal == Signal.STRONG_BUY)
				{
					if(isRSIInRange(rsi, rsiRank1A, rsiRank1B))
					{
						daySignal = Signal.SELL;
					}
					else if(isRSIInRange(rsi, rsiRank2A, rsiRank2B))
					{
						daySignal = Signal.HOLD;
					}
					else if(rsi<rsiRank2B-7 && rsi>60)
					{
						daySignal = tempSignal;
					}
					
				} //end when temp signal is Buy or Strong buy
				
				else if(tempSignal == Signal.HOLD)
				{
						if(daySignal == Signal.NO_SIGNAL && !(isRSIInRange(rsi, rsiRank1A, rsiRank1B)))
						{
							daySignal = Signal.STRONG_SELL;
						}
						else if(daySignal == Signal.NO_SIGNAL && !(isRSIInRange(rsi, rsiRank2A, rsiRank2B)))
						{
							daySignal = Signal.SELL;
						}
						else if(daySignal == Signal.NO_SIGNAL && rsi < (rsiR2B-3))
						{
							daySignal = Signal.HOLD;
						}
						
				}//end when temp signal is hold
						
				else if(tempSignal == Signal.SELL || tempSignal == Signal.STRONG_SELL)
				{
						if((isRSIInRange(rsi, rsiRank1A, rsiRank1B)))
						{
							daySignal = Signal.STRONG_SELL;
						}
						else if((isRSIInRange(rsi, rsiRank2A, rsiRank2B)))
						{
							daySignal = Signal.SELL;
						}
						
				}//end when temp signal is hold
				else if(tempSignal == Signal.NO_SIGNAL)
				{
					
					if(daySignal == Signal.BUY || daySignal == Signal.STRONG_BUY)
					{
						if( (isRSIInRange(rsi, rsiRank1A, rsiRank1B)))
						{
							daySignal = Signal.SELL;
						}
						else if((isRSIInRange(rsi, rsiRank2A, rsiRank2B)))
						{
							daySignal = Signal.SELL;
						}
						else if(rsi>rsiRank2B-7 && rsi>60)
						{
							// No Change in Day Signal
						}
						else daySignal = Signal.NO_SIGNAL;
						
					}
					else
					{
						if( (isRSIInRange(rsi, rsiRank1A, rsiRank1B)))
						{
							daySignal = Signal.STRONG_SELL;
						}
						else if((isRSIInRange(rsi, rsiRank2A, rsiRank2B)))
						{
							daySignal = Signal.SELL;
						}
						
					}
					
					
				}
						
				System.out.println("3B. TempSignal Convered to DaySignal : - "+daySignal);		
						
			
				
				
			} // end if
				// final touch up code , which set the final dataset repeat after every
				// objective
			dayValues.setDaySignal(daySignal);
			tempDataMap.put(key, dayValues);
			/* ---- end of Objective 1 */
//_________________________________________________________________________________________________________________________________________________________________________			
			/*
			 * Objective no 2B: get signal through MACD indicator , reason mentioned in
			 * doMACDThings() of GISTIndicator.java class
			 */

			//daySignal = macd.getMACDBearishSignal(stockLast6Days);
			////System.out.println("4. Signal Through MACD : - "+daySignal);
			// final touch up code , which set the final dataset repeat after every
			// objective
			//dayValues.setDaySignal(daySignal);
			//tempDataMap.put(key, dayValues);
			/* end of objective no 2 */

//_________________________________________________________________________________________________________________________________________________________________________			

			/* Objective no 3: get signal through DMI indicator */

			/* end of objective no 3 */

//_________________________________________________________________________________________________________________________________________________________________________			

			// final touch up code , which set the final dataset repeat after every
			// objective is to set the final data
			System.out.println("5 :-   " + dayValues.getDate() + " " + dayValues.getRsi() + " " + daySignal);
			dayValues.setDaySignal(daySignal);
			tempDataMap.put(key, dayValues);

		} // end while

		dataMap = tempDataMap;
		return dataMap;

	}// end of RSI prediction
	
	public boolean isRSIInRange(int rsi,int r1,int r2)
	{
		return (rsi<=r2 && rsi>=r1);
	}

	public Signal getSellSignal(int rsi, Stock stock) {
		
		Signal daySignal = Signal.NO_SIGNAL;
		// System.out.print(" Inside get Sell Signal RSI := "+rsi+"\n");
		if ((rsi >= rsiR1A && rsi <= rsiR1B)) {
			// System.out.print("\nInside rsi R1");
			if (rsiR1Weightage == 0) {
				daySignal = Signal.HOLD;
			}
			if (rsiR1Weightage > 1) {
				daySignal = Signal.STRONG_SELL;
			}

		}
		if ((rsi >= rsiR2A && rsi <= rsiR2B)) {
			// System.out.print("\nInside rsi R2");
			if (rsiR2Weightage == 0) {
				if (rsiR1Weightage > 0) {
					if (dmi.getDMITrend(stock) == DMI.CHOPPY)
						daySignal = Signal.STRONG_SELL;
					if (dmi.getDMITrend(stock) == DMI.BUY_TREND_STRAT)
						daySignal = Signal.NO_SIGNAL;
					if (dmi.getDMITrend(stock) == DMI.BUY_CONTINUE_1)
						daySignal = Signal.NO_SIGNAL;
					if (dmi.getDMITrend(stock) == DMI.BUY_CONTINUE_2)
						daySignal = Signal.SELL;
					if (dmi.getDMITrend(stock) == DMI.BUY_TEAND_EXHAUST)
						daySignal = Signal.STRONG_SELL;

				}
			} else {
				/* case if RSI hit upper range most of the time */
				if (rsiR2Weightage < rsiR1Weightage) {
					if (dmi.getDMITrend(stock) == DMI.CHOPPY)
						daySignal = Signal.STRONG_SELL;
					if (dmi.getDMITrend(stock) == DMI.BUY_TREND_STRAT)
						daySignal = Signal.NO_SIGNAL;
					if (dmi.getDMITrend(stock) == DMI.BUY_CONTINUE_1)
						daySignal = Signal.HOLD;
					if (dmi.getDMITrend(stock) == DMI.BUY_CONTINUE_2)
						daySignal = Signal.HOLD;
					if (dmi.getDMITrend(stock) == DMI.BUY_TEAND_EXHAUST)
						daySignal = Signal.STRONG_SELL;
				} else if (rsiR2Weightage > rsiR1Weightage) {
					if (dmi.getDMITrend(stock) == DMI.CHOPPY)
						daySignal = Signal.STRONG_SELL;
					if (dmi.getDMITrend(stock) == DMI.BUY_TREND_STRAT)
						daySignal = Signal.HOLD;
					if (dmi.getDMITrend(stock) == DMI.BUY_CONTINUE_1)
						daySignal = Signal.HOLD;
					if (dmi.getDMITrend(stock) == DMI.BUY_CONTINUE_2)
						daySignal = Signal.SELL;
					if (dmi.getDMITrend(stock) == DMI.BUY_TEAND_EXHAUST)
						daySignal = Signal.STRONG_SELL;
				}
			}
		}
		if (rsi >= rsiR3A && rsi <= rsiR3B) {
			// System.out.print("\nInside rsi R3");
			if (rsiR3Weightage == 0) {
				// System.out.print("\n\t 1. ------ Inside RSI 3 If ");
				if (rsiR2Weightage == 0 && rsiR1Weightage == 0) // means RSI never go up
				{
					if (dmi.getDMITrend(stock) == DMI.CHOPPY)
						daySignal = Signal.STRONG_SELL;
					if (dmi.getDMITrend(stock) == DMI.BUY_TREND_STRAT)
						daySignal = Signal.NO_SIGNAL;
					if (dmi.getDMITrend(stock) == DMI.BUY_CONTINUE_1)
						daySignal = Signal.SELL;
					if (dmi.getDMITrend(stock) == DMI.BUY_CONTINUE_2)
						daySignal = Signal.STRONG_SELL;
					if (dmi.getDMITrend(stock) == DMI.BUY_TEAND_EXHAUST)
						daySignal = Signal.STRONG_SELL;
				}
				if (rsiR3Weightage < rsiR2Weightage) // means RSI most of the times go up
				{
					if (dmi.getDMITrend(stock) == DMI.CHOPPY)
						daySignal = Signal.STRONG_SELL;
					if (dmi.getDMITrend(stock) == DMI.BUY_TREND_STRAT)
						daySignal = Signal.HOLD;
					if (dmi.getDMITrend(stock) == DMI.BUY_CONTINUE_1)
						daySignal = Signal.HOLD;
					if (dmi.getDMITrend(stock) == DMI.BUY_CONTINUE_2)
						daySignal = Signal.HOLD;
					if (dmi.getDMITrend(stock) == DMI.BUY_TEAND_EXHAUST)
						daySignal = Signal.SELL;
				}

				if ((rsiR2Weightage > 0 && rsiR2Weightage < rsiR3Weightage)) // means RSI hardly hit range 2 and above
				{
					if (dmi.getDMITrend(stock) == DMI.CHOPPY)
						daySignal = Signal.STRONG_SELL;
					if (dmi.getDMITrend(stock) == DMI.BUY_TREND_STRAT)
						daySignal = Signal.NO_SIGNAL;
					if (dmi.getDMITrend(stock) == DMI.BUY_CONTINUE_1)
						daySignal = Signal.NO_SIGNAL;
					if (dmi.getDMITrend(stock) == DMI.BUY_CONTINUE_2) {
						//System.out.println("DMI Buy continue 2");
						MACD macdSignal = macd.getMACDSellSignal(stock);
						if (macdSignal == MACD.High_Buy)
							daySignal = Signal.HOLD;
						if (macdSignal == MACD.BUY)
							daySignal = Signal.SELL;
						if (macdSignal == MACD.TOP_Made)
							daySignal = Signal.STRONG_SELL;
					}
					if (dmi.getDMITrend(stock) == DMI.BUY_TEAND_EXHAUST)
						daySignal = Signal.STRONG_SELL;
				}

				if (rsiR2Weightage >= 4) {
					if (dmi.getDMITrend(stock) == DMI.CHOPPY)
						daySignal = Signal.HOLD;
					if (dmi.getDMITrend(stock) == DMI.BUY_TREND_STRAT)
						daySignal = Signal.NO_SIGNAL;
					if (dmi.getDMITrend(stock) == DMI.BUY_CONTINUE_1)
						daySignal = Signal.NO_SIGNAL;
					if (dmi.getDMITrend(stock) == DMI.BUY_CONTINUE_2) {
						 //System.out.println("DMI Buy continue 2");
						MACD macdSignal = macd.getMACDSellSignal(stock);
						if (macdSignal == MACD.High_Buy)
							daySignal = Signal.HOLD;
						if (macdSignal == MACD.BUY)
							daySignal = Signal.SELL;
						if (macdSignal == MACD.TOP_Made)
							daySignal = Signal.SELL;
					}
					if (dmi.getDMITrend(stock) == DMI.BUY_TEAND_EXHAUST)
						daySignal = Signal.SELL;
				}

			} else {
				// System.out.print("\n\t 1. ------ Inside RSI 3 else ");
				if (rsiR3Weightage > 0) {
					if (rsiR2Weightage == 0 && rsiR1Weightage == 0) {
						 System.out.print("\n\t 2.A ------ Stock never hit Above RSI");
						if (dmi.getDMITrend(stock) == DMI.CHOPPY)
							daySignal = Signal.HOLD;
						if (dmi.getDMITrend(stock) == DMI.BUY_TREND_STRAT)
							daySignal = Signal.NO_SIGNAL;
						if (dmi.getDMITrend(stock) == DMI.BUY_CONTINUE_1)
							daySignal = Signal.SELL;
						if (dmi.getDMITrend(stock) == DMI.BUY_CONTINUE_2)
							daySignal = Signal.STRONG_SELL;
						if (dmi.getDMITrend(stock) == DMI.BUY_TEAND_EXHAUST)
							daySignal = Signal.STRONG_SELL;

					}
					/* case if RSI hit upper range most of the time */
					else if (rsiR2Weightage == 0 && rsiR1Weightage > 0) {
						// System.out.print("\n\t 2.B ------ Stock hit Above RSI");
						// System.err.print("\n\t"+dmi.getDMITrend(stock));
						if (dmi.getDMITrend(stock) == DMI.CHOPPY) {
							//System.out.println("DMI Choppy");
							daySignal = Signal.HOLD;
						}
						if (dmi.getDMITrend(stock) == DMI.BUY_TREND_STRAT) {
							 //System.out.println("DMI Byt treand Strat");
							MACD macdSignal = macd.getMACDSellSignal(stock);
							if (macdSignal == MACD.High_Buy)
								daySignal = Signal.HOLD;
							if (macdSignal == MACD.BUY)
								daySignal = Signal.NO_SIGNAL;
							if (macdSignal == MACD.TOP_Made)
								daySignal = Signal.NO_SIGNAL;

						}
						if (dmi.getDMITrend(stock) == DMI.BUY_CONTINUE_1) {
							 //System.out.println("DMI Buy Continue 1");
							MACD macdSignal = macd.getMACDSellSignal(stock);
							if (macdSignal == MACD.High_Buy)
								daySignal = Signal.HOLD;
							if (macdSignal == MACD.BUY)
								daySignal = Signal.NO_SIGNAL;
							if (macdSignal == MACD.TOP_Made)
								daySignal = Signal.SELL;

						}
						if (dmi.getDMITrend(stock) == DMI.BUY_CONTINUE_2) {
							 //System.out.println("DMI Buy Continue 2");
							MACD macdSignal = macd.getMACDSellSignal(stock);
							if (macdSignal == MACD.High_Buy)
								daySignal = Signal.NO_SIGNAL;
							if (macdSignal == MACD.BUY)
								daySignal = Signal.SELL;
							if (macdSignal == MACD.TOP_Made)
								daySignal = Signal.STRONG_SELL;

						}
						if (dmi.getDMITrend(stock) == DMI.BUY_TEAND_EXHAUST) {
							 //System.out.println("DMI Buy Exhaust");
							daySignal = Signal.STRONG_SELL;
						}

					}

					/* case if RSI hit upper range most of the time */
					else if (rsiR2Weightage > 0) {
						if (rsiR1Weightage > 0)// R1 Weight is above 0
						{
						//	 System.out.print("\n\t 2.B ------ Stock hit Above RSI and R1 Range Also");
							if (dmi.getDMITrend(stock) == DMI.CHOPPY)
								daySignal = Signal.HOLD;
							if (dmi.getDMITrend(stock) == DMI.BUY_TREND_STRAT
									|| dmi.getDMITrend(stock) == DMI.BUY_CONTINUE_1) {
								MACD macdSignal = macd.getMACDSellSignal(stock);
								if (macdSignal == MACD.High_Buy)
									daySignal = Signal.HOLD;
								if (macdSignal == MACD.BUY)
									daySignal = Signal.HOLD;
								if (macdSignal == MACD.TOP_Made)
									daySignal = Signal.HOLD;

							}

							if (dmi.getDMITrend(stock) == DMI.BUY_CONTINUE_2
									|| dmi.getDMITrend(stock) == DMI.BUY_TEAND_EXHAUST) {
								MACD macdSignal = macd.getMACDSellSignal(stock);
								if (macdSignal == MACD.High_Buy)
									daySignal = Signal.HOLD;
								if (macdSignal == MACD.BUY)
									daySignal = Signal.HOLD;
								if (macdSignal == MACD.TOP_Made)
									daySignal = Signal.SELL;

							}
						} // end R1 Weight is above 0

						if (rsiR1Weightage == 0)// R1 Weight is 0
						{
							// System.out.print("\n\t 2.B ------ Stock hit Above RSI and R1 Range Also");
							if (dmi.getDMITrend(stock) == DMI.CHOPPY)
								daySignal = Signal.STRONG_SELL;
							if (dmi.getDMITrend(stock) == DMI.BUY_TREND_STRAT
									|| dmi.getDMITrend(stock) == DMI.BUY_CONTINUE_1) {
								MACD macdSignal = macd.getMACDSellSignal(stock);
								if (macdSignal == MACD.High_Buy)
									daySignal = Signal.HOLD;
								if (macdSignal == MACD.BUY)
									daySignal = Signal.HOLD;
								if (macdSignal == MACD.TOP_Made)
									daySignal = Signal.SELL;

							}

							if (dmi.getDMITrend(stock) == DMI.BUY_CONTINUE_2
									|| dmi.getDMITrend(stock) == DMI.BUY_TEAND_EXHAUST) {
								MACD macdSignal = macd.getMACDSellSignal(stock);
								if (macdSignal == MACD.High_Buy)
									daySignal = Signal.HOLD;
								if (macdSignal == MACD.BUY)
									daySignal = Signal.SELL;
								if (macdSignal == MACD.TOP_Made)
									daySignal = Signal.STRONG_SELL;

							}
						} // end R1 Weight is 0

					}

					// System.out.print("\n\t 3 ------ day Signal :- "+daySignal);
				}

			}
		}
		if (rsi >= rsiR4A && rsi <= rsiR4B) {
			 System.out.print("\nInside rsi R4");
			if (rsiR3Weightage == 0 & rsiR2Weightage == 0 & rsiR1Weightage == 0) {
				if (dmi.getDMITrend(stock) == DMI.CHOPPY)
					daySignal = Signal.NO_SIGNAL;
				if (dmi.getDMITrend(stock) == DMI.BUY_TREND_STRAT)
					daySignal = Signal.NO_SIGNAL;
				if (dmi.getDMITrend(stock) == DMI.BUY_CONTINUE_1)
					daySignal = Signal.NO_SIGNAL;
				if (dmi.getDMITrend(stock) == DMI.BUY_CONTINUE_2)
					daySignal = Signal.SELL;
				if (dmi.getDMITrend(stock) == DMI.BUY_TEAND_EXHAUST)
					daySignal = Signal.STRONG_SELL;
			} else {
				if (rsiR3Weightage != 0 | rsiR2Weightage != 0 | rsiR1Weightage != 0) {
					if (dmi.getDMITrend(stock) == DMI.CHOPPY)
						daySignal = Signal.NO_SIGNAL;
					if (dmi.getDMITrend(stock) == DMI.BUY_TREND_STRAT)
						daySignal = Signal.NO_SIGNAL;
					if (dmi.getDMITrend(stock) == DMI.BUY_CONTINUE_1)
						daySignal = Signal.NO_SIGNAL;
					if (dmi.getDMITrend(stock) == DMI.BUY_CONTINUE_2
							|| dmi.getDMITrend(stock) == DMI.BUY_TEAND_EXHAUST) {
						MACD macdSignal = macd.getMACDSellSignal(stock);

						if (macdSignal.High_Buy == macdSignal)
							daySignal = Signal.NO_SIGNAL;
						if (macdSignal.BUY == macdSignal)
							daySignal = Signal.NO_SIGNAL;
						if (macdSignal.TOP_Made == macdSignal)
							daySignal = Signal.SELL;
					}

				}

			}
		}
		if (rsi >= rsiR5A && rsi <= rsiR5B) {
		System.out.print("\nInside rsi R5 "+rsi);
		//System.out.println(rsiR4Weightage);
		//System.out.println(rsiR3Weightage);
		//System.out.println(rsiR2Weightage);
		//System.out.println(rsiR1Weightage);
		
		//System.out.println("dmi.getDMITrend(stock) :- "+dmi.getDMITrend(stock));
			if (rsiR4Weightage == 0 & rsiR3Weightage == 0 & rsiR3Weightage == 0 & rsiR1Weightage == 0)
			{
				
				if (dmi.getDMITrend(stock) == DMI.CHOPPY)
				{
					MACD macdSignal = macd.getMACDSellSignal(stock);
					System.out.println("Inside DMI Choppy & MACD :- "+macdSignal);
					if (macdSignal.High_Buy == macdSignal)
						daySignal = Signal.NO_SIGNAL;
					if (macdSignal.BUY == macdSignal)
						daySignal = Signal.NO_SIGNAL;
					if (macdSignal.TOP_Made == macdSignal)
						daySignal = Signal.SELL;
				}
				if (dmi.getDMITrend(stock) == DMI.BUY_TREND_STRAT)
					daySignal = Signal.NO_SIGNAL;
				if (dmi.getDMITrend(stock) == DMI.BUY_CONTINUE_1)
					daySignal = Signal.NO_SIGNAL;
				if (dmi.getDMITrend(stock) == DMI.BUY_CONTINUE_2)
					daySignal = Signal.SELL;
				if (dmi.getDMITrend(stock) == DMI.BUY_TEAND_EXHAUST)
					daySignal = Signal.STRONG_SELL;
			}
		}

		// RSI range R5 & R6 Pending

		// System.out.print("\n\t ===>> Day Signal "+daySignal+"\n");
		return daySignal;
	} // end of method get Sell Signal

	public Signal getBuySignal(int rsi) {
		
		if (rsi >= 33 && rsi < 40) {
			return Signal.BUY;
		} else if (rsi < 33) {
			return Signal.STRONG_BUY;
		} else {
			return Signal.NO_SIGNAL;
		}

	}

	public boolean isSellConsiderableDay(int rsi) {
		if (rsi > 60) {
			return true;
		}
		return false;
	}

	public Signal getSellPreviousCalls(int day) {

		return Signal.NO_SIGNAL;
	}

	public Signal getSupportSignal() {
		return Signal.HOLD;
	}

}
