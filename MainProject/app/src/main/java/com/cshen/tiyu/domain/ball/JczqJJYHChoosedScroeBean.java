package com.cshen.tiyu.domain.ball;

import java.util.ArrayList;

public class JczqJJYHChoosedScroeBean {
	boolean dan;
	String matchKey;
	String playType;
	ArrayList<String> sps = new ArrayList<String>();
	String value;//选择位置的二进制
	public boolean isDan() {
		return dan;
	}
	public void setDan(boolean dan) {
		this.dan = dan;
	}
	public String getMatchKey() {
		return matchKey;
	}
	public void setMatchKey(String matchKey) {
		this.matchKey = matchKey;
	}
	public String getPlayType() {
		return playType;
	}
	public void setPlayType(String playType) {
		this.playType = playType;
	}
	public ArrayList<String> getSps() {
		return sps;
	}
	public void setSps(ArrayList<String> sps) {
		this.sps = sps;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
