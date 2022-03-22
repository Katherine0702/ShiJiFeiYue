package com.cshen.tiyu.domain;

import java.util.ArrayList;

public class PeriodResultData {

	public String processId;
	public ArrayList<Period> periodList;
	public String errorMsg;
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public Prize previousPeriod;
	public Prize getPreviousPeriod() {
		return previousPeriod;
	}
	public void setPreviousPeriod(Prize previousPeriod) {
		this.previousPeriod = previousPeriod;
	}
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public ArrayList<Period> getPeriodList() {
		return periodList;
	}
	public void setPeriodList(ArrayList<Period> periodList) {
		this.periodList = periodList;
	}
}
