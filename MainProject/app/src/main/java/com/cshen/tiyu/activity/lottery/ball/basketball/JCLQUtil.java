package com.cshen.tiyu.activity.lottery.ball.basketball;



import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.cshen.tiyu.activity.lottery.ball.util.PassType2;
import com.cshen.tiyu.activity.lottery.ball.util.PeiLv;
import com.cshen.tiyu.activity.lottery.dlt.ChooseUtil;
import com.cshen.tiyu.domain.ball.BasketBallMatch;
import com.cshen.tiyu.domain.ball.Match;
import com.cshen.tiyu.domain.ball.PassTypeEach;
import com.cshen.tiyu.domain.ball.ScroeBean;

public class JCLQUtil {
	PassType2 chuanguan = new PassType2();
	ArrayList<PassTypeEach> chuansChoosed;
	public static JCLQUtil jczqUtil;
	HashMap _cacheMaxOdds;
	int editFlag;

	public static JCLQUtil getJCLQUtil() {
		if (jczqUtil == null) {
			jczqUtil = new JCLQUtil();
		}
		return jczqUtil;
	}

	public boolean isFiveMin(Context _this,String fileName){
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dateNow= new Date(System.currentTimeMillis()); 
		Date dateSave = (Date)ChooseUtil.getDate(_this,fileName);
		if(dateSave == null){
			return true;
		}else{
			long between=(dateNow.getTime()-dateSave.getTime());
			if(between>=5*60*1000){
				return true;
			}
		}
		return false;
	}
	// 串1
	public ArrayList<PassTypeEach> getMultiplePassType(int matchCount) {
		ArrayList<PassTypeEach> types = new ArrayList<PassTypeEach>();
		if (matchCount < 1 || matchCount > chuanguan.maxPassMatchCount) {
			return types;
		}
		for (int i = 0; i < chuanguan.PassTypeArr.length; i++) {
			PassTypeEach type = chuanguan.PassTypeArr[i];
			if (type.getMatchCount() > 1) {
				if (type.getMatchCount() <= matchCount && type.getUnits() == 1)
					types.add(type);
				else if (type.getMatchCount() > matchCount)
					break;
			}
		}
		return types;
	}

	// 串n
	public ArrayList<PassTypeEach> getNormalPassType(int matchCount) {
		ArrayList<PassTypeEach> types = new ArrayList<PassTypeEach>();
		if (matchCount < 1 || matchCount > chuanguan.maxPassMatchCount) {
			return null;
		}
		if (matchCount > 1) {
			for (int i = 0; i < chuanguan.PassTypeArr.length; i++) {
				PassTypeEach type = chuanguan.PassTypeArr[i];
				if (type.getMatchCount() <= matchCount && type.getUnits() != 1)
					types.add(type);
				else if (type.getMatchCount() > matchCount) {
					break;
				}

			}
		}
		return types;
	}

	public int getCanShedanNum(PassTypeEach pteMin, int matchCount) {
		int canShedanNum = 0; // 可以设胆的数目
		if (pteMin != null) {
			if (matchCount > pteMin.getMatchCount()) {
				canShedanNum = pteMin.getMatchCount()-1;
			} 
		}
		return canShedanNum;
	}
	public int getUnit(boolean isDangun,ArrayList<Match> match){
		int matchCount = match.size();
		int totalDanGuanCount = 0;
		if(isDangun){
			for (int h = 0; h<matchCount; h++) {
				totalDanGuanCount = totalDanGuanCount+match.get(h).getCheckNumber().size();
			}
		}
		ArrayList<Integer> arr_passDanPass = new ArrayList<Integer>();

		for (int n = 0; n<matchCount; n++) {
			if(match.get(n).isDan()){
				arr_passDanPass.add(n);
			}
		}
		int passTypeCount = 0;
		int passChuanCount = 0;
		int totalCount = 0;
		for (int i = 0; i<getPassTypeEach().size(); i++) {
			passTypeCount = 0;

			int[] arrChuan = getPassTypeEach().get(i).getPassMatchs();
			if (arrChuan.length == 1 && arrChuan[0] == 1) {//单关
				totalCount = totalCount+totalDanGuanCount;
			}else{
				for (int m = 0; m<arrChuan.length; m++) {
					passChuanCount = 0;
					int passMatchCount = arrChuan[m];
					//算组合数 如：3场比赛，3串1 C3x3 2串1 C3x2
					ArrayList<ArrayList<Integer>> arrComb = 
							combinForX(matchCount,passMatchCount,arr_passDanPass);
					//循环所有组合
					for (int j = 0; j<arrComb.size(); j++) {
						int tempCount = 1;
						//tempcount 乘的基数
						for (int k = 0; k<arrComb.get(j).size(); k++) {
							//循环每场比赛，如果＝1说明 改比赛在串中。就乘上该比赛选中的所有赔率数
							BasketBallMatch sectionInfo = (BasketBallMatch)match.get(k);
							if (arrComb.get(j).get(k) == 1) {
								tempCount = tempCount*sectionInfo.getCheckNumber().size();
							}
						}
						//每种组合的 注数累加
						passTypeCount = passTypeCount+tempCount;
					}
					passChuanCount = passChuanCount + passTypeCount;
				}
				//所有组合的累加
				totalCount = totalCount + passChuanCount;
			}
		}
		return totalCount;
	}
	public void setPassTypeEach(ArrayList<PassTypeEach> chuansChoosed) {
		this.chuansChoosed = chuansChoosed;
	}

	public ArrayList<PassTypeEach> getPassTypeEach() {
		return chuansChoosed;
	}

	public static String chooseScroeName(String WANFAINT) {
		String str = "";
		switch (WANFAINT) {
		case "SF_WIN":
			str = "主胜;";
			break;
		case "SF_LOSE":
			str = "客胜;";
			break;

		case "WIN":
			str = "主胜;";
			break;
		case "LOSE":
			str = "客胜;";
			break;
		case "LARGE":
			str = "大于;";
			break;
		case "LITTLE":
			str = "小于;";
			break;
		case "GUEST1_5":
			str = "客胜1-5;";
			break;
		case "GUEST6_10":
			str = "客胜6-10;";
			break;
		case "GUEST11_15":
			str = "客胜11-15;";
			break;
		case "GUEST16_20":
			str = "客胜16-20;";
			break;
		case "GUEST21_25":
			str = "客胜21-25;";
			break;
		case "GUEST26":
			str = "客胜guest26+;";
			break;
		case "HOME1_5":
			str = "主胜1-5;";
			break;
		case "HOME6_10":
			str = "主胜6-10;";
			break;
		case "HOME11_15":
			str = "主胜11-15;";
			break;
		case "HOME16_20":
			str = "主胜16-20;";
			break;
		case "HOME21_25":
			str = "主胜21-25;";
			break;
		case "HOME26":
			str = "主胜26+;";
		}
		return str;
	}
	
	
	
	
	public static String chooseScroeValue(String scroe) {
		String str = "";
		switch (scroe) {
		case "SF_WIN":
			str = "3";
			break;
		case "SF_LOSE":
			str = "0";
			break;

		case "WIN":
			str = "3";
			break;
		case "LOSE":
			str = "0";
			break;
		case "LARGE":
			str = "1";
			break;
		case "LITTLE":
			str = "0";
			break;
		case "GUEST1_5":
			str = "guest1-5";
			break;
		case "GUEST6_10":
			str = "guest6-10";
			break;
		case "GUEST11_15":
			str = "guest11-15";
			break;
		case "GUEST16_20":
			str = "guest16-20";
			break;
		case "GUEST21_25":
			str = "guest21-25";
			break;
		case "GUEST26":
			str = "guest26+";
			break;
		case "HOME1_5":
			str = "home1-5";
			break;
		case "HOME6_10":
			str = "home6-10";
			break;
		case "HOME11_15":
			str = "home11-15";
			break;
		case "HOME16_20":
			str = "home16-20";
			break;
		case "HOME21_25":
			str = "home21-25";
			break;
		case "HOME26":
			str = "home26+";
		}
		return str;
	}

	public int getPlayTypeItem(String key) {// 0,胜负。1,让分胜负。2,胜分差。3,大小分。4,混合
		int str = -1;
		if (key.contains("SF_")) {
			str = 1;
		} else if (key.contains("GUEST")||key.contains("HOME")) {
			str = 2;
		} else if (key.contains("LITTLE") ||key.contains("LARGE")) {
			str = 3;
		} else {
			str = 0;
		} 
		return str;
	}
	public String getPlayTypeString(String key) {// 0,胜负。1,让分胜负。2,胜分差。3,大小分。4,混合
		String str = "";
		if (key.contains("SF_")) {
			str = "RFSF";
		} else if (key.contains("GUEST")||key.contains("HOME")) {
			str = "SFC";
		} else if (key.contains("LITTLE") ||key.contains("LARGE")) {
			str = "DXF";
		} else {
			str = "SF";
		} 
		return str;
	}
	public void setHuancun() {
		_cacheMaxOdds = new HashMap<String,Float>();
		editFlag = -1;
	}

	public float getMaxMoney(ArrayList<Match> match) { // 比赛场次数
		ArrayList<PeiLv> allMatchOddsArray = new ArrayList<PeiLv>();
		int matchCount = match.size();
		// 循环比赛场次，分组
		for (int i = 0; i < matchCount; i++) {
			ArrayList<ScroeBean> choose = match.get(i).getCheckNumber();
			String matchKey = match.get(i).getMatchKey();
			// 如果有缓存，继续循环，否则，重新算新的该场比赛的最大赔率组合
			if (editFlag == -1) {
			}else{
				if (_cacheMaxOdds.containsKey(matchKey)) {
					continue;
				}
			}

			// 一场比赛的所有玩法，用以存放按spf分组的赔率
			ArrayList<ScroeBean> win = new ArrayList<ScroeBean>();
			ArrayList<ScroeBean> lose = new ArrayList<ScroeBean>();
			String rqstr = match.get(i).getHandicap();
			float rf = 0;
			try {
				rf = Float.parseFloat(rqstr);
			} catch (Exception e) {
				e.printStackTrace();
				rf = 0;
			}

			// 循环一场比赛的所有赔率，赔率按spf分组
			for (int j = 0; j < choose.size(); j++) {
				String peilvStr = chooseSF(choose.get(j).getKey(),rf);
				if (peilvStr.contains("3")) {
					win.add(choose.get(j));
				}
				if (peilvStr.contains("0")) {
					lose.add(choose.get(j));
				}
			}
			PeiLv peilv = new PeiLv();
			peilv.setMatchKey(matchKey);
			peilv.setRf(rf);
			peilv.setWin(win);
			peilv.setLose(lose);

			allMatchOddsArray.add(peilv);
		}
		// 更新缓存标记
		editFlag = 1;
		// 开始按allMatchOddsArray 返回 数组中的数据计算最大组合赔率
		DecimalFormat    df   = new DecimalFormat("######0.00");   
		return Float.parseFloat(df.format(calcuateMaxOddsComb(allMatchOddsArray,match))+"");
	}

	public float calcuateMaxOddsComb(ArrayList<PeiLv> arrAllMatchOdds,ArrayList<Match> match) {
		int matchCount = match.size();

		ArrayList<ScroeBean> sf = new ArrayList<ScroeBean>();
		ArrayList<ScroeBean> rfsf = new ArrayList<ScroeBean>();
		ArrayList<ScroeBean> dxf = new ArrayList<ScroeBean>();
		ArrayList<ScroeBean> sfc = new ArrayList<ScroeBean>();
		ArrayList<ScroeBean> win = null;
		ArrayList<ScroeBean> lose = null;
		ArrayList<ScroeBean> arr = null;
		HashMap wanfa = new HashMap<String, ArrayList<ScroeBean>>();
		ArrayList<Float> arr_spfValue = new ArrayList<>();
		ArrayList<Float> arr_allMaxValue = new ArrayList<>();
		ArrayList<String> cacheKeysArray = new ArrayList<>();
		ArrayList<Integer> danWeiZhi = new ArrayList<>();
		try{
			for (int i = 0; i < arrAllMatchOdds.size(); i++) {// 循环所有赛事
				arr_spfValue.clear();
				PeiLv peilv = arrAllMatchOdds.get(i);
				float rqshu = peilv.getRf();
				if(win!=null){
					win.clear();
				}
				win = peilv.getWin();
				if(lose!=null){
					lose.clear();
				}
				lose = peilv.getLose();

				String keySF = "";
				for (int k = 0; k < 2; k++) {
					sf.clear();
					rfsf.clear();
					dxf.clear();
					sfc.clear();
					if (k == 0) {
						arr = win;
						keySF = "3";
					}
					if (k == 1) {
						arr = lose;
						keySF = "0";
					}
					wanfa.clear();
					for (int j = 0; j < arr.size(); j++) {
						String key = arr.get(j).getKey();
						if (key.contains("SF_")) {
							rfsf.add(arr.get(j));
						} else if (key.contains("GUEST")||key.contains("HOME")) {
							sfc.add(arr.get(j));
						} else if (key.contains("LITTLE") ||key.contains("LARGE")) {
							dxf.add(arr.get(j));
						} else {
							sf.add(arr.get(j));
						} 					
					}
					if(sf!=null&&sf.size()>0){
						wanfa.put("SF", sf);
					}if(rfsf!=null&&rfsf.size()>0){
						wanfa.put("RFSF",rfsf);
					}if(dxf!=null&&dxf.size()>0){
						wanfa.put("DXF",dxf);
					}if(sfc!=null&&sfc.size()>0){
						wanfa.put("SFC",sfc);
					}
					arr_spfValue.add(meanWhileHappen(wanfa, keySF, rqshu));
				}
				// 从这场比赛的 3 1 0中，获取最大赔率
				float backFloat = 0;
				for (Float arr_combSing : arr_spfValue) {
					if (arr_combSing > backFloat) {
						backFloat = arr_combSing;
					}
				}
				// 按matchKey计入缓存
				_cacheMaxOdds.put(arrAllMatchOdds.get(i).getMatchKey(), backFloat);
			}

			// 所有比赛的最大赔率数组
			Iterator iter = _cacheMaxOdds.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String keys = (String) entry.getKey();
				cacheKeysArray.add(keys);
			}

			Collections.sort(cacheKeysArray, new SortByMatchKey());
			for (int i = 0; i<_cacheMaxOdds.size(); i++) {
				arr_allMaxValue.add((Float) _cacheMaxOdds.get(cacheKeysArray.get(i)));
			}
			for (int i = 0; i < match.size(); i++) {
				if (match.get(i).isDan()) {
					danWeiZhi.add(i);
				}
			}
			if (getPassTypeEach().size() > 0) {
				float passTypeMoney = 0.0f;
				float totalMoney = 0.0f;
				float passChuanMoney = 0;
				for (int i = 0; i < getPassTypeEach().size(); i++) {
					passTypeMoney = 0;

					int[] arrChuan = getPassTypeEach().get(i).getPassMatchs();
					for (int m = 0; m < arrChuan.length; m++) {
						passChuanMoney = 0;
						int passMatchCount = arrChuan[m];
						ArrayList<ArrayList<Integer>> arrComb = combinForX(
								matchCount, passMatchCount, danWeiZhi);// 总比赛数，和串关比赛数

						for (int j = 0; j < arrComb.size(); j++) {
							float tempMoney = 1.0f;
							for (int k = 0; k < arrComb.get(j).size(); k++) {
								if (arrComb.get(j).get(k) == 1) {
									tempMoney = tempMoney * arr_allMaxValue.get(k);
								}
							}
							BigDecimal passTypeMoneyB = new BigDecimal(passTypeMoney+"");
							BigDecimal tempMoneyB = new BigDecimal(tempMoney+"");
							passTypeMoney = passTypeMoneyB.add(tempMoneyB).floatValue();
						}
						BigDecimal passChuanMoneyB = new BigDecimal(passChuanMoney+"");
						BigDecimal passTypeMoneyB = new BigDecimal(passTypeMoney+"");
						passChuanMoney = passChuanMoneyB.add(passTypeMoneyB).floatValue();
					}
					BigDecimal totalMoneyB = new BigDecimal(totalMoney+"");
					BigDecimal passChuanMoneyB = new BigDecimal(passChuanMoney+"");
					totalMoney = totalMoneyB.add(passChuanMoneyB).floatValue();
				}
				return totalMoney;
			} else {
				return 0.0f;
			}
		}
		catch(Exception e){
			e.printStackTrace();
			return 0.0f;
		}finally{
			if(sf!=null){
				sf.clear();sf = null;
			}
			if(rfsf!=null){
				rfsf.clear();rfsf = null;
			}
			if(dxf!=null){
				dxf.clear();dxf = null;
			}
			if(sfc!=null){
				sfc.clear();sfc = null;
			}
			if(win!=null){
				win.clear();win = null;
			}
			if(lose!=null){
				lose.clear();lose = null;
			}
			if(arr!=null){
				arr.clear();arr = null;
			}
			if(arr_spfValue!=null){
				arr_spfValue.clear();arr_spfValue = null;
			}
			if(arr_allMaxValue!=null){
				arr_allMaxValue.clear();arr_allMaxValue = null;
			}
			if(cacheKeysArray!=null){
				cacheKeysArray.clear();cacheKeysArray = null;
			}
			if(danWeiZhi!=null){
				danWeiZhi.clear();danWeiZhi = null;
			}
		}
	}

	public float meanWhileHappen(HashMap dicGroupedOdds, String spfType,float rqshu) {
		float backFloat = 0.0f;
		ArrayList<String> allTypes = new ArrayList<>();
		ArrayList<ArrayList<Integer>> arrComb = null;
		ArrayList<ArrayList<ScroeBean>> arr_combPlay = new ArrayList<>();
		try{
			Iterator iter = dicGroupedOdds.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String key = (String) entry.getKey();
				allTypes.add(key);
			}
			int maxLen = allTypes.size();
			for (int i = 0; i < maxLen; i++) {
				if(arrComb!=null){
					arrComb.clear();
				}
				arrComb = combinForX(maxLen, i + 1,null);// 总比赛数，和串关比赛数

				// 循环所有组合 1,2,3,4,5
				for (int j = 0; j < arrComb.size(); j++) {
					arr_combPlay.clear();
					for (int k = 0; k < arrComb.get(j).size(); k++) {
						if (arrComb.get(j).get(k) == 1) {// 这种玩法，需要组合
							arr_combPlay.add((ArrayList<ScroeBean>) dicGroupedOdds.get(allTypes.get(k)));
						}
					}
					float combValue = calculatePlayComb(arr_combPlay, spfType,
							rqshu);
					if (combValue > backFloat) {
						backFloat = combValue;
					}
				}
			}
			return backFloat;
		}catch(Exception e){
			e.printStackTrace();
			return backFloat;
		}finally{

			if(allTypes!=null){
				allTypes.clear();
				allTypes = null;
			}
			if(arrComb!=null){
				arrComb.clear();
				arrComb = null;
			}
			if(arr_combPlay!=null){
				arr_combPlay.clear();
				arr_combPlay = null;
			}
		}
	}
	public ArrayList<ArrayList<Integer>> combinForX(int x, int y,//一共5个选项，从y = 1，即两个项组合开始
			ArrayList<Integer> arr_dan) {
		ArrayList<Integer> arr_T = new ArrayList<Integer>();
		if (arr_dan != null) {
			x = x - arr_dan.size();
			y = y - arr_dan.size();
		}

		for (int i = 0; i < x; i++) {
			if (i < y) {//1,0,0,0,0
				arr_T.add(1);
			} else {
				arr_T.add(0);
			}
		}
		ArrayList<Integer> arrA = new ArrayList<Integer>();
		arrA.addAll(arr_T);
		if (arr_dan != null) {
			for (int j = 0; j < arr_dan.size(); j++) {
				int index = arr_dan.get(j);
				// 此处添加胆，注意胆的位置，必需在数组范围内或最后一个，不能越界（胆需要排好顺序）
				arrA.add(index, 1);
			}
		}

		ArrayList<ArrayList<Integer>> set_Result = new ArrayList<ArrayList<Integer>>();
		set_Result.add(arrA);

		boolean notFound = true;
		do {
			notFound = true;
			ArrayList<Integer> arrB = new ArrayList<Integer>();
			for (int i = 1; i < arr_T.size(); i++) {
				if (arr_T.get(i) == 0 && arr_T.get(i - 1) == 1) {
					arr_T.set(i, 1);
					arr_T.set(i - 1, 0);
					List<Integer> arrx = (List<Integer>) arr_T.subList(0, i);
					Collections.sort(arrx, new SortByName());
					for (int k = 0; k < i; k++) {
						arr_T.set(k, (Integer) arrx.get(k));
					}
					arrB.clear();
					arrB.addAll(arr_T);
					if (arr_dan != null) {
						for (int j = 0; j < arr_dan.size(); j++) {
							int index = arr_dan.get(j);
							// 此处添加胆，注意胆的位置，必需在数组范围内或最后一个，不能越界（胆需要排好顺序）
							arrB.add(index, 1);
						}
					}
					set_Result.add(arrB);

					notFound = false;
					break;
				} else {
				}
			}

		} while (!notFound);

		return set_Result;
	}
	public float calculatePlayComb(ArrayList<ArrayList<ScroeBean>> arrCombPlay,
			String spfType, float rqshu) {
		float backFloat = 0,arr_combSing = 0;
		ArrayList<ScroeBean>  arrSingle = new ArrayList<ScroeBean>();
		try{
			if (arrCombPlay.size() == 0) {// 没有组合 不会出现
				return backFloat;
			} else if (arrCombPlay.size() == 1) {// 组合只有一种玩法 直接拿最大的值 (等同于遍历3/1/0中
				// 每种玩法 所有赔率，取最大值)
				ArrayList<ScroeBean> odds = arrCombPlay.get(0);
				for (ScroeBean odd : odds) {
					if (Float.parseFloat(odd.getValue()) > backFloat) {
						backFloat = Float.parseFloat(odd.getValue());
					}
				}
				return backFloat;
			} else if (arrCombPlay.size() == 2) {
				for (int i = 0; i < arrCombPlay.get(0).size(); i++) {
					arrSingle.clear();
					arrSingle.add(arrCombPlay.get(0).get(i));
					for (int j = 0; j < arrCombPlay.get(1).size(); j++) {
						arrSingle.add(arrCombPlay.get(1).get(j));	
						arr_combSing = combPlayWithDataInArr(
								arrSingle, spfType, rqshu);
						if (arr_combSing > backFloat) {
							backFloat = arr_combSing;
						}
						arrSingle.remove(arrSingle.size()-1);
					}
				}
				return backFloat;
			} else if (arrCombPlay.size() == 3) {
				for (int i = 0; i < arrCombPlay.get(0).size(); i++) {
					arrSingle.clear();
					arrSingle.add(arrCombPlay.get(0).get(i));
					for (int j = 0; j < arrCombPlay.get(1).size(); j++) {
						arrSingle.add(arrCombPlay.get(1).get(j));
						for (int k = 0; k < arrCombPlay.get(2).size(); k++) {
							arrSingle.add(arrCombPlay.get(2).get(k));					
							arr_combSing = combPlayWithDataInArr(
									arrSingle, spfType, rqshu);
							if (arr_combSing > backFloat) {
								backFloat = arr_combSing;
							}
							arrSingle.remove(arrSingle.size()-1);
						}
						arrSingle.remove(arrSingle.size()-1);
					}
				}
				return backFloat;
			} else if (arrCombPlay.size() == 4) {
				for (int i = 0; i < arrCombPlay.get(0).size(); i++) {
					arrSingle.clear();
					arrSingle.add(arrCombPlay.get(0).get(i));
					for (int j = 0; j < arrCombPlay.get(1).size(); j++) {
						arrSingle.add(arrCombPlay.get(1).get(j));
						for (int k = 0; k < arrCombPlay.get(2).size(); k++) {
							arrSingle.add(arrCombPlay.get(2).get(k));
							for (int m = 0; m < arrCombPlay.get(3).size(); m++) {
								arrSingle.add(arrCombPlay.get(3).get(m));
								arr_combSing = combPlayWithDataInArr(
										arrSingle, spfType, rqshu);
								if (arr_combSing > backFloat) {
									backFloat = arr_combSing;
								}
								arrSingle.remove(arrSingle.size()-1);
							}
							arrSingle.remove(arrSingle.size()-1);
						}
						arrSingle.remove(arrSingle.size()-1);
					}
				}
				return backFloat;
			} 
			return backFloat;
		}catch(Exception e){
			e.printStackTrace();
			return backFloat;
		}finally{
			if(arrSingle!=null){
				arrSingle.clear();
				arrSingle = null;	
			}if(arrCombPlay!=null){
				arrCombPlay.clear();
				arrCombPlay = null;	
			}
		}
	}
	public float combPlayWithDataInArr(ArrayList<ScroeBean> combArr,
			String spfType, float rfshu) {

		if (combArr.size() == 0) {
			return 0;
		} else if (combArr.size() == 1) {
			return 0;
		} else if (combArr.size() == 2) {
			ScroeBean x = combArr.get(0);
			ScroeBean y = combArr.get(1);
			ScroeBean a = null;// sf
			ScroeBean b = null;// rfsf
			ScroeBean c = null;// sfc
			ScroeBean d = null;// dxf
			String keyX = x.getKey();
			if (keyX.contains("SF_")) {
				b = x;
			} else if (keyX.contains("HOME")||keyX.contains("GUEST")) {
				c = x;
			} else if (keyX.contains("LARGE")||keyX.contains("LITTLE")) {
				d = x;
			} else {
				a = x;
			}
			String keyY = y.getKey();
			if (keyY.contains("SF_")) {
				b = y;
			} else if (keyY.contains("HOME")||keyY.contains("GUEST")) {
				c = y;
			} else if (keyY.contains("LARGE")||keyY.contains("LITTLE")) {
				d = y;    
			} else {
				a = y;  
			}
			if (a != null && b != null) { // sf + rfsf
				BigDecimal accountA = new BigDecimal(a.getValue());
				BigDecimal accountB = new BigDecimal(b.getValue());
				if (rfshu < 0) {
					if ("3".equals(spfType)) {
						if (b.getKey().contains("SF_WIN")
								||b.getKey().contains("SF_LOSE")) {
							if(a.getKey().contains("WIN")){
								return accountA.add(accountB).floatValue();
							}
						}
					}else if ("0".equals(spfType)) {
						if (b.getKey().contains("SF_LOSE")) {
							if(a.getKey().contains("LOSE")){
								return accountA.add(accountB).floatValue();
							}
						}
					}
				} else if (rfshu > 0) {
					if ("3".equals(spfType)) {
						if (b.getKey().contains("SF_WIN")) {
							if(a.getKey().contains("WIN")){
								return accountA.add(accountB).floatValue();
							}
						}
					}else if ("0".equals(spfType)) {
						if (b.getKey().contains("SF_WIN")
								||b.getKey().contains("SF_LOSE")) {
							if(a.getKey().contains("LOSE")){
								return accountA.add(accountB).floatValue();
							}
						}
					}
				}else{
					if ("3".equals(spfType)) {
						if (b.getKey().contains("SF_WIN")) {
							if(a.getKey().contains("WIN")){
								return accountA.add(accountB).floatValue();
							}
						}
					}else if ("0".equals(spfType)) {
						if (b.getKey().contains("SF_LOSE")) {
							if(a.getKey().contains("LOSE")){
								return accountA.add(accountB).floatValue();
							}
						}
					}
				}
			} else if (a != null && c != null) {// sf + sfc
				BigDecimal accountA = new BigDecimal(a.getValue());
				BigDecimal accountC = new BigDecimal(c.getValue());
				if ("3".equals(spfType)) {
					if (a.getKey().contains("WIN")
							&&c.getKey().contains("HOME")) {
						return accountA.add(accountC).floatValue();
					}
				}else if (a.getKey().contains("LOSE")
						&&c.getKey().contains("GUEST")) {
					return accountA.add(accountC).floatValue();
				}
			} else if (a != null && d != null) {// sf + dxf
				BigDecimal accountA = new BigDecimal(a.getValue());
				BigDecimal accountD = new BigDecimal(d.getValue());
				if ("3".equals(spfType)) {
					if (a.getKey().contains("WIN")) {
						return accountA.add(accountD).floatValue();
					}
				}else if (a.getKey().contains("LOSE")) {
					return accountA.add(accountD).floatValue();
				}

			} else if (b != null && c != null) { // rfsf + sfc
				BigDecimal accountB = new BigDecimal(b.getValue());
				BigDecimal accountC = new BigDecimal(c.getValue());
				if (rfshu < 0) {
					if ("3".equals(spfType)) {
						if (b.getKey().contains("SF_WIN")
								||b.getKey().contains("SF_LOSE")) {
							if(c.getKey().contains("HOME")){
								return accountC.add(accountB).floatValue();
							}
						}
					}else if ("0".equals(spfType)) {
						if (b.getKey().contains("SF_LOSE")) {
							if(c.getKey().contains("GUEST")){
								return accountC.add(accountB).floatValue();
							}
						}
					}
				} else if (rfshu > 0) {
					if ("3".equals(spfType)) {
						if (b.getKey().contains("SF_WIN")) {
							if(c.getKey().contains("HOME")){
								return accountC.add(accountB).floatValue();
							}
						}
					}else if ("0".equals(spfType)) {
						if (b.getKey().contains("SF_WIN")
								||b.getKey().contains("SF_LOSE")) {
							if(c.getKey().contains("GUEST")){
								return accountC.add(accountB).floatValue();
							}
						}
					}
				}else{
					if ("3".equals(spfType)) {
						if (b.getKey().contains("SF_WIN")) {
							if(c.getKey().contains("HOME")){
								return accountC.add(accountB).floatValue();
							}
						}
					}else if ("0".equals(spfType)) {
						if (b.getKey().contains("SF_LOSE")) {
							if(c.getKey().contains("GUEST")){
								return accountC.add(accountB).floatValue();
							}
						}
					}
				}
			} else if (b != null && d != null) {// rfsf + dxf
				BigDecimal accountB = new BigDecimal(b.getValue());
				BigDecimal accountD = new BigDecimal(d.getValue());
				if (rfshu < 0) {
					if ("3".equals(spfType)) {
						if (b.getKey().contains("SF_WIN")
								||b.getKey().contains("SF_LOSE")) {
							return accountD.add(accountB).floatValue();
						}
					}else if ("0".equals(spfType)) {
						if (b.getKey().contains("SF_LOSE")) {
							return accountD.add(accountB).floatValue();
						}
					}
				} else if (rfshu > 0) {
					if ("3".equals(spfType)) {
						if (b.getKey().contains("SF_WIN")) {
							return accountD.add(accountB).floatValue();
						}
					}else if ("0".equals(spfType)) {
						if (b.getKey().contains("SF_WIN")
								||b.getKey().contains("SF_LOSE")) {
							return accountD.add(accountB).floatValue();
						}
					}
				}else{
					if ("3".equals(spfType)) {
						if (b.getKey().contains("SF_WIN")) {
							return accountD.add(accountB).floatValue();
						}
					}else if ("0".equals(spfType)) {
						if (b.getKey().contains("SF_LOSE")) {
							return accountD.add(accountB).floatValue();
						}
					}
				}
			} else if (c != null && d != null) { // sfc + dxf
				BigDecimal accountD = new BigDecimal(d.getValue());
				BigDecimal accountC = new BigDecimal(c.getValue());
				if ("3".equals(spfType)) {
					if (c.getKey().contains("HOME")) {
						return accountD.add(accountC).floatValue();
					}
				}else if (c.getKey().contains("GUEST")) {
					return accountD.add(accountC).floatValue();
				}
			}
		} else if (combArr.size() == 3) {
			ArrayList<ScroeBean> arr = new ArrayList<ScroeBean>();
			arr.add(combArr.get(0));
			arr.add(combArr.get(1));
			float a = combPlayWithDataInArr(arr, spfType, rfshu);
			if (a == 0) {
				return 0;
			}
			ArrayList<ScroeBean> arr2 = new ArrayList<ScroeBean>();
			arr2.add(combArr.get(0));
			arr2.add(combArr.get(2));
			float b = combPlayWithDataInArr(arr2, spfType, rfshu);
			if (b == 0) {
				return 0;
			}
			ArrayList<ScroeBean> arr3 = new ArrayList<ScroeBean>();
			arr3.add(combArr.get(1));
			arr3.add(combArr.get(2));
			float c = combPlayWithDataInArr(arr3, spfType, rfshu);
			if (c == 0) {
				return 0;
			}
			if (a > 0 && b > 0 && c > 0) {
				return (float) ((a + b + c) / 2.0);
			} else {
				return 0;
			}
		} else if (combArr.size() == 4) {
			ArrayList<ScroeBean> arr = new ArrayList<ScroeBean>();
			arr.add(combArr.get(0));
			arr.add(combArr.get(1));
			float a = combPlayWithDataInArr(arr, spfType, rfshu);
			if (a == 0) {
				return 0;
			}
			ArrayList<ScroeBean> arr1 = new ArrayList<ScroeBean>();
			arr1.add(combArr.get(0));
			arr1.add(combArr.get(2));
			float b = combPlayWithDataInArr(arr1, spfType, rfshu);
			if (b == 0) {
				return 0;
			}
			ArrayList<ScroeBean> arr2 = new ArrayList<ScroeBean>();
			arr2.add(combArr.get(0));
			arr2.add(combArr.get(3));
			float c = combPlayWithDataInArr(arr2, spfType, rfshu);
			if (c == 0) {
				return 0;
			}
			ArrayList<ScroeBean> arr3 = new ArrayList<ScroeBean>();
			arr3.add(combArr.get(1));
			arr3.add(combArr.get(2));
			float d = combPlayWithDataInArr(arr3, spfType, rfshu);
			if (d == 0) {
				return 0;
			}
			ArrayList<ScroeBean> arr4 = new ArrayList<ScroeBean>();
			arr4.add(combArr.get(1));
			arr4.add(combArr.get(3));
			float e = combPlayWithDataInArr(arr4, spfType, rfshu);
			if (e == 0) {
				return 0;
			}
			ArrayList<ScroeBean> arr5 = new ArrayList<ScroeBean>();
			arr5.add(combArr.get(2));
			arr5.add(combArr.get(3));
			float f = combPlayWithDataInArr(arr5, spfType, rfshu);
			if (f == 0) {
				return 0;
			}

			if (a > 0 && b > 0 && c > 0 && d > 0 && e > 0 && f > 0) {
				return (float) ((a + b + c + d + e + f) / 3.0);
			} else {
				return 0;
			}
		} 
		return 0;
	}

	class SortByValue implements Comparator {
		@Override
		public int compare(Object o1, Object o2) {
			float s1 = (float) o1;
			float s2 = (float) o2;
			if (s1 > s2)
				return 1;
			return -1;
		}
	}
	class SortByName implements Comparator {
		@Override
		public int compare(Object o1, Object o2) {
			int s1 = (int) o1;
			int s2 = (int) o2;
			if (s1 > s2)
				return -1;
			return 1;
		}
	}
	class SortByMatchKey implements Comparator {
		@Override
		public int compare(Object o1, Object o2) {
			long s1 = Long.parseLong(((String) o1).substring(0, 8));
			long s2 = Long.parseLong(((String) o2).substring(0, 8));
			if (s1 > s2){
				return 1;
			}else if (s1 < s2){
				return -1;
			}else{
				long s3 = Long.parseLong(((String) o1).substring(9, ((String)o1).length()));
				long s4 = Long.parseLong(((String) o2).substring(9, ((String)o2).length()));
				if (s3 > s4){
					return 1;
				}else{
					return -1;
				}	

			}		
		}
	}
	public String chooseSF(String scroe, float rf) {
		String str = "";
		String rfwin = "", rflose = "";
		if (rf < 0) {
			rfwin = "3";
			rflose = "30";
		}
		if (rf == 0) {
			rfwin = "3";
			rflose = "0";
		}
		if (rf > 0) {
			rfwin = "30";
			rflose = "0";
		}
		switch (scroe) {
		case "SF_WIN":
			str = rfwin;
			break;
		case "SF_LOSE":
			str = rflose;
			break;

		case "WIN":
			str = "3";
			break;
		case "LOSE":
			str = "0";
			break;
		case "LARGE":
			str = "30";
			break;
		case "LITTLE":
			str = "30";
			break;
		case "GUEST1_5":
			str = "0";
			break;
		case "GUEST6_10":
			str = "0";
			break;
		case "GUEST11_15":
			str = "0";
			break;
		case "GUEST16_20":
			str = "0";
			break;
		case "GUEST21_25":
			str = "0";
			break;
		case "GUEST26":
			str = "0";
			break;
		case "HOME1_5":
			str = "3";
			break;
		case "HOME6_10":
			str = "3";
			break;
		case "HOME11_15":
			str = "3";
			break;
		case "HOME16_20":
			str = "3";
			break;
		case "HOME21_25":
			str = "3";
			break;
		case "HOME26":
			str = "3";
		}
		return str;
	}


	/**
	 *  计算中奖最小金额
	 */
	public float  getMinMoney(ArrayList<Match> match){
		ArrayList<Float> minOddsArray = new ArrayList<Float>();
		ArrayList<Float> danA = new ArrayList<Float>();
		for (int i = 0; i<match.size(); i++) {
			ArrayList<Float> arr_combValue = new ArrayList<Float>();
			for(int j =0;j<match.get(i).getCheckNumber().size();j++){
				arr_combValue.add(Float.parseFloat(match.get(i).getCheckNumber().get(j).getValue()));
			}
			float backFloat = -1;
			for (Float arr_combSing : arr_combValue) {
				if (arr_combSing < backFloat||backFloat == -1) {
					backFloat = arr_combSing;
				}
			}

			if (match.get(i).isDan()) {
				danA.add(backFloat);
			}else{
				minOddsArray.add(backFloat);
			}
		}

		Collections.sort(minOddsArray, new SortByValue());

		ArrayList<Float> minSortedOddsArray = new ArrayList<Float>();
		minSortedOddsArray.addAll(danA);
		minSortedOddsArray.addAll(minOddsArray);

		if (minSortedOddsArray!=null) {
			if (getPassTypeEach().size()>0) {
				ArrayList<Integer> arr_allChuan = new ArrayList<>();
				for (int i = 0; i<getPassTypeEach().size(); i++) {
					int[] arrChuan = getPassTypeEach().get(i).getPassMatchs();
					for (int j = 0; j<arrChuan.length; j++) {
						arr_allChuan.add(arrChuan[j]);
					}
				}
				int minC = -1;
				for (int arr_combSing : arr_allChuan) {
					if (arr_combSing < minC||minC == -1) {
						minC = arr_combSing;
					}
				}
				double minMoney = 1;
				for (int i = 0; i<minC; i++) {
					minMoney = minMoney *minSortedOddsArray.get(i);
				}
				DecimalFormat    df   = new DecimalFormat("######0.00");   
				return Float.parseFloat(df.format(minMoney)+"");
			}else{
				return 0f;
			}
		}else{
			return 0f;
		}
	}
	public boolean isSingleBasket(ArrayList<Match> matchs){
		boolean isSingle = true;
		for(Match match:matchs){
			if(isSingle){
				for(ScroeBean sb:match.getCheckNumber()){
					String key = sb.getKey();
					if (key.contains("SF")) {
						if(!((BasketBallMatch)match).getMixOpen().isSingle_1()){
							isSingle = false;
							break;
						}
					} else if (key.contains("HOME")||key.contains("GUEST")) {
						if(!((BasketBallMatch)match).getMixOpen().isSingle_2()){
							isSingle = false;
							break;
						}
					} else if (key.contains("LARGE")||key.contains("LITTLE")) {
						if(!((BasketBallMatch)match).getMixOpen().isSingle_3()){
							isSingle = false;
							break;
						}
					}else{
						if(!((BasketBallMatch)match).getMixOpen().isSingle_0()){
							isSingle = false;
							break;
						}
					}
				}

			}
		}
		return isSingle;
	}
	public String isHaveSFC(ArrayList<ScroeBean> has){
		StringBuffer sfcStr = new StringBuffer();
		for(ScroeBean sb:has){
			switch(sb.getKey()){
			case "GUEST1_5":
				sfcStr.append("客胜1-5,");
				break;
			case "GUEST6_10":
				sfcStr.append("客胜6-10,");
				break;
			case "GUEST11_15":
				sfcStr.append("客胜11-15,");
				break;
			case "GUEST16_20":
				sfcStr.append("客胜16-20,");
				break;
			case "GUEST21_25":
				sfcStr.append("客胜21-25,");
				break;
			case "GUEST26":
				sfcStr.append("客胜26+,");
				break;

			case "HOME1_5":
				sfcStr.append("主胜1-5,");
				break;
			case "HOME6_10":
				sfcStr.append("主胜6-10,");
				break;
			case "HOME11_15":
				sfcStr.append("主胜11-15,");
				break;
			case "HOME16_20":
				sfcStr.append("主胜16-20,");
				break;
			case "HOME21_25":
				sfcStr.append("主胜21-25,");
				break;
			case "HOME26":
				sfcStr.append("主胜26+,");
				break;
			}
		}
		return sfcStr.toString();
	}
}
