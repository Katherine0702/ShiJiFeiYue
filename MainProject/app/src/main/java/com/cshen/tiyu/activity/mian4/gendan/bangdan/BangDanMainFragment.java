package com.cshen.tiyu.activity.mian4.gendan.bangdan;



import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.mian4.personcenter.orders.OrdersAllFragment;
import com.cshen.tiyu.activity.mian4.personcenter.orders.OrdersChaseFragment;
import com.cshen.tiyu.activity.mian4.personcenter.orders.OrdersWaitingFragment;
import com.cshen.tiyu.activity.mian4.personcenter.orders.OrdersWinFragment;
import com.cshen.tiyu.base.MyFragmentPagerAdapter;

public class BangDanMainFragment extends Fragment {

	public static final int HOT = 3;
	public static final int RENQI = 4;
	public static final int MINGZHONG = 1;
	public static final int YINGLI = 2;

	private ViewPager mPager;//对应的fragment
	private ArrayList<Fragment> fragments=new ArrayList<Fragment>();
	public HotFragment hotFragment;
	public RenQiFragment renqiFragment;
	public MingZhongFragment mingzhongFragment;
	public YingLiFragment yingliFragment;
	private TextView ordersHot,ordersRenQi,ordersMingZhong,ordersYingLi;
	Activity _this;
	private TextView ivBottomLine;
	private int currIndex = 0;
	private int position_one;
	private int position_two;
	private int position_three;
	Bundle savedInstanceState;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.bangdan_main,container, false);
		_this = this.getActivity();
		initView(view);
		return view;
	}
	private void initView(View view){
		InitWidth(view);
		ordersHot = (TextView) view.findViewById(R.id.tv_tab_hot);
		ordersRenQi = (TextView) view.findViewById(R.id.tv_tab_renqi);
		ordersMingZhong= (TextView) view.findViewById(R.id.tv_tab_mingzhong);
		ordersYingLi = (TextView) view.findViewById(R.id.tv_tab_yingli);

		ordersHot.setOnClickListener(new MyOnClickListener(0));
		ordersRenQi.setOnClickListener(new MyOnClickListener(1));
		ordersMingZhong.setOnClickListener(new MyOnClickListener(2));
		ordersYingLi.setOnClickListener(new MyOnClickListener(3));
		mPager = (ViewPager) view.findViewById(R.id.pager);
		setFragment();//设置订单数据
	}
	private void InitWidth(View view) {
		ivBottomLine = (TextView) view.findViewById(R.id.iv_bottom_line1);
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
			hotFragment = (HotFragment) getFragmentManager().getFragment(
					savedInstanceState, "hotFragment");
			if(hotFragment == null){
				hotFragment = new HotFragment();
			}
			
			renqiFragment = (RenQiFragment) getFragmentManager().getFragment(
					savedInstanceState, "renqiFragment");
			if(renqiFragment == null){
				renqiFragment = new RenQiFragment();
			}
			
			mingzhongFragment = (MingZhongFragment) getFragmentManager().getFragment(
					savedInstanceState, "mingzhongFragment");
			if(mingzhongFragment == null){
				mingzhongFragment = new MingZhongFragment();
			}

			yingliFragment = (YingLiFragment) getFragmentManager().getFragment(
					savedInstanceState, "yingliFragment");
			if(yingliFragment == null){
				yingliFragment = new YingLiFragment();
			}
			
		}else{
			mingzhongFragment = new MingZhongFragment();
			yingliFragment = new YingLiFragment();
			hotFragment = new HotFragment();
			renqiFragment = new RenQiFragment();
		}

		fragments.add(hotFragment);
		fragments.add(renqiFragment);
		fragments.add(mingzhongFragment);
		fragments.add(yingliFragment);

		mPager.setAdapter(new MyFragmentPagerAdapter(getFragmentManager(), fragments));
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
					ordersRenQi.setTextColor(_this.getResources().getColor(R.color.dingdanuncolor));
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(position_two, 0, 0, 0);
					ordersMingZhong.setTextColor(_this.getResources().getColor(R.color.dingdanuncolor));
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(position_three, 0, 0, 0);
					ordersYingLi.setTextColor(_this.getResources().getColor(R.color.dingdanuncolor));
				}     
				ordersHot.setTextColor(_this.getResources().getColor(R.color.mainred));
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(0, position_one, 0, 0);
					ordersHot.setTextColor(_this.getResources().getColor(R.color.dingdanuncolor));
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(position_two, position_one, 0, 0);
					ordersMingZhong.setTextColor(_this.getResources().getColor(R.color.dingdanuncolor));
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(position_three, position_one, 0, 0);
					ordersYingLi.setTextColor(_this.getResources().getColor(R.color.dingdanuncolor));
				} 
				ordersRenQi.setTextColor(_this.getResources().getColor(R.color.mainred));
				break;
			case 2:
				if (currIndex == 0) {
					animation = new TranslateAnimation(0, position_two, 0, 0);
					ordersHot.setTextColor(_this.getResources().getColor(R.color.dingdanuncolor));
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(position_one, position_two, 0, 0);
					ordersRenQi.setTextColor(_this.getResources().getColor(R.color.dingdanuncolor));
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(position_three, position_two, 0, 0);
					ordersYingLi.setTextColor(_this.getResources().getColor(R.color.dingdanuncolor));
				} 
				ordersMingZhong.setTextColor(_this.getResources().getColor(R.color.mainred));
				break;
			case 3:
				if (currIndex == 0) {
					animation = new TranslateAnimation(0, position_three, 0, 0);
					ordersHot.setTextColor(_this.getResources().getColor(R.color.dingdanuncolor));
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(position_one, position_three, 0, 0);
					ordersRenQi.setTextColor(_this.getResources().getColor(R.color.dingdanuncolor));
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(position_two, position_three, 0, 0);
					ordersMingZhong.setTextColor(_this.getResources().getColor(R.color.dingdanuncolor));
				} 
				ordersYingLi.setTextColor(_this.getResources().getColor(R.color.mainred));
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
			if (hotFragment != null
				&&hotFragment.isVisible()) {
			getFragmentManager().putFragment(savedInstanceState, "hotFragment", hotFragment);
		}
		if (renqiFragment != null
				&&renqiFragment.isVisible()) {
			getFragmentManager().putFragment(savedInstanceState, "renqiFragment", renqiFragment);
		}if (mingzhongFragment != null
				&&mingzhongFragment.isVisible()) {
			getFragmentManager().putFragment(savedInstanceState, "mingzhongFragment", mingzhongFragment);
		}
		if (yingliFragment != null
				&&yingliFragment.isVisible()) {
			getFragmentManager().putFragment(savedInstanceState, "yingliFragment", yingliFragment);
		}
	}
}
