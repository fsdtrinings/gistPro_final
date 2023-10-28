package com.mkj.gist.service;

import java.util.List;

import com.mkj.gist.entity.StockResponseDTO;
import com.mkj.gist.util.Signal;

public class PrintingService {

	public static void doPrint(List<StockResponseDTO> stockDayWiseFinalData)
	{
		for (StockResponseDTO dayRecord : stockDayWiseFinalData) {
			if(dayRecord.getSignal() == Signal.NO_SIGNAL)
			{
				System.out.println(dayRecord);
			}
			else
			{
				System.err.println(dayRecord);
			}
			
		}
	}
}
