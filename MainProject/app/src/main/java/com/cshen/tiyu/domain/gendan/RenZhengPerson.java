package com.cshen.tiyu.domain.gendan;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

public class RenZhengPerson implements Serializable{
    private static final long serialVersionUID = -7060210544600464481L; 
	int	multiple = 1;//倍数
	BigDecimal	copiedSchemePrize;//跟投金额	BigDecimal
	String firstMatchTime;//截止时间	
	String passTypeStr;//玩法	
	String recommendTitle;//推荐标题	
	String recommendContent;//推荐语	
	String schemeBackupsId;//推荐单Id	
	String schemeCost;//推荐单投注金额	BigDecimal
	String sponsorId;//发起人Id	
	String sponsorName;//发起人用户名	
	String sponsorUserLevelNew;//发起人用户名等级	
	String sponsorUserPic;//发起人用户头像	
	int sponsorFormNumber;//发起人单子总数	
	boolean canCopy;//是否可以跟单
	String  hitRateWeek;
	int timeOfSeven;//中单数
	String earningRateSeven;//近期7场的盈利率
	int copiedSchemeNumber;//跟单人数
	
	public int getTimeOfSeven() {
		return timeOfSeven;
	}
	public void setTimeOfSeven(int timeOfSeven) {
		this.timeOfSeven = timeOfSeven;
	}
	public int getCopiedSchemeNumber() {
		return copiedSchemeNumber;
	}
	public void setCopiedSchemeNumber(int copiedSchemeNumber) {
		this.copiedSchemeNumber = copiedSchemeNumber;
	}
	public String getEarningRateSeven() {
		return earningRateSeven;
	}
	public void setEarningRateSeven(String earningRateSeven) {
		this.earningRateSeven = earningRateSeven;
	}
	public String getHitRateWeek() {
		return hitRateWeek;
	}
	public void setHitRateWeek(String hitRateWeek) {
		this.hitRateWeek = hitRateWeek;
	}
	public int getMultiple() {
		return multiple;
	}
	public void setMultiple(int multiple) {
		this.multiple = multiple;
	}
	public boolean isCanCopy() {
		return canCopy;
	}
	public void setCanCopy(boolean canCopy) {
		this.canCopy = canCopy;
	}
	public ArrayList<RenZhengMatch> getItems() {
		return items;
	}
	public void setItems(ArrayList<RenZhengMatch> items) {
		this.items = items;
	}
	ArrayList<RenZhengMatch> items=new ArrayList<RenZhengMatch>();
	public ArrayList<RenZhengMatch> getResultList() {
		return items;
	}
	public void setResultList(ArrayList<RenZhengMatch> resultList) {
		this.items = resultList;
	} ;//投注内容	
	public BigDecimal getCopiedSchemePrize() {
		return copiedSchemePrize;
	}
	public void setCopiedSchemePrize(BigDecimal copiedSchemePrize) {
		this.copiedSchemePrize = copiedSchemePrize;
	}
	public String getFirstMatchTime() {
		return firstMatchTime;
	}
	public void setFirstMatchTime(String firstMatchTime) {
		this.firstMatchTime = firstMatchTime;
	}
	public String getPassTypeStr() {
		return passTypeStr;
	}
	public void setPassTypeStr(String passTypeStr) {
		this.passTypeStr = passTypeStr;
	}
	public String getRecommendTitle() {
		return recommendTitle;
	}
	public void setRecommendTitle(String recommendTitle) {
		this.recommendTitle = recommendTitle;
	}
	public String getRecommendContent() {
		return recommendContent;
	}
	public void setRecommendContent(String recommendContent) {
		this.recommendContent = recommendContent;
	}
	public String getSchemeBackupsId() {
		return schemeBackupsId;
	}
	public void setSchemeBackupsId(String schemeBackupsId) {
		this.schemeBackupsId = schemeBackupsId;
	}
	public String getSchemeCost() {
		return schemeCost;
	}
	public void setSchemeCost(String schemeCost) {
		this.schemeCost = schemeCost;
	}
	public String getSponsorId() {
		return sponsorId;
	}
	public void setSponsorId(String sponsorId) {
		this.sponsorId = sponsorId;
	}
	public String getSponsorName() {
		return sponsorName;
	}
	public void setSponsorName(String sponsorName) {
		this.sponsorName = sponsorName;
	}
	public String getSponsorUserLevelNew() {
		return sponsorUserLevelNew;
	}
	public void setSponsorUserLevelNew(String sponsorUserLevelNew) {
		this.sponsorUserLevelNew = sponsorUserLevelNew;
	}
	public String getSponsorUserPic() {
		return sponsorUserPic;
	}
	public void setSponsorUserPic(String sponsorUserPic) {
		this.sponsorUserPic = sponsorUserPic;
	}
	public int getSponsorFormNumber() {
		return sponsorFormNumber;
	}
	public void setSponsorFormNumber(int sponsorFormNumber) {
		this.sponsorFormNumber = sponsorFormNumber;
	}
	
}
