package com.cshen.tiyu.domain.ball;

import java.io.Serializable;

public class ScroeBean implements Serializable{
    private static final long serialVersionUID = -7060210544600464481L; 
	String chg;
	String key;//名字"win"
	String value;//赔率

	public String getChg() {
		return chg;
	}
	public void setChg(String chg) {
		this.chg = chg;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
