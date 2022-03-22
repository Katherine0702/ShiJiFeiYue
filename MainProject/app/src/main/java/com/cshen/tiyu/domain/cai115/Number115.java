package com.cshen.tiyu.domain.cai115;

import java.io.Serializable;
import java.util.ArrayList;

public class Number115 implements Serializable{
    private static final long serialVersionUID = -7060210544600464481L;   
	int mode;//0：复式/胆拖;;;;;;;1：单式
	int playType;//
	int num;//注数
	ArrayList<Integer> wan = new ArrayList<>();
	ArrayList<Integer> qian = new ArrayList<>();
	ArrayList<Integer> bai = new ArrayList<>();

	ArrayList<Integer> dan = new ArrayList<>();
	ArrayList<Integer> tuo = new ArrayList<>();
	ArrayList<Integer> numbers = new ArrayList<>();//选中的数字
	public ArrayList<Integer> getDan() {
		return dan;
	}
	public void setDan(ArrayList<Integer> dan) {
		this.dan = dan;
	}
	public ArrayList<Integer> getTuo() {
		return tuo;
	}
	public void setTuo(ArrayList<Integer> tuo) {
		this.tuo = tuo;
	}
	public ArrayList<Integer> getWan() {
		return wan;
	}
	public void setWan(ArrayList<Integer> wan) {
		this.wan = wan;
	}
	public ArrayList<Integer> getQian() {
		return qian;
	}
	public void setQian(ArrayList<Integer> qian) {
		this.qian = qian;
	}
	public ArrayList<Integer> getBai() {
		return bai;
	}
	public void setBai(ArrayList<Integer> bai) {
		this.bai = bai;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
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
	public ArrayList<Integer> getNumbers() {
		return numbers;
	}
	public void setNumbers(ArrayList<Integer> numbers) {
		this.numbers = numbers;
	}
}

