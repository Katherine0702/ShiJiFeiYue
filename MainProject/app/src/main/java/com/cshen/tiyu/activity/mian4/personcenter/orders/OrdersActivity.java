package com.cshen.tiyu.activity.mian4.personcenter.orders;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.base.MyFragmentPagerAdapter;

public class OrdersActivity extends BaseActivity {
	OrdersActivity _this;
	private TextView ordersAll,ordersWin,ordersWaiting,ordersChase;//全部订单，中奖，带开奖，追号
	private ViewPager mPager;//对应的fragment
	private ArrayList<Fragment> fragments=new ArrayList<Fragment>();
	public  OrdersAllFragment allOrdersFragment;
	public OrdersWinFragment winOrdersFragment;
	public OrdersWaitingFragment waitingOrdersFragment;
	public OrdersChaseFragment chaseOrdersFragment;

	private TextView title;
	private TextView ivBottomLine;
	private int arg0 = 0;
	private int currIndex = 0;
	private int position_one;
	private int position_two;
	private int position_three;
	Bundle savedInstanceState;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.savedInstanceState = savedInstanceState;
		_this = this;
		setContentView(R.layout.ordersmain);
		arg0 = getIntent().getIntExtra("arg0", 0);
		initView();
		if(arg0!=0){
			//setBase(arg0);
			mPager.setCurrentItem(arg0);
		}
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
		title.setText("购彩记录");
		ordersAll= (TextView) findViewById(R.id.tv_tab_all);
		ordersWin = (TextView) findViewById(R.id.tv_tab_win);
		ordersWaiting = (TextView) findViewById(R.id.tv_tab_waiting);
		ordersChase = (TextView) findViewById(R.id.tv_tab_chase);

		ordersAll.setOnClickListener(new MyOnClickListener(0));
		ordersWin.setOnClickListener(new MyOnClickListener(1));
		ordersWaiting.setOnClickListener(new MyOnClickListener(2));
		ordersChase.setOnClickListener(new MyOnClickListener(3));
		mPager = (ViewPager) findViewById(R.id.pager);
		setFragment();//设置订单数据
	}
	private void InitWidth() {
		ivBottomLine = (TextView)findViewById(R.id.iv_bottom_line1);
		DisplayMetrics dm = new DisplayMetrics();
		_this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;//屏幕宽度
		position_one = (int) (screenW / 4.0);
		position_two = position_one * 2;
		position_three = position_one * 3;
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
			allOrdersFragment = (OrdersAllFragment) getSupportFragmentManager().getFragment(
					savedInstanceState, "allOrdersFragment");
			if(allOrdersFragment == null){
				allOrdersFragment = new OrdersAllFragment();
			}
			winOrdersFragment = (OrdersWinFragment) getSupportFragmentManager().getFragment(
					savedInstanceState, "winOrdersFragment");
			if(winOrdersFragment == null){
				winOrdersFragment = new OrdersWinFragment();
				}
			waitingOrdersFragment = (OrdersWaitingFragment) getSupportFragmentManager().getFragment(
					savedInstanceState, "waitingOrdersFragment");
			if(waitingOrdersFragment == null){
				waitingOrdersFragment = new OrdersWaitingFragment();
				}
			chaseOrdersFragment = (OrdersChaseFragment) getSupportFragmentManager().getFragment(
					savedInstanceState, "chaseOrdersFragment");
			if(chaseOrdersFragment == null){
				chaseOrdersFragment = new OrdersChaseFragment();
				}

		}else{
			allOrdersFragment = new OrdersAllFragment();
			winOrdersFragment = new OrdersWinFragment();
			waitingOrdersFragment = new OrdersWaitingFragment();
			chaseOrdersFragment = new OrdersChaseFragment();
		}

		fragments.add(allOrdersFragment);
		fragments.add(winOrdersFragment);
		fragments.add(waitingOrdersFragment);
		fragments.add(chaseOrdersFragment);

		mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments));
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());

	}


	public void setBase(int arg0){
		Animation animation = null;
		switch (arg0) {
		case 0:
			if (currIndex == 1) {
				animation = new TranslateAnimation(position_one, 0, 0, 0);
				ordersWin.setTextColor(_this.getResources().getColor(R.color.dingdanuncolor));
			} else if (currIndex == 2) {
				animation = new TranslateAnimation(position_two, 0, 0, 0);
				ordersWaiting.setTextColor(_this.getResources().getColor(R.color.dingdanuncolor));
			} else if (currIndex == 3) {
				animation = new TranslateAnimation(position_three, 0, 0, 0);
				ordersChase.setTextColor(_this.getResources().getColor(R.color.dingdanuncolor));
			}     
			ordersAll.setTextColor(_this.getResources().getColor(R.color.mainred));
			break;
		case 1:
			if (currIndex == 0) {
				animation = new TranslateAnimation(0, position_one, 0, 0);
				ordersAll.setTextColor(_this.getResources().getColor(R.color.dingdanuncolor));
			} else if (currIndex == 2) {
				animation = new TranslateAnimation(position_two, position_one, 0, 0);
				ordersWaiting.setTextColor(_this.getResources().getColor(R.color.dingdanuncolor));
			} else if (currIndex == 3) {
				animation = new TranslateAnimation(position_three, position_one, 0, 0);
				ordersChase.setTextColor(_this.getResources().getColor(R.color.dingdanuncolor));
			} 
			ordersWin.setTextColor(_this.getResources().getColor(R.color.mainred));
			break;
		case 2:
			if (currIndex == 0) {
				animation = new TranslateAnimation(0, position_two, 0, 0);
				ordersAll.setTextColor(_this.getResources().getColor(R.color.dingdanuncolor));
			} else if (currIndex == 1) {
				animation = new TranslateAnimation(position_one, position_two, 0, 0);
				ordersWin.setTextColor(_this.getResources().getColor(R.color.dingdanuncolor));
			} else if (currIndex == 3) {
				animation = new TranslateAnimation(position_three, position_two, 0, 0);
				ordersChase.setTextColor(_this.getResources().getColor(R.color.dingdanuncolor));
			} 
			ordersWaiting.setTextColor(_this.getResources().getColor(R.color.mainred));
			break;
		case 3:
			if (currIndex == 0) {
				animation = new TranslateAnimation(0, position_three, 0, 0);
				ordersAll.setTextColor(_this.getResources().getColor(R.color.dingdanuncolor));
			} else if (currIndex == 1) {
				animation = new TranslateAnimation(position_one, position_three, 0, 0);
				ordersWin.setTextColor(_this.getResources().getColor(R.color.dingdanuncolor));
			} else if (currIndex == 2) {
				animation = new TranslateAnimation(position_two, position_three, 0, 0);
				ordersWaiting.setTextColor(_this.getResources().getColor(R.color.dingdanuncolor));
			} 
			ordersChase.setTextColor(_this.getResources().getColor(R.color.mainred));
			break;
		}

		currIndex = arg0;
		if(animation!=null){
			animation.setFillAfter(true);
			animation.setDuration(300);
			ivBottomLine.startAnimation(animation);
		}
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			setBase(arg0);
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
		if (allOrdersFragment != null
				&&allOrdersFragment.isVisible()) {
			getSupportFragmentManager().putFragment(savedInstanceState, "allOrdersFragment", allOrdersFragment);
		}
		if (winOrdersFragment != null
				&&winOrdersFragment.isVisible()) {
			getSupportFragmentManager().putFragment(savedInstanceState, "winOrdersFragment", winOrdersFragment);
		}

		if (waitingOrdersFragment != null
				&&waitingOrdersFragment.isVisible()) {
			getSupportFragmentManager().putFragment(savedInstanceState, "waitingOrdersFragment", waitingOrdersFragment);
		}
		if (chaseOrdersFragment != null
				&&chaseOrdersFragment.isVisible()) {
			getSupportFragmentManager().putFragment(savedInstanceState, "chaseOrdersFragment", chaseOrdersFragment);
		}
	}
}
