package com.cshen.tiyu.domain;

public class NowPrize extends Back{
	Prize previousPeriod;

	public Prize getPreviousPeriod() {
		return previousPeriod;
	}

	public void setPreviousPeriod(Prize previousPeriod) {
		this.previousPeriod = previousPeriod;
	}
}
