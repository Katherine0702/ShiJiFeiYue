package com.cshen.tiyu.domain.information;

public class InformationDetail{
	long id;
	String content;
	String sign;
	String signTime;
	String modifiedTime;
	String titleDetail;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getModifiedTime() {
		return modifiedTime;
	}
	
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getSignTime() {
		return signTime;
	}
	public void setSignTime(String signTime) {
		this.signTime = signTime;
	}
	public void setModifiedTime(String modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	public String getTitleDetail() {
		return titleDetail;
	}
	public void setTitleDetail(String titleDetail) {
		this.titleDetail = titleDetail;
	}

}
