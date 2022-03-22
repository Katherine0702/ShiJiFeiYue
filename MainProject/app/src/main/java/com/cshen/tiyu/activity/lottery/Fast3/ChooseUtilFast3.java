package com.cshen.tiyu.activity.lottery.Fast3;



import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import android.text.Html;
import android.text.Spanned;

import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.domain.fast3.NumberFast;

public class ChooseUtilFast3 {
	public  static ChooseUtilFast3  chooseUtil;
	public  static ChooseUtilFast3 getChooseUtil(){
		if(chooseUtil == null){
			chooseUtil = new ChooseUtilFast3();
		}
		return chooseUtil;
	}
	public static String  choose(int WANFAINT,int DANTUO){
		String titleItem = "";
		switch (WANFAINT) {
		case ConstantsBase.HEZHI:
			titleItem = "和值";
			break;
		case ConstantsBase.SANTONGHAO:
			titleItem = "三同号单选";
			break;
		case ConstantsBase.SANTONGHAOTONG:
			titleItem = "三同号通选";
			break;
		case ConstantsBase.SANBUTONGHAO:
			if(DANTUO == 1){
				titleItem = "三不同号";
			}else{
				titleItem = "三不同号胆拖";
			}
			break;

		case ConstantsBase.SANLIANHAO:
			titleItem = "三连号通选";
			break;
		case ConstantsBase.ERTONGHAO:
			titleItem = "二同号单选";
			break;
		case ConstantsBase.ERTONGHAOFU:
			titleItem = "二同号复选";
			break;
		case ConstantsBase.ERBUTONGHAO:
			if(DANTUO == 1){
				titleItem = "二不同号";
			}else{
				titleItem = "二不同号胆拖";
			}
			break;
		}
		return titleItem;
	}
	public int jiangJin = 0;
	public Spanned maymoney(NumberFast number){
		String text, prizeDetail;
		ArrayList<Integer> prizes = new ArrayList<>();
		ArrayList<Integer> numbers = number.getNumbers();//和值/三同号、三同号通选、三不同号、三不同号连选/二同号中的同号、二同号复选、二不同号//胆
		ArrayList<Integer> number1 = number.getNumber1();//二同号中的不同号//脱
		switch (number.getPlayType()) {
		case ConstantsBase.HEZHI:
			for(int nums:numbers){
				if(nums == 4||nums == 17){
					prizes.add(80);
				}if(nums == 5||nums == 16){
					prizes.add(40);
				}if(nums == 6||nums == 15){
					prizes.add(25);
				}if(nums == 7||nums == 14){
					prizes.add(16);
				}if(nums == 8||nums == 13){
					prizes.add(12);
				}if(nums == 9||nums == 12){
					prizes.add(10);
				}if(nums == 10||nums == 11){
					prizes.add(9);
				}
			}
			break;
		case ConstantsBase.SANTONGHAO:
			prizes.add(240);
			break;
		case ConstantsBase.SANTONGHAOTONG:
			prizes.add(40);
			break;
		case ConstantsBase.SANLIANHAO:
			prizes.add(10);
			break;
		case ConstantsBase.ERTONGHAOFU:
			prizes.add(15);
			break;
		case ConstantsBase.SANBUTONGHAO:
			prizes.add(40);
			break;
		case ConstantsBase.ERBUTONGHAO:
			prizes.add(8);
			if(number.getNum() > 1){
				if(number.getMode() == 1){
					prizes.add(24);
				}else{
					prizes.add(16);
				}
			}
			break;
		case ConstantsBase.ERTONGHAO:
			prizes.add(80);
			break;
		}	
		Collections.sort(prizes);
		int min = prizes.get(0);
		int max = prizes.get(prizes.size() - 1);
		int benjin = number.getNum()*2;
		int afterPrize_1 = min - benjin;
		int afterPrize_2 = max - benjin;
		if (afterPrize_1 == afterPrize_2) {
			if (afterPrize_1 < 0) {
				text = "亏损";
				afterPrize_1 = Math.abs(afterPrize_1);
			} else {
				text = "盈利";
			}
			prizeDetail = "<html><font color=\"#ffffff\">如中奖，奖金" 
					+"</font><font color=\"#fab12f\">"
					+ min+ "</font><font color=\"#ffffff\">元，" 
					+ text 
					+"</font><font color=\"#fab12f\">"
					+ afterPrize_1
					+ "</font><font color=\"#ffffff\">元</font></html>";
			setJiangjin(prizes.get(0));
		} else {
			if (afterPrize_1 < 0 && afterPrize_2 < 0) {
				text = "亏损";
				afterPrize_1 = Math.abs(afterPrize_1);
				afterPrize_2 = Math.abs(afterPrize_2);
			} else {
				text = "盈利";
			}
			prizeDetail = "<html><font color=\"#ffffff\">如中奖，奖金" 
					+"</font><font color=\"#fab12f\">"
					+ min + "</font><font color=\"#ffffff\">至" 
					+"</font><font color=\"#fab12f\">"+ max
					+"</font><font color=\"#ffffff\">元，" + text
					+"</font><font color=\"#fab12f\">"
					+ afterPrize_1 
					+"</font><font color=\"#ffffff\">至" 
					+"</font><font color=\"#fab12f\">"+ afterPrize_2 
					+ "</font><font color=\"#ffffff\">元</font></html>";
		}
		return Html.fromHtml(prizeDetail);
	}

	public void setJiangjin(int jiangjin){
		jiangJin = jiangjin;
	}
	public int getJiangjin(){
		return jiangJin;
	}
	public NumberFast getRandom(int type){
		NumberFast number = new NumberFast();
		ArrayList<Integer> numbers = new ArrayList<>();//和值/三同号、三同号通选、三不同号、三不同号连选/二同号中的同号、二同号复选、二不同号//胆
		ArrayList<Integer> number1 = new ArrayList<>();//二同号中的不同号//脱

		number.setPlayType(type);
		number.setMode(1);
		Random random=new Random();
		switch (type) {
		case ConstantsBase.HEZHI:
			int hezhi = 0;
			while(number.getNumberHezhi().size()<3){
				int randomInt=random.nextInt(6)+1;
				hezhi=hezhi+randomInt;
				if(hezhi != 3&&hezhi != 18){
					number.getNumberHezhi().add(randomInt);
				}else{
					hezhi=hezhi-randomInt;
				}
			}	
			numbers.add(hezhi);
			break;
		case ConstantsBase.SANTONGHAO:
			while(numbers.size()<1){
				int randomInt=random.nextInt(6)+1;
				numbers.add(randomInt);				
			}
			break;
		case ConstantsBase.ERTONGHAO:
			while(numbers.size()<1){
				int randomInt=random.nextInt(6)+1;
				numbers.add(randomInt);
			}
			while(number1.size()<1){
				int randomInt=random.nextInt(6)+1;
				if (!numbers.contains(randomInt)) {
					number1.add(randomInt);
				}
			}
			break;
		case ConstantsBase.ERTONGHAOFU:
			while(numbers.size()<1){
				int randomInt=random.nextInt(6)+1;
				numbers.add(randomInt);
			}
			break;
		case ConstantsBase.SANBUTONGHAO:
			while(numbers.size()<3){
				int randomInt=random.nextInt(6)+1;
				if (!numbers.contains(randomInt)) {
					numbers.add(randomInt);
				}
			}
			break;
		case ConstantsBase.ERBUTONGHAO:
			while(numbers.size()<2){
				int randomInt=random.nextInt(6)+1;
				if (!numbers.contains(randomInt)) {
					numbers.add(randomInt);
				}
			}
			break;
		}
		number.setNumbers(getSortListBase(numbers));
		number.setNumber1(getSortListBase(number1));
		number.setNum(getTime(number));
		return number;
	}
	public ArrayList<Integer> getSortListBase(ArrayList<Integer> numbers){
		Collections.sort(numbers, new SortByName()); 
		return numbers;
	}
	public int getTime(NumberFast number){
		int nums = 0;
		int playType = number.getPlayType();
		int mode = number.getMode();
		if(mode == 0){//胆拖
			if (playType == ConstantsBase.SANBUTONGHAO){				
				nums = combinForX2(number.getNumbers().size(),number.getNumber1().size());
			}else if (playType == ConstantsBase.ERBUTONGHAO){				
				nums = number.getNumbers().size()*number.getNumber1().size();
			}
		}else{
			if (playType == ConstantsBase.HEZHI||playType == ConstantsBase.ERTONGHAOFU||playType == ConstantsBase.SANTONGHAO) {
				nums = number.getNumbers().size();
			} else if (playType == ConstantsBase.SANLIANHAO||playType == ConstantsBase.SANTONGHAOTONG) {
				nums = 1;
			} else if (playType == ConstantsBase.SANBUTONGHAO){				
				nums = combinForX(number.getNumbers().size(),3);
			}else if (playType == ConstantsBase.ERBUTONGHAO){				
				nums = combinForX(number.getNumbers().size(),2);
			}else if (playType == ConstantsBase.ERTONGHAO){				
				nums = number.getNumbers().size()*number.getNumber1().size();
			}
		}
		return nums;
	}
	public int combinForX(int mother,int child){
		int numberMother = 1;
		int numberChild = 1;
		for(int i = 0;i<child;i++){
			numberMother = numberMother*(mother-i);
		}
		for(int j = 0;j<child;j++){
			numberChild = numberChild*(child-j);
		}
		return numberMother/numberChild;
	}
	public int combinForX2(int mother,int child){//3,1
		int numberChild = 1;
		if(mother == 1){
			for(int j = 0;j<2;j++){
				numberChild = numberChild*(child-j);
			}
			return numberChild/2;
		}
		if(mother >1 ){
			return child;
		}
		return 1;
	}
	public int getWANFA(String wanfa){
		int wanfaInt = 0;
		switch (wanfa.trim()) {
		case "和值":
			wanfaInt = ConstantsBase.HEZHI;
			break;
		case "三同号单选":
			wanfaInt = ConstantsBase.SANTONGHAO;
			break;
		case "三同号通选":
			wanfaInt = ConstantsBase.SANTONGHAOTONG;
			break;
		case "三不同号":
			wanfaInt = ConstantsBase.SANBUTONGHAO;
			break;
		case "三连号通选":
			wanfaInt = ConstantsBase.SANLIANHAO;
			break;
		case "二同号单选":
			wanfaInt = ConstantsBase.ERTONGHAO;
			break;
		case "二同号复选":
			wanfaInt = ConstantsBase.ERTONGHAOFU;
			break;
		case "二不同号":
			wanfaInt = ConstantsBase.ERBUTONGHAO;
			break;
		}
		return wanfaInt;
	}
	public NumberFast getSortList(NumberFast numbers){
		if(numbers.getNumbers()!=null&&
				numbers.getNumbers().size()>0){
			Collections.sort(numbers.getNumbers(), new SortByName()); 
		}
		if(numbers.getNumber1()!=null&&
				numbers.getNumber1().size()>0){
			Collections.sort(numbers.getNumber1(), new SortByName()); 
		}
		return numbers;
	}
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
}
