package com.cshen.tiyu.utils;

import android.content.Context;
import android.text.TextUtils;

import com.cshen.tiyu.domain.ErrorMsg;

public class PostHttpInfoUtils {

	public static void doPostFail(Context mContext,ErrorMsg errorMessage,String defaultErrMsg) {
		if (errorMessage != null
				&& (!TextUtils.isEmpty(errorMessage.msg))) {
			String[] msgs = errorMessage.msg.split("-");
			if (msgs.length == 1) {

				ToastUtils.showShort(mContext, msgs[0]);
			} else {

				ToastUtils.showShort(mContext, msgs[1]);
			}
		} else {
			ToastUtils.showShort(mContext, defaultErrMsg);
		}
	}

}
