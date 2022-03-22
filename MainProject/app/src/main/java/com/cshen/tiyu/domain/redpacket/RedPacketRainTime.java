package com.cshen.tiyu.domain.redpacket;

import com.cshen.tiyu.domain.Back;

public class RedPacketRainTime extends Back{
    String sysDate;
    long subSecond;
    int isGet;
    int  message;
 
	public int getIsGet() {
		return isGet;
	}
	public void setIsGet(int isGet) {
		this.isGet = isGet;
	}
	public int getMessage() {
		return message;
	}
	public void setMessage(int message) {
		this.message = message;
	}
	public long getSubSecond() {
		return subSecond;
	}
	public void setSubSecond(long subSecond) {
		this.subSecond = subSecond;
	}
	public String getSysDate() {
		return sysDate;
	}
	public void setSysDate(String sysDate) {
		this.sysDate = sysDate;
	}
}
