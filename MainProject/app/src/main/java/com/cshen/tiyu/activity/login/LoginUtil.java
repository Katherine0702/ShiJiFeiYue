package com.cshen.tiyu.activity.login;

import android.content.Context;
import android.text.TextUtils;

import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.utils.Util;

public class LoginUtil {

	public static LoginUtil loginUtil;
	public static LoginUtil getLoginUtil() {
		if (loginUtil == null) {
			loginUtil = new LoginUtil();
		}
		return loginUtil;
	}

	public boolean isWrongMobile(String mobile,Context mContext){
		if (TextUtils.isEmpty(mobile)) {
			ToastUtils.showShort(mContext, "手机号码不能为空");
			return false;
		} else if (!Util.isMobileValid(mobile)) {
			ToastUtils.showShort(mContext, "手机号格式不正确");
			return false;
		}
		return true;
	}
	public boolean isWrongName(String name,Context mContext){
		if (TextUtils.isEmpty(name)) {
			ToastUtils.showShort(mContext, "用户名不能为空");
			return false;
		}  else if (name.length()<3||name.length()>20) {
			ToastUtils.showShort(mContext, "用户名3-20个字符");
			return false;
		} else if (!Util.textNameTemp(name)) {
			ToastUtils.showShort(mContext, "用户名只可包含汉字、数字、字母");
			return false;
		}
		return true;
	}

	public boolean isWrongPassWord(String password,Context mContext){
		if (TextUtils.isEmpty(password)) {
			ToastUtils.showShort(mContext, "密码不能为空");
			return false;
		} else if ((!TextUtils.isEmpty(password)) && Util.textNameTemp1(password)) {
			ToastUtils.showShort(mContext, "密码不能全为数字");
			return false;
		}else if ((!TextUtils.isEmpty(password)) && Util.isAllEnglish(password)) {
			ToastUtils.showShort(mContext, "密码不能全为字母");
			return false;
		}else if (!Util.isPasswordValid_OnlyNumAndLeter(password)||password.length()<6 || password.length()>15) {
			ToastUtils.showShort(mContext, "密码为6-15位字母和数字组成的密码");
			return false;
		}  
		return true;
	}



}
