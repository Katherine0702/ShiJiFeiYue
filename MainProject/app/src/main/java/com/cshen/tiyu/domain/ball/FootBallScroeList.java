package com.cshen.tiyu.domain.ball;

import java.io.Serializable;
import java.util.ArrayList;


public class FootBallScroeList implements Serializable{
	private static final long serialVersionUID = -7060210544600464481L; 

	long id;
	int chooseBF = 0;
	ScroeBiFen BF;//比分
	int chooseJQS = 0;
	ScroeIn JQS;//进球数
	int chooseBQQ = 0;
	ScroeBQC BQQ;//半全场
	ScroePeilv SPF;//胜平负
	ScroePeilv RQSPF;//让球胜平负

	public int getChooseBF() {
		return chooseBF;
	}


	public void setChooseBF(int chooseBF) {
		this.chooseBF = chooseBF;
	}
	public int getChooseJQS() {
		return chooseJQS;
	}
	public void setChooseJQS(int chooseJQS) {
		this.chooseJQS = chooseJQS;
	}
	public int getChooseBQQ() {
		return chooseBQQ;
	}
	public void setChooseBQQ(int chooseBQQ) {
		this.chooseBQQ = chooseBQQ;
	}
	public long getId() {
		return id;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public void setId(long id) {
		this.id = id;
	}
	String key;
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	ArrayList<String> checkBFNumber = new ArrayList<String>();

	public ArrayList<String> getCheckBFNumber() {
		return checkBFNumber;
	}
	public void setCheckBFNumber(ArrayList<String> checkBFNumber) {
		this.checkBFNumber = checkBFNumber;
	}
	public ScroeBiFen getBF() {
		return BF;
	}
	public void setBF(ScroeBiFen bF) {
		BF = bF;
	}
	public ScroeIn getJQS() {
		return JQS;
	}
	public void setJQS(ScroeIn jQS) {
		JQS = jQS;
	}
	public ScroeBQC getBQQ() {
		return BQQ;
	}
	public void setBQQ(ScroeBQC bQQ) {
		BQQ = bQQ;
	}
	public ScroePeilv getSPF() {
		return SPF;
	}
	public void setSPF(ScroePeilv sPF) {
		SPF = sPF;
	}
	public ScroePeilv getRQSPF() {
		return RQSPF;
	}
	public void setRQSPF(ScroePeilv rQSPF) {
		RQSPF = rQSPF;
	}
}
