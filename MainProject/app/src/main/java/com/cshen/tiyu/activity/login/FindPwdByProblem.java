package com.cshen.tiyu.activity.login;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.opengl.Visibility;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.LotteryTypeActivity;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.deprecated.ForgetUserMobileResultData;
import com.cshen.tiyu.net.https.ServiceUser;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.utils.IDCard;
import com.cshen.tiyu.utils.PostHttpInfoUtils;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.utils.Util;
import com.cshen.tiyu.widget.SpannableClickText;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;

/**
 * 安全问题找回
 * 
 * @author yx
 * 
 */
public class FindPwdByProblem extends BaseActivity implements OnClickListener {

	private TopViewLeft tv_head;
	private Context mContext;
	private LinearLayout ll_error;// 未进行验证、多账号等
	private ScrollView mScrollView;
	private TextView tv_tips;
	private ImageView iv_refresh1, iv_refresh2;// 刷新
	private TextView tv_question1, tv_question2;
	private EditText edt_answer1, edt_answer2;
	private EditText edt_name, edt_idCard;
	private EditText edt_pwd_new, edt_pwd_confirm;
	private Button btn_sure;
	private int layout_state;// 判断显示那种页面 0:未验证 1:多账号 2：安全找回
	private ForgetUserMobileResultData resultData;
	private int index1 = 0, index2 = 1;
	private String[] qustionItems;
	private boolean isTasking = false;
	private int userId;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_find_pwd_security);
		mContext = this;
		layout_state = getIntent().getIntExtra("layout_state", -1);
		resultData = (ForgetUserMobileResultData) getIntent()
				.getSerializableExtra("resultData");
		if (resultData == null) {
			ToastUtils.showShortCenter(mContext, "获取数据错误！");
			finish();
			return;
		}
		userId = resultData.userId;
		qustionItems = new String[] { resultData.p0, resultData.p1,
				resultData.p2 };
		initHead();
		initView();
		setLayout();// 设置显示的是什么页面
	}

	private void initView() {
		ll_error = (LinearLayout) findViewById(R.id.ll_showError);
		mScrollView = (ScrollView) findViewById(R.id.scrollview);
		tv_tips = (TextView) findViewById(R.id.tv_tips);

		iv_refresh1 = (ImageView) findViewById(R.id.iv_refresh1);
		iv_refresh2 = (ImageView) findViewById(R.id.iv_refresh2);

		tv_question1 = (TextView) findViewById(R.id.tv_question1);
		tv_question2 = (TextView) findViewById(R.id.tv_question2);

		edt_answer1 = (EditText) findViewById(R.id.edt_answer1);
		edt_answer2 = (EditText) findViewById(R.id.edt_answer2);

		edt_name = (EditText) findViewById(R.id.edt_name);
		edt_idCard = (EditText) findViewById(R.id.edt_idcard);

		edt_pwd_new = (EditText) findViewById(R.id.edt_pwd_new);
		edt_pwd_confirm = (EditText) findViewById(R.id.edt_pwd_confirm);
		btn_sure = (Button) findViewById(R.id.btn_sure);

		iv_refresh1.setOnClickListener(this);
		iv_refresh2.setOnClickListener(this);
		btn_sure.setOnClickListener(this);

	}

	private void initHead() {
		// TODO 自动生成的方法存根
		tv_head = (TopViewLeft) findViewById(R.id.tv_head);

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

	private void setLayout() {
		if (layout_state == 2) {
			ll_error.setVisibility(View.GONE);
			mScrollView.setVisibility(View.VISIBLE);
			tv_question1.setText(resultData.p0);
			tv_question2.setText(resultData.p1);
		} else {
			ll_error.setVisibility(View.VISIBLE);
			mScrollView.setVisibility(View.GONE);
			setTipsText(layout_state);
		}
	}

	// 設置提示文字
	private void setTipsText(int layout_state) {
		String tishiStr = "";
		if (layout_state == 0) {
			// tishiStr =
			// "<html><font color=\"#666666\">亲，你还没有进行相关验证，<br />请点击联系"
			// + "</font><font color=\"#0000ff\">在线客服" + "</font></html>";
			 tishiStr = "亲，你还没有进行相关验证\n请点击联系在线客服 ";
		} else if (layout_state == 1) {
//			tishiStr = "<html><font color=\"#666666\">该手机下存在多个账户，<br />请点击联系"
//					+ "</font><font color=\"#0000ff\">在线客服" + "</font></html>";
			tishiStr = "该手机号下存在多个账户\n请点击联系在线客服 ";
		}
//		tv_tips.setText(Html.fromHtml(tishiStr));
//		tv_tips.setOnClickListener(this);
		SpannableClickText spt = new SpannableClickText(tv_tips, tishiStr, Color.BLUE);
		spt.setSpannableText(tishiStr.length()-5, tishiStr.length()-1,
				new SpannableClickText.onTextClickListener() {
					@Override
					public void onclick(View view) {
						toLotteryTypeActivity();
					}
				});
	}

	// 跳转到在线客服页面
	private void toLotteryTypeActivity() {
		Intent nIntent = new Intent();
		nIntent.setClass(mContext, LotteryTypeActivity.class);

		nIntent.putExtra("url", "http://www16.53kf.com/webCompany.php?style=1&arg=10167473");
		nIntent.putExtra("show", true);
		startActivity(nIntent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_refresh1:// 问题1刷新
			refreshQuestion(true);
			tv_question1.setText(qustionItems[index1]);

			break;
		case R.id.iv_refresh2:// 问题2刷新
			refreshQuestion(false);
			tv_question2.setText(qustionItems[index2]);
			break;
//		case R.id.tv_tips:// 提示
//			toLotteryTypeActivity();
//			break;
		case R.id.btn_sure:// 确认
			if (isTasking) {// 任务在运行中不再登入
				return;
			}
			findPwd();
			break;
		default:
			break;
		}

	}

	private void findPwd() {

		String answer1 = edt_answer1.getText().toString();
		String answer2 = edt_answer2.getText().toString();
		String name = edt_name.getText().toString();
		String idCard = edt_idCard.getText().toString();
		String pwd_new = edt_pwd_new.getText().toString();
		String pwd_confirm = edt_pwd_confirm.getText().toString();
		// 正则判断
		if (TextUtils.isEmpty(answer1) || TextUtils.isEmpty(answer2)) {// 答案不能为空
			ToastUtils.showShortCenter(mContext, "答案不可为空！");
			return;
		}
		if (answer1.length() < 2 || answer2.length() < 2
				|| !Util.textNameTemp_NoSpace(answer1) || !Util.textNameTemp_NoSpace(answer2)) {// 答案格式不正确
			ToastUtils.showShortCenter(mContext, "2-15位汉字,字母,数字答案！");
			return;
		}
		if (TextUtils.isEmpty(name) || TextUtils.isEmpty(idCard)) {// 姓名/身份证为空
			ToastUtils.showShortCenter(mContext, "请填写正确的身份信息！");
			return;
		}
		if (!Util.isNameValid(name) || name.length() < 2) {// 姓名格式不对
			ToastUtils.showShortCenter(mContext, "2-10位汉字组合姓名！");
			return;
		}
		String result = "";
		try {
			result = IDCard.IDCardValidate(idCard);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!"".equals(result)) {
			ToastUtils.showShort(mContext, "身份证号码位数错误");
			edt_idCard.requestFocus();
			return;
		}
		if (TextUtils.isEmpty(pwd_new) || TextUtils.isEmpty(pwd_confirm)) {// 密码为空
			ToastUtils.showShortCenter(mContext, "密码不可为空！");
			return;
		}
		if (!pwd_confirm.equals(pwd_new)) {// 密码不一致
			ToastUtils.showShortCenter(mContext, "两次密码不一致！");
			return;
		}
		if (Util.isAllEnglish(pwd_new)) {// 全为英文
			ToastUtils.showShortCenter(mContext, "密码不能全部为字母！");
			return;
		}
		if (Util.textNameTemp1(pwd_new)) {// 全为数字
			ToastUtils.showShortCenter(mContext, "密码不能全部为数字！");
			return;
		}
		if (!Util.isPasswordValid_OnlyNumAndLeter(pwd_new)
				|| pwd_new.length() < 6) {// 密码规则不正确
			ToastUtils.showShortCenter(mContext, "6-12位数字字母组合密码！");
			return;
		}
		String answerJosn = "";
		JSONObject object = new JSONObject();
		try {
			object.put(tv_question1.getText().toString(), answer1);
			object.put(tv_question2.getText().toString(), answer2);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		answerJosn = object.toString();
		isTasking = true;
		ServiceUser.getInstance().MatchAnswers(mContext, userId, idCard,
				pwd_new, name, answerJosn, new CallBack<String>() {

					@Override
					public void onSuccess(String processId) {
						isTasking = false;
						if ("0".equals(processId)) {// 成功
							ToastUtils.showShortCenter(mContext, "修改密码成功！");
							Intent intent = new Intent(mContext,
									LoginActivity.class);
							startActivity(intent);
						} else if ("1".equals(processId)) {// 答案错误
							ToastUtils.showShortCenter(mContext, "答案错误！");
						} else if ("2".equals(processId)) {// 身份信息错误
							ToastUtils.showShortCenter(mContext, "身份信息错误！");
						} else {// 其他情况失败
							ToastUtils.showShortCenter(mContext, "修改密码失败！");
						}
					}

					@Override
					public void onFailure(ErrorMsg errorMessage) {
						PostHttpInfoUtils.doPostFail(mContext, errorMessage,
								"查询无结果，建议咨询客服");
						isTasking = false;
					}
				});
	}

	private void refreshQuestion(Boolean isRefresh1) {
		// TODO 待优化

		if (index1 == 0) {
			if (index2 == 1) {
				if (isRefresh1) {
					index1 = 2;
				} else {
					index2 = 2;
				}
			} else {
				if (isRefresh1) {
					index1 = 1;
				} else {
					index2 = 1;
				}
			}
		} else if (index1 == 1) {
			if (index2 == 0) {
				if (isRefresh1) {
					index1 = 2;
				} else {
					index2 = 2;
				}
			} else {
				if (isRefresh1) {
					index1 = 0;
				} else {
					index2 = 0;
				}
			}
		} else if (index1 == 2) {
			if (index2 == 0) {
				if (isRefresh1) {
					index1 = 1;
				} else {
					index2 = 1;
				}
			} else {
				if (isRefresh1) {
					index1 = 0;
				} else {
					index2 = 0;
				}
			}
		}

	}

}
