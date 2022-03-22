package com.cshen.tiyu.base;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.cshen.tiyu.R;
import com.cshen.tiyu.utils.StatusBarUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

public class BaseActivity extends FragmentActivity {
	// 广播接受 判断你是否连接网络
	private com.cshen.tiyu.base.NetReceivers mReceiver = new NetReceivers();
	private IntentFilter mFilter = new IntentFilter();

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		((CaiPiaoApplication) getApplication()).addActivity(this);
		PushAgent.getInstance(this).onAppStart();
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mReceiver, mFilter);
	}

	/**
	 * 设置沉浸式状态栏
	 */
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);	
		 if (Build.VERSION.SDK_INT !=  Build.VERSION_CODES.LOLLIPOP_MR1 && Build.VERSION.SDK_INT !=  Build.VERSION_CODES.LOLLIPOP) {
			   setStatusBar();
	 		}
		
		

	}

	public void setStatusBar(){		
			StatusBarUtil.setColor(this, getResources().getColor(R.color.mainred));//状态栏
	}
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		((CaiPiaoApplication) getApplication()).removeActivity(this);
	}

	/**
	 * 开启新的activity并且关闭自己
	 * 
	 * @param cls
	 *            新的activity的字节码
	 */
	public void startActivityAndFinishSelf(Class<?> cls) {
		Intent intent = new Intent(this, cls);
		startActivity(intent);
		finish();
	}

	/**
	 * 开启新的activity不关闭自己
	 * 
	 * @param cls
	 *            新的activity的字节码
	 */
	public void startActivity(Class<?> cls) {
		Intent intent = new Intent(this, cls);
		startActivity(intent);
	}


}
