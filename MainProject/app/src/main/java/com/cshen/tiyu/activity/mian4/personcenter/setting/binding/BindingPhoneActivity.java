package com.cshen.tiyu.activity.mian4.personcenter.setting.binding;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.cshen.tiyu.R;
import com.cshen.tiyu.MainActivity;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.login.UserResultData;
import com.cshen.tiyu.net.https.ServiceUser;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.utils.PostHttpInfoUtils;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.utils.Util;
import com.cshen.tiyu.widget.MyCountTime;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;

import de.greenrobot.event.EventBus;

public class BindingPhoneActivity extends BaseActivity implements
OnClickListener {

	private EditText mobileEt, codeEt, mobileEtnew;
	private TextView tv_info,notice, tv_mobile,mobileTv,mobileTvnew;
	private LinearLayout newphone,linl_code;
	private Context mContext;
	private boolean isTasking = false;
	private Button btn_verif;
	private TextView btn_bin;
	private MyCountTime mMyCountTime;
	private boolean hasBindMobile = false;
	private String bindMobile;
	private String mobile,mobilenew, code;
	private boolean ubBind0 = false;
	private Boolean checkUserName = Boolean.FALSE;
	private String fromWat,authCode;
	private EditText et_password, et_password_sure;
	private LinearLayout  ll_password, ll_password_sure;
	TopViewLeft tv_head=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_binding_phone);
		mContext = this;
		hasBindMobile = PreferenceUtil.getBoolean(mContext, "hasBindMobile");
		bindMobile = PreferenceUtil.getString(mContext, "bindMobile");

	    fromWat = getIntent().getStringExtra("fromWay");
	    authCode = getIntent().getStringExtra("authCode");
	    if (!TextUtils.isEmpty(fromWat) && "loginByZFB".equals(fromWat)) {
	    	checkUserName = Boolean.TRUE;
	    }
	    
		tv_head=(TopViewLeft) findViewById(R.id.tv_head);
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

				//	startActivity(new Intent(BindingPhoneActivity.this,SafeSettingActivity.class));


				finish();
			}
		});
		initView();
	}

	private void initView() {
		et_password = (EditText) findViewById(R.id.et_password);
		et_password_sure = (EditText) findViewById(R.id.et_password_sure);
	    ll_password = (LinearLayout) findViewById(R.id. ll_password);
	    ll_password_sure = (LinearLayout) findViewById(R.id. ll_password_sure);
		
		
		notice  = (TextView) findViewById(R.id.notice);
		tv_info = (TextView) findViewById(R.id.tv_info);
		tv_mobile = (TextView) findViewById(R.id.tv_mobile);

		mobileTv = (TextView) findViewById(R.id.tv_mobile);
		mobileEt = (EditText) findViewById(R.id.et_monbile);
//		mobileEt.addTextChangedListener(new TextChangedListener());

		newphone = (LinearLayout) findViewById(R.id.newphone);
		mobileTvnew = (TextView) findViewById(R.id.tv_mobile_new);
		mobileEtnew = (EditText) findViewById(R.id.et_mobile_new);
//		mobileEtnew.addTextChangedListener(new TextChangedListener());

		linl_code = (LinearLayout) findViewById(R.id.linl_code);
		codeEt = (EditText) findViewById(R.id.codeEt);
//		codeEt.addTextChangedListener(new TextChangedListener());
		btn_verif = (Button) findViewById(R.id.btn_verif);
		btn_verif.setOnClickListener(this);
		btn_bin = (TextView) findViewById(R.id.btn_bin);
		
		btn_bin.setBackgroundResource(R.drawable.selector_redbutton);
		btn_bin.setEnabled(true);
		
		btn_bin.setOnClickListener(this);
		
		
		if (hasBindMobile == true) {
			newphone.setVisibility(View.VISIBLE);
			mobileTv.setText("旧手机号码");
			mobileTvnew.setText("新手机号码");
			btn_bin.setText("立即绑定");
			tv_head.setTitle("更换手机");
			notice.setVisibility(View.GONE);
		}else{
			mobileTv.setText("绑定号码");
			newphone.setVisibility(View.GONE);
			notice.setVisibility(View.VISIBLE);
		}
		
		
		if (!TextUtils.isEmpty(fromWat) && "loginByZFB".equals(fromWat)) {
	    	ll_password.setVisibility(View.VISIBLE);
	    	ll_password_sure.setVisibility(View.VISIBLE);
	    }
	}

	@Override
	public void onClick(View v) {
		String mobilebase = null;
		mobile = mobileEt.getText().toString().trim();
		mobilenew = mobileEtnew.getText().toString().trim();
		code = codeEt.getText().toString().trim();
		if (v == btn_verif) {

			btn_verif.setEnabled(false);
			EditText et= null;
			if (hasBindMobile) {//已经绑定，获取新手机的验证码
				mobilebase = mobilenew;
				et = mobileEtnew;
			}else{
				mobilebase = mobile;
				et = mobileEt;
			}
			
			/*** 密码验证 ***/
			if (!TextUtils.isEmpty(fromWat) && "loginByZFB".equals(fromWat)) {
				String newPassword = et_password.getText().toString().trim();
				String surePassword = et_password_sure.getText().toString().trim();
				if (TextUtils.isEmpty(newPassword)) {
					ToastUtils.showShort(mContext, "新密码为空");
					et_password.requestFocus();
					btn_verif.setEnabled(true);
					return;
				}
				if ((!TextUtils.isEmpty(newPassword))
						&& Util.textNameTemp1(newPassword)) {
					ToastUtils.showShort(mContext, "密码不能全为数字");
					et_password.requestFocus();
					btn_verif.setEnabled(true);
					return;
				}
				if ((!TextUtils.isEmpty(newPassword))
						&& Util.isAllEnglish(newPassword)) {
					ToastUtils.showShort(mContext, "密码不能全为字母");
					et_password.requestFocus();
					btn_verif.setEnabled(true);
					return;
				}
				if (newPassword.length() < 6 || newPassword.length() > 15) {
					ToastUtils.showShort(mContext, "密码为6-16位字符和密码");
					et_password.requestFocus();
					btn_verif.setEnabled(true);
					return;
				}
				if (TextUtils.isEmpty(surePassword)) {
					ToastUtils.showShort(mContext, "确认密码为空");
					et_password_sure.requestFocus();
					btn_verif.setEnabled(true);
					return;
				} else if (!newPassword.equals(surePassword)) {
					ToastUtils.showShort(mContext, "确认密码与新密码不符");
					et_password_sure.setText("");
					et_password_sure.requestFocus();
					btn_verif.setEnabled(true);
					return;
				}
				
			}
			
			/*** 密码验证 ***/
			
			
			
			
			if (TextUtils.isEmpty(mobilebase)) {
				ToastUtils.showShort(mContext, "手机号码不能为空");
				et.requestFocus();
				btn_verif.setEnabled(true);
				return;
			}
			if (hasBindMobile) {//已经绑定，获取新手机的验证码
				if (TextUtils.isEmpty(mobile)) {
					ToastUtils.showShort(mContext, "旧手机号码不能为空");
					et.requestFocus();
					btn_verif.setEnabled(true);
					return;
				}
//			 if (!mobile.equals(bindMobile)) {
//					ToastUtils.showShort(mContext, "原手机号输入不正确");
//					mobileEt.requestFocus();
//					btn_verif.setEnabled(true);
//					return;
//				} 
				if(mobilenew!=null&&mobilenew.equals(mobile)){
					ToastUtils.showShort(mContext, "新手机号码和旧手机号码一致");
					btn_verif.setEnabled(true);
					return;
				}
			}
			if (!Util.isMobileValid(mobilebase)) {
				ToastUtils.showShort(mContext, "手机号格式不正确");
				et.requestFocus();
				btn_verif.setEnabled(true);
				return;
			}
			isTasking = true;
			ServiceUser.getInstance().newPostVerif(mContext,mobilebase,checkUserName,
					new CallBack<UserResultData>() {
				@Override
				public void onSuccess(UserResultData userResultData) {
					mMyCountTime = new MyCountTime(mContext, 60000,
							1000, btn_verif, "重新获取", "", null, false);
					mMyCountTime.start();
					isTasking = false;
					btn_verif.setEnabled(true);
				}

				@Override
				public void onFailure(ErrorMsg errorMsg) {
					PostHttpInfoUtils.doPostFail(mContext, errorMsg, "获取失败");
					isTasking = false;
					btn_verif.setEnabled(true);
				}
			});

		}
		if (v == btn_bin && hasBindMobile == false) {//没绑定，要绑定
			if (isTasking) {
				return;
			}
			if (!TextUtils.isEmpty(fromWat) && "loginByZFB".equals(fromWat)) {
				String newPassword = et_password.getText().toString().trim();
				String surePassword = et_password_sure.getText().toString().trim();
				if (TextUtils.isEmpty(newPassword)) {
					ToastUtils.showShort(mContext, "新密码为空");
					et_password.requestFocus();
					btn_verif.setEnabled(true);
					return;
				}
				if ((!TextUtils.isEmpty(newPassword))
						&& Util.textNameTemp1(newPassword)) {
					ToastUtils.showShort(mContext, "密码不能全为数字");
					et_password.requestFocus();
					btn_verif.setEnabled(true);
					return;
				}
				if ((!TextUtils.isEmpty(newPassword))
						&& Util.isAllEnglish(newPassword)) {
					ToastUtils.showShort(mContext, "密码不能全为字母");
					et_password.requestFocus();
					btn_verif.setEnabled(true);
					return;
				}
				if (newPassword.length() < 6 || newPassword.length() > 15) {
					ToastUtils.showShort(mContext, "密码为6-16位字符和密码");
					et_password.requestFocus();
					btn_verif.setEnabled(true);
					return;
				}
				if (TextUtils.isEmpty(surePassword)) {
					ToastUtils.showShort(mContext, "确认密码为空");
					et_password_sure.requestFocus();
					btn_verif.setEnabled(true);
					return;
				} else if (!newPassword.equals(surePassword)) {
					ToastUtils.showShort(mContext, "确认密码与新密码不符");
					et_password_sure.setText("");
					et_password_sure.requestFocus();
					btn_verif.setEnabled(true);
					return;
				}
				
				if (TextUtils.isEmpty(mobile)) {
					ToastUtils.showShort(mContext, "手机号码不能为空");
					mobileEt.requestFocus();
					return;
				}else if (!Util.isMobileValid(mobile)) {
					ToastUtils.showShort(mContext, "手机号格式不对");
					mobileEt.requestFocus();
					return;
				}  else if (TextUtils.isEmpty(code)) {
					ToastUtils.showShort(mContext, "验证码不能为空");
					codeEt.requestFocus();
					return;
				}
				isTasking = true;
				ServiceUser.getInstance().PostBindMobile(mContext,mobile, code,newPassword,authCode,
						new CallBack<UserResultData>() {
					@Override
					public void onSuccess(UserResultData userResultData) {
						ToastUtils.showShort(mContext, "绑定成功");
						EventBus.getDefault().post("updateNotice");
						PreferenceUtil.putBoolean(mContext,
								"hasBindMobile", true);
						PreferenceUtil.putString(mContext,
								"bindMobile", mobile);
						Intent data = new Intent();
						data.putExtra("bindMobile", mobile);
						setResult(RESULT_OK, data);
						isTasking = false;
						PreferenceUtil.putBoolean(mContext, "hasLogin", true);
						PreferenceUtil.putBoolean(mContext, "isExitWay", false);
						Intent intent = new Intent(mContext,
								MainActivity.class);
						intent.putExtra("hasLogin", "yes");// 标记
						startActivity(intent);
						finish();
					}

					@Override
					public void onFailure(ErrorMsg errorMsg) {
						PostHttpInfoUtils.doPostFail(mContext, errorMsg, "绑定失败");
						isTasking = false;
					}
				});
			
				
			}else{
				if (TextUtils.isEmpty(mobile)) {
					ToastUtils.showShort(mContext, "手机号码不能为空");
					mobileEt.requestFocus();
					return;
				}else if (!Util.isMobileValid(mobile)) {
					ToastUtils.showShort(mContext, "手机号格式不对");
					mobileEt.requestFocus();
					return;
				}  else if (TextUtils.isEmpty(code)) {

					isTasking = false;ToastUtils.showShort(mContext, "验证码不能为空");
					codeEt.requestFocus();
					return;
				}
				isTasking = true;
				ServiceUser.getInstance().PostBindMobile(mContext,mobile, code,null,null,
						new CallBack<UserResultData>() {
					@Override
					public void onSuccess(UserResultData userResultData) {
						ToastUtils.showShort(mContext, "绑定成功");
						EventBus.getDefault().post("updateNotice");
						PreferenceUtil.putBoolean(mContext,
								"hasBindMobile", true);
						PreferenceUtil.putString(mContext,
								"bindMobile", mobile);
						Intent data = new Intent();
						data.putExtra("bindMobile", mobile);
						setResult(RESULT_OK, data);
						isTasking = false;
						finish();
					}

					@Override
					public void onFailure(ErrorMsg errorMsg) {
						PostHttpInfoUtils.doPostFail(mContext, errorMsg, "绑定失败");
						isTasking = false;
					}
				});
			}
			
		}
		if (v == btn_bin && hasBindMobile == true) {//已绑定要解绑
			if (isTasking) {
				return;
			}
			if (TextUtils.isEmpty(mobile)) {
				ToastUtils.showShort(mContext, "旧手机号为空");
				mobileEt.requestFocus();
				return;
			}else if (!mobile.equals(bindMobile)) {
				ToastUtils.showShort(mContext, "旧手机号输入不正确");
				mobileEt.requestFocus();
				return;
			} 
			
			if (TextUtils.isEmpty(mobilenew)) {
				ToastUtils.showShort(mContext, "新手机号为空");
				mobileEtnew.requestFocus();
				return;
			}else if (!Util.isMobileValid(mobilenew)) {
				ToastUtils.showShort(mContext, "新手机号格式不正确");
				mobileEtnew.requestFocus();
				return;
			} 
			if(mobilenew.equals(mobile)){
				ToastUtils.showShort(mContext, "新手机号和旧手机号一致");
				return;
			}
			if (TextUtils.isEmpty(code)) {
				ToastUtils.showShort(mContext, "验证码为空");
				codeEt.requestFocus();
				return;
			}
			isTasking = true;
			ServiceUser.getInstance().PostUnBindMobile(mContext,mobile,mobilenew,code,
					new CallBack<String>() {
				@Override
				public void onSuccess(String userResultData) {
					ToastUtils.showShort(mContext, "成功更换绑定！");
					PreferenceUtil.putBoolean(mContext,
							"hasBindMobile", true);
					PreferenceUtil.putString(mContext,
							"bindMobile", mobilenew);
					isTasking = false;
					finish();
				}

				@Override
				public void onFailure(ErrorMsg errorMsg) {
					PostHttpInfoUtils.doPostFail(mContext, errorMsg, "更换绑定失败");
					isTasking = false;
				}
			});
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// do something...
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
//	class TextChangedListener implements TextWatcher{
//		
//		@Override
//		public void onTextChanged(CharSequence s, int start, int before, int count) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//		@Override
//		public void beforeTextChanged(CharSequence s, int start, int count,
//				int after) {
//			
//		}
//		
//		@Override
//		public void afterTextChanged(Editable s) {
//		String	mobile = mobileEt.getText().toString().trim();
//		String	mobilenew = mobileEtnew.getText().toString().trim();
//		String	code = codeEt.getText().toString().trim();
//			if (hasBindMobile) {
//				if (!TextUtils.isEmpty(mobile) && !TextUtils.isEmpty(mobilenew) && !TextUtils.isEmpty(code)) {
//					btn_bin.setBackgroundResource(R.drawable.selector_redbutton);
//					btn_bin.setEnabled(true);
//				}else{
//					btn_bin.setBackgroundResource(R.drawable.ic_gray_button_normal);
//					btn_bin.setEnabled(false);
//				}
//			}else{
//				if (!TextUtils.isEmpty(mobile) && !TextUtils.isEmpty(code)) {
//					btn_bin.setBackgroundResource(R.drawable.selector_redbutton);
//					btn_bin.setEnabled(true);
//				}else{
//					btn_bin.setBackgroundResource(R.drawable.ic_gray_button_normal);
//					btn_bin.setEnabled(false);
//				}
//				
//				
//			}
//			
//		}
//	}
}
