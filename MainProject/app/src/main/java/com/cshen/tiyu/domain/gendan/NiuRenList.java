package com.cshen.tiyu.domain.gendan;

import java.util.ArrayList;

import com.cshen.tiyu.domain.Back;

public class NiuRenList  extends Back {
	ArrayList<NiuRen> resultList=new ArrayList<NiuRen>();
	public ArrayList<NiuRen> getResultList() {
		return resultList;
	}
	public void setResultList(ArrayList<NiuRen> niurenList) {
		this.resultList = niurenList;
	}

}
