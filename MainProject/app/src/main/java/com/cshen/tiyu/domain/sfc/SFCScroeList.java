package com.cshen.tiyu.domain.sfc;

import java.io.Serializable;

import com.cshen.tiyu.domain.ball.ScroeBean;

public class SFCScroeList implements Serializable{
	private static final long serialVersionUID = -7060210544600464481L; 
	ScroeBean  odds1;
	boolean isCheck1 = false;
    ScroeBean  odds3;
	boolean isCheck3 = false;
    ScroeBean  odds2;
	boolean isCheck2 = false;
    ScroeBean  asiaOdd;
    
	public boolean isCheck1() {
		return isCheck1;
	}
	public void setCheck1(boolean isCheck1) {
		this.isCheck1 = isCheck1;
	}
	public boolean isCheck3() {
		return isCheck3;
	}
	public void setCheck3(boolean isCheck3) {
		this.isCheck3 = isCheck3;
	}
	public boolean isCheck2() {
		return isCheck2;
	}
	public void setCheck2(boolean isCheck2) {
		this.isCheck2 = isCheck2;
	}
	public ScroeBean getOdds1() {
		return odds1;
	}
	public void setOdds1(ScroeBean odds1) {
		this.odds1 = odds1;
	}
	public ScroeBean getOdds3() {
		return odds3;
	}
	public void setOdds3(ScroeBean odds3) {
		this.odds3 = odds3;
	}
	public ScroeBean getOdds2() {
		return odds2;
	}
	public void setOdds2(ScroeBean odds2) {
		this.odds2 = odds2;
	}
	public ScroeBean getAsiaOdd() {
		return asiaOdd;
	}
	public void setAsiaOdd(ScroeBean asiaOdd) {
		this.asiaOdd = asiaOdd;
	}
    
}
