package com.cshen.tiyu.domain.main;

/**
 * 首页各种弹窗
 * @author Administrator
 *
 */
public class HomePpwBean {
	private String pushPay;//是否弹出充二十送二十提示框	  0 ： 不弹  1：弹
	private String pushBack;//是否弹出回归用户回归礼包提示框 0 ： 不弹  1：弹  
	private String pushWin;//是否弹出中奖提示框	0 ： 不弹  1：弹  
	private String processId ;
	private String sourceSchemeId;
	private String lotteryType ;
	private String playType ;
	private String msg ;
	private String prize ;
	
	
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getPrize() {
		return prize;
	}
	public void setPrize(String prize) {
		this.prize = prize;
	}
	public String getSourceSchemeId() {
		return sourceSchemeId;
	}
	public void setSourceSchemeId(String sourceSchemeId) {
		this.sourceSchemeId = sourceSchemeId;
	}
	public String getLotteryType() {
		return lotteryType;
	}
	public void setLotteryType(String lotteryType) {
		this.lotteryType = lotteryType;
	}
	public String getPlayType() {
		return playType;
	}
	public void setPlayType(String playType) {
		this.playType = playType;
	}
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public String getPushPay() {
		return pushPay;
	}
	public void setPushPay(String pushPay) {
		this.pushPay = pushPay;
	}
	public String getPushBack() {
		return pushBack;
	}
	public void setPushBack(String pushBack) {
		this.pushBack = pushBack;
	}
	public String getPushWin() {
		return pushWin;
	}
	public void setPushWin(String pushWin) {
		this.pushWin = pushWin;
	}
	

}
