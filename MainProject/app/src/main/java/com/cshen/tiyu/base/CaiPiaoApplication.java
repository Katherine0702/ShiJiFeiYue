package com.cshen.tiyu.base;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import org.xutils.DbManager;
import org.xutils.x;

import com.cshen.tiyu.MainActivity;
import com.cshen.tiyu.activity.login.LoginActivity;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.main.Message;
import com.cshen.tiyu.net.https.ServiceABase;
import com.cshen.tiyu.net.https.ServiceMain;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.utils.Util;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v7.appcompat.BuildConfig;
import android.text.TextUtils;
import android.util.Log;

import de.greenrobot.event.EventBus;


public class CaiPiaoApplication extends Application {
	// 用于存放倒计时时间
	public static Map<String, Long> map;
	private List<Activity> activitys = new LinkedList<Activity>();
	private List<Service> services = new LinkedList<Service>();

	private static Context mContext;
	private static DbManager.DaoConfig daoConfig;
	public  static DbManager.DaoConfig getConfig() {
		return daoConfig;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = getApplicationContext();    
		x.Ext.init(this);
		x.Ext.setDebug(BuildConfig.DEBUG);
		daoConfig =new DbManager.DaoConfig().setDbName("caipiao.db");//数据库更新操作

		//FeedbackAPI.init(this,"25115230","c9b9b481001556f2644d756160e4278b");//初始化用户反馈在调用时:
		UMConfigure.init(this, "5b76578b8f4a9d59f60000b0", "zhuxian", UMConfigure.DEVICE_TYPE_PHONE, "3cebf3d56831e34343b7c1021fc4c69b");
		//MiPushRegistar.register(this, "5101772186948", "6WIJu7omKawFZbJjdv2/WQ==");
		//HuaWeiRegister.register(this);
		//MeizuRegister.register(this,"113218", "f78fea22795e415b8e6b2fe59ac459ee");

		PushAgent mPushAgent = PushAgent.getInstance(this);
     //注册推送服务，每次调用register方法都会回调该接口
		mPushAgent.register(new IUmengRegisterCallback() {
			@Override
			public void onSuccess(String deviceToken) {
				//注册成功会返回device token
				Log.e("deviceToken",deviceToken+",deviceToken");
				setDeviceToken(deviceToken);
			}
			@Override
			public void onFailure(String s, String s1) {
				Log.e("deviceToken","s1:"+s1+",s"+s);
			}
		});
		mPushAgent.setDisplayNotificationNumber(10);
		UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
			@Override
			public void dealWithCustomAction(Context context, UMessage msg) {
				if(!TextUtils.isEmpty(msg.custom)){
					switch(msg.custom){

						case "openredpacket":
							Intent	intent = new Intent(context,MainActivity.class);
							intent.putExtra("todo", "toopenredpacket");
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							context.startActivity(intent);
							break;
						case "getmessage":
							Intent	intentMessage = new Intent(context,MainActivity.class);
							intentMessage.putExtra("todo", "togetmessage");
							if(msg!=null){
								intentMessage.putExtra("title", msg.title);
								intentMessage.putExtra("content", msg.text);
							}
							intentMessage.putExtra("needSave", true);
							intentMessage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							context.startActivity(intentMessage);
							break;

						case "openorderpage"://中奖通知


							String mesg[] = msg.extra.get("parameter").split(",");
							String lotteryType = Util.getLotteryType(mesg[0]);
							String schemeId = mesg[1];
							if (PreferenceUtil.getBoolean(context,"hasLogin")) {//已登录
								Intent intent_order = new Intent(context,MainActivity.class) ;
								intent_order.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								intent_order.putExtra("todo", "toOrderDetail");
								intent_order.putExtra("schemeId",schemeId );
								intent_order.putExtra("lotteryId", lotteryType);
								context.startActivity(intent_order);
							}else {//未登录,先登录
								Intent intent_order = new Intent(context,LoginActivity.class) ;
								intent_order.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								intent_order.putExtra("isOrderDetail", true);//是否进入订单详情
								intent_order.putExtra("schemeId", schemeId);
								intent_order.putExtra("lotteryId", lotteryType);
								context.startActivity(intent_order);
							}
							//保存消息到消息列表
							saveMessage(msg.title,msg.text);

							break;

						case "rechargeSuccess"://充值通知
							Intent	intent_rechange = new Intent(context,MainActivity.class);
							intent_rechange.putExtra("todo", "rechargeSuccess");
							intent_rechange.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							context.startActivity(intent_rechange);
							//保存消息到消息列表
							saveMessage(msg.title,msg.text);
							break;

						case "link"://跳链接
							String url = msg.extra.get("parameter");

							Intent intent_link =  new Intent(context,MainActivity.class);
							intent_link.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							intent_link.putExtra("todo", "toLink");
							intent_link.putExtra("url", url);
							startActivity(intent_link);

							break;

					}

				}
			}
		};
		mPushAgent.setNotificationClickHandler(notificationClickHandler);
		mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SERVER); //声音
		mPushAgent.setNotificationPlayLights(MsgConstant.NOTIFICATION_PLAY_SERVER);//呼吸灯
		mPushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SERVER);//振动
	}


	public void saveMessage(String titleStr,String contentStr){
		SimpleDateFormat  formatter = new SimpleDateFormat("yyyy-MM-dd    HH:mm:ss");
		Date  curDate = new  Date(System.currentTimeMillis());//获取当前时间
		String timeStr =  formatter.format(curDate);
		Message mess = new Message();
		mess.setTitle(titleStr);
		mess.setTime(timeStr);
		mess.setContent(contentStr);
		MyDbUtils.saveMessageSigleData(mess);
	}

	public static boolean isAppAlive(Context context, String packageName){
		ActivityManager activityManager =
				(ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> processInfos
				= activityManager.getRunningAppProcesses();
		for(int i = 0; i < processInfos.size(); i++){
			if(processInfos.get(i).processName.equals(packageName)){
				return true;
			}
		}
		return false;
	}
	public void setDeviceToken(final String deviceToken){
		PreferenceUtil.putString(mContext, "deviceToken", deviceToken);
		ServiceMain.getInstance().getSaveDeviceToken(this, deviceToken,new ServiceABase.CallBack<String>() {

			@Override
			public void onSuccess(String t) {
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
			}
		});
	}

	public static Context getmContext() {
		return mContext;
	}

	public static void setmContext(Context mContext) {
		CaiPiaoApplication.mContext = mContext;
	}

	public void addActivity(Activity activity) {
		activitys.add(activity);
	}

	public void removeActivity(Activity activity) {
		activitys.remove(activity);
	}


}
