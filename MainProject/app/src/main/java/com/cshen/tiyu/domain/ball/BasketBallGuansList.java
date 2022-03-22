package com.cshen.tiyu.domain.ball;

import java.io.Serializable;

public class BasketBallGuansList implements Serializable{
    private static final long serialVersionUID = -7060210544600464481L; 
	boolean pass_0;//胜负
	boolean single_0;
	boolean pass_1;//让分胜负
	boolean single_1;
	boolean pass_2;//胜负差
	boolean single_2;
	boolean pass_3;//大小分
	boolean single_3;
	public boolean isPass_0() {
		return pass_0;
	}
	public void setPass_0(boolean pass_0) {
		this.pass_0 = pass_0;
	}
	public boolean isSingle_0() {
		return single_0;
	}
	public void setSingle_0(boolean single_0) {
		this.single_0 = single_0;
	}
	public boolean isPass_1() {
		return pass_1;
	}
	public void setPass_1(boolean pass_1) {
		this.pass_1 = pass_1;
	}
	public boolean isSingle_1() {
		return single_1;
	}
	public void setSingle_1(boolean single_1) {
		this.single_1 = single_1;
	}
	public boolean isPass_2() {
		return pass_2;
	}
	public void setPass_2(boolean pass_2) {
		this.pass_2 = pass_2;
	}
	public boolean isSingle_2() {
		return single_2;
	}
	public void setSingle_2(boolean single_2) {
		this.single_2 = single_2;
	}
	public boolean isPass_3() {
		return pass_3;
	}
	public void setPass_3(boolean pass_3) {
		this.pass_3 = pass_3;
	}
	public boolean isSingle_3() {
		return single_3;
	}
	public void setSingle_3(boolean single_3) {
		this.single_3 = single_3;
	}
}
