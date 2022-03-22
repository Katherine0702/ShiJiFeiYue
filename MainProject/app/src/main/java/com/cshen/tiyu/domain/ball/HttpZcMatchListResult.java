package com.cshen.tiyu.domain.ball;

import java.util.List;
import java.util.Map;

public class HttpZcMatchListResult {
	public String processId;
	public String errorMsg;
	public Map<String,List<ZcMatchResultDTO>> result;

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

	public Map<String, List<ZcMatchResultDTO>> getResult() {
		return result;
	}

	public void setResult(Map<String, List<ZcMatchResultDTO>> result) {
		this.result = result;
	}




}
