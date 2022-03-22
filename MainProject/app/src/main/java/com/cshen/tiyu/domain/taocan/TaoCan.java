package com.cshen.tiyu.domain.taocan;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TaoCan implements Serializable{
    private static final long serialVersionUID = -7060210544600464481L; 


	private int id;
	/** 套餐标题    */
	private String title;
	/** 活动类型  精选 or 超值*/
	private String type;//0:  精选套餐；	1：超值套餐；

	/** 彩种 */
	private int lottery;
	/** 玩法类型   11选5时：有任一、任八等玩法 **/
	private int playType;//
	/** 中奖率 */
	private String win_rate;
	/** 开始时间 */
	private String startTime;
	/** 截止时间 */
	private String endTime;
	/** 期数 */
	private int periodNumber;
	/** 份数 */
	private int number;
	/** 已领份数 */
	private int user_get_number;
	/** 返额 */
	private String backMoney;
	/** 状态 1：开启 0：禁用 */
	private Byte status;

	/** 添加时间 */
	private Date createTime;
	/** 修改时间 */
	private Date modifiedTime;

	/** 针对Android or IOS 0：公用；1：Android；2：IOS */
	private Byte useFor ;
	private String imageURL;
	private int buyStatus;//1: 已领完 ，截止2:未开始3:正常 4:超时截止

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public int getBuyStatus() {
		return buyStatus;
	}

	public void setBuyStatus(int buyStatus) {
		this.buyStatus = buyStatus;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getLottery() {
		return lottery;
	}

	public void setLottery(int lottery) {
		this.lottery = lottery;
	}

	public int getPlayType() {
		return playType;
	}

	public void setPlayType(int playType) {
		this.playType = playType;
	}

	public String getWin_rate() {
		return win_rate;
	}

	public void setWin_rate(String win_rate) {
		this.win_rate = win_rate;
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

	public int getPeriodNumber() {
		return periodNumber;
	}

	public void setPeriodNumber(int periodNumber) {
		this.periodNumber = periodNumber;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getUser_get_number() {
		return user_get_number;
	}

	public void setUser_get_number(int user_get_number) {
		this.user_get_number = user_get_number;
	}

	public String getBackMoney() {
		return backMoney;
	}

	public void setBackMoney(String backMoney) {
		this.backMoney = backMoney;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public Byte getUseFor() {
		return useFor;
	}

	public void setUseFor(Byte useFor) {
		this.useFor = useFor;
	}

}
