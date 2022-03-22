package com.cshen.tiyu.domain.sfc;

import java.util.ArrayList;

public class SFCMatchList {
	ArrayList<SFCMatch> match=new ArrayList<SFCMatch>();
	public ArrayList<SFCMatch> getResultList() {
		return match;
	}
	public void setResultList(ArrayList<SFCMatch> resultList) {
		this.match = resultList;
	}

}
