package com.cshen.tiyu.domain.login;

/**
 * Created by lingcheng on 15/10/2.
 */
public class UserResultData {
	public String processId;
	public String errorMsg;
	public User user;
	public String isNew;
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
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getIsNew() {
		return isNew;
	}
	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}
	
	
	
}
