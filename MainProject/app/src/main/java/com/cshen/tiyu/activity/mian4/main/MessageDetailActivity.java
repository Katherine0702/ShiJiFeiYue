package com.cshen.tiyu.activity.mian4.main;

import com.cshen.tiyu.R;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.main.NewsBean.NewslistBean;
import com.cshen.tiyu.utils.NetUtils;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.widget.AlertDialog;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * 资讯详情
 * @author Administrator
 *
 */
public class MessageDetailActivity  extends BaseActivity{
	 private Context mContext;
	 private TopViewLeft tv_head;
	 private WebView mWebView;
	 private ProgressBar mProgressBar;
	 private boolean  hasLogin = false;
	 private String URL= "";
	 private String id="";
	 private int newsType;

	 private boolean isMaJia=false;//马甲包
	 private boolean isMaJiaBanner=false;
	 private NewslistBean dataBean;
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		mContext=this;
		setContentView(R.layout.activity_message_detail);
		isMaJia=PreferenceUtil.getBoolean(mContext, "isMaJia");
		isMaJiaBanner=getIntent().getBooleanExtra("isBanner", false);
		dataBean=(NewslistBean) getIntent().getSerializableExtra("dataBean");
		
		hasLogin = PreferenceUtil.getBoolean(mContext, "hasLogin");
		id=getIntent().getStringExtra("id");
		newsType=getIntent().getIntExtra("newsType",1);
		if (isMaJia&&hasLogin&&dataBean!=null&&!isMaJiaBanner) {//如果是马甲包且不是banner的，存入浏览历史
			MyDbUtils.saveHistorySigleData(dataBean);
		}
		if (isMaJiaBanner) {
			URL=getIntent().getStringExtra("url");
		}else if (isMaJia) {
			id=getIntent().getStringExtra("id");	
			URL=ConstantsBase.IP_NEWS+"/news_detail.html?id="+id;
		}else {	
			id=getIntent().getStringExtra("id");	
			URL=ConstantsBase.IP_NEWS+"/news_detail.html?id="+id;
		}
		
		initViews();
		setWebView();
	}
	
	


	private void initViews() {
		tv_head = (TopViewLeft) findViewById(R.id.tv_head);
        if (newsType==1) {
			tv_head.setTitle("公告详情");
		}else {
			tv_head.setTitle("资讯详情");
		}
		tv_head.setResourceVisiable(true, false, false);
		tv_head.setTopClickItemListener(new TopClickItemListener() {

			@Override
			public void clickLoginView(View view) {
				// TODO 自动生成的方法存根

			}

			@Override
			public void clickContactView(View view) {
				// TODO 自动生成的方法存根

			}

			@Override
			public void clickBackImage(View view) {
				// TODO 自动生成的方法存根
				finish();
			}
		});

		mWebView=(WebView) findViewById(R.id.webView_message_detail);
		mProgressBar=(ProgressBar) findViewById(R.id.progressBar);
		
	}


	
	@SuppressWarnings("deprecation")
	@SuppressLint("SetJavaScriptEnabled") 
	private void setWebView() {
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT); // 设置
		// 缓存模式
		mWebView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
		mWebView.getSettings().setAllowFileAccess(true);
		// 开启 database storage API 功能 //启用数据库
		mWebView.getSettings().setDatabaseEnabled(true);
		String cacheDirPath = mContext.getFilesDir().getAbsolutePath() + "/webviewCache";
		// 设置数据库缓存路径
		mWebView.getSettings().setDatabasePath(cacheDirPath);
		// 设置 Application Caches 缓存目录
		mWebView.getSettings().setAppCachePath(cacheDirPath);
		// 开启 Application Caches 功能
		mWebView.getSettings().setAppCacheEnabled(true);
		String dir = mContext.getApplicationContext()
				.getDir("database", Context.MODE_PRIVATE).getPath();
		// 启用地理定位
		mWebView.getSettings().setGeolocationEnabled(true);
		// 设置定位的数据库路径
		mWebView.getSettings().setGeolocationDatabasePath(dir);
		// 最重要的方法，一定要设置，这就是出不来的主要原因
		mWebView.getSettings().setDomStorageEnabled(true);

		mWebView.getSettings().setJavaScriptEnabled(true);
//		mWebView.addJavascriptInterface(new ReadHtmlVersion(), "back");
		// 解决缓存问题
		if (!hasLogin) {
			mWebView.clearCache(true);
			mWebView.clearHistory();
			CookieSyncManager.createInstance(mContext);
			CookieSyncManager.getInstance().startSync();
			CookieManager.getInstance().removeSessionCookie();
		}

		mWebView.loadUrl(URL);
		//设置进度条
		mWebView.setWebChromeClient(new WebChromeClient(){
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
		             mProgressBar.setVisibility(View.GONE);
		         } else {
		             if (mProgressBar.getVisibility() == View.GONE)
		                 mProgressBar.setVisibility(View.VISIBLE);
		             mProgressBar.setProgress(newProgress);
		         }
				super.onProgressChanged(view, newProgress);
			}
		});
		mWebView.setWebViewClient(new WebViewClient() {
			
			@Override
			public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
				// TODO Auto-generated method stub


				return super.shouldOverrideKeyEvent(view, event);
			}
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				// TODO 自动生成的方法存根
				super.onReceivedError(view, errorCode, description, failingUrl);

				boolean isConnected = NetUtils.isNetworkConnected(mContext);
				if (!isConnected) {
					new AlertDialog(mContext).builder().setTitle("提示").setMsg("检测到无网络连接")
					.setPositiveButton("设置", new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = null;
							// 判断手机系统的版本 即API大于10 就是3.0或以上版本
							intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
							mContext.startActivity(intent);
						}
					}).setNegativeButton("取消", new OnClickListener() {
						@Override
						public void onClick(View v) {

						}
					}).show();

				}
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);

			}
			@Override  
			public void onPageFinished(WebView view, String url) {  
				super.onPageFinished(view, url);  
				//在加载完成之后对网页中内容进行隐藏,这里我们隐藏了登录的按钮  
				if(URL.equals(url)){
					tv_head.setResourceVisiable(false, false, false);
				}else{
					tv_head.setResourceVisiable(false, false, false);
				}
			}  

		});
		
		
	}


}
