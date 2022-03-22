package com.cshen.tiyu.net.https;

import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.main.NewsBean;
import com.cshen.tiyu.utils.NetWorkUtil;
import com.google.gson.Gson;

import android.content.Context;

/**
 * 资讯
 * @author Administrator
 *
 */
public class ServiceMessage extends ServiceABase {
	private static ServiceMessage instance;

	public static ServiceMessage getInstance() {
		if (null == instance) {
			instance = new ServiceMessage();
		}
		return instance;
	}
	/**
	 * 首页资讯
	 */

	public void PostGetHomeMessageData(Context context,int newsType,int pageIndex,int pageSize,boolean isShowNotice ,final CallBack<NewsBean> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return ;
		}
		String url=ConstantsBase.IP_NEWS+"/news/newsList!showNewsList.action?newstype="+newsType+"&start="+pageIndex+"&count="+pageSize;
		if (isShowNotice) {
			url=url+"&notice=1";
		}
		
		
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
	 * 首页资讯
	 */

	public void PostGetHomeMessageData(Context context,int newsType,int mType,int pageIndex,int pageSize,boolean isShowNotice ,final CallBack<NewsBean> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return ;
		}
		String url="";
		if (newsType==5) {//是马甲包
			url=ConstantsBase.IP_NEWS+"/news/newsList!showNewsList.action?newstype="+newsType+"&mType="+mType+"&start="+pageIndex+"&count="+pageSize;
		}else {
			url=ConstantsBase.IP_NEWS+"/news/newsList!showNewsList.action?newstype="+newsType+"&start="+pageIndex+"&count="+pageSize;
		}
		
		if (isShowNotice) {
			url=url+"&notice=1";
		}
		
		
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
	 * 首页资讯
	 */

	public void PostGetDrawing(Context context,int newsType,int pageIndex,int pageSize,boolean isShowNotice ,final CallBack<NewsBean> callBack) {
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			callBack.onFailure(new ErrorMsg("-1", "当前网络信号较差，请检查网络设置"));
			return ;
		}
		String url=ConstantsBase.IP_NEWS+"/news/newsList!showNewsList.action?newstype="+newsType+"&start="+pageIndex+"&count="+pageSize;
		if (isShowNotice) {
			url=url+"&notice=1";
		}


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
}
