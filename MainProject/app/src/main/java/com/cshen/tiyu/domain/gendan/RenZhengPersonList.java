package com.cshen.tiyu.domain.gendan;

import java.util.ArrayList;

import com.cshen.tiyu.domain.Back;

public class RenZhengPersonList  extends Back {
	ArrayList<RenZhengPerson> resultList=new ArrayList<RenZhengPerson>();
	public ArrayList<RenZhengPerson> getResultList() {
		return resultList;
	}
	public void setResultList(ArrayList<RenZhengPerson> resultList) {
		this.resultList = resultList;
	}
}
