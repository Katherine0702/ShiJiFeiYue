package com.cshen.tiyu.domain.ball;

import java.util.List;

public class SchemeInfoDTO {
	private String bet;
	private String passTypeText;
	
	/** 胆码最小命中数 */
	private Integer danMinHit;

	/** 胆码最大命中数 */
	private Integer danMaxHit;
	
	/** 选择的场次内容 */
	private List<SchemeMatchDTO> items;

	public String getBet() {
		return bet;
	}

	public void setBet(String bet) {
		this.bet = bet;
	}

	public String getPassTypeText() {
		return passTypeText;
	}

	public void setPassTypeText(String passTypeText) {
		this.passTypeText = passTypeText;
	}

	public Integer getDanMinHit() {
		return danMinHit;
	}

	public void setDanMinHit(Integer danMinHit) {
		this.danMinHit = danMinHit;
	}

	public Integer getDanMaxHit() {
		return danMaxHit;
	}

	public void setDanMaxHit(Integer danMaxHit) {
		this.danMaxHit = danMaxHit;
	}

	public List<SchemeMatchDTO> getItems() {
		return items;
	}

	public void setItems(List<SchemeMatchDTO> items) {
		this.items = items;
	}
	
	
}
