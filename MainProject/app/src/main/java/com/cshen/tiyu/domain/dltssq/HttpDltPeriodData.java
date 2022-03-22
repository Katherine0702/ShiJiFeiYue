package com.cshen.tiyu.domain.dltssq;


public class HttpDltPeriodData {
	public String processId;
	public String errorMsg;
	public DltPeriodData dltPeriodData;

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public DltPeriodData getDltPeriodData() {
		return dltPeriodData;
	}

	public void setDltPeriodData(DltPeriodData dltPeriodData) {
		this.dltPeriodData = dltPeriodData;
	}

}
