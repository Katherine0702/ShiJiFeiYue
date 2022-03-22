package com.cshen.tiyu.activity.lottery.ball.util;

import java.util.Comparator;

public class SortByKey implements Comparator {
	@Override
	public int compare(Object o1, Object o2) {
		String s1 = (String) o1;
		String s2 = (String) o2;
		if(s1.contains("+")
				&&s2.contains("+")){
			String[] keys1 = s1.split("\\+");
			String[] keys2 = s2.split("\\+");
			int montherKey1 = Integer.parseInt(keys1[0]);
			int montherK2y2 = Integer.parseInt(keys2[0]);
			if (montherKey1 > montherK2y2){
				return 1;
			}else if (montherKey1 < montherK2y2){
				return -1;
			}else{
				int childKey1 = Integer.parseInt(keys1[1]);
				int childKey2 = Integer.parseInt(keys2[1]);
				if (childKey1 > childKey2)
					return 1;
				return -1;
			}
		}
		return 0;
	}

}
