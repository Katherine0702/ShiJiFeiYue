package com.cshen.tiyu.domain.dltssq;

import java.io.Serializable;
import java.util.ArrayList;

public class NumberEach implements Serializable{
    private static final long serialVersionUID = -7060210544600464481L;   
	int type;//0大乐透前区数字；1大乐透后区数字
    ArrayList<Integer> numbers = new ArrayList<>();//选中的数字
    public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public ArrayList<Integer> getNumbers() {
		return numbers;
	}
	public void setNumbers(ArrayList<Integer> numbers) {
		this.numbers = numbers;
	}
}
