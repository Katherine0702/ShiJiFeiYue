package com.cshen.tiyu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class MyListView  extends ListView{

	public MyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO 自动生成的构造函数存根
	}

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO 自动生成的构造函数存根
	}
	public MyListView(Context context) {
		super(context);
		// TODO 自动生成的构造函数存根
	}
	
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { 
        // TODO Auto-generated method stub 
        int expandSpec = MeasureSpec.makeMeasureSpec(  
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);  
         
        super.onMeasure(widthMeasureSpec, expandSpec); 
    } 
	
	
}
