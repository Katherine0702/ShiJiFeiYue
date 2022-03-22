package com.cshen.tiyu.domain.find;

import java.util.ArrayList;

public class FindBeanDatas {
	private String errCode;
	private ArrayList<FindBean> findBeans=new ArrayList<>();
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	public ArrayList<FindBean> getFindBeans() {
		return findBeans;
	}
	public void setFindBeans(ArrayList<FindBean> findBeans) {
		this.findBeans = findBeans;
	}
    
}
