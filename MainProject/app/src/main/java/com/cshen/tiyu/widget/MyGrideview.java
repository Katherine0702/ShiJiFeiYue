package com.cshen.tiyu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

public class MyGrideview extends GridView  {

	int startX;
	int startY;
	public MyGrideview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO 自动生成的构造函数存根
	}

	public MyGrideview(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO 自动生成的构造函数存根
	}
	public MyGrideview(Context context) {
		super(context);
		// TODO 自动生成的构造函数存根
	}
	
	
	//重新测量其大小
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO 自动生成的方法存根
		
		  int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
          super.onMeasure(widthMeasureSpec, expandSpec);
		//super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	
	
	
	//bu不处理触摸事件只用来动态显示数目 不然grideview会消耗事件
//	@Override
//	public boolean onTouchEvent(MotionEvent ev) {
//		// TODO 自动生成的方法存根
//		
//		if(ev.getAction()==MotionEvent.ACTION_MOVE){
//			
//			return false;
//		}else{
//			return true;
//		}
//		
//	}
      @Override
    public boolean onTouchEvent(MotionEvent ev) {
    	// TODO 自动生成的方法存根
    	return super.onTouchEvent(ev);
    }
      
      
      
   
      
      @Override
  	public boolean dispatchTouchEvent(MotionEvent ev) {
  		switch (ev.getAction()) {
  		case MotionEvent.ACTION_DOWN:
  			getParent().requestDisallowInterceptTouchEvent(true);// 不要拦截,
  																	// 这样是为了保证ACTION_MOVE调用
  			startX = (int) ev.getRawX();
  			startY = (int) ev.getRawY();
  			break;
  		case MotionEvent.ACTION_MOVE:

  			int endX = (int) ev.getRawX();
  			int endY = (int) ev.getRawY();

  			if (Math.abs(endX - startX) > Math.abs(endY - startY)) {
  				//左右滑动
  				
  			} else {// 上下滑动
  				getParent().requestDisallowInterceptTouchEvent(false);
  			}

  			break;

  		default:
  			break;
  		}

  		return super.dispatchTouchEvent(ev);
  	}
      
      
      
}
