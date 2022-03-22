package com.cshen.tiyu.net.https;

import org.xutils.common.Callback.CancelledException;
import org.xutils.common.Callback.CommonCallback;

public class BaseHttpsCallback<T> implements CommonCallback<T> {

	@Override
	public void onCancelled(CancelledException arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onError(Throwable arg0, boolean arg1) {
		// TODO Auto-generated method stub
		onError(0,arg0.getMessage());
	}

	@Override
	public void onFinished() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSuccess(T arg0) {
		// TODO Auto-generated method stub

	}
	/** 通讯成功，返回正常的数据时回调的方法 
	 *  
	 * @param result 返回信息 
	 */  

	/** 
	 * 请求失败、拦截到错误等，回调的方法 
	 *  
	 * @param mTResult 错误信息 根据自己的项目接口的返回去model 
	 * @param message 提示信息 
	 */  
	public  void onError(int code, String message){

	}
}
