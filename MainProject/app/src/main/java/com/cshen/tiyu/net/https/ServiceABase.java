package com.cshen.tiyu.net.https;

import android.text.TextUtils;

import java.io.File;
import java.util.Map;

import org.w3c.dom.Text;
import org.xutils.http.RequestParams;

import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.PicUpLoad;
import com.cshen.tiyu.utils.SecurityUtil;
import com.google.gson.Gson;

public class ServiceABase {
	public static final String IP_TICKET = ConstantsBase.IP + "/ticket.jsp";
	public static final String IP_MOBILE = ConstantsBase.IP+"/mobile.jsp";
	public static final String IP_THESUN = ConstantsBase.IP+"/thesunimg.jsp";//晒单上传图片
	
	public static final String HOMEIP_APP = ConstantsBase.HOMEIP + "/app.jsp";// app首页配置信息接口
	public static final String HOMEIP_APP_SD = ConstantsBase.IP_NEWS + "/app.jsp";// 晒单模块
	private static String wAgent = "1";// 安卓平台
	public static final String key = "E10ADC3949BA59ABBE56E057F20F883E";//
	public interface CallBack<T> {
		void onSuccess(T t);

		void onFailure(ErrorMsg errorMessage);

	}
	public String getWrongBack(String wrongMessage){
		if(!TextUtils.isEmpty(wrongMessage)){
			if(wrongMessage.contains("timed out")){
				wrongMessage = "链接超时";
			}
			if(wrongMessage.contains("refused")){
				wrongMessage = "链接被拒绝，可能是服务器未开启";
			}
			if("Internal Server Error".equals(wrongMessage)){
				wrongMessage = "链接被拒绝，可能是未开启";
			}
			if("Endofinputatcharacter0of".equals(wrongMessage.trim().replace(" ", ""))){
				wrongMessage = "暂无数据";
			}
		}
		return wrongMessage;
	}
	public RequestParams GetCommonParam(String url,String wAction,
			Map<String, Object> ParamMap) {
		RequestParams ParamMapCom = new RequestParams(url);
		Gson gson = new Gson();
		String  wParam  = "";
		if(ParamMap != null){
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
	}		
	public RequestParams GetCommonParamNoAction(String url) {
		RequestParams ParamMapCom = new RequestParams(url);
		return ParamMapCom;
	}

	public RequestParams GetCommonParamPic(String url,File imageFile,String wAction,
			Map<String, Object> ParamMap,String UserId) {
		RequestParams ParamMapCom = new RequestParams(url);
		Gson gson = new Gson();
		String wParam = gson.toJson(ParamMap);
		try {
			ParamMapCom.setMultipart(true);
            PicUpLoad picupload = new PicUpLoad();
            picupload.setwAction(wAction);
            picupload.setwParam(wParam);
            picupload.setwAgent("1");
            picupload.setwSign(picupload.getPwd());
            picupload.setReqUserId(UserId);
		    Gson aGson = new Gson();
		    String json2 = aGson.toJson(picupload);
			ParamMapCom.addBodyParameter("wJson",json2);
			System.out.println("file+json2"+json2.toString());
			ParamMapCom.addBodyParameter("picFile",imageFile,"multipart/form-data");			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ParamMapCom;
	}
}
