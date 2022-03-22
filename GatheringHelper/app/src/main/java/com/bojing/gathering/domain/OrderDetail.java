package com.bojing.gathering.domain;

/**
 * Created by admin on 2018/10/23.
 */

public class OrderDetail {
    String  orderNumber;//订单编号
    String  orderStatus;//订单状态(1：待支付；2：已支付)
    String  createDate;//创建时间
    String  payDate;//支付时间
    String  payer;//付款方

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

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }
}
