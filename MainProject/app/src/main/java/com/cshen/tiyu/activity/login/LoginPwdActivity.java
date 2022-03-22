package com.cshen.tiyu.activity.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.net.https.ServiceUser;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.utils.Util;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;

import de.greenrobot.event.EventBus;

public class LoginPwdActivity extends BaseActivity implements OnClickListener {

	private Context mContext;
	private boolean isTasking = false;
	private String oldPassword, newPassword;
	private EditText et_old_password, et_new_password, et_sure_password;
	private TextView btn_sure;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_pwd);

		TopViewLeft tv_head=(TopViewLeft) findViewById(R.id.tv_head);
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
				//startActivity(new Intent(BankNameActivity.this,SafeSettingActivity.class));
				finish();
			}
		});



		mContext = this;

		initView();

	}

	private void initView() {
		et_old_password = (EditText) findViewById(R.id.et_old_password);
//		et_old_password.addTextChangedListener(new TextChangedListener());
		et_new_password = (EditText) findViewById(R.id.et_new_password);
//		et_new_password.addTextChangedListener(new TextChangedListener());
		et_sure_password = (EditText) findViewById(R.id.et_sure_password);
//		et_sure_password.addTextChangedListener(new TextChangedListener());
		btn_sure = (TextView) findViewById(R.id.btn_sure);
		btn_sure.setBackgroundResource(R.drawable.selector_redbutton);
		btn_sure.setEnabled(true);   
		btn_sure.setOnClickListener(this);
		et_new_password.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				String password = et_new_password.getText().toString().trim();
				if (!TextUtils.isEmpty(password)) {
					if ( Util.textNameTemp1(password)) {
						ToastUtils.showShort(mContext, "密码不能全为数字");
						return;
					}
					if ( Util.isAllEnglish(password)) {
						ToastUtils.showShort(mContext, "密码不能全为字母");
						return;
					}
					if (!Util.isPasswordValid_OnlyNumAndLeter(password)||password.length()<6 || password.length()>15) {
						ToastUtils.showShort(mContext, "密码为6-15位字母和数字组成的密码");
						return;
					}
				}


			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v == btn_sure) {
			if (isTasking) {// 任务在运行中不再登入
				return;
			}
			oldPassword = et_old_password.getText().toString().trim();
			newPassword = et_new_password.getText().toString().trim();
			String surePassword = et_sure_password.getText().toString().trim();
			if (TextUtils.isEmpty(oldPassword)) {
				ToastUtils.showShort(mContext, "旧密码为空");
				et_old_password.requestFocus();
				return;
			}
			if (TextUtils.isEmpty(newPassword)) {
				ToastUtils.showShort(mContext, "新密码为空哟！");
				et_new_password.requestFocus();
				return;
			} 
			if ((!TextUtils.isEmpty(newPassword)) && Util.textNameTemp1(newPassword)) {
				ToastUtils.showShort(mContext, "密码不能全为数字");
				et_new_password.requestFocus();
				return;
			}
			if ((!TextUtils.isEmpty(newPassword)) && Util.isAllEnglish(newPassword)) {
				ToastUtils.showShort(mContext, "密码不能全为字母");
				et_new_password.requestFocus();
				return;
			}
			if (!Util.isPasswordValid_OnlyNumAndLeter(newPassword)||newPassword.length()<6 || newPassword.length()>15) {
				ToastUtils.showShort(mContext, "密码为6-15位字母和数字组成的密码");
				et_new_password.requestFocus();
				return;
			}
			if (TextUtils.isEmpty(surePassword)) {
				ToastUtils.showShort(mContext, "确认密码为空！");
				et_sure_password.requestFocus();
				return;
			} else if (!newPassword.equals(surePassword)) {
				ToastUtils.showShort(mContext, "确认密码与新密码不符");
				et_sure_password.setText("");
				et_sure_password.requestFocus();
				return;
			}
			attemptLoginPwd(oldPassword, newPassword);
		}

	}

	private void attemptLoginPwd(String oldPassword, String newPassword) {
		if (isTasking) {// 任务在运行中不再登入
			return;
		}
		isTasking = true;
		ServiceUser.getInstance().PostLoginPwd(mContext,oldPassword,newPassword,new CallBack<String>() {

			@Override
			public void onSuccess(String s) {
				ToastUtils.showShort(mContext, "成功修改登录密码");
				PreferenceUtil.putBoolean(mContext, "hasPayPassword", false);
				PreferenceUtil.putBoolean(mContext, "hasLogin", false);
				PreferenceUtil.putBoolean(mContext, "isExitWay", true);
				isTasking = false;
				EventBus.getDefault().post("clearSign");// gengxingqichi
				startActivity(new Intent(mContext,LoginActivity.class));
				finish();
			}

			@Override
			public void onFailure(ErrorMsg errorMsg) {
				if (errorMsg!=null && errorMsg.code!=null && "9".equals(errorMsg.code)) {
					ToastUtils.showShort(mContext, "旧密码不正确");
				}else {
					ToastUtils.showShort(mContext,"登录密码修改失败");
				}
				isTasking = false;
			}
		});
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getRepeatCount() == 0) {
			//do something...
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
//			String oldPassword = et_old_password.getText().toString().trim();
//			String newPassword = et_new_password.getText().toString().trim();
//			String surePassword = et_sure_password.getText().toString().trim();
//			if (!TextUtils.isEmpty(oldPassword) && !TextUtils.isEmpty(newPassword) && !TextUtils.isEmpty(surePassword)) {
//				btn_sure.setBackgroundResource(R.drawable.selector_redbutton);
//				btn_sure.setEnabled(true);
//			}else{
//				btn_sure.setBackgroundResource(R.drawable.ic_gray_button_normal);
//				btn_sure.setEnabled(false);
//			}
//				
//		}
//	}



}
