package com.mkj.gist.entity;

import com.mkj.gist.util.Signal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockResponseDTO {
	
	private String date;
	private String symbol;
	private String price;
	private String rsi;
	private Signal signal;
	private String openPrice;
	private String closePrice;
	private String highPrice;
	private String lowPrice;

}
