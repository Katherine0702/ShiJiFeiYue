package com.cshen.tiyu.widget;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

@SuppressLint("NewApi")
public class MyCountTime2 extends CountDownTimer {
	private int blue;
	private Context mContext;
	// 倒计时的显示面板控件
	private View view = null;
	// 倒计时结束显示的文字
	private String finishStr;
	// 倒计时进行中显示的文字
	private String ingGoStr;
	// 倒计时的
	private long millisInFuture;
	// AutoTextView控件的数据源
	private String[] strs;
	// 统计索引之用,和统计等待时间
	private int count = 0;
	// 在AutoTextView情况下，是否为倒计时： false(不是倒计时) true(是倒计时)
	private boolean isCountTime = false;

	// 获得等待车的时间长度，单位为秒




	private View[] views = null;
	// 倒计时结束显示的文字
	private String[] finishStrs;
	// 倒计时进行中显示的文字



	public int getWaitTime() {
		return count;
	}

	// 这个参数是控制每隔几秒钟进行一个短暂的轮询,默认为3秒钟
	private int betweenTime = 1;

	public void setBetweenTime(int betweenTime) {
		this.betweenTime = betweenTime;
	}

	// MyCount :前者是倒计的时间数，后者是倒计每秒中间的间隔时间，都是以毫秒为单位。
	// 例如要倒计时30秒，每秒中间间隔时间是1秒，两个参数可以这样写MyCount(30000,1000)
	public MyCountTime2(long millisInFuture, long countDownInterval) {
		super(millisInFuture, countDownInterval);
	}

	private long countDownInterval;

	/**
	 * View的倒计时
	 *
	 * @param millisInFuture    : 倒计的时间数(毫秒为单位)
	 * @param countDownInterval : 倒计每秒中间的间隔时间(毫秒为单位)
	 * @param view              : 那个控件进行时间的显示
	 * @param mContext          ： Context
	 * @param finishStr         ： 计时结束的提示字符
	 * @param ingGoStr          ： 倒计时正在执行是的固定显示的字符
	 * @param strs              ： AutoTextView控件的数据源
	 * @param isCountTime       ： 在AutoTextView控件下是否是倒计时
	 */
	public MyCountTime2(Context mContext, long millisInFuture,
			long countDownInterval, View view, String finishStr,
			String ingGoStr, String[] strs, boolean isCountTime) {
		super(millisInFuture, countDownInterval);
		this.countDownInterval = countDownInterval;
		this.millisInFuture = millisInFuture;
		this.mContext = mContext;
		this.view = view;
		this.finishStr = finishStr;
		this.ingGoStr = ingGoStr;
		this.strs = strs;
		this.isCountTime = isCountTime;
		view.setPressed(true);	
	}
	public MyCountTime2(Context mContext, long millisInFuture,long countDownInterval,View[] view, String[] finishStr) {
		super(millisInFuture, countDownInterval);
		this.countDownInterval = countDownInterval;
		this.millisInFuture = millisInFuture;
		this.mContext = mContext;
		this.views = view;
		this.finishStrs = finishStr;
	}
	@Override
	public void onFinish() {
		if(finishStrs!=null&&finishStrs.length==3){
			instanceView(views, new String[]{"00","00","00"});
		}else{
			instanceView(view, finishStr);
			view.setClickable(true);
			view.setPressed(false);
		}
		this.cancel();
	}
	public void setMillisInFuture(long millisInFuture){
		this.millisInFuture = millisInFuture;
	}
	@Override
	public void onTick(long millisUntilFinished) {
		if(finishStrs!=null&&finishStrs.length==3){
			instanceView(views, timeParse(millisUntilFinished).split(":"));
		}else{
			view.setClickable(false);
			instanceView(view, timeParse(millisUntilFinished) + ingGoStr);
		}
	}
	public  String timeParse(long duration) {  
		int time = (int) (duration/1000);
		String timeStr = null;
		int hour = 0;
		int minute = 0;
		int second = 0;
		if (time <= 0)
			if(finishStrs!=null&&finishStrs.length==3){
				return "00:00:00";
			}else{
				return "00:00";
			}

		else {
			minute = time / 60;
			if (minute < 60) {
				second = time % 60;
				if(finishStrs!=null&&finishStrs.length==3){
					timeStr ="00:"+ unitFormat(minute) + ":" + unitFormat(second);
				}else{
					timeStr =unitFormat(minute) + ":" + unitFormat(second);
				}

			} else {
				hour = minute / 60;
				if (hour > 99){
					if(finishStrs!=null&&finishStrs.length==3){
						return "99:59:59";
					}else{
						return "59:59";
					}
				}
				minute = minute % 60;
				second = time - hour * 3600 - minute * 60;
				timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
			}
		}
		return timeStr;
	}  
	public static String unitFormat(int i) {
		String retStr = null;
		if (i >= 0 && i < 10)
			retStr = "0" + Integer.toString(i);
		else
			retStr = "" + i;
		return retStr;
	}
	@SuppressLint("NewApi")
	private void instanceView(View view, String str) {
		if (str == null) {
			str = "";
		}
		if (view instanceof TextView) {
			((TextView) view).setText(str);
			return;
		}
		if (view instanceof Button) {
			((Button) view).setText(str);
			return;
		}
	}
	@SuppressLint("NewApi")
	private void instanceView(View[] views, String[] str) {
		if (str == null) {
			str = new String[]{"00","00","00"};
		}
		if(views.length == str.length){
			for(int i=0;i<views.length;i++){
				if (views[i] instanceof TextView) {
					((TextView) views[i]).setText(str[i]);
				}
			}
		}
	}
}
