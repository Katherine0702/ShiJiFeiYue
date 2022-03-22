package com.cshen.tiyu.net.https;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.cshen.tiyu.base.CaiPiaoApplication;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.HttpOrderDetailInfo;
import com.cshen.tiyu.domain.PeriodResultData;
import com.cshen.tiyu.domain.account.CathecticItem;
import com.cshen.tiyu.domain.deprecated.DetailAccountData;
import com.cshen.tiyu.domain.deprecated.ForgetUserMobileResultData;
import com.cshen.tiyu.domain.login.AccountList;
import com.cshen.tiyu.domain.login.User;
import com.cshen.tiyu.domain.login.UserInfoResultData;
import com.cshen.tiyu.domain.login.UserResultData;
import com.cshen.tiyu.domain.login.UserTime;
import com.cshen.tiyu.domain.order.OrderData;
import com.cshen.tiyu.domain.pay.TicketBackResult;
import com.cshen.tiyu.utils.NetWorkUtil;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.SecurityUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import de.greenrobot.event.EventBus;

public class ServiceUser extends ServiceABase {
	private static ServiceUser instance;

	public static ServiceUser getInstance() {
		if (null == instance) {
			instance = new ServiceUser();
		}
		return instance;
	}

	/**
	 * 支付宝登录
	 */
	public void PostZFBLogin(final Context context, String authCode,
							 final CallBack<UserResultData> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "234";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("authCode", authCode);
		BaseHttps.getInstance().postHttpRequest(context,
				GetCommonParam(IP_TICKET, wAction, ParamMap),
				new BaseHttpsCallback<String>() {
					@Override
					public void onSuccess(String result) {
						if (!TextUtils.isEmpty(result)) {
							try {
								Gson gson = new Gson();
								UserResultData mResult = gson.fromJson(result,
										UserResultData.class);
								if ("0".equals(mResult.processId)) {
									if (!TextUtils.isEmpty(mResult.isNew)
											&& "0".endsWith(mResult.isNew)) {
										MyDbUtils.saveUser(mResult.user);
										GetUserInfo(context);
									}
									callBack.onSuccess(mResult);

								} else {
									callBack.onFailure(new ErrorMsg(
											mResult != null ? mResult.processId
													: "-1",
											getWrongBack(mResult.errorMsg)));
								}
								return;
							} catch (Exception e) {
								e.printStackTrace();
								callBack.onFailure(new ErrorMsg("-1",
										getWrongBack(e.getMessage())));

							}
						} else {
							callBack.onFailure(new ErrorMsg("-1", ""));
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

	/**
	 * 支付宝用户登陆201
	 */
	public void PostLogin(final Context context, String userName,
						  String password, final CallBack<UserResultData> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "201";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("userName", userName);
		ParamMap.put("userPwd", password);

		BaseHttps.getInstance().postHttpRequest(context,
				GetCommonParam(IP_TICKET, wAction, ParamMap),
				new BaseHttpsCallback<String>() {
					@Override
					public void onSuccess(String result) {
						if (!TextUtils.isEmpty(result)) {
							Log.d("tag", "result  " + result);
							try {
								Gson gson = new Gson();
								UserResultData mResult = gson.fromJson(result,
										UserResultData.class);
								if ("0".equals(mResult.processId)) {
									MyDbUtils.saveUser(mResult.user);
									GetUserInfo(context);
									callBack.onSuccess(mResult);

								} else {
									callBack.onFailure(new ErrorMsg(
											mResult != null ? mResult.processId
													: "-1",
											getWrongBack(mResult.errorMsg)));

								}
								return;
							} catch (Exception e) {
								e.printStackTrace();
								callBack.onFailure(new ErrorMsg("-1",
										getWrongBack(e.getMessage())));
							}
						} else {
							callBack.onFailure(new ErrorMsg("-1", ""));
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

	/**
	 *
	 * @return
	 */
	public void PostRegistByMobile(final Context context, String mobile,
								   String userName, String password, String validateCode,
								   final CallBack<UserResultData> callBack) {

		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "814";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("mobile", mobile);
		ParamMap.put("userName", userName);
		ParamMap.put("userPwd", SecurityUtil.md5(password));
		ParamMap.put("validateCode", validateCode);
		ParamMap.put("userWay", "APHONE");
		ParamMap.put("mid", "1");//主站

		//ParamMap.put("mid", "76");
		//ParamMap.put("mid", "86");//360
		//ParamMap.put("mid", "80");//微米
		//ParamMap.put("mid", "65");//渠道7
		//ParamMap.put("mid", "69");//推啊
		//ParamMap.put("mid", "79");//券小美
		//ParamMap.put("mid", "59");//魅族
		//ParamMap.put("mid", "63");//券妈妈
		//ParamMap.put("mid", "15");//推啊
		//ParamMap.put("mid", "36");//百度
		//ParamMap.put("mid", "37");//豌豆荚
		//ParamMap.put("mid", "38");//vivo
		//ParamMap.put("mid", "39");//oppo
		//ParamMap.put("mid", "47");//应用宝
		//ParamMap.put("mid", "48");//华为
		//ParamMap.put("mid", "49");//小米

		if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
			ParamMap.put("appId", ((TelephonyManager) context
					.getSystemService(context.TELEPHONY_SERVICE)).getDeviceId());
		}

		BaseHttps.getInstance().postHttpRequest(context,
				GetCommonParam(IP_TICKET, wAction, ParamMap),
				new BaseHttpsCallback<String>() {
			@Override
			public void onSuccess(String result) {
				try {
					Gson gson = new Gson();
					UserResultData mResult = gson.fromJson(result,
							UserResultData.class);
					if ("0".equals(mResult.processId)) {
						MyDbUtils.removeAllUser();
						MyDbUtils.saveUser(mResult.user);
						EventBus.getDefault().post("updateUserName");
						GetUserInfo(context);
						callBack.onSuccess(mResult);

					} else {
						callBack.onFailure(new ErrorMsg(
								mResult != null ? mResult.processId
										: "-1",
										getWrongBack(mResult.errorMsg)));

					}
					return;
				} catch (Exception e) {
					e.printStackTrace();
					callBack.onFailure(new ErrorMsg("-1",
							getWrongBack(e.getMessage())));

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

	// 代码复用
	public void PostRegistByName(final Context context, String userName,
			String password, String mobile, String validateCode,
			final CallBack<UserResultData> callBack) {

		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "202";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("userName", userName);
		ParamMap.put("userPwd", SecurityUtil.md5(password));
		ParamMap.put("mobile", mobile);
		ParamMap.put("validateCode", validateCode);
		ParamMap.put("userWay", "APHONE");
		ParamMap.put("mid", "2");
		BaseHttps.getInstance().postHttpRequest(context,
				GetCommonParam(IP_TICKET, wAction, ParamMap),
				new BaseHttpsCallback<String>() {
			@Override
			public void onSuccess(String result) {
				try {
					Gson gson = new Gson();
					UserResultData mResult = gson.fromJson(result,
							UserResultData.class);
					if ("0".equals(mResult.processId)) {
						MyDbUtils.removeAllUser();
						MyDbUtils.saveUser(mResult.user);
						EventBus.getDefault().post("updateUserName");
						GetUserInfo(context);
						callBack.onSuccess(mResult);

					} else {
						callBack.onFailure(new ErrorMsg(
								mResult != null ? mResult.processId
										: "-1",
										getWrongBack(mResult.errorMsg)));
					}
					return;
				} catch (Exception e) {
					e.printStackTrace();
					callBack.onFailure(new ErrorMsg("-1",
							getWrongBack(e.getMessage())));
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

	public void PostVerif(Context context, String mobile,
			final CallBack<UserResultData> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "222";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("mobile", mobile);
		ParamMap.put("from", "APHONE");
		ParamMap.put("index", 1);

		BaseHttps.getInstance().postHttpRequest(context,
				GetCommonParam(IP_TICKET, wAction, ParamMap),
				new BaseHttpsCallback<String>() {
			@Override
			public void onSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					try {
						Gson gson = new Gson();
						UserResultData mResult = gson.fromJson(result,
								UserResultData.class);
						if ("0".equals(mResult.processId)) {
							callBack.onSuccess(mResult);
						} else {
							callBack.onFailure(new ErrorMsg(
									mResult != null ? mResult.processId
											: "-1",
											getWrongBack(mResult.errorMsg)));
						}
						return;
					} catch (Exception e) {
						e.printStackTrace();
						callBack.onFailure(new ErrorMsg("-1",
								getWrongBack(e.getMessage())));

					}
				}
				callBack.onFailure(new ErrorMsg("-1", ""));
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

	public void newPostVerif(Context context, String mobile,
			Boolean checkUserName, final CallBack<UserResultData> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "811";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("mobile", mobile);
		ParamMap.put("from", "APHONE");
		ParamMap.put("index", 1);
		ParamMap.put("checkUserName", checkUserName);

		BaseHttps.getInstance().postHttpRequest(context,
				GetCommonParam(IP_TICKET, wAction, ParamMap),
				new BaseHttpsCallback<String>() {
			@Override
			public void onSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					try {
						Gson gson = new Gson();
						UserResultData mResult = gson.fromJson(result,
								UserResultData.class);
						if ("0".equals(mResult.processId)) {
							callBack.onSuccess(mResult);
						} else {
							callBack.onFailure(new ErrorMsg(
									mResult != null ? mResult.processId
											: "-1",
											getWrongBack(mResult.errorMsg)));
						}
						return;
					} catch (Exception e) {
						e.printStackTrace();
						callBack.onFailure(new ErrorMsg("-1",
								getWrongBack(e.getMessage())));

					}
				}
				callBack.onFailure(new ErrorMsg("-1", ""));
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

	public void PostVerifLoginWithYanZhengMa(Context context, String mobile,
			final CallBack<UserResultData> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "227";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		try {
			String md51 = SecurityUtil.md5((mobile + ConstantsBase.KEY));
			ParamMap.put("mobile", mobile);
			ParamMap.put("mobilekey", SecurityUtil.md5(md51));
			ParamMap.put("from", "APHONE");
			ParamMap.put("index", 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		BaseHttps.getInstance().postHttpRequest(context,
				GetCommonParam(IP_TICKET, wAction, ParamMap),
				new BaseHttpsCallback<String>() {
			@Override
			public void onSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					try {
						Gson gson = new Gson();
						UserResultData mResult = gson.fromJson(result,
								UserResultData.class);
						if ("0".equals(mResult.processId)) {
							callBack.onSuccess(mResult);
						} else {
							callBack.onFailure(new ErrorMsg(
									mResult != null ? mResult.processId
											: "-1", mResult.errorMsg));
						}
						return;
					} catch (Exception e) {
						e.printStackTrace();
						callBack.onFailure(new ErrorMsg("-1",
								getWrongBack(e.getMessage())));

					}
				}
				callBack.onFailure(new ErrorMsg("-1", ""));
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

	public void newPostVerifLoginWithYanZhengMa(Context context, String mobile,
			final CallBack<UserResultData> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "816";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		try {
			String md51 = SecurityUtil.md5((mobile + ConstantsBase.KEY));
			ParamMap.put("mobile", mobile);
			ParamMap.put("mobilekey", SecurityUtil.md5(md51));
			ParamMap.put("from", "APHONE");
			ParamMap.put("index", 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		BaseHttps.getInstance().postHttpRequest(context,
				GetCommonParam(IP_TICKET, wAction, ParamMap),
				new BaseHttpsCallback<String>() {
			@Override
			public void onSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					try {
						Gson gson = new Gson();
						UserResultData mResult = gson.fromJson(result,
								UserResultData.class);
						if ("0".equals(mResult.processId)) {
							callBack.onSuccess(mResult);
						} else {
							callBack.onFailure(new ErrorMsg(
									mResult != null ? mResult.processId
											: "-1",
											getWrongBack(mResult.errorMsg)));
						}
						return;
					} catch (Exception e) {
						e.printStackTrace();
						callBack.onFailure(new ErrorMsg("-1",
								getWrongBack(e.getMessage())));
					}
				}
				callBack.onFailure(new ErrorMsg("-1", ""));
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

	// 验证码登录，参数手机号，验证码，回调函数
	public void PostLoginWithYanZhengMa(final Context context, String mobile,
			String yanzhengma, final CallBack<AccountList> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "228";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		try {
			ParamMap.put("mobile", mobile);
			ParamMap.put("validateCode", yanzhengma);
		} catch (Exception e) {
			e.printStackTrace();
		}
		BaseHttps.getInstance().postHttpRequest(context,
				GetCommonParam(IP_TICKET, wAction, ParamMap),
				new BaseHttpsCallback<String>() {
					@Override
					public void onSuccess(String result) {
						if (!TextUtils.isEmpty(result)) {
							try {
								Gson gson = new Gson();
								AccountList mResult = gson.fromJson(result,
										AccountList.class);
								if ("0".equals(mResult.processId)) {
									ArrayList<UserTime> userlst = mResult
											.getUserlst();
									String isNew = mResult.getIsNew();
									if (userlst != null) {
										if ("1".equals(isNew)
												|| userlst.size() == 1) {
											if (userlst.get(0) != null) {
												MyDbUtils.removeAllUser();
												MyDbUtils.saveUser(userlst.get(
														0).getUser());
												GetUserInfo(context);
											} else {
												callBack.onFailure(new ErrorMsg(
														"-1", "登录失败"));
											}
										}
										callBack.onSuccess(mResult);
									} else {
										callBack.onFailure(new ErrorMsg("-1",
												"登录失败"));
									}
								} else {
									callBack.onFailure(new ErrorMsg(
											mResult != null ? mResult.processId
													: "-1",
											getWrongBack(mResult.errorMsg)));
								}
								return;
							} catch (Exception e) {
								e.printStackTrace();
								callBack.onFailure(new ErrorMsg("-1",
										getWrongBack(e.getMessage())));
							}
						} else {
							callBack.onFailure(new ErrorMsg("-1", "登录失败"));
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

	/*
	 * 用户资料查看和修改 (个人资料)（209）
	 */
	public void PostUserInfo(final Context context, String update,
			String realName, String idcard, String password,
			String passworduser, String payPasswordOld, String payPasswordNew,
			String mobile, final String updatePayPassword,
			final boolean renzhengName, final CallBack<String> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "209";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		User user = MyDbUtils.getCurrentUser();
		if (user != null) {
			ParamMap.put("userId", user.getUserId());// 用户id
			ParamMap.put("update", update);// 是否是更新
			if (!TextUtils.isEmpty(realName)) {
				ParamMap.put("realName", realName);// 实名（可为空）
			}
			if (!TextUtils.isEmpty(idcard)) {
				ParamMap.put("idcard", idcard);// 身份证（可为空）
			}
			if (!TextUtils.isEmpty(password)) {
				ParamMap.put("userPwd", password);// 密码
			}
			if (!TextUtils.isEmpty(passworduser)) {
				ParamMap.put("realNamePwd", passworduser);// 明文密码（可为空）
			} else {
				ParamMap.put("realNamePwd", "");
			}
			if (!TextUtils.isEmpty(payPasswordOld)) {
				ParamMap.put("payPasswordOld", payPasswordOld);// 旧密码（可为空）
			}
			if (!TextUtils.isEmpty(payPasswordNew)) {
				ParamMap.put("payPasswordNew", payPasswordNew);// 新密码
			}
			if (!TextUtils.isEmpty(mobile)) {
				ParamMap.put("mobile", mobile);// 手机号码
			}
			if (!TextUtils.isEmpty(updatePayPassword)) {
				ParamMap.put("updatePayPassword", updatePayPassword);// 更新支付密码
			}

			BaseHttps.getInstance().postHttpRequest(context,
					GetCommonParam(IP_TICKET, wAction, ParamMap),
					new BaseHttpsCallback<String>() {
						@Override
						public void onSuccess(String result) {
							if (!TextUtils.isEmpty(result)) {
								Gson gson = new Gson();
								UserInfoResultData mResult = gson.fromJson(
										result, UserInfoResultData.class);
								if ("0".equals(mResult.getProcessId())) {

									MyDbUtils.saveUserInfo(mResult.userInfo);
									PreferenceUtil.putString(
											CaiPiaoApplication.getmContext(),
											"payPassword",
											mResult.getPayPassword());
									callBack.onSuccess("");
								} else {
									callBack.onFailure(new ErrorMsg(
											mResult != null ? mResult.processId
													: "-1",
											getWrongBack(mResult.errorMsg)));
								}
							} else {
								callBack.onFailure(new ErrorMsg("-1", ""));
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
	}

	/**
	 * 修改昵称
	 */
	public void PostChangeName(Context context, String nickname,
			final CallBack<String> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "229";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		User user = MyDbUtils.getCurrentUser();
		if (user != null) {
			ParamMap.put("userId", user.getUserId());
			ParamMap.put("userPwd", user.getUserPwd());
			ParamMap.put("usernickname", nickname);
			BaseHttps.getInstance().postHttpRequest(context,
					GetCommonParam(IP_TICKET, wAction, ParamMap),
					new BaseHttpsCallback<String>() {
						@Override
						public void onSuccess(String result) {
							if (!TextUtils.isEmpty(result)) {
								try {
									JSONObject jsonObject = new JSONObject(
											result);
									String processId = jsonObject
											.getString("processId");
									if ("0".equals(processId)) {
										callBack.onSuccess("修改成功");
									} else {
										String ErrorMsg = jsonObject
												.getString("errorMsg");
										callBack.onFailure(new ErrorMsg("-1",
												getWrongBack(ErrorMsg)));
									}
								} catch (Exception e) {
									e.printStackTrace();
									callBack.onFailure(new ErrorMsg("-1",
											getWrongBack(e.getMessage())));
								}
							} else {
								callBack.onFailure(new ErrorMsg("-1", "没有数据"));
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
	}

	/**
	 * 绑定手机号（502）
	 */
	public void oldPostBindMobile(final Context context, String mobile,
			String code, final CallBack<String> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "502";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		User user = MyDbUtils.getCurrentUser();
		if (user != null) {
			ParamMap.put("mobile", mobile);// 手机号码
			ParamMap.put("code", code);// 验证码
			ParamMap.put("userId", user.getUserId());
			ParamMap.put("userPwd", user.getUserPwd());
			BaseHttps.getInstance().postHttpRequest(context,
					GetCommonParam(IP_TICKET, wAction, ParamMap),
					new BaseHttpsCallback<String>() {
						@Override
						public void onSuccess(String result) {
							if (!TextUtils.isEmpty(result)) {
								try {
									JSONObject jsonObject = new JSONObject(
											result);
									String processId = jsonObject
											.getString("processId");
									if ("0".equals(processId)) {
										GetUserInfo(context);
										callBack.onSuccess("");
									} else {
										String msg = jsonObject
												.getString("errorMsg");
										ErrorMsg errorMsg = new ErrorMsg(
												processId, getWrongBack(msg));
										callBack.onFailure(errorMsg);
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									callBack.onFailure(new ErrorMsg("-1",
											getWrongBack(e.getMessage())));

								}
							} else {
								callBack.onFailure(new ErrorMsg("-1", ""));
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
	}

	/**
	 * 绑定手机号（810）
	 */
	public void PostBindMobile(final Context context, String mobile,
			String code, String newPwd, String authCode,
			final CallBack<UserResultData> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "810";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		User user = MyDbUtils.getCurrentUser();

		ParamMap.put("mobile", mobile);// 手机号码
		ParamMap.put("code", code);// 验证码
		if (user != null) {
			ParamMap.put("userId", user.getUserId());
			ParamMap.put("userPwd", user.getUserPwd());
		}
		if (!TextUtils.isEmpty(newPwd)) {
			ParamMap.put("newPwd", SecurityUtil.md5(newPwd).toUpperCase());
		}
		if (!TextUtils.isEmpty(authCode)) {
			ParamMap.put("authCode", authCode);
		}
		BaseHttps.getInstance().postHttpRequest(context,
				GetCommonParam(IP_TICKET, wAction, ParamMap),
				new BaseHttpsCallback<String>() {
					@Override
					public void onSuccess(String result) {
						if (!TextUtils.isEmpty(result)) {
							try {
								Gson gson = new Gson();
								UserResultData mResult = gson.fromJson(result,
										UserResultData.class);
								if ("0".equals(mResult.getProcessId())) {
									if (null != mResult.getUser()) {
										MyDbUtils.saveUser(mResult.user);
										GetUserInfo(context);
									}
									callBack.onSuccess(mResult);
								} else {
									String msg = mResult.getErrorMsg();
									ErrorMsg errorMsg = new ErrorMsg(mResult
											.getProcessId(), getWrongBack(msg));
									callBack.onFailure(errorMsg);
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								callBack.onFailure(new ErrorMsg("-1",
										getWrongBack(e.getMessage())));

							}
						} else {
							callBack.onFailure(new ErrorMsg("-1", ""));
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

	/**
	 * 换绑手机号（505）
	 * 
	 * @param code
	 */
	public void PostUnBindMobile(final Context context, String mobile,
			String mobilenew, String code, final CallBack<String> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "812";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		User user = MyDbUtils.getCurrentUser();
		if (user != null) {
			ParamMap.put("userId", user.getUserId());
			ParamMap.put("userPwd", user.getUserPwd());
			ParamMap.put("mobile", mobile);// 旧的手机号码
			ParamMap.put("mobileNew", mobilenew);// 新的手机号
			ParamMap.put("code", code);// 新手机号码的验证码
			BaseHttps.getInstance().postHttpRequest(context,
					GetCommonParam(IP_TICKET, wAction, ParamMap),
					new BaseHttpsCallback<String>() {
						@Override
						public void onSuccess(String result) {
							if (!TextUtils.isEmpty(result)) {
								try {

									JSONObject jsonObject = new JSONObject(
											result);
									String processId = jsonObject
											.getString("processId");
									if ("0".equals(processId)) {
										GetUserInfo(context);
										callBack.onSuccess("");
									} else {
										String msg = jsonObject
												.getString("errorMsg");
										ErrorMsg errorMsg = new ErrorMsg(
												processId, getWrongBack(msg));
										callBack.onFailure(errorMsg);
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									callBack.onFailure(new ErrorMsg("-1",
											getWrongBack(e.getMessage())));

								}
							} else {
								callBack.onFailure(new ErrorMsg("-1", ""));
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
	}

	/**
	 * 绑定银行卡
	 * 
	 * @param code
	 */
	public void PostBindBankCard(Context context, String realName,
			String idCard, String bankName, String bankCard,
			String partProvince, String partCity, String partBankName,
			final CallBack<String> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "504";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		User user = MyDbUtils.getCurrentUser();
		if (user != null) {
			ParamMap.put("userId", user.getUserId());
			ParamMap.put("userPwd", user.getUserPwd());
			ParamMap.put("realName", realName);
			ParamMap.put("idCard", idCard);
			ParamMap.put("bankName", bankName);
			ParamMap.put("bankCard", bankCard);
			ParamMap.put("partProvince", partProvince);
			ParamMap.put("partCity", partCity);
			ParamMap.put("partBankName", partBankName);
			BaseHttps.getInstance().postHttpRequest(context,
					GetCommonParam(IP_TICKET, wAction, ParamMap),
					new BaseHttpsCallback<String>() {
						@Override
						public void onSuccess(String result) {
							if (!TextUtils.isEmpty(result)) {
								try {
									JSONObject jsonObject = new JSONObject(
											result);
									String processId = jsonObject
											.getString("processId");
									if ("0".equals(processId)) {
										callBack.onSuccess("");
									} else {
										String msg = jsonObject
												.getString("errorMsg");
										ErrorMsg errorMsg = new ErrorMsg(
												processId, getWrongBack(msg));
										callBack.onFailure(errorMsg);
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									callBack.onFailure(new ErrorMsg("-1",
											getWrongBack(e.getMessage())));
								}
							} else {
								callBack.onFailure(new ErrorMsg("-1", ""));
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
	}

	/**
	 * 220修改登录密码
	 * 
	 * @param code
	 */
	public void PostLoginPwd(Context context, String oldPassword,
			String newPassword, final CallBack<String> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "220";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		User user = MyDbUtils.getCurrentUser();
		if (user != null) {
			ParamMap.put("newPwd", SecurityUtil.md5(newPassword).toUpperCase());
			ParamMap.put("userId", user.getUserId());
			ParamMap.put("userPwd", user.getUserPwd());
			ParamMap.put("oldPwd", SecurityUtil.md5(oldPassword).toUpperCase());
			BaseHttps.getInstance().postHttpRequest(context,
					GetCommonParam(IP_TICKET, wAction, ParamMap),
					new BaseHttpsCallback<String>() {
						@Override
						public void onSuccess(String result) {
							if (!TextUtils.isEmpty(result)) {
								try {
									JSONObject jsonObject = new JSONObject(
											result);
									String processId = jsonObject
											.getString("processId");
									if ("0".equals(processId)) {
										callBack.onSuccess("");
									} else {
										String msg = jsonObject
												.getString("errorMsg");
										ErrorMsg errorMsg = new ErrorMsg(
												processId, msg);
										callBack.onFailure(errorMsg);
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									callBack.onFailure(new ErrorMsg("-1",
											getWrongBack(e.getMessage())));
								}
							} else {
								callBack.onFailure(new ErrorMsg("-1", ""));
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
	}

	public void GetUserInfo(Context context) {

		PostUserInfo(context, "0", null, null, MyDbUtils.getCurrentUser()
				.getUserPwd(), null, null, null, null, null, false,
				new CallBack<String>() {
					@Override
					public void onSuccess(String s) {
						EventBus.getDefault().post("updateUserInfo");
						EventBus.getDefault().post("updateUserName");
						EventBus.getDefault().post("updateNotice");
					}

					@Override
					public void onFailure(ErrorMsg errorMsg) {
					}
				});
	}

	public void PostDistll(final Context context, String amount, String update,
			final CallBack<String> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "210";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		User user = MyDbUtils.getCurrentUser();
		if (user != null) {
			ParamMap.put("userId", user.getUserId());
			ParamMap.put("userPwd", user.getUserPwd());
			ParamMap.put("amount", amount);
			ParamMap.put("update", update);
			BaseHttps.getInstance().postHttpRequest(context,
					GetCommonParam(IP_TICKET, wAction, ParamMap),
					new BaseHttpsCallback<String>() {
						@Override
						public void onSuccess(String result) {
							if (!TextUtils.isEmpty(result)) {
								try {
									JSONObject jsonObject = new JSONObject(
											result);
									String processId = jsonObject
											.getString("processId");
									if ("0".equals(processId)) {
										GetUserInfo(context);
										callBack.onSuccess("");
									} else {
										String msg = jsonObject
												.getString("errorMsg");
										ErrorMsg errorMsg = new ErrorMsg(
												processId, getWrongBack(msg));
										callBack.onFailure(errorMsg);
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									callBack.onFailure(new ErrorMsg("-1",
											getWrongBack(e.getMessage())));
								}
							} else {
								callBack.onFailure(new ErrorMsg("-1", ""));
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
	}

	// 代码复用
	/**
	 * 用户追号接口(账户明细) type
	 * 
	 * @return
	 */
	public void PostChaseNumber(Context context, String count, String start,
			String userId, String userPwd,
			final CallBack<DetailAccountData> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "206";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("count", count);
		ParamMap.put("start", start);
		ParamMap.put("userPwd", userPwd);
		ParamMap.put("userId", userId);
		BaseHttps.getInstance().postHttpRequest(context,
				GetCommonParam(IP_TICKET, wAction, ParamMap),
				new BaseHttpsCallback<String>() {
					@Override
					public void onSuccess(String result) {
						if (!TextUtils.isEmpty(result)) {
							try {
								Gson gson = new Gson();
								DetailAccountData mResult = gson.fromJson(
										result, DetailAccountData.class);
								if ("0".equals(mResult.processId)) {
									callBack.onSuccess(mResult);// 类型不定用t
								} else {
									callBack.onFailure(new ErrorMsg(
											mResult != null ? mResult.processId
													: "-1", "nono"));//
								}
								return;
							} catch (Exception e) {
								e.printStackTrace();
								callBack.onFailure(new ErrorMsg("-1",
										getWrongBack(e.getMessage())));
							}
						}
						callBack.onFailure(new ErrorMsg("-1", ""));
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

	// 代码复用
	/**
	 * 用户注册208,个人账户明细接口(账户明细) type
	 * 
	 * @return
	 */
	public void PostDetailAccount(Context context, String count, String start,
			String userId, String userPwd, String type, String timeType,
			final CallBack<ArrayList<CathecticItem>> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "208";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("count", count);
		ParamMap.put("start", start);
		ParamMap.put("timeType", timeType);
		ParamMap.put("userPwd", userPwd);
		ParamMap.put("userId", userId);
		ParamMap.put("type", type);// 全部=0投注=1提现=2派奖=3充值=4
		BaseHttps.getInstance().postHttpRequest(context,
				GetCommonParam(IP_TICKET, wAction, ParamMap),
				new BaseHttpsCallback<String>() {
					@Override
					public void onSuccess(String result) {
						if (!TextUtils.isEmpty(result)) {
							try {

								Gson gson = new Gson();
								JSONObject jsonObject2 = new JSONObject(result);

								JSONArray jsonArray = jsonObject2
										.getJSONArray("fundList");

								ArrayList<CathecticItem> items = new ArrayList<CathecticItem>();

								for (int i = 0; i < jsonArray.length(); i++) {

									String twoArray = jsonArray.getString(i);
									// 对象
									// 第二个数组

									CathecticItem item = new CathecticItem();

									JSONArray twoJsonArray = new JSONArray(
											twoArray);
									// 类型
									item.setType(twoJsonArray.getString(0));
									item.setMoney(twoJsonArray.getString(1));

									JSONObject jsonTimeJsonObject = new JSONObject(
											twoJsonArray.getString(2));

									long time = Long
											.parseLong(jsonTimeJsonObject.get(
													"time").toString());
									Date date = new Date(time);
									SimpleDateFormat sd = new SimpleDateFormat(
											"yyyy-MM-dd HH:mm:ss");
									String totalTimeString = sd.format(date);

									item.setTime1(totalTimeString.substring(0,
											11)); // reqi

									item.setTime2(totalTimeString.substring(11));// shijian

									item.setMoneyType(twoJsonArray.getString(3));

									items.add(item);
								}

								callBack.onSuccess(items);// 类型不定用t

								return;
							} catch (Exception e) {
								e.printStackTrace();
								callBack.onFailure(new ErrorMsg("-1",
										getWrongBack(e.getMessage())));
							}
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

	// 代码复用
	/**
	 * 用户注册207,采种期次接口 type
	 * 
	 * @return
	 */
	public void PostPeriod(Context context, String wLotteryId,
			final CallBack<PeriodResultData> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "207";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("wLotteryId", wLotteryId);
		BaseHttps.getInstance().postHttpRequest(context,
				GetCommonParam(IP_TICKET, wAction, ParamMap),
				new BaseHttpsCallback<String>() {
			@Override
			public void onSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					try {
						Gson gson = new Gson();
						PeriodResultData mResult = gson.fromJson(
								result, PeriodResultData.class);
						if ("0".equals(mResult.getProcessId())) {
							callBack.onSuccess(mResult);// 类型不定用t

						} else {
							if(mResult!=null&&!TextUtils.isEmpty(mResult.errorMsg)&&mResult.errorMsg.contains("系统内部错误")){
								callBack.onFailure(new ErrorMsg("-1",
										"期次获取错误"));
							}else{
								callBack.onFailure(new ErrorMsg("-1",
										getWrongBack(mResult.errorMsg)));
							}
						}

						return;
					} catch (Exception e) {
						e.printStackTrace();
						if(!TextUtils.isEmpty(e.getMessage())&&e.getMessage().contains("系统内部错误")){
							callBack.onFailure(new ErrorMsg("-1",
									"期次获取错误"));
						}else{
							callBack.onFailure(new ErrorMsg("-1",
									getWrongBack(e.getMessage())));
						}
					}
				}
			}

			@Override
			public void onError(int code, String message) {
				if(!TextUtils.isEmpty(message)&&message.contains("系统内部错误")){
					callBack.onFailure(new ErrorMsg("-1",
							"期次获取错误"));
				}else{
					callBack.onFailure(new ErrorMsg("-1",
							getWrongBack(message)));
				}
			}

			@Override
			public void onFinished() {
			}
		});
	}

	// 投注101接口

	// 代码复用
	/**
	 * 投注101接口数据 type 参数：彩种编号；gson转换过的json格式的彩票数据list（方便传递）
	 * 
	 * @return
	 */
	/*public void PostCathectic(Context context, UUID uuid, int activityId,
			String wLotteryId, String json2,
			final CallBack<TicketBackResult> callback) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callback.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "101";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("wLotteryId", wLotteryId);
		ParamMap.put("orderId", uuid.toString());
		ParamMap.put("ticket", json2);
		if (activityId != -2) {
			ParamMap.put("activityId", activityId);
		}
		BaseHttps.getInstance().postHttpRequest(context,
				GetCommonParam(IP_TICKET, wAction, ParamMap),
				new BaseHttpsCallback<String>() {
					@Override
					public void onSuccess(String result) {
						if (!TextUtils.isEmpty(result)) {
							try {
								Gson gson = new Gson();
								TicketBackResult ticket = gson.fromJson(result,
										TicketBackResult.class);
								if ("0".equals(ticket.getProcessId())) {
									callback.onSuccess(ticket);// 类型不定用t
								} else {
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
						callback.onFailure(new ErrorMsg("-1",
								getWrongBack(message)));
					}

					@Override
					public void onFinished() {
					}
				});
	}*/

	/**
	 * 投注001接口数据 type 参数：彩种编号；gson转换过的json格式的彩票数据list（方便传递）
	 * 
	 * @return
	 */
	/*
	 * public void PostCathecticTuiJian(Context context,UUID uuid,String
	 * wLotteryId,String json2, final CallBack<RecommendPayBackResult> callback)
	 * { if (!NetWorkUtil.isNetworkAvailable(context)) { callback.onFailure(new
	 * ErrorMsg("-1", "当前网络信号较差，请检查网络设置")); return ; } String wAction = "001";
	 * Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
	 * ParamMap.put("wLotteryId", wLotteryId); ParamMap.put("orderId",
	 * uuid.toString()); ParamMap.put("fox", json2);
	 * BaseHttps.getInstance().postHttpRequest
	 * (context,GetCommonParam(IP_TICKET,wAction, ParamMap), new
	 * BaseHttpsCallback<String>() {
	 * 
	 * @Override public void onSuccess(String result) { if
	 * (!TextUtils.isEmpty(result)) { try { Gson gson = new Gson();
	 * RecommendPayBackResult payback = gson.fromJson(
	 * result,RecommendPayBackResult.class); if
	 * ("0".equals(payback.getProcessId())) { callback.onSuccess(payback);//
	 * 类型不定用t }else{ callback.onFailure(new ErrorMsg("-1",
	 * getWrongBack(payback.getErrorMsg()))); } } catch (Exception e) {
	 * e.printStackTrace(); callback.onFailure(new ErrorMsg("-1", getWrongBack(e
	 * .getMessage()))); } } }
	 * 
	 * @Override public void onError(int code, String message) {
	 * callback.onFailure(new ErrorMsg("-1", getWrongBack(message))); }
	 * 
	 * @Override public void onFinished() { } }); }
	 */
	// 205 接口数据获取
	public void PostOrder(Context context, String count, String start,
			String userId, String userPwd, String isWon,
			String winningUpdateStatus, String isChase, String timeType,
			final CallBack<OrderData> callBack) {
		if (context != null && !NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "205";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();

		ParamMap.put("count", count);
		ParamMap.put("start", start);
		ParamMap.put("userId", userId);
		ParamMap.put("userPwd", userPwd);
		ParamMap.put("timeType", timeType);// 时间长度
		if (isWon != null) {
			ParamMap.put("won", isWon);// 中奖
		}
		if (winningUpdateStatus != null) {
			ParamMap.put("winningUpdateStatus", winningUpdateStatus);// 带开奖
		}
		if (isChase != null) {
			ParamMap.put("isChase", isChase);// 追号
		}
		Gson gson = new Gson();
		String wParam = gson.toJson(ParamMap);
		BaseHttps.getInstance().postHttpRequest(context,
				GetCommonParam(IP_TICKET, wAction, ParamMap),
				new BaseHttpsCallback<String>() {
			@Override
			public void onSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					try {
						Gson gson = new Gson();
						OrderData mResult = gson.fromJson(result,
								OrderData.class);
						if ("0".equals(mResult.getProcessId())) {
							callBack.onSuccess(mResult);// 类型不定用t
						} else {
							callBack.onFailure(new ErrorMsg("-1",
									getWrongBack(mResult.getErrorMsg())));
						}
						return;
					} catch (Exception e) {
						e.printStackTrace();
						callBack.onFailure(new ErrorMsg("-1",
								getWrongBack(e.getMessage())));
					}
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

	/**
	 * 826 忘记密码
	 * 
	 * @paramamount
	 * @paramupdate
	 * @param callBack
	 * @return
	 */
	public void ForgotLoginPwd_temp1(Context context, String userName,
			final CallBack<ForgetUserMobileResultData> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "826";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("userName", userName);
		BaseHttps.getInstance().postHttpRequest(context,
				GetCommonParam(IP_TICKET, wAction, ParamMap),
				new BaseHttpsCallback<String>() {
			@Override
			public void onSuccess(String result) {
				Log.d("tag","result" + result);
				if (!TextUtils.isEmpty(result)) {
					try {
						Gson gson = new Gson();
						ForgetUserMobileResultData mResult = gson
								.fromJson(
										result,
										ForgetUserMobileResultData.class);
						// processId 0:成功 1：用户名输入错误 2：未设置安全问题 3：未实名认证
						// 4：电话号码绑定多个
						Log.d("tag","result--" + mResult.processId);
						if ("0".equals(mResult.processId)
								|| "1".equals(mResult.processId)
								|| "2".equals(mResult.processId)
								|| "3".equals(mResult.processId)
								|| "4".equals(mResult.processId)) {

							callBack.onSuccess(mResult);
						} else {
							callBack.onFailure(new ErrorMsg(
									mResult != null ? mResult.processId
											: "-1",
											getWrongBack(mResult.errorMsg)));
						}
						return;
					} catch (Exception e) {
						e.printStackTrace();
						callBack.onFailure(new ErrorMsg("-1",
								getWrongBack(e.getMessage())));

					}

				}
				callBack.onFailure(new ErrorMsg("-1", ""));
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

	/**
	 * 514 是否存在用户名为userName的用户 813 是否存在用户名或者手机号为userName的用户
	 * 
	 * @paramamount
	 * @paramupdate
	 * @param callBack
	 * @return
	 */
	public void isExitUser(Context context, String userName, String mobile,
			final CallBack<ForgetUserMobileResultData> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "823";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("userName", userName);
		ParamMap.put("mobile", mobile);
		BaseHttps.getInstance().postHttpRequest(context,
				GetCommonParam(IP_TICKET, wAction, ParamMap),
				new BaseHttpsCallback<String>() {
					@Override
					public void onSuccess(String result) {
						if (!TextUtils.isEmpty(result)) {
							try {
								Gson gson = new Gson();
								ForgetUserMobileResultData mResult = gson
										.fromJson(
												result,
												ForgetUserMobileResultData.class);
								if ("0".equals(mResult.processId)) {

									callBack.onSuccess(mResult);
								} else {
									callBack.onFailure(new ErrorMsg(
											mResult != null ? mResult.processId
													: "-1",
											getWrongBack(mResult.errorMsg)));
								}
								return;
							} catch (Exception e) {
								e.printStackTrace();
								callBack.onFailure(new ErrorMsg("-1",
										getWrongBack(e.getMessage())));
							}
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

	/**
	 * 512 忘记密码2
	 * 
	 * updateFlag ： 0 获取验证码 1 验证验证码
	 * 
	 * @paramcode
	 * @paramamount
	 * @paramupdate
	 * @paramcallBack
	 * @return
	 */
	public void ForgotLoginPwd_Temp2(Context context, String mobile,
			String userName, String updateFlag, String validateCode,
			final CallBack<UserResultData> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "512";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("mobile", mobile);
		ParamMap.put("userName", userName);
		ParamMap.put("from", "Android");
		ParamMap.put("updateFlag", updateFlag);
		if (validateCode != null) {
			ParamMap.put("validateCode", validateCode == null ? ""
					: validateCode);
		}

		BaseHttps.getInstance().postHttpRequest(context,
				GetCommonParam(IP_TICKET, wAction, ParamMap),
				new BaseHttpsCallback<String>() {
					@Override
					public void onSuccess(String result) {
						if (!TextUtils.isEmpty(result)) {
							try {
								Gson gson = new Gson();
								UserResultData mResult = gson.fromJson(result,
										UserResultData.class);
								if ("0".equals(mResult.processId)) {
									callBack.onSuccess(mResult);
								} else {
									callBack.onFailure(new ErrorMsg(
											mResult != null ? mResult.processId
													: "-1",
											getWrongBack(mResult.errorMsg)));
								}
								return;
							} catch (Exception e) {
								e.printStackTrace();
								callBack.onFailure(new ErrorMsg("-1",
										getWrongBack(e.getMessage())));
							}
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

	/**
	 * 513
	 * 
	 * @parammobile
	 * @param userName
	 * @paramupdateFlag
	 * @paramvalidateCode
	 * @param callBack
	 * @return
	 */
	public void ForgotLoginPwd_Temp3(Context context, String newPwd,
			String userName, final CallBack<UserResultData> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "513";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		if (newPwd != null) {
			ParamMap.put("newPwd", SecurityUtil.md5(newPwd).toUpperCase());
		}
		if (userName != null) {
			ParamMap.put("userName", userName);
		}
		BaseHttps.getInstance().postHttpRequest(context,
				GetCommonParam(IP_TICKET, wAction, ParamMap),
				new BaseHttpsCallback<String>() {
					@Override
					public void onSuccess(String result) {
						if (!TextUtils.isEmpty(result)) {
							try {
								Gson gson = new Gson();
								UserResultData mResult = gson.fromJson(result,
										UserResultData.class);
								if ("0".equals(mResult.processId)) {
									callBack.onSuccess(mResult);
								} else {
									callBack.onFailure(new ErrorMsg(
											mResult != null ? mResult.processId
													: "-1", mResult.errorMsg));
								}
								return;
							} catch (Exception e) {
								e.printStackTrace();
								callBack.onFailure(new ErrorMsg("-1",
										getWrongBack(e.getMessage())));
							}
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

	public void PostFindOrderDetail(Context context, int schemeId,
			String lotteryId, final CallBack<HttpOrderDetailInfo> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "106";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		User user = MyDbUtils.getCurrentUser();
		if (user != null) {
			ParamMap.put("userId", user.getUserId());
			ParamMap.put("userPwd", user.getUserPwd());
		}
		ParamMap.put("wLotteryId", lotteryId);
		ParamMap.put("id", schemeId);
		BaseHttps.getInstance().postHttpRequest(context,
				GetCommonParam(IP_TICKET, wAction, ParamMap),
				new BaseHttpsCallback<String>() {
					@Override
					public void onSuccess(String result) {
						if (!TextUtils.isEmpty(result)) {
							try {
								Gson gson = new Gson();
								HttpOrderDetailInfo info = gson.fromJson(
										result, HttpOrderDetailInfo.class);
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

	public void findPwd(Context context, String newPassword, String mobile,
			String code, final CallBack<String> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "815";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("newPwd", SecurityUtil.md5(newPassword).toUpperCase());
		ParamMap.put("mobile", mobile);
		ParamMap.put("validateCode", code);
		BaseHttps.getInstance().postHttpRequest(context,
				GetCommonParam(IP_TICKET, wAction, ParamMap),
				new BaseHttpsCallback<String>() {
					@Override
					public void onSuccess(String result) {
						if (!TextUtils.isEmpty(result)) {
							try {
								JSONObject jsonObject = new JSONObject(result);
								String processId = jsonObject
										.getString("processId");
								if ("0".equals(processId)) {
									callBack.onSuccess("");
								} else {
									String msg = jsonObject
											.getString("errorMsg");
									ErrorMsg errorMsg = new ErrorMsg(processId,
											getWrongBack(msg));
									callBack.onFailure(errorMsg);
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								callBack.onFailure(new ErrorMsg("-1",
										getWrongBack(e.getMessage())));
							}
						} else {
							callBack.onFailure(new ErrorMsg("-1", "数据为空"));
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

	/**
	 * 827找回密码---匹配用户输入的安全问题的答案
	 * 
	 * @param context
	 * @param idCard
	 * @param newPassword
	 * @param realName
	 * @param answerJson
	 *            密保问题json字符串
	 * @param callBack
	 */
	public void MatchAnswers(Context context, int userId, String idCard,
			String newPassword, String realName, String answerJson,
			final CallBack<String> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "827";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("userId", userId);
		ParamMap.put("idCard", idCard);
		ParamMap.put("problemJson", answerJson);
		ParamMap.put("newPassWord", newPassword);
		ParamMap.put("realName", realName);
		BaseHttps.getInstance().postHttpRequest(context,
				GetCommonParam(IP_TICKET, wAction, ParamMap),
				new BaseHttpsCallback<String>() {
					@Override
					public void onSuccess(String result) {

						if (!TextUtils.isEmpty(result)) {
//							Log.d("result" + result);
							try {
								JSONObject jsonObject = new JSONObject(result);
								String processId = jsonObject
										.getString("processId");
								if (processId != null) {
									callBack.onSuccess(processId);
								} else {
									String msg = jsonObject
											.getString("errorMsg");
									ErrorMsg errorMsg = new ErrorMsg(processId,
											getWrongBack(msg));
									callBack.onFailure(errorMsg);
								}
							} catch (JSONException e) {
								e.printStackTrace();
								callBack.onFailure(new ErrorMsg("-1",
										getWrongBack(e.getMessage())));
							}
						} else {
							callBack.onFailure(new ErrorMsg("-1", "数据为空"));
						}
					}

					@Override
					public void onError(int code, String message) {
						callBack.onFailure(new ErrorMsg("-1",
								getWrongBack(message)));
					}

					@Override
					public void onFinished() {
//						Log.d("result--onFinished" + "message");
					}
				});
	}

	/**
	 * 824安全问题验证---获取密保的所有问题
	 * 
	 * @param context
	 * @param callBack
	 */
	public void getAllSafeQuestions(Context context,
			final CallBack<String> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "824";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		User user = MyDbUtils.getCurrentUser();

		if (user != null) {
			ParamMap.put("userId", user.getUserId());
			ParamMap.put("userPwd", user.getUserPwd());
		}
		BaseHttps.getInstance().postHttpRequest(context,
				GetCommonParam(IP_TICKET, wAction, ParamMap),
				new BaseHttpsCallback<String>() {
					@Override
					public void onSuccess(String result) {
//						Log.d("result " + result);
						String processId;
						try {			
							JSONObject jsonObject = new JSONObject(result);
							processId = jsonObject.getString("processId");
							if ("0".equals(processId)) {
								
									callBack.onSuccess(result);								
							} else {
								callBack.onFailure(new ErrorMsg("-1", "数据为空"));
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
	
	/**
	 * 825安全问题验证---提交用户输入的密保问题及答案
	 * 
	 * @param context
	 * @param callBack
	 */
	public void submitSafeQuestions(final Context context,String problemJson,
			final CallBack<String> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "825";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		User user = MyDbUtils.getCurrentUser();
		if (user != null) {
			ParamMap.put("userId", user.getUserId());
			ParamMap.put("userPwd", user.getUserPwd());
		}
		ParamMap.put("problemJson", problemJson);
		BaseHttps.getInstance().postHttpRequest(context,
				GetCommonParam(IP_TICKET, wAction, ParamMap),
				new BaseHttpsCallback<String>() {
			@Override
			public void onSuccess(String result) {
				//						Log.d("result " + result);
				String processId;
				try {
					JSONObject jsonObject = new JSONObject(result);
					processId = jsonObject.getString("processId");
					if ("0".equals(processId)) {
						try {
							EventBus.getDefault().post("updateNotice");
							PreferenceUtil.putBoolean(context, "hasSafeCertification",true);
							callBack.onSuccess(result);
						} catch (Exception e) {
							e.printStackTrace();
						}

					} else {
						callBack.onFailure(new ErrorMsg("-1", "数据为空"));
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
	
	 /*设置页面 216修改推送中奖通知
	 * 
	 * @param context
	 * @param callBack
	 */
	public void getChangeNoticeFlag(final Context context,int noticeFlag,
			final CallBack<String> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return;
		}
		String wAction = "910";
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		User user = MyDbUtils.getCurrentUser();
		if (user != null) {
			ParamMap.put("userId", user.getUserId());
			ParamMap.put("userPwd", user.getUserPwd());
		}
		ParamMap.put("noticeFlag", noticeFlag);
		BaseHttps.getInstance().postHttpRequest(context,
				GetCommonParam(IP_TICKET, wAction, ParamMap),
				new BaseHttpsCallback<String>() {
			@Override
			public void onSuccess(String result) {
				String processId;
				try {
					JSONObject jsonObject = new JSONObject(result);
					processId = jsonObject.getString("processId");
					if ("0".equals(processId)) {					
							callBack.onSuccess(processId);						
					} else {
						callBack.onFailure(new ErrorMsg("-1", "修改失败"));
					}
				} catch (JSONException e1) {
					e1.printStackTrace();
					callBack.onFailure(new ErrorMsg("-1", "修改失败："+e1.getMessage()));
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
	
	    /*设置页面 911 查询之前中奖状态通知
		 * 
		 * @param context
		 * @param callBack
		 */
		public void getQueryNoticeFlag(final Context context,final CallBack<String> callBack) {
			if (!NetWorkUtil.isNetworkAvailable(context)) {
				callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
				return;
			}
			String wAction = "911";
			Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
			User user = MyDbUtils.getCurrentUser();
			if (user != null) {
				ParamMap.put("userId", user.getUserId());
				ParamMap.put("userPwd", user.getUserPwd());
			}
			BaseHttps.getInstance().postHttpRequest(context,
					GetCommonParam(IP_TICKET, wAction, ParamMap),
					new BaseHttpsCallback<String>() {
				@Override
				public void onSuccess(String result) {
					try {
						JSONObject jsonObject = new JSONObject(result);
						boolean noticeFlag = jsonObject.getBoolean("noticeFlag");	
						if (noticeFlag) {
							callBack.onSuccess("true");		
						}
																
					} catch (JSONException e1) {
						e1.printStackTrace();
						callBack.onFailure(new ErrorMsg("-1",e1.getMessage()));
					}

				}

				@Override
				public void onError(int code, String message) {
					callBack.onFailure(new ErrorMsg("-1",getWrongBack(message)));
				}

				@Override
				public void onFinished() {
				}
			});
		}
}
