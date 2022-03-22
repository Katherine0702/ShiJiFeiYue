package com.cshen.tiyu.domain.pay;

import java.util.List;
import java.util.Map;


public class HttpPayWayConfig {
	public String processId;
	public String errorMsg;

	public List<PayWayConfig> payWayList;

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

	public List<PayWayConfig> getPayWayList() {
		return payWayList;
	}

	public void setPayWayList(List<PayWayConfig> payWayList) {
		this.payWayList = payWayList;
	}

}
