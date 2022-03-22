package com.cshen.tiyu.domain.ball;



/**
 * 足球数据传输对象
 * 
 */
public class ZcMatchResultDTO {
	/** 1场次编号 */
	private String matchKey;

	/** 1联赛名称 */
	private String gameName;

	/** 1联赛颜色 */
	private String gameColor;

	/** 1比赛时间 */
	private String matchTime;
	
	/** 1主队名称 */
	private String homeTeamName;

	/** 1客队名称 */
	private String guestTeamName;

	/** 1比赛是否已经结束 */
	private boolean ended;

	/** 1比赛是否取消 */
	private boolean cancel;
	
	/** 1让球或者总分 */
	private Float handicap;
	/**上半场主队比分*/
	private Integer homeHalfScore;
	/**上半场客队比分*/
	private Integer guestHalfScore;
	/**全场主队比分*/
	private Integer homeScore;
	/**全场客队比分*/
	private Integer guestScore;
	/** 比赛结果 */
	private String result;
	
	private String resultSp;
	
	private Float totalScore;

	
	/***篮球专用***/
	private float dxfResultSp;
	private String guestTeamGuangdongName;
	private String homeTeamGuangdongName;
	private int openFlag;   
	private float rfsfResultSp;
	private String sfResult;
	private float sfResultSp;
    private String sfcResult;
    private float sfcResultSp;
    private String shortName;
    private float singleHandicap;
    private float singleTotalScore;
    private boolean stop;
	public float getDxfResultSp() {
		return dxfResultSp;
	}
	public void setDxfResultSp(float dxfResultSp) {
		this.dxfResultSp = dxfResultSp;
	}

	public String getGuestTeamGuangdongName() {
		return guestTeamGuangdongName;
	}

	public void setGuestTeamGuangdongName(String guestTeamGuangdongName) {
		this.guestTeamGuangdongName = guestTeamGuangdongName;
	}

	public String getHomeTeamGuangdongName() {
		return homeTeamGuangdongName;
	}

	public void setHomeTeamGuangdongName(String homeTeamGuangdongName) {
		this.homeTeamGuangdongName = homeTeamGuangdongName;
	}

	public int getOpenFlag() {
		return openFlag;
	}

	public void setOpenFlag(int openFlag) {
		this.openFlag = openFlag;
	}

	public float getRfsfResultSp() {
		return rfsfResultSp;
	}

	public void setRfsfResultSp(float rfsfResultSp) {
		this.rfsfResultSp = rfsfResultSp;
	}

	public String getSfResult() {
		return sfResult;
	}

	public void setSfResult(String sfResult) {
		this.sfResult = sfResult;
	}

	public float getSfResultSp() {
		return sfResultSp;
	}

	public void setSfResultSp(float sfResultSp) {
		this.sfResultSp = sfResultSp;
	}

	public String getSfcResult() {
		return sfcResult;
	}

	public void setSfcResult(String sfcResult) {
		this.sfcResult = sfcResult;
	}

	public float getSfcResultSp() {
		return sfcResultSp;
	}

	public void setSfcResultSp(float sfcResultSp) {
		this.sfcResultSp = sfcResultSp;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public float getSingleHandicap() {
		return singleHandicap;
	}

	public void setSingleHandicap(float singleHandicap) {
		this.singleHandicap = singleHandicap;
	}

	public float getSingleTotalScore() {
		return singleTotalScore;
	}

	public void setSingleTotalScore(float singleTotalScore) {
		this.singleTotalScore = singleTotalScore;
	}

	public boolean isStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}
	public String getMatchKey() {
		return matchKey;
	}

	public void setMatchKey(String matchKey) {
		this.matchKey = matchKey;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getGameColor() {
		return gameColor;
	}

	public void setGameColor(String gameColor) {
		this.gameColor = gameColor;
	}

	public String getMatchTime() {
		return matchTime;
	}

	public void setMatchTime(String matchTime) {
		this.matchTime = matchTime;
	}

	public String getHomeTeamName() {
		return homeTeamName;
	}

	public void setHomeTeamName(String homeTeamName) {
		this.homeTeamName = homeTeamName;
	}

	public String getGuestTeamName() {
		return guestTeamName;
	}

	public void setGuestTeamName(String guestTeamName) {
		this.guestTeamName = guestTeamName;
	}

	public boolean isEnded() {
		return ended;
	}

	public void setEnded(boolean ended) {
		this.ended = ended;
	}

	public boolean isCancel() {
		return cancel;
	}

	public void setCancel(boolean cancel) {
		this.cancel = cancel;
	}

	public Float getHandicap() {
		return handicap;
	}

	public void setHandicap(Float handicap) {
		this.handicap = handicap;
	}

	public Integer getHomeHalfScore() {
		return homeHalfScore;
	}

	public void setHomeHalfScore(Integer homeHalfScore) {
		this.homeHalfScore = homeHalfScore;
	}

	public Integer getGuestHalfScore() {
		return guestHalfScore;
	}

	public void setGuestHalfScore(Integer guestHalfScore) {
		this.guestHalfScore = guestHalfScore;
	}

	public Integer getHomeScore() {
		return homeScore;
	}

	public void setHomeScore(Integer homeScore) {
		this.homeScore = homeScore;
	}

	public Integer getGuestScore() {
		return guestScore;
	}

	public void setGuestScore(Integer guestScore) {
		this.guestScore = guestScore;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	

	public Float getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Float totalScore) {
		this.totalScore = totalScore;
	}

	public String getResultSp() {
		return resultSp;
	}

	public void setResultSp(String resultSp) {
		this.resultSp = resultSp;
	}

	@Override
	public String toString() {
		return "ZcMatchResultDTO [matchKey=" + matchKey + ", gameName="
				+ gameName + ", gameColor=" + gameColor + ", matchTime="
				+ matchTime + ", homeTeamName=" + homeTeamName
				+ ", guestTeamName=" + guestTeamName + ", ended=" + ended
				+ ", cancel=" + cancel + ", handicap=" + handicap
				+ ", homeHalfScore=" + homeHalfScore + ", guestHalfScore="
				+ guestHalfScore + ", homeScore=" + homeScore + ", guestScore="
				+ guestScore + ", result=" + result + ", resultSp=" + resultSp
				+ ", totalScore=" + totalScore + "]";
	}

	
}
