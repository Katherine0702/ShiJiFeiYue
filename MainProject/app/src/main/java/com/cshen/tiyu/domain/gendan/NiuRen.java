package com.cshen.tiyu.domain.gendan;

import java.io.Serializable;
import java.util.ArrayList;

public class NiuRen implements Serializable{
    private static final long serialVersionUID = -7060210544600464481L; 
	String id;//用户id
	String userNameTemp;//显示用户名	
	String userPic;//用户头像	
	String userLevelNew;//用户等级
	int currentCopiedSchemeNumber;//当前推荐单数量
	int concernNumber;//关注数量	
	int concernedNumber;//被关注数量
	boolean concern = false;//是否已关注	
	String hitRate;//中奖率	
	String hitRateWeek;//周中奖率	Double
	String earningsRate;//盈利率	
	String earningsRateWeek;//周盈利率			
	String allCopiedSchemePrize;//推荐中奖	
	String hitRateSeven;//近期7场的命中率
	String earningRateSeven;//近期7场的盈利率

	public String getHitRateSeven() {
		return hitRateSeven;
	}
	public void setHitRateSeven(String hitRateSeven) {
		this.hitRateSeven = hitRateSeven;
	}
	public String getEarningRateSeven() {
		return earningRateSeven;
	}
	public void setEarningRateSeven(String earningRateSeven) {
		this.earningRateSeven = earningRateSeven;
	}
	ArrayList<Boolean> nearFiveResult = new ArrayList<Boolean>();//近五期中奖状态	

	public String getAllCopiedSchemePrize() {
		return allCopiedSchemePrize;
	}
	public void setAllCopiedSchemePrize(String allCopiedSchemePrize) {
		this.allCopiedSchemePrize = allCopiedSchemePrize;
	}
	public ArrayList<Boolean> getNearFiveResult() {
		return nearFiveResult;
	}
	public void setNearFiveResult(ArrayList<Boolean> nearFiveResult) {
		this.nearFiveResult = nearFiveResult;
	}
	public String getEarningsRate() {
		return earningsRate;
	}
	public void setEarningsRate(String earningsRate) {
		this.earningsRate = earningsRate;
	}
	public String getHitRate() {
		return hitRate;
	}
	public void setHitRate(String hitRate) {
		this.hitRate = hitRate;
	}
	public String getHitRateWeek() {
		return hitRateWeek;
	}
	public void setHitRateWeek(String hitRateWeek) {
		this.hitRateWeek = hitRateWeek;
	}
	public String getEarningsRateWeek() {
		return earningsRateWeek;
	}
	public void setEarningsRateWeek(String earningsRateWeek) {
		this.earningsRateWeek = earningsRateWeek;
	}
	public boolean isConcern() {
		return concern;
	}
	public void setConcern(boolean concern) {
		this.concern = concern;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserNameTemp() {
		return userNameTemp;
	}
	public void setUserNameTemp(String userNameTemp) {
		this.userNameTemp = userNameTemp;
	}
	public String getUserPic() {
		return userPic;
	}
	public void setUserPic(String userPic) {
		this.userPic = userPic;
	}
	public String getUserLevelNew() {
		return userLevelNew;
	}
	public void setUserLevelNew(String userLevelNew) {
		this.userLevelNew = userLevelNew;
	}
	public int getCurrentCopiedSchemeNumber() {
		return currentCopiedSchemeNumber;
	}
	public void setCurrentCopiedSchemeNumber(int currentCopiedSchemeNumber) {
		this.currentCopiedSchemeNumber = currentCopiedSchemeNumber;
	}
	public int getConcernNumber() {
		return concernNumber;
	}
	public void setConcernNumber(int concernNumber) {
		this.concernNumber = concernNumber;
	}
	public int getConcernedNumber() {
		return concernedNumber;
	}
	public void setConcernedNumber(int concernedNumber) {
		this.concernedNumber = concernedNumber;
	}
    
}
