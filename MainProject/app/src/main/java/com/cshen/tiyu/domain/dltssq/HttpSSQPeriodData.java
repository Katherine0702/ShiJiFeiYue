package com.cshen.tiyu.domain.dltssq;


public class HttpSSQPeriodData {
	public String processId;
	public String errorMsg;
	public SSQPeriodData ssqPeriodData;

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

	public SSQPeriodData getSsqPeriodData() {
		return ssqPeriodData;
	}

	public void setSsqPeriodData(SSQPeriodData ssqPeriodData) {
		this.ssqPeriodData = ssqPeriodData;
	}


}
