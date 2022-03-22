package com.cshen.tiyu.domain.ball;

import java.io.Serializable;

public class ScroeRFSF implements Serializable{
    private static final long serialVersionUID = -7060210544600464481L; 

    ScroeBean SF_WIN;
    ScroeBean SF_LOSE;
	public ScroeBean getSF_WIN() {
		return SF_WIN;
	}
	public void setSF_WIN(ScroeBean sF_WIN) {
		SF_WIN = sF_WIN;
	}
	public ScroeBean getSF_LOSE() {
		return SF_LOSE;
	}
	public void setSF_LOSE(ScroeBean sF_LOSE) {
		SF_LOSE = sF_LOSE;
	}

}
