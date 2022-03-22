package com.cshen.tiyu.domain.information;

import java.util.ArrayList;

public class InformationData {
	String id;
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	String errCode;

	ArrayList<Information> infoList=new ArrayList<Information>();

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public ArrayList<Information> getInfoList() {
		return infoList;
	}

	public void setInfoList(ArrayList<Information> infoList) {
		this.infoList = infoList;
	}






}
