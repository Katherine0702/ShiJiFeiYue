package com.cshen.tiyu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cshen.tiyu.R;

public class RedPacketView extends RelativeLayout
{
//	ImageView imageBackView=null;//
	TextView tv_red_title=null;//标题
//	
    TextView tv_red_content=null;//红包类型
//	String name=null;
//	//String value=null;
//
//	ImageView iv_loginView=null ;//登录图标
//
//	TopClickItemListener topClickItemListener;
//	
	
//	public TopClickItemListener getTopClickItemListener() {
//		return topClickItemListener;
//	}
//
//
//
//	public void setTopClickItemListener(TopClickItemListener topClickItemListener) {
//		this.topClickItemListener = topClickItemListener;
//	}



	public RedPacketView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO 自动生成的构造函数存根
		
		

		init();
	}

	
	
	public RedPacketView(Context context) {
		super(context);
		// TODO 自动生成的构造函数存根
		init();
	}



	private void init() {
		// TODO 自动生成的方法存根
		
		LayoutInflater.from(getContext()).inflate(R.layout.redpacket_item_view, this);
//		tv_red_title=(TextView) findViewById(R.id.tv_red_title);
//		tv_red_title.setText("wxq123");
//		tv_red_content=(TextView) findViewById(R.id.tv_red_content);
//		tv_red_content.setText("买10减去5");
//		
//		
//		itemTitle=(TextView) findViewById(R.id.tv_head_title);
//		imageBackView=(ImageView)findViewById(R.id.iv_back);
//		contactTextView=(TextView)findViewById(R.id.tv_kefu);
//		iv_loginView=(ImageView)findViewById(R.id.iv_login_setting);
//		
//		imageBackView.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO 自动生成的方法存根
//				
//				topClickItemListener.clickBackImage(v);
//				
//			}
//		});
//		
//		
//		contactTextView.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO 自动生成的方法存根
//				
//				topClickItemListener.clickContactView(v);
//				
//			}
//		});
//		
//		
//		iv_loginView.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO 自动生成的方法存根
//				
//				topClickItemListener.clickLoginView(v);
//				
//			}
//		});
//		
//		
//		
//		if(name!=null){
//		itemTitle.setText(name);
//			
//		}
		
		
		
		
	}
	
	
  
	public void setImageVisiable(){
		
	//	imageBackView.setVisibility(View.VISIBLE);
	}
	
	//设置资源是否可见
	public void setResourceVisiable(boolean imageBackViewVisible,boolean loginImageVisible,boolean contactVisiable){
//		
//		if(imageBackViewVisible){
//			imageBackView.setVisibility(View.VISIBLE);
//		}
//      if(loginImageVisible){
//    	  iv_loginView.setVisibility(View.VISIBLE);
//    	
//		}
//     if(contactVisiable){
//	
//    	 contactTextView.setVisibility(View.VISIBLE);
//    	 
//    }
//		
//		
		
	}
	
	
	public interface TopClickItemListener{
		//有高级调用者去实现点击后该干什么
		public void clickBackImage(View view);
		public void clickContactView(View view);
		public void clickLoginView(View view);
		
		
		
		
	}
	
	
	
	
}
