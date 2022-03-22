package com.cshen.tiyu.domain.zhuihao;

import java.math.BigDecimal;

public class AddOrder {
	BigDecimal totalCost;
    String chaseId;
    int lotteryId;
    String  playType;
    String state;//追号状态 RUNNING("追号中"),STOPED("已停止");
    String chaseTime;
    
	public String getPlayType() {
		return playType;
	}
	public void setPlayType(String playType) {
		this.playType = playType;
	}
	public BigDecimal getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(BigDecimal totalCost) {
		this.totalCost = totalCost;
	}
	public String getChaseId() {
		return chaseId;
	}
	public void setChaseId(String chaseId) {
		this.chaseId = chaseId;
	}
	public int getLotteryId() {
		return lotteryId;
	}
	public void setLotteryId(int lotteryId) {
		this.lotteryId = lotteryId;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getChaseTime() {
		return chaseTime;
	}
	public void setChaseTime(String chaseTime) {
		this.chaseTime = chaseTime;
	}

}
