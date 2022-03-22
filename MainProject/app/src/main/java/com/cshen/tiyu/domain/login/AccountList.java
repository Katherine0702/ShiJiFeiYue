package com.cshen.tiyu.domain.login;

import java.util.ArrayList;


public class AccountList {
	public String isNew;//1 新的；0 旧的
	public String processId;
	public String errorMsg;
	ArrayList<UserTime> userlst=new ArrayList<UserTime>();
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public ArrayList<UserTime> getUserlst() {
		return userlst;
	}
	public void setUserlst(ArrayList<UserTime> userlst) {
		this.userlst = userlst;
	}
	public String getIsNew() {
		return isNew;
	}
	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
