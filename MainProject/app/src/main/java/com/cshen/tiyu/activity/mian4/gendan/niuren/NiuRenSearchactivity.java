package com.cshen.tiyu.activity.mian4.gendan.niuren;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.base.BaseActivity;

public class NiuRenSearchactivity extends BaseActivity implements View.OnClickListener{
	NiuRenSearchactivity _this;
	EditText searchet;
	ImageView clear;
	TextView sure,cancel;
	String from;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_this = this;
		from = getIntent().getStringExtra("from");
		setContentView(R.layout.gendan_mainsearch);
		initView();
	}
	public void initView(){
		searchet = (EditText) findViewById(R.id.searchet);
		if("bangdan".equals(from)){
			searchet.setHint("输入方案发起人名字");
		}else{
			searchet.setHint("输入牛人名字");
		}
		clear = (ImageView) findViewById(R.id.clear);
		clear.setOnClickListener(this);
		sure = (TextView) findViewById(R.id.sure);
		sure.setOnClickListener(this);
		cancel = (TextView) findViewById(R.id.cancel);
		cancel.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.clear:
			searchet.setText("");
			break;
		case R.id.sure:
			Intent renzheng = new Intent(_this,NiuRenListActivity.class);
			renzheng.putExtra("from", from);
			renzheng.putExtra("keyword", searchet.getText().toString());
			renzheng.putExtra("isSearch",true);
			startActivity(renzheng);
			break;
		case R.id.cancel:
			_this.finish();
			break;
		}
	}

}
