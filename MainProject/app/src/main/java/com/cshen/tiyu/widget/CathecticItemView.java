package com.cshen.tiyu.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cshen.tiyu.R;


//这个代表说有的账户详情条目通用
public class CathecticItemView extends RelativeLayout
{
	ImageView imageView=null;
	TextView tv_cathectic_ymd=null;
	TextView tv_cathectic_hms=null;
	TextView tv_cathectic_type=null;
	TextView tv_cathectic_money=null;
	TextView tv_cathectic_getmoney=null;
	
	public TextView getTv_cathectic_ymd() {
		return tv_cathectic_ymd;
	}



	public void setTv_cathectic_ymd(TextView tv_cathectic_ymd) {
		this.tv_cathectic_ymd = tv_cathectic_ymd;
	}



	public TextView getTv_cathectic_hms() {
		return tv_cathectic_hms;
	}



	public void setTv_cathectic_hms(TextView tv_cathectic_hms) {
		this.tv_cathectic_hms = tv_cathectic_hms;
	}



	public TextView getTv_cathectic_type() {
		return tv_cathectic_type;
	}



	public void setTv_cathectic_type(TextView tv_cathectic_type) {
		this.tv_cathectic_type = tv_cathectic_type;
	}



	public TextView getTv_cathectic_money() {
		return tv_cathectic_money;
	}



	public void setTv_cathectic_money(TextView tv_cathectic_money) {
		this.tv_cathectic_money = tv_cathectic_money;
	}

	String name;
	String value;
	
	
	private Drawable drawable;
	
	
	public CathecticItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
	
		
		
		init();
	}

	
	
	public CathecticItemView(Context context) {
		super(context);
		// TODO 自动生成的构造函数存根
		init();
	}



	@SuppressLint("NewApi")
	private void init() {
		// TODO 自动生成的方法存根
		
		LayoutInflater.from(getContext()).inflate(R.layout.cathectic_item_view, this);
		tv_cathectic_ymd=(TextView) findViewById(R.id.tv_cathectic_ymd);
		tv_cathectic_hms=(TextView) findViewById(R.id.tv_cathectic_hms);
		tv_cathectic_type=(TextView) findViewById(R.id.tv_cathectic_type);
		tv_cathectic_money=(TextView) findViewById(R.id.tv_cathectic_money);
		tv_cathectic_getmoney=(TextView) findViewById(R.id.tv_cathectic_getmoney);
		
	
		
	}
	//设置普通文本
   public void setNormalTextValue(String cathectic_ymd,String cathectic_hms,String cathectic_type){
		
		
		
	}
   
   public void setNormalTextValue(String cathectic_ymd,String cathectic_hms,String cathectic_type,String cathectic_money
	   ,String cathectic_getmoney){
	   tv_cathectic_ymd.setText(cathectic_ymd);
	   tv_cathectic_hms.setText(cathectic_hms);
	   tv_cathectic_type.setText(cathectic_type);
	   tv_cathectic_money.setText(cathectic_money);
	   if(cathectic_getmoney == null){
		   tv_cathectic_getmoney.setVisibility(View.GONE);
	   }else{
		   tv_cathectic_getmoney.setVisibility(View.VISIBLE);
		   tv_cathectic_getmoney.setText(cathectic_getmoney);
	   }
		
	}
   
	//设置特殊文本  文本和金额
	public void setSpecialTextValue(String tv_cathectic_type, String tv_cathectic_money){
		
		
		
		
	}



	public void setTextColor(String  isred) {
		// TODO 自动生成的方法存根
		if (isred.equals("OUT")) {
			tv_cathectic_money.setTextColor(Color.parseColor("#e63c42"));
		}else if (isred.equals("IN")) {
			tv_cathectic_money.setTextColor(getResources().getColor(R.color.green));
		} else {
			tv_cathectic_money.setTextColor(getResources().getColor(R.color.grey));
		}
		
	}
}
