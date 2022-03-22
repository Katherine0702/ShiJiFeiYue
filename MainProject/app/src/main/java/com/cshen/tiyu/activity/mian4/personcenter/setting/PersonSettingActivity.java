package com.cshen.tiyu.activity.mian4.personcenter.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.MainActivity;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.utils.PreferenceUtil;

public class PersonSettingActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person_setting);
		TextView  textView=(TextView) findViewById(R.id.tv_cancel_login);
		
		textView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				PreferenceUtil.putBoolean(PersonSettingActivity.this, "hasLogin", false);
//				PrefUtils.setBoolean(PersonSettingActivity.this, "hasLogin", false);
				Intent intent=new Intent(PersonSettingActivity.this, MainActivity.class);
				
				intent.putExtra("hasLogin","cancel");//标记 为了让主activity根据此标记选中相应的fragment 
						
				
				startActivity(intent);
				finish();
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.person_setting, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
