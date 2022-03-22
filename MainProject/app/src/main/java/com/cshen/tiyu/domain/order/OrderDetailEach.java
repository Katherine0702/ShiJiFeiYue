package com.cshen.tiyu.domain.order;

import java.math.BigDecimal;

public class OrderDetailEach {
	String id;//订单ID
	BigDecimal schemeCost;//单注金额
    String won;//中奖标记
    String state;
    String periodNumber;//期
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public BigDecimal getSchemeCost() {
		return schemeCost;
	}
	public void setSchemeCost(BigDecimal schemeCost) {
		this.schemeCost = schemeCost;
	}
	public String getWon() {
		return won;
	}
	public void setWon(String won) {
		this.won = won;
	}
	public String getPeriodNumber() {
		return periodNumber;
	}
	public void setPeriodNumber(String periodNumber) {
		this.periodNumber = periodNumber;
	}
    
}
