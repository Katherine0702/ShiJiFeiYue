package com.cshen.tiyu.domain.gendan;

import java.util.ArrayList;

import com.cshen.tiyu.domain.Back;


public class HuanDengBack extends Back{
    ArrayList<HuanDengsInfo> adsList=new ArrayList<HuanDengsInfo>();
   public ArrayList<HuanDengsInfo> getAdsList() {
		return adsList;
	}
	public void setAdsList(ArrayList<HuanDengsInfo> adsList) {
		this.adsList = adsList;
	}
}
