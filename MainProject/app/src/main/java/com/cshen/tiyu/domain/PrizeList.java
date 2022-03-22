package com.cshen.tiyu.domain;

import java.util.ArrayList;

public class PrizeList extends Back {
	ArrayList<Prize> resultList=new ArrayList<Prize>();
	public ArrayList<Prize> getResultList() {
		return resultList;
	}
	public void setResultList(ArrayList<Prize> resultList) {
		this.resultList = resultList;
	}
}
