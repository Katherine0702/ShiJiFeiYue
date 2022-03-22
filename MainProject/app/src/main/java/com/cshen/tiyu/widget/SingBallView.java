package com.cshen.tiyu.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

public class SingBallView extends TextView{
	int time;
	String textAfterAnim="D";
	boolean isStop = false;
     public String getTextAfterAnim() {
		return textAfterAnim;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	//玩法view
	public SingBallView(Context context) {
		super(context);
		// TODO 自动生成的构造函数存根
	}
	public SingBallView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}
	public void setTime(RotateAnimation rotate,long time){
		
		
	}
	@SuppressLint("ResourceAsColor")
	public void startRotateAnimation(){
		// 旋转动画
		RotateAnimation rotate = new RotateAnimation(0,2880,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
		rotate.setDuration(time);// 动画时间
		rotate.setFillAfter(true);// 保持动画状态
		rotate.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO 自动生成的方法存根
				//动画开始时设置背景图片
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO 自动生成的方法存根
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO 自动生成的方法存根
				SingBallView.this.setText(textAfterAnim);
			}
		   });
		this.startAnimation(rotate);
	}
	@SuppressLint("ResourceAsColor")
	public void startRotateAnimation(boolean isStop){
		// 旋转动画
		RotateAnimation rotate = new RotateAnimation(0, 1440,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
		rotate.setDuration(time);// 动画时间
		rotate.setFillAfter(true);// 保持动画状态
		rotate.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO 自动生成的方法存根
				//动画开始时设置背景图片
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO 自动生成的方法存根
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO 自动生成的方法存根
				SingBallView.this.setText(textAfterAnim);				
			}
		   });
		this.startAnimation(rotate);
	}
	public void setTextAfterAnim(String textAfterAnim) {
		this.textAfterAnim = textAfterAnim;
	}         
	public void setIsStop(boolean isStop) {
		this.isStop = isStop;
	}              	
	public void setSingBallColor(int color){
		this.setTextColor(color);
	}
	
}
