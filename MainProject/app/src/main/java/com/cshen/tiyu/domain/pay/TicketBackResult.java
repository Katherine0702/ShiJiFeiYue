package com.cshen.tiyu.domain.pay;

import java.util.ArrayList;

import com.cshen.tiyu.domain.Back;


public class TicketBackResult extends Back{
	public ArrayList<TicketBack> ticket;

	public ArrayList<TicketBack> getTicket() {
		return ticket;
	}

	public void setTicket(ArrayList<TicketBack> ticket) {
		this.ticket = ticket;
	}
	
}
