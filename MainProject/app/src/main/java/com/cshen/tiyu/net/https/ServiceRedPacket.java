package com.cshen.tiyu.net.https;

import java.util.HashMap;
import org.xutils.common.Callback.CommonCallback;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback.CommonCallback;

import android.content.Context;
import android.widget.Toast;

import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.login.User;
import com.cshen.tiyu.domain.redpacket.RedPacketRainPacket;
import com.cshen.tiyu.domain.redpacket.RedPacketRainTime;
import com.cshen.tiyu.utils.NetWorkUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.google.gson.Gson;

public class ServiceRedPacket  extends ServiceABase{
	private static ServiceRedPacket instance;

	public static final String IP_TICKET = ConstantsBase.IP + "/ticket.jsp";
	public static ServiceRedPacket getInstance() {
		if (null == instance) {
			instance = new ServiceRedPacket();
		}
		return instance;
	}
	public  void getRedPacketTime(Context context,
			final CallBack<RedPacketRainTime> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "0002";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		User user = MyDbUtils.getCurrentUser();
		if (user != null) {
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
						RedPacketRainTime mResult = gson.fromJson(
								result,RedPacketRainTime.class);
						callBack.onSuccess(mResult);
					} else {
						errMsg = jsonObject.getString("errorMsg");
						callBack.onFailure(new ErrorMsg("-1", getWrongBack(errMsg)));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					callBack.onFailure(new ErrorMsg("-1", getWrongBack(e.getMessage())));
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
	public  void getRedPacket(Context context,
			final CallBack<RedPacketRainPacket> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "0003";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		User user = MyDbUtils.getCurrentUser();
		if (user != null) {
			ParamMap.put("userId", user.getUserId());
			ParamMap.put("userPwd", user.getUserPwd());
		}

		BaseHttps.getInstance().getHttpRequest(context,
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
						RedPacketRainPacket mResult = gson.fromJson(
								result,RedPacketRainPacket.class);
						callBack.onSuccess(mResult);
					} else {
						errMsg = jsonObject.getString("errorMsg");
						callBack.onFailure(new ErrorMsg("-1", getWrongBack(errMsg)));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					callBack.onFailure(new ErrorMsg("-1", getWrongBack(e.getMessage())));
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
