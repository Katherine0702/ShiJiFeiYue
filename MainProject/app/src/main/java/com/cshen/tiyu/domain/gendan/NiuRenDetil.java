package com.cshen.tiyu.domain.gendan;

import java.util.ArrayList;

import com.cshen.tiyu.domain.Back;

public class NiuRenDetil extends Back{
	NiuRen result;
	ArrayList<MyFollowRecommend> resultList = new ArrayList<>();
	

	public NiuRen getResult() {
		return result;
	}
	public void setResult(NiuRen result) {
		this.result = result;
	}
	public ArrayList<MyFollowRecommend> getResultList() {
		return resultList;
	}
	public void setResultList(ArrayList<MyFollowRecommend> resultList) {
		this.resultList = resultList;
	}}
