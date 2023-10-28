package com.mkj.gist;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.mkj.gist.dao.StockMapDAOImpl;
import com.mkj.gist.entity.Stock;
import com.mkj.gist.entity.StockResponseDTO;
import com.mkj.gist.service.PrintingService;
import com.mkj.gist.service.RSIIndicator;
import com.mkj.gist.service.StockCSVReaderService;
import com.mkj.gist.util.DateUtil;
import com.mkj.gist.util.Signal;
import com.opencsv.CSVReader;


public class GISTIndicator {

	public static List<StockResponseDTO> stockDayWiseFinalData = new ArrayList<StockResponseDTO>();
	
	static String symbolName = "HAVELLS";
	public static void main(String[] args) {
		
		
		
		GISTIndicator gist = new GISTIndicator();
		//gist.symbolName = args[0];
		
		Map<Integer, Stock> dataMap = new TreeMap<>();

		try {

			
			String fileName = symbolName.toUpperCase()+".csv";
			gist.init(fileName); // populate map

			dataMap = StockMapDAOImpl.dayWisedataMap; // this code will load the stock map

			gist.doRSIThings(dataMap);

			//doPrint();
			doLoadOnExcel();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void init(String fileName) throws Exception {
		/*
		 * this method is to read the stock from CSV and load in map based on
		 * <number,Stock day> entry as "stockdayEntry" 
		 */
		String[] dayValues = null;
		CSVReader reader = StockCSVReaderService.loadFile(fileName);

		int i = 1;
		while ((dayValues = reader.readNext()) != null) {

			LocalDate date = DateUtil.getDateFromString(dayValues[StockCSVReaderService.DATE]);
			String symbol = dayValues[StockCSVReaderService.SYMBOL];
			String prevClose = dayValues[StockCSVReaderService.PREVIOUS_CLOSE];
			String open = dayValues[StockCSVReaderService.OPEN_Price];
			String low = dayValues[StockCSVReaderService.LOW_Price];
			String high = dayValues[StockCSVReaderService.HIGH_Price];
			String last = dayValues[StockCSVReaderService.LAST];
			String close = dayValues[StockCSVReaderService.CLOSE_Price];
			String macdBlueLine = dayValues[StockCSVReaderService.MACD_Blue_Line];
			String macdSignalRedLine = dayValues[StockCSVReaderService.MACD_Signal_RED_Line];
			String historgramUp = dayValues[StockCSVReaderService.UP_HISTOGRAM];
			String historgramDown = dayValues[StockCSVReaderService.DOWN_HISTOGRAM];
			String rsi = dayValues[StockCSVReaderService.RSI];
			String ema10 = dayValues[StockCSVReaderService.EMA10];
			String ema20 = dayValues[StockCSVReaderService.EMA20];
			String ema50 = dayValues[StockCSVReaderService.EMA50];
			String ema200 = dayValues[StockCSVReaderService.EMA200];
			String ema250 = dayValues[StockCSVReaderService.EMA250];
			String ema300 = dayValues[StockCSVReaderService.EMA300];
			String dmin = dayValues[StockCSVReaderService.DMIN];
			String dmip = dayValues[StockCSVReaderService.DMIP];
			String adx = dayValues[StockCSVReaderService.ADX];
			Signal daySignal = Signal.NO_SIGNAL;

			Stock stockdayEntry = new Stock(date, symbol, prevClose, open, low, high, last, close, macdBlueLine,
					macdSignalRedLine, historgramUp, historgramDown, rsi, ema10, ema20, ema50, ema200, ema250, ema300,
					dmin, dmip, adx, daySignal);

			StockMapDAOImpl.insertData(i++, stockdayEntry);

		} // end while

	}

	public static void doRSIThings(Map<Integer, Stock> dataMap) throws IOException {

		RSIIndicator rsiIndicator = new RSIIndicator(dataMap);

		StockMapDAOImpl.dayWisedataMap = rsiIndicator.getRSIPrediction();

	}// end of read stock

	public static void doMACDThings(Map<Integer, Stock> dataMap) throws IOException {
		
		
		/*
		 * this method is not called from here 
		 * instead method is called from RSIIndicator class getRSIPrediction()
		 * Reason : actually the whole data of stock is maintain under one map 
		 * which stores data as <Integer,Stock> , key int resembles as day count
		 * 
		 * now for the prediction we have to iterate map and fetch daily record 
		 * this activity we did inside RSIIndicator.java getRSIPrediction()
		 * 
		 * so now this method will act like as main initiator of GIST process 
		 * which first check the RSI things , then MACD things and then after DMI things 
		 * 
		 * we can iterate the map in this method too, but imagine the situation 
		 * first RSI reads whole last one year daily record and set predictions 
		 * and then after same process is done by MACD and set the predictions again 
		 * so wrt to save iteration time we use RSIIndicator.java getRSIPrediction() method
		 * 
		 * */
		
	}// end of read stock

	public static void doDMIThings(Map<Integer, Stock> dataMap) throws IOException {

	}// end of read stock

	public static void doPrint() {
		System.out.println("\n\n ---- Print Final Data ---\n\n");
		for (Map.Entry<Integer, Stock> entry : StockMapDAOImpl.dayWisedataMap.entrySet()) {

			Stock dayValues = entry.getValue();

			if (dayValues.getDaySignal() == Signal.NO_SIGNAL)
				System.out.println(entry.getKey() + " - " + dayValues);
			else
				System.err.println(entry.getKey() + " - " + dayValues);
		}
	}

	public static void doLoadOnExcel() {
		
		LocalDate today = LocalDate.now();
		String outputFolder = "C:\\gistpro\\outputcsv\\";
		
		//String file = outputFolder+symbolName+"_output_"+today+".csv";
		String file = outputFolder+symbolName+"_output_"+today+".csv";
		
		try {
			
			
			PrintWriter pw = new PrintWriter(new FileWriter(new File(file)));
			
			pw.print("Date,Symbol,Price,RSI,SIGNAL,Open,Close,High,Low");
			pw.print("\n");
			
			
			for (Map.Entry<Integer, Stock> entry : StockMapDAOImpl.dayWisedataMap.entrySet()) {
				
				Stock dayEntry = entry.getValue();
				StockResponseDTO dto = new StockResponseDTO
						(dayEntry.getDate()+"",dayEntry.getSymbol(), 
						dayEntry.getClose(), dayEntry.getRsi(), dayEntry.getDaySignal(),
						dayEntry.getOpen(),dayEntry.getClose(),dayEntry.getHigh(),dayEntry.getLow());
				
				
				pw.print(dto.getDate()+","+dto.getSymbol()+","
				+dto.getPrice()+","+dto.getRsi()+","+dto.getSignal()+","
				+dto.getOpenPrice()+","+dto.getClosePrice()+","
				+dto.getHighPrice()+","+dto.getLowPrice());
				
				
				pw.print("\n");
				
			}
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

}// end of class
