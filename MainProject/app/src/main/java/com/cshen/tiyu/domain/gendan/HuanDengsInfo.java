package com.cshen.tiyu.domain.gendan;

import org.xutils.db.annotation.Column;

public class HuanDengsInfo {
	public int _id;  
	long id;
	private String title;
	private String icon;
	private int rank;	
	private String url;
	private int status;	
	String useLocal;//1调用本地
	String lotteryId;//彩种id
	private String playType;
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getUseLocal() {
		return useLocal;
	}
	public void setUseLocal(String useLocal) {
		this.useLocal = useLocal;
	}
	public String getLotteryId() {
		return lotteryId;
	}
	public void setLotteryId(String lotteryId) {
		this.lotteryId = lotteryId;
	}
	public String getPlayType() {
		return playType;
	}
	public void setPlayType(String playType) {
		this.playType = playType;
	}
	
}
