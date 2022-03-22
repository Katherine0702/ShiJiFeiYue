package com.cshen.tiyu.domain.pay;

public class TicketBack {

	String process;
	String errorMsg;
	String orderId;

	String orderState;
/*	UNFULL("未满")
	FULL("满员")
	SUCCESS("成功")
	CANCEL("撤销")
	REFUNDMENT("退款")
	MANUAlESUCCESS("成功")
	PAYSAVE("待付款")*/

	

	public String getErrorMsg() {
		return errorMsg;
	}
	public String getOrderState() {
		return orderState;
	}
	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getProcess() {
		return process;
	}
	public void setProcess(String process) {
		this.process = process;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
}
