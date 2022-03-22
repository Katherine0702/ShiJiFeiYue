package com.cshen.tiyu.net.https;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.Back;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.Level;
import com.cshen.tiyu.domain.find.FindBean;
import com.cshen.tiyu.domain.find.FindBeanDatas;
import com.cshen.tiyu.domain.information.Information;
import com.cshen.tiyu.domain.information.InformationData;
import com.cshen.tiyu.domain.information.InformationDetailBack;
import com.cshen.tiyu.domain.login.User;
import com.cshen.tiyu.domain.main.HomeAdsData;
import com.cshen.tiyu.domain.main.HomeAdsInfo;
import com.cshen.tiyu.domain.main.HomePpwBean;
import com.cshen.tiyu.domain.main.LotteryType;
import com.cshen.tiyu.domain.main.LotteryTypeData;
import com.cshen.tiyu.domain.main.NewsBean;
import com.cshen.tiyu.domain.main.PreLoadPage;
import com.cshen.tiyu.domain.main.PreLoadPageData;
import com.cshen.tiyu.domain.main.TabIndicator;
import com.cshen.tiyu.domain.main.TabIndicatorData;
import com.cshen.tiyu.utils.NetWorkUtil;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.SecurityUtil;
import com.google.gson.Gson;

import android.content.Context;
import android.text.TextUtils;

public class ServiceMain extends ServiceABase{
	private static ServiceMain instance;

	public static ServiceMain getInstance() {
		if (null == instance) {
			instance = new ServiceMain();
		}
		return instance;
	}

	/**
	 * 预加载图106接口
	 */
	public void PostGetAdsData(Context context,
			final CallBack<PreLoadPageData> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return ;
		}
		String wAction = "106";//106接口数据
		BaseHttps.getInstance().getHttpRequest(context,GetCommonParam(HOMEIP_APP,wAction,null),
				new BaseHttpsCallback<String>() {  
			@Override  
			public void onSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					try {
						// 获取某些信息
						PreLoadPageData preLoadPageData = new PreLoadPageData();

						JSONObject jsonObject2 = new JSONObject(result);

						JSONArray jsonArray = jsonObject2
								.getJSONArray("indexIconList");

						for (int i = 0; i < jsonArray.length(); i++) {

							PreLoadPage preLoadPage = new PreLoadPage(); //

							JSONObject jsonObjectSon = (JSONObject) jsonArray
									.opt(i);

							preLoadPage.setIcon(jsonObjectSon.getString("icon"));
							preLoadPage.setType(jsonObjectSon.getInt("type"));
							preLoadPage.setUrl(jsonObjectSon.getString("url"));
							preLoadPageData.getPreLoadPages().add(preLoadPage);

						}						
						if (preLoadPageData != null) {									
							MyDbUtils.savePreLoadPageData(preLoadPageData);
						}
						
						callBack.onSuccess(preLoadPageData);
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
	/**
	 * 首页轮播图103接口
	 */
	public void PostGetHomeAdsData(Context context,final CallBack<HomeAdsData> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return ;
		}
		String wAction = "103";
		BaseHttps.getInstance().getHttpRequest(context,GetCommonParam(HOMEIP_APP,wAction,null),
				new BaseHttpsCallback<String>() {  
			@Override  
			public void onSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					try {
						// 获取某些信息
						HomeAdsData homeAdsData = new HomeAdsData();
						JSONObject jsonObject2 = new JSONObject(result);
						JSONArray jsonArray = jsonObject2
								.getJSONArray("adsList");
						for (int i = 0; i < jsonArray.length(); i++) {
							HomeAdsInfo homeAdsInfo = new HomeAdsInfo();
							JSONObject jsonObjectSon = (JSONObject) jsonArray.opt(i);
							homeAdsInfo.setIcon(jsonObjectSon.getString("icon"));
							homeAdsInfo.setRank(jsonObjectSon.getInt("rank"));
							homeAdsInfo.setStatus(jsonObjectSon.getInt("status"));
							homeAdsInfo.setTitle(jsonObjectSon.getString("title"));
							homeAdsInfo.setUrl(jsonObjectSon.getString("url"));
							homeAdsInfo.setLotteryId(jsonObjectSon.getString("lotteryId"));
							homeAdsInfo.setUseLocal(jsonObjectSon.getString("useLocal"));
							homeAdsInfo.setPlayType(jsonObjectSon.getString("playType"));
							homeAdsData.getAdsList().add(homeAdsInfo);
						}
						if (homeAdsData != null) {
							MyDbUtils.saveHomeAdsData(homeAdsData);
						}
						callBack.onSuccess(homeAdsData);
						return;
					} catch (Exception e) {
						e.printStackTrace();
						callBack.onFailure(new ErrorMsg("-1",getWrongBack(
								e.getMessage())));
					}
				}
				callBack.onFailure(new ErrorMsg("-1","首页轮播图103接口返回数据为空"));
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
	 * 首页轮播图103接口
	 */
	public void PostGetHomeAdsData2(Context context,final CallBack<HomeAdsData> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return ;
		}
		String wAction = "122";
		BaseHttps.getInstance().getHttpRequest(context,GetCommonParam(HOMEIP_APP,wAction,null),
				new BaseHttpsCallback<String>() {
					@Override
					public void onSuccess(String result) {
						if (!TextUtils.isEmpty(result)) {
							try {
								// 获取某些信息
								HomeAdsData homeAdsData = new HomeAdsData();
								JSONObject jsonObject2 = new JSONObject(result);
								JSONArray jsonArray = jsonObject2
										.getJSONArray("adsList");
								for (int i = 0; i < jsonArray.length(); i++) {
									HomeAdsInfo homeAdsInfo = new HomeAdsInfo();
									JSONObject jsonObjectSon = (JSONObject) jsonArray.opt(i);
									homeAdsInfo.setIcon(jsonObjectSon.getString("icon"));
									homeAdsInfo.setRank(jsonObjectSon.getInt("rank"));
									homeAdsInfo.setStatus(jsonObjectSon.getInt("status"));
									homeAdsInfo.setTitle(jsonObjectSon.getString("title"));
									homeAdsInfo.setUrl(jsonObjectSon.getString("url"));
									homeAdsInfo.setLotteryId(jsonObjectSon.getString("lotteryId"));
									homeAdsInfo.setUseLocal(jsonObjectSon.getString("useLocal"));
									homeAdsInfo.setPlayType(jsonObjectSon.getString("playType"));
									homeAdsData.getAdsList().add(homeAdsInfo);
								}
								if (homeAdsData != null) {
									MyDbUtils.saveHomeAdsData(homeAdsData);
								}
								callBack.onSuccess(homeAdsData);
								return;
							} catch (Exception e) {
								e.printStackTrace();
								callBack.onFailure(new ErrorMsg("-1",getWrongBack(
										e.getMessage())));
							}
						}
						callBack.onFailure(new ErrorMsg("-1","首页轮播图103接口返回数据为空"));
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
	 * 首页采种104接口
	 */
	public void PostGetLotteryTypeDatas(final Context context,
			final CallBack<LotteryTypeData> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return ;
		}
		String wAction = "104";
		BaseHttps.getInstance().getHttpRequest(context,GetCommonParam(HOMEIP_APP,wAction,null),
				new BaseHttpsCallback<String>() {  
			@Override  
			public void onSuccess(String result) {

				if (!TextUtils.isEmpty(result)) {
					try {
						// 获取某些信息
						LotteryTypeData lotteryTypeData = new LotteryTypeData();

						JSONObject jsonObject2 = new JSONObject(result);

						JSONArray jsonArray = jsonObject2
								.getJSONArray("lotteryList");
						for (int i = 0; i < jsonArray.length(); i++) {
							LotteryType lotteryType = new LotteryType();

							JSONObject jsonObjectSon = (JSONObject) jsonArray
									.opt(i);

							lotteryType.setIcon(jsonObjectSon
									.getString("icon"));

							lotteryType.setRank(jsonObjectSon
									.getInt("rank"));

							lotteryType.setInfo(jsonObjectSon
									.getString("info"));

							lotteryType.setTitle(jsonObjectSon
									.getString("title"));

							lotteryType.setUrl(jsonObjectSon
									.getString("url"));

							lotteryType.setLotteryId(jsonObjectSon
									.getString("lotteryId"));

							lotteryType.setInfoMsg(jsonObjectSon
									.getString("infoMsg"));
							lotteryType.setUseLocal(jsonObjectSon
									.getString("useLocal"));
							lotteryType.setPlayType(jsonObjectSon
									.getString("playType"));
							lotteryTypeData.getLotteryList().add(
									lotteryType);
						}
						if (lotteryTypeData != null) {
							MyDbUtils.saveLotteryTypeData(lotteryTypeData);

						}
						callBack.onSuccess(lotteryTypeData);
						return;
					} catch (Exception e) {
						e.printStackTrace();
						callBack.onFailure(new ErrorMsg("-1",getWrongBack(
								e.getMessage())));
					}
				}
				callBack.onFailure(new ErrorMsg("-1", "首页采种图104接口返回数据为空"));
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
	 * 首页105资讯接口 最新
	 */
	public void PostGetInformationDatas(Context context,
			final CallBack<InformationData> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return ;
		}
		String wAction = "105";
		BaseHttps.getInstance().getHttpRequest(context,GetCommonParam(HOMEIP_APP,wAction,null),
				new BaseHttpsCallback<String>() {  
			@Override  
			public void onSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					try {
						// 获取某些信息
						InformationData informationData = new InformationData();
						JSONObject jsonObject2 = new JSONObject(result);
						JSONArray jsonArray = jsonObject2
								.getJSONArray("infoList");
						for (int i = 0; i < jsonArray.length(); i++) {									
							Information information=new Information();									
							JSONObject jsonObjectSon = (JSONObject) jsonArray
									.opt(i);
							information.setId(jsonObjectSon.getString("id"));
							information.setClickable(jsonObjectSon.getBoolean("clickable"));
							information.setTitle(jsonObjectSon.getString("title"));
							informationData.getInfoList().add(information);
						}					
						MyDbUtils.saveInformationData(informationData);								
						callBack.onSuccess(informationData);
						return;
					} catch (Exception e) {
						e.printStackTrace();
						callBack.onFailure(new ErrorMsg("-1", ""));
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
	public void PostGetInformationDetail(Context context,String id,
			final CallBack<InformationDetailBack> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
		}
		String wAction = "107&&id="+id;
		BaseHttps.getInstance().getHttpRequest(context,GetCommonParam(HOMEIP_APP,wAction,null),
				new BaseHttpsCallback<String>() {  
			@Override  
			public void onSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					try {
						Gson gson = new Gson();
						InformationDetailBack mResult = gson.fromJson(result,
								InformationDetailBack.class);
						if ("0".equals(mResult.getErrCode())) {
							callBack.onSuccess(mResult);

						} else {
							callBack.onFailure(new ErrorMsg("-1", getWrongBack(mResult.getErrMsg())));
						}
						return;
					} catch (Exception e) {
						e.printStackTrace();
						callBack.onFailure(new ErrorMsg("-1", getWrongBack(e.getMessage())));
					}
				} else {
					callBack.onFailure(new ErrorMsg("-1", ""));
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
	 * 首页底部导航102接口
	 */
	public void PostGetTabIndicatorDatas(Context context,
			final CallBack<TabIndicatorData> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return ;
		}
		String wAction = "102";
		BaseHttps.getInstance().getHttpRequest(context,GetCommonParam(HOMEIP_APP,wAction,null),
				new BaseHttpsCallback<String>() {  
			@Override  
			public void onSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					try {

						// 获取某些信息
						TabIndicatorData tabIndicatorData = new TabIndicatorData();

						JSONObject jsonObject2 = new JSONObject(result);

						JSONArray jsonArray = jsonObject2
								.getJSONArray("tabsList");
						for (int i = 0; i < jsonArray.length(); i++) {
							TabIndicator tabIndicator = new TabIndicator();

							JSONObject jsonObjectSon = (JSONObject) jsonArray
									.opt(i);

							tabIndicator.setIcon_common(jsonObjectSon
									.getString("icon_common"));

							tabIndicator.setRank(jsonObjectSon
									.getInt("rank"));

							tabIndicator.setIcon_light(jsonObjectSon
									.getString("icon_light"));

							tabIndicator.setTitle(jsonObjectSon
									.getString("title"));

							tabIndicator.setUrl(jsonObjectSon
									.getString("url"));
							tabIndicator.setLotteryId(jsonObjectSon
									.getString("lotteryId"));

							tabIndicator.setUseLocal(jsonObjectSon
									.getString("useLocal"));
							tabIndicator.setPlayType(jsonObjectSon
									.getString("playType"));
							tabIndicator.setId(0);// 给个默认的id

							tabIndicatorData.getIndicators().add(
									tabIndicator);

						}
						// //保存至数据库
						if (tabIndicatorData != null) {
							MyDbUtils.saveTabIndicatorData(tabIndicatorData);
						}

						callBack.onSuccess(tabIndicatorData);

						return;
					} catch (Exception e) {
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
	 * 首页资讯
	 */

	public void PostGetHomeMessageData(Context context,int count ,final CallBack<NewsBean> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return ;
		}
		String url=ConstantsBase.IP_NEWS+"/news/newsList!showNewsList.action?start=0&count="+count+"&index=1";
//		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
//		ParamMap.put("start", 0);
//		ParamMap.put("count", count);
//		ParamMap.put("index", 1);
		
		BaseHttps.getInstance().getHttpRequest(context,GetCommonParamNoAction(url),
				new BaseHttpsCallback<String>() {  
			@Override  
			public void onSuccess(String result) {
				if (result!=null) {
					Gson gson=new Gson();
					try {
						NewsBean newsBean=gson.fromJson(result, NewsBean.class);
						if (newsBean.getProcessId().equals("0")) {
							if (newsBean.getNewslist()!=null) {
								callBack.onSuccess(newsBean);
							}
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
					
				    
						
				}else {
					callBack.onFailure(new ErrorMsg("-1","首页资讯消息获取失败"));
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
	 * 发现页面108接口 
	 */
	public void PostGetFindDatas(Context context,
			final CallBack<FindBeanDatas> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return ;
		}
		String wAction = "119";
		BaseHttps.getInstance().getHttpRequest(context,GetCommonParam(HOMEIP_APP,wAction,null),
				new BaseHttpsCallback<String>() {  
			@Override  
			public void onSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					try {
						// 获取某些信息
						FindBeanDatas findBeanDatas=new FindBeanDatas();
						JSONObject jsonObject = new JSONObject(result);
						JSONArray jsonArray = jsonObject.getJSONArray("findList");
						for (int i = 0; i < jsonArray.length(); i++) {
							FindBean findBean = new FindBean();
							JSONObject jsonObjectSon = (JSONObject) jsonArray.opt(i);

							findBean.setIcon(jsonObjectSon
									.getString("icon"));

							findBean.setRank(jsonObjectSon
									.getInt("rank"));

							findBean.setInfo(jsonObjectSon
									.getString("info"));

							findBean.setTitle(jsonObjectSon
									.getString("title"));

							findBean.setUrl(jsonObjectSon
									.getString("url"));

							findBean.setStatus(jsonObjectSon
									.getInt("status"));

							findBean.setUseLocal(jsonObjectSon
									.getInt("useLocal"));
							findBean.setTkey(jsonObjectSon
									.getString("tkey"));
							findBean.setId(jsonObjectSon
									.getInt("id"));

							findBeanDatas.getFindBeans().add(findBean);
						}
						
						callBack.onSuccess(findBeanDatas);
						return;
					} catch (Exception e) {
						e.printStackTrace();
						callBack.onFailure(new ErrorMsg("-1",getWrongBack(
								e.getMessage())));
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
	 * 0004五大联赛周一首次登录送5元红包
	 */
	public void PostFiveLeagueAcivity(final Context context,
			final CallBack<String> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "0004";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		User user = MyDbUtils.getCurrentUser();
		if (user != null) {
			ParamMap.put("userId", user.getUserId());
			ParamMap.put("userPwd", user.getUserPwd());
		}else {
			return;
		}
		BaseHttps.getInstance().postHttpRequest(context,
				GetCommonParam(IP_TICKET, wAction, ParamMap),
				new BaseHttpsCallback<String>() {
					@Override
					public void onSuccess(String result) {
						String hongbao;
						try {
							JSONObject jsonObject = new JSONObject(result);
							hongbao = jsonObject.getString("hongbao");
							if (hongbao!=null) {
								callBack.onSuccess(hongbao);					
							} else {
								
							}
						} catch (JSONException e1) {
							e1.printStackTrace();
						}

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

	public void GetIsShowingDLTLayout(Context context,
			final CallBack<String> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return ;
		}
		String wAction = "120";
		BaseHttps.getInstance().getHttpRequest(context,GetCommonParam(HOMEIP_APP,wAction,null),
				new BaseHttpsCallback<String>() {  
			@Override  
			public void onSuccess(String result) {
				String showPage="";
				if (!TextUtils.isEmpty(result)) {
					try {					
						JSONObject jsonObject = new JSONObject(result);
						showPage = jsonObject.getString("showPage");
						if (showPage!=null) {
							callBack.onSuccess(showPage);
						}else{
							callBack.onFailure(new ErrorMsg("-1",getWrongBack("showPage == null")));
						}
					} catch (Exception e) {
						e.printStackTrace();
						callBack.onFailure(new ErrorMsg("-1",getWrongBack(
								e.getMessage())));
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
	 * 保存友盟唯一标示参数接口 wAction 215
	 * @param context
	 * @param callBack
	 */
	public void getSaveDeviceToken(Context context,String deviceTokens,
			final CallBack<String> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return ;
		}
		
		String wAction = "909";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		User user = MyDbUtils.getCurrentUser();
		if (user != null) {
			ParamMap.put("userId", user.getUserId());
		}
		ParamMap.put("deviceTokens", deviceTokens);
		ParamMap.put("terminalName", 1);
		ParamMap.put("version", 1);
		//1 彩娃包名：com.cshen.tiyu
		//2 高球彩（高版本）包名：com.cainiu.zixun
		//3 高球彩（低版本）包名：com.cainiu.zixun
		//4 进球彩包名：com.cshen.tiyu
		//5 彩娃（应用宝）-原彩牛绿色包名：com.cshen.tiyu.channel
		//6快投彩-原彩牛包名：com.cshen.tiyu
		
		BaseHttps.getInstance().getHttpRequest(context,GetCommonParam(IP_TICKET,wAction,ParamMap),
				new BaseHttpsCallback<String>() {  
			@Override  
			public void onSuccess(String result) {
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(result);
					String processId = jsonObject.getString("processId");
					if ("0".equals(processId)) {
						callBack.onSuccess(processId);
					}else {
						String msg=jsonObject.getString("errorMsg");
						callBack.onFailure(new ErrorMsg("-1", "保存友盟唯一标示参数失败"+msg==null?"":msg));	
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
	/**
	 * 获取首页各种弹窗
	 * @param context
	 * @param callBack
	 */
	public void getHomePPWInfo(Context context,
			final CallBack<HomePpwBean> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return ;
		}

		String wAction = "0006";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		User user = MyDbUtils.getCurrentUser();
		if (user != null) {
			ParamMap.put("userId", user.getUserId());
			ParamMap.put("userPwd", user.getUserPwd());
		}else {
			callBack.onFailure(new ErrorMsg("-1", "您还未登录！"));
			return;
		}
		BaseHttps.getInstance().getHttpRequest(context,GetCommonParam(IP_TICKET,wAction,ParamMap),
				new BaseHttpsCallback<String>() {  
			@Override  
			public void onSuccess(String result) {
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(result);
					String processId = jsonObject.getString("processId");
					if ("0".equals(processId)) {
						HomePpwBean homePpwBean = new HomePpwBean();
						String pushPay = jsonObject.getString("pushPay");
						String pushBack = jsonObject.getString("pushBack");
						String pushWin = jsonObject.getString("pushWin");
						System.out.println("====pushPay"+pushPay);
						homePpwBean.setPushBack(pushBack);
						homePpwBean.setPushWin(pushWin);
						homePpwBean.setPushPay(pushPay);
						if ("1".equals(pushWin)) {
							
							String lotteryType = jsonObject.getString("lotteryType");
							
							if (lotteryType.equals((ConstantsBase.SFC+""))||lotteryType.equals("(ConstantsBase.PL35+")) {//胜负彩/任选九
								homePpwBean.setPlayType(jsonObject.getString("playType"));
							}
							homePpwBean.setMsg(jsonObject.getString("msg"));
							homePpwBean.setPrize(jsonObject.getString("prize"));
							homePpwBean.setLotteryType(lotteryType);
							homePpwBean.setSourceSchemeId(jsonObject.getString("sourceSchemeId"));
						}
					    callBack.onSuccess(homePpwBean);
						
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
	public void GetMyLevel(Context context,
							   final CallBack<Level> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return ;
		}

		String wAction = "830";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		User user = MyDbUtils.getCurrentUser();
		if (user != null) {
			ParamMap.put("userId", user.getUserId());
			ParamMap.put("userPwd", user.getUserPwd());
		}else {
			callBack.onFailure(new ErrorMsg("-1", "您还未登录！"));
			return;
		}
		BaseHttps.getInstance().getHttpRequest(context,GetCommonParam(IP_TICKET,wAction,ParamMap),
				new BaseHttpsCallback<String>() {
					@Override
					public void onSuccess(String result) {
						JSONObject jsonObject;
						try {
							jsonObject = new JSONObject(result);
							String processId = jsonObject.getString("processId");
							if ("0".equals(processId)) {
								Level homePpwBean = new Level();
								homePpwBean.setUserIntegrationGrade( jsonObject.getString("userIntegrationGrade"));
								callBack.onSuccess(homePpwBean);

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
	/**
	 * 资讯首页资讯
	 */
	public void PostGetZXHomeMessageData(Context context,final CallBack<NewsBean> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return ;
		}
		String url;
		url=ConstantsBase.IP_NEWS+"/news/newsList!showHotList.action?newstype=5&mType=13&start=0";	
		BaseHttps.getInstance().getHttpRequest(context,GetCommonParamNoAction(url),
				new BaseHttpsCallback<String>() {  
			@Override  
			public void onSuccess(String result) {
				if (result!=null) {
					Gson gson=new Gson();					
					NewsBean newsBean=gson.fromJson(result, NewsBean.class);
					if (newsBean.getProcessId().equals("0")) {
						if (newsBean.getNewslist()!=null) {
							callBack.onSuccess(newsBean);
						}
					}else if (newsBean.getProcessId().equals("2")) {//暂无数据，但服务器会返回获取资讯失败
						callBack.onFailure(new ErrorMsg("-1",getWrongBack("已无更多数据")));
					}else {
						callBack.onFailure(new ErrorMsg("-1",getWrongBack(newsBean.getErrMsg())));
					}
				    
						
				}else {
					callBack.onFailure(new ErrorMsg("-1","首页资讯消息获取失败"));
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
	 * 首页轮播图103接口
	 */
	public void PostGetMyData(Context context,final CallBack<FindBeanDatas> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return ;
		}
		String wAction = "123";
		BaseHttps.getInstance().getHttpRequest(context,GetCommonParam(HOMEIP_APP,wAction,null),
				new BaseHttpsCallback<String>() {
					@Override
					public void onSuccess(String result) {
						if (!TextUtils.isEmpty(result)) {
							try {
								// 获取某些信息
								FindBeanDatas findBeanDatas=new FindBeanDatas();
								JSONObject jsonObject = new JSONObject(result);
								JSONArray jsonArray = jsonObject.getJSONArray("mpList");
								for (int i = 0; i < jsonArray.length(); i++) {
									FindBean findBean = new FindBean();
									JSONObject jsonObjectSon = (JSONObject) jsonArray.opt(i);

									findBean.setIcon(jsonObjectSon
											.getString("icon"));

									findBean.setRank(jsonObjectSon
											.getInt("rank"));

									findBean.setInfo(jsonObjectSon
											.getString("titleDetail"));

									findBean.setTitle(jsonObjectSon
											.getString("title"));

									findBean.setUrl(jsonObjectSon
											.getString("url"));

									findBean.setStatus(jsonObjectSon
											.getInt("status"));

									findBean.setUseLocal(jsonObjectSon
											.getInt("useLocal"));
									findBean.setTkey(jsonObjectSon
											.getString("tkey"));
									findBean.setId(jsonObjectSon
											.getInt("id"));

									findBeanDatas.getFindBeans().add(findBean);

								}

								callBack.onSuccess(findBeanDatas);
								return;
							} catch (Exception e) {
								e.printStackTrace();
								callBack.onFailure(new ErrorMsg("-1",getWrongBack(
										e.getMessage())));
							}
						}
						callBack.onFailure(new ErrorMsg("-1"," "));
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
