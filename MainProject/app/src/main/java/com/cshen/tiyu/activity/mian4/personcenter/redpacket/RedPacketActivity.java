package com.cshen.tiyu.activity.mian4.personcenter.redpacket;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.base.MyFragmentPagerAdapter;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;

import de.greenrobot.event.EventBus;

public class RedPacketActivity extends BaseActivity {

	private RedPacketActivity _this;
	private TopViewLeft tv_head;//头
	private View nodata;
	private TextView help,ivBottomLine;
	private TextView redPacketUse,redPacketUseNo;
	private ViewPager mPager;//对应的fragment
	private ArrayList<Fragment> fragments=new ArrayList<Fragment>();
	private RedPacketUseFragment redPacketUseFragment;//未使用
	private RedPacketUseNoFragment redPacketUseNoFragment;//已使用/过期


	private int currIndex = 0;
	private int position_one;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_red_packet);
		EventBus.getDefault().register(this);
		_this = this;
		initView();
	}
	private void initView() {
		InitWidth();
		tv_head = (TopViewLeft) findViewById(R.id.title);
		tv_head.setResourceVisiable(true, true ,false);
		tv_head.setImageRight(R.mipmap.shuoming);
		tv_head.setTopClickItemListener(new TopClickItemListener() {
			@Override
			public void clickLoginView(View view) {
				Intent intentHelp = new Intent(_this,RedPacketShuomingActivity.class);
				startActivity(intentHelp);
			}
			@Override
			public void clickContactView(View view) {}
			@Override
			public void clickBackImage(View view) {
				finish();
			}
		});
		
		redPacketUse = (TextView) findViewById(R.id.tv_redpacket_use);
		redPacketUseNo = (TextView) findViewById(R.id.tv_redpacket_useno);


		redPacketUse.setOnClickListener(new MyOnClickListener(0));
		redPacketUseNo.setOnClickListener(new MyOnClickListener(1));
		mPager = (ViewPager) findViewById(R.id.pager);
		setViews();//设置订单数据
	}
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;
		public MyOnClickListener(int i) {
			index = i;
		}
		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(index);
		}
	};
	private void InitWidth() {
		ivBottomLine = (TextView) findViewById(R.id.iv_bottom_line1);
		DisplayMetrics dm = new DisplayMetrics();
		_this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;//屏幕宽度
		position_one = (int) (screenW / 2.0);
	}
	private void setViews(){
    
		redPacketUseFragment = new RedPacketUseFragment();
		redPacketUseNoFragment = new RedPacketUseNoFragment();

		fragments.add(redPacketUseFragment);
		fragments.add(redPacketUseNoFragment);

		mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments));
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());

	}
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				if (currIndex == 1) {
					animation = new TranslateAnimation(position_one, 0, 0, 0);
					redPacketUseNo.setTextColor(_this.getResources().getColor(R.color.dingdanuncolor));
				}   
				redPacketUse.setTextColor(_this.getResources().getColor(R.color.mainred));
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(0, position_one, 0, 0);
					redPacketUse.setTextColor(_this.getResources().getColor(R.color.dingdanuncolor));
				}
				redPacketUseNo.setTextColor(_this.getResources().getColor(R.color.mainred));
				break;
			}
			currIndex = arg0;
			animation.setFillAfter(true);
			animation.setDuration(300);
			ivBottomLine.startAnimation(animation);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
	public void setBase(){
		TranslateAnimation animation = null;
		if (currIndex == 1) {
			animation = new TranslateAnimation(position_one, 0, 0, 0);
			redPacketUseNo.setTextColor(_this.getResources().getColor(R.color.dingdanuncolor));
		}    
		redPacketUse.setTextColor(_this.getResources().getColor(R.color.mainred));
		if(animation!=null){
			animation.setFillAfter(true);
			animation.setDuration(300);
			ivBottomLine.startAnimation(animation);
		}
		currIndex = 0;
	}
	public void onEventMainThread(String event) {

		if (!TextUtils.isEmpty(event)) {			
			if ("updateRedPacketFragment".equals(event)) {
				if(fragments!=null){
					fragments.clear();
				}
				setBase();
				setViews();
			}
		}
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}
