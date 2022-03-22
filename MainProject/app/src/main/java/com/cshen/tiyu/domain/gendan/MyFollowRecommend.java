package com.cshen.tiyu.domain.gendan;

import java.math.BigDecimal;
import java.util.ArrayList;


public class MyFollowRecommend{
	private static final long serialVersionUID = -7060210544600464481L; 
	String	copiedSchemePrize;//跟投金额	BigDecimal
	int	multiple = 1;//倍数
	String firstMatchTime;//截止时间	
	String passTypeStr;//玩法	
	String recommendTitle;//推荐标题	
	String recommendContent;//推荐语	
	String schemeBackupsId;//推荐单Id
	String sourceSchemeId;  //对应订单Id
	String schemeCost;//推荐单投注金额	BigDecimal
	String copiedUserId;//发起人Id	
	String copiedUserName;//发起人用户名	
	String copiedUserLevelNew;//发起人用户名等级	
	String copiedUserPic;//发起人用户头像	
	String copyParentSchemeId;//发起人单Id	
	ArrayList<RenZhengMatch> items=new ArrayList<RenZhengMatch>();
	boolean canCopy;//是否可以跟单
	/*	WAIT	0	待开奖
		WON1	1     已中奖
		LOST	2	未中奖*/
	int schemeStateInt;
	String 	schemeState	;
	private String createTime;//创建时间

	public int getMultiple() {
		return multiple;
	}
	public void setMultiple(int multiple) {
		this.multiple = multiple;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getSourceSchemeId() {
		return sourceSchemeId;
	}
	public void setSourceSchemeId(String sourceSchemeId) {
		this.sourceSchemeId = sourceSchemeId;
	}
	public String getCopiedSchemePrize() {
		return copiedSchemePrize;
	}
	public void setCopiedSchemePrize(String copiedSchemePrize) {
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
	public String getCopiedUserId() {
		return copiedUserId;
	}
	public void setCopiedUserId(String copiedUserId) {
		this.copiedUserId = copiedUserId;
	}
	public String getCopiedUserName() {
		return copiedUserName;
	}
	public void setCopiedUserName(String copiedUserName) {
		this.copiedUserName = copiedUserName;
	}
	public String getCopiedUserLevelNew() {
		return copiedUserLevelNew;
	}
	public void setCopiedUserLevelNew(String copiedUserLevelNew) {
		this.copiedUserLevelNew = copiedUserLevelNew;
	}
	public String getCopiedUserPic() {
		return copiedUserPic;
	}
	public void setCopiedUserPic(String copiedUserPic) {
		this.copiedUserPic = copiedUserPic;
	}
	public String getCopyParentSchemeId() {
		return copyParentSchemeId;
	}
	public void setCopyParentSchemeId(String copyParentSchemeId) {
		this.copyParentSchemeId = copyParentSchemeId;
	}
	public ArrayList<RenZhengMatch> getItems() {
		return items;
	}
	public void setItems(ArrayList<RenZhengMatch> items) {
		this.items = items;
	}
	public boolean isCanCopy() {
		return canCopy;
	}
	public void setCanCopy(boolean canCopy) {
		this.canCopy = canCopy;
	}
	public int getSchemeStateInt() {
		return schemeStateInt;
	}
	public void setSchemeStateInt(int schemeStateInt) {
		this.schemeStateInt = schemeStateInt;
	}
	public String getSchemeState() {
		return schemeState;
	}
	public void setSchemeState(String schemeState) {
		this.schemeState = schemeState;
	}

}
