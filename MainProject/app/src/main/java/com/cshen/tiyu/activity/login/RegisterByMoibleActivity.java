package com.cshen.tiyu.activity.login;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.pay.TZXYActivity;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.deprecated.ForgetUserMobileResultData;
import com.cshen.tiyu.domain.login.UserResultData;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.net.https.ServiceMain;
import com.cshen.tiyu.net.https.ServiceUser;
import com.cshen.tiyu.utils.PostHttpInfoUtils;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.utils.Util;
import com.cshen.tiyu.widget.MyCountTime;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;
import com.cshen.tiyu.zx.ZXMainActivity;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.greenrobot.event.EventBus;

/**
 * A login screen that offers login via username/password and via Google+ sign
 * in.
 * <p/>
 * ************ IMPORTANT SETUP NOTES: ************ In order for Google+ sign in
 * to work with your app, you must first go to:
 * https://developers.google.com/+/mobile
 * /android/getting-started#step_1_enable_the_google_api and follow the steps in
 * "Step 1" to create an OAuth 2.0 client for your package.
 */
public class RegisterByMoibleActivity extends BaseActivity implements
OnClickListener, OnFocusChangeListener {
	private EditText mMobilenameView,mUsernameView;
	private EditText mPasswordView, mVerificationcodeView;
	private View mProgressView;
	private View mLoginFormView;
	private RegisterByMoibleActivity mContext;
	private boolean isTasking = false;
	private TextView mTextTiaoKuan;
	private Button btn_hide;
	private TextView btn_verif;
	private boolean isPwdChecked = true;
	private MyCountTime mMyCountTime;
	private TextView tv_tzxy;
	private String initMobile;
	TopViewLeft tv_head;
	private CheckBox tongyi;
	boolean isTongyi = true;
	private LinearLayout layout_tiaokuan;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_bymobile);

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
		TextView textView = (TextView)findViewById(R.id.loginnophone);   
		textView.setText(Html.fromHtml("<u>" + "免手机号码注册" + "</u>" ));
		tv_tzxy = (TextView) findViewById(R.id.tv_tzxy);
		tv_tzxy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, TZXYActivity.class));

			}
		});
		TextView tv_login = (TextView)findViewById(R.id.tv_login);   
		tv_login.setVisibility(View.VISIBLE);
		tv_login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if("activity".equals(getIntent().getStringExtra("from"))){
					startActivity(new Intent(mContext, LoginActivity.class));
				}
				mContext.finish();
			}
		});

		// Set up the login form.
		mMobilenameView = (EditText) findViewById(R.id.mobilename);
		mMobilenameView.setOnFocusChangeListener(this);
		mUsernameView = (EditText) findViewById(R.id.username);
		mUsernameView.setOnFocusChangeListener(this);
		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView.setOnFocusChangeListener(this);

		mPasswordView
		.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int id,
					KeyEvent keyEvent) {
				if (id == KeyEvent.ACTION_DOWN
						|| id == EditorInfo.IME_ACTION_NONE) {
					attemptRegister();
					return true;
				}
				return false;
			}
		});
		mVerificationcodeView = (EditText) findViewById(R.id.edt_verificationcode);
		btn_hide = (Button) findViewById(R.id.btn_hide);
		LayoutParams para = btn_hide.getLayoutParams();//获取按钮的布局
		para.width=52;//修改宽度
		para.height=32;//修改高度
		btn_hide.setLayoutParams(para); //设置修改后的布局。
		btn_hide.setBackgroundResource(R.mipmap.eyes96_01b);
		btn_verif = (TextView) findViewById(R.id.btn_verif);
		btn_hide.setOnClickListener(this);
		btn_verif.setOnClickListener(this);
		tongyi = (CheckBox) findViewById(R.id.text_tongyi);
		tongyi.setOnCheckedChangeListener(new OnCheckedChangeListener(){  
			@Override  
			public void onCheckedChanged(CompoundButton button, boolean isChecked){  
				if(isChecked){  
					isTongyi = true;
				} else{  
					isTongyi = false;
				}
			}  
		}); 
		TextView mrRegisterButton = (TextView) findViewById(R.id.register_button);
		mrRegisterButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				attemptRegister();
			}
		});

		mLoginFormView = findViewById(R.id.login_form);
		mProgressView = findViewById(R.id.login_progress);
		
		layout_tiaokuan=(LinearLayout) findViewById(R.id.layout_tiaokuan);
		if (PreferenceUtil.getBoolean(this, "isMaJia")) {
			layout_tiaokuan.setVisibility(View.GONE);
		}
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid username, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptRegister() {
		/*Intent registerSuccess = new Intent(mContext,RegisterSuccessActivity.class);
		registerSuccess.putExtra("fromOther", getIntent().getStringExtra("fromOther"));
		startActivity(registerSuccess);
		finish();
		return;*/


		if (isTasking) {
			return;
		}
		// Store values at the time of the login attempt.
		final String mobilename = mMobilenameView.getText().toString();
		final String username = mUsernameView.getText().toString();
		final String password = mPasswordView.getText().toString();
		String edt_verificationcode = mVerificationcodeView.getText()
				.toString();

		if (TextUtils.isEmpty(mobilename)) {
			ToastUtils.showShort(mContext, "手机号码不能为空");
			mMobilenameView.requestFocus();
			return;
		}
		if (!Util.isMobileValid(mobilename)) {
			ToastUtils.showShort(mContext, "手机号格式不正确");
			mMobilenameView.requestFocus();
			return;
		}
		if (TextUtils.isEmpty(username)) {
			ToastUtils.showShort(mContext, "用户名不能为空");
			mUsernameView.requestFocus();
			return;
		}
		if (username.length()<3||username.length()>20) {
			ToastUtils.showShort(mContext, "用户名3-20个字符");
			mUsernameView.requestFocus();
			return;
		}
		if (!Util.textNameTemp(username)) {
			ToastUtils.showShort(mContext, "用户名只可包含汉字、数字、字母");
			mUsernameView.requestFocus();
			return;
		}
		if (TextUtils.isEmpty(password)) {
			ToastUtils.showShort(mContext, "密码不能为空");
			mPasswordView.requestFocus();
			return;
		}
		if ((!TextUtils.isEmpty(password)) && Util.textNameTemp1(password)) {
			ToastUtils.showShort(mContext, "密码不能全为数字");
			mPasswordView.requestFocus();
			return;
		}
		if ((!TextUtils.isEmpty(password)) && Util.isAllEnglish(password)) {
			ToastUtils.showShort(mContext, "密码不能全为字母");
			mPasswordView.requestFocus();
			return;
		}
		if (!Util.isPasswordValid_OnlyNumAndLeter(password)||password.length()<6 || password.length()>15) {
			ToastUtils.showShort(mContext, "密码为6-15位字母和数字组成的密码");
			mPasswordView.requestFocus();
			btn_verif.setEnabled(true);
			return;
		}
		if (!mobilename.equals(initMobile)) {
			ToastUtils.showShort(mContext, "请获取验证码");
			mMobilenameView.requestFocus();
			return;
		}
		if (TextUtils.isEmpty(edt_verificationcode)) {
			ToastUtils.showShort(mContext, "验证码不能为空");
			mVerificationcodeView.requestFocus();
			return;
		}
		if(!isTongyi){
			ToastUtils.showShort(mContext,mContext.getResources().getString(R.string.tiaokuan));
			return;
		}
		// Show a progress spinner, and kick off a background task to
		// perform the user login attempt.
		showProgress(true);
		isTasking = true;
		ServiceUser.getInstance().PostRegistByMobile(mContext,mobilename,username, password,
				edt_verificationcode,
				new CallBack<UserResultData>() {
			@Override
			public void onSuccess(UserResultData userResultData) {
				PreferenceUtil.putString(mContext, "username", username);
				showProgress(false);
				ConstantsBase.setUser(userResultData.user);
				isTasking = false;
				// 任务标记访问结束后
				// 存放你已经登入的信息
				PreferenceUtil.putBoolean(mContext, "hasLogin", true);
				PreferenceUtil.putBoolean(mContext, "isExitWay", false);

				String deviceToken = PreferenceUtil.getString(mContext, "deviceToken");
				if (!TextUtils.isEmpty(deviceToken)) {
					setDeviceToken(deviceToken);
				}
				addPushAlias(userResultData.user.userId);
				
				if (PreferenceUtil.getBoolean(RegisterByMoibleActivity.this, "isMaJia")) {
					Intent intent = new Intent(mContext,ZXMainActivity.class);
					intent.putExtra("hasLogin", "yes");// 标记
					startActivity(intent);
				}else {
					Intent registerSuccess = new Intent(mContext,
							RegisterSuccessActivity.class);
					registerSuccess.putExtra("fromOther", getIntent().getStringExtra("fromOther"));
					startActivity(registerSuccess);

					EventBus.getDefault().post("clearSign");// gengxingqichi
					EventBus.getDefault().post("initFiveLeagueFromRegt");// 获取五大联赛登录状态,区别于登录是因为注册完后首页是visible的
				}
				
				finish();
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				PostHttpInfoUtils.doPostFail(mContext, errorMessage,
						"注册失败");
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
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
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
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		if (v == btn_hide) {
			if (isPwdChecked) {
				// 否则隐藏密码
				mPasswordView
				.setTransformationMethod(PasswordTransformationMethod
						.getInstance());
				mPasswordView.setSelection(mPasswordView.length());
				LayoutParams para = btn_hide.getLayoutParams();//获取按钮的布局
				para.width=52;//修改宽度
				para.height=32;//修改高度
				btn_hide.setLayoutParams(para); //设置修改后的布局。
				btn_hide.setBackgroundResource(R.mipmap.eyes96_01);

				isPwdChecked = false;
			} else {
				// 如果选中，显示密码
				mPasswordView
				.setTransformationMethod(HideReturnsTransformationMethod
						.getInstance());
				mPasswordView.setSelection(mPasswordView.length());
				LayoutParams para = btn_hide.getLayoutParams();//获取按钮的布局
				para.width=52;//修改宽度
				para.height=32;//修改高度
				btn_hide.setLayoutParams(para); //设置修改后的布局。
				btn_hide.setBackgroundResource(R.mipmap.eyes96_01b);
				isPwdChecked = true;
			}
		} else if (v == btn_verif) {

			btn_verif.setEnabled(false);
			final String name = mUsernameView.getText().toString().trim();
			final String mobile = mMobilenameView.getText().toString().trim();
			String password = mPasswordView.getText().toString().trim();
			if (TextUtils.isEmpty(mobile)) {
				ToastUtils.showShort(mContext, "手机号码不能为空");
				mMobilenameView.requestFocus();
				btn_verif.setEnabled(true);
				return;
			} else if (!Util.isMobileValid(mobile)) {
				ToastUtils.showShort(mContext, "手机号格式不正确");
				mMobilenameView.requestFocus();
				btn_verif.setEnabled(true);
				return;
			}else if (TextUtils.isEmpty(name)) {
				ToastUtils.showShort(mContext, "用户名不能为空");
				mUsernameView.requestFocus();
				btn_verif.setEnabled(true);
				return;
			}  else if (name.length()<3||name.length()>20) {
				ToastUtils.showShort(mContext, "用户名3-20个字符");
				mUsernameView.requestFocus();
				btn_verif.setEnabled(true);
				return;
			} else if (!Util.textNameTemp(name)) {
				ToastUtils.showShort(mContext, "用户名只可包含汉字、数字、字母");
				mUsernameView.requestFocus();
				btn_verif.setEnabled(true);
				return;
			} else if (TextUtils.isEmpty(password)) {
				ToastUtils.showShort(mContext, "密码不能为空");
				mPasswordView.requestFocus();
				btn_verif.setEnabled(true);
				return;
			} else if ((!TextUtils.isEmpty(password)) && Util.textNameTemp1(password)) {
				ToastUtils.showShort(mContext, "密码不能全为数字");
				mPasswordView.requestFocus();
				btn_verif.setEnabled(true);
				return;
			}else if ((!TextUtils.isEmpty(password)) && Util.isAllEnglish(password)) {
				ToastUtils.showShort(mContext, "密码不能全为字母");
				mPasswordView.requestFocus();
				btn_verif.setEnabled(true);
				return;
			}else if (!Util.isPasswordValid_OnlyNumAndLeter(password)||password.length()<6 || password.length()>15) {
				ToastUtils.showShort(mContext, "密码为6-15位字母和数字组成的密码");
				mPasswordView.requestFocus();
				btn_verif.setEnabled(true);
				return;
			}  
			initMobile = mobile;
			isTasking = true;
			ServiceUser.getInstance().isExitUser(mContext,name,mobile,
					new CallBack<ForgetUserMobileResultData>() {
				@Override
				public void onSuccess(ForgetUserMobileResultData mResult) {
					isTasking = false;
					clickBtnVerIf(mobile);
				}

				@Override
				public void onFailure(ErrorMsg errorMessage) {
					isTasking = false;// 任务标记访问结束后
					PostHttpInfoUtils.doPostFail(mContext, errorMessage, "亲，该账号已存在");
					mMobilenameView.requestFocus();
					btn_verif.setEnabled(true);
				}
			});

		}
	}

	//获取中奖通知开关状态
	private void queryNoticeFlag() {
		ServiceUser.getInstance().getQueryNoticeFlag(mContext, new CallBack<String>() {

			@Override
			public void onSuccess(String noticeFlag) {
				if (noticeFlag.equals("true")) {//开
					PreferenceUtil.putBoolean(mContext, "isOpenNotice", true);
				}else {//关
					PreferenceUtil.putBoolean(mContext, "isOpenNotice", false);
				}
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				PreferenceUtil.putBoolean(mContext, "isOpenNotice", true);//获取不到，默认开
			}
		});
	}

	@SuppressLint("NewApi")
	private void clickBtnVerIf(String mobile) {
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
		if (v == mMobilenameView) {
			String mobile = mMobilenameView.getText().toString().trim();
			if ((!TextUtils.isEmpty(mobile)) && !Util.isMobileValid(mobile)) {
				ToastUtils.showShort(mContext, "手机号格式不正确");
				return;
			}
		}
		if (v == mPasswordView) {
			String password = mPasswordView.getText().toString().trim();
			if (!TextUtils.isEmpty(password)) {
				if ( Util.textNameTemp1(password)) {
					ToastUtils.showShort(mContext, "密码不能全为数字");
					return;
				}
				if ( Util.isAllEnglish(password)) {
					ToastUtils.showShort(mContext, "密码不能全为字母");
					return;
				}
				if (password.length()<6 || password.length()>15) {
					ToastUtils.showShort(mContext, "密码为6-16位字符和密码");
					return;
				}
			}


		}
	}
	public void setDeviceToken(String deviceToken){
		ServiceMain.getInstance().getSaveDeviceToken(mContext, deviceToken,new CallBack<String>() {

			@Override
			public void onSuccess(String t) {

			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
			}
		});
	}
	private void addPushAlias(String userId) {
		PushAgent.getInstance(mContext).deleteAlias(userId, ConstantsBase.AliasType, new UTrack.ICallBack() {

			@Override
			public void onMessage(boolean arg0, String arg1) {
			}
		});
		PushAgent.getInstance(mContext).addAlias(userId, ConstantsBase.AliasType,new UTrack.ICallBack() {

			@Override
			public void onMessage(boolean arg0, String arg1) {
			}
		});
	}
    
}
