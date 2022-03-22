package com.cshen.tiyu.activity.lottery.ball.football;


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
import android.graphics.Color;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.cshen.tiyu.activity.lottery.ball.football.item.Dictionary;
import com.cshen.tiyu.activity.lottery.ball.util.PassType2;
import com.cshen.tiyu.activity.lottery.ball.util.PeiLv;
import com.cshen.tiyu.activity.lottery.dlt.ChooseUtil;
import com.cshen.tiyu.domain.ball.FootBallMatch;
import com.cshen.tiyu.domain.ball.Match;
import com.cshen.tiyu.domain.ball.PassTypeEach;
import com.cshen.tiyu.domain.ball.ScroeBean;

public class JCZQUtil {
	PassType2 chuanguan = new PassType2();
	ArrayList<PassTypeEach> chuansChoosed;

	ArrayList<FootBallMatch> matchs;
	public static JCZQUtil jczqUtil;
	HashMap _cacheMaxOdds;
	int editFlag;

	public static JCZQUtil getJCZQUtil() {
		if (jczqUtil == null) {
			jczqUtil = new JCZQUtil();
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
	public String isHaveBF(ArrayList<ScroeBean> has){
		StringBuffer sfcStr = new StringBuffer();
		for(ScroeBean sb:has){
			sfcStr.append(chooseScroeName(sb.getKey()));
		}
		return sfcStr.toString();
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
							FootBallMatch sectionInfo = (FootBallMatch) match.get(k);
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
		case "WINSPF":
			str = "主胜;";
			break;
		case "DRAWSPF":
			str = "平;";
			break;
		case "LOSESPF":
			str = "客胜;";
			break;
		case "WINRQSPF":
			str = "让球主胜;";
			break;
		case "DRAWRQSPF":
			str = "让球平;";
			break;
		case "LOSERQSPF":
			str = "让球客胜;";
			break;
		case "DRAW00":
			str = "0:0;";
			break;
		case "DRAW11":
			str = "1:1;";
			break;
		case "DRAW22":
			str = "2:2;";
			break;
		case "DRAW33":
			str = "3:3;";
			break;
		case "DRAW_OTHER":
			str = "平其他;";
			break;
		case "WIN10":
			str = "1:0;";
			break;
		case "WIN20":
			str = "2:0;";
			break;
		case "WIN21":
			str = "2:1;";
			break;
		case "WIN30":
			str = "3:0;";
			break;
		case "WIN31":
			str = "3:1;";
			break;
		case "WIN32":
			str = "3:2;";
			break;
		case "WIN40":
			str = "4:0;";
			break;
		case "WIN41":
			str = "4:1;";
			break;
		case "WIN42":
			str = "4:2;";
			break;
		case "WIN50":
			str = "5:0;";
			break;
		case "WIN51":
			str = "5:1;";
			break;
		case "WIN52":
			str = "5:2;";
			break;
		case "WIN_OTHER":
			str = "胜其他;";
			break;
		case "LOSE01":
			str = "0:1;";
			break;
		case "LOSE02":
			str = "0:2;";
			break;
		case "LOSE12":
			str = "1:2;";
			break;
		case "LOSE03":
			str = "0:3;";
			break;
		case "LOSE13":
			str = "1:3;";
			break;
		case "LOSE23":
			str = "2:3;";
			break;
		case "LOSE04":
			str = "0:4;";
			break;
		case "LOSE14":
			str = "1:4;";
			break;
		case "LOSE24":
			str = "2:4;";
			break;
		case "LOSE05":
			str = "0:5;";
			break;
		case "LOSE15":
			str = "1:5;";
			break;
		case "LOSE25":
			str = "2:5;";
			break;
		case "LOSE_OTHER":
			str = "负其他;";
			break;
		case "WIN_WIN":
			str = "胜胜;";
			break;
		case "WIN_DRAW":
			str = "胜平;";
			break;
		case "WIN_LOSE":
			str = "胜负;";
			break;
		case "DRAW_DRAW":
			str = "平平;";
			break;
		case "DRAW_WIN":
			str = "平胜;";
			break;
		case "DRAW_LOSE":
			str = "平负;";
			break;
		case "LOSE_LOSE":
			str = "负负;";
			break;
		case "LOSE_WIN":
			str = "负胜;";
			break;
		case "LOSE_DRAW":
			str = "负平;";
			break;
		case "S0":
			str = "0球;";
			break;
		case "S1":
			str = "1球;";
			break;
		case "S2":
			str = "2球;";
			break;
		case "S3":
			str = "3球;";
			break;
		case "S4":
			str = "4球;";
			break;
		case "S5":
			str = "5球;";
			break;
		case "S6":
			str = "6球;";
			break;
		case "S7":
			str = "7+球;";
			break;
		}
		return str;
	}

	public static String chooseScroeValue(String scroe) {
		String str = "";
		switch (scroe) {
		case "WINRQSPF":
			str = 3 + "";
			break;
		case "DRAWRQSPF":
			str = 1 + "";
			break;
		case "LOSERQSPF":
			str = 0 + "";
			break;
		case "WINSPF":
			str = 3 + "";
			break;
		case "DRAWSPF":
			str = 1 + "";
			break;
		case "LOSESPF":
			str = 0 + "";
			break;
		case "S0":
			str = 0 + "";
			break;
		case "S1":
			str = 1 + "";
			break;
		case "S2":
			str = 2 + "";
			break;
		case "S3":
			str = 3 + "";
			break;
		case "S4":
			str = 4 + "";
			break;
		case "S5":
			str = 5 + "";
			break;
		case "S6":
			str = 6 + "";
			break;
		case "S7":
			str = 7 + "";
			break;
		case "DRAW00":
			str = "00";
			break;
		case "DRAW11":
			str = "11";
			break;
		case "DRAW22":
			str = "22";
			break;
		case "DRAW33":
			str = "33";
			break;
		case "DRAW_OTHER":
			str = "DD";
			break;
		case "WIN10":
			str = "10";
			break;
		case "WIN20":
			str = "20";
			break;
		case "WIN21":
			str = "21";
			break;
		case "WIN30":
			str = "30";
			break;
		case "WIN31":
			str = "31";
			break;
		case "WIN32":
			str = "32";
			break;
		case "WIN40":
			str = "40";
			break;
		case "WIN41":
			str = "41";
			break;
		case "WIN42":
			str = "42";
			break;
		case "WIN50":
			str = "50";
			break;
		case "WIN51":
			str = "51";
			break;
		case "WIN52":
			str = "52";
			break;
		case "WIN_OTHER":
			str = "WW";
			break;
		case "LOSE01":
			str = "01";
			break;
		case "LOSE02":
			str = "02";
			break;
		case "LOSE12":
			str = "12";
			break;
		case "LOSE03":
			str = "03";
			break;
		case "LOSE13":
			str = "13";
			break;
		case "LOSE23":
			str = "23";
			break;
		case "LOSE04":
			str = "04";
			break;
		case "LOSE14":
			str = "14";
			break;
		case "LOSE24":
			str = "24";
			break;
		case "LOSE05":
			str = "05";
			break;
		case "LOSE15":
			str = "15";
			break;
		case "LOSE25":
			str = "25";
			break;
		case "LOSE_OTHER":
			str = "LL";
			break;
		case "WIN_WIN":
			str = "33";
			break;
		case "WIN_DRAW":
			str = "31";
			break;
		case "WIN_LOSE":
			str = "30";
			break;
		case "DRAW_DRAW":
			str = "11";
			break;
		case "DRAW_WIN":
			str = "13";
			break;
		case "DRAW_LOSE":
			str = "10";
			break;
		case "LOSE_LOSE":
			str = "00";
			break;
		case "LOSE_WIN":
			str = "03";
			break;
		case "LOSE_DRAW":
			str = "01";
			break;
		}
		return str;
	}

	public int getPlayTypeItem(String key) {// 0,胜平负。1,进球数。2,比分。3,半全场。5,让球胜平负
		int str = -1;
		if (key.contains("RQSPF")) {
			str = 5;
		} else if (key.contains("SPF")) {
			str = 0;
		} else if (key.contains("S") && !key.contains("LOSE")) {
			str = 1;
		} else if (key.contains("_") && !key.contains("OTHER")) {
			str = 3;
		} else {
			str = 2;
		}
		return str;
	}
	public String getPlayTypeString(String key) {// 0,胜平负。1,进球数。2,比分。3,半全场。5,让球胜平负
		String str = "";
		if (key.contains("RQSPF")) {
			str = "RQSPF";
		} else if (key.contains("SPF")) {
			str = "SPF";
		} else if (key.contains("S") && !key.contains("LOSE")) {
			str = "JQS";
		} else if (key.contains("_") && !key.contains("OTHER")) {
			str = "BQQ";
		} else {
			str = "BF";
		}
		return str;
	}

	public void setHuancun() {
		if(_cacheMaxOdds == null){
			_cacheMaxOdds = new HashMap<String,Float>();
		}
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
			ArrayList<ScroeBean> draw = new ArrayList<ScroeBean>();
			ArrayList<ScroeBean> lose = new ArrayList<ScroeBean>();
			String rqstr = match.get(i).getHandicap();
			int rq = 0;
			try {
				rq = Integer.parseInt(rqstr);
			} catch (Exception e) {
				e.printStackTrace();
				rq = 0;
			}

			// 循环一场比赛的所有赔率，赔率按spf分组
			for (int j = 0; j < choose.size(); j++) {
				String peilvStr = this.chooseSPF(choose.get(j).getKey(), rq);
				if (peilvStr.contains("3")) {
					win.add(choose.get(j));
				}
				if (peilvStr.contains("1")) {
					draw.add(choose.get(j));
				}
				if (peilvStr.contains("0")) {
					lose.add(choose.get(j));
				}
			}
			PeiLv peilv = new PeiLv();
			peilv.setMatchKey(matchKey);
			peilv.setRqshu(rq);
			peilv.setWin(win);
			peilv.setDraw(draw);
			peilv.setLose(lose);

			allMatchOddsArray.add(peilv);
		}
		// 更新缓存标记
		editFlag = 1;
		// 开始按allMatchOddsArray 返回 数组中的数据计算最大组合赔率
		return calcuateMaxOddsComb(allMatchOddsArray,match);
	}

	public float calcuateMaxOddsComb(ArrayList<PeiLv> arrAllMatchOdds,ArrayList<Match> match) {
		int matchCount = match.size();

		ArrayList<ScroeBean> spf = new ArrayList<ScroeBean>();
		ArrayList<ScroeBean> rqspf = new ArrayList<ScroeBean>();
		ArrayList<ScroeBean> bifen = new ArrayList<ScroeBean>();
		ArrayList<ScroeBean> jqs = new ArrayList<ScroeBean>();
		ArrayList<ScroeBean> bqc = new ArrayList<ScroeBean>();
		ArrayList<ScroeBean> win = null;
		ArrayList<ScroeBean> draw = null;
		ArrayList<ScroeBean> lose = null;
		ArrayList<ScroeBean> arr = null;
		HashMap wanfa = new HashMap<String, ArrayList<ScroeBean>>();
		ArrayList<Float> arr_spfValue = new ArrayList<>();
		ArrayList<Float> arr_allMaxValue = new ArrayList<>();
		ArrayList<String> cacheKeysArray = new ArrayList<>();
		ArrayList<Integer> danWeiZhi = new ArrayList<>();
		try{
			_cacheMaxOdds.clear();
			for (int i = 0; i < arrAllMatchOdds.size(); i++) {// 循环所有赛事
				arr_spfValue.clear();
				PeiLv peilv = arrAllMatchOdds.get(i);
				int rqshu = peilv.getRqshu();
				if(win!=null){
					win.clear();
				}
				win = peilv.getWin();
				if(draw!=null){
					draw.clear();
				}
				draw = peilv.getDraw();
				if(lose!=null){
					lose.clear();
				}
				lose = peilv.getLose();

				String keySPF = "";
				for (int k = 0; k < 3; k++) {
					spf.clear();
					rqspf.clear();
					bifen.clear();
					jqs.clear();
					bqc.clear();
					if (k == 0) {
						arr = win;
						keySPF = "3";
					}
					if (k == 1) {
						arr = draw;
						keySPF = "1";
					}
					if (k == 2) {
						arr = lose;
						keySPF = "0";
					}
					wanfa.clear();
					for (int j = 0; j < arr.size(); j++) {
						String key = arr.get(j).getKey();
						if (key.contains("RQSPF")) {// 让球
							rqspf.add(arr.get(j));
						} else if (key.contains("SPF")) {
							spf.add(arr.get(j));
						} else if (key.contains("S") && !key.contains("LOSE")) {// 进球数
							jqs.add(arr.get(j));
						} else if (key.contains("_") && !key.contains("OTHER")) {// 半全场
							bqc.add(arr.get(j));
						} else {// 比分
							bifen.add(arr.get(j));
						}
					}
					if(spf!=null&&spf.size()>0){
						wanfa.put("SPF", spf);
					}if(rqspf!=null&&rqspf.size()>0){
						wanfa.put("RQSPF",rqspf);
					}if(bifen!=null&&bifen.size()>0){
						wanfa.put("BF",bifen);
					}if(jqs!=null&&jqs.size()>0){
						wanfa.put("JQS",jqs);
					}if(bqc!=null&&bqc.size()>0){
						wanfa.put("BQQ",bqc);
					}
					arr_spfValue.add(meanWhileHappen(wanfa, keySPF, rqshu));
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

				totalMoney =totalMoney*2;
				DecimalFormat    df   = new DecimalFormat("######0.00");   
				return Float.parseFloat(df.format(totalMoney)+"");
			} else {
				return 0.0f;
			}
		}
		catch(Exception e){
			e.printStackTrace();
			return 0.0f;
		}finally{
			if(spf!=null){
				spf.clear();spf = null;
			}
			if(rqspf!=null){
				rqspf.clear();rqspf = null;
			}
			if(bifen!=null){
				bifen.clear();bifen = null;
			}
			if(jqs!=null){
				jqs.clear();jqs = null;
			}
			if(bqc!=null){
				bqc.clear();bqc = null;
			}
			if(win!=null){
				win.clear();win = null;
			}
			if(draw!=null){
				draw.clear();draw = null;
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

	public float meanWhileHappen(HashMap dicGroupedOdds, String spfType,int rqshu) {
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
			String spfType, int rqshu) {
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
			} else if (arrCombPlay.size() == 5) {
				for (int i = 0; i < arrCombPlay.get(0).size(); i++) {
					arrSingle.clear();
					arrSingle.add(arrCombPlay.get(0).get(i));
					for (int j = 0; j < arrCombPlay.get(1).size(); j++) {
						arrSingle.add(arrCombPlay.get(1).get(j));
						for (int k = 0; k < arrCombPlay.get(2).size(); k++) {
							arrSingle.add(arrCombPlay.get(2).get(k));
							for (int m = 0; m < arrCombPlay.get(3).size(); m++) {
								arrSingle.add(arrCombPlay.get(3).get(m));
								for (int n = 0; n < arrCombPlay.get(4).size(); n++) {
									arrSingle.add(arrCombPlay.get(4).get(n));
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
	public float calculatePlayComb2(ArrayList<ArrayList<ScroeBean>> arrCombPlay,
			String spfType, int rqshu) {
		float backFloat = 0;
		ArrayList<ScroeBean>  arrSingle = new ArrayList<ScroeBean>();
		ArrayList<ArrayList<ScroeBean>> arrAllComb = new ArrayList<ArrayList<ScroeBean>>();
		ArrayList<Float> arr_combSingleValue = new ArrayList<Float>();
		ArrayList<ScroeBean>  arrSingle2 = new ArrayList<ScroeBean>();
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
				arrAllComb.clear();
				for (int i = 0; i < arrCombPlay.get(0).size(); i++) {
					arrSingle.clear();
					arrSingle.add(arrCombPlay.get(0).get(i));
					for (int j = 0; j < arrCombPlay.get(1).size(); j++) {
						arrSingle.add(arrCombPlay.get(1).get(j));								
						if(arrSingle2!=null){
							arrSingle2.clear();
						}
						arrSingle2.addAll(arrSingle);
						arrAllComb.add(arrSingle2);
						arrSingle.remove(arrSingle.size()-1);
					}
				}
				for (int i = 0; i < arrAllComb.size(); i++) {				
					arr_combSingleValue.add(combPlayWithDataInArr(
							arrAllComb.get(i), spfType, rqshu));
				}
				for (Float arr_combSing : arr_combSingleValue) {
					if (arr_combSing > backFloat) {
						backFloat = arr_combSing;
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

							if(arrSingle2!=null){
								arrSingle2.clear();
							}
							arrSingle2.addAll(arrSingle);
							arrAllComb.add(arrSingle2);
							arrSingle.remove(arrSingle.size()-1);
						}
						arrSingle.remove(arrSingle.size()-1);
					}
				}
				for (int i = 0; i < arrAllComb.size(); i++) {
					arr_combSingleValue.add(combPlayWithDataInArr(
							arrAllComb.get(i), spfType, rqshu));
				}
				for (Float arr_combSing : arr_combSingleValue) {
					if (arr_combSing > backFloat) {
						backFloat = arr_combSing;
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

								if(arrSingle2!=null){
									arrSingle2.clear();
								}
								arrSingle2.addAll(arrSingle);
								arrAllComb.add(arrSingle2);
								arrSingle.remove(arrSingle.size()-1);
							}
							arrSingle.remove(arrSingle.size()-1);
						}
						arrSingle.remove(arrSingle.size()-1);
					}
				}
				for (int i = 0; i < arrAllComb.size(); i++) {
					arr_combSingleValue.add(combPlayWithDataInArr(
							arrAllComb.get(i), spfType, rqshu));
				}
				for (Float arr_combSing : arr_combSingleValue) {
					if (arr_combSing > backFloat) {
						backFloat = arr_combSing;
					}
				}
				return backFloat;
			} else if (arrCombPlay.size() == 5) {
				for (int i = 0; i < arrCombPlay.get(0).size(); i++) {
					arrSingle.clear();
					arrSingle.add(arrCombPlay.get(0).get(i));
					for (int j = 0; j < arrCombPlay.get(1).size(); j++) {
						arrSingle.add(arrCombPlay.get(1).get(j));
						for (int k = 0; k < arrCombPlay.get(2).size(); k++) {
							arrSingle.add(arrCombPlay.get(2).get(k));
							for (int m = 0; m < arrCombPlay.get(3).size(); m++) {
								arrSingle.add(arrCombPlay.get(3).get(m));
								for (int n = 0; n < arrCombPlay.get(4).size(); m++) {
									arrSingle.add(arrCombPlay.get(4).get(n));
									if(arrSingle2!=null){
										arrSingle2.clear();
									}
									arrSingle2.addAll(arrSingle);
									arrAllComb.add(arrSingle2);
									arrSingle.remove(arrSingle.size()-1);
								}
								arrSingle.remove(arrSingle.size()-1);
							}
							arrSingle.remove(arrSingle.size()-1);
						}
						arrSingle.remove(arrSingle.size()-1);
					}
				}
				for (int i = 0; i < arrAllComb.size(); i++) {
					arr_combSingleValue.add(combPlayWithDataInArr(
							arrAllComb.get(i), spfType, rqshu));
				}
				for (Float arr_combSing : arr_combSingleValue) {
					if (arr_combSing > backFloat) {
						backFloat = arr_combSing;
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
			}
			if(arrAllComb!=null){
				arrAllComb.clear();
				arrAllComb = null;
			}
			if(arr_combSingleValue!=null){
				arr_combSingleValue.clear();
				arr_combSingleValue = null;	
			}
			if(arrSingle2!=null){
				arrSingle2.clear();
				arrSingle2 = null;	
			}
		}
	}

	public float combPlayWithDataInArr(ArrayList<ScroeBean> combArr,
			String spfType, int rqshu) {

		if (combArr.size() == 0) {
			return 0;
		} else if (combArr.size() == 1) {
			return 0;
		} else if (combArr.size() == 2) {
			ScroeBean x = combArr.get(0);
			ScroeBean y = combArr.get(1);
			ScroeBean a = null;// rqspf
			ScroeBean b = null;// bqc
			ScroeBean c = null;// bf
			ScroeBean d = null;// jqs
			ScroeBean e = null;// spf
			String keyX = x.getKey();
			if (keyX.contains("RQSPF")) {
				a = x;
			} else if (keyX.contains("SPF")) {
				e = x;
			} else if (keyX.contains("S") && !keyX.contains("LOSE")) {
				d = x;
			} else if (keyX.contains("_") && !keyX.contains("OTHER")) {
				b = x;
			} else {
				c = x;
			}
			String keyY = y.getKey();
			if (keyY.contains("RQSPF")) {
				a = y;
			}else if (keyY.contains("SPF")) {
				e = y;
			}else if (keyY.contains("S") && !keyY.contains("LOSE")) {
				d = y;
			} else if (keyY.contains("_") && !keyY.contains("OTHER")) {
				b = y;
			} else {
				c = y;
			}

			int absScore = Math.abs(rqshu);
			if (e != null) { // spf + 其它
				if (a != null) {
					BigDecimal accountA = new BigDecimal(a.getValue());
					BigDecimal accountE = new BigDecimal(e.getValue());
					if (rqshu < 0) {
						if ("3".equals(spfType)) {
							if ("WINRQSPF".equals(a.getKey())
									|| "DRAWRQSPF".equals(a.getKey())) {
								return accountA.add(accountE).floatValue();
							}
						} else if ("1".equals(spfType)) {
							if ("LOSERQSPF".equals(a.getKey())) {
								return accountA.add(accountE).floatValue();
							}
						} else if ("0".equals(spfType)) {
							if ("LOSERQSPF".equals(a.getKey())) {
								return accountA.add(accountE).floatValue();
							}
						}
					} else {
						if ("3".equals(spfType)) {
							if ("WINRQSPF".equals(a.getKey())) {
								return accountA.add(accountE).floatValue();
							}
						} else if ("1".equals(spfType)) {
							if ("WINRQSPF".equals(a.getKey())) {
								return accountA.add(accountE).floatValue();
							}
						} else if ("0".equals(spfType)) {
							if ("LOSERQSPF".equals(a.getKey())
									|| "DRAWRQSPF".equals(a.getKey())) {
								return accountA.add(accountE).floatValue();
							}
						}
					}
				} else {
					BigDecimal accountA = new BigDecimal(x.getValue());
					BigDecimal accountE = new BigDecimal(y.getValue());
					return accountA.add(accountE).floatValue();
				}
			}
			if (a != null && b != null) { // rqspf + bqc
				BigDecimal accountA = new BigDecimal(a.getValue());
				BigDecimal accountB = new BigDecimal(b.getValue());
				if (rqshu < 0) {
					if ("3".equals(spfType)) {
						if ("WINRQSPF".equals(a.getKey())
								|| "DRAWRQSPF".equals(a.getKey())) {
							return accountA.add(accountB).floatValue();
						}
						if (rqshu < -1 && "LOSERQSPF".equals(a.getKey())) {
							return accountA.add(accountB).floatValue();
						}
					} else if ("1".equals(spfType)) {
						if ("LOSERQSPF".equals(a.getKey())) {
							return accountA.add(accountB).floatValue();
						}
					} else if ("0".equals(spfType)) {
						if ("LOSERQSPF".equals(a.getKey())) {
							return accountA.add(accountB).floatValue();
						}
					}
				} else {
					if ("3".equals(spfType)) {
						if ("WINRQSPF".equals(a.getKey())) {
							return accountA.add(accountB).floatValue();
						}
					} else if ("1".equals(spfType)) {
						if ("WINRQSPF".equals(a.getKey())) {
							return accountA.add(accountB).floatValue();
						}
					} else if ("0".equals(spfType)) {
						if ("LOSERQSPF".equals(a.getKey())
								|| "DRAWRQSPF".equals(a.getKey())) {
							return accountA.add(accountB).floatValue();
						}
						if (rqshu > 1 && "DRAWRQSPF".equals(a.getKey())) {
							return accountA.add(accountB).floatValue();
						}
					}
				}
			} else if (a != null && c != null) {
				Dictionary bf = cutBF(c);
				int visit = bf.getVisit();
				int host = bf.getHost();
				BigDecimal accountA = new BigDecimal(a.getValue());
				BigDecimal accountC = new BigDecimal(c.getValue());
				if (rqshu < 0) {
					if ("3".equals(spfType)) {
						if ("WIN_OTHER".equals(c.getKey())
								&& 
								("WINRQSPF".equals(a.getKey()) || "DRAWRQSPF".equals(a.getKey()))
								) {
							return accountA.add(accountC).floatValue();
						} else if ("WIN_OTHER".equals(c.getKey())
								&& "DRAWRQSPF".equals(a.getKey()) && rqshu <= -5) {
							return accountA.add(accountC).floatValue();
						} else if (rqshu < -1 && "LOSERQSPF".equals(a.getKey())
								&& host > visit && ((host - absScore)< visit)) {
							return accountA.add(accountC).floatValue();
						} else if ("WINRQSPF".equals(a.getKey())
								&& ((host - visit) > absScore)) {
							return accountA.add(accountC).floatValue();
						} else if ("DRAWRQSPF".equals(a.getKey())
								&& ((host - visit) == absScore)) {
							return accountA.add(accountC).floatValue();
						}
					} else {
						if ("LOSERQSPF".equals(a.getKey())) {
							return accountA.add(accountC).floatValue();
						}
					}
				} else {
					if ("0".equals(spfType)) {
						if ("LOSE_OTHER".equals(c.getKey())
								&& ("LOSERQSPF".equals(a.getKey()) || "DRAWRQSPF"
										.equals(a.getKey()))) {
							return accountA.add(accountC).floatValue();
						} else if ("LOSE_OTHER".equals(c.getKey())
								&& "WINRQSPF".equals(a.getKey()) && rqshu >= 5) {
							return accountA.add(accountC).floatValue();
						} else if (rqshu > 1 && "WINRQSPF".equals(a.getKey())
								&& host < visit && ((visit - absScore) < host)) {
							return accountA.add(accountC).floatValue();
						} else if ("LOSERQSPF".equals(a.getKey())
								&& ((visit - host) > absScore)) {
							return accountA.add(accountC).floatValue();
						} else if ("DRAWRQSPF".equals(a.getKey())
								&& ((visit - host) == absScore)) {
							return accountA.add(accountC).floatValue();
						}
					} else {
						if ("WINRQSPF".equals(a.getKey())) {
							return accountA.add(accountC).floatValue();
						}
					}
				}

			} else if (a != null && d != null) {// rqspf + jqs
				BigDecimal accountA = new BigDecimal(a.getValue());
				BigDecimal accountD = new BigDecimal(d.getValue());
				// 7+球不和rqspf冲突
				Integer jqsNum = cutJQSNum(d);
				if (jqsNum == 7) {
					return accountA.add(accountD).floatValue();
				}
				if (rqshu < 0) {
					if ("3".equals(spfType)) {
						if ("WINRQSPF".equals(a.getKey()) && jqsNum > absScore) {
							return accountA.add(accountD).floatValue();
						} else if ("DRAWRQSPF".equals(a.getKey())
								&& (jqsNum + absScore) % 2 == 0
								&& jqsNum >= absScore) {
							return accountA.add(accountD).floatValue();
						} else if (rqshu == -2 && "LOSERQSPF".equals(a.getKey())
								&& jqsNum % 2 > 0) {
							return accountA.add(accountD).floatValue();
						} else if (rqshu < -2 && "LOSERQSPF".equals(a.getKey())
								&& jqsNum > 0) {
							return accountA.add(accountD).floatValue();
						}
					} else if ("1".equals(spfType)) {
						if (jqsNum % 2 == 0 && "LOSERQSPF".equals(a.getKey())) {
							return accountA.add(accountD).floatValue();
						}
					} else {
						if ("LOSERQSPF".equals(a.getKey())) {
							return accountA.add(accountD).floatValue();
						}
					}
				} else {
					if ("3".equals(spfType)) {
						if ("WINRQSPF".equals(a.getKey())) {
							return accountA.add(accountD).floatValue();
						}
					} else if ("1".equals(spfType)) {
						if (jqsNum % 2 == 0 && "WINRQSPF".equals(a.getKey())) {
							return accountA.add(accountD).floatValue();
						}
					} else {
						if ("LOSERQSPF".equals(a.getKey()) && jqsNum > absScore) {
							return accountA.add(accountD).floatValue();
						} else if ("DRAWRQSPF".equals(a.getKey())
								&& (jqsNum + absScore) % 2 == 0
								&& jqsNum >= absScore) {
							return accountA.add(accountD).floatValue();
						} else if (rqshu == 2 && "WINRQSPF".equals(a.getKey())
								&& jqsNum % 2 > 0) {
							return accountA.add(accountD).floatValue();
						} else if (rqshu > 2 && "WINRQSPF".equals(a.getKey())
								&& jqsNum > 0) {
							return accountA.add(accountD).floatValue();
						}
					}
				}
			} else if (b != null && c != null) { // bqc + bf
				Dictionary bf = cutBF(c);
				int visit = bf.getVisit();
				int host = bf.getHost();
				BigDecimal accountB = new BigDecimal(b.getValue());
				BigDecimal accountC = new BigDecimal(c.getValue());
				if ("WIN_OTHER".equals(c.getKey())
						|| "LOSE_OTHER".equals(c.getKey())
						|| "DRAW_OTHER".equals(c.getKey())) {
					return accountB.add(accountC).floatValue();
				}
				if ("3".equals(spfType)) {
					if ("LOSE_WIN".equals(b.getKey()) && visit > 0) {
						return accountB.add(accountC).floatValue();
					} else if ("WIN_WIN".equals(b.getKey())
							|| "DRAW_WIN".equals(b.getKey())) {

						return accountB.add(accountC).floatValue();
					}
				} else if ("1".equals(spfType)) {
					if (("WIN_DRAW".equals(b.getKey()) || "LOSE_DRAW".equals(b
							.getKey())) && visit > 0 && host > 0) {
						return accountB.add(accountC).floatValue();
					} else if ("DRAW_DRAW".equals(b.getKey())) {
						return accountB.add(accountC).floatValue();
					}
				} else {
					if ("WIN_LOSE".equals(b.getKey()) && host > 0) {
						return accountB.add(accountC).floatValue();
					} else if ("LOSE_LOSE".equals(b.getKey())
							|| "DRAW_LOSE".equals(b.getKey())) {
						return accountB.add(accountC).floatValue();
					}
				}
			} else if (b != null && d != null) {// bqc + jqs
				BigDecimal accountB = new BigDecimal(b.getValue());
				BigDecimal accountD = new BigDecimal(d.getValue());
				int jqsNum = cutJQSNum(d);
				if ("3".equals(spfType)) {
					if ("LOSE_WIN".equals(b.getKey()) && jqsNum > 2) {
						return accountB.add(accountD).floatValue();
					} else if (("WIN_WIN".equals(b.getKey()) || "DRAW_WIN"
							.equals(b.getKey())) && jqsNum > 0) {
						return accountB.add(accountD).floatValue();
					}
				} else if ("1".equals(spfType)) {
					if (jqsNum == 7) {
						return accountB.add(accountD).floatValue();
					} else if ("DRAW_DRAW".equals(b.getKey()) && jqsNum == 0) {
						return accountB.add(accountD).floatValue();
					} else if (("WIN_DRAW".equals(b.getKey()) || "LOSE_DRAW"
							.equals(b.getKey()))
							&& jqsNum % 2 == 0
							&& jqsNum > 0) {
						return accountB.add(accountD).floatValue();
					}
				} else {
					if ("WIN_LOSE".equals(b.getKey()) && jqsNum > 2) {
						return accountB.add(accountD).floatValue();
					} else if (("LOSE_LOSE".equals(b.getKey()) || "DRAW_LOSE"
							.equals(b.getKey())) && jqsNum > 0) {
						return accountB.add(accountD).floatValue();
					}
				}
			} else if (c != null && d != null) { // bf + jqs
				int jqsNum = cutJQSNum(d);
				Dictionary bf = cutBF(c);
				int visit = bf.getVisit();
				int host = bf.getHost();
				BigDecimal accountC = new BigDecimal(c.getValue());
				BigDecimal accountD = new BigDecimal(d.getValue());
				if (("WIN_OTHER".equals(c.getKey())
						|| "LOSE_OTHER".equals(c.getKey())
						|| "DRAW_OTHER".equals(c.getKey())
						|| c.getKey().endsWith("25") || c.getKey().endsWith(
								"52"))
								&& jqsNum == 7) {
					return accountC.add(accountD).floatValue();
				} else if (host + visit == jqsNum) {
					return accountC.add(accountD).floatValue();
				}
			}
		} else if (combArr.size() == 3) {
			ArrayList<ScroeBean> arr = new ArrayList<ScroeBean>();
			arr.add(combArr.get(0));
			arr.add(combArr.get(1));
			float a = combPlayWithDataInArr(arr, spfType, rqshu);
			if (a == 0) {
				return 0;
			}
			ArrayList<ScroeBean> arr2 = new ArrayList<ScroeBean>();
			arr2.add(combArr.get(0));
			arr2.add(combArr.get(2));
			float b = combPlayWithDataInArr(arr2, spfType, rqshu);
			if (b == 0) {
				return 0;
			}
			ArrayList<ScroeBean> arr3 = new ArrayList<ScroeBean>();
			arr3.add(combArr.get(1));
			arr3.add(combArr.get(2));
			float c = combPlayWithDataInArr(arr3, spfType, rqshu);
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
			float a = combPlayWithDataInArr(arr, spfType, rqshu);
			if (a == 0) {
				return 0;
			}
			ArrayList<ScroeBean> arr1 = new ArrayList<ScroeBean>();
			arr1.add(combArr.get(0));
			arr1.add(combArr.get(2));
			float b = combPlayWithDataInArr(arr1, spfType, rqshu);
			if (b == 0) {
				return 0;
			}
			ArrayList<ScroeBean> arr2 = new ArrayList<ScroeBean>();
			arr2.add(combArr.get(0));
			arr2.add(combArr.get(3));
			float c = combPlayWithDataInArr(arr2, spfType, rqshu);
			if (c == 0) {
				return 0;
			}
			ArrayList<ScroeBean> arr3 = new ArrayList<ScroeBean>();
			arr3.add(combArr.get(1));
			arr3.add(combArr.get(2));
			float d = combPlayWithDataInArr(arr3, spfType, rqshu);
			if (d == 0) {
				return 0;
			}
			ArrayList<ScroeBean> arr4 = new ArrayList<ScroeBean>();
			arr4.add(combArr.get(1));
			arr4.add(combArr.get(3));
			float e = combPlayWithDataInArr(arr4, spfType, rqshu);
			if (e == 0) {
				return 0;
			}
			ArrayList<ScroeBean> arr5 = new ArrayList<ScroeBean>();
			arr5.add(combArr.get(2));
			arr5.add(combArr.get(3));
			float f = combPlayWithDataInArr(arr5, spfType, rqshu);
			if (f == 0) {
				return 0;
			}

			if (a > 0 && b > 0 && c > 0 && d > 0 && e > 0 && f > 0) {
				return (float) ((a + b + c + d + e + f) / 3.0);
			} else {
				return 0;
			}
		} else if (combArr.size() == 5) {
			ArrayList<ScroeBean> arr = new ArrayList<ScroeBean>();
			arr.add(combArr.get(0));
			arr.add(combArr.get(1));
			float a = combPlayWithDataInArr(arr, spfType, rqshu);
			if (a == 0) {
				return 0;
			}

			ArrayList<ScroeBean> arr1 = new ArrayList<ScroeBean>();
			arr1.add(combArr.get(0));
			arr1.add(combArr.get(2));
			float b = combPlayWithDataInArr(arr1, spfType, rqshu);
			if (b == 0) {
				return 0;
			}
			ArrayList<ScroeBean> arr2 = new ArrayList<ScroeBean>();
			arr2.add(combArr.get(0));
			arr2.add(combArr.get(3));
			float c = combPlayWithDataInArr(arr2, spfType, rqshu);
			if (c == 0) {
				return 0;
			}
			ArrayList<ScroeBean> arr3 = new ArrayList<ScroeBean>();
			arr3.add(combArr.get(0));
			arr3.add(combArr.get(4));
			float d = combPlayWithDataInArr(arr3, spfType, rqshu);
			if (d == 0) {
				return 0;
			}
			ArrayList<ScroeBean> arr4 = new ArrayList<ScroeBean>();
			arr4.add(combArr.get(1));
			arr4.add(combArr.get(2));
			float e = combPlayWithDataInArr(arr4, spfType, rqshu);
			if (e == 0) {
				return 0;
			}
			ArrayList<ScroeBean> arr5 = new ArrayList<ScroeBean>();
			arr5.add(combArr.get(1));
			arr5.add(combArr.get(3));
			float f = combPlayWithDataInArr(arr5, spfType, rqshu);
			if (f == 0) {
				return 0;
			}
			ArrayList<ScroeBean> arr6 = new ArrayList<ScroeBean>();
			arr6.add(combArr.get(1));
			arr6.add(combArr.get(4));
			float g = combPlayWithDataInArr(arr6, spfType, rqshu);
			if (g == 0) {
				return 0;
			}
			ArrayList<ScroeBean> arr7 = new ArrayList<ScroeBean>();
			arr7.add(combArr.get(2));
			arr7.add(combArr.get(3));
			float h = combPlayWithDataInArr(arr7, spfType, rqshu);
			if (h == 0) {
				return 0;
			}
			ArrayList<ScroeBean> arr8 = new ArrayList<ScroeBean>();
			arr8.add(combArr.get(2));
			arr8.add(combArr.get(4));
			float i = combPlayWithDataInArr(arr8, spfType, rqshu);
			if (i == 0) {
				return 0;
			}
			ArrayList<ScroeBean> arr9 = new ArrayList<ScroeBean>();
			arr9.add(combArr.get(3));
			arr9.add(combArr.get(4));
			float j = combPlayWithDataInArr(arr9, spfType, rqshu);
			if (j == 0) {
				return 0;
			}

			if (a > 0 && b > 0 && c > 0 && d > 0 && e > 0 && f > 0 && g > 0
					&& h > 0 && i > 0 && j > 0) {
				return (float) ((a + b + c + d + e + f + g + h + i + j) / 4.0);
			} else {
				return 0;
			}
		}

		return 0;
	}

	public Dictionary cutBF(ScroeBean odds) {
		Dictionary dicBF = new Dictionary();
		if ("WIN_OTHER".equals(odds.getKey())) {
			dicBF.setHost(9);
			dicBF.setVisit(0);
		} else if ("DRAW_OTHER".equals(odds.getKey())) {
			dicBF.setHost(9);
			dicBF.setVisit(9);
		} else if ("LOSE_OTHER".equals(odds.getKey())) {
			dicBF.setHost(0);
			dicBF.setVisit(9);
		} else {
			String a = odds.getKey().substring(odds.getKey().length() - 2,
					odds.getKey().length());
			dicBF.setHost(Integer.parseInt(a.charAt(0)+""));
			dicBF.setVisit(Integer.parseInt(a.charAt(1)+""));
		}
		return dicBF;
	}

	public static int cutJQSNum(ScroeBean odds) {
		if ("S0".equals(odds.getKey())) {
			return 0;
		} else if ("S1".equals(odds.getKey())) {
			return 1;
		} else if ("S2".equals(odds.getKey())) {
			return 2;
		} else if ("S3".equals(odds.getKey())) {
			return 3;
		} else if ("S4".equals(odds.getKey())) {
			return 4;
		} else if ("S5".equals(odds.getKey())) {
			return 5;
		} else if ("S6".equals(odds.getKey())) {
			return 6;
		} else if ("S7".equals(odds.getKey())) {
			return 7;
		}else{
			return 0;
		}
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
	public String chooseSPF(String scroe, int rq) {
		String str = "";
		String rqwin = "", rqdraw = "", rqlose = "";
		if (rq == -1) {
			rqwin = "3";
			rqdraw = "3";
			rqlose = "10";
		}else if (rq == 1) {
			rqwin = "31";
			rqdraw = "0";
			rqlose = "0";
		}else if (rq > 1) {
			rqwin = "310";
			rqdraw = "0";
			rqlose = "0";
		}else if (rq < -1) {
			rqwin = "3";
			rqdraw = "3";
			rqlose = "310";
		}else{
			rqwin = "3";
			rqdraw = "1";
			rqlose = "0";
		}
		switch (scroe) {
		case "WINRQSPF":
			str = rqwin;
			break;
		case "DRAWRQSPF":
			str = rqdraw;
			break;
		case "LOSERQSPF":
			str = rqlose;
			break;

		case "WINSPF":
			str = "3";
			break;
		case "DRAWSPF":
			str = "1";
			break;
		case "LOSESPF":
			str = "0";
			break;
		case "S0":
			str = "1";
			break;
		case "S1":
			str = "30";
			break;
		case "S2":
			str = "310";
			break;
		case "S3":
			str = "30";
			break;
		case "S4":
			str = "310";
			break;
		case "S5":
			str = "30";
			break;
		case "S6":
			str = "310";
			break;
		case "S7":
			str = "310";
			break;
		case "DRAW00":
			str = "1";
			break;
		case "DRAW11":
			str = "1";
			break;
		case "DRAW22":
			str = "1";
			break;
		case "DRAW33":
			str = "1";
			break;
		case "DRAW_OTHER":
			str = "1";
			break;
		case "WIN10":
			str = "3";
			break;
		case "WIN20":
			str = "3";
			break;
		case "WIN21":
			str = "3";
			break;
		case "WIN30":
			str = "3";
			break;
		case "WIN31":
			str = "3";
			break;
		case "WIN32":
			str = "3";
			break;
		case "WIN40":
			str = "3";
			break;
		case "WIN41":
			str = "3";
			break;
		case "WIN42":
			str = "3";
			break;
		case "WIN50":
			str = "3";
			break;
		case "WIN51":
			str = "3";
			break;
		case "WIN52":
			str = "3";
			break;
		case "WIN_OTHER":
			str = "3";
			break;
		case "LOSE01":
			str = "0";
			break;
		case "LOSE02":
			str = "0";
			break;
		case "LOSE12":
			str = "0";
			break;
		case "LOSE03":
			str = "0";
			break;
		case "LOSE13":
			str = "0";
			break;
		case "LOSE23":
			str = "0";
			break;
		case "LOSE04":
			str = "0";
			break;
		case "LOSE14":
			str = "0";
			break;
		case "LOSE24":
			str = "0";
			break;
		case "LOSE05":
			str = "0";
			break;
		case "LOSE15":
			str = "0";
			break;
		case "LOSE25":
			str = "0";
			break;
		case "LOSE_OTHER":
			str = "0";
			break;
		case "WIN_WIN":
			str = "3";
			break;
		case "WIN_DRAW":
			str = "1";
			break;
		case "WIN_LOSE":
			str = "0";
			break;
		case "DRAW_DRAW":
			str = "1";
			break;
		case "DRAW_WIN":
			str = "3";
			break;
		case "DRAW_LOSE":
			str = "0";
			break;
		case "LOSE_LOSE":
			str = "0";
			break;
		case "LOSE_WIN":
			str = "3";
			break;
		case "LOSE_DRAW":
			str = "1";
			break;
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
			minMoney = minMoney*2;
			DecimalFormat    df   = new DecimalFormat("######0.00");   
			return Float.parseFloat(df.format(minMoney)+"");
		}else{
			return 0f;
		}
	}

	public boolean isSingle(ArrayList<Match> matchs){
		boolean isSingle = true;
		for(Match match:matchs){
			if(isSingle){
				for(ScroeBean sb:match.getCheckNumber()){
					String key = sb.getKey();
					if (key.contains("RQSPF")) {
						if(!((FootBallMatch)match).getMixOpen().isSingle_5()){
							isSingle = false;
							break;
						}
					} else if (key.contains("SPF")) {
						if(!((FootBallMatch)match).getMixOpen().isSingle_0()){
							isSingle = false;
							break;
						}
					} else if (key.contains("S") && !key.contains("LOSE")) {
						if(!((FootBallMatch)match).getMixOpen().isSingle_1()){
							isSingle = false;
							break;
						}
					} else if (key.contains("_") && !key.contains("OTHER")) {
						if(!((FootBallMatch)match).getMixOpen().isSingle_3()){
							isSingle = false;
							break;
						}
					} else {
						if(!((FootBallMatch)match).getMixOpen().isSingle_2()){
							isSingle = false;
							break;
						}
					}
				}

			}
		}
		return isSingle;
	}
	public void setStringClick(TextView view,String name,ScroeBean key,ArrayList<ScroeBean> has){
		boolean isCheck = true;
		for(ScroeBean sb:has){
			if(sb.getKey().equals(key.getKey())){
				isCheck = false;
				break;
			}
		}
		setTextColor(view,name,key.getValue(),isCheck);
	}
	public void setTextColor(TextView view,String name,String detail,boolean isCheck){
		String Str = "";
		if(isCheck){
			Str = "<html><font color=\"#ffffff\">"+name
					+"</font><font color=\"#ffffff\">&#160;&#160;&#160;"+detail
					+ "</font></html>";
			view.setBackgroundColor(Color.parseColor("#FF3232"));
		}else{
			Str = "<html><font color=\"#333333\">"+name
					+"</font><font color=\"#888888\">&#160;&#160;&#160;"+detail
					+ "</font></html>";
			view.setBackgroundColor(Color.parseColor("#ffffff"));
		}
		view.setText(Html.fromHtml(Str));
	};
	public void setString(TextView view,String name,ScroeBean key,ArrayList<ScroeBean> has){
		boolean isCheck = false;
		for(ScroeBean sb:has){
			if(sb.getKey().equals(key.getKey())){
				isCheck = true;
				break;
			}
		}
		setTextColor(view,name,key.getValue(),isCheck);
	}
	public void setColor(View view,TextView tv0,TextView tv,ScroeBean key,ArrayList<ScroeBean> has){
		boolean isCheck = false;
		for(ScroeBean sb:has){
			if(sb.getKey().equals(key.getKey())){
				isCheck = true;
				break;
			}
		}
		setColorBack(view,tv0,tv,isCheck);
	}
	public void setColorBack(View view,TextView tv0,TextView tv,boolean isCheck){
		if(isCheck){
			tv0.setTextColor(Color.parseColor("#ffffff"));
			tv.setTextColor(Color.parseColor("#ffffff"));
			view.setBackgroundColor(Color.parseColor("#FF3232"));
		}else{
			tv0.setTextColor(Color.parseColor("#333333"));
			tv.setTextColor(Color.parseColor("#888888"));
			view.setBackgroundColor(Color.parseColor("#ffffff"));
		}
	}

	public void setColorClick(View view,TextView tv0,TextView tv,ScroeBean key,ArrayList<ScroeBean> has){
		boolean isCheck = true;
		for(ScroeBean sb:has){
			if(sb.getKey().equals(key.getKey())){
				isCheck = false;
				break;
			}
		}
		setColorBack(view,tv0,tv,isCheck);
	}

}
