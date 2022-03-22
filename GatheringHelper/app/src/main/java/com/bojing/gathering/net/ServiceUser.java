package com.bojing.gathering.net;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.bojing.gathering.base.ConstantsBase;
import com.bojing.gathering.domain.Back;
import com.bojing.gathering.domain.BackCommomString;
import com.bojing.gathering.domain.ErrorMsg;
import com.bojing.gathering.domain.OrderDetailBack;
import com.bojing.gathering.domain.OrderInfo;
import com.bojing.gathering.domain.OrderInfoBack;
import com.bojing.gathering.domain.UserBack;
import com.bojing.gathering.domain.UserInfo;
import com.bojing.gathering.domain.UserInfoBack;
import com.bojing.gathering.util.NetWorkUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by admin on 2018/10/15.
 */

public class ServiceUser extends ServiceABase {
    private static ServiceUser instance;

    public static ServiceUser getInstance() {
        if (null == instance) {
            instance = new ServiceUser();
        }
        return instance;
    }
    public void PostLogin(final Context context, String userName,
                          String password, final CallBack<UserBack> callBack) {
        if (!NetWorkUtil.isNetworkAvailable(context)) {
            callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
            return;
        }
        Map<String, String> ParamMap = new LinkedHashMap<String, String>();
        ParamMap.put("devCode", userName);
        ParamMap.put("devPwd", password);
        BaseHttps.getInstance().postHttpRequest(context,
                GetCommonParam(ConstantsBase.IP+"/admin/private/code/app/devLogin", ParamMap),
                new BaseHttpsCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        if (!TextUtils.isEmpty(result)) {
                            try {
                                Gson gson = new Gson();
                                UserBack mResult = gson.fromJson(result,UserBack.class);
                                if (200 == mResult.getStatus()) {
                                    callBack.onSuccess(mResult);
                                } else {
                                    callBack.onFailure(new ErrorMsg( mResult.getStatus()+"",getWrongBack(mResult.getMsg())));
                                }
                                return;
                            } catch (Exception e) {
                                e.printStackTrace();
                                callBack.onFailure(new ErrorMsg("-1",
                                        getWrongBack(e.getMessage())));
                            }
                        } else {
                            callBack.onFailure(new ErrorMsg("-1", "返回为空"));
                        }
                    }

                    @Override
                    public void onError(int code, String message) {
                        callBack.onFailure(new ErrorMsg("-1", getWrongBack(message)));
                    }

                    @Override
                    public void onFinished() {
                    }
                });
    }
    public void GetInfo(final Context context, String userName,
                          final CallBack<UserInfoBack> callBack) {
        if (!NetWorkUtil.isNetworkAvailable(context)) {
            callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
            return;
        }
        Map<String, String> ParamMap = new LinkedHashMap<String, String>();
        ParamMap.put("devCode", userName);
        BaseHttps.getInstance().postHttpRequest(context,
                GetCommonParam(ConstantsBase.IP+"/admin/private/code/app/devInfo", ParamMap),
                new BaseHttpsCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        if (!TextUtils.isEmpty(result)) {
                            try {
                                Gson gson = new Gson();
                                UserInfoBack mResult = gson.fromJson(result,UserInfoBack.class);
                                if (200 == mResult.getStatus()) {
                                    callBack.onSuccess(mResult);
                                } else {
                                    callBack.onFailure(new ErrorMsg( mResult.getStatus()+"",getWrongBack(mResult.getMsg())));
                                }
                                return;
                            } catch (Exception e) {
                                e.printStackTrace();
                                callBack.onFailure(new ErrorMsg("-1",
                                        getWrongBack(e.getMessage())));
                            }
                        } else {
                            callBack.onFailure(new ErrorMsg("-1", "返回为空"));
                        }
                    }

                    @Override
                    public void onError(int code, String message) {
                        callBack.onFailure(new ErrorMsg("-1", getWrongBack(message)));
                    }

                    @Override
                    public void onFinished() {
                    }
                });
    }
    public void GetOrder(final Context context, String userName,
                        final CallBack<OrderInfoBack> callBack) {
        if (!NetWorkUtil.isNetworkAvailable(context)) {
            callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
            return;
        }
        Map<String, String> ParamMap = new LinkedHashMap<String, String>();
        ParamMap.put("devCode", userName);
        BaseHttps.getInstance().postHttpRequest(context,
               GetCommonParam(ConstantsBase.IP+"/admin/private/code/app/orderList", ParamMap),
                new BaseHttpsCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        if (!TextUtils.isEmpty(result)) {
                            try {
                                Gson gson = new Gson();
                                OrderInfoBack mResult = gson.fromJson(result,OrderInfoBack.class);
                                if (200 == mResult.getStatus()) {
                                    callBack.onSuccess(mResult);
                                } else {
                                    callBack.onFailure(new ErrorMsg( mResult.getStatus()+"",getWrongBack(mResult.getMsg())));
                                }
                                return;
                            } catch (Exception e) {
                                e.printStackTrace();
                                callBack.onFailure(new ErrorMsg("-1",
                                        getWrongBack(e.getMessage())));
                            }
                        } else {
                            callBack.onFailure(new ErrorMsg("-1", "返回为空"));
                        }
                    }

                    @Override
                    public void onError(int code, String message) {
                        callBack.onFailure(new ErrorMsg("-1", getWrongBack(message)));
                    }

                    @Override
                    public void onFinished() {
                    }
                });
    }
    public void GetOrderDetail(final Context context, String userName,String orderNumber,
                         final CallBack<OrderDetailBack> callBack) {
        if (!NetWorkUtil.isNetworkAvailable(context)) {
            callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
            return;
        }
        Map<String, String> ParamMap = new LinkedHashMap<String, String>();
        ParamMap.put("devCode", userName);
        ParamMap.put("orderNumber", orderNumber);
        BaseHttps.getInstance().postHttpRequest(context,
                GetCommonParam(ConstantsBase.IP+"/admin/private/code/app/orderDetail", ParamMap),
                new BaseHttpsCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        if (!TextUtils.isEmpty(result)) {
                            try {
                                Gson gson = new Gson();
                                OrderDetailBack mResult = gson.fromJson(result,OrderDetailBack.class);
                                if (200 == mResult.getStatus()) {
                                    callBack.onSuccess(mResult);
                                } else {
                                    callBack.onFailure(new ErrorMsg( mResult.getStatus()+"",getWrongBack(mResult.getMsg())));
                                }
                                return;
                            } catch (Exception e) {
                                e.printStackTrace();
                                callBack.onFailure(new ErrorMsg("-1",
                                        getWrongBack(e.getMessage())));
                            }
                        } else {
                            callBack.onFailure(new ErrorMsg("-1", "返回为空"));
                        }
                    }

                    @Override
                    public void onError(int code, String message) {
                        callBack.onFailure(new ErrorMsg("-1", getWrongBack(message)));
                    }

                    @Override
                    public void onFinished() {
                    }
                });
    }
}
