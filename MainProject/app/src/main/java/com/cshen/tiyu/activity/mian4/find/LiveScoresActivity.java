package com.cshen.tiyu.activity.mian4.find;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.cshen.tiyu.R;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.login.User;
import com.cshen.tiyu.domain.login.UserInfo;
import com.cshen.tiyu.utils.Base64;
import com.cshen.tiyu.utils.NetUtils;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.SignatureData;
import com.cshen.tiyu.widget.AlertDialog;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;

import android.content.Context;
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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import de.greenrobot.event.EventBus;

public class LiveScoresActivity extends BaseActivity{

	private UserInfo userInfo;
	private User user;
	private LinearLayout errorPage;
	private TopViewLeft tv_head;
	String newUrl;
	private LiveScoresActivity _this;
	private WebView mWebView = null;
	private boolean  hasLogin = false;
	private String urlStr = "";
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:  
				mWebView.loadDataWithBaseURL(null, newUrl, "text/html",  
						"utf-8", null);

				break;     
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_this = this;
		EventBus.getDefault().register(this);
		setContentView(R.layout.live_scores_fragment);
		hasLogin = PreferenceUtil.getBoolean(_this, "hasLogin");
		urlStr = getIntent().getStringExtra("url");
		initView();
	}


	private void initView() {
		tv_head = (TopViewLeft) findViewById(R.id.tv_head);
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
				finish();
			}
		});
		
		errorPage=(LinearLayout) findViewById(R.id.error_page);

		mWebView = (WebView) findViewById(R.id.wv_live_score);
		mWebView.getSettings().setJavaScriptEnabled(true);

		mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT); // 设置
		// 缓存模式
		mWebView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
		mWebView.getSettings().setAllowFileAccess(true);
		// 开启 database storage API 功能 //启用数据库
		mWebView.getSettings().setDatabaseEnabled(true);
		String cacheDirPath = _this.getFilesDir().getAbsolutePath() + "/webviewCache";
		// 设置数据库缓存路径
		mWebView.getSettings().setDatabasePath(cacheDirPath);
		// 设置 Application Caches 缓存目录
		mWebView.getSettings().setAppCachePath(cacheDirPath);
		// 开启 Application Caches 功能
		mWebView.getSettings().setAppCacheEnabled(true);
		String dir = _this.getApplicationContext()
				.getDir("database", Context.MODE_PRIVATE).getPath();
		// 启用地理定位
		mWebView.getSettings().setGeolocationEnabled(true);
		// 设置定位的数据库路径
		mWebView.getSettings().setGeolocationDatabasePath(dir);
		// 最重要的方法，一定要设置，这就是出不来的主要原因
		mWebView.getSettings().setDomStorageEnabled(true);

		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.addJavascriptInterface(new ReadHtmlVersion(), "back");
		// 解决缓存问题
		if (!hasLogin) {
			mWebView.clearCache(true);
			mWebView.clearHistory();
			CookieSyncManager.createInstance(_this);
			CookieSyncManager.getInstance().startSync();
			CookieManager.getInstance().removeSessionCookie();
		}

		mWebView.loadUrl(urlStr);
		//javascript:history.back()
		mWebView.setWebViewClient(new WebViewClient() {
			
			@Override
			public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {


				return super.shouldOverrideKeyEvent(view, event);
			}
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				// TODO 自动生成的方法存根
				super.onReceivedError(view, errorCode, description, failingUrl);

				boolean isConnected = NetUtils.isNetworkConnected(_this);
				if (!isConnected) {
					new AlertDialog(_this).builder().setTitle("提示").setMsg("检测到无网络连接")
					.setPositiveButton("设置", new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = null;
							// 判断手机系统的版本 即API大于10 就是3.0或以上版本
							intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
							_this.startActivity(intent);
						}
					}).setNegativeButton("取消", new OnClickListener() {
						@Override
						public void onClick(View v) {

						}
					}).show();

				}
			}
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {		
				if ("http://sj.qq.com/myapp/detail.htm?apkName=com.liao310.www".equals(url)) {
					tv_head.setVisibility(View.VISIBLE);
					Intent intent= new Intent();        
				    intent.setAction("android.intent.action.VIEW");    
				    Uri content_url = Uri.parse(url);   
				    intent.setData(content_url);  
				    startActivity(intent);
					return true;
				}
				return false;
				
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
				if(urlStr.equals(url)){
					tv_head.setVisibility(View.VISIBLE);
				}else{
					tv_head.setVisibility(View.GONE);
				}
			}  

		});

	} 
	public class ReadHtmlVersion {
		@JavascriptInterface
		public void back() {
			_this.finish();
		}
	}

	public String changUrl(String url){
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
			Elements elements = doc.select("a.icon");
			for (Element element : elements) {
				String imgUrl = element.attr("href");
				if("javascript:history.back()".equals(imgUrl)){
					element.remove();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return doc.toString();
	}
	private String getSignUrl(String URL) {
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


			String myinfoAES = Base64.encode(myinfo.getBytes("utf-8"));

			par = "Value="
					+ (myinfoAES + "&Key=" + SignatureData
							.bytesToHexStr(signed));
		} catch (Exception e) {

			e.printStackTrace();

		}

		return URL + "?" + par + "&date=" + System.currentTimeMillis();
	}


	public void onEventMainThread(String event) {
		if (!TextUtils.isEmpty(event)) {
			if ("updateLiveScores".equals(event)) {

			}
		}
	}

}
