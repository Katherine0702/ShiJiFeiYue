package com.cshen.tiyu.widget;

import com.cshen.tiyu.R;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;	
import android.widget.PopupWindow;


/**
 * 一般的ppw 屏幕中间显示，显示有500ms延时
 * @author Administrator
 *
 */
public class CommonPPW extends PopupWindow{

	public  CommonPPW(final Activity context,View ppwLayout){

		this.setContentView(ppwLayout);
		this.setWidth(LinearLayoutCompat.LayoutParams.MATCH_PARENT);
		this.setHeight(LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
		this.setBackgroundDrawable(new BitmapDrawable());
		// 因为某些机型是虚拟按键的,所以要加上以下设置防止挡住按键
		this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		this.setTouchable(true); // 设置popupwindow可点击
		this.setOutsideTouchable(true); // 设置popupwindow外部可点击
		this.setFocusable(true); // 获取焦点
		this.setAnimationStyle(R.style.bonuce_anim_style);
		setWindowAlpha(context, 0.4f);
		this.setTouchInterceptor(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// 如果点击了popupwindow的外部，popupwindow也会消失
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					dismiss();
					return true;
				}
				return false;
			}
		});
		this.setOnDismissListener(new PopupWindow.OnDismissListener() {

			// 在dismiss中恢复透明度
			public void onDismiss() {
				setWindowAlpha(context,1f);
			}
		});

	}

	/** 
	 * 显示popupWindow 
	 *  
	 * @param parent 
	 */  
	public void showPopupWindow(final View anchor) {  
		if (!this.isShowing()) {  
			//屏幕中间显示
			new Handler().postDelayed(new Runnable() {//这样做是为了解决有时候activity视图未加载完成是报异常或动画不显示
				@Override
				public void run() {					
					showAtLocation(anchor, Gravity.CENTER, 0, 0);
					update();
				}
			}, 400);

		} else {  
			this.dismiss();  
		}  
	}  

	/** 
	 * 关闭popupWindow 
	 *  
	 * @param parent 
	 */  
	public void dissmissPopupWindow() {  
		if (this != null && this.isShowing()) {  
			this.dismiss();
		} 
	}  


	public  void setWindowAlpha(Activity context,float alpha){
		if (context==null) {
			return;
		}
		WindowManager.LayoutParams lp = context.getWindow()
				.getAttributes();
		lp.alpha = alpha;
		context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		context.getWindow().setAttributes(lp);
	}

}
