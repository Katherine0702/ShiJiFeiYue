package com.bojing.gathering.domain;

/**
 * Created by admin on 2018/9/18.
 */

public class InfoDetail extends Back{
    String body;//商品说明
    String createTime;//创建时间
    String recordNo;//订单号
    String txNo;//第三方交易号
    String txNoStatus;//交易状态
    String buyerName;//买家昵称
    String totalFee;//交易金额
    String providerId;//收钱码提供者编号
    String providerAccount;//收钱码支付宝账号
    String sign;//MD5签名信息
    int paymentId;//支付宝1；

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getRecordNo() {
        return recordNo;
    }

    public void setRecordNo(String recordNo) {
        this.recordNo = recordNo;
    }

    public String getTxNo() {
        return txNo;
    }

    public void setTxNo(String txNo) {
        this.txNo = txNo;
    }

    public String getTxNoStatus() {
        return txNoStatus;
    }

    public void setTxNoStatus(String txNoStatus) {
        this.txNoStatus = txNoStatus;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getProviderAccount() {
        return providerAccount;
    }

    public void setProviderAccount(String providerAccount) {
        this.providerAccount = providerAccount;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }
}
