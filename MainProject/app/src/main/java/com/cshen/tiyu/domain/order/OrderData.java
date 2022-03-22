package com.cshen.tiyu.domain.order;

import java.util.ArrayList;

public class OrderData {

	public String errorMsg;
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	String processId;
	String totalCount;
	ArrayList<Order> myScheme=new ArrayList<Order>();
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public String getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}
	public ArrayList<Order> getMyScheme() {
		return myScheme;
	}
	public void setMyScheme(ArrayList<Order> myScheme) {
		this.myScheme = myScheme;
	}
	
	
	
	
}
