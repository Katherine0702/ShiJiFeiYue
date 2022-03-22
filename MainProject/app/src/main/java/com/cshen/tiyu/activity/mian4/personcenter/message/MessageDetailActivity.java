package com.cshen.tiyu.activity.mian4.personcenter.message;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.main.Message;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;


public class MessageDetailActivity  extends Activity {
	MessageDetailActivity _this;
	TopViewLeft tv_head;
	TextView title,content,time;
	boolean needSave;
	String titleStr,timeStr,contentStr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_detail);
		_this = this;
		initView();
		initdata();
	}
	public void initView(){
		tv_head = (TopViewLeft) findViewById(R.id.title);
		tv_head.setResourceVisiable(true, false, false);
		tv_head.setTopClickItemListener(new TopClickItemListener() {
			@Override
			public void clickLoginView(View view) {}
			@Override
			public void clickContactView(View view) {}
			@Override
			public void clickBackImage(View view) {
				finish();
			}
		});
		title = (TextView) findViewById(R.id.titlestr);
		content   = (TextView) findViewById(R.id.content);
		time   = (TextView) findViewById(R.id.time);
	}
	void initdata() {
		needSave = getIntent().getBooleanExtra("needSave", false);
		titleStr = getIntent().getStringExtra("title");
		timeStr = getIntent().getStringExtra("time");
		contentStr = getIntent().getStringExtra("content");
		if(TextUtils.isEmpty(timeStr)){
			SimpleDateFormat  formatter = new SimpleDateFormat("yyyy-MM-dd    HH:mm:ss");       
			Date  curDate = new  Date(System.currentTimeMillis());//获取当前时间       
			timeStr =  formatter.format(curDate);  
		}
		
		title.setText(titleStr);
		content.setText(contentStr);
		time.setText(timeStr);
		if(PreferenceUtil.getBoolean(_this,"hasLogin")&&needSave){	
			Message mess = new Message();
			mess.setTitle(titleStr);
			mess.setTime(timeStr);
			mess.setContent(contentStr);
			MyDbUtils.saveMessageSigleData(mess);
		}
	}
}
