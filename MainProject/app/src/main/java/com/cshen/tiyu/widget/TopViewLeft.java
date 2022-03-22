package com.cshen.tiyu.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import com.cshen.tiyu.R;

public class TopViewLeft extends RelativeLayout
{
	ImageView imageBackView=null;//返回键
	TextView itemTitle=null;//标题

	TextView contactTextView=null;//联系客服
	String name=null;
	//String value=null;

	ImageView iv_loginView=null ;//登录图标

	TopClickItemListener topClickItemListener;


	public TopClickItemListener getTopClickItemListener() {
		return topClickItemListener;
	}



	public void setTopClickItemListener(TopClickItemListener topClickItemListener) {
		this.topClickItemListener = topClickItemListener;
	}



	public TopViewLeft(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO 自动生成的构造函数存根


		TypedArray a=context.obtainStyledAttributes(attrs, R.styleable.topview);
		name = a.getString(R.styleable.topview_topname);




		a.recycle();
		//	this.setBackgroundResource(R.drawable.list_selector);
		init();
	}



	public TopViewLeft(Context context) {
		super(context);
		// TODO 自动生成的构造函数存根
		init();
	}


	private void init() {
		// TODO 自动生成的方法存根

		LayoutInflater.from(getContext()).inflate(R.layout.topleft, this);
		this.setBackgroundResource(R.drawable.list_selector);

		itemTitle=(TextView) findViewById(R.id.tv_head_title);
		imageBackView=(ImageView)findViewById(R.id.iv_back);
		contactTextView=(TextView)findViewById(R.id.tv_forgetpw);
		iv_loginView=(ImageView)findViewById(R.id.iv_login_setting);

		imageBackView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根

				topClickItemListener.clickBackImage(v);

			}
		});


		contactTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根

				topClickItemListener.clickContactView(v);

			}
		});


		iv_loginView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根

				topClickItemListener.clickLoginView(v);

			}
		});



		if(name!=null){
			itemTitle.setText(name);

		}




	}



	public void setTitle(String title){

		itemTitle.setText(title);

	}

	public void setImageVisiable(){

		imageBackView.setVisibility(View.VISIBLE);
	}
	public void contactTextView(String id){

		contactTextView.setText(id);

	}
	@SuppressLint("NewApi")
	public void contactTextViewBackground(){
		contactTextView.setBackground(null);

	}
	public void contactTextViewSize(){

		contactTextView.setTextSize(16);

	}
	public void setImage(int id){

		imageBackView.setImageResource(id);

	}public void setImageRight(int id){
		iv_loginView.setImageResource(id);

	}
	//设置资源是否可见
	public void setResourceVisiable(boolean imageBackViewVisible,boolean loginImageVisible,boolean contactVisiable){

		if(imageBackViewVisible){
			imageBackView.setVisibility(View.VISIBLE);
		}
		if(loginImageVisible){
			iv_loginView.setVisibility(View.VISIBLE);

		}
		if(contactVisiable){
			contactTextView.setVisibility(View.VISIBLE);
		}else {
			contactTextView.setVisibility(View.INVISIBLE);
		}
	}

	public interface TopClickItemListener{
		//有高级调用者去实现点击后该干什么
		public void clickBackImage(View view);
		public void clickContactView(View view);
		public void clickLoginView(View view);

	}
}
