package com.cshen.tiyu.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lingcheng on 15/10/20.
 */
public class Util {
	public static boolean isAllEnglish(String s) {// 是否全为数字
		String reg = "[a-zA-Z]+";
		return startCheck(reg, s);
	}
	public static boolean textNameTemp1(String s) {// 是否全为数字
		String reg = "^\\d+$";
		return startCheck(reg, s);
	}
	public static boolean textNameTemp(String str) {// 汉字，字母，数字
		  String regEx = "^[a-zA-Z0-9\u4e00-\u9fa5]+$";
		  Pattern p = Pattern.compile(regEx);
		  Matcher m = p.matcher(str);
		  return m.find();
	}
	
	public static boolean textNameTemp_NoSpace(String str) {// 汉字，字母，数字，不包含空格等
		 String regEx = "^[\u4e00-\u9fa5_a-zA-Z0-9]{2,15}$";
		
		  Pattern p = Pattern.compile(regEx);
		
		  Matcher m = p.matcher(str);
		  return m.find();
	}
	
	public static boolean textNameTemp2(String str) {// 不能包含特殊字符
		 String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		  Pattern p = Pattern.compile(regEx);
		  Matcher m = p.matcher(str);
		  return m.find();
	}
	public static boolean textNameTemp3(String str) {// 汉字，字母，数字标点符号
		 String regEx = "^[a-zA-Z0-9,.?!，。？！~\u4e00-\u9fa5]+$";
		  Pattern p = Pattern.compile(regEx);
		  Matcher m = p.matcher(str);
		  return m.find();
	}
	public static boolean isDollar(String cellPhoneNr) {
		String reg = "^[0-9]+(.[0-9]{1,2})?";
		return startCheck(reg, cellPhoneNr);
	}

//	public static boolean isIdCard(String cellPhoneNr) {
//		String reg = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$|^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";
////		String reg = "^(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$)|(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[Xx])$)$";
//		return startCheck(reg, cellPhoneNr);
//	}

//	public static boolean isBankCard(String cardNo) {
//		String reg = "^\\d{16,19}$|^\\d{6}[- ]\\d{10,13}$|^\\d{4}[- ]\\d{4}[- ]\\d{4}[- ]\\d{4,7}$";
//		return startCheck(reg, cardNo);
//	}

	public static boolean isMobileValid(String cellPhoneNr) {
		String reg = "^((13[0-9])|(14[0-9])|(15[^4,\\D])|(16[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$";
		return startCheck(reg, cellPhoneNr);
	}
	public static boolean isNameValid(String NameNr) {
		String reg = "^[\u4e00-\u9fa5]*$";
		return startCheck(reg, NameNr);
	}
	// public static boolean isEmailValid(String email) {
	// String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
	// return startCheck(regex, email);
	// }

	public static boolean isPasswordValid(String cellPhoneNr) {
		String reg = "^(?!^\\d+$)(?!^[a-zA-Z]+$)(?!^[_*/~!@#￥%&*()<>+-]+$).{6,15}$";
		return startCheck(reg, cellPhoneNr);
	}

	public static boolean isPayPassword(String payPassword) {
		String reg = "\\d{6}";
		return startCheck(reg, payPassword);
	}
	//字母和数字组合
	public static boolean isPasswordValid_OnlyNumAndLeter(String passWord) {
		String reg = "^[A-Za-z0-9]+$";
		return startCheck(reg, passWord);
	}

	// public static boolean isUserNameValid(String cellPhoneNr) {
	// String reg = "^[\\u4e00-\\u9fa50-9a-zA-Z]{4,16}$";
	// return startCheck(reg, cellPhoneNr);
	// }
	private static boolean startCheck(String reg, String string) {
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(string);
		return matcher.matches();
	}

	public static String getTypeString(String typeString) {
		if (typeString.equals("SUBSCRIPTION")) {
			typeString = "认购";
		} else if (typeString.equals("DRAWING")) {
			typeString = "用户提款";
		} else if (typeString.equals("CANCEL_SCHEME")) {
			typeString = "方案撤单";
		} else if (typeString.equals("CANCEL_SUBSCRIPTION")) {
			typeString = "撤销认购";
		} else if (typeString.equals("BAODI")) {
			typeString = "保底";
		} else if (typeString.equals("CANCEL_BAODI")) {
			typeString = "撤销保底";
		} else if (typeString.equals("REFUNDMENT_SCHEME")) {
			typeString = "方案退款";
		} else if (typeString.equals("SCHEME_COMMISSION")) {
			typeString = "方案佣金";
		} else if (typeString.equals("SCHEME_BONUS")) {
			typeString = "奖金分红";
		} else if (typeString.equals("COPY_SCHEME_BONUS")) {
			typeString = "发单奖金分红";
		} else if (typeString.equals("ADMIN_OPR")) {
			typeString = "手动调整额度";
		} else if (typeString.equals("CHASE")) {
			typeString = "追号";
		} else if (typeString.equals("DRAWING")) {
			typeString = "用户提款";
		} else if (typeString.equals("DRAWINGFAIL")) {
			typeString = "提款失败返回现金";
		} else if (typeString.equals("PAY")) {
			typeString = "在线充值";
		} else if (typeString.equals("ADMINPAY")) {
			typeString = "后台充值";
		} else if (typeString.equals("DRAWING")) {
			typeString = "用户提款";
		} else if (typeString.equals("RECHARGEACTIVITY")) {
			typeString = "活动赠送彩金";
		} else if (typeString.equals("AGENTOPR")) {
			typeString = "代理商操作";
		} else if (typeString.equals("REBATE")) {
			typeString = "佣金";
		} else if (typeString.equals("GOLDBEAN")) {
			typeString = "领取金豆";
		} else if (typeString.equals("HONGBAO")) {
			typeString = "红包";
		} else if (typeString.equals("STOP_CHASE")) {
			typeString = "停止追号";
		}
		return typeString;
	}

	public static String getLotteryType(String typeString) {
		if (typeString.equals("SSQ")) {
			typeString = "0";
		} else if (typeString.equals("KLSF")) {
			typeString = "1";
		} else if (typeString.equals("EL11TO5")) {
			typeString = "2";
		} else if (typeString.equals("SSC")) {
			typeString = "3";
		} else if (typeString.equals("SSL")) {
			typeString = "4";
		} else if (typeString.equals("WELFARE3D")) {
			typeString = "5";
		} else if (typeString.equals("PL")) {
			typeString = "6";
		} else if (typeString.equals("DCZC")) {
			typeString = "7";
		} else if (typeString.equals("SEVEN")) {
			typeString = "8";
		} else if (typeString.equals("SFZC")) {
			typeString = "9";
		} else if (typeString.equals("LCZC")) {
			typeString = "10";
		} else if (typeString.equals("SCZC")) {
			typeString = "11";
		} else if (typeString.equals("WELFARE36To7")) {
			typeString = "12";
		} else if (typeString.equals("DLT")) {
			typeString = "13";
		} else if (typeString.equals("SDEL11TO5")) {
			typeString = "14";
		} else if (typeString.equals("QYH")) {
			typeString = "15";
		} else if (typeString.equals("TC22TO5")) {
			typeString = "16";
		} else if (typeString.equals("JCZQ")) {
			typeString = "17";
		} else if (typeString.equals("JCLQ")) {
			typeString = "18";
		} else if (typeString.equals("GDEL11TO5")) {
			typeString = "19";
		} else if (typeString.equals("SEVENSTAR")) {
			typeString = "20";
		} else if (typeString.equals("KLPK")) {
			typeString = "21";
		} else if (typeString.equals("AHKUAI3")) {
			typeString = "22";
		} else if (typeString.equals("XJEL11TO5")) {
			typeString = "23";
		} else if (typeString.equals("JXKUAI3")) {
			typeString = "24";
		}

		return typeString;
	}
	public static String getLotteryTypeToString(int typeInt) {
		String typeString = "";
		if (typeInt == 0 ) {
			typeString = "SSQ";
		} else if (typeInt == 1 ) {
			typeString = "KLSF";
		} else if (typeInt == 2 ) {
			typeString = "EL11TO5";
		} else if (typeInt == 3 ) {
			typeString = "SSC";
		} else if (typeInt == 4 ) {
			typeString = "SSL";
		} else if (typeInt == 5 ) {
			typeString = "WELFARE3D";
		} else if (typeInt == 6 ) {
			typeString = "PL";
		} else if (typeInt == 7 ) {
			typeString = "DCZC";
		} else if (typeInt == 8 ) {
			typeString = "SEVEN";
		} else if (typeInt == 9 ) {
			typeString = "SFZC";
		} else if (typeInt == 10 ) {
			typeString = "LCZC";
		} else if (typeInt == 11 ) {
			typeString = "SCZC";
		} else if (typeInt == 12 ) {
			typeString = "WELFARE36To7";
		} else if (typeInt == 13 ) {
			typeString = "DLT";
		} else if (typeInt == 14 ) {
			typeString = "SDEL11TO5";
		} else if (typeInt == 15 ) {
			typeString = "QYH";
		} else if (typeInt == 16 ) {
			typeString = "TC22TO5";
		} else if (typeInt == 17 ) {
			typeString = "JCZQ";
		} else if (typeInt == 18 ) {
			typeString = "JCLQ";
		} else if (typeInt == 19 ) {
			typeString = "GDEL11TO5";
		} else if (typeInt == 20 ) {
			typeString = "SEVENSTAR";
		} else if (typeInt == 21) {
			typeString = "KLPK";
		} else if (typeInt == 22 ) {
			typeString = "AHKUAI3";
		} else if (typeInt == 23 ) {
			typeString = "XJEL11TO5";
		}else if (typeInt == 24 ) {
			typeString = "JXKUAI3";
		}

		return getLotteryName(typeString);
	}
	public static String get11to5TypeName(String typeString) {
		String typeName=null;
		if (typeString!=null && !"".endsWith(typeString)) {
			if (typeString.equals("NormalOne")) {
				typeString = "任选一";
			} else if (typeString.equals("RandomTwo")) {
				typeString = "任选二";
			} else if (typeString.equals("ForeTwoGroup")) {
				typeString = "前二组选";
			} else if (typeString.equals("RandomThree")) {
				typeString = "任选三";
			} else if (typeString.equals("ForeThreeGroup")) {
				typeString = "前三组选";
			} else if (typeString.equals("RandomFour")) {
				typeString = "任选四";
			} else if (typeString.equals("RandomFive")) {
				typeString = "任选五";
			} else if (typeString.equals("RandomSix")) {
				typeString = "任选六";
			} else if (typeString.equals("RandomSeven")) {
				typeString = "任选七";
			} else if (typeString.equals("RandomEight")) {
				typeString = "任选八";
			} else if (typeString.equals("ForeTwoDirect")) {
				typeString = "前二直选";
			} else if (typeString.equals("ForeThreeDirect")) {
				typeString = "前三直选";
			} 
		}
		return typeString;
	}
	public static String getfast3TypeName(String typeString) {
		if (typeString!=null && !"".endsWith(typeString)) {
			if (typeString.equals("HeZhi")) {
				typeString = "和值";
			} else if (typeString.equals("ThreeDX")) {
				typeString = "三同号";
			} else if (typeString.equals("ThreeTX")) {
				typeString = "三同号通选";
			} else if (typeString.equals("RandomThree")) {
				typeString = "三不同号";
			} else if (typeString.equals("ThreeLX")) {
				typeString = "三连号通选";
			} else if (typeString.equals("TwoDX")) {
				typeString = "二同号";
			} else if (typeString.equals("TwoFX")) {
				typeString = "二同号复选";
			} else if (typeString.equals("RandomTwo")) {
				typeString = "二不同号";
			} 
		}
		return typeString;
	}
	
	public static String getLotteryName(String typeString) {

		if ("SSQ".equals(typeString)) {
			typeString = "双色球";
		} else if ("KLSF".equals(typeString)) {
			typeString = "快乐十分";
		} else if ("EL11TO5".equals(typeString)) {
			typeString = "山东11选5";
		} else if ("SSC".equals(typeString)) {
			typeString = "时时彩";
		} else if ("SSL".equals(typeString)) {
			typeString = "时时乐";
		} else if ("WELFARE3D".equals(typeString)) {
			typeString = "福彩3D";
		} else if ("PL".equals(typeString)) {
			typeString = "排列3/5";
		} else if ("DCZC".equals(typeString)) {
			typeString = "DCZC";
		} else if ("SEVEN".equals(typeString)) {
			typeString = "SEVEN";
		} else if ("SFZC".equals(typeString)) {
			/*if("0".equals(playType)){
				typeString = "胜负彩";
			}if("1".equals(playType)){
				typeString = "rexu";
			}*/
		}  else if ("LCZC".equals(typeString)) {
			typeString = "六场半全场";
		} else if ("SCZC".equals(typeString)) {
			typeString = "四场进球";
		} else if ("WELFARE36To7".equals(typeString)) {
			typeString = "36选7";
		} else if ("DLT".equals(typeString)) {
			typeString = "大乐透";
		} else if ("SDEL11TO5".equals(typeString)) {
			typeString = "山东11选5";
		} else if ("QYH".equals(typeString)) {
			typeString = "山东群英会";
		} else if ("TC22TO5".equals(typeString)) {
			typeString = "体彩22选5";
		} else if ("JCZQ".equals(typeString)) {
			typeString = "竞彩足球";
		} else if ("JCLQ".equals(typeString)) {
			typeString = "竞彩篮球";
		} else if ("GDEL11TO5".equals(typeString)) {
			typeString = "广东11选5";
		} else if (typeString.equals("SEVENSTAR")) {
			typeString = "七星彩";
		} else if (typeString.equals("KLPK")) {
			typeString = "快乐扑克3";
		} else if (typeString.equals("AHKUAI3")) {
			typeString = "安徽快3";
		} else if (typeString.equals("XJEL11TO5")) {
			typeString = "新疆11选5";
		}else if (typeString.equals("JXKUAI3")) {
			typeString = "江西快三";
		}

		return typeString;
	}
	public static String periodNumberFromat(String number){
		if (number!=null && number.trim().length()<2) {
			number="0"+number;
		}
		return number;
	}

	public static final String SPECIAL_CHAR = "_*/~!@#￥%";
	public static boolean existSpecialChar(String srcString, char[] specialChar) {
		for (Character c : specialChar) {
			if (srcString.contains(c.toString())) {
				return true;
			}
		}
		return false;
	}
	public static String getDollarFormat(Long number) {
		if (number == null) {
			return "";
		}
		NumberFormat nf = new DecimalFormat("#,###");
	    String str = nf.format(number);
		return str;
	}
	public static String getDollarFormat(Integer number) {
		if (number == null) {
			return "";
		}
		NumberFormat nf = new DecimalFormat("#,###");
	    String str = nf.format(number);
		return str;
	}
	public static String getDayForWeek(String date) throws Exception {
		String day = null;
		switch (DateUtils.dayForWeek(date)) {
		case 1:
			day = "周一";
			break;
		case 2:
			day = "周二";
			break;
		case 3:
			day = "周三";
			break;
		case 4:
			day = "周四";
			break;
		case 5:
			day = "周五";
			break;
		case 6:
			day = "周六";
			break;
		case 7:
			day = "周日";
			break;

		default:
			break;
		}
		return day;
	}
	
	public static String getPlayTypeString(String typeString) {
		if (typeString == null) {
			return "";
		}
		if (typeString!=null && !"".endsWith(typeString)) {
			if (typeString.equals("SPF_RQSPF")) {
				typeString = "胜平负/让球胜平负";
			} else if (typeString.equals("JQS")) {
				typeString = "进球数";
			} else if (typeString.equals("BF")) {
				typeString = "比分";
			} else if (typeString.equals("BQQ")) {
				typeString = "半全场";
			} else if (typeString.equals("MIX")) {
				typeString = "混合过关";
			} else if (typeString.equals("RENJIU")) {
				typeString = "任九新玩";
			} else if (typeString.equals("EXY")) {
				typeString = "二选一过关";
			} else if (typeString.equals("DGP")) {
				typeString = "二选一单关配";
			} else if (typeString.equals("YP")) {
				typeString = "亚盘玩法";
			} else if (typeString.equals("WC")) {
				typeString = "世界杯冠军";
			}
		}
		return typeString;
	}
	public static String getPlayTypeString(Integer playTypeOrdinal) {
		if (playTypeOrdinal == null) {
			return "";
		}
		String typeName=null;
			if (playTypeOrdinal == 0) {
				typeName = "胜平负";
			} else if (playTypeOrdinal == 1) {
				typeName = "进球数";
			} else if (playTypeOrdinal == 2) {
				typeName = "比分";
			} else if (playTypeOrdinal == 3) {
				typeName = "半全场";
			} else if (playTypeOrdinal == 4) {
				typeName = "混合过关";
			} else if (playTypeOrdinal == 5) {
				typeName = "让球胜平负";
			} else if (playTypeOrdinal == 6) {
				typeName = "欧冠杯冠军";
			} else if (playTypeOrdinal == 7) {
				typeName = "世界杯冠军";
			} else if (playTypeOrdinal == 8) {
				typeName = "欧洲杯冠军";
			}else if (playTypeOrdinal == 9) {
				typeName = "欧洲杯冠亚军";
			}
			
			
			
		return typeName;
	}  
	
	public static String getJCLQPlayTypeString(Integer playTypeOrdinal) {
		if (playTypeOrdinal == null) {
			return "";
		}
		String typeName=null;
			if (playTypeOrdinal == 0) {
				typeName = "胜负";
			} else if (playTypeOrdinal == 1) {
				typeName = "让分胜负";
			} else if (playTypeOrdinal == 2) {
				typeName = "胜分差";
			} else if (playTypeOrdinal == 3) {
				typeName = "大小分";
			} else if (playTypeOrdinal == 4) {
				typeName = "混合串";
			} 
		return typeName;
	}  
	

}
