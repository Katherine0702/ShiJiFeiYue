package com.cshen.tiyu.domain.gendan;


public class MyRecommendDetil {
	String prizeAfterTax;//税后奖金0	
	String schemeCost;//推荐单投注金额	BigDecimal
	String sponsorName;//发起人用户名	
	String sponsorUserPic;//发起人用户头像	
	int schemeStateInt;//附录- CopySchemeDTOSchemeState
	String schemeState;//附录- CopySchemeDTOSchemeState
	public String getPrizeAfterTax() {
		return prizeAfterTax;
	}
	public void setPrizeAfterTax(String prizeAfterTax) {
		this.prizeAfterTax = prizeAfterTax;
	}
	public String getSchemeCost() {
		return schemeCost;
	}
	public void setSchemeCost(String schemeCost) {
		this.schemeCost = schemeCost;
	}
	public String getSponsorName() {
		return sponsorName;
	}
	public void setSponsorName(String sponsorName) {
		this.sponsorName = sponsorName;
	}
	public String getSponsorUserPic() {
		return sponsorUserPic;
	}
	public void setSponsorUserPic(String sponsorUserPic) {
		this.sponsorUserPic = sponsorUserPic;
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
