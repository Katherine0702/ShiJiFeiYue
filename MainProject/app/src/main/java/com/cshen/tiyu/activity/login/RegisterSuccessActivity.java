package com.cshen.tiyu.activity.login;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.cshen.tiyu.MainActivity;
import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.mian4.personcenter.setting.SafeSettingActivity;
import com.cshen.tiyu.activity.mian4.personcenter.setting.binding.NameAuthActivity;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;


/**
 * A login screen that offers login via username/password and via Google+ sign in.
 * <p/>
 * ************ IMPORTANT SETUP NOTES: ************
 * In order for Google+ sign in to work with your app, you must first go to:
 * https://developers.google.com/+/mobile/android/getting-started#step_1_enable_the_google_api
 * and follow the steps in "Step 1" to create an OAuth 2.0 client for your package.
 */
public class RegisterSuccessActivity extends BaseActivity {

	// UI references.
	private Context mContext;
	private TopViewLeft tv_head=null;
	private TextView tv_toMain,tv_toAuthName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_success);
		mContext = this;

		tv_head=(TopViewLeft) findViewById(R.id.tv_head);
		tv_head.setResourceVisiable(true,  false, false);
		tv_head.setTopClickItemListener(new TopClickItemListener() {

			@Override
			public void clickLoginView(View view) {
				startActivity(new Intent(RegisterSuccessActivity.this,SafeSettingActivity.class));
				finish();
			}

			@Override
			public void clickContactView(View view) {
				// TODO 自动生成的方法存根

			}

			@Override
			public void clickBackImage(View view) {
				Intent intent=new Intent(RegisterSuccessActivity.this, MainActivity.class);
				intent.putExtra("hasLogin", "yes");
				intent.putExtra("nowPage", "4");
				startActivity(intent);
				finish();
			}
		});
		
		tv_toMain=(TextView) findViewById(R.id.tv_toMain);
		tv_toAuthName=(TextView) findViewById(R.id.tv_toAuthName);
		tv_toMain.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(RegisterSuccessActivity.this, MainActivity.class);
				intent.putExtra("hasLogin", "yes");
				intent.putExtra("nowPage", "1");
				startActivity(intent);
				finish();
			}
		});
		
		tv_toAuthName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(RegisterSuccessActivity.this, NameAuthActivity.class);
				intent.putExtra("isFromRegister", true);
				startActivity(intent);
				finish();
			}
		});
		
	
	}
	// 返回键退出
		@Override
		public boolean onKeyUp(int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				Intent intent=new Intent(RegisterSuccessActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
				break;
			}
			
			return super.onKeyUp(keyCode, event);
		}


		@Override
		public void onResume() {  
			// TODO Auto-generated method stub
			super.onResume();
			if("redpacket".equals(getIntent().getStringExtra("fromOther"))){
				//MobclickAgent.onEvent(mContext, "registercaseredpacketonResume");//统计
	        }
		}
}

