package com.cshen.tiyu.domain.gendan;

import java.io.Serializable;

import com.cshen.tiyu.domain.ball.Match;

public class RenZhengMatch extends Match implements Serializable{
    private static final long serialVersionUID = -7060210544600464481L; 
	String bet;
	boolean dan;
	String result;
	String playType;
	public String getBet() {
		return bet;
	}
	public void setBet(String bet) {
		this.bet = bet;
	}
	public boolean isDan() {
		return dan;
	}
	public void setDan(boolean dan) {
		this.dan = dan;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getPlayType() {
		return playType;
	}
	public void setPlayType(String playType) {
		this.playType = playType;
	}
	
}
