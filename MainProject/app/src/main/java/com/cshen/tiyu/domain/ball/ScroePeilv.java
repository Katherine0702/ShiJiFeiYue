package com.cshen.tiyu.domain.ball;

import java.io.Serializable;

public class ScroePeilv implements Serializable{
    private static final long serialVersionUID = -7060210544600464481L; 
	ScroeBean LOSE;
	ScroeBean WIN;
	ScroeBean DRAW;
	public ScroeBean getLOSE() {
		return LOSE;
	}
	public void setLOSE(ScroeBean lOSE) {
		LOSE = lOSE;
	}
	public ScroeBean getWIN() {
		return WIN;
	}
	public void setWIN(ScroeBean wIN) {
		WIN = wIN;
	}
	public ScroeBean getDRAW() {
		return DRAW;
	}
	public void setDRAW(ScroeBean dRAW) {
		DRAW = dRAW;
	}
}
