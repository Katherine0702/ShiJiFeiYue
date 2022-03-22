package com.cshen.tiyu.net.https;


import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.NowPrize;
import com.cshen.tiyu.domain.PrizeList;
import com.cshen.tiyu.domain.ball.BFZBDateList;
import com.cshen.tiyu.domain.ball.BasketBallMatchList;
import com.cshen.tiyu.domain.ball.FootBallMatchList;
import com.cshen.tiyu.domain.ball.HttpZcMatchListResult;
import com.cshen.tiyu.domain.ball.HttpZcMatchResult;
import com.cshen.tiyu.domain.ball.JCLQTicketDetailList;
import com.cshen.tiyu.domain.ball.URLBack;
import com.cshen.tiyu.domain.dltssq.DltPeriodData;
import com.cshen.tiyu.domain.dltssq.HttpDltPeriodData;
import com.cshen.tiyu.domain.dltssq.HttpSSQPeriodData;
import com.cshen.tiyu.domain.dltssq.SSQPeriodData;
import com.cshen.tiyu.domain.fast3.MissDateList;
import com.cshen.tiyu.domain.login.User;
import com.cshen.tiyu.domain.order.OrderDetail;
import com.cshen.tiyu.domain.sfc.SFCMatchList;
import com.cshen.tiyu.domain.taocan.TaoCanResult;
import com.cshen.tiyu.domain.zhuihao.AddOrderList;
import com.cshen.tiyu.utils.NetWorkUtil;
import com.google.gson.Gson;


public class ServiceCaiZhongInformation extends ServiceABase{
	private static ServiceCaiZhongInformation instance;

	public static ServiceCaiZhongInformation getInstance() {
		if (null == instance) {
			instance = new ServiceCaiZhongInformation();
		}
		return instance;
	}
	public void pastUrl(Context context,String matchKey,
									final CallBack<URLBack> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "136";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("matchKey", matchKey);
		BaseHttps.getInstance().postHttpRequest(context,
				GetCommonParam(IP_TICKET,wAction, ParamMap),
				new BaseHttpsCallback<String>() {

					@Override
					public void onSuccess(String result) {
						String processId, errMsg;
						try {
							JSONObject jsonObject = new JSONObject(result);
							processId =  jsonObject.getString("processId");
							if ("0".equals(processId)) {
								Gson gson = new Gson();
								URLBack mResult = gson.fromJson(
										result,URLBack.class);
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
	//足球数据
	public void pastFootBallMatches(Context context,String periodNumber, String playType,
			String type, String wLotteryId,String specialFlag,
			final CallBack<FootBallMatchList> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "104";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("periodNumber", periodNumber);
		ParamMap.put("playType", playType);// 混合投注4
		ParamMap.put("type", type);// 混合投注2
		ParamMap.put("specialFlag", specialFlag);// index
		ParamMap.put("wLotteryId", wLotteryId);
		BaseHttps.getInstance().postHttpRequest(context,
				GetCommonParam(IP_TICKET,wAction, ParamMap),
				new BaseHttpsCallback<String>() {  

			@Override  
			public void onSuccess(String result) { 
				String processId, errMsg;
				try {
					JSONObject jsonObject = new JSONObject(result);
					processId =  jsonObject.getString("processId");    
					if ("0".equals(processId)) {
						Gson gson = new Gson();
						FootBallMatchList mResult = gson.fromJson(
								result,FootBallMatchList.class);
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
	//足球比分直播
	public void pastFootBallBFZB(Context context, String schemeId,
			String lotteryId, final CallBack<BFZBDateList> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "134";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		User user = MyDbUtils.getCurrentUser();
		if (user != null) {
			ParamMap.put("userId", user.getUserId());
			ParamMap.put("userPwd", user.getUserPwd());
		}
		ParamMap.put("wLotteryId", lotteryId);
		ParamMap.put("id", schemeId);
		BaseHttps.getInstance().postHttpRequest(context,
				GetCommonParam(IP_TICKET,wAction, ParamMap),
				new BaseHttpsCallback<String>() {  

			@Override  
			public void onSuccess(String result) { 
				String processId, errMsg;
				try {
					JSONObject jsonObject = new JSONObject(result);
					processId =  jsonObject.getString("processId");    
					if ("0".equals(processId)) {
						Gson gson = new Gson();
						BFZBDateList mResult = gson.fromJson(
								result,BFZBDateList.class);
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
	//篮球数据
	public void pastBasketBallMatches(Context context,String periodNumber, String playType,
			String type, String wLotteryId,
			final CallBack<BasketBallMatchList> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "104";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("periodNumber", periodNumber);
		ParamMap.put("playType", playType);// 混合投注4
		ParamMap.put("type", type);// 混合投注2
		ParamMap.put("wLotteryId", wLotteryId);
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
						BasketBallMatchList mResult = gson.fromJson(
								result,BasketBallMatchList.class);
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
	//篮球出票详情
	public void getJCLQTicketDetail(final Context context, int start,int count,int wLotteryId,int schemeId,
			final CallBack<JCLQTicketDetailList> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "135";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		User user = MyDbUtils.getCurrentUser();
		if (user != null) {
			ParamMap.put("userId", user.getUserId());
			ParamMap.put("userPwd", user.getUserPwd());
		}
		ParamMap.put("start", start);
		ParamMap.put("count", count);
		ParamMap.put("wLotteryId", wLotteryId);
		ParamMap.put("schemeId", schemeId);
		BaseHttps.getInstance().postHttpRequest(context,
				GetCommonParam(IP_TICKET, wAction, ParamMap),
				new BaseHttpsCallback<String>() {
			@Override
			public void onSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					try {
						Gson gson = new Gson();
						JCLQTicketDetailList info = gson.fromJson(
								result, JCLQTicketDetailList.class);
						if ("0".equals(info.getProcessId())) {
							callBack.onSuccess(info);// 类型不定用t
						} else {
							callBack.onFailure(new ErrorMsg("-1",
									"访问出错"));
						}
						return;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				callBack.onFailure(new ErrorMsg("-1", "访问出错"));
			}

			@Override
			public void onError(int code, String message) {
				callBack.onFailure(new ErrorMsg("-1",
						getWrongBack(message)));
			}

			@Override
			public void onFinished() {
			}
		});
	}
	//胜负彩、任选九数据
	public void pastSFCMatches(Context context,String periodNumber, String playType,
			String type, String wLotteryId,
			final CallBack<SFCMatchList> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "104";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("periodNumber", periodNumber);
		ParamMap.put("playType", playType);// 混合投注4
		ParamMap.put("type", type);// 混合投注2
		ParamMap.put("wLotteryId", wLotteryId);
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
						SFCMatchList mResult = gson.fromJson(
								result,SFCMatchList.class);
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
	//历史开奖数据
	public void pastPrizeList(Context context,String caizhong, String start, String count,
			String missType, final CallBack<PrizeList> callBack) {
		if(context!=null&&!NetWorkUtil.isNetworkAvailable(context)){
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return ;
		}
		String wAction = "";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("start", start);
		ParamMap.put("count", count);
		ParamMap.put("wLotteryId", caizhong);
		if(!TextUtils.isEmpty(missType)){
			ParamMap.put("missType", missType);
			wAction = "118";//11选5
		}else{
			wAction = "108";//其他
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
						PrizeList mResult = gson.fromJson(
								result,PrizeList.class);
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
	//最新开奖号码和期次 
	public void pastNowPrize(Context context,String caizhong,
			final CallBack<NowPrize> callBack) {
		if(context!=null&&!NetWorkUtil.isNetworkAvailable(context)){
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return ;
		}
		String wAction = "804";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("wLotteryId", caizhong);
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
						NowPrize mResult = gson.fromJson(
								result, NowPrize.class);
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
	
	public void getDLT_period_detail(Context context, String periodId,final CallBack<DltPeriodData> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "805";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("wLotteryId", 13);
		ParamMap.put("periodId", periodId);
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
						HttpDltPeriodData mResult = gson.fromJson(
								result,HttpDltPeriodData.class);
						if ("0".equals(mResult.getProcessId())) {
							callBack.onSuccess(mResult
									.getDltPeriodData());// 类型不定用t
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
	public void getSSQ_period_detail(Context context, String periodId,final CallBack<SSQPeriodData> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "805";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("wLotteryId", 0);
		ParamMap.put("periodId", periodId);

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
						HttpSSQPeriodData mResult = gson.fromJson(
								result,HttpSSQPeriodData.class);
						if ("0".equals(mResult.getProcessId())) {
							callBack.onSuccess(mResult
									.getSsqPeriodData());// 类型不定用t
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

	public void getZc_Macth_Result(Context context, String matchDate,
			String wLotteryId, final CallBack<HttpZcMatchListResult> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "105";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("wLotteryId", wLotteryId);
		ParamMap.put("matchDate", matchDate);
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
						HttpZcMatchListResult mResult = gson.fromJson(
								result,HttpZcMatchListResult.class);
						if ("0".equals(mResult.getProcessId())) {
							callBack.onSuccess(mResult);

						} else {
							callBack.onFailure(new ErrorMsg("-1",
									mResult.getErrorMsg()));
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

	public void getZc_first_Macth_Result(Context context,String wLotteryId,
			final CallBack<HttpZcMatchResult> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "817";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		if(!TextUtils.isEmpty(wLotteryId)){
			ParamMap.put("wLotteryId", wLotteryId);
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

						HttpZcMatchResult mResult = gson.fromJson(
								result,HttpZcMatchResult.class);

						if ("0".equals(mResult.getProcessId())) {
							callBack.onSuccess(mResult);

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
	public void new_getZc_Macth_Result(Context context, String matchDate, int start,int count,
			String wLotteryId, final CallBack<HttpZcMatchListResult> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "105";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("wLotteryId", wLotteryId);
		if (!TextUtils.isEmpty(matchDate)) {
			ParamMap.put("matchDate", matchDate);
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
						HttpZcMatchListResult mResult = gson.fromJson(
								result,HttpZcMatchListResult.class);
						if ("0".equals(mResult.getProcessId())) {
							callBack.onSuccess(mResult);

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
	/**
	 * 查询竞彩历史开镜 每天场次数
	 * @param context
	 * @param matchDate
	 * @param callBack
	 */
	public void getZc_Macth_Number(Context context, String matchDate,
			final CallBack<String> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "818";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("matchDate", matchDate);

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
						callBack.onSuccess(jsonObject.getString("result"));
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
	//快三遗漏
	public void getYiLou(Context context,String caizhong, final CallBack<MissDateList> callBack) {
		if(context!=null&&!NetWorkUtil.isNetworkAvailable(context)){
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return ;
		}
		String wAction = "133";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("wLotteryId", caizhong);
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
						MissDateList mResult = gson.fromJson(
								result, MissDateList.class);
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
	public void getTaoCan(Context context,
			final CallBack<TaoCanResult> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "808";

		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("from", "1");
		BaseHttps.getInstance().postHttpRequest(context,
				GetCommonParam(IP_TICKET,wAction, ParamMap),
				new BaseHttpsCallback<String>() {  

			@Override  
			public void onSuccess(String result) {  
				Gson gson = new Gson();
				TaoCanResult mResult = gson.fromJson(
						result,TaoCanResult.class);
				if ("0".equals(mResult.getProcessId())) {						
					callBack.onSuccess(mResult);
				} else {
					callBack.onFailure(new ErrorMsg("-1", getWrongBack(
							mResult.getErrorMsg())));
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
	public void getAddOrder(Context context, String count, String start,
			String type, final CallBack<AddOrderList> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "819";
		User user = MyDbUtils.getCurrentUser();
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("userId", user.getUserId());
		ParamMap.put("userPwd", user.getUserPwd());
		ParamMap.put("count", count);
		ParamMap.put("start", start);
		ParamMap.put("type", type);//keno 高频 number 数字彩 

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
						AddOrderList mResult = gson.fromJson(
								result,AddOrderList.class);
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
	public void getAddOrderDetail(Context context, String chaseId, String wlotteryId,
			final CallBack<OrderDetail> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "820";
		User user = MyDbUtils.getCurrentUser();
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("userId", user.getUserId());
		ParamMap.put("userPwd", user.getUserPwd());
		ParamMap.put("chaseId", chaseId);
		ParamMap.put("wLotteryId",wlotteryId);

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
						OrderDetail mResult = gson.fromJson(
								result,OrderDetail.class);
						if ("0".equals(mResult.getProcessId())) {
							callBack.onSuccess(mResult);// 类型不定用t
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
}
