package com.cshen.tiyu.domain.fast3;

import java.util.List;

public class Fast3CompoundContent {

	/** 注数 */
	private Integer units;

	private List<String> betList;
	/**胆码**/
	private List<String> betDanList;

	/**二同号中的不同号 */
	private List<String> disList;
	public Integer getUnits() {
		return units;
	}
	public void setUnits(Integer units) {
		this.units = units;
	}
	public List<String> getBetList() {
		return betList;
	}
	public void setBetList(List<String> betList) {
		this.betList = betList;
	}
	public List<String> getBetDanList() {
		return betDanList;
	}
	public void setBetDanList(List<String> betDanList) {
		this.betDanList = betDanList;
	}
	public List<String> getDisList() {
		return disList;
	}
	public void setDisList(List<String> disList) {
		this.disList = disList;
	}



}
