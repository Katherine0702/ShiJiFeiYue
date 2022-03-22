package com.cshen.tiyu.activity.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cshen.tiyu.R;
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

public class FindLoginPwdActivity extends BaseActivity implements
		OnClickListener {

	private Context mContext;
	private boolean isTasking = false;
	private TextView btn_verif;
	private MyCountTime mMyCountTime;
	private String mobileTemp;
	private EditText et_new_password, et_sure_password, et_mobile, codeEt;

	private TextView btn_sure;
	private TextView tv_unget_yzm;// 收不到验证码
	private PopupWindow ppw;
	private String userName;//上个页面的用户名

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_pwd);

		TopViewLeft tv_head = (TopViewLeft) findViewById(R.id.tv_head);
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
				finish();
			}
		});

		mContext = this;
        userName=getIntent().getStringExtra("userName");
		initView();

	}

	private void initView() {
		et_new_password = (EditText) findViewById(R.id.et_new_password);
		// et_new_password.addTextChangedListener(new TextChangedListener());
		et_sure_password = (EditText) findViewById(R.id.et_sure_password);
		// et_sure_password.addTextChangedListener(new TextChangedListener());
		et_mobile = (EditText) findViewById(R.id.et_mobile);
		// et_mobile.addTextChangedListener(new TextChangedListener());
		codeEt = (EditText) findViewById(R.id.codeEt);
		// codeEt.addTextChangedListener(new TextChangedListener());
		btn_sure = (TextView) findViewById(R.id.btn_sure);

		btn_sure.setBackgroundResource(R.drawable.selector_redbutton);
		btn_sure.setEnabled(true);

		btn_sure.setOnClickListener(this);
		btn_verif = (TextView) findViewById(R.id.btn_verif);
		btn_verif.setOnClickListener(this);

		tv_unget_yzm = (TextView) findViewById(R.id.tv_unget_yzm);
		tv_unget_yzm.setOnClickListener(this);
		
		

	}

	@Override
	public void onClick(View v) {
		if (v == btn_verif) {
			String mobilebase = et_mobile.getText().toString().trim();
			mobileTemp = mobilebase;
			btn_verif.setEnabled(false);

			String newPassword = et_new_password.getText().toString().trim();
			String surePassword = et_sure_password.getText().toString().trim();

			if (TextUtils.isEmpty(newPassword)) {
				ToastUtils.showShort(mContext, "新密码为空");
				et_new_password.requestFocus();
				btn_verif.setEnabled(true);
				return;
			}
			if ((!TextUtils.isEmpty(newPassword))
					&& Util.textNameTemp1(newPassword)) {
				ToastUtils.showShort(mContext, "密码不能全为数字");
				et_new_password.requestFocus();
				btn_verif.setEnabled(true);
				return;
			}
			if ((!TextUtils.isEmpty(newPassword))
					&& Util.isAllEnglish(newPassword)) {
				ToastUtils.showShort(mContext, "密码不能全为字母");
				et_new_password.requestFocus();
				btn_verif.setEnabled(true);
				return;
			}
			if (!Util.isPasswordValid_OnlyNumAndLeter(newPassword)||newPassword.length()<6 || newPassword.length()>15) {
				ToastUtils.showShort(mContext, "密码为6-15位字母和数字组成的密码");
				et_new_password.requestFocus();
				btn_verif.setEnabled(true);
				return;
			}
			if (TextUtils.isEmpty(surePassword)) {
				ToastUtils.showShort(mContext, "确认密码为空");
				et_sure_password.requestFocus();
				btn_verif.setEnabled(true);
				return;
			} else if (!newPassword.equals(surePassword)) {
				ToastUtils.showShort(mContext, "确认密码与新密码不符");
				et_sure_password.setText("");
				et_sure_password.requestFocus();
				btn_verif.setEnabled(true);
				return;
			}
			if (TextUtils.isEmpty(mobilebase)) {
				ToastUtils.showShort(mContext, "手机号码不能为空");
				et_mobile.requestFocus();
				btn_verif.setEnabled(true);
				return;
			}

			if (!Util.isMobileValid(mobilebase)) {
				ToastUtils.showShort(mContext, "手机号格式不正确");
				et_mobile.requestFocus();
				btn_verif.setEnabled(true);
				return;
			}
			mobileTemp = mobilebase;
			isTasking = true;
			ServiceUser.getInstance().newPostVerifLoginWithYanZhengMa(mContext,
					mobilebase, new CallBack<UserResultData>() {
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
							PostHttpInfoUtils.doPostFail(mContext, errorMsg,
									"获取失败");
							isTasking = false;
							btn_verif.setEnabled(true);
						}
					});

		}
		if (v == btn_sure) {
			String newPassword = et_new_password.getText().toString().trim();
			String surePassword = et_sure_password.getText().toString().trim();
			String mobile = et_mobile.getText().toString().trim();
			String code = codeEt.getText().toString().trim();
			if (TextUtils.isEmpty(newPassword)) {
				ToastUtils.showShort(mContext, "新密码为空");
				et_new_password.requestFocus();
				return;
			}
			if ((!TextUtils.isEmpty(newPassword))
					&& Util.textNameTemp1(newPassword)) {
				ToastUtils.showShort(mContext, "密码不能全为数字");
				et_new_password.requestFocus();
				return;
			}
			if ((!TextUtils.isEmpty(newPassword))
					&& Util.isAllEnglish(newPassword)) {
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
				ToastUtils.showShort(mContext, "确认密码为空");
				et_sure_password.requestFocus();
				return;
			} else if (!newPassword.equals(surePassword)) {
				ToastUtils.showShort(mContext, "确认密码与新密码不符");
				et_sure_password.setText("");
				et_sure_password.requestFocus();
				return;
			}
			if (TextUtils.isEmpty(mobile)) {
				ToastUtils.showShort(mContext, "手机号码不能为空");
				et_mobile.requestFocus();
				btn_verif.setEnabled(true);
				return;
			}

			if (!mobile.equals(mobileTemp)) {
				ToastUtils.showShort(mContext, "手机号与获取验证码时输入的手机号不一致");
				et_mobile.requestFocus();
				btn_verif.setEnabled(true);
				return;
			}
			if (TextUtils.isEmpty(code)) {
				ToastUtils.showShort(mContext, "验证码不能为空");
				codeEt.requestFocus();
				return;
			}
			isTasking = true;
			ServiceUser.getInstance().findPwd(mContext, newPassword, mobile,
					code, new CallBack<String>() {
						@Override
						public void onSuccess(String s) {

							ToastUtils.showShort(mContext, "成功找回密码");
							isTasking = false;
							finish();
						}

						@Override
						public void onFailure(ErrorMsg errorMsg) {
							PostHttpInfoUtils.doPostFail(mContext, errorMsg,
									"失败");
							isTasking = false;
							btn_verif.setEnabled(true);

						}
					});
		}

		if (v.getId() == R.id.tv_unget_yzm) {// 收不到验证码
			showFindPwdByProblemPPW();
		}

	}

	// 显示安全问题的pupupwindow
	private void showFindPwdByProblemPPW() {
		if (ppw != null && ppw.isShowing()) {
			ppw.dismiss();
		} else {
			View view = getLayoutInflater().inflate(R.layout.ppw_unget_yzm,
					null);
			Button btn_find=(Button) view.findViewById(R.id.btn_ppw_find);
			Button btn_cancle=(Button) view.findViewById(R.id.btn_ppw_cancle);
			ppw = new PopupWindow(view, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			ppw.setBackgroundDrawable(new BitmapDrawable());
			 //因为某些机型是虚拟按键的,所以要加上以下设置防止挡住按键.
	        ppw.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//			ppw.setAnimationStyle(R.style.PopupAnimation);
			ppw.update();
			ppw.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
			ppw.setTouchable(true); // 设置popupwindow可点击
			ppw.setOutsideTouchable(true); // 设置popupwindow外部可点击
			ppw.setFocusable(true); // 获取焦点
			// 设置popupwindow的位置
			ppw.showAtLocation(findViewById(R.id.layout_find_pwd),
					Gravity.BOTTOM, 0, 0);
			ppw.setTouchInterceptor(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// 如果点击了popupwindow的外部，popupwindow也会消失
					if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
						ppw.dismiss();
						return true;
					}
					return false;
				}
			});
			
			btn_cancle.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ppw.dismiss();
				}
			});
			btn_find.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent =new Intent(FindLoginPwdActivity.this,ForgotPwdActivity.class);
					if (userName==null) {
						userName="";
					}
					intent.putExtra("userName", userName);
					startActivity(intent);
					ppw.dismiss();
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

	// class TextChangedListener implements TextWatcher {
	//
	// @Override
	// public void onTextChanged(CharSequence s, int start, int before,
	// int count) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void beforeTextChanged(CharSequence s, int start, int count,
	// int after) {
	//
	// }
	//
	// @Override
	// public void afterTextChanged(Editable s) {
	// String newPassword = et_new_password.getText().toString().trim();
	// String surePassword = et_sure_password.getText().toString().trim();
	// String mobile = et_mobile.getText().toString().trim();
	// String code = codeEt.getText().toString().trim();
	// if (!TextUtils.isEmpty(newPassword)
	// && !TextUtils.isEmpty(surePassword)
	// && !TextUtils.isEmpty(mobile)
	// && !TextUtils.isEmpty(code)) {
	// btn_sure.setBackgroundResource(R.drawable.selector_redbutton);
	// btn_sure.setEnabled(true);
	// } else {
	// btn_sure.setBackgroundResource(R.drawable.ic_gray_button_normal);
	// btn_sure.setEnabled(false);
	// }
	//
	// }
	// }

}
