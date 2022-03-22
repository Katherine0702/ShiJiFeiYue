package com.cshen.tiyu.domain.ball;

import java.io.Serializable;
import java.util.ArrayList;


public class BasketBallMatch extends Match implements Serializable{
    private static final long serialVersionUID = -7060210544600464481L; 
    
    float totalScore;
	String openFlag;//是否结束销售
	String weekStr;//是否结束销售
	BasketBallGuansList mixOpen;//
	BasketBallScroeList mixSp;//即时sp
    
	public float getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(float totalScore) {
		this.totalScore = totalScore;
	}
	public String getOpenFlag() {
		return openFlag;
	}
	public void setOpenFlag(String openFlag) {
		this.openFlag = openFlag;
	}
	public String getWeekStr() {
		return weekStr;
	}
	public void setWeekStr(String weekStr) {
		this.weekStr = weekStr;
	}
    public BasketBallScroeList getMixSp() {
		return mixSp;
	}
	public void setMixSp(BasketBallScroeList mixSp) {
		this.mixSp = mixSp;
	}
	public BasketBallGuansList getMixOpen() {
		return mixOpen;
	}
	public void setMixOpen(BasketBallGuansList mixOpen) {
		this.mixOpen = mixOpen;
	}
}
