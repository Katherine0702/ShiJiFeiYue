package com.cshen.tiyu.domain.main;

import java.util.ArrayList;

public class TabIndicatorData {

	private long id;
	//底部导航
    private ArrayList<TabIndicator> indicators=new ArrayList<TabIndicator>();;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	private String errCode;

	public ArrayList<TabIndicator> getIndicators() {
		return indicators;
	}

	public void setIndicators(ArrayList<TabIndicator> indicators) {
		this.indicators = indicators;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	
	
	
	
	
	
	
}
