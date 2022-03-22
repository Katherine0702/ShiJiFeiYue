package com.cshen.tiyu.domain.ball;

import java.io.Serializable;
import java.util.ArrayList;


public class Match implements Serializable{
	private static final long serialVersionUID = -7060210544600464481L; 

	boolean isDan = false;
	boolean top = false;//是否置顶
	String cancel;//是否取消
	String endSaleTime;//截止时间
	String ended;//是否结束比赛
	String gameColor;//比赛颜色
	String gameName;//比赛名字
	String guestTeamName;//比赛客队
	String handicap;//比赛让分
	String homeTeamName;//比赛主队
	String matchKey;//场次序号
	String matchTime;//比赛日期
	String homeTeamImage;
	String guestTeamImage;
	ArrayList<ScroeBean> checkNumber = new ArrayList<ScroeBean>();
	
	public boolean isTop() {
		return top;
	}
	public void setTop(boolean top) {
		this.top = top;
	}
	public String getGameColor() {
		return gameColor;
	}
	public void setGameColor(String gameColor) {
		this.gameColor = gameColor;
	}
	public String getHomeTeamImage() {
		return homeTeamImage;
	}
	public void setHomeTeamImage(String homeTeamImage) {
		this.homeTeamImage = homeTeamImage;
	}
	public String getGuestTeamImage() {
		return guestTeamImage;
	}
	public void setGuestTeamImage(String guestTeamImage) {
		this.guestTeamImage = guestTeamImage;
	}
	public String getEndSaleTime() {
		return endSaleTime;
	}
	public void setEndSaleTime(String endSaleTime) {
		this.endSaleTime = endSaleTime;
	}
	public boolean isDan() {
		return isDan;
	}
	public void setDan(boolean isDan) {
		this.isDan = isDan;
	}
	public String getGameColo() {
		return gameColor;
	}
	public void setGameColo(String gameColo) {
		this.gameColor = gameColo;
	}
	public String getCancel() {
		return cancel;
	}
	public void setCancel(String cancel) {
		this.cancel = cancel;
	}
	public String getEnded() {
		return ended;
	}
	public void setEnded(String ended) {
		this.ended = ended;
	}public String getMatchKey() {
		return matchKey;
	}
	public void setMatchKey(String matchKey) {
		this.matchKey = matchKey;
	}
	public String getMatchTime() {
		return matchTime;
	}
	public void setMatchTime(String matchTime) {
		this.matchTime = matchTime;
	}
	public String getHomeTeamName() {
		return homeTeamName;
	}
	public void setHomeTeamName(String homeTeamName) {
		this.homeTeamName = homeTeamName;
	}
	public String getGuestTeamName() {
		return guestTeamName;
	}
	public void setGuestTeamName(String guestTeamName) {
		this.guestTeamName = guestTeamName;
	}
	public String getHandicap() {
		return handicap;
	}
	public void setHandicap(String handicap) {
		this.handicap = handicap;
	}

	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public ArrayList<ScroeBean> getCheckNumber() {
		return checkNumber;
	}
	public void setCheckNumber(ArrayList<ScroeBean> checkNumber) {
		this.checkNumber = checkNumber;
	}

}
