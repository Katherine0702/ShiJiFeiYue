package com.cshen.tiyu.domain;

public class Prize  {
	String lotteryType;//彩种编号
	String periodNumber;//期号
	String periodTitle;//彩种简单描述
	String prizeTime;//官方开奖时间
	String periodId;//期ID
	String result;//开奖号码
	String resultType;
	public String getResultType() {
		return resultType;
	}
	public void setResultType(String resultType) {
		this.resultType = resultType;
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
	public String getPrizeTime() {
		return prizeTime;
	}
	public void setPrizeTime(String prizeTime) {
		this.prizeTime = prizeTime;
	}
	public String getPeriodId() {
		return periodId;
	}
	public void setPeriodId(String periodId) {
		this.periodId = periodId;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}

}
