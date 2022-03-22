package com.cshen.tiyu.domain.ball;

import java.io.Serializable;

public class ScroeSF implements Serializable{
    private static final long serialVersionUID = -7060210544600464481L; 

    ScroeBean LOSE;
    ScroeBean WIN;
	public ScroeBean getWIN() {
		return WIN;
	}
	public void setSF_WIN(ScroeBean WIN) {
		this.WIN = WIN;
	}
	public ScroeBean getLOSE() {
		return LOSE;
	}
	public void setSF_LOSE(ScroeBean LOSE) {
		this.LOSE = LOSE;
	}

}
