package com.cshen.tiyu.domain;

import java.io.Serializable;
import java.util.ArrayList;

public class NumberPl35 implements Serializable{
    private static final long serialVersionUID = -7060210544600464481L;   
	int num;//注数
	ArrayList<Integer> wan = new ArrayList<>();
	ArrayList<Integer> qian = new ArrayList<>();
	ArrayList<Integer> bai = new ArrayList<>();
	ArrayList<Integer> shi = new ArrayList<>();
	ArrayList<Integer> ge = new ArrayList<>();

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
	
	public ArrayList<Integer> getShi() {
		return shi;
	}
	public void setShi(ArrayList<Integer> shi) {
		this.shi = shi;
	}
	public ArrayList<Integer> getGe() {
		return ge;
	}
	public void setGe(ArrayList<Integer> ge) {
		this.ge = ge;
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
}

