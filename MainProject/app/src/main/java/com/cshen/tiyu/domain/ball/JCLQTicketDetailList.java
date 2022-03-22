package com.cshen.tiyu.domain.ball;

import java.util.ArrayList;

import com.cshen.tiyu.domain.Back;


public class JCLQTicketDetailList   extends Back {
	ArrayList<JCLQTicketDetail> resultList=new ArrayList<JCLQTicketDetail>();
	public ArrayList<JCLQTicketDetail> getResultList() {
		return resultList;
	}
	public void setResultList(ArrayList<JCLQTicketDetail> resultList) {
		this.resultList = resultList;
	}

}
