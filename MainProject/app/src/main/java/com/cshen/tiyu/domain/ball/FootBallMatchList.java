package com.cshen.tiyu.domain.ball;

import java.util.ArrayList;

import com.cshen.tiyu.domain.Back;

public class FootBallMatchList extends Back {
	ArrayList<FootBallMatch> match=new ArrayList<FootBallMatch>();
	public ArrayList<FootBallMatch> getResultList() {
		return match;
	}
	public void setResultList(ArrayList<FootBallMatch> resultList) {
		this.match = resultList;
	}
}
