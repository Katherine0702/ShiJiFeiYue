package com.cshen.tiyu.domain.pay;

public class HttpPayUrlData {
	public String processId;
	public String errorMsg;
	
	public String payUrl;
	public String flag;
	public String httpbody;
	public String orderId;
	public String signData;
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getSignData() {
		return signData;
	}
	public void setSignData(String signData) {
		this.signData = signData;
	}
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
	public String getPayUrl() {
		return payUrl;
	}
	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getHttpbody() {
		return httpbody;
	}
	public void setHttpbody(String httpbody) {
		this.httpbody = httpbody;
	}

	
}
