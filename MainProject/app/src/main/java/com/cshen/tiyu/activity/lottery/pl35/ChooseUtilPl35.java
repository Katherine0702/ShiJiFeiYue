package com.cshen.tiyu.activity.lottery.pl35;



import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import android.text.Html;
import android.text.Spanned;

import com.cshen.tiyu.domain.NumberPl35;


public class ChooseUtilPl35 {
	public  static ChooseUtilPl35  chooseUtil;
	public  static ChooseUtilPl35 getChooseUtil(){
		if(chooseUtil == null){
			chooseUtil = new ChooseUtilPl35();
		}
		return chooseUtil;
	}
	public NumberPl35 getRandomPl5(){
		NumberPl35 numberpl5 = new NumberPl35();
		Random random=new Random();
		numberpl5.getWan().add(random.nextInt(9));	
		numberpl5.getQian().add(random.nextInt(9));	
		numberpl5.getBai().add(random.nextInt(9));	
		numberpl5.getShi().add(random.nextInt(9));	
		numberpl5.getGe().add(random.nextInt(9));	
		getSortListPl5(numberpl5);
		numberpl5.setNum(getPl35Time(numberpl5));
		return numberpl5;
	}
	@SuppressWarnings("unchecked")
	public NumberPl35 getSortListPl5(NumberPl35 numbers){
		Collections.sort(numbers.getWan(), new SortByName()); 
		Collections.sort(numbers.getQian(), new SortByName());
		Collections.sort(numbers.getBai(), new SortByName());
		Collections.sort(numbers.getShi(), new SortByName());
		Collections.sort(numbers.getGe(), new SortByName());
		return numbers;
	}
	@SuppressWarnings("rawtypes")
	class SortByName implements Comparator {
		@Override
		public int compare(Object o1, Object o2) {
			Integer s1 = (Integer) o1;
			Integer s2 = (Integer) o2;
			if (s1 > s2)
				return 1;
			return -1;
		}
	}
	public int getPl35Time(NumberPl35 number){
		return number.getWan().size()*
				number.getQian().size()*
				number.getBai().size()*
				number.getShi().size()*
				number.getGe().size();
	}
	public Spanned maymoney(NumberPl35 number){
		String text, prizeDetail;
		int max = 100000;
		int benjin = number.getNum()*2;
		int afterPrize = max - benjin;
		if (afterPrize < 0) {
			text = "亏损";
			afterPrize = Math.abs(afterPrize);
		} else {
			text = "盈利";
		}
		prizeDetail = "<html><font color=\"#666666\">如中奖，奖金" 
				+"</font><font color=\"#666666\">"
				+ max+ "</font><font color=\"#666666\">元，" 
				+ text 
				+"</font><font color=\"#4fb07c\">"
				+ afterPrize
				+ "</font><font color=\"#666666\">元</font></html>";
		return Html.fromHtml(prizeDetail);
	}
}
