package com.cshen.tiyu.domain.ball;

public class SchemeMatchDTO {
	/** 场次编号 */
	private String matchKey;
    
	private String matchTime;
	
	/** 主队名称 */
	private String homeTeamName;

	/** 客队名称 */
	private String guestTeamName;
	/** 联赛名称 */
	private String gameName;

	/** 联赛颜色 */
	private String gameColor;
	
	private Integer homeHalfScore;
	private Integer guestHalfScore;
	private Integer homeScore;
	private Integer guestScore;
	/** 让球或者总分 */
	private String handicap;
	
	/**混合投注玩法*/
	private Integer playType;
	
	/** 比赛结果 */
	private String result;
	
	/** 设胆 */
	private Boolean dan;
	
	/** 投注内容 */
	private String bet;
	
	/** 比赛是否已经结束 */
	private boolean ended;

	/** 比赛是否取消 */
	private boolean cancel;

	public String getMatchKey() {
		return matchKey;
	}

	public void setMatchKey(String matchKey) {
		this.matchKey = matchKey;
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

	public String getHandicap() {
		return handicap;
	}

	public void setHandicap(String handicap) {
		this.handicap = handicap;
	}

	public Integer getPlayType() {
		return playType;
	}

	public void setPlayType(Integer playType) {
		this.playType = playType;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Boolean getDan() {
		return dan;
	}

	public void setDan(Boolean dan) {
		this.dan = dan;
	}

	public String getBet() {
		return bet;
	}

	public void setBet(String bet) {
		this.bet = bet;
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

}
