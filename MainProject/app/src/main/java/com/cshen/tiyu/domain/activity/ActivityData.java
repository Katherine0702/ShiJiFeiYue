package com.cshen.tiyu.domain.activity;

import java.util.ArrayList;

public class ActivityData {

	public String errorMsg;
	String processId;
	ArrayList<ActivityEach> activityList=new ArrayList<ActivityEach>();
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public ArrayList<ActivityEach> getMyScheme() {
		return activityList;
	}
	public void setMyScheme(ArrayList<ActivityEach> myScheme) {
		this.activityList = myScheme;
	}
}
