package com.mkj.gist.service;

import com.mkj.gist.entity.Stock;
import com.mkj.gist.util.DMI;
import com.mkj.gist.util.Signal;

public class DMIIndecator {

	public DMI getDMITrend(Stock stock)
	{
		int adx = (int)(Float.parseFloat(stock.getAdx()));
		int dmiP = (int)(Float.parseFloat(stock.getDmip()));
		int dmiN = (int)(Float.parseFloat(stock.getDmin()));
		//System.err.print("\n\t adx : "+adx+"  dmi+ "+dmiP+" dmi- "+dmiN);
		
		if((dmiP>dmiN) & ((dmiP-dmiN)>10))
		{
			//System.err.print("\n\t inside dmi buy trend");
			if(adx>20 && adx<25) return DMI.BUY_TREND_STRAT;
			if(adx>25 && adx<50) return DMI.BUY_CONTINUE_1;
			if(adx>50 && adx<55) return DMI.BUY_CONTINUE_2;
			if(adx>55 ) return DMI.BUY_TEAND_EXHAUST;
		}
		
		if((dmiN>dmiP) & ((dmiN-dmiP)>10))
		{
			//System.err.print("\n\t inside dmi sell trend");
			if(adx>25 && adx<30) return DMI.SELL_TREND_STRAT;
			if(adx>30 && adx<62) return DMI.SELL_CONTINUE;
			if(adx>62 ) return DMI.SELL_TREND_EXHAUST;
		}
		
		return DMI.CHOPPY;
	}
}
