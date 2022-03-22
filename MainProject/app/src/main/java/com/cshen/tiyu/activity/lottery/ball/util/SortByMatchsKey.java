package com.cshen.tiyu.activity.lottery.ball.util;

import java.util.Comparator;

import com.cshen.tiyu.domain.ball.Match;

public class SortByMatchsKey implements Comparator {
	@Override
	public int compare(Object o1, Object o2) {
		Match m1 = (Match) o1;
		Match m2 = (Match) o2;
		long s1 = Long.parseLong(m1.getMatchKey().substring(0, 8));
		long s2 = Long.parseLong(m2.getMatchKey().substring(0, 8));
		if (s1 > s2){
			return 1;
		}else if (s1 < s2){
			return -1;
		}else{
			long s3 = Long.parseLong(m1.getMatchKey().substring(9,m1.getMatchKey().length()));
			long s4 = Long.parseLong(m2.getMatchKey().substring(9,m2.getMatchKey().length()));
			if (s3 > s4){
				return 1;
			}else{
				return -1;
			}	

		}		
	}

}
