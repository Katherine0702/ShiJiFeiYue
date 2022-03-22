package com.cshen.tiyu.domain.fast3;

import java.util.ArrayList;

public class MissDate {

	ArrayList<String> hz_nummiss=new ArrayList<String>();//和值

	ArrayList<String> dx_miss=new ArrayList<String>();//三同号单选遗漏
	ArrayList<String> tx_miss=new ArrayList<String>();//三同通选遗漏
	
	ArrayList<String> fx_miss=new ArrayList<String>();//二同复选，二同单选同号遗漏
	ArrayList<String> dx2_miss=new ArrayList<String>();//二同单选不同号遗漏
	
	ArrayList<String> lx_miss=new ArrayList<String>();//三连通选遗漏
	
	ArrayList<String> num=new ArrayList<String>();//三不同号 二不同号胆拖遗漏

	public ArrayList<String> getHz() {
		return hz_nummiss;
	}

	public void setHz(ArrayList<String> hz_nummiss) {
		this.hz_nummiss = hz_nummiss;
	}

	public ArrayList<String> getSTHDx() {
		return dx_miss;
	}

	public void setSTHDx(ArrayList<String> dx_miss) {
		this.dx_miss = dx_miss;
	}

	public ArrayList<String> getSTHTx() {
		return tx_miss;
	}

	public void setSTHTx(ArrayList<String> tx_miss) {
		this.tx_miss = tx_miss;
	}

	public ArrayList<String> getETHDFx() {
		return fx_miss;
	}

	public void setETHDFx(ArrayList<String> fx_miss) {
		this.fx_miss = fx_miss;
	}

	public ArrayList<String> getETHBTH() {
		return dx2_miss;
	}

	public void setETHBTH(ArrayList<String> dx2_miss) {
		this.dx2_miss = dx2_miss;
	}

	public ArrayList<String> getSTHL() {
		return lx_miss;
	}

	public void setSTHL(ArrayList<String> lx_miss) {
		this.lx_miss = lx_miss;
	}

	public ArrayList<String> getSBEB() {
		return num;
	}

	public void setSBEB(ArrayList<String> num) {
		this.num = num;
	}
	
}
