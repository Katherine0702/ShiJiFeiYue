package com.bojing.gathering.net;

import android.content.Context;
import android.text.TextUtils;

import com.bojing.gathering.base.ConstantsBase;
import com.bojing.gathering.domain.Back;
import com.bojing.gathering.domain.BackCommomString;
import com.bojing.gathering.domain.ErrorMsg;
import com.bojing.gathering.domain.InfoDetail;
import com.bojing.gathering.domain.MoneyListBack;
import com.bojing.gathering.domain.MoneyOtherListBack;
import com.bojing.gathering.domain.MoneyOtherPicListBack;
import com.bojing.gathering.util.NetWorkUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by admin on 2018/10/12.
 */

public class ServiceMaku extends ServiceABase {
    private static ServiceMaku instance;

    public static ServiceMaku getInstance() {
        if (null == instance) {
            instance = new ServiceMaku();
        }
        return instance;
    }
    public void GetMoneyList(Context context,String  devCode,
                        final CallBack<MoneyListBack> callBack) {
        if (!NetWorkUtil.isNetworkAvailable(context)) {
            callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
            return;
        }
        Map<String, String> ParamMap = new LinkedHashMap<String, String>();
        ParamMap.put("devCode", devCode);
        BaseHttps.getInstance().postHttpRequest(context,
                GetCommonParam(ConstantsBase.IP + "/admin/private/code/app/getFixedMoneyList", ParamMap),
                new BaseHttpsCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        if (!TextUtils.isEmpty(result)) {
                            Gson gson = new Gson();
                            MoneyListBack back = gson.fromJson(result, MoneyListBack.class);
                            if (back.getStatus() == 200) {
                                callBack.onSuccess(back);
                            } else {
                                callBack.onFailure(new ErrorMsg("-1", "11111," + getWrongBack(back.getMsg())));
                            }
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
    public void GetMoneyOtherList(Context context,String  devCode,String price,
                             final CallBack<MoneyOtherListBack> callBack) {
        if (!NetWorkUtil.isNetworkAvailable(context)) {
            callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
            return;
        }
        Map<String, String> ParamMap = new LinkedHashMap<String, String>();
        ParamMap.put("devCode", devCode);
        ParamMap.put("price", price);
        BaseHttps.getInstance().postHttpRequest(context,
                GetCommonParam(ConstantsBase.IP + "/admin/private/code/app/getDynamicMoneyList", ParamMap),
                new BaseHttpsCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        if (!TextUtils.isEmpty(result)) {
                            Gson gson = new Gson();
                            MoneyOtherListBack back = gson.fromJson(result, MoneyOtherListBack.class);
                            if (back.getStatus() == 200) {
                                callBack.onSuccess(back);
                            } else {
                                callBack.onFailure(new ErrorMsg("-1", "11111," + getWrongBack(back.getMsg())));
                            }
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
    public void GetMoneyOtherPicList(Context context,String  devCode,String price,
                                  final CallBack<MoneyOtherPicListBack> callBack) {
        if (!NetWorkUtil.isNetworkAvailable(context)) {
            callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
            return;
        }
        Map<String, String> ParamMap = new LinkedHashMap<String, String>();
        ParamMap.put("devCode", devCode);
        ParamMap.put("price", price);
        BaseHttps.getInstance().postHttpRequest(context,
                GetCommonParam(ConstantsBase.IP + "/admin/private/code/app/getTwoDimensionalPicList", ParamMap),
                new BaseHttpsCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        if (!TextUtils.isEmpty(result)) {
                            Gson gson = new Gson();
                            MoneyOtherPicListBack back = gson.fromJson(result, MoneyOtherPicListBack.class);
                            if (back.getStatus() == 200) {
                                callBack.onSuccess(back);
                            } else {
                                callBack.onFailure(new ErrorMsg("-1", "11111," + getWrongBack(back.getMsg())));
                            }
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
    public void GetNewPic(Context context,String  devCode,String price,
                                     final CallBack<BackCommomString> callBack) {
        if (!NetWorkUtil.isNetworkAvailable(context)) {
            callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
            return;
        }
        Map<String, String> ParamMap = new LinkedHashMap<String, String>();
        ParamMap.put("devCode", devCode);
        ParamMap.put("price", price);
        BaseHttps.getInstance().postHttpRequest(context,
                GetCommonParam(ConstantsBase.IP + "/admin/private/code/app/createNewMoney", ParamMap),
                new BaseHttpsCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        if (!TextUtils.isEmpty(result)) {
                            Gson gson = new Gson();
                            BackCommomString back = gson.fromJson(result, BackCommomString.class);
                            if (back.getStatus() == 200) {
                                callBack.onSuccess(back);
                            } else {
                                callBack.onFailure(new ErrorMsg("-1", "11111," + getWrongBack(back.getMsg())));
                            }
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
    /**
     * 上传信息
     */
    public void PostPic(Context context,String  devCode,String  price,ArrayList<File> fileImages,
                        final CallBack<BackCommomString> callBack) {
        if (!NetWorkUtil.isNetworkAvailable(context)) {
            callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
            return;
        }
        Map<String, String> ParamMap = new LinkedHashMap<String, String>();
        ParamMap.put("devCode", devCode);
        ParamMap.put("price", price);
        BaseHttps.getInstance().postHttpRequest(context,
                GetCommonParamPic(ConstantsBase.IP + "/admin/private/code/app/upLoadTwoDimensionalPic",
                        fileImages,ParamMap),
                new BaseHttpsCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        if (!TextUtils.isEmpty(result)) {
                            Gson gson = new Gson();
                            BackCommomString back = gson.fromJson(result, BackCommomString.class);
                            if (back.getStatus() == 200) {
                                callBack.onSuccess(back);
                            } else {
                                callBack.onFailure(new ErrorMsg("-1", "11111," + getWrongBack(back.getMsg())));
                            }
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
