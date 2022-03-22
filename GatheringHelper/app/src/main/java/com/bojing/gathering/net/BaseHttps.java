package com.bojing.gathering.net;


import android.content.Context;
import android.util.Log;

import com.bojing.gathering.base.ConstantsBase;
import com.bojing.gathering.util.NetWorkUtil;

import org.xutils.common.Callback.Cancelable;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.ex.HttpException;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * xUtils3 应用与网络通讯管理类 
 **/  
public class  BaseHttps {
  
    /** Https 证书验证对象 */   
    private static final String TAG = "BaseHttps";  
	private static BaseHttps instance;
	public static BaseHttps getInstance() {
		if (null == instance) {
			instance = new BaseHttps();
		}
		return instance;
	} 
      
    /** 
     * http get请求 
     *  
     * @param params 请求参数 get请求使用 addQueryStringParameter方法添加参数 
     * @param mCallback 回调对象 
     * @return 网络请求的Cancelable 可以中断请求 
     */  
    public  Cancelable getHttpRequest(Context context,RequestParams params, BaseHttpsCallback mCallback) {  
        if(ConstantsBase.httpOrhttps == ConstantsBase.https){
            return sendHttpRequest(context,HttpMethod.GET, params, mCallback);  
    	}else{
    		return Get(params, mCallback);  
    	}
    }  
    
    public  Cancelable getHttpRequestTemp(Context context,RequestParams params, BaseHttpsCallback mCallback) {  
        
    		return Get(params, mCallback);  
    	
    }  
      
    /** 
     * http post请求 
     *  
     * @param params 请求参数 post请求使用 addBodyParameter方法添加参数 
     * @param mCallback 回调对象 
     * @return 网络请求的Cancelable 可以中断请求 
     */  
    public synchronized Cancelable postHttpRequest(Context context,RequestParams params, BaseHttpsCallback mCallback) {  
    	if(ConstantsBase.httpOrhttps == ConstantsBase.https){
            return sendHttpRequest(context,HttpMethod.POST, params, mCallback);  
    	}else{
    		return Post(params, mCallback);  
    	}
    }  
    /**
     * 发送post请求
     * @param <T>
     */
    public static <T> Cancelable  Post(RequestParams map,CommonCallback<T> callback){
       Cancelable cancelable = x.http().post(map, callback);
        return cancelable;
    }
    public static <T> Cancelable Get(RequestParams params,CommonCallback<T> callback){
        Cancelable cancelable = x.http().get(params, callback);
        return cancelable;
    }
      
    /** 
     * 发送请求 Cancelable 
     *  
     * @param method 请求方式(GET POST) 
     * @param params 请求参数 
     * @param mCallback 回调对象 
     * @return 网络请求的Cancelable 可以中断请求 
     */  
    public synchronized Cancelable sendHttpRequest(Context context,HttpMethod method, RequestParams params, final BaseHttpsCallback mCallback){  
    	if (!NetWorkUtil.isNetworkAvailable(context)) {
    		mCallback.onFinished();  
            mCallback.onError(-1, "网络连接失败，请重试");  
            return null;  
        } 
    	 /** 判断https证书是否成功验证 */  
        
        if (params == null) {  
            params = new RequestParams();  
        }  
        params.setCacheMaxAge(1000*0); //为请求添加缓存时间  
        params.setConnectTimeout(60 * 1000); //超时时间60s  
        params.setSslSocketFactory(HTTPS(context)); //绑定SSL证书(https请求)  
        
        /** Cancelable cancelable = x.http().get(params, Callback); */  
        return  x.http().request(method, params , new CommonCallback<String>() {  
  
            @Override //取消  
            public synchronized void onCancelled(CancelledException msg) {  
                mCallback.onError(-2, msg.getMessage());  
                mCallback.onFinished();  
            }  
  
            @Override //错误  
            public synchronized void onError(Throwable arg0, boolean arg1) {  
                if (arg0 instanceof HttpException) { // 网络错误  
                	
                }   
                mCallback.onError(-2, arg0.getMessage());  
                mCallback.onFinished();  
            }  
  
            @Override //成功  
            public synchronized void onSuccess(String result) {  
                if (result == null) {  
                    return;  
                }  
                Log.i(TAG, "==> RequestCallBack.onSuccess()");  
                Log.e(TAG, "==> RequestCallBack.onSuccess():"+result);  
                mCallback.onSuccess(result);  
            }  
              
            @Override //完成  
            public synchronized void onFinished() {  
            }  
        });  
    }  
      
    /** 
     * 中断网络请求 
     *  
     * @param mCancelable Cancelable 
     */  
    public <T> void interrupt(Cancelable mCancelable) {  
        if (mCancelable != null && !mCancelable.isCancelled()) {  
            mCancelable.cancel();  
        }  
    }  

	/** 
	 * 获取Https的证书 
	 * @param context Activity（fragment）的上下文 
	 * @return SSL的上下文对象 
	 */  
    public SSLSocketFactory HTTPS(Context context){	
    	SSLContext s_sSLContext;
		InputStream in = null;   
		CertificateFactory cf = null; 
		Certificate ca = null;  
		
		KeyStore keystore = null;  
		TrustManagerFactory tmf = null;  
		try {  

			cf = CertificateFactory.getInstance("X.509");  
			in = context.getAssets().open("cainiu.crt");//这里导入SSL证书文件  
			try {  
				//读取证书  
				ca = cf.generateCertificate(in); 
			} catch (Exception e) {  
				e.printStackTrace(); 
			}finally {  
				in.close();  
			}  
			//创建一个证书库，并将证书导入证书库  
			keystore = KeyStore.getInstance(KeyStore.getDefaultType());  
			keystore.load(null); //双向验证时使用  
			keystore.setCertificateEntry("cainiu", ca);  

			// 实例化信任库  
			String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();  
			tmf = TrustManagerFactory.getInstance(tmfAlgorithm);  
			tmf.init(keystore); // 初始化信任库  

			s_sSLContext = SSLContext.getInstance("TLS");  
			s_sSLContext.init(null, tmf.getTrustManagers(), new SecureRandom());  
			return s_sSLContext.getSocketFactory();

		} catch (Exception e) {  
			e.printStackTrace();  
		}  
		return null;  
	}   
}  