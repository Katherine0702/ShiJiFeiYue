package com.cshen.tiyu.domain.ball;

import java.io.Serializable;

public class ScroeDXF  implements Serializable{
    private static final long serialVersionUID = -7060210544600464481L; 

    ScroeBean LARGE;
    ScroeBean LITTLE;
	public ScroeBean getLARGE() {
		return LARGE;
	}
	public void setLARGE(ScroeBean lARGE) {
		LARGE = lARGE;
	}
	public ScroeBean getLITTLE() {
		return LITTLE;
	}
	public void setLITTLE(ScroeBean lITTLE) {
		LITTLE = lITTLE;
	}   
}
