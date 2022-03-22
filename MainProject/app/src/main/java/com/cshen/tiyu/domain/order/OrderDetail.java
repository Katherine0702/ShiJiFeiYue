package com.cshen.tiyu.domain.order;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.cshen.tiyu.domain.Back;

public class OrderDetail extends Back{
	String chaseState;//追号状态 RUNNING("追号中"),STOPED("已停止");
	int successCount;//成功注数
	String lotteryId;//"13"注数
	int totalChaseCount;//总共要追几期
	
	BigDecimal wonPrice;// 中奖金额
	boolean isWonStop;// 是否中奖后停追
	String prizeForWonStop;// 中奖金额大于该金额才停止追号
	String firstPeriodNumber;// 首期期号
	BigDecimal firstSchemeCost;// 首期投注金额
	String firstCreateTime;// 首期投注时间
	String firstIsWon;// 首期中奖状态
	String firstContent;// 首期投注内容
	String firstOrderId;
	private String playType;//11选5：玩法

	
	
	public String getFirstOrderId() {
		return firstOrderId;
	}
	public void setFirstOrderId(String firstOrderId) {
		this.firstOrderId = firstOrderId;
	}
	public String getPlayType() {
		return playType;
	}
	public void setPlayType(String playType) {
		this.playType = playType;
	}
	ArrayList<OrderDetailEach> chaseOrderList = new ArrayList<>();
	public String getChaseState() {
		return chaseState;
	}
	public void setChaseState(String chaseState) {
		this.chaseState = chaseState;
	}
	public int getSuccessCount() {
		return successCount;
	}
	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}
	public String getLotteryId() {
		return lotteryId;
	}
	public void setLotteryId(String lotteryId) {
		this.lotteryId = lotteryId;
	}
	public int getTotalChaseCount() {
		return totalChaseCount;
	}
	public void setTotalChaseCount(int totalChaseCount) {
		this.totalChaseCount = totalChaseCount;
	}
	
	public boolean isWonStop() {
		return isWonStop;
	}
	public void setWonStop(boolean isWonStop) {
		this.isWonStop = isWonStop;
	}
	
	public BigDecimal getWonPrice() {
		return wonPrice;
	}
	public void setWonPrice(BigDecimal wonPrice) {
		this.wonPrice = wonPrice;
	}
	public String getPrizeForWonStop() {
		return prizeForWonStop;
	}
	public void setPrizeForWonStop(String prizeForWonStop) {
		this.prizeForWonStop = prizeForWonStop;
	}
	public String getFirstPeriodNumber() {
		return firstPeriodNumber;
	}
	public void setFirstPeriodNumber(String firstPeriodNumber) {
		this.firstPeriodNumber = firstPeriodNumber;
	}
	public BigDecimal getFirstSchemeCost() {
		return firstSchemeCost;
	}
	public void setFirstSchemeCost(BigDecimal firstSchemeCost) {
		this.firstSchemeCost = firstSchemeCost;
	}
	public String getFirstCreateTime() {
		return firstCreateTime;
	}
	public void setFirstCreateTime(String firstCreateTime) {
		this.firstCreateTime = firstCreateTime;
	}
	public String getFirstIsWon() {
		return firstIsWon;
	}
	public void setFirstIsWon(String firstIsWon) {
		this.firstIsWon = firstIsWon;
	}
	public String getFirstContent() {
		return firstContent;
	}
	public void setFirstContent(String firstContent) {
		this.firstContent = firstContent;
	}
	public ArrayList<OrderDetailEach> getChaseOrderList() {
		return chaseOrderList;
	}
	public void setChaseOrderList(ArrayList<OrderDetailEach> chaseOrderList) {
		this.chaseOrderList = chaseOrderList;
	}

}
