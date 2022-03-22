package com.cshen.tiyu.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cshen.tiyu.R;

public class PersonalItemView extends RelativeLayout {
	ImageView imageView = null;
	ImageView imageIn = null;
	TextView itemTitle = null;
	TextView itemValue = null;

	String name;
	String value;
	private Drawable drawable;

	public PersonalItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO 自动生成的构造函数存根

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.PersonalItemView);
		name = a.getString(R.styleable.PersonalItemView_pname);

		value = a.getString(R.styleable.PersonalItemView_pvalue);

		drawable = a.getDrawable(R.styleable.PersonalItemView_src);

		a.recycle();
		// this.setBackgroundResource(R.drawable.list_selector);

		init();
	}

	public PersonalItemView(Context context) {
		super(context);
		// TODO 自动生成的构造函数存根
		init();
	}

	@SuppressLint("NewApi")
	private void init() {
		// TODO 自动生成的方法存根

		LayoutInflater.from(getContext()).inflate(R.layout.personal_item_view,
				this);
		this.setBackgroundResource(R.drawable.list_selector);
		imageView = (ImageView) findViewById(R.id.iv_item_icon);
		itemTitle = (TextView) findViewById(R.id.tv_item_title);
		itemValue = (TextView) findViewById(R.id.tv_item_value);
		imageIn = (ImageView) findViewById(R.id.ImageView01);

		// imageView.setBackgroundResource(drawable);
		itemTitle.setText(name);
		itemValue.setText(value);
		imageView.setImageDrawable(drawable); // 注意吧 ！！
	}

	public void setResource(int drawable, String titleString) {
		//
		imageView.setBackgroundResource(drawable);
		itemTitle.setText(titleString);

	}

	public void setInfoValue(String value, int color) {

		itemValue.setText(value);
		itemValue.setTextColor(color);

	}
	public String getValue() {
		return itemValue.getText().toString();
	}
}
