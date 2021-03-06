package com.cshen.tiyu.activity.login;

import com.cshen.tiyu.MainActivity;
import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.mian4.personcenter.setting.binding.BindingPhoneActivity;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.login.UserResultData;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.net.https.ServiceMain;
import com.cshen.tiyu.net.https.ServiceUser;
import com.cshen.tiyu.utils.PostHttpInfoUtils;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.SecurityUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;
import com.cshen.tiyu.zx.ZXMainActivity;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import de.greenrobot.event.EventBus;

public class LoginActivity extends BaseActivity {

	public static final int BACKCLOSE = 1;
	// UI references.
	private TextView text_forgot,loginyzm;
	private EditText mUsernameView;
	private EditText mPasswordView;
	private ImageView delete;
	private View mProgressView;
	private View mLoginFormView;
	private TopViewLeft mTopViewLeft;
	private LoginActivity mContext;
	private boolean isTasking = false;
	private String requestName,from;
	private boolean isToOrderDetail=false;//????????????????????????????????????
	private static final int SDK_AUTH_FLAG = 1;
	private boolean isMaJia;//??????????????????
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_AUTH_FLAG: {
				try{
					AuthResult authResult = new AuthResult((String) msg.obj);

					String resultStatus = authResult.getResultStatus();
					// ??????resultStatus ??????9000??????result_code ??????200?????????????????????????????????????????????????????????????????????????????????
					if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
						zfbLogin(authResult.getAuthCode());
					}else{
						// ?????????????????????????????????
						if(!TextUtils.isEmpty(authResult.getMemo())){
							Toast.makeText(LoginActivity.this,authResult.getMemo(),
									Toast.LENGTH_SHORT).show();
						}
					}
				}catch(Exception e){
					e.printStackTrace();
					Toast.makeText(LoginActivity.this, "????????????" ,
							Toast.LENGTH_SHORT).show();
				}
				break;
			}
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		mContext = this;
		requestName=getIntent().getStringExtra("requestName");
		from=getIntent().getStringExtra("from");
		isToOrderDetail=getIntent().getBooleanExtra("isOrderDetail", false);
		isMaJia=PreferenceUtil.getBoolean(mContext, "isMaJia");
		
		initHead();
		
		// Set up the login form.
		text_forgot=(TextView) findViewById(R.id.text_forgot);
		text_forgot.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent=new Intent(mContext, FindLoginPwdActivity.class);
				intent.putExtra("userName",mUsernameView.getText().toString());
				startActivity(intent);
			}
		});
		loginyzm=(TextView) findViewById(R.id.loginyzm);
		loginyzm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent=new Intent(mContext, LoginWithYanZhengMaActivity.class);
				if (!TextUtils.isEmpty(requestName)) {
					intent.putExtra("requestName",requestName);
					startActivityForResult(intent,BACKCLOSE);
				}else{
					startActivity(intent);
				}

			}
		});
		mUsernameView = (EditText) findViewById(R.id.username);

		mPasswordView = (EditText) findViewById(R.id.password);
		delete = (ImageView) findViewById(R.id.delete);
		delete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mPasswordView.setText("");
			}
		});
		mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int id,
					KeyEvent keyEvent) {
				if (id == KeyEvent.ACTION_DOWN
						|| id == EditorInfo.IME_ACTION_NONE) {
					attemptLogin();
					return true;
				}
				return false;
			}
		});
		mPasswordView.addTextChangedListener(new TextWatcher(){  
			@Override  
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}  

			@Override  
			public void onTextChanged(CharSequence s, int start, int before, int count) {}  

			@Override  
			public void afterTextChanged(Editable s) {  
				String ee = s.toString();
				if(ee!=null&&ee.length()>0){
					delete.setVisibility(View.VISIBLE);
				}else{
					delete.setVisibility(View.GONE);
				}
			}  
		}); 
		TextView mSignInButton = (TextView) findViewById(R.id.sign_in_button);
		mSignInButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptLogin();
			}
		});
		Button mrRegisterButton = (Button) findViewById(R.id.register_button);
		mrRegisterButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,
						RegisterByMoibleActivity.class);
				intent.putExtra("from", "login");
				intent.putExtra("fromOther", from);
				startActivity(intent);
			}
		});

		mLoginFormView = findViewById(R.id.login_form);
		mProgressView = findViewById(R.id.login_progress);
		String username = PreferenceUtil.getString(mContext, "username");
		if (!TextUtils.isEmpty(username)) {
			mUsernameView.setText(username);
			mUsernameView.setSelection(username.length());
		}
	}
	// ????????????????????????
	private void initHead() {
		// TODO ???????????????????????????
		mTopViewLeft = (TopViewLeft) findViewById(R.id.top_head);//

		mTopViewLeft.setResourceVisiable(true, false, false);
		mTopViewLeft.setTopClickItemListener(new TopClickItemListener() {

			@Override
			public void clickLoginView(View view) {
				// TODO ???????????????????????????

			}

			@Override
			public void clickContactView(View view) {
				// TODO ???????????????????????????

			}

			@Override
			public void clickBackImage(View view) {
				// TODO ???????????????????????????
				finish();// ????????? ?????????activity ??????
				if (TextUtils.isEmpty(requestName)) {
					Intent intent;
					if (isMaJia) {//?????????
						intent=new Intent(mContext, ZXMainActivity.class);					
					}else {
						intent=new Intent(mContext, MainActivity.class);					
					}
					intent.putExtra("hasLogin", "cancel");
					startActivity(intent);	

				}
			}
		});

	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid username, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (isTasking) {// ??????????????????????????????
			return;
		}
		// Reset errors.
		// Store values at the time of the login attempt.
		final String username = mUsernameView.getText().toString();
		final String password = mPasswordView.getText().toString();


		// Check for a valid password, if the user entered one.
		if (TextUtils.isEmpty(password)) {
			ToastUtils.showShort(mContext, "??????????????????????????????");
			mPasswordView.requestFocus();
			return;
		} 

		// Check for a valid username address.
		if (TextUtils.isEmpty(username)) {
			ToastUtils.showShort(mContext, "?????????????????????????????????");
			mUsernameView.requestFocus();
			return;
		}
		// ?????????????????? ??????

		showProgress(true);
		isTasking = true;
		ServiceUser.getInstance().PostLogin(mContext,username, SecurityUtil.md5(password).toUpperCase(),
				new CallBack<UserResultData>() {
			@Override
			public void onSuccess(UserResultData userResultData) {
				ToastUtils.showShort(mContext, "???????????????");
				PreferenceUtil.putString(mContext, "username",username);
				String oldName = PreferenceUtil.getString(mContext, "useroldname");
				if(!username.equals(oldName)){
					if (isMaJia) {
						MyDbUtils.removeAllCurrentHistoryData();
					}else {
						MyDbUtils.removeAllCurrentMessageData();
					}
					PreferenceUtil.putString(mContext, "useroldname",username);
				}
				showProgress(false);
				ConstantsBase.setUser(userResultData.user);
				isTasking = false;// ???????????????????????????
				// ??????????????????????????????
				PreferenceUtil.putBoolean(mContext, "hasLogin", true);
				PreferenceUtil.putBoolean(mContext, "isExitWay", false);

				PreferenceUtil.putBoolean(mContext, "hasSafeCertification", userResultData.user.isComplete);//????????????
				queryNoticeFlag();//????????????????????????

				String deviceToken = PreferenceUtil.getString(mContext, "deviceToken");
				if (!TextUtils.isEmpty(deviceToken)) {
					setDeviceToken(deviceToken);
				}
				addPushAlias(userResultData.user.userId);

				if (!TextUtils.isEmpty(requestName)) {
					Intent intent=new Intent();
					if("intentLogin".equals(requestName)){
						setResult(RESULT_OK, intent);
					}else{
						setResult(1, intent);
					}
				}else{
					Intent intent;
					if (isMaJia) {//?????????
						intent = new Intent(mContext,ZXMainActivity.class);	
					}else {
						intent = new Intent(mContext,MainActivity.class);
						if (isToOrderDetail) {//????????????????????????
							intent.putExtra("todo", "toOrderDetail");
							intent.putExtra("schemeId", getIntent().getStringExtra("schemeId"));
							intent.putExtra("lotteryId", getIntent().getStringExtra("lotteryId"));
						}		

					}
					intent.putExtra("hasLogin", "yes");// ??????
					startActivity(intent);


				}
				if (!isMaJia) {
					EventBus.getDefault().post("clearSign");// gengxingqichi
					EventBus.getDefault().post("initFiveLeague");// ??????????????????????????????
				}
				finish();

			}



			


			@Override
			public void onFailure(ErrorMsg errorMsg) {
				PostHttpInfoUtils.doPostFail(mContext, errorMsg, "????????????");		
				showProgress(false);
				isTasking = false;
			}
		});
	}
	
	public void zfbLogin(final String authCode){
		showProgress(true);
		ServiceUser.getInstance().PostZFBLogin(mContext,authCode,
				new CallBack<UserResultData>() {
			@Override
			public void onSuccess(UserResultData userResultData) {
				//				ToastUtils.showShort(mContext, "???????????????");
				PreferenceUtil.putString(mContext, "username","");
				showProgress(false);
				ConstantsBase.setUser(userResultData.user);
				isTasking = false;					
				if (!TextUtils.isEmpty(requestName)) {
					// ??????????????????????????????
					PreferenceUtil.putBoolean(mContext, "hasLogin", true);
					PreferenceUtil.putBoolean(mContext, "isExitWay", false);

					EventBus.getDefault().post("deviceToken");// ?????????????????????????????????/??????????????????????????????
					addPushAlias(userResultData.user.userId);
					Intent intent=new Intent();
					if("intentLogin".equals(requestName)){
						setResult(RESULT_OK, intent);
						mContext.finish();
					}else{
						setResult(1, intent);
					}
				}else{
					if (!TextUtils.isEmpty(userResultData.isNew) && "0".endsWith(userResultData.isNew)) {
						if (userResultData.user.isValidatedPhoneNo()) {
							// ??????????????????????????????
							PreferenceUtil.putBoolean(mContext, "hasLogin", true);
							PreferenceUtil.putBoolean(mContext, "isExitWay", false);
							Intent intent = new Intent(mContext,
									MainActivity.class);
							intent.putExtra("hasLogin", "yes");// ??????
							startActivity(intent);
						}else{
							Intent intent = new Intent(mContext,
									BindingPhoneActivity.class);
							intent.putExtra("fromWay", "loginByZFB");
							intent.putExtra("authCode", authCode);
							startActivity(intent);
						}
					}

					if (!TextUtils.isEmpty(userResultData.isNew) && "1".endsWith(userResultData.isNew)) {//?????????????????????
						Intent intent = new Intent(mContext,
								BindingPhoneActivity.class);
						intent.putExtra("fromWay", "loginByZFB");
						intent.putExtra("authCode", authCode);
						startActivity(intent);
					}

				}
				finish();

			}

			@Override
			public void onFailure(ErrorMsg errorMsg) {
				PostHttpInfoUtils.doPostFail(mContext, errorMsg, "????????????");			
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

	// ???????????????
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:

			// TODO ???????????????????????????
			finish();// ????????? ?????????activity ??????
			if (TextUtils.isEmpty(requestName)) {
				Intent intent;
				if (isMaJia) {//?????????
					intent=new Intent(mContext, ZXMainActivity.class);					
				}else {
					intent=new Intent(mContext, MainActivity.class);					
				}				
				intent.putExtra("hasLogin", "cancel");
				startActivity(intent);	

			}
			break;
		}
		return super.onKeyUp(keyCode, event);
	}

	//??????????????????????????????
		private void queryNoticeFlag() {
			ServiceUser.getInstance().getQueryNoticeFlag(LoginActivity.this, new CallBack<String>() {

				@Override
				public void onSuccess(String noticeFlag) {
					if (noticeFlag.equals("true")) {//???
						PreferenceUtil.putBoolean(LoginActivity.this, "isOpenNotice", true);
					}else {//???
						PreferenceUtil.putBoolean(LoginActivity.this, "isOpenNotice", false);
					}
				}

				@Override
				public void onFailure(ErrorMsg errorMessage) {
					PreferenceUtil.putBoolean(LoginActivity.this, "isOpenNotice", true);//????????????????????????
				}
			});
		}

	@Override
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
