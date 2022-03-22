package com.cshen.tiyu.activity.mian4.personcenter.setting.binding;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.cshen.tiyu.R;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.login.UserInfo;
import com.cshen.tiyu.net.https.ServiceUser;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.utils.BankCard;
import com.cshen.tiyu.utils.PostHttpInfoUtils;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;

import de.greenrobot.event.EventBus;

public class BankCardActivity extends BaseActivity implements OnClickListener {

	private TextView btn_sure;
	private TextView et_bankName,et_province_city;
	private View ibtn_bank_name,ibtn_province_city;
	private ImageView ibtn_bank_name_im,ibtn_province_city_im;
	private Context mContext;
	private TextView tv_real_name, tv_idCard;
	private EditText et_bankCard,et_parkBankName;
	private boolean hasBindBankCard = false;
	private boolean hasRealName = false;
	private String realName, idCard;
	TopViewLeft tv_head = null;
	boolean isTasking = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bank_card);

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
				// startActivity(new
				// Intent(BankNameActivity.this,SafeSettingActivity.class));
				Intent intent = new Intent();  
				setResult(RESULT_OK, intent); 
				finish();
			}
		});

		// try {
		// MyDbUtils.test();
		// } catch (DbException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		mContext = this;
		// formatUserInfo=MyDbUtils.getCurrentFormatUserInfo();
		UserInfo userInfo = MyDbUtils.getCurrentUserInfo();
		if (userInfo != null) {
			realName = userInfo.getRealName();
			idCard = userInfo.getIdCard();
		}
		hasRealName = PreferenceUtil.getBoolean(mContext, "hasRealName");
		hasBindBankCard = PreferenceUtil
				.getBoolean(mContext, "hasBindBankCard");
		initView();
	}

	private void initView() {
		btn_sure = (TextView) findViewById(R.id.btn_sure);
		ibtn_bank_name =  findViewById(R.id.ibtn_bank_name);
		ibtn_province_city =  findViewById(R.id.ibtn_province_city);
		ibtn_province_city_im =  (ImageView) findViewById(R.id.ibtn_bank_name_im);
		ibtn_bank_name_im =  (ImageView) findViewById(R.id.ibtn_province_city_im);
		tv_real_name = (TextView) findViewById(R.id.et_real_name);
		tv_real_name.setOnClickListener(this);
		tv_idCard = (TextView) findViewById(R.id.et_idCard);
		tv_idCard.setOnClickListener(this);
		et_bankName = (TextView) findViewById(R.id.et_bankName);
		et_bankCard = (EditText) findViewById(R.id.et_bankCard);
		et_bankCard.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				String bankCard=et_bankCard.getText().toString().trim();
				if (!TextUtils.isEmpty(bankCard)) {
					if (!BankCard.checkBankCard(bankCard)) {
						ToastUtils.showShort(mContext, "银行卡格式不正确");
					}
				}
			}
		});
		et_province_city = (TextView) findViewById(R.id.et_province_city);
		et_parkBankName = (EditText) findViewById(R.id.et_parkBankName);

		btn_sure.setOnClickListener(this);
		ibtn_bank_name.setOnClickListener(this);
		ibtn_province_city.setOnClickListener(this);

		UserInfo userInfo = MyDbUtils.getCurrentUserInfo();
		if (userInfo != null) {
			String realName = userInfo.getRealName();
			if (!TextUtils.isEmpty(realName)) {
				StringBuilder realNameBuilder = new StringBuilder(
						realName.subSequence(0, 1));
				for (int i = 0; i < realName.length() - 1; i++) {
					realNameBuilder.append("*");
					tv_real_name.setText(realNameBuilder);
				}
			}
			String idCard = userInfo.getIdCard();
			if ((!TextUtils.isEmpty(idCard)) && idCard.length() > 4) {
				tv_idCard.setText(new StringBuilder(userInfo.getIdCard())
				.replace(userInfo.getIdCard().length() - 4,
						userInfo.getIdCard().length(), "****"));
			}
		}
		if (hasBindBankCard) {
			changeViewHasBindBankCard();
		}

	}

	private void changeViewHasBindBankCard() {
		UserInfo userInfo = MyDbUtils.getCurrentUserInfo();
		if (userInfo != null) {
			if (hasBindBankCard) {

				et_bankName.setText(userInfo.getBankName());

				if (!TextUtils.isEmpty(userInfo.getBankCard())) {
					if (userInfo.getBankCard().length() > 16) {
						et_bankCard.setText(new StringBuilder(userInfo
								.getBankCard()).replace(3, 16, "************"));
					} else if (userInfo.getBankCard().length() > 3) {
						et_bankCard.setText(new StringBuilder(userInfo
								.getBankCard()).replace(3, userInfo
										.getBankCard().length(), "************"));
					}
				}
				et_bankCard.setEnabled(false);
				et_province_city.setText(userInfo.getPartBankProvince() + " "
						+ userInfo.getPartBankCity());
				et_parkBankName.setText(userInfo.getPartBankName());
				et_parkBankName.setEnabled(false);
				ibtn_province_city_im.setVisibility(View.GONE);
				ibtn_bank_name_im.setVisibility(View.GONE);
				btn_sure.setText("修改");
			}

		}

	}

	@Override
	public void onClick(View v) {
		if (v == tv_real_name || v == tv_idCard) {
			if (!hasRealName) {
				Intent intent = new Intent(mContext, NameAuthActivity.class);
				startActivityForResult(intent, 2);
			}
		}
		if (v == ibtn_bank_name) {
			Intent intent = new Intent(mContext, BankNameActivity.class);
			startActivityForResult(intent, 1);
		}

		if (v == ibtn_province_city) {
			Intent intent = new Intent(mContext, ProvinceCityActivity.class);
			intent.putExtra("provinceCity", et_province_city.getText()
					.toString().trim());
			startActivityForResult(intent, 3);

		}
		if (v == btn_sure) {
			String btnsure = btn_sure.getText().toString();
			if (btnsure.equals("确认")) {
				if (isTasking) {// 任务在运行中不再登入
					return;
				}
				String s_realName = tv_real_name.getText().toString().trim();
				String s_idCard = tv_idCard.getText().toString().trim();
				String bankName = et_bankName.getText().toString().trim();
				String bankCard = et_bankCard.getText().toString().trim();
				String province_city = et_province_city.getText().toString()
						.trim();
				String partBankName = et_parkBankName.getText().toString()
						.trim();
				String partProvince ="", partCity ="";
				if (TextUtils.isEmpty(s_realName)
						|| TextUtils.isEmpty(s_idCard)) {
					ToastUtils.showShort(mContext, "亲，要先进行实名认证哦！");
					Intent intent = new Intent(mContext, NameAuthActivity.class);
					startActivityForResult(intent, 2);
					return;
				}
				if (TextUtils.isEmpty(bankName)||bankName.equals("选择银行")) {
					ToastUtils.showShort(mContext, "亲，请选择银行");
					et_parkBankName.requestFocus();
					return;
				}
				if (TextUtils.isEmpty(bankCard)) {
					ToastUtils.showShort(mContext, "亲，银行卡号不能为空哟！");
					et_bankCard.requestFocus();
					return;
				}
				if (!BankCard.checkBankCard(bankCard)) {
					ToastUtils.showShort(mContext, "亲，银行卡格式不正确");
					et_bankCard.requestFocus();
					return;
				}
				if (TextUtils.isEmpty(province_city)) {
					ToastUtils.showShort(mContext, "亲，开户地不能为空哟!");
					return;
				} else {
					if(province_city.contains(" ")
							&&province_city.split(" ").length>1){
						partProvince = province_city.split(" ")[0];
						partCity = province_city.split(" ")[1];
					}else{
						partProvince="";
						partCity ="";
					}
				};
				attemptBindBankCard(realName, idCard, bankName, bankCard,
						partProvince, partCity, partBankName);
			}
			if (btnsure.equals("修改")) {
				et_bankName.setText("选择银行");
				et_bankCard.setEnabled(true);
				et_bankCard.setText("");
				et_province_city.setText("请选择开户行地区");
				et_parkBankName.setEnabled(true);
				et_parkBankName.setText("");
				ibtn_bank_name_im.setVisibility(View.VISIBLE);
				ibtn_province_city_im.setVisibility(View.VISIBLE);
				btn_sure.setText("确认");
			}

		}

	}

	private void attemptBindBankCard(final String realName, String idCard,
			final String bankName, final String bankCard, String partProvince,
			String partCity, String partBankName) {
        isTasking= true;
		ServiceUser.getInstance().PostBindBankCard(mContext,realName, idCard, bankName,
				bankCard, partProvince, partCity, partBankName,
				new CallBack<String>() {
			@Override
			public void onSuccess(String t) {
                isTasking= false;
				ToastUtils.showShort(mContext, "绑定银行卡成功啦！");
				EventBus.getDefault().post("updateNotice");
				PreferenceUtil.putBoolean(mContext, "hasBindBankCard",true);
				ServiceUser.getInstance().GetUserInfo(mContext);
				Intent data = new Intent();
				data.putExtra("bankName", bankName);
				data.putExtra("bankCard", bankCard);
				data.putExtra("realName", realName);
				setResult(RESULT_OK, data);
				finish();
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
                isTasking= false;
				PostHttpInfoUtils.doPostFail(mContext, errorMessage,"绑定银行卡失败");
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 1) {
			if (requestCode == 1) {
				String bankName = data.getStringExtra("bankName");
				et_bankName.setText(bankName);
			}
			if (requestCode == 3) {
				String province_ciry = data.getStringExtra("province_ciry");
				et_province_city.setText(province_ciry);
			}
		}
		if (requestCode == 2) {

			UserInfo userInfo = MyDbUtils.getCurrentUserInfo();
			if (userInfo != null) {
				realName = userInfo.getRealName();
				if (!TextUtils.isEmpty(realName)) {
					StringBuilder realNameBuilder = new StringBuilder(
							realName.subSequence(0, 1));
					for (int i = 0; i < realName.length() - 1; i++) {
						realNameBuilder.append("*");
						tv_real_name.setText(realNameBuilder);
					}
				}
				idCard = userInfo.getIdCard();
				if (!TextUtils.isEmpty(idCard) && idCard.length() > 4) {
					tv_idCard.setText(new StringBuilder(userInfo.getIdCard())
					.replace(userInfo.getIdCard().length() - 4,
							userInfo.getIdCard().length(), "****"));
				}

				if ((!TextUtils.isEmpty(idCard)) && (!TextUtils.isEmpty(realName))) {
					hasRealName = true;
				}

			}
		}

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// do something...
			Intent intent = new Intent();  
			setResult(RESULT_OK, intent); 
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
