package com.cshen.tiyu.activity.mian4.gendan.myarena;



import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.base.MyFragmentPagerAdapter;

public class MyArenaActivity extends BaseActivity {
	MyArenaActivity _this;
	private TextView myFouce,myFollow,myRecommend;
	private ViewPager mPager;//对应的fragment
	private ArrayList<Fragment> fragments=new ArrayList<Fragment>();
	public  MyFouceFragment myFouceFragment;
	public  MyFollowFragment myFollowFragment;
	public  MyRecommendFragment myRecommendFragment;

	private TextView title;
	private TextView ivBottomLine;
	private int currIndex = 0;
	private int position_one;
	private int position_two;
	Bundle savedInstanceState;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.savedInstanceState = savedInstanceState;
		_this = this;
		setContentView(R.layout.myarena);
		initView();
	}
	private void initView(){
		InitWidth();
		findViewById(R.id.iv_back).setVisibility(View.VISIBLE);
		findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				_this.finish();
			}
		});
		title= (TextView) findViewById(R.id.tv_head_title);
		title.setText("我的擂台");
		myFouce= (TextView) findViewById(R.id.fouce);
		myFollow = (TextView) findViewById(R.id.follow);
		myRecommend = (TextView) findViewById(R.id.recommend);

		myFouce.setOnClickListener(new MyOnClickListener(0));
		myFollow.setOnClickListener(new MyOnClickListener(1));
		myRecommend.setOnClickListener(new MyOnClickListener(2));
		mPager = (ViewPager) findViewById(R.id.pager);
		setFragment();//设置订单数据
	}
	private void InitWidth() {
		ivBottomLine = (TextView)findViewById(R.id.iv_bottom_line1);
		DisplayMetrics dm = new DisplayMetrics();
		_this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;//屏幕宽度
		position_one = (int) (screenW / 3.0);
		position_two = position_one * 2;
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
	private void setFragment(){
		if(savedInstanceState != null){
			myFouceFragment = (MyFouceFragment) getSupportFragmentManager().getFragment(
					savedInstanceState, "myFouceFragment");
			if(myFouceFragment == null){
				myFouceFragment = new MyFouceFragment();
			}
			
			myFollowFragment = (MyFollowFragment) getSupportFragmentManager().getFragment(
					savedInstanceState, "myFollowFragment");
			if(myFollowFragment == null){
				myFollowFragment = new MyFollowFragment();
			}
			
			myRecommendFragment = (MyRecommendFragment) getSupportFragmentManager().getFragment(
					savedInstanceState, "myRecommendFragment");
			if(myRecommendFragment == null){
				myRecommendFragment = new MyRecommendFragment();
			}

		}else{
			myFouceFragment = new MyFouceFragment();
			myFollowFragment = new MyFollowFragment();
			myRecommendFragment = new MyRecommendFragment();
		}

		fragments.add(myFouceFragment);
		fragments.add(myFollowFragment);
		fragments.add(myRecommendFragment);

		mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments));
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());

	}


	public void setBase(){
		TranslateAnimation animation = null;
		if (currIndex == 1) {
			animation = new TranslateAnimation(position_one, 0, 0, 0);
			myFollow.setTextColor(_this.getResources().getColor(R.color.dingdanuncolor));
		} else if (currIndex == 2) {
			animation = new TranslateAnimation(position_two, 0, 0, 0);
			myRecommend.setTextColor(_this.getResources().getColor(R.color.dingdanuncolor));
		}    
		myFouce.setTextColor(_this.getResources().getColor(R.color.mainred));
		if(animation!=null){
			animation.setFillAfter(true);
			animation.setDuration(300);
			ivBottomLine.startAnimation(animation);
		}
		currIndex = 0;
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				if (currIndex == 1) {
					animation = new TranslateAnimation(position_one, 0, 0, 0);
					myFollow.setTextColor(_this.getResources().getColor(R.color.dingdanuncolor));
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(position_two, 0, 0, 0);
					myRecommend.setTextColor(_this.getResources().getColor(R.color.dingdanuncolor));
				}     
				myFouce.setTextColor(_this.getResources().getColor(R.color.mainred));
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(0, position_one, 0, 0);
					myFouce.setTextColor(_this.getResources().getColor(R.color.dingdanuncolor));
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(position_two, position_one, 0, 0);
					myRecommend.setTextColor(_this.getResources().getColor(R.color.dingdanuncolor));
				} 
				myFollow.setTextColor(_this.getResources().getColor(R.color.mainred));
				break;
			case 2:
				if (currIndex == 0) {
					animation = new TranslateAnimation(0, position_two, 0, 0);
					myFouce.setTextColor(_this.getResources().getColor(R.color.dingdanuncolor));
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(position_one, position_two, 0, 0);
					myFollow.setTextColor(_this.getResources().getColor(R.color.dingdanuncolor));
				}
				myRecommend.setTextColor(_this.getResources().getColor(R.color.mainred));
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
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		if (myFollowFragment != null
				&&myFollowFragment.isVisible()) {
			getSupportFragmentManager().putFragment(savedInstanceState, "myFollowFragment", myFollowFragment);
		}
		if (myFouceFragment != null
				&&myFouceFragment.isVisible()) {
			getSupportFragmentManager().putFragment(savedInstanceState, "myFouceFragment", myFouceFragment);
		}

		if (myRecommendFragment != null
				&&myRecommendFragment.isVisible()) {
			getSupportFragmentManager().putFragment(savedInstanceState, "myRecommendFragment", myRecommendFragment);
		}
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:

			if(myFollowFragment.timekeybroad!=null&&myFollowFragment.timekeybroad.getVisibility() == View.VISIBLE){
				myFollowFragment.timekeybroad.setVisibility(View.GONE);
				return true;
			}else{
				_this.finish();
			}
			break;
		}
		return super.onKeyUp(keyCode, event);
	}
}
