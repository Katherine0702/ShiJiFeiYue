package com.cshen.tiyu.activity.lottery.ball.util;

import java.util.Comparator;

import com.cshen.tiyu.domain.ball.PassTypeEach;

public class SortByChuanName implements Comparator {
	@Override
	public int compare(Object o1, Object o2) {
		int s1 = 0,s2=0;
		try{
			s1 = Integer.parseInt(((PassTypeEach)o1).getText().charAt(0)+"");
		}catch(Exception e){
			e.printStackTrace();
			s1 = 1;
		}
		try{
			s2 = Integer.parseInt(((PassTypeEach)o2).getText().charAt(0)+"");
		}catch(Exception e){
			s2 = 1;
		}
		if (s1 > s2)
			return 1;
		return -1;
	}

}
