package com.cshen.tiyu.domain.sign;

import com.cshen.tiyu.domain.Back;

public class SignData extends Back{
	
	boolean isSignIn;//			当天是否已经签到	“true” or “false”
	int accumulatedNo;//签到次数	int类型
	boolean isMon;//	周一是否已签到	“true” or “false”
	boolean isTue;//	周二是否已签到	“true” or “false”
	boolean isWed;//	周三是否已签到	“true” or “false”
	boolean isThu;//	周四是否已签到	“true” or “false”
	boolean isFri;//	周五是否已签到	“true” or “false”
	boolean isSat;//	周六是否已签到	“true” or “false”
	boolean isSun;//	周日是否已签到	“true” or “false”
	boolean isOne;//	当前累计一天	“true” or “false”
	boolean isThree;//	当前累计三天	“true” or “false”
	boolean isSeven;//	当前累计七天	“true” or “false”
	public boolean isSignIn() {
		return isSignIn;
	}
	public void setSignIn(boolean isSignIn) {
		this.isSignIn = isSignIn;
	}
	public int getAccumulatedNo() {
		return accumulatedNo;
	}
	public void setAccumulatedNo(int accumulatedNo) {
		this.accumulatedNo = accumulatedNo;
	}
	public boolean isMon() {
		return isMon;
	}
	public void setMon(boolean isMon) {
		this.isMon = isMon;
	}
	public boolean isTue() {
		return isTue;
	}
	public void setTue(boolean isTue) {
		this.isTue = isTue;
	}
	public boolean isWed() {
		return isWed;
	}
	public void setWed(boolean isWed) {
		this.isWed = isWed;
	}
	public boolean isThu() {
		return isThu;
	}
	public void setThu(boolean isThu) {
		this.isThu = isThu;
	}
	public boolean isFri() {
		return isFri;
	}
	public void setFri(boolean isFri) {
		this.isFri = isFri;
	}
	public boolean isSat() {
		return isSat;
	}
	public void setSat(boolean isSat) {
		this.isSat = isSat;
	}
	public boolean isSun() {
		return isSun;
	}
	public void setSun(boolean isSun) {
		this.isSun = isSun;
	}
	public boolean isOne() {
		return isOne;
	}
	public void setOne(boolean isOne) {
		this.isOne = isOne;
	}
	public boolean isThree() {
		return isThree;
	}
	public void setThree(boolean isThree) {
		this.isThree = isThree;
	}
	public boolean isSeven() {
		return isSeven;
	}
	public void setSeven(boolean isSeven) {
		this.isSeven = isSeven;
	}

}
