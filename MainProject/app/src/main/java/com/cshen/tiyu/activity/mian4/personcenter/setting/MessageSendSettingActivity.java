package com.cshen.tiyu.activity.mian4.personcenter.setting;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.utils.PreferenceUtil;


public class MessageSendSettingActivity extends BaseActivity implements OnClickListener 
{
	TextView title;
	View ifprize_child;
	boolean prizeB,followorderB,ifprizeB,
	footballB,doublecolorB,happyB;
	CheckBox prizeCb,followorderCb,ifprizeCb,
	footballCb,doublecolorCb,happyCb;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messagesend_setting);

		title=(TextView) findViewById(R.id.tv_head_title);
		title.setText("推送设置");
		findViewById(R.id.iv_back).setVisibility(View.VISIBLE);
		findViewById(R.id.iv_back).setOnClickListener(this);

		prizeCb = (CheckBox)findViewById(R.id.prize_notice_iv);//中奖通知
		prizeCb.setOnClickListener(this);

		followorderCb = (CheckBox)findViewById(R.id.followorder_notice_iv);//跟单通知
		followorderCb.setOnClickListener(this);

		ifprizeCb = (CheckBox)findViewById(R.id.ifprize_notice_iv);//开奖通知
		ifprizeCb.setOnClickListener(this);

		ifprize_child= findViewById(R.id.ifprize_notice_child);//开奖通知子通知栏

		footballCb = (CheckBox)findViewById(R.id.football_notice_iv);//足彩
		footballCb.setOnClickListener(this);

		doublecolorCb = (CheckBox)findViewById(R.id.doublecolor_notice_iv);//双色球
		doublecolorCb.setOnClickListener(this);

		happyCb = (CheckBox)findViewById(R.id.happy_notice_iv);//大乐透
		happyCb.setOnClickListener(this);

		setDefaultNotice();
	}
	public void setDefaultNotice(){
		prizeB = PreferenceUtil.getBoolean(this, PreferenceUtil.PRIZE);
		prizeCb.setChecked(prizeB);
		followorderB = PreferenceUtil.getBoolean(this, PreferenceUtil.FOLLOWORDER);
		followorderCb.setChecked(followorderB);
		ifprizeB = PreferenceUtil.getBoolean(this, PreferenceUtil.IFPRIZE);
		ifprizeCb.setChecked(ifprizeB);
		
		footballB = PreferenceUtil.getBoolean(this, PreferenceUtil.FOOTBALL);
		footballCb.setChecked(footballB);
		doublecolorB = PreferenceUtil.getBoolean(this, PreferenceUtil.DOUBLECOLOR); 
		doublecolorCb.setChecked(doublecolorB);
		happyB = PreferenceUtil.getBoolean(this, PreferenceUtil.HAPPY);
		happyCb.setChecked(happyB);
		if(!ifprizeB){
			ifprize_child.setVisibility(View.GONE);
		}else{
			ifprize_child.setVisibility(View.VISIBLE);
		}
	}
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.iv_back:
			this.finish();
			break;
		case R.id.prize_notice_iv:
			if(prizeB){
				prizeB = false;
			}else{
				prizeB = true;
			}
			PreferenceUtil.putBoolean(this, PreferenceUtil.PRIZE,prizeB);
			break;
		case R.id.followorder_notice_iv:
			if(followorderB){
				followorderB = false;
			}else{
				followorderB = true;
			}
			PreferenceUtil.putBoolean(this, PreferenceUtil.FOLLOWORDER,followorderB);
			break;
		case R.id.ifprize_notice_iv:
			if(ifprizeB){
				ifprizeB = false;
				ifprize_child.setVisibility(View.GONE);
				PreferenceUtil.putBoolean(this, PreferenceUtil.FOOTBALL,false);
				PreferenceUtil.putBoolean(this, PreferenceUtil.DOUBLECOLOR,false);
				PreferenceUtil.putBoolean(this, PreferenceUtil.HAPPY,false);
			}else{
				ifprizeB = true;
				ifprize_child.setVisibility(View.VISIBLE);
			}			
			PreferenceUtil.putBoolean(this, PreferenceUtil.IFPRIZE,ifprizeB);
			break;
		case R.id.football_notice_iv:
			if(footballB){
				footballB = false;
			}else{
				footballB = true;
			}
			PreferenceUtil.putBoolean(this, PreferenceUtil.FOOTBALL,footballB);
			break;
		case R.id.doublecolor_notice_iv:
			if(doublecolorB){
				doublecolorB = false;
			}else{
				doublecolorB = true;
			}
			PreferenceUtil.putBoolean(this, PreferenceUtil.DOUBLECOLOR,doublecolorB);
			break;
		case R.id.happy_notice_iv:
			if(happyB){
				happyB = false;
			}else{
				happyB = true;
			}
			PreferenceUtil.putBoolean(this, PreferenceUtil.HAPPY,happyB);
			break;
	
		default:
			break;
		}
	
	
	}
}
