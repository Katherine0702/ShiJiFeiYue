package com.cshen.tiyu.domain.ball;

import java.io.Serializable;

public class PassTypeEach implements Serializable{
    private static final long serialVersionUID = -7060210544600464481L;   
	String key;
	int units;
	int matchCount;
	int[] passMatchs;
	String text;
	long  value;
	int  passTypeValue;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public int getUnits() {
		return units;
	}
	public void setUnits(int units) {
		this.units = units;
	}
	public int getMatchCount() {
		return matchCount;
	}
	public void setMatchCount(int matchCount) {
		this.matchCount = matchCount;
	}
	public int[] getPassMatchs() {
		return passMatchs;
	}
	public void setPassMatchs(int[] passMatchs) {
		this.passMatchs = passMatchs;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public long getValue() {
		return value;
	}
	public void setValue(long value) {
		this.value = value;
	}
	public int getPassTypeValue() {
		return passTypeValue;
	}
	public void setPassTypeValue(int passTypeValue) {
		this.passTypeValue = passTypeValue;
	}
	
}
