package com.cshen.tiyu.activity.mian4.personcenter.setting.binding;

import org.xutils.DbManager;
import org.xutils.x;
import org.xutils.ex.DbException;

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

import com.cshen.tiyu.R;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.base.CaiPiaoApplication;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.login.UserInfo;
import com.cshen.tiyu.domain.login.UserResultData;
import com.cshen.tiyu.net.https.ServiceUser;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.utils.Util;
import com.cshen.tiyu.widget.MyCountTime;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;

import de.greenrobot.event.EventBus;

public class PayPwdActivity extends BaseActivity implements OnClickListener {

	private LinearLayout linear_old_password;
	private Button btn_verif;
	private TextView btn_sure;
	private MyCountTime mMyCountTime;
	private Context mContext;
	private boolean  isTasking = false;
	private String bindMobile,oldPassword,newPassword;
	private EditText et_old_password, et_new_password, et_sure_password,
	et_code;
	private TextView tv_bind_mobile;
	private boolean hasPayPassword = false, hasBindMobile = false;
	private boolean checkCode;
	TopViewLeft tv_head=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_pwd);
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
				//startActivity(new Intent(BankNameActivity.this,SafeSettingActivity.class));
				finish();
			}
		});
		mContext = this;
		UserInfo userInfo=MyDbUtils.getCurrentUserInfo();
		if (userInfo!=null) {
			hasPayPassword = MyDbUtils.getCurrentUserInfo().getHasPayPassword() ||  PreferenceUtil.getBoolean(mContext, "hasPayPassword");
		}
		hasBindMobile = PreferenceUtil.getBoolean(mContext, "hasBindMobile");
		bindMobile = PreferenceUtil.getString(mContext, "bindMobile");
		initView();

	}

	private void initView() {
		linear_old_password = (LinearLayout) findViewById(R.id.linear_old_password);
		et_old_password = (EditText) findViewById(R.id.et_old_password);
		et_new_password = (EditText) findViewById(R.id.et_new_password);
		et_sure_password = (EditText) findViewById(R.id.et_sure_password);
		tv_bind_mobile = (TextView) findViewById(R.id.tv_bind_mobile);
		tv_bind_mobile.setOnClickListener(this);
		et_code = (EditText) findViewById(R.id.et_code);
		btn_verif = (Button) findViewById(R.id.btn_verif);
		btn_verif.setOnClickListener(this);
		btn_sure = (TextView) findViewById(R.id.btn_sure);
		btn_sure.setOnClickListener(this);
		initByHasPwdOrHasBindMobile();

	}

	@Override
	public void onClick(View v) {
		if (v == btn_verif) {
			btn_verif.setEnabled(false);
			String oldPwd = et_old_password.getText().toString();
			String newPwd = et_new_password.getText().toString();
			String surePwd = et_sure_password.getText().toString();
			if(hasPayPassword){
				if (TextUtils.isEmpty(oldPwd)) {
					ToastUtils.showShort(mContext, "旧密码不能为空");
					btn_verif.setEnabled(true);
					return;
				}	
			}
			if (TextUtils.isEmpty(newPwd)) {
				ToastUtils.showShort(mContext, "新密码不能为空");
				btn_verif.setEnabled(true);
				return;
			}if (newPwd.equals(oldPwd)) {
				ToastUtils.showShort(mContext, "旧密码和新密码不能一样哦");
				btn_verif.setEnabled(true);
				return;
			}if (TextUtils.isEmpty(surePwd)) {
				ToastUtils.showShort(mContext, "确认密码不能为空");
				btn_verif.setEnabled(true);
				return;
			}if (!surePwd.equals(newPwd)) {
				ToastUtils.showShort(mContext, "新密码和确认密码不一致");
				btn_verif.setEnabled(true);
				return;
			}
			if (TextUtils.isEmpty(bindMobile)) {
				ToastUtils.showShort(mContext, "手机号码不能为空");
				btn_verif.setEnabled(true);
				return;
			}
			isTasking = true;
			ServiceUser.getInstance().PostVerif(mContext,bindMobile,
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

					if (errorMsg!=null && errorMsg.code!=null && "8".equals(errorMsg.code)) {
						ToastUtils.showShort(mContext, "亲，已超过当天发送次数了哟!");

					}else{
						ToastUtils.showShort(mContext, "获取失败!");
					}
					isTasking = false;
					btn_verif.setEnabled(true);

				}
			});
		}
		if (v == tv_bind_mobile) {
			if (hasBindMobile == false) {
				Intent intent = new Intent(mContext, BindingPhoneActivity.class);
				startActivityForResult(intent, 1);
			}
		}
		if (v == btn_sure) {
			if (isTasking) {// 任务在运行中不再登入
				return;
			}
			oldPassword = et_old_password.getText().toString().trim();
			newPassword = et_new_password.getText().toString().trim();
			String surePassword = et_sure_password.getText().toString().trim();
			String mobile = tv_bind_mobile.getText().toString().trim();
			String code = et_code.getText().toString().trim();
			if (hasPayPassword == true) {
				if (TextUtils.isEmpty(oldPassword)) {
					ToastUtils.showShort(mContext, "旧密码不能为空");
					et_old_password.requestFocus();
					return;
				}else if (oldPassword.length()!=6) {
					ToastUtils.showShort(mContext, "旧密码格式不正确");
					et_old_password.requestFocus();
					return;
				}else if (!Util.isPayPassword(oldPassword)) {
					ToastUtils.showShort(mContext, "旧密码格式不正确");
					et_old_password.requestFocus();
					return;
				}
			}
			if (TextUtils.isEmpty(newPassword)) {
				ToastUtils.showShort(mContext, "新密码不能为空");
				et_new_password.requestFocus();
				return;
			}else if (newPassword.length()!=6) {
				ToastUtils.showShort(mContext, "新密码格式不正确");
				et_new_password.requestFocus();
				return;
			}else if (!Util.isPayPassword(newPassword)) {
				ToastUtils.showShort(mContext, "新密码格式不正确");
				et_new_password.requestFocus();
				return;
			}
			if (hasPayPassword == true) {
				if (newPassword.equals(oldPassword)) {
					ToastUtils.showShort(mContext, "旧密码和新密码不能一样哦");
					btn_verif.setEnabled(true);
					return;
				}
			}
			if (TextUtils.isEmpty(surePassword)) {
				ToastUtils.showShort(mContext, "确认密码不能为空");
				et_sure_password.requestFocus();
				return;
			}else if (!newPassword.equals(surePassword)) {
				ToastUtils.showShort(mContext, "确认密码与新密码不符");
				et_sure_password.requestFocus();
				return;
			}
			if (TextUtils.isEmpty(mobile)) {
				//绑定手机号
				ToastUtils.showShort(mContext, "号码不能为空");


				return;
			}
			if (TextUtils.isEmpty(code)) {
				ToastUtils.showShort(mContext, "验证码不能为空");
				et_code.requestFocus();
				return;
			}

			isTasking = true;
			ServiceUser.getInstance().oldPostBindMobile(mContext,bindMobile, code, new CallBack<String>() {
				@Override
				public void onSuccess(String userResultData) {
					isTasking = false;
					attemptPayPwd(oldPassword,newPassword,bindMobile);
					UserInfo userinfo=MyDbUtils.getCurrentUserInfo();
					if (userinfo.getHasPayPassword()==false) {
						userinfo.setHasPayPassword(true);
						DbManager manager = x.getDb(CaiPiaoApplication.getConfig());
						try {
							manager.save(userinfo);
						} catch (DbException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

				@Override
				public void onFailure(ErrorMsg errorMsg) {
					ToastUtils.showShort(mContext,"验证码错误");
					isTasking = false;
				}
			});
		}
	}

	private void attemptPayPwd(String oldPassword, String newPassword,
			String mobile) {
		if (isTasking) {//任务在运行中不再登入
			return;
		}

		isTasking = true;
		ServiceUser.getInstance().PostUserInfo(mContext,"1",null, null,
				MyDbUtils.getCurrentUser().getUserPwd(),null,oldPassword,newPassword,mobile,"1",false,new CallBack<String>() {

			@Override
			public void onSuccess(String s) {
				EventBus.getDefault().post("updateNotice");
				if (hasPayPassword) {
					ToastUtils.showShort(mContext, "成功修改支付密码");
				}else{
					ToastUtils.showShort(mContext, "成功设置支付密码");
				}
				PreferenceUtil.putBoolean(mContext, "hasPayPassword", true);
				isTasking = false;
				//数据是使用Intent返回  
				Intent intent = new Intent();  
				setResult(RESULT_OK, intent); 
				finish();  
			}

			@Override
			public void onFailure(ErrorMsg errorMsg) {
				if (errorMsg!=null && errorMsg.code!=null && "13".equals(errorMsg.code)) {
					ToastUtils.showShort(mContext, "支付原密码错误！");
					return;
				}else if (errorMsg!=null && errorMsg.code!=null && "8".equals(errorMsg.code)) {
					ToastUtils.showShort(mContext, "验证码错误！");
					return;
				}else{
					ToastUtils.showShort(mContext, "支付密码修改失败！");
				}
				isTasking = false;
			}
		});
	}

	private boolean checkCode(String mobile, String code) {
		checkCode=false;
		if (isTasking) {// 任务在运行中不再登入
			return false;
		}

		isTasking = true;
		ServiceUser.getInstance().oldPostBindMobile(mContext,mobile, code, new 
				CallBack<String>() {
			@Override
			public void onSuccess(String userResultData) {
				checkCode=true;
				isTasking = false;
			}

			@Override
			public void onFailure(ErrorMsg errorMsg) {
				ToastUtils.showShort(mContext,"验证码错误");
				isTasking = false;
				checkCode=false;
			}
		});
		return checkCode;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == 1) {
				bindMobile = data.getStringExtra("bindMobile");
				tv_bind_mobile.setText(new StringBuilder(bindMobile).replace(3,
						7, "****"));
				hasBindMobile = true;
			}
		}
	}

	private void initByHasPwdOrHasBindMobile() {
		if (hasPayPassword == true) {
			linear_old_password.setVisibility(View.VISIBLE);
		}
		if (hasBindMobile == true) {
			if ((!TextUtils.isEmpty(bindMobile))&& bindMobile.length()>7){ 
				tv_bind_mobile.setText(new StringBuilder(bindMobile).replace(3, 7,
						"****"));
			}
		}
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

}
