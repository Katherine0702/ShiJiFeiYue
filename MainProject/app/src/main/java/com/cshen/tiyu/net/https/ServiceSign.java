package com.cshen.tiyu.net.https;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.login.User;
import com.cshen.tiyu.domain.main.PreLoadPage;
import com.cshen.tiyu.domain.main.PreLoadPageData;
import com.cshen.tiyu.domain.pay.IpsorderReslut;
import com.cshen.tiyu.domain.sign.SignData;
import com.cshen.tiyu.utils.NetWorkUtil;
import com.google.gson.Gson;

public class ServiceSign  extends ServiceABase{
	private static ServiceSign instance;

	public static ServiceSign getInstance() {
		if (null == instance) {
			instance = new ServiceSign();
		}
		return instance;
	}
	public void getSignData(Context context,
			final CallBack<SignData> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return ;
		}
		String wAction = "0000";//106接口数据
		User user = MyDbUtils.getCurrentUser();
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		if(user!=null){
			ParamMap.put("userId", user.getUserId());
			ParamMap.put("userPwd", user.getUserPwd());
		}
		
		BaseHttps.getInstance().postHttpRequest(context,
				GetCommonParam(IP_TICKET,wAction, ParamMap),
				new BaseHttpsCallback<String>() {  
			@Override  
			public void onSuccess(String result) {
				String processId, errMsg;
				try {
					JSONObject jsonObject = new JSONObject(result);
					processId = jsonObject.getString("processId");
					if ("0".equals(processId)) {
						Gson gson = new Gson();
						SignData mResult = gson.fromJson(
								result,SignData.class);
						callBack.onSuccess(mResult);
					} else {
						errMsg = jsonObject.getString("errorMsg");
						callBack.onFailure(new ErrorMsg("-1", getWrongBack(errMsg)));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					callBack.onFailure(new ErrorMsg("-1", getWrongBack(e
							.getMessage())));
				}
			}
			@Override  
			public void onError(int code, String message) {  
				callBack.onFailure(new ErrorMsg("-1", getWrongBack(message)));
			}  
			@Override  
			public void onFinished() {  }  
		});

	}
	public void toSign(Context context,
			final CallBack<SignData> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return ;
		}
		String wAction = "0001";//106接口数据
		User user = MyDbUtils.getCurrentUser();
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("userId", user.getUserId());
		ParamMap.put("userPwd", user.getUserPwd());
		
		BaseHttps.getInstance().postHttpRequest(context,
				GetCommonParam(IP_TICKET,wAction, ParamMap),
				new BaseHttpsCallback<String>() {  
			@Override  
			public void onSuccess(String result) {
				String processId, errMsg;
				try {
					JSONObject jsonObject = new JSONObject(result);
					processId = jsonObject.getString("processId");
					if ("0".equals(processId)) {
						Gson gson = new Gson();
						SignData mResult = gson.fromJson(
								result,SignData.class);
						callBack.onSuccess(mResult);
					} else {
						errMsg = jsonObject.getString("errorMsg");
						callBack.onFailure(new ErrorMsg("-1", getWrongBack(errMsg)));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					callBack.onFailure(new ErrorMsg("-1", getWrongBack(e
							.getMessage())));
				}
			}
			@Override  
			public void onError(int code, String message) {  
				callBack.onFailure(new ErrorMsg("-1", getWrongBack(message)));
			}  
			@Override  
			public void onFinished() {  }  
		});

	}
}
