package com.cshen.tiyu.domain.order;

import java.math.BigDecimal;


public class Order {
	boolean prizeSended;
	String createTimeStr;
	String lotteryType;
	int multiple;
	BigDecimal myBonus;
	BigDecimal myCost;
	String periodNumber;
	String plPlayType;
	int playTypeOrdinal;
	BigDecimal prize;
	BigDecimal prizeAfterTax;
	long chaseId = -1;
	String resultTimeStr;
	int schemeCost;
	int schemeId;
	String schemeNumber;
	String schemePrintState;
	String schemeState;
	boolean sendToPrint;
	String shareType;
	int sponsorId;
	String sponsorName;
	int units;
	String winningUpdateStatus;
	boolean won;
	String zcPlayType;
	int zoomMyCost ;

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
	public int getMultiple() {
		return multiple;
	}
	public void setMultiple(int multiple) {
		this.multiple = multiple;
	}
	public BigDecimal getMyBonus() {
		return myBonus;
	}
	public void setMyBonus(BigDecimal myBonus) {
		this.myBonus = myBonus;
	}
	public BigDecimal getMyCost() {
		return myCost;
	}
	public void setMyCost(BigDecimal myCost) {
		this.myCost = myCost;
	}
	public String getPeriodNumber() {
		return periodNumber;
	}
	public void setPeriodNumber(String periodNumber) {
		this.periodNumber = periodNumber;
	}
	public String getPlPlayType() {
		return plPlayType;
	}
	public void setPlPlayType(String plPlayType) {
		this.plPlayType = plPlayType;
	}
	public int getPlayTypeOrdinal() {
		return playTypeOrdinal;
	}
	public void setPlayTypeOrdinal(int playTypeOrdinal) {
		this.playTypeOrdinal = playTypeOrdinal;
	}
	public BigDecimal getPrize() {
		return prize;
	}
	public void setPrize(BigDecimal prize) {
		this.prize = prize;
	}
	public BigDecimal getPrizeAfterTax() {
		return prizeAfterTax;
	}
	public void setPrizeAfterTax(BigDecimal prizeAfterTax) {
		this.prizeAfterTax = prizeAfterTax;
	}
	public String getResultTimeStr() {
		return resultTimeStr;
	}
	public void setResultTimeStr(String resultTimeStr) {
		this.resultTimeStr = resultTimeStr;
	}
	public int getSchemeCost() {
		return schemeCost;
	}
	public void setSchemeCost(int schemeCost) {
		this.schemeCost = schemeCost;
	}
	public int getSchemeId() {
		return schemeId;
	}
	public void setSchemeId(int schemeId) {
		this.schemeId = schemeId;
	}
	public String getSchemeNumber() {
		return schemeNumber;
	}
	public void setSchemeNumber(String schemeNumber) {
		this.schemeNumber = schemeNumber;
	}
	public String getSchemePrintState() {
		return schemePrintState;
	}
	public void setSchemePrintState(String schemePrintState) {
		this.schemePrintState = schemePrintState;
	}
	public String getSchemeState() {
		return schemeState;
	}
	public void setSchemeState(String schemeState) {
		this.schemeState = schemeState;
	}
	public boolean isSendToPrint() {
		return sendToPrint;
	}
	public void setSendToPrint(boolean sendToPrint) {
		this.sendToPrint = sendToPrint;
	}
	public String getShareType() {
		return shareType;
	}
	public void setShareType(String shareType) {
		this.shareType = shareType;
	}
	public int getSponsorId() {
		return sponsorId;
	}
	public void setSponsorId(int sponsorId) {
		this.sponsorId = sponsorId;
	}
	public String getSponsorName() {
		return sponsorName;
	}
	public void setSponsorName(String sponsorName) {
		this.sponsorName = sponsorName;
	}
	public int getUnits() {
		return units;
	}
	public void setUnits(int units) {
		this.units = units;
	}
	public String getWinningUpdateStatus() {
		return winningUpdateStatus;
	}
	public void setWinningUpdateStatus(String winningUpdateStatus) {
		this.winningUpdateStatus = winningUpdateStatus;
	}
	public boolean isWon() {
		return won;
	}
	public void setWon(boolean won) {
		this.won = won;
	}
	public String getZcPlayType() {
		return zcPlayType;
	}
	public void setZcPlayType(String zcPlayType) {
		this.zcPlayType = zcPlayType;
	}
	public int getZoomMyCost() {
		return zoomMyCost;
	}
	public void setZoomMyCost(int zoomMyCost) {
		this.zoomMyCost = zoomMyCost;
	}

	public long getChaseId() {
		return chaseId;
	}
	public void setChaseId(long chaseId) {
		this.chaseId = chaseId;
	}

	public boolean isPrizeSended() {
		return prizeSended;
	}
	public void setPrizeSended(boolean prizeSended) {
		this.prizeSended = prizeSended;
	}
}
