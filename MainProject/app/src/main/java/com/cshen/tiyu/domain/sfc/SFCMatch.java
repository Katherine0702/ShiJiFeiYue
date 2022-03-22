package com.cshen.tiyu.domain.sfc;

import java.io.Serializable;

import com.cshen.tiyu.domain.ball.Match;

public class SFCMatch  extends Match implements Serializable{
	private static final long serialVersionUID = -7060210544600464481L; 
	
	String guanYaJunSp;
	boolean open;
	boolean openSigle;
	String weekStr;
	SFCScroeList sp;
	public String getGuanYaJunSp() {
		return guanYaJunSp;
	}
	public void setGuanYaJunSp(String guanYaJunSp) {
		this.guanYaJunSp = guanYaJunSp;
	}
	public boolean isOpen() {
		return open;
	}
	public void setOpen(boolean open) {
		this.open = open;
	}
	public boolean isOpenSigle() {
		return openSigle;
	}
	public void setOpenSigle(boolean openSigle) {
		this.openSigle = openSigle;
	}
	public String getWeekStr() {
		return weekStr;
	}
	public void setWeekStr(String weekStr) {
		this.weekStr = weekStr;
	}
	public SFCScroeList getSp() {
		return sp;
	}
	public void setSp(SFCScroeList sp) {
		this.sp = sp;
	}
	
}
