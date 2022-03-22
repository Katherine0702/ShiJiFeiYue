package com.cshen.tiyu.activity.lottery.dlt;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.dltssq.DltPeriodData;
import com.cshen.tiyu.net.https.ServiceCaiZhongInformation;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.utils.PostHttpInfoUtils;
import com.cshen.tiyu.utils.Util;


public class DLTPeriodDetailActivity extends BaseActivity {
	private DLTPeriodDetailActivity _this;
	private TextView textview_periodNumber, textview_time, btn_go;
	private TextView textview_result_dlt01, textview_result_dlt02,
			textview_result_dlt03, textview_result_dlt04,
			textview_result_dlt05, textview_result_dlt06,
			textview_result_dlt07;
	private TextView tv_sales_money, tv_pond_money;
	private TextView tv_money_01, tv_units_01;
	private TextView tv_money_02, tv_units_02;
	private TextView tv_money_03, tv_units_03;
	private TextView tv_money_04, tv_units_04;
	private TextView tv_money_05, tv_units_05;
	private TextView tv_money_base01, tv_units_base01;
	private TextView tv_money_base02, tv_units_base02;
	private TextView tv_money_base03, tv_units_base03;
	private TextView tv_money_base04, tv_units_base04;
	private TextView tv_money_base05, tv_units_base05;
	private TextView tv_money_base06, tv_units_base06;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_period_detail_dlt);
		_this = this;
		Intent intent = getIntent();
		changHead();
		initView();
		String periodNumber = intent.getStringExtra("periodNumber");
		String prizeTime = intent.getStringExtra("prizeTime");
		String result = intent.getStringExtra("result");
		String periodId = intent.getStringExtra("periodId");

		if (!TextUtils.isEmpty(periodNumber)) {
			textview_periodNumber.setText("第" + periodNumber + "期");
		}
		if (!TextUtils.isEmpty(prizeTime)) {
			textview_time.setText(prizeTime.substring(0, 16));
		}
		if (result != null) {
			String[] resultNumber = result.split(",");
			textview_result_dlt01.setText(Util
					.periodNumberFromat(resultNumber[0]));
			textview_result_dlt02.setText(Util
					.periodNumberFromat(resultNumber[1]));
			textview_result_dlt03.setText(Util
					.periodNumberFromat(resultNumber[2]));
			textview_result_dlt04.setText(Util
					.periodNumberFromat(resultNumber[3]));
			textview_result_dlt05.setText(Util
					.periodNumberFromat(resultNumber[4]));
			textview_result_dlt06.setText(Util
					.periodNumberFromat(resultNumber[5]));
			textview_result_dlt07.setText(Util
					.periodNumberFromat(resultNumber[6]));
		}
		btn_go.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DLTPeriodDetailActivity.this,
						DLTMainActivity.class);
				startActivity(intent);
				finish();

			}
		});

		httpInfo(periodId);

	}

	private void httpInfo(String periodId) {
		ServiceCaiZhongInformation.getInstance().getDLT_period_detail(_this, periodId,
				new CallBack<DltPeriodData>() {
					@Override
					public void onFailure(ErrorMsg errorMessage) {
						PostHttpInfoUtils.doPostFail(_this, errorMessage,"网络问题");
					}
					@Override
					public void onSuccess(DltPeriodData t) {
						if (t != null) {
							tv_sales_money.setText(Util.getDollarFormat(t.getTotalSales()));
							tv_pond_money.setText(Util.getDollarFormat(t.getPrizePool()));
							tv_money_01.setText(Util.getDollarFormat(t.getFirstAddPrize()));
							tv_units_01.setText(t.getFirstAddWinUnits() + "注");
							tv_money_02.setText(Util.getDollarFormat(t.getSecondAddPrize()));
							tv_units_02.setText(t.getSecondAddWinUnits() + "注");
							tv_money_03.setText(Util.getDollarFormat(t.getThirdAddPrize()));
							tv_units_03.setText(t.getThirdAddWinUnits() + "注");
							tv_money_04.setText(Util.getDollarFormat(t.getFourthAddPrize()));
							tv_units_04.setText(t.getFourthAddWinUnits() + "注");
							tv_money_05.setText(Util.getDollarFormat(t.getFifthAddPrize()));
							tv_units_05.setText(t.getFifthAddWinUnits() + "注");
							tv_money_base01.setText(Util.getDollarFormat(t.getFirstPrize()));
							tv_units_base01.setText(t.getFirstWinUnits() + "注");
							tv_money_base02.setText(Util.getDollarFormat(t.getSecondPrize()));
							tv_units_base02.setText(t.getSecondWinUnits() + "注");
							tv_money_base03.setText(Util.getDollarFormat(t.getThirdPrize()));
							tv_units_base03.setText(t.getThirdWinUnits() + "注");
							tv_money_base04.setText(Util.getDollarFormat(t.getFourthPrize()));
							tv_units_base04.setText(t.getFourthWinUnits() + "注");
							tv_money_base05.setText(Util.getDollarFormat(t.getFifthPrize()));
							tv_units_base05.setText(t.getFifthWinUnits() + "注");
							tv_money_base06.setText(Util.getDollarFormat(t.getSixthPrize()));
							tv_units_base06.setText(t.getSixthWinUnits() + "注");
						}
					}
				});

	}

	private void initView() {
		textview_periodNumber = (TextView) findViewById(R.id.textview_periodNumber);
		textview_time = (TextView) findViewById(R.id.textview_time);
		btn_go = (TextView) findViewById(R.id.btn_go);
		textview_result_dlt01 = (TextView) findViewById(R.id.textview_result_dlt01);
		textview_result_dlt02 = (TextView) findViewById(R.id.textview_result_dlt02);
		textview_result_dlt03 = (TextView) findViewById(R.id.textview_result_dlt03);
		textview_result_dlt04 = (TextView) findViewById(R.id.textview_result_dlt04);
		textview_result_dlt05 = (TextView) findViewById(R.id.textview_result_dlt05);
		textview_result_dlt06 = (TextView) findViewById(R.id.textview_result_dlt06);
		textview_result_dlt07 = (TextView) findViewById(R.id.textview_result_dlt07);
		tv_sales_money = (TextView) findViewById(R.id.tv_sales_money);
		tv_pond_money = (TextView) findViewById(R.id.tv_pond_money);
		tv_money_01 = (TextView) findViewById(R.id.tv_money_01);
		tv_units_01 = (TextView) findViewById(R.id.tv_units_01);
		tv_money_02 = (TextView) findViewById(R.id.tv_money_02);
		tv_units_02 = (TextView) findViewById(R.id.tv_units_02);
		tv_money_03 = (TextView) findViewById(R.id.tv_money_03);
		tv_units_03 = (TextView) findViewById(R.id.tv_units_03);
		tv_money_04 = (TextView) findViewById(R.id.tv_money_04);
		tv_units_04 = (TextView) findViewById(R.id.tv_units_04);
		tv_money_05 = (TextView) findViewById(R.id.tv_money_05);
		tv_units_05 = (TextView) findViewById(R.id.tv_units_05);
		tv_money_base01 = (TextView) findViewById(R.id.tv_money_base01);
		tv_units_base01 = (TextView) findViewById(R.id.tv_units_base01);
		tv_money_base02 = (TextView) findViewById(R.id.tv_money_base02);
		tv_units_base02 = (TextView) findViewById(R.id.tv_units_base02);
		tv_money_base03 = (TextView) findViewById(R.id.tv_money_base03);
		tv_units_base03 = (TextView) findViewById(R.id.tv_units_base03);
		tv_money_base04 = (TextView) findViewById(R.id.tv_money_base04);
		tv_units_base04 = (TextView) findViewById(R.id.tv_units_base04);
		tv_money_base05 = (TextView) findViewById(R.id.tv_money_base05);
		tv_units_base05 = (TextView) findViewById(R.id.tv_units_base05);
		tv_money_base06 = (TextView) findViewById(R.id.tv_money_base06);
		tv_units_base06 = (TextView) findViewById(R.id.tv_units_base06);
		
	}

	private void changHead() {
		// TODO 自动生成的方法存根

		TextView tvTextView = (TextView) findViewById(R.id.tv_head_title);
		tvTextView.setText("大乐透");
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
