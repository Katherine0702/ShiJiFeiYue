package com.cshen.tiyu.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	public DateUtils() {
	}

	public static String toDateAndTimeFormat(Date d) {
		String myDate = "";
		if (d != null) {
			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			myDate = f.format(d);
		}

		return myDate;
	}

	public static Date strToDateMin(String str) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;

		try {
			date = format.parse(str);
		} catch (ParseException var4) {
			Log.e("ParseException", var4.getMessage());
		}

		return date;
	}
	
	

	public static long toDateAndTime(String time, String dateFormat) {
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		Date d = null;

		try {
			d = format.parse(time);
		} catch (ParseException var6) {
			var6.printStackTrace();
		}

		long l = d.getTime();
		return l;
	}

	public static Date toDate_(String dateFormat) {
		SimpleDateFormat myFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		try {
			Date e1 = myFmt.parse(dateFormat);
			return e1;
		} catch (ParseException var3) {
			Log.e("ParseException", var3.getMessage());
			return null;
		}
	}

	public static String toDateAndTimeFormatNoSecend(Date d) {
		String myDate = "";
		if (d != null) {
			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			myDate = f.format(d);
		}

		return myDate;
	}

	public static String toDateStringToString(String date) {
		String _dateStr = null;
		Date _date = strToDate(date);
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		_dateStr = f.format(_date);
		return _dateStr;
	}

	public static String toDateAndTimeFormatNoYearNoSecend(Date d) {
		String myDate = "";
		if (d != null) {
			SimpleDateFormat f = new SimpleDateFormat("MM-dd HH:mm");
			myDate = f.format(d);
		}

		return myDate;
	}

	public static String toDateFormat(Date d) {
		if (d == null) {
			return "";
		} else {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String strdate = format.format(d);
			return strdate;
		}
	}

	public static String getDateforint() {
		Calendar c = Calendar.getInstance();
		Date nowdate = c.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String s = sdf.format(nowdate);
		return s;
	}
	public static String getDateforint(Date date) {
		if (date==null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String s = sdf.format(date);
		return s;
	}
	public static String getDateforChina(Date date) {
		if (date==null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
		String s = sdf.format(date);
		return s;
	}

	@SuppressLint({ "SimpleDateFormat" })
	public static String ConverDateoString(String strDate) throws Exception {
		String _dateStr = null;
		if (isEmpty(strDate)) {
			return null;
		} else {
			Date _date = strToDate(strDate);
			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			_dateStr = f.format(_date);
			return _dateStr;
		}
	}

	public static String toTimeFormat(Date d) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		String strdate = format.format(d);
		return strdate;
	}

	public static Date toDate(String dateFormat) {
		SimpleDateFormat myFmt = new SimpleDateFormat("yyyy-MM-dd");

		try {
			Date e1 = myFmt.parse(dateFormat);
			return e1;
		} catch (ParseException var3) {
			Log.e("ParseException", var3.getMessage());
			return null;
		}
	}

	public static Date strToDate(String str) {
		if (str == null) {
			return null;
		} else {
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			Date date = null;

			try {
				date = format.parse(str);
			} catch (ParseException var4) {
				var4.printStackTrace();
			}

			return date;
		}
	}

	public static Date toDateAndTime(String dateFormat) {
		StringBuffer sb = new StringBuffer();
		if (dateFormat.split(":").length < 3) {
			dateFormat = sb.append(dateFormat + ":00").toString();
		}

		SimpleDateFormat myFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			Date e1 = myFmt.parse(dateFormat);
			return e1;
		} catch (ParseException var4) {
			Log.e("ParseException", var4.getMessage());
			return null;
		}
	}

	public static String pad(int c) {
		return c >= 10 ? String.valueOf(c) : "0" + String.valueOf(c);
	}

	public static String getNowDate() {
		Calendar c = Calendar.getInstance();
		Date nowdate = c.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String s = sdf.format(nowdate);
		return s;
	}

	public static String getNowDateShuZi() {
		Calendar c = Calendar.getInstance();
		Date nowdate = c.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmssSSS");
		String s = sdf.format(nowdate);
		return s;
	}

	public static String getNowDateHM() {
		Calendar c = Calendar.getInstance();
		Date nowdate = c.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSS");
		String s = sdf.format(nowdate);
		return s;
	}

	public static Boolean isAmOrPm() {
		Calendar c = Calendar.getInstance();
		int AMPM = c.get(9);
		return AMPM == 0 ? Boolean.valueOf(true) : Boolean.valueOf(false);
	}

	@SuppressLint({ "SimpleDateFormat" })
	public static String getNowDay() {
		Calendar c = Calendar.getInstance();
		Date nowdate = c.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String s = sdf.format(nowdate);
		return s;
	}

	public static String proDay(String data) {
		StringBuilder s = new StringBuilder();
		if (data == null) {
			return "";
		} else {
			String[] sp = data.split("-");
			if (sp == null) {
				return "";
			} else {
				if (sp.length > 1 && sp[1].startsWith("0")) {
					sp[1] = sp[1].replaceFirst("0", "");
				}

				if (sp.length > 2 && sp[2].startsWith("0")) {
					sp[2] = sp[2].replaceFirst("0", "");
				}

				try {
					s.append(sp[0]).append("-").append(sp[1]).append("-")
					.append(sp[2]);
				} catch (Exception var4) {
					;
				}

				return s.toString();
			}
		}
	}

	public static String proDays(String data) {
		StringBuilder s = new StringBuilder();
		if (data != null && !"".equals(data)) {
			String[] sps = data.split(" ");
			String[] sp = sps[0].split("-");
			if (sp == null) {
				return "";
			} else {
				if (sp.length > 1 && sp[1].length() == 1) {
					sp[1] = "0" + sp[1];
				}

				if (sp.length > 2 && sp[2].length() == 1) {
					sp[2] = "0" + sp[2];
				}

				try {
					s.append(sp[0]).append("-").append(sp[1]).append("-")
					.append(sp[2]);
				} catch (Exception var7) {
					var7.printStackTrace();
				}

				if (sps != null && sps.length > 1) {
					String[] ssp = sps[1].split(":");
					if (ssp == null) {
						return "";
					}

					if (ssp.length > 1 && ssp[0].length() == 1) {
						ssp[0] = "0" + ssp[0];
					}

					if (ssp.length >= 2 && ssp[1].length() == 1) {
						ssp[1] = "0" + ssp[1];
					}

					try {
						s.append(" ").append(ssp[0]).append(":").append(ssp[1]);
					} catch (Exception var6) {
						var6.printStackTrace();
					}
				}

				return s.toString();
			}
		} else {
			return "";
		}
	}

	public static String getNowDate(String dateFormat) {
		Calendar c = Calendar.getInstance();
		Date nowdate = c.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		String s = sdf.format(nowdate);
		return s;
	}

	public static String Sub(String str) {
		return str != null && !"".equals(str) ? (str.split(":").length > 1 ? str
				.split(":")[0] + ":" + str.split(":")[1]
						: str)
						: "";
	}

	public static String SubToDay(String str) {
		return str != null && !"".equals(str) ? (str.split(" ").length > 1 ? str
				.split(" ")[0] : str)
				: str;
	}

	public static String SubChinese(String str) {
		if (str != null && !"".equals(str)) {
			String H = str.split(" ").length > 1 ? str.split(" ")[1] : str;
			String time = H.split(":").length > 1 ? H.split(":")[0] + "时"
					+ H.split(":")[1] + "分" : H;
			return H.length() > 10 ? str : SubToDayChinese(str) + time;
		} else {
			return "";
		}
	}

	public static String SubToH(String str) {
		if (str != null && !"".equals(str)) {
			String H = str.split(" ").length > 1 ? str.split(" ")[1] : str;
			String time = H.split(":").length > 0 ? H.split(":")[0] + "时" : H;
			return SubToDayChinese(str) + time;
		} else {
			return "";
		}
	}

	public static String SubToHH(String str) {
		if (str != null && !"".equals(str)) {
			String H = str.split(" ").length > 1 ? str.split(" ")[1] : str;
			String time = H.split(":").length > 0 ? H.split(":")[0] : H;
			return time.length() < 3 ? SubToDayChinese(str) + time : str;
		} else {
			return "";
		}
	}

	public static String SubToDayChinese(String str) {
		if (str != null && !"".equals(str)) {
			String strtime = str.split(" ").length > 1 ? str.split(" ")[0]
					: str;
			return strtime.split("-").length > 1 ? strtime.split("-")[0] + "年"
			+ strtime.split("-")[1] + "月" + strtime.split("-")[2] + "日"
			: strtime;
		} else {
			return str;
		}
	}

	public static String SubToDayChinese2(String str) {
		if (str != null && !"".equals(str)) {
			String strtime = str.split(" ").length > 1 ? str.split(" ")[0]
					: str;
			return strtime.split("/").length > 1 ? strtime.split("/")[0] + "年"
			+ strtime.split("/")[1] + "月" + strtime.split("/")[2] + "日"
			: strtime;
		} else {
			return str;
		}
	}
	public static String SubToDayChinese3(String str) {
		if (str != null && !"".equals(str)
				&&str.length()==8) {
			String year = str.substring(0, 4);
			String month = str.substring(4, 6);
			String day = str.substring(6, 8);
			String week = onDateSet(str,"天");
			StringBuffer strtime = new StringBuffer().append(year).append("年")
					.append(month).append("月")
					.append(day).append("日")
					.append("星期").append(week);
			return strtime.toString();
		} else {
			return str;
		}
	}
	public static String SubToDayChinese4(String str,String number) {
		if (str != null && !"".equals(str)
				&&str.length()==8) {
			String year = str.substring(0, 4);
			String month = str.substring(4, 6);
			String day = str.substring(6, 8);
			String week = onDateSet(str,"日");
			StringBuffer strtime = new StringBuffer()
			.append("周").append(week).append(number);
			return strtime.toString();
		} else {
			return str;
		}
	}
	public static String onDateSet(String  pTime,String ri) {
		String Week = "";
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		Calendar c = Calendar.getInstance();
		try {

			c.setTime(format.parse(pTime));

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			Week += ri;
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 2) {
			Week += "一";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 3) {
			Week += "二";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 4) {
			Week += "三";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 5) {
			Week += "四";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 6) {
			Week += "五";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 7) {
			Week += "六";
		}
		return Week;
	}
	public static String SubTime(String str) {
		if (str != null && !"".equals(str)) {
			String year = SubToDay(str);
			if (SubToDay(getNowDate()).equals(year)) {
				if (str.contains(year)) {
					str = str.replace(year, "");
					str = str.substring(0, str.length() - 3);
					return str;
				} else {
					str = str.substring(5, str.length() - 3);
					return str;
				}
			} else {
				str = str.substring(5, str.length() - 3);
				return str;
			}
		} else {
			str = str.substring(5, str.length() - 3);
			return str;
		}
	}

	public static String SubYear(String str) {
		return str != null && !"".equals(str) ? (str.split("-").length > 1 ? str
				.split("-")[0] : str)
				: str;
	}

	public static Date toDayAfter(Date date, long dayAgo) {
		return new Date(date.getTime() + dayAgo * 24L * 60L * 60L * 1000L);
	}

	public static Date toDayBefore(Date date, long dayAgo) {
		return new Date(date.getTime() - dayAgo * 24L * 60L * 60L * 1000L);
	}

	public static String toDateString(long longTime) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(longTime);
		String time = format.format(date);
		return time;
	}

	public static String toDateString(long longTime, String dateFormat) {
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		Date date = new Date(longTime);
		String time = format.format(date);
		return time;
	}

	public static String strToStr(String time) {
		if (time != null && !"".equals(time)) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
			String t1 = SubToDay(time);
			Date t2 = toDate(t1);
			return t2 == null ? time : format.format(t2);
		} else {
			return time;
		}
	}

	public static String replaceTime(String str) {
		return str != null ? str.replace("年", "-").replace("月", "-")
				.replace("日", "") : str;
	}

	public static String replaceToMMTime(String str) {
		return str != null ? str.replace("年", "-").replace("月", "-")
				.replace("日", " ").replace("时", ":").replace("分", "") : str;
	}

	public static String replaceAllTime(String str) {
		return str != null ? str.replace("年", "-").replace("月", "-")
				.replace("日", " ").replace("时", ":").replace("分", ":")
				.replace("秒", ":") : str;
	}

	public static String getDateTime(String str) {
		if (str != null) {
			String[] tt = str.split(" ");
			if (tt.length <= 1) {
				return str;
			} else {
				String time = str.split(" ")[tt.length - 1];
				StringBuilder minsec = (new StringBuilder())
						.append(time.split(":")[0]).append(":")
						.append(time.split(":")[1]);
				return minsec.toString();
			}
		} else {
			return str;
		}
	}

	public static String secToTime(int time) {
		String timeStr = null;
		boolean hour = false;
		boolean minute = false;
		boolean second = false;
		if (time <= 0) {
			return "00:00";
		} else {
			int minute1 = time / 60;
			int second1;
			if (minute1 < 60) {
				second1 = time % 60;
				timeStr = unitFormat(minute1) + ":" + unitFormat(second1);
			} else {
				int hour1 = minute1 / 60;
				if (hour1 > 99) {
					return "99:59:59";
				}

				minute1 %= 60;
				second1 = time - hour1 * 3600 - minute1 * 60;
				timeStr = unitFormat(hour1) + ":" + unitFormat(minute1) + ":"
						+ unitFormat(second1);
			}

			return timeStr;
		}
	}

	public static String unitFormat(int i) {
		String retStr = null;
		if (i >= 0 && i < 10) {
			retStr = "0" + Integer.toString(i);
		} else {
			retStr = "" + i;
		}

		return retStr;
	}

	public static boolean isEmpty(String str) {
		boolean empty = true;
		if (str != null && !"".equals(str) && !"null".equalsIgnoreCase(str)
				&& !"0".equals(str)) {
			empty = false;
		}

		return empty;
	}

	/**
	 * 判断当前日期是星期几<br>
	 * <br>
	 * 
	 * @param pTime
	 *            修要判断的时间<br>
	 * @return dayForWeek 判断结果<br>
	 * @Exception 发生异常<br>
	 */
	public static int dayForWeek(String pTime) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(format.parse(pTime));
		int dayForWeek = 0;
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			dayForWeek = 7;
		} else {
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		}
		return dayForWeek;
	}

	public static Date strToDateNumber(String str) {
		if (str == null) {
			return null;
		} else {
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyyMMdd");
			Date date = null;
			
			try {
				date = format.parse(str);
			} catch (ParseException var4) {
				var4.printStackTrace();
			}
			
			return date;
		}
	}
	
	
	public static String strToDateChina(Date date) {
		if (date==null) {
			return "";
		}
		String s="";
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dayForWeek = 0;
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			dayForWeek = 7;
		} else {
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		}
		switch (dayForWeek) {
		case 1:
			s=s+"周一";
			break;
		case 2:
			s=s+"周二";
			break;
		case 3:
			s=s+"周三";
			break;
		case 4:
			s=s+"周四";
			break;
		case 5:
			s=s+"周五";
			break;
		case 6:
			s=s+"星期六";
			break;
		case 7:
			s=s+"周日";
			break;

		default:
			break;
		}
		
		return s;
	}
	
	
	
	public static String getChinaStr(Date date) {
		if (date==null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		String s = sdf.format(date);
		
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dayForWeek = 0;
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			dayForWeek = 7;
		} else {
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		}
		switch (dayForWeek) {
		case 1:
			s=s+"星期一";
			break;
		case 2:
			s=s+"星期二";
			break;
		case 3:
			s=s+"星期三";
			break;
		case 4:
			s=s+"星期四";
			break;
		case 5:
			s=s+"星期五";
			break;
		case 6:
			s=s+"星期六";
			break;
		case 7:
			s=s+"星期日";
			break;

		default:
			break;
		}
		
		return s;
	}                              
}
