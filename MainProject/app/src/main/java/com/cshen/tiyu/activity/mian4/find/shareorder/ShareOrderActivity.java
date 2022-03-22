package com.cshen.tiyu.activity.mian4.find.shareorder;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.login.LoginActivity;
import com.cshen.tiyu.activity.mian4.personcenter.orders.OrdersActivity;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.base.MyFragmentPagerAdapter;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.net.https.ServiceShareOrder;
import com.cshen.tiyu.net.https.xUtilsImageUtils;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;
import de.greenrobot.event.EventBus;

public class ShareOrderActivity extends BaseActivity {

	private ShareOrderActivity _this;
	private TopViewLeft tv_head;// 头
	private View nodata;
	private ImageView iv_bg;
	private TextView ivBottomLine;
	private TextView tv_hotest, tv_newest;
	private ViewPager mPager;// 对应的fragment
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	private HotestFragment hotestFragment;// 最热
	private NewestFragment newestFragment;// 最新
	private LinearLayout ll_add, ll_my;
	private int currIndex = 0;
	private int position_one;
	public static final int LOGIN_ADD = 3;// 登录
	public static final int LOGIN_MY = 4;// 登录
	public static final int LOGIN_PRISE = 5;// 登录

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_share_order);
		_this = this;
		initView();
		initTopImage();
	}

	private void initTopImage() {
		ServiceShareOrder.getInstance().PostGetTopImage(_this,
				new CallBack<String>() {

			@Override
			public void onSuccess(String t) {

				xUtilsImageUtils.displayWithLoading(iv_bg,
						R.mipmap.iv_sd_top, R.mipmap.iv_sd_top, t);

			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {

			}
		});
	}

	private void initView() {
		InitWidth();
		tv_head = (TopViewLeft) findViewById(R.id.title);
		tv_head.setResourceVisiable(true, false, false);
		tv_head.setTopClickItemListener(new TopClickItemListener() {
			@Override
			public void clickLoginView(View view) {
			}

			@Override
			public void clickContactView(View view) {
			}

			@Override
			public void clickBackImage(View view) {
				finish();
			}
		});

		tv_hotest = (TextView) findViewById(R.id.tv_hotest);
		tv_newest = (TextView) findViewById(R.id.tv_newest);
		iv_bg = (ImageView) findViewById(R.id.iv_bg);
		ll_add = (LinearLayout) findViewById(R.id.ll_add);
		ll_my = (LinearLayout) findViewById(R.id.ll_my);

		tv_hotest.setOnClickListener(new MyOnClickListener(0));
		tv_newest.setOnClickListener(new MyOnClickListener(1));
		mPager = (ViewPager) findViewById(R.id.pager);
		setViews();// 设置订单数据

		ll_add.setOnClickListener(new OnClickListener() {// 添加

			@Override
			public void onClick(View v) {
				boolean hasLongin = PreferenceUtil
						.getBoolean(_this, "hasLogin");
				if (hasLongin) {
					Intent intent = new Intent(_this,OrdersActivity.class);
					intent.putExtra("arg0", 1);
					startActivity(intent);
				} else {
					Intent intent = new Intent(_this, LoginActivity.class);
					intent.putExtra("requestName", "intentLogin");
					startActivityForResult(intent, LOGIN_ADD);

				}

			}
		});
		ll_my.setOnClickListener(new OnClickListener() {// 我的晒单圈

			@Override
			public void onClick(View v) {
				boolean hasLongin = PreferenceUtil
						.getBoolean(_this, "hasLogin");
				if (hasLongin) {
					Intent intent = new Intent(_this,
							MyShareOrderActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(_this, LoginActivity.class);
					intent.putExtra("requestName", "intentLogin");
					startActivityForResult(intent, LOGIN_MY);
				}

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
		super.onActivityResult(requestCode, resultCode, arg2);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case LOGIN_ADD:
				EventBus.getDefault().post("updataHotest");
				EventBus.getDefault().post("updataNewest");
				Intent intent = new Intent(_this,OrdersActivity.class);
				intent.putExtra("arg0", 1);
				startActivity(intent);
				break;
			case LOGIN_MY:
				EventBus.getDefault().post("updataHotest");
				EventBus.getDefault().post("updataNewest");
				Intent intent_my = new Intent(_this,MyShareOrderActivity.class);
				startActivity(intent_my);
				break;
			case LOGIN_PRISE:
				EventBus.getDefault().post("updataHotest");
				EventBus.getDefault().post("updataNewest");
				break;
			default:
				break;
			}

		}
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
		int screenW = dm.widthPixels;// 屏幕宽度
		position_one = (int) (screenW / 2.0);
	}

	private void setViews() {

		hotestFragment = new HotestFragment();
		newestFragment = new NewestFragment();

		fragments.add(hotestFragment);
		fragments.add(newestFragment);

		mPager.setAdapter(new MyFragmentPagerAdapter(
				getSupportFragmentManager(), fragments));
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
					tv_newest.setTextColor(_this.getResources().getColor(
							R.color.dingdanuncolor));
				}
				tv_hotest.setTextColor(_this.getResources().getColor(
						R.color.mainred));
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(0, position_one, 0, 0);
					tv_hotest.setTextColor(_this.getResources().getColor(
							R.color.dingdanuncolor));
				}
				tv_newest.setTextColor(_this.getResources().getColor(
						R.color.mainred));

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

	public void setBase() {
		TranslateAnimation animation = null;
		if (currIndex == 1) {
			animation = new TranslateAnimation(position_one, 0, 0, 0);
			tv_newest.setTextColor(_this.getResources().getColor(
					R.color.dingdanuncolor));
		}
		tv_hotest.setTextColor(_this.getResources().getColor(
				R.color.mainred));
		if (animation != null) {
			animation.setFillAfter(true);
			animation.setDuration(300);
			ivBottomLine.startAnimation(animation);
		}
		currIndex = 0;
	}

}
