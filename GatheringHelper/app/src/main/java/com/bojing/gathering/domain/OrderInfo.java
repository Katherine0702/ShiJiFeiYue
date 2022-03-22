package com.bojing.gathering.domain;

/**
 * Created by admin on 2018/10/16.
 */

public class OrderInfo {
    String orderNumber;//订单编号
    String orderStatus;//订单状态(1：待支付；2：已支付)
    String payDate;//支付时间

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }
}

