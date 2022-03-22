package com.cshen.tiyu.domain.ball;

import java.io.Serializable;
import java.util.ArrayList;

public class JCLQJJYHTOUZHU implements Serializable{
    private static final long serialVersionUID = -7060210544600464481L;  
	boolean optimize;
	String bestMaxPrize;
	String bestMinPrize;
	String content;
	ArrayList<JclqJJYHChoosedScroeBean> items = new ArrayList<JclqJJYHChoosedScroeBean>();
	ArrayList<String> matchkeys;
	ArrayList<Integer> multiples;
	ArrayList<String> playTypes;
	public boolean isOptimize() {
		return optimize;
	}
	public void setOptimize(boolean optimize) {
		this.optimize = optimize;
	}
	public String getBestMaxPrize() {
		return bestMaxPrize;
	}
	public void setBestMaxPrize(String bestMaxPrize) {
		this.bestMaxPrize = bestMaxPrize;
	}
	public String getBestMinPrize() {
		return bestMinPrize;
	}
	public void setBestMinPrize(String bestMinPrize) {
		this.bestMinPrize = bestMinPrize;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public ArrayList<String> getMatchkeys() {
		return matchkeys;
	}
	public void setMatchkeys(ArrayList<String> matchkeys) {
		this.matchkeys = matchkeys;
	}
	public ArrayList<Integer> getMultiples() {
		return multiples;
	}
	public void setMultiples(ArrayList<Integer> multiples) {
		this.multiples = multiples;
	}
	public ArrayList<String> getPlayTypes() {
		return playTypes;
	}
	public void setPlayTypes(ArrayList<String> playTypes) {
		this.playTypes = playTypes;
	}
	public ArrayList<JclqJJYHChoosedScroeBean> getItems() {
		return items;
	}
	public void setItems(ArrayList<JclqJJYHChoosedScroeBean> items) {
		this.items = items;
	}

}
