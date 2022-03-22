package com.cshen.tiyu.domain.zhuihao;

import java.util.ArrayList;

import com.cshen.tiyu.domain.Back;

public class AddOrderList extends Back	{
	ArrayList<AddOrder> chaseList =new ArrayList<AddOrder>();
	public ArrayList<AddOrder> getNumberChanse() {
		return chaseList;
	}

	public void setNumberChanse(ArrayList<AddOrder> numberChanse) {
		this.chaseList = numberChanse;
	}
}
