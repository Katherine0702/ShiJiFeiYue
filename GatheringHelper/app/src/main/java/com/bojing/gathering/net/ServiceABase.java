package com.bojing.gathering.net;

import android.text.TextUtils;

import com.bojing.gathering.base.ConstantsBase;
import com.bojing.gathering.domain.ErrorMsg;
import com.bojing.gathering.util.MD5SignUtil;
import com.google.gson.Gson;

import org.xutils.http.RequestParams;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public class ServiceABase {
    private static String wAgent = "1";// 安卓平台
    public static final String key = "5bfa84fae862c604a63be1d24eef5861";//

    public interface CallBack<T> {
        void onSuccess(T t);

        void onFailure(ErrorMsg errorMessage);

    }

    public String getWrongBack(String wrongMessage) {
        if (!TextUtils.isEmpty(wrongMessage)) {
            if (wrongMessage.contains("timed out")) {
                wrongMessage = "链接超时";
            }
            if (wrongMessage.contains("refused")) {
                wrongMessage = "链接被拒绝，可能是服务器未开启";
            }
            if ("Internal Server Error".equals(wrongMessage)) {
                wrongMessage = "链接被拒绝，可能是未开启";
            }
            if ("Endofinputatcharacter0of".equals(wrongMessage.trim().replace(" ", ""))) {
                wrongMessage = "暂无数据";
            }
        }
        return wrongMessage;
    }
   /* public RequestParams GetCommonParam(String url, String wAction,
                                        Map<String, Object> ParamMap) {
        RequestParams ParamMapCom = new RequestParams(url);
        Gson gson = new Gson();
        String wParam = "";
        if (ParamMap != null) {
            wParam = gson.toJson(ParamMap);
        }
        String pwd = "";
        try {
            ParamMapCom.addBodyParameter("wAction", wAction);
            ParamMapCom.addBodyParameter("wParam", wParam);
            ParamMapCom.addBodyParameter("wAgent", wAgent);
            String param = wAction + wParam + wAgent + key;
            pwd = SecurityUtil.md5(param.getBytes("UTF-8")).toUpperCase()
                    .trim();
            ParamMapCom.addBodyParameter("wSign", pwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ParamMapCom;
    }*/
  /* public RequestParams GetCommonParam(String url, Map<String, String> ParamMap) {
       RequestParams ParamMapCom = new RequestParams(url);
       Gson gson = new Gson();
       String wParam = "";
       if (ParamMap != null) {
           wParam = gson.toJson(ParamMap);
       }
       try {
           ParamMapCom.addBodyParameter("wParam", wParam);
       } catch (Exception e) {
           e.printStackTrace();
       }
       return ParamMapCom;
   }*/
    public RequestParams GetCommonParam(String url, Map<String, String> wParam) {
        wParam.put("signMode", "MD5");
        wParam.put("timestamp",  System.currentTimeMillis()+"");
        RequestParams ParamMapCom = new RequestParams(url);
        for (String key : wParam.keySet()) {
            ParamMapCom.addBodyParameter(key, wParam.get(key));
        }
        String sign = "";
        try {
            sign = MD5SignUtil.buildRequestMysign(wParam,key);
            ParamMapCom.addBodyParameter("sign", sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ParamMapCom;
    }


    public RequestParams GetCommonParamPic(String url, ArrayList<File> fileImages, Map<String, String> wParam) {
        wParam.put("signMode", "MD5");
        wParam.put("timestamp",  System.currentTimeMillis()+"");
        RequestParams ParamMapCom = new RequestParams(url);
        for (String key : wParam.keySet()) {
            ParamMapCom.addBodyParameter(key, wParam.get(key));
        }
        String sign = "";
        try {
            sign = MD5SignUtil.buildRequestMysign(wParam,key);
            ParamMapCom.addBodyParameter("sign", sign);
            for(File file:fileImages){
                ParamMapCom.addBodyParameter("pics", file, "multipart/form-data");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ParamMapCom;
    }
}
