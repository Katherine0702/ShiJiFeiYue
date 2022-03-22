package com.cshen.tiyu.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cshen.tiyu.R;

public class SafeSettingItemView extends RelativeLayout {
	ImageView imageView = null;
	TextView itemTitle = null;
	TextView itemTitle2 = null;
	TextView itemValue = null;
	String name = null;
	String name2 = null;
	String value = null;

	public SafeSettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO 自动生成的构造函数存根

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.safesettingview);
		name = a.getString(R.styleable.safesettingview_name);

		name2 = a.getString(R.styleable.safesettingview_name2);
		
		value = a.getString(R.styleable.safesettingview_value);

		a.recycle();
		init();
	}

	public SafeSettingItemView(Context context) {
		super(context);
		// TODO 自动生成的构造函数存根
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根

		LayoutInflater.from(getContext()).inflate(
				R.layout.safe_setting_item_view, this);
		this.setBackgroundResource(R.drawable.list_selector);

		imageView=(ImageView) findViewById(R.id.iv_item_icon);
		itemTitle = (TextView) findViewById(R.id.tv_item_title);
		itemTitle2 = (TextView) findViewById(R.id.tv_item_title2);
		itemValue = (TextView) findViewById(R.id.tv_item_value);
		if (name != null) {
			itemTitle.setText(name);

		}
		if (name2 != null) {
			itemTitle2.setText(name2);
		}
		if (value != null) {
			itemValue.setText(value);
		}

		this.setBackgroundResource(R.drawable.list_selector);

	}
	
	public void setResource(boolean visible, String titleString) {
		if (visible) {
			//imageView.setVisibility(View.VISIBLE);
			imageView.setVisibility(View.GONE);
		} else {
			imageView.setVisibility(View.GONE);
		}
		if (titleString != null) {
			itemValue.setText(titleString);
		}
	}

	public void setRightItemVisible(boolean visible) {
		if (visible) {
			imageView.setVisibility(View.VISIBLE);
		} else {
			itemValue.setVisibility(View.INVISIBLE);
		}
	}
	public void setTitle(String name) {
		itemTitle.setText(name);
	}
	
	
	
	public void setTitleHtmlValue(String name) {
		itemTitle.setText(Html.fromHtml(name));
	}
	
	
	public String getValue() {
		if(itemValue!=null&&itemValue.getText()!=null){
			return itemValue.getText().toString();
		}
		return "";
	}

	public void setTextValue(String value) {

	}
    
	public void setTitle2TextColor(int color){
		itemTitle2.setTextColor(color);
	}
	public void setTitle2Text(String tv){
		if (tv==null) {
			itemTitle2.setText("");
			return;
		}
		itemTitle2.setText(tv);
	}
	
	public void setTitle2Visibility(boolean isVisib){
		if (isVisib) {
			itemTitle2.setVisibility(View.VISIBLE);
		}else {
			itemTitle2.setVisibility(View.GONE);
		}
	}
	
	public void setItemValueTextColor(boolean isSetting){
		if (isSetting) {
			itemValue.setTextColor(getResources().getColor(R.color.black));
		}else {
			itemValue.setTextColor(getResources().getColor(R.color.gray));
		}
	}

}
