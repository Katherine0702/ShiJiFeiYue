package com.cshen.tiyu.activity.mian4.personcenter.setting.binding;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;


import com.cshen.tiyu.MainActivity;
import com.cshen.tiyu.R;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.login.UserInfo;
import com.cshen.tiyu.net.https.ServiceUser;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.utils.IDCard;
import com.cshen.tiyu.utils.PostHttpInfoUtils;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.utils.Util;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;

import de.greenrobot.event.EventBus;

public class NameAuthActivity extends BaseActivity {
	private TextView nameAuthBtn;
	private EditText personNameEt;
	private EditText personIdEt;
	private Context mContext;
	private boolean hasRealName = false,needback = false,isTasking = false,isFromRegister=false;
	TopViewLeft tv_head=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_name_auth);
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
				if(needback){
					Intent intent = new Intent();  
					intent.putExtra("yanzheng", hasRealName);
					setResult(RESULT_OK, intent);
				} 
				if (isFromRegister) {
					toPersonalPage();
				}
				finish();
			}
		});
		mContext = this;
		needback = getIntent().getBooleanExtra("needback",false);
		hasRealName = PreferenceUtil.getBoolean(mContext, "hasRealName");
		isFromRegister=getIntent().getBooleanExtra("isFromRegister",false);//from注册成功页面，认证成功直接去个人中心
		initView();

	}
	//跳转到我的彩票页面
	public void toPersonalPage(){
		Intent intent=new Intent(NameAuthActivity.this, MainActivity.class);
		intent.putExtra("hasLogin", "yes");
		intent.putExtra("nowPage", "4");
		startActivity(intent);
	}

	private void initView() {
		nameAuthBtn = (TextView) findViewById(R.id.btn_name_auth);
		personNameEt = (EditText) findViewById(R.id.et_person_name);
		personIdEt = (EditText) findViewById(R.id.et_person_id);
		changViewByHasRealname();
		nameAuthBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				attemptRealName();
			}

			private void attemptRealName() {
				if (isTasking) {// 任务在运行中不再登入
					return;
				}
				final String realName = personNameEt.getText().toString();
				final String idCard = personIdEt.getText().toString();

				if (TextUtils.isEmpty(realName)) {
					ToastUtils.showShort(mContext, "请填写正确的身份信息！");
					personNameEt.requestFocus();
					return;
				}
				if (!Util.isNameValid(realName)|| realName.length() < 2) {
					ToastUtils.showShort(mContext, "2-10位汉字组合姓名！");
					personNameEt.requestFocus();
					return;
				}
				if (TextUtils.isEmpty(idCard)) {
					ToastUtils.showShort(mContext, "请填写正确的身份信息！");
					personIdEt.requestFocus();
					return;
				}
				String result="";
				try {
					result = IDCard.IDCardValidate(idCard);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (!"".equals(result)) {
					ToastUtils.showShort(mContext, "亲，身份格式不正确");
					personIdEt.requestFocus();
					return;
				}
				isTasking = true;
				ServiceUser.getInstance().PostUserInfo(mContext,"1", realName, idCard, MyDbUtils.getCurrentUser().getUserPwd(),null,//SecurityUtil.md5(passWord).toUpperCase(),
						null,null, null, "0",true, new CallBack<String>() {

					@Override
					public void onSuccess(String s) {
						UserInfo userInfo=MyDbUtils.getCurrentUserInfo();
						userInfo.setRealName(realName);
						userInfo.setIdCard(idCard);
						MyDbUtils.saveUserInfo(userInfo);

						EventBus.getDefault().post("updateNotice");
						ToastUtils.showShort(mContext, "认证成功啦！");
						PreferenceUtil.putBoolean(mContext,"hasRealName", true);
						hasRealName = true;
						changViewByHasRealname();
						isTasking = false;
						if (isFromRegister) {//from 注册成功页面，直接跳到我的彩票页面
							toPersonalPage();
						}else {
							Intent intent = new Intent();  
							intent.putExtra("yanzheng", hasRealName);
							setResult(RESULT_OK, intent); 
						}
						
						finish();

					}

					@Override
					public void onFailure(ErrorMsg errorMsg) {
						if (errorMsg != null && errorMsg.code != null
								&& "9".equals(errorMsg.code)) {
							personNameEt.requestFocus();
							ToastUtils.showShort(mContext,"亲，真实姓名必须由英文字母或中文组成！");
						} else if (errorMsg != null
								&& errorMsg.code != null
								&& "10".equals(errorMsg.code)) {
							ToastUtils.showShort(mContext, "身份证错误！");
						} else {
							PostHttpInfoUtils.doPostFail(mContext, errorMsg, "认证失败！");
						}
						isTasking = false;
					}
				});
			}
		});

	}

	private void changViewByHasRealname() {
		if (hasRealName) {
			findViewById(R.id.warnview).setVisibility(View.GONE);
			findViewById(R.id.view_person_password).setVisibility(View.GONE);
			nameAuthBtn.setVisibility(View.GONE);

			personNameEt.setEnabled(false);
			personNameEt.setTextColor(this.getResources().getColor(R.color.grey));
			personIdEt.setEnabled(false);
			personIdEt.setTextColor(this.getResources().getColor(R.color.grey));
			UserInfo userInfo = MyDbUtils.getCurrentUserInfo();
			if (userInfo != null) {
				String realName = userInfo.getRealName();
				if (!TextUtils.isEmpty(realName)) {
					personNameEt.setText(realName);
				}
				String idCard = userInfo.getIdCard();
				if (!TextUtils.isEmpty(idCard) && idCard.length() > 4) {
					personIdEt.setText(idCard);
				}
			}

		}else{
			findViewById(R.id.warnview).setVisibility(View.VISIBLE);
			nameAuthBtn.setVisibility(View.VISIBLE);

			UserInfo userInfo = MyDbUtils.getCurrentUserInfo();
			if (userInfo != null) {
				String realName = userInfo.getRealName();
				if (!TextUtils.isEmpty(realName)) {
					personNameEt.setText(realName);
					personNameEt.setEnabled(false);
					personNameEt.setTextColor(this.getResources().getColor(R.color.grey));
				}
				String idCard = userInfo.getIdCard();
				if (!TextUtils.isEmpty(idCard) && idCard.length() > 4) {
					personIdEt.setText(idCard);
					personIdEt.setEnabled(false);
					personIdEt.setTextColor(this.getResources().getColor(R.color.grey));
				}
			}

		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// do something...
			if(needback){
				Intent intent = new Intent();  
				intent.putExtra("yanzheng", hasRealName);
				setResult(RESULT_OK, intent);
			} 
			if (isFromRegister) {//from 注册成功页面，直接跳到我的彩票页面
				toPersonalPage();
			}
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}


}
