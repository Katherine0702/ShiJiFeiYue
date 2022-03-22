package com.bojing.gathering.net;

import android.content.Context;
import android.text.TextUtils;

import com.bojing.gathering.base.ConstantsBase;
import com.bojing.gathering.domain.Back;
import com.bojing.gathering.domain.ErrorMsg;
import com.bojing.gathering.domain.InfoDetail;
import com.bojing.gathering.util.NetWorkUtil;
import com.google.gson.Gson;

import org.xutils.http.RequestParams;

import java.util.LinkedHashMap;
import java.util.Map;

public class ServiceMain extends ServiceABase {
    private static ServiceMain instance;

    public static ServiceMain getInstance() {
        if (null == instance) {
            instance = new ServiceMain();
        }
        return instance;
    }

    /**
     * 上传信息
     */
    public void PostInfo(Context context, InfoDetail detail,
                         final CallBack<Back> callBack) {
        if (!NetWorkUtil.isNetworkAvailable(context)) {
            callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
            return;
        }
        Map<String, String> ParamMap = new LinkedHashMap<String, String>();
        if (!TextUtils.isEmpty(detail.getBuyerName())) {
            ParamMap.put("buyerName", detail.getBuyerName());
        }
        if (!TextUtils.isEmpty(detail.getCreateTime())) {
            ParamMap.put("createTime", detail.getCreateTime());
        }
        ParamMap.put("paymentId", detail.getPaymentId() + "");
        ParamMap.put("providerAccount", detail.getProviderAccount());
        ParamMap.put("providerId", detail.getProviderId());
        ParamMap.put("totalFee", detail.getTotalFee());
        ParamMap.put("txNoStatus", detail.getTxNoStatus());
        BaseHttps.getInstance().postHttpRequest(context,
                GetCommonParam(ConstantsBase.IP + "/admin/private/code/receiveMoney", ParamMap),
                new BaseHttpsCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        if (!TextUtils.isEmpty(result)) {
                            try {
                                // 获取某些信息
                                Gson gson = new Gson();
                                Back back = gson.fromJson(result, Back.class);
                                if (back.getStatus() == 200) {
                                    callBack.onSuccess(back);
                                } else {
                                    callBack.onFailure(new ErrorMsg("-1", getWrongBack(back.getMsg())));
                                }
                                return;
                            } catch (Exception e) {
                                e.printStackTrace();
                                callBack.onFailure(new ErrorMsg("-1", getWrongBack(e.getMessage())));
                            }
                        }
                    }

                    @Override
                    public void onError(int code, String message) {
                        callBack.onFailure(new ErrorMsg("-1", code + "," + message));
                    }

                    @Override
                    public void onFinished() {
                    }
                });
    }
}
