package com.cshen.tiyu.domain.deprecated;

import java.util.ArrayList;

public class DetailAccountData {

	public ArrayList<EachDetaiAccountData> myChasePlan;

	public String processId;

	public int totalCount;

	public ArrayList<EachDetaiAccountData> getFundList() {
		return myChasePlan;
	}

	public void setFundList(ArrayList<EachDetaiAccountData> fundList) {
		this.myChasePlan = fundList;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}




}
