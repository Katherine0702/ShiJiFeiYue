package com.cshen.tiyu.activity.lottery.Fast3;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.utils.Util;


public class JXK3PeriodDetailActivity extends BaseActivity {
	private TextView textview_periodNumber, textview_time, btn_go;
	private TextView textview_result_jxk301;
	private TextView textview_result_jxk302;
	private TextView textview_result_jxk303;
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_period_detail_jxk3);
		Intent intent = getIntent();
		changHead();
		initView();
		String periodNumber = intent.getStringExtra("periodNumber");
		String prizeTime = intent.getStringExtra("prizeTime");
		String result = intent.getStringExtra("result");
		if (!TextUtils.isEmpty(periodNumber)) {
			textview_periodNumber.setText("第"
					+ periodNumber.substring(periodNumber.length() - 2,
							periodNumber.length()) + "期");
		}
		if (!TextUtils.isEmpty(prizeTime)) {
			textview_time.setText(prizeTime.substring(0, 16));
		}
		if (result != null) {
			String[] resultNumber = result.split(",");
			textview_result_jxk301.setText(Util
					.periodNumberFromat(resultNumber[0]));
			textview_result_jxk302.setText(Util
					.periodNumberFromat(resultNumber[1]));
			textview_result_jxk303.setText(Util
					.periodNumberFromat(resultNumber[2]));
		}
		btn_go.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(JXK3PeriodDetailActivity.this,
						Fast3MainActivity.class);

				if (null != intent) {
					startActivity(intent);
					finish();
				}
			}
		});

	}

	private void initView() {

		textview_periodNumber = (TextView) findViewById(R.id.textview_periodNumber);
		textview_time = (TextView) findViewById(R.id.textview_time);
		btn_go = (TextView) findViewById(R.id.btn_go);
		textview_result_jxk301 = (TextView) findViewById(R.id.textview_result_jxk301);
		textview_result_jxk302 = (TextView) findViewById(R.id.textview_result_jxk302);
		textview_result_jxk303 = (TextView) findViewById(R.id.textview_result_jxk303);
	}

	private void changHead() {
		// TODO 自动生成的方法存根
		((TextView)findViewById(R.id.tv_head_title)).setText("江西快3");
		ImageView backImageView = (ImageView) findViewById(R.id.iv_back);
		backImageView.setVisibility(View.VISIBLE);

		backImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}
}
