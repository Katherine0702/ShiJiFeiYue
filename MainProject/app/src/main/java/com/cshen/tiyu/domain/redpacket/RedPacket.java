package com.cshen.tiyu.domain.redpacket;

import java.math.BigDecimal;
import java.sql.Date;

public class RedPacket {
	private Long id;//红包ID
	private String hongbaoName;//红包名称
	private Integer usefor;//针对彩种
	private Byte isOneTimeUsed;//是否只能使用一次 1：是  0：否
	private BigDecimal minSchemeMoney;//该红包满多少金额才能使用
	private String startTime;//开始时间
	private String availabilityTime;//有效截止时间
	private BigDecimal totalMoney;//所有金额
	private BigDecimal usedMoney;//已经使用的金额
	private int status;//-1：未激活 0：未用完 1：已用完
	private String schemeId;//订单Id 红包使用订单记录 ：用","隔开
	private String realAvailabilityDays;

	public String getRealAvailabilityDays() {
		return realAvailabilityDays;
	}
	public void setRealAvailabilityDays(String realAvailabilityDays) {
		this.realAvailabilityDays = realAvailabilityDays;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getHongbaoName() {
		return hongbaoName;
	}
	public void setHongbaoName(String hongbaoName) {
		this.hongbaoName = hongbaoName;
	}
	public Integer getUsefor() {
		return usefor;
	}
	public void setUsefor(Integer usefor) {
		this.usefor = usefor;
	}
	public Byte getIsOneTimeUsed() {
		return isOneTimeUsed;
	}
	public void setIsOneTimeUsed(Byte isOneTimeUsed) {
		this.isOneTimeUsed = isOneTimeUsed;
	}
	public BigDecimal getMinSchemeMoney() {
		return minSchemeMoney;
	}
	public void setMinSchemeMoney(BigDecimal minSchemeMoney) {
		this.minSchemeMoney = minSchemeMoney;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getAvailabilityTime() {
		return availabilityTime;
	}
	public void setAvailabilityTime(String availabilityTime) {
		this.availabilityTime = availabilityTime;
	}
	public BigDecimal getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(BigDecimal totalMoney) {
		this.totalMoney = totalMoney;
	}
	public BigDecimal getUsedMoney() {
		return usedMoney;
	}
	public void setUsedMoney(BigDecimal usedMoney) {
		this.usedMoney = usedMoney;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getSchemeId() {
		return schemeId;
	}
	public void setSchemeId(String schemeId) {
		this.schemeId = schemeId;
	}

}
