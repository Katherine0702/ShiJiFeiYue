package com.cshen.tiyu.activity.login;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.MainActivity;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.login.AccountList;
import com.cshen.tiyu.domain.login.User;
import com.cshen.tiyu.domain.login.UserResultData;
import com.cshen.tiyu.domain.login.UserTime;
import com.cshen.tiyu.net.https.ServiceUser;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.utils.PostHttpInfoUtils;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.utils.Util;
import com.cshen.tiyu.widget.MyCountTime;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;

public class LoginWithYanZhengMaActivity extends BaseActivity implements
OnClickListener, OnFocusChangeListener {
    public static final int BACKCLOSE = 1;
	private LoginWithYanZhengMaActivity mContext;
	private EditText mUsernameView;//手机号码
	private EditText mVerificationcodeView;//验证码
	private View mProgressView;//加载显示
	private View mLoginFormView;
	private boolean isTasking = false;
	private Button  btn_verif;
	private MyCountTime mMyCountTime;
	private String initMobile;
	TopViewLeft tv_head;
	String requestName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_with_yanzhengma);

		requestName=getIntent().getStringExtra("requestName");
		mContext = this;
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

		mUsernameView = (EditText) findViewById(R.id.username);
		mUsernameView.setOnFocusChangeListener(this);

		mVerificationcodeView = (EditText) findViewById(R.id.edt_verificationcode);
		btn_verif = (Button) findViewById(R.id.btn_verif);
		btn_verif.setOnClickListener(this);

		TextView mrRegisterButton = (TextView) findViewById(R.id.register_button);
		mrRegisterButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				attemptRegister();
			}
		});

		mLoginFormView = findViewById(R.id.login_form);
		mProgressView = findViewById(R.id.login_progress);
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid username, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptRegister() {
		if (isTasking) {
			return;
		}
		final String username = mUsernameView.getText().toString().trim();
		String edt_verificationcode = mVerificationcodeView.getText()
				.toString().trim();

		if (TextUtils.isEmpty(username)) {
			ToastUtils.showShort(mContext, "手机号码不能为空");
			mUsernameView.requestFocus();
			return;
		}
		if (!Util.isMobileValid(username)) {
			ToastUtils.showShort(mContext, "手机号格式不正确");
			mUsernameView.requestFocus();
			return;
		}
		if (!username.equals(initMobile)) {
			ToastUtils.showShort(mContext, "请获取验证码");
			mUsernameView.requestFocus();
			return;
		}
		if (TextUtils.isEmpty(edt_verificationcode)) {
			ToastUtils.showShort(mContext, "验证码不能为空");
			mVerificationcodeView.requestFocus();
			return;
		}

		showProgress(true);
		isTasking = true;
		ServiceUser.getInstance().PostLoginWithYanZhengMa(mContext,username,
				edt_verificationcode,
				new CallBack<AccountList>() {
			@Override
			public void onSuccess(AccountList userResultData) {
				showProgress(false);
				isTasking = false;// 任务标记访问结束后
				if(userResultData!=null){
					if("0".equals(userResultData.getIsNew())){//旧的
						PreferenceUtil.putBoolean(mContext, ConstantsBase.ISNEW, false);
					}else{
						PreferenceUtil.putBoolean(mContext, ConstantsBase.ISNEW, true);
					}
					ArrayList<UserTime> userlst= userResultData.getUserlst();
					if(userlst!=null&&userlst.size()>0){
						if("0".equals(userResultData.getIsNew())
								&&userlst.size()>1){
							Intent toChooseOne = new Intent(mContext,LoginChooseOneActivity.class);
							Bundle bundle = new Bundle();   
							bundle.putSerializable("names", userResultData.getUserlst());
							bundle.putString("userPhone", username);					
							if (!TextUtils.isEmpty(requestName)) {
								bundle.putString("requestName",requestName);
								toChooseOne.putExtras(bundle);
								startActivityForResult(toChooseOne,BACKCLOSE);
							}else{
								toChooseOne.putExtras(bundle);
								startActivity(toChooseOne);	
								finish();
							}
							
						}else{
							if(userlst.get(0)!=null){
								User use = userlst.get(0).getUser();
								PreferenceUtil.putString(mContext, "username",
										use.getUserName());
								ConstantsBase.setUser(use);
								PreferenceUtil.putBoolean(mContext, "hasLogin", true);
								PreferenceUtil.putBoolean(mContext, "isExitWay", false);	
								if (!TextUtils.isEmpty(requestName)) {
									Intent intent=new Intent();
									if("intentLogin".equals(requestName)){
										setResult(RESULT_OK, intent);
									}
								}else{
									Intent toMain = new Intent(mContext,MainActivity.class);
									toMain.putExtra("hasLogin", "yes");// 标记
									startActivity(toMain);	
								}																			
								finish();
							}

						}
					}else{
						ToastUtils.showShort(mContext,"未获得数据");
					}

				}else{
					ToastUtils.showShort(mContext,"未获得数据");
				}
				isTasking = false;
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				PostHttpInfoUtils.doPostFail(mContext, errorMessage,
						"登录失败");
				showProgress(false);
				isTasking = false;
			}
		});
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void showProgress(final boolean show) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
			.alpha(show ? 0 : 1)
			.setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					mLoginFormView.setVisibility(show ? View.GONE
							: View.VISIBLE);
				}
			});

			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mProgressView.animate().setDuration(shortAnimTime)
			.alpha(show ? 1 : 0)
			.setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					mProgressView.setVisibility(show ? View.VISIBLE
							: View.GONE);
				}
			});
		} else {
			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		if (v == btn_verif) {
			btn_verif.setEnabled(false);
			final String mobile = mUsernameView.getText().toString().trim();
			if (TextUtils.isEmpty(mobile)) {
				ToastUtils.showShort(mContext, "手机号码不能为空");
				mUsernameView.requestFocus();
				btn_verif.setEnabled(true);
				return;
			} else if (!Util.isMobileValid(mobile)) {
				ToastUtils.showShort(mContext, "手机号格式不正确");
				mUsernameView.requestFocus();
				btn_verif.setEnabled(true);
				return;
			} 
			clickBtnVerIf(mobile);

		}
	}

	private void clickBtnVerIf(final String mobile) {
		isTasking = true;
		ServiceUser.getInstance().PostVerifLoginWithYanZhengMa(mContext,mobile,
				new CallBack<UserResultData>() {
			@Override
			public void onSuccess(UserResultData userResultData) {
				mMyCountTime = new MyCountTime(mContext, 60000, 1000,
						btn_verif, "重新获取", "", null, false);
				mMyCountTime.start();
				isTasking = false;
				btn_verif.setEnabled(true);
				initMobile = mobile;
			}

			@Override
			public void onFailure(ErrorMsg errorMsg) {
				PostHttpInfoUtils
				.doPostFail(mContext, errorMsg, "获取失败");
				isTasking = false;
				btn_verif.setEnabled(true);
			}
		});
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (v == mUsernameView) {
			String mobile = mUsernameView.getText().toString().trim();
			if ((!TextUtils.isEmpty(mobile)) && !Util.isMobileValid(mobile)) {
				ToastUtils.showShort(mContext, "手机号格式不正确");
				return;
			}
		}
	}@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case BACKCLOSE:
				if("intentLogin".equals(requestName)){
					Intent intent=new Intent();
					setResult(RESULT_OK, intent);
					mContext.finish();
				}
				break;
			}
		}
	} 
}

