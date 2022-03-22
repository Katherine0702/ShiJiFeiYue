package com.cshen.tiyu.activity.pay;

import android.os.Bundle;
import android.view.View;

import com.cshen.tiyu.R;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;

public class TZXYActivity extends BaseActivity {
	TopViewLeft tv_head;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tzxy);
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
	}

}
