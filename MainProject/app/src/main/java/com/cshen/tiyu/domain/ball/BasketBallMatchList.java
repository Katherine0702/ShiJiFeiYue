package com.cshen.tiyu.domain.ball;

import java.util.ArrayList;
import java.util.Date;

import com.cshen.tiyu.domain.Back;

public class BasketBallMatchList extends Back {
	ArrayList<BasketBallMatch> match=new ArrayList<BasketBallMatch>();
	public ArrayList<BasketBallMatch> getResultList() {
		return match;
	}
	public void setResultList(ArrayList<BasketBallMatch> resultList) {
		this.match = resultList;
	}
}