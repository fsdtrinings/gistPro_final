package com.mkj.gist.service;

import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

import com.mkj.gist.entity.Stock;
import com.mkj.gist.util.MACD;
import com.mkj.gist.util.Signal;

public class MACDIndicator {
	Map<Integer, Stock> dataMap = new TreeMap<>();
	
	/* constructor used in RSIIndicator.java class in order to call same class  getMACDSellSignal(Stock stock) method*/
	
	int currentDatePosition;
	
	PriorityQueue<Float> last5HistogramsQ = new PriorityQueue<>(5);
	
	
	private void MACDIndicator() {
	}
	private void MACDIndicator(Map<Integer, Stock> dataMap) {
		this.dataMap = dataMap;
	}
	
	private void MACDIndicator(Map<Integer, Stock> dataMap,int dayFromKey) {
		this.dataMap = dataMap;

		
	}
	
	
		
	
	/* below method called various times from RSIIndicator.java class
	 * and having overloaded versions
	 * */
	
	
	
	public MACD getMACDSellSignal(Stock stock)
	{
		int macdBlueCurrentDate = (int)(Float.parseFloat(stock.getMacdBlueLine()));
		int macdRedCurrentDate = (int)(Float.parseFloat(stock.getMacdSignalRedLine()));
		int histogramUpCurrentDate = (int)(Float.parseFloat(stock.getHistorgramUp()));
		int histogramDownCurrentDate = (int)(Float.parseFloat(stock.getHistorgramDown()));
		
		Stock currentDayStock = dataMap.get(currentDatePosition);
		Stock PreviousDayStock1 = dataMap.get(currentDatePosition-1);
		Stock PreviousDayStock2 = dataMap.get(currentDatePosition-2);
		Stock PreviousDayStock3 = dataMap.get(currentDatePosition-3);
		Stock PreviousDayStock4 = dataMap.get(currentDatePosition-4);
		
		
		
		if(currentDatePosition>=30) // we ignore first 30 days data
		{
			
			float hCd = (Float.parseFloat(currentDayStock.getHistorgramUp()) != 0)? Float.parseFloat(currentDayStock.getHistorgramUp()) : Float.parseFloat(currentDayStock.getHistorgramDown());
			float hPd1 = (Float.parseFloat(PreviousDayStock1.getHistorgramUp()) != 0)? Float.parseFloat(PreviousDayStock1.getHistorgramUp()) : Float.parseFloat(PreviousDayStock1.getHistorgramDown());
			float hPd2 = (Float.parseFloat(PreviousDayStock2.getHistorgramUp()) != 0)? Float.parseFloat(PreviousDayStock2.getHistorgramUp()) : Float.parseFloat(PreviousDayStock2.getHistorgramDown());
			float hPd3 = (Float.parseFloat(PreviousDayStock3.getHistorgramUp()) != 0)? Float.parseFloat(PreviousDayStock3.getHistorgramUp()) : Float.parseFloat(PreviousDayStock3.getHistorgramDown());
			float hPd4 = (Float.parseFloat(PreviousDayStock4.getHistorgramUp()) != 0)? Float.parseFloat(PreviousDayStock4.getHistorgramUp()) : Float.parseFloat(PreviousDayStock4.getHistorgramDown());
		
			
			float totalHistogram = hCd+hPd1+hPd2+hPd3+hPd4;
			/*
			System.out.println("\n-----------"+currentDatePosition+"--------------------------------------------");
			
			System.out.println(currentDayStock);
			System.out.println(PreviousDayStock1);
			System.out.println(PreviousDayStock2);
			System.out.println(PreviousDayStock3);
			System.out.println(PreviousDayStock4);
			
			System.out.println(" ^^^   Total of Histogram "+totalHistogram+"\n");
			*/
			if(last5HistogramsQ.size() == 5)
			{
				
			}
			else
			{
				last5HistogramsQ.add(hCd);
				last5HistogramsQ.add(hPd1);
				last5HistogramsQ.add(hPd2);
				last5HistogramsQ.add(hPd3);
				last5HistogramsQ.add(hPd4);
				
				
				
				
			}
			
		}//end of if to check first 30 days
		
		
		
		
		
		
	
		
		if(macdBlueCurrentDate > 0 & macdRedCurrentDate>0 & macdBlueCurrentDate>macdRedCurrentDate)
		{
			float checkPoint = (Float.parseFloat(stock.getPrevClose()))*0.01f;
			
			if(histogramUpCurrentDate<checkPoint) return MACD.TOP_Made;
			if(histogramUpCurrentDate>checkPoint) return MACD.High_Buy;
		}
		if(macdBlueCurrentDate > 0 & macdRedCurrentDate>0 & macdBlueCurrentDate<macdRedCurrentDate)
		{
			float checkPoint = (Float.parseFloat(stock.getPrevClose()))*0.01f;
			
			if(histogramUpCurrentDate<checkPoint) return MACD.No_Signal;
			if(histogramUpCurrentDate>checkPoint) return MACD.High_Buy;
		}
		
		// -------------------------------------------------------------------------------------------
		
		if(macdBlueCurrentDate < 0 & macdRedCurrentDate < 0 & macdBlueCurrentDate > macdRedCurrentDate)
		{
			float checkPoint = (Float.parseFloat(stock.getPrevClose()))*0.01f;
			
			if(histogramUpCurrentDate<checkPoint) return MACD.No_Signal;
			if(histogramUpCurrentDate>checkPoint) return MACD.BUY;
		}
		if(macdBlueCurrentDate < 0 & macdRedCurrentDate <0 & macdBlueCurrentDate < macdRedCurrentDate)
		{
			float checkPoint = (Float.parseFloat(stock.getPrevClose()))*0.01f;
			checkPoint = 0-checkPoint;
			
			if(histogramDownCurrentDate <checkPoint) return MACD.No_Signal;
			if(histogramDownCurrentDate>checkPoint) return MACD.High_Sell;
		}
		
		
		return MACD.No_Signal;
	}

	public Signal getMACDBuySignal(Stock stockLast6Days[])
	{
		
		Stock currentDateStock = stockLast6Days[0];
		int macdBlueCurrentDate = (int)(Float.parseFloat(currentDateStock.getMacdBlueLine()));
		int macdRedCurrentDate = (int)(Float.parseFloat(currentDateStock.getMacdSignalRedLine()));
		int histogramUpCurrentDate = (int)(Float.parseFloat(currentDateStock.getHistorgramUp()));
		int histogramDownCurrentDate = (int)(Float.parseFloat(currentDateStock.getHistorgramDown()));
		
		int cmp = (int)(Float.parseFloat(currentDateStock.getPrevClose()));
		float thresholdValue = (float)(cmp*0.75/100); // taking 0.75% of CMP to get the threshold value , its been observed that histogram if within 0.5% range its small and market is choppy , no trend
		
		if(histogramDownCurrentDate < 0)
		{
			return Signal.NO_SIGNAL;
		}
		if(histogramUpCurrentDate>0 && macdBlueCurrentDate>0 && macdRedCurrentDate>0)
		{
			float histogramTotal = 0;
			float blueTotal = 0;
			
			//System.err.println(" \n\t  >>>>  MACD Indicator <<<< ");
			
			for(Stock s: stockLast6Days)
			{
				float hisUp = Float.parseFloat(s.getHistorgramUp());
				float hisDown = Float.parseFloat(s.getHistorgramDown());
				histogramTotal += (hisUp+hisDown);
				float blueValue = Float.parseFloat(s.getMacdBlueLine());
				blueTotal += blueValue;
			}
			
			float blueAvg = blueTotal/6;
			float upperRange = (float) (blueAvg + (blueAvg*0.05));
			float lowerRange = (float) (blueAvg - (blueAvg*0.05));
			
			
			
			if((macdBlueCurrentDate<upperRange && macdBlueCurrentDate> lowerRange) && macdBlueCurrentDate < macdRedCurrentDate)
			{
				System.out.println("\n 1st if :- HOLD");
				return Signal.HOLD;
			}
			
			if(histogramTotal > (thresholdValue/2) && macdBlueCurrentDate > thresholdValue && macdRedCurrentDate > thresholdValue && macdBlueCurrentDate > macdRedCurrentDate )
			{
				System.err.println("\n 2nd if :- STRONG_BUY through threshold");
				return Signal.STRONG_BUY;
			}
			if((histogramUpCurrentDate > (histogramTotal/6)) && histogramUpCurrentDate>0 && (histogramTotal/6)>0 && (macdBlueCurrentDate >  macdRedCurrentDate ))
			{
				System.err.println("\n 3rd if :- STRONG_BUY through HisAvg");
				return Signal.STRONG_BUY;
			}
	
		}
		
		System.err.println("\n return from method end :- No Signal");
		return Signal.NO_SIGNAL;
	}

	public Signal getMACDBearishSignal(Stock stockLast6Days[])
	{
		Stock currentDateStock = stockLast6Days[0];
		float blueLineValue = (Float.parseFloat(currentDateStock.getMacdBlueLine()));
		float redLineValue = (Float.parseFloat(currentDateStock.getMacdSignalRedLine()));
		float histogramUpCurrentDate = (Float.parseFloat(currentDateStock.getHistorgramUp()));
		float histogramDownCurrentDate = (Float.parseFloat(currentDateStock.getHistorgramDown()));
		float dmiM = (Float.parseFloat(currentDateStock.getDmin()));
		float adx = (Float.parseFloat(currentDateStock.getAdx()));
		float dmiP = (Float.parseFloat(currentDateStock.getDmip()));
		
		int cmp = (int)(Float.parseFloat(currentDateStock.getPrevClose()));
		float thresholdValue = (float)(cmp*0.75/100); // taking 0.75% of CMP to get the threshold value , its been observed that histogram if within 0.5% range its small and market is choppy , no trend
			
		float histogramTotal = 0;
		float hisAvg = 0;
		
		for(Stock s: stockLast6Days)
		{
			float hisUp = Float.parseFloat(s.getHistorgramUp());
			float hisDown = Float.parseFloat(s.getHistorgramDown());
			histogramTotal += (hisUp+hisDown);
			
		}
		
		hisAvg = histogramTotal/6;
		
		
		if(histogramDownCurrentDate < 0 & redLineValue < 0 & blueLineValue < 0) // in case if MACD is below Zero
		{
			if((Math.abs(histogramDownCurrentDate) < hisAvg) ) // means bigger down histogram
			{
				if(redLineValue < Math.abs(histogramDownCurrentDate) && blueLineValue < Math.abs(histogramDownCurrentDate)) 
				{
					if(blueLineValue < redLineValue)
					{
						return Signal.STRONG_SELL;
					}
					
				}
				
				if(redLineValue >= hisAvg && blueLineValue >= hisAvg) 
				{
					if(blueLineValue < redLineValue)
					{
						return Signal.STRONG_SELL;
					}
					if(blueLineValue > redLineValue && dmiM > 19 && adx > 19 && dmiP<14)
					{
						return Signal.STRONG_SELL;
					}
					if(blueLineValue > redLineValue && dmiM <15  && adx > 19 && dmiP>25)
					{
						return Signal.HOLD;
					}
					
				}
			}
			if((Math.abs(histogramDownCurrentDate) > hisAvg))
			{
				if(redLineValue < Math.abs(histogramDownCurrentDate) && blueLineValue < Math.abs(histogramDownCurrentDate)) 
				{
					if(blueLineValue < redLineValue)
					{
						return Signal.STRONG_SELL;
					}
					
				}
				if(redLineValue >= hisAvg && blueLineValue >= hisAvg) 
				{
					if(dmiM <15  && adx > 19 && dmiP>25)
					{
						return Signal.STRONG_SELL;
					}
					else return Signal.HOLD;
				}
			}
			
			
		}
	
		return Signal.NO_SIGNAL;
		
	}
	
	
	
}//end class
