package com.cshen.tiyu.net.https;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.deprecated.HttpActivityForPaypage;
import com.cshen.tiyu.domain.login.User;
import com.cshen.tiyu.domain.pay.ActivityForPayPage;
import com.cshen.tiyu.domain.pay.HttpPayUrlData;
import com.cshen.tiyu.domain.pay.HttpPayWayConfig;
import com.cshen.tiyu.domain.pay.IpsorderReslut;
import com.cshen.tiyu.domain.pay.TicketBackResult;
import com.cshen.tiyu.domain.redpacket.RedPacketData;
import com.cshen.tiyu.utils.NetWorkUtil;
import com.cshen.tiyu.utils.NetWorkUtils;
import com.google.gson.Gson;



public class ServicePay extends ServiceABase{
	private static ServicePay instance;

	public static ServicePay getInstance() {
		if (null == instance) {
			instance = new ServicePay();
		}
		return instance;
	}
	// 充值
	public void setCharge(Context context,double money, String payWay,
			final CallBack<IpsorderReslut> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return ;
		}
		String wAction = "203";
		User user = MyDbUtils.getCurrentUser();
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("userId", user.getUserId());
		ParamMap.put("userPwd", user.getUserPwd());
		ParamMap.put("userWay", "APHONE");
		ParamMap.put("payWay", payWay);
		ParamMap.put("amount", money + "");
		BaseHttps.getInstance().postHttpRequest(context,GetCommonParam(IP_TICKET,wAction, ParamMap),
				new BaseHttpsCallback<String>() {  
			@Override  
			public void onSuccess(String result) { 
				String processId, errMsg;
				try {
					JSONObject jsonObject = new JSONObject(result);
					processId = jsonObject.getString("processId");
					if ("0".equals(processId)) {
						Gson gson = new Gson();
						IpsorderReslut mResult = gson.fromJson(
								result,IpsorderReslut.class);
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
	//支付第一步
	public void PostCathectic(Context context,int activityId,String wLotteryId,String  json2,
			final CallBack<TicketBackResult> callback) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callback.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return ;
		}
		String wAction = "101";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("wLotteryId", wLotteryId);
		ParamMap.put("ticket", json2);
		if(activityId!=-2){
			ParamMap.put("activityId", activityId);
		}
		BaseHttps.getInstance().postHttpRequest(context,GetCommonParam(IP_TICKET,wAction, ParamMap),
				new BaseHttpsCallback<String>() {  
			@Override  
			public void onSuccess(String result) {  
				if (!TextUtils.isEmpty(result)) {
					try {
						Gson gson = new Gson();
						TicketBackResult ticket = gson.fromJson(
								result,TicketBackResult.class);
						if ("0".equals(ticket.getProcessId())) {
							callback.onSuccess(ticket);// 类型不定用t
						}else{
							callback.onFailure(new ErrorMsg("-1", 
									getWrongBack(ticket.getErrorMsg())));
						}
					} catch (Exception e) {
						e.printStackTrace();
						callback.onFailure(new ErrorMsg("-1", 
								getWrongBack(e.getMessage())));
					}
				}
			}
			@Override  
			public void onError(int code, String message) {  
				callback.onFailure(new ErrorMsg("-1", getWrongBack(message)));
			}  
			@Override  
			public void onFinished() {  }  
		});
	}
	// 即买即付第二步
	public void setAccount(Context context,double money, String payWay,int wLotteryId,
			long hongbaoId,String schemeId,final CallBack<IpsorderReslut> callBack) {
		String wAction = "822";
		User user = MyDbUtils.getCurrentUser();
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("userId", user.getUserId());
		ParamMap.put("userPwd", user.getUserPwd());
		ParamMap.put("userWay", "APHONE");
		ParamMap.put("payWay", payWay);
		ParamMap.put("amount", money + "");

		ParamMap.put("wLotteryId", wLotteryId);
		ParamMap.put("hongbaoId", hongbaoId+"");
		ParamMap.put("schemeId",schemeId );
		BaseHttps.getInstance().postHttpRequest(context,GetCommonParam(IP_TICKET,wAction, ParamMap),
				new BaseHttpsCallback<String>() {  
			@Override  
			public void onSuccess(String result) {  
				String processId, errMsg;
				try {
					JSONObject jsonObject = new JSONObject(result);
					processId = jsonObject.getString("processId");
					if ("0".equals(processId)) {
						Gson gson = new Gson();
						IpsorderReslut mResult = gson.fromJson(
								result,IpsorderReslut.class);
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
	// 即买即付第三步
	/**
	 * 0 易宝 YEEPAY("易宝"), 1 支付宝 ALIPAY("支付宝"), 2 支付宝ALIPAY_PHONE("手机支付宝"), 3
	 * 汇潮支付 ECPSS("汇潮支付"), 4 首信易支付YIZHIFU("首信易支付"), 5 YEEPAY_ZGT("易宝-掌柜通");
	 **/
	public void getPayUrl(Context context,String dingdannumber, double money, int payWay,
			final CallBack<HttpPayUrlData> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String backURL = "http://getPayBack/#" + dingdannumber + "#";
		String wAction = "401";
		User user = MyDbUtils.getCurrentUser();
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("userId", user.getUserId());
		ParamMap.put("userPwd", user.getUserPwd());

		ParamMap.put("amount", money + "");
		ParamMap.put("bankName", "UnionPay_126");
		ParamMap.put("payWay", payWay);
		ParamMap.put("orderNo", dingdannumber);
		ParamMap.put("backURL", backURL);
		ParamMap.put("appType", "1");
		ParamMap.put("package_name", "green");
		if (payWay == 8 || payWay == 10) {
			ParamMap.put("payIP", NetWorkUtils.getLocalIpAddress(context));
		}if(payWay == 21){
			ParamMap.put("P_RequestType", "Android");
		}

		BaseHttps.getInstance().postHttpRequest(context,GetCommonParam(IP_TICKET,wAction, ParamMap),
				new BaseHttpsCallback<String>() {  
			@Override  
			public void onSuccess(String result) {  
				String processId, errMsg;
				try {
					JSONObject jsonObject = new JSONObject(result);
					processId = jsonObject.getString("processId");
					if ("0".equals(processId)) {
						Gson g = new Gson();
						HttpPayUrlData t= g.fromJson(
								result, HttpPayUrlData.class);
						if (!TextUtils.isEmpty(t.getPayUrl())) {
							callBack.onSuccess(t);
						} else {
							callBack.onFailure(new ErrorMsg("-1","payurl为空"));
						}
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
	/*// 即买即付第四步，获取支付结果
	public void setCaiZhongOrder(Context context,int activityId,
			String lotteryId, String schemeTempId,
			String dingdannumber, final CallBack<TicketBackResult> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "121";
		User user = MyDbUtils.getCurrentUser();
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("userId", user.getUserId());
		ParamMap.put("userPwd", user.getUserPwd());

		ParamMap.put("wLotteryId", lotteryId);
		ParamMap.put("schemeTempId", schemeTempId);
		ParamMap.put("orderId", dingdannumber);
		if(activityId != -2 ){
			ParamMap.put("activityId", activityId);
		}
		BaseHttps.getInstance().postHttpRequest(context,GetCommonParam(IP_TICKET,wAction, ParamMap),
				new BaseHttpsCallback<String>() {  
			@Override  
			public void onSuccess(String result) {  
				String processId, errMsg;
				try {
					JSONObject jsonObject = new JSONObject(result);
					processId = jsonObject.getString("processId");
					if ("0".equals(processId)) {
						Gson gson = new Gson();
						TicketBackResult ticket = gson.fromJson(
								result,TicketBackResult.class);
						if ("0".equals(ticket.getProcessId())) {
							callBack.onSuccess(ticket);// 类型不定用t
						} else {
							callBack.onFailure(new ErrorMsg("-1","访问出错"));
						}
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

	}*/
	// 获取支付方式
	public void GetPayWay(Context context,int payFor,
			final CallBack<HttpPayWayConfig> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return ;
		}
		String wAction = "806";
		User user = MyDbUtils.getCurrentUser();
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("userId", user.getUserId());
		ParamMap.put("userPwd", user.getUserPwd());
		ParamMap.put("from", "Android");
		ParamMap.put("payfor", payFor+"");
		BaseHttps.getInstance().postHttpRequest(context,GetCommonParam(IP_TICKET,wAction, ParamMap),
				new BaseHttpsCallback<String>() {  
			@Override  
			public void onSuccess(String result) {  
				if (!TextUtils.isEmpty(result)) {
					try {
						Gson gson = new Gson();
						HttpPayWayConfig mResult = gson.fromJson(
								result,HttpPayWayConfig.class);
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
	// 获取付钱时的红包
	public void getRedPacketPayOrder(Context context, String money,
			String lotteryId, final CallBack<RedPacketData> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "802";
		User user = MyDbUtils.getCurrentUser();
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("userId", user.getUserId());
		ParamMap.put("userPwd", user.getUserPwd());
		ParamMap.put("schemeMoney", money);
		ParamMap.put("usefor", lotteryId);
		BaseHttps.getInstance().postHttpRequest(context,GetCommonParam(IP_TICKET,wAction, ParamMap),
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
	//支付结果页面获取
	public void getActivityForPayPage(Context context, int lotteryId,String playType,
			final CallBack<ActivityForPayPage> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "807";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("wLotteryId", lotteryId);
		ParamMap.put("playType", playType);
		BaseHttps.getInstance().postHttpRequest(context,GetCommonParam(IP_TICKET,wAction, ParamMap),
				new BaseHttpsCallback<String>() {  
			@Override  
			public void onSuccess(String result) {  
				String processId, errMsg;
				try {
					JSONObject jsonObject = new JSONObject(result);
					processId = jsonObject.getString("processId");
					if ("0".equals(processId)) {
						Gson gson = new Gson();
						HttpActivityForPaypage mResult = gson.fromJson(
								result,HttpActivityForPaypage.class);
						if ("0".equals(mResult.getProcessId())) {
							callBack.onSuccess(mResult.getEntity());// 类型不定用t
						} else {
							callBack.onFailure(new ErrorMsg("-1",
									getWrongBack(mResult.getErrorMsg())));
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
	
	//获取是否是首次充值且充值金额大于等于20
		public void getIsFirstChargeAndThan20(Context context, String orderId,
				final CallBack<String> callBack) {
			if (!NetWorkUtil.isNetworkAvailable(context)) {
				callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
				return;
			}
			String wAction = "0005";

			Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
			User user = MyDbUtils.getCurrentUser();
			if (user!=null) {
				ParamMap.put("userId", user.getUserId());
				ParamMap.put("userPwd", user.getUserPwd());
				ParamMap.put("orderNo", orderId);

			}else {
				return;
			}

			BaseHttps.getInstance().postHttpRequest(context,GetCommonParam(IP_TICKET,wAction, ParamMap),
					new BaseHttpsCallback<String>() {  
				@Override  
				public void onSuccess(String result) {  
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(result);
						String processId = jsonObject.getString("processId");
						if ("0".equals(processId)) {
							String payedPush = jsonObject.getString("payedPush");
							callBack.onSuccess(payedPush);							
						}else if ("7".equals(processId)) {
							callBack.onFailure(new ErrorMsg("-1","系统内部错误"));	
						}else if ("2".equals(processId)) {
							callBack.onFailure(new ErrorMsg("-1","账户冻结"));	
						}else {
							String msg=jsonObject.getString("errorMsg");
							callBack.onFailure(new ErrorMsg("-1", "参数错误："+msg==null?"":msg));	
						}
					} catch (JSONException e) {
						e.printStackTrace();
						callBack.onFailure(new ErrorMsg("-1", e.getMessage()));	
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
