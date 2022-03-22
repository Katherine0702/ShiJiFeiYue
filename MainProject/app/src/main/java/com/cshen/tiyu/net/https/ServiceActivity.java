package com.cshen.tiyu.net.https;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.activity.ActivityData;
import com.cshen.tiyu.domain.login.User;
import com.cshen.tiyu.domain.redpacket.RedPacketData;
import com.cshen.tiyu.utils.NetWorkUtil;

public class ServiceActivity extends ServiceABase{
	private static ServiceActivity instance;

	public static ServiceActivity getInstance() {
		if (null == instance) {
			instance = new ServiceActivity();
		}
		return instance;
	}
	// 获取红包列表
		/**
		 * searchType： 0 派发中红包 -1 所有红包 1 用完或者过期的红包 2 可用红包 3 已用完的红包 4 已过期红包
		 **/
		public void getRedPacketOrder(Context context, String count, String start,
				String searchType, final CallBack<RedPacketData> callBack) {
			if (!NetWorkUtil.isNetworkAvailable(context)) {
				callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
				return;
			}
			String wAction = "801";
			User user = MyDbUtils.getCurrentUser();
			Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
			ParamMap.put("userId", user.getUserId());
			ParamMap.put("userPwd", user.getUserPwd());
			ParamMap.put("count", count);
			ParamMap.put("start", start);
			ParamMap.put("searchType", searchType);
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
							RedPacketData mResult = gson.fromJson(
									result,RedPacketData.class);
							if ("0".equals(mResult.getProcessId())) {
								callBack.onSuccess(mResult);// 类型不定用t
							} else {
								callBack.onFailure(new ErrorMsg("-1",getWrongBack(
										mResult.getErrorMsg())));
							}
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
	// 获取活动列表
		public void GetActivity(Context context, String count, String start,
				final CallBack<ActivityData> callBack) {
			if (!NetWorkUtil.isNetworkAvailable(context)) {
				callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
				return;
			}
			String wAction = "800";
			Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
			ParamMap.put("count", count);
			ParamMap.put("start", start);
			BaseHttps.getInstance().postHttpRequest(context,
					GetCommonParam(IP_TICKET,wAction, ParamMap),
					new BaseHttpsCallback<String>() {  
				@Override
				public void onSuccess(String result) {
					if (!TextUtils.isEmpty(result)) {
						try {
							Gson gson = new Gson();
							ActivityData mResult = gson.fromJson(
									result,ActivityData.class);
							if ("0".equals(mResult.getProcessId())) {
								callBack.onSuccess(mResult);// 类型不定用t
							} else {
								callBack.onFailure(new ErrorMsg("-1",getWrongBack(
										mResult.getErrorMsg())));
							}

							return;
						} catch (Exception e) {
							e.printStackTrace();
							callBack.onFailure(new ErrorMsg("-1", getWrongBack(e
									.getMessage())));
						}
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

	// 获取活动列表
		public void GetActivityTop(Context context,
				final CallBack<ActivityData> callBack) {
			if (!NetWorkUtil.isNetworkAvailable(context)) {
				callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
				return ;
			}
			String wAction = "803";
			Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
			BaseHttps.getInstance().postHttpRequest(context,
					GetCommonParam(IP_TICKET,wAction, ParamMap),
					new BaseHttpsCallback<String>() {  
				@Override
				public void onSuccess(String result) {
					if (!TextUtils.isEmpty(result)) {
						try {
							Gson gson = new Gson();
							ActivityData mResult = gson.fromJson(
									result,ActivityData.class);
							if ("0".equals(mResult.getProcessId())) {
								callBack.onSuccess(mResult);// 类型不定用t
							} else {
								callBack.onFailure(new ErrorMsg("-1",getWrongBack(
										mResult.getErrorMsg())));
							}

							return;
						} catch (Exception e) {
							e.printStackTrace();
							callBack.onFailure(new ErrorMsg("-1", getWrongBack(e
									.getMessage())));
						}
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
