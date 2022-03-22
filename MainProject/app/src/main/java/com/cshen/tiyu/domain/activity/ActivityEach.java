package com.cshen.tiyu.domain.activity;

public class ActivityEach {
	String createTimeStr;//活动创建时间
	private String title;//图片名字
	private String imageURL;//图片url
	private String content;//活动内容
	private String url;//点击详情url
	private String startTime;//活动开始时间
	private String endTime;//活动结束时间
	private int state;//状态  1：开启   ；  0：禁止
	private int stillTop;//状态  1：开启   ；  0：禁止
	private String useLocal;//1调用本地
	private String lotteryId;//彩种id
	private String playType;
	public String getPlayType() {
		return playType;
	}
	public void setPlayType(String playType) {
		this.playType = playType;
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
	public String getCreateTimeStr() {
		return createTimeStr;
	}
	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getStillTop() {
		return stillTop;
	}
	public void setStillTop(int stillTop) {
		this.stillTop = stillTop;
	}

}
