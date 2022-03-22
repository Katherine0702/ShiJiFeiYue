package com.cshen.tiyu.domain.main;

import java.util.ArrayList;

public class PreLoadPageData {


    long id;

	private ArrayList<PreLoadPage> preLoadPages=new ArrayList<PreLoadPage>();;
	
	private String errCode;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	

	public ArrayList<PreLoadPage> getPreLoadPages() {
		return preLoadPages;
	}

	public void setPreLoadPages(ArrayList<PreLoadPage> preLoadPages) {
		this.preLoadPages = preLoadPages;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	
	
	
	
	
	
}
