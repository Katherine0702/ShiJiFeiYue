package com.cshen.tiyu.domain.gendan;

import java.util.ArrayList;

import com.cshen.tiyu.domain.Back;

public class MyFollowRecommendList  extends Back {
	ArrayList<MyFollowRecommend> resultList=new ArrayList<MyFollowRecommend>();
	public ArrayList<MyFollowRecommend> getResultList() {
		return resultList;
	}
	public void setResultList(ArrayList<MyFollowRecommend> resultList) {
		this.resultList = resultList;
	}
}
