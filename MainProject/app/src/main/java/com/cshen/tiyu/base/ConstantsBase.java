package com.cshen.tiyu.base;

import android.Manifest;

import com.cshen.tiyu.domain.login.User;

/**
 * Created by lingcheng on 15/10/1.
 */
public class ConstantsBase {   
	public static final int http = 0;//0是http,1是https
	public static final int https = 1;//0是http,1是https
	public static final int httpOrhttps = http;//0是http,1是https

	/************************线上环境*************************/
	//public static final String IP = "http://106.14.7.132:8080";
	//public static final String IP = "http://192.168.0.105:8080";
	//public static final String HOMEIP = "http://192.168.0.102:8081";
	public static final String IP = "http://www.wocai188.com";
	public static final String HOMEIP = "http://an.wocai188.com";
	public static final String IP_NEWS = "http://news.wocai188.com";
	public static final String IPQT = "http://shouji.wocai188.com";


	/*public static final String IP = "http://www.caiwa188.com";
	public static final String HOMEIP = "http://an.caiwa188.com";
	public static final String IP_NEWS = "http://news.caiwa188.com";    //资讯IP n
	public static final String IPQT = "http://shouji.caiwa188.com";*/

	public static final String AliasType = "com.58cainiu";

	//public static final String IP = "http://192.168.1.15:8080";
	//public static final String IP = "http://192.168.1.18:8090";

	/************************m环境******************************/
	//public static final String IP  = "https://m.58cainiu.com";
	//public static final String HOMEIP = "http://192.168.1.105:8080";
    //public static final String IP_NEWS = "http://newstest.58cainiu.com/";//晒单  

	/*************************本地环境**********************************/
//	public static final String HOMEIP = "http://192.168.0.127:8090";
//	public static final String HOMEIP = "http://192.168.0.119:8081";
//  public static final String IP_NEWS = "http://192.168.0.145:8080";  //lh



	public static final int REQUEST_EXTERNAL_STORAGE = 1;
	public static String[] PERMISSIONS_STORAGE = {
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE};
	
	
	public static final int SSQ = 0;
	public static final int SFC = 9;
	public static final int DLT = 13;
	public static final int SD115 = 14;
	public static final int JCZQ = 17;
	public static final int JCLQ = 18;
	public static final int GD115 = 19;
	public static final int PL35 = 6;
	public static final int Fast3 = 24;

	public static final int ZHIXUAN1 = 0;
	public static final int RENXUAN2 = 1;
	public static final int ZUXUAN2 = 2;
	public static final int ZHIXUAN2 = 3;
	public static final int RENXUAN3 = 4;
	public static final int ZUXUAN3 = 5;
	public static final int ZHIXUAN3 = 6;
	public static final int RENXUAN4 = 7;
	public static final int RENXUAN5 = 8;
	public static final int RENXUAN6 = 9;
	public static final int RENXUAN7 = 10;
	public static final int RENXUAN8 = 11;

	public static final int HEZHI = 0;// 和值 0
	public static final int SANTONGHAO = 2;// 三同号单选 2
	public static final int SANTONGHAOTONG = 1;// 三同号通选 1
	public static final int SANBUTONGHAO = 5;// 三不同号 5
	public static final int SANLIANHAO = 7;// 三连号 7
	public static final int ERTONGHAO = 4;// 二同号单选 4
	public static final int ERTONGHAOFU = 3;// 二同号复选 号3
	public static final int ERBUTONGHAO = 6;

	public static final String CHOOSEDNUMBERSSSQ = "ChoosedNumbersSSQ";
	public static final String CHOOSEDNUMBERS = "ChoosedNumbers";
	public static final String CHOOSEDNUMBERS115 = "ChoosedNumbers115";
	public static final String CHOOSEDNUMBERSGD115 = "ChoosedNumbersgd115";
	public static final String CHOOSEDNUMBERSJCZQ = "ChoosedNumbersJCZQ";
	public static final String CHOOSEDNUMBERSJCZQTIME = "CHOOSEDNUMBERSJCZQTIME ";
	public static final String CHOOSEDNUMBERSJCLQ = "ChoosedNumbersJCLQ";
	public static final String CHOOSEDNUMBERSJCLQTIME = "CHOOSEDNUMBERSJCLQTIME";
	public static final String CHOOSEDNUMBERSFC = "CHOOSEDNUMBERSFC";
	public static final String CHOOSEDNUMBERSFAST3 = "CHOOSEDNUMBERSFAST3";

	public static final String KEY = "571f0a79e0f55a5a09001681cde";// 验证码登录
	public static final String WEBVIEW_CACHE_PATH = // 网页存储位置
	CaiPiaoApplication.getmContext().getFilesDir().getAbsolutePath()
			+ "/webViewCache";

	public static final String IP_LIVE_SCORES = "http://data.fox008.com/miracleData/match";// 比分直播
	public static final int PayForChongZhiPay = 0;
	public static final int PayForChongZhi = 1;
	public static final int PayForPay = 2;
	public static final String CLOSE = "closecaipiao.html";
	public static final String INDEX = "/open/index.html";
	public static final String SUCCESS = "0";
	public static final String TESTIP = "http://172.20.75.3:8080/first/";
	/************************ 京东支付 *************************/
	public static final String APPIDJD = "90d12e542bd6a0cd2c9634ffca3f84f1";
	public static final String SHHJD = "110278961002";// 商户号
	/************************ 支付宝 *************************/
	// 合作身份者id，以2088开头的16位纯数字
	public static final String PID = "2088711808547180";
	public static final String APPID = "2015012600027969";
	public static final String TARGETID = "";
	// 商户PID
	public static final String PARTNER = "2088711808547180";
	// 商户收款账号
	public static final String SELLER0 = "";
	// 商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANh0BsvH3uIw+YxX"
			+ "D9gAoVT8xigsRiKs+n5tb/hAR5kXD3qjgzNLk0vkaHV86mCzquZLSv2EpjczKasC"
			+ "wqgqRcvetGDY1/KmMsIvxAr1KHH+2FlZ6DVRJdqJinWVDxh8CYnJrRBiopir17W2"
			+ "nsOl7zK/ObP6d77oTzeUeZNs8CFJAgMBAAECgYEAxXc0K/wvLZMYVhum7JvGks33"
			+ "yPZ7FW9RJr2YBJY/uz36dRtuugEcY4QAeA55k7bVFjtiMl4tdPZL8SMXimrmOCR/"
			+ "2zjxTNd7M490/5hHsdUH7lo/+qXQcOiVxMTRzuyfVmxWGw4PN15JYm60m1HUJkiH"
			+ "BV8jesfX71K6T3U+ncECQQD7uJ7Pa9ghnWP05qx3R46QC7KDuizAPLDj+IJvcGPN"
			+ "sp0xcozxf6F/4aeTRbQBWtqaQJu0NVVug3zeCruK05h1AkEA3CHvhe9F5dB2AhpU"
			+ "LPkC9foKl9gRe0zZLcnLtYdUCf6aeKBsO+Dh+p1Q82m7+7mPqvhz9tHlNJcipWbN"
			+ "IRirBQJBAJdS53W+WVHr8jk5qxiR1exWe9ygwWJXmeeSb6dao0T3SXR+bqiHl6jv"
			+ "GPR6BLqDycLNcE0netJTDW/Wd+P/TDkCQE/T5p51VK2wWPsOPTRJ4O7+i9TgwAYs"
			+ "qh1Wro7X0nwFXlMcKePU+138qpx63Xa9pgHkM3lwSGQ34pKEoyK7ZVkCQFCMp7f3"
			+ "RdI3FP0Vt87uLjAkp6ZfZOw2EekaDNuje3Q2dGPxOKPctkCke8B3wy1dJsWWU9ILv5ljwbaJas9lfPo=";
	// 支付宝公钥
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
	/************************ 微信支付 ************************/
	public static final String WEIXINAPPID = "wxb473b0489a6944b4";
	public static final String WEIXINAPPSECRET = "c2094c09d8733c37d911025922ae61d4";

	/************************ 现在支付 ************************/
	public static final String XZAppID = "149553252573370";// 1408709961320306(微众银行使用)
	public static final String XZKey = "56xbf3aHzgRFbqF4H6ohQmgBhHiGCnt9";// 0nqIDgkOnNBD6qoqO5U68RO1fNqiaisg(微众银行使用)
	
	/************************ 微信分享 ************************//*
	public static final String WEIXINAPPID = "wx5194382ca095b5cb";
	//public static final String WEIXINAPPSECRET = "59a5be8e59a31efe8a9e111076359d65";
	public static final String WEIXINAPPSECRET = "bf03bf4cc21343a25366fc09b56e4105";
	*//************************ QQ分享 ************************//*
	public static final String QQAPPID = "1105946855";
	public static final String QQAPPSECRET = "elAAhyWprJ3L8S3U";
	*//************************ 微博分享 ************************//*
	public static final String WEIBOAPPID = "wxb473b0489a6944b4";
	public static final String WEIBOAPPSECRET = "c2094c09d8733c37d911025922ae61d4";*/

	
	
	
	
	/************************ 途呗支付 ************************/

	// 正式商户号
	public static final String TBAppID = "23109";
	// 正式密钥
	public static final String TBKey = "fCk4NV3G8JUDAvRYWs9nnpzP8TVTV6Wy";

	/**
	 * 附件下载成功状态位
	 */
	public final static int FILEDOWNOK = 1;
	/**
	 * 附件下载进度
	 */
	public final static int FILEDOWNNOW = 3;
	/**
	 * 附件下载失败状态位
	 */
	public final static int FILEDOWNERROR = 2;
	/**
	 * 附件下载开始状态位
	 */
	public final static int FILEDOWNSTART = 5;
	/**
	 * 图片本地下载
	 */
	public static final int GETLOCATEDRAWABLE = 7;

	public static final int REQUEST_CODE_TAKE_PHOTO = 11;// 拍照的requestCode
	public static final int REQUEST_CODE_CHOOSE_PHOTO = 12;// 相册的requestCode
	// use信息经常要用到说以额外存放
	public static final String SEARCH_TIME = "3";// 0=7天前;1=15天前;2=1个月前;3=3个月前

	public static final String ISNEW = "ISNEW";// 1是新用户
	public static final int MAX_UNITS = 10000;
	private static User mUser;

	public static User getUser() {
		return mUser;
	}

	public static void setUser(User user) {
		mUser = user;
	}
}
