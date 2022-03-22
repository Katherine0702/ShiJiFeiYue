package com.cshen.tiyu.domain;

import java.math.BigDecimal;
import java.util.Date;


public class Scheme {
	/* ---------------------- 属性 ----------------------- */

	private Long id;
	/** 期编号 */
	private Long periodId;

	/** 期号 */
	private String periodNumber;

	/** 方案发起人的用户编号 */
	private Long sponsorId;

	/** 方案发起人的用户名 */
	private String sponsorName;

	/** 方案描述 */        
	private String description;

	/** 方案内容是否已上传 */
	private boolean uploaded;

	/** 方案内容上传时间 */
	private Date uploadTime;

	/** 方案注数（单倍注数） */
	private Integer units;

	/** 倍数 */
	private Integer multiple;

	/** 方案金额 */
	protected Integer schemeCost;

	/** 方案进度 */
	private Float progressRate;

	/** 已认购金额 */
	private BigDecimal subscribedCost;

	/** 保底金额 */
	private BigDecimal baodiCost;

	/** 方案内容 */
	private String content;

	/**
	 * 方案投注的方式
	 * 
	 * @see com.cai310.lottery.common.SalesMode
	 */
	private String mode;

	/**
	 * 方案分享类型
	 * 
	 * @see com.cai310.lottery.common.ShareType
	 */
	private String shareType;

	/**
	 * 方案保密类型
	 * 
	 * @see com.cai310.lottery.common.SecretType
	 */
	private String secretType;

	/**
	 * 方案状态
	 * 
	 * @see com.cai310.lottery.common.SchemeState
	 */
	private String state;

	/**
	 * 认购许可类型
	 * 
	 * @see com.cai310.lottery.common.SubscriptionLicenseType
	 */
	private String subscriptionLicenseType;

	/** 认购密码 */
	private String subscriptionPassword;

	/** 最小认购金额 */
	private BigDecimal minSubscriptionCost;

	/** 自动跟单是否已完成,如果没有跟单,设为true */
	private boolean autoFollowCompleted;

	/** 方案备注 */
	private String remark;

	/**
	 * 方案中奖更新状态
	 * 
	 * @see com.cai310.lottery.common.WinningUpdateStatus
	 */
	private String winningUpdateStatus;

	/** 是否中奖 */
	private boolean won;

	/** 中奖详情 */
	private String wonDetail;

	/** 奖金是否已派发 */
	private boolean prizeSended;

	/** 奖金详情 */
	private String prizeDetail;

	/** 税前奖金 */
	private BigDecimal prize;

	/** 税后奖金 */
	private BigDecimal prizeAfterTax;

	/** 版本号,用于实现乐观锁 */
	private Integer version;

	/** 方案发起人的佣金率，值应小于1 */
	private Float commissionRate;

	/** 方案是否已发送去打印 */
	private boolean sendToPrint;

	/** 创建时间 */
	private Date createTime;

	/** 最后更新时间 */
	private Date lastModifyTime;

	/** 是否保留(当方案被保留，执行保底、完成交易、赠送积分、更新中奖等等操作时，该方案将不会被执行) */
	private boolean reserved;

	/** 交易ID */
	private Long transactionId;

	/** 完成交易时间 --方案发起的是时候为结束出票时间*/
	private Date commitTime;

	/** 派奖时间 */
	private Date prizeSendTime;

	/** 方案出票状态 */
	private String schemePrintState;


	private Byte orderPriority ;
	
	/** 接票ID(订单号)*/
	private String orderId;
	/** 方案来源*/
	private String platform;
	/**
	 * 接票非数据字段
	 */
    private String ticketSchemeState;
    
    /**
	 * 销售统计状态
	 */
    private String saleAnalyseState ;
    
    /**
   	 * 佣金统计状态
   	 */
    private String agentAnalyseState ;
    
    /**用户中奖排行统计，保存当前统计日期(20131125)*/
    private Integer rankFlag;
    
    /**用户战绩统计*/
    private boolean gradeFlag;

    private String hongbaoId;
    
	/** 11to5 玩法 **/
	private String betType;
	private String schemeNumber;
	private String createTimeStr;
	
	/** 页面玩法类型*/
	private String playTypeWeb;
	private Integer playTypeOrdinal;
	private boolean copyContentShow = false;//是否保密赛果
	/** 
	/* ---------------------- getter and setter method ----------------------- */

	public Long getId() {
		return id;
	}

	public boolean isCopyContentShow() {
		return copyContentShow;
	}

	public void setCopyContentShow(boolean copyContentShow) {
		this.copyContentShow = copyContentShow;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPeriodId() {
		return periodId;
	}

	public void setPeriodId(Long periodId) {
		this.periodId = periodId;
	}

	public String getPeriodNumber() {
		return periodNumber;
	}

	public void setPeriodNumber(String periodNumber) {
		this.periodNumber = periodNumber;
	}

	public Long getSponsorId() {
		return sponsorId;
	}

	public void setSponsorId(Long sponsorId) {
		this.sponsorId = sponsorId;
	}

	public String getSponsorName() {
		return sponsorName;
	}

	public void setSponsorName(String sponsorName) {
		this.sponsorName = sponsorName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isUploaded() {
		return uploaded;
	}

	public void setUploaded(boolean uploaded) {
		this.uploaded = uploaded;
	}

	public Date getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}

	public Integer getUnits() {
		return units;
	}

	public void setUnits(Integer units) {
		this.units = units;
	}

	public Integer getMultiple() {
		return multiple;
	}

	public void setMultiple(Integer multiple) {
		this.multiple = multiple;
	}

	public Integer getSchemeCost() {
		return schemeCost;
	}

	public void setSchemeCost(Integer schemeCost) {
		this.schemeCost = schemeCost;
	}

	public Float getProgressRate() {
		return progressRate;
	}

	public void setProgressRate(Float progressRate) {
		this.progressRate = progressRate;
	}

	public BigDecimal getSubscribedCost() {
		return subscribedCost;
	}

	public void setSubscribedCost(BigDecimal subscribedCost) {
		this.subscribedCost = subscribedCost;
	}

	public BigDecimal getBaodiCost() {
		return baodiCost;
	}

	public void setBaodiCost(BigDecimal baodiCost) {
		this.baodiCost = baodiCost;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getShareType() {
		return shareType;
	}

	public void setShareType(String shareType) {
		this.shareType = shareType;
	}

	public String getSecretType() {
		return secretType;
	}

	public void setSecretType(String secretType) {
		this.secretType = secretType;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getSubscriptionLicenseType() {
		return subscriptionLicenseType;
	}

	public void setSubscriptionLicenseType(String subscriptionLicenseType) {
		this.subscriptionLicenseType = subscriptionLicenseType;
	}

	public String getSubscriptionPassword() {
		return subscriptionPassword;
	}

	public void setSubscriptionPassword(String subscriptionPassword) {
		this.subscriptionPassword = subscriptionPassword;
	}

	public BigDecimal getMinSubscriptionCost() {
		return minSubscriptionCost;
	}

	public void setMinSubscriptionCost(BigDecimal minSubscriptionCost) {
		this.minSubscriptionCost = minSubscriptionCost;
	}

	public boolean isAutoFollowCompleted() {
		return autoFollowCompleted;
	}

	public void setAutoFollowCompleted(boolean autoFollowCompleted) {
		this.autoFollowCompleted = autoFollowCompleted;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getWinningUpdateStatus() {
		return winningUpdateStatus;
	}

	public void setWinningUpdateStatus(String winningUpdateStatus) {
		this.winningUpdateStatus = winningUpdateStatus;
	}

	public boolean getWon() {
		return won;
	}

	public void setWon(boolean won) {
		this.won = won;
	}

	public String getWonDetail() {
		return wonDetail;
	}

	public void setWonDetail(String wonDetail) {
		this.wonDetail = wonDetail;
	}

	public boolean isPrizeSended() {
		return prizeSended;
	}

	public void setPrizeSended(boolean prizeSended) {
		this.prizeSended = prizeSended;
	}

	public String getPrizeDetail() {
		return prizeDetail;
	}

	public void setPrizeDetail(String prizeDetail) {
		this.prizeDetail = prizeDetail;
	}

	public BigDecimal getPrize() {
		return prize;
	}

	public void setPrize(BigDecimal prize) {
		this.prize = prize;
	}

	public BigDecimal getPrizeAfterTax() {
		return prizeAfterTax;
	}

	public void setPrizeAfterTax(BigDecimal prizeAfterTax) {
		this.prizeAfterTax = prizeAfterTax;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Float getCommissionRate() {
		return commissionRate;
	}

	public void setCommissionRate(Float commissionRate) {
		this.commissionRate = commissionRate;
	}

	public boolean isSendToPrint() {
		return sendToPrint;
	}

	public void setSendToPrint(boolean sendToPrint) {
		this.sendToPrint = sendToPrint;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public boolean isReserved() {
		return reserved;
	}

	public void setReserved(boolean reserved) {
		this.reserved = reserved;
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public Date getCommitTime() {
		return commitTime;
	}

	public void setCommitTime(Date commitTime) {
		this.commitTime = commitTime;
	}

	public Date getPrizeSendTime() {
		return prizeSendTime;
	}

	public void setPrizeSendTime(Date prizeSendTime) {
		this.prizeSendTime = prizeSendTime;
	}

	public String getSchemePrintState() {
		return schemePrintState;
	}

	public void setSchemePrintState(String schemePrintState) {
		this.schemePrintState = schemePrintState;
	}

	public Byte getOrderPriority() {
		return orderPriority;
	}

	public void setOrderPriority(Byte orderPriority) {
		this.orderPriority = orderPriority;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getTicketSchemeState() {
		return ticketSchemeState;
	}

	public void setTicketSchemeState(String ticketSchemeState) {
		this.ticketSchemeState = ticketSchemeState;
	}

	public String getSaleAnalyseState() {
		return saleAnalyseState;
	}

	public void setSaleAnalyseState(String saleAnalyseState) {
		this.saleAnalyseState = saleAnalyseState;
	}

	public String getAgentAnalyseState() {
		return agentAnalyseState;
	}

	public void setAgentAnalyseState(String agentAnalyseState) {
		this.agentAnalyseState = agentAnalyseState;
	}

	public Integer getRankFlag() {
		return rankFlag;
	}

	public void setRankFlag(Integer rankFlag) {
		this.rankFlag = rankFlag;
	}

	public boolean isGradeFlag() {
		return gradeFlag;
	}

	public void setGradeFlag(boolean gradeFlag) {
		this.gradeFlag = gradeFlag;
	}

	public String getHongbaoId() {
		return hongbaoId;
	}

	public void setHongbaoId(String hongbaoId) {
		this.hongbaoId = hongbaoId;
	}

	public String getBetType() {
		return betType;
	}

	public void setBetType(String betType) {
		this.betType = betType;
	}

	public String getSchemeNumber() {
		return schemeNumber;
	}

	public void setSchemeNumber(String schemeNumber) {
		this.schemeNumber = schemeNumber;
	}

	public String getCreateTimeStr() {
		return createTimeStr;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}

	public String getPlayTypeWeb() {
		return playTypeWeb;
	}

	public void setPlayTypeWeb(String playTypeWeb) {
		this.playTypeWeb = playTypeWeb;
	}

	public Integer getPlayTypeOrdinal() {
		return playTypeOrdinal;
	}

	public void setPlayTypeOrdinal(Integer playTypeOrdinal) {
		this.playTypeOrdinal = playTypeOrdinal;
	}
    
	
}
