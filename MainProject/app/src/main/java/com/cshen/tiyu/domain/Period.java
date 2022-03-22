package com.cshen.tiyu.domain;

public class Period {
	String endJoinTime_compound;

	String endJoinTime_single;

	String endedTime;

	String lotteryType;

	String periodNumber;
	String periodTitle;
	String playType;
	String prizePool;

	String prizeTime;//
	String selfEndInitTime_compound;
	String selfEndInitTime_single;
	String shareEndInitTime_compound;
	String shareEndInitTime_single;
	String state;

	long interval;
	long realInterval;
	public long getRealInterval() {
		return realInterval;
	}
	public void setRealInterval(long realInterval) {
		this.realInterval = realInterval;
	}
	public long getInterval() {
		return interval;
	}
	public void setInterval(long interval) {
		this.interval = interval;
	}
	public String getEndJoinTime_compound() {
		return endJoinTime_compound;
	}
	public void setEndJoinTime_compound(String endJoinTime_compound) {
		this.endJoinTime_compound = endJoinTime_compound;
	}
	public String getEndJoinTime_single() {
		return endJoinTime_single;
	}
	public void setEndJoinTime_single(String endJoinTime_single) {
		this.endJoinTime_single = endJoinTime_single;
	}
	public String getEndedTime() {
		return endedTime;
	}
	public void setEndedTime(String endedTime) {
		this.endedTime = endedTime;
	}
	public String getLotteryType() {
		return lotteryType;
	}
	public void setLotteryType(String lotteryType) {
		this.lotteryType = lotteryType;
	}
	public String getPeriodNumber() {
		return periodNumber;
	}
	public void setPeriodNumber(String periodNumber) {
		this.periodNumber = periodNumber;
	}
	public String getPeriodTitle() {
		return periodTitle;
	}
	public void setPeriodTitle(String periodTitle) {
		this.periodTitle = periodTitle;
	}
	public String getPlayType() {
		return playType;
	}
	public void setPlayType(String playType) {
		this.playType = playType;
	}
	public String getPrizePool() {
		return prizePool;
	}
	public void setPrizePool(String prizePool) {
		this.prizePool = prizePool;
	}
	public String getPrizeTime() {
		return prizeTime;
	}
	public void setPrizeTime(String prizeTime) {
		this.prizeTime = prizeTime;
	}
	public String getSelfEndInitTime_compound() {
		return selfEndInitTime_compound;
	}
	public void setSelfEndInitTime_compound(String selfEndInitTime_compound) {
		this.selfEndInitTime_compound = selfEndInitTime_compound;
	}
	public String getSelfEndInitTime_single() {
		return selfEndInitTime_single;
	}
	public void setSelfEndInitTime_single(String selfEndInitTime_single) {
		this.selfEndInitTime_single = selfEndInitTime_single;
	}
	public String getShareEndInitTime_compound() {
		return shareEndInitTime_compound;
	}
	public void setShareEndInitTime_compound(String shareEndInitTime_compound) {
		this.shareEndInitTime_compound = shareEndInitTime_compound;
	}
	public String getShareEndInitTime_single() {
		return shareEndInitTime_single;
	}
	public void setShareEndInitTime_single(String shareEndInitTime_single) {
		this.shareEndInitTime_single = shareEndInitTime_single;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}




}
