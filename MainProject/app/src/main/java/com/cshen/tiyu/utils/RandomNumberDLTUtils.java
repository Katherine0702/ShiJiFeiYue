package com.cshen.tiyu.utils;

import java.util.Arrays;
import java.util.Random;



public class RandomNumberDLTUtils {
	
	public static int[] getRandomNumberDLTInt() {
		int[] randomNumber_f = getRandomInt(5, 35);
		if (randomNumber_f!=null) {
			Arrays.sort(randomNumber_f);
		}
		int[] randomNumber_b = getRandomInt(2, 12);
		if (randomNumber_b!=null) {
			Arrays.sort(randomNumber_b);
		}
		int[] randomNumber_result = new int[7];

		for (int i = 0; i < randomNumber_f.length; i++) {
			randomNumber_result[i]=randomNumber_f[i];
		}
		for (int i = 0; i < randomNumber_b.length; i++) {
			randomNumber_result[i+5]=randomNumber_b[i];
		}
		return randomNumber_result;
	}
	public static String[] getRandomNumberDLTString() {
		int[] randomNumber_f = getRandomInt(5, 35);
		int[] randomNumber_b = getRandomInt(2, 12);
		String[]  randomNumber_result=new String[7];
		for (int i = 0; i < randomNumber_f.length; i++) {
			if (randomNumber_f[i]<10) {
				randomNumber_result[i]="0"+randomNumber_f[i];
			}else{
				randomNumber_result[i]=""+randomNumber_f[i];
			}
		}
		for (int i = 0; i < randomNumber_b.length; i++) {
			if (randomNumber_b[i]<10) {
				randomNumber_result[i+5]="0"+randomNumber_b[i];
			}else {
				randomNumber_result[i+5]=""+randomNumber_b[i];
			}
		}
		return randomNumber_result;
	}

	



	private static int[] getRandomInt(int number, int maxValue) {
		if (number != 0 && maxValue != 0) {
			Random random = new Random();
			int[] result = new int[number];
			for (int i = 0; i < result.length; i++) {
				boolean temp=true;
				int num = random.nextInt(maxValue);
				if (num==0 ) {
					temp=false;
				}
				else{
					for (int j = 0; j < i; j++) {
						if (num == result[j] ) {
							temp=false;
						}
					}
				}
				if (temp) {
					result[i] = num;
				}else{
					i--;
				}
				
			}

			return result;
		}
		return null;
	}

}
