package com.cshen.tiyu.utils;

import android.content.Context;

import com.cshen.tiyu.base.CaiPiaoApplication;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.login.UserInfo;

public class SafeScoreAndRemainMoneyUtils {
	private static Context mContext;
	private static boolean hasBindMobile;
	private static boolean hasRealName;
	private static boolean hasBindBankCard;
	private static boolean hasPayPassword;
	
	private static void initShareInfo() {
		mContext=CaiPiaoApplication.getmContext();
		hasBindMobile = PreferenceUtil.getBoolean(mContext, "hasBindMobile");
		hasRealName = PreferenceUtil.getBoolean(mContext, "hasRealName");
		hasBindBankCard = PreferenceUtil
				.getBoolean(mContext, "hasBindBankCard");
		hasPayPassword = PreferenceUtil.getBoolean(mContext, "hasPayPassword");
	}

	
	public static String  getRemainMoney() {
		String remainMoney=null;
		initShareInfo();
		UserInfo userInfo = MyDbUtils.getCurrentUserInfo();
		if (userInfo != null) {
			remainMoney=userInfo.getRemainMoney();
		}
		return remainMoney;
	}
	
	public static int getSafeScore() {
		int safeScore=0;
		if (hasBindBankCard) {
			safeScore = safeScore + 1;
		}
		if (hasRealName) {
			safeScore = safeScore + 1;
		}
		if (hasBindMobile) {
			safeScore = safeScore + 1;
		}
		if (hasPayPassword) {
			safeScore = safeScore + 1;
		}
		if (safeScore != 0) {
			safeScore = safeScore * 25;
		}
		return safeScore;
	}

	
	
	
}
