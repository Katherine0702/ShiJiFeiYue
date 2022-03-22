package com.cshen.tiyu.activity.mian4.personcenter.account;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cshen.tiyu.R;
import com.cshen.tiyu.animation.ZoomOutPageTransformer;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.widget.PagerSlidingTabStrip;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;

public class AccountDetailActivity extends BaseActivity {
	PagerSlidingTabStrip tabs;
	ViewPager pager;
	DisplayMetrics dm;
	TotalFragment afrag;
	TouZhuFragment bfrag;
	RechargeFragment cfrag;

	TiKuanFragment dfrag;
	AwardFragment efrag;
	RechargeFragment ffrag;
	ArrayList<Fragment> fragments=new ArrayList<Fragment>();

	TopViewLeft tv_head=null;

	String[] titles = { "全部", "投注", "充值","提款","派奖" };	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_detail);
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





		afrag=new TotalFragment();
		bfrag=new TouZhuFragment();
		cfrag=new RechargeFragment();
		dfrag=new TiKuanFragment();
		efrag=new AwardFragment();
		fragments.add(afrag);
		fragments.add(bfrag);
		fragments.add(cfrag);		
		fragments.add(dfrag);
		fragments.add(efrag);
		initView();	

	}

	private void initView() {
		// TODO 自动生成的方法存根

		dm = getResources().getDisplayMetrics();
		pager = (ViewPager) findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager.setAdapter(new MyAdapter(getSupportFragmentManager(), titles));
		pager.setOffscreenPageLimit(0);
		pager.setPageTransformer(true, new ZoomOutPageTransformer());
		tabs.setViewPager(pager);




	}


	public class MyAdapter extends FragmentPagerAdapter {
		String[] _titles;

		public MyAdapter(FragmentManager fm, String[] titles) {
			super(fm);
			_titles = titles;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return _titles[position];
		}

		@Override
		public int getCount() {
			return _titles.length;
		}

		@Override
		public Fragment getItem(int position) {
			//			switch (position) {
			//			case 0:
			//				if (afrag == null) {
			//					afrag = new AFrag();
			//				}
			//				return afrag;
			//			case 1:
			//				if (bfrag == null) {
			//					bfrag = new BFrag();
			//				}
			//				return bfrag;
			//			case 2:
			//				if (cfrag == null) {
			//					cfrag = new CFrag();
			//				}
			//				return cfrag;
			//			default:
			//				return null;
			//			}

			if(fragments.get(position)!=null){				
				return	fragments.get(position);
			}else{				
				return null;

			}

		}
	}





	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.account_detail, menu);
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
