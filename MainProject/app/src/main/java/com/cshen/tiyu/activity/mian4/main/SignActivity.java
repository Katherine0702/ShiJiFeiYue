package com.cshen.tiyu.activity.mian4.main;

import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.activity.ActivityEach;
import com.cshen.tiyu.domain.sign.SignData;
import com.cshen.tiyu.net.https.ServiceSign;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;

public class SignActivity extends BaseActivity{
	private SignActivity _this;
	private TopViewLeft tv_head;//头
	private ImageView sign;
	private ImageView zhong1,zhong2,zhong3,zhong4,zhong5,zhong6,zhong7;
	private TextView zhong2line,zhong3line,zhong4line,zhong5line,zhong6line,zhong7line;
	private TextView zhong1txt,zhong2txt,zhong3txt,zhong4txt,zhong5txt,zhong6txt,zhong7txt;
	private TextView caozuo1,caozuo2,caozuo5;
	private SignData data;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign);
		_this = this;
		initView();
		initdata(); 
	}
	public void initView(){
		tv_head = (TopViewLeft) findViewById(R.id.head);
		tv_head.setResourceVisiable(true, false, false);
		tv_head.setTopClickItemListener(new TopClickItemListener() {
			@Override
			public void clickLoginView(View view) {}
			@Override
			public void clickContactView(View view) {}
			@Override
			public void clickBackImage(View view) {
				finish();
			}
		});
		
		sign = (ImageView) findViewById(R.id.signim);
		sign.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(data!=null&&data.isSignIn()){
					ToastUtils.showShort(_this, "您已签到过");
				}else{
					toSign();
				}
			}
		});
		
		zhong1 = (ImageView) findViewById(R.id.zhong1);
		zhong2 = (ImageView) findViewById(R.id.zhong2);
		zhong3 = (ImageView) findViewById(R.id.zhong3);
		zhong4 = (ImageView) findViewById(R.id.zhong4);
		zhong5 = (ImageView) findViewById(R.id.zhong5);
		zhong6 = (ImageView) findViewById(R.id.zhong6);
		zhong7 = (ImageView) findViewById(R.id.zhong7);

		zhong2line = (TextView) findViewById(R.id.zhong2line);
		zhong3line = (TextView) findViewById(R.id.zhong3line);
		zhong4line = (TextView) findViewById(R.id.zhong4line);
		zhong5line = (TextView) findViewById(R.id.zhong5line);
		zhong6line = (TextView) findViewById(R.id.zhong6line);
		zhong7line = (TextView) findViewById(R.id.zhong7line);

		zhong1txt = (TextView) findViewById(R.id.zhong1txt);
		zhong2txt = (TextView) findViewById(R.id.zhong2txt);
		zhong3txt = (TextView) findViewById(R.id.zhong3txt);
		zhong4txt = (TextView) findViewById(R.id.zhong4txt);
		zhong5txt = (TextView) findViewById(R.id.zhong5txt);
		zhong6txt = (TextView) findViewById(R.id.zhong6txt);
		zhong7txt = (TextView) findViewById(R.id.zhong7txt);

		caozuo1 = (TextView) findViewById(R.id.caozuo1);
		caozuo2 = (TextView) findViewById(R.id.caozuo2);
		caozuo5 = (TextView) findViewById(R.id.caozuo5);
	}
	private void initdata() {
		ServiceSign.getInstance().getSignData(_this, new CallBack<SignData>() {

			@Override
			public void onSuccess(SignData t) {
				// TODO 自动生成的方法存根	
				if(t!=null){
					data = t;
					setData();
				}
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO 自动生成的方法存根				
				ToastUtils.showShort(_this,errorMessage.msg+"");				
			}
		});
	}
	public void setData(){
		if(data.isSignIn()){
			sign.setImageResource(R.mipmap.signedicon);
		}else{
			sign.setImageResource(R.mipmap.sign);
		}
		if(data.isMon()){
			zhong1.setBackgroundResource(R.mipmap.signed);
			zhong1txt.setTextColor(Color.parseColor("#fff0ab"));
		}else{
			zhong1.setBackgroundResource(R.mipmap.signuned);
			zhong1txt.setTextColor(Color.parseColor("#ffffff"));
		}
		if(data.isTue()){
			zhong2.setBackgroundResource(R.mipmap.signed);
			zhong2txt.setTextColor(Color.parseColor("#fff0ab"));
		}else{
			zhong2.setBackgroundResource(R.mipmap.signuned);
			zhong2txt.setTextColor(Color.parseColor("#ffffff"));
		}
		
		if(data.isMon()&&data.isTue()){
			zhong2line.setBackgroundColor(Color.parseColor("#fff0ab"));
		}else{
			zhong2line.setBackgroundColor(Color.parseColor("#ffffff"));
		}
		
		
		if(data.isWed()){
			zhong3.setBackgroundResource(R.mipmap.signed);
			zhong3txt.setTextColor(Color.parseColor("#fff0ab"));
		}else{
			zhong3.setBackgroundResource(R.mipmap.signuned);
			zhong3txt.setTextColor(Color.parseColor("#ffffff"));
		}
		
		if(data.isWed()&&data.isTue()){
			zhong3line.setBackgroundColor(Color.parseColor("#fff0ab"));
		}else{
			zhong3line.setBackgroundColor(Color.parseColor("#ffffff"));
		}
		
		
		if(data.isThu()){
			zhong4.setBackgroundResource(R.mipmap.signed);
			zhong4txt.setTextColor(Color.parseColor("#fff0ab"));
		}else{
			zhong4.setBackgroundResource(R.mipmap.signuned);
			zhong4txt.setTextColor(Color.parseColor("#ffffff"));
		}
		
		if(data.isWed()&&data.isThu()){
			zhong4line.setBackgroundColor(Color.parseColor("#fff0ab"));
		}else{
			zhong4line.setBackgroundColor(Color.parseColor("#ffffff"));
		}
		
		
		
		if(data.isFri()){
			zhong5.setBackgroundResource(R.mipmap.signed);
			zhong5txt.setTextColor(Color.parseColor("#fff0ab"));
		}else{
			zhong5.setBackgroundResource(R.mipmap.signuned);
			zhong5txt.setTextColor(Color.parseColor("#ffffff"));
		}
		
		if(data.isFri()&&data.isThu()){
			zhong5line.setBackgroundColor(Color.parseColor("#fff0ab"));
		}else{
			zhong5line.setBackgroundColor(Color.parseColor("#ffffff"));
		}
		
		if(data.isSat()){
			zhong6.setBackgroundResource(R.mipmap.signed);
			zhong6txt.setTextColor(Color.parseColor("#fff0ab"));
		}else{
			zhong6.setBackgroundResource(R.mipmap.signuned);
			zhong6txt.setTextColor(Color.parseColor("#ffffff"));
		}
		
		if(data.isFri()&&data.isSat()){
			zhong6line.setBackgroundColor(Color.parseColor("#fff0ab"));
		}else{
			zhong6line.setBackgroundColor(Color.parseColor("#ffffff"));
		}
		
		if(data.isSun()){
			zhong7.setBackgroundResource(R.mipmap.signed);
			zhong7txt.setTextColor(Color.parseColor("#fff0ab"));
		}else{
			zhong7.setBackgroundResource(R.mipmap.signuned);
			zhong7txt.setTextColor(Color.parseColor("#ffffff"));
		}
		
		if(data.isSun()&&data.isSat()){
			zhong7line.setBackgroundColor(Color.parseColor("#fff0ab"));
		}else{
			zhong7line.setBackgroundColor(Color.parseColor("#ffffff"));
		}
		
		
		if(data.isOne()){
			caozuo1.setText("已领取");
		}else{
			caozuo1.setText("未领取");
		}
		if(data.isThree()){
			caozuo2.setText("已领取");
		}else{
			caozuo2.setText("未领取");
		}
		if(data.isSeven()){
			caozuo5.setText("已领取");
		}else{
			caozuo5.setText("未领取");
		}
	}
	private void toSign() {
		ServiceSign.getInstance().toSign(_this, new CallBack<SignData>() {

			@Override
			public void onSuccess(SignData t) {
				// TODO 自动生成的方法存根	
				if(t!=null){
					data = t;
					setData();
				}
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO 自动生成的方法存根				
				ToastUtils.showShort(_this,errorMessage.msg+"");				
			}
		});
	}
}
