package com.cshen.tiyu.domain.deprecated;

import com.cshen.tiyu.domain.pay.ActivityForPayPage;

public class HttpActivityForPaypage {
	public String processId;
	public String errorMsg;
	public ActivityForPayPage entity;

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

	public ActivityForPayPage getEntity() {
		return entity;
	}

	public void setEntity(ActivityForPayPage entity) {
		this.entity = entity;
	}



}
