package com.cshen.tiyu.domain.pay;

import java.io.Serializable;

public class Ticket implements Serializable{
    private static final long serialVersionUID = -7060210544600464481L;  
    String userId;
    String userPwd;
	String periodNumber;//期号
	String mode;//单式 1/复式 0
	String multiple;//倍数
	String value;//投注内容
	String specialFlag;//是否追加
	String playType;//玩法追加2，常规0
	String passType;//
	String isChase;//是否追期
	String periodSizeOfChase;//追号期数
	String wonStopOfChase;//中奖停止默认false=中奖不停止追号
	String hongBaoId;//红包ID
	String orderId;//订单编号
    String type;
	String units;//总注数
	String totalCostOfChase;//追号总金额;
	String cost;//总金额
	String wLotteryId ;
	String multiplesOfChase;
	String bestMinPrize;//最小奖金预测
	String bestMaxPrize;//最大奖金预测
	
	String agreeCopy;
	String recommendTitle;
	String recommendContent;
	boolean isOptimize = false;//奖金优化为true
	
	public String getAgreeCopy() {
		return agreeCopy;
	}
	public void setAgreeCopy(String agreeCopy) {
		this.agreeCopy = agreeCopy;
	}
	public boolean isOptimize() {
		return isOptimize;
	}
	public void setOptimize(boolean isOptimize) {
		this.isOptimize = isOptimize;
	}
	public String getAreeCopy() {
		return agreeCopy;
	}
	public void setAreeCopy(String isTuiJian) {
		this.agreeCopy = isTuiJian;
	}
	public String getRecommendTitle() {
		return recommendTitle;
	}
	public void setRecommendTitle(String tuijianTiltle) {
		this.recommendTitle = tuijianTiltle;
	}
	public String getRecommendContent() {
		return recommendContent;
	}
	public void setRecommendContent(String tuijianContent) {
		this.recommendContent = tuijianContent;
	}
	public String getBestMinPrize() {
		return bestMinPrize;
	}
	public void setBestMinPrize(String bestMinPrize) {
		this.bestMinPrize = bestMinPrize;
	}
	public String getBestMaxPrize() {
		return bestMaxPrize;
	}
	public void setBestMaxPrize(String bestMaxPrize) {
		this.bestMaxPrize = bestMaxPrize;
	}
	public String getMultiplesOfChase() {
		return multiplesOfChase;
	}
	public void setMultiplesOfChase(String multiplesOfChase) {
		this.multiplesOfChase = multiplesOfChase;
	}
	public String getPassType() {
		return passType;
	}
	public void setPassType(String passType) {
		this.passType = passType;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getUnits() {
		return units;
	}
	public void setUnits(String units) {
		this.units = units;
	}
	public String getCost() {
		return cost;
	}
	public void setCost(String cost) {
		this.cost = cost;
	}
	public String getSpecialFlag() {
		return specialFlag;
	}
	public void setSpecialFlag(String specialFlag) {
		this.specialFlag = specialFlag;
	}
	public String getPlayType() {
		return playType;
	}
	public void setPlayType(String playType) {
		this.playType = playType;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserPwd() {
		return userPwd;
	}
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	
	
	
	public String getHongBaoId() {
		return hongBaoId;
	}
	public void setHongBaoId(String hongBaoId) {
		this.hongBaoId = hongBaoId;
	}
	public String getIsChase() {
		return isChase;
	}
	public void setIsChase(String isChase) {
		this.isChase = isChase;
	}
	public String getPeriodSizeOfChase() {
		return periodSizeOfChase;
	}
	public void setPeriodSizeOfChase(String periodSizeOfChase) {
		this.periodSizeOfChase = periodSizeOfChase;
	}
	public String getTotalCostOfChase() {
		return totalCostOfChase;
	}
	public void setTotalCostOfChase(String totalCostOfChase) {
		this.totalCostOfChase = totalCostOfChase;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getMultiple() {
		return multiple;
	}
	public void setMultiple(String multiple) {
		this. multiple = multiple;
	}
	public String getNumber() {
		return value;
	}
	public void setNumber(String number) {
		value = number;
	}
	public String getSumNum() {
		return units;
	}
	public void setSumNum(String sumNum) {
		this.units = sumNum;
	}
	public String getTotalAmount() {
		return cost;
	}
	public void setTotalAmount(String totalAmount) {
		this.cost = totalAmount;
	}
	public String getPlayTypeID() {
		return playType;
	}
	public void setPlayTypeID(String playTypeID) {
		this.playType = playTypeID;
	}
	public String getIsSpecialFlag() {
		return specialFlag;
	}
	public void setIsSpecialFlag(String isSpecialFlag) {
		this.specialFlag = isSpecialFlag;
	}
	public String getWonStopOfChase() {
		return wonStopOfChase;
	}
	public void setWonStopOfChase(String wonStopOfChase) {
		this.wonStopOfChase = wonStopOfChase;
	}
	public String getPeriodNumber() {
		return periodNumber;
	}
	public void setPeriodNumber(String periodNumber) {
		this.periodNumber = periodNumber;
	}
	public String getwLotteryId() {
		return wLotteryId;
	}
	public void setwLotteryId(String wLotteryId) {
		this.wLotteryId = wLotteryId;
	}
	
	
	
	
}
