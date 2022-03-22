package com.cshen.tiyu.domain.gendan;

import java.util.ArrayList;

import com.cshen.tiyu.domain.Back;

public class MyRecommendDetilList   extends Back {
	ArrayList<MyRecommendDetil> resultList=new ArrayList<MyRecommendDetil>();
	public ArrayList<MyRecommendDetil> getResultList() {
		return resultList;
	}
	public void setResultList(ArrayList<MyRecommendDetil> resultList) {
		this.resultList = resultList;
	}
}
