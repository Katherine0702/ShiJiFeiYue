package com.cshen.tiyu.activity.lottery.cai115;

import java.text.DecimalFormat;


public class ZNZHUtils {
	private static ZNZHUtils znzhUtil;
	public static ZNZHUtils getZNZHUtils() {
		if (znzhUtil == null) {
			znzhUtil = new ZNZHUtils();
		}
		return znzhUtil;
	}
	public  int getBeishuYLL(long lastMoney,int prizeMoney,double yll){
		DecimalFormat df = new DecimalFormat("0.00");
		double beishuM = lastMoney*(yll+1);
		double beishuC = prizeMoney-2*(yll+1);
		return (int)Math.ceil(
				Double.parseDouble(df.format(beishuM))/
				Double.parseDouble(df.format(beishuC)));
	}
	public  int getYLL0(int lastMoney,int beishu,int prizeMoney){	
		return (prizeMoney*beishu-2*beishu-lastMoney)/(lastMoney+2*beishu);
	}
	public  int getBeishuYLMoney(long lastMoney,int prizeMoney,int ylmoney){
		double beishuM = lastMoney+ylmoney;
		double beishuC = prizeMoney-2;
		return (int)Math.ceil(beishuM/beishuC);
	}
}
 