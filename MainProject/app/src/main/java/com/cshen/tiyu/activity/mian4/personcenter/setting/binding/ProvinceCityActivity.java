package com.cshen.tiyu.activity.mian4.personcenter.setting.binding;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;

import com.cshen.tiyu.R;

public class ProvinceCityActivity extends BaseActivity {
	private String province_ciry;
	private Spinner province_spinner;
	private Spinner city_spinner;
	private Integer provinceId, cityId;
	private int preProvinceId = 0, preCityId = 0;
	private TextView btn_sure;
	private String strProvince, strCity, provinceCity;
	private Context mContext;
	private boolean firstTemp = true;
	// 市，自治区集合
	private int[] city = {
			R.array.beijin_province_item,
			R.array.shanghai_province_item,
			R.array.tianjin_province_item,
			R.array.chongqing_province_item, 
			R.array.hebei_province_item,
			
			R.array.shanxi1_province_item, 
			R.array.neimenggu_province_item,
			R.array.liaoning_province_item,
			R.array.jilin_province_item,
			R.array.heilongjiang_province_item,
			
			R.array.jiangsu_province_item,
			R.array.zhejiang_province_item,
			R.array.anhui_province_item, 
			R.array.fujian_province_item,
			R.array.jiangxi_province_item, 
			
			R.array.shandong_province_item,
			R.array.henan_province_item,
			R.array.hubei_province_item,
			R.array.hunan_province_item, 
			R.array.guangdong_province_item,
			
			R.array.guangxi_province_item, 
			R.array.hainan_province_item,
			R.array.sichuan_province_item,
			R.array.guizhou_province_item,
			R.array.yunnan_province_item,
			
			R.array.xizang_province_item,
			R.array.shanxi2_province_item, 
			R.array.gansu_province_item,
			R.array.ningxia_province_item,
			R.array.qinghai_province_item, 
			
			R.array.xinjiang_province_item,
			R.array.hongkong_province_item,
			R.array.aomen_province_item,
			R.array.taiwan_province_item };
	private ArrayAdapter<CharSequence> province_adapter;
	private ArrayAdapter<CharSequence> city_adapter;
	TopViewLeft tv_head = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_province_city);
		mContext = this;
		provinceCity = getIntent().getStringExtra("provinceCity");
		if (!TextUtils.isEmpty(provinceCity)) {
			preProvinceId = PreferenceUtil.getInt(mContext, "provinceId");
			preCityId = PreferenceUtil.getInt(mContext, "cityId");
		}
		province_spinner = (Spinner) findViewById(R.id.province_spinner);
		city_spinner = (Spinner) findViewById(R.id.city_spinner);
		btn_sure = (TextView) findViewById(R.id.btn_sure);
		btn_sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent data = new Intent();
				data.putExtra("province_ciry", province_ciry);
				setResult(1, data);
				finish();

			}
		});
		tv_head = (TopViewLeft) findViewById(R.id.tv_head);
		tv_head.setResourceVisiable(true, false, false);

		tv_head.setTopClickItemListener(new TopClickItemListener() {

			@Override
			public void clickLoginView(View view) {
				// TODO Auto-generated method stub

			}

			@Override
			public void clickContactView(View view) {
				// TODO Auto-generated method stub

			}

			@Override
			public void clickBackImage(View view) {
				// TODO Auto-generated method stub 
				finish();
			}
		});
		loadSpinner();
		province_spinner.setSelection(preProvinceId);

	}

	private void loadSpinner() {
		// 绑定省份的数据
		province_spinner.setPrompt("请选择省份");
		province_adapter = ArrayAdapter.createFromResource(this,
				R.array.province_item, android.R.layout.simple_spinner_item);
		province_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		province_spinner.setAdapter(province_adapter);
		// select(province_spinner, province_adapter, R.array.province_item);
		// 添加监听，一开始的时候城市，县区的内容是不显示的而是根据省的内容进行联动
		province_spinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						provinceId = province_spinner.getSelectedItemPosition();
						PreferenceUtil.putInt(mContext, "provinceId",
								provinceId);
						strProvince = province_spinner.getSelectedItem()
								.toString();// 得到选择的内容，也就是省的名字

						if (true) {

							city_spinner = (Spinner) findViewById(R.id.city_spinner);
							city_spinner.setPrompt("请选择城市");// 设置标题
							select(city_spinner, city_adapter, city[provinceId]);// 城市一级的数据绑定
							/*
							 * 通过这个city[provinceId]指明了该省市的City集合 R。array.beijing
							 */
							city_spinner
									.setOnItemSelectedListener(new OnItemSelectedListener() {

										@Override
										public void onItemSelected(
												AdapterView<?> arg0, View arg1,
												int arg2, long arg3) {
											if (firstTemp) {
												city_spinner.setSelection(
														preCityId, true);
											}
											firstTemp = false;
											cityId = city_spinner
													.getSelectedItemPosition();// 得到city的id
											PreferenceUtil.putInt(mContext,
													"cityId", cityId);
											strCity = city_spinner
													.getSelectedItem()
													.toString();// 得到city的内容
											Log.v("test", "city: "
													+ city_spinner
															.getSelectedItem()
															.toString()// 输出测试一下
													+ cityId.toString());
											province_ciry = strProvince + " "
													+ strCity;

										}

										@Override
										public void onNothingSelected(
												AdapterView<?> arg0) {
											// TODO Auto-generated method stub

										}

									});
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});

	}

	/* 通过方法动态的添加适配器 */
	private void select(Spinner spin, ArrayAdapter<CharSequence> adapter,
			int arry) {
		// 注意这里的arry不仅仅但是一个整形，他代表了一个数组！
		adapter = ArrayAdapter.createFromResource(this, arry,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin.setAdapter(adapter);
		// spin.setSelection(0,true);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// do something...
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
