package com.bojing.gathering.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.bojing.gathering.domain.InfoDetail;
import com.bojing.gathering.util.PreferenceUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.greenrobot.event.EventBus;

/**
 * Created by admin on 2018/9/18.
 */

public class MyStaticReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //写下收到广播后所执行的操作
        if ("com.bojing.gathering.base.MyStaticReceiver".equals(intent.getAction())) {
            // intent.getStringExtra("msg")
            /*Toast.makeText(context, "消息内容是：" + intent.getStringExtra("msg"),
                    Toast.LENGTH_LONG).show();*/
            if ("微信支付".equals(intent.getStringExtra("contentTitle"))) {
                String msg = intent.getStringExtra("msg");
                long timeBase = intent.getLongExtra("when", 0l);
                if(msg.contains("收款")){
                    msg =  msg.substring(msg.indexOf("收款")+2,msg.length()-1) ;
                }
                String time = times(timeBase);
                InfoDetail infoDetail = new InfoDetail();
                infoDetail.setCreateTime(time);
                infoDetail.setPaymentId(2);
                infoDetail.setTotalFee(msg);
                infoDetail.setTxNoStatus("success");
                infoDetail.setProviderAccount(PreferenceUtil.getString(context,"weixinbaoAccountNo"));
                infoDetail.setProviderId(PreferenceUtil.getString(context,"weixinbaoProviderId"));
                EventBus.getDefault().post(infoDetail);
            }
            if ("支付宝通知".equals(intent.getStringExtra("contentTitle"))) {
                String msg = intent.getStringExtra("msg");
                long timeBase = intent.getLongExtra("when", 0l);
                String buyername = "";
                if(msg.contains("通过扫码")){
                    buyername =  msg.substring(0,msg.indexOf("通过扫码")) ;
                }
                String money = "";
                if(msg.contains("付款")){
                    money =  msg.substring(msg.indexOf("付款")+2,msg.indexOf("元")) ;
                }else  if(msg.contains("收款")){
                    money =  msg.substring(msg.indexOf("收款")+2,msg.indexOf("元")) ;
                }
                String time = times(timeBase);
                InfoDetail infoDetail = new InfoDetail();
                infoDetail.setBuyerName(buyername);
                infoDetail.setCreateTime(time);
                infoDetail.setPaymentId(1);
                infoDetail.setTotalFee(money);
                infoDetail.setTxNoStatus("success");
                infoDetail.setProviderAccount(PreferenceUtil.getString(context,"zhifubaoAccountNo"));
                infoDetail.setProviderId(PreferenceUtil.getString(context,"zhifubaoProviderId"));
                EventBus.getDefault().post(infoDetail);


            }
        }
    }

    public static String times(long time) {
        String timeString = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 hh:mm");
        timeString = sdf.format(new Date(time));//单位秒
        return timeString;
    }
}