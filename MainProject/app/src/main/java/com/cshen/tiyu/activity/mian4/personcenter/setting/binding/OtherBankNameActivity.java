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

public class OtherBankNameActivity extends BaseActivity {

	TopViewLeft tv_head;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_other_bank_name);
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
				finish();
			}
		});

	}

	public void selectBankName(View v){
		TextView tv=(TextView) v;
		Intent data = new Intent();
		data.putExtra("bankName", tv.getText().toString().trim());
		setResult(1, data);
		finish();
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

}
