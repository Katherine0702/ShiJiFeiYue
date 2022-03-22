package com.cshen.tiyu.net.https;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;

import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.Attachment;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.HttpOrderDetailInfo;
import com.cshen.tiyu.domain.find.MyShareOrderBean;
import com.cshen.tiyu.domain.login.User;
import com.cshen.tiyu.domain.main.NewsBean;
import com.cshen.tiyu.domain.main.PreLoadPage;
import com.cshen.tiyu.domain.main.PreLoadPageData;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.utils.NetWorkUtil;
import com.google.gson.Gson;

/**
 * 晒单
 * @author Administrator
 *
 */
public class ServiceShareOrder extends ServiceABase {
	private static ServiceShareOrder instance;

	public static ServiceShareOrder getInstance() {
		if (null == instance) {
			instance = new ServiceShareOrder();
		}
		return instance;
	}
	
	
	/**
	 * 发起晒单 115
	 */
	public void PostAddShareOrder(Context context,Map<String, Object> ParamMap,final CallBack<String> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return ;
		}
	
		String wAction = "115";//
		
		User user = MyDbUtils.getCurrentUser();

		if (user != null) {
			ParamMap.put("userId", user.getUserId());
			ParamMap.put("userPwd", user.getUserPwd());
			ParamMap.put("userName", user.getUserName());
			ParamMap.put("userimg", user.getUserPic());
			
		}
		
		BaseHttps.getInstance().getHttpRequest(context,GetCommonParam(HOMEIP_APP_SD,wAction,ParamMap),
				new BaseHttpsCallback<String>() {  
			@Override  
			public void onSuccess(String result) {
				int  processId;
				String errorMsg;
				if (!TextUtils.isEmpty(result)) {
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(result);
						processId = jsonObject.getInt("processId");
						
						if (0==processId) {
							callBack.onSuccess(processId+"");
						}else if ((3==processId)||(4==processId)||(5==processId)||(6==processId)) {//其他情况，如只可晒近两周数据
							callBack.onSuccess(processId+"");
							errorMsg=jsonObject.getString("errorMsg");
							callBack.onFailure(new ErrorMsg("-1", errorMsg==null?"发送失败":errorMsg));
						}else{
							errorMsg=jsonObject.getString("errorMsg");
							callBack.onFailure(new ErrorMsg("-1", errorMsg==null?"发送失败":errorMsg));
						}
					} catch (JSONException e) {
						e.printStackTrace();
						callBack.onFailure(new ErrorMsg("-1", getWrongBack(e.getMessage())));
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
	/**
	 * 我的晒单 109
	 */
	public void PostGetMyShareOrder(Context context,int start,int checkedPass,String otherId,final CallBack<MyShareOrderBean> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return ;
		}

		String wAction = "109";//
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		User user = MyDbUtils.getCurrentUser();
		if(checkedPass == 1){
			ParamMap.put("checkedPass", checkedPass);	
			ParamMap.put("userId", otherId);	
			if (user != null) {
				ParamMap.put("loginUserId", user.getUserId());	
			}	
		}else{
			if (user != null) {
				ParamMap.put("userId", user.getUserId());
				ParamMap.put("userPwd", user.getUserPwd());			
			}
		}
		ParamMap.put("start", start+"");
		BaseHttps.getInstance().getHttpRequest(context,GetCommonParam(HOMEIP_APP_SD,wAction,ParamMap),
				new BaseHttpsCallback<String>() {  
			@Override  
			public void onSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					Gson gson = new Gson();
					MyShareOrderBean info = gson.fromJson(
							result, MyShareOrderBean.class);
					if (0==info.getProcessId()) {
						callBack.onSuccess(info);// 类型不定用t
					} else {
						callBack.onFailure(new ErrorMsg("-1",
								"已无更多数据"));
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

	/**
	 * 删除我的晒单 110
	 */
	public void PostDeleteMyShareOrder(Context context,String theSunId,final CallBack<String> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return ;
		}

		String wAction = "110";//
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		User user = MyDbUtils.getCurrentUser();

		if (user != null) {
			ParamMap.put("userId", user.getUserId());
			ParamMap.put("userPwd", user.getUserPwd());			
		}
		ParamMap.put("theSunId", theSunId);//
		BaseHttps.getInstance().getHttpRequest(context,GetCommonParam(HOMEIP_APP_SD,wAction,ParamMap),
				new BaseHttpsCallback<String>() {  
			@Override  
			public void onSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					int processId;
					try {
						JSONObject jsonObject = new JSONObject(result);
						processId = jsonObject.getInt("processId");
						if (1==processId) {
							callBack.onSuccess(processId+"");
						}else {
							//							callBack.onFailure(new ErrorMsg("-1", "删除失败"));
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}

				}else {
					callBack.onFailure(new ErrorMsg("-1", "删除失败"));
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

	/**
	 * 最新/最热晒单(接口wAction111)
	 * @param context
	 * @param isHotest 是否是最热
	 * @param callBack
	 */
	public void PostGetHotestOrder(Context context,boolean isHotest,String start,final CallBack<MyShareOrderBean> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return ;
		}

		String wAction = "111";//
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		User user = MyDbUtils.getCurrentUser();

		if (user != null) {
			ParamMap.put("userId", user.getUserId());
		}
		if (isHotest) {//最热
			ParamMap.put("hotTheSun", 1);			
		}else {//最新
			ParamMap.put("newTheSun", 1);
		}
		ParamMap.put("start", start);//
		BaseHttps.getInstance().getHttpRequest(context,GetCommonParam(HOMEIP_APP_SD,wAction,ParamMap),
				new BaseHttpsCallback<String>() {  
			@Override  
			public void onSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					Gson gson = new Gson();
					MyShareOrderBean info = gson.fromJson(
							result, MyShareOrderBean.class);
					if (0==info.getProcessId()) {
						callBack.onSuccess(info);// 类型不定用t
					} else if (1==info.getProcessId()) {
						callBack.onFailure(new ErrorMsg("-1",
								"已无更多数据"));
					}else {
						callBack.onFailure(new ErrorMsg("-1",
								"访问出错"));
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

	/**
	 * 点赞(接口wAction112)
	 */
	public void PostPraiseOrder(Context context,String theSunId,final CallBack<String> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return ;
		}

		String wAction = "112";//
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		User user = MyDbUtils.getCurrentUser();

		if (user != null) {
			ParamMap.put("userId", user.getUserId());
			ParamMap.put("userPwd", user.getUserPwd());			
		}
		ParamMap.put("theSunId", theSunId);//
		BaseHttps.getInstance().getHttpRequest(context,GetCommonParam(HOMEIP_APP_SD,wAction,ParamMap),
				new BaseHttpsCallback<String>() {  
			@Override  
			public void onSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					int processId;
					try {
						JSONObject jsonObject = new JSONObject(result);
						processId = jsonObject.getInt("processId");//3:点赞成功 1：已点过赞了
						if (3==processId) {
							callBack.onSuccess(processId+"");
						}else if (1==processId) {
							callBack.onFailure(new ErrorMsg("-1", "您已点过赞~"));
						}

					} catch (JSONException e) {
						e.printStackTrace();
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


	public void postImageView(Context context,File imageFile,final CallBack<String> callBack){
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return ;
		}
		String wAction="506";
		User user = MyDbUtils.getCurrentUser();
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("userId", user.getUserId());
		ParamMap.put("userPwd", user.getUserPwd());
		ParamMap.put("istheSun","thesun");
		BaseHttps.getInstance().postHttpRequest(context,
				GetCommonParamPic(IP_THESUN,imageFile,wAction, ParamMap,user.getUserId()),
				new BaseHttpsCallback<String>() {  
			@Override  
			public void onSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					String picUrl = "",processId,errMsg;
					try {
						JSONObject jsonObject = new JSONObject(result);
						processId= jsonObject
								.getString("processId");
						if("1".equals(processId)){
							try{
								picUrl= jsonObject
										.getString("userPic");
								callBack.onSuccess(picUrl);
							}catch(JSONException e){
								errMsg= jsonObject
										.getString("errorMsg");
								callBack.onFailure(new ErrorMsg("-1",getWrongBack(errMsg)+","+result));
							}
						}else{
							errMsg= jsonObject
									.getString("errorMsg");
							callBack.onFailure(new ErrorMsg("-1",getWrongBack(errMsg)+","+result));
						}
					}  catch (JSONException e) {
						e.printStackTrace();
						callBack.onFailure(new ErrorMsg("-1",getWrongBack(e.getMessage())+","+result));
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

	/**
	 * 获取顶部图片(接口wAction114)
	 */
	public void PostGetTopImage(Context context,final CallBack<String> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return ;
		}

		String wAction = "114";//
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		BaseHttps.getInstance().getHttpRequest(context,GetCommonParam(HOMEIP_APP_SD,wAction,ParamMap),
				new BaseHttpsCallback<String>() {  
			@Override  
			public void onSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					String processId;
					try {
						JSONObject jsonObject = new JSONObject(result);
						processId = jsonObject.getString("processId");//0
						if ("0".equals(processId)) {
							String imgUrl=jsonObject.getString("imgUrl");
							if (imgUrl!=null) {
								callBack.onSuccess(imgUrl);
							}

						}

					} catch (JSONException e) {
						e.printStackTrace();
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
