package com.cshen.tiyu.activity.mian4.personcenter.setting.binding;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;
import com.cshen.tiyu.R;

public class BankNameActivity extends BaseActivity {

	TopViewLeft tv_head;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bank_name);
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
				//startActivity(new Intent(BankNameActivity.this,SafeSettingActivity.class));
				finish();
			}
		});

	}

	public void selectBankName(View v){
		TextView tv=(TextView) v;
		if("其他银行".equals(tv.getText().toString().trim())){
			Intent data = new Intent(this,OtherBankNameActivity.class);
			data.putExtra("bankName", tv.getText().toString().trim());
			startActivityForResult(data,0);
		}else{
			Intent data = new Intent();
			data.putExtra("bankName", tv.getText().toString().trim());
			setResult(1, data);
			finish();
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getRepeatCount() == 0) {
			//do something...
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 1) {
			if (requestCode == 0) {
				String bankName = data.getStringExtra("bankName");
				data.putExtra("bankName",bankName);
				setResult(1, data);
				finish();
			}
		}
	}
}
