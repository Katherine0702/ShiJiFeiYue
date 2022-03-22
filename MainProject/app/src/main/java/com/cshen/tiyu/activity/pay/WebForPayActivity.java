package com.cshen.tiyu.activity.pay;

import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.cshen.tiyu.MainActivity;
import com.cshen.tiyu.R;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.login.User;
import com.cshen.tiyu.domain.login.UserInfo;
import com.cshen.tiyu.domain.login.Version;
import com.cshen.tiyu.net.https.ServiceUser;
import com.cshen.tiyu.utils.NetUtils;
import com.cshen.tiyu.utils.NetWorkUtil;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.AlertDialog;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;


import de.greenrobot.event.EventBus;

public class WebForPayActivity extends BaseActivity {
	public static final int WX_ZF = 2;
	public static final int ZFBBACK = 1;
	private WebForPayActivity mContext;
	private TopViewLeft tv_head;
	private Dialog mDialog;
	private LinearLayout errorPage;
	// 无网络状态显示头部 头部的返回做特殊处理
	boolean rightPage = true;
	String URL = null;
	WebView mWebView = null;
	//int paywayInt = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lottery_type);
		mContext = this;
		tv_head = (TopViewLeft) findViewById(R.id.tv_head);
		errorPage = (LinearLayout) findViewById(R.id.error_page);
		tv_head.setResourceVisiable(true, false, false);
		tv_head.setTopClickItemListener(new TopClickItemListener() {

			@Override
			public void clickLoginView(View view) {
				// TODO Auto-generated method stub
			}

			@Override
			public void clickContactView(View view) {
				// TODO Auto-generated method stub
			}

			@Override
			public void clickBackImage(View view) {
				// TODO Auto-generated method stu
				if (MyDbUtils.getCurrentUser() != null) {
					ServiceUser.getInstance().GetUserInfo(mContext);// 余额跟新
				}
				Intent intentClose = new Intent();
				setResult(RESULT_OK, intentClose);
				finish();
			}
		});
		// TODO 自动生成的方法存根
		initView();

	}

	private void initView() {

		mWebView = (WebView) findViewById(R.id.wv_latest_lottery);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT); // 设置
		// 缓存模式
		mWebView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
		mWebView.getSettings().setAllowFileAccess(true);
		// 开启 database storage API 功能 //启用数据库
		mWebView.getSettings().setDatabaseEnabled(true);
		String cacheDirPath = getFilesDir().getAbsolutePath() + "/webviewCache";
		// 设置数据库缓存路径
		mWebView.getSettings().setDatabasePath(cacheDirPath);
		// 设置 Application Caches 缓存目录
		mWebView.getSettings().setAppCachePath(cacheDirPath);
		// 开启 Application Caches 功能
		mWebView.getSettings().setAppCacheEnabled(true);
		String dir = this.getApplicationContext()
				.getDir("database", Context.MODE_PRIVATE).getPath();
		// 启用地理定位
		mWebView.getSettings().setGeolocationEnabled(true);
		// 设置定位的数据库路径
		mWebView.getSettings().setGeolocationDatabasePath(dir);
		// 最重要的方法，一定要设置，这就是出不来的主要原因
		mWebView.getSettings().setDomStorageEnabled(true);

		boolean hasHtmlTitleChanged = PreferenceUtil.getBoolean(
				mContext, "hasHtmlTitleChanged");

		if (hasHtmlTitleChanged) {
			mWebView.clearCache(true);
			mWebView.clearHistory();
			CookieSyncManager.createInstance(this);
			CookieSyncManager.getInstance().startSync();
			CookieManager.getInstance().removeSessionCookie();

		}
		showDialog("");

		URL = getIntent().getStringExtra("url");

		String flag=getIntent().getStringExtra("flag");
		if (!TextUtils.isEmpty(flag) &&  "post".equals(flag)) {
			String httpbody=getIntent().getStringExtra("httpbody");
			try {
				mWebView.postUrl(URL, httpbody.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}else{
			mWebView.loadUrl(URL);
		}

		mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				// TODO 自动生成的方法存根
				super.onReceivedError(view, errorCode, description, failingUrl);
				tv_head.setVisibility(View.VISIBLE);
				errorPage.setVisibility(View.VISIBLE);
				mWebView.setVisibility(View.GONE);

				// 提示框
				boolean isConnected = NetUtils.isNetworkConnected(mContext);
				if (!isConnected) {
					new AlertDialog(mContext).builder()
					.setTitle("提示").setMsg("检测到无网络连接")
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
			public void onPageStarted(WebView view,final String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				if (!url.contains(ConstantsBase.IP)&& !url.contains(ConstantsBase.IPQT)) {					
					tv_head.setVisibility(View.VISIBLE);
				} else {
					tv_head.setVisibility(View.GONE);
				}
			}
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				//盛付通
				if ("https://api.shengpay.com/html5-gateway/express.htm?page=mobile#express/success|0".equals(url)) {
					Intent intentClose = new Intent();
					setResult(RESULT_OK, intentClose);
					Timer timer=new Timer();
					ServiceUser.getInstance().GetUserInfo(mContext);// 余额跟新

					TimerTask task=new TimerTask(){
						public void run(){
							mContext.finish();
						}
					};
					try{
						timer.schedule(task, 5000);
					}catch(Exception e){
						e.printStackTrace();
					}catch(Error er){
						er.printStackTrace();
					}
				}

			}
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, final String url) {
				//支付宝H5
				if(url.contains("alipays://platformapi/startApp")||url.contains("alipays://platformapi/startapp")){
					try {
						Uri uri = Uri.parse(url);
						Intent intent = new Intent(Intent.ACTION_VIEW, uri);
						startActivityForResult(intent, ZFBBACK);
					} catch (Exception e) {
						ToastUtils.showShortCenter(mContext,"请先安装支付宝应用！");
					}
					return true;
				}
				//兴业QQH5
				if (url.contains("mqqapi://forward/url?url_prefix")||url.contains("myun.tenpay.com/mqq/pay")) {
					try {
						Uri uri = Uri.parse(url);
						Intent intent = new Intent(Intent.ACTION_VIEW, uri);
						startActivityForResult(intent, ZFBBACK);

					} catch (Exception e) {
						ToastUtils.showShortCenter(mContext,"请先安装QQ！");
					}
					return true;
				}
				//兴业微信H5
				if (url.startsWith("weixin://wap/pay?")||url.contains("weixin://dl/business/?ticket")) {
					try{
						Intent intent = new Intent();
						intent.setAction(Intent.ACTION_VIEW);
						intent.setData(Uri.parse(url));
						startActivityForResult(intent, ZFBBACK);
					}catch (ActivityNotFoundException e){
						ToastUtils.showShortCenter(mContext, "请安装微信最新版！");
					}

					return true;
				}
				return false;
			}
		});
		mWebView.setWebChromeClient(new WebChromeClient() {
			// 动画加载
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
					dismissDialog();
				}
				super.onProgressChanged(view, newProgress);
			}
		});
	}
	public void dismissDialog() {
		if (!this.isFinishing() && mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ZFBBACK) {
			showDialogClose();
		}
	}

	public void showDialogClose() {
		new AlertDialog(mContext).builder()
		.setTitle("支付提示").setMsg("是否支付完成")
		.setPositiveButton("完成", new OnClickListener() {
			@Override
			public void onClick(View v) {
				ServiceUser.getInstance().GetUserInfo(mContext);// 余额跟新	
				Intent intentClose = new Intent();
				setResult(RESULT_OK, intentClose);
				mContext.finish(); 
			}
		}).setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intentClose = new Intent();
				setResult(RESULT_OK, intentClose);
				mContext.finish(); 
			}
		}).show();

	}
	//正在加载对话框
	public void showDialog(String msg) {
		if (!this.isFinishing()) {
			if (mDialog == null)
				mDialog = makeDialog(this, msg);
			if (!mDialog.isShowing())
				mDialog.show();
		}
	}
	public Dialog makeDialog(final Context context, String msg) {
		return makeDialog(context, msg, true, null);
	}
	public Dialog makeDialog(final Context context, String msg,
			boolean cancelable, DialogInterface.OnCancelListener listener) {
		final Dialog loadingDialog = ProgressDialog.show(context, "",
				TextUtils.isEmpty(msg) ? "正在加载，请稍候..." : msg);
		loadingDialog.setCancelable(cancelable);
		loadingDialog
		.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialogInterface) {
				try {
					if (loadingDialog != null
							&& loadingDialog.isShowing()
							&& !((Activity) context).isFinishing()) {
						loadingDialog.dismiss();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		loadingDialog
		.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialogInterface) {
				try {
					if (loadingDialog != null
							&& loadingDialog.isShowing()
							&& !((Activity) context).isFinishing()) {
						loadingDialog.dismiss();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		if (listener != null)
			loadingDialog.setOnCancelListener(listener);
		return loadingDialog;
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (MyDbUtils.getCurrentUser() != null) {
				ServiceUser.getInstance().GetUserInfo(mContext);// 余额跟新
			}
			Intent intentClose = new Intent();
			setResult(RESULT_OK, intentClose);
			mContext.finish();

		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (!TextUtils.isEmpty(PreferenceUtil.getString(mContext, "rechargeOrderId"))) {
			EventBus.getDefault().post("checkMoney"+PreferenceUtil.getString(mContext, "rechargeOrderId"));
		}
	}
}
