package com.cshen.tiyu.domain.cai115;


import java.util.List;


public class El11to5CompoundContent {
	/** 注数 */
	private Integer units;

	/** 除选二直选，前三直选外投注号码 */
	private List<String> betList;
	/**胆码,选二直选，前三直选,任选一没有胆拖功能**/
	private List<String> betDanList;

	/** 选二直选(前二)，前三直选投注号码 */
	private List<String> bet1List;
	private List<String> bet2List;
	private List<String> bet3List;
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
	public List<String> getBet1List() {
		return bet1List;
	}
	public void setBet1List(List<String> bet1List) {
		this.bet1List = bet1List;
	}
	public List<String> getBet2List() {
		return bet2List;
	}
	public void setBet2List(List<String> bet2List) {
		this.bet2List = bet2List;
	}
	public List<String> getBet3List() {
		return bet3List;
	}
	public void setBet3List(List<String> bet3List) {
		this.bet3List = bet3List;
	}



}
