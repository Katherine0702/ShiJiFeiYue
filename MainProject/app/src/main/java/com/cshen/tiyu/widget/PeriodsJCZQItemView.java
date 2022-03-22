package com.cshen.tiyu.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cshen.tiyu.R;



public class PeriodsJCZQItemView extends LinearLayout {
	View view_top;
	View view_bottom;
	LinearLayout ll;
	TextView tv;
	TextView tv_number;
	ImageView img ;
	int temp=0,temp02=0;
	String periodName = null;
	String periodNumber = null;
	OnPeriodJczqClickListener onPeriodJczqClickListener =null;

	public PeriodsJCZQItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO 自动生成的构造函数存根

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.periodsJczqItem);
		periodName = a.getString(R.styleable.periodsJczqItem_periodName);
		periodNumber = a.getString(R.styleable.periodsJczqItem_periodNumber);
		a.recycle();
		init();
	}

	public PeriodsJCZQItemView(Context context) {
		super(context);
		// TODO 自动生成的构造函数存根
		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(
				R.layout.activity_periods_jczq_item, this);
//		this.setBackgroundResource(R.drawable.list_selector);

		
		view_top = findViewById(R.id.view_top);
		view_bottom = findViewById(R.id.view_bottom);
		ll = (LinearLayout) findViewById(R.id.ll);
		tv = (TextView) findViewById(R.id.tv);
		tv_number = (TextView) findViewById(R.id.tv_number);
		img = (ImageView) findViewById(R.id.img);
		RotateAnimation animation =new RotateAnimation(0f,270f,Animation.RELATIVE_TO_SELF, 
				0.5f,Animation.RELATIVE_TO_SELF,0.5f); 
//		animation.setDuration(500);//设置动画持续时间 
		animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态 
		img.startAnimation(animation);
		if (periodName != null) {
			tv.setText(periodName);
		}
		
		if (periodNumber != null) {
			tv_number.setText(periodNumber);
		}
		
		
		ll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (temp == 0) {
					img.startAnimation(upDown(270f,450f));
//					img.startAnimation(upDown( 90f,270f));
					temp02 = 1;
				}
				if (temp == 1) {
//					img.startAnimation(upDown(270f,450f));
					img.startAnimation(upDown( 90f,270f));
					temp02 = 0;
				}
				temp =temp02;
				if (onPeriodJczqClickListener !=null) {
					onPeriodJczqClickListener.onPeriodJczqClick();
				}
				
			}
		});
	}

	
	protected Animation upDown(float from, float to) {
		RotateAnimation animation =new RotateAnimation(from,to,Animation.RELATIVE_TO_SELF, 
				0.5f,Animation.RELATIVE_TO_SELF,0.5f); 
		animation.setDuration(500);//设置动画持续时间 
		animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态 
		return animation;
	}

	public void setTitle(String periodName) {
		tv.setText(periodName);
	}
	
	public String getTitle() {
		if (tv.getText() != null) {
			return tv.getText().toString();
		}else{
			return "";
		}
	}
	
	
	public void setPeriodNumber(String periodNumber) {
		tv_number.setText(periodNumber);
	}

	public String getPeriodNumber() {
		if (tv_number.getText() != null) {
			return tv_number.getText().toString();
		}else{
			return "";
		}
	}


	public void setOnPeriodJczqClickListener(
			OnPeriodJczqClickListener onPeriodJczqClickListener) {
		this.onPeriodJczqClickListener = onPeriodJczqClickListener;
	}

	public void setTitleHtmlValue(String periodName) {
		tv.setText(Html.fromHtml(periodName));
	}
	
	public interface OnPeriodJczqClickListener{
		public void onPeriodJczqClick();
	}

	
	public  void setViewTopVisibilable(int i){
		view_top.setVisibility(i);
	}
	
	public void setViewBottomVisibilable(int i){
		view_bottom.setVisibility(i);
	}
	

}
