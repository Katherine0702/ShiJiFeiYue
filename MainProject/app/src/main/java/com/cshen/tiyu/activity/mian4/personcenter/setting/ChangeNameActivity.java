package com.cshen.tiyu.activity.mian4.personcenter.setting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.login.User;
import com.cshen.tiyu.net.https.ServiceUser;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.utils.PostHttpInfoUtils;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;

public class ChangeNameActivity extends BaseActivity{
	private ChangeNameActivity mContext;
	private EditText mUsernameView;//昵称
	TopViewLeft tv_head;
	TextView setNikeName;
	private final String reg ="[^a-zA-Z0-9\u4E00-\u9FA5]";  
	private Pattern pattern = Pattern.compile(reg);
	private boolean isTasking = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_name);

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
		setNikeName = (TextView) findViewById(R.id.tv_login);
		setNikeName.setText("保存");
		setNikeName.setVisibility(View.VISIBLE);
		setNikeName.setTextSize(16);
		setNikeName.setBackgroundDrawable(null);

		mUsernameView = (EditText) findViewById(R.id.username);
		setNikeName.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				changeName();
			}
		});

	}

	public void changeName() {
		if (isTasking) {
			return;
		}
		final String username = mUsernameView.getText().toString().trim();
		if (TextUtils.isEmpty(username)) {
			ToastUtils.showShort(mContext, "昵称不能为空");
			mUsernameView.requestFocus();
			return;
		}

		String str = stringFilter(username.toString());
		if(!username.equals(str)){
			ToastUtils.showShort(this, "不能输入非法字符！" );
			return;
		}
		isTasking = true;
		ServiceUser.getInstance().PostChangeName(mContext,username,
				new CallBack<String>() {
			@Override
			public void onSuccess(String userResultData) {
				isTasking = false;
				User user =  MyDbUtils.getCurrentUser();
				user.setUsernickName(username);
				MyDbUtils.saveUser(user);
				Intent intent = new Intent();  
				setResult(RESULT_OK, intent); 
				finish();
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				PostHttpInfoUtils.doPostFail(mContext, errorMessage,
						"修改失败");
				isTasking = false;
			}
		});
	}

	public static String stringFilter(String str)throws PatternSyntaxException{     
		// 只允许字母、数字和汉字      
		String   regEx  =  "[^a-zA-Z0-9\u4E00-\u9FA5]";                     
		Pattern   p   =   Pattern.compile(regEx);     
		Matcher   m   =   p.matcher(str);     
		return   m.replaceAll("").trim();     
	}
}

