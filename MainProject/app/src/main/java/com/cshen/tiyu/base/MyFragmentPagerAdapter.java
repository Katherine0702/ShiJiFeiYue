package com.cshen.tiyu.base;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

/**
 * vatty * 
 *
 * hongshengpeng.com
 * 
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
	//FragmentManager fm;
	/*@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		Fragment fragment = (Fragment)super.instantiateItem(container,position);
		FragmentTransaction ft =fm.beginTransaction();
		ft.add(container.getId(), fragment, "Tab"+position);
		ft.attach(fragment);
		ft.commit();
		return super.instantiateItem(container, position);
	}*/

	private ArrayList<Fragment> fragmentsList;

	public MyFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
		//this.fm = fm;
	}


	public MyFragmentPagerAdapter(FragmentManager fragmentManager, ArrayList<Fragment> fragments) {
		super(fragmentManager);
		this.fragmentsList = fragments;
		//this.fm = fragmentManager;
	}



	@Override
	public int getCount() {
		return fragmentsList.size();
	}

	@Override
	public Fragment getItem(int arg0) {
		return fragmentsList.get(arg0);
	}

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

}
