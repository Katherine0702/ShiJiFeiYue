package com.cshen.tiyu.domain;

import java.io.UnsupportedEncodingException;

import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.utils.SecurityUtil;

public class PicUpLoad {
	String wAgent;
	public static final String key = "E10ADC3949BA59ABBE56E057F20F883E";
	String wAction;
	String wParam;
	String pwd ;
	String reqUserId;
	String wSign;
	public String getwSign() {
		return wSign;
	}
	public void setwSign(String wSign) {
		this.wSign = wSign;
	}
	public String getwAction() {
		return wAction;
	}
	public void setwAction(String wAction) {
		this.wAction = wAction;
	}
	public String getwParam() {
		return wParam;
	}
	public void setwParam(String wParam) {
		this.wParam = wParam;
	}
	public String getReqUserId() {
		return reqUserId;
	}
	public void setReqUserId(String reqUserId) {
		this.reqUserId = reqUserId;
	}
	
	public  String getwAgent() {
		return wAgent;
	}
	public  void setwAgent(String wAgent) {
		this.wAgent = wAgent;
	}
	public String getParam() {
		return getwAction() +  getwParam() + getwAgent() + key;
	}
	public String getPwd() {
		try {
			return SecurityUtil.md5(getParam().getBytes("UTF-8"))
					.toUpperCase().trim();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  "";
	}
}
