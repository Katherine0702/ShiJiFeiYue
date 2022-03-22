package com.cshen.tiyu.activity.lottery.cai115;

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


public class SDEL11to5PeriodDetailActivity extends BaseActivity {
	private TextView textview_periodNumber, textview_time, btn_go;
	private TextView textview_result_sdel11to501;
	private TextView textview_result_sdel11to502;
	private TextView textview_result_sdel11to503;
	private TextView textview_result_sdel11to504;
	private TextView textview_result_sdel11to505;
	private String lottery;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_period_detail_sdel11to5);
		Intent intent = getIntent();
		lottery = intent.getStringExtra("lottery");
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
			textview_result_sdel11to501.setText(Util
					.periodNumberFromat(resultNumber[0]));
			textview_result_sdel11to502.setText(Util
					.periodNumberFromat(resultNumber[1]));
			textview_result_sdel11to503.setText(Util
					.periodNumberFromat(resultNumber[2]));
			textview_result_sdel11to504.setText(Util
					.periodNumberFromat(resultNumber[3]));
			textview_result_sdel11to505.setText(Util
					.periodNumberFromat(resultNumber[4]));
		}
		btn_go.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = null;
				if ("14".equals(lottery)) {
					intent = new Intent(SDEL11to5PeriodDetailActivity.this,
							Main115Activity.class);
				}
				if ("19".equals(lottery)) {
					intent = new Intent(SDEL11to5PeriodDetailActivity.this,
							MainGd115Activity.class);
				}

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
		textview_result_sdel11to501 = (TextView) findViewById(R.id.textview_result_sdel11to501);
		textview_result_sdel11to502 = (TextView) findViewById(R.id.textview_result_sdel11to502);
		textview_result_sdel11to503 = (TextView) findViewById(R.id.textview_result_sdel11to503);
		textview_result_sdel11to504 = (TextView) findViewById(R.id.textview_result_sdel11to504);
		textview_result_sdel11to505 = (TextView) findViewById(R.id.textview_result_sdel11to505);
	}

	private void changHead() {
		// TODO 自动生成的方法存根
		TextView tvTextView = (TextView) findViewById(R.id.tv_head_title);
		tvTextView.setText("山东11选5");
		if ("14".equals(lottery)) {
			tvTextView.setText("山东11选5");
			((TextView) findViewById(R.id.textview_lotteryName_sdel11to5)).setText("山东11选5");
		}
		if ("19".equals(lottery)) {
			tvTextView.setText("广东11选5");
			((TextView) findViewById(R.id.textview_lotteryName_sdel11to5)).setText("广东11选5");

		}
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
