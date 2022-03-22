package com.cshen.tiyu.activity.login;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.LotteryTypeActivity;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.login.UserResultData;
import com.cshen.tiyu.net.https.ServiceUser;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.utils.PostHttpInfoUtils;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.utils.Util;
import com.cshen.tiyu.widget.MyCountTime;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;

import de.greenrobot.event.EventBus;

public class ForgotPwdTwoActivity extends BaseActivity implements
OnClickListener {

	private Context mContext;
	private boolean isTasking = false;
	private MyCountTime mMyCountTime;
	private LinearLayout ll_temp1, ll_temp2, ll_temp3;
	private LinearLayout ll_monbile_way;
	private ImageView img_temp1, img_temp2, img_temp3;
	private TextView tv_forget_kefu, tv_mobile_number1;
	private TextView tv_forgetUserPhoneBeFore_temp2,tv_forgetUserPhoneAfter_temp2;
	private TextView tv_temp2_info;
	private EditText et_code;
	private EditText et_new_password, et_sure_password;
	private TextView btn_sure;
	private Button btn_verif;
	private Button btn_new_password, btn_sure_password;
	private int currentTemp;
	private String forgetUserPhoneBeFore, forgetUserPhoneAfter;
	private EditText et_mobile_number;
	private String mobile, userName;
	private TopViewLeft tv_head;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_pwd_two);
		mContext = this;
		EventBus.getDefault().register(this);
		initHead();
		Intent intent = getIntent();
		userName = intent.getStringExtra("userName") == null ? "" : intent
				.getStringExtra("userName");
		forgetUserPhoneBeFore = intent.getStringExtra("forgetUserPhoneBeFore") == null ? ""
				: intent.getStringExtra("forgetUserPhoneBeFore");
		forgetUserPhoneAfter = intent.getStringExtra("forgetUserPhoneAfter") == null ? ""
				: intent.getStringExtra("forgetUserPhoneAfter");

		currentTemp = 1;
		initView();
	}

	private void initHead() {
		// TODO 自动生成的方法存根
		tv_head=(TopViewLeft) findViewById(R.id.tv_head_forget_two);

		tv_head.setResourceVisiable(true, false, false);
		tv_head.setTopClickItemListener(new TopClickItemListener() {

			@Override
			public void clickLoginView(View view) {
				// TODO 自动生成的方法存根
			}

			@Override
			public void clickContactView(View view) {
				// TODO 自动生成的方法存根
			}

			@Override
			public void clickBackImage(View view) {
				// TODO 自动生成的方法存根
				back();
			}
		});


	}

	private void initView() {
		et_mobile_number = (EditText) findViewById(R.id.et_mobile_number);

		img_temp1=(ImageView) findViewById(R.id.img_temp1);
		img_temp2=(ImageView) findViewById(R.id.img_temp2);
		img_temp3=(ImageView) findViewById(R.id.img_temp3);

		tv_forgetUserPhoneBeFore_temp2 = (TextView) findViewById(R.id.tv_forgetUserPhoneBeFore_temp2);
		tv_forgetUserPhoneAfter_temp2 = (TextView) findViewById(R.id.tv_forgetUserPhoneAfter_temp2);
		tv_forgetUserPhoneBeFore_temp2.setText(forgetUserPhoneBeFore);
		tv_forgetUserPhoneAfter_temp2.setText(forgetUserPhoneAfter);

		ll_temp1 = (LinearLayout) findViewById(R.id.ll_temp1);
		ll_temp2 = (LinearLayout) findViewById(R.id.ll_temp2);
		ll_temp3 = (LinearLayout) findViewById(R.id.ll_temp3);

		ll_monbile_way = (LinearLayout) findViewById(R.id.ll_monbile_way);

		tv_forget_kefu = (TextView) findViewById(R.id.tv_forget_kefu);
		tv_forget_kefu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//ToastUtils.showShort(ForgotPwdTwoActivity.this, "dianjile ");

				//联系客服
				Intent nIntent=new Intent();						
				nIntent.setClass(ForgotPwdTwoActivity.this,LotteryTypeActivity.class);

				nIntent.putExtra("url",ForgotPwdTwoActivity.this.getResources().getString(R.string.kefu).replace("#", "&"));
				nIntent.putExtra("show", true);
				startActivity(nIntent);

			}
		});
		tv_mobile_number1 = (TextView) findViewById(R.id.tv_mobile_number1);

		tv_temp2_info = (TextView) findViewById(R.id.tv_temp2_info);

		et_code = (EditText) findViewById(R.id.et_code);

		et_new_password = (EditText) findViewById(R.id.et_new_password);
		et_sure_password = (EditText) findViewById(R.id.et_sure_password);
		et_new_password.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				String newPassword=et_new_password.getText().toString().trim();
				if ((!TextUtils.isEmpty(newPassword)) && Util.textNameTemp1(newPassword)) {
					ToastUtils.showShort(mContext, "密码不能全为数字");
					return;
				}
				if ((!TextUtils.isEmpty(newPassword)) && Util.isAllEnglish(newPassword)) {
					ToastUtils.showShort(mContext, "密码不能全为字母");
					return;
				}
				if (!Util.isPasswordValid_OnlyNumAndLeter(newPassword)||newPassword.length()<6 || newPassword.length()>15) {
					ToastUtils.showShort(mContext, "密码为6-15位字母和数字组成的密码");
					btn_verif.setEnabled(true);
					return;
				}
			}
		});

		btn_sure = (TextView) findViewById(R.id.btn_sure);
		btn_verif = (Button) findViewById(R.id.btn_verif);
		btn_verif.setOnClickListener(this);
		btn_new_password = (Button) findViewById(R.id.btn_new_password);
		btn_new_password.setOnClickListener(this);
		btn_sure_password = (Button) findViewById(R.id.btn_sure_password);
		btn_sure_password.setOnClickListener(this);
		if ((!TextUtils.isEmpty(forgetUserPhoneBeFore))
				&& (!TextUtils.isEmpty(forgetUserPhoneAfter))) {
			tv_mobile_number1.setText(forgetUserPhoneBeFore + "****"
					+ forgetUserPhoneAfter);
		}
		ll_monbile_way.setOnClickListener(this);
		btn_sure.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == btn_verif) {
			btn_verif.setEnabled(false);
			String mobile_number = et_mobile_number.getText().toString().trim();
			if (TextUtils.isEmpty(mobile_number)) {
				ToastUtils.showShort(mContext, "请完善手机号");
				et_mobile_number.requestFocus();
				btn_verif.setEnabled(true);
				return;
			}
			mobile = forgetUserPhoneBeFore + mobile_number
					+ forgetUserPhoneAfter;

			if (!Util.isMobileValid(mobile)) {
				ToastUtils.showShort(mContext, "手机号格式错误");
				et_mobile_number.requestFocus();
				btn_verif.setEnabled(true);
				return;
			}

			isTasking = true;
			ServiceUser.getInstance().ForgotLoginPwd_Temp2(mContext,mobile, userName, "0",
					
					null, new CallBack<UserResultData>() {
				@Override
				public void onSuccess(UserResultData userResultData) {
					mMyCountTime = new MyCountTime(mContext, 60000,
							1000, btn_verif, "重新获取", "", null, false);
					mMyCountTime.start();
					isTasking = false;
					btn_verif.setEnabled(true);
				}

				@Override
				public void onFailure(ErrorMsg errorMessage) {
					PostHttpInfoUtils.doPostFail(mContext, errorMessage, "获取失败");
					btn_verif.setEnabled(true);
					isTasking = false;
				}
			});
		}
		if (v == ll_monbile_way) {
			btn_verif.setEnabled(true);
			currentTemp = 2;
			viewChangeByClick(currentTemp);
			imgChangeByClick(currentTemp);
		}
		if (v == btn_sure && currentTemp == 2) {
			String code = et_code.getText().toString().trim();
			if (TextUtils.isEmpty(code)) {
				ToastUtils.showShort(mContext, "验证码不能为空");
				et_code.requestFocus();
				return;
			}

			String mobile_number = et_mobile_number.getText().toString().trim();
			if (TextUtils.isEmpty(mobile_number)) {
				ToastUtils.showShort(mContext, "请完善手机号");
				et_mobile_number.requestFocus();
				return;
			}
			mobile = forgetUserPhoneBeFore + mobile_number
					+ forgetUserPhoneAfter;

			if (!Util.isMobileValid(mobile)) {
				ToastUtils.showShort(mContext, "手机号格式错误");
				et_mobile_number.requestFocus();
				return;
			}
			isTasking = true;
			ServiceUser.getInstance().ForgotLoginPwd_Temp2(mContext,mobile, userName, "1",
					code, new CallBack<UserResultData>() {
				@Override
				public void onSuccess(UserResultData userResultData) {
					isTasking = false;
					currentTemp = 3;
					viewChangeByClick(currentTemp);
					imgChangeByClick(currentTemp);
				}

				@Override
				public void onFailure(ErrorMsg errorMessage) {
					PostHttpInfoUtils.doPostFail(mContext, errorMessage, "验证有误");
					EventBus.getDefault().post("setForgotBtn_verif_enable");
					isTasking = false;

				}
			});

		}
		if (v == btn_sure && currentTemp == 3) {
			String newPassword = et_new_password.getText().toString().trim();
			String surePassword = et_sure_password.getText().toString().trim();
			if (TextUtils.isEmpty(newPassword)) {
				ToastUtils.showShort(mContext, "新密码不能为空");
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
			if (newPassword.length()<6 || newPassword.length()>15) {
				ToastUtils.showShort(mContext, "密码为6-16位字符和密码");
				et_new_password.requestFocus();
				return;
			}
			if (TextUtils.isEmpty(surePassword)) {
				ToastUtils.showShort(mContext, "确认密码不能为空");
				et_sure_password.requestFocus();
				return;
			} else if (!newPassword.equals(surePassword)) {
				ToastUtils.showShort(mContext, "确认密码与新密码不符");
				et_sure_password.setText("");
				et_sure_password.requestFocus();
				return;
			}
			isTasking = true;
			ServiceUser.getInstance().ForgotLoginPwd_Temp3(mContext,newPassword, userName,
					new CallBack<UserResultData>() {

				@Override
				public void onSuccess(UserResultData t) {
					isTasking = false;
					ToastUtils.showShort(mContext, "设置新密码成功");
					finish();

				}

				@Override
				public void onFailure(ErrorMsg errorMessage) {
					PostHttpInfoUtils.doPostFail(mContext, errorMessage, "设置新密码失败");
					isTasking = false;

				}
			});

		}
		if (v==btn_new_password) {
			if ("隐藏".equals(btn_new_password.getText().toString().trim())) {
				et_new_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
				et_new_password.setSelection(et_new_password.length());
				btn_new_password.setText("显示");
				return ;
			}
			if ("显示".equals(btn_new_password.getText().toString().trim())) {
				et_new_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				et_new_password.setSelection(et_new_password.length());
				btn_new_password.setText("隐藏");
			}

		}
		if (v==btn_sure_password) {
			if ("隐藏".equals(btn_sure_password.getText().toString().trim())) {
				et_sure_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
				et_sure_password.setSelection(et_sure_password.length());
				btn_sure_password.setText("显示");
				return;
			}
			if ("显示".equals(btn_sure_password.getText().toString().trim())) {
				et_sure_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				et_sure_password.setSelection(et_sure_password.length());
				btn_sure_password.setText("隐藏");
			}

		}

	}

	private void viewChangeByClick(int currentTemp) {
		ll_temp1.setVisibility(View.GONE);
		ll_temp2.setVisibility(View.GONE);
		ll_temp3.setVisibility(View.GONE);
		btn_sure.setVisibility(View.GONE);
		tv_temp2_info.setVisibility(View.GONE);
		switch (currentTemp) {
		case 1:
			ll_temp1.setVisibility(View.VISIBLE);
			break;
		case 2:
			ll_temp2.setVisibility(View.VISIBLE);
			btn_sure.setVisibility(View.VISIBLE);
			tv_temp2_info.setVisibility(View.VISIBLE);
			break;
		case 3:
			ll_temp3.setVisibility(View.VISIBLE);
			btn_sure.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}

	}
	private void imgChangeByClick(int currentTemp) {
		Resources resources=mContext.getResources();
		img_temp1.setImageResource(R.mipmap.sstep1);
		img_temp2.setImageResource(R.mipmap.sstep2);
		img_temp3.setImageResource(R.mipmap.sstep3);
		switch (currentTemp) {
		case 1:
			img_temp1.setImageResource(R.mipmap.step1);
			break;
		case 2:
			img_temp2.setImageResource(R.mipmap.step2);
			break;
		case 3:
			img_temp3.setImageResource(R.mipmap.step3);
			break;

		default:
			break;
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	public void onEventMainThread(String event) {
		if (!TextUtils.isEmpty(event)) {
			if ("setForgotBtn_verif_enable".equals(event)) {
				btn_verif.setEnabled(true);
			}
		}
	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(KeyEvent.KEYCODE_BACK == keyCode){
			back();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	public void back(){
		if(currentTemp>1){
			imgChangeByClick(currentTemp-1);
		}
		ll_temp1.setVisibility(View.GONE);
		ll_temp2.setVisibility(View.GONE);
		ll_temp3.setVisibility(View.GONE);
		btn_sure.setVisibility(View.GONE);
		tv_temp2_info.setVisibility(View.GONE);
		if(currentTemp == 1) {
			Intent intent = new Intent(mContext,ForgotPwdActivity.class);
			startActivity(intent);
			finish();
		}else if(currentTemp == 2) {
			et_mobile_number.setText("");
			et_code.setText("");
			btn_verif.setText("重新获取");
			btn_verif.setEnabled(true);
			if(mMyCountTime!=null){
				mMyCountTime.cancel();
				mMyCountTime = null;
			}
			ll_temp1.setVisibility(View.VISIBLE);
			currentTemp = 1;
		}else if(currentTemp == 3) {
			et_new_password.setText("");
			et_sure_password.setText("");
			/*et_mobile_number.setText("");
			et_code.setText("");
			btn_verif.setText("重新获取");
			btn_verif.setEnabled(true);
			if(mMyCountTime!=null){
				mMyCountTime.cancel();
				mMyCountTime = null;
			}*/
			ll_temp2.setVisibility(View.VISIBLE);
			btn_sure.setVisibility(View.VISIBLE);
			tv_temp2_info.setVisibility(View.VISIBLE);
			currentTemp = 2;		
		}
	}
}
