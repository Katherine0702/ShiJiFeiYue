package com.cshen.tiyu.activity.mian4.find.message;

public class GridItemsBean {
	
	private int iconRes;
	private String title;
	private String info;
	
	
	public GridItemsBean(int iconRes, String title, String info) {
		super();
		this.iconRes = iconRes;
		this.title = title;
		this.info = info;
	}
	public int getIconRes() {
		return iconRes;
	}
	public void setIconRes(int iconRes) {
		this.iconRes = iconRes;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	

}
