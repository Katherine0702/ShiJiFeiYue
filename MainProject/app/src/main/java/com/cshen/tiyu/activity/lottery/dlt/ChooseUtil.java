package com.cshen.tiyu.activity.lottery.dlt;




import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.domain.cai115.Number115;
import com.cshen.tiyu.domain.dltssq.DLTNumber;
import com.cshen.tiyu.domain.dltssq.NumberEach;
import com.cshen.tiyu.utils.Base64;


public class ChooseUtil {
	public  static ChooseUtil  chooseUtil;
	public int jiangJin = 0;
	public  static ChooseUtil getChooseUtil(){
		if(chooseUtil == null){
			chooseUtil = new ChooseUtil();
		}
		return chooseUtil;
	}
	public DLTNumber getRandom(){
		DLTNumber dltNumber = new DLTNumber();
		NumberEach qianqu = new NumberEach();
		qianqu.setType(0);
		NumberEach houqu = new NumberEach();
		houqu.setType(1);

		Random random=new Random();
		while(qianqu.getNumbers().size()<5){
			int randomInt=random.nextInt(35)+1;
			if (!qianqu.getNumbers().contains(randomInt)) {
				qianqu.getNumbers().add(randomInt);
			}
		}
		while(houqu.getNumbers().size()<2){
			int randomInt=random.nextInt(12)+1;
			if (!houqu.getNumbers().contains(randomInt)) {
				houqu.getNumbers().add(randomInt);
			}
		}
		dltNumber.setQianqu(getSortList(qianqu));
		dltNumber.setHouqu(getSortList(houqu));
		int qianquSize = dltNumber.getQianqu().getNumbers().size();
		int houquSize = dltNumber.getHouqu().getNumbers().size();			
		dltNumber.setNum(getTime(qianquSize,houquSize));
		return dltNumber;
	}
	public NumberEach getSortList(NumberEach numbers){
		Collections.sort(numbers.getNumbers(), new SortByName()); 
		return numbers;
	}
	public Number115 getSortList115(Number115 numbers){
		Collections.sort(numbers.getNumbers(), new SortByName()); 
		if(numbers.getTuo()!=null&&
				numbers.getTuo().size()>0){
			Collections.sort(numbers.getTuo(), new SortByName()); 
		}
		if(numbers.getDan()!=null&&
				numbers.getDan().size()>0){
			Collections.sort(numbers.getDan(), new SortByName()); 
		}
		if(numbers.getWan()!=null&&
				numbers.getWan().size()>0){
			Collections.sort(numbers.getWan(), new SortByName()); 
		}
		if(numbers.getQian()!=null&&
				numbers.getQian().size()>0){
			Collections.sort(numbers.getQian(), new SortByName()); 
		}
		if(numbers.getBai()!=null&&
				numbers.getBai().size()>0){
			Collections.sort(numbers.getBai(), new SortByName()); 
		}
		return numbers;
	}
	public Number115 getRandom115(int leastNum,int type){
		Number115 number115 = new Number115();
		number115.setPlayType(type);
		number115.setMode(1);
		Random random=new Random();
		while(number115.getNumbers().size()<leastNum){
			int randomInt=random.nextInt(11)+1;
			if (!number115.getNumbers().contains(randomInt)) {
				number115.getNumbers().add(randomInt);
				number115.getWan().add(randomInt);
			}
		}	
		getSortList115(number115);
		if(type == 3&&leastNum == 2){
			number115.getWan().clear();
			number115.getWan().add(number115.getNumbers().get(0));
			number115.getQian().add(number115.getNumbers().get(1));
		}else if(type == 6&&leastNum == 3){
			number115.getWan().clear();
			number115.getWan().add(number115.getNumbers().get(0));
			number115.getQian().add(number115.getNumbers().get(1));
			number115.getBai().add(number115.getNumbers().get(2));
		}
		number115.setNum(get115Time(number115));
		return number115;
	}

	public DLTNumber getRandomSSQ(){
		DLTNumber dltNumber = new DLTNumber();
		NumberEach qianqu = new NumberEach();
		qianqu.setType(0);
		NumberEach houqu = new NumberEach();
		houqu.setType(1);

		Random random=new Random();
		while(qianqu.getNumbers().size()<6){
			int randomInt=random.nextInt(33)+1;
			if (!qianqu.getNumbers().contains(randomInt)) {
				qianqu.getNumbers().add(randomInt);
			}
		}
		while(houqu.getNumbers().size()<1){
			int randomInt=random.nextInt(16)+1;
			if (!houqu.getNumbers().contains(randomInt)) {
				houqu.getNumbers().add(randomInt);
			}
		}
		dltNumber.setQianqu(getSortList(qianqu));
		dltNumber.setHouqu(getSortList(houqu));
		int qianquSize = dltNumber.getQianqu().getNumbers().size();
		int houquSize = dltNumber.getHouqu().getNumbers().size();			
		dltNumber.setNum(getTimeSSQ(qianquSize,houquSize));
		return dltNumber;
	}
	public int getTime(int qianquSize,int houquSize){
		return (ChooseUtil.getChooseUtil().calcZhushu(qianquSize,qianquSize - 4) / 
				ChooseUtil.getChooseUtil().calcZhushu(5, 1))
				* (ChooseUtil.getChooseUtil().calcZhushu(houquSize, houquSize- 1) / 2);
	}
	public int getTimeSSQ(int qianquSize,int houquSize){
		return comp(6,qianquSize) *comp(1,houquSize);
	}
	public int get115Time(Number115 number115){
		int nums = 0;
		int playType = number115.getPlayType();
		int mode = number115.getMode();
		int least = 0;
		switch (playType) {
		case ConstantsBase.RENXUAN2:
			least =2;
			break;
		case ConstantsBase.ZUXUAN2:
			least = 2;
			break;
		case ConstantsBase.RENXUAN3:
			least = 3;
			break;
		case ConstantsBase.ZUXUAN3:
			least = 3;
			break;
		case ConstantsBase.RENXUAN4:
			least = 4;
			break;
		case ConstantsBase.RENXUAN5:
			least = 5;
			break;
		case ConstantsBase.RENXUAN6:
			least = 6;
			break;
		case ConstantsBase.RENXUAN7:
			least = 7;
			break;
		case ConstantsBase.RENXUAN8:
			least = 8;
			break;
		}

		if (playType == ConstantsBase.ZHIXUAN1) {//任选一  
			nums = number115.getNumbers().size();
		}else if (playType == ConstantsBase.ZHIXUAN2) {//前二直选 
			nums = countUnit(number115.getWan(), number115.getQian());
		} else if (playType == ConstantsBase.ZHIXUAN3) {//前三直选  
			nums = countUnit(number115.getWan(), number115.getQian(), number115.getBai());
		} else {
			if (mode == 0) {//含胆码  
				nums = countDanUnit(least,number115.getDan(), number115.getTuo());
			}else{
				nums = countUnit(least,number115.getNumbers());
			}
		}
		return nums;
	}
	public int getWANFA(String wanfa){
		int wanfaInt = 0;
		switch (wanfa.trim()) {
		case "选一":
			wanfaInt = ConstantsBase.ZHIXUAN1;
			break;
		case "选二":
			wanfaInt = ConstantsBase.RENXUAN2;
			break;
		case "前二直选":
			wanfaInt = ConstantsBase.ZHIXUAN2;
			break;
		case "前二组选":
			wanfaInt = ConstantsBase.ZUXUAN2;
			break;
		case "任选三":
			wanfaInt = ConstantsBase.RENXUAN3;
			break;
		case "前三直选":
			wanfaInt = ConstantsBase.ZHIXUAN3;
			break;
		case "选三前直":
			wanfaInt = ConstantsBase.ZHIXUAN3;
			break;
		case "前三组选":
			wanfaInt = ConstantsBase.ZUXUAN3;
			break;
		case "选三前组":
			wanfaInt = ConstantsBase.ZUXUAN3;
			break;
		case "任选四":
			wanfaInt = ConstantsBase.RENXUAN4;
			break;
		case "任选五":
			wanfaInt = ConstantsBase.RENXUAN5;
			break;
		case "任选六":
			wanfaInt = ConstantsBase.RENXUAN6;
			break;
		case "任选七":
			wanfaInt = ConstantsBase.RENXUAN7;
			break;
		case "任选八":
			wanfaInt = ConstantsBase.RENXUAN8;
			break;
		}
		return wanfaInt;
	}
	public Integer countUnit(int least,List<Integer> bets) {
		return comp(least, bets.size());
	}
	public static int comp(int r, int n) {
		long C = 1;
		for (int i = n - r + 1; i <= n; i++) {
			C = C * i;
		}
		for (int i = 2; i <= r; i++) {
			C = C / i;
		}
		return (int) C;
	}
	public Integer countUnit(List<Integer> bets1, List<Integer> bets2) {
		Set<Integer> betSet = new HashSet<Integer>();
		Integer betUnits = 0;
		for (int i = 0; i < bets1.size(); i++) {
			for (int j = 0; j < bets2.size(); j++) {
				betSet.clear();
				betSet.add(bets1.get(i));
				betSet.add(bets2.get(j));
				if (betSet.size() == 2) {
					betUnits += 1;
				}
			}
		}
		return betUnits;
	}
	public Integer countUnit(List<Integer> bets1, List<Integer> bets2, List<Integer> bets3) {
		Set<Integer> betSet = new HashSet<Integer>();
		Integer betUnits = 0;
		for (int i = 0; i < bets1.size(); i++) {
			for (int j = 0; j < bets2.size(); j++) {
				for (int k = 0; k < bets3.size(); k++) {
					betSet.clear();
					betSet.add(bets1.get(i));
					betSet.add(bets2.get(j));
					betSet.add(bets3.get(k));
					if (betSet.size() == 3) {
						betUnits += 1;
					}
				}
			}
		}
		return betUnits;
	}
	public Integer countDanUnit(int least,List<Integer> danbets, List<Integer> tuobets) {
		return comp(least-danbets.size(), tuobets.size());
	}

	public Spanned maymoney(Number115 number115){
		int dan,others;
		int playType = number115.getPlayType();
		int mode = number115.getMode();
		if(mode == 1){
			dan = 0;
			others = number115.getNumbers().size();
		}else{
			dan = number115.getDan().size();
			others = number115.getTuo().size();
		}
		String[] values = null;
		switch (playType) {
		case ConstantsBase.ZHIXUAN1:
			values = new String[]{"13"};
			break;
		case ConstantsBase.RENXUAN2:
			values = new String[]{"6","18","36","60"};
			break;
		case ConstantsBase.ZUXUAN2:
			values = new String[]{"65"};
			break;
		case ConstantsBase.ZHIXUAN2:
			values = new String[]{"130"};
			others = 0;
			break;
		case ConstantsBase.RENXUAN3:
			values = new String[]{"19","76","190"};
			break;
		case ConstantsBase.ZUXUAN3:
			values = new String[]{"195"};
			break;
		case ConstantsBase.ZHIXUAN3:
			values = new String[]{"1170"};
			others = 0;
			break;
		case ConstantsBase.RENXUAN4:
			values = new String[]{"78","390"};
			break;
		case ConstantsBase.RENXUAN5:
			values = new String[]{"540"};
			break;
		case ConstantsBase.RENXUAN6:
			values = new String[]{"90","180","270","360","450","540"};
			break;
		case ConstantsBase.RENXUAN7:
			values = new String[]{"26","78","156","260","390"};
			break;
		case ConstantsBase.RENXUAN8:
			values = new String[]{"9","36","90","180"};
			break;
		}
		String text, prizeDetail;
		int Principal = number115.getNum()*2;
		List<Integer> winValues = forecastPrize(dan,others,playType,values);		
		Collections.sort(winValues);
		int min = winValues.get(0);
		int max = winValues.get(winValues.size() - 1);
		int afterPrize_1 = min - Principal;
		int afterPrize_2 = max - Principal;
		if (afterPrize_1 == afterPrize_2) {
			if (afterPrize_1 < 0) {
				text = "亏损";
				afterPrize_1 = Math.abs(afterPrize_1);
			} else {
				text = "盈利";
			}
			prizeDetail = "<html><font color=\"#666666\">如中奖，奖金" 
					+"</font><font color=\"#e73c42\">"
					+winValues.get(0)+ "</font><font color=\"#666666\">元，" 
					+ text 
					+"</font><font color=\"#e73c42\">"
					+ afterPrize_1
					+ "</font><font color=\"#666666\">元</font></html>";
			setJiangjin(winValues.get(0));
		} else {
			if (afterPrize_1 < 0 && afterPrize_2 < 0) {
				text = "亏损";
				afterPrize_1 = Math.abs(afterPrize_1);
				afterPrize_2 = Math.abs(afterPrize_2);
			} else {
				text = "盈利";
			}
			prizeDetail = "<html><font color=\"#666666\">如中奖，奖金" 
					+"</font><font color=\"#e73c42\">"
					+ min + "</font><font color=\"#666666\">至" 
					+"</font><font color=\"#e73c42\">"+ max
					+"</font><font color=\"#666666\">元，" + text
					+"</font><font color=\"#e73c42\">"
					+ afterPrize_1 
					+"</font><font color=\"#666666\">至" 
					+"</font><font color=\"#e73c42\">"+ afterPrize_2 
					+ "</font><font color=\"#666666\">元</font></html>";
		}
		return Html.fromHtml(prizeDetail);
	}
	public void setJiangjin(int jiangjin){
		jiangJin = jiangjin;
	}
	public int getJiangjin(){
		return jiangJin;
	}
	/**
	 * 特殊情况：直选 danCount和count 都传0
	 * @param danCount  胆拖个数  没有则传 0
	 * @param count   非胆拖个数   
	 * @param betType
	 * @return
	 */
	private static List<Integer> forecastPrize(int danCount, int count,
			int betType,String[] value) {
		int sum = danCount + count;
		List<Integer> winValues = new LinkedList<Integer>();
		if (danCount != 0) {
			switch (betType) {
			case ConstantsBase.RENXUAN2:
				if (count == 1) {
					winValues.add(Integer.parseInt(value[0]));
				} else if (count == 2) {
					winValues.add(Integer.parseInt(value[0]));
					winValues.add(Integer.parseInt(value[1])
							- Integer.parseInt(value[0]));
				} else if (count == 3) {
					winValues.add(Integer.parseInt(value[0]));
					winValues.add(Integer.parseInt(value[1]));
				} else if (count > 3 && count < 8) {
					winValues.add(Integer.parseInt(value[0]));
					winValues.add(Integer.parseInt(value[1])
							+ Integer.parseInt(value[0]));
				} else if (count == 8) {
					winValues.add(Integer.parseInt(value[1])
							- Integer.parseInt(value[0]));
					winValues.add(Integer.parseInt(value[1])
							+ Integer.parseInt(value[0]));
				} else if (count == 9) {
					winValues.add(Integer.parseInt(value[1]));
					winValues.add(Integer.parseInt(value[1])
							+ Integer.parseInt(value[0]));
				} else if (count == 10) {
					winValues.add(Integer.parseInt(value[1])
							+ Integer.parseInt(value[0]));
				}
				break;
			case ConstantsBase.RENXUAN3:
				if (sum > 2) {
					if (danCount == 1) {
						if (count == 2) {
							winValues.add(Integer.parseInt(value[0]));
						} else if (count == 3) {
							winValues.add(Integer.parseInt(value[0]));
							winValues.add(Integer.parseInt(value[0]) * 3);
						} else if (count > 3 && count < 9) {
							winValues.add(Integer.parseInt(value[0]));
							winValues.add(Integer.parseInt(value[0]) * 6);
						} else if (count == 9) {
							winValues.add(Integer.parseInt(value[0]) * 3);
							winValues.add(Integer.parseInt(value[0]) * 6);
						} else if (count == 10) {
							winValues.add(Integer.parseInt(value[0]) * 6);
						}
					} else {
						if (count == 1) {
							winValues.add(Integer.parseInt(value[0]));
						} else if (count == 2) {
							winValues.add(Integer.parseInt(value[0]));
							winValues.add(Integer.parseInt(value[0]) * 2);
						} else if (count > 2 && count < 8) {
							winValues.add(Integer.parseInt(value[0]));
							winValues.add(Integer.parseInt(value[0]) * 3);
						} else if (count == 8) {
							winValues.add(Integer.parseInt(value[0]) * 2);
							winValues.add(Integer.parseInt(value[0]) * 3);
						} else if (count == 9) {
							winValues.add(Integer.parseInt(value[0]) * 3);
						}
					}
				}
				break;
			case ConstantsBase.RENXUAN4:
				if (sum > 3) {
					if (danCount == 1) {
						if (count == 3) {
							winValues.add(Integer.parseInt(value[0]));
						} else if (count > 3 && count < 10) {
							winValues.add(Integer.parseInt(value[0]));
							winValues
							.add(Integer.parseInt(value[1])
									- Integer.parseInt(value[0]));
						} else if (count == 10) {
							winValues
							.add(Integer.parseInt(value[1])
									- Integer.parseInt(value[0]));
						}
					} else if (danCount == 2) {
						if (count == 2) {
							winValues.add(Integer.parseInt(value[0]));
						} else if (count > 2 && count < 9) {
							winValues.add(Integer.parseInt(value[0]));
							winValues.add(Integer.parseInt(value[0]) * 3);
						} else if (count == 9) {
							winValues.add(Integer.parseInt(value[0]) * 3);
						}
					} else {
						if (count == 1) {
							winValues.add(Integer.parseInt(value[0]));
						} else if (count > 1 && count < 8) {
							winValues.add(Integer.parseInt(value[0]));
							winValues.add(Integer.parseInt(value[0]) * 2);
						} else if (count == 8) {
							winValues.add(Integer.parseInt(value[0]) * 2);
						}
					}
				}
				break;
			case ConstantsBase.RENXUAN6:
				if (sum > 5) {
					winValues.add(Integer.parseInt(value[0]));
					winValues
					.add(Integer.parseInt(value[sum - 6]));
				}
				break;
			case ConstantsBase.RENXUAN7:
				if (sum > 6) {
					winValues.add(Integer.parseInt(value[0]));
					winValues
					.add(Integer.parseInt(value[sum - 7]));
				}
				break;
			case ConstantsBase.RENXUAN8:
				if (sum > 7) {
					winValues.add(Integer.parseInt(value[0]));
					winValues
					.add(Integer.parseInt(value[sum - 8]));
				}
				break;
			default:
				String[] winValue = value;
				for (int i = 0; i < winValue.length; i++) {
					winValues.add(Integer.parseInt(value[i]));
				}

				break;
			}
		} else {
			switch (betType) {
			case ConstantsBase.RENXUAN2:
				if (count == 2) {
					winValues.add(Integer.parseInt(value[0]));
				} else if (count == 3) {
					winValues.add(Integer.parseInt(value[0]));
					winValues.add(Integer.parseInt(value[1]));
				} else if (count == 4) {
					winValues.add(Integer.parseInt(value[0]));
					winValues.add(Integer.parseInt(value[2]));
				} else if (count > 4 && count < 9) {
					winValues.add(Integer.parseInt(value[0]));
					winValues.add(Integer.parseInt(value[3]));
				} else if (count == 9) {
					winValues.add(Integer.parseInt(value[1]));
					winValues.add(Integer.parseInt(value[3]));
				} else if (count == 10) {
					winValues.add(Integer.parseInt(value[2]));
					winValues.add(Integer.parseInt(value[3]));
				} else if (count == 11) {
					winValues.add(Integer.parseInt(value[3]));
				}
				break;
			case ConstantsBase.RENXUAN3:
				if (count == 3) {
					winValues.add(Integer.parseInt(value[0]));
				} else if (count == 4) {
					winValues.add(Integer.parseInt(value[0]));
					winValues.add(Integer.parseInt(value[1]));
				} else if (count > 4 && count < 10) {
					winValues.add(Integer.parseInt(value[0]));
					winValues.add(Integer.parseInt(value[2]));
				} else if (count == 10) {
					winValues.add(Integer.parseInt(value[1]));
					winValues.add(Integer.parseInt(value[2]));
				} else if (count == 11) {
					winValues.add(Integer.parseInt(value[2]));
				}
				break;
			case ConstantsBase.RENXUAN4:
				if (count == 4) {
					winValues.add(Integer.parseInt(value[0]));
				} else if (count > 4 && count < 11) {
					winValues.add(Integer.parseInt(value[0]));
					winValues.add(Integer.parseInt(value[1]));
				} else if (count == 11) {
					winValues.add(Integer.parseInt(value[1]));
				}
				break;
			case ConstantsBase.RENXUAN6:
				winValues.add(Integer.parseInt(value[count - 6]));
				break;
			case ConstantsBase.RENXUAN7:
				winValues.add(Integer.parseInt(value[count - 7]));
				break;
			case ConstantsBase.RENXUAN8:
				winValues.add(Integer.parseInt(value[count - 8]));
				break;
			default:
				String[] winValue = value;
				for (int i = 0; i < winValue.length; i++) {
					winValues.add(Integer.parseInt(value[i]));
				}

				break;
			}
		}
		return winValues;
	}
	public static String  choose(int WANFAINT,int DANTUO){
		String titleItem = "";
		switch (WANFAINT) {
		case ConstantsBase.ZHIXUAN1:
			if(DANTUO == 1){
				titleItem = "前一直选";
			}
			break;
		case ConstantsBase.RENXUAN2:
			if(DANTUO == 1){
				titleItem = "任选二";
			}else{
				titleItem = "任选二胆拖";
			}
			break;
		case ConstantsBase.ZUXUAN2:
			if(DANTUO == 1){
				titleItem = "前二组选";
			}else{
				titleItem = "前二组选胆拖";
			}
			break;
		case ConstantsBase.ZHIXUAN2:
			if(DANTUO == 1){
				titleItem = "前二直选";
			}
			break;
		case ConstantsBase.RENXUAN3:
			if(DANTUO == 1){
				titleItem = "任选三";
			}else{
				titleItem = "任选三胆拖";
			}
			break;
		case ConstantsBase.ZUXUAN3:
			if(DANTUO == 1){
				titleItem = "前三组选";
			}else{
				titleItem = "前三组选胆拖";
			}
			break;
		case ConstantsBase.ZHIXUAN3:
			if(DANTUO == 1){
				titleItem = "前三直选";
			}
			break;
		case ConstantsBase.RENXUAN4:
			if(DANTUO == 1){
				titleItem = "任选四";
			}else{
				titleItem = "任选四胆拖";
			}
			break;
		case ConstantsBase.RENXUAN5:
			if(DANTUO == 1){
				titleItem = "任选五";
			}else{
				titleItem = "任选五胆拖";
			}
			break;
		case ConstantsBase.RENXUAN6:
			if(DANTUO == 1){
				titleItem = "任选六";
			}else{
				titleItem = "任选六胆拖";
			}
			break;
		case ConstantsBase.RENXUAN7:
			if(DANTUO == 1){
				titleItem = "任选七";
			}else{
				titleItem = "任选七胆拖";
			}
			break;
		case ConstantsBase.RENXUAN8:
			if(DANTUO == 1){
				titleItem = "任选八";
			}
			break;
		}
		return titleItem;
	}
	public static int  chooseNUMLEAST(int WANFAINT){
		int NUMLEAST = 0;
		switch (WANFAINT) {
		case ConstantsBase.ZHIXUAN1:
			NUMLEAST = 1; 
			break;
		case ConstantsBase.RENXUAN2:
			NUMLEAST = 2; 
			break;
		case ConstantsBase.ZUXUAN2:
			NUMLEAST = 2; 
			break;
		case ConstantsBase.ZHIXUAN2:
			NUMLEAST = 2; 
			break;
		case ConstantsBase.RENXUAN3:
			NUMLEAST = 3; 
			break;
		case ConstantsBase.ZUXUAN3:
			NUMLEAST = 3; 
			break;
		case ConstantsBase.ZHIXUAN3:
			NUMLEAST = 3; 
			break;
		case ConstantsBase.RENXUAN4:
			NUMLEAST = 4; 
			break;
		case ConstantsBase.RENXUAN5:
			NUMLEAST = 5; 
			break;
		case ConstantsBase.RENXUAN6:
			NUMLEAST = 6; 
			break;
		case ConstantsBase.RENXUAN7:
			NUMLEAST = 7; 
			break;
		case ConstantsBase.RENXUAN8:
			NUMLEAST = 8; 
			break;
		}
		return NUMLEAST;
	}
	public static int  chooseNUMMOST(int WANFAINT){
		int NUMMOST = 0;
		switch (WANFAINT) {
		case ConstantsBase.RENXUAN2:
			NUMMOST = 1; 
			break;
		case ConstantsBase.RENXUAN3:
			NUMMOST = 2; 
			break;
		case ConstantsBase.RENXUAN4:
			NUMMOST = 3; 
			break;
		case ConstantsBase.RENXUAN5:
			NUMMOST = 4; 
			break;
		case ConstantsBase.RENXUAN6:
			NUMMOST = 5; 
			break;
		case ConstantsBase.RENXUAN7:
			NUMMOST = 6; 
			break;
		case ConstantsBase.ZUXUAN2:
			NUMMOST = 1; 
			break;
		case ConstantsBase.ZUXUAN3:
			NUMMOST = 2; 
			break;
		}
		return NUMMOST;
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
	public int calcZhushu (int m,int n){
		if (m == n) {
			return m;
		} else {
			return m * this.calcZhushu(m - 1, n);
		}
	}
	/**
	 * 存储数据
	 *
	 * @param mContext
	 *            上下文
	 * @param tempName
	 *            存储名称
	 * @param tempList
	 *            数据集合
	 */
	public static void setData(Context mContext, String tempName, List<?> tempList) {
		SharedPreferences sps = mContext.getSharedPreferences("base64", Context.MODE_PRIVATE);
		if(tempList == null){
			Editor editor = sps.edit();
			editor.putString(tempName, null);
			editor.commit();
			return ;
		}
		// 创建字节输出流
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			// 创建对象输出流，并封装字节流
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			// 将对象写入字节流
			oos.writeObject(tempList);
			// 将字节流编码成base64的字符串
			String tempBase64 = new String(Base64.encode(baos.toByteArray()));
			Editor editor = sps.edit();
			editor.putString(tempName, tempBase64);
			editor.commit();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public static List<?> getData(Context mContext, String tempName) {
		List<?> tempList = null ; 
		SharedPreferences sps = mContext.getSharedPreferences("base64", Context.MODE_PRIVATE);
		String tempBase64 = sps.getString(tempName, "");// 初值空
		if (TextUtils.isEmpty(tempBase64)) {
			return tempList;
		}
		// 读取字节
		byte[] base64 = Base64.decode(tempBase64);
		// 封装到字节流
		ByteArrayInputStream bais = new ByteArrayInputStream(base64);
		try {
			// 再次封装
			ObjectInputStream ois = new ObjectInputStream(bais);
			// 读取对象
			tempList = (List<?>) ois.readObject();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return tempList;

	}
	public static void setDate(Context mContext, String tempName, Date date) {
		SharedPreferences sps = mContext.getSharedPreferences("base64", Context.MODE_PRIVATE);		
		// 创建字节输出流
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			// 创建对象输出流，并封装字节流
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			// 将对象写入字节流
			oos.writeObject(date);
			// 将字节流编码成base64的字符串
			String tempBase64 = new String(Base64.encode(baos.toByteArray()));
			Editor editor = sps.edit();
			editor.putString(tempName, tempBase64);
			editor.commit();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public static Date getDate(Context mContext, String tempName) {
		Date date = null ; 
		SharedPreferences sps = mContext.getSharedPreferences("base64", Context.MODE_PRIVATE);
		String tempBase64 = sps.getString(tempName, "");// 初值空
		if (TextUtils.isEmpty(tempBase64)) {
			return date;
		}
		// 读取字节
		byte[] base64 = Base64.decode(tempBase64);
		// 封装到字节流
		ByteArrayInputStream bais = new ByteArrayInputStream(base64);
		try {
			// 再次封装
			ObjectInputStream ois = new ObjectInputStream(bais);
			// 读取对象
			date = (Date) ois.readObject();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return date;

	}
}