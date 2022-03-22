package com.cshen.tiyu.domain.fast3;

import java.io.Serializable;
import java.util.ArrayList;

public class NumberFast implements Serializable{
    private static final long serialVersionUID = -7060210544600464481L;   
	int mode;//0：复式/胆拖     1：单式
	int playType;//
	int num;//注数
	int da,xiao,dan,shuang;
	ArrayList<Integer> numberhezhi = new ArrayList<>();//机选时单个和值
	
	ArrayList<Integer> numbers = new ArrayList<>();//和值/三同号、三同号通选、三不同号、三不同号连选/二同号中的同号、二同号复选、二不同号//胆
	ArrayList<Integer> number1 = new ArrayList<>();//二同号中的不同号//脱
	
	public int getDa() {
		return da;
	}
	public void setDa(int da) {
		this.da = da;	
	}
	public int getXiao() {
		return xiao;
	}
	public void setXiao(int xiao) {
		this.xiao = xiao;
		
	}
	public int getDan() {
		return dan;
	}
	public void setDan(int dan) {
		this.dan = dan;
	}
	public int getShuang() {
		return shuang;
	}
	public void setShuang(int shuang) {
		this.shuang = shuang;
	}
	public int getMode() {
		return mode;
	}
	public void setMode(int mode) {
		this.mode = mode;
	}
	public int getPlayType() {
		return playType;
	}
	public void setPlayType(int playType) {
		this.playType = playType;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}

	public ArrayList<Integer> getNumberHezhi() {
		return numberhezhi;
	}
	public void setNumberHezhi(ArrayList<Integer> numberhezhi) {
		this.numberhezhi = numberhezhi;
	}
	public ArrayList<Integer> getNumbers() {
		return numbers;
	}
	public void setNumbers(ArrayList<Integer> numbers) {
		this.numbers = numbers;
	}
	public ArrayList<Integer> getNumber1() {
		return number1;
	}
	public void setNumber1(ArrayList<Integer> number1) {
		this.number1 = number1;
	}
}