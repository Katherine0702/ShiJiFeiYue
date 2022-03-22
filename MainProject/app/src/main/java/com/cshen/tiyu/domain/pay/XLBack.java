package com.cshen.tiyu.domain.pay;

public class XLBack {

	String merchantOutOrderNo;
	String merid;
	String noncestr;
	String notifyUrl;
	String orderMoney;
	String orderTime;
	String sign;
	public String getMerchantOutOrderNo() {
		return merchantOutOrderNo;
	}
	public void setMerchantOutOrderNo(String merchantOutOrderNo) {
		this.merchantOutOrderNo = merchantOutOrderNo;
	}
	public String getMerid() {
		return merid;
	}
	public void setMerid(String merid) {
		this.merid = merid;
	}
	public String getNoncestr() {
		return noncestr;
	}
	public void setNoncestr(String noncestr) {
		this.noncestr = noncestr;
	}
	public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	public String getOrderMoney() {
		return orderMoney;
	}
	public void setOrderMoney(String orderMoney) {
		this.orderMoney = orderMoney;
	}
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
}
