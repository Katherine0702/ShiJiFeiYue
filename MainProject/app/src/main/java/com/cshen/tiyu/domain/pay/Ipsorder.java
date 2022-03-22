package com.cshen.tiyu.domain.pay;

public class Ipsorder {
    public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getCardtype() {
		return cardtype;
	}
	public void setCardtype(String cardtype) {
		this.cardtype = cardtype;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getPayOrderID() {
		return payOrderID;
	}
	public void setPayOrderID(String payOrderID) {
		this.payOrderID = payOrderID;
	}
	public String getPayWay() {
		return payWay;
	}
	public void setPayWay(String payWay) {
		this.payWay = payWay;
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
	public String getUserWay() {
		return userWay;
	}
	public void setUserWay(String userWay) {
		this.userWay = userWay;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	String amount;
    String cardtype;
    String  id;
    String  memo;
    String  payOrderID;
    String  payWay;
    String  userId;
    String  userName;
    String  userWay;
    String  version;
}
