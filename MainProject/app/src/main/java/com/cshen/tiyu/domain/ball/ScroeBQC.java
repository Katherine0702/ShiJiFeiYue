package com.cshen.tiyu.domain.ball;

import java.io.Serializable;

public class ScroeBQC implements Serializable{
    private static final long serialVersionUID = -7060210544600464481L; 
	  ScroeBean  LOSE_LOSE;
      ScroeBean  LOSE_WIN;
      ScroeBean  WIN_DRAW;
      ScroeBean  DRAW_DRAW;
      ScroeBean  WIN_WIN;
      ScroeBean  LOSE_DRAW;
      ScroeBean  WIN_LOSE;
      ScroeBean  DRAW_WIN;
      ScroeBean  DRAW_LOSE;
	public ScroeBean getLOSE_LOSE() {
		return LOSE_LOSE;
	}
	public void setLOSE_LOSE(ScroeBean lOSE_LOSE) {
		LOSE_LOSE = lOSE_LOSE;
	}
	public ScroeBean getLOSE_WIN() {
		return LOSE_WIN;
	}
	public void setLOSE_WIN(ScroeBean lOSE_WIN) {
		LOSE_WIN = lOSE_WIN;
	}
	public ScroeBean getWIN_DRAW() {
		return WIN_DRAW;
	}
	public void setWIN_DRAW(ScroeBean wIN_DRAW) {
		WIN_DRAW = wIN_DRAW;
	}
	public ScroeBean getDRAW_DRAW() {
		return DRAW_DRAW;
	}
	public void setDRAW_DRAW(ScroeBean dRAW_DRAW) {
		DRAW_DRAW = dRAW_DRAW;
	}
	public ScroeBean getWIN_WIN() {
		return WIN_WIN;
	}
	public void setWIN_WIN(ScroeBean wIN_WIN) {
		WIN_WIN = wIN_WIN;
	}
	public ScroeBean getLOSE_DRAW() {
		return LOSE_DRAW;
	}
	public void setLOSE_DRAW(ScroeBean lOSE_DRAW) {
		LOSE_DRAW = lOSE_DRAW;
	}
	public ScroeBean getWIN_LOSE() {
		return WIN_LOSE;
	}
	public void setWIN_LOSE(ScroeBean wIN_LOSE) {
		WIN_LOSE = wIN_LOSE;
	}
	public ScroeBean getDRAW_WIN() {
		return DRAW_WIN;
	}
	public void setDRAW_WIN(ScroeBean dRAW_WIN) {
		DRAW_WIN = dRAW_WIN;
	}
	public ScroeBean getDRAW_LOSE() {
		return DRAW_LOSE;
	}
	public void setDRAW_LOSE(ScroeBean dRAW_LOSE) {
		DRAW_LOSE = dRAW_LOSE;
	}
}
