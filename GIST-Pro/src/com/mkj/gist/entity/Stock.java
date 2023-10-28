package com.mkj.gist.entity;

import java.time.LocalDate;

import com.mkj.gist.util.Signal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock {

	private LocalDate date;
	private String symbol;
	private String prevClose;
	private String open;
	private String low;
	private String high;
	private String last;
	private String close;
	private String macdBlueLine;
	private String macdSignalRedLine;
	private String historgramUp;
	private String historgramDown;
	private String rsi;
	private String ema10;
	private String ema20;
	private String ema50;
	private String ema200;
	private String ema250;
	private String ema300;
	private String dmin;
	private String dmip;
	private String adx;
	private Signal daySignal;
	@Override
	
	public String toString() {
		return "Stock [date=" + date + ", prevClose=" + prevClose + ", macdBlueLine=" + macdBlueLine
				+ ", macdSignalRedLine=" + macdSignalRedLine + ", historgramUp=" + historgramUp + ", historgramDown="
				+ historgramDown + ", rsi=" + rsi + ", dmin=" + dmin + ", dmip=" + dmip + ", adx=" + adx
				+ ", daySignal=" + daySignal + "]";
	}
	
	
	
}
