package com.cshen.tiyu.domain.ball;

import java.io.Serializable;
import java.util.ArrayList;


public class FootBallMatch extends Match implements Serializable{
    private static final long serialVersionUID = -7060210544600464481L; 
  
	String open;//是否结束销售
	
	FootBallScroeList mixSp;//即时sp
	FootBallGuansList mixOpen;//0,胜平负。1,进球数。2,比分。3,半全场。5,让球胜平负
	String historyHelp;
    boolean isOpen;
	
	public String getHistoryHelp() {
		return historyHelp;
	}
	public void setHistoryHelp(String historyHelp) {
		this.historyHelp = historyHelp;
	}
	public boolean isOpen() {
		return isOpen;
	}
	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
	public String getOpen() {
		return open;
	}
	public void setOpen(String open) {
		this.open = open;
	}
	public FootBallScroeList getSp() {
		return mixSp;
	}
	public void setSp(FootBallScroeList sp) {
		this.mixSp = sp;
	}
	public FootBallScroeList getMixSp() {
		return mixSp;
	}
	public void setMixSp(FootBallScroeList mixSp) {
		this.mixSp = mixSp;
	}
	public FootBallGuansList getMixOpen() {
		return mixOpen;
	}
	public void setMixOpen(FootBallGuansList mixOpen) {
		this.mixOpen = mixOpen;
	}
   }
