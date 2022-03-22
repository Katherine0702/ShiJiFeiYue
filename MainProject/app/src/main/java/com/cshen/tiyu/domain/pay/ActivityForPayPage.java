package com.cshen.tiyu.domain.pay;


public class ActivityForPayPage  {
	private Long id;
	private Integer pay_page_lotteryId;

	/** 活动图片地址 */
	private String imageURL;

	/** 活动链接地址 */
	private String url;

	private Byte useLocal;
	/** 对应原生彩种Id */
	private String lotteryId;
	private String playType;
	public String getPlayType() {
	return playType;
    }
    public void setPlayType(String playType) {
	this.playType = playType;
    }
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getPay_page_lotteryId() {
		return pay_page_lotteryId;
	}
	public void setPay_page_lotteryId(Integer pay_page_lotteryId) {
		this.pay_page_lotteryId = pay_page_lotteryId;
	}
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Byte getUseLocal() {
		return useLocal;
	}
	public void setUseLocal(Byte useLocal) {
		this.useLocal = useLocal;
	}
	public String getLotteryId() {
		return lotteryId;
	}
	public void setLotteryId(String lotteryId) {
		this.lotteryId = lotteryId;
	}



}
