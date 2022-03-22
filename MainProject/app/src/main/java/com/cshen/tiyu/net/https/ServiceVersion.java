package com.cshen.tiyu.net.https;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cshen.tiyu.SplashActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.Attachment;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.login.User;
import com.cshen.tiyu.utils.NetWorkUtil;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;

public class ServiceVersion extends ServiceABase{
	private static ServiceVersion instance;

	public static ServiceVersion getInstance() {
		if (null == instance) {
			instance = new ServiceVersion();
		}
		return instance;
	}

	/**
	 * 首页版本号101接口  获取版本信息
	 */
	public void PostGetVersionDatas(Context context,
			final CallBack<Map<String, String>> callBack) {
		if(!NetWorkUtil.isNetworkAvailable(context)){
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return ;
		}
		String wAction = "101";
		BaseHttps.getInstance().getHttpRequest(context,GetCommonParam(HOMEIP_APP,wAction,null),
				new BaseHttpsCallback<String>() {  
			@Override  
			public void onSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					try {

						// 获取某些信息 封装给map
						Map<String, String> versionMap= new HashMap<String, String>();
						JSONObject jsonObject2 = new JSONObject(result);
						JSONArray jsonArray = jsonObject2.getJSONArray("versionsList");
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObjectSon = (JSONObject) jsonArray.opt(i);
							//获取mouldkey
							String moduleKey=jsonObjectSon.getString("moduleKey");
							//获取版本号
							String versionNO=jsonObjectSon.getString("versionNO");

							versionMap.put(moduleKey, versionNO);
							//获取moduleName更新内容
							String moduleName=jsonObjectSon.getString("moduleName");

							versionMap.put("moduleName", moduleName);
						}
						callBack.onSuccess(versionMap);
						return;
					} catch (Exception e) {
						e.printStackTrace();
						callBack.onFailure(new ErrorMsg("-1",getWrongBack(e.getMessage())));
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

	public void uploadMethod(Context context,File imageFile,Attachment attachment,
			final CallBack<String> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "506";
		User user = MyDbUtils.getCurrentUser();
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		if (user != null) {
			ParamMap.put("userId", user.getUserId());
			ParamMap.put("userPwd", user.getUserPwd());
			ParamMap.put("attachmentId", attachment.getAttachmentId());//随机唯一码
			ParamMap.put("attachmentUserId", user.getUserId());  
			ParamMap.put("attachmentType", attachment.getAttachmentType());
			ParamMap.put("attachmentExtension", attachment.getAttachmentExtension()); 
			ParamMap.put("attachmentName", attachment.getAttachmentName());  
			ParamMap.put("attchmentAllLength", attachment.getAttchmentAllLength()+"");
			BaseHttps.getInstance().postHttpRequest(context,
					GetCommonParamPic(IP_MOBILE,imageFile,wAction, ParamMap,user.getUserId()),
					new BaseHttpsCallback<String>() {  
				@Override  
				public void onSuccess(String result) {
					if (!TextUtils.isEmpty(result)) {
						String picUrl = "",processId,errMsg;
						try {
							JSONObject jsonObject = new JSONObject(result);
							processId= jsonObject
									.getString("processId");
							if("0".equals(processId)){
								picUrl= jsonObject
										.getString("userPic");
								callBack.onSuccess(picUrl);
							}else{
								errMsg= jsonObject
										.getString("errorMsg");
								callBack.onFailure(new ErrorMsg("-1",getWrongBack(errMsg)));
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							callBack.onFailure(new ErrorMsg("-1",getWrongBack(e.getMessage())));
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
	/**
	 * 获取对应市场的马甲包的开关
	 * @paramcontext
	 * @paramcount
	 * @param callBack
	 */
	public void PostGetMarketFlag(Context context,final CallBack<String> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return ;
		}
		PackageInfo info;
		try {
			info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			int marketKey = 7;// 0:全部    1:百度   2:豌豆荚  3:魅族   4:VIVO  5:OPPO  6:安智市场    7:360 8:小米   9:华为 11:应用市场
			String url=ConstantsBase.HOMEIP+"/app/appmarketwhite!list.action"
					+ "?marketKey="+marketKey
					+ "&bid="+1
					+ "&version="
					+ info.versionName;		
			BaseHttps.getInstance().getHttpRequest(context,GetCommonParamNoAction(url),
					new BaseHttpsCallback<String>() {  
				@Override  
				public void onSuccess(String result) {
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(result);
						String errCode = jsonObject.getString("errCode");
						if ("0".equals(errCode)) {//成功
							String struts = jsonObject.getString("struts");//0：管不 1：开启
							callBack.onSuccess(struts);
						}else {
							String msg=jsonObject.getString("errorMsg");
							callBack.onFailure(new ErrorMsg("-1",msg));	
						}
					} catch (JSONException e) {
						e.printStackTrace();
						callBack.onFailure(new ErrorMsg("-1",e.getMessage()));	
					}

				}
				@Override  
				public void onError(int code, String message) {  
					callBack.onFailure(new ErrorMsg("-1", getWrongBack(message)));
				}  
				@Override  
				public void onFinished() {  }  
			});
		} catch (NameNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
}
