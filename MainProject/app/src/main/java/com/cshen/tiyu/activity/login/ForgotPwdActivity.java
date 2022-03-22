package com.cshen.tiyu.activity.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.LotteryTypeActivity;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.deprecated.ForgetUserMobileResultData;
import com.cshen.tiyu.net.https.ServiceUser;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.utils.PostHttpInfoUtils;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.SpannableClickText;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;

public class ForgotPwdActivity extends BaseActivity implements OnClickListener {

	private Context mContext;
	private boolean isTasking = false;
	private EditText et_userName;
	private TextView  tishi;
	private Button btn_sure;
	TopViewLeft tv_head;
	private String userName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_pwd);
		mContext = this;
		userName = getIntent().getStringExtra("userName");
		initView();
		initHead();

	}

	private void initHead() {
		// TODO 自动生成的方法存根
		tv_head = (TopViewLeft) findViewById(R.id.tv_head_forget);

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
				finish();
			}
		});

	}

	private void initView() {
		et_userName = (EditText) findViewById(R.id.et_userName);
		et_userName.setText(userName);
		tishi = (TextView) findViewById(R.id.tishi);
		// String tishiStr =
		// "<html><font color=\"#666666\">温馨提示：如果您的账号暂未绑定手机，或已忘记账号等疑问问，请联系客服"
		// + "</font><font color=\"#0000ff\">400-928-2770" + "</font></html>";
		// tishi.setText(Html.fromHtml(tishiStr));
		// tishi.setOnClickListener(this);
		String tv = "温馨提示:如果您的账号暂未绑定手机,或已忘记账号等疑问,请联系客服";//多加一个空格是因为链接在最后，如果不加，页面电话号码后面的空白部分也可点击
		SpannableClickText spt = new SpannableClickText(tishi, tv, Color.BLUE);
		spt.setSpannableText(tv.length()-13, tv.length()-1,
				new SpannableClickText.onTextClickListener() {
					@Override
					public void onclick(View view) {
						//toCall("4009282770");
					}
				});

		btn_sure = (Button) findViewById(R.id.btn_sure);
		btn_sure.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		if (v == btn_sure) {
			if (isTasking) {// 任务在运行中不再登入
				return;
			}
			final String userName = et_userName.getText().toString().trim();
			if (TextUtils.isEmpty(userName)) {
				ToastUtils.showShortCenter(mContext, "用户名/手机号不能为空！");
				et_userName.requestFocus();
				return;
			}
			isTasking = true;
			ServiceUser.getInstance().ForgotLoginPwd_temp1(mContext, userName,
					new CallBack<ForgetUserMobileResultData>() {

						@Override
						public void onSuccess(ForgetUserMobileResultData mResult) {
							isTasking = false;
							String processId = mResult.processId;
							if (processId.equals("1")) {// 用户名输入错误
								ToastUtils.showShortCenter(mContext, "用户名/手机号不存在！");
							} else {
								int layout_state = -1;
								if (processId.equals("0")) {// 成功
									layout_state = 2;
								} else if (processId.equals("2")) {// 未设置安全问题
									layout_state = 0;
								} else if (processId.equals("3")) {// 未实名认证
									layout_state = 0;
								} else if (processId.equals("4")) {// 电话号码绑定多个
									layout_state = 1;
								}
								Intent intent = new Intent(mContext,
										FindPwdByProblem.class);
								intent.putExtra("layout_state", layout_state);
								intent.putExtra("resultData", mResult);
								startActivity(intent);
								finish();

							}

							// Intent intent = new Intent(mContext,
							// ForgotPwdTwoActivity.class);
							// intent.putExtra("userName", userName);
							// if (mResult != null) {
							// intent.putExtra("forgetUserPhoneBeFore",
							// mResult.forgetUserPhoneBeFore);
							// intent.putExtra("forgetUserPhoneAfter",
							// mResult.forgetUserPhoneAfter);
							// }
							// startActivity(intent);
							// finish();

						}

						@Override
						public void onFailure(ErrorMsg errorMessage) {
							PostHttpInfoUtils.doPostFail(mContext,
									errorMessage, "查询无结果，建议咨询客服");
							isTasking = false;
						}
					});
		}
//		if (v == tishi) {
////			toCall("4009282770");
//			// Intent nIntent = new Intent();
//			// nIntent.setClass(ForgotPwdActivity.this,
//			// LotteryTypeActivity.class);
//			//
//			// nIntent.putExtra("url", ForgotPwdActivity.this.getResources()
//			// .getString(R.string.kefu).replace("#", "&"));
//			// nIntent.putExtra("show", true);
//			// startActivity(nIntent);
//
//		}

	}

	public void toCall(String phoneno) {

		if (phoneno == null || "".equals(phoneno.trim())) {
			phoneno = "4009282770";
		}
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
				+ phoneno));
		startActivity(intent);

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// do something...
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
