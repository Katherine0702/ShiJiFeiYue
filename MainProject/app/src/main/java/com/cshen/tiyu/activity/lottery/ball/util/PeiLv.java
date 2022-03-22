package com.cshen.tiyu.activity.lottery.ball.util;

import java.util.ArrayList;

import com.cshen.tiyu.domain.ball.ScroeBean;

public class PeiLv {
	ArrayList<ScroeBean> win,draw,lose;
	String matchKey;
	float rf;
	int rqshu;
	
	public float getRf() {
		return rf;
	}
	public void setRf(float rf) {
		this.rf = rf;
	}
	public int getRqshu() {
		return rqshu;
	}
	public void setRqshu(int rqshu) {
		this.rqshu = rqshu;
	}
	public ArrayList<ScroeBean> getWin() {
		if(win == null){
			win = new ArrayList<ScroeBean>();
		}
		return win;
	}
	public void setWin(ArrayList<ScroeBean> win) {
		this.win = win;
	}
	public ArrayList<ScroeBean> getDraw() {
		if(draw == null){
			draw = new ArrayList<ScroeBean>();
		}
		return draw;
	}
	public void setDraw(ArrayList<ScroeBean> draw) {
		this.draw = draw;
	}
	public ArrayList<ScroeBean> getLose() {
		if(lose == null){
			lose = new ArrayList<ScroeBean>();
		}
		return lose;
	}
	public void setLose(ArrayList<ScroeBean> lose) {
		this.lose = lose;
	}
	public String getMatchKey() {
		return matchKey;
	}
	public void setMatchKey(String matchKey) {
		this.matchKey = matchKey;
	}

}
