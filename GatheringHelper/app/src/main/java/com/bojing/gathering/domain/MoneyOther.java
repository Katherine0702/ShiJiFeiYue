package com.bojing.gathering.domain;

/**
 * Created by admin on 2018/10/23.
 */

public class MoneyOther {
    String paymentId;//1、支付宝支付 2、微信支付 3、QQ钱包支付 4、京东支付 5、网银支付 6、百度钱包支付
    String paymentTypeDesc;
    String isHighlight;// 0、否 1、是
    String price;
    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentTypeDesc() {
        return paymentTypeDesc;
    }

    public void setPaymentTypeDesc(String paymentTypeDesc) {
        this.paymentTypeDesc = paymentTypeDesc;
    }

    public String getIsHighlight() {
        return isHighlight;
    }

    public void setIsHighlight(String isHighlight) {
        this.isHighlight = isHighlight;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
