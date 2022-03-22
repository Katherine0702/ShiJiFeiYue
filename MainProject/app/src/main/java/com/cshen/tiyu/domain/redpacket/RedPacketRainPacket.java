package com.cshen.tiyu.domain.redpacket;

import com.cshen.tiyu.domain.Back;

public class RedPacketRainPacket extends Back{
	String amount;
    int isGet;//0没抢过//1抢过
    int message;/*1 = 不在红包雨活动时间
    		2 = 启动了活动，红包没有启动
    		3 = 红包已抢完
    		4 = 活动类型未开启
    		5 = 红包雨上午活动未开启
    		6 = 红包雨下午活动未开启
    		（不返回则走正常流程）*/
    		

    
	public String getAmount() {
		return amount;
	}

	public int getMessage() {
		return message;
	}

	public void setMessage(int message) {
		this.message = message;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public int getIsGet() {
		return isGet;
	}

	public void setIsGet(int isGet) {
		this.isGet = isGet;
	}
	
}
