package com.cshen.tiyu.activity.mian4.personcenter.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.MainActivity;
import com.cshen.tiyu.activity.login.LoginPwdActivity;
import com.cshen.tiyu.activity.login.RegisterSuccessActivity;
import com.cshen.tiyu.activity.mian4.personcenter.setting.binding.BankCardActivity;
import com.cshen.tiyu.activity.mian4.personcenter.setting.binding.BindingPhoneActivity;
import com.cshen.tiyu.activity.mian4.personcenter.setting.binding.NameAuthActivity;
import com.cshen.tiyu.activity.mian4.personcenter.setting.binding.PayPwdActivity;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.login.UserInfo;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.SafeSettingItemView;
import com.cshen.tiyu.zx.ZXMainActivity;

public class SafeSettingActivity extends BaseActivity {
	private static int UPDATE = 1;
	SafeSettingItemView bindingPhone;// 绑定手机号
	SafeSettingItemView nameAuth;// 实名认证
	SafeSettingItemView bankCard;// 银行卡
	SafeSettingItemView payPwd;// 支付密码
	SafeSettingItemView loginPwd;// 登录密码
	private SafeSettingItemView sv_safeCertification;//安全认证
	private String bindMobile;
	private boolean hasPayPassword, hasBindMobile, hasRealName,
	hasBindBankCard,hasSafeCertification;
	private Context mContext;
	private boolean isMaJia;//是否是资讯包
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_safe_setting);
		mContext = this;
		isMaJia=PreferenceUtil.getBoolean(mContext, "isMaJia");
		changHead();
		initView();
	}

	@Override
	public void onResume() {
		super.onResume();
		initShareInfo();
		initValue();
	}

	private void initShareInfo() {
		hasBindMobile = PreferenceUtil.getBoolean(mContext, "hasBindMobile");
		hasRealName = PreferenceUtil.getBoolean(mContext, "hasRealName");
		hasBindBankCard = PreferenceUtil
				.getBoolean(mContext, "hasBindBankCard");
		hasPayPassword = PreferenceUtil.getBoolean(mContext, "hasPayPassword");
		bindMobile = PreferenceUtil.getString(mContext, "bindMobile");
		hasSafeCertification=PreferenceUtil.getBoolean(mContext, "hasSafeCertification");
	}

	private void initView() {

		bindingPhone = (SafeSettingItemView) findViewById(R.id.sv_binding_phone);
		nameAuth = (SafeSettingItemView) findViewById(R.id.sv_name_auth);
		payPwd = (SafeSettingItemView) findViewById(R.id.sv_pay_pwd);
		bankCard = (SafeSettingItemView) findViewById(R.id.sv_bank_card);
		loginPwd = (SafeSettingItemView) findViewById(R.id.sv_loginPwd);
		sv_safeCertification=(SafeSettingItemView) findViewById(R.id.sv_safeCertification);
		
		loginPwd.setResource(false, "可修改");
		
		setOnclick();

	}

	private void initValue() {

		if (hasBindMobile) {
			bindingPhone.setRightItemVisible(false);
			if ((!TextUtils.isEmpty(bindMobile)) && bindMobile.length() > 7) {
				String text="手机号码:"+"<font color='#666666'>"
						+ new StringBuilder(bindMobile).replace(3, 7, "****")+"</font>";
				bindingPhone.setTitleHtmlValue(text);
			}
		} else {
			bindingPhone.setRightItemVisible(true);
			bindingPhone.setResource(false, "未绑定");
		}
		if (hasRealName) {
			nameAuth.setResource(true, "已认证");
		} else {
			nameAuth.setResource(false, "未认证");
		}
		if (hasBindBankCard) {
			bankCard.setResource(true, "查看");
		} else {
			bankCard.setResource(false, "未设置");
		}
		if (!hasPayPassword) {
			payPwd.setResource(false, "未设置");
		} else {
			payPwd.setResource(true, "可修改");
		}
		if (!hasSafeCertification) {//未进行验证
			sv_safeCertification.setTitle2Visibility(true);
			sv_safeCertification.setTitle2Text("请及时进行认证");
			sv_safeCertification.setTitle2TextColor(getResources().getColor(R.color.mainred));
			sv_safeCertification.setResource(false, "未认证");
		}else {
			sv_safeCertification.setTitle2Visibility(false);
			sv_safeCertification.setResource(false, "已认证");
		}
	}

	private void setOnclick() {
		// 手机号绑定
		bindingPhone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!hasBindMobile) {
					// TODO 自动生成的方法存根
					startActivityForResult(new Intent(SafeSettingActivity.this,
							BindingPhoneActivity.class), UPDATE);
				}
			}
		});
		// 实名认证
		nameAuth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				startActivityForResult(new Intent(SafeSettingActivity.this,
						NameAuthActivity.class), UPDATE);
			}
		});
		// 提款银行卡
		bankCard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				startActivityForResult(new Intent(SafeSettingActivity.this,
						BankCardActivity.class), UPDATE);
			}
		});

		// 支付密码
		payPwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				startActivityForResult(new Intent(SafeSettingActivity.this,
						PayPwdActivity.class), UPDATE);
			}
		});
		// 登录密码
		loginPwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				startActivity(new Intent(SafeSettingActivity.this,
						LoginPwdActivity.class));
			}
		});
		//安全问题验证
		sv_safeCertification.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (hasSafeCertification) {
					ToastUtils.showShortCenter(mContext, "您已完成安全认证问题！");
				}else {
					if (hasRealName) {//已经实名认证了
						Intent intent=new Intent(mContext,SafeCertificationActivity.class);
						startActivity(intent);
					}else {
						ToastUtils.showShortCenter(mContext, "请务必先进行实名认证！");
					}
				}
				
			}
		});
	}

	private void changHead() {
		// TODO 自动生成的方法存根

		TextView tvTextView = (TextView) findViewById(R.id.tv_head_title);
		tvTextView.setText("安全中心");
		ImageView backImageView = (ImageView) findViewById(R.id.iv_back);
		backImageView.setVisibility(View.VISIBLE);

		backImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent;
				if (isMaJia) {//资讯包
					intent=new Intent(mContext, ZXMainActivity.class);					
				}else {
					intent=new Intent(mContext, MainActivity.class);					
				}
				intent.putExtra("hasLogin", "yes");
				intent.putExtra("nowPage", "4");

				startActivity(intent);
				finish();
			}
		});

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// do something...
			Intent intent;
			if (isMaJia) {//资讯包
				intent=new Intent(mContext, ZXMainActivity.class);					
			}else {
				intent=new Intent(mContext, MainActivity.class);					
			}	
			intent.putExtra("hasLogin", "yes");
			intent.putExtra("nowPage", "4");

			startActivity(intent);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			default:
				initShareInfo();
				initValue();
				break;
			}
		}

	}
}