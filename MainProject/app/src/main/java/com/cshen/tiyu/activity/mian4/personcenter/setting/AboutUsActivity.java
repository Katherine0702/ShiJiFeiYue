package com.cshen.tiyu.activity.mian4.personcenter.setting;



import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.base.BaseActivity;

public class AboutUsActivity extends BaseActivity implements OnClickListener 
{
	private TextView title,tv_version;
	private RelativeLayout rl_website,rl_servicePhoneNum,rl_version;
	private View padnetwork;
	private Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutus);
		mContext=this;
		title= (TextView) findViewById(R.id.tv_head_title);
		title.setText("关于我们");
		findViewById(R.id.iv_back).setVisibility(View.VISIBLE);
		findViewById(R.id.iv_back).setOnClickListener(this);
		
//		rl_website=(RelativeLayout) findViewById(R.id.rl_address);
		rl_servicePhoneNum=(RelativeLayout) findViewById(R.id.rl_service);
		rl_version=(RelativeLayout) findViewById(R.id.rl_version);
		
		tv_version=(TextView) findViewById(R.id.tv_version);
		
		padnetwork = findViewById(R.id.padnetwork);
		padnetwork.setOnClickListener(this);
		
//		rl_website.setOnClickListener(this);
		rl_servicePhoneNum.setOnClickListener(this);
		rl_version.setOnClickListener(this);
		
		String currentVersionName=getCurrentVersionName();
		tv_version.setText(currentVersionName);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.iv_back:
			this.finish();
			break;
//		case R.id.rl_address://网址
//			Intent intentnetwork = new Intent();        
//			intentnetwork.setAction("android.intent.action.VIEW");    
//			Uri content_urlnetwork = Uri.parse(this.getString(R.string.network));   
//			intentnetwork.setData(content_urlnetwork);  
//			startActivity(intentnetwork);
//			break;
		case R.id.padnetwork:
			Intent padnetwork = new Intent();        
			padnetwork.setAction("android.intent.action.VIEW");    
			Uri content_urlpadnetwork = Uri.parse(this.getString(R.string.padnetwork));   
			padnetwork.setData(content_urlpadnetwork);  
			startActivity(padnetwork);
			break;
			
		case R.id.rl_service://客服电话
			toCall("4009282770");
			break;
		case R.id.rl_version://版本号
			
			break;
		default:
			break;
		}
	}
	
	public void toCall(String phoneno) {

		if (phoneno == null || "".equals(phoneno.trim())) {
			phoneno = "4009282770";
		}
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
				+ phoneno));
		startActivity(intent);

	}
	
	public String   getCurrentVersionName(){
		
		PackageInfo info;
		try {
			PackageManager manager = mContext.getPackageManager();
			info = manager.getPackageInfo(mContext.getPackageName(), 0);
			String  currentVersionName = info.versionName;
			return currentVersionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "1.0";
		}
		
	}

}
