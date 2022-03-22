package com.cshen.tiyu.domain.ball;

import java.util.ArrayList;



public class JJYHEach {
    String names;
    ArrayList<String>  matchKey = new ArrayList<>();
	ArrayList<ScroeBean> checks = new ArrayList<>();
    int beishu = 1;
    double beishuDouble;//倍数的全部小数
    double jiangjinDouble;//单倍奖金的全部小数
    double jiangjinEach;//单倍奖金的两位小数
    boolean isCheck = true;

    public ArrayList<String> getMatchKey() {
		return matchKey;
	}
	public void setMatchKey(ArrayList<String> matchKey) {
		this.matchKey = matchKey;
	}
	public ArrayList<ScroeBean> getChecks() {
		return checks;
	}
	public void setChecks(ArrayList<ScroeBean> checks) {
		this.checks = checks;
	}
	public String getNames() {
		return names;
	}
	public void setNames(String names) {
		this.names = names;
	}
	
	public int getBeishu() {
		return beishu;
	}
	public void setBeishu(int beishu) {
		this.beishu = beishu;
	}
	public double getJiangjinEach() {
		return jiangjinEach;
	}
	public void setJiangjinEach(double jiangjinEach) {
		this.jiangjinEach = jiangjinEach;
	}
	public boolean isCheck() {
		return isCheck;
	}
	public double getJiangjinDouble() {
		return jiangjinDouble;
	}
	public void setJiangjinDouble(double jiangjinDouble) {
		this.jiangjinDouble = jiangjinDouble;
	}
	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}
	public double getBeishuDouble() {
		return beishuDouble;
	}
	public void setBeishuDouble(double beishuDouble) {	
		this.beishuDouble = beishuDouble;
	}
    
}
