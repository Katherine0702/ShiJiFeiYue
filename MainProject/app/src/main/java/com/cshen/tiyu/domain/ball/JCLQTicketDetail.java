package com.cshen.tiyu.domain.ball;

import java.util.ArrayList;

public class JCLQTicketDetail {
	String wanfa;
	String time;
    ArrayList<JCLQTicketDetailBean> result = new ArrayList<>();
	public String getWanfa() {
		return wanfa;
	}
	public void setWanfa(String wanfa) {
		this.wanfa = wanfa;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public ArrayList<JCLQTicketDetailBean> getResult() {
		return result;
	}
	public void setResult(ArrayList<JCLQTicketDetailBean> result) {
		this.result = result;
	}
    
}
