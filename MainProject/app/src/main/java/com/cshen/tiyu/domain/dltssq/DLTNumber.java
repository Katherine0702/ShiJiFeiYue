package com.cshen.tiyu.domain.dltssq;

import java.io.Serializable;


public class DLTNumber implements Serializable{
    private static final long serialVersionUID = -7060210544600464481L;   
	NumberEach qianqu;
	NumberEach houqu;
	int num;//注数
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public NumberEach getQianqu() {
		return qianqu;
	}
	public void setQianqu(NumberEach qianqu) {
		this.qianqu = qianqu;
	}
	public NumberEach getHouqu() {
		return houqu;
	}
	public void setHouqu(NumberEach houqu) {
		this.houqu = houqu;
	}
}
