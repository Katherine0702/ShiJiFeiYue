package com.cshen.tiyu.domain.ball;

import java.util.ArrayList;

import com.cshen.tiyu.domain.Back;


public class BFZBDateList  extends Back {
	ArrayList<BFZBDate> listDtos=new ArrayList<BFZBDate>();
	public ArrayList<BFZBDate> getResultList() {
		return listDtos;
	}
	public void setResultList(ArrayList<BFZBDate> resultList) {
		this.listDtos = resultList;
	}
}

