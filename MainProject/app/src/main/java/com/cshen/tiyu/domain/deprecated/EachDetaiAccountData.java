package com.cshen.tiyu.domain.deprecated;

import java.math.BigDecimal;

public class EachDetaiAccountData {
	
	String createTimeStr;//发起时间	yyyy-MM-dd HH:mm:ss
	String lotteryType;//彩种编号	附录-彩种编号-lotteryType
	boolean canStop;//是否停止追号	
	String chaseTotalSize;//追号总期号	
	String chasedCost;//已追号金额	
	String chasedSize;//已追号期数	
	String curPeriodId;//当前期ID	
	long id;//追号ID	
	String multiples;//各期追号倍数	
	String nextMultiple;//下一期追号倍数	
	String prizeForWonStop;//停止追号中奖金额	默认为0， 要wonStop为true才生效
	boolean wonStop;//是否中奖停止追号	true/false 
	String state;//追号状态（大写）	RUNNING=追号中
	String STOPED;//已停止
	BigDecimal totalCost;//追号总金额	
	BigDecimal totalPrize;//追号总奖金	
	String	userId;//用户ID	
	String	userName;//用户名	
	String	version;//版本号	
	public String getCreateTimeStr() {
		return createTimeStr;
	}
	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
	public String getLotteryType() {
		return lotteryType;
	}
	public void setLotteryType(String lotteryType) {
		this.lotteryType = lotteryType;
	}
	public boolean isCanStop() {
		return canStop;
	}
	public void setCanStop(boolean canStop) {
		this.canStop = canStop;
	}
	public String getChaseTotalSize() {
		return chaseTotalSize;
	}
	public void setChaseTotalSize(String chaseTotalSize) {
		this.chaseTotalSize = chaseTotalSize;
	}
	public String getChasedCost() {
		return chasedCost;
	}
	public void setChasedCost(String chasedCost) {
		this.chasedCost = chasedCost;
	}
	public String getChasedSize() {
		return chasedSize;
	}
	public void setChasedSize(String chasedSize) {
		this.chasedSize = chasedSize;
	}
	public String getCurPeriodId() {
		return curPeriodId;
	}
	public void setCurPeriodId(String curPeriodId) {
		this.curPeriodId = curPeriodId;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getMultiples() {
		return multiples;
	}
	public void setMultiples(String multiples) {
		this.multiples = multiples;
	}
	public String getNextMultiple() {
		return nextMultiple;
	}
	public void setNextMultiple(String nextMultiple) {
		this.nextMultiple = nextMultiple;
	}
	public String getPrizeForWonStop() {
		return prizeForWonStop;
	}
	public void setPrizeForWonStop(String prizeForWonStop) {
		this.prizeForWonStop = prizeForWonStop;
	}
	public boolean isWonStop() {
		return wonStop;
	}
	public void setWonStop(boolean wonStop) {
		this.wonStop = wonStop;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getSTOPED() {
		return STOPED;
	}
	public void setSTOPED(String sTOPED) {
		STOPED = sTOPED;
	}
	public BigDecimal getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(BigDecimal totalCost) {
		this.totalCost = totalCost;
	}
	public BigDecimal getTotalPrize() {
		return totalPrize;
	}
	public void setTotalPrize(BigDecimal totalPrize) {
		this.totalPrize = totalPrize;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	

}