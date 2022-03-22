package com.cshen.tiyu.domain;

public class HttpOrderDetailInfo {
	public String errorMsg;
	public String processId;
	public String result;
	public Scheme scheme;
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
	public Scheme getScheme() {
		return scheme;
	}
	public void setScheme(Scheme scheme) {
		this.scheme = scheme;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
}
