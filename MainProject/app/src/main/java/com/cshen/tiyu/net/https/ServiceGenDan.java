package com.cshen.tiyu.net.https;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.Back;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.gendan.HuanDengBack;
import com.cshen.tiyu.domain.gendan.MyFollowRecommendList;
import com.cshen.tiyu.domain.gendan.MyRecommendDetilList;
import com.cshen.tiyu.domain.gendan.NiuRen;
import com.cshen.tiyu.domain.gendan.NiuRenDetil;
import com.cshen.tiyu.domain.gendan.NiuRenList;
import com.cshen.tiyu.domain.gendan.RenZhengPersonList;
import com.cshen.tiyu.domain.login.User;
import com.cshen.tiyu.domain.pay.TicketBack;
import com.cshen.tiyu.utils.NetWorkUtil;
import com.google.gson.Gson;


public class ServiceGenDan  extends ServiceABase{
	private static ServiceGenDan instance;

	public static final String IP_TICKET = ConstantsBase.IP + "/ticket.jsp";
	public static ServiceGenDan getInstance() {
		if (null == instance) {
			instance = new ServiceGenDan();
		}
		return instance;
	}
	
	public void PostGetHuangDengData(Context context,final CallBack<HuanDengBack> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return ;
		}
		String wAction = "121";
		BaseHttps.getInstance().getHttpRequest(context,GetCommonParam(HOMEIP_APP,wAction,null),
				new BaseHttpsCallback<String>() {  
			@Override  
			public void onSuccess(String result) {
				String processId, errMsg;
				try {
					JSONObject jsonObject = new JSONObject(result);
					processId = jsonObject.getString("processId");
					if ("0".equals(processId)) {
						Gson gson = new Gson();
						HuanDengBack mResult = gson.fromJson(
								result,HuanDengBack.class);
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
	//牛人
	public  void pastNiuRenList(Context context,int start,int count,String keyword,String userID,String password,String bangdanniuren,
			final CallBack<NiuRenList> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "900";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("start", start);
		ParamMap.put("count", count);
		ParamMap.put("keyword", keyword);
		ParamMap.put("userId", userID);
		ParamMap.put("userPwd", password);
		if(!TextUtils.isEmpty(bangdanniuren)&&"bangdan".equals(bangdanniuren)){
			ParamMap.put("versions", 1);
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
						NiuRenList mResult = gson.fromJson(
								result,NiuRenList.class);
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
	//认证人推荐 
	public  void pastRenZhengList(Context context,int start,int count,int  version,int type,int state,
			final CallBack<RenZhengPersonList> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "901";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("versions",version);
		ParamMap.put("type",type);
		if(state != 0){
			ParamMap.put("state",state);
		}
		User user = MyDbUtils.getCurrentUser();
		if(user!=null&&user.getUserId()!=null){
			ParamMap.put("userId", user.getUserId());
			ParamMap.put("userPwd", user.getUserPwd());
		}
		ParamMap.put("start", start);
		ParamMap.put("count", count);
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
						RenZhengPersonList mResult = gson.fromJson(
								result,RenZhengPersonList.class);
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
	//支付
	public void pay(Context context,String selectId,String selectResourceId, int multiple,
			final CallBack<TicketBack> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "902";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		String request_token =  UUID.randomUUID().toString();
		ParamMap.put("request_token",request_token);
		ParamMap.put("selectId", selectId);
		ParamMap.put("multiple", multiple);
		ParamMap.put("selectResourceId", selectResourceId);
		User user = MyDbUtils.getCurrentUser();
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
						TicketBack mResult = gson.fromJson(
								result,TicketBack.class);
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
	//我的跟单和推荐
	public  void MyFollowRecommend(Context context,int start,int count,String userID,String password,String seatchType,
			final CallBack<MyFollowRecommendList> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "903";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("start", start);
		ParamMap.put("count", count);
		ParamMap.put("seatchType", seatchType);
		ParamMap.put("userId", userID);
		ParamMap.put("userPwd", password);
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
						MyFollowRecommendList mResult = gson.fromJson(
								result,MyFollowRecommendList.class);
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
	public   void getMyRecommendDetil(Context context,int start,int count,String schemeId,
			final CallBack<MyRecommendDetilList> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}

		String wAction = "904";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("start", start);
		ParamMap.put("count", count);
		ParamMap.put("schemeId", schemeId);

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
						MyRecommendDetilList mResult = gson.fromJson(
								result,MyRecommendDetilList.class);
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
	//关注
	public void setFouce(Context context,String otherID,String userID,String password,String eventType,
			final CallBack<Back> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "905";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("eventType", eventType);
		ParamMap.put("otherUserId", otherID);
		ParamMap.put("userId", userID);
		ParamMap.put("userPwd", password);
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
						Back mResult = gson.fromJson(
								result,Back.class);
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
	//关注人
	public  void MyFouces(Context context,int start,int count,String userID,String password,
			final CallBack<NiuRenList> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "906";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("start", start);
		ParamMap.put("count", count);
		ParamMap.put("userId", userID);
		ParamMap.put("userPwd", password);
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
						NiuRenList mResult = gson.fromJson(
								result,NiuRenList.class);
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
	//推荐详情
	public void getFollowDetil(Context context,String geniusUserId,String userID,String password,
			final CallBack<NiuRen> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "907";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("geniusUserId", geniusUserId);
		ParamMap.put("userId", userID);
		ParamMap.put("userPwd", password);
		BaseHttps.getInstance().postHttpRequest(context,
				GetCommonParam(IP_TICKET,wAction, ParamMap),
				new BaseHttpsCallback<String>() {  

			@Override  
			public void onSuccess(String result) {  
				String processId, errMsg;
				try {
					JSONObject jsonObject = new JSONObject(result);
					processId = jsonObject.getString("processId");
					if ("0".equals(processId)&&
							jsonObject.getString("result")!=null) {
						Gson gson = new Gson();
						NiuRen mResult = gson.fromJson(
								jsonObject.getString("result"),NiuRen.class);
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
	//牛人详情
	public  void getNiuRenDetil(Context context,int start,int count,String geniusUserId,String userID,String password,
			final CallBack<NiuRenDetil> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "908";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("start", start);
		ParamMap.put("count", count);
		ParamMap.put("geniusUserId", geniusUserId);
		ParamMap.put("userId", userID);
		ParamMap.put("userPwd", password);
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
						NiuRenDetil mResult = gson.fromJson(
								result,NiuRenDetil.class);
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
