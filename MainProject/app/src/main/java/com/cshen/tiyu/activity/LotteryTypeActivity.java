package com.cshen.tiyu.activity;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

import org.json.JSONException;

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
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cshen.tiyu.R;
import com.cshen.tiyu.MainActivity;
import com.cshen.tiyu.activity.login.LoginActivity;
import com.cshen.tiyu.activity.login.RegisterByMoibleActivity;
import com.cshen.tiyu.activity.login.RegisterSuccessActivity;
import com.cshen.tiyu.activity.lottery.ball.basketball.BasketBallMainActivity;
import com.cshen.tiyu.activity.lottery.ball.football.FootballMainActivity;
import com.cshen.tiyu.activity.mian4.personcenter.setting.binding.NameAuthActivity;
import com.cshen.tiyu.activity.pay.PayMoneyActivity;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.login.User;
import com.cshen.tiyu.domain.login.UserInfo;
import com.cshen.tiyu.domain.login.Version;
import com.cshen.tiyu.utils.Base64;
import com.cshen.tiyu.utils.NetUtils;
import com.cshen.tiyu.utils.NetWorkUtil;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.SignatureData;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.AlertDialog;
import com.cshen.tiyu.widget.NewToast;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;


public class LotteryTypeActivity extends BaseActivity {
	public static final int WX_ZF = 2;
	public static final int LOGIN = 3;//登录
	public static final int SMRZ = 4;//实名认证
	public static final int CZ = 5;//充值
	private Context mContext;
	private User user;
	private UserInfo userInfo;
	private Version version;
	private String caipiaoVersion0;
	private boolean hasLogin, isExitWay;
	private String continueURLParam;
	private TopViewLeft tv_head;
	private String intentWay;
	private Dialog mDialog;
	private LinearLayout errorPage;
	// 无网络状态显示头部 头部的返回做特殊处理
	boolean rightPage = true;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			User user = MyDbUtils.getCurrentUser();
			boolean hasLogin = PreferenceUtil.getBoolean(mContext, "hasLogin");
			switch (msg.what) {
			case 0:
				String versionNow = (String) msg.obj;
				if (!TextUtils.isEmpty(version.getVersion())) {
					if (!version.getVersion().equals(versionNow)) {
						mWebView.clearCache(true);
						mWebView.clearHistory();
						Version newVersion = new Version();
						newVersion.setUserid((hasLogin && (user.getId() != null)) ?
								user.getId() + "" : "0");
						newVersion.setVersion(versionNow);
						MyDbUtils.saveCurrentVersion(newVersion);
					}
				} else {
					Version newVersion = new Version();
					newVersion.setUserid((hasLogin && (user.getId() != null)) ?
							user.getId() + "" : "0");
					newVersion.setVersion(versionNow);
					MyDbUtils.saveCurrentVersion(newVersion);
				}
				break;
			}
		}
	};
	String URL = null;
	String orderId="";
	WebView mWebView = null;
	String flag;
	private AlertDialog alertDialog;// 投注成功弹出框
	private String activityName="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lottery_type);
		mContext = this;
		intentWay = getIntent().getStringExtra("intentWay");
		activityName=getIntent().getStringExtra("activityName");
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
				// TODO Auto-generated method stub
				// 如果是活动页面传过来的还要特殊处理
				// Intent(BankNameActivity.this,SafeSettingActivity.class));
				if ((!TextUtils.isEmpty(intentWay))
						&& "exercise".equals(intentWay)) {
					startActivity(new Intent(LotteryTypeActivity.this,
							MainActivity.class));
				}
				finish();
			}
		});
		// TODO 自动生成的方法存根
		//URL = "http://ios.58cainiu.com/100hongbao.html";
		URL = getIntent().getStringExtra("url");
		hasLogin = PreferenceUtil.getBoolean(mContext, "hasLogin");
		isExitWay = PreferenceUtil.getBoolean(mContext, "isExitWay");
		flag=getIntent().getStringExtra("flag");

		if (hasLogin&&!"sft".equals(flag)&&!"weixin".equals(flag)&&!"post".equals(flag)&&!"matchkey".equals(flag)) {
			URL = getSignUrl(URL.trim());
		}else{
			user = MyDbUtils.getCurrentUser();
		}

		version = MyDbUtils.getCurrentVersion();
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
		//mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
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

		// 解决缓存问题
		mWebView.addJavascriptInterface(new ReadHtmlVersion(), "readVersionObj");
		if ((!hasLogin) && isExitWay) {
			mWebView.clearCache(true);
			mWebView.clearHistory();
			CookieSyncManager.createInstance(this);
			CookieSyncManager.getInstance().startSync();
			CookieManager.getInstance().removeSessionCookie();
		}

		boolean hasHtmlTitleChanged = PreferenceUtil.getBoolean(
				LotteryTypeActivity.this, "hasHtmlTitleChanged");

		if (hasHtmlTitleChanged) {


			mWebView.clearCache(true);
			mWebView.clearHistory();
			CookieSyncManager.createInstance(this);
			CookieSyncManager.getInstance().startSync();
			CookieManager.getInstance().removeSessionCookie();

		}
		showDialog();


		if (!TextUtils.isEmpty(flag) &&  "sft".equals(flag)) {
			String[] strs=URL.split("page=mobile");
			try {
				mWebView.postUrl(strs[0]+"page=mobile", strs[1].getBytes("UTF_8"));
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}else if (!TextUtils.isEmpty(flag) &&  "post".equals(flag)) {
			String httpbody=getIntent().getStringExtra("httpbody");
			try {
				orderId = getIntent().getStringExtra("orderId");
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
				if (failingUrl.startsWith("weixin://wap/pay?")) {
					return;
				}

				tv_head.setVisibility(View.VISIBLE);

				errorPage.setVisibility(View.VISIBLE);

				mWebView.setVisibility(View.GONE);

				// 提示框

				boolean isConnected = NetUtils.isNetworkConnected(LotteryTypeActivity.this);
				if (!isConnected) {
					new AlertDialog(LotteryTypeActivity.this).builder()
					.setTitle("提示").setMsg("检测到无网络连接")
					.setPositiveButton("设置", new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = null;
							// 判断手机系统的版本 即API大于10 就是3.0或以上版本
							intent = new Intent(
									android.provider.Settings.ACTION_SETTINGS);
							LotteryTypeActivity.this
							.startActivity(intent);
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
				if (!url.contains(ConstantsBase.IP)
						&& !url.contains(ConstantsBase.IPQT)) {
					if(getIntent().getBooleanExtra("show",false)){
						tv_head.setTitle(LotteryTypeActivity.this.getResources().getString(R.string.kefuname));			
					}
					tv_head.setVisibility(View.VISIBLE);
				} else {
					tv_head.setVisibility(View.GONE);
				}

				if (url.contains("https://m.fox008.com/analysis/tips?")) {
					tv_head.setVisibility(View.GONE);
				}
				if (activityName!=null) {
					tv_head.setTitle(activityName);	
					tv_head.setVisibility(View.VISIBLE);			
				}

				if(url.contains("http://an.caiwa188.com/help/dlt.html")){
					tv_head.setTitle("大乐透玩法说明");	
					tv_head.setVisibility(View.VISIBLE);		
				}if(url.contains("http://an.caiwa188.com/help/syydj.html")){
					tv_head.setTitle("11选5玩法说明");
					tv_head.setVisibility(View.VISIBLE);			
				}if(url.contains("http://an.caiwa188.com/help/syydj.html")){
					tv_head.setTitle("11选5玩法说明");
					tv_head.setVisibility(View.VISIBLE);			
				}if(url.contains("http://an.caiwa188.com/help/jczq.html")){
					tv_head.setTitle("竞彩足球玩法说明");
					tv_head.setVisibility(View.VISIBLE);			
				}if(url.contains("http://an.caiwa188.com/help/jclq.html")){
					tv_head.setTitle("竞彩篮球玩法说明");
					tv_head.setVisibility(View.VISIBLE);			
				}if(url.contains("http://an.caiwa188.com/help/ssq.html")){
					tv_head.setTitle("双色球玩法说明");
					tv_head.setVisibility(View.VISIBLE);			
				}if(url.contains("http://an.caiwa188.com/help/rx9c.html")){
					tv_head.setTitle("任选九玩法说明");
					tv_head.setVisibility(View.VISIBLE);			
				}if(url.contains("http://an.caiwa188.com/help/sfc.html")){
					tv_head.setTitle("胜负彩玩法说明");
					tv_head.setVisibility(View.VISIBLE);			
				}if(url.contains("http://an.caiwa188.com/help/gendan.html")){
					tv_head.setTitle("跟单说明");
					tv_head.setVisibility(View.VISIBLE);			
				}if(url.contains("http://an.caiwa188.com/help/k3.html")){
					tv_head.setTitle("快三说明");
					tv_head.setVisibility(View.VISIBLE);			
				}if(url.contains("http://an.caiwa188.com/help/taocan.html")){
					tv_head.setTitle("规则");
					tv_head.setVisibility(View.VISIBLE);			
				}if(url.contains("http://www.wocai188.com/drawing.html")){
					tv_head.setTitle("提款说明");
					tv_head.setVisibility(View.VISIBLE);
				}
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);

				if ("https://api.shengpay.com/html5-gateway/express.htm?page=mobile#express/success|0".equals(url)) {
					Intent intentClose = new Intent();  
					intentClose.putExtra("url", "shengfutong");
					intentClose.putExtra("orderId", orderId);
					setResult(RESULT_OK, intentClose); 
					LotteryTypeActivity.this.finish();
				} 
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {	
				if ("http://sj.qq.com/myapp/detail.htm?apkName=com.liao310.www".equals(url)) {
					Intent intent= new Intent();        
				    intent.setAction("android.intent.action.VIEW");    
				    Uri content_url = Uri.parse(url);   
				    intent.setData(content_url);  
				    startActivity(intent);
					return true;
				}
				if (url.startsWith("weixin://wap/pay?")) {
					try{
						Intent intent = new Intent();
						intent.setAction(Intent.ACTION_VIEW);
						intent.setData(Uri.parse(url));
						startActivityForResult(intent, WX_ZF);
						//						startActivity(intent);

					}catch (ActivityNotFoundException e){
						ToastUtils.showShort(LotteryTypeActivity.this, "请安装微信最新版！");
					}
					return true;
				}
				if ("https://m.fox008.com/analysis/undefined".equals(url)) {
					LotteryTypeActivity.this.finish();
					return true;
				}
				if(url.contains("getpayback")){
					Intent intentClose = new Intent();  
					intentClose.putExtra("url", url);
					setResult(RESULT_OK, intentClose); 
					LotteryTypeActivity.this.finish();
					return true;
				}
				if (!TextUtils.isEmpty(url)) {
					if (url.contains(ConstantsBase.CLOSE)) {
						if ((!TextUtils.isEmpty(intentWay))
								&& "exercise".equals(intentWay)) {
							startActivity(new Intent(LotteryTypeActivity.this,MainActivity.class));}
						LotteryTypeActivity.this.finish();
						return true;
					}

					if (!NetWorkUtil
							.isNetworkAvailable(LotteryTypeActivity.this)) {
						NewToast.makeText(LotteryTypeActivity.this,"网络不稳定，请稍后再试！");
						tv_head.setVisibility(View.VISIBLE);
					}
					int index = -1;
					String params = "";
					continueURLParam = "";
					if ((index = url.indexOf("?data=")) != -1) {
						try {
							int len = "?data=".length();
							params = URLDecoder.decode(url.substring(index + len), "UTF-8");
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
					if (!TextUtils.isEmpty(params)) {
						try {
							org.json.JSONObject jobj = new org.json.JSONObject(
									params);
							String action = jobj.getString("action");
							org.json.JSONObject paramsObj = null;
							if (jobj.has("params")) {
								paramsObj = jobj.getJSONObject("params");
								if ("login".equals(action) && paramsObj != null) {
									if (paramsObj.has("url")) {
										continueURLParam = paramsObj
												.getString("url");
									}
									Intent intent = new Intent(mContext,
											LoginActivity.class);
									intent.putExtra("requestName", "webView");
									startActivityForResult(intent, 1);
								}
								if ("share".equals(action) && paramsObj != null) {									
									/*displaylist = new SHARE_MEDIA[]{SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE};
									image = new UMImage(mContext,paramsObj.getString("picUrl"));
									new ShareAction(LotteryTypeActivity.this)
									.setDisplayList(displaylist)
									.withTitle(paramsObj.getString("title"))
									.withText(paramsObj.getString("content"))
									.withTargetUrl(paramsObj.getString("url"))
									.withMedia(image)
									.setCallback(umShareListener)
									.open();*/
								}
								return true;
							}
							if ("login".equals(action) ) {
								Intent intent = new Intent(mContext,LoginActivity.class);
								startActivityForResult(intent, 1);
								return true;
							}
							if ("pay".equals(action) ) {
								if (!PreferenceUtil.getBoolean(LotteryTypeActivity.this,"hasLogin") ) {
									Intent intentLogin = new Intent(LotteryTypeActivity.this, LoginActivity.class); 
									intentLogin.putExtra("requestName", "intentLogin");
									startActivityForResult(intentLogin,LOGIN);
									return false;
								}else{
//									if(!PreferenceUtil.getBoolean(LotteryTypeActivity.this, "hasRealName")){
//										continueURLParam = url;
//										ToastUtils.showShort(LotteryTypeActivity.this, "亲，请您先进行实名认证");
//										Intent intent = new Intent(LotteryTypeActivity.this, NameAuthActivity.class);
//										startActivityForResult(intent,SMRZ);
//										return true;
//									}
									Intent intent = new Intent(LotteryTypeActivity.this, PayMoneyActivity.class);
									intent.putExtra("needback", true);									
									startActivityForResult(intent,CZ);
									return true;
								}
							}
							if ("football".equals(action) ) {
								Intent intent = new Intent(LotteryTypeActivity.this, FootballMainActivity.class);
								startActivity(intent);
								return true;
							}if ("basketball".equals(action) ) {
								Intent intent = new Intent(LotteryTypeActivity.this, BasketBallMainActivity.class);
								startActivity(intent);
								return true;
							}
							if ("realName".equals(action) ) {
								continueURLParam = url;
								ToastUtils.showShort(LotteryTypeActivity.this, "亲，请您先进行实名认证");
								Intent intent = new Intent(LotteryTypeActivity.this, NameAuthActivity.class);
								startActivity(intent);
								return true;
							}
							if ("register".equals(action) ) {
								if(!hasLogin){
									Intent intent = new Intent(mContext,RegisterByMoibleActivity.class);
									intent.putExtra("from", "activity");
									startActivity(intent);
								}else{
									ToastUtils.showShort(LotteryTypeActivity.this, "您已注册过");
								}
								return true;
							}
							if ("gendan".equals(action) ) {
								Intent intent=new Intent(mContext, MainActivity.class);
								intent.putExtra("hasLogin", "yes");
								intent.putExtra("nowPage", "2");
								startActivity(intent);
								finish();
								return true;
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					view.loadUrl(url);
				}
				return false;
			}
		});

		mWebView.setWebChromeClient(new WebChromeClient() {
			// 动画加载
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
					// if (NetWorkUtil.isNetworkAvailable(getContext())) {
					// getVc().showContent();
					// }
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

	/**
	 * 带菊花的对话框 *
	 */
	public Dialog makeDialog(final Context context, String msg) {
		return makeDialog(context, msg, true, null);
	}

	public void showDialog(String msg) {
		if (!this.isFinishing()) {
			if (mDialog == null)
				mDialog = makeDialog(this, msg);
			if (!mDialog.isShowing())
				mDialog.show();
		}
	}

	/**
	 * 带菊花的对话框 *
	 */
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
	public class ReadHtmlVersion {
		@JavascriptInterface
		public String getVersion(String version) {
			Message msg = mHandler.obtainMessage(0);
			msg.obj = version;
			mHandler.sendMessage(msg);
			return version;
		}
		@JavascriptInterface
		public void getClose(String dingdan) {
			Intent intentClose = new Intent();  
			intentClose.putExtra("url", dingdan);
			setResult(RESULT_OK, intentClose); 
			LotteryTypeActivity.this.finish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == 1 && resultCode == 1) {
			if (continueURLParam != null) {
				continueURLParam = getSignUrl(continueURLParam);
				if (mWebView != null) {
					mWebView.loadUrl(continueURLParam);
				}
			}
		}
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case SMRZ:// 实名验证回来
				if(data.getBooleanExtra("yanzheng", false)){
					Intent intent = new Intent(LotteryTypeActivity.this, PayMoneyActivity.class);
					startActivity(intent);
					LotteryTypeActivity.this.finish();
				}
			case CZ:// 充值
				if(data.getBooleanExtra("czchenggong", false)){
					LotteryTypeActivity.this.finish();
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void showDialog() {
		showDialog("");
	}

	private String getSignUrl(String url) {
		String par = "Value=（AES加密后）+&Key=（数字签名）";
		try {


			String prikeyvalue = "30820154020100300d06092a864886f70d01010105000482013e3082013a0201000241009c90788ae7870fda60a0b00cb28c690cbfbe423cc38f25934a60d38558402ff92116181c38095797dfb9b33333920f85cfa0d1c32bb376361305e983d6171eed020301000102406f6eb02d052eef0c99dba491d4fef4c1db331a47cf5472050c5a30126746801d6da3cd02590c3dc0a09cb8a43ddf10a52173ed29117e8c2904672e15584511cd022100dcbdb2365b398d39ae5faf3bf2faf4d1f087adf9183634a174a75933eec96def022100b59288199567f9c282bf02549da5c58779f4674b8630ca44a29552abaa539ce3022100b0bb744eced5123c275f569681e0e95898e298a8c1f8cc44a47844142f4fb8a302206c4727d669dc897acf516ce85ce2c07adbe53dbc3217e2672fb5708962975e1502205b273116d697f997181a0ed6916ca5a6f1a8e253f46bd376f2d80e5302516da8";// 这是输出的私钥编码，不需要修改
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
					SignatureData.hexStrToBytes(prikeyvalue));
			KeyFactory keyf = KeyFactory.getInstance("RSA");
			PrivateKey myprikey = keyf.generatePrivate(priPKCS8);

			String userId = null;
			String password = null;
			String mobile = null;
			String userName = null;
			user = MyDbUtils.getCurrentUser();
			if (user != null) {
				userId = user.getUserId();
				userName = user.getUserName();
				password = user.getUserPwd();
			}
			userInfo = MyDbUtils.getCurrentUserInfo();
			if (userInfo != null) {
				mobile = userInfo.getMobile();
			}

			String myinfo = "SBSUserID=" + userId + "&password=" + password
					+ "&userName=" + userName + "&SiteID=2&mobile=" + mobile;

			// // 要签名的信息
			// String myinfo = "SBSUserID=3A&SiteID=1&mobile=18913167527";
			// 用私钥对信息生成数字签名

			java.security.Signature signet = java.security.Signature
					.getInstance("MD5withRSA");
			signet.initSign(myprikey);
			signet.update(myinfo.getBytes("ISO-8859-1"));
			byte[] signed = signet.sign(); // 对信息的数字签名


			// String myinfoAES = des.encryptDES(myinfo, AESKey);
			String myinfoAES = Base64.encode(myinfo.getBytes("utf-8"));

			par = "Value="
					+ (myinfoAES + "&Key=" + SignatureData
							.bytesToHexStr(signed));
			// 加密过的16进制的字符串转化成二进制数组
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url + "?" + par + "&date=" + System.currentTimeMillis();
	}

	public void deleteFile(File file) {

		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					deleteFile(files[i]);
				}
			}
			file.delete();
		} 
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && "exercise".equals(intentWay)) {
			startActivity(new Intent(LotteryTypeActivity.this,
					MainActivity.class));
			finish();

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	
}
