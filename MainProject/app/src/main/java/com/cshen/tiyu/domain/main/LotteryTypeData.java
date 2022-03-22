package com.cshen.tiyu.domain.main;

import java.util.ArrayList;

public class LotteryTypeData {
	
    long id;

	private ArrayList<LotteryType> lotteryList=new ArrayList<LotteryType>();;
	
	private String errCode;

	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public ArrayList<LotteryType> getLotteryList() {
		return lotteryList;
	}

	public void setLotteryList(ArrayList<LotteryType> lotteryList) {
		this.lotteryList = lotteryList;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
