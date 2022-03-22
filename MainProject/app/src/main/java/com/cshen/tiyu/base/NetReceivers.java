package com.cshen.tiyu.base;


import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.cshen.tiyu.MainActivity;
import com.cshen.tiyu.activity.LotteryTypeActivity;
import com.cshen.tiyu.activity.login.LoginActivity;
import com.cshen.tiyu.activity.mian4.personcenter.message.MessageDetailActivity;
import com.cshen.tiyu.activity.redpacket.RedPacketRainActivity;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.main.Message;
import com.cshen.tiyu.utils.NetUtils;
import com.cshen.tiyu.utils.OpenOrderPageUtil;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.Util;
import com.cshen.tiyu.widget.AlertDialog;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

public class NetReceivers extends BroadcastReceiver {


	public void onReceive(final Context context, Intent intent) {
		String action = intent.getAction();
		if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
			boolean isConnected = NetUtils.isNetworkConnected(context);
			if (!isConnected) {

				new AlertDialog(context).builder().setTitle("提示").setMsg("检测到无网络连接")
				.setPositiveButton("设置", new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = null;
						// 判断手机系统的版本 即API大于10 就是3.0或以上版本
						intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
						context.startActivity(intent);
					}
				}).setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				}).show();

			}
		}
		// TODO Auto-generated method stub
		if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
			String packageName = intent.getDataString();
			System.out.println("android.intent.action.PACKAGE_ADDED---------------" + packageName);
		}
		// 覆盖安装
		if (intent.getAction().equals("android.intent.action.PACKAGE_REPLACED")) {
			String packageName = intent.getDataString();
			System.out.println("android.intent.action.PACKAGE_REPLACED---------------" + packageName);
		}
		// 移除
		if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
			String packageName = intent.getDataString();
			System.out.println("android.intent.action.PACKAGE_REMOVED---------------" + packageName);
		}

		PushAgent mPushAgent = PushAgent.getInstance(context);

		UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
			@Override
			public void dealWithCustomAction(Context context, UMessage msg) {
				if(!TextUtils.isEmpty(msg.custom)){
					switch(msg.custom){
					case "openredpacket":
						Intent	intent = new Intent(context,RedPacketRainActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(intent);
						break;
					case "getmessage":
						Intent	intentMessage = new Intent(context,MessageDetailActivity.class);
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
						if (PreferenceUtil.getBoolean(context,"hasLogin")) {//已登录,直接去订单详情
							new OpenOrderPageUtil().toOpenOrderPage(context, lotteryType, schemeId,true,"");//跳转
						}else {//未登录,先登录
							Intent intent_order = new Intent(context,LoginActivity.class);
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
						if (PreferenceUtil.getBoolean(context,"hasLogin")) {//已登录
							intent_rechange.putExtra("hasLogin", "yes");
							intent_rechange.putExtra("nowPage", "4");
						}else {//
							intent_rechange.putExtra("hasLogin", "cancel");
							intent_rechange.putExtra("nowPage", "4");
						}

						intent_rechange.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(intent_rechange);		
						//保存消息到消息列表						
						saveMessage(msg.title,msg.text);
						break;
					case "link"://跳链接
						String url = msg.extra.get("parameter");
						Intent	intentLink = new Intent(context,LotteryTypeActivity.class);
						intentLink.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intentLink.putExtra("url", url);
						context.startActivity(intentLink);

						break;

					}
				}
			}

		};
		mPushAgent.setNotificationClickHandler(notificationClickHandler);


		context.unregisterReceiver(this);
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

}